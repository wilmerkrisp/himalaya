package life.expert.value.numeric.amount;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.amount
//                           wilmer 2019/04/29
//
//--------------------------------------------------------------------------------









import life.expert.common.async.LogUtils;
import life.expert.value.numeric.context.AmountContext;
import life.expert.value.numeric.utils.AmountParseException;
import life.expert.value.numeric.utils.ValueException;
import life.expert.value.numeric.operators.Operator;
import life.expert.value.numeric.unit.Piece;
import life.expert.value.numeric.utils.DefaultNumberValue;
import life.expert.value.numeric.utils.NumberUtils;
import life.expert.value.numeric.unit.Unit;

import life.expert.value.numeric.utils.NumberValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;









/**
 * <pre>{@code long}</pre> based implementation of {@link Quantity}.This class internally uses a
 * single long number as numeric representation, which basically is interpreted as minor units.
 * It suggested to have a performance advantage of a 10-15 times faster compared to {@link BigAmount},
 * which internally uses {@link java.math.BigDecimal}. Nevertheless this comes with a amount of less precision.
 * As an example performing the following calculation one million times, results in slightly
 * different results:
 *
 * <pre>{@code
 * Amount x = x.add(DefaultAmount.of(1234567.3444, "BOXES"));
 * x = x.subtract(DefaultAmount.of(232323, "BOTTLES"));
 * x = x.multiply(3.4);
 * x = x.divide(5.456);
 * }*</pre>
 */
