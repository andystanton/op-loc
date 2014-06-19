package otos

import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.{Success, Failure}
import org.scalatra.{Accepted, AsyncResult, FutureSupport, ScalatraServlet}

class ResponseBuilder extends Actor {
    def receive = {
        case _ => {
            sender ! {
                <response>
                    hello world. I'm an akka actor responding to your
                    request through a scalatra servlet
                </response>
            }
        }
    }
}

class OptLocApi()(implicit val system: ActorSystem) extends ScalatraServlet with FutureSupport {

  protected implicit def executor: ExecutionContext = system.dispatcher

  val responseBuilder = system.actorOf(Props[ResponseBuilder])

  get("/") {
    implicit val defaultTimeout = Timeout(10 seconds)

    new AsyncResult {
        contentType = "application/xml"
        val is = responseBuilder ? None
    }
  }

}
