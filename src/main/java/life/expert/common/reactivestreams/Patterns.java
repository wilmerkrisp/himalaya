package life.expert.common.reactivestreams;
//-------------------------------------------------------------------------------------------------------
//  __    __   __  .___  ___.      ___       __          ___   ____    ____  ___   ____    ____  ___
// |  |  |  | |  | |   \/   |     /   \     |  |        /   \  \   \  /   / /   \  \   \  /   / /   \
// |  |__|  | |  | |  \  /  |    /  ^  \    |  |       /  ^  \  \   \/   / /  ^  \  \   \/   / /  ^  \
// |   __   | |  | |  |\/|  |   /  /_\  \   |  |      /  /_\  \  \_    _/ /  /_\  \  \_    _/ /  /_\  \
// |  |  |  | |  | |  |  |  |  /  _____  \  |  `----./  _____  \   |  |  /  _____  \   |  |  /  _____  \
// |__|  |__| |__| |__|  |__| /__/     \__\ |_______/__/     \__\  |__| /__/     \__\  |__| /__/     \__\
//
//                                            Wilmer Krisp 2019/02/05
//--------------------------------------------------------------------------------------------------------

import io.vavr.*;

import io.vavr.control.Try;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import lombok.NonNull;//@NOTNULL

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.util.Objects;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.NullableUtils.*;//.map(nullableFunction)
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
import static life.expert.common.base.Objects.*;          //deepCopyOfObject
import static life.expert.common.reactivestreams.Preconditions.*;

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case
import static io.vavr.Patterns.*;                         //switch - case - success/failure
import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

/**
 * <pre>
 * auxiliary static functions with arguments - several Mono
 *
 * - functional for-comprehension pattern for reactive flows
 * - required to convert a null value returned by a function to an empty flow event
 * - at the first null value returned, the chain of nested calls stops
 *
 * - how to use for-comprehension: suppose that you need cycle with inner cycle, like,
 * 1) imperative variant:
 *
 *      for i
 *              for j
 *                      someFunc(i,j)
 *
 * 2) pure reactive variant:
 *
 *      Flux1.flatMap(i-&gt;Flux2.map(j-&gt;someFunc(i,j)))
 *
 * 3) for-comprehension variant:
 *
 *      For(flux1,flux2).yield(someFunc(i,j))
 *
 *
 *
 * </pre>
 */
@UtilityClass
@Slf4j

