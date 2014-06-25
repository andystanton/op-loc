package otos

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import otos.api.OptLocApiActor
import otos.service.GooglePlacesServiceActor
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App {
  implicit val system = ActorSystem("opt-loc-actor-system")
  implicit val timeout = Timeout(5.seconds)

  val placesService = system.actorOf(Props[GooglePlacesServiceActor], "google-places-service")
  val service = system.actorOf(Props(classOf[OptLocApiActor], placesService), "opt-loc-api")

  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
