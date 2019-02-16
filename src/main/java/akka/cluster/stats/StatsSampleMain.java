package akka.cluster.stats;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * complete running by:
 * runMain akka.cluster.stats.StatsSampleMain
 * or by case:
 * runMain akka.cluster.stats
 */
public class StatsSampleMain {
	public static void main(String[] args) {
		if (args.length == 0) {
			startup(new String[] { "2551", "2552", "0" });
			StatsSampleClientMain.main(new String[0]);
		} else {
			startup(args);
		}
	}
	
	/***/
	public static void startup(String[] ports) {
		for (String port : ports) {
			// Override the configuration of the port
		    // To use artery instead of netty, change to "akka.remote.artery.canonical.port"
		    // See https://doc.akka.io/docs/akka/current/remoting-artery.html for details
			Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
					.withFallback(ConfigFactory.parseString("akka.cluster.roles = [compute]"))
					.withFallback(ConfigFactory.load("stats1")); 			/** load akka configuration at stats1.conf */
			
			/** */
			ActorSystem system = ActorSystem.create("ClusterSystem", config);
			// create new actor of StatsWorker & StatsService
			system.actorOf(Props.create(StatsWorker.class), "statsWorker");
			system.actorOf(Props.create(StatsService.class), "statsService");
		}
	}
}
