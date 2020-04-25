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

    @Test
    fun writeTest(){
        val mockDB = HashMap<String,String>()
        val keySlot = slot<ByteArray>()
        val valueSlot = slot<ByteArray>()
        val database = Database()

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { write(capture(keySlot), capture(valueSlot)) } answers
                {mockDB.put(String(keySlot.captured), String(valueSlot.captured))}


        database.write("Key", "Value")

        assertThat(mockDB["Key"], equalTo( "Value"))
    }

    @Test
    fun containsTest(){
        val mockDB = HashMap<String,String>()
        val keySlot = slot<ByteArray>()
        //val valueSlot = slot<ByteArray>()
        val database = Database()


        mockDB["bbb"] = "answer"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                else (mockDB[String(keySlot.captured)])?.toByteArray() }

        assertThat(database.contains("aaa"), equalTo( false))
        assertThat(database.contains("bbb"), equalTo( true))
    }

    @Test
    fun readTest(){
        val mockDB = HashMap<String,String>()
        val keySlot = slot<ByteArray>()
        //val valueSlot = slot<ByteArray>()
        val database = Database()

        mockDB["bbb"] = "answer"

        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { read(capture(keySlot)) } answers
                { if (!mockDB.containsKey(String(keySlot.captured))) null
                    else (mockDB[String(keySlot.captured)])?.toByteArray() }

        assertThat(database.read("aaa"), com.natpryce.hamkrest.isNullOrBlank)
        assertThat(database.read("bbb"), equalTo( "answer"))
    }
}
