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

class OptLocApiSpec extends FunSpec with ScalatestRouteTest with OptLocApi with Matchers with BeforeAndAfterEach {
  def actorRefFactory = system
  val placesService = system.actorOf(Props[GooglePlacesService], "places-service")

  val port: Int = 9374
  val host: String = "localhost"

  val wireMockServer = new WireMockServer(wireMockConfig().port(port))

  override def beforeEach {
    wireMockServer.start()
    WireMock.configureFor(host, port)

    stubFor(WireMock.get(urlEqualTo("/places")).willReturn(
      aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(
          """
            |{
            |   "html_attributions" : [],
            |   "results" : [
            |      {
            |         "formatted_address" : "Newbury, West Berkshire, UK",
            |         "geometry" : {
            |            "location" : {
            |               "lat" : 51.401409,
            |               "lng" : -1.3231139
            |            },
            |            "viewport" : {
            |               "northeast" : {
            |                  "lat" : 51.4163978,
            |                  "lng" : -1.2870671
            |               },
            |               "southwest" : {
            |                  "lat" : 51.36067019999999,
            |                  "lng" : -1.3647926
            |               }
            |            }
            |         },
            |         "icon" : "http://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png",
            |         "id" : "67f199721dc32dd385fd8f6735120b7072670ad2",
            |         "name" : "Newbury",
            |         "reference" : "CpQBhgAAAP_4bKHcA0ujQ2saV0p1Ak_JkI7lIhvVUyn9vRHK3tvj7kDlRDIzEbQ3XznZXdOa4NhxK-3Zt-kZaw0QfDyBA4PM-l2WouTRTX7N6ghfKrm404Sznk6Fxn94Pkp9V7ne9GGpW13Dyue3nYne4umX2kLOXqazqNtsA-vewVtCSMPWDBWoDdzriuGnbfULBmq6mxIQiY8cfyiX8xwVU4azA5nwkBoUoqHBAOzRzExrjVjzE5wS2KgGVtU",
            |         "types" : [ "locality", "political" ]
            |      }
            |   ],
            |   "status" : "OK"
            |}
          """.stripMargin)
    ))
  }

  override def afterEach {
    wireMockServer.stop()
  }

  describe("the Optimum Locum API") {
    describe("the /find endpoint") {
      it("responds with the lattitude and longitude of the search target") {
        Get("/find/newbury%2C%20uk") ~> optLocApiRoute ~> check {
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
