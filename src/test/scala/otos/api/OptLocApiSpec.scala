package otos.api

import akka.testkit.TestActorRef
import org.scalatest._
import otos.service._
import otos.util.FixtureLoading
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class GooglePlacesServiceActorStub extends GooglePlacesServiceActor {
  override def receive = {
    case _ => sender ! Location(-1, "Newbury, West Berkshire, UK", Center(51.401409, -1.3231139))
  }
}

class PostgresPlacesServiceActorStub extends PostgresPlacesServiceActor {
  override def receive = {
    case IdRequest(location) => sender ! Location(1, "Newbury", Center(51.40033, -1.32059))
    case NameRequest(location) => sender ! List(
      Location(1, "Newbury", Center(51.40033, -1.32059))
    )
    case NearRequest(location, range) => sender ! List(
      Location(2, "Thatcham", Center(51.40366, -1.26049)),
      Location(3, "Highclere", Center(51.3386, -1.37569))
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
          responseAs[Map[String, Int]] should contain(("code", 200))
        }
      }
    }

    describe("the /find endpoint") {
      describe("the /find/id endpoint") {
        it("responds with location details of the location with the specified Id") {
          Get("/find/id/1") ~> optLocApiRoute ~> check {
            status shouldBe OK
            responseAs[Location] shouldBe Location(1, "Newbury", Center(51.40033, -1.32059))
          }
        }
      }

      describe("the /find/name endpoint") {
        it("responds with location details of locations matching the search target") {
          Get("/find/name/newbury") ~> optLocApiRoute ~> check {
            status shouldBe OK
            responseAs[List[Location]] should contain (Location(1, "Newbury", Center(51.40033, -1.32059)))
          }
        }
      }

      describe("the /find/near endpoint") {
        it("responds with the location details of locations near the search target") {
          Get("/find/near/1") ~> optLocApiRoute ~> check {
            status shouldBe OK
            val locations = responseAs[List[Location]]
            locations should contain (Location(2, "Thatcham", Center(51.40366, -1.26049)))
            locations should contain (Location(3, "Highclere", Center(51.3386, -1.37569)))
          }
        }
      }
    }

    describe("the /googleplaces endpoint") {
      it("responds with the latitude and longitude of the search target") {
        Get("/googleplaces/newbury") ~> optLocApiRoute ~> check {
          status shouldBe OK
          responseAs[Location] shouldBe Location(-1, "Newbury, West Berkshire, UK", Center(51.401409, -1.3231139))
        }
      }
    }
  }
}
