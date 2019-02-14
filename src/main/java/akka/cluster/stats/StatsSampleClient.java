package akka.cluster.stats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Address;
import akka.actor.Cancellable;

import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.ReachabilityEvent;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.ClusterEvent.ReachableMember;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.CurrentClusterState;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import akka.cluster.stats.StatsMessages.JobFailed;
import akka.cluster.stats.StatsMessages.StatsJob;
import akka.cluster.stats.StatsMessages.StatsResult;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 */
public class StatsSampleClient extends AbstractActor {
	final String servicePath;							/** TODO ??? */
	final Cancellable tickTask;							/** TODO ??? */
	final Set<Address> nodes = new HashSet<Address>();	/** TODO ??? */
	
	/** TODO ??? */
	Cluster cluster = Cluster.get( getContext().system() );
	
	/** TODO ??? */
	public StatsSampleClient(String servicePath) {
		this.servicePath = servicePath;
		FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
		this.tickTask = getContext().system().scheduler().schedule(interval, interval, self(), "tick", getContext().dispatcher(), null);
	}

	/** TODO ??? */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				/** TODO ??? */
				.matchEquals("tick", t -> !nodes.isEmpty(), t -> {
					/** Just pick any one */
					List<Address> nodeList = new ArrayList<>(nodes);
					Address address = nodeList.get( ThreadLocalRandom.current().nextInt(nodeList.size()) );
					ActorSelection service = getContext().actorSelection(address + servicePath);
					service.tell(new StatsJob("this is the text that will be analyzed"), self());
				})
				// see the other way to syso
				.match(StatsResult.class, System.out::println)
				.match(JobFailed.class, System.out::println)
				/** TODO ?? */
				.match(CurrentClusterState.class, state -> {
					nodes.clear();			/** TODO clear the nodes */
					for (Member member : state.getMembers()) {
						/** TODO check ??? */
						if (member.hasRole("compute") && member.status().equals(MemberStatus.up())) {
							nodes.add(member.address());
						}
					}
				})
				/** TODO ??? */
				.match(MemberUp.class, mUp -> {
					if (mUp.member().hasRole("compute"))
						nodes.add(mUp.member().address());
				})
				/** TODO ?? */
				.match(MemberEvent.class, other -> {
					nodes.remove(other.member().address());
				})
				/** TODO ?? */
				.match(UnreachableMember.class, unreachable -> {
					nodes.remove(unreachable.member().address());
				})
				/**  */
				.match(ReachableMember.class, reachable -> {
					if (reachable.member().hasRole("compute"))
						nodes.add(reachable.member().address());
				})
				.build();
	}

	/** re-subscribe when restart */
	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(self());
		tickTask.cancel();
	}

	/** subscribe to cluster changes, MemberEvent */
	@Override
	public void preStart() throws Exception {
		cluster.subscribe(self(), MemberEvent.class, ReachabilityEvent.class);
	}
	
}
