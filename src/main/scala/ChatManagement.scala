import akka.actor.{Actor, ActorRef}

import scala.collection.immutable.HashMap
import scala.collection.mutable


trait ChatManagement {
  this: Actor =>
  val sessions: HashMap[String, ActorRef]
  val channels: HashMap[String, List[String]]

  protected def chatManagement: Receive = {
    case msg@ChatMessage(from, channel, _) => getSession(from, channel).foreach(_ ! msg)
    case msg@GetChatLog(from, channel) => getSession(from, channel).foreach(_ forward msg)
    case msg@JoinChannel(from, channel) => channels.updated(channel, channels.get(channel) ++ from)
    case msg@LeftChannel(from, channel) => channels.updated(channel, channels.get(channel).filterNot(_ == from))
  }

  private def getSession(from: String, channel: String): List[ActorRef] = {
    if (sessions.contains(from)) {
      val cls = channels.get(channel)
      sessions.filter((t) => cls.contains(t._1)).values.toList
    }
    else {
      EventHandler.info(this, "Session expired for %s".format(from))
      List()
    }
  }
}

