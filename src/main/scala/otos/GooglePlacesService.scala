package otos

import akka.actor.Actor

case class LatLong(latitude: Double, longitude: Double)
case class Location(id: String, latlong: LatLong)

case class FindLocation(id: String)

class GooglePlacesService extends Actor {
  def receive = {
    // TODO: Query Google Places
    // TODO: Decide on strategy for handling places with multiple results
    case locationSearch: String => sender ! new Location(locationSearch, new LatLong(12.34, 56.78))
  }
}
