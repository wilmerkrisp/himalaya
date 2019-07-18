package life.expert.common.function;









import com.google.common.base.Throwables;
import cyclops.function.checked.CheckedBiConsumer;
import cyclops.function.checked.CheckedBiFunction;
import cyclops.function.checked.CheckedBiPredicate;
import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedFunction2;
import io.vavr.CheckedPredicate;
import io.vavr.CheckedRunnable;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.function.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static life.expert.common.async.LogUtils.logAtErrorFunction;
import static life.expert.common.async.LogUtils.printFunction;
import static reactor.core.publisher.Mono.never;



//import static life.expert.common.base.Preconditions.*;  //checkCollection



//import java.util.List;                                 //usual list
//import io.vavr.collection.List;                        //immutable List
//import com.google.common.collect.*;                   //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/10
//
//--------------------------------------------------------------------------------









/**
 * - contains methods that wrap lambdas with checked exceptions inside
 * 1) wrap to bool
 * 2) wrap to unchecked exception
 * 3) wrap to unchecked exception and log
 */
@UtilityClass
@Slf4j
public final class CheckedUtils
	{
	
	
	
	//<editor-fold desc="consumer->bool">
	
	
	
	/**
	 * Lifts the given {@code consumer} into a total function that returns an {@code Option} result.
	 *
	 * @param <T>
	 * 	1st argument
	 * @param consumer
	 * 	a function that is not defined for all values of the domain (e.g. by throwing)
	 *
	 * @return a function that applies arguments to the given {@code consumer} and returns {@code Some(null)} 	if the function is defined for the given arguments, and {@code None} otherwise.
	 */
	//@SuppressWarnings( "RedundantTypeArguments" )
	public static <T> Function<T,Optional<Boolean>> consumerToOptional( CheckedConsumer<? super T> consumer )
		{
		return t -> Try.<Boolean>of( () ->
		                             {
		                             uncheckedConsumer( consumer ).accept( t );
		                             return true;
		                             } ).toJavaOptional();
		}
	
	
	
	/**
	 * Bi consumer to optional bi function.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the bi function
	 */
	public static <T1, T2> BiFunction<T1,T2,Optional<Boolean>> biConsumerToOptional( CheckedBiConsumer<? super T1,? super T2> consumer )
		{
		return ( t1 , t2 ) -> Try.<Boolean>of( () ->
		                                       {
		                                       uncheckedBiConsumer( consumer ).accept( t1 , t2 );
		                                       return true;
		                                       } ).toJavaOptional();
		}
	
	
	
	/**
	 * Consumer 3 to optional function 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 3
	 */
	public static <T1, T2, T3> Function3<T1,T2,T3,Optional<Boolean>> consumer3ToOptional( CheckedConsumer3<? super T1,? super T2,? super T3> consumer )
		{
		return ( t1 , t2 , t3 ) -> Try.<Boolean>of( () ->
		                                            {
		                                            uncheckedConsumer3( consumer ).accept( t1 , t2 , t3 );
		                                            return true;
		                                            } ).toJavaOptional();
		}
	
	
	
	/**
	 * Consumer 4 to optional function 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 4
	 */
	public static <T1, T2, T3, T4> Function4<T1,T2,T3,T4,Optional<Boolean>> consumer4ToOptional( CheckedConsumer4<? super T1,? super T2,? super T3,? super T4> consumer )
		{
		return ( t1 , t2 , t3 , t4 ) -> Try.<Boolean>of( () ->
		                                                 {
		                                                 uncheckedConsumer4( consumer ).accept( t1 , t2 , t3 , t4 );
		                                                 return true;
		                                                 } ).toJavaOptional();
		}
	
	
	
	/**
	 * Consumer 5 to optional function 5.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 5
	 */
	public static <T1, T2, T3, T4, T5> Function5<T1,T2,T3,T4,T5,Optional<Boolean>> consumer5ToOptional( CheckedConsumer5<? super T1,? super T2,? super T3,? super T4,? super T5> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 ) -> Try.<Boolean>of( () ->
		                                                      {
		                                                      uncheckedConsumer5( consumer ).accept( t1 , t2 , t3 , t4 , t5 );
		                                                      return true;
		                                                      } ).toJavaOptional();
		}
	
	
	
	/**
	 * Consumer 6 to optional function 6.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Function6<T1,T2,T3,T4,T5,T6,Optional<Boolean>> consumer6ToOptional( CheckedConsumer6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) -> Try.<Boolean>of( () ->
		                                                           {
		                                                           uncheckedConsumer6( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 );
		                                                           return true;
		                                                           } ).toJavaOptional();
		}
	
	
	
	/**
	 * Consumer 7 to optional function 7.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Function7<T1,T2,T3,T4,T5,T6,T7,Optional<Boolean>> consumer7ToOptional( CheckedConsumer7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) -> Try.<Boolean>of( () ->
		                                                                {
		                                                                uncheckedConsumer7( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
		                                                                return true;
		                                                                } ).toJavaOptional();
		}
	
	
	
	/**
	 * Consumer 8 to optional function 8.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Function8<T1,T2,T3,T4,T5,T6,T7,T8,Optional<Boolean>> consumer8ToOptional( CheckedConsumer8<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? super T8> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) -> Try.<Boolean>of( () ->
		                                                                     {
		                                                                     uncheckedConsumer8( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
		                                                                     return true;
		                                                                     } ).toJavaOptional();
		}
	
	
	
	/**
	 * Lifts the given {@code consumer} into a total function that returns an {@code Try} result.
	 *
	 * @param <T>
	 * 	1st argument
	 * @param consumer
	 * 	a function that is not defined for all values of the domain (e.g. by throwing)
	 *
	 * @return a function that applies arguments to the given {@code consumer} and returns {@code Success(null)} 	if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
	 */
	public static <T> Function<T,Try<Boolean>> consumerToTry( CheckedConsumer<? super T> consumer )
		{
		
		return t -> Try.of( () ->
		                    {
		                    uncheckedConsumer( consumer ).accept( t );
		                    return true;
		                    } );
		}
	
	
	
	/**
	 * Bi consumer to try bi function.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the bi function
	 */
	public static <T1, T2> BiFunction<T1,T2,Try<Boolean>> biConsumerToTry( CheckedBiConsumer<? super T1,? super T2> consumer )
		{
		
		return ( t1 , t2 ) -> Try.of( () ->
		                              {
		                              uncheckedBiConsumer( consumer ).accept( t1 , t2 );
		                              return true;
		                              } );
		}
	
	
	
	/**
	 * Consumer 3 to try function 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 3
	 */
	public static <T1, T2, T3> Function3<T1,T2,T3,Try<Boolean>> consumer3ToTry( CheckedConsumer3<? super T1,? super T2,? super T3> consumer )
		{
		return ( t1 , t2 , t3 ) -> Try.<Boolean>of( () ->
		                                            {
		                                            uncheckedConsumer3( consumer ).accept( t1 , t2 , t3 );
		                                            return true;
		                                            } );
		}
	
	
	
	/**
	 * Consumer 4 to try function 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 4
	 */
	public static <T1, T2, T3, T4> Function4<T1,T2,T3,T4,Try<Boolean>> consumer4ToTry( CheckedConsumer4<? super T1,? super T2,? super T3,? super T4> consumer )
		{
		return ( t1 , t2 , t3 , t4 ) -> Try.<Boolean>of( () ->
		                                                 {
		                                                 uncheckedConsumer4( consumer ).accept( t1 , t2 , t3 , t4 );
		                                                 return true;
		                                                 } );
		}
	
	
	
	/**
	 * Consumer 5 to try function 5.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 5
	 */
	public static <T1, T2, T3, T4, T5> Function5<T1,T2,T3,T4,T5,Try<Boolean>> consumer5ToTry( CheckedConsumer5<? super T1,? super T2,? super T3,? super T4,? super T5> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 ) -> Try.<Boolean>of( () ->
		                                                      {
		                                                      uncheckedConsumer5( consumer ).accept( t1 , t2 , t3 , t4 , t5 );
		                                                      return true;
		                                                      } );
		}
	
	
	
	/**
	 * Consumer 6 to try function 6.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Function6<T1,T2,T3,T4,T5,T6,Try<Boolean>> consumer6ToTry( CheckedConsumer6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) -> Try.<Boolean>of( () ->
		                                                           {
		                                                           uncheckedConsumer6( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 );
		                                                           return true;
		                                                           } );
		}
	
	
	
	/**
	 * Consumer 7 to try function 7.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Function7<T1,T2,T3,T4,T5,T6,T7,Try<Boolean>> consumer7ToTry( CheckedConsumer7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) -> Try.<Boolean>of( () ->
		                                                                {
		                                                                uncheckedConsumer7( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
		                                                                return true;
		                                                                } );
		}
	
	
	
	/**
	 * Consumer 8 to try function 8.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Function8<T1,T2,T3,T4,T5,T6,T7,T8,Try<Boolean>> consumer8ToTry( CheckedConsumer8<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? super T8> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) -> Try.<Boolean>of( () ->
		                                                                     {
		                                                                     uncheckedConsumer8( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
		                                                                     return true;
		                                                                     } );
		}
	
	
	
	/**
	 * Alias for {@link CheckedConsumer#unchecked}
	 *
	 * @param <T>
	 * 	return type
	 * @param consumer
	 * 	A method reference
	 *
	 * @return An unchecked wrapper of supplied {@link CheckedFunction0}
	 */
	public static <T> Function<T,Boolean> consumerToBoolean( CheckedConsumer<T> consumer )
		{
		return t ->
		{
		uncheckedConsumer( consumer ).accept( t );
		return true;
		};
		}
	
	
	
	/**
	 * Bi consumer to boolean bi function.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the bi function
	 */
	public static <T1, T2> BiFunction<T1,T2,Boolean> biConsumerToBoolean( CheckedBiConsumer<T1,T2> consumer )
		{
		return ( t1 , t2 ) ->
		{
		uncheckedBiConsumer( consumer ).accept( t1 , t2 );
		return true;
		};
		}
	
	
	
	/**
	 * Consumer 3 to boolean function 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 3
	 */
	public static <T1, T2, T3> Function3<T1,T2,T3,Boolean> consumer3ToBoolean( CheckedConsumer3<? super T1,? super T2,? super T3> consumer )
		{
		return ( t1 , t2 , t3 ) ->
		{
		uncheckedConsumer3( consumer ).accept( t1 , t2 , t3 );
		return true;
		};
		}
	
	
	
	/**
	 * Consumer 4 to boolean function 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 4
	 */
	public static <T1, T2, T3, T4> Function4<T1,T2,T3,T4,Boolean> consumer4ToBoolean( CheckedConsumer4<? super T1,? super T2,? super T3,? super T4> consumer )
		{
		return ( t1 , t2 , t3 , t4 ) ->
		{
		uncheckedConsumer4( consumer ).accept( t1 , t2 , t3 , t4 );
		return true;
		};
		}
	
	
	
	/**
	 * Consumer 5 to boolean function 5.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 5
	 */
	public static <T1, T2, T3, T4, T5> Function5<T1,T2,T3,T4,T5,Boolean> consumer5ToBoolean( CheckedConsumer5<? super T1,? super T2,? super T3,? super T4,? super T5> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 ) ->
		{
		uncheckedConsumer5( consumer ).accept( t1 , t2 , t3 , t4 , t5 );
		return true;
		};
		}
	
	
	
	/**
	 * Consumer 6 to boolean function 6.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Function6<T1,T2,T3,T4,T5,T6,Boolean> consumer6ToBoolean( CheckedConsumer6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) ->
		{
		uncheckedConsumer6( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 );
		return true;
		};
		}
	
	
	
	/**
	 * Consumer 7 to boolean function 7.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Function7<T1,T2,T3,T4,T5,T6,T7,Boolean> consumer7ToBoolean( CheckedConsumer7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) ->
		{
		uncheckedConsumer7( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
		return true;
		};
		}
	
	
	
	/**
	 * Consumer 8 to boolean function 8.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Function8<T1,T2,T3,T4,T5,T6,T7,T8,Boolean> consumer8ToBoolean( CheckedConsumer8<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? super T8> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ->
		{
		uncheckedConsumer8( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
		return true;
		};
		}
	
	
	
	/**
	 * Alias for {@link CheckedConsumer#unchecked}
	 *
	 * @param <T>
	 * 	return type
	 * @param consumer
	 * 	A method reference
	 *
	 * @return An unchecked wrapper of supplied {@link CheckedFunction0}
	 */
	public static <T> Function<T,Mono<Boolean>> consumerToMono( CheckedConsumer<T> consumer )
		{
		return t -> Mono.fromSupplier( () ->
		                               {
		                               uncheckedConsumer( consumer ).accept( t );
		                               return true;
		                               } );
			
			
			
		}
	
	
	
	/**
	 * Bi consumer to mono bi function.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the bi function
	 */
	public static <T1, T2> BiFunction<T1,T2,Mono<Boolean>> biConsumerToMono( CheckedBiConsumer<T1,T2> consumer )
		{
		return ( t1 , t2 ) -> Mono.fromSupplier( () ->
		                                         {
		                                         uncheckedBiConsumer( consumer ).accept( t1 , t2 );
		                                         return true;
		                                         } );
		}
	
	
	
	/**
	 * Consumer 3 to mono function 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 3
	 */
	public static <T1, T2, T3> Function3<T1,T2,T3,Mono<Boolean>> consumer3ToMono( CheckedConsumer3<? super T1,? super T2,? super T3> consumer )
		{
		return ( t1 , t2 , t3 ) -> Mono.fromSupplier( () ->
		                                              {
		                                              uncheckedConsumer3( consumer ).accept( t1 , t2 , t3 );
		                                              return true;
		                                              } );
		}
	
	
	
	/**
	 * Consumer 4 to mono function 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 4
	 */
	public static <T1, T2, T3, T4> Function4<T1,T2,T3,T4,Mono<Boolean>> consumer4ToMono( CheckedConsumer4<? super T1,? super T2,? super T3,? super T4> consumer )
		{
		return ( t1 , t2 , t3 , t4 ) -> Mono.fromSupplier( () ->
		                                                   {
		                                                   uncheckedConsumer4( consumer ).accept( t1 , t2 , t3 , t4 );
		                                                   return true;
		                                                   } );
		}
	
	
	
	/**
	 * Consumer 5 to mono function 5.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 5
	 */
	public static <T1, T2, T3, T4, T5> Function5<T1,T2,T3,T4,T5,Mono<Boolean>> consumer5ToMono( CheckedConsumer5<? super T1,? super T2,? super T3,? super T4,? super T5> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 ) -> Mono.fromSupplier( () ->
		                                                        {
		                                                        uncheckedConsumer5( consumer ).accept( t1 , t2 , t3 , t4 , t5 );
		                                                        return true;
		                                                        } );
		}
	
	
	
	/**
	 * Consumer 6 to mono function 6.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Function6<T1,T2,T3,T4,T5,T6,Mono<Boolean>> consumer6ToMono( CheckedConsumer6<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) -> Mono.fromSupplier( () ->
		                                                             {
		                                                             uncheckedConsumer6( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 );
		                                                             return true;
		                                                             } );
		}
	
	
	
	/**
	 * Consumer 7 to mono function 7.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Function7<T1,T2,T3,T4,T5,T6,T7,Mono<Boolean>> consumer7ToMono( CheckedConsumer7<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) -> Mono.fromSupplier( () ->
		                                                                  {
		                                                                  uncheckedConsumer7( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
		                                                                  return true;
		                                                                  } );
		}
	
	
	
	/**
	 * Consumer 8 to mono function 8.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the function 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Function8<T1,T2,T3,T4,T5,T6,T7,T8,Mono<Boolean>> consumer8ToMono( CheckedConsumer8<? super T1,? super T2,? super T3,? super T4,? super T5,? super T6,? super T7,? super T8> consumer )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) -> Mono.fromSupplier( () ->
		                                                                       {
		                                                                       uncheckedConsumer8( consumer ).accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
		                                                                       return true;
		                                                                       } );
		}
	
	//</editor-fold>
	
	
	//<editor-fold desc="runnable->bool">
	
	
	
	/**
	 * Runnable to option function 1.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the function 1
	 */
	//@SuppressWarnings( "RedundantTypeArguments" )
	public static <T> Function<T,Optional<Boolean>> runnableToOptional( CheckedRunnable runnable )
		{
		return t -> Try.<Boolean>of( () ->
		                             {
		                             uncheckedRunnable( runnable ).run();
		                             return true;
		                             } ).toJavaOptional();
		}
	
	
	
	/**
	 * Runnable to try function 1.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the function 1
	 */
	public static <T> Function<T,Try<Boolean>> runnableToTry( CheckedRunnable runnable )
		{
		
		return t -> Try.of( () ->
		                    {
		                    uncheckedRunnable( runnable ).run();
		                    return true;
		                    } );
		}
	
	
	
	/**
	 * Runnable to boolean function 1.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the function 1
	 */
	public static <T> Function<T,Boolean> runnableToBoolean( CheckedRunnable runnable )
		{
		return t ->
		{
		uncheckedRunnable( runnable ).run();
		return true;
		};
		}
	
	
	
	/**
	 * Alias for {@link CheckedConsumer#unchecked}
	 *
	 * @param <T>
	 * 	return type
	 * @param <R>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return An unchecked wrapper of supplied {@link CheckedFunction0}
	 */
	public static <T, R> Function<T,Mono<Boolean>> runnableToMono( CheckedRunnable runnable )
		{
		return t -> Mono.fromSupplier( () ->
		                               {
		                               uncheckedRunnable( runnable ).run();
		                               return true;
		                               } );
		}
	
	
	//</editor-fold>
	
	
	
	//
	//
	//	/**
	//	 * Supplier to mono function.
	//	 *
	//	 * @param <T>
	//	 * 	the type parameter
	//	 * @param <R>
	//	 * 	the type parameter
	//	 * @param supplier
	//	 * 	the supplier
	//	 *
	//	 * @return the function
	//	 */
	//	public static <T, R> Function<T,Mono<R>> supplierToMono( CheckedFunction0<R> supplier )
	//		{
	//		return t -> Mono.fromSupplier( uncheckedSupplier( supplier ) );
	//		}
	//
	
	
	
	//<editor-fold desc="unchecked wrappers">
	
	
	
	/**
	 * Unchecked consumer consumer.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	public static <T> Consumer<T> uncheckedConsumer( CheckedConsumer<T> consumer )
		{
		return t ->
		{
		try
			{
			consumer.accept( t );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked bi consumer bi consumer.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the bi consumer
	 */
	public static <T1, T2> BiConsumer<T1,T2> uncheckedBiConsumer( CheckedBiConsumer<T1,T2> consumer )
		{
		
		return ( t1 , t2 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + ", " + t2 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 3 consumer 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer 3
	 */
	public static <T1, T2, T3> Consumer3<T1,T2,T3> uncheckedConsumer3( CheckedConsumer3<T1,T2,T3> consumer )
		{
		
		return ( t1 , t2 , t3 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + ", " + t2 + ", " + t3 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 4 consumer 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer 4
	 */
	public static <T1, T2, T3, T4> Consumer4<T1,T2,T3,T4> uncheckedConsumer4( CheckedConsumer4<T1,T2,T3,T4> consumer )
		{
		
		return ( t1 , t2 , t3 , t4 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + ", " + t2 + ", " + t3 + ", " + t4 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 5 consumer 5.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer 5
	 */
	public static <T1, T2, T3, T4, T5> Consumer5<T1,T2,T3,T4,T5> uncheckedConsumer5( CheckedConsumer5<T1,T2,T3,T4,T5> consumer )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 6 consumer 6.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Consumer6<T1,T2,T3,T4,T5,T6> uncheckedConsumer6( CheckedConsumer6<T1,T2,T3,T4,T5,T6> consumer )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 , t6 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 7 consumer 7.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Consumer7<T1,T2,T3,T4,T5,T6,T7> uncheckedConsumer7( CheckedConsumer7<T1,T2,T3,T4,T5,T6,T7> consumer )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 8 consumer 8.
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
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Consumer8<T1,T2,T3,T4,T5,T6,T7,T8> uncheckedConsumer8( CheckedConsumer8<T1,T2,T3,T4,T5,T6,T7,T8> consumer )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 + ", " + t8 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked runnable runnable.
	 *
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the runnable
	 */
	public static Runnable uncheckedRunnable( CheckedRunnable runnable )
		{
		
		
		return () ->
		{
		try
			{
			runnable.run();
			}
		catch( Throwable throwable )
			{
			Throwables.throwIfUnchecked( throwable );
			throw new RuntimeException( throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked supplier supplier.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param supplier
	 * 	the supplier
	 *
	 * @return the supplier
	 */
	public static <E> Supplier<E> uncheckedSupplier( CheckedFunction0<E> supplier )
		{
		
		
		return () ->
		{
		try
			{
			return supplier.apply();
			}
		catch( Throwable throwable )
			{
			Throwables.throwIfUnchecked( throwable );
			throw new RuntimeException( throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,R> uncheckedFunction( CheckedFunction1<T,R> f )
		{
		return ( t ) ->
		{
		try
			{
			return f.apply( t );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked bi function bi function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <U>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 *
	 * @return the bi function
	 */
	public static <T, U, R> BiFunction<T,U,R> uncheckedBiFunction( CheckedBiFunction<T,U,R> f )
		{
		return ( t1 , t2 ) ->
		{
		try
			{
			return f.apply( t1 , t2 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate predicate.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 *
	 * @return the predicate
	 */
	public static <T> Predicate<T> uncheckedPredicate( CheckedPredicate<T> f )
		{
		return ( t ) ->
		{
		try
			{
			return f.test( t );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked bi predicate bi predicate.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 *
	 * @return the bi predicate
	 */
	public static <T1, T2> BiPredicate<T1,T2> uncheckedBiPredicate( CheckedBiPredicate<T1,T2> f )
		{
		return ( t1 , t2 ) ->
		{
		try
			{
			return f.test( t1 , t2 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 3 predicate 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 *
	 * @return the predicate 3
	 */
	public static <T1, T2, T3> Predicate3<T1,T2,T3> uncheckedPredicate3( CheckedPredicate3<T1,T2,T3> f )
		{
		return ( t1 , t2 , t3 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 + ", " + t3 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 4 predicate 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 *
	 * @return the predicate 4
	 */
	public static <T1, T2, T3, T4> Predicate4<T1,T2,T3,T4> uncheckedPredicate4( CheckedPredicate4<T1,T2,T3,T4> f )
		{
		return ( t1 , t2 , t3 , t4 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 + ", " + t3 + ", " + t4 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 5 predicate 5.
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
	 * @param f
	 * 	the f
	 *
	 * @return the predicate 5
	 */
	public static <T1, T2, T3, T4, T5> Predicate5<T1,T2,T3,T4,T5> uncheckedPredicate5( CheckedPredicate5<T1,T2,T3,T4,T5> f )
		{
		return ( t1 , t2 , t3 , t4 , t5 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 6 predicate 6.
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
	 * @param f
	 * 	the f
	 *
	 * @return the predicate 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Predicate6<T1,T2,T3,T4,T5,T6> uncheckedPredicate6( CheckedPredicate6<T1,T2,T3,T4,T5,T6> f )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 , t6 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 7 predicate 7.
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
	 * @param f
	 * 	the f
	 *
	 * @return the predicate 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Predicate7<T1,T2,T3,T4,T5,T6,T7> uncheckedPredicate7( CheckedPredicate7<T1,T2,T3,T4,T5,T6,T7> f )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 8 predicate 8.
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
	 * @param f
	 * 	the f
	 *
	 * @return the predicate 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Predicate8<T1,T2,T3,T4,T5,T6,T7,T8> uncheckedPredicate8( CheckedPredicate8<T1,T2,T3,T4,T5,T6,T7,T8> f )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( "" + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 + ", " + t8 , throwable );
			}
		};
		}
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="unchecked wrappers +err test">
	
	
	
	/**
	 * Unchecked consumer consumer.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the consumer
	 */
	public static <T> Consumer<T> uncheckedConsumer( CheckedConsumer<T> consumer ,
	                                                 String errorMessage )
		{
		return t ->
		{
		try
			{
			consumer.accept( t );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked bi consumer bi consumer.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the bi consumer
	 */
	public static <T1, T2> BiConsumer<T1,T2> uncheckedBiConsumer( CheckedBiConsumer<T1,T2> consumer ,
	                                                              String errorMessage )
		{
		
		return ( t1 , t2 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + ", " + t2 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 3 consumer 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the consumer 3
	 */
	public static <T1, T2, T3> Consumer3<T1,T2,T3> uncheckedConsumer3( CheckedConsumer3<T1,T2,T3> consumer ,
	                                                                   String errorMessage )
		{
		
		return ( t1 , t2 , t3 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + ", " + t2 + ", " + t3 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 4 consumer 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the consumer 4
	 */
	public static <T1, T2, T3, T4> Consumer4<T1,T2,T3,T4> uncheckedConsumer4( CheckedConsumer4<T1,T2,T3,T4> consumer ,
	                                                                          String errorMessage )
		{
		
		return ( t1 , t2 , t3 , t4 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + ", " + t2 + ", " + t3 + ", " + t4 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 5 consumer 5.
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
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the consumer 5
	 */
	public static <T1, T2, T3, T4, T5> Consumer5<T1,T2,T3,T4,T5> uncheckedConsumer5( CheckedConsumer5<T1,T2,T3,T4,T5> consumer ,
	                                                                                 String errorMessage )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 6 consumer 6.
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
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the consumer 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Consumer6<T1,T2,T3,T4,T5,T6> uncheckedConsumer6( CheckedConsumer6<T1,T2,T3,T4,T5,T6> consumer ,
	                                                                                        String errorMessage )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 , t6 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 7 consumer 7.
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
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the consumer 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Consumer7<T1,T2,T3,T4,T5,T6,T7> uncheckedConsumer7( CheckedConsumer7<T1,T2,T3,T4,T5,T6,T7> consumer ,
	                                                                                               String errorMessage )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked consumer 8 consumer 8.
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
	 * @param consumer
	 * 	the consumer
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the consumer 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Consumer8<T1,T2,T3,T4,T5,T6,T7,T8> uncheckedConsumer8( CheckedConsumer8<T1,T2,T3,T4,T5,T6,T7,T8> consumer ,
	                                                                                                      String errorMessage )
		{
		
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ->
		{
		try
			{
			consumer.accept( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + ", " + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 + ", " + t8 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked runnable runnable.
	 *
	 * @param runnable
	 * 	the runnable
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the runnable
	 */
	public static Runnable uncheckedRunnable( CheckedRunnable runnable ,
	                                          String errorMessage )
		{
		
		
		return () ->
		{
		try
			{
			runnable.run();
			}
		catch( Throwable throwable )
			{
			Throwables.throwIfUnchecked( throwable );
			throw new RuntimeException( throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked supplier supplier.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param supplier
	 * 	the supplier
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the supplier
	 */
	public static <E> Supplier<E> uncheckedSupplier( CheckedFunction0<E> supplier ,
	                                                 String errorMessage )
		{
		
		
		return () ->
		{
		try
			{
			return supplier.apply();
			}
		catch( Throwable throwable )
			{
			Throwables.throwIfUnchecked( throwable );
			throw new RuntimeException( throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,R> uncheckedFunction( CheckedFunction1<T,R> f ,
	                                                      String errorMessage )
		{
		return ( t ) ->
		{
		try
			{
			return f.apply( t );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked bi function bi function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <U>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the bi function
	 */
	public static <T, U, R> BiFunction<T,U,R> uncheckedBiFunction( CheckedBiFunction<T,U,R> f ,
	                                                               String errorMessage )
		{
		return ( t1 , t2 ) ->
		{
		try
			{
			return f.apply( t1 , t2 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate predicate.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the predicate
	 */
	public static <T> Predicate<T> uncheckedPredicate( CheckedPredicate<T> f ,
	                                                   String errorMessage )
		{
		return ( t ) ->
		{
		try
			{
			return f.test( t );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked bi predicate bi predicate.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the bi predicate
	 */
	public static <T1, T2> BiPredicate<T1,T2> uncheckedBiPredicate( CheckedBiPredicate<T1,T2> f ,
	                                                                String errorMessage )
		{
		return ( t1 , t2 ) ->
		{
		try
			{
			return f.test( t1 , t2 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 3 predicate 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the predicate 3
	 */
	public static <T1, T2, T3> Predicate3<T1,T2,T3> uncheckedPredicate3( CheckedPredicate3<T1,T2,T3> f ,
	                                                                     String errorMessage )
		{
		return ( t1 , t2 , t3 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 + ", " + t3 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 4 predicate 4.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <T4>
	 * 	the type parameter
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the predicate 4
	 */
	public static <T1, T2, T3, T4> Predicate4<T1,T2,T3,T4> uncheckedPredicate4( CheckedPredicate4<T1,T2,T3,T4> f ,
	                                                                            String errorMessage )
		{
		return ( t1 , t2 , t3 , t4 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 + ", " + t3 + ", " + t4 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 5 predicate 5.
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
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the predicate 5
	 */
	public static <T1, T2, T3, T4, T5> Predicate5<T1,T2,T3,T4,T5> uncheckedPredicate5( CheckedPredicate5<T1,T2,T3,T4,T5> f ,
	                                                                                   String errorMessage )
		{
		return ( t1 , t2 , t3 , t4 , t5 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 6 predicate 6.
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
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the predicate 6
	 */
	public static <T1, T2, T3, T4, T5, T6> Predicate6<T1,T2,T3,T4,T5,T6> uncheckedPredicate6( CheckedPredicate6<T1,T2,T3,T4,T5,T6> f ,
	                                                                                          String errorMessage )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 , t6 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 7 predicate 7.
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
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the predicate 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Predicate7<T1,T2,T3,T4,T5,T6,T7> uncheckedPredicate7( CheckedPredicate7<T1,T2,T3,T4,T5,T6,T7> f ,
	                                                                                                 String errorMessage )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 , t6 , t7 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 , throwable );
			}
		};
		}
	
	
	
	/**
	 * Unchecked predicate 8 predicate 8.
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
	 * @param f
	 * 	the f
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the predicate 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Predicate8<T1,T2,T3,T4,T5,T6,T7,T8> uncheckedPredicate8( CheckedPredicate8<T1,T2,T3,T4,T5,T6,T7,T8> f ,
	                                                                                                        String errorMessage )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ->
		{
		try
			{
			return f.test( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 );
			}
		catch( Throwable throwable )
			{
			throw new RuntimeException( errorMessage + ": " + t1 + "," + t2 + ", " + t3 + ", " + t4 + ", " + t5 + ", " + t6 + ", " + t7 + ", " + t8 , throwable );
			}
		};
		}
	
	
	
	//<editor-fold desc="exception utils">
	
	
	
	/**
	 * Null pointer exception function.
	 *
	 * @param description
	 * 	the description
	 *
	 * @return the function
	 */
	public static Function<? super Throwable,NullPointerException> nullPointerException( String description )
		{
		Objects.requireNonNull( description , "description is null" );
		return ( err ) -> new NullPointerException( description );
		}
	
	
	
	/**
	 * Illegal argument exception function.
	 *
	 * @param description
	 * 	the description
	 *
	 * @return the function
	 */
	public static Function<? super Throwable,IllegalArgumentException> illegalArgumentException( String description )
		{
		Objects.requireNonNull( description , "description is null" );
		return ( err ) -> new IllegalArgumentException( description , err );
		}
	
	
	
	/**
	 * Illegal state exception function.
	 *
	 * @param description
	 * 	the description
	 *
	 * @return the function
	 */
	public static Function<? super Throwable,IllegalStateException> illegalStateException( String description )
		{
		Objects.requireNonNull( description , "description is null" );
		return ( err ) -> new IllegalStateException( description , err );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="try utils">
	
	
	
	/**
	 * Alias for  Try.failure(new NullPointerException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> nullPointerFailure()
		{
		return (Try.Failure<T>) Try.failure( new NullPointerException() );
		}
	
	
	
	/**
	 * Alias for  Try.failure(new NullPointerException(description))
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 * @param description
	 * 	the description
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> nullPointerFailure( String description )
		{
		return (Try.Failure<T>) Try.failure( new NullPointerException( description ) );
		}
	
	
	
	/**
	 * Alias for  Try.failure(new IllegalArgumentException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> illegalArgumentFailure()
		{
		return (Try.Failure<T>) Try.failure( new IllegalArgumentException() );
		}
	
	
	
	/**
	 * Alias for  Try.failure(new IllegalArgumentException(description))
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 * @param description
	 * 	the description
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> illegalArgumentFailure( String description )
		{
		return (Try.Failure<T>) Try.failure( new IllegalArgumentException( description ) );
		}
	
	
	
	/**
	 * Alias for  Try.failure(new IllegalArgumentException(description,cause))
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 * @param description
	 * 	the description
	 * @param cause
	 * 	the cause
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> illegalArgumentFailure( String description ,
	                                                         Throwable cause )
		{
		return (Try.Failure<T>) Try.failure( new IllegalArgumentException( description , cause ) );
		}
	
	
	
	/**
	 * Alias for  Try.failure(new IllegalStateException())
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> illegalStateFailure()
		{
		return (Try.Failure<T>) Try.failure( new IllegalStateException() );
		}
	
	
	
	/**
	 * Alias for  Try.failure(new IllegalStateException(description))
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 * @param description
	 * 	the description
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> illegalStateFailure( String description )
		{
		return (Try.Failure<T>) Try.failure( new IllegalStateException( description ) );
		}
	
	
	
	/**
	 * Alias for  Try.failure(new IllegalStateException(description,cause))
	 *
	 * @param <T>
	 * 	Component type of the {@code Try}.
	 * @param description
	 * 	the description
	 * @param cause
	 * 	the cause
	 *
	 * @return A new {@link Try.Failure}.
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> Try.Failure<T> illegalStateFailure( String description ,
	                                                      Throwable cause )
		{
		return (Try.Failure<T>) Try.failure( new IllegalStateException( description , cause ) );
		}
	
	
	//</editor-fold>
	
	
	
	}



//
//interface CheckedConsumerModule
//	{
//
//	// DEV-NOTE: we do not plan to expose this as public API
//	@SuppressWarnings( "unchecked" )
//	static <T extends Throwable, R> R sneakyThrow( Throwable t )
//	throws T
//		{
//		throw (T) t;
//		}
//
//	}