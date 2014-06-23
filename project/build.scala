import net.virtualvoid.sbt.graph.Plugin._
import sbt.Keys._
import sbt._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._
import spray.revolver.RevolverPlugin.Revolver.settings.{settings => revolverSettings}

object Build extends sbt.Build {
  lazy val project = Project(
    id        = "optimum-locum",
    base      = file("."),
    settings  = Defaults.coreDefaultSettings ++ assemblySettings ++ graphSettings ++ revolverSettings ++ Seq(
      libraryDependencies   ++= Dependencies(),
      jarName in assembly   := "opt-loc.jar"
    )
  )

  object Dependencies {
    object Versions {
      val akka = "2.3.3"
      val spray = "1.3.1-20140423"
      val sprayJson = "1.2.6"
      val scalatest = "2.2.0"
      val logback = "1.0.6"
      val json4s = "3.2.9"
    }

    val compileDependencies = Seq(
      "com.typesafe.akka" %%  "akka-actor"      % Versions.akka,
      "io.spray"          %%  "spray-can"       % Versions.spray,
      "io.spray"          %%  "spray-routing"   % Versions.spray,
      "io.spray"          %%  "spray-http"      % Versions.spray,
      "io.spray"          %%  "spray-httpx"     % Versions.spray,
      "io.spray"          %%  "spray-client"    % Versions.spray,
      "org.json4s"        %%  "json4s-native"   % Versions.json4s,
      "ch.qos.logback"    %   "logback-classic" % Versions.logback    % "runtime"
    )

    val testDependencies = Seq(
      "org.scalatest"     %%  "scalatest"       % Versions.scalatest  % "test",
      "com.typesafe.akka" %%  "akka-testkit"    % Versions.akka       % "test",
      "io.spray"          %%  "spray-testkit"   % Versions.spray      % "test"
    )

    def apply(): Seq[ModuleID] = compileDependencies ++ testDependencies
  }
}
