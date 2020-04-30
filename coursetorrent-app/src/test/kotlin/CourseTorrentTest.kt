import il.ac.technion.cs.softwaredesign.*
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class CourseTorrentTest {
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val fakeDatabase = FakeDatabase()
    private val mockBencoder = mockk<IBencoder>(relaxed = true)
    private val courseTorrent = CourseTorrent(fakeDatabase, mockBencoder)

    //Load Tests

    @Test
    fun `loading an already loaded torrent throws exception`(){
        every {mockBencoder.getInfoHash(debian)} returns "infohash"
        every {mockBencoder.getBencodedAnnounceList(debian)} returns "bencodedAnnounceList"

        courseTorrent.load(debian)

        assertThrows<IllegalStateException>{courseTorrent.load(debian)}
    }

    @Test fun `loading an invalid torrent throws exception`(){
        every {mockBencoder.getInfoHash(debian)} throws IllegalArgumentException()

        assertThrows<IllegalArgumentException>{courseTorrent.load(debian)}
    }


    @Test
    fun `after loading torrent its bencoded announce-list is available under its infohash key`(){
        every {mockBencoder.getInfoHash(debian)} returns "infohash"
        every {mockBencoder.getBencodedAnnounceList(debian)} returns "bencodedAnnounceList"

        courseTorrent.load(debian)

        assertEquals("bencodedAnnounceList", fakeDatabase.read("infohash"))
    }


    //Unload Tests

    @Test
    fun `unloading a torrent that wasn't loaded throws exception`(){
        assertThrows<IllegalArgumentException>{courseTorrent.unload("infohash")}
    }

    @Test
    fun `unloading a torrent deletes it from the database`(){
        every {mockBencoder.getInfoHash(debian)} returns "infohash"
        every {mockBencoder.getBencodedAnnounceList(debian)} returns "bencodedAnnounceList"

        courseTorrent.load(debian)
        courseTorrent.unload("infohash")

        assertTrue(fakeDatabase.read("infohash") == null)
    }

    //Announces Tests

    @Test
    fun `announcing an unloaded torrent throws exception`(){
        assertThrows<IllegalArgumentException>{courseTorrent.announces("infohash")}
    }

    @Test
    fun `announcing a loaded torrent returns the decoding of the stored bencoded announce-list`(){
        every {mockBencoder.getInfoHash(debian)} returns "infohash"
        every {mockBencoder.getBencodedAnnounceList(debian)} returns "bencodedAnnounceList"
        every {mockBencoder.decodeAnnounceList("bencodedAnnounuceList")} returns ArrayList<ArrayList<String>>()

        courseTorrent.load(debian)

        assertEquals(ArrayList<ArrayList<String>>(), courseTorrent.announces("infohash"))
    }


}