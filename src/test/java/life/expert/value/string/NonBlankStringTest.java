package life.expert.value.string;



import io.vavr.control.Try;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.util.logging.Level;

import static io.vavr.API.Success;
import static reactor.core.publisher.Mono.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
//import static life.expert.common.function.Patterns.*;    //for-comprehension

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.string
//                           wilmer 2019/07/23
//
//--------------------------------------------------------------------------------
@Slf4j
class NonBlankStringTest
	{
	
	private static final String GOOD_STRING = " str ";
	
	private static final String BLANK_STRING = " ";
	
	private static final String NULL_STRING = null;
	
	private static final String STRIPPED_STRING = "str";
	
	private static final String STRING_IS_NULL_FAILURE = "Failure(java.lang.IllegalArgumentException: String must not be null.)";
	
	private static final String STRING_IS_BLANK_FAILURE = "Failure(java.lang.IllegalArgumentException: String must not be blank.)";
	
	@Test
	void tryOfTest()
		{
		
		assert NonBlankString.tryOf( GOOD_STRING )
		                     .get()
		                     .toString()
		                     .equals( STRIPPED_STRING );
		
		assert NonBlankString.tryOf( BLANK_STRING )
		                     .toString()
		                     .equals( STRING_IS_BLANK_FAILURE );
		
		assert NonBlankString.tryOf( NULL_STRING )
		                     .toString()
		                     .equals( STRING_IS_NULL_FAILURE );
		
		//log( NonBlankString.tryOf( NULL_STRING ).toString() );
		}
	
	@Test
	void ofTest()
		{
		NonBlankString.of( GOOD_STRING )
		              .toString()
		              .equals( STRIPPED_STRING );
		
		Assertions.assertThrows( IllegalArgumentException.class , () ->
		{
		NonBlankString.of( BLANK_STRING );
		} );
		
		Assertions.assertThrows( IllegalArgumentException.class , () ->
		{
		NonBlankString.of( NULL_STRING );
		} );
		
		}
	
	@Test
	void optionalOfTest()
		{
		assert NonBlankString.optionalOf( GOOD_STRING )
		                     .get()
		                     .toString()
		                     .equals( STRIPPED_STRING );
		
		assert NonBlankString.optionalOf( BLANK_STRING ).isEmpty();
		
		
		assert NonBlankString.optionalOf( NULL_STRING ).isEmpty();
		
		}
	
	@Test
	void monoOfTest()
		{
		StepVerifier.create( NonBlankString.monoOf( GOOD_STRING ) )
		            .expectNextCount( 1 )
		            .expectComplete()
		            .verify();
		
		StepVerifier.create( NonBlankString.monoOf( BLANK_STRING ) )
		            .expectError()
		            .verify();
		
		StepVerifier.create( NonBlankString.monoOf( NULL_STRING ) )
		            .expectError()
		            .verify();
		
		}
	
	@Test
	void monoOfTest2()
		{
		String input = " ";
		var f = justOrEmpty( input ).filter( StringUtils::isNotBlank )
		                            .single()
		                            .onErrorMap( illegalArgumentException( "Invalid param1" ) )
		                            .log( "debug" , Level.FINE , SignalType.ON_NEXT )
		                            .map( this::someMethod_ )                           // если плохо, если $method$_  вернет null
		                            //.flatMap( nullableFunction( this::$method$_ ) )  // если нормально для  $method$_  вернуть null
		                            .single();
		
		//var ff=checkNotNull( input ).then(just("NEW VALUE"));
		var ff = just( input ).then( just( "NEW VALUE" ) );
		ff.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtErrorConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		
		}
	
	private String someMethod_( final String string )
		{
		logAtInfo( "tested method" );
		//throw new RuntimeException( " cause " );
		return "otvet+" + string;
		//return null;
		
		}
		
	}