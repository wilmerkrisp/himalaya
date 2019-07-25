package life.expert.common.base;

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.utils.base
//                           wilmer 2019/01/23
//
//--------------------------------------------------------------------------------

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Represents an operation on a String operand that produces a String result.  This is a specialization of {@code UnaryOperator} for
 * the case where the operand is String.
 *
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @see Function
 * @since 1.8
 */
@FunctionalInterface
public interface StringUnaryOperator
	extends UnaryOperator<String>
	{
	
	/**
	 * Returns a unary operator that always returns its input argument.
	 *
	 * @return a unary operator that always returns its input argument
	 */
	static StringUnaryOperator identity()
		{
		return t -> t;
		}
	}