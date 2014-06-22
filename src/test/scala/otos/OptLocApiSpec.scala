package otos

import spray.http._
import StatusCodes._
import spray.testkit.ScalatestRouteTest
import org.scalatest._
import scala.xml._
import org.json4s._
import org.json4s.native.JsonMethods._

class OptLocApiSpec extends FunSpec with ScalatestRouteTest with OptLocApi with Matchers {
  def actorRefFactory = system

  describe("the Optimum Locum API") {
    describe("the /find endpoint") {
      it("responds with the lattitude and longitude of the search target") {
        Get("/find/newbury") ~> optLocApiRoute ~> check {
          status shouldBe OK
          val responseJson = responseAs[JObject]
          (responseJson \ "id").extract[String] shouldBe "newbury"
          (responseJson \ "latlong" \ "latitude").extract[Double] shouldBe 12.34
          (responseJson \ "latlong" \ "longitude").extract[Double] shouldBe 56.78
        }
      }
    }
  }
}
