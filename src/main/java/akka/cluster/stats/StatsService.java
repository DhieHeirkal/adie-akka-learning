package akka.cluster.stats;

import akka.cluster.stats.StatsMessages.StatsJob;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import akka.routing.FromConfig;
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 */
public class StatsService extends AbstractActor {
	// This router is used both with lookup and deploy of routees. If you
	// have a router with only lookup of routees you can use Props.empty()
	// instead of Props.create(StatsWorker.class).
	ActorRef workerRouter =
			/** TODO Take focus on this remoting method */
			getContext().actorOf(FromConfig.getInstance().props(Props.create(StatsWorker.class)), "workerRouter");

	/**  */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				/** TODO ??? */
				.match(StatsJob.class, job -> !job.getText().isEmpty(), job -> {
					String[] words = job.getText().split(" ");
					ActorRef replyTo = sender();
					
					// create actor that collects replies from workers
					ActorRef aggregator =
							/** TODO akka.cluster.stats.StatsAggregator */
							getContext().actorOf(Props.create(StatsAggregator.class, words.length, replyTo));
					
					// send each word to a worker
					for (String word : words) {
						/** TODO ??? */
						workerRouter.tell(new ConsistentHashableEnvelope(word, word), aggregator);
					}
				})
				.build();
	}

}
