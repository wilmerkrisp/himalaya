package life.expert.value.numeric.amount;
//-------------------------------------------------------------------------------------------------------
//  __    __   __  .___  ___.      ___       __          ___   ____    ____  ___   ____    ____  ___
// |  |  |  | |  | |   \/   |     /   \     |  |        /   \  \   \  /   / /   \  \   \  /   / /   \
// |  |__|  | |  | |  \  /  |    /  ^  \    |  |       /  ^  \  \   \/   / /  ^  \  \   \/   / /  ^  \
// |   __   | |  | |  |\/|  |   /  /_\  \   |  |      /  /_\  \  \_    _/ /  /_\  \  \_    _/ /  /_\  \
// |  |  |  | |  | |  |  |  |  /  _____  \  |  `----./  _____  \   |  |  /  _____  \   |  |  /  _____  \
// |__|  |__| |__| |__|  |__| /__/     \__\ |_______/__/     \__\  |__| /__/     \__\  |__| /__/     \__\
//
//                                            Wilmer Krisp 2019/02/05
//---------------------------------------------------------------------------------------------------------

import life.expert.value.numeric.context.AmountContext;
import life.expert.value.numeric.operators.Operator;
import life.expert.value.numeric.unit.Piece;
import life.expert.value.numeric.unit.Unit;
import life.expert.value.numeric.utils.AmountParseException;
import life.expert.value.numeric.utils.DefaultNumberValue;
import life.expert.value.numeric.utils.NumberUtils;
import life.expert.value.numeric.utils.NumberValue;
import life.expert.value.numeric.utils.ValueException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

/**
 * Default immutable implementation of {@link Quantity} based on
 * {@link java.math.BigDecimal} for the numeric representation.
 *
 * As required by {@link Quantity} this class is final, thread-safe, immutable and
 * serializable.
 */
