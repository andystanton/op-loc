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

  def doThing = {
    val query = "SELECT name FROM places_gb WHERE ST_Distance_Sphere(lat_long, ST_MakePoint(51.401409,-1.3231139)) <= 10000"
    val stmt = databaseConnection.prepareStatement(query)
    val resultSet = stmt.executeQuery()
    Iterator.continually(resultSet.next).takeWhile(_ == true).map{
      _ =>
        resultSet.getString("name")
    }.toList
  }
}
