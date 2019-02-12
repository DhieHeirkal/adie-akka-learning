package akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import akka.sample.Printer.Greeting;

/**
 * https://doc.akka.io/docs/akka/2.5.20/actors.html#introduction
 * 
 * General Doc about actor: https://doc.akka.io/docs/akka/2.5.20/general/actor-systems.html
 */
public class Greeter extends AbstractActor {
  private final String message;
  private final ActorRef printerActor;
  private String greeting = "";

  public Greeter(String message, ActorRef printerActor) {
    this.message = message;
    this.printerActor = printerActor;
  }

  /**
   * Inital behavior the Greeter as AbstractActor */
  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(WhoToGreet.class, whotogreet -> {
          this.greeting = message + ", " + whotogreet.who;
        })
        .match(Greet.class, x -> {
          //#greeter-send-message
          printerActor.tell(new Greeting(greeting), getSelf());
        })
        .build();
  }
  

  /** greeter-messages */
  static public Props props(String message, ActorRef printerActor) {
    return Props.create(Greeter.class, () -> new Greeter(message, printerActor));
  }

  /** greeter-messages */
  static public class WhoToGreet {
    public final String who;

    public WhoToGreet(String who) {
        this.who = who;
    }
  }

  static public class Greet {
    public Greet() { }
  }
}
