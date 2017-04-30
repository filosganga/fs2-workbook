import sbt._
import sbt.Keys._

object AmmonitePlugin extends AutoPlugin {
  override def trigger: PluginTrigger = allRequirements
  override def requires: Plugins = plugins.JvmPlugin

  override lazy val projectSettings = Seq(
    libraryDependencies += "com.lihaoyi" % "ammonite" % "0.8.3" % "test" cross CrossVersion.full,
    initialCommands in (Test, console) := """ammonite.Main().run()"""  )
}