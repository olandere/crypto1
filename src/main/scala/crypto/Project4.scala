package crypto

object Project4 {
  import crypto._
  import scalaj.http._
  import scala.annotation.tailrec
    
  val base = Http("http://crypto-class.appspot.com/po")
  val param = "er=f20bdba6ff29eed7b046d1df9fb7000058b1ffb4210a580f748b4ac714c001bd4a61044426fb515dad3f21f18aa577c0bdf302936266926ff37dbf7035d5eeb4"
  val iv = param.drop(3).take(32)
  val blocks = param.drop(35).sliding(32,32).toList
     
  // decoded is hexstring
  def attackBlocks(ct: String, pad: Int, decoded: String): Stream[String] = {
    val prefix = ct.take(ct.length - 2*pad)
    val byte = ct.drop(ct.length - 2*pad).ba.head & 0xff
    val suffix = ct.drop(ct.length - 2*(pad-1)).ba.zip(decoded.ba).map{case (a,b) => (a^b^pad).toByte}.hexStr
    println(s"prefix: $prefix, byte: $byte, suffix: $suffix")
    
    def blocks(z: Int): Stream[String] = if (z > 255) Stream.empty else
      (prefix + f"${byte^z^pad}%02x" + suffix) #:: blocks(z+1)
        
    blocks(0)   
  } 

  def sendBlock(ct: String) = {
	require(ct.length % 32 == 0)
    val response = base.param("er", ct)//iv + ct)
    //println(response.params)
    val result = response.asString
    if (result.code == 404) {
      println("FOUND!!!!")
    }
    result
//  new java.net.URL(s"${base}?er=${iv}${ct}").getContent
  }

  @tailrec
  def decryptBlock(pad: Int, pt: String): String = {
	  if (pad > 16) pt else {
	    val byte = attackBlocks(blocks(0), pad, pt).map{ct => sendBlock(iv+ct+blocks(1))}.zipWithIndex.filter{case(a,b)=>a.code==404}.head._2
	    decryptBlock(pad+1, f"$byte%02x$pt")
    }
  }
  //attackBlocks(blocks(0), 7, "697368204f73").map{ct => sendBlock(iv+ct+blocks(1))}.zipWithIndex.filter{case(a,b)=>a.code==404}.head

val pt0 = "546865204d6167696320576f72647320"
val pt1 = "6172652053717565616d697368204f73"
val pt2 = "73696672616765090909090909090909"

unpad((pt0+pt1+pt2).ba).map(_.toChar).mkString
}