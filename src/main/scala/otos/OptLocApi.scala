package otos

import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import org.scalatra._
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Success, Failure}
import ExecutionContext.Implicits.global


final case class Result(value: String)

class SomeActor extends Actor {
    def receive = {
        case _ => {
            sender ! Result("hello world. I'm an akka actor responding to your request through a scalatra servlet")
        }
    }
}

class OptLocApi()(implicit val system: ActorSystem) extends ScalatraServlet {

  get("/") {
    val someActor = system.actorOf(Props[SomeActor])

    val future = someActor.ask()(5 seconds)

    Await.result(future, 5 seconds) match {
      case Result(response) => {
        contentType = "application/xml"
        <response>{response}</response>
      }
    }
  }

}
