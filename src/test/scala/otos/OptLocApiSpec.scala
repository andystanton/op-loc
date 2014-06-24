package otos

import akka.actor.Props
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.json4s._
import org.scalatest._
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

trait FixtureLoading {
  def loadFixture(filename: String): Option[String] = {
    for {
      resource <- Option(getClass.getResourceAsStream(filename))
      bufferedResource <- Option(io.Source.fromInputStream(resource))
      content <- Option(bufferedResource.mkString)
    } yield content
  }
}

class OptLocApiSpec extends FunSpec with ScalatestRouteTest with OptLocApi with FixtureLoading with Matchers with BeforeAndAfterEach {
  def actorRefFactory = system
  val placesService = system.actorOf(Props[GooglePlacesService], "places-service")

  val port: Int = 9374
  val host: String = "localhost"

  val wireMockServer = new WireMockServer(wireMockConfig().port(port))

  override def beforeEach {
    wireMockServer.start()
    WireMock.configureFor(host, port)
  }

  override def afterEach {
    wireMockServer.stop()
  }

  describe("the Optimum Locum API") {
    describe("the /find endpoint") {
      it("responds with the lattitude and longitude of the search target") {
        stubFor(WireMock.get(urlEqualTo("/places")).willReturn(
          aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(loadFixture("/fixtures/google-places-newbury.json").orNull)
        ))

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
