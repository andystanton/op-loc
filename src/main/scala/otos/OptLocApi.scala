package otos

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import org.json4s._
import org.json4s.native.Serialization
import spray.httpx.Json4sSupport
import spray.routing._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class OptLocApiActor(val placesService: ActorRef) extends Actor with OptLocApi {
  def actorRefFactory = context
  def receive = runRoute(optLocApiRoute)
}

trait OptLocApi extends HttpService with Json4sSupport {
  implicit def placesService: ActorRef
  implicit val json4sFormats = Serialization.formats(NoTypeHints)
  implicit val timeout = Timeout(5 seconds)

  val optLocApiRoute = pathPrefix("find" / """\w+""".r) { locationSearch =>
    get {
      complete {
        Await.result(placesService ? locationSearch, timeout.duration).asInstanceOf[Location]
      }
    }
  }
}
