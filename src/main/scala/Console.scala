/**
 * Created by arjones on 7/8/15.
 */

import la.smx.util.csv2lucene.{Searcher, Indexer}

import scala.util.control.Breaks._
import la.smx.util.csv2lucene.util.ResourceManagement._

object Console extends App {
  if (args.length == 0)
    help()


  val csvFile: String = args(0)
  println(s"Preparing to Index ... ${csvFile}")

  Indexer.indexCSV(csvFile)

  banner()

  using(new Searcher(csvFile)) { searcher =>
    println

    breakable {
      while (true) {
        val query = readLine("search> ")
        if (query == null || query == "q" || query == "quit")
          break

        try {
          searcher.search(query)
        } catch {
          case e: Exception => println("ERROR: " + e.getMessage)
        }

        println
      }
    }
    searcher.close()
  }
  println("Goodbye!")

  def help(): Unit = {
    println
    println("Console <csv_file>")
    sys.exit(1)
  }

  def banner(): Unit = {
    println
    println("Explore your CSV content")
    println("  Press q to quit")
    println
  }

}
