package akka.cluster.transformation;

import java.io.Serializable;

/**
 * https://doc.akka.io/docs/akka/2.5/cluster-usage.html?language=java
 * or see at: akka-cluster-docs.html
 * 
 * this interface used to store some static classes
 */
public interface TransformationMessages {
	/**  */
	public static class TransformationJob implements Serializable {
		private static final long serialVersionUID = 1L;		
		private final String text;		
		public TransformationJob(String text) {
			this.text = text;
		}		
		public String getText() { return text; }
	}
	
	/**  */
	public static class TransformationResult implements Serializable {
		private static final long serialVersionUID = 1L;		
		private final String text;		
		public TransformationResult(String text) {
			this.text = text;
		}		
		public String getText() { return text; }
		
		@Override
		public String toString() {
			return "TransformationResult(" + text + ")";
		}		
	}
	
	/** */
	public static class JobFailed implements Serializable {
		private static final long serialVersionUID = 1L;
		
		//
		private final String reason;
		private final TransformationJob job;
		
		public JobFailed(String reason, TransformationJob job) {
			this.reason = reason;
			this.job = job;
		}
		
		public String getReason() { return reason; }
		public TransformationJob getJob() { return job; }

		@Override
		public String toString() {
			return "JobFailed(" + reason +")";
		}		
	}
	
	public static final String BACKEND_REGISTRATION = "BackendRegistration";
}
