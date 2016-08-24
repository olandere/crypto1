/**
  * Created by ericolander on 8/22/16.
  */
package object crypto {
  import org.bouncycastle.util.encoders.Hex

  implicit class HexStrToByteArray(val str: String) extends AnyRef {
    def ba = Hex.decode(str)
    //str.sliding(2,2).map(Integer.parseInt(_,16).toByte).toArray
  }
}
