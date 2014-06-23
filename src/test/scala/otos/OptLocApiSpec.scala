package otos

import akka.actor.Props
import org.json4s._
import org.scalatest._
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class OptLocApiSpec extends FunSpec with ScalatestRouteTest with OptLocApi with Matchers {
  def actorRefFactory = system
  val placesService = system.actorOf(Props[GooglePlacesService], "places-service")

  describe("the Optimum Locum API") {
    describe("the /find endpoint") {
      it("responds with the lattitude and longitude of the search target") {
        Get("/find/newbury") ~> optLocApiRoute ~> check {
          status shouldBe OK
          val responseJson = responseAs[JObject]
          (responseJson \ "id").extract[String] shouldBe "newbury"
          (responseJson \ "latlong" \ "latitude").extract[Double] shouldBe 12.34
          (responseJson \ "latlong" \ "longitude").extract[Double] shouldBe 56.78
          true
        }
      }
    }
  }
}
