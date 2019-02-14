package akka.cluster.transformation;

import akka.actor.AbstractActor;

import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.cluster.transformation.TransformationMessages.TransformationJob;
import akka.cluster.transformation.TransformationMessages.TransformationResult;

import static akka.cluster.transformation.TransformationMessages.BACKEND_REGISTRATION;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 */
public class TransformationBackend extends AbstractActor {
	// 
	Cluster cluster = Cluster.get( getContext().system() );

	/** re-subscribe when restart */
	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(self());
	}

	/** subscribe to cluster changes, MemberUp */
	@Override
	public void preStart() throws Exception {
		cluster.subscribe(self(), MemberUp.class);
	}

	/** TODO ??? */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(TransformationJob.class, job -> {
					sender().tell(new TransformationResult(job.getText().toUpperCase()), self());
				})
				.match(CurrentClusterState.class, state ->{
					for (Member member : state.getMembers()) {
						if ( member.status().equals(MemberStatus.up()) ) {
							register(member);
						}
					}
				})
				.match(MemberUp.class, mUp ->{
					register(mUp.member());
				})
				.build();
	}

	/** TODO ??? */
	void register(Member member) {
		if (member.hasRole("frontend"))
			getContext().actorSelection(member.address() + "/user/frontend").tell(BACKEND_REGISTRATION, self());
	}
}
