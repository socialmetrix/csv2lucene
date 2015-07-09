package la.smx.util.csv2lucene

import java.io.FileReader

import la.smx.util.csv2lucene.util.Lucene
import la.smx.util.csv2lucene.util.ResourceManagement._
import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}
import org.apache.lucene.document.{Document, Field, LongField, TextField}
import org.apache.lucene.index.IndexWriter

import scala.collection.JavaConversions.{asScalaIterator, _}

/**
 * Created by arjones on 7/7/15.
 */
object Indexer {

  def indexCSV(csvFile: String) {
    println(s"Indexing ${csvFile} ...")

    using(new FileReader(csvFile)) { reader =>

      using(new CSVParser(reader, CSVFormat.EXCEL.withHeader())) { parser =>
        val fields = parser.getHeaderMap

        using(Lucene.openIndexDir(csvFile)) { dir =>

          var writer: IndexWriter = null
          try {
            writer = Lucene.createIndexWriter(dir)

            for (record <- parser.iterator()) {
              // print(r.getRecordNumber + " / ")
              print(".")

              val doc = addToIndex(fields.keySet.toSet, record)
              if (doc.isDefined)
                writer.addDocument(doc.get)
            }

            writer.forceMerge(10)
            println("Done")


          } finally {
            writer.close()
          }
        }
      }
    }
  }

  def addToIndex(fields: Set[String], data: CSVRecord): Option[Document] = {

    try {
      val doc = new Document()
      doc.add(new LongField("id", data.getRecordNumber, Field.Store.YES))

      for (field <- fields) {
        val value = data.get(field)
        doc.add(new TextField(field.toLowerCase, value, Field.Store.YES))
      }

      Some(doc)
    } catch {
      case ex: java.lang.IllegalArgumentException => None
    }

  }


}
