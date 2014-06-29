package otos.service

import java.sql.DriverManager

import akka.actor.Actor
import com.typesafe.config.ConfigFactory

case class FindRequest(locationSearch: String)
case class NearRequest(locationSearch: String, range: Int)

class PostgresPlacesServiceActor extends Actor with PostgresPlacesService {
  val config = ConfigFactory.load("opt-loc.properties")

  implicit val jdbcUrl = config.getString("javax.persistence.jdbc.url")
  implicit val jdbcUser = config.getString("javax.persistence.jdbc.user")
  implicit val jdbcPassword = config.getString("javax.persistence.jdbc.password")

  def receive = {
    case FindRequest(locationSearch) =>
      sender ! findLocation(locationSearch)
    case NearRequest(locationSearch, range) =>
      sender ! findNear(locationSearch, range)
  }
}

trait PostgresPlacesService {
  implicit def jdbcUrl: String
  implicit def jdbcUser: String
  implicit def jdbcPassword: String

  lazy val databaseConnection = {
    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)
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

  def findNear(locationSearch: String, locationDistance: Int) = {
    val location = findLocation(locationSearch)
    val query =
      s"""|SELECT
          |  name,
          |  ST_X(geom) as latitude,
          |  ST_Y(geom) as longitude
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
        Location(resultSet.getString("name"), LatLong(resultSet.getDouble("latitude"), resultSet.getDouble("longitude")))
    }.toList
  }
}

class PostgresPlacesServiceThing {
  val config = ConfigFactory.load("opt-loc.properties")

  lazy val databaseConnection = {
    Class.forName("org.postgresql.Driver")
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
