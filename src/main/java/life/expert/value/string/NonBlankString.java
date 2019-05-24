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

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.string
//                           wilmer 2019/05/12
//
//--------------------------------------------------------------------------------









/**
 * simple immutable class: String
 *
 * - pattern new-call
 * - not for inheritance
 *
 * <pre>{@code
 * //pattern new-call
 * 	  var  o = NonBlankString.of("Test");
 * 	  var o = NonBlankString.<String, String>builder().item1("f").build();
 * var b=o.compute();
 * }****</pre>
 *
 *
 * Every constructor/fabric can raise the exceptions:
 * throws NullPointerException if argument nullable
 * throws IllegalArgumentException if argument empty
 */
@Value
@AllArgsConstructor( access = AccessLevel.PRIVATE )
@Slf4j
@Patterns
public final class NonBlankString
	implements Comparable<NonBlankString>
	{
	
	
	
	/**
	 * item1
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
	
	
	
	/*
	pattern matching in vavr
	
	    - you need add static import to method with pattern matching
	    import static life.expert.value.string.NonBlankStringPatterns.*;
	*/
	@Unapply
	static Tuple1<String> NonBlankString( NonBlankString object )
		{
		return Tuple.of( object.getString() );
		
		}
	
	
	
	@Override
	public int compareTo( NonBlankString o )
		{
		return ComparisonChain.start()
		                      .compare( this.string , o.string )
		                      .result();
		}
	
	
	
	/**
	 * Of non blank string.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the non blank string
	 *
	 * @deprecated please use pure functional methods #optionalOf #monoOf, without raise exceptions.
	 */
	@Deprecated
	public static NonBlankString of( final String string )
		{
		//@formatter:off
		Try<NonBlankString> try_ = Match( string ).of( Case( $( isNull() ) ,
                                         nullPointerFailure ( "string is marked non-null but is null" ) ) ,
                                   Case( $( String::isBlank ) ,
                                         illegalArgumentFailure( "string is marked non-blank but is blunk" )) ,
                                   Case( $() ,
                                          Success(  new NonBlankString( string ) ) ) );
			//@formatter:on
		
		return try_.get();
		}
	
	
	
	/**
	 * Optional of optional.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the optional
	 */
	public static Optional<NonBlankString> optionalOf( final String string )
		{
		return Optional.ofNullable( string )
		               .filter( StringUtils::isNotBlank )
		               .map( NonBlankString::new );
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
		return justOrEmpty( string ).filter( StringUtils::isNotBlank )
		                            .map( NonBlankString::new )
		                            .single();
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
			return "NonBlankString.NonBlankStringBuilder(string=" + this.string + ")";
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
