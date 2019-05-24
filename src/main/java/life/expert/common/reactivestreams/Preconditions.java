package life.expert.common.reactivestreams;









import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;



import static com.google.common.base.Preconditions.checkNotNull;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


import java.util.function.*;                            //producer supplier



import static reactor.core.publisher.Mono.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)

import static  life.expert.common.reactivestreams.ForComprehension.*;

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
 * }</pre>
 */
@UtilityClass
@Slf4j
public final class Preconditions
	{
	
	//<editor-fold desc="check functions">
	
	
	
	
	
	/**
	 * Check argument mono.
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
		 
		
		Function<Mono<T>,Mono<T>> check_argument = s -> s.filter( predicate.or( x -> false ) )
		                                                 .single()
		                                                 .onErrorMap( illegalArgumentException( "f"));
		
		
		
		
		return For(checkNotNull( predicate , "Predicate should not be null" ).then( checkNotNull( argument ) ), check_argument );
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
	
	
	
	
	
	
	}
