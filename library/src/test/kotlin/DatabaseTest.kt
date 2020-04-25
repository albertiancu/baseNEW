import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import il.ac.technion.cs.softwaredesign.Database
import il.ac.technion.cs.softwaredesign.storage.write
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.jupiter.api.Test

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
}