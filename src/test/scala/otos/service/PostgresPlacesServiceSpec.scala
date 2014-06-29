package otos.service

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}
import otos.util.FixtureLoading

class PostgresPlacesServiceSpec extends TestKit(ActorSystem("PostgresPlacesServiceSpec")) with FunSpecLike with FixtureLoading with Matchers with BeforeAndAfterEach {
  def actorRefFactory = system
  val postgresPlacesServiceActor = system.actorOf(Props[PostgresPlacesServiceActor], "postgres-places-service")
}
