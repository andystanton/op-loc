package otos

import akka.actor.{Actor, ActorRef}
import spray.httpx.Json4sSupport
import spray.routing._

class OptLocApiActor(val placesService: ActorRef) extends Actor with OptLocApi {
  def actorRefFactory = context
  def receive = runRoute(optLocApiRoute)
}

trait OptLocApi extends HttpService with Json4sSupport {
  import akka.util.Timeout
  import scala.concurrent.Await
  import scala.concurrent.duration._
  import scala.language.postfixOps
  import org.json4s._
  import org.json4s.native.Serialization

  implicit val json4sFormats = Serialization.formats(NoTypeHints)
  implicit val timeout = Timeout(10 seconds)

  implicit def placesService: ActorRef

  val optLocApiRoute = pathPrefix("find" / """\w+""".r) { locationSearch =>
    get {
      complete {
        import akka.pattern.ask
        Await.result(placesService ? locationSearch, timeout.duration).asInstanceOf[Location]
      }
    }
  }
}
