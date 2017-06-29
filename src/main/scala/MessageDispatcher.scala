import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.collection.mutable

object MessageDispatcher {
  val channels = new mutable.HashMap[Int, Channel]
  def handleMessage(username: String, channelNum: Int)(implicit materializer: ActorMaterializer, system: ActorSystem): Flow[Message, Message, _] = {
    val channel = channels.getOrElse(channelNum, throw new IllegalArgumentException(""))
    Flow[Message]
      .collect {
        case TextMessage.Strict(text) => text
      }
      .via(channel.chatFlow(username))
      .map {
        case msg: messaging.Message =>
          TextMessage.Strict(toJson(msg))
      }
  }

  def handleChannelMessage(username: String)(implicit materializer: ActorMaterializer, system: ActorSystem): Flow[Message, Message, _] = {
    Flow[Message]
    .collect {
      case TextMessage.Strict(text) => {
        val messageParts = text.split('_')
        messageParts(0) match {
          case "addchannel" =>
            channels.put(messageParts(1).toInt, new Channel())
          case "joinchannel" =>
            Nil                 // ??
          case "leftchannel" =>
            Nil                 // ??
        }
      }
    }
    .map {
      case msg: Any =>
        TextMessage.Strict("")  // ??
    }
  }

  def toJson(msg: messaging.Message): String = {
    msg.toString // TODO
  }
}
