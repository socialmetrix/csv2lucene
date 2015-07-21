package la.smx.util.csv2lucene

import java.io.{File, PrintWriter}

import la.smx.util.csv2lucene.util.Lucene
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher

/**
 * Created by arjones on 7/7/15.
 */
class Searcher(file: File, fields: Set[String]) {
  val DEFAULT_FIELD_FOR_QUERY_TERMS = detectDefaultField
  println(s"Default field for query terms is [ ${DEFAULT_FIELD_FOR_QUERY_TERMS} ]\n")

  val dir = Lucene.openIndexDir(file)

  val reader = DirectoryReader.open(dir)
  val analyzer = new AccentInsensitiveAnalyzer()
  val parser = new QueryParser(DEFAULT_FIELD_FOR_QUERY_TERMS, analyzer)

  def search(queryStr: String, out: PrintWriter) {
    if (queryStr.isEmpty)
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

      out.println(doc.get(DEFAULT_FIELD_FOR_QUERY_TERMS) + "\t" + doc.get("link"))
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

  /**
   * This method try to identify which field should be used
   * as default to the search, ie: "content", "text".
   * The order in the list is from most likely to least likely, if the
   * target CSV contains both fields, the first matched will be
   * selected as default
   */
  def detectDefaultField() = {
    val possibleFields = List("content", "text", "contenido", "texto", "conteúdo")

    val options: List[String] = for (field <- fields.toList;
                                     p <- possibleFields if field.contains(p)) yield p

    if (options.size == 0) {
      println("WARN: no default field found! Always query using notation `field:value`. ie: content:test")
      "none"

    } else
      options(0)
  }


}
