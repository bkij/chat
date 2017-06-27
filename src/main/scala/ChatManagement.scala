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
  }

  private def getSession(from: String,channel:String) : List[ActorRef] = {
    if (sessions.contains(from)) {
      val cls = channels.get(channel)
      for(cl <-cls){
        sessions.get(cl).collect
      }
    }
    else {
      EventHandler.info(this, "Session expired for %s".format(from))
      None
    }
  }
}

