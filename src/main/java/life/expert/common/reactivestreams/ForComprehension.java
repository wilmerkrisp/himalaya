package life.expert.common.reactivestreams;









import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;



import org.reactivestreams.Publisher;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.function.*;                            //producer supplier



import static reactor.core.publisher.Mono.*;



//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/20
//
//--------------------------------------------------------------------------------









/**
 * auxiliary static functions with arguments - several Function
 *
 * - functional for-comprehension pattern for reactive flows
 * - required to convert a null value returned by a function to an empty flow event
 * - at the first null value returned, the chain of nested calls stops
 */
@UtilityClass
@Slf4j
public final class ForComprehension
	{
	
	
	
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
		
		
		
	}
