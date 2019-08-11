package life.expert.value.numeric;
//---------------------------------------------
//      ___       __        _______   ______
//     /   \     |  |      /  _____| /  __  \
//    /  ^  \    |  |     |  |  __  |  |  |  |
//   /  /_\  \   |  |     |  | |_ | |  |  |  |
//  /  _____  \  |  `----.|  |__| | |  `--'  |
// /__/     \__\ |_______| \______|  \______/
//
//               wilmer 2019/08/08
//---------------------------------------------

import com.google.common.collect.ComparisonChain;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;//@NOTNULL

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import java.util.Optional;

//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R

import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
import static life.expert.common.reactivestreams.Patterns.*;    //reactive helper functions

//import static io.vavr.API.*;                           //conflicts with my reactive For-comprehension

import static io.vavr.API.$;                            // pattern matching
import static io.vavr.API.Case;
import static io.vavr.API.Match;
//import static java.util.function.Predicate.*;           //isEqual streamAPI

import static io.vavr.API.CheckedFunction;//checked functions
import static io.vavr.API.unchecked;    //checked->unchecked
import static io.vavr.API.Function;     //lambda->Function3
import static io.vavr.API.Tuple;

import io.vavr.control.Try;                               //try
import reactor.core.publisher.Mono;

import static io.vavr.API.Success;

import io.vavr.Tuple;
import io.vavr.Tuple1;

/**
 * <pre>
 * Simple value-object long wrapper.
 * Class invariant: value &gt; 0
 *
 * Preconditions: none
 * Postconditions: none
 * Side effects: none
 * Tread safety:  Immutable
 * </pre>
 */
@Value
@AllArgsConstructor( access = AccessLevel.PRIVATE )
@Patterns /*pattern matching in vavr*/
@Slf4j
public final class PositiveLong
	implements Comparable<PositiveLong>
	{
	
	/**
	 * long primitive value
	 *
	 * -- SETTER --
	 *
	 * @param long
	 * 	long primitive value
	 * @return long
	 *
	 * 	-- GETTER --
	 * @return long
	 * 	the long primitive value
	 */
	@NonNull private final long longNumber;
	
	/**
	 * Classic factory method.
	 * Not recomended with functional style because raise Exception.
	 *
	 * @param number
	 * 	the long primitive value
	 *
	 * @return the positive long
	 */
	@Deprecated
	public static PositiveLong of( final long number )
		{
		return tryOf( number ).get();
		}
	
	/**
	 * Factory method returns Vavr's Try.
	 *
	 * @param number
	 * 	the long primitive value
	 *
	 * @return Try with PositiveLong
	 */
	public static Try<PositiveLong> tryOf( final long number )
		{
		if( number < 1 )
			return illegalArgumentFailure( "Input argument must posititve >0 ." );
		else
			return Success( new PositiveLong( number ) );
			
		}
	
	/**
	 * Factory method returns Optional.
	 *
	 * @param number
	 * 	the long primitive value
	 *
	 * @return Optional with PositiveInteger
	 */
	public static Optional<PositiveLong> optionalOf( final long number )
		{
		return tryOf( number ).toJavaOptional();
		}
	
	/**
	 * Factory method returns Reactor's Mono.
	 *
	 * @param number
	 * 	the long primitive value
	 *
	 * @return Mono with PositiveInteger
	 */
	public static Mono<PositiveLong> monoOf( final long number )
		{
		return monoFromTry( tryOf( number ) );
		}
	
	@Unapply
	static Tuple1<Long> PositiveLong( PositiveLong object )
		{
		return Tuple.of( object.getLongNumber() );
		}
	
	@Override
	public String toString()
		{
		return "" + this.longNumber;
		}
	
	@Override
	public int compareTo( PositiveLong o )
		{
		return ComparisonChain.start()
		                      .compare( this.longNumber , o.longNumber )
		                      .result();
		}
	}

