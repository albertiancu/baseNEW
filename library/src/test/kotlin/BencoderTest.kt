import java.nio.charset.Charset
import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import il.ac.technion.cs.softwaredesign.Bencoder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException


class BencoderTest {
    var bencoder = Bencoder()

    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val debian_error = this::class.java.getResource("/debian-withError.iso.torrent").readBytes()
    private val superhuman =  this::class.java.getResource("/superhuman by habit epub.torrent").readBytes()

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
    fun `getBencodedAnnounceList returns correct value for superhuman`(){
        //var bencoder = Bencoder()

        var s = bencoder.getBencodedAnnounceList(superhuman)

        assertThat(s, equalTo("ll32:udp://tracker.ccc.de:80/announceel35:udp://tracker.istole.it:80/announceel38:udp://tracker.publicbt.com:80/announceel44:udp://tracker.openbittorrent.com:80/announceel35:udp://tracker.istole.it:80/announceel33:http://tracker.istole.it/announceel38:udp://tracker.publicbt.com:80/announceel36:http://tracker.publicbt.com/announceel26:udp://pow7.com:80/announceel31:udp://9.rarbg.com:2710/announceel36:udp://open.demonii.com:1337/announceee"))
    }


    @Test
    fun `getBencodedAnnounceList returns correct value for debian`(){
        //var bencoder = Bencoder()

        var s = bencoder.getBencodedAnnounceList(debian)

        assertThat(s, equalTo("ll41:http://bttracker.debian.org:6969/announceee"))
    }

    @Test
    fun `decodeAnnounceList returns correct value for debian`(){
        //var bencoder = Bencoder()

        var s = bencoder.getBencodedAnnounceList(debian)
        var b = bencoder.decodeAnnounceList(s)

        assertThat(b, allElements(hasSize(equalTo(1))))
        assertThat(b, hasSize(equalTo(1)))
        assertThat(b, allElements(hasElement("http://bttracker.debian.org:6969/announce")))
    }

}
