package otos

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class OpLocApiSpec extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[OpLocApi], "/*")

  test("simple get") {
    get("/") {
      status should equal (200)
      body should include ("Optimum Locum")
    }
  }
}
