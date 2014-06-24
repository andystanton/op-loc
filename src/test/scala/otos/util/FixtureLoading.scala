package otos.util

trait FixtureLoading {
  def loadFixture(filename: String): Option[String] = {
    for {
      resource <- Option(getClass.getResourceAsStream(filename))
      bufferedResource <- Option(io.Source.fromInputStream(resource))
      content <- Option(bufferedResource.mkString)
    } yield content
  }
}