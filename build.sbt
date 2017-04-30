
organization := "com.filippodeluca"

name := "fs2-workbook"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq (
	"co.fs2" %% "fs2-core" % "0.9.5",
	"co.fs2" %% "fs2-io" % "0.9.5",
	"org.scalatest" %% "scalatest" % "3.0.1" % Test,
	"org.scalacheck" %% "scalacheck" % "1.13.5" % Test
)
