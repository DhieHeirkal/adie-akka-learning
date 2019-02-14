package akka.cluster.factorial;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * runMain akka.cluster.factorial.FactorialBackendMain
 */
public class FactorialBackendMain {
	public static void main(String[] args) {
		// Override the configuration of the port when specified as program argument
	    // To use artery instead of netty, change to "akka.remote.artery.canonical.port"
	    // See https://doc.akka.io/docs/akka/current/remoting-artery.html for details
		final String port = args.length > 0 ? args[0] : "0";
		final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
				.withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]"))
				.withFallback(ConfigFactory.load("factorial"));					/** Load factorial.conf */

		// 
		ActorSystem system = ActorSystem.create("ClusterSystem", config);
		
		// Only running from here will no appearance in console
		system.actorOf(Props.create(FactorialBackend.class), "factorialBackend");
		
		// Here will appear some console metrics 
		system.actorOf(Props.create(MetricsListener.class), "metricsListener");
	}
}
