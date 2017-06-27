/*
 * TODO: Add more config options
 */
class Config(val port: Int)

object Config {
  def apply(): Config = {
    new Config(8080)
  }

  def fromFile(filename: String): Config = {
    // TODO: implement
  }
}