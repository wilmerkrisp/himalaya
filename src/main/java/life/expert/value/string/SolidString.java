package life.expert.value.string;
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

import io.vavr.Tuple1;
import io.vavr.control.Try;
import life.expert.common.function.TupleUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import io.vavr.Tuple;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

import com.google.common.collect.ComparisonChain;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Mono;

import java.util.Optional;

import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R

import static life.expert.common.reactivestreams.Patterns.*;    //reactive helper functions

import java.util.ResourceBundle;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R

import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.reactivestreams.Preconditions.*; //reactive check

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

/**
 * Simple immutable String holder class
 * Class invariant: non blank and stripped String
 *
 * - pattern new-call
 * - not for inheritance
 
 *	- only the monoOf.. factory methods is allowed, because it allows you to lazily create objects only with a real subscription
 *	- 'of' - factory method is prohibited because it is intended only for easy creation of objects in tests, please use pure functional methods monoOf.., without raise exceptions.
 *
 *
 *
 *
 * <pre>{@code
 *      var s=SolidString.tryOf( goodString );
 * }******</pre>
 */
@Value
@AllArgsConstructor( access = AccessLevel.PRIVATE )
@Slf4j
@Patterns
public final class SolidString
	implements Comparable<SolidString>
	{
	
	/**
	 * string
	 *
	 * -- SETTER --
	 *
	 * @param string
	 * 	item1
	 * @return item1
	 * 	the item1
	 *
	 * 	-- GETTER --
	 * @return item1
	 * 	the item1
	 */
	private final String string;
	
	//	/**
	//	 * Main fabric method for creating non blank string.
	//	 *
	//	 * @param string
	//	 * 	the string
	//	 *
	//	 * @return the try with SolidString or with exception inside.
	//	 */
	//	public static Try<SolidString> tryOf( final String string )
	//		{
	//		//@formatter:off
//		return Match(string).of(
//				Case( $(isNull()) ,
//				      illegalArgumentFailure( "String must not be null." )),
//				Case( $( String::isBlank ) ,
//				      illegalArgumentFailure( "String must not be blank." )) ,
//				Case( $() ,
//				      s->Success( new SolidString(s.strip())) )
//	                       );
//		//@formatter:on
	//		}
	
	/*
	Other factories use this method to create an object.
	He himself calls the private constructor to create the object.
	* */
	private static Mono<SolidString> monoOf_( final String string )
		{
		return fromSupplier( () -> new SolidString( string ) );
		}
	
	/**
	 * Create SolidString from String
	 * Only the monoOf.. factory methods is allowed, because it allows you to lazily create objects only with a real subscription
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the Mono with lazyli created object
	 *
	 * @implNote to create objects, this method calls the private factory monoOf_
	 */
	public static Mono<SolidString> monoOf( final String string )
		{
		if( string == null )
			return illegalArgumentMonoError( "String must not be null." );
		else if( string.isBlank() )
			return illegalArgumentMonoError( "String must not be blank." );
		else
			return monoOf_( string.strip() );
		}
	
	/**
	 * <pre>
	 * Classic fabric method for creating non blank string.
	 * This factory method is prohibited because it is intended only for easy creation of objects in tests
	 *
	 * @param string the string
	 * @return the non blank string
	 * @throws IllegalArgumentException if string non blank or nullable
	 * @deprecated please use pure functional methods monoOf.., without raise exceptions. </pre>
	 */
	@Deprecated
	public static SolidString of( final String string )
		{
		return monoOf( string ).block();
		}
	
	/**
	 * Create SolidString from Tuple
	 * The method helps with conversion operations Tuple-&gt;SolidString
	 *
	 * @param tuple
	 * 	the tuple
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<SolidString> monoOfTuple( Tuple1<String> tuple )
		{
		if( tuple == null )
			return illegalArgumentMonoError( "Input tuple must not be null." );
		else
			return TupleUtils.function( SolidString::monoOf )
			                 .apply( tuple );
		}
	
	/**
	 * Create SolidString from Mono with Tuple inside
	 * The method helps chaining flows together
	 *
	 * @param tuple
	 * 	the tuple
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<SolidString> monoOfMonoWithTuple( Mono<Tuple1<String>> tuple )
		{
		if( tuple == null )
			return illegalArgumentMonoError( "Input Mono must not be null." );
		else
			return tuple.flatMap( SolidString::monoOfTuple );
		}
	
	/**
	 * Create SolidString from Mono with String inside
	 * The method helps chaining flows together
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the Mono with lazyli created object
	 */
	public static Mono<SolidString> monoOfMono( Mono<String> string )
		{
		if( string == null )
			return illegalArgumentMonoError( "Input Mono must not be null." );
		else
			return string.flatMap( SolidString::monoOf );
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
	public static Mono<SolidString> copyOf( final SolidString other )
		{
		return monoOf_( other.getString() );
		}
	
	//	/**
	//	 * Creating non blank string.
	//	 *
	//	 * @param string
	//	 * 	the string
	//	 *
	//	 * @return the optional witn non blank string if success (or Optional.empty if exception)
	//	 */
	//	public static Optional<SolidString> optionalOf( final String string )
	//		{
	//		return tryOf( string ).toJavaOptional();
	//		}
	
	@Override
	public int compareTo( SolidString o )
		{
		return ComparisonChain.start()
		                      .compare( this.string , o.string )
		                      .result();
		}
	
	@Override
	public String toString()
		{
		return this.string;
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
	public static Tuple1<String> SolidString( SolidString object )
		{
		return Tuple.of( object.getString() );
		
		}
	
	/////////////////////////////Builder/////////////////////////////
	
	/**
	 * The type Non blank string builder.
	 */
	public static class SolidStringBuilder
		{
		
		private StringBuilder string = new StringBuilder();
		
		SolidStringBuilder()
			{
			
			}
		
		/**
		 * String non blank string builder.
		 *
		 * @param string
		 * 	item1
		 *
		 * @return item1 the item1
		 */
		public SolidStringBuilder append( final String string )
			{
			
			this.string.append( string );
			return this;
			}
		
		/**
		 * Build non blank string.
		 *
		 * @return the non blank string
		 *
		 * @deprecated please use pure functional methods #buildOptional #buildMono, without raise exceptions.
		 */
		@Deprecated
		public SolidString build()
			{
			
			return SolidString.of( string.toString() );
			}
		
		/**
		 * Build mono mono.
		 *
		 * @return the mono
		 */
		public Mono<SolidString> buildMono()
			{
			return SolidString.monoOf( string.toString() );
			}
		
		@Override
		public String toString()
			{
			return this.string.toString();
			}
		}
	
	/**
	 * Builder non blank string builder.
	 *
	 * @return the non blank string builder
	 */
	//
	public static SolidStringBuilder builder()
		{
		return new SolidStringBuilder();
		}
	
	/**
	 * To builder non blank string builder.
	 *
	 * @return the non blank string builder
	 */
	public SolidStringBuilder toBuilder()
		{
		return new SolidStringBuilder().append( this.string.toString() );
		}
		
	}
