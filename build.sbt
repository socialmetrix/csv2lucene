import sbt.Keys._

name := "csv2lucene"

version := "1.0"

organization := "la.smx"

scalaVersion := "2.10.5"

mainClass in Compile := Some("la.smx.util.csv2lucene.Terminal")

libraryDependencies ++= Seq(
  "org.apache.lucene" % "lucene-core" % "5.0.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "5.0.0",
  "org.apache.lucene" % "lucene-queryparser" % "5.0.0",
  "org.apache.commons" % "commons-csv" % "1.1",
  "org.scala-lang" % "jline" % "2.10.5"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}
