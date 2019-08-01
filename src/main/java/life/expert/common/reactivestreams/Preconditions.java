package life.expert.common.reactivestreams;



import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkNotNull;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.function.*;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple5;
import reactor.util.function.Tuple6;
import reactor.util.function.Tuple7;
import reactor.util.function.Tuple8;
import reactor.util.function.Tuples;

import java.util.function.*;                            //producer supplier

import static reactor.core.publisher.Mono.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)

import static life.expert.common.reactivestreams.ForComprehension.*;

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.reactivestreams
//                           wilmer 2019/05/22
//
//--------------------------------------------------------------------------------

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
	 * @return the mono
	 */
	public static <T> Mono<T> checkArgument( T argument ,
	                                         Predicate<T> predicate ,
	                                         String message )
		{
		//		Function<T,Mono<T>> check_argument = ( s ) -> just( s ).filter( predicate == null ? x -> false : predicate )
		//		                                                       .single()
		//		                                                       .onErrorMap( illegalArgumentException( message == null ? "Condition should evaluate to true" : message ) );
		
		//return checkNotNull( argument ).flatMap( check_argument );
		
		/*
		Function<Mono<T>,Mono<T>> check_argument = s -> s.filter( predicate.or( x -> false ) )
		                                                 .single()
		                                                 .onErrorMap( illegalArgumentException( message.orElse( "Condition should evaluate to true")));
		
		 
		
	 
		return checkNotNull( predicate , "Predicate should not be null" ).then( checkNotNull( argument ) ).For( check_argument );
		
		 */
		/*      - filter event
			- aproving that exactly one value (if void filtered -> generate error)
		 *
		 *
		 * -  ошибка в методе2 не обрабатывается и выходит наверх, null значение не допускается
		* */
		var check_argument = justOrEmpty( argument ).filter( predicate == null ? x -> false : predicate )
		                                            .single()
		                                            .onErrorMap( illegalArgumentException( message ) );
		
		return checkNotNull( predicate , "Predicate should not be null" ).then( checkNotNull( argument ) )
		                                                                 .then( check_argument );
		}
	
	/**
	 * Check argument and invoke some function.
	 * Helper function when public method is wrapper for private.
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
	 * @return the mono
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
	 * Checking 2 arguments with predicate  (usual test for empty elements)
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness1 = checkNotNull( argument1 );
		var check_nullness2 = checkNotNull( argument2 );
		var check_argument = fromSupplier( () -> Tuples.of( argument1 , argument2 ) ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 ) -> false : predicate ) )
		                                                                             .single()
		                                                                             .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness1 )
		                      .flatMap( x -> check_nullness2 )
		                      .flatMap( x -> check_argument );
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness  = checkNotNull( tuple );
		
		var check_argument = justOrEmpty( tuple ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 ) -> false : predicate ) )
		                                         .single()
		                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness )
		                      .flatMap( x -> check_argument );
		}
	
	/**
	 * Checking 3 arguments with predicate  (usual test for empty elements)
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness1 = checkNotNull( argument1 );
		var check_nullness2 = checkNotNull( argument2 );
		var check_nullness3 = checkNotNull( argument3 );
		var check_argument = fromSupplier( () -> Tuples.of( argument1 , argument2 , argument3 ) ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 ) -> false : predicate ) )
		                                                                                         .single()
		                                                                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness1 )
		                      .flatMap( x -> check_nullness2 )
		                      .flatMap( x -> check_nullness3 )
		                      .flatMap( x -> check_argument );
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness  = checkNotNull( tuple );
		
		var check_argument = justOrEmpty( tuple ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 ) -> false : predicate ) )
		                                         .single()
		                                         .onErrorMap( illegalArgumentException( message ) );
		
		return check_predicate.flatMap( x -> check_nullness )
		                      .flatMap( x -> check_argument );
		}
	
	/**
	 * Checking 4 arguments with predicate  (usual test for empty elements)
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness1 = checkNotNull( argument1 );
		var check_nullness2 = checkNotNull( argument2 );
		var check_nullness3 = checkNotNull( argument3 );
		var check_nullness4 = checkNotNull( argument4 );
		var check_argument = fromSupplier( () -> Tuples.of( argument1 , argument2 , argument3 , argument4 ) ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 ) -> false : predicate ) )
		                                                                                                     .single()
		                                                                                                     .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness1 )
		                      .flatMap( x -> check_nullness2 )
		                      .flatMap( x -> check_nullness3 )
		                      .flatMap( x -> check_nullness4 )
		                      .flatMap( x -> check_argument );
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness  = checkNotNull( tuple );
		
		var check_argument = justOrEmpty( tuple ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 ) -> false : predicate ) )
		                                         .single()
		                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness )
		                      .flatMap( x -> check_argument );
		}
	
	/**
	 * Checking 5 arguments with predicate  (usual test for empty elements)
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness1 = checkNotNull( argument1 );
		var check_nullness2 = checkNotNull( argument2 );
		var check_nullness3 = checkNotNull( argument3 );
		var check_nullness4 = checkNotNull( argument4 );
		var check_nullness5 = checkNotNull( argument5 );
		var check_argument = fromSupplier( () -> Tuples.of( argument1 , argument2 , argument3 , argument4 , argument5 ) ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 ) -> false : predicate ) )
		                                                                                                                 .single()
		                                                                                                                 .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness1 )
		                      .flatMap( x -> check_nullness2 )
		                      .flatMap( x -> check_nullness3 )
		                      .flatMap( x -> check_nullness4 )
		                      .flatMap( x -> check_nullness5 )
		                      .flatMap( x -> check_argument );
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness  = checkNotNull( tuple );
		
		var check_argument = justOrEmpty( tuple ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 ) -> false : predicate ) )
		                                         .single()
		                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness )
		
		                      .flatMap( x -> check_argument );
		}
	
	/**
	 * Checking 6 arguments with predicate  (usual test for empty elements)
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness1 = checkNotNull( argument1 );
		var check_nullness2 = checkNotNull( argument2 );
		var check_nullness3 = checkNotNull( argument3 );
		var check_nullness4 = checkNotNull( argument4 );
		var check_nullness5 = checkNotNull( argument5 );
		var check_nullness6 = checkNotNull( argument6 );
		var check_argument = fromSupplier( () -> Tuples.of( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 ) ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 , t6 ) -> false : predicate ) )
		                                                                                                                             .single()
		                                                                                                                             .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness1 )
		                      .flatMap( x -> check_nullness2 )
		                      .flatMap( x -> check_nullness3 )
		                      .flatMap( x -> check_nullness4 )
		                      .flatMap( x -> check_nullness5 )
		                      .flatMap( x -> check_nullness6 )
		                      .flatMap( x -> check_argument );
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness  = checkNotNull( tuple );
		
		var check_argument = justOrEmpty( tuple ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 , t6 ) -> false : predicate ) )
		                                         .single()
		                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness )
		                      .flatMap( x -> check_argument );
		}
	
	/**
	 * Checking 7 arguments with predicate  (usual test for empty elements)
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness1 = checkNotNull( argument1 );
		var check_nullness2 = checkNotNull( argument2 );
		var check_nullness3 = checkNotNull( argument3 );
		var check_nullness4 = checkNotNull( argument4 );
		var check_nullness5 = checkNotNull( argument5 );
		var check_nullness6 = checkNotNull( argument6 );
		var check_nullness7 = checkNotNull( argument7 );
		var check_argument = fromSupplier( () -> Tuples.of( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 ) ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) -> false : predicate ) )
		                                                                                                                                         .single()
		                                                                                                                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness1 )
		                      .flatMap( x -> check_nullness2 )
		                      .flatMap( x -> check_nullness3 )
		                      .flatMap( x -> check_nullness4 )
		                      .flatMap( x -> check_nullness5 )
		                      .flatMap( x -> check_nullness6 )
		                      .flatMap( x -> check_nullness7 )
		                      .flatMap( x -> check_argument );
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness  = checkNotNull( tuple );
		
		var check_argument = justOrEmpty( tuple ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) -> false : predicate ) )
		                                         .single()
		                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness )
		                      .flatMap( x -> check_argument );
		}
	
	/**
	 * Checking 8 arguments with predicate  (usual test for empty elements)
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness1 = checkNotNull( argument1 );
		var check_nullness2 = checkNotNull( argument2 );
		var check_nullness3 = checkNotNull( argument3 );
		var check_nullness4 = checkNotNull( argument4 );
		var check_nullness5 = checkNotNull( argument5 );
		var check_nullness6 = checkNotNull( argument6 );
		var check_nullness7 = checkNotNull( argument7 );
		var check_nullness8 = checkNotNull( argument8 );
		var check_argument = fromSupplier( () -> Tuples.of( argument1 , argument2 , argument3 , argument4 , argument5 , argument6 , argument7 , argument8 ) ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) -> false : predicate ) )
		                                                                                                                                                     .single()
		                                                                                                                                                     .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness1 )
		                      .flatMap( x -> check_nullness2 )
		                      .flatMap( x -> check_nullness3 )
		                      .flatMap( x -> check_nullness4 )
		                      .flatMap( x -> check_nullness5 )
		                      .flatMap( x -> check_nullness6 )
		                      .flatMap( x -> check_nullness7 )
		                      .flatMap( x -> check_nullness8 )
		                      .flatMap( x -> check_argument );
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
		
		var check_predicate = checkNotNull( predicate , "Predicate should not be null" );
		var check_nullness  = checkNotNull( tuple );
		
		var check_argument = justOrEmpty( tuple ).filter( TupleUtils.predicate( predicate == null ? ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) -> false : predicate ) )
		                                         .single()
		                                         .onErrorMap( illegalArgumentException( message ) );
		return check_predicate.flatMap( x -> check_nullness )
		                      .flatMap( x -> check_argument );
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
	 * Alias for  Flux.error(new NullPointerException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 *
	 * @return Flux with error event.
	 */
	public static <T> Flux<T> nullPointerError()
		{
		return Flux.error( new NullPointerException() );
		}
	
	 
	
	/**
	 * Alias for  Flux.error(new NullPointerException(description))
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
		return Flux.error( new NullPointerException( description ) );
		}
	
	/**
	 * Alias for  Flux.error(new IllegalArgumentException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalArgumentError()
		{
		return Flux.error( new IllegalArgumentException() );
		}
	
	/**
	 * Alias for  Flux.error(new IllegalArgumentException(description))
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
		return Flux.error( new IllegalArgumentException( description ) );
		}
	
	/**
	 * Alias for  Flux.error(new IllegalArgumentException(description,cause))
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
		return Flux.error( new IllegalArgumentException( description , cause ) );
		}
	
	/**
	 * Alias for  Flux.error(new IllegalStateException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Flux}.
	 *
	 * @return Flux with error event.
	 */
	
	public static <T> Flux<T> illegalStateError()
		{
		return Flux.error( new IllegalStateException() );
		}
	
	/**
	 * Alias for  Flux.error(new IllegalStateException(description))
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
		return Flux.error( new IllegalStateException( description ) );
		}
	
	/**
	 * Alias for  Flux.error(new IllegalStateException(description,cause))
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
		return Flux.error( new IllegalStateException( description , cause ) );
		}
	
	//</editor-fold>
	
	
	
	
	//<editor-fold desc="error utils Mono">
	
	
	
	/**
	 * Alias for Mono.error(new NullPointerException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> nullPointerMonoError()
		{
		return error( new NullPointerException() );
		}
	
	/**
	 * Alias for  Mono.error(new NullPointerException(description))
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
		return Mono.error( new NullPointerException( description ) );
		}
	
	/**
	 * Alias for  Mono.error(new IllegalArgumentException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalArgumentMonoError()
		{
		return Mono.error( new IllegalArgumentException() );
		}
	
	/**
	 * Alias for  Mono.error(new IllegalArgumentException(description))
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
		return Mono.error( new IllegalArgumentException( description ) );
		}
	
	/**
	 * Alias for  Mono.error(new IllegalArgumentException(description,cause))
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
		return Mono.error( new IllegalArgumentException( description , cause ) );
		}
	
	/**
	 * Alias for  Mono.error(new IllegalStateException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Mono}.
	 *
	 * @return Mono with error event.
	 */
	
	public static <T> Mono<T> illegalStateMonoError()
		{
		return Mono.error( new IllegalStateException() );
		}
	
	/**
	 * Alias for  Mono.error(new IllegalStateException(description))
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
		return Mono.error( new IllegalStateException( description ) );
		}
	
	/**
	 * Alias for  Mono.error(new IllegalStateException(description,cause))
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
		return Mono.error( new IllegalStateException( description , cause ) );
		}
	
	//</editor-fold>
	}
