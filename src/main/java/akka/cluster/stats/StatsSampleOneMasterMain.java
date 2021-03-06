package akka.cluster.stats;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.cluster.singleton.ClusterSingletonManager;
import akka.cluster.singleton.ClusterSingletonManagerSettings;
import akka.cluster.singleton.ClusterSingletonProxy;
import akka.cluster.singleton.ClusterSingletonProxySettings;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * runMain akka.cluster.stats.StatsSampleOneMasterMain
 * TODO check diff with:
 * akka.cluster.stats.StatsSampleOneMasterClientMain
 */
public class StatsSampleOneMasterMain {
	public static void main(String[] args) {
		if (args.length == 0) {
			startup(new String[] { "2551", "2552", "0" });
			StatsSampleOneMasterClientMain.main(new String[0]);
		} else {
			startup(args);
 		}
	}
	
	/** */
	public static void startup(String[] ports) {
		for (String port : ports) {
			// Override the configuration of the port
		    // To use artery instead of netty, change to "akka.remote.artery.canonical.port"
		    // See https://doc.akka.io/docs/akka/current/remoting-artery.html for details
			Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
					.withFallback(ConfigFactory.parseString("akka.cluster.roles = [compute]"))
					.withFallback(ConfigFactory.load("stats2")); 				/** load akka configuration at stats2.conf */
			
			ActorSystem system = ActorSystem.create("ClusterSystem", config);
			
			/** TODO ??? */
			ClusterSingletonManagerSettings settings = ClusterSingletonManagerSettings.create(system).withRole("compute");
			system.actorOf(ClusterSingletonManager.props(Props.create(StatsService.class), PoisonPill.getInstance(), settings), "statsService");
			
			/** TODO ??? */
			ClusterSingletonProxySettings proxySettings = ClusterSingletonProxySettings.create(system).withRole("compute");
			system.actorOf(ClusterSingletonProxy.props("/user/statsService", proxySettings), "statsServiceProxy");
		}
	}
}