public final class RoundedAmount
	implements Quantity,
	           Comparable<Quantity>
	{
	
	/**
	 * The constant DEFAULT_CONTEXT.
	 */
	public static final AmountContext DEFAULT_CONTEXT = AmountContext.of( RoundedAmount.class , 256 , false , 63 , RoundingMode.HALF_EVEN );
	
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
	
	/**
	 * The rounding to be done.
	 */
	private final Operator rounding;
	
	//<editor-fold desc="helpers">
	
	/**
	 * Ulp rounded unit.
	 *
	 * @return the rounded unit
	 */
	public RoundedAmount ulp()
		{
		return new RoundedAmount( number.ulp() , unit , rounding );
		}
	
	/**
	 * Pow rounded unit.
	 *
	 * @param n
	 * 	the n
	 *
	 * @return the rounded unit
	 */
	public RoundedAmount pow( int n )
		{
		return new RoundedAmount( number.pow( n , Optional.ofNullable( amountContext.getMathContext() )
		                                                  .orElse( MathContext.DECIMAL64 ) ) , unit , rounding ).with( rounding );
		}
	
	/**
	 * With rounded unit.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the rounded unit
	 */
	public RoundedAmount with( Number amount )
		{
		checkNumber( amount );
		return new RoundedAmount( NumberUtils.getBigDecimal( amount ) , unit , rounding );
		}
	
	/**
	 * Creates a new Amount instance, by just replacing the {@link Unit}.
	 *
	 * @param unit
	 * 	the unit unit to be replaced, not {@code null}
	 *
	 * @return the new amount with the same numeric value and {@link MathContext}, but the new {@link Unit}.
	 */
	public RoundedAmount with( Unit unit )
		{
		Objects.requireNonNull( unit , "unit required" );
		return new RoundedAmount( asType( BigDecimal.class ) , unit , rounding );
		}
	
	/**
	 * With rounded unit.
	 *
	 * @param unit
	 * 	the unit
	 * @param amount
	 * 	the amount
	 *
	 * @return the rounded unit
	 */
	/*
	 * (non-Javadoc)
	 * @see javax.unit.Quantity#with(Unit, java.lang.Number)
	 */
	public RoundedAmount with( Unit unit ,
	                           Number amount )
		{
		checkNumber( amount );
		return new RoundedAmount( NumberUtils.getBigDecimal( amount ) , unit , rounding );
		}
	
	/**
	 * Gets scale.
	 *
	 * @return the scale
	 */
	public int getScale()
		{
		return number.scale();
		}
	
	/**
	 * Gets precision.
	 *
	 * @return the precision
	 */
	public int getPrecision()
		{
		return number.precision();
		}
	
	/**
	 * As type t.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param type
	 * 	the type
	 *
	 * @return the t
	 */
	@Deprecated
	@SuppressWarnings( "unchecked" )
	public <T> T asType( Class<T> type )
		{
		if( BigDecimal.class.equals( type ) )
			{
			return (T) this.number;
			}
		if( Number.class.equals( type ) )
			{
			return (T) this.number;
			}
		if( Double.class.equals( type ) )
			{
			return (T) Double.valueOf( this.number.doubleValue() );
			}
		if( Float.class.equals( type ) )
			{
			return (T) Float.valueOf( this.number.floatValue() );
			}
		if( Long.class.equals( type ) )
			{
			return (T) Long.valueOf( this.number.longValue() );
			}
		if( Integer.class.equals( type ) )
			{
			return (T) Integer.valueOf( this.number.intValue() );
			}
		if( Short.class.equals( type ) )
			{
			return (T) Short.valueOf( this.number.shortValue() );
			}
		if( Byte.class.equals( type ) )
			{
			return (T) Byte.valueOf( this.number.byteValue() );
			}
		if( BigInteger.class.equals( type ) )
			{
			return (T) this.number.toBigInteger();
			}
		throw new IllegalArgumentException( "Unsupported representation type: " + type );
		}
	
	/**
	 * As type t.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param type
	 * 	the type
	 * @param adjuster
	 * 	the adjuster
	 *
	 * @return the t
	 */
	@Deprecated
	public <T> T asType( Class<T> type ,
	                     Operator adjuster )
		{
		RoundedAmount amount = (RoundedAmount) adjuster.apply( this );
		return amount.asType( type );
		}
	
	/**
	 * Method that returns BigDecimal.ZERO, if {@link #isZero()}, and #number
	 * {@link #stripTrailingZeros()} in all other cases.
	 *
	 * @return the stripped number value.
	 */
	public BigDecimal asNumberStripped()
		{
		if( isZero() )
			{
			return BigDecimal.ZERO;
			}
		return number.stripTrailingZeros();
		}
	
	/**
	 * Internal method to check for correct number parameter.
	 *
	 * @param number
	 * 	the number to check.
	 *
	 * @throws IllegalArgumentException
	 * 	If the number is null
	 */
	private void checkNumber( Number number )
		{
		Objects.requireNonNull( number , "Number is required." );
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
	
	//</editor-fold>
	
	//<editor-fold desc="constructors and fabriks">
	
	/**
	 * Creates a new instance os {@link RoundedAmount}.
	 *
	 * @param number
	 * 	the amount, not null.
	 * @param unit
	 * 	the unit, not null.
	 */
	public RoundedAmount( Number number ,
	                      Unit unit )
		{
		
		this( number , unit , null , null );
		
		}
	
	/**
	 * Instantiates a new Rounded unit.
	 *
	 * @param number
	 * 	the number
	 * @param unit
	 * 	the unit
	 * @param mathContext
	 * 	the math context
	 */
	@Deprecated
	public RoundedAmount( Number number ,
	                      Unit unit ,
	                      MathContext mathContext )
		{
		this( number , unit , DEFAULT_CONTEXT.toBuilder()
		                                     .roundingMode( mathContext.getRoundingMode() )
		                                     .mathContext( mathContext )
		                                     .build() , null );
			
		}
	
	/**
	 * Instantiates a new Rounded amount.
	 *
	 * @param number
	 * 	the number
	 * @param unit
	 * 	the unit
	 * @param mathContext
	 * 	the math context
	 */
	public RoundedAmount( Number number ,
	                      Unit unit ,
	                      AmountContext mathContext )
		{
		this( number , unit , mathContext , null );
		}
	
	/**
	 * Creates a new instance os {@link RoundedAmount}.
	 *
	 * @param number
	 * 	the amount, not null.
	 * @param unit
	 * 	, not null.
	 * @param rounding
	 * 	the rounding
	 */
	public RoundedAmount( Number number ,
	                      Unit unit ,
	                      Operator rounding )
		{
		this( number , unit , null , rounding );
		}
	
	/**
	 * Instantiates a new Rounded unit.
	 *
	 * @param number
	 * 	the number
	 * @param unit
	 * 	the unit
	 * @param context
	 * 	the context
	 * @param rounding
	 * 	the rounding
	 */
	@Deprecated
	public RoundedAmount( Number number ,
	                      Unit unit ,
	                      AmountContext context ,
	                      Operator rounding )
		{
		Objects.requireNonNull( unit , "Currency is required." );
		this.unit = unit;
		
		//nullable
		this.rounding = rounding;
		
		Objects.requireNonNull( number , "Number is required." );
		checkNumber( number );
		
		this.amountContext = ( context == null ) ? DEFAULT_CONTEXT : AmountContext.builder()
		                                                                          .build();
		this.number = NumberUtils.getBigDecimal( number , amountContext );
		}
	
	/**
	 * Translates a {@code BigDecimal} value and a {@code Unit} unit into a
	 * {@code Amount}.
	 *
	 * @param number
	 * 	numeric value of the {@code Amount}.
	 * @param unit
	 * 	unit unit of the {@code Amount}.
	 *
	 * @return a {@code Amount} combining the numeric value and unit unit.
	 */
	public static RoundedAmount of( BigDecimal number ,
	                                Unit unit )
		{
		return new RoundedAmount( number , unit );
		}
	
	/**
	 * Translates a {@code BigDecimal} value and a {@code Unit} unit into a
	 * {@code Amount}.
	 *
	 * @param number
	 * 	numeric value of the {@code Amount}.
	 * @param unit
	 * 	unit unit of the {@code Amount}.
	 * @param rounding
	 * 	The rounding to be applied.
	 *
	 * @return a {@code Amount} combining the numeric value and unit unit.
	 */
	public static RoundedAmount of( BigDecimal number ,
	                                Unit unit ,
	                                Operator rounding )
		{
		return new RoundedAmount( number , unit , rounding );
		}
	
	/**
	 * Translates a {@code BigDecimal} value and a {@code Unit} unit into a
	 * {@code Amount}.
	 *
	 * @param number
	 * 	numeric value of the {@code Amount}.
	 * @param unit
	 * 	unit unit of the {@code Amount}.
	 * @param mathContext
	 * 	the {@link MathContext} to be used.
	 *
	 * @return a {@code Amount} combining the numeric value and unit unit.
	 */
	public static RoundedAmount of( BigDecimal number ,
	                                Unit unit ,
	                                MathContext mathContext )
		{
		return new RoundedAmount( number , unit , mathContext );
		}
	
	/**
	 * Static factory method for creating a new instance of {@link RoundedAmount} .
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unit
	 * 	The target unit, not null.
	 * @param rounding
	 * 	The rounding to be applied.
	 *
	 * @return A new instance of {@link RoundedAmount}.
	 */
	public static RoundedAmount of( Number number ,
	                                Unit unit ,
	                                Operator rounding )
		{
		return new RoundedAmount( number , unit , rounding );
		}
	
	/**
	 * Static factory method for creating a new instance of {@link RoundedAmount} .
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unit
	 * 	The target unit, not null.
	 * @param amountContext
	 * 	the monetary context
	 *
	 * @return A new instance of {@link RoundedAmount}.
	 */
	public static RoundedAmount of( Number number ,
	                                Unit unit ,
	                                AmountContext amountContext )
		{
		return new RoundedAmount( number , unit , amountContext );
		}
	
	/**
	 * Static factory method for creating a new instance of {@link RoundedAmount} .
	 *
	 * @param unit
	 * 	The target unit, not null.
	 * @param number
	 * 	The numeric part, not null.
	 * @param amountContext
	 * 	the {@link AmountContext} to be used.
	 * @param rounding
	 * 	The rounding to be applied.
	 *
	 * @return A new instance of {@link RoundedAmount}.
	 */
	@Deprecated
	public static RoundedAmount of( Unit unit ,
	                                Number number ,
	                                AmountContext amountContext ,
	                                Operator rounding )
		{
		return new RoundedAmount( number , unit , amountContext , rounding );
		}
	
	/**
	 * Static factory method for creating a new instance of {@link RoundedAmount} .
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 *
	 * @return A new instance of {@link RoundedAmount}.
	 */
	@Deprecated
	public static RoundedAmount of( Number number ,
	                                String unitCode )
		{
		return new RoundedAmount( number , Piece.of( unitCode ) );
		}
	
	/**
	 * Static factory method for creating a new instance of {@link RoundedAmount} .
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 * @param rounding
	 * 	The rounding to be applied.
	 *
	 * @return A new instance of {@link RoundedAmount}.
	 */
	public static RoundedAmount of( Number number ,
	                                String unitCode ,
	                                Operator rounding )
		{
		return new RoundedAmount( number , Piece.of( unitCode ) , rounding );
		}
	
	/**
	 * Static factory method for creating a new instance of {@link RoundedAmount} .
	 *
	 * @param number
	 * 	The numeric part, not null.
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 * @param amountContext
	 * 	the monetary context
	 *
	 * @return A new instance of {@link RoundedAmount}.
	 */
	@Deprecated
	public static RoundedAmount of( Number number ,
	                                String unitCode ,
	                                AmountContext amountContext )
		{
		return new RoundedAmount( number , Piece.of( unitCode ) , amountContext );
		}
	
	/**
	 * Static factory method for creating a new instance of {@link RoundedAmount} .
	 *
	 * @param unitCode
	 * 	The target unit as ISO unit code.
	 * @param number
	 * 	The numeric part, not null.
	 * @param amountContext
	 * 	the monetary context
	 * @param rounding
	 * 	The rounding to be applied.
	 *
	 * @return A new instance of {@link RoundedAmount}.
	 */
	public static RoundedAmount of( String unitCode ,
	                                Number number ,
	                                AmountContext amountContext ,
	                                Operator rounding )
		{
		return new RoundedAmount( number , Piece.of( unitCode ) , amountContext , rounding );
		}
	
	/**
	 * Obtains an instance of {@link RoundedAmount} representing zero.
	 *
	 * @param unit
	 * 	the unit, not null.
	 *
	 * @return an instance of {@link RoundedAmount} representing zero.
	 *
	 * @since 1.0.1
	 */
	public static RoundedAmount zero( Unit unit )
		{
		return of( BigDecimal.ZERO , unit );
		}
	
	/**
	 * Obtains an instance of {@code RoundedAmount} from an amount in minor units.
	 * For example, {@code ofMinor(USD, 1234)} creates the instance {@code USD 12.34}.
	 *
	 * @param unit
	 * 	the unit, not null
	 * @param amountMinor
	 * 	the amount of unit in the minor division of the unit
	 *
	 * @return the Amount from minor units
	 *
	 * @throws NullPointerException
	 * 	when the unit is null
	 * @throws IllegalArgumentException
	 * 	when {@link Unit#getDefaultFractionDigits()} is lesser than zero.
	 * @see Unit#getDefaultFractionDigits() Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()
	 * @since 1.0.1
	 */
	public static RoundedAmount ofMinor( Unit unit ,
	                                     long amountMinor )
		{
		return ofMinor( unit , amountMinor , unit.getDefaultFractionDigits() );
		}
	
	/**
	 * Obtains an instance of {@code RoundedAmount} from an amount in minor units.
	 * For example, {@code ofMinor(USD, 1234, 2)} creates the instance {@code USD 12.34}.
	 *
	 * @param unit
	 * 	the unit, not null
	 * @param amountMinor
	 * 	the amount of unit in the minor division of the unit
	 * @param factionDigits
	 * 	number of digits
	 *
	 * @return the monetary amount from minor units
	 *
	 * @throws NullPointerException
	 * 	when the unit is null
	 * @throws IllegalArgumentException
	 * 	when the factionDigits is negative
	 * @see Unit#getDefaultFractionDigits() Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()Unit#getDefaultFractionDigits()
	 * @see Amount#ofMinor(Unit , long , int) Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit , long , int)Amount#ofMinor(Unit, long, int)
	 * @since 1.0.1
	 */
	public static RoundedAmount ofMinor( Unit unit ,
	                                     long amountMinor ,
	                                     int factionDigits )
		{
		if( factionDigits < 0 )
			{
			throw new IllegalArgumentException( "The factionDigits cannot be negative" );
			}
		return of( BigDecimal.valueOf( amountMinor , factionDigits ) , unit );
		}
	
	/**
	 * From rounded unit.
	 *
	 * @param amt
	 * 	the amt
	 *
	 * @return the rounded unit
	 */
	public static RoundedAmount from( Quantity amt )
		{
		if( amt.getClass() == RoundedAmount.class )
			{
			return (RoundedAmount) amt;
			}
		if( amt.getClass() == Amount.class )
			{
			return RoundedAmount.of( amt.getNumber()
			                            .numberValue( BigDecimal.class ) , amt.getUnit() );
			}
		else if( amt.getClass() == BigAmount.class )
			{
			return RoundedAmount.of( amt.getNumber()
			                            .numberValue( BigDecimal.class ) , amt.getUnit() );
			}
		return RoundedAmount.of( amt.getNumber()
		                            .numberValue( BigDecimal.class ) , amt.getUnit() );
		}
	
	/**
	 * Obtains an instance of RoundedAmount from a text string such as 'EUR
	 * 25.25'.
	 *
	 * @param text
	 * 	the input text, not null.
	 *
	 * @return RoundedAmount instance
	 *
	 * @throws NumberFormatException
	 * 	if the amount is not a number
	 * @throws life.expert.value.numeric.utils.UnknownUnitException
	 * 	the unit cannot be resolved
	 */
	public static RoundedAmount parse( CharSequence text )
		{
		String[] array = Objects.requireNonNull( text )
		                        .toString()
		                        .split( " " );
		if( array.length != 2 )
			{
			throw new AmountParseException( "An error happened when try to parse the Amount." , text , 0 );
			}
		
		String parsed_unit = array[0];
		
		BigDecimal number = new BigDecimal( array[1] );
		return RoundedAmount.of( number , Piece.of( parsed_unit ) );
		
		}
	
	//</editor-fold>
	
	//<editor-fold desc="implemented interface">
	
	/**
	 * Gets unit.
	 *
	 * @return the unit
	 */
	@Override
	public Unit getUnit()
		{
		return unit;
		}
	
	/**
	 * Access the {@link MathContext} used by this instance.
	 *
	 * @return the {@link MathContext} used, never null.
	 */
	@Override
	public AmountContext getContext()
		{
		return amountContext;
		}
	
	@Override
	public RoundedAmount abs()
		{
		if( isPositiveOrZero() )
			{
			return this;
			}
		return negate();
		}
	
	// Arithmetic Operations
	
	/**
	 * Add rounded unit.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the rounded unit
	 */
	@Override
	public RoundedAmount add( Quantity amount )
		{
		NumberUtils.checkAmountParameter( amount , unit );
		if( amount.isZero() )
			{
			return this;
			}
		return new RoundedAmount( number.add( amount.getNumber()
		                                            .numberValue( BigDecimal.class ) ) , unit , rounding ).with( rounding );
		}
	
	@Override
	public RoundedAmount divide( Number divisor )
		{
		BigDecimal bd = NumberUtils.getBigDecimal( divisor );
		if( isOne( bd ) )
			{
			return this;
			}
		BigDecimal dec = number.divide( bd , Optional.ofNullable( amountContext.getRoundingMode() )
		                                             .orElse( RoundingMode.HALF_EVEN ) );
		return new RoundedAmount( dec , unit , rounding ).with( rounding );
		}
	
	@Override
	public RoundedAmount[] divideAndRemainder( Number divisor )
		{
		BigDecimal bd = NumberUtils.getBigDecimal( divisor );
		if( isOne( bd ) )
			{
			return new RoundedAmount[] { this ,
			                             new RoundedAmount( 0L , getUnit() , rounding ) };
			}
		BigDecimal[] dec = number.divideAndRemainder( NumberUtils.getBigDecimal( divisor ) , Optional.ofNullable( amountContext.getMathContext() )
		                                                                                             .orElse( MathContext.DECIMAL64 ) );
		return new RoundedAmount[] { new RoundedAmount( dec[0] , unit , rounding ) ,
		                             new RoundedAmount( dec[1] , unit , rounding ).with( rounding ) };
		}
	
	@Override
	public RoundedAmount divideToIntegralValue( Number divisor )
		{
		BigDecimal dec = number.divideToIntegralValue( NumberUtils.getBigDecimal( divisor ) , Optional.ofNullable( amountContext.getMathContext() )
		                                                                                              .orElse( MathContext.DECIMAL64 ) );
		return new RoundedAmount( dec , unit , rounding );
		}
	
	@Override
	public RoundedAmount multiply( Number multiplicand )
		{
		BigDecimal bd = NumberUtils.getBigDecimal( multiplicand );
		if( isOne( bd ) )
			{
			return this;
			}
		BigDecimal dec = number.multiply( bd , Optional.ofNullable( amountContext.getMathContext() )
		                                               .orElse( MathContext.DECIMAL64 ) );
		return new RoundedAmount( dec , unit , rounding ).with( rounding );
		}
	
	@Override
	public RoundedAmount negate()
		{
		return new RoundedAmount( number.negate( Optional.ofNullable( amountContext.getMathContext() )
		                                                 .orElse( MathContext.DECIMAL64 ) ) , unit , rounding );
		}
	
	@Override
	public RoundedAmount plus()
		{
		return this;
		}
	
	/**
	 * Subtract rounded unit.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the rounded unit
	 */
	@Override
	public RoundedAmount subtract( Quantity amount )
		{
		NumberUtils.checkAmountParameter( amount , unit );
		if( amount.isZero() )
			{
			return this;
			}
		return new RoundedAmount( number.subtract( amount.getNumber()
		                                                 .numberValue( BigDecimal.class ) , Optional.ofNullable( amountContext.getMathContext() )
		                                                                                            .orElse( MathContext.DECIMAL64 ) ) , unit , rounding );
		}
	
	@Override
	public RoundedAmount remainder( Number divisor )
		{
		return new RoundedAmount( number.remainder( NumberUtils.getBigDecimal( divisor ) , Optional.ofNullable( amountContext.getMathContext() )
		                                                                                           .orElse( MathContext.DECIMAL64 ) ) , unit , rounding );
		}
	
	@Override
	public RoundedAmount scaleByPowerOfTen( int power )
		{
		return new RoundedAmount( number.scaleByPowerOfTen( power ) , unit , rounding );
		}
	
	@Override
	public boolean isZero()
		{
		return number.signum() == 0;
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
	
	@Override
	public int signum()
		{
		return number.signum();
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
		NumberUtils.checkAmountParameter( amount , unit );
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
		NumberUtils.checkAmountParameter( amount , unit );
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
		NumberUtils.checkAmountParameter( amount , unit );
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
		NumberUtils.checkAmountParameter( amount , unit );
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
		NumberUtils.checkAmountParameter( amount , unit );
		return number.stripTrailingZeros()
		             .compareTo( amount.getNumber()
		                               .numberValue( BigDecimal.class )
		                               .stripTrailingZeros() ) == 0;
		}
	
	/**
	 * Is not equal to boolean.
	 *
	 * @param amount
	 * 	the amount
	 *
	 * @return the boolean
	 */
	public boolean isNotEqualTo( Quantity amount )
		{
		NumberUtils.checkAmountParameter( amount , unit );
		return number.stripTrailingZeros()
		             .compareTo( amount.getNumber()
		                               .numberValue( BigDecimal.class )
		                               .stripTrailingZeros() ) != 0;
		}
	
	/**
	 * With rounded unit.
	 *
	 * @param operator
	 * 	the operator
	 *
	 * @return the rounded unit
	 */
	
	@Override
	public RoundedAmount with( Operator operator )
		{
		Objects.requireNonNull( operator );
		try
			{
			return RoundedAmount.from( operator.apply( this ) );
			}
		catch( ValueException | ArithmeticException e )
			{
			throw e;
			}
		catch( Exception e )
			{
			throw new ValueException( "Query failed: " + operator , e );
			}
		}
	
	@Override
	public String toString()
		{
		return unit.getCode() + ' ' + number;
		}
	
	@Override
	public int hashCode()
		{
		return Objects.hash( unit , asNumberStripped() );
		}
	
	@Override
	public boolean equals( Object obj )
		{
		if( obj == this )
			{
			return true;
			}
		if( obj instanceof RoundedAmount )
			{
			RoundedAmount other = (RoundedAmount) obj;
			return Objects.equals( unit , other.unit ) && Objects.equals( asNumberStripped() , other.asNumberStripped() );
			}
		return false;
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
		int compare;
		if( unit.equals( o.getUnit() ) )
			{
			compare = asNumberStripped().compareTo( RoundedAmount.from( o )
			                                                     .asNumberStripped() );
			}
		else
			{
			compare = unit.getCode()
			              .compareTo( o.getUnit()
			                           .getCode() );
			}
		return compare;
		}
	
	@Override
	public NumberValue getNumber()
		{
		return new DefaultNumberValue( number );
		}
	
	@Override
	public RoundedAmount multiply( long multiplicand )
		{
		if( multiplicand == 1L )
			{
			return this;
			}
		return multiply( NumberUtils.getBigDecimal( multiplicand ) );
		}
	
	@Override
	public RoundedAmount multiply( double multiplicand )
		{
		NumberUtils.checkNoInfinityOrNaN( multiplicand );
		if( multiplicand == 1.0d )
			{
			return this;
			}
		return multiply( NumberUtils.getBigDecimal( multiplicand ) );
		}
	
	@Override
	public RoundedAmount divide( long divisor )
		{
		if( divisor == 1L )
			{
			return this;
			}
		return divide( NumberUtils.getBigDecimal( divisor ) );
		}
	
	@Override
	public RoundedAmount divide( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new RoundedAmount( 0L , getUnit() , amountContext , rounding );
			}
		if( divisor == 1.0d )
			{
			return this;
			}
		return divide( NumberUtils.getBigDecimal( divisor ) );
		}
	
	@Override
	public RoundedAmount remainder( long divisor )
		{
		return remainder( NumberUtils.getBigDecimal( divisor ) );
		}
	
	@Override
	public RoundedAmount remainder( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			return new RoundedAmount( 0L , getUnit() , amountContext , rounding );
			}
		return remainder( NumberUtils.getBigDecimal( divisor ) );
		}
	
	@Override
	public RoundedAmount[] divideAndRemainder( long divisor )
		{
		return divideAndRemainder( NumberUtils.getBigDecimal( divisor ) );
		}
	
	@Override
	public RoundedAmount[] divideAndRemainder( double divisor )
		{
		if( NumberUtils.isInfinityAndNotNaN( divisor ) )
			{
			RoundedAmount zero = new RoundedAmount( 0L , getUnit() , amountContext , rounding );
			return new RoundedAmount[] { zero ,
			                             zero };
			}
		return divideAndRemainder( NumberUtils.getBigDecimal( divisor ) );
		}
	
	@Override
	public RoundedAmount stripTrailingZeros()
		{
		if( isZero() )
			{
			return of( BigDecimal.ZERO , getUnit() );
			}
		return of( number.stripTrailingZeros() , getUnit() );
		}
	
	@Override
	public RoundedAmount divideToIntegralValue( long divisor )
		{
		return divideToIntegralValue( NumberUtils.getBigDecimal( divisor ) );
		}
	
	@Override
	public RoundedAmount divideToIntegralValue( double divisor )
		{
		return divideToIntegralValue( NumberUtils.getBigDecimal( divisor ) );
		}
	
	//</editor-fold>
	
	}
