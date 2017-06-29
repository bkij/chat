import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import messaging.ChatMessage
import play.api.libs.json.Json

import scala.collection.mutable
import scala.util.parsing.json.JSONObject

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

  def handleChannelMessage()(implicit materializer: ActorMaterializer, system: ActorSystem): Flow[Message, Message, _] = {
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
          case "getchannels" =>
              JSONObject(Map("channelList" -> channels.keySet)).toString()
        }
      }
    }
    .map {
      case msg: Any =>
        TextMessage.Strict(Json.toJson(Map("channelList" -> channels.keySet)).toString())  // ??
    }
  }

  def toJson(msg: messaging.Message): String = {
    msg match {
      case textmessage @ ChatMessage(user, content) =>
        "{" + "\"purpose\": \"message\", " + "\"userMessage\": " + "\"" + wrapHtml(user, content) + "\"" + "}"
    }
  }

  def wrapHtml(sender: String, content: String) = {
    s"<article><b>$sender says:</b> <p>$content</p></article>"
  }
}
