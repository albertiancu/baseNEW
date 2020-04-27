import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.CourseTorrent
import il.ac.technion.cs.softwaredesign.IBencoder
import il.ac.technion.cs.softwaredesign.IDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class CourseTorrentTest {
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val mockDatabase = mockk<IDatabase>(relaxed = true)
    private val mockBencoder = mockk<IBencoder>(relaxed = true)
    private val courseTorrent = CourseTorrent(mockDatabase, mockBencoder)

    @Test
    fun `loading an already loaded torrent throws exception`(){
        every {mockDatabase.read(any())} returns "value"
        assertThrows<IllegalStateException>{courseTorrent.load(debian)}
    }

    @Test fun `loading an invalid torrent throws exception`(){
        every {mockBencoder.getInfoHash(any())} throws IllegalArgumentException()
        assertThrows<IllegalArgumentException>{courseTorrent.load(debian)}
    }

    @Test
    fun `after loading torrent its bencoded announce-list is available under its infohash key`(){
        every {mockBencoder.getInfoHash(debian)} returns "infohash"
        every {mockDatabase.read("infohash")} returns null
        every {mockBencoder.getBencodedAnnounceList(debian)} returns "announce-list"
        courseTorrent.load(debian)
        verify(exactly = 1) {mockDatabase.write(key = "infohash", value = "announce-list")}
    }

    @Test
    fun `unloading a torrent that wasn't loaded throws exception`(){
        every { mockDatabase.read("key")} returns null
        assertThrows<IllegalArgumentException>{courseTorrent.unload("key")}
    }

    @Test
    fun `unloading a torrent deletes it from the database`(){
        every { mockDatabase.read("key")} returns "value"
        courseTorrent.unload("key")
        verify(exactly = 1) {mockDatabase.delete(key = "key")}
    }


    @Test
    fun `announcing an unloaded torrent throws exception`(){
        every {mockDatabase.read("key")} returns null
        assertThrows<IllegalArgumentException>{courseTorrent.announces("key")}
    }

    @Test
    fun `announcing a loaded torrent returns the decoding of the stored bencoded announce-list`(){
        val expectedAnnounceList = ArrayList<ArrayList<String>>()
        every {mockDatabase.read("infohash")} returns "bencodedAnnonuceList"
        every {mockBencoder.decodeAnnounceList("bencodedAnnounceList")} returns expectedAnnounceList
        assertEquals(expectedAnnounceList, courseTorrent.announces("infohash"))
    }

}
