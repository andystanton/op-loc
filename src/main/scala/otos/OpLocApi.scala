package otos

import org.scalatra._

class OpLocApi extends ScalatraServlet {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }

}
