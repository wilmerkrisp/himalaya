package life.expert.value.numeric.utils;
//-------------------------------------------------------------------------------------------------------
//  __    __   __  .___  ___.      ___       __          ___   ____    ____  ___   ____    ____  ___
// |  |  |  | |  | |   \/   |     /   \     |  |        /   \  \   \  /   / /   \  \   \  /   / /   \
// |  |__|  | |  | |  \  /  |    /  ^  \    |  |       /  ^  \  \   \/   / /  ^  \  \   \/   / /  ^  \
// |   __   | |  | |  |\/|  |   /  /_\  \   |  |      /  /_\  \  \_    _/ /  /_\  \  \_    _/ /  /_\  \
// |  |  |  | |  | |  |  |  |  /  _____  \  |  `----./  _____  \   |  |  /  _____  \   |  |  /  _____  \
// |__|  |__| |__| |__|  |__| /__/     \__\ |_______/__/     \__\  |__| /__/     \__\  |__| /__/     \__\
//
//                                            Wilmer Krisp 2019/02/05
//--------------------------------------------------------------------------------------------------------

import life.expert.value.numeric.amount.Quantity;
import life.expert.value.numeric.context.AmountContext;
import life.expert.value.numeric.unit.Unit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

/**
 * This utility class simplifies implementing {@link life.expert.value.numeric.amount.Amount},
 * by providing the common functionality. The different explicitly typed methods
 * are all reduced to methods using {@link java.math.BigDecimal} as input, hereby
 * performing any conversion to {@link java.math.BigDecimal} as needed. Obviously this
 * takes some time, so implementors that want to avoid this overhead should
 * implement {@link life.expert.value.numeric.amount.Amount} directly.
 *
 * @author Anatole Tresch
 */
@UtilityClass
@Slf4j
public class NumberUtils
	{
	
	//<editor-fold desc="big decimapl helper functions">
	
	/**
	 * Creates a {@link BigDecimal} from the given {@link Number} doing the
	 * valid conversion depending the type given.
	 *
	 * @param num
	 * 	the number type
	 *
	 * @return the corresponding {@link BigDecimal}
	 */
	public static BigDecimal getBigDecimal( long num )
		{
		return BigDecimal.valueOf( num );
		}
	
	/**
	 * Creates a {@link BigDecimal} from the given {@link Number} doing the
	 * valid conversion depending the type given.
	 *
	 * @param num
	 * 	the number type
	 *
	 * @return the corresponding {@link BigDecimal}
	 */
	public static BigDecimal getBigDecimal( double num )
		{
		if( Double.isNaN( num ) )
			{
			throw new ArithmeticException( "Invalid input Double.NaN." );
			}
		else if( Double.isInfinite( num ) )
			{
			throw new ArithmeticException( "Invalid input Double.xxx_INFINITY." );
			}
		return new BigDecimal( String.valueOf( num ) );
		}
	
	/**
	 * Creates a {@link BigDecimal} from the given {@link Number} doing the
	 * valid conversion depending the type given.
	 *
	 * @param num
	 * 	the number type
	 *
	 * @return the corresponding {@link BigDecimal}
	 */
	public static BigDecimal getBigDecimal( Number num )
		{
		return ConvertBigDecimal.of( num );
		}
	
	/**
	 * Creates a {@link BigDecimal} from the given {@link Number} doing the
	 * valid conversion depending the type given, if a {@link AmountContext}
	 * is given, it is applied to the number returned.
	 *
	 * @param num
	 * 	the number type
	 * @param amountContext
	 * 	the money context
	 *
	 * @return the corresponding {@link BigDecimal}
	 */
	public static BigDecimal getBigDecimal( Number num ,
	                                        AmountContext amountContext )
		{
		BigDecimal bd = getBigDecimal( num );
		if( Objects.nonNull( amountContext ) )
			{
			var mc = getMathContext( amountContext , RoundingMode.HALF_EVEN );
			bd = new BigDecimal( bd.toString() , mc );
			if( amountContext.getMaxScale() > 0 )
				{
				logger_.trace( "Got Max Scale {}" , amountContext.getMaxScale() );
				bd = bd.setScale( amountContext.getMaxScale() , mc.getRoundingMode() );
				}
			}
		return bd;
		}
	
	/**
	 * Evaluates the {@link MathContext} from the given {@link AmountContext}.
	 *
	 * @param amountContext
	 * 	the {@link AmountContext}
	 * @param defaultMode
	 * 	the default {@link RoundingMode}, to be used if no one is set 	in {@link AmountContext}.
	 *
	 * @return the corresponding {@link MathContext}
	 */
	public static MathContext getMathContext( AmountContext amountContext ,
	                                          RoundingMode defaultMode )
		{
		MathContext ctx = amountContext.getMathContext();
		if( Objects.nonNull( ctx ) )
			{
			return ctx;
			}
		RoundingMode roundingMode = amountContext.getRoundingMode();
		if( roundingMode == null )
			{
			roundingMode = Optional.ofNullable( defaultMode )
			                       .orElse( RoundingMode.HALF_EVEN );
			}
		return new MathContext( amountContext.getPrecision() , roundingMode );
		}
	
	/**
	 * Method to check if a unit is compatible with this amount instance.
	 *
	 * @param amount
	 * 	The amount to be compared to, never null.
	 * @param unit
	 * 	the amount's unit to compare, never null.
	 *
	 * @throws ValueException
	 * 	If the amount is null, or the amount's {@link Unit} is not 	compatible, meaning has a different value of        {@link Unit#getCode()} ()}).
	 */
	public static void checkAmountParameter( Quantity amount ,
	                                         Unit unit )
		{
		Objects.requireNonNull( amount , "Amount must not be null." );
		final Unit unit_of_amount = amount.getUnit();
		if( !( unit.getCode()
		           .equals( unit_of_amount.getCode() ) ) )
			{
			throw new ValueException( "Currency mismatch: " + unit + '/' + unit_of_amount );
			}
		}
	
	/**
	 * Internal method to check for correct number parameter.
	 *
	 * @param number
	 * 	the number to be checked.
	 *
	 * @throws IllegalArgumentException
	 * 	If the number is null
	 */
	public static void checkNumberParameter( Number number )
		{
		Objects.requireNonNull( number , "Number is required." );
		}
	
	//</editor-fold>
	
	/**
	 * Check no infinity or na n.
	 *
	 * @param number
	 * 	the number
	 */
	//<editor-fold desc="number verifier">
	public static void checkNoInfinityOrNaN( Number number )
		{
		if( Double.class != number.getClass() && Float.class != number.getClass() )
			{
			return;
			}
		
		double dValue = number.doubleValue();
		if( Double.isNaN( dValue ) )
			{
			throw new ArithmeticException( "Not a valid input: NaN." );
			}
		else if( Double.isInfinite( dValue ) )
			{
			throw new ArithmeticException( "Not a valid input: INFINITY: " + dValue );
			}
			
		}
	
	/**
	 * Is infinity and not na n boolean.
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the boolean
	 */
	public static boolean isInfinityAndNotNaN( Number number )
		{
		if( Double.class != number.getClass() && Float.class != number.getClass() )
			{
			return false;
			}
		
		double dValue = number.doubleValue();
		if( Double.isNaN( dValue ) )
			{
			throw new ArithmeticException( "Not a valid input: NaN." );
			}
		else if( Double.isInfinite( dValue ) )
			{
			return true;
			}
		
		return false;
		}
	
	//</editor-fold>
	
	}
