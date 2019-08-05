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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.reactivestreams.Publisher;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.*;                            //producer supplier

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Function6;
import io.vavr.Function7;
import io.vavr.Function8;

import io.vavr.control.Try;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static life.expert.common.reactivestreams.Preconditions.*;

import static reactor.core.publisher.Mono.*;

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

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList



/**
 * <pre>
 * auxiliary static functions with arguments - several Function
 *
 * - functional for-comprehension pattern for reactive flows
 * - required to convert a null value returned by a function to an empty flow event
 * - at the first null value returned, the chain of nested calls stops
 *
 * 1) first group of functions,like For(flux,func1,func) take result of func1 and put in on the input fo func2
 *
 *      flux=&gt;result1
 *      func1(result1)=&gt;result2
 *      func2(result2)=&gt;output result (as Publisher)
 *
 *      A shortcut for {@code  ts.flatMap(f)} which allows us to write real for-comprehensions using  {@code For(...).yield(...)}.
 * 	 For(getPersons(), person -&gt;
 * 	        For(person.getTweets(), tweet -&gt;
 * 	                For(tweet.getReplies())
 * 	                        .yield(reply -&gt; person + ", " + tweet + ", " + reply)));
 *
 *      or For(getPersons(),getTweets(),getReplies())
 *
 *
 * 2) second group of functions, like For(flux1,flux2).yield(func)
 *    A shortcut for  {@code flux1.flatMap( i->flux2.map(j->func(i,j)) ) }  what means
 *    {@code    for i
 *              for j
 *                   func(i,j)}
 *
 *
 * </pre>
 */
