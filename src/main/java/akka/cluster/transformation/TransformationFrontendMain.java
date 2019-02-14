package akka.cluster.transformation;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import static akka.pattern.Patterns.ask;

import akka.cluster.transformation.TransformationMessages.TransformationJob;
import akka.dispatch.OnSuccess;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * runMain akka.cluster.transformation.TransformationFrontendMain
 * or with complete running at:
 * runMain akka.cluster.transformation.TransformationApp
 */
public class TransformationFrontendMain {
	public static void main(String[] args) {
		// Override the configuration of the port when specified as program argument
	    // To use artery instead of netty, change to "akka.remote.artery.canonical.port"
	    // See https://doc.akka.io/docs/akka/current/remoting-artery.html for details
		final String port = args.length > 0 ? args[0] : "0";
		final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
				.withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]"))
				.withFallback(ConfigFactory.load());					/** No Configuration Load */
		
		// TODO ??
		ActorSystem system = ActorSystem.create("ClusterSystem", config);
		// TODO ??
		final ActorRef frontend = system.actorOf( Props.create(TransformationFrontend.class), "frontend");
		// TODO ??
		final FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
		// TODO ??
		final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
		// TODO ??
		final ExecutionContext ec = system.dispatcher();
		// TODO ??
		final AtomicInteger counter = new AtomicInteger();
		
		/** TODO ??? */
		system.scheduler().schedule(interval, interval, new Runnable() {
			@Override
			public void run() {
				ask(frontend, new TransformationJob("hello-" + counter.incrementAndGet()), timeout).onSuccess(new OnSuccess<Object>() {
					@Override
					public void onSuccess(Object result) {
						System.out.println(result);
					}
				}, ec);
			}
		}, ec);
	}
}
