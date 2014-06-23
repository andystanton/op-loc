import otos._
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot extends App {
  implicit val system = ActorSystem("spray-can-actor-system")

  implicit val timeout = Timeout(5.seconds)

  val placesService = system.actorOf(Props[GooglePlacesService], "places-service")

  val service = system.actorOf(Props(classOf[OptLocApiActor], placesService), "opt-loc-api")


  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
