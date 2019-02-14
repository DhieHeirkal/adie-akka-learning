package akka.sample;

import java.io.IOException;

import akka.sample.Greeter.Greet;
import akka.sample.Greeter.WhoToGreet;
import akka.sample.Printer.Greeting;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * https://developer.lightbend.com/guides/akka-quickstart-java/
 * https://developer.lightbend.com/guides/akka-quickstart-scala/define-actors.html
 * 
 * https://doc.akka.io/docs/akka/2.5.20/actors.html#introduction
 * 
 * General Doc about actor: https://doc.akka.io/docs/akka/2.5.20/general/actor-systems.html
 * 							https://github.com/akka/akka/blob/v2.5.20/akka-docs/src/main/paradox/actors.md
 */
public class AkkaQuickstart {
	 
  public static void main(String[] args) {
    final ActorSystem system = ActorSystem.create("helloakka");
    
    try {
      //#create-actors
      final ActorRef printerActor = system.actorOf(Printer.props(), "printerActor");
      final ActorRef howdyGreeter = system.actorOf(Greeter.props("Howdy", printerActor), "howdyGreeter");
      final ActorRef helloGreeter = system.actorOf(Greeter.props("Hello", printerActor), "helloGreeter");
      final ActorRef goodDayGreeter = system.actorOf(Greeter.props("Good day", printerActor), "goodDayGreeter");

      /** main-send-messages */      
      printerActor.tell(new Greeting("From Printer Actor"), ActorRef.noSender());
      
      howdyGreeter.tell(new WhoToGreet("Akka"), ActorRef.noSender());
      howdyGreeter.tell(new Greet(), ActorRef.noSender());

      howdyGreeter.tell(new WhoToGreet("Lightbend"), ActorRef.noSender());
      howdyGreeter.tell(new Greet(), ActorRef.noSender());

      helloGreeter.tell(new WhoToGreet("Java"), ActorRef.noSender());
      helloGreeter.tell(new Greet(), ActorRef.noSender());

      goodDayGreeter.tell(new WhoToGreet("Play"), ActorRef.noSender());
      goodDayGreeter.tell(new Greet(), ActorRef.noSender());

      System.out.println(">>> Press ENTER to exit <<<");
      System.in.read();
    } catch (IOException ioe) {
    } finally { system.terminate(); }
    
  }
}
