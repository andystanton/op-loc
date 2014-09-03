package otos

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import otos.api.OptLocApiActor
import otos.service.PostgresPlacesServiceActor
import spray.servlet.WebBoot

import scala.concurrent.duration._

//object Boot extends App {
//  implicit val system = ActorSystem("opt-loc-actor-system")
//  implicit val timeout = Timeout(5.seconds)
//
//  val googlePlacesService = system.actorOf(Props[GooglePlacesServiceActor], "google-places-service")
//  val postgresPlacesService = system.actorOf(Props[PostgresPlacesServiceActor], "postgres-places-service")
//
//  val service = system.actorOf(Props(classOf[OptLocApiActor], googlePlacesService, postgresPlacesService), "opt-loc-api")
//
//  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8080)
//}

class Boot extends WebBoot {
  implicit val system = ActorSystem("opt-loc-actor-system")
  implicit val timeout = Timeout(5.seconds)

  val postgresPlacesService = system.actorOf(Props[PostgresPlacesServiceActor], "postgres-places-service")

  val serviceActor = system.actorOf(Props(classOf[OptLocApiActor], postgresPlacesService), "opt-loc-api")

  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }
}