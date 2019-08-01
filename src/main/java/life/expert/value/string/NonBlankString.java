package life.expert.value.string;



import io.vavr.Tuple1;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import io.vavr.Tuple;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

import com.google.common.collect.ComparisonChain;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Mono;

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R
import static life.expert.common.function.TupleUtils.*; //vavr's tuple->R INTO func->R

import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.NullableUtils.*;//.map(nullableFunction)
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
import static life.expert.common.reactivestreams.Preconditions.*; //reactive check
import static life.expert.common.reactivestreams.Patterns.*;    //reactive helper functions
import static life.expert.common.base.Objects.*;          //deepCopyOfObject

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case
import static io.vavr.Patterns.*;                         //switch - case - success/failure
import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.string
//                           wilmer 2019/05/12
//
//--------------------------------------------------------------------------------

/**
 * simple immutable class: non blank and stripped String
 *
 * - pattern new-call
 * - not for inheritance
 *
 * <pre>{@code
 *      var s=NonBlankString.tryOf( goodString );
 * }</pre>
 *
 */
@Value
@AllArgsConstructor( access = AccessLevel.PRIVATE )
@Slf4j
@Patterns
public final class NonBlankString
	implements Comparable<NonBlankString>
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
	
	/**
	 * Main fabric method for creating non blank string.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the try with NonBlankString or with exception inside.
	 */
	public static Try<NonBlankString> tryOf( final String string )
		{
		//@formatter:off
		return Match(string).of(
				Case( $(isNull()) ,
				      illegalArgumentFailure( "String must not be null." )),
				Case( $( String::isBlank ) ,
				      illegalArgumentFailure( "String must not be blank." )) ,
				Case( $() ,
				      s->Success( new NonBlankString(s.strip())) )
	                       );
		//@formatter:on
		}
	
	/**<pre>
	 * Classic fabric method for creating non blank string.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the non blank string
	 *
	 * @throws IllegalArgumentException
	 * 	if string non blank or nullable
	 *
	 * @deprecated please use pure functional methods #optionalOf #monoOf, without raise exceptions.
	</pre>*/
	@Deprecated
	public static NonBlankString of( final String string )
		{
		return tryOf( string ).get();
		}
	
	/**
	 * Creating non blank string.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the optional witn non blank string if success (or Optional.empty if exception)
	 */
	public static Optional<NonBlankString> optionalOf( final String string )
		{
		return tryOf( string ).toJavaOptional();
		}
	
	/**
	 * Mono of mono.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the mono
	 */
	public static Mono<NonBlankString> monoOf( final String string )
		{
		return monoFromTry( tryOf( string ) );
		}
	
	@Override
	public int compareTo( NonBlankString o )
		{
		return ComparisonChain.start()
		                      .compare( this.string , o.string )
		                      .result();
		}
	
	@Override
	public String toString()
		{
		return this.string ;
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
	public static Tuple1<String> NonBlankString( NonBlankString object )
		{
		return Tuple.of( object.getString() );
		
		}
	
	/////////////////////////////Builder/////////////////////////////
	
	/**
	 * The type Non blank string builder.
	 */
	public static class NonBlankStringBuilder
		{
		
		private String string;
		
		NonBlankStringBuilder()
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
		public NonBlankStringBuilder string( final String string )
			{
			
			this.string = string;
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
		public NonBlankString build()
			{
			
			return NonBlankString.of( string );
			}
		
		/**
		 * Build optional optional.
		 *
		 * @return the optional
		 */
		public Optional<NonBlankString> buildOptional()
			{
			return NonBlankString.optionalOf( string );
			}
		
		public Try<NonBlankString> buildTry()
			{
			return NonBlankString.tryOf( string );
			}
		
		/**
		 * Build mono mono.
		 *
		 * @return the mono
		 */
		public Mono<NonBlankString> buildMono()
			{
			return NonBlankString.monoOf( string );
			}
		
		@Override
		public String toString()
			{
			return this.string ;
			}
		}
	
	/**
	 * Builder non blank string builder.
	 *
	 * @return the non blank string builder
	 */
	//
	public static NonBlankStringBuilder builder()
		{
		return new NonBlankStringBuilder();
		}
	
	/**
	 * To builder non blank string builder.
	 *
	 * @return the non blank string builder
	 */
	public NonBlankStringBuilder toBuilder()
		{
		return new NonBlankStringBuilder().string( this.string );
		}
		
	}
