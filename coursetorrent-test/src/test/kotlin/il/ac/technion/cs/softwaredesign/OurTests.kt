package il.ac.technion.cs.softwaredesign

import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class OurTests {
    private val torrent = CourseTorrent()
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val debian_error = this::class.java.getResource("/debian-withError.iso.torrent").readBytes()

    val mockDB = HashMap<String,String>()
    val keySlotRead = slot<ByteArray>()
    val keySlot = slot<ByteArray>()
    val valueSlot = slot<ByteArray>()
    val database = Database()

    @Test
    fun `after load, infohash calculated correctly`() {
        val infohash = torrent.load(debian)

        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
    }

    @Test
    fun `after load of incorrect file, exception is thrown`() {
        assertThrows<IllegalArgumentException> { torrent.load(debian_error) }
    }

    @Test
    fun `after load announce-list is stored`() {
        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                else (mockDB[String(keySlot.captured)])?.toByteArray() }

        every { write(capture(keySlot), capture(valueSlot)) } answers
                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}

        val infohash = torrent.load(debian)
        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
        assertThat(mockDB[infohash], equalTo("ll41:http://bttracker.debian.org:6969/announceee"))

    }


    @Test
    fun `at load found that key already exists`() {

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                else (mockDB[String(keySlot.captured)])?.toByteArray() }

        every { write(capture(keySlot), capture(valueSlot)) } answers
                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}

        mockDB["5a8062c076fa85e8056451c0d9aa04349ae27909"] = "some answer"

        assertThrows<IllegalStateException> { val infohash = torrent.load(debian) }

    }


    @Test
    fun `after load, announce is correct`() {

        mockDB["5a8062c076fa85e8056451c0d9aa04349ae27909"] = "ll41:http://bttracker.debian.org:6969/announceee"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                else (mockDB[String(keySlot.captured)])?.toByteArray() }

        val infohash = "5a8062c076fa85e8056451c0d9aa04349ae27909"

        val announces = torrent.announces(infohash)

        assertThat(announces, allElements(hasSize(equalTo(1))))
        assertThat(announces, hasSize(equalTo(1)))
        assertThat(announces, allElements(hasElement("http://bttracker.debian.org:6969/announce")))
    }

    @Test
    fun `announce - infohash does not exists test`() {

        mockDB["5a8062c076fa85e8056451c0d9aa04349ae27909"] = "ll41:http://bttracker.debian.org:6969/announceee"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                else (mockDB[String(keySlot.captured)])?.toByteArray() }

        val infohash = "aaa"

        assertThrows<IllegalArgumentException> { val announces = torrent.announces(infohash) }
    }


    @Test
    fun `load and announce test`() {

        //mockDB["5a8062c076fa85e8056451c0d9aa04349ae27909"] = "ll41:http://bttracker.debian.org:6969/announceee"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                else (mockDB[String(keySlot.captured)])?.toByteArray() }
        every { write(capture(keySlot), capture(valueSlot)) } answers
                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}

        val infohash = torrent.load(debian)

        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
        assertThat(mockDB[infohash], equalTo("ll41:http://bttracker.debian.org:6969/announceee"))

        val announces = torrent.announces(infohash)

        assertThat(announces, allElements(hasSize(equalTo(1))))
        assertThat(announces, hasSize(equalTo(1)))
        assertThat(announces, allElements(hasElement("http://bttracker.debian.org:6969/announce")))
    }

//
//    @Test
//    fun timeTest(){
//        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
//        every { read(capture(keySlot)) } answers
//                { if (!mockDB.containsKey(String(keySlot.captured))) null
//                else (mockDB[String(keySlot.captured)])?.toByteArray() }
//        every { write(capture(keySlot), capture(valueSlot)) } answers
//                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}
//
//        for(i in 1..100000){
//            var infohash = torrent.load(debian)
//            mockDB[infohash + i.toString()] = mockDB[infohash] ?: ""
//            mockDB[infohash] = ""
//        }
//
//
//
//    }

}
