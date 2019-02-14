package akka.cluster.stats;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * runMain akka.cluster.stats.StatsSampleClientMain
 */
public class StatsSampleClientMain {
	public static void main(String[] args) {
		/** note that client is not a compute node, role not defined */
		ActorSystem system = ActorSystem.create("ClusterSystem", ConfigFactory.load("stats1"));		/** load akka configuration at stats1.conf */
		system.actorOf( Props.create(StatsSampleClient.class, "/user/statsService"), "client" );
	}
}
