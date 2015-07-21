import sbt.Keys._

name := "csv2lucene"

version := "1.1.2"

organization := "la.smx"

scalaVersion := "2.10.5"

scalacOptions := Seq("-deprecation", "-unchecked", "-feature")

mainClass in Compile := Some("la.smx.util.csv2lucene.Terminal")

libraryDependencies ++= Seq(
  "org.apache.lucene" % "lucene-core" % "5.0.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "5.0.0",
  "org.apache.lucene" % "lucene-queryparser" % "5.0.0",
  "org.apache.commons" % "commons-csv" % "1.1",
  "org.scala-lang" % "jline" % "2.10.5"
)

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

//import sbtassembly.AssemblyPlugin.defaultShellScript
//assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}
