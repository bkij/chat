import akka.actor.Actor
import messaging._

class SessionActor extends Actor {
  override def receive: Receive = {
    case msg @ ChatMessage(from, message, _) =>
      ???
    case msg @ GetChatLog(from, channel) =>
      ???
  }
}
