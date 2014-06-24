package otos

import akka.testkit.TestActorRef
import org.json4s._
import org.scalatest._
import otos.util.FixtureLoading
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class GooglePlacesServiceStub extends GooglePlacesService {
  override def receive = {
    case _ => sender ! Location("Newbury, West Berkshire, UK", LatLong(51.401409, -1.3231139))
  }
}

class OptLocApiSpec extends FunSpec with ScalatestRouteTest with OptLocApi with FixtureLoading with Matchers {
  def actorRefFactory = system

  val placesService = TestActorRef[GooglePlacesServiceStub]

  describe("the Optimum Locum API") {
    describe("the /find endpoint") {
      it("responds with the lattitude and longitude of the search target") {
        Get("/find/newbury") ~> optLocApiRoute ~> check {
          status shouldBe OK
          val responseJson = responseAs[JObject]
          (responseJson \ "id").extract[String] shouldBe "Newbury, West Berkshire, UK"
          (responseJson \ "latlong" \ "latitude").extract[Double] shouldBe 51.401409
          (responseJson \ "latlong" \ "longitude").extract[Double] shouldBe -1.3231139
        }
      }
    }
  }
}
