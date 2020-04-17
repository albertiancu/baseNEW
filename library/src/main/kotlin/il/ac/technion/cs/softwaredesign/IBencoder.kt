package il.ac.technion.cs.softwaredesign
interface IBencoder {

    fun getInfoHash(torrent: ByteArray): String
    fun checkValidMetaInfo(torrent: ByteArray): Boolean
    /**
     * Not sure if this should take a String or a ByteArray
     */
    fun decodeAnnounceList(bencodedAnnounceList: String): List<List<String>>
    fun getBencodedAnnounceList(torrent: ByteArray): String

}
