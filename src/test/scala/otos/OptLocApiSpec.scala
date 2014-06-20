package otos

import spray.http._
import StatusCodes._
import spray.testkit.ScalatestRouteTest
import org.scalatest._

class OptLocApiSpec extends FunSpec with ScalatestRouteTest with OptLocApi with Matchers {
  def actorRefFactory = system

  describe("the Optimum Locum API") {
    it("""returns a 200 response containing the text "hello world" """) {
      Get() ~> optLocApiRoute ~> check {
        status === OK
        responseAs[String] should include ("hello world")
      }
    }
  }
}
