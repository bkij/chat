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

    val route = get {
      pathSingleSlash {
        getFromResource("index.html")
      } ~
      path("webSocket.js") (getFromResource("webSocket.js")) ~
      path("style.css") (getFromResource("style.css")) ~
      path("chat") {
        cookie("username") { usernameCookie =>
          handleWebSocketMessages(MessageDispatcher.handleMessage(usernameCookie.value))
        }
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
