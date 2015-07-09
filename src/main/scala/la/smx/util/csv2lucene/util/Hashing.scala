package la.smx.util.csv2lucene.util

import java.security.MessageDigest

/**
 * Created by arjones on 7/8/15.
 */
object Hashing {
  def MD5(message: String) = MessageDigest.getInstance("MD5").digest(message.getBytes).map("%02X".format(_)).mkString
}
