package life.expert.value.numeric.amount;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.amount
//                           wilmer 2019/04/30
//
//--------------------------------------------------------------------------------

import life.expert.value.numeric.context.Context;
import life.expert.value.numeric.utils.ValueException;
import life.expert.value.numeric.context.PieceContext;
import life.expert.value.numeric.operators.Operator;
import life.expert.value.numeric.unit.Unit;
import life.expert.value.numeric.utils.NumberValue;

/**
 * Interface defining an amount. The effective format representation of an amount may vary
 * depending on the implementation used. Reason behind is that the requirements to an implementation
 * heavily vary for different usage scenarios.
 *
 * <ul>
 * <li>Arithmetic operations should throw an {@link ArithmeticException}, if performing arithmetic
 * operations between amounts exceeds the capabilities of the numeric representation type used. Any
 * implicit truncating, that would lead to complete invalid and useless results, should be avoided.
 * This recommendation does not affect format rounding, as required by the format numeric
 * representation of an amount.
 * <li>Quantitys should allow numbers as argument for arithmetic operations like division and
 * multiplication. Adding or subtracting of amounts must only be possible by passing instances of
 * {@link Quantity}.</li>
 * <li>Nevertheless numeric truncation is also explicitly supported when calling
 * {@link NumberValue#numberValue(Class)}, whereas the <i>exact</i> counterpart,
 * {@link NumberValue#numberValueExact(Class)}, works similar to
 * {@link java.math.BigDecimal#longValueExact()}.
 * <li>Since implementations are recommended to be immutable, an operation should never change any
 * format state of an instance. Given an instance, all operations are required to be fully
 * reproducible.</li>
 * </ul>
 */
