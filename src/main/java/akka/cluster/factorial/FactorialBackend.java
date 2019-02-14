package akka.cluster.factorial;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import akka.actor.AbstractActor;
import akka.pattern.PatternsCS;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 */
public class FactorialBackend extends AbstractActor {

	/** mandatory method for actor */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Integer.class, n -> {
					//
					CompletableFuture<FactorialResult> result = 
							CompletableFuture.supplyAsync(() -> factorial(n))
								.thenApply( (factorial) -> new FactorialResult(n, factorial));
					//
					PatternsCS.pipe(result, getContext().dispatcher()).to(sender());
				}).build();
	}

	/** factorial formulation */
	BigInteger factorial(int n) {
		BigInteger acc = BigInteger.ONE;
		for (int i = 1; i <= n; ++i) {
			acc = acc.multiply(BigInteger.valueOf(i));
		}
		return acc;
	}
	
}
