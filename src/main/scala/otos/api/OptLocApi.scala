package otos.api

import akka.actor.{Actor, ActorRef}
import otos.service.Location
import otos.service.PostgresPlacesServices
import spray.httpx.Json4sSupport
import spray.routing._

class OptLocApiActor(val placesServiceActor: ActorRef) extends Actor with OptLocApi {
  def actorRefFactory = context
  def receive = runRoute(optLocApiRoute)
}

trait OptLocApi extends HttpService with Json4sSupport {
  import akka.util.Timeout
  import org.json4s._
  import org.json4s.native.Serialization

import scala.concurrent.Await
  import scala.concurrent.duration._
  import scala.language.postfixOps

  implicit val json4sFormats = Serialization.formats(NoTypeHints)
  implicit val timeout = Timeout(10 seconds)

  implicit def placesServiceActor: ActorRef

  val optLocApiRoute =  {
    path("find" / """\w+""".r) { locationSearch =>
      get {
        complete {
          import akka.pattern.ask
          Await.result(placesServiceActor ? locationSearch, timeout.duration).asInstanceOf[Location]
        }
      }
    } ~
    path("find2" / """\w+""".r) { locationSearch =>
      get {
        complete {
          val pgService = new PostgresPlacesServices
          pgService.findLocation(locationSearch)
        }
      }
    }
  }
}
