package life.expert.value.amount;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.amount
//                           wilmer 2019/04/29
//
//--------------------------------------------------------------------------------









import life.expert.value.context.AmountContext;
import life.expert.value.operators.Operator;
import life.expert.value.unit.Piece;
import life.expert.value.unit.Unit;
import life.expert.value.utils.AmountParseException;
import life.expert.value.utils.DefaultNumberValue;
import life.expert.value.utils.NumberUtils;
import life.expert.value.utils.NumberValue;
import life.expert.value.utils.ValueException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;









/**
 * Default immutable implementation of {@link Quantity} based
 * on {@link java.math.BigDecimal} as numeric representation.
 *
 * As required by {@link Quantity} this class is final, thread-safe,
 * immutable and serializable.
 *
 * This class can be configured with an arbitrary {@link AmountContext}. The
 * default {@link AmountContext} used models by default the same settings as
 * {@link java.math.MathContext#DECIMAL64} .
 */
public final class BigAmount
	implements Quantity,
	           Comparable<Quantity>
	{
	
	
	
	/**
	 * The constant DEFAULT_CONTEXT.
	 */
	public static final AmountContext DEFAULT_CONTEXT = AmountContext.of( BigAmount.class ,
	                                                                      256 ,
	                                                                      false ,
	                                                                      63 ,
	                                                                      RoundingMode.HALF_EVEN );
	
	
	
	/**
	 * The constant MAX_CONTEXT.
	 */
	public static final AmountContext MAX_CONTEXT = AmountContext.of( BigAmount.class ,
	                                                                  0 ,
	                                                                  false ,
	                                                                  -1 ,
	                                                                  RoundingMode.HALF_EVEN );
	
	
	
	/**
	 * The unit of this amount.
	 */
	private final Unit unit;
	
	
	
	/**
	 * the {@link AmountContext} used by this instance, e.g. on division.
	 */
	private final AmountContext amountContext;
	
	
	
	/**
	 * The numeric part of this amount.
	 */
	private final BigDecimal number;
	
	
	
	//<editor-fold desc="constructors and fabrics">
	
	
	
	/**
	 * Creates a new instance os {@link BigAmount}.
	 *
	 * @param unit
	 * 	the unit, not null.
	 * @param number
	 * 	the amount, not null.
	 *
	 * @throws ArithmeticException
	 * 	If the number exceeds the capabilities of the default
	 *        {@link AmountContext}.
	 */
	private BigAmount( BigDecimal number ,
	                   Unit unit )
		{
		this( number ,
		      unit ,
		      null );
		}
	
	
	
	/**
	 * Creates a new instance of {@link BigAmount}.
	 *
	 * @param unit
	 * 	the unit, not {@code null}.
	 * @param number
	 * 	the amount, not {@code null}.
	 * @param amountContext
	 * 	the {@link AmountContext}, if {@code null}, the default is
	 * 	used.
	 *
	 * @throws ArithmeticException
	 * 	If the number exceeds the capabilities of the
	 *        {@link AmountContext} used.
	 */
	private BigAmount( BigDecimal number ,
	                   Unit unit ,
	                   AmountContext amountContext )
		{
		Objects.requireNonNull( unit ,
		                        "Unit is required." );
		this.unit = unit;
		if( Objects.nonNull( amountContext ) )
			{
			this.amountContext = amountContext;
			}
		else
			{
			this.amountContext = DEFAULT_CONTEXT;
			}
		Objects.requireNonNull( number ,
		                        "Number is required." );
		this.number = NumberUtils.getBigDecimal( number ,
		                                         amountContext );
		}
	
	
	
	/**
	 * Creates a new instance of {@link BigAmount}, using the default
	 * {@link AmountContext}.
	 *
	 * @param number
	 * 	numeric value, not {@code null}.
	 * @param unit
	 * 	unit unit, not {@code null}.
	 *
	 * @return a {@code BigAmount} combining the numeric value and unit unit.
	 *
	 * @throws ArithmeticException
	 * 	If the number exceeds the capabilities of the default        {@link AmountContext} used.
	 */
	public static BigAmount of( BigDecimal number ,
	                            Unit unit )
		{
		return new BigAmount( number ,
		                      unit );
		}
	
	
	
	/**
	 * Creates a new instance of {@link BigAmount}, using an explicit
	 * {@link AmountContext}.
	 *
	 * @param number
	 * 	numeric value, not {@code null}.
	 * @param unit
	 * 	unit unit, not {@code null}.
	 * @param amountContext
	 * 	the {@link AmountContext} to be used, if {@code null} the 	default {@link AmountContext} is used.
	 *
	 * @return a {@code BigAmount} instance based on the monetary context with the 	given numeric value, unit unit.
	 *
	 * @throws ArithmeticException
	 * 	If the number exceeds the capabilities of the        {@link AmountContext} used.
	 */
	public static BigAmount of( BigDecimal number ,
	                            Unit unit ,
	                            AmountContext amountContext )
		{
		return new BigAmount( number ,
		                      unit ,
		                      amountContext );
		}
	
	
	
	/**
	 * Creates a new instance of {@link BigAmount}, using the default
	 * {@link AmountContext}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unit
	 * 	The target unit, not null.
	 *
	 * @return A new instance of {@link BigAmount}.
	 *
	 * @throws ArithmeticException
	 * 	If the number exceeds the capabilities of the default        {@link AmountContext} used.
	 */
	public static BigAmount of( Number number ,
	                            Unit unit )
		{
		return new BigAmount( NumberUtils.getBigDecimal( number ) ,
		                      unit );
		}
	
	
	
	/**
	 * Creates a new instance of {@link BigAmount}, using an explicit
	 * {@link AmountContext}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unit
	 * 	The target unit, not null.
	 * @param amountContext
	 * 	the {@link AmountContext} to be used, if {@code null} the 	default {@link AmountContext} is used.
	 *
	 * @return A new instance of {@link BigAmount}.
	 *
	 * @throws ArithmeticException
	 * 	If the number exceeds the capabilities of the        {@link AmountContext} used.
	 */
	public static BigAmount of( Number number ,
	                            Unit unit ,
	                            AmountContext amountContext )
		{
		return new BigAmount( NumberUtils.getBigDecimal( number ) ,
		                      unit ,
		                      amountContext );
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link BigAmount}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 *
	 * @return A new instance of {@link BigAmount}.
	 */
	public static BigAmount of( Number number ,
	                            String unitCode )
		{
		return new BigAmount( NumberUtils.getBigDecimal( number ) ,
		                      Piece.of( unitCode ) );
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link BigAmount}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 *
	 * @return A new instance of {@link BigAmount}.
	 */
	public static BigAmount of( BigDecimal number ,
	                            String unitCode )
		{
		return new BigAmount( number ,
		                      Piece.of( unitCode ) );
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link BigAmount}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 * @param amountContext
	 * 	the {@link AmountContext} to be used, if {@code null} the 	default {@link AmountContext} is used.
	 *
	 * @return A new instance of {@link BigAmount}.
	 */
	public static BigAmount of( Number number ,
	                            String unitCode ,
	                            AmountContext amountContext )
		{
		return new BigAmount( NumberUtils.getBigDecimal( number ) ,
		                      Piece.of( unitCode ) ,
		                      amountContext );
		}
	
	
	
	/**
	 * Static factory method for creating a new instance of {@link BigAmount}.
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 * @param amountContext
	 * 	the {@link AmountContext} to be used, if {@code null} the 	default {@link AmountContext} is used.
	 *
	 * @return A new instance of {@link BigAmount}.
	 */
	public static BigAmount of( BigDecimal number ,
	                            String unitCode ,
	                            AmountContext amountContext )
		{
		return new BigAmount( number ,
		                      Piece.of( unitCode ) ,
		                      amountContext );
		}
	
	
	
	/**
	 * Obtains an instance of {@link BigAmount} representing zero.
	 *
	 * @param unit
	 * 	the unit, not null.
	 *
	 * @return an instance of {@link BigAmount} representing zero.
	 */
	public static BigAmount zero( Unit unit )
		{
		return new BigAmount( BigDecimal.ZERO ,
		                      unit );
		}
	
	
	
	/**
	 * Obtains an instance of {@code BigAmount} from an amount in minor units.
	 * For example, {@code ofMinor(USD, 1234)} creates the instance {@code USD 12.34}.
	 *
	 * @param unit
	 * 	the unit
	 * @param amountMinor
	 * 	the amount of unit in the minor division of the unit
	 *
	 * @return the BigAmount from minor units
	 *
	 * @throws NullPointerException
	 * 	when the unit is null
	 * @throws IllegalArgumentException
	 * 	when {@link Unit#getDefaultFractionDigits()} is lesser than zero.
	 * @see Unit#getDefaultFractionDigits() Unit#getDefaultFractionDigits()
	 * @since 1.0.1
	 */
	public static BigAmount ofMinor( Unit unit ,
	                                 long amountMinor )
		{
		return ofMinor( unit ,
		                amountMinor ,
		                unit.getDefaultFractionDigits() );
		}
	
	
	
	/**
	 * Obtains an instance of {@code BigAmount} from an amount in minor units.
	 * For example, {@code ofMinor(USD, 1234, 2)} creates the instance {@code USD 12.34}.
	 *
	 * @param unit
	 * 	the unit, not null
	 * @param amountMinor
	 * 	the amount of unit in the minor division of the unit
	 * @param fractionDigits
	 * 	number of digits
	 *
	 * @return the monetary amount from minor units
	 *
	 * @throws NullPointerException
	 * 	when the unit is null
	 * @throws IllegalArgumentException
	 * 	when the fractionDigits is negative
	 * @see Unit#getDefaultFractionDigits() Unit#getDefaultFractionDigits()
	 * @see BigAmount#ofMinor(Unit , long , int) BigAmount#ofMinor(Unit , long , int)
	 * @since 1.0.1
	 */
	public static BigAmount ofMinor( Unit unit ,
	                                 long amountMinor ,
	                                 int fractionDigits )
		{
		if( fractionDigits < 0 )
			{
			throw new IllegalArgumentException( "The fractionDigits cannot be negative" );
			}
		return of( BigDecimal.valueOf( amountMinor ,
		                               fractionDigits ) ,
		           unit );
		}
	
	
	
	/**
	 * Converts (if necessary) the given {@link Quantity} to a
	 * {@link BigAmount} instance. The {@link AmountContext} will be adapted as
	 * necessary, if the precision of the given amount exceeds the capabilities
	 * of the default {@link AmountContext}.
	 *
	 * @param amt
	 * 	the amount to be converted
	 *
	 * @return an according BigAmount instance.
	 */
	public static BigAmount from( Quantity amt )
		{
		if( amt.getClass() == BigAmount.class )
			{
			return (BigAmount) amt;
			}
		return BigAmount.of( amt.getNumber()
		                        .numberValue( BigDecimal.class ) ,
		                     amt.getUnit() ,
		                     (AmountContext) amt.getContext() );
		}
	
	
	
	/**
	 * Obtains an instance of BigAmount from a text string such as 'EUR 25.25'.
	 *
	 * @param text
	 * 	the text to parse not null
	 *
	 * @return BigAmount instance
	 *
	 * @throws NumberFormatException
	 * 	if the amount is not a number
	 * @throws life.expert.value.utils.UnknownUnitException
	 * 	if the unit cannot be resolved
	 */
	public static BigAmount parse( CharSequence text )
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
		return BigAmount.of( number ,
		                     Piece.of( parsed_unit ) );
			
			
			
		}
	
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="helper methods">
	
	
	
	/**
	 * Method that returns BigDecimal.ZERO, if {@link #isZero()}, and
	 * {@link #number #stripTrailingZeros()} in all other cases.
	 *
	 * @return the stripped number value.
	 */
	public BigDecimal getNumberStripped()
		{
		if( isZero() )
			{
			return BigDecimal.ZERO;
			}
		return this.number.stripTrailingZeros();
		}
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="implemented interface">
	
	
	
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
		return amountContext;
		}
	
	
	
	/**
	 * Gets the number representation of the numeric value of this item.
	 *
	 * @return The {@link Number} representation matching best.
	 */
	@Override
	public NumberValue getNumber()
		{
		return new DefaultNumberValue( number );
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
			compare = this.number.compareTo( BigAmount.from( o ).number );
			}
		return compare;
		}
	
	// Arithmetic Operations
	
	
	
	@Override
	public BigAmount abs()
		{
		if( this.isPositiveOrZero() )
			{
			return this;
			}
		return negate();
		}
	
	
	
	@Override
	public BigAmount divide( long divisor )
		{
		if( divisor == 1L )
			{
			return this;
			}
		return divide( BigDecimal.valueOf( divisor ) );
		}
	
	
	
	@Override
	public BigAmount divide( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return BigAmount.of( 0 ,
			                     getUnit() );
			}
		if( divisor == 1.0d )
			{
			return this;
			}
		return divide( new BigDecimal( String.valueOf( divisor ) ) );
		}
	
	
	
	@Override
	public BigAmount[] divideAndRemainder( long divisor )
		{
		return divideAndRemainder( BigDecimal.valueOf( divisor ) );
		}
	
	
	
	@Override
	public BigAmount[] divideAndRemainder( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			BigAmount zero = BigAmount.of( 0 ,
			                               getUnit() );
			return new BigAmount[] { zero ,
			                         zero };
			}
		return divideAndRemainder( new BigDecimal( String.valueOf( divisor ) ) );
		}
	
	
	
	@Override
	public BigAmount multiply( long multiplicand )
		{
		if( multiplicand == 1L )
			{
			return this;
			}
		return multiply( BigDecimal.valueOf( multiplicand ) );
		}
	
	
	
	@Override
	public BigAmount multiply( double multiplicand )
		{
		NumberUtils.checkNoInfinityOrNaN( multiplicand );
		if( multiplicand == 1.0d )
			{
			return this;
			}
		return multiply( new BigDecimal( String.valueOf( multiplicand ) ) );
		}
	
	
	
	@Override
	public BigAmount remainder( long divisor )
		{
		return remainder( BigDecimal.valueOf( divisor ) );
		}
	
	
	
	@Override
	public BigAmount remainder( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return BigAmount.of( 0 ,
			                     getUnit() );
			}
		return remainder( new BigDecimal( String.valueOf( divisor ) ) );
		}
	
	
	
	@Override
	public boolean isZero()
		{
		return signum() == 0;
		}
	
	
	
	@Override
	public boolean isPositive()
		{
		return signum() == 1;
		}
	
	
	
	@Override
	public boolean isPositiveOrZero()
		{
		return signum() >= 0;
		}
	
	
	
	@Override
	public boolean isNegative()
		{
		return signum() == -1;
		}
	
	
	
	@Override
	public boolean isNegativeOrZero()
		{
		return signum() <= 0;
		}
	
	
	
	/**
	 * With unit.
	 *
	 * @param operator
	 * 	the operator
	 *
	 * @return the unit
	 */
	
	@Override
	public BigAmount with( Operator operator )
		{
		Objects.requireNonNull( operator );
		try
			{
			return BigAmount.class.cast( operator.apply( this ) );
			}
		catch( ValueException e )
			{
			throw e;
			}
		catch( Exception e )
			{
			throw new ValueException( "Operator failed: " + operator ,
			                          e );
			}
		}
	
	
	
	/**
	 * Add unit.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the unit
	 */
	
	@Override
	public BigAmount add( Quantity amount )
		{
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		if( amount.isZero() )
			{
			return this;
			}
		MathContext mc = NumberUtils.getMathContext( amountContext ,
		                                             RoundingMode.HALF_EVEN );
		return new BigAmount( this.number.add( amount.getNumber()
		                                             .numberValue( BigDecimal.class ) ,
		                                       mc ) ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public BigAmount divide( Number divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return BigAmount.of( 0 ,
			                     getUnit() );
			}
		BigDecimal divisorBD = NumberUtils.getBigDecimal( divisor );
		if( divisorBD.equals( BigDecimal.ONE ) )
			{
			return this;
			}
		MathContext mc = NumberUtils.getMathContext( amountContext ,
		                                             RoundingMode.HALF_EVEN );
		int        maxScale = amountContext.getMaxScale();
		BigDecimal dec;
		if( maxScale > 0 )
			{
			return new BigAmount( this.number.divide( divisorBD ,
			                                          maxScale ,
			                                          mc.getRoundingMode() ) ,
			                      getUnit() ,
			                      amountContext );
			}
		return new BigAmount( this.number.divide( divisorBD ,
		                                          mc ) ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public BigAmount[] divideAndRemainder( Number divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			BigAmount zero = BigAmount.of( 0 ,
			                               getUnit() );
			return new BigAmount[] { zero ,
			                         zero };
			}
		BigDecimal divisorBD = NumberUtils.getBigDecimal( divisor );
		if( divisorBD.equals( BigDecimal.ONE ) )
			{
			return new BigAmount[] { this ,
			                         new BigAmount( BigDecimal.ZERO ,
			                                        getUnit() ) };
			}
		MathContext mc = NumberUtils.getMathContext( amountContext ,
		                                             RoundingMode.HALF_EVEN );
		BigDecimal[] dec = this.number.divideAndRemainder( divisorBD ,
		                                                   mc );
		return new BigAmount[] { new BigAmount( dec[0] ,
		                                        getUnit() ,
		                                        amountContext ) ,
		                         new BigAmount( dec[1] ,
		                                        getUnit() ,
		                                        amountContext ) };
		}
	
	
	
	@Override
	public BigAmount divideToIntegralValue( long divisor )
		{
		return divideToIntegralValue( NumberUtils.getBigDecimal( divisor ) );
		}
	
	
	
	@Override
	public BigAmount divideToIntegralValue( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return BigAmount.of( 0 ,
			                     getUnit() );
			}
		return divideToIntegralValue( NumberUtils.getBigDecimal( divisor ) );
		}
	
	
	
	@Override
	public BigAmount divideToIntegralValue( Number divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return BigAmount.of( 0 ,
			                     getUnit() );
			}
		MathContext mc = NumberUtils.getMathContext( amountContext ,
		                                             RoundingMode.HALF_EVEN );
		BigDecimal divisorBD = NumberUtils.getBigDecimal( divisor );
		BigDecimal dec = this.number.divideToIntegralValue( divisorBD ,
		                                                    mc );
		return new BigAmount( dec ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public BigAmount multiply( Number multiplicand )
		{
		NumberUtils.checkNoInfinityOrNaN( multiplicand );
		BigDecimal multiplicandBD = NumberUtils.getBigDecimal( multiplicand );
		if( multiplicandBD.equals( BigDecimal.ONE ) )
			{
			return this;
			}
		MathContext mc = NumberUtils.getMathContext( amountContext ,
		                                             RoundingMode.HALF_EVEN );
		BigDecimal dec = this.number.multiply( multiplicandBD ,
		                                       mc );
		return new BigAmount( dec ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public BigAmount negate()
		{
		return new BigAmount( this.number.negate() ,
		                      getUnit() );
		}
	
	
	
	@Override
	public BigAmount plus()
		{
		return this;
		}
	
	
	
	/**
	 * Subtract unit.
	 *
	 * @param amount
	 * 	the subtrahend
	 *
	 * @return the unit
	 */
	
	@Override
	public BigAmount subtract( Quantity amount )
		{
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		if( amount.isZero() )
			{
			return this;
			}
		MathContext mc = NumberUtils.getMathContext( amountContext ,
		                                             RoundingMode.HALF_EVEN );
		return new BigAmount( this.number.subtract( amount.getNumber()
		                                                  .numberValue( BigDecimal.class ) ,
		                                            mc ) ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public BigAmount stripTrailingZeros()
		{
		if( isZero() )
			{
			return new BigAmount( BigDecimal.ZERO ,
			                      getUnit() );
			}
		return new BigAmount( this.number.stripTrailingZeros() ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public BigAmount remainder( Number divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new BigAmount( BigDecimal.ZERO ,
			                      getUnit() );
			}
		MathContext mc = NumberUtils.getMathContext( amountContext ,
		                                             RoundingMode.HALF_EVEN );
		BigDecimal bd = NumberUtils.getBigDecimal( divisor );
		return new BigAmount( this.number.remainder( bd ,
		                                             mc ) ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public BigAmount scaleByPowerOfTen( int power )
		{
		return new BigAmount( this.number.scaleByPowerOfTen( power ) ,
		                      getUnit() ,
		                      amountContext );
		}
	
	
	
	@Override
	public int signum()
		{
		return this.number.signum();
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
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		return number.stripTrailingZeros()
		             .compareTo( amount.getNumber()
		                               .numberValue( BigDecimal.class )
		                               .stripTrailingZeros() ) < 0;
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
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		return number.stripTrailingZeros()
		             .compareTo( amount.getNumber()
		                               .numberValue( BigDecimal.class )
		                               .stripTrailingZeros() ) <= 0;
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
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		return number.stripTrailingZeros()
		             .compareTo( amount.getNumber()
		                               .numberValue( BigDecimal.class )
		                               .stripTrailingZeros() ) > 0;
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
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		return number.stripTrailingZeros()
		             .compareTo( amount.getNumber()
		                               .numberValue( BigDecimal.class )
		                               .stripTrailingZeros() ) >= 0;
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
		NumberUtils.checkAmountParameter( amount ,
		                                  this.unit );
		return number.stripTrailingZeros()
		             .compareTo( amount.getNumber()
		                               .numberValue( BigDecimal.class )
		                               .stripTrailingZeros() ) == 0;
		}
	
	
	
	@Override
	public boolean equals( Object obj )
		{
		if( obj == this )
			{
			return true;
			}
		if( obj instanceof BigAmount )
			{
			BigAmount other = (BigAmount) obj;
			return Objects.equals( getUnit() ,
			                       other.getUnit() ) && Objects.equals( getNumberStripped() ,
			                                                            other.getNumberStripped() );
			}
		return false;
		}
	
	
	
	@Override
	public String toString()
		{
		return getUnit().getCode() + ' ' + number.toPlainString();
		}
	
	
	
	@Override
	public int hashCode()
		{
		return Objects.hash( getUnit() ,
		                     getNumberStripped() );
		}
	
	
	
	//</editor-fold>
	
	
	
	}
