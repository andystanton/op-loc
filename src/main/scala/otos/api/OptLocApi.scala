package otos.api

import akka.actor.{Actor, ActorRef}
import otos.service.{NearRequest, FindRequest, Location}
import spray.httpx.Json4sSupport
import spray.routing._

class OptLocApiActor(val googlePlacesServiceActor: ActorRef, val postgresPlacesServiceActor: ActorRef) extends Actor with OptLocApi {
  def actorRefFactory = context
  def receive = runRoute(optLocApiRoute)
}

trait OptLocApi extends HttpService with Json4sSupport {
  import akka.pattern.ask
  import akka.util.Timeout
  import org.json4s._
  import org.json4s.native.Serialization

  import scala.concurrent.Await
  import scala.concurrent.duration._
  import scala.language.postfixOps

  implicit val json4sFormats = Serialization.formats(NoTypeHints)
  implicit val timeout = Timeout(10 seconds)

  implicit def googlePlacesServiceActor: ActorRef
  implicit def postgresPlacesServiceActor: ActorRef

  val optLocApiRoute = {
    path("ping") {
      get {
        complete {
          Map("code" -> 200)
        }
      }
    } ~
    path("googleplaces" / """\w+""".r) { locationSearch =>
      get {
        complete {
          Await.result(googlePlacesServiceActor ? locationSearch, timeout.duration).asInstanceOf[Location]
        }
      }
    } ~
    path("find" / """\w+""".r) { locationSearch =>
      get {
        complete {
          Await.result(postgresPlacesServiceActor ? FindRequest(locationSearch), timeout.duration).asInstanceOf[Location]
        }
      }
    } ~
    path("near" / """\w+""".r) { locationSearch =>
      get {
        parameters("range" ? "10000") { range =>
          complete {
            Await.result(postgresPlacesServiceActor ? NearRequest(locationSearch, range.toInt), timeout.duration).asInstanceOf[Seq[Location]]
          }
        }
      }
    }
  }
}
