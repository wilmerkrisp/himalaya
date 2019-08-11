package life.expert.common.reactivestreams;



import io.vavr.Tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.reactivestreams
//                           wilmer 2019/07/31
//
//--------------------------------------------------------------------------------

class ForComprehensionTest
	{
	
	@BeforeEach
	void setUp()
		{
		}
	
	//<editor-fold desc="helper functions for testing">
	Flux<String> excflux( String message )
		{
		log( "excFlux: " + message );
		throw new IllegalStateException( "excFlux" );
		//return just( "excFlux" ).flux();
		}
	
	String excFunc( String message )
		{
		log( "excFunc: " + message );
		throw new IllegalStateException( "excFunc" );
		//return message;
		}
	
	Flux<Object> errflux( String message )
		{
		log( "errFlux: " + message );
		return error( new IllegalStateException( "errFLux event" ) ).flux();
		}
	
	Flux<String> succflux( String message )
		{
		log( "succFlux: " + message );
		return just( "okMessage" ).flux();
		}
	
	String succFunc( String message )
		{
		log( "succFunc: " + message );
		return message;
		}
	
	Flux<Object> emptyflux( String message )
		{
		log( "emptyflux: " + message );
		return empty().flux();
		}
	
	String emptyFunc( String message )
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
	
	//<editor-fold desc="Flux for comprehension testing">
	@Test
	void For_flux()
		{
		var f1  = range( 1 , 10 );
		var f2  = range( 11 , 20 );
		var rez = For( f1 , f2 ).yield( Tuple::of );
		//rez.subscribe( logAtInfoConsumer("NEXT") , logAtErrorConsumer("ERROR") , logAtInfoRunnable("COMPLETE") );
		
		StepVerifier.create( rez )
		            .expectNextCount( 100 )
		            .expectComplete()
		            .verify();
		}
	//</editor-fold>
	
	}