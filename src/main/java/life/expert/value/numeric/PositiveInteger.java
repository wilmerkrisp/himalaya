package life.expert.value.numeric;



import life.expert.common.function.TupleUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

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

//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R

//import static io.vavr.API.*;                           //conflicts with my reactive For-comprehension

import static io.vavr.API.$;                            // pattern matching
import static io.vavr.API.Case;
import static io.vavr.API.Match;
//import static java.util.function.Predicate.*;           //isEqual streamAPI

import static io.vavr.API.CheckedFunction;//checked functions
import static io.vavr.API.unchecked;    //checked->unchecked
import static io.vavr.API.Function;     //lambda->Function3
import static io.vavr.API.Tuple;

import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.*;
//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R

import static life.expert.common.reactivestreams.Preconditions.*; //reactive check

/**
 * <pre> Simple value-object int wrapper.
 * Class invariant: value &gt; 0
 *
 * *	- only the monoOf.. factory methods is allowed, because it allows you to lazily create objects only with a real subscription
 *  *	- 'of' - factory method is prohibited because it is intended only for easy creation of objects in tests, please use pure functional methods monoOf.., without raise exceptions.
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
	private final int number;
	
	/*
       Other factories use this method to create an object.
       He himself calls the private constructor to create the object.
       * */
	private static Mono<PositiveInteger> monoOf_( final int number )
		{
		return fromSupplier( () -> new PositiveInteger( number ) );
		}
	
	/**
	 * Create PositiveInteger from integer
	 * Only the monoOf.. factory methods is allowed, because it allows you to lazily create objects only with a real subscription
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the Mono with lazyli created object
	 *
	 * @implNote to create objects, this method calls the private factory monoOf_
	 */
	public static Mono<PositiveInteger> monoOf( final int number )
		{
		if( number < 1 )
			return illegalArgumentMonoError( "Input argument must posititve >0 ." );
		else
			return monoOf_( number );
		}
	
	/**
	 * <pre>
	 * Classic fabric method for creating positive number.
	 * This factory method is prohibited because it is intended only for easy creation of objects in tests
	 *
	 * @param number the number
	 * @return the always positive number
	 * @throws IllegalArgumentException if number not positive
	 * @deprecated please use pure functional methods monoOf.., without raise exceptions. </pre>
	 */
	@Deprecated
	public static PositiveInteger of( final int number )
		{
		return monoOf( number ).block();
		}
	
	/**
	 * Create PositiveInteger from Tuple
	 * The method helps with conversion operations Tuple-&gt;PositiveInteger
	 *
	 * @param tuple
	 * 	the tuple
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<PositiveInteger> monoOfTuple( Tuple1<Integer> tuple )
		{
		if( tuple == null )
			return illegalArgumentMonoError( "Input tuple must not be null." );
		else
			return TupleUtils.function( PositiveInteger::monoOf )
			                 .apply( tuple );
		}
	
	/**
	 * Create PositiveInteger from Mono with Tuple inside
	 * The method helps chaining flows together
	 *
	 * @param tuple
	 * 	the tuple
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<PositiveInteger> monoOfMonoWithTuple( Mono<Tuple1<Integer>> tuple )
		{
		if( tuple == null )
			return illegalArgumentMonoError( "Input Mono must not be null." );
		else
			return tuple.flatMap( PositiveInteger::monoOfTuple );
		}
	
	/**
	 * Create PositiveInteger from Mono with positive number inside
	 * The method helps chaining flows together
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<PositiveInteger> monoOfMono( Mono<Integer> number )
		{
		if( number == null )
			return illegalArgumentMonoError( "Input Mono must not be null." );
		else
			return number.flatMap( PositiveInteger::monoOf );
		}
	
	/**
	 * Standard Copy Factory
	 *
	 * @param other
	 * 	the other
	 *
	 * @return the Mono with lazyli created object
	 *
	 * @implNote to create objects, this method calls the private factory monoOf_
	 */
	public static Mono<PositiveInteger> copyOf( final PositiveInteger other )
		{
		return monoOf_( other.getNumber() );
		}
	
	/**
	 * pattern matching in vavr
	 * - you need add static import to method with pattern matching
	 * import static life.expert.value.string.NonBlankStringPatterns.*;
	 *
	 * @param object
	 * 	the object
	 *
	 * @return the tuple 1
	 */
	@Unapply
	static Tuple1<Integer> PositiveInteger( PositiveInteger object )
		{
		return Tuple.of( object.getNumber() );
		}
	
	@Override
	public String toString()
		{
		return "" + this.number;
		}
	
	@Override
	public int compareTo( PositiveInteger o )
		{
		return ComparisonChain.start()
		                      .compare( this.number , o.number )
		                      .result();
		}
	}
