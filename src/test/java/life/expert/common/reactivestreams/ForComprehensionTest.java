package life.expert.common.reactivestreams;



import static org.junit.jupiter.api.Assertions.*;

import io.vavr.CheckedFunction2;
import io.vavr.Function2;
import io.vavr.control.Try;
import life.expert.value.string.NonBlankString;
import lombok.NonNull;//@NOTNULL

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

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
//                           wilmer 2019/07/31
//
//--------------------------------------------------------------------------------

class ForComprehensionTest
	{
	
	@BeforeEach
	void setUp()
		{
		
		}
	
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
	
	
	
	}