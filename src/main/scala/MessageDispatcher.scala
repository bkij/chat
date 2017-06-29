import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

object MessageDispatcher {
  val chatServer = new ChatServer()
  def handleMessage(username: String)(implicit materializer: ActorMaterializer, system: ActorSystem): Flow[Message, Message, Any] = {
    Flow[Message].collect {
      case msg @ TextMessage.Strict(text) =>
        val messageParts = text.split('_')
        messageParts(0) match {
          case "usermessage" =>  chatServer ! messaging.ChatMessage(username, messageParts(1), _)
          case "addchannel" => chatServer ! messaging.AddChannel(username, messageParts(1), _)
          case "username" => chatServer ! messaging.Login(messageParts(1))
          case "joinchannel" => chatServer ! messaging.JoinChannel(username, messageParts(1))
          case "exitchannel" => chatServer ! messaging.LeftChannel(username, messageParts(1))
          case _ => Nil
        }
      case bm: BinaryMessage =>
        bm.dataStream.runWith(Sink.ignore)
        Nil
      case otherMessage: TextMessage.Streamed =>
        otherMessage.textStream.runWith(Sink.ignore)
        Nil
    }
    .map {
      case msg: messaging.Message =>
        /*
         * TODO: implement toJson
         */
        TextMessage.Strict(toJson(msg))
    }
  }
}
