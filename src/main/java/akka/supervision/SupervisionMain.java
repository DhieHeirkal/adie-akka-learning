package supervision;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.reflect.ClassTag;

import static supervision.Expression.*;
import static akka.pattern.Patterns.ask;
import static akka.japi.Util.classTag;

/**
 * Full Documentation at: 
 * https://doc.akka.io/docs/akka/2.5.20/general/supervision.html
 */
public class SupervisionMain {

	public static void main(String[] args) throws Exception {
		// creating actor
		ActorSystem system = ActorSystem.create("calculator-system");
		ActorRef calcService = system.actorOf( Props.create(ArithmeticService.class), "arithmetic-service" );
		
		Expression task = new Divide(
				new Add(new Const(3), new Const(5)),
				new Multiply(
						new Const(2),
						new Add(new Const(1), new Const(1))
					)
			);
		
		FiniteDuration duration = Duration.create(1, TimeUnit.SECONDS);
		Integer result = Await.result( ask(calcService, task, new Timeout(duration)).mapTo(classTag(Integer.class)), duration );
		System.out.println("Got result: " + result); 
		
		Await.ready(system.terminate(), Duration.Inf());
	}
	
}
