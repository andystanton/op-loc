package otos.service

import java.sql.DriverManager
import com.typesafe.config.ConfigFactory

class PostgresPlacesServices {
  val config = ConfigFactory.load("opt-loc.properties")

  lazy val databaseConnection = {
    val jdbcUrl = config.getString("javax.persistence.jdbc.url")
    val user = config.getString("javax.persistence.jdbc.user")
    val password = config.getString("javax.persistence.jdbc.password")
    DriverManager.getConnection(jdbcUrl, user, password)
  }

  def findLocation(locationSearch: String) = {
    val query =
      s"""|SELECT
          |  name,
          |  ST_X(geom) as latitude,
          |  ST_Y(geom) as longitude
          |FROM
          |  places_gb
          |WHERE
          |  feature_class='P'
          |  AND name ILIKE '%$locationSearch%'
          |ORDER BY
          |  population DESC
          |LIMIT 1;
          |""".stripMargin

    val stmt = databaseConnection.prepareStatement(query)
    val resultSet = stmt.executeQuery()
    resultSet.next

    Location(resultSet.getString("name"), LatLong(resultSet.getDouble("latitude"), resultSet.getDouble("longitude")))
  }

  def findStuffNear(locationSearch: String, locationDistance: Int) = {
    val location = findLocation(locationSearch)
    val query =
      s"""|SELECT
          |  name
          |FROM
          |  places_gb
          |WHERE
          |  name != '$locationSearch'
          |  AND feature_class='P'
          |  AND ST_Distance_Sphere(geom, ST_MakePoint(${location.latlong.latitude}, ${location.latlong.longitude})) <= $locationDistance
          |ORDER BY
          |  name ASC
          |""".stripMargin
    val stmt = databaseConnection.prepareStatement(query)
    val resultSet = stmt.executeQuery()
    Iterator.continually(resultSet.next).takeWhile(_ == true).map{
      _ =>
        resultSet.getString("name")
    }.toList
  }
}
