package il.ac.technion.cs.softwaredesign

import java.lang.IllegalStateException


class Database : IDatabase {
    val charset = Charsets.UTF_8

    override fun write(key: String, value: String) {
        il.ac.technion.cs.softwaredesign.storage.write(key.toByteArray(),value.toByteArray())
    }

    override fun read(key: String) : String?{
        val value = il.ac.technion.cs.softwaredesign.storage.read(key.toByteArray());
        if(value?.size === 0)
            return null
        return value?.toString(charset);
    }

    override fun delete(key: String) {
        write(key, "")
    }

}
