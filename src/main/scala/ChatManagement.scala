import akka.actor.{Actor, ActorRef}

import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * Created by grzegrz on 27.06.17.
  */
trait ChatManagement   { this:  Actor =>
  val sessions:HashMap[String,ActorRef]
  val channels:HashMap[String,List[String]]
  protected def chatManagement: Receive = {
    case msg @ ChatMessage(from,channel, _) => getSession(from,channel).foreach(_ ! msg)
    case msg @ GetChatLog(from,channel) =>     getSession(from,channel).foreach(_ forward msg)
    case msg @ JoinChannel(from,channel) => channels.
  }

  private def getSession(from: String,channel:String) : List[ActorRef] = {
    if (sessions.contains(from)) {
      sessions.filterKeys(p=>channels.get(channel).contains(p)).values.toList
    }
    else {
      EventHandler.info(this, "Session expired for %s".format(from))
      List()
    }
  }
}

