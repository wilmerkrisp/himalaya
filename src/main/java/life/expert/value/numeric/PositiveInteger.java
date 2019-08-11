package life.expert.value.numeric;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;

import io.vavr.Tuple;
import io.vavr.Tuple1;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

import com.google.common.collect.ComparisonChain;

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

/**
 * <pre> Simple value-object int wrapper.
 * Class invariant: value &gt; 0
 *
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
public final class PositiveInteger
	implements Comparable<PositiveInteger>
	{
	
	/**
	 * int primitive value
	 *
	 * -- SETTER --
	 *
	 * @param integer
	 * 	int primitive value
	 * @return long
	 *
	 * 	-- GETTER --
	 * @return long
	 * 	the int primitive value
	 */
	@NonNull private final int integer;
	
	/**
	 * Classic factory method.
	 * Not recomended with functional style because raise Exception.
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the positive integer
	 */
	@Deprecated
	public static PositiveInteger of( final int number )
		{
		return tryOf( number ).get();
		}
	
	/**
	 * Factory method returns Vavr's Try.
	 *
	 * @param number
	 * 	the int primitive value
	 *
	 * @return the Try with PositiveInteger
	 */
	public static Try<PositiveInteger> tryOf( final int number )
		{
		if( number < 1 )
			return illegalArgumentFailure( "Input argument must posititve >0 ." );
		else
			return Success( new PositiveInteger( number ) );
			
		}
	
	/**
	 * Factory method returns Optional.
	 *
	 * @param number
	 * 	the int primitive value
	 *
	 * @return the Optional  with PositiveInteger
	 */
	public static Optional<PositiveInteger> optionalOf( final int number )
		{
		return tryOf( number ).toJavaOptional();
		}
	
	/**
	 * Factory method returns Reactor's Mono.
	 *
	 * @param number
	 * 	the int primitive value
	 *
	 * @return Mono with PositiveInteger
	 */
	public static Mono<PositiveInteger> monoOf( final int number )
		{
		return monoFromTry( tryOf( number ) );
		}
	
	@Unapply
	static Tuple1<Integer> PositiveInteger( PositiveInteger object )
		{
		return Tuple.of( object.getInteger() );
		}
	
	@Override
	public String toString()
		{
		return "" + this.integer;
		}
	
	@Override
	public int compareTo( PositiveInteger o )
		{
		return ComparisonChain.start()
		                      .compare( this.integer , o.integer )
		                      .result();
		}
	}
