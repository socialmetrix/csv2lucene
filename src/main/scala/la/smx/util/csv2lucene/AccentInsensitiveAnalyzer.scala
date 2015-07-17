package la.smx.util.csv2lucene

import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.core.{LowerCaseFilter, StopFilter}
import org.apache.lucene.analysis.miscellaneous.{WordDelimiterFilter, ASCIIFoldingFilter}
import org.apache.lucene.analysis.standard.{StandardAnalyzer, StandardFilter, StandardTokenizer}
import org.apache.lucene.analysis.util.StopwordAnalyzerBase
import org.apache.lucene.util.Version

/**
 * Created by arjones on 7/10/15.
 */
class AccentInsensitiveAnalyzer extends StopwordAnalyzerBase(StandardAnalyzer.STOP_WORDS_SET) {
  val DEFAULT_MAX_TOKEN_LENGTH = 255

  override def createComponents(fieldName: String): TokenStreamComponents = {
    val src = new StandardTokenizer
    src.setMaxTokenLength(DEFAULT_MAX_TOKEN_LENGTH)

    val tok: TokenStream =
      new ASCIIFoldingFilter(
        new StopFilter(
          new LowerCaseFilter(
            new StandardFilter(src)), stopwords))

    return new TokenStreamComponents(src, tok)
  }
}
