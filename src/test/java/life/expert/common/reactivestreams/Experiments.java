package life.expert.common.reactivestreams;



import io.vavr.Tuple;

import life.expert.common.function.CheckedUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static reactor.core.publisher.Mono.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.reactivestreams.Patterns.*;
import static life.expert.common.reactivestreams.ForComprehension.*;
//import static life.expert.common.function.Patterns.*;    //for-comprehension

import static io.vavr.API.$;                            // pattern matching
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.CheckedFunction;//checked functions
import static io.vavr.API.unchecked;    //checked->unchecked
import static io.vavr.API.Function;     //lambda->Function3
import static io.vavr.API.Tuple;


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

class Experiments
	{
	
	@BeforeEach
	void setUp()
		{
		}
	
	//<editor-fold desc="helper functions for testing">
	public static Flux<String> excflux( String message )
		{
		log( "excFlux: " + message );
		throw new IllegalStateException( "excFlux" );
		//return just( "excFlux" ).flux();
		}
	
	public static  String excFunc( String message )
		{
		log( "excFunc: " + message );
		throw new IllegalStateException( "excFunc" );
		//return message;
		}
	
	public static Flux<Object> errflux( String message )
		{
		log( "errFlux: " + message );
		return error( new IllegalStateException( "errFLux event" ) ).flux();
		}
	
	public static Flux<String> succflux( String message )
		{
		log( "succFlux: " + message );
		return just( "okMessage" ).flux();
		}
	
	public static String succFunc( String message )
		{
		log( "succFunc: " + message );
		return message;
		}
	
	public static Flux<Object> emptyflux( String message )
		{
		log( "emptyflux: " + message );
		return empty().flux();
		}
	
	public static String emptyFunc( String message )
		{
		log( "emptyFunc: " + message );
		return null;
		}
	//</editor-fold>
	
	//<editor-fold desc="error event functions">
	
	@Test
	void test_empty_func()
		{
		
		//if null is UNnormal for return from the func
		var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .map( i -> emptyFunc( "_1_" ) )
		            .map( i -> succFunc( "_2_" ) )
		            .flatMap( i -> succflux( "_3_" ) );
		o.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		
		}
	
	@Test
	void test_empty_flux()
		{
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> justOrEmpty( emptyFunc( "_1_" ) ) )
		            .map( i -> succFunc( "_2_" ) )
		            .flatMap( i -> succflux( "_3_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	@Test
	void test_err_flux()
		{
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> errflux( "_1_" ) )
		            .map( i -> succFunc( "_2_" ) )
		            .flatMap( i -> succflux( "_3_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	@Test
	void test_exc_func()
		{
		log( "____________________________________" );
		//if null is UNnormal for return from the func
		var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .map( i -> excFunc( "_1_" ) )
		            .map( i -> succFunc( "_2_" ) )
		            .flatMap( i -> succflux( "_3_" ) );
		o.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		
		log( "____________________________________" );
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> justOrEmpty( excFunc( "_1_" ) ) )
		            .map( i -> succFunc( "_2_" ) )
		            .flatMap( i -> succflux( "_3_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	@Test
	void test_exc_flux()
		{
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> excflux( "_1_" ) )
		            .map( i -> succFunc( "_2_" ) )
		            .flatMap( i -> succflux( "_3_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="repair afret error functions">
	
	@Test
	void testRepair_empty_func()
		{
		
		//if null is UNnormal for return from the func
		var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .map( i -> emptyFunc( "_1_" ) )
		            .onErrorContinue( ( a , b ) -> log( "_2_" ) )
		            .map( i -> succFunc( "_3_" ) )
		            .flatMap( i -> succflux( "_4_" ) );
		o.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		
		}
	
	@Test
	void testRepair_empty_flux()
		{
		
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> justOrEmpty( emptyFunc( "_1_" ) ) )
		            .onErrorContinue( logAtErrorBiConsumer( "_2_" ) )
		            .map( i -> succFunc( "_3_" ) )
		            .flatMap( i -> succflux( "_4_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	@Test
	void testRepair_err_flux()
		{
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> errflux( "_1_" ) )
		            .onErrorContinue( logAtErrorBiConsumer( "_2_" ) )
		            .map( i -> succFunc( "_3_" ) )
		            .flatMap( i -> succflux( "_4_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	@Test
	void testRepair_exc_func()
		{
		log( "____________________________________" );
		//if null is UNnormal for return from the func
		var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .map( i -> excFunc( "_1_" ) )
		            .onErrorContinue( logAtErrorBiConsumer( "_2_" ) )
		            .map( i -> succFunc( "_3_" ) )
		            .flatMap( i -> succflux( "_4_" ) );
		o.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		
		log( "____________________________________" );
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> justOrEmpty( excFunc( "_1_" ) ) )
		            .onErrorContinue( logAtErrorBiConsumer( "_2_" ) )
		            .map( i -> succFunc( "_3_" ) )
		            .flatMap( i -> succflux( "_4_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	@Test
	void testRepair_exc_flux()
		{
		//if null is normal for return from the func
		var a = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .flatMap( i -> excflux( "_1_" ) )
		            .onErrorContinue( logAtErrorBiConsumer( "_2_" ) )
		            .map( i -> succFunc( "_3_" ) )
		            .flatMap( i -> succflux( "_4_" ) );
		a.subscribe( logAtInfoConsumer( "NEXTT" ) , logAtInfoConsumer( "ERRORR" ) , logAtInfoRunnable( "COMPLETEE" ) );
		}
	
	//</editor-fold>
	
	
	
	
	
	//	@Test
	//	void checkArgument_Map_filter()
	//		{
	//		Throwable thrown = assertThrows( IllegalArgumentException.class , () ->
	//		{
	//		checkArgument( badMap_ , String::isBlank,"m" );
	//		} );
	//		assertNotNull( thrown.getMessage() );
	//		}
	
	public static Mono<String> meth1( final String string1 ,
	                                  final String string2 ,
	                                  final String string3 )
		{
		//		//@formatter:off
//		return zip( justOrEmpty( string1 ).filter( StringUtils::isNotBlank ).single().onErrorMap( illegalArgumentException( "Invalid param1" ) ),
//		            justOrEmpty( string2 ).filter( StringUtils::isNotBlank ).single().onErrorMap( illegalArgumentException( "Invalid param2" ) ) ,
//		            justOrEmpty( string3 ).filter( StringUtils::isNotBlank ).single().onErrorMap( illegalArgumentException( "Invalid param3" ) ) )
//			                .map( TupleUtils.function( PatternsTest::meth1_ )).single() ;
//		//@formatter:on
		
		//@formatter:off
		return zip( Preconditions.checkArgument( string1,StringUtils::isNotBlank,"invalid param1" ) ,
		            justOrEmpty( string2 ).filter( StringUtils::isNotBlank ).single().onErrorMap( CheckedUtils.illegalArgumentException( "Invalid param2" ) ) ,
		            justOrEmpty( string3 ).filter( StringUtils::isNotBlank ).single().onErrorMap( CheckedUtils.illegalArgumentException( "Invalid param3" ) ) )
			                .map( TupleUtils.function( Experiments::meth1_ )).single() ;
		//@formatter:on
		
		}
	
	public static String meth1_( final String string1 ,
	                             final String string2 ,
	                             final String string3 )
		{
		//throw new RuntimeException( " cause " );
		return "otvet+" + string1 + string2 + string3;
		}
	
	public static Mono<String> meth1err( final String string1 ,
	                                     final String string2 ,
	                                     final String string3 )
		{
		//@formatter:off
		return zip( justOrEmpty( string1 ).filter( StringUtils::isNotBlank ).single().onErrorMap( CheckedUtils.illegalArgumentException( "Invalid param1" ) ),
		            justOrEmpty( string2 ).filter( StringUtils::isNotBlank ).single().onErrorMap( CheckedUtils.illegalArgumentException( "Invalid param2" ) ) ,
		            justOrEmpty( string3 ).filter( StringUtils::isNotBlank ).single().onErrorMap( CheckedUtils.illegalArgumentException( "Invalid param3" ) ) )
			                .map( TupleUtils.function( Experiments::meth1err_ )).single() ;
		//@formatter:on
		
		}
	
	public static String meth1err_( final String string1 ,
	                                final String string2 ,
	                                final String string3 )
		{
		throw new RuntimeException( " cause " );
		///return "otvet+" + string1 + string2 + string3;
		}
	
	public static Mono<String> meth1noerr( final String string1 ,
	                                       final String string2 ,
	                                       final String string3 )
		{
		//		//@formatter:off
//		return zip( justOrEmpty( string1 ).filter( StringUtils::isNotBlank ).single().onErrorMap( illegalArgumentException( "Invalid param1" ) ),
//		            justOrEmpty( string2 ).filter( StringUtils::isNotBlank ).single().onErrorMap( illegalArgumentException( "Invalid param2" ) ) ,
//		            justOrEmpty( string3 ).filter( StringUtils::isNotBlank ).single().onErrorMap( illegalArgumentException( "Invalid param3" ) ) )
//			                .map( TupleUtils.function( PatternsTest::meth1_ )).single() ;
//		//@formatter:on
		
		//@formatter:off
		return zip( justOrEmpty( string1 ) ,
		            justOrEmpty( string2 )  ,
		            justOrEmpty( string3 )  )
			                .map( TupleUtils.function( Experiments::meth1_ )) ;
		//@formatter:on
		
		}
	
	@Test
	void forTest1()
		{
		var a = "one";
		var b = "two";
		var c = "three";
		
		var o = meth1( a,b,c );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectSubscription()
		            .expectNext( "otvet+onetwothree" )
		            .expectComplete()
		            .verify();
			
			
		}
	
	
	@Test
	void forTest2()
		{
		String a = null;
		var    b = "two";
		var    c = "three";
		
		var o = meth1( a , b , c );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectError()
		            .verify();
			
			
		}
	
	
	@Test
	void forTest3()
		{
		String a = "";
		var    b = "two";
		var    c = "three";
		
		
		var o = meth1( a , b , c );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectError()
		            .verify();
		}
	
	
	
	@Test
	void forTest4()
		{
		String a = "error";
		var    b = "two";
		var    c = "three";
		
		
		var o = meth1err( a , b , c );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectError()
		            .verify();
		}
	
	
	
	@Test
	void forTest5()
		{
		String a = null;
		var    b = "two";
		var    c = "three";
		
		
		var o = meth1noerr( a , b , c );
		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( o )
		            .expectComplete()
		            .verify();
		}
		
		
		
		
		
	}