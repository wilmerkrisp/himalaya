package life.expert.value.numeric.utils;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.utils
//                           wilmer 2019/05/02
//
//--------------------------------------------------------------------------------

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This enumeration provides general utility functions supporting conversion of number types to BigDecimal.
 */
public enum ConvertBigDecimal
	{
		/**
		 * Conversion from integral numeric types, short, int, long.
		 */
		INTEGER
				{
				@Override
				BigDecimal getDecimal( Number num )
					{
					return BigDecimal.valueOf( num.longValue() );
					}
				},
		/**
		 * Conversion for floating point numbers.
		 */
		FLUCTUAGE
				{
				@Override
				BigDecimal getDecimal( Number num )
					{
					double d = num.doubleValue();
					if( Double.isNaN( d ) || Double.isInfinite( d ) )
						{
						throw new ArithmeticException( "NaN, POSITIVE_INFINITY and NEGATIVE_INFINITY cannot be used as " + "parameters for monetary operations." );
						}
					return new BigDecimal( num.toString() );
					}
				},
		/**
		 * Conversion from BigInteger.
		 */
		BIGINTEGER
				{
				@Override
				BigDecimal getDecimal( Number num )
					{
					return new BigDecimal( (BigInteger) num );
					}
				},
		/**
		 * Conversion from NumberValue.
		 */
		NUMBERVALUE
				{
				@Override
				BigDecimal getDecimal( Number num )
					{
					BigDecimal result = ( (NumberValue) num ).numberValue( BigDecimal.class );
					return stripScalingZeroes( result );
					}
				},
		/**
		 * Conversion from BigDecimal.
		 */
		BIGDECIMAL
				{
				@Override
				BigDecimal getDecimal( Number num )
					{
					BigDecimal result = ( (BigDecimal) num );
					return stripScalingZeroes( result );
					}
				},
		/**
		 * Conversion from BigDecimal, extended.
		 */
		BIGDECIMAL_EXTENDS
				{
				@Override
				BigDecimal getDecimal( Number num )
					{
					BigDecimal result = ( (BigDecimal) num ).stripTrailingZeros();
					return stripScalingZeroes( result );
					}
				},
		/**
		 * Default conversion based on String, if everything else failed.
		 */
		DEFAULT
				{
				@Override
				BigDecimal getDecimal( Number num )
					{
					BigDecimal result = null;
					try
						{
						result = new BigDecimal( num.toString() );
						}
					catch( NumberFormatException ignored )
						{
						}
					result = Optional.ofNullable( result )
					                 .orElse( BigDecimal.valueOf( num.doubleValue() ) );
					return stripScalingZeroes( result );
					}
				};
	
	abstract BigDecimal getDecimal( Number num );
	
	static BigDecimal of( Number num )
		{
		Objects.requireNonNull( num , "Number is required." );
		return factory( num ).getDecimal( num );
		}
	
	private static ConvertBigDecimal factory( Number num )
		{
		if( INTEGERS.contains( num.getClass() ) )
			{
			return INTEGER;
			}
		if( FLOATINGS.contains( num.getClass() ) )
			{
			return FLUCTUAGE;
			}
		if( num instanceof NumberValue )
			{
			return NUMBERVALUE;
			}
		if( BigDecimal.class.equals( num.getClass() ) )
			{
			return BIGDECIMAL;
			}
		if( num instanceof BigInteger )
			{
			return BIGINTEGER;
			}
		if( num instanceof BigDecimal )
			{
			return BIGDECIMAL_EXTENDS;
			}
		return DEFAULT;
		}
	
	private static final List<Class<? extends Number>> INTEGERS = Arrays.asList( Long.class , Integer.class , Short.class , Byte.class , AtomicLong.class , AtomicInteger.class );
	
	private static final List<Class<? extends Number>> FLOATINGS = Arrays.asList( Float.class , Double.class );
	
	private static BigDecimal stripScalingZeroes( BigDecimal result )
		{
		if( result.signum() == 0 )
			{
			return BigDecimal.ZERO;
			}
		if( result.scale() > 0 )
			{
			return result.stripTrailingZeros();
			}
		return result;
		}
	}
