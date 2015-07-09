package la.smx.util.csv2lucene

import java.io.{File, FileReader}

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

  def indexCSV(csvFile: String) = {
    println(s"Indexing ${csvFile} ...")

    using(new FileReader(csvFile)) { reader =>

      using(new CSVParser(reader, CSVFormat.EXCEL.withHeader())) { parser =>
        // extract fields name from CSV header
        val fields = parser.getHeaderMap.keySet().toSet[String]

        using(Lucene.openIndexDir(csvFile)) { dir =>

          using(Lucene.createIndexWriter(dir)) { writer =>
            index(parser, fields, writer)
          }

        }

        fields.map(_.toLowerCase())
      }
    }
  }

  def index(parser: CSVParser, fields: Set[String], writer: IndexWriter): Unit = {
    for (record <- parser.iterator()) {
      if (record.getRecordNumber % 25 == 0) print(".")

      val doc = addToIndex(fields, record)
      if (doc.isDefined)
        writer.addDocument(doc.get)
    }

    writer.forceMerge(10)
    println(" done")
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
