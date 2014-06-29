package otos.service

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import org.scalatest.{FunSpecLike, Matchers}

class PostgresPlacesServiceSpec
  extends TestKit(ActorSystem("PostgresPlacesServiceSpec"))
  with FunSpecLike
  with Matchers {

  def actorRefFactory = system
  val postgresPlacesServiceActor = system.actorOf(Props[PostgresPlacesServiceActor], "postgres-places-service")
}
