package il.ac.technion.cs.softwaredesign

interface IDatabase {
     fun write(key: String, value: String)
     fun read(key: String) : String?
     fun delete(key: String)
}
