package life.expert.common.io;



import static life.expert.common.function.CheckedUtils.consumerToBoolean;

import io.vavr.CheckedFunction1;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import life.expert.common.async.ThreadUtils;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.Duration;

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static life.expert.common.function.NullableUtils.nullableFunction;
import static life.expert.common.io.FileUtils.createFile;
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
import static life.expert.common.async.LogUtils.*;
import static life.expert.common.base.Objects.*;        //deepCopyOfObject

import static io.vavr.API.*;                            //switch
import static io.vavr.Predicates.*;                     //switch - case
import static io.vavr.Patterns.*;                       //switch - case - success/failure
import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;

//import java.util.List;                                 //usual list
//import io.vavr.collection.List;                        //immutable List
//import com.google.common.collect.*;                   //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.io
//                           wilmer 2019/05/10
//
//--------------------------------------------------------------------------------

class FileUtilsTest
	{
	
	private static void throwIoException_( final String string )
	throws IOException
		{
		System.out.println( "FileUtilsTest throwIoException_ " + string );
		throw new IOException( " cause " );
		
		}
	
	private static void noThrowIoException_( final String string )
	throws IOException
		{
		
		}
	
	@Test
	void createFileTest()
		{
		
		var b1 = justOrEmpty( "test" ).map( consumerToBoolean( FileUtilsTest::throwIoException_ ) );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( b1 )
		            .expectError()
		            .verify();
		
		//
		//
		//		var o = Flux.just( "1" , null , "3" )
		//		            .map( i -> "a" + i );
		//		//o.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		//
		//
		//		var b1 = justOrEmpty( "" ).map( consumerToBoolean( FileUtilsTest::throwIoException_ ) );
		//		b1.subscribe( printConsumer( "NEXT" ) , ()->{} , printRunnable( "COMPLETE" ) );
		//
		//
		//		var b2 = justOrEmpty( "" ).map( consumerToBoolean( FileUtilsTest::noThrowIoException_ ) );
		//		b2.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		
		}
	
	private static Mono<Path> createFile2( String path ,
	                                       String defaultParentDirectory ,
	                                       String defaultFileName )
		{
		var path_ = Flux.from( justOrEmpty( path ) )
		                .takeWhile( StringUtils::isNotBlank )
		                .map( p -> Paths.get( p ) );
		
		var default_name = Flux.from( justOrEmpty( defaultFileName ) )
		                       .takeWhile( StringUtils::isNotBlank )
		                       .map( p -> Paths.get( p ) );
		
		var default_root = Flux.from( justOrEmpty( defaultParentDirectory ) )
		                       .takeWhile( StringUtils::isNotBlank )
		                       .map( p -> Paths.get( p ) );
		
		var name = path_.flatMap( nullableFunction( Path::getFileName ) )
		                .switchIfEmpty( default_name );
		
		var root = path_.flatMap( nullableFunction( Path::getParent ) )
		                .switchIfEmpty( default_root );
		
		//		var p1= Paths.get( path );
		//		var n1=p1.getFileName();
		//		var d1=p1.getParent();
		
		var name_root = Flux.concat( root , name )
		                    .reduce( Path::resolve );
		return ( name_root.single() );
		
		}
	
	@Test
	void createFileTest2_1()
		{
		var m = createFile2( "/one/onee.txt" , "/two/" , "twoo.txt" );
		//m.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		//ThreadUtils.delay( 10 );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( m )
		            .expectSubscription()
		            .expectNext( Paths.get( "/one/onee.txt" ) )
		            .expectComplete()
		            .verify();
			
		}
	
	@Test
	void createFileTest2_2()
		{
		var m = createFile2( "" , "/two/" , "twoo.txt" );
		//m.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		//ThreadUtils.delay( 10 );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( m )
		            .expectSubscription()
		            .expectNext( Paths.get( "/two/twoo.txt" ) )
		            .expectComplete()
		            .verify();
		}
	
	@Test
	void createFileTest2_3()
		{
		var m = createFile2( "onee.txt" , "/two/" , "twoo.txt" );
		//m.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		//ThreadUtils.delay( 10 );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( m )
		            .expectSubscription()
		            .expectNext( Paths.get( "/two/onee.txt" ) )
		            .expectComplete()
		            .verify();
			
		}
	
	@Test
	void createFileTest2_4()
		{
		var m = createFile2( "/onee/" , "/two/" , "twoo.txt" );
		//m.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		//ThreadUtils.delay( 10 );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( m )
		            .expectSubscription()
		            .expectNext( Paths.get( "/onee" ) )
		            .expectComplete()
		            .verify();
			
		}
	
	@Test
	void createFileTest2_5()
		{
		var m = createFile2( "onee.txt" , null , null );
		//m.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		//ThreadUtils.delay( 10 );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( m )
		            .expectSubscription()
		            .expectNext( Paths.get( "onee.txt" ) )
		            .expectComplete()
		            .verify();
			
		}
	
	@Test
	void createFileTest2_6()
		{
		var m = createFile2( null , null , null );
		//m.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
		//ThreadUtils.delay( 10 );
		
		StepVerifier.setDefaultTimeout( Duration.ofSeconds( 1 ) );
		StepVerifier.create( m )
		            .expectSubscription()
		            .expectError()
		            .verify();
			
		}
	
	//	@Test
	//	void createFileTest2_7()
	//		{
	//		var m = createFile( "onee.txt" , null , null );
	//		m.subscribe( printConsumer( "NEXT" ) , printConsumer( "ERROR" ) , printRunnable( "COMPLETE" ) );
	//		ThreadUtils.delay( 10 );
	//
	//
	//
	//		}
	//
	
	}