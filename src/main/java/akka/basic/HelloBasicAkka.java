package akka.basic;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import static akka.basic.Greeter.Msg;

/**
 * Fetch from:
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-java
 */
public class HelloBasicAkka extends AbstractActor {

	/** Mandatory method of actor class */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.matchEquals(Msg.DONE, m -> {
					/** when the greeter is done, stop this actor and with it the application */
					getContext().stop(self());
				})
				.build();
	}

	/**  */
	@Override
	public void preStart() throws Exception {
		/** create the greeter actor */
		final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
		/** tell it to perform the greeting */
		greeter.tell(Msg.GREET, self());
	}

}