public interface Quantity
	extends Comparable<Quantity>
	{
	
	/**
	 * Returns the {@link  Context} of this {@code Quantity}. The
	 * {@link  Context} provides additional information about the numeric representation and
	 * the numeric capabilities. This information can be used by code to determine situations where
	 * {@code Qunatity} instances must be converted to avoid implicit truncation, which can
	 * lead to invalid results.
	 *
	 * @return the {@link Context} of this {@code Quantity}, never {@code null} .
	 */
	Context getContext();
	
	/**
	 * Gets the corresponding {@link Unit}.
	 *
	 * @return the corresponding {@link Unit}, not null.
	 */
	Unit getUnit();
	
	/**
	 * Gets the corresponding {@link  NumberValue}.
	 *
	 * @return the corresponding {@link NumberValue}, not null.
	 */
	NumberValue getNumber();
	
	/**
	 * Returns an operated object <b>of the same type</b> as this object with the operation made.
	 * Hereby returning an instannce <b>of the same type</b> is very important to prevent
	 * uncontrolled mixup of implementations.
	 *
	 *
	 * This converts this  amount according to the rules of the specified operator. A
	 * typical operator will change the amount and leave the unit unchanged. A more complex
	 * operator might also change the unit.
	 *
	 * Some example code indicating how and why this method is used:
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * Quantity pieces = pieces.with(amountMultipliedBy(2));
	 * pieces = pieces.with(amountRoundedToNearestWholeUnit());
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * Hereby also the method signature on the implementation type must return the concrete type, to
	 * enable a fluent API, e.g.
	 *
	 * <blockquote>
	 *
	 * <pre>
	 * public final class MyAmount implements Quantity{
	 *   ...
	 *   public <b>MyAmount</b> with(Operator operator){
	 *     ...
	 *   }
	 *
	 *   ...
	 * }
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param operator
	 * 	the operator to use, not null
	 *
	 * @return an object of the same type with the specified conversion made, not null
	 */
	default Quantity with( Operator operator )
		{
		return operator.apply( this );
		}
	
	/**
	 * Compares two instances of {@link Quantity}, hereby ignoring non significant trailing
	 * zeroes and different numeric capabilities.
	 *
	 * @param amount
	 * 	the {@code Quantity} to be compared with this instance.
	 *
	 * @return {@code true} if {@code amount > this}.
	 *
	 * @throws ValueException
	 * 	if the amount's unit is not equals to the unit of this instance.
	 */
	boolean isGreaterThan( Quantity amount );
	
	/**
	 * Compares two instances of {@link Quantity}, hereby ignoring non significant trailing
	 * zeroes and different numeric capabilities.
	 *
	 * @param amount
	 * 	the {@link Quantity} to be compared with this instance.
	 *
	 * @return {@code true} if {@code amount >= this}.
	 *
	 * @throws ValueException
	 * 	if the amount's unit is not equals to the unit of this instance.
	 */
	boolean isGreaterThanOrEqualTo( Quantity amount );
	
	/**
	 * Compares two instances of {@link Quantity}, hereby ignoring non significant trailing
	 * zeroes and different numeric capabilities.
	 *
	 * @param amount
	 * 	the {@link Quantity} to be compared with this instance.
	 *
	 * @return {@code true} if {@code amount < this}.
	 *
	 * @throws ValueException
	 * 	if the amount's unit is not equals to the unit of this instance.
	 */
	boolean isLessThan( Quantity amount );
	
	/**
	 * Compares two instances of {@link Quantity}, hereby ignoring non significant trailing
	 * zeroes and different numeric capabilities.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return {@code true} if {@code amount <= this}.
	 *
	 * @throws ValueException
	 * 	if the amount's unit is not equals to the unit of this instance.
	 */
	boolean isLessThanOrEqualTo( Quantity amount );
	
	/**
	 * Compares two instances of {@link Quantity}, hereby ignoring non significant trailing
	 * zeroes and different numeric capabilities.
	 *
	 * @param amount
	 * 	the {@link Quantity} to be compared with this instance.
	 *
	 * @return {@code true} if {@code amount == this}.
	 *
	 * @throws ValueException
	 * 	if the amount's unit is not equals to the unit of this instance.
	 */
	boolean isEqualTo( Quantity amount );
	
	/**
	 * Checks if a {@code Quantity} is negative.
	 *
	 * @return {@code true if signum() <0 }
	 */
	default boolean isNegative()
		{
		return signum() < 0;
		}
	
	/**
	 * Checks if a {@code Quantity} is negative or zero.
	 *
	 * @return {@code true if signum() <= 0. }
	 */
	default boolean isNegativeOrZero()
		{
		return signum() <= 0;
		}
	
	/**
	 * Checks if a {@code Quantity} is positive.
	 *
	 * @return {@code true if  signum() > 0.}
	 */
	default boolean isPositive()
		{
		return signum() > 0;
		}
	
	/**
	 * Checks if a {@code Quantity} is positive or zero.
	 *
	 * @return {@code true if signum() >= 0}
	 */
	default boolean isPositiveOrZero()
		{
		return signum() >= 0;
		}
	
	/**
	 * Checks if an {@code Quantity} is zero.
	 *
	 * @return {@code true if signum() == 0. }
	 */
	default boolean isZero()
		{
		return signum() == 0;
		}
	
	/**
	 * Returns the signum function of this {@code Quantity}.
	 *
	 * @return -1, 0, or 1 as the value of this  Quantity is negative, zero, or 	positive.
	 */
	int signum();
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this + amount}</pre>, and whose scale is <pre>{@code max(this.scale(),
	 * amount.scale()}</pre>.
	 *
	 * @param amount
	 * 	value to be added to this {@code Quantity}.
	 *
	 * @return {@code this + amount}
	 *
	 * @throws ArithmeticException
	 * 	if the result exceeds the numeric capabilities of this implementation class, i.e. 	the {@link PieceContext} cannot be adapted as required.
	 */
	Quantity add( Quantity amount );
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this -
	 * amount}</pre>, and whose scale is <pre>{@code max(this.scale(),
	 * subtrahend.scale()}</pre>.
	 *
	 * @param amount
	 * 	value to be subtracted from this {@code Quantity}.
	 *
	 * @return {@code this - amount}
	 *
	 * @throws ArithmeticException
	 * 	if the result exceeds the numeric capabilities of this implementation class, i.e. 	the {@link PieceContext} cannot be adapted as required.
	 */
	Quantity subtract( Quantity amount );
	
	/**
	 * Returns a {@code Quantity} whose value is <b>(this &times;
	 * multiplicand)</b>, and whose scale is <pre>{@code this.scale() +
	 * multiplicand.scale()}</pre>.
	 *
	 * @param multiplicand
	 * 	value to be multiplied by this {@code Quantity}.
	 *
	 * @return {@code this * multiplicand}
	 *
	 * @throws ArithmeticException
	 * 	if the result exceeds the numeric capabilities of this implementation class, i.e. 	the {@link PieceContext} cannot be adapted as required.
	 */
	Quantity multiply( long multiplicand );
	
	/**
	 * Returns a {@code Quantity} whose value is <b>(this &times;
	 * multiplicand)</b>, and whose scale is <pre>{@code this.scale() +
	 * multiplicand.scale()}</pre>.
	 * By default the input value's scale will be rounded to
	 * accommodate the format capabilities, and no {@link java.lang.ArithmeticException}
	 * is thrown if the input number's scale exceeds the capabilities.
	 *
	 * @param multiplicand
	 * 	value to be multiplied by this {@code Quantity}. If the multiplicand's scale exceeds 	the 	capabilities of the implementation, it may be rounded implicitly.
	 *
	 * @return {@code this * multiplicand}
	 *
	 * @throws ArithmeticException
	 * 	if the result exceeds the numeric capabilities of this implementation class, i.e. 	the {@link PieceContext} cannot be adapted as required.
	 */
	Quantity multiply( double multiplicand );
	
	/**
	 * Returns a {@code Quantity} whose value is    <b>(this &times;
	 * multiplicand)   </b>, and whose scale is <pre>{@code this.scale() +
	 * multiplicand.scale()}</pre>.
	 *
	 * @param multiplicand
	 * 	value to be multiplied by this {@code Quantity}. If the multiplicand's scale exceeds 	the 	capabilities of the implementation, it may be rounded implicitly.
	 *
	 * @return {@code this * multiplicand}
	 *
	 * @throws ArithmeticException
	 * 	if the result exceeds the numeric capabilities of this implementation class, i.e. 	the {@link PieceContext} cannot be adapted as required.
	 */
	Quantity multiply( Number multiplicand );
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this /
	 * divisor}</pre>, and whose preferred scale is <pre>{@code this.scale() -
	 * divisor.scale()}</pre>; if the exact quotient cannot be represented an {@code ArithmeticException}
	 * is thrown.
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided.
	 *
	 * @return {@code this / divisor}
	 *
	 * @throws ArithmeticException
	 * 	if the exact quotient does not have a terminating decimal expansion, or if the 	result exceeds the numeric capabilities of this implementation class, i.e. the        {@link PieceContext} cannot be adapted as required.
	 */
	Quantity divide( long divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this /
	 * divisor}</pre>, and whose preferred scale is <pre>{@code this.scale() -
	 * divisor.scale()}</pre>; if the exact quotient cannot be represented an {@code ArithmeticException}
	 * is thrown.
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided.
	 *
	 * @return {@code this / divisor}
	 *
	 * @throws ArithmeticException
	 * 	if the exact quotient does not have a terminating decimal expansion, or if the 	result exceeds the numeric capabilities of this implementation class, i.e. the        {@link PieceContext} cannot be adapted as required.
	 */
	Quantity divide( double divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this /
	 * divisor}</pre>, and whose preferred scale is <pre>{@code this.scale() -
	 * divisor.scale()}</pre>; if the exact quotient cannot be represented an {@code ArithmeticException}
	 * is thrown.
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided.
	 *
	 * @return {@code this / divisor}
	 *
	 * @throws ArithmeticException
	 * 	if the exact quotient does not have a terminating decimal expansion, or if the 	result exceeds the numeric capabilities of this implementation class, i.e. the        {@link PieceContext} cannot be adapted as required.
	 */
	Quantity divide( Number divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this % divisor}</pre>.
	 *
	 *
	 * The remainder is given by
	 * <pre>{@code this.subtract(this.divideToIntegralValue(divisor).multiply(divisor)}</pre> . Note that this
	 * is not the modulo operation (the result can be negative).
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided.
	 *
	 * @return {@code this % divisor}.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}, or if the result exceeds the numeric capabilities of this 	implementation class, i.e. the {@link PieceContext} cannot be adapted as 	required.
	 */
	Quantity remainder( long divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this % divisor}</pre>.
	 *
	 *
	 * The remainder is given by
	 * <pre>{@code this.subtract(this.divideToIntegralValue(divisor).multiply(divisor)}</pre> . Note that this
	 * is not the modulo operation (the result can be negative).
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided.
	 *
	 * @return {@code this % divisor}.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}, or if the result exceeds the numeric capabilities of this 	implementation class, i.e. the {@link PieceContext} cannot be adapted as 	required.
	 */
	Quantity remainder( double divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code this % divisor}</pre>.
	 *
	 *
	 * The remainder is given by
	 * <pre>{@code this.subtract(this.divideToIntegralValue(divisor).multiply(divisor)}</pre> . Note that this
	 * is not the modulo operation (the result can be negative).
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided.
	 *
	 * @return {@code this % divisor}.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}, or if the result exceeds the numeric capabilities of this 	implementation class, i.e. the {@link PieceContext} cannot be adapted as 	required.
	 */
	Quantity remainder( Number divisor );
	
	/**
	 * Returns a two-element {@code Quantity} array containing the result of
	 * {@code divideToIntegralValue} followed by the result of {@code remainder} on the two
	 * operands.
	 *
	 *
	 * Note that if both the integer quotient and remainder are needed, this method is faster than
	 * using the {@code divideToIntegralValue} and {@code remainder} methods separately because the
	 * division need only be carried out once.
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided, and the remainder 	computed.
	 *
	 * @return a two element {@code Quantity} array: the quotient (the result of        {@code divideToIntegralValue}) is the initial element and the remainder is the final 	element.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}, or if the result exceeds the numeric capabilities of this 	implementation class, i.e. the {@link PieceContext} cannot be adapted as 	required.
	 * @see #divideToIntegralValue(long) #divideToIntegralValue(long)#divideToIntegralValue(long)#divideToIntegralValue(long)#divideToIntegralValue(long)#divideToIntegralValue(long)
	 * @see #remainder(long) #remainder(long)#remainder(long)#remainder(long)#remainder(long)#remainder(long)
	 */
	Quantity[] divideAndRemainder( long divisor );
	
	/**
	 * Returns a two-element {@code Quantity} array containing the result of
	 * {@code divideToIntegralValue} followed by the result of {@code remainder} on the two
	 * operands.
	 *
	 *
	 * Note that if both the integer quotient and remainder are needed, this method is faster than
	 * using the {@code divideToIntegralValue} and {@code remainder} methods separately because the
	 * division need only be carried out once.
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided, and the remainder 	computed.
	 *
	 * @return a two element {@code Quantity} array: the quotient (the result of        {@code divideToIntegralValue}) is the initial element and the remainder is the final 	element.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}, or if the result exceeds the numeric capabilities of this 	implementation class, i.e. the {@link PieceContext} cannot be adapted as 	required.
	 * @see #divideToIntegralValue(double) #divideToIntegralValue(double)#divideToIntegralValue(double)#divideToIntegralValue(double)#divideToIntegralValue(double)#divideToIntegralValue(double)
	 * @see #remainder(double) #remainder(double)#remainder(double)#remainder(double)#remainder(double)#remainder(double)
	 */
	Quantity[] divideAndRemainder( double divisor );
	
	/**
	 * Returns a two-element {@code Quantity} array containing the result of
	 * {@code divideToIntegralValue} followed by the result of {@code remainder} on the two
	 * operands.
	 *
	 *
	 * Note that if both the integer quotient and remainder are needed, this method is faster than
	 * using the {@code divideToIntegralValue} and {@code remainder} methods separately because the
	 * division need only be carried out once.
	 *
	 * @param divisor
	 * 	value by which this {@code Quantity} is to be divided, and the remainder 	computed.
	 *
	 * @return a two element {@code Quantity} array: the quotient (the result of        {@code divideToIntegralValue}) is the initial element and the remainder is the final 	element.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}, or if the result exceeds the numeric capabilities of this 	implementation class, i.e. the {@link PieceContext} cannot be adapted as 	required.
	 * @see #divideToIntegralValue(Number) #divideToIntegralValue(Number)#divideToIntegralValue(Number)#divideToIntegralValue(Number)#divideToIntegralValue(Number)#divideToIntegralValue(Number)
	 * @see #remainder(Number) #remainder(Number)#remainder(Number)#remainder(Number)#remainder(Number)#remainder(Number)
	 */
	Quantity[] divideAndRemainder( Number divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is the integer part of the quotient
	 * <pre>{@code this / divisor}</pre> rounded down. The preferred scale of the result is
	 * <pre>{@code this.scale() -
	 * divisor.scale()}</pre>.
	 *
	 * @param divisor
	 * 	value by which this {@code BigDecimal} is to be divided.
	 *
	 * @return The integer part of {@code this / divisor}.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}
	 * @see java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal) java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)
	 */
	Quantity divideToIntegralValue( long divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is the integer part of the quotient
	 * <pre>{@code this / divisor}</pre> rounded down. The preferred scale of the result is
	 * <pre>{@code this.scale() - divisor.scale()}</pre>.
	 *
	 * @param divisor
	 * 	value by which this {@code BigDecimal} is to be divided.
	 *
	 * @return The integer part of {@code this / divisor}.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}
	 * @see java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal) java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)
	 */
	Quantity divideToIntegralValue( double divisor );
	
	/**
	 * Returns a {@code Quantity} whose value is the integer part of the quotient
	 * <pre>{@code this / divisor}</pre> rounded down. The preferred scale of the result is
	 * <pre>{@code this.scale() -
	 * divisor.scale()}</pre>.
	 *
	 * @param divisor
	 * 	value by which this {@code BigDecimal} is to be divided.
	 *
	 * @return The integer part of {@code this / divisor}.
	 *
	 * @throws ArithmeticException
	 * 	if {@code divisor==0}
	 * @see java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal) java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)java.math.BigDecimal#divideToIntegralValue(java.math.BigDecimal)
	 */
	Quantity divideToIntegralValue( Number divisor );
	
	/**
	 * Returns a {@code Quantity} whose numerical value is equal to ( {@code this} *
	 * 10<sup>n</sup>). The scale of the result is <pre>{@code this.scale() - n}</pre>.
	 *
	 * @param power
	 * 	the power.
	 *
	 * @return the calculated amount value.
	 *
	 * @throws ArithmeticException
	 * 	if the scale would be outside the range of a 32-bit integer, or if the result 	exceeds the numeric capabilities of this implementation class, i.e. the        {@link PieceContext} cannot be adapted as required.
	 */
	Quantity scaleByPowerOfTen( int power );
	
	/**
	 * Returns a {@code Quantity} whose value is the absolute value of this
	 * {@code Quantity}, and whose scale is {@code this.scale()}.
	 *
	 * @return <pre>{@code abs(this}</pre>
	 */
	Quantity abs();
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code -this}</pre>, and whose scale is
	 * {@code this.scale()}.
	 *
	 * @return {@code -this}.
	 */
	Quantity negate();
	
	/**
	 * Returns a {@code Quantity} whose value is <pre>{@code +this}</pre>, with rounding according to
	 * the context settings.
	 *
	 * @return {@code this}, rounded as necessary. A zero result will have a scale of 0.
	 *
	 * @throws ArithmeticException
	 * 	if rounding fails.
	 * @see java.math.BigDecimal#plus() java.math.BigDecimal#plus()java.math.BigDecimal#plus()java.math.BigDecimal#plus()java.math.BigDecimal#plus()java.math.BigDecimal#plus()
	 */
	Quantity plus();
	
	/**
	 * Returns a {@code Quantity} which is numerically equal to this one but with any trailing
	 * zeros removed from the representation. For example, stripping the trailing zeros from the
	 * {@code Quantity} value {@code UNIT 600.0}, which has [{@code BigInteger}, {@code scale}]
	 * components equals to [6000, 1], yields {@code 6E2} with [ {@code BigInteger}, {@code scale}]
	 * components equals to [6, -2]
	 *
	 * @return a numerically equal {@code Quantity} with any trailing zeros removed.
	 */
	Quantity stripTrailingZeros();
	
	}
