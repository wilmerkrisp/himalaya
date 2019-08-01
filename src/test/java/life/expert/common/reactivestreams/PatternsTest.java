package life.expert.common.reactivestreams;



import life.expert.common.function.CheckedUtils;
import life.expert.common.reactivestreams.Preconditions;

import org.junit.jupiter.api.Test;

import java.time.Duration;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import reactor.test.StepVerifier;

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
			                .map( TupleUtils.function( PatternsTest::meth1_ )).single() ;
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
			                .map( TupleUtils.function( PatternsTest::meth1err_ )).single() ;
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
			                .map( TupleUtils.function( PatternsTest::meth1_ )) ;
		//@formatter:on
		
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