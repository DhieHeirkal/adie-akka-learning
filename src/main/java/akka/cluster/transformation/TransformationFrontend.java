package akka.cluster.transformation;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.cluster.transformation.TransformationMessages.TransformationJob;
import akka.cluster.transformation.TransformationMessages.JobFailed;

import static akka.cluster.transformation.TransformationMessages.BACKEND_REGISTRATION;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 */
public class TransformationFrontend extends AbstractActor {
	// new Object list for ActorRef list
	List<ActorRef> backends = new ArrayList<>();
	int jobCounter = 0;
	
	/**  */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(TransformationJob.class, job -> {
					sender().tell(new JobFailed("Service unavailable, try again later", job), sender());
				})
				.match(TransformationJob.class, job -> {
					jobCounter++;
					/** */
					backends.get(jobCounter % backends.size()).forward(job, getContext());
				})
				/** */ 
				.matchEquals(BACKEND_REGISTRATION, message -> {
					getContext().watch(sender());
					backends.add(sender());
				})
				.match(Terminated.class, terminated -> {
					backends.remove(terminated.getActor());
				})
				.build();
	}

}
