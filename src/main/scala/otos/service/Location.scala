package otos.service

case class Center(latitude: Double, longitude: Double)
case class Location(id: Int, name: String, center: Center, population: Long)