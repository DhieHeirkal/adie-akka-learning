package akka.cluster.stats;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * runMain akka.cluster.stats.StatsSampleOneMasterClientMain
 * TODO check diff with:
 * akka.cluster.stats.StatsSampleOneMasterMain
 */
public class StatsSampleOneMasterClientMain {
	public static void main(String[] args) {
		/** note that client is not a compute node, role not defined */
		ActorSystem system = ActorSystem.create("ClusterSystem", ConfigFactory.load("stats2"));		/** load akka configuration at stats2.conf */
		system.actorOf( Props.create(StatsSampleClient.class, "/user/statsServiceProxy"), "client");
	}
}
