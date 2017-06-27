import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

object MessageDispatcher {
  def handleMessage()(implicit materializer: ActorMaterializer, system: ActorSystem): Flow[Message, Message, Any] = {
    Flow[Message].mapConcat {
      case TextMessage.Strict(text) =>
        val messageParts = text.split('_')
        messageParts(0) match {
          case "usermessage" => TextMessage("") :: Nil // TextMessage(chatManagement.foo(messageParts(1)))?
          case "addchannel" => TextMessage("") :: Nil
          case "username" => TextMessage("") :: Nil
          case "joinchannel" => TextMessage("") :: Nil
          case "exitchannel" => TextMessage("") :: Nil
          case _ => TextMessage("") :: Nil // Error?
        }
      case bm: BinaryMessage =>
        bm.dataStream.runWith(Sink.ignore)
        Nil
    }
  }
}
