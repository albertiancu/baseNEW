package il.ac.technion.cs.softwaredesign

class FakeDatabase : IDatabase {
    val hashmap = HashMap<String,String>()
    override fun write(key: String, value: String) {
        hashmap[key] = value
    }

    override fun read(key: String): String? {
        if(!hashmap.containsKey(key))
            return null
        return hashmap[key]
    }

    override fun delete(key: String) {
        hashmap.remove(key)
    }
}