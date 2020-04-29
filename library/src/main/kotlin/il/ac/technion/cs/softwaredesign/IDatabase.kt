package il.ac.technion.cs.softwaredesign

interface IDatabase {

     fun write(key: String, value: String)
     /**
      *@return The value associated with the given key. If key was never written to,
      * or the key was written to but then deleted from, then returns null.
      */
     fun read(key: String) : String?
     fun delete(key: String)
}
