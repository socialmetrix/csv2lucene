package la.smx.util.csv2lucene.util

import org.apache.lucene.index.IndexWriter

/**
 * Created by arjones on 7/8/15.
 */
object ResourceManagement {
  def using[ToCloseType <: {def close()}, ReturnType](toClose: => ToCloseType)(operation: ToCloseType => ReturnType): ReturnType = {

    try {
      operation(toClose)

    } finally {
//            println(s"   Closing ${operation.toString()}")
      toClose.close()
    }
  }
}
