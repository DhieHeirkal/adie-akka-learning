package akka.cluster.simple;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Source code: https://developer.lightbend.com/start/?group=akka&project=akka-samples-cluster-java
 * 
 * runMain akka.cluster.simple.SimpleClusterApp
 */
public class SimpleClusterApp {
	/**  */
	public static void main(String[] args) {
		if (args.length == 0)
			startup(new String[] { "2551", "2552", "0"});
		else 
			startup(args);  // running with some ports args
	}
	
	/**  */
	public static void startup(String[] ports) {
		for (String port : ports) {
			// Override the configuration of the port
		    // To use artery instead of netty, change to "akka.remote.artery.canonical.port"
		    // See https://doc.akka.io/docs/akka/current/remoting-artery.html for details
			Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
					.withFallback(ConfigFactory.load());
			
			// Create an Akka system
			ActorSystem system = ActorSystem.create("ClusterSystem", config);
			
			// Create an actor that handles cluster domain events
//			system.actorOf(Props.create(SimpleClusterListener.class), "clusterListener");
			system.actorOf(Props.create(SimpleClusterListener2.class), "clusterListener");
		}
	}
}
