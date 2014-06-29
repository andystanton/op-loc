package otos.service

import akka.actor.{Props, ActorSystem}
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterEach, Matchers, FunSpecLike}
import otos.api.OptLocApi
import otos.util.FixtureLoading

class PostgresPlacesServiceSpec extends TestKit(ActorSystem("PostgresPlacesServiceSpec")) with FunSpecLike with OptLocApi with FixtureLoading with Matchers with BeforeAndAfterEach {
  def actorRefFactory = system
  val placesServiceActor = system.actorOf(Props[PostgresPlacesServiceActor], "postgres-places-service")

}
