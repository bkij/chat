import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import messaging._

import scala.collection.mutable
trait SessionManagement extends Actor {
  val sessions = mutable.HashMap[String, ActorRef]()

  protected def sessionManagement(implicit system: ActorSystem): Receive = {
    case Login(username) =>
      val sessionActor = system.actorOf(Props[SessionActor])
      sessions.put(username, sessionActor)
    case Logout(username) =>
      sessions.remove(username)
  }
}
