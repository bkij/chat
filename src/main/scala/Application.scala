import akka.actor.ActorSystem

object Application {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
  }
}
