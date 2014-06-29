package otos.service

case class LatLong(latitude: Double, longitude: Double)
case class Location(id: Int, name: String, latlong: LatLong)