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

/**
 * Simple immutable String holder class
 * Class invariant: non blank and stripped String
 *
 * - pattern new-call
 * - not for inheritance
 *
 * <pre>{@code
 *      var s=SolidString.tryOf( goodString );
 * }*</pre>
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
	
	/**
	 * Main fabric method for creating non blank string.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the try with SolidString or with exception inside.
	 */
	public static Try<SolidString> tryOf( final String string )
		{
		//@formatter:off
		return Match(string).of(
				Case( $(isNull()) ,
				      illegalArgumentFailure( "String must not be null." )),
				Case( $( String::isBlank ) ,
				      illegalArgumentFailure( "String must not be blank." )) ,
				Case( $() ,
				      s->Success( new SolidString(s.strip())) )
	                       );
		//@formatter:on
		}
	
	/**
	 * <pre>
	 * Classic fabric method for creating non blank string.
	 *
	 * @param string        the string
	 * @return the non blank string
	 * @throws IllegalArgumentException        if string non blank or nullable
	 * @deprecated please use pure functional methods #optionalOf #monoOf, without raise exceptions. </pre>
	 */
	@Deprecated
	public static SolidString of( final String string )
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
	public static Optional<SolidString> optionalOf( final String string )
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
	public static Mono<SolidString> monoOf( final String string )
		{
		return monoFromTry( tryOf( string ) );
		}
	
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
		 * Build optional optional.
		 *
		 * @return the optional
		 */
		public Optional<SolidString> buildOptional()
			{
			return SolidString.optionalOf( string.toString() );
			}
		
		/**
		 * Build try try.
		 *
		 * @return the try
		 */
		public Try<SolidString> buildTry()
			{
			return SolidString.tryOf( string.toString() );
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
