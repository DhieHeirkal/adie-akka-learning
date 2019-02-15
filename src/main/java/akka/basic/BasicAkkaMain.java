package akka.basic;

/**
 * Fetch from:
 * https://developer.lightbend.com/start/?group=akka&project=akka-samples-main-java
 * 
 * runMain akka.basic.BasicAkkaMain
 */
public class BasicAkkaMain {
	public static void main(String[] args) {
		/** TODO check the akka.Main */
		akka.Main.main(new String[] { HelloBasicAkka.class.getName() });
	}
}
