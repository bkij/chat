import akka.actor.{Actor, ActorRef}

class ChatServer extends Actor with ChatManagement with ChannelsManagement with SessionManagement {
  override def receive: Receive = chatManagement orElse channelManagement orElse sessionManagement
}
