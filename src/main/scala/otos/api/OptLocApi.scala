package otos.api

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import akka.actor.{Actor, ActorRef}
import otos.service._
import spray.http.{StatusCodes, MediaTypes, ContentTypeRange, ContentType}
import spray.http.ContentTypeRange._
import spray.http.HttpHeaders.{RawHeader, `Content-Type`}
import spray.httpx.Json4sSupport
import spray.routing._

class OptLocApiActor(val googlePlacesServiceActor: ActorRef, val postgresPlacesServiceActor: ActorRef) extends Actor with OptLocApi {
  def actorRefFactory = context
  def receive = runRoute(optLocApiRoute)
}

trait OptLocApi extends HttpService with Json4sSupport {
  import akka.pattern.ask
  import akka.util.Timeout
  import org.json4s._
  import org.json4s.native.Serialization

  import scala.concurrent.Await
  import scala.concurrent.duration._
  import scala.language.postfixOps

  implicit val json4sFormats = Serialization.formats(NoTypeHints)
  implicit val timeout = Timeout(10 seconds)

  implicit def googlePlacesServiceActor: ActorRef
  implicit def postgresPlacesServiceActor: ActorRef

  val optLocApiRoute = {
    path("ping") {
      get {
        complete {
          Map("code" -> 200)
        }
      }
    } ~
    path("googleplaces" / """\w+""".r) { locationSearch =>
      get {
        complete {
          Await.result(googlePlacesServiceActor ? locationSearch, timeout.duration).asInstanceOf[Location]
        }
      }
    } ~
    pathPrefix("find") {
      path("id" / """\d+""".r) { id =>
        get {
          complete {
            Await.result(postgresPlacesServiceActor ? IdRequest(id.toInt), timeout.duration).asInstanceOf[Location]
          }
        }
      } ~
        path("name" / """[\w\s]+""".r) { name =>
          get {
            complete {
              Await.result(postgresPlacesServiceActor ? NameRequest(name), timeout.duration).asInstanceOf[Seq[Location]]
            }
          }
        } ~
        path("near" / """\d+""".r) { id =>
          get {
            parameters("range" ? 10, "minpop" ? 1000, "maxpop" ?).as(NearParams) { nearParams =>
              complete {
                Await.result(postgresPlacesServiceActor ? NearRequest(id.toInt, nearParams), timeout.duration).asInstanceOf[Seq[Location]]
              }
            }
          }
        }
    } ~
    pathPrefix("") {
      get {
        getFromResourceDirectory("WEB-INF")
      }
    } ~
    pathSingleSlash {
      get {
        getFromResource("WEB-INF/index.html")
      }
    }
  }
}
