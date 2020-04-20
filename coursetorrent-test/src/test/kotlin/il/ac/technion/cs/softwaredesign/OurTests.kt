package il.ac.technion.cs.softwaredesign

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class OurTests {
    private val torrent = CourseTorrent()
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val debian_error = this::class.java.getResource("/debian-withError.iso.torrent").readBytes()

    @Test
    fun `after load, infohash calculated correctly`() {
        val infohash = torrent.load(debian)

        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
    }

    @Test
    fun `after load of incorrect file, exception is thrown`() {
        assertThrows<IllegalArgumentException> { torrent.load(debian_error) }
    }
//
//    @Test
//    fun `after load, announce is correct`() {
//        val infohash = torrent.load(debian)
//
//        val announces = torrent.announces(infohash)
//
//        assertThat(announces, allElements(hasSize(equalTo(1))))
//        assertThat(announces, hasSize(equalTo(1)))
//        assertThat(announces, allElements(hasElement("http://bttracker.debian.org:6969/announce")))
//    }
}
