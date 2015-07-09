package la.smx.util.csv2lucene

import la.smx.util.csv2lucene.util.Lucene
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher

/**
 * Created by arjones on 7/7/15.
 */
class Searcher(csvFile: String) {
  val dir = Lucene.openIndexDir(csvFile)
  // Now search the index:
  val reader = DirectoryReader.open(dir)

  // Parse a simple query that searches for "text":
  val analyzer = new StandardAnalyzer()
  val parser = new QueryParser("content", analyzer)

  def search(queryStr: String) {

    val searcher = new IndexSearcher(reader)
    val query = parser.parse(queryStr)
    val topDocs = searcher.search(query, 1000000)

    println
    println("=" * 30)
    println("Running query: " + queryStr)
    println("Total Documents: " + topDocs.totalHits)
    println("-" * 30)

    val hits = topDocs.scoreDocs

    for (hit <- hits) {
      val doc = searcher.doc(hit.doc)
      println(doc.get("content") + "\t" + doc.get("link"))
      println("-" * 35)
    }

    println
    println
    println("-" * 30)
    println("Total Documents: " + topDocs.totalHits)
    println("-" * 30)
    println

  }

  def close(): Unit = {
    reader.close()
    dir.close()
  }

}
