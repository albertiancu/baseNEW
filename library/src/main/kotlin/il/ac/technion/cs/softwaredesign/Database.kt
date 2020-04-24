package il.ac.technion.cs.softwaredesign


class Database : IDatabase {
    override fun contains(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun write(key: String, value: String) {
        il.ac.technion.cs.softwaredesign.storage.write(key.toByteArray(),value.toByteArray())
    }

    override fun read(key: String) :String{
        TODO("Not yet implemented")
    }

    override fun delete(key: String) {
        TODO("Not yet implemented")
    }

}