package akka.basic;

import akka.actor.AbstractActor;

/**
 * Fetch from:
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-java
 */
public class Greeter extends AbstractActor {
	/**  */ 
	public static enum Msg {
		GREET, DONE;
	}

	/** mandatory for actor */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.matchEquals(Msg.GREET, m -> {
					System.out.println("Hello Wolrd!");
					sender().tell(Msg.DONE, self());
				})
				.build();
	}

}
