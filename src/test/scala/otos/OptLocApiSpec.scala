package otos

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import _root_.akka.actor.{ActorSystem, Props}

class OptLocApiSpec extends ScalatraSuite with FunSuiteLike {
  implicit val system = ActorSystem()
  addServlet(new OptLocApi, "/*")

  test("simple get") {
    get("/") {
      status should equal (200)
      body should include ("Optimum Locum")
    }
  }
}
