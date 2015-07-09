package la.smx.util.csv2lucene.util

import scala.language.reflectiveCalls

/**
 * Created by arjones on 7/8/15.
 */
object ResourceManagement {
  def using[ToCloseType <: {def close()}, ReturnType](toClose: ToCloseType)(operation: ToCloseType => ReturnType): ReturnType = {
    try {
      operation(toClose)
    } finally {
      toClose.close()
    }
  }
}
