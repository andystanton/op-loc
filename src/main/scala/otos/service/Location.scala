package otos.service

case class LatLong(latitude: Double, longitude: Double)
case class Location(id: String, latlong: LatLong)