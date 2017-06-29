import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import messaging._

class Channel(implicit system: ActorSystem) {
  val actor = system.actorOf(Props[ChannelActor])

  def chatFlow(user: String): Flow[String, messaging.Message, _] = {
    Flow.fromSinkAndSource(
      Flow[String]
        .map { msg => {
            val messageParts = msg.split('_')
            messageParts(0) match {
              case "username" =>
                messaging.Login(user)
              case "usermessage" =>
                messaging.ChatMessage(user, messageParts(1))
            }
          }
        }
        .to(Sink.actorRef[messaging.Message](actor, messaging.LeftChannel(user))),
      Source.actorRef[messaging.Message](5, OverflowStrategy.fail).mapMaterializedValue(actor ! messaging.JoinChannel(user, _))
    )
  }
}
