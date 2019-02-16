package akka.cluster.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import scala.concurrent.duration.Duration;

import akka.cluster.stats.StatsMessages.JobFailed;
import akka.cluster.stats.StatsMessages.StatsResult;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 */
public class StatsAggregator extends AbstractActor {
	final int expectedResults;				/** TODO ?? */
	final ActorRef replyTo;					/** TODO ?? */
	
	final List<Integer> results = new ArrayList<>();
	
	public StatsAggregator(int expectedResults, ActorRef replyTo) {
		this.expectedResults = expectedResults;
		this.replyTo = replyTo;
	}
	
	@Override
	public void preStart() {
		/** Remoting timeout 3 seconds */
		getContext().setReceiveTimeout(Duration.create(3, TimeUnit.SECONDS));
	}

	/**  */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Integer.class, wordCount -> {
					// add results int
					results.add(wordCount);
					// is result achieve the expectation
					if (results.size() == expectedResults) {
						// count the result
						int sum = 0;
						for (int c : results) sum += c;
//						int sum = results.size();		// TODO Check
						
						double meanWordLength = ((double) sum) / results.size();
						replyTo.tell(new StatsResult(meanWordLength), self());
						getContext().stop(self());
					}
				})
				.match(ReceiveTimeout.class, x -> {
					replyTo.tell(new JobFailed("Service unavailable, try again later"), self());
					getContext().stop(self());
				})
				.build();
	}

}
