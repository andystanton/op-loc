package otos

import org.scalatra._

class OpLocApi extends ScalatraServlet {

  get("/") {
    <html>
      <body>
        <h1>Optimum Locum</h1>
      </body>
    </html>
  }

}
