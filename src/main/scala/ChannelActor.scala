import akka.actor.{Actor, ActorRef}
import messaging._


class ChannelActor extends Actor {
  var sessions: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case JoinChannel(name, actorRef) =>
      sessions += name -> actorRef
      broadcast(Statement("User $name joined channel"))
    case LeftChannel(name) =>
      sessions -= name
      broadcast(Statement("User $name left channel"))
    case msg: ChatMessage =>
      broadcast(msg)
  }

  def broadcast(message: ChatMessage): Unit = sessions.values.foreach(_ ! message)
}
