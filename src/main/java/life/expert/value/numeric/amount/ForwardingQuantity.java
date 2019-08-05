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
//--------------------------------------------------------------------------------------------------------

import com.google.common.collect.ForwardingObject;
import life.expert.value.numeric.context.Context;
import life.expert.value.numeric.unit.Unit;
import life.expert.value.numeric.utils.NumberValue;
import org.jetbrains.annotations.NotNull;

/**
 * The type Forwarding numeric.
 */
public abstract class ForwardingQuantity
	extends ForwardingObject
	implements Quantity
	{
	
	/**
	 * Constructor for use by subclasses.
	 */
	protected ForwardingQuantity() {}
	
	///
	@Override
	protected abstract Quantity delegate();
	
	@Override
	public Context getContext()
		{
		return delegate().getContext();
		}
	
	@Override
	public boolean isGreaterThan( final Quantity amount )
		{
		return delegate().isGreaterThan( amount );
		}
	
	@Override
	public boolean isGreaterThanOrEqualTo( final Quantity amount )
		{
		return delegate().isGreaterThanOrEqualTo( amount );
		}
	
	@Override
	public boolean isLessThan( final Quantity amount )
		{
		return delegate().isLessThan( amount );
		}
	
	@Override
	public boolean isLessThanOrEqualTo( final Quantity amt )
		{
		return delegate().isLessThanOrEqualTo( amt );
		}
	
	@Override
	public boolean isEqualTo( final Quantity amount )
		{
		return delegate().isEqualTo( amount );
		}
	
	@Override
	public int signum()
		{
		return delegate().signum();
		}
	
	@Override
	public Quantity add( final Quantity amount )
		{
		return delegate().add( amount );
		}
	
	@Override
	public Quantity subtract( final Quantity amount )
		{
		return delegate().subtract( amount );
		}
	
	@Override
	public Quantity multiply( final long multiplicand )
		{
		return delegate().multiply( multiplicand );
		}
	
	@Override
	public Quantity multiply( final double multiplicand )
		{
		return delegate().multiply( multiplicand );
		}
	
	@Override
	public Quantity multiply( final Number multiplicand )
		{
		return delegate().multiply( multiplicand );
		}
	
	@Override
	public Quantity divide( final long divisor )
		{
		return delegate().divide( divisor );
		}
	
	@Override
	public Quantity divide( final double divisor )
		{
		return delegate().divide( divisor );
		}
	
	@Override
	public Quantity divide( final Number divisor )
		{
		return delegate().divide( divisor );
		}
	
	@Override
	public Quantity remainder( final long divisor )
		{
		return delegate().remainder( divisor );
		}
	
	@Override
	public Quantity remainder( final double divisor )
		{
		return delegate().remainder( divisor );
		}
	
	@Override
	public Quantity remainder( final Number divisor )
		{
		return delegate().remainder( divisor );
		}
	
	@Override
	public Quantity[] divideAndRemainder( final long divisor )
		{
		return delegate().divideAndRemainder( divisor );
		}
	
	@Override
	public Quantity[] divideAndRemainder( final double divisor )
		{
		return delegate().divideAndRemainder( divisor );
		}
	
	@Override
	public Quantity[] divideAndRemainder( final Number divisor )
		{
		return delegate().divideAndRemainder( divisor );
		}
	
	@Override
	public Quantity divideToIntegralValue( final long divisor )
		{
		return delegate().divideToIntegralValue( divisor );
		}
	
	@Override
	public Quantity divideToIntegralValue( final double divisor )
		{
		return delegate().divideToIntegralValue( divisor );
		}
	
	@Override
	public Quantity divideToIntegralValue( final Number divisor )
		{
		return delegate().divideToIntegralValue( divisor );
		}
	
	@Override
	public Quantity scaleByPowerOfTen( final int power )
		{
		return delegate().scaleByPowerOfTen( power );
		}
	
	@Override
	public Quantity abs()
		{
		return delegate().abs();
		}
	
	@Override
	public Quantity negate()
		{
		return delegate().negate();
		}
	
	@Override
	public Quantity plus()
		{
		return delegate().plus();
		}
	
	@Override
	public Quantity stripTrailingZeros()
		{
		return delegate().stripTrailingZeros();
		}
	
	@Override
	public int compareTo( @NotNull final Quantity o )
		{
		return delegate().compareTo( o );
		}
	
	@Override
	public Unit getUnit()
		{
		return delegate().getUnit();
		}
	
	@Override
	public NumberValue getNumber()
		{
		return delegate().getNumber();
		}
	}
