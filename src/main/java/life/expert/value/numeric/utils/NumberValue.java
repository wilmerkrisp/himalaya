package life.expert.value.numeric.utils;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.amount
//                           wilmer 2019/05/02
//
//--------------------------------------------------------------------------------









import life.expert.value.numeric.amount.Quantity;

import java.math.BigDecimal;
import java.math.MathContext;









/**
 * Instances of this class allow to externalize the numeric value of a {@link Quantity}. The classs extends
 * {@link java.lang.Number} for maximal compatibility with the JDK but also adds additional functionality to
 * overcome some of the pitfalls of the JDK's Number class.
 * Implementation specification
 * Implementations of this interface must be
 * <ul>
 * <li>Comparable, hereby comparing the numeric value.</li>
 * <li>immutable</li>
 * <li>serializable</li>
 * <li>thread-safe</li>
 * </ul>
 */
public abstract class NumberValue
	extends Number
	implements Comparable<NumberValue>
	{
	
	
	
	/**
	 * Get the numeric implementation type, that is the base of this number.
	 *
	 * @return the numeric implementation type, not {@code null}.
	 */
	public abstract Class<?> getNumberType();
	
	
	
	/**
	 * Returns the <i>precision</i> of this {@code Quantity}. (The precision is the number of
	 * digits in the unscaled value.)
	 *
	 *
	 * The precision of a zero value is 1.
	 *
	 * @return the precision of this {@code Quantity}.
	 */
	public abstract int getPrecision();
	
	
	
	/**
	 * Returns the <i>scale</i> of this {@code Quantity}. If zero or positive, the scale is
	 * the number of digits to the right of the decimal point. If negative, the unscaled value of
	 * the number is multiplied by ten to the power of the negation of the scale. For example, a
	 * scale of {@code -3} means the unscaled value is multiplied by 1000.
	 *
	 * @return the scale of this {@code Quantity}.
	 */
	public abstract int getScale();
	
	
	
	/**
	 * Access the numeric value as {@code int}. Hereby no truncation will be performed to fit the
	 * value into the target data type.
	 *
	 * @return the (possibly) truncated value of the {@link Quantity}.
	 *
	 * @throws ArithmeticException
	 * 	If the value must be truncated to fit the target datatype.
	 */
	public abstract int intValueExact();
	
	
	
	/**
	 * Access the numeric value as {@code long}. Hereby no truncation will be performed to fit the
	 * value into the target data type.
	 *
	 * @return the (possibly) truncated value of the {@link Quantity}.
	 *
	 * @throws ArithmeticException
	 * 	If the value must be truncated to fit the target datatype.
	 */
	public abstract long longValueExact();
	
	
	
	/**
	 * Access the numeric value as {@code double}. Hereby no truncation will be performed to fit the
	 * value into the target data type.
	 *
	 * @return the (possibly) truncated value of the {@link Quantity}.
	 *
	 * @throws ArithmeticException
	 * 	If the value must be truncated to fit the target datatype.
	 */
	public abstract double doubleValueExact();
	
	
	
	/**
	 * Access the numeric value as {@code Number}. Hereby truncation may be performed as needed to
	 * fit the value into the target data type.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param numberType
	 * 	The concrete number class to be returned. Basically the following Number types, 	must be supported if available on the corresponding runtime platform: 	<ul> 	<li>{@code java.lang.Long}</li> 	<li>{@code java.lang.Double}</li> 	<li>{@code java.lang.Number}</li> 	<li>{@code java.math.BigInteger}, currently not available on all platforms.</li> 	<li>{@code java.math.BigDecimal}, currently not
	 * 	available on all platforms.</li> 	</ul>
	 *
	 * @return the (possibly) truncated value of the {@link Quantity}.
	 */
	public abstract <T extends Number> T numberValue( Class<T> numberType );
	
	
	
	/**
	 * Access the current NumberValue rounded using the given {@link java.math.MathContext}.
	 *
	 * @param mathContext
	 * 	the {@link java.math.MathContext} to be applied.
	 *
	 * @return the new NumberValue, never null.
	 *
	 * @see java.math.BigDecimal#round(java.math.MathContext) java.math.BigDecimal#round(java.math.MathContext)java.math.BigDecimal#round(java.math.MathContext)
	 */
	public abstract NumberValue round( MathContext mathContext );
	
	
	
	/**
	 * Access the numeric value as {@code Number}. Hereby no truncation will be performed to fit the
	 * value into the target data type.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param numberType
	 * 	The concrete number class to be returned. Basically the following Number types, 	must be supported if available on the corresponding runtime platform: 	<ul> 	<li>{@code java.lang.Long}</li> 	<li>{@code java.lang.Double}</li> 	<li>{@code java.lang.Number}</li> 	<li>{@code java.math.BigInteger}, currently not available on all platforms.</li> 	<li>{@code java.math.BigDecimal}, currently not
	 * 	available on all platforms.</li> 	</ul>
	 *
	 * @return the (possibly) truncated value of the {@link Quantity}.
	 *
	 * @throws ArithmeticException
	 * 	If the value must be truncated to fit the target datatype.
	 */
	public abstract <T extends Number> T numberValueExact( Class<T> numberType );
	
	
	
	/**
	 * This method allows to extract the numerator part of the current fraction, hereby given
	 *  <pre>{@code  *
	 * w = longValue()
	 * n = getFractionNominator()
	 * d = getFractionDenominator() }*</pre>
	 *
	 *
	 * the following must be always true:
	 *
	 *
	 *  <pre>{@code  * !(w<0 && n>0)  and
	 * !(w>0 && n<0)  and
	 * d>0            and
	 * |n| < d        // || = absolute value }</pre>
	 *
	 * @return the amount's fraction numerator..
	 */
	public abstract long getAmountFractionNumerator();
	
	
	
	/**
	 * This method allows to extract the denominator part of the current fraction, hereby given
	 *  <pre>{@code
	 * w = longValue()
	 * n = getFractionNominator()
	 * d = getFractionDenominator()
	 * }*</pre>
	 *
	 * the following must be always true:
	 *
	 *  <pre>{@code
	 * !(w<0 && n>0)  and
	 * !(w>0 && n<0)  and
	 * d>0            and
	 * |n| < d        // || = absolute value
	 * }*</pre>.
	 *
	 * @return the amount's fraction denominator.
	 */
	public abstract long getAmountFractionDenominator();
	
	
	
	@Override
	public int compareTo( NumberValue o )
		{
		return numberValue( BigDecimal.class ).compareTo( o.numberValue( BigDecimal.class ) );
		}
		
	}

