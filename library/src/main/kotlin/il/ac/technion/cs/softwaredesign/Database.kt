package il.ac.technion.cs.softwaredesign

import java.lang.IllegalStateException


class Database : IDatabase {
    val charset = Charsets.UTF_8

    override fun contains(key: String): Boolean {
        return il.ac.technion.cs.softwaredesign.storage.read(key.toByteArray()) != null
    }

    override fun write(key: String, value: String) {
        il.ac.technion.cs.softwaredesign.storage.write(key.toByteArray(),value.toByteArray())
    }

    override fun read(key: String) : String?{
//        if (!contains(key)) {
//            return null
//        }

        val ans = il.ac.technion.cs.softwaredesign.storage.read(key.toByteArray());
        return ans?.toString(charset);
    }

    override fun delete(key: String) {
        TODO("Not yet implemented")
    }

}
