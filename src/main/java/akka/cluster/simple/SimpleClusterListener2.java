package akka.cluster.simple;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SimpleClusterListener2 extends AbstractActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Cluster cluster = Cluster.get(getContext().system());

	/** subscribe to cluster changes, without ClusterEvent */
	@Override
	public void preStart() throws Exception {
		cluster.subscribe(self(), MemberEvent.class, UnreachableMember.class);
	}
	
	/** re-subscribe when restart */
	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(self());
	}

	/** Overriding mandatory method of actor
	 *  Check at SimpleClusterListener */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(CurrentClusterState.class, state -> {
					log.info("Current members: {}", state.members());
				})
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

}
