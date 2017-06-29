import akka.actor.ActorRef



object Statement{
  def apply(content:String)= ChatMessage("info",content)
}

sealed trait Event

case class UserJoined(name:String,actor:ActorRef) extends Event

case class UserLeft(name:String) extends Event

case class IncMessage(from:String, content:String) extends Event


case class ChatMessage(from:String,content:String) extends Event