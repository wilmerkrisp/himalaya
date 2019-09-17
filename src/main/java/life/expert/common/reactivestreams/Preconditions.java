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
import life.expert.common.function.TupleUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkNotNull;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.*;                            //producer supplier

import static reactor.core.publisher.Mono.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R
import static life.expert.common.function.TupleUtils.*; //vavr's tuple->R INTO func->R

import life.expert.value.string.*;
import life.expert.value.numeric.*;

import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.NullableUtils.*;//.map(nullableFunction)
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
import static life.expert.common.reactivestreams.Preconditions.*; //reactive check
import static life.expert.common.reactivestreams.Patterns.*;    //reactive helper functions
import static life.expert.common.base.Objects.*;          //deepCopyOfObject
import static life.expert.common.reactivestreams.ForComprehension.*; //reactive for-comprehension

import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;

//import static io.vavr.API.*;                           //conflicts with my reactive For-comprehension

import static io.vavr.API.$;                            // pattern matching
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.*;                         //switch - case - success/failure
import static io.vavr.Predicates.*;                       //switch - case
//import static java.util.function.Predicate.*;           //isEqual streamAPI

import static io.vavr.API.CheckedFunction;//checked functions
import static io.vavr.API.unchecked;    //checked->unchecked
import static io.vavr.API.Function;     //lambda->Function3
import static io.vavr.API.Tuple;

import static io.vavr.API.Try;          //Try

import io.vavr.control.Try;                               //try
import reactor.function.*;
import reactor.function.Function3;
import reactor.function.Function4;
import reactor.function.Function5;
import reactor.function.Function6;
import reactor.function.Function7;
import reactor.function.Function8;
import reactor.util.function.Tuples;

import static io.vavr.API.Failure;
import static io.vavr.API.Success;
import static io.vavr.API.Left;         //Either
import static io.vavr.API.Right;
import static life.expert.common.reactivestreams.ForComprehension.*;

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

/**
 * service (static class)
 *
 * <pre>{@code
 *               Preconditions.compute();
 *               var s=Preconditions.MY_CONSTANT;
 * }*****</pre>
 */
