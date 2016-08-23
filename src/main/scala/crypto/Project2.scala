package crypto

object Project2 {
	import org.bouncycastle.util.encoders.Hex
	implicit class HexStrToByteArray(val str: String) extends AnyRef {
		def ba = Hex.decode(str)
		//str.sliding(2,2).map(Integer.parseInt(_,16).toByte).toArray
	}
}

object CTR_Decrypt {
	import org.bouncycastle.crypto.engines._
	import org.bouncycastle.crypto.params._
	import org.bouncycastle.util.BigIntegers
	import Project2._
	
	
	def apply(key: String, ct: String) = {
		val cipher = new AESEngine()
		cipher.init(true, new KeyParameter(key.ba))
		val ctr = Array.fill[Byte](8)(0)
		val iv = (ct.ba).take(16) ++ ctr
		val block1 = ct.ba.drop(16).take(16)
		val b = iv.zip(block1).map{case (a,b) => ((a^b) & 0xff).toByte}
		var out = Array.fill[Byte](16)(0)
		cipher.processBlock(b, 0, out, 0)
	//	iv.zip(out).map{case (a,b) => ((a^b) & 0xff).toByte}.toList
		out.map(_&0xff).toList //.map(_.toChar).mkString
		
	}
}