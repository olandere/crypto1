package crypto

object Project2 {
  val pt1 = CTR_Decrypt("36f18357be4dbd77f050515c73fcf9f2", "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329")
  val pt2 = CTR_Decrypt("36f18357be4dbd77f050515c73fcf9f2", "770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451")
  //println(pt1)
  println(pt1.map(_.toChar).mkString)

	//println(pt2)
  println(pt2.map(_.toChar).mkString)

}

object CTR_Decrypt {
	import org.bouncycastle.crypto.engines._
	import org.bouncycastle.crypto.params._

	def apply(key: String, ct: String): List[Byte] = {
		val cipher = new AESEngine()
		cipher.init(true, new KeyParameter(key.ba))
		var iv = BigInt(ct.ba.take(16))
    val blocks = ct.ba.drop(16).sliding(16, 16)
    val out: Array[Byte] = Array.fill[Byte](16)(0)
    blocks.flatMap{
      block =>
        cipher.processBlock(iv.toByteArray, 0, out, 0)
        iv += 1
        block.zip(out).map{case (a,b) => ((a^b) & 0xff).toByte}.toList
    }.toList
	}
}