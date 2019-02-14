package akka.cluster.factorial;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 
 */
public class FactorialResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final int n;
	public final BigInteger factorial;
	
	public FactorialResult(int n, BigInteger factorial) {
		this.n = n;
		this.factorial = factorial;
	}
}
