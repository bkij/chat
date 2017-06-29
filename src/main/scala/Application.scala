import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

object Application {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val serverConfig = Config()

    val route =
      pathSingleSlash {
        getFromResource("index.html")
      } ~
      path("webSocket.js") (getFromResource("webSocket.js")) ~
      path("style.css") (getFromResource("style.css")) ~
      pathPrefix("chat" / IntNumber) { channel =>
        parameter('name) { username =>
          handleWebSocketMessages(MessageDispatcher.handleMessage(username, channel))
        }
      } ~
      path("channels") {
        parameter('name) { username =>
          handleWebSocketMessages(MessageDispatcher.handleChannelMessage(username))
        }
      }

    val binding = Http().bindAndHandle(route, "127.0.0.1", serverConfig.port)

    binding.onComplete {
      case Success(_) =>
        println(s"Server is listening on port ${serverConfig.port}")
      case Failure(_) =>
        println("Couldn't start server")
        system.terminate()
    }
  }
}
