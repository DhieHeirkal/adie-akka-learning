package akka.quickstart;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * printer-messages
 */
public class Printer extends AbstractActor {

  static public Props props() {
    return Props.create(Printer.class, () -> new Printer());
  }

  // printer-messages
  static public class Greeting {
    public final String message;
    public Greeting(String message) {
      this.message = message;
    }
  }

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public Printer() { }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(Greeting.class, greeting -> {
            log.info(greeting.message);
        })
        .build();
  }
}
