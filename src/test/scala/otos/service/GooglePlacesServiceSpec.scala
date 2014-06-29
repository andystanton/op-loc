package otos.service

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import akka.util.Timeout
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.scalatest._
import otos.util.FixtureLoading

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class GooglePlacesServiceSpec
  extends TestKit(ActorSystem("GooglePlacesServiceSpec"))
  with FunSpecLike
  with FixtureLoading
  with Matchers
  with BeforeAndAfterEach {

  def actorRefFactory = system
  val googlePlacesServiceActor = system.actorOf(Props[GooglePlacesServiceActor], "google-places-service")

  val port: Int = 9374
  val host: String = "localhost"
  val wireMockServer = new WireMockServer(wireMockConfig().port(port))

  implicit val timeout = Timeout(10 seconds)

  override def beforeEach() {
    wireMockServer.start()
    WireMock.configureFor(host, port)

    stubFor(WireMock.get(urlEqualTo("/places")).willReturn(
      aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(loadFixture("/fixtures/google-places-newbury.json").orNull)
    ))
  }

  override def afterEach() {
    wireMockServer.stop()
  }

  describe("the Google Places Service Actor") {
    it("responds with a location") {
      import akka.pattern.ask
      val location = Await.result(googlePlacesServiceActor ? "newbury,uk", timeout.duration).asInstanceOf[Location]

      location.id shouldBe -1
      location.name shouldBe "Newbury, West Berkshire, UK"
      location.latlong shouldBe LatLong(51.401409, -1.3231139)
    }
  }

  describe("the Google Places Service") {
    it("responds with the latitude and longitude of the search target") {
      val pService = new GooglePlacesService {
        override implicit def apiKey: String = "0123456789"
        override implicit def apiUrl: String = "http://localhost:9374/places"
        override implicit def system: ActorSystem = actorRefFactory
      }

      val locationResponse = Await.result(pService.queryPlacesApi("newbury,uk"), timeout.duration)
      val locationJson = locationResponse.entity.asString
      val location: Location = pService.jsonToLocation(locationJson)

      location.id shouldBe -1
      location.name shouldBe "Newbury, West Berkshire, UK"
      location.latlong shouldBe LatLong(51.401409, -1.3231139)
    }
  }
}
