package il.ac.technion.cs.softwaredesign

import be.adaxisoft.bencode.BDecoder
import be.adaxisoft.bencode.BEncoder
import java.nio.charset.Charset
import java.security.MessageDigest


class Bencoder : IBencoder {


    override fun getInfoHash(torrent: ByteArray): String {
        var b = BDecoder(torrent.inputStream())
        var document = b.decodeMap();
        val info = document.map["info"]!!.map
        val bb = BEncoder.encode(info)
        val bytes = ByteArray(bb.remaining())
        bb.get(bytes)

        val digest = MessageDigest.getInstance("SHA-1")
        digest.reset()
        val infoHASHH = digest.digest(bytes)
        val infohash = infoHASHH.toHex()
        return infohash
    }

    override fun checkValidMetaInfo(torrent: ByteArray): Boolean {
        TODO("Not yet implemented")
    }

    override fun decodeAnnounceList(encodedAnnounceList: String): List<List<String>> {
        TODO("Not yet implemented")
    }

    override fun getBencodedAnnounceList(torrent: ByteArray): String {
        TODO("Not yet implemented")
    }

    fun ByteArray.toHex(): String {
        return joinToString("") { "%02x".format(it) }
    }
}