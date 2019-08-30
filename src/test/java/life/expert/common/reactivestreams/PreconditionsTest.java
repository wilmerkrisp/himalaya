package life.expert.common.reactivestreams;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.*;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.vavr.Tuple;
import life.expert.common.function.CheckedUtils;
import lombok.NonNull;//@NOTNULL

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.time.Duration;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import reactor.test.StepVerifier;

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
//import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.NullableUtils.*;//.map(nullableFunction)
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
import static life.expert.common.reactivestreams.Preconditions.*; //reactive check
//import static life.expert.common.function.Patterns.*;    //for-comprehension
import static life.expert.common.base.Objects.*;          //deepCopyOfObject

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case
import static io.vavr.Patterns.*;                         //switch - case - success/failure
import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.reactivestreams
//                           wilmer 2019/07/23
//
//--------------------------------------------------------------------------------

class PreconditionsTest
	{
	private String badString_="";
	
	private String goodString_ ="d";
	
	private ImmutableList<String> badList_;
	
	private ImmutableList<String> goodList_;
	
	private ImmutableMap<String,String> badMap_;
	
	private ImmutableMap<String,String> goodMap_;
	
	private ImmutableList<String> emptyList_;
	
	private ImmutableMap<String,String> emptyMap_;
	
	@BeforeEach
	void setUp()
		{
		badList_ = ImmutableList.of( "1" , "2" , "" );
		goodList_ = ImmutableList.of( "1" , "2" , "3" );
		badMap_ = ImmutableMap.of( "1" , "one" , "2" , "two" , "3" , "" );
		goodMap_ = ImmutableMap.of( "1" , "one" , "2" , "two" , "3" , "three" );
		
		emptyList_ = ImmutableList.of();
		
		emptyMap_ = ImmutableMap.of();
		}
	@Test
	void checkArgument1Test()
		{
		String input = "SUPERVALUE";
		var    f     = checkArgument( input , StringUtils::isNotBlank , "EMPTY" );
		f.subscribe( logAtInfoConsumer( "NEXT" ) , logAtErrorConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
		
		//f.map( TupleUtils.function( this::$method$_ ) ).single() ;
		}
	
	@Test
	void checkArgument2Test()
		{
		String input1 = "1";
		String input2 = "2";
		var    f      = checkArgument( input1 , input2 , ( x , y ) -> false , "EMPTY" );
		f.subscribe( logAtInfoConsumer( "NEXT" ) , logAtErrorConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
		
		//f.map( TupleUtils.function( this::$method$_ ) ).single() ;
		}
	
	@Test
	void checkArgument_exceptionTest()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class , () ->
		{
		checkArgument( badString_ , not(String::isBlank) ,"m").block();
		} );
		assertNotNull( thrown.getMessage() );
		
		}
	
	@Test
	void checkArgument1_tuple_Test()
		{
		String input = "SUPERVALUE";
		var    f     = checkArgument( Tuple.of( 1,2 ) ,(a,b)->true , "EMPTY" );
		f.subscribe( logAtInfoConsumer( "NEXT" ) , logAtErrorConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
		
		//f.map( TupleUtils.function( this::$method$_ ) ).single() ;
		}
	
	@Test
	void checkArgument2_tuple_Test()
		{
		String input1 = "1";
		String input2 = "2";
		var    f      = checkArgument( Tuple.of( 1,2 ) , ( x , y ) -> false , "EMPTY" );
		f.subscribe( logAtInfoConsumer( "NEXT" ) , logAtErrorConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
		
		//f.map( TupleUtils.function( this::$method$_ ) ).single() ;
		}
	
	@Test
	void checkArgument_tuple_exceptionTest()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class , () ->
		{
		checkArgument(Tuple.of( 1,2 ) , ( x , y ) -> false  ,"m").block();
		} );
		assertNotNull( thrown.getMessage() );
		
		}
	
	

	
	
	
	
	@Test
	void checkArgumentTest()
		{
		var o = Preconditions.checkArgument( null , StringUtils::isNotBlank , "arg is blank" );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectError()
		            .verify();
			
		}
	
	@Test
	void checkArgumentTest2()
		{
		var o = Preconditions.checkArgument( " " , StringUtils::isNotBlank , "arg is blank" );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectError()
		            .verify();
		}
	
	@Test
	void checkArgumentTest3()
		{
		var o = Preconditions.checkArgument( "i " , null , "arg is blank" );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectError()
		            .verify();
			
		}
	
	@Test
	void checkArgumentTest4()
		{
		var o = Preconditions.checkArgument( "i " , StringUtils::isNotBlank , "arg is blank" );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectSubscription()
		            .expectNext( "i " )
		            .expectComplete()
		            .verify();
			
		}
	
	
	
	
	
	
	
	
	}