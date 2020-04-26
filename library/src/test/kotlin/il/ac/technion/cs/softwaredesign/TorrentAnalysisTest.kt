package il.ac.technion.cs.softwaredesign
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException


class TorrentAnalysisTest {
    private val bencoder = Bencoder()
    private val debian =  this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val superhuman = this::class.java.getResource("/superhuman by habit epub.torrent").readBytes()
    private val debian_error = this::class.java.getResource("/debian-withError.iso.torrent").readBytes()

    @Test
    fun `getInfoHash calculated correctly for debian`() {
        val infohash = bencoder.getInfoHash(debian)
        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
    }

    @Test
    fun `getInfoHash calculated correctly for superhuman`() {
        val infohash = bencoder.getInfoHash(superhuman)
        assertThat(infohash, equalTo("627566b90d81958a75847622477f1a699f1b2907"))
    }


    @Test
    fun `getInfohash for file with error`() {
        assertThrows<IllegalArgumentException> { bencoder.getInfoHash(debian_error) }
    }

    @Test
    fun `getAnnounce for debian` () {
        val announce = bencoder.getAnnounce(debian)
        assertThat(announce, equalTo("http://bttracker.debian.org:6969/announce"))
    }

}



