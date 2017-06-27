/*
 * TODO: Add more config options
 */
class Config(val port: Int, val path: String)

object Config {
  def apply(): Config = {
    new Config(8080, "chat")
  }

  def fromFile(filename: String): Config = {
    // TODO: implement
    new Config(0, "")
  }
}