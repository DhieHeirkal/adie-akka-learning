package akka.basic

import akka.actor.Actor
import akka.actor.Props

/**
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-scala
 */
class HelloBasicAkkaSc extends Actor {

  /** */
  override def preStart(): Unit = {
    // create the greeter actor
    val greeter = context.actorOf(Props[GreeterSc], "greeter")
    // tell it to perform the greeting
    greeter ! GreeterSc.Greet
  }
  
  /** */
  def receive = {
    // when the greeter is done, stop this actor and with it the application
    case GreeterSc.Done => context.stop(self)
  }
  
}