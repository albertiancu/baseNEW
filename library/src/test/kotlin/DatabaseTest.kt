import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.Database
import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class DatabaseTest {

    private val mockDB = HashMap<String,String>()
    private val keySlot = slot<ByteArray>()
    private val valueSlot = slot<ByteArray>()
    private val database = Database()

    @Test
    fun writeTest(){

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { write(capture(keySlot), capture(valueSlot)) } answers
                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}


        database.write("Key", "Value")

        assertThat(mockDB["Key"], equalTo( "Value"))
    }

    @Test
    fun readTest(){

        mockDB["bbb"] = "answer"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                    else (mockDB[String(keySlot.captured)])?.toByteArray() }

        assertThat(database.read("aaa"), com.natpryce.hamkrest.isNullOrBlank)
        assertThat(database.read("bbb"), equalTo( "answer"))
    }

    @Test
    fun `test that read unexisting field return null`(){

        mockDB["bbb"] = "answer"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                else (mockDB[String(keySlot.captured)])?.toByteArray() }

        assertThat(database.read("aaa"), com.natpryce.hamkrest.isNullOrBlank)

    }

    @Test
    fun deleteTest() {
        mockDB["bbb"] = "answer"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { write(capture(keySlot), capture(valueSlot)) } answers
                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}

        database.delete("bbb")
        assertThat(mockDB["bbb"], equalTo( ""))
    }

    @Test
    fun `test the read return null after deleting the key`() {
        mockDB["bbb"] = "answer"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { write(capture(keySlot), capture(valueSlot)) } answers
                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}

        database.delete("bbb")
        assertThat(database.read("bbb"), com.natpryce.hamkrest.isNullOrBlank)
    }

}
