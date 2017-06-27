import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.io.StdIn

object Application {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val serverConfig = Config()

    println("Started server at localhost:" + serverConfig.port + ", press enter to kill")
    StdIn.readLine()
    system.terminate()
  }
}
