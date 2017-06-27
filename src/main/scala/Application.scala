import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import akka.http.scaladsl._
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.StdIn

import scala.io.StdIn

object Application {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val serverConfig = Config()

    val route = path(serverConfig.path) {
      get {
        handleWebSocketMessages(MessageDispatcher.handleMessage())
      }
    }

    val binding = Await.result(Http().bindAndHandle(route, "127.0.0.1", serverConfig.port), 3.seconds)

    println("Started server at localhost:" + serverConfig.port + ", press enter to kill")
    StdIn.readLine()
    system.terminate()
  }
}
