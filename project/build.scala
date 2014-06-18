import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import sbtassembly.Plugin._
import AssemblyKeys._

object Build extends sbt.Build {
  lazy val project = Project(
    id = "optimum-locum",
    base = file("."),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ assemblySettings ++ Seq(
      name                  := "optimum-locum",
      organization          := "otos",
      version               := "0.1.0-SNAPSHOT",
      scalaVersion          := "2.11.1",
      scalacOptions         := Seq("-deprecation", "-feature", "-encoding", "utf8"),
      resolvers             += Classpaths.typesafeReleases,
      libraryDependencies   ++= Dependencies()
    )
  ).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  object Dependencies {

    object Versions {
      val akka = "2.3.3"
      val scalatra = "2.3.0"
      val scalatest = "2.2.0"
      val logback = "1.0.6"
      val jetty = "8.1.8.v20121106"
    }

    val compileDependencies = Seq(
      "com.typesafe.akka" %% "akka-actor" % Versions.akka,
      "org.scalatra" %% "scalatra" % Versions.scalatra,
      "ch.qos.logback" % "logback-classic" % Versions.logback % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % Versions.jetty % "container;compile"
    )

    val testDependencies = Seq(
      "org.scalatest" %% "scalatest" % Versions.scalatest % "test",
      "org.scalatra" %% "scalatra-scalatest" % Versions.scalatra % "test",
      "com.typesafe.akka" %% "akka-testkit" % Versions.akka % "test"
    )

    def apply(): Seq[ModuleID] = compileDependencies ++ testDependencies

  }

}
