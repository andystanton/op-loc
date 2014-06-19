package otos

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import _root_.akka.actor.{ActorSystem, Props}
import akka.testkit.TestActorRef
import scala.util.{Success, Failure}
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.language.postfixOps
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.xml._

class OptLocApiSpec extends ScalatraSuite with FunSuiteLike {
  implicit val system = ActorSystem()

  addServlet(new OptLocApi, "/*")

  test("the Optimum Locum API") {
    get("/") {
      status should equal (200)
      body should include ("hello world")
    }
  }

  test("the Response Builder") {
    implicit val defaultTimeout = Timeout(10 seconds)

    val actorRef = TestActorRef[ResponseBuilder]
    val actor = actorRef.underlyingActor

    val future = actorRef ? None
    val Success(result: Elem) = future.value.get
    (result \\ "response").text should include ("hello world")
  }
}