@UtilityClass
@Slf4j
public final class ForComprehension
	{
	
	//<editor-fold desc="chain operators on the same value">
	
	/**
	 * For publisher.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 *
	 * @return the publisher
	 */
	public static <T1, T2> Publisher<T2> For( Flux<T1> ts1 ,
	                                          Function<? super Flux<T1>,? extends Publisher<T2>> ts2 )
		{
		
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).flux()
		                                           .transform( ts2 ) );
		}
	
	/**
	 * For mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 *
	 * @return the mono
	 */
	public static <T1, T2> Mono<T2> For( Mono<T1> ts1 ,
	                                     Function<? super Mono<T1>,? extends Mono<T2>> ts2 )
		{
		
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).transform( ts2 ) );
		}
	
	/**
	 * For publisher.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 *
	 * @return the publisher
	 */
	public static <T1, T2, T3> Publisher<T3> For( Flux<T1> ts1 ,
	                                              Function<? super Flux<T1>,? extends Publisher<T2>> ts2 ,
	                                              Function<? super Flux<T2>,? extends Publisher<T3>> ts3 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).flux()
		                                           .transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).flux()
		                                                                            .transform( ts3 ) ) );
		}
	
	/**
	 * For mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3> Mono<T3> For( Mono<T1> ts1 ,
	                                         Function<? super Mono<T1>,? extends Mono<T2>> ts2 ,
	                                         Function<? super Mono<T2>,? extends Mono<T3>> ts3 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).transform( ts3 ) ) );
		}
	
	/**
	 * For publisher.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 *
	 * @return the publisher
	 */
	public static <T1, T2, T3, T4> Publisher<T4> For( Flux<T1> ts1 ,
	                                                  Function<? super Flux<T1>,? extends Publisher<T2>> ts2 ,
	                                                  Function<? super Flux<T2>,? extends Publisher<T3>> ts3 ,
	                                                  Function<? super Flux<T3>,? extends Publisher<T4>> ts4 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).flux()
		                                           .transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).flux()
		                                                                            .transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).flux()
		                                                                                                             .transform( ts4 ) ) ) );
		}
	
	/**
	 * For mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4> Mono<T4> For( Mono<T1> ts1 ,
	                                             Function<? super Mono<T1>,? extends Mono<T2>> ts2 ,
	                                             Function<? super Mono<T2>,? extends Mono<T3>> ts3 ,
	                                             Function<? super Mono<T3>,? extends Mono<T4>> ts4 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).transform( ts4 ) ) ) );
		}
	
	/**
	 * For publisher.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 *
	 * @return the publisher
	 */
	public static <T1, T2, T3, T4, T5> Publisher<T5> For( Flux<T1> ts1 ,
	                                                      Function<? super Flux<T1>,? extends Publisher<T2>> ts2 ,
	                                                      Function<? super Flux<T2>,? extends Publisher<T3>> ts3 ,
	                                                      Function<? super Flux<T3>,? extends Publisher<T4>> ts4 ,
	                                                      Function<? super Flux<T4>,? extends Publisher<T5>> ts5 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).flux()
		                                           .transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).flux()
		                                                                            .transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).flux()
		                                                                                                             .transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).flux()
		                                                                                                                                              .transform( ts5 ) ) ) ) );
		}
	
	/**
	 * For mono.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5> Mono<T5> For( Mono<T1> ts1 ,
	                                                 Function<? super Mono<T1>,? extends Mono<T2>> ts2 ,
	                                                 Function<? super Mono<T2>,? extends Mono<T3>> ts3 ,
	                                                 Function<? super Mono<T3>,? extends Mono<T4>> ts4 ,
	                                                 Function<? super Mono<T4>,? extends Mono<T5>> ts5 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).transform( ts5 ) ) ) ) );
		}
	
	/**
	 * For publisher.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 * @param ts6
	 * 	the ts 6
	 *
	 * @return the publisher
	 */
	public static <T1, T2, T3, T4, T5, T6> Publisher<T6> For( Flux<T1> ts1 ,
	                                                          Function<? super Flux<T1>,? extends Publisher<T2>> ts2 ,
	                                                          Function<? super Flux<T2>,? extends Publisher<T3>> ts3 ,
	                                                          Function<? super Flux<T3>,? extends Publisher<T4>> ts4 ,
	                                                          Function<? super Flux<T4>,? extends Publisher<T5>> ts5 ,
	                                                          Function<? super Flux<T5>,? extends Publisher<T6>> ts6 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).flux()
		                                           .transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).flux()
		                                                                            .transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).flux()
		                                                                                                             .transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).flux()
		                                                                                                                                              .transform( ts5 )
		                                                                                                                                              .flatMap( t5 -> justOrEmpty( t5 ).flux()
		                                                                                                                                                                               .transform( ts6 ) ) ) ) ) );
		}
	
	/**
	 * For mono.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 * @param ts6
	 * 	the ts 6
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6> Mono<T6> For( Mono<T1> ts1 ,
	                                                     Function<? super Mono<T1>,? extends Mono<T2>> ts2 ,
	                                                     Function<? super Mono<T2>,? extends Mono<T3>> ts3 ,
	                                                     Function<? super Mono<T3>,? extends Mono<T4>> ts4 ,
	                                                     Function<? super Mono<T4>,? extends Mono<T5>> ts5 ,
	                                                     Function<? super Mono<T5>,? extends Mono<T6>> ts6 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).transform( ts5 )
		                                                                                                                                              .flatMap( t5 -> justOrEmpty( t5 ).transform( ts6 ) ) ) ) ) );
		}
	
	/**
	 * For publisher.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 * @param ts6
	 * 	the ts 6
	 * @param ts7
	 * 	the ts 7
	 *
	 * @return the publisher
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Publisher<T7> For( Flux<T1> ts1 ,
	                                                              Function<? super Flux<T1>,? extends Publisher<T2>> ts2 ,
	                                                              Function<? super Flux<T2>,? extends Publisher<T3>> ts3 ,
	                                                              Function<? super Flux<T3>,? extends Publisher<T4>> ts4 ,
	                                                              Function<? super Flux<T4>,? extends Publisher<T5>> ts5 ,
	                                                              Function<? super Flux<T5>,? extends Publisher<T6>> ts6 ,
	                                                              Function<? super Flux<T6>,? extends Publisher<T7>> ts7 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).flux()
		                                           .transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).flux()
		                                                                            .transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).flux()
		                                                                                                             .transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).flux()
		                                                                                                                                              .transform( ts5 )
		                                                                                                                                              .flatMap( t5 -> justOrEmpty( t5 ).flux()
		                                                                                                                                                                               .transform( ts6 )
		                                                                                                                                                                               .flatMap( t6 -> justOrEmpty( t6 ).flux()
		                                                                                                                                                                                                                .transform( ts7 ) ) ) ) ) ) );
		}
	
	/**
	 * For mono.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 * @param ts6
	 * 	the ts 6
	 * @param ts7
	 * 	the ts 7
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Mono<T7> For( Mono<T1> ts1 ,
	                                                         Function<? super Mono<T1>,? extends Mono<T2>> ts2 ,
	                                                         Function<? super Mono<T2>,? extends Mono<T3>> ts3 ,
	                                                         Function<? super Mono<T3>,? extends Mono<T4>> ts4 ,
	                                                         Function<? super Mono<T4>,? extends Mono<T5>> ts5 ,
	                                                         Function<? super Mono<T5>,? extends Mono<T6>> ts6 ,
	                                                         Function<? super Mono<T6>,? extends Mono<T7>> ts7 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).transform( ts5 )
		                                                                                                                                              .flatMap( t5 -> justOrEmpty( t5 ).transform( ts6 )
		                                                                                                                                                                               .flatMap( t6 -> justOrEmpty( t6 ).transform( ts7 ) ) ) ) ) ) );
		}
	
	/**
	 * For publisher.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 * @param ts6
	 * 	the ts 6
	 * @param ts7
	 * 	the ts 7
	 * @param ts8
	 * 	the ts 8
	 *
	 * @return the publisher
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Publisher<T8> For( Flux<T1> ts1 ,
	                                                                  Function<? super Flux<T1>,? extends Publisher<T2>> ts2 ,
	                                                                  Function<? super Flux<T2>,? extends Publisher<T3>> ts3 ,
	                                                                  Function<? super Flux<T3>,? extends Publisher<T4>> ts4 ,
	                                                                  Function<? super Flux<T4>,? extends Publisher<T5>> ts5 ,
	                                                                  Function<? super Flux<T5>,? extends Publisher<T6>> ts6 ,
	                                                                  Function<? super Flux<T6>,? extends Publisher<T7>> ts7 ,
	                                                                  Function<? super Flux<T7>,? extends Publisher<T8>> ts8 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).flux()
		                                           .transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).flux()
		                                                                            .transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).flux()
		                                                                                                             .transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).flux()
		                                                                                                                                              .transform( ts5 )
		                                                                                                                                              .flatMap( t5 -> justOrEmpty( t5 ).flux()
		                                                                                                                                                                               .transform( ts6 )
		                                                                                                                                                                               .flatMap( t6 -> justOrEmpty( t6 ).flux()
		                                                                                                                                                                                                                .transform( ts7 )
		                                                                                                                                                                                                                .flatMap( t7 -> justOrEmpty( t7 ).flux()
		                                                                                                                                                                                                                                                 .transform( ts8 ) ) ) ) ) ) ) );
		}
	
	/**
	 * For mono.
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
	 * @param ts1
	 * 	the ts 1
	 * @param ts2
	 * 	the ts 2
	 * @param ts3
	 * 	the ts 3
	 * @param ts4
	 * 	the ts 4
	 * @param ts5
	 * 	the ts 5
	 * @param ts6
	 * 	the ts 6
	 * @param ts7
	 * 	the ts 7
	 * @param ts8
	 * 	the ts 8
	 *
	 * @return the mono
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Mono<T8> For( Mono<T1> ts1 ,
	                                                             Function<? super Mono<T1>,? extends Mono<T2>> ts2 ,
	                                                             Function<? super Mono<T2>,? extends Mono<T3>> ts3 ,
	                                                             Function<? super Mono<T3>,? extends Mono<T4>> ts4 ,
	                                                             Function<? super Mono<T4>,? extends Mono<T5>> ts5 ,
	                                                             Function<? super Mono<T5>,? extends Mono<T6>> ts6 ,
	                                                             Function<? super Mono<T6>,? extends Mono<T7>> ts7 ,
	                                                             Function<? super Mono<T7>,? extends Mono<T8>> ts8 )
		{
		return ts1.flatMap( t1 -> justOrEmpty( t1 ).transform( ts2 )
		                                           .flatMap( t2 -> justOrEmpty( t2 ).transform( ts3 )
		                                                                            .flatMap( t3 -> justOrEmpty( t3 ).transform( ts4 )
		                                                                                                             .flatMap( t4 -> justOrEmpty( t4 ).transform( ts5 )
		                                                                                                                                              .flatMap( t5 -> justOrEmpty( t5 ).transform( ts6 )
		                                                                                                                                                                               .flatMap( t6 -> justOrEmpty( t6 ).transform( ts7 )
		                                                                                                                                                                                                                .flatMap( t7 -> justOrEmpty( t7 ).transform( ts8 ) ) ) ) ) ) ) );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="Mono For-comprehension">
	

	/**
	 * Creates a {@code For}-comprehension of one Mono.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param ts1
	 * 	the 1st Mono
	 *
	 * @return a new {@code For}-comprehension of arity 1
	 */
	public static <T1> MonoFor1<T1> For( Mono<T1> ts1 )
		{
		return new MonoFor1<>( ts1 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of two Monos.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param <T2>
	 * 	component type of the 2nd Mono
	 * @param ts1
	 * 	the 1st Mono
	 * @param ts2
	 * 	the 2nd Mono
	 *
	 * @return a new {@code For}-comprehension of arity 2
	 */
	public static <T1, T2> MonoFor2<T1,T2> For( Mono<T1> ts1 ,
	                                            Mono<T2> ts2 )
		{
		return new MonoFor2<>( ts1 , ts2 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of three Monos.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param <T2>
	 * 	component type of the 2nd Mono
	 * @param <T3>
	 * 	component type of the 3rd Mono
	 * @param ts1
	 * 	the 1st Mono
	 * @param ts2
	 * 	the 2nd Mono
	 * @param ts3
	 * 	the 3rd Mono
	 *
	 * @return a new {@code For}-comprehension of arity 3
	 */
	public static <T1, T2, T3> MonoFor3<T1,T2,T3> For( Mono<T1> ts1 ,
	                                                   Mono<T2> ts2 ,
	                                                   Mono<T3> ts3 )
		{
		return new MonoFor3<>( ts1 , ts2 , ts3 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 4 Monos.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param <T2>
	 * 	component type of the 2nd Mono
	 * @param <T3>
	 * 	component type of the 3rd Mono
	 * @param <T4>
	 * 	component type of the 4th Mono
	 * @param ts1
	 * 	the 1st Mono
	 * @param ts2
	 * 	the 2nd Mono
	 * @param ts3
	 * 	the 3rd Mono
	 * @param ts4
	 * 	the 4th Mono
	 *
	 * @return a new {@code For}-comprehension of arity 4
	 */
	public static <T1, T2, T3, T4> MonoFor4<T1,T2,T3,T4> For( Mono<T1> ts1 ,
	                                                          Mono<T2> ts2 ,
	                                                          Mono<T3> ts3 ,
	                                                          Mono<T4> ts4 )
		{
		return new MonoFor4<>( ts1 , ts2 , ts3 , ts4 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 5 Monos.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param <T2>
	 * 	component type of the 2nd Mono
	 * @param <T3>
	 * 	component type of the 3rd Mono
	 * @param <T4>
	 * 	component type of the 4th Mono
	 * @param <T5>
	 * 	component type of the 5th Mono
	 * @param ts1
	 * 	the 1st Mono
	 * @param ts2
	 * 	the 2nd Mono
	 * @param ts3
	 * 	the 3rd Mono
	 * @param ts4
	 * 	the 4th Mono
	 * @param ts5
	 * 	the 5th Mono
	 *
	 * @return a new {@code For}-comprehension of arity 5
	 */
	public static <T1, T2, T3, T4, T5> MonoFor5<T1,T2,T3,T4,T5> For( Mono<T1> ts1 ,
	                                                                 Mono<T2> ts2 ,
	                                                                 Mono<T3> ts3 ,
	                                                                 Mono<T4> ts4 ,
	                                                                 Mono<T5> ts5 )
		{
		return new MonoFor5<>( ts1 , ts2 , ts3 , ts4 , ts5 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 6 Monos.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param <T2>
	 * 	component type of the 2nd Mono
	 * @param <T3>
	 * 	component type of the 3rd Mono
	 * @param <T4>
	 * 	component type of the 4th Mono
	 * @param <T5>
	 * 	component type of the 5th Mono
	 * @param <T6>
	 * 	component type of the 6th Mono
	 * @param ts1
	 * 	the 1st Mono
	 * @param ts2
	 * 	the 2nd Mono
	 * @param ts3
	 * 	the 3rd Mono
	 * @param ts4
	 * 	the 4th Mono
	 * @param ts5
	 * 	the 5th Mono
	 * @param ts6
	 * 	the 6th Mono
	 *
	 * @return a new {@code For}-comprehension of arity 6
	 */
	public static <T1, T2, T3, T4, T5, T6> MonoFor6<T1,T2,T3,T4,T5,T6> For( Mono<T1> ts1 ,
	                                                                        Mono<T2> ts2 ,
	                                                                        Mono<T3> ts3 ,
	                                                                        Mono<T4> ts4 ,
	                                                                        Mono<T5> ts5 ,
	                                                                        Mono<T6> ts6 )
		{
		return new MonoFor6<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 7 Monos.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param <T2>
	 * 	component type of the 2nd Mono
	 * @param <T3>
	 * 	component type of the 3rd Mono
	 * @param <T4>
	 * 	component type of the 4th Mono
	 * @param <T5>
	 * 	component type of the 5th Mono
	 * @param <T6>
	 * 	component type of the 6th Mono
	 * @param <T7>
	 * 	component type of the 7th Mono
	 * @param ts1
	 * 	the 1st Mono
	 * @param ts2
	 * 	the 2nd Mono
	 * @param ts3
	 * 	the 3rd Mono
	 * @param ts4
	 * 	the 4th Mono
	 * @param ts5
	 * 	the 5th Mono
	 * @param ts6
	 * 	the 6th Mono
	 * @param ts7
	 * 	the 7th Mono
	 *
	 * @return a new {@code For}-comprehension of arity 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> MonoFor7<T1,T2,T3,T4,T5,T6,T7> For( Mono<T1> ts1 ,
	                                                                               Mono<T2> ts2 ,
	                                                                               Mono<T3> ts3 ,
	                                                                               Mono<T4> ts4 ,
	                                                                               Mono<T5> ts5 ,
	                                                                               Mono<T6> ts6 ,
	                                                                               Mono<T7> ts7 )
		{
		return new MonoFor7<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 , ts7 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 8 Monos.
	 *
	 * @param <T1>
	 * 	component type of the 1st Mono
	 * @param <T2>
	 * 	component type of the 2nd Mono
	 * @param <T3>
	 * 	component type of the 3rd Mono
	 * @param <T4>
	 * 	component type of the 4th Mono
	 * @param <T5>
	 * 	component type of the 5th Mono
	 * @param <T6>
	 * 	component type of the 6th Mono
	 * @param <T7>
	 * 	component type of the 7th Mono
	 * @param <T8>
	 * 	component type of the 8th Mono
	 * @param ts1
	 * 	the 1st Mono
	 * @param ts2
	 * 	the 2nd Mono
	 * @param ts3
	 * 	the 3rd Mono
	 * @param ts4
	 * 	the 4th Mono
	 * @param ts5
	 * 	the 5th Mono
	 * @param ts6
	 * 	the 6th Mono
	 * @param ts7
	 * 	the 7th Mono
	 * @param ts8
	 * 	the 8th Mono
	 *
	 * @return a new {@code For}-comprehension of arity 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> MonoFor8<T1,T2,T3,T4,T5,T6,T7,T8> For( Mono<T1> ts1 ,
	                                                                                      Mono<T2> ts2 ,
	                                                                                      Mono<T3> ts3 ,
	                                                                                      Mono<T4> ts4 ,
	                                                                                      Mono<T5> ts5 ,
	                                                                                      Mono<T6> ts6 ,
	                                                                                      Mono<T7> ts7 ,
	                                                                                      Mono<T8> ts8 )
		{
		return new MonoFor8<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 , ts7 , ts8 );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="Flux For-comprehension">
	
	/**
	 * Creates a {@code For}-comprehension of one Flux.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param ts1
	 * 	the 1st Flux
	 *
	 * @return a new {@code For}-comprehension of arity 1
	 */
	public static <T1> FluxFor1<T1> For( Flux<T1> ts1 )
		{
		return new FluxFor1<>( ts1 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of two Fluxs.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param <T2>
	 * 	component type of the 2nd Flux
	 * @param ts1
	 * 	the 1st Flux
	 * @param ts2
	 * 	the 2nd Flux
	 *
	 * @return a new {@code For}-comprehension of arity 2
	 */
	public static <T1, T2> FluxFor2<T1,T2> For( Flux<T1> ts1 ,
	                                            Flux<T2> ts2 )
		{
		return new FluxFor2<>( ts1 , ts2 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of three Fluxs.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param <T2>
	 * 	component type of the 2nd Flux
	 * @param <T3>
	 * 	component type of the 3rd Flux
	 * @param ts1
	 * 	the 1st Flux
	 * @param ts2
	 * 	the 2nd Flux
	 * @param ts3
	 * 	the 3rd Flux
	 *
	 * @return a new {@code For}-comprehension of arity 3
	 */
	public static <T1, T2, T3> FluxFor3<T1,T2,T3> For( Flux<T1> ts1 ,
	                                                   Flux<T2> ts2 ,
	                                                   Flux<T3> ts3 )
		{
		return new FluxFor3<>( ts1 , ts2 , ts3 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 4 Fluxs.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param <T2>
	 * 	component type of the 2nd Flux
	 * @param <T3>
	 * 	component type of the 3rd Flux
	 * @param <T4>
	 * 	component type of the 4th Flux
	 * @param ts1
	 * 	the 1st Flux
	 * @param ts2
	 * 	the 2nd Flux
	 * @param ts3
	 * 	the 3rd Flux
	 * @param ts4
	 * 	the 4th Flux
	 *
	 * @return a new {@code For}-comprehension of arity 4
	 */
	public static <T1, T2, T3, T4> FluxFor4<T1,T2,T3,T4> For( Flux<T1> ts1 ,
	                                                          Flux<T2> ts2 ,
	                                                          Flux<T3> ts3 ,
	                                                          Flux<T4> ts4 )
		{
		return new FluxFor4<>( ts1 , ts2 , ts3 , ts4 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 5 Fluxs.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param <T2>
	 * 	component type of the 2nd Flux
	 * @param <T3>
	 * 	component type of the 3rd Flux
	 * @param <T4>
	 * 	component type of the 4th Flux
	 * @param <T5>
	 * 	component type of the 5th Flux
	 * @param ts1
	 * 	the 1st Flux
	 * @param ts2
	 * 	the 2nd Flux
	 * @param ts3
	 * 	the 3rd Flux
	 * @param ts4
	 * 	the 4th Flux
	 * @param ts5
	 * 	the 5th Flux
	 *
	 * @return a new {@code For}-comprehension of arity 5
	 */
	public static <T1, T2, T3, T4, T5> FluxFor5<T1,T2,T3,T4,T5> For( Flux<T1> ts1 ,
	                                                                 Flux<T2> ts2 ,
	                                                                 Flux<T3> ts3 ,
	                                                                 Flux<T4> ts4 ,
	                                                                 Flux<T5> ts5 )
		{
		return new FluxFor5<>( ts1 , ts2 , ts3 , ts4 , ts5 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 6 Fluxs.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param <T2>
	 * 	component type of the 2nd Flux
	 * @param <T3>
	 * 	component type of the 3rd Flux
	 * @param <T4>
	 * 	component type of the 4th Flux
	 * @param <T5>
	 * 	component type of the 5th Flux
	 * @param <T6>
	 * 	component type of the 6th Flux
	 * @param ts1
	 * 	the 1st Flux
	 * @param ts2
	 * 	the 2nd Flux
	 * @param ts3
	 * 	the 3rd Flux
	 * @param ts4
	 * 	the 4th Flux
	 * @param ts5
	 * 	the 5th Flux
	 * @param ts6
	 * 	the 6th Flux
	 *
	 * @return a new {@code For}-comprehension of arity 6
	 */
	public static <T1, T2, T3, T4, T5, T6> FluxFor6<T1,T2,T3,T4,T5,T6> For( Flux<T1> ts1 ,
	                                                                        Flux<T2> ts2 ,
	                                                                        Flux<T3> ts3 ,
	                                                                        Flux<T4> ts4 ,
	                                                                        Flux<T5> ts5 ,
	                                                                        Flux<T6> ts6 )
		{
		return new FluxFor6<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 7 Fluxs.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param <T2>
	 * 	component type of the 2nd Flux
	 * @param <T3>
	 * 	component type of the 3rd Flux
	 * @param <T4>
	 * 	component type of the 4th Flux
	 * @param <T5>
	 * 	component type of the 5th Flux
	 * @param <T6>
	 * 	component type of the 6th Flux
	 * @param <T7>
	 * 	component type of the 7th Flux
	 * @param ts1
	 * 	the 1st Flux
	 * @param ts2
	 * 	the 2nd Flux
	 * @param ts3
	 * 	the 3rd Flux
	 * @param ts4
	 * 	the 4th Flux
	 * @param ts5
	 * 	the 5th Flux
	 * @param ts6
	 * 	the 6th Flux
	 * @param ts7
	 * 	the 7th Flux
	 *
	 * @return a new {@code For}-comprehension of arity 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> FluxFor7<T1,T2,T3,T4,T5,T6,T7> For( Flux<T1> ts1 ,
	                                                                               Flux<T2> ts2 ,
	                                                                               Flux<T3> ts3 ,
	                                                                               Flux<T4> ts4 ,
	                                                                               Flux<T5> ts5 ,
	                                                                               Flux<T6> ts6 ,
	                                                                               Flux<T7> ts7 )
		{
		return new FluxFor7<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 , ts7 );
		}
	
	/**
	 * Creates a {@code For}-comprehension of 8 Fluxs.
	 *
	 * @param <T1>
	 * 	component type of the 1st Flux
	 * @param <T2>
	 * 	component type of the 2nd Flux
	 * @param <T3>
	 * 	component type of the 3rd Flux
	 * @param <T4>
	 * 	component type of the 4th Flux
	 * @param <T5>
	 * 	component type of the 5th Flux
	 * @param <T6>
	 * 	component type of the 6th Flux
	 * @param <T7>
	 * 	component type of the 7th Flux
	 * @param <T8>
	 * 	component type of the 8th Flux
	 * @param ts1
	 * 	the 1st Flux
	 * @param ts2
	 * 	the 2nd Flux
	 * @param ts3
	 * 	the 3rd Flux
	 * @param ts4
	 * 	the 4th Flux
	 * @param ts5
	 * 	the 5th Flux
	 * @param ts6
	 * 	the 6th Flux
	 * @param ts7
	 * 	the 7th Flux
	 * @param ts8
	 * 	the 8th Flux
	 *
	 * @return a new {@code For}-comprehension of arity 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> FluxFor8<T1,T2,T3,T4,T5,T6,T7,T8> For( Flux<T1> ts1 ,
	                                                                                      Flux<T2> ts2 ,
	                                                                                      Flux<T3> ts3 ,
	                                                                                      Flux<T4> ts4 ,
	                                                                                      Flux<T5> ts5 ,
	                                                                                      Flux<T6> ts6 ,
	                                                                                      Flux<T7> ts7 ,
	                                                                                      Flux<T8> ts8 )
		{
		return new FluxFor8<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 , ts7 , ts8 );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="Mono For-comprehension helper classes">
	
	/**
	 * For-comprehension with one Mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 */
	public static class MonoFor1<T1>
		{
		
		private final Mono<T1> ts1;
		
		private MonoFor1( Mono<T1> ts1 )
			{
			this.ts1 = ts1;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Mono.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( Function<? super T1,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.map( f );
			}
		
		/**
		 * A shortcut for {@code yield(Function.identity())}.
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public Mono<T1> yield()
			{
			return yield( Function.identity() );
			}
		}
	
	/**
	 * For-comprehension with two Monos.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 */
	public static class MonoFor2<T1, T2>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private MonoFor2( Mono<T1> ts1 ,
		                  Mono<T2> ts2 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Monos.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( BiFunction<? super T1,? super T2,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.map( t2 -> f.apply( t1 , t2 ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with three Monos.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 */
	public static class MonoFor3<T1, T2, T3>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private MonoFor3( Mono<T1> ts1 ,
		                  Mono<T2> ts2 ,
		                  Mono<T3> ts3 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Monos.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( Function3<? super T1,? super T2,? super T3,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.map( t3 -> f.apply( t1 , t2 , t3 ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 4 Monos.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 */
	public static class MonoFor4<T1, T2, T3, T4>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private MonoFor4( Mono<T1> ts1 ,
		                  Mono<T2> ts2 ,
		                  Mono<T3> ts3 ,
		                  Mono<T4> ts4 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Monos.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( Function4<? super T1,? super T2,? super T3,? super T4,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.map( t4 -> f.apply( t1 , t2 , t3 , t4 ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 5 Monos.
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
	 */
	public static class MonoFor5<T1, T2, T3, T4, T5>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private MonoFor5( Mono<T1> ts1 ,
		                  Mono<T2> ts2 ,
		                  Mono<T3> ts3 ,
		                  Mono<T4> ts4 ,
		                  Mono<T5> ts5 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Monos.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( Function5<? super T1,? super T2,? super T3,? super T4,? super T5,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.map( t5 -> f.apply( t1 , t2 , t3 , t4 , t5 ) ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 6 Monos.
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
	 */
	public static class MonoFor6<T1, T2, T3, T4, T5, T6>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private final Mono<T6> ts6;
		
		private MonoFor6( Mono<T1> ts1 ,
		                  Mono<T2> ts2 ,
		                  Mono<T3> ts3 ,
		                  Mono<T4> ts4 ,
		                  Mono<T5> ts5 ,
		                  Mono<T6> ts6 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			this.ts6 = ts6;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Monos.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( Function6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null || ts6 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.flatMap( t5 -> ts6.map( t6 -> f.apply( t1 , t2 , t3 , t4 , t5 , t6 ) ) ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 7 Monos.
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
	 */
	public static class MonoFor7<T1, T2, T3, T4, T5, T6, T7>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private final Mono<T6> ts6;
		
		private final Mono<T7> ts7;
		
		private MonoFor7( Mono<T1> ts1 ,
		                  Mono<T2> ts2 ,
		                  Mono<T3> ts3 ,
		                  Mono<T4> ts4 ,
		                  Mono<T5> ts5 ,
		                  Mono<T6> ts6 ,
		                  Mono<T7> ts7 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			this.ts6 = ts6;
			this.ts7 = ts7;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Monos.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( Function7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null || ts6 == null || ts7 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.flatMap( t5 -> ts6.flatMap( t6 -> ts7.map( t7 -> f.apply( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) ) ) ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 8 Monos.
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
	 */
	public static class MonoFor8<T1, T2, T3, T4, T5, T6, T7, T8>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private final Mono<T6> ts6;
		
		private final Mono<T7> ts7;
		
		private final Mono<T8> ts8;
		
		private MonoFor8( Mono<T1> ts1 ,
		                  Mono<T2> ts2 ,
		                  Mono<T3> ts3 ,
		                  Mono<T4> ts4 ,
		                  Mono<T5> ts5 ,
		                  Mono<T6> ts6 ,
		                  Mono<T7> ts7 ,
		                  Mono<T8> ts8 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			this.ts6 = ts6;
			this.ts7 = ts7;
			this.ts8 = ts8;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Monos.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Mono} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Mono} of mapped results
		 */
		public <R> Mono<R> yield( Function8<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? super T8,? extends R> f )
			{
			if( f == null )
				return illegalArgumentMonoError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null || ts6 == null || ts7 == null || ts8 == null )
				return illegalArgumentMonoError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.flatMap( t5 -> ts6.flatMap( t6 -> ts7.flatMap( t7 -> ts8.map( t8 -> f.apply( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ) ) ) ) ) ) ) );
			}
			
		}
	//</editor-fold>
	
	//<editor-fold desc="Flux For-comprehension helper classes">
	
	/**
	 * For-comprehension with one Flux.
	 *
	 * @param <T1>
	 * 	the type parameter
	 */
	public static class FluxFor1<T1>
		{
		
		private final Flux<T1> ts1;
		
		private FluxFor1( Flux<T1> ts1 )
			{
			this.ts1 = ts1;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Flux.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( Function<? super T1,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.map( f );
			}
		
		/**
		 * A shortcut for {@code yield(Function.identity())}.
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public Flux<T1> yield()
			{
			return yield( Function.identity() );
			}
		}
	
	/**
	 * For-comprehension with two Fluxs.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 */
	public static class FluxFor2<T1, T2>
		{
		
		private final Flux<T1> ts1;
		
		private final Flux<T2> ts2;
		
		private FluxFor2( Flux<T1> ts1 ,
		                  Flux<T2> ts2 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Fluxs.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( BiFunction<? super T1,? super T2,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.map( t2 -> f.apply( t1 , t2 ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with three Fluxs.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 */
	public static class FluxFor3<T1, T2, T3>
		{
		
		private final Flux<T1> ts1;
		
		private final Flux<T2> ts2;
		
		private final Flux<T3> ts3;
		
		private FluxFor3( Flux<T1> ts1 ,
		                  Flux<T2> ts2 ,
		                  Flux<T3> ts3 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Fluxs.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( Function3<? super T1,? super T2,? super T3,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.map( t3 -> f.apply( t1 , t2 , t3 ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 4 Fluxs.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 */
	public static class FluxFor4<T1, T2, T3, T4>
		{
		
		private final Flux<T1> ts1;
		
		private final Flux<T2> ts2;
		
		private final Flux<T3> ts3;
		
		private final Flux<T4> ts4;
		
		private FluxFor4( Flux<T1> ts1 ,
		                  Flux<T2> ts2 ,
		                  Flux<T3> ts3 ,
		                  Flux<T4> ts4 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Fluxs.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( Function4<? super T1,? super T2,? super T3,? super T4,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.map( t4 -> f.apply( t1 , t2 , t3 , t4 ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 5 Fluxs.
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
	 */
	public static class FluxFor5<T1, T2, T3, T4, T5>
		{
		
		private final Flux<T1> ts1;
		
		private final Flux<T2> ts2;
		
		private final Flux<T3> ts3;
		
		private final Flux<T4> ts4;
		
		private final Flux<T5> ts5;
		
		private FluxFor5( Flux<T1> ts1 ,
		                  Flux<T2> ts2 ,
		                  Flux<T3> ts3 ,
		                  Flux<T4> ts4 ,
		                  Flux<T5> ts5 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Fluxs.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( Function5<? super T1,? super T2,? super T3,? super T4,? super T5,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.map( t5 -> f.apply( t1 , t2 , t3 , t4 , t5 ) ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 6 Fluxs.
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
	 */
	public static class FluxFor6<T1, T2, T3, T4, T5, T6>
		{
		
		private final Flux<T1> ts1;
		
		private final Flux<T2> ts2;
		
		private final Flux<T3> ts3;
		
		private final Flux<T4> ts4;
		
		private final Flux<T5> ts5;
		
		private final Flux<T6> ts6;
		
		private FluxFor6( Flux<T1> ts1 ,
		                  Flux<T2> ts2 ,
		                  Flux<T3> ts3 ,
		                  Flux<T4> ts4 ,
		                  Flux<T5> ts5 ,
		                  Flux<T6> ts6 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			this.ts6 = ts6;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Fluxs.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( Function6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null || ts6 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.flatMap( t5 -> ts6.map( t6 -> f.apply( t1 , t2 , t3 , t4 , t5 , t6 ) ) ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 7 Fluxs.
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
	 */
	public static class FluxFor7<T1, T2, T3, T4, T5, T6, T7>
		{
		
		private final Flux<T1> ts1;
		
		private final Flux<T2> ts2;
		
		private final Flux<T3> ts3;
		
		private final Flux<T4> ts4;
		
		private final Flux<T5> ts5;
		
		private final Flux<T6> ts6;
		
		private final Flux<T7> ts7;
		
		private FluxFor7( Flux<T1> ts1 ,
		                  Flux<T2> ts2 ,
		                  Flux<T3> ts3 ,
		                  Flux<T4> ts4 ,
		                  Flux<T5> ts5 ,
		                  Flux<T6> ts6 ,
		                  Flux<T7> ts7 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			this.ts6 = ts6;
			this.ts7 = ts7;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Fluxs.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( Function7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null || ts6 == null || ts7 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.flatMap( t5 -> ts6.flatMap( t6 -> ts7.map( t7 -> f.apply( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) ) ) ) ) ) ) );
			}
			
		}
	
	/**
	 * For-comprehension with 8 Fluxs.
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
	 */
	public static class FluxFor8<T1, T2, T3, T4, T5, T6, T7, T8>
		{
		
		private final Flux<T1> ts1;
		
		private final Flux<T2> ts2;
		
		private final Flux<T3> ts3;
		
		private final Flux<T4> ts4;
		
		private final Flux<T5> ts5;
		
		private final Flux<T6> ts6;
		
		private final Flux<T7> ts7;
		
		private final Flux<T8> ts8;
		
		private FluxFor8( Flux<T1> ts1 ,
		                  Flux<T2> ts2 ,
		                  Flux<T3> ts3 ,
		                  Flux<T4> ts4 ,
		                  Flux<T5> ts5 ,
		                  Flux<T6> ts6 ,
		                  Flux<T7> ts7 ,
		                  Flux<T8> ts8 )
			{
			this.ts1 = ts1;
			this.ts2 = ts2;
			this.ts3 = ts3;
			this.ts4 = ts4;
			this.ts5 = ts5;
			this.ts6 = ts6;
			this.ts7 = ts7;
			this.ts8 = ts8;
			}
		
		/**
		 * Yields a result for elements of the cross product of the underlying Fluxs.
		 *
		 * @param <R>
		 * 	type of the resulting {@code Flux} elements
		 * @param f
		 * 	a function that maps an element of the cross product to a result
		 *
		 * @return an {@code Flux} of mapped results
		 */
		public <R> Flux<R> yield( Function8<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? super T8,? extends R> f )
			{
			if( f == null )
				return illegalArgumentError( "Input function must not be null." );
			else if( ts1 == null || ts2 == null || ts3 == null || ts4 == null || ts5 == null || ts6 == null || ts7 == null || ts8 == null )
				return illegalArgumentError( "Transformation function must not be null." );
			else
				return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.flatMap( t5 -> ts6.flatMap( t6 -> ts7.flatMap( t7 -> ts8.map( t8 -> f.apply( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ) ) ) ) ) ) ) );
			}
			
		}
	//</editor-fold>
	
	}
