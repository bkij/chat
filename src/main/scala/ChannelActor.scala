import akka.actor.{Actor, ActorRef}


class ChannelActor extends Actor {
  var sessions: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case UserJoined(name, actorRef) =>
      sessions += name -> actorRef
      broadcast(Statement("User $name joined channel"))
    case UserLeft(name) =>
      sessions -= name
      broadcast(Statement("User $name left channel"))
    case msg: IncMessage =>
      broadcast(msg)
  }

  def broadcast(message: Event): Unit = sessions.values.foreach(_ ! message)
}