@UtilityClass
@Slf4j
public final class Preconditions
	{
	
	//<editor-fold desc="check functions">
	
	/**
	 * Checking  argument with predicate  (usual test for empty elements)
	 * If argument is null then empty event
	 * Wrap error as event
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono with input argument
	 */
	public static <T> Mono<T> checkArgument( T argument ,
	                                         Predicate<T> predicate ,
	                                         String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( argument );
		}
	
	/**
	 * Check argument and invoke some function.
	 * Helper function when public method is wrapper for private
	 * Result must not be null
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono with transformed argument
	 */
	public static <T, R> Mono<R> checkArgumentAndMap( T argument ,
	                                                  Predicate<T> predicate ,
	                                                  String message ,
	                                                  Function<T,R> function )
		{
		return checkArgument( argument , predicate , message ).map( function )
		                                                      .single();
		}
	
	/**
	 * Checking 2  arguments with predicate  (usual test for empty elements)
	 * If argument is null then  event with Tuple(null..
	 * Wrap error as event
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2> Mono<Tuple2<T1,T2>> checkArgument( T1 argument1 ,
	                                                          T2 argument2 ,
	                                                          BiPredicate<T1,T2> predicate ,
	                                                          String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument1 , argument2 ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( Tuple.of( argument1 , argument2 ) );
		}
	
	/**
	 * Check 2 arguments and invoke some function.
	 * Helper function when public method is wrapper for private.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono
	 */
	public static <T1, T2, R> Mono<R> checkArgumentAndMap( T1 argument1 ,
	                                                       T2 argument2 ,
	                                                       BiPredicate<T1,T2> predicate ,
	                                                       String message ,
	                                                       BiFunction<T1,T2,R> function )
		{
		return checkArgument( argument1 , argument2 , predicate , message ).map( TupleUtils.function( function ) )
		                                                                   .single();
		}
	
	/**
	 * Check argument mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param tuple
	 * 	the tuple
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2> Mono<Tuple2<T1,T2>> checkArgument( Tuple2<T1,T2> tuple ,
	                                                          BiPredicate<T1,T2> predicate ,
	                                                          String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( TupleUtils.predicate( predicate )
		                      .test( tuple ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( tuple );
		}
	
	/**
	 * Checking 3 arguments with predicate  (usual test for empty elements)
	 * * If argument is null then  event with Tuple(null..
	 * Wrap error as event
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3> Mono<Tuple3<T1,T2,T3>> checkArgument( T1 argument1 ,
	                                                                 T2 argument2 ,
	                                                                 T3 argument3 ,
	                                                                 Predicate3<T1,T2,T3> predicate ,
	                                                                 String message )
		{
		
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument1 , argument2 , argument3 ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( Tuple.of( argument1 , argument2 , argument3 ) );
		}
	
	/**
	 * Check 3 arguments and invoke some function.
	 * Helper function when public method is wrapper for private.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, R> Mono<R> checkArgumentAndMap( T1 argument1 ,
	                                                           T2 argument2 ,
	                                                           T3 argument3 ,
	                                                           Predicate3<T1,T2,T3> predicate ,
	                                                           String message ,
	                                                           Function3<T1,T2,T3,R> function )
		{
		return checkArgument( argument1 , argument2 , argument3 , predicate , message ).map( TupleUtils.function( function ) )
		                                                                               .single();
		}
	
	/**
	 * Check argument mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param tuple
	 * 	the tuple
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3> Mono<Tuple3<T1,T2,T3>> checkArgument( Tuple3<T1,T2,T3> tuple ,
	                                                                 Predicate3<T1,T2,T3> predicate ,
	                                                                 String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( TupleUtils.predicate( predicate )
		                      .test( tuple ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( tuple );
		}
	
	/**
	 * Checking 4 arguments with predicate  (usual test for empty elements)
	 * * If argument is null then  event with Tuple(null..
	 * Wrap error as event
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4> Mono<Tuple4<T1,T2,T3,T4>> checkArgument( T1 argument1 ,
	                                                                        T2 argument2 ,
	                                                                        T3 argument3 ,
	                                                                        T4 argument4 ,
	                                                                        Predicate4<T1,T2,T3,T4> predicate ,
	                                                                        String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument1 , argument2 , argument3 , argument4 ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( Tuple.of( argument1 , argument2 , argument3 , argument4 ) );
			
		}
	
	/**
	 * Check 4 arguments and invoke some function.
	 * Helper function when public method is wrapper for private.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, R> Mono<R> checkArgumentAndMap( T1 argument1 ,
	                                                               T2 argument2 ,
	                                                               T3 argument3 ,
	                                                               T4 argument4 ,
	                                                               Predicate4<T1,T2,T3,T4> predicate ,
	                                                               String message ,
	                                                               Function4<T1,T2,T3,T4,R> function )
		{
		return checkArgument( argument1 , argument2 , argument3 , argument4 , predicate , message ).map( TupleUtils.function( function ) )
		                                                                                           .single();
		}
	
	/**
	 * Check argument mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param tuple
	 * 	the tuple
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4> Mono<Tuple4<T1,T2,T3,T4>> checkArgument( Tuple4<T1,T2,T3,T4> tuple ,
	                                                                        Predicate4<T1,T2,T3,T4> predicate ,
	                                                                        String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( TupleUtils.predicate( predicate )
		                      .test( tuple ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( tuple );
		}
	
	/**
	 * Checking 5 arguments with predicate  (usual test for empty elements)
	 * * If argument is null then  event with Tuple(null..
	 * Wrap error as event
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5> Mono<Tuple5<T1,T2,T3,T4,T5>> checkArgument( T1 argument1 ,
	                                                                               T2 argument2 ,
	                                                                               T3 argument3 ,
	                                                                               T4 argument4 ,
	                                                                               T5 argument5 ,
	                                                                               Predicate5<T1,T2,T3,T4,T5> predicate ,
	                                                                               String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument1 , argument2 , argument3 , argument4 , argument5 ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( Tuple.of( argument1 , argument2 , argument3 , argument4 , argument5 ) );
		}
	
	/**
	 * Check 5 arguments and invoke some function.
	 * Helper function when public method is wrapper for private.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, R> Mono<R> checkArgumentAndMap( T1 argument1 ,
	                                                                   T2 argument2 ,
	                                                                   T3 argument3 ,
	                                                                   T4 argument4 ,
	                                                                   T5 argument5 ,
	                                                                   Predicate5<T1,T2,T3,T4,T5> predicate ,
	                                                                   String message ,
	                                                                   Function5<T1,T2,T3,T4,T5,R> function )
		{
		return checkArgument( argument1 , argument2 , argument3 , argument4 , argument5 , predicate , message ).map( TupleUtils.function( function ) )
		                                                                                                       .single();
		}
	
	/**
	 * Check argument mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param tuple
	 * 	the tuple
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5> Mono<Tuple5<T1,T2,T3,T4,T5>> checkArgument( Tuple5<T1,T2,T3,T4,T5> tuple ,
	                                                                               Predicate5<T1,T2,T3,T4,T5> predicate ,
	                                                                               String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( TupleUtils.predicate( predicate )
		                      .test( tuple ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( tuple );
		}
	
	/**
	 * Checking 6 arguments with predicate  (usual test for empty elements)
	 * * If argument is null then  event with Tuple(null..
	 * Wrap error as event
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param argument6
	 * 	the argument 6
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6> Mono<Tuple6<T1,T2,T3,T4,T5,T6>> checkArgument( T1 argument1 ,
	                                                                                      T2 argument2 ,
	                                                                                      T3 argument3 ,
	                                                                                      T4 argument4 ,
	                                                                                      T5 argument5 ,
	                                                                                      T6 argument6 ,
	                                                                                      Predicate6<T1,T2,T3,T4,T5,T6> predicate ,
	                                                                                      String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( Tuple.of( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 ) );
		}
	
	/**
	 * Check 6 arguments and invoke some function.
	 * Helper function when public method is wrapper for private.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param argument6
	 * 	the argument 6
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, R> Mono<R> checkArgumentAndMap( T1 argument1 ,
	                                                                       T2 argument2 ,
	                                                                       T3 argument3 ,
	                                                                       T4 argument4 ,
	                                                                       T5 argument5 ,
	                                                                       T6 argument6 ,
	                                                                       Predicate6<T1,T2,T3,T4,T5,T6> predicate ,
	                                                                       String message ,
	                                                                       Function6<T1,T2,T3,T4,T5,T6,R> function )
		{
		return checkArgument( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , predicate , message ).map( TupleUtils.function( function ) )
		                                                                                                                   .single();
		}
	
	/**
	 * Check argument mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param tuple
	 * 	the tuple
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6> Mono<Tuple6<T1,T2,T3,T4,T5,T6>> checkArgument( Tuple6<T1,T2,T3,T4,T5,T6> tuple ,
	                                                                                      Predicate6<T1,T2,T3,T4,T5,T6> predicate ,
	                                                                                      String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( TupleUtils.predicate( predicate )
		                      .test( tuple ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( tuple );
		}
	
	/**
	 * Checking 7 arguments with predicate  (usual test for empty elements)
	 * * If argument is null then  event with Tuple(null..
	 * Wrap error as event
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param <T7>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param argument6
	 * 	the argument 6
	 * @param argument7
	 * 	the argument 7
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Mono<Tuple7<T1,T2,T3,T4,T5,T6,T7>> checkArgument( T1 argument1 ,
	                                                                                             T2 argument2 ,
	                                                                                             T3 argument3 ,
	                                                                                             T4 argument4 ,
	                                                                                             T5 argument5 ,
	                                                                                             T6 argument6 ,
	                                                                                             T7 argument7 ,
	                                                                                             Predicate7<T1,T2,T3,T4,T5,T6,T7> predicate ,
	                                                                                             String message )
		{
		
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( Tuple.of( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 ) );
		}
	
	/**
	 * Check 7 arguments and invoke some function.
	 * Helper function when public method is wrapper for private.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param <T7>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param argument6
	 * 	the argument 6
	 * @param argument7
	 * 	the argument 7
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, R> Mono<R> checkArgumentAndMap( T1 argument1 ,
	                                                                           T2 argument2 ,
	                                                                           T3 argument3 ,
	                                                                           T4 argument4 ,
	                                                                           T5 argument5 ,
	                                                                           T6 argument6 ,
	                                                                           T7 argument7 ,
	                                                                           Predicate7<T1,T2,T3,T4,T5,T6,T7> predicate ,
	                                                                           String message ,
	                                                                           Function7<T1,T2,T3,T4,T5,T6,T7,R> function )
		{
		return checkArgument( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 , predicate , message ).map( TupleUtils.function( function ) )
		                                                                                                                               .single();
		}
	
	/**
	 * Check argument mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param <T7>
	 * 	the type parameter
	 * @param tuple
	 * 	the tuple
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Mono<Tuple7<T1,T2,T3,T4,T5,T6,T7>> checkArgument( Tuple7<T1,T2,T3,T4,T5,T6,T7> tuple ,
	                                                                                             Predicate7<T1,T2,T3,T4,T5,T6,T7> predicate ,
	                                                                                             String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( TupleUtils.predicate( predicate )
		                      .test( tuple ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( tuple );
		}
	
	/**
	 * Checking 8 arguments with predicate  (usual test for empty elements)
	 * * If argument is null then  event with Tuple(null..
	 * Wrap error as event
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param <T7>
	 * 	the type parameter
	 * @param <T8>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param argument6
	 * 	the argument 6
	 * @param argument7
	 * 	the argument 7
	 * @param argument8
	 * 	the argument 8
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Mono<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>> checkArgument( T1 argument1 ,
	                                                                                                    T2 argument2 ,
	                                                                                                    T3 argument3 ,
	                                                                                                    T4 argument4 ,
	                                                                                                    T5 argument5 ,
	                                                                                                    T6 argument6 ,
	                                                                                                    T7 argument7 ,
	                                                                                                    T8 argument8 ,
	                                                                                                    Predicate8<T1,T2,T3,T4,T5,T6,T7,T8> predicate ,
	                                                                                                    String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( predicate.test( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 , argument8 ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( Tuple.of( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 , argument8 ) );
		}
	
	/**
	 * Check argument and map mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param <T7>
	 * 	the type parameter
	 * @param <T8>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param argument1
	 * 	the argument 1
	 * @param argument2
	 * 	the argument 2
	 * @param argument3
	 * 	the argument 3
	 * @param argument4
	 * 	the argument 4
	 * @param argument5
	 * 	the argument 5
	 * @param argument6
	 * 	the argument 6
	 * @param argument7
	 * 	the argument 7
	 * @param argument8
	 * 	the argument 8
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 * @param function
	 * 	the function
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Mono<R> checkArgumentAndMap( T1 argument1 ,
	                                                                               T2 argument2 ,
	                                                                               T3 argument3 ,
	                                                                               T4 argument4 ,
	                                                                               T5 argument5 ,
	                                                                               T6 argument6 ,
	                                                                               T7 argument7 ,
	                                                                               T8 argument8 ,
	                                                                               Predicate8<T1,T2,T3,T4,T5,T6,T7,T8> predicate ,
	                                                                               String message ,
	                                                                               Function8<T1,T2,T3,T4,T5,T6,T7,T8,R> function )
		{
		return checkArgument( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 , argument8 , predicate , message ).map( TupleUtils.function( function ) )
		                                                                                                                                           .single();
		}
	
	/**
	 * Check 8 arguments and invoke some function.
	 * Helper function when public method is wrapper for private.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param <T5>
	 * 	the type parameter
	 * @param <T6>
	 * 	the type parameter
	 * @param <T7>
	 * 	the type parameter
	 * @param <T8>
	 * 	the type parameter
	 * @param tuple
	 * 	the tuple
	 * @param predicate
	 * 	the predicate
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Mono<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>> checkArgument( Tuple8<T1,T2,T3,T4,T5,T6,T7,T8> tuple ,
	                                                                                                    Predicate8<T1,T2,T3,T4,T5,T6,T7,T8> predicate ,
	                                                                                                    String message )
		{
		if( predicate == null )
			return nullPointerMonoError( "Predicate must not be null" );
		else if( !( TupleUtils.predicate( predicate )
		                      .test( tuple ) ) )
			return illegalArgumentMonoError( message );
		else
			return justOrEmpty( tuple );
		}
	
	/**
	 * Check argument mono.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param predicate
	 * 	the predicate
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> checkArgument( T argument ,
	                                         Predicate<T> predicate )
		{
		return checkArgument( argument , predicate , "Condition should evaluate to true" );
		}
	
	/**
	 * Check true mono.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param condition
	 * 	the condition
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> checkTrue( boolean condition )
		{
		return checkTrue( condition , "Condition should be true" );
		}
	
	/**
	 * Check true mono.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param condition
	 * 	the condition
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> checkTrue( boolean condition ,
	                                     String message )
		{
		return condition ? empty() : error( new IllegalArgumentException( message == null ? "Condition should be true" : message ) );
		}
	
	/**
	 * Check false mono.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param condition
	 * 	the condition
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> checkFalse( boolean condition )
		{
		return checkFalse( !condition , "Condition should be false" );
		}
	
	/**
	 * Chec false mono.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param condition
	 * 	the condition
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> checkFalse( boolean condition ,
	                                      String message )
		{
		return !condition ? empty() : error( new IllegalArgumentException( message == null ? "Condition should be false" : message ) );
		}
	
	/**
	 * Check not null mono.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param message
	 * 	the message
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> checkNotNull( T argument ,
	                                        String message )
		{
		return justOrEmpty( argument ).single()
		                              .onErrorMap( nullPointerException( message == null ? "Argument should not be null" : message ) );
		}
	
	/**
	 * Check not null mono.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 *
	 * @return the mono
	 */
	public static <T> Mono<T> checkNotNull( T argument )
		{
		return checkNotNull( argument , "Argument should not be null" );
		}
	//</editor-fold>
	
	//<editor-fold desc="error utils Flux">
	
	/**
	 * Only when subscription, lazily produce Flux.error with NullPointerException
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 *
	 * @return Flux with error event.
	 */
	public static <T> Flux<T> nullPointerError()
		{
		return Flux.defer( () -> Flux.error( new NullPointerException() ) );
		}
	
	/**
	 * Only when subscription, lazily produce Flux.error with  NullPointerException(description))
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 * @param description
	 * 	the description
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> nullPointerError( String description )
		{
		return Flux.defer( () -> Flux.error( new NullPointerException( description == null ? "" : description ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Flux.error with  IllegalArgumentException()
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalArgumentError()
		{
		return Flux.defer( () -> Flux.error( new IllegalArgumentException() ) );
		}
	
	/**
	 * Only when subscription, lazily produce Flux.error with  IllegalArgumentException(description)
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 * @param description
	 * 	the description
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalArgumentError( String description )
		{
		return Flux.defer( () -> Flux.error( new IllegalArgumentException( description == null ? "" : description ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Flux.error with IllegalArgumentException(description,cause)
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 * @param description
	 * 	the description
	 * @param cause
	 * 	the cause
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalArgumentError( String description ,
	                                                Throwable cause )
		{
		return Flux.defer( () -> Flux.error( new IllegalArgumentException( description == null ? "" : description , cause ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Flux.error with IllegalStateException()
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalStateError()
		{
		return Flux.defer( () -> Flux.error( new IllegalStateException() ) );
		}
	
	/**
	 * Only when subscription, lazily produce Flux.error with IllegalStateException(description)
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 * @param description
	 * 	the description
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalStateError( String description )
		{
		return Flux.defer( () -> Flux.error( new IllegalStateException( description == null ? "" : description ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Flux.error with IllegalStateException(description,cause)
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 * @param description
	 * 	the description
	 * @param cause
	 * 	the cause
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalStateError( String description ,
	                                             Throwable cause )
		{
		return Flux.defer( () -> Flux.error( new IllegalStateException( description == null ? "" : description , cause ) ) );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="error utils Mono">
	
	/**
	 * Only when subscription, lazily produce Mono.error with NullPointerException()
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> nullPointerMonoError()
		{
		return defer( () -> error( new NullPointerException() ) );
		}
	
	/**
	 * Only when subscription, lazily produce Mono.error with NullPointerException(description)
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 * @param description
	 * 	the description
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> nullPointerMonoError( String description )
		{
		return defer( () -> Mono.error( new NullPointerException( description == null ? "" : description ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Mono.error with IllegalArgumentException()
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalArgumentMonoError()
		{
		return defer( () -> error( new IllegalArgumentException() ) );
		}
	
	/**
	 * Only when subscription, lazily produce Mono.error with IllegalArgumentException(description)
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 * @param description
	 * 	the description
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalArgumentMonoError( String description )
		{
		return defer( () -> error( new IllegalArgumentException( description == null ? "" : description ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Mono.error with IllegalArgumentException(description,cause)
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 * @param description
	 * 	the description
	 * @param cause
	 * 	the cause
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalArgumentMonoError( String description ,
	                                                    Throwable cause )
		{
		return defer( () -> error( new IllegalArgumentException( description == null ? "" : description , cause ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Mono.error with IllegalStateException()
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalStateMonoError()
		{
		return defer( () -> error( new IllegalStateException() ) );
		}
	
	/**
	 * Only when subscription, lazily produce Mono.error with  IllegalStateException(description)
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 * @param description
	 * 	the description
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalStateMonoError( String description )
		{
		return defer( () -> error( new IllegalStateException( description == null ? "" : description ) ) );
		}
	
	/**
	 * Only when subscription, lazily produce Mono.error with IllegalStateException(description,cause)
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 * @param description
	 * 	the description
	 * @param cause
	 * 	the cause
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalStateMonoError( String description ,
	                                                 Throwable cause )
		{
		return defer( () -> error( new IllegalStateException( description == null ? "" : description , cause ) ) );
		}
	
	//</editor-fold>
	}
