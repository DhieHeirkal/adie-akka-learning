package akka.basic;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;

/**
 * Fetch from:
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-java
 * 
 * runMain akka.basic.BasicAkkaMain2
 */
public class BasicAkkaMain2 {
	public static void main(String[] args) {
		/** */
		ActorSystem system = ActorSystem.create("Hello");
		ActorRef a = system.actorOf(Props.create(HelloBasicAkka.class), "helloWorld");
		system.actorOf(Props.create(Terminator.class, a), "terminator");
	}
	
	/**
	 * Also check: akka.Main.Terminator
	 */
	public static class Terminator extends AbstractLoggingActor {
		/**  */
		private final ActorRef ref;
		
		public Terminator(ActorRef ref) {
			this.ref = ref;
			getContext().watch(ref);
		}

		/** mandatory actor func */
		@Override
		public Receive createReceive() {
			return receiveBuilder()
					.match(Terminated.class, t -> {
						log().info("{} has terminated, shutting down system", ref.path());
						getContext().system().terminate();
					})
					.build();
		}
		
	}
}
