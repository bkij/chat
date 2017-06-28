import akka.actor.Actor

import scala.collection.immutable.HashMap

/**
  * Created by grzegrz on 27.06.17.
  */
trait ChannelsManagement { this: Actor =>
  val channels:HashMap[String,List[String]]
  protected def chatManagement: Receive = {
    case msg @ AddChannel(from,channel, _) => channels.updated(channel,List(from))
    case msg @ RemoveChannel(from,channel) => channels.filterNot(_._1.equals(channel))
  }

}
