package otos

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import otos.api.OptLocApiActor
import otos.service.{PostgresPlacesServiceActor, GooglePlacesServiceActor}
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App {
  implicit val system = ActorSystem("opt-loc-actor-system")
  implicit val timeout = Timeout(5.seconds)

  val googlePlacesService = system.actorOf(Props[GooglePlacesServiceActor], "google-places-service")
  val postgresPlacesService = system.actorOf(Props[PostgresPlacesServiceActor], "postgres-places-service")

  val service = system.actorOf(Props(classOf[OptLocApiActor], googlePlacesService, postgresPlacesService), "opt-loc-api")

  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8080)
}
