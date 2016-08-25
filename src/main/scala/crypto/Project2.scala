package crypto

import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.params.KeyParameter

object Project2 {

  def apply() {

    def byteArrToStr(ba: Seq[Byte]) = ba.map(_.toChar).mkString

    val pt1 = CBC_Decrypt("140b41b22a29beb4061bda66b6747e14", "4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81")
    val pt2 = CBC_Decrypt("140b41b22a29beb4061bda66b6747e14", "5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253")
    val pt3 = CTR_Decrypt("36f18357be4dbd77f050515c73fcf9f2", "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329")
    val pt4 = CTR_Decrypt("36f18357be4dbd77f050515c73fcf9f2", "770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451")

    println(byteArrToStr(pt1))
    println(byteArrToStr(pt2))
    println(byteArrToStr(pt3))
    println(byteArrToStr(pt4))
  }
}

object CBC_Decrypt {
  def unpad(str: Seq[Byte]) = str.dropRight(str.last)

  def apply(key: String, message: String): Seq[Byte] = {
    var iv = message.ba.take(16)
    val cipher = new AESEngine()
    cipher.init(false, new KeyParameter(key.ba))
    val blocks = message.ba.drop(16).sliding(16, 16)
    val out: Array[Byte] = Array.fill[Byte](16)(0)
    unpad(blocks.flatMap {
      block =>
        cipher.processBlock(block, 0, out, 0)
        val result = out.zip(iv).map { case (a, b) => ((a ^ b) & 0xff).toByte }.toList
        iv = block
        result
    }.toList)
  }
}

object CTR_Encrypt {

  def apply(key: String, pt: String, iv: String): Seq[Byte] = {
    val cipher = new AESEngine()
    cipher.init(true, new KeyParameter(key.ba))
    var ivbi = BigInt(iv.ba)
    val blocks = pt.toCharArray.sliding(16, 16)
    val out: Array[Byte] = Array.fill[Byte](16)(0)
    ivbi.toByteArray ++ blocks.flatMap {
      block =>
        cipher.processBlock(ivbi.toByteArray, 0, out, 0)
        ivbi += 1
        block.zip(out).map { case (a, b) => ((a ^ b) & 0xff).toByte }.toList
    }.toList
  }
}

object CTR_Decrypt {

  def apply(key: String, ct: String): Seq[Byte] = {
    val cipher = new AESEngine()
    cipher.init(true, new KeyParameter(key.ba))
    var iv = BigInt(ct.ba.take(16))
    val blocks = ct.ba.drop(16).sliding(16, 16)
    val out: Array[Byte] = Array.fill[Byte](16)(0)
    blocks.flatMap {
      block =>
        cipher.processBlock(iv.toByteArray, 0, out, 0)
        iv += 1
        block.zip(out).map { case (a, b) => ((a ^ b) & 0xff).toByte }.toList
    }.toList
  }
}