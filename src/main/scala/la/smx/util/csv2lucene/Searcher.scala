package la.smx.util.csv2lucene

import java.io.PrintWriter

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
  val analyzer = new AccentInsensitiveAnalyzer()
  val parser = new QueryParser("content", analyzer)

  def search(queryStr: String, out: PrintWriter) {
    if(queryStr.isEmpty)
      return

    val searcher = new IndexSearcher(reader)
    val query = parser.parse(queryStr)
    val topDocs = searcher.search(query, 1000000)

    out.println
    out.println("=" * 30)
    out.println("Running query: " + queryStr)
    out.println("Total Documents: " + topDocs.totalHits)
    out.println("-" * 30)

    val hits = topDocs.scoreDocs

    for (hit <- hits) {
      val doc = searcher.doc(hit.doc)

      out.println(doc.get("content") + "\t" + doc.get("link"))
      out.println("-" * 35)
    }

    out.println
    out.println
    out.println("-" * 30)
    out.println("Total Documents: " + topDocs.totalHits)
    out.println("-" * 30)
    out.println

    out.flush()
  }

  def close(): Unit = {
    reader.close()
    dir.close()
  }

  def querySyntaxHelp() =
    """
      |Search for word "foo" on the "content" field (default)
      |foo
      |
      |Seacrh for "foo" OR "bar"
      |foo bar
      |
      |Search for word "socialmetrix" in the authors field.
      |authors:socialmetrix
      |
      |Search for exact phrase "social media".
      |"social media"
      |
      |Search for "twitter" in the sources field AND the phrase "social media" in the body field.
      |sources:"twitter" AND "social media"
      |
      |Search for "social data" within 4 words from each other.
      |"social data"~4
      |
      |More info: http://www.lucenetutorial.com/lucene-query-syntax.html
      |
      |
    """.stripMargin

}
