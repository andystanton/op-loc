package otos.api

import akka.testkit.TestActorRef
import org.json4s._
import org.scalatest._
import otos.service._
import otos.util.FixtureLoading
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class GooglePlacesServiceActorStub extends GooglePlacesServiceActor {
  override def receive = {
    case _ => sender ! Location("Newbury, West Berkshire, UK", LatLong(51.401409, -1.3231139))
  }
}

class PostgresPlacesServiceActorStub extends PostgresPlacesServiceActor {
  override def receive = {
    case FindRequest(location) => sender ! Location("Newbury", LatLong(51.40033, -1.32059))
    case NearRequest(location, range) => sender ! List(
      Location("Thatcham", LatLong(51.40366, -1.26049)),
      Location("Highclere", LatLong(51.3386, -1.37569))
    )
  }
}

class OptLocApiSpec extends FunSpec with ScalatestRouteTest with OptLocApi with FixtureLoading with Matchers {
  def actorRefFactory = system

  val googlePlacesServiceActor = TestActorRef[GooglePlacesServiceActorStub]
  val postgresPlacesServiceActor = TestActorRef[PostgresPlacesServiceActorStub]

  describe("the Optimum Locum API") {
    describe("the /ping endpoint") {
      it("responds with an HTTP 200 code and a status response message") {
        Get("/ping") ~> optLocApiRoute ~> check {
          status shouldBe OK
          val responseJson = responseAs[JObject]
          (responseJson \ "code").extract[Int] shouldBe 200
        }
      }
    }

    describe("the /find endpoint") {
      it("responds with the latitude and longitude of the search target") {
        Get("/find/newbury") ~> optLocApiRoute ~> check {
          status shouldBe OK
          println(response.entity.asString)
          val responseJson = responseAs[JObject]
          (responseJson \ "id").extract[String] shouldBe "Newbury"
          (responseJson \ "latlong" \ "latitude").extract[Double] shouldBe 51.40033
          (responseJson \ "latlong" \ "longitude").extract[Double] shouldBe -1.32059
        }
      }
    }

    describe("the /near endpoint") {
      it("responds with the location details of locations near the search target") {
        Get("/near/newbury") ~> optLocApiRoute ~> check {
          status shouldBe OK
          println(response.entity.asString)
        }
      }
    }

    describe("the /googleplaces endpoint") {
      it("responds with the latitude and longitude of the search target") {
        Get("/googleplaces/newbury") ~> optLocApiRoute ~> check {
          status shouldBe OK
          println(response.entity.asString)
          val responseJson = responseAs[JObject]
          (responseJson \ "id").extract[String] shouldBe "Newbury, West Berkshire, UK"
          (responseJson \ "latlong" \ "latitude").extract[Double] shouldBe 51.401409
          (responseJson \ "latlong" \ "longitude").extract[Double] shouldBe -1.3231139
        }
      }
    }
  }
}
