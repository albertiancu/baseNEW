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


class BenTest {
    var bencoder = Bencoder()

    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val debian_error = this::class.java.getResource("/debian-withError.iso.torrent").readBytes()
    private val superhuman =  this::class.java.getResource("/superhuman by habit epub.torrent").readBytes()

    @Test
    fun `getInfoHash calculated correctly for debian`() {
        val infohash = bencoder.getInfoHash(debian)
        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
    }



}


