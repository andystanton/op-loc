import otos._
import org.scalatra._
import javax.servlet.ServletContext
import _root_.akka.actor.{ActorSystem, Props}

class ScalatraBootstrap extends LifeCycle {
  implicit val system = ActorSystem()

  override def init(context: ServletContext) {
    context.mount(new OptLocApi, "/*")
  }

  override def destroy(context:ServletContext) {
    system.shutdown()
  }
}
