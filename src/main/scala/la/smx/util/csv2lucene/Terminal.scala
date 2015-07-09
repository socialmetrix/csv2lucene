package la.smx.util.csv2lucene

/**
 * Created by arjones on 7/8/15.
 */

import java.io.{File, PrintWriter}

import la.smx.util.csv2lucene.util.ResourceManagement._

import scala.tools.jline.console.ConsoleReader
import scala.tools.jline.console.completer.StringsCompleter

import scala.util.control.Breaks._
import collection.JavaConversions._

object Terminal extends App {
  if (args.length == 0)
    help()

  val csvFile: String = args(0)
  checkFile(csvFile)

  println(s"Preparing to Index ...")
  val fields = Indexer.indexCSV(csvFile)

  banner()

  val reader = new ConsoleReader()
  reader.setPrompt("search> ")
  reader.addCompleter(
    new StringsCompleter(fields)
  )

  val out = new PrintWriter(reader.getOutput())

  using(new Searcher(csvFile)) { searcher =>
    breakable {
      while (true) {
        val line: String = reader.readLine()


        if (line == null || line == "q" || line == "quit" || line == "exit")
          break

        if (line == "help")
          out.println(searcher.querySyntaxHelp)

        else
          try {
            searcher.search(line, out)

          } catch {
            case e: Exception => out.println("ERROR: " + e.getMessage)
          }

        out.println
      }
    }
  }

  reader.getTerminal.restore
  println("Goodbye!")

  def help(): Unit = {
    println("Missing CSV file")
    println(s" ${this.getClass.getSimpleName} <csv_file>")
    sys.exit(1)
  }


  def banner(): Unit = {
    val message =
      """               ___  _
        |              |__ \| |
        |  ___ _____   __ ) | |_   _  ___ ___ _ __   ___
        | / __/ __\ \ / // /| | | | |/ __/ _ \ '_ \ / _ \
        || (__\__ \\ V // /_| | |_| | (_|  __/ | | |  __/
        | \___|___/ \_/|____|_|\__,_|\___\___|_| |_|\___|
        |
        |Explore your CSV content
        |    Type help to get detailed information
        |    Press [tab] to auto-complete fields
        |    Press q to quit
      """.stripMargin

    println(message)
  }

  def checkFile(csvFile: String): Unit = {
    if (!new File(csvFile).exists()) {
      println(s"\nFile ${csvFile} not found!\n")

      help()
    }
  }

}
