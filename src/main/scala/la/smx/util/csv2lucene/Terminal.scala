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
import ui.FileChooser

object Terminal extends App {
  if (args.length == 0)
    help()

  val target = checkFile(args(0))
  if (target.isEmpty)
    help()

  println(s"Preparing to Index ...")
  val fields = Indexer.indexCSV(target.get)

  banner()

  val reader = new ConsoleReader()
  reader.setPrompt("search> ")
  reader.addCompleter(
    new StringsCompleter(fields)
  )

  val out = new PrintWriter(reader.getOutput())

  using(new Searcher(target.get)) { searcher =>
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
    println(s" ${this.getClass.getSimpleName} -gui")

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

  def checkFile(file: String): Option[File] =
    if (file == "-gui") {

      // show file selector
      val f = FileChooser.getFile()
      if (f == null) {
        println("ERROR: You must select a file!")
        None

      } else
        Some(f)

    } else {
      // check as a current file
      val f = new File(file)
      if (f.exists())
        Some(f)

      else {
        println(s"ERROR: File ${file} not found!")
        None
      }
    }


}
