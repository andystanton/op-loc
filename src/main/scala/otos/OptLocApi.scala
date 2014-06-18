package otos

import akka.actor._
import org.scalatra._

class SomeActor extends Actor {
    def receive = {
        case _ => println("some actor received message")
    }
}

class OptLocApi()(implicit val system: ActorSystem) extends ScalatraServlet {

  get("/") {
    <html>
      <body>
        <h1>Optimum Locum</h1>
      </body>
    </html>
  }

}
