import akka.actor.Actor

import scala.collection.immutable.HashMap

/**
  * Created by grzegrz on 27.06.17.
  */
trait ChannelsManagement { this: Actor =>
  val channels:HashMap[String,List[String]]
  protected def chatManagement: Receive = {
    case msg @ AddChannel(from,channel, _) => getSession(from,channel).foreach(_ ! msg)
    case msg @ RemoveChannel(from,channel) =>     getSession(from,channel).foreach(_ forward msg)
  }

}