public final class Amount
	implements Quantity,
	           Comparable<Quantity>
	{
	
	
	
	private static final Logger logger_ = LoggerFactory.getLogger( LogUtils.class );
	
	
	
	private static final void log_( String format ,
	                                Object... arguments )
		{
		logger_.info( format ,
		              arguments );
		}
	
	
	
	private static final void logAtError_( String format ,
	                                       Object... arguments )
		{
		logger_.error( format ,
		               arguments );
		}
	
	
	
	private static final void logAtWarning_( String format ,
	                                         Object... arguments )
		{
		logger_.warn( format ,
		              arguments );
		}
	
	
	//<editor-fold desc="properties & concstants">
	
	
	
	/**
	 * The unit of this amount.
	 */
	private final Unit unit;
	
	
	
	/**
	 * The numeric part of this amount.
	 */
	private final long number;
	
	
	
	/**
	 * The current scale represented by the number.
	 */
	private static final int SCALE = 5;
	
	
	
	/**
	 * The constant DEFAULT_CONTEXT.
	 */
	public static final AmountContext DEFAULT_CONTEXT = AmountContext.of( Amount.class ,
	                                                                      19 ,
	                                                                      true ,
	                                                                      5 ,
	                                                                      RoundingMode.HALF_EVEN );
	
	
	
	/**
	 * The constant MAX_CONTEXT.
	 */
	public static final AmountContext MAX_CONTEXT = AmountContext.of( Amount.class ,
	                                                                  19 ,
	                                                                  true ,
	                                                                  5 ,
	                                                                  RoundingMode.HALF_EVEN );
	
	
	
	/**
	 * Maximum possible value supported, using XX (no unit).
	 */
	public static final Amount MAX_VALUE = new Amount( Long.MAX_VALUE ,
	                                                   Piece.builder()
	                                                        .code( "piece" )
	                                                        .build() );
	
	
	
	/**
	 * Maximum possible numeric value supported.
	 */
	private static final BigDecimal MAX_BD = MAX_VALUE.getBigDecimal();
	
	
	
	/**
	 * Minimum possible value supported, using XX (no unit).
	 */
	public static final Amount MIN_VALUE = new Amount( Long.MIN_VALUE ,
	                                                   Piece.builder()
	                                                        .code( "piece" )
	                                                        .build() );
	
	
	
	/**
	 * Minimum possible numeric value supported.
	 */
	private static final BigDecimal MIN_BD = MIN_VALUE.getBigDecimal();
	
	
	//</editor-fold>
	
	//<editor-fold desc="helper methods">
	
	
	
	private long getInternalNumber( Number number ,
	                                boolean allowInternalRounding )
		{
		BigDecimal bd = NumberUtils.getBigDecimal( number );
		if( !allowInternalRounding && bd.scale() > SCALE )
			{
			throw new ArithmeticException( number + " can not be represented by this class, scale > " + SCALE );
			}
		if( bd.compareTo( MIN_BD ) < 0 )
			{
			throw new ArithmeticException( "Overflow: " + number + " < " + MIN_BD );
			}
		else if( bd.compareTo( MAX_BD ) > 0 )
			{
			throw new ArithmeticException( "Overflow: " + number + " > " + MAX_BD );
			}
		return bd.movePointRight( SCALE )
		         .longValue();
		}
	
	
	
	private void checkAmountParameter( Quantity amount )
		{
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		// numeric check for overflow...
		if( amount.getNumber()
		          .getScale() > SCALE )
			{
			throw new ArithmeticException( "Parameter exceeds maximal scale: " + SCALE );
			}
		if( amount.getNumber()
		          .getPrecision() > MAX_BD.precision() )
			{
			throw new ArithmeticException( "Parameter exceeds maximal precision: " + SCALE );
			}
		}
	
	
	
	// Internal helper methods
	
	
	
	/**
	 * Internal method to check for correct number parameter.
	 *
	 * @param number
	 * 	the number to be checked, including null..
	 *
	 * @throws NullPointerException
	 * 	If the number is null
	 * @throws java.lang.ArithmeticException
	 * 	If the number exceeds the capabilities of this class.
	 */
	protected void checkNumber( Number number )
		{
		Objects.requireNonNull( number ,
		                        "Number is required." );
		// numeric check for overflow...
		if( number.longValue() > MAX_BD.longValue() )
			{
			throw new ArithmeticException( "Value exceeds maximal value: " + MAX_BD );
			}
		BigDecimal bd = NumberUtils.getBigDecimal( number );
		if( bd.precision() > MAX_BD.precision() )
			{
			throw new ArithmeticException( "Precision exceeds maximal precision: " + MAX_BD.precision() );
			}
		if( bd.scale() > SCALE )
			{
			throw new ArithmeticException( "Scale of " + bd + " exceeds maximal scale: " + SCALE );
			
			}
		}
	
	
	
	private boolean isOne( Number number )
		{
		BigDecimal bd = NumberUtils.getBigDecimal( number );
		try
			{
			return bd.scale() == 0 && bd.longValueExact() == 1L;
			}
		catch( Exception e )
			{
			// The only way to end up here is that longValueExact throws an ArithmeticException,
			// so the amount is definitively not equal to 1.
			return false;
			}
		}
	
	
	
	private BigDecimal getBigDecimal()
		{
		return BigDecimal.valueOf( this.number )
		                 .movePointLeft( SCALE );
		}
	
	
	
	/**
	 * Is less than boolean.
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the boolean
	 */
	public final boolean isLessThan( Number number )
		{
		checkNumber( number );
		return getBigDecimal().compareTo( NumberUtils.getBigDecimal( number ) ) < 0;
		}
	
	
	
	/**
	 * Is less than or equal to boolean.
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the boolean
	 */
	public final boolean isLessThanOrEqualTo( Number number )
		{
		checkNumber( number );
		return getBigDecimal().compareTo( NumberUtils.getBigDecimal( number ) ) <= 0;
		}
	
	
	
	/**
	 * Is greater than boolean.
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the boolean
	 */
	public final boolean isGreaterThan( Number number )
		{
		checkNumber( number );
		return getBigDecimal().compareTo( NumberUtils.getBigDecimal( number ) ) > 0;
		}
	
	
	
	/**
	 * Is greater than or equal to boolean.
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the boolean
	 */
	public final boolean isGreaterThanOrEqualTo( Number number )
		{
		checkNumber( number );
		return getBigDecimal().compareTo( NumberUtils.getBigDecimal( number ) ) >= 0;
		}
	
	
	
	/**
	 * Has same number as boolean.
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the boolean
	 */
	public final boolean hasSameNumberAs( Number number )
		{
		checkNumber( number );
		try
			{
			return this.number == getInternalNumber( number ,
			                                         false );
			}
		catch( ArithmeticException e )
			{
			return false;
			}
		}
	
	
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="constructors & fabrics">
	
	
	
	/**
	 * Creates a new instance os {@link Amount}.
	 *
	 * @param unit
	 * 	the unit, not null.
	 * @param number
	 * 	the amount, not null.
	 */
	private Amount( Number number ,
	                Unit unit ,
	                boolean allowInternalRounding )
		{
		Objects.requireNonNull( unit ,
		                        "Unit is required." );
		this.unit = unit;
		Objects.requireNonNull( number ,
		                        "Number is required." );
		this.number = getInternalNumber( number ,
		                                 allowInternalRounding );
		}
	
	
	
	/**
	 * Creates a new instance os {@link Amount}.
	 *
	 * @param unit
	 * 	the unit, not null.
	 * @param numberValue
	 * 	the numeric value, not null.
	 */
	private Amount( NumberValue numberValue ,
	                Unit unit ,
	                boolean allowInternalRounding )
		{
		Objects.requireNonNull( unit ,
		                        "Unit is required." );
		this.unit = unit;
		Objects.requireNonNull( numberValue ,
		                        "Number is required." );
		this.number = getInternalNumber( numberValue.numberValue( BigDecimal.class ) ,
		                                 allowInternalRounding );
		}
	
	
	
	/**
	 * Creates a new instance os {@link Amount}.
	 *
	 * @param number
	 * 	The format number value
	 * @param unit
	 * 	the unit, not null.
	 */
	private Amount( long number ,
	                Unit unit )
		{
		Objects.requireNonNull( unit ,
		                        "Unit is required." );
		this.unit = unit;
		this.number = number;
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link Amount}.
	 *
	 * @param numberBinding
	 * 	The numeric part, not null.
	 * @param unit
	 * 	The target unit, not null.
	 *
	 * @return A new instance of {@link Amount}.
	 */
	public static Amount of( NumberValue numberBinding ,
	                         Unit unit )
		{
		return new Amount( numberBinding ,
		                   unit ,
		                   false );
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link Amount}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unit
	 * 	The target unit, not null.
	 *
	 * @return A new instance of {@link Amount}.
	 */
	public static Amount of( Number number ,
	                         Unit unit )
		{
		return new Amount( number ,
		                   unit ,
		                   false );
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link Amount}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as unit code.
	 *
	 * @return A new instance of {@link Amount}.
	 */
	public static Amount of( Number number ,
	                         String unitCode )
		{
		Unit unit = Piece.of( unitCode );
		return of( number ,
		           unit );
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link Amount}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 *
	 * @return A new instance of {@link Amount}.
	 */
	public static Amount of( Number number )
		{
		Unit unit = Piece.of( "piece" );
		return of( number ,
		           unit );
		}
	
	
	
	/**
	 * Obtains an instance of {@link Amount} representing zero.
	 *
	 * @param unit
	 * 	the target unit
	 *
	 * @return an instance of {@link Amount} representing zero.
	 *
	 * @since 1.0.1
	 */
	public static Amount zero( Unit unit )
		{
		return of( BigDecimal.ZERO ,
		           unit );
		}
	
	
	
	/**
	 * Obtains an instance of {@code DefaultAmount} from an amount in minor units.
	 * For example, {@code ofMinor(USD, 1234)} creates the instance {@code USD 12.34}.
	 *
	 * @param unit
	 * 	the unit, not null
	 * @param amountMinor
	 * 	the amount of units in the minor division of the unit
	 *
	 * @return the amount from minor units
	 *
	 * @throws NullPointerException
	 * 	when the unit is null
	 * @throws IllegalArgumentException
	 * 	when {@link Unit#getDefaultFractionDigits()} is lesser than zero.
	 * @see Unit#getDefaultFractionDigits() Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()
	 * @see Amount#ofMinor(Unit , long , int) Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long ,
	 * 	int)Amount#ofMinor(Unit , long , 	int)
	 */
	public static Amount ofMinor( Unit unit ,
	                              long amountMinor )
		{
		return ofMinor( unit ,
		                amountMinor ,
		                unit.getDefaultFractionDigits() );
		}
	
	
	
	/**
	 * Obtains an instance of {@code DefaultAmount} from an amount in minor units.
	 * For example, {@code ofMinor(USD, 1234, 2)} creates the instance {@code USD 12.34}.
	 *
	 * @param unit
	 * 	the unit, not null
	 * @param amountMinor
	 * 	the amount in the minor division of the unit
	 * @param factionDigits
	 * 	number of digits
	 *
	 * @return the unit's amount from minor units
	 *
	 * @throws NullPointerException
	 * 	when the unit is null
	 * @throws IllegalArgumentException
	 * 	when the factionDigits is negative
	 * @see Unit#getDefaultFractionDigits() Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()
	 */
	public static Amount ofMinor( Unit unit ,
	                              long amountMinor ,
	                              int factionDigits )
		{
		if( factionDigits < 0 )
			{
			throw new IllegalArgumentException( "The factionDigits cannot be negative" );
			}
		return of( BigDecimal.valueOf( amountMinor ,
		                               factionDigits ) ,
		           unit );
		}
	
	
	
	/**
	 * From amount.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the amount
	 */
	public static Amount from( Quantity amount )
		{
		if( Amount.class.isInstance( amount ) )
			{
			return Amount.class.cast( amount );
			}
		return new Amount( amount.getNumber() ,
		                   amount.getUnit() ,
		                   false );
		}
	
	
	
	/**
	 * Obtains an instance of DefaultAmount from a text string such as 'piece 25.25'.
	 *
	 * @param text
	 * 	the text to parse not null
	 *
	 * @return DefaultAmount instance
	 *
	 * @throws NumberFormatException
	 * 	if the amount is not a number
	 * @throws life.expert.value.numeric.utils.UnknownUnitException
	 * 	if the unit cannot be resolved
	 */
	public static Amount parse( CharSequence text )
		{
		
		String[] array = Objects.requireNonNull( text )
		                        .toString()
		                        .split( " " );
		if( array.length != 2 )
			{
			throw new AmountParseException( "An error happened when try to parse the Amount." ,
			                                text ,
			                                0 );
			}
		
		String parsed_unit = array[0];
		
		
		
		BigDecimal number = new BigDecimal( array[1] );
		return Amount.of( number ,
		                  Piece.of( parsed_unit ) );
			
			
		}
	
	
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="interface methods">
	
	
	
	/**
	 * Returns the amount’s unit, modelled as {@link Unit}.
	 * Implementations may co-variantly change the return type to a more
	 * specific implementation of {@link Unit} if desired.
	 *
	 * @return the unit, never {@code null}
	 *
	 * @see Quantity#getUnit()
	 */
	@Override
	public Unit getUnit()
		{
		return unit;
		}
	
	
	
	/**
	 * Access the {@link AmountContext} used by this instance.
	 *
	 * @return the {@link AmountContext} used, never null.
	 *
	 * @see Quantity#getContext()
	 */
	@Override
	public AmountContext getContext()
		{
		return DEFAULT_CONTEXT;
		}
	
	
	
	/**
	 * Compare to int.
	 *
	 * @param o
	 * 	the o
	 *
	 * @return the int
	 */
	@Override
	public int compareTo( Quantity o )
		{
		Objects.requireNonNull( o );
		int compare = getUnit().getCode()
		                       .compareTo( o.getUnit()
		                                    .getCode() );
		if( compare == 0 )
			{
			compare = getNumber().numberValue( BigDecimal.class )
			                     .compareTo( o.getNumber()
			                                  .numberValue( BigDecimal.class ) );
			}
		return compare;
		}
	
	
	
	@Override
	public int hashCode()
		{
		return Objects.hash( unit ,
		                     number );
		}
	
	
	
	@Override
	public boolean equals( Object obj )
		{
		if( obj == this )
			{
			return true;
			}
		if( obj instanceof Amount )
			{
			Amount other = (Amount) obj;
			return Objects.equals( unit ,
			                       other.unit ) && Objects.equals( number ,
			                                                       other.number );
			}
		return false;
		}
	
	
	
	@Override
	public Amount abs()
		{
		if( this.isPositiveOrZero() )
			{
			return this;
			}
		return this.negate();
		}
	
	
	
	/**
	 * Add amount.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the amount
	 */
	@Override
	public Amount add( Quantity amount )
		{
		checkAmountParameter( amount );
		if( amount.isZero() )
			{
			return this;
			}
		return new Amount( Math.addExact( this.number ,
		                                  getInternalNumber( amount.getNumber() ,
		                                                     false ) ) ,
		                   getUnit() );
		}
	
	
	
	@Override
	public Amount divide( Number divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new Amount( 0L ,
			                   getUnit() );
			}
		checkNumber( divisor );
		if( isOne( divisor ) )
			{
			return this;
			}
		return new Amount( Math.round( this.number / divisor.doubleValue() ) ,
		                   getUnit() );
		}
	
	
	
	@Override
	public Amount[] divideAndRemainder( Number divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			Amount zero = new Amount( 0L ,
			                          getUnit() );
			return new Amount[] { zero ,
			                      zero };
			}
		checkNumber( divisor );
		BigDecimal   div = NumberUtils.getBigDecimal( divisor );
		BigDecimal[] res = getBigDecimal().divideAndRemainder( div );
		return new Amount[] { new Amount( res[0] ,
		                                  getUnit() ,
		                                  true ) ,
		                      new Amount( res[1] ,
		                                  getUnit() ,
		                                  true ) };
		}
	
	
	
	@Override
	public Amount divideToIntegralValue( Number divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new Amount( 0L ,
			                   getUnit() );
			}
		checkNumber( divisor );
		if( isOne( divisor ) )
			{
			return this;
			}
		BigDecimal div = NumberUtils.getBigDecimal( divisor );
		return new Amount( getBigDecimal().divideToIntegralValue( div ) ,
		                   getUnit() ,
		                   false );
		}
	
	
	
	@Override
	public Amount multiply( Number multiplicand )
		{
		NumberUtils.checkNoInfinityOrNaN( multiplicand );
		checkNumber( multiplicand );
		if( isOne( multiplicand ) )
			{
			return this;
			}
		return new Amount( Math.multiplyExact( this.number ,
		                                       getInternalNumber( multiplicand ,
		                                                          false ) ) / 100000L ,
		                   getUnit() );
		}
	
	
	
	@Override
	public Amount negate()
		{
		return new Amount( Math.multiplyExact( this.number ,
		                                       -1 ) ,
		                   getUnit() );
		}
	
	
	
	@Override
	public Amount plus()
		{
		return this;
		}
	
	
	
	/**
	 * Subtract amount.
	 *
	 * @param amount
	 * 	the subtrahend
	 *
	 * @return the amount
	 */
	@Override
	public Amount subtract( Quantity amount )
		{
		checkAmountParameter( amount );
		if( amount.isZero() )
			{
			return this;
			}
		return new Amount( Math.subtractExact( this.number ,
		                                       getInternalNumber( amount.getNumber() ,
		                                                          false ) ) ,
		                   getUnit() );
		}
	
	
	
	@Override
	public Amount remainder( Number divisor )
		{
		checkNumber( divisor );
		return new Amount( this.number % getInternalNumber( divisor ,
		                                                    false ) ,
		                   getUnit() );
		}
	
	
	
	@Override
	public Amount scaleByPowerOfTen( int power )
		{
		return new Amount( getNumber().numberValue( BigDecimal.class )
		                              .scaleByPowerOfTen( power ) ,
		                   getUnit() ,
		                   true );
		}
	
	
	
	@Override
	public boolean isZero()
		{
		return this.number == 0L;
		}
	
	
	
	@Override
	public boolean isPositive()
		{
		return this.number > 0L;
		}
	
	
	
	@Override
	public boolean isPositiveOrZero()
		{
		return this.number >= 0L;
		}
	
	
	
	@Override
	public boolean isNegative()
		{
		return this.number < 0L;
		}
	
	
	
	@Override
	public boolean isNegativeOrZero()
		{
		return this.number <= 0L;
		}
	
	
	
	/**
	 * Gets scale.
	 *
	 * @return the scale
	 */
	public int getScale()
		{
		return Amount.SCALE;
		}
	
	
	
	/**
	 * Gets precision.
	 *
	 * @return the precision
	 */
	public int getPrecision()
		{
		return getNumber().numberValue( BigDecimal.class )
		                  .precision();
		}
	
	
	
	@Override
	public int signum()
		{
		if( this.number < 0 )
			{
			return -1;
			}
		if( this.number == 0 )
			{
			return 0;
			}
		return 1;
		}
	
	
	
	/**
	 * Is less than boolean.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isLessThan( Quantity amount )
		{
		checkAmountParameter( amount );
		return getBigDecimal().compareTo( amount.getNumber()
		                                        .numberValue( BigDecimal.class ) ) < 0;
		}
	
	
	
	/**
	 * Is less than or equal to boolean.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isLessThanOrEqualTo( Quantity amount )
		{
		checkAmountParameter( amount );
		return getBigDecimal().compareTo( amount.getNumber()
		                                        .numberValue( BigDecimal.class ) ) <= 0;
		}
	
	
	
	/**
	 * Is greater than boolean.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isGreaterThan( Quantity amount )
		{
		checkAmountParameter( amount );
		return getBigDecimal().compareTo( amount.getNumber()
		                                        .numberValue( BigDecimal.class ) ) > 0;
		}
	
	
	
	/**
	 * Is greater than or equal to boolean.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isGreaterThanOrEqualTo( Quantity amount )
		{
		checkAmountParameter( amount );
		return getBigDecimal().compareTo( amount.getNumber()
		                                        .numberValue( BigDecimal.class ) ) >= 0;
		}
	
	
	
	/**
	 * Is equal to boolean.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isEqualTo( Quantity amount )
		{
		checkAmountParameter( amount );
		return getBigDecimal().compareTo( amount.getNumber()
		                                        .numberValue( BigDecimal.class ) ) == 0;
		}
	
	
	
	/**
	 * Gets the number representation of the numeric value of this item.
	 *
	 * @return The {@link Number} representation matching best.
	 */
	@Override
	public NumberValue getNumber()
		{
		return new DefaultNumberValue( getBigDecimal() );
		}
	
	
	
	@Override
	public String toString()
		{
		
		return unit.toString() + ' ' + getBigDecimal();
		}
	
	
	
	/**
	 * With amount.
	 *
	 * @param operator
	 * 	the operator
	 *
	 * @return the amount
	 */
	@Override
	public Amount with( Operator operator )
		{
		Objects.requireNonNull( operator );
		try
			{
			return Amount.class.cast( operator.apply( this ) );
			}
		catch( ArithmeticException e )
			{
			throw e;
			}
		catch( Exception e )
			{
			throw new ValueException( "Operator failed: " + operator ,
			                          e );
			}
		}
	
	
	
	@Override
	public Amount multiply( double multiplicand )
		{
		NumberUtils.checkNoInfinityOrNaN( multiplicand );
		if( multiplicand == 1.0 )
			{
			return this;
			}
		if( multiplicand == 0.0 )
			{
			return new Amount( 0 ,
			                   this.unit );
			}
		return new Amount( Math.round( this.number * multiplicand ) ,
		                   this.unit );
		}
	
	
	
	@Override
	public Amount divide( long divisor )
		{
		if( divisor == 1L )
			{
			return this;
			}
		return new Amount( this.number / divisor ,
		                   this.unit );
		}
	
	
	
	@Override
	public Amount divide( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new Amount( 0L ,
			                   getUnit() );
			}
		if( divisor == 1.0d )
			{
			return this;
			}
		return new Amount( Math.round( this.number / divisor ) ,
		                   getUnit() );
		}
	
	
	
	@Override
	public Amount remainder( long divisor )
		{
		return remainder( BigDecimal.valueOf( divisor ) );
		}
	
	
	
	@Override
	public Amount remainder( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new Amount( 0L ,
			                   getUnit() );
			}
		return remainder( new BigDecimal( String.valueOf( divisor ) ) );
		}
	
	
	
	@Override
	public Amount[] divideAndRemainder( long divisor )
		{
		return divideAndRemainder( BigDecimal.valueOf( divisor ) );
		}
	
	
	
	@Override
	public Amount[] divideAndRemainder( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			Amount zero = new Amount( 0L ,
			                          getUnit() );
			return new Amount[] { zero ,
			                      zero };
			}
		else if( Double.isNaN( divisor ) )
			{
			throw new ArithmeticException( "Not a number: NaN." );
			}
		return divideAndRemainder( new BigDecimal( String.valueOf( divisor ) ) );
		}
	
	
	
	@Override
	public Amount stripTrailingZeros()
		{
		return this;
		}
	
	
	
	@Override
	public Amount multiply( long multiplicand )
		{
		if( multiplicand == 1 )
			{
			return this;
			}
		if( multiplicand == 0 )
			{
			return new Amount( 0L ,
			                   this.unit );
			}
		return new Amount( Math.multiplyExact( multiplicand ,
		                                       this.number ) ,
		                   this.unit );
		}
	
	
	
	@Override
	public Amount divideToIntegralValue( long divisor )
		{
		if( divisor == 1 )
			{
			return this;
			}
		return divideToIntegralValue( NumberUtils.getBigDecimal( divisor ) );
		}
	
	
	
	@Override
	public Amount divideToIntegralValue( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new Amount( 0L ,
			                   getUnit() );
			}
		if( divisor == 1.0 )
			{
			return this;
			}
		return divideToIntegralValue( NumberUtils.getBigDecimal( divisor ) );
		}
	
	
	
	//</editor-fold>
	
	}
