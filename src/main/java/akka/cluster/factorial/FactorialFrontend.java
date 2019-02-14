package akka.cluster.factorial;

import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import scala.concurrent.duration.Duration;

public class FactorialFrontend extends AbstractActor {
	final int upToN;						// TODO ???
	final boolean repeat;					// ???
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	// 
	ActorRef backend = getContext().actorOf(FromConfig.getInstance().props(), "factorialBackendRouter");
	
	public FactorialFrontend(int upToN, boolean repeat) {
		this.upToN = upToN;
		this.repeat = repeat;
	}

	/**  */
	@Override
	public void preStart() throws Exception {
		sendJobs();
		getContext().setReceiveTimeout(Duration.create(10, TimeUnit.SECONDS));
	}

	/** mandatory method for actor */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(FactorialResult.class, result -> {
					if (result.n == upToN) {
						log.debug("{}! = {}", result.n, result.factorial);
						if (repeat) sendJobs();
						else getContext().stop(self()); 
					}
				})
				.match(ReceiveTimeout.class, message -> {
					log.info("Timeout");
					sendJobs();
				})
				.build();
	}
	
	/** TODO ??? */
	void sendJobs() {
		log.info("Starting batch of factorial up to [{}]", upToN);
		for (int n = 1; n <= upToN; n++) {
			/** TODO backend as ActorRef */ 
			backend.tell(n, self());
		}
	}

}
