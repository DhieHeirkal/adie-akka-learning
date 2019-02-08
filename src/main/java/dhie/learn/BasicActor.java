package dhie.learn;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * https://doc.akka.io/docs/akka/2.5.20/actors.html#introduction
 * &
 * https://doc.akka.io/docs/akka/2.4/java/untyped-actors.html#untyped-actors-java
 */
public class BasicActor extends AbstractActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(String.class, s -> {
									log.info("Received String message: {}", s);
								})
							   .matchAny(o -> log.info("received unknown message"))
							   .build();
	}
	
	public static void main(String[] args) {
		Props props1 = Props.create(BasicActor.class);
//		Props props2 = Props.create(ActorWithArgs.class, () -> new ActorWithArgs("arg"));
//		Props props3 = Props.create(ActorWithArgs.class, "arg");
	}
}
