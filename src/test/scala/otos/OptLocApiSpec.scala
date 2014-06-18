package otos

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class OptLocApiSpec extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[OptLocApi], "/*")

  test("simple get") {
    get("/") {
      status should equal (200)
      body should include ("Optimum Locum")
    }
  }
}
