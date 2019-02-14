package akka.cluster.stats;

import java.io.Serializable;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * this interface used to store some static classes
 */
public interface StatsMessages {

	/**  */
	public static class StatsJob implements Serializable {
		private static final long serialVersionUID = 1L;		
		private final String text;
		
		public StatsJob(String text) {
			this.text = text;
		}
		
		public String getText() { return text; } 
	}
	
	/** */
	public static class StatsResult implements Serializable {
		private static final long serialVersionUID = 1L;
		private final double meanWordLength;
		
		public StatsResult(double meanWordLength) {
			this.meanWordLength = meanWordLength;
		}
		
		public double getMeanWordLength() { return meanWordLength; }

		@Override
		public String toString() {
			return "meanWordLength: " + meanWordLength;
		}		
	}
	
	/** */
	public static class JobFailed implements Serializable {
		private static final long serialVersionUID = 1L;
		private final String reason;
		
		public JobFailed(String reason) {
			this.reason = reason;
		}
		
		public String getReason() { return reason; }

		@Override
		public String toString() {
			return "JobFailed(" + reason + ")";
		}		
	}
	
}
