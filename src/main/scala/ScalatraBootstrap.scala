import otos._
import org.scalatra._
import javax.servlet.ServletContext
import _root_.akka.actor.{ActorSystem, Props}

class ScalatraBootstrap extends LifeCycle {
  val system = ActorSystem()
  val myActor = system.actorOf(Props[RunLoop])

  override def init(context: ServletContext) {
    context.mount(new OpLocApi, "/*")
  }

  override def destroy(context:ServletContext) {
    system.shutdown()
  }
}
