import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._
import sbtassembly.Plugin._
import AssemblyKeys._

object Build extends sbt.Build {
  lazy val project = Project(
    id = "optimum-locum",
    base = file("."),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ assemblySettings ++ Seq(
      name                  := "optimum-locum",
      organization          := "otos",
      version               := "0.1.0-SNAPSHOT",

      scalacOptions         := Seq("-deprecation", "-feature", "-encoding", "utf8"),
      resolvers             += Classpaths.typesafeReleases,
      libraryDependencies   ++= Dependencies(),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )

  object Dependencies {

    object Versions {
      val akka = "2.3.3"
      val scalatra = "2.2.2"
    }

    val compileDependencies = Seq(
      "com.typesafe.akka" %% "akka-actor" % Versions.akka,
      "org.scalatra" %% "scalatra" % Versions.scalatra,
      "org.scalatra" %% "scalatra-scalate" % Versions.scalatra,
      "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container;compile",
      "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided" artifacts (Artifact("javax.servlet", "jar", "jar"))
    )

    val testDependencies = Seq(
      "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
      "org.scalatra" %% "scalatra-scalatest" % Versions.scalatra % "test",
      "com.typesafe.akka" %% "akka-testkit" % Versions.akka % "test",
      "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "test" artifacts (Artifact("javax.servlet", "jar", "jar"))
    )

    def apply(): Seq[ModuleID] = compileDependencies ++ testDependencies

  }

}
