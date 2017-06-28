package messaging

sealed trait Message
case class ChatMessage(from: String, channel: String, msthng: String) extends Message
case class GetChatLog(from: String, channel: String) extends Message                  // ??
case class JoinChannel(from: String, channel: String) extends Message
case class LeftChannel(from: String, channel: String) extends Message
case class AddChannel(from: String, channel: String, abcd: String) extends Message
case class RemoveChannel(from: String, channel: String) extends Message
case class Login(username: String) extends Message
case class Logout(username:String) extends Message