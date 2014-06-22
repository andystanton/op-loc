package otos

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging

case class LatLong(val latitude: Double, val longitude: Double)
case class Location(val id: String, val latlong: LatLong)

case class FindLocation(val id: String)

class GooglePlacesService extends Actor {
  def receive = {
    case locationSearch: String => {
      println(s"got here with $locationSearch")
      sender ! new Location(locationSearch, new LatLong(12.34, 56.78))
    }
  }
}
