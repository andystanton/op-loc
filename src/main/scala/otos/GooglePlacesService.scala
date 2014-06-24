package otos

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import spray.client.pipelining._
import spray.http._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class LatLong(latitude: Double, longitude: Double)
case class Location(id: String, latlong: LatLong)

class GooglePlacesService extends Actor {
  val config = ConfigFactory.load("opt-loc.properties")
  val apiKey = config.getString("google.places.api.key")
  val apiUrl = config.getString("google.places.api.url")

  def receive = {
    case locationSearch: String =>
      val requestSender = sender()
      queryPlacesApi(locationSearch) map { response =>
        requestSender ! jsonToLocation(response.entity.asString)
      }
  }

  def queryPlacesApi(searchKey: String): Future[HttpResponse] = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    pipeline(Get(apiUrl.replace("$apiKey", apiKey).replace("$searchKey", searchKey)))
  }

  def jsonToLocation(placesJson: String): Location = {
    import org.json4s._
    import org.json4s.native.JsonMethods._

    val locations: Seq[Location] = for {
      JArray(results) <- parse(placesJson) \ "results"
      JObject(result) <- results
      JField("formatted_address", JString(address)) <- result
      JField("geometry", JObject(geometry)) <- result
      JField("location", JObject(location)) <- geometry
      JField("lat", JDouble(latitude)) <- location
      JField("lng", JDouble(longitude)) <- location
    } yield Location(address, LatLong(latitude, longitude))

    locations(0)
  }
}