public final class Patterns
	{
	
	//<editor-fold desc="gently try to flux">
	
	/**
	 * Mono from try. Try with null inside transforms to empty event
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param tryObject
	 * 	the try object
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> monoFromNullableTry( Try<T> tryObject )
		{
		if( tryObject == null )
			{
			return error( new NullPointerException( "Input argument Try-object is null" ) );
			}
		
		return tryObject.map( Mono::justOrEmpty )
		                .getOrElseGet( Mono::error );
		}
	
	/**
	 * Mono from try. Try with null inside transforms to error event
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param tryObject
	 * 	the try object
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> monoFromTry( Try<T> tryObject )
		{
		if( tryObject == null )
			{
			return error( new NullPointerException( "Input argument Try-object is null" ) );
			}
		
		return tryObject.map( Mono::just )
		                .getOrElseGet( Mono::error );
		}
	
	/**
	 * Flux from  try. Try with null inside transforms to empty event
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param tryObject
	 * 	the try object
	 *
	 * @return the flux
	 */
	public static <T> Flux<T> fluxFromNullableTry( Try<T> tryObject )
		{
		if( tryObject == null )
			{
			return Flux.error( new NullPointerException( "Input argument Try-object is null" ) );
			}
		
		return tryObject.map( Mono::justOrEmpty )
		                .map( Mono::flux )
		                .getOrElseGet( Flux::error );
		}
	
	/**
	 * Flux from try.  Try with null inside transforms to error event
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param tryObject
	 * 	the try object
	 *
	 * @return the flux
	 */
	public static <T> Flux<T> fluxFromTry( Try<T> tryObject )
		{
		if( tryObject == null )
			{
			return Flux.error( new NullPointerException( "Input argument Try-object is null" ) );
			}
		
		return tryObject.map( Flux::just )
		                .getOrElseGet( Flux::error );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="reactor parallel flows">
	
	/**
	 * Alias for {@link CheckedConsumer#unchecked}
	 *
	 * @param <T>
	 * 	return type
	 * @param <R>
	 * 	the type parameter
	 * @param function
	 * 	the function
	 * @param scheduler
	 * 	the scheduler
	 *
	 * @return An unchecked wrapper of supplied  uncheckedFunction
	 */
	public static <T, R> Function<T,Mono<R>> functionToMonoParallel( CheckedFunction1<T,R> function ,
	                                                                 Scheduler scheduler )
		{
		return t -> Mono.fromSupplier( () -> uncheckedFunction( function ).apply( t ) )
		                .subscribeOn( scheduler );
		}
	
	/**
	 * Function to mono parallel log error function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param function
	 * 	the function
	 * @param scheduler
	 * 	the scheduler
	 * @param message
	 * 	the message
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,Mono<R>> functionToMonoParallelLogError( CheckedFunction1<T,R> function ,
	                                                                         Scheduler scheduler ,
	                                                                         String message )
		{
		return t -> Mono.fromSupplier( () -> uncheckedFunction( function ).apply( t ) )
		                .subscribeOn( scheduler )
		                .onErrorResume( logAtErrorFunction( message , never() ) ); //logAtErrorFunction
		}
	
	//</editor-fold>
	
	//<editor-fold desc="simple for iterators">
	
	/**
	 * Range of Integers from start to end, even in reverse order.
	 * Borders inclusive.
	 *
	 * @param start
	 * 	the start
	 * @param end
	 * 	the end
	 *
	 * @return the flux
	 */
	public static Flux<Integer> range( final int start ,
	                                   final int end )
		{
		boolean reverse = start > end;
		
		return Flux.generate( () -> start , ( i , f ) ->
		{
		f.next( i );
		if( i == end )
			f.complete();
		
		return reverse ? i - 1 : i + 1;
		} );
		}
	
	/**
	 * Range of Longs from start to end, even in reverse order.
	 * Borders inclusive.
	 *
	 * @param start
	 * 	the start
	 * @param end
	 * 	the end
	 *
	 * @return the flux
	 */
	public static Flux<Long> longRange( final long start ,
	                                    final long end )
		{
		boolean reverse = start > end;
		
		return Flux.generate( () -> start , ( i , f ) ->
		{
		f.next( i );
		if( i == end )
			f.complete();
		
		return reverse ? i - 1 : i + 1;
		} );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="wrap with index collection ">
	
	/**
	 * Indexed (with Integer) elements of Flux.
	 * Wrap element with index into Tuple2
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param flux
	 * 	the flux
	 *
	 * @return the flux
	 */
	public static <E> Flux<Tuple2<Integer,E>> indexed( Flux<E> flux )
		{
		if( flux == null )
			return illegalArgumentError( "Input argument flux must not be null." );
		else
			return flux.zipWith( Flux.range( 0 , Integer.MAX_VALUE ) , ( e , i ) -> Tuple.of( i , e ) );
		}
	
	/**
	 * Indexed (with Long) elements of Flux.
	 * Wrap element with index into Tuple2
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param flux
	 * 	the flux
	 *
	 * @return the flux
	 */
	public static <E> Flux<Tuple2<Long,E>> longIndexed( Flux<E> flux )
		{
		if( flux == null )
			return illegalArgumentError( "Input argument flux must not be null." );
		else
			return flux.zipWith( longRange( 0 , Long.MAX_VALUE ) , ( e , i ) -> Tuple.of( i , e ) );
		}
	
	//</editor-fold>
	
	}


 