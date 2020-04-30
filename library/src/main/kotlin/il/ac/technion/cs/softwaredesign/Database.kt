package il.ac.technion.cs.softwaredesign

import java.lang.IllegalStateException


class Database : IDatabase {
    val charset = Charsets.UTF_8
    var cache = HashMap<String, String>()

    override fun write(key: String, value: String) {
        cache[key] = value
        il.ac.technion.cs.softwaredesign.storage.write(key.toByteArray(),value.toByteArray())
    }

    override fun read(key: String) : String?{
        if(cache.containsKey(key)) {
            if(cache[key] == "") return null
            return cache[key]
        }
        val value = il.ac.technion.cs.softwaredesign.storage.read(key.toByteArray())
        if(value === null) return null
        if(value.size === 0)
            return null
        return value.toString(charset);
    }

    override fun delete(key: String) {
        write(key, "")
    }

}
