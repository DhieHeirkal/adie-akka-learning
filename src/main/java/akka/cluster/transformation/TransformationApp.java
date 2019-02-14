package akka.cluster.transformation;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * complete running by:
 * runMain akka.cluster.transformation.TransformationApp
 * or by case:
 * runMain akka.cluster.transformation.TransformationFrontendMain
 */
public class TransformationApp {
	public static void main(String[] args) {
		/** starting 2 frontend nodes and 3 backend nodes */
		TransformationBackendMain.main(new String[] { "2551" });
		TransformationBackendMain.main(new String[] { "2552" });
		TransformationBackendMain.main(new String[0]);
		TransformationFrontendMain.main(new String[0]);
	}
}
