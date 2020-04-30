package il.ac.technion.cs.softwaredesign
interface IBencoder {

    fun getInfoHash(torrent: ByteArray): String
    fun decodeAnnounceList(bencodedAnnounceList: String): List<List<String>>
    fun getBencodedAnnounceList(torrent: ByteArray): String

}
