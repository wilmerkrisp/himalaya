package life.expert.value.numeric.operators;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.operators
//                           wilmer 2019/05/02
//
//--------------------------------------------------------------------------------









import life.expert.value.numeric.amount.Quantity;

import java.util.function.UnaryOperator;









/**
 * Represents an operation on a single {@link Quantity} that produces a
 * result of type {@link Quantity}.
 *
 * Examples might be an operator that rounds the amount to the nearest 1000, or
 * one that performs units conversion.
 *
 * There are two equivalent ways of using a {@code Operator}. The first
 * is to invoke the method on this interface. The second is to use
 * {@link Quantity#with(Operator)}
 *
 * <pre>
 * // these two lines are equivalent, but the second approach is recommended
 * values = thisOperator.apply(values);
 * values = values.with(thisOperator);
 * </pre>
 *
 * It is recommended to use the second approach, {@code with(Operator)},
 * as it is a lot clearer to read in code.
 *
 * Implementation specification
 * The implementation must take the input object and apply it. The
 * implementation defines the logic of the operator and is responsible for
 * documenting that logic. It may use any method on {@code Quantity} to
 * determine the result.
 *
 * The input object must not be altered. Instead, an altered copy of the
 * original must be returned. This provides equivalent, safe behavior for
 * immutable and mutable value amounts.
 *
 * This method may be called from multiple threads in parallel. It must be
 * thread-safe when invoked.
 *
 *
 * This interface extends {@code java.util.function.UnaryOperator} introduced by Java 8.
 */
@FunctionalInterface
public interface Operator
	extends UnaryOperator<Quantity>
	{
	
	
	
	/**
	 * Applies the operator on the given amount.
	 *
	 * @param t
	 * 	the amount to be operated on.
	 *
	 * @return the applied amount.
	 */
	@Override
	Quantity apply( Quantity t );
	
	}
