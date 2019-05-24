package life.expert.value.context;









import life.expert.value.amount.Amount;
import life.expert.value.amount.Quantity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

import java.util.List;


import lombok.NonNull;//@NOTNULL

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.util.Map;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)
import static life.expert.common.base.Objects.*;        //deepCopyOfObject

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;









/**
 * simple immutable class:  <pre>{@code  Class<? extends Quantity> }</pre>
 *
 * - pattern new-call
 * - not for inheritance
 *
 * This class models the meta data (mostly the numeric capabilities) of a {@link Quantity} in a
 * platform independent way. It provides information about
 * <ul>
 * <li>the maximal precision supported (0, for unlimited precision).
 * <li>the minimum scale <pre>{@code   (>=0) }</pre>
 * <li>the maximal scale  <pre>{@code  <>(>= -1, -1 for unlimited scale).</> }</pre>
 * <li>the numeric representation class.
 * <li>any other attributes, identified by the attribute type, e.g.
 * {@link java.math.RoundingMode}.
 * </ul>
 *
 * This class is immutable, serializable and thread-safe.
 * *
 *
 * Every constructor/fabric can raise the exceptions:
 *
 * throws NullPointerException if argument nullable
 * throws IllegalArgumentException if argument empty
 */
@Value
@RequiredArgsConstructor( staticName = "of" )
@Builder( toBuilder = true )
public final class AmountContext
	implements Context
	
	{
	
	
	
	/**
	 * Get the Quantity implementation class.
	 *
	 * -- SETTER --
	 *
	 * @param quantityType
	 * 	the implementation class of the containing amount instance, never null.
	 * @return the implementation
	 *
	 * 	-- GETTER --
	 * @return the implementation class of the containing amount instance, never null.
	 */
	
	@NonNull private final Class<? extends Quantity> quantityType;
	
	
	
	/**
	 * Returns the {@code precision} setting. This value is always non-negative.
	 *
	 * -- SETTER --
	 *
	 * @param precision
	 * 	precision
	 * @return precision
	 *
	 * 	-- GETTER --
	 * @return an {@code int} which is the value of the {@code precision}
	 */
	private final int precision;
	
	
	
	/**
	 * Allows to check if {@code minScale == maxScale}.
	 *
	 * -- SETTER --
	 *
	 * @param fixedScale
	 * 	fixedScale
	 * @return fixedScale
	 *
	 * 	-- GETTER --
	 * @return {@code true} if {@code minScale == maxScale}.
	 */
	private final boolean fixedScale;
	
	
	
	/**
	 * Get the maximal scale supported, always {@code >= -1}. Fixed scaled
	 * numbers will have {@code scale==maxScale} for all values. {@code -1}
	 * declares the maximal scale to be <i>unlimited</i>.
	 *
	 * -- SETTER --
	 *
	 * @param maxScale
	 * 	maxScale
	 * @return maxScale
	 *
	 * 	-- GETTER --
	 * @return the maximal scale supported, always {@code >= -1}
	 */
	private final int maxScale;
	
	
	
	/**
	 * -- SETTER --
	 *  @param roundingMode
	 *  	roundingMode
	 * @return roundingMode
	 *
	 * -- GETTER --
	 *
	 * @return java RoundingMode
	 */
	@NonNull private final RoundingMode roundingMode;
	
	
	
	/**
	 * -- SETTER --
	 *
	 * @param mathContext
	 * 	mathContext
	 * @return mathContext
	 *
	 * 	-- GETTER --
	 * @return math context
	 */
	private final MathContext mathContext;
	
	
	
	/**
	 * Of amount context.
	 *
	 * @param quantityType
	 * 	the quantity type
	 * @param precision
	 * 	the precision
	 * @param fixedScale
	 * 	the fixed scale
	 * @param maxScale
	 * 	the max scale
	 * @param roundingMode
	 * 	the rounding mode
	 *
	 * @return the amount context
	 */
	public static final AmountContext of( @NonNull final Class<? extends Quantity> quantityType ,
	                                      final int precision ,
	                                      final boolean fixedScale ,
	                                      final int maxScale ,
	                                      @NonNull final RoundingMode roundingMode )
		{
		return of( quantityType ,
		           precision ,
		           fixedScale ,
		           maxScale ,
		           roundingMode );
		}
		
	}
