package life.expert.common.function;









import static life.expert.common.async.LogUtils.printConsumer;
import static life.expert.common.async.LogUtils.printRunnable;
import static life.expert.common.function.CheckedUtils.*;
import static reactor.core.publisher.Mono.justOrEmpty;

import io.vavr.control.Try;


import life.expert.common.async.ThreadUtils;
import life.expert.common.reactivestreams.Patterns;
import life.expert.common.reactivestreams.Preconditions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.Duration;


//import static life.expert.common.base.Preconditions.*;  //checkCollection


import static reactor.core.scheduler.Schedulers.*;
import static life.expert.common.async.LogUtils.*;
//import static life.expert.common.base.Preconditions.*;  //checkCollection



//import java.util.List;                                 //usual list
//import io.vavr.collection.List;                        //immutable List
//import com.google.common.collect.*;                   //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/10
//
//--------------------------------------------------------------------------------









class CheckedUtilsTest
	{
	
	
	
	private static void throwIoException_( final String string )
	throws IOException
		{
		throw new IOException( " cause " );
		
		}
	
	
	
	private static void noThrowIoException_( final String string )
	throws IOException
		{
		
		
		}
	
	
	
	@Test
	void consumerToTryTest()
		{
		
		var t1 = consumerToTry( CheckedUtilsTest::throwIoException_ ).apply( "test" );
		var t2 = consumerToTry( CheckedUtilsTest::noThrowIoException_ ).apply( "test" );
		
		//System.out.println( "RESULT:" + t );
		assert t1 instanceof Try.Failure;
		assert t2 instanceof Try.Success;
		
		}
	
	
	
	@Test
	void consumerToOptionTest()
		{
		
		
		var t1 = consumerToOptional( CheckedUtilsTest::throwIoException_ ).apply( "test" );
		var t2 = consumerToOptional( CheckedUtilsTest::noThrowIoException_ ).apply( "test" );
		
		//		System.out.println( "RESULT:" + t1 );
		//		System.out.println( "RESULT:" + t2 );
		
		assert t1.isEmpty();
		assert t2.isPresent();
		}
	
	
	
	@Test
	void consumerToBooleanTest()
		{
		var t2 = consumerToBoolean( CheckedUtilsTest::noThrowIoException_ ).apply( "test" );
		//System.out.println(t2 );
		try
			{
			var t1 = consumerToBoolean( CheckedUtilsTest::throwIoException_ ).apply( "test" );
			}
		catch( Exception e )
			{
			return;
			}
		
		assert false;
		}
	
	
	
	@Test
	void consumerToBooleanReactiveTest()
		{
		
		var b1 = justOrEmpty( "test" ).map( consumerToBoolean( CheckedUtilsTest::throwIoException_ ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( b1 )
		            .expectError()
		            .verify();
		}
	
	
	
	@Test
	void consumerToMonoTest()
		{
		
		var b1 = justOrEmpty( "test" ).flatMap( consumerToMono( CheckedUtilsTest::throwIoException_ ) );
		//var b1 = justOrEmpty( "test" ).flatMap( consumerToMono( null ) );
		b1.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		//		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		//		StepVerifier.create( b1 )
		//		            .expectError()
		//		            .verify();
		//System.out.println( "RESULT:" );
		}
	
	
	
	@Test
	void uncheckedFunctionTest()
		{
		var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .map( i -> "a" + i );
		
		
		var b = o.parallel()
		         .runOn( parallel() )
		         .map( uncheckedFunction( x ->
		                                  {
		                                  throw new IOException();
		                                  } ) )
		         .sequential()
		         .onErrorContinue( printBiConsumer( "CONTINUEE" ) );
		
		//b.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( b )
		            .expectSubscription()
		            .expectComplete()
		            .verify();
		
		//System.out.println( "RESULT:" );
		
		//ThreadUtils.delay( 20 );
		}
	
	
	
	@Test
	void functionToMonoTest()
		{
		
		
		var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
		            .map( i -> "a" + i );
		
		
		/*
		// v1
		var b = o.flatMap( e -> fromSupplier( delaySupplierWithError( "GENERATE" , e , "a2" , 1 ) ).subscribeOn( elastic() )
		                                                                                           .onErrorReturn( "!!!BIGERROR" ) );
		//v2
		/var b = o.flatMap( e -> fromSupplier( delaySupplierWithError( "GENERATE" , e , "a2" , 1 ) ).subscribeOn( elastic() ) )
		         .onErrorContinue( logAtErrorBiConsumer( "CONTINUEE" ) );
		
		//v3
		var b = o.flatMap( e -> fromSupplier( () -> Try( delaySupplierWithError( "GENERATE" , e , "a2" , 1 ) ) ).subscribeOn( elastic() )
		                                                                                                        .map( t -> t.onFailure( logAtErrorConsumer( "ERRR:" + e ) ) )
		                                                                                                        .map( Try::getOrNull ) )
		         .onErrorContinue( logAtErrorBiConsumer( "CONTINUEE" ) );
		*/
		
		
		var b = o.flatMap( Patterns.functionToMonoParallelLogError( x ->
		                                                   {
		                                                   //return x;
		                                                   throw new IOException();
		                                                   } , elastic(), "vovan" ) );
		//  .onErrorContinue !НЕ РАБОТАЕТ ТК применимо только к ограниченному набору операторов
		
		
		
		b.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		ThreadUtils.delay( 20 );
		}
		
		
		
	}