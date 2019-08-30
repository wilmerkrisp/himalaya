package life.expert.common.reactivestreams;



import life.expert.common.function.CheckedUtils;
import life.expert.common.reactivestreams.Preconditions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import reactor.test.StepVerifier;

import static life.expert.common.reactivestreams.Patterns.tryFromFlux;
import static reactor.core.publisher.Mono.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo

//switch

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/18
//
//--------------------------------------------------------------------------------

class PatternsTest
	{
	
	
	@BeforeEach
	void setUp()
		{
		/*Spring Reactor global error log handler*/
		Hooks.onOperatorError( ( err , data ) ->
		                       {
		                       String s = ( data == null ) ? "No additional data" : "Additional data" + data.toString();
		                       print( "Global Stream Error: %s %s",s,err );
		                       //logger_.error( s , err );
		                       return err;
		                       } );
		}
	
	@Test
	void tryFromMonoTest()
		{
		
		}
	
	@Test
	void tryFromFluxTest()
		{
		var a=tryFromFlux(Experiments.succflux( "OKK" ));
		System.out.println("1) " +a);
		assert a.isSuccess();
		
		var b=tryFromFlux(Experiments.errflux( "ERR" ));
		System.out.println("2) " +b);
		assert b.isFailure();
		
		
//		var b=tryFromFlux(Experiments.excflux( "ERR" ));
//		System.out.println("2) " +b);
//		assert b.isFailure();
		}

	
	/*
	full analog of:
	
	    var a = "one";
	    var b = "two";
	    var c = "three";
	    
	    var o = justOrEmpty( a ).flatMap( x -> justOrEmpty( b ).flatMap( y -> justOrEmpty( c ).map( z -> Tuples.of( x , y , z ) ) ) );
	    
	    //var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 ) .map( i -> "a" + i );
	    o.subscribe( logAtInfoConsumer( "NEXT" ) , logAtInfoConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
    
    
    
    
    
    
var o = Flux.just( 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 )
            .map( i -> "a" + i )
            .buffer( 2 )
            .map( l -> (Tuple2<String,String>)Tuples.fromArray( l.toArray() ) )
            .map( TupleUtils.function( Person::new ) );
o.subscribe( logAtInfoConsumer( "NEXT" ) , logAtInfoConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );



//@formatter:off
		return Mono.zip(  l -> (Tuple3<String,String,String>)Tuples.fromArray( l ) ,
				             justOrEmpty( string1 ).filter( StringUtils::isNotBlank ) ,
				             justOrEmpty( string2 ).filter( StringUtils::isNotBlank ) ,
				             justOrEmpty( string3 ).filter( StringUtils::isNotBlank ) );//.map( TupleUtils.function( PatternsTest::meth1 )) ;
		//@formatter:on
	* */
	
	
	

	
	
	
	
	
	//
	//	@Test
	//	void checkArgumentTest5()
	//		{
	//		var o = checkArgument( "i " ,  StringUtils::isNotBlank  , "arg is blank" );
	//		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
	//
	//		var b=error(new RuntimeException( "vovaexc" )).flatMap( m->justOrEmpty( m ).just(elem()) );
	//		b.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
	//		}
	//
	//	public static String elem()
	//		{
	//		print("EMIT2");
	//		return "el2";
	//		}
	
	//	@Test
	//	void checkArgumentTest3()
	//		{
	//		var o = error( new RuntimeException( "EXCC1" ) ).then( defer( printSupplier( "mymsgg" , error( new RuntimeException( "EXCC2" ) ) ) ) )
	//		                                                .single();
	//
	//
	//		o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
	//		}
	
	}