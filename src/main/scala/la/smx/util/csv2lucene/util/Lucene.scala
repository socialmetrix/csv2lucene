package la.smx.util.csv2lucene.util

import java.io.File
import java.nio.file.Paths

import la.smx.util.csv2lucene.AccentInsensitiveAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory

/**
 * Created by arjones on 7/8/15.
 */
object Lucene {
  def createIndexWriter(dir: FSDirectory) = {
    val analyzer = new AccentInsensitiveAnalyzer()
    val conf = new IndexWriterConfig(analyzer)
    conf.setOpenMode(OpenMode.CREATE)
    conf.setRAMBufferSizeMB(256.0)

    new IndexWriter(dir, conf)
  }

  def openIndexDir(csvFile: File) = {
    val indexDir = "indices" + File.separator + Hashing.MD5(csvFile.toString)
    FSDirectory.open(Paths.get(indexDir))
  }
}
