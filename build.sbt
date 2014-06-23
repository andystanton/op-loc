name                  := "optimum-locum"

organization          := "otos"

version               := "0.1.0-SNAPSHOT"

scalaVersion          := "2.11.1"

scalacOptions         := Seq("-deprecation", "-feature", "-encoding", "utf8")

resolvers             ++= Seq(Classpaths.typesafeReleases, "spray repo" at "http://repo.spray.io/")