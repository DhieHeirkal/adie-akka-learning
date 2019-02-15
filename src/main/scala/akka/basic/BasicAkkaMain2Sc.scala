package akka.basic

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Terminated

/**
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-scala
 * 
 * runMain akka.basic.BasicAkkaMain2Sc
 */

object BasicAkkaMain2Sc {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Hello")
    val a = system.actorOf(Props[HelloBasicAkkaSc], "helloworld")
    system.actorOf(Props(classOf[TerminatorSc], a), "terminator")
  }
  
  class TerminatorSc(ref: ActorRef) extends Actor with ActorLogging {
    /** */
    context watch ref
    /**  */
    def receive = {
      case Terminated(_) =>
        log.info("{} has terminated, shutting down system", ref.path)
        context.system.terminate()
    }
  }
}