package otos

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.scalatest._
import otos.util.FixtureLoading

import scala.concurrent.Await

class GooglePlacesServiceSpec extends TestKit(ActorSystem("GooglePlacesServiceSpec")) with FunSpecLike with OptLocApi with FixtureLoading with Matchers with BeforeAndAfterEach {
  def actorRefFactory = system
  val placesService = system.actorOf(Props[GooglePlacesService], "places-service")

  val port: Int = 9374
  val host: String = "localhost"

  val wireMockServer = new WireMockServer(wireMockConfig().port(port))

  override def beforeEach() {
    wireMockServer.start()
    WireMock.configureFor(host, port)
  }

  override def afterEach() {
    wireMockServer.stop()
  }

  describe("the Google Places Service") {
    it("responds with the lattitude and longitude of the search target") {
      stubFor(WireMock.get(urlEqualTo("/places")).willReturn(
        aResponse()
          .withStatus(200)
          .withHeader("Content-Type", "application/json")
          .withBody(loadFixture("/fixtures/google-places-newbury.json").orNull)
      ))

      import akka.pattern.ask
      val location = Await.result(placesService ? "newbury,uk", timeout.duration).asInstanceOf[Location]

      location.id shouldBe "Newbury, West Berkshire, UK"
      location.latlong shouldBe LatLong(51.401409, -1.3231139)
    }
  }
}
