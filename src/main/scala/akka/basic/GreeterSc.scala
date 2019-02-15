package akka.basic

import akka.actor.Actor

/**
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-scala */

object GreeterSc {
  case object Greet
  case object Done
}

class GreeterSc extends Actor {
  /** */
  def receive = {
    case GreeterSc.Greet =>
      println("Hello World!")
      sender() ! GreeterSc.Done
  }
}