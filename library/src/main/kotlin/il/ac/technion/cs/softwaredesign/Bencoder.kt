package il.ac.technion.cs.softwaredesign

import be.adaxisoft.bencode.BDecoder
import be.adaxisoft.bencode.BEncodedValue
import be.adaxisoft.bencode.BEncoder
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.security.MessageDigest


class Bencoder : IBencoder {

    private fun hashWithSHA1(byteArray: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-1")
        digest.reset()
        val infohash = digest.digest(byteArray)
        return infohash.toHex()
    }

    override fun getInfoHash(torrent: ByteArray): String {
        val decoder = BDecoder(torrent.inputStream())
        val outerDictionary = decoder.decodeMap().map
        val infoDictionary = outerDictionary["info"]!!.map
        val bencodedInfoMap = BEncoder.encode(infoDictionary)
        val bencodedInfoByteArray = ByteArray(bencodedInfoMap.remaining())
        bencodedInfoMap.get(bencodedInfoByteArray)
        return hashWithSHA1(bencodedInfoByteArray)
    }

    override fun checkValidMetaInfo(torrent: ByteArray): Boolean {
        TODO("Not yet implemented")

    }

    override fun decodeAnnounceList(bencodedAnnounceList: String): List<List<String>> {
        val announceListByteArray = bencodedAnnounceList.toByteArray(Charsets.UTF_8)
        val decoder = BDecoder(announceListByteArray.inputStream())
        val outerListOfBEncoded = decoder.decodeList().list
        val outerList = ArrayList<ArrayList<String>>()
        var innerList : ArrayList<String> = ArrayList()
        var innerBEncodedList: ArrayList<BEncodedValue>
        for ( lst in outerListOfBEncoded){
            innerBEncodedList = lst.list as ArrayList<BEncodedValue>
            for( v in innerBEncodedList){
                innerList.add(v.string)
            }
            outerList.add(innerList)
            innerList = ArrayList()
        }
        return outerList
    }

//     override fun getAnnounce(torrent: ByteArray): String {
//        try {
//            var b = BDecoder(torrent.inputStream())
//            var document = b.decodeMap();
//            val announce = document.map["announce"]!!.bytes
//
//            val ann = String(announce);
//            return ann;
//
//        }
//        catch (e: Exception) {
//            throw IllegalArgumentException();
//        }
//
//    }

//    private fun wrapWithDoubleLists(innerBEncodedValue : BEncodedValue): BEncodedValue{
//        val outerList = ArrayList<BEncodedValue>()
//        val innerList = ArrayList<BEncodedValue>()
//        innerList.add(innerBEncodedValue)
//
//        val bencodedInnerList = ByteArrayOutputStream()
//        BEncoder.encode(innerList, bencodedInnerList)
//
//        val innerListBEncodedValue = BDecoder.bdecode(ByteBuffer.wrap(bencodedInnerList.toByteArray()))
//
//        outerList.add(innerListBEncodedValue)
//        val bencodedOuterList = ByteArrayOutputStream()
//        BEncoder.encode(outerList, bencodedOuterList)
//        val outerListBEncodedValue = BDecoder.bdecode(ByteBuffer.wrap(bencodedOuterList.toByteArray()))
//
//        return outerListBEncodedValue
//    }

    override fun getBencodedAnnounceList(torrent: ByteArray): String {
        val decoder = BDecoder(torrent.inputStream())
        val outerDictionary : Map<String, BEncodedValue> = decoder.decodeMap().map
        val resultBEncodedValue  = if(outerDictionary.containsKey("announce-list")){
            val announceListBEncodedValue = outerDictionary["announce-list"]?: error("Torrent doesn't have 'announce-list' field")
            announceListBEncodedValue
        } else {
            val announceBEncodedValue = outerDictionary["announce"] ?: error("Torrent doesn't have 'announce' field")
           // wrapWithDoubleLists(announceBEncodedValue)
            return "ll" + announceBEncodedValue.string.length.toString() +":" +announceBEncodedValue.string + "ee"
        }
        val bencodedResult = ByteArrayOutputStream()
        BEncoder.encode(resultBEncodedValue, bencodedResult)
        return bencodedResult.toString()
    }

    fun ByteArray.toHex(): String {
        return joinToString("") { "%02x".format(it) }
    }
}