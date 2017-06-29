package messaging

import akka.actor.ActorRef

sealed trait Message
object Statement{
  def apply(content:String)= ChatMessage("info",content)
}
case class ChatMessage(from: String, content:String) extends Message
case class JoinChannel(from: String,actor:ActorRef) extends Message
case class LeftChannel(from: String) extends Message
case class AddChannel(channel: String) extends Message
case class RemoveChannel(channel: String) extends Message
case class Login(from: String) extends Message