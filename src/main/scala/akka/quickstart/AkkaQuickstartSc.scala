package akka.quickstart

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }

/********************************************************************************
 * https://developer.lightbend.com/guides/akka-quickstart-scala/full-example.html
 ********************************************************************************/

/** */
object GreeterSc {
  //
  def props(message: String, printerActor: ActorRef): Props = Props(new GreeterSc(message, printerActor))
  
  final case class WhoToGreetSc(who: String)     // WhoToGreet: recipient of the greeting
  case object GreetSc                            // Greet: instruction to execute greeting
}
/** */
class GreeterSc(message: String, printerActor: ActorRef) extends Actor {
  // importing object GreeterSc & PrinterSc
  import GreeterSc._
  import PrinterSc._
  
  var greeting = ""                            // 
  
  /** Overriding mandatory method of actor */
  def receive = {
    case WhoToGreetSc(who) =>
      greeting = message + ", " + who
    case GreetSc           =>
      printerActor ! GreetingSc(greeting)        // print out the greeting message using class Greeting 
  }
}

/** */
object PrinterSc {  
  def props: Props = Props[PrinterSc]            // class PrinterSc as props  
  final case class GreetingSc(greeting: String)  // Greeting: message containing the greeting
}
/** */
class PrinterSc extends Actor with ActorLogging {
  import PrinterSc._                          // import object PrinterSc
  
  /** Overriding mandatory method of actor */
  def receive = {
    case GreetingSc(greeting) =>
      log.info("Greeting received (from " + sender() +"): " + greeting)
  }
}

/** */
object AkkaQuickstartSc extends App {
  import GreeterSc._                           // import object GreeterSc
  
  // Create the 'helloAkka' actor system
  val system: ActorSystem = ActorSystem("helloAkka")
  
  // Create the printer actor
  val printer: ActorRef = system.actorOf(PrinterSc.props, "printerActor")

  // Create the 'greeter' actors
  val howdyGreeter: ActorRef = system.actorOf(GreeterSc.props("Howdy", printer), "howdyGreeter")
  val helloGreeter: ActorRef = system.actorOf(GreeterSc.props("Hello", printer), "helloGreeter")
  val goodDayGreeter: ActorRef = system.actorOf(GreeterSc.props("Good Day", printer), "goodDayGreeter")
  
  howdyGreeter ! WhoToGreetSc("Akka")
  howdyGreeter ! GreetSc

  howdyGreeter ! WhoToGreetSc("Lightbend")
  howdyGreeter ! GreetSc

  helloGreeter ! WhoToGreetSc("Scala")
  helloGreeter ! GreetSc

  goodDayGreeter ! WhoToGreetSc("Play")
  goodDayGreeter ! GreetSc
  
//  println(">>> Press ENTER to exit <<<")
}