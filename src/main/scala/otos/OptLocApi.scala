package otos

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class OptLocApiActor extends Actor with OptLocApi {
  def actorRefFactory = context
  def receive = runRoute(optLocApiRoute)
}

trait OptLocApi extends HttpService {
  val optLocApiRoute =
    path("") {
      get {
        respondWithMediaType(`application/xml`) {
          complete {
            <response>hello world</response>
          }
        }
      }
    }
}
