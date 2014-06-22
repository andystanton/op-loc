package otos

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.util.Timeout
import akka.pattern.ask
import spray.routing._
import spray.http._
import MediaTypes._
import spray.httpx.Json4sSupport
import org.json4s._
import org.json4s.native.Serialization
import spray.util._
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.language.postfixOps

class OptLocApiActor(val placesService: ActorRef) extends Actor with OptLocApi {
  def actorRefFactory = context
  def receive = runRoute(optLocApiRoute)
}

trait OptLocApi extends HttpService with Json4sSupport {
  implicit def placesService: ActorRef;
  implicit val json4sFormats = Serialization.formats(NoTypeHints)
  implicit val timeout = Timeout(5 seconds)

  val optLocApiRoute = pathPrefix("find" / """\w+""".r) { locationSearch =>
    get {
      complete {
        val future = placesService ? locationSearch
        val result = Await.result(future, timeout.duration).asInstanceOf[Location]
        result
      }
    }
  }
}
