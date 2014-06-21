import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import spray.revolver.RevolverPlugin._

object Build extends sbt.Build {
  lazy val project = Project(
    id = "optimum-locum",
    base = file("."),
    settings = Defaults.defaultSettings
        ++ assemblySettings
        ++ Seq(
      name                  := "optimum-locum",
      organization          := "otos",
      version               := "0.1.0-SNAPSHOT",
      scalaVersion          := "2.11.1",
      scalacOptions         := Seq("-deprecation", "-feature", "-encoding", "utf8"),
      resolvers             ++= Seq(Classpaths.typesafeReleases, "spray repo" at "http://repo.spray.io/"),
      libraryDependencies   ++= Dependencies()
    )
  )
  .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)
  .settings(Revolver.settings: _*)

  object Dependencies {

    object Versions {
      val akka = "2.3.3"
      val spray = "1.3.1-20140423"
      val scalatest = "2.2.0"
      val logback = "1.0.6"
    }

    val compileDependencies = Seq(
      "com.typesafe.akka" %% "akka-actor" % Versions.akka,
      "io.spray" %% "spray-can" % Versions.spray,
      "io.spray" %% "spray-routing" % Versions.spray,
      "ch.qos.logback" % "logback-classic" % Versions.logback % "runtime"
    )

    val testDependencies = Seq(
      "org.scalatest" %% "scalatest" % Versions.scalatest % "test",
      "com.typesafe.akka" %% "akka-testkit" % Versions.akka % "test",
      "io.spray" %% "spray-testkit" % Versions.spray  % "test"
    )

    def apply(): Seq[ModuleID] = compileDependencies ++ testDependencies
  }
}
