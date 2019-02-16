package akka.cluster.simple;

import akka.actor.AbstractActor;

import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;

import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Source code: https://developer.lightbend.com/start/?group=akka&project=akka-samples-cluster-java
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html
 */
public class SimpleClusterListener extends AbstractActor {
	// 
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	// Get actor system from cluster
	Cluster cluster = Cluster.get(getContext().system()); 
	
	/** subscribe to cluster changes */
	@Override
	public void preStart() throws Exception {
		cluster.subscribe(self(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
	}
	

	/** Overriding mandatory method of actor
	 *  Check at SimpleClusterListener2 */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(MemberUp.class, mUp -> {
					log.info("Member is Up: {}", mUp.member());
				})
				.match(UnreachableMember.class, mUnreachable -> {
					log.info("Member detected as unreachable: {}", mUnreachable.member());
				})
				.match(MemberRemoved.class, mRemoved -> {
					log.info("Member is Removed: {}", mRemoved.member());
				})
				.match(MemberEvent.class, message -> {
					// ignore
				})
				.build();
	}

	/** re-subscribe when restart */
	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(self()); 
	}
}
