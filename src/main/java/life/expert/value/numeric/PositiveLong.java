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
import life.expert.common.function.TupleUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

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

import io.vavr.Tuple;
import io.vavr.Tuple1;

import static reactor.core.publisher.Mono.*;
//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R

import static life.expert.common.reactivestreams.Preconditions.*; //reactive check

/**
 * <pre>
 * Simple value-object long wrapper.
 * Class invariant: value &gt; 0
 *
 * 	- only the monoOf.. factory methods is allowed, because it allows you to lazily create objects only with a real subscription
 *  	- 'of' - factory method is prohibited because it is intended only for easy creation of objects in tests, please use pure functional methods monoOf.., without raise exceptions.
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
	private final long number;
	
	/*
       Other factories use this method to create an object.
       He himself calls the private constructor to create the object.
       * */
	private static Mono<PositiveLong> monoOf_( final long number )
		{
		return fromSupplier( () -> new PositiveLong( number ) );
		}
	
	/**
	 * Create PositiveLong from long
	 * Only the monoOf.. factory methods is allowed, because it allows you to lazily create objects only with a real subscription
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the Mono with lazyli created object
	 *
	 * @implNote to create objects, this method calls the private factory monoOf_
	 */
	public static Mono<PositiveLong> monoOf( final long number )
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
	public static PositiveLong of( final long number )
		{
		return monoOf( number ).block();
		}
	
	/**
	 * Create PositiveLong from Tuple
	 * The method helps with conversion operations Tuple-&gt;PositiveLong
	 *
	 * @param tuple
	 * 	the tuple
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<PositiveLong> monoOfTuple( Tuple1<Long> tuple )
		{
		if( tuple == null )
			return illegalArgumentMonoError( "Input tuple must not be null." );
		else
			return TupleUtils.function( PositiveLong::monoOf )
			                 .apply( tuple );
		}
	
	/**
	 * Create PositiveLong from Mono with Tuple inside
	 * The method helps chaining flows together
	 *
	 * @param tuple
	 * 	the tuple
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<PositiveLong> monoOfMonoWithTuple( Mono<Tuple1<Long>> tuple )
		{
		if( tuple == null )
			return illegalArgumentMonoError( "Input Mono must not be null." );
		else
			return tuple.flatMap( PositiveLong::monoOfTuple );
		}
	
	/**
	 * Create PositiveLong from Mono with positive number inside
	 * The method helps chaining flows together
	 *
	 * @param number
	 * 	the number
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<PositiveLong> monoOfMono( Mono<Long> number )
		{
		if( number == null )
			return illegalArgumentMonoError( "Input Mono must not be null." );
		else
			return number.flatMap( PositiveLong::monoOf );
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
	public static Mono<PositiveLong> copyOf( final PositiveLong other )
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
	static Tuple1<Long> PositiveLong( PositiveLong object )
		{
		return Tuple.of( object.getNumber() );
		}
	
	@Override
	public String toString()
		{
		return "" + this.number;
		}
	
	@Override
	public int compareTo( PositiveLong o )
		{
		return ComparisonChain.start()
		                      .compare( this.number , o.number )
		                      .result();
		}
	}

