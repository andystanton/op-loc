package otos

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class HelloWorldServletTests extends ScalatraSuite with FunSuiteLike {
  // `HelloWorldServlet` is your app which extends ScalatraServlet
  addServlet(classOf[OpLocApi], "/*")

  test("simple get") {
    get("/") {
      status should equal (200)
      //body should include ("hi!")
    }
  }
}
