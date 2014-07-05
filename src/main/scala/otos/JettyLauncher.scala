package otos

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.ServerConnector
import spray.servlet.Servlet30ConnectorServlet
import spray.servlet.Initializer
import org.eclipse.jetty.servlet.ServletContextHandler

object JettyLauncher {
  def main(args: Array[String]) {
    val server = new Server()
    val connector = new ServerConnector(server);
    connector.setPort(8080);

    val context = new ServletContextHandler(ServletContextHandler.SESSIONS);

    context.setContextPath("/");
    server.setHandler(context);
    context.addEventListener(new Initializer());

    val servletHolder = context.addServlet(classOf[Servlet30ConnectorServlet].getName(), "/*");
    server.setConnectors(Array(connector));

    server.start
    server.join
  }
}