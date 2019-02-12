package supervision;

/**
 * Represents an arithmetic expression involving integer numbers
 */
public interface Expression {
	//
	public Expression getLeft();
	public Expression getRight();
	
	/**
	 * Basic arithmetic operations that are supported by the ArithmeticService. 
	 * Every operation except the constant value has a left and right side. For example
	 * the addition in (3 * 2) + (6 * 6) has the left side (3 * 2) and the right side (6 * 6)
	 */
	public static abstract class AbstractExpression implements Expression {
		private final Expression left;
		private final Expression right;
		private final String operator;
		
		protected AbstractExpression(Expression left, Expression right, String operator) {
			this.left = left;
			this.right = right;
			this.operator = operator;
		}

		@Override
		public Expression getLeft() { return left; }

		@Override
		public Expression getRight() { return right; }

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if ( !(obj instanceof AbstractExpression) ) return false;
			
			AbstractExpression that = (AbstractExpression) obj;
			
			if ( !left.equals(that.left) ) return false;
			if ( !right.equals(that.right) ) return false;
			if ( !operator.equals(that.operator) ) return false;
			
			return true;
		}

		@Override
		public int hashCode() {
			int result = left.hashCode();
			result = 31 * result + right.hashCode();
			result = 31 * result + operator.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "(" + getLeft() + " " + operator + " " + getRight() + ")";
		}			
	}
	
	/**
	 * Basic Aritmethic func obj of Add, Multiply, & Divide 
	 */
	public static final class Add extends AbstractExpression {
		public Add(Expression left, Expression right) {
			super(left, right, "+");
		}		
	}
	public static final class Multiply extends AbstractExpression {
		public Multiply(Expression left, Expression right) {
			super(left, right, "*");
		}		
	}
	public static final class Divide extends AbstractExpression {
		public Divide(Expression left, Expression right) {
			super(left, right, "/");
		}		
	}
	
	/** */
	public static final class Const implements Expression {
		private final int value;
		
		public Const(int value) {
			this.value = value;
		}

		@Override
		public Expression getLeft() { return this; }

		@Override
		public Expression getRight() { return this; }
		
		public int getValue() { return value; }

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if ( !(obj instanceof Const) ) return false;
			
			Const aConst = (Const) obj;
			
			if (value != aConst.value) return false;
			
			return true;
		}

		@Override
		public int hashCode() { return value; }

		@Override
		public String toString() {
			return String.valueOf(value);
		}		
	}
}
