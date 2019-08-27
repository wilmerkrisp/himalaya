package life.expert.common.function;
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

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.Tuple5;
import io.vavr.Tuple6;
import io.vavr.Tuple7;
import io.vavr.Tuple8;

import reactor.function.*;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <pre>
 *  Hepler functions for  conversions Vavr's tuple-result into (a,b..)-result
 *  Utils is insired by  TupleUtils from io.projectreactor.addons:reactor-extra
 *  Unlike the library, they allow you to contain null values, which allows you to take input values of methods
 *  and check them later with the Flux.error event (not raise exception as in reactor-extra  )
 *
 * Preconditions: none
 * Postconditions: none
 * Side effects: none
 * Tread safety:  Immutable
 *
 * </pre>
 */
public class TupleUtils
	{
	/**
	 * Returns a {@link Consumer} of {@link Tuple2} that wraps a consumer of the component values of the tuple
	 *
	 * @param consumer
	 * 	the component value consumer
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 *
	 * @return the wrapper consumer
	 */
	public static <T1, T2> Consumer<Tuple2<T1,T2>> consumer( BiConsumer<T1,T2> consumer )
		{
		return tuple -> consumer.accept( tuple._1 , tuple._2 );
		}
	
	/**
	 * Returns a {@link Consumer} of {@link Tuple3} that wraps a consumer of the component values of the tuple
	 *
	 * @param consumer
	 * 	the component value consumer
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 *
	 * @return the wrapper consumer
	 */
	public static <T1, T2, T3> Consumer<Tuple3<T1,T2,T3>> consumer( Consumer3<T1,T2,T3> consumer )
		{
		return tuple -> consumer.accept( tuple._1 , tuple._2 , tuple._3 );
		}
	
	/**
	 * Returns a {@link Consumer} of {@link Tuple4} that wraps a consumer of the component values of the tuple
	 *
	 * @param consumer
	 * 	the component value consumer
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 *
	 * @return the wrapper consumer
	 */
	public static <T1, T2, T3, T4> Consumer<Tuple4<T1,T2,T3,T4>> consumer( Consumer4<T1,T2,T3,T4> consumer )
		{
		return tuple -> consumer.accept( tuple._1 , tuple._2 , tuple._3 , tuple._4 );
		}
	
	/**
	 * Returns a {@link Consumer} of {@link Tuple5} that wraps a consumer of the component values of the tuple
	 *
	 * @param consumer
	 * 	the component value consumer
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 *
	 * @return the wrapper consumer
	 */
	public static <T1, T2, T3, T4, T5> Consumer<Tuple5<T1,T2,T3,T4,T5>> consumer( Consumer5<T1,T2,T3,T4,T5> consumer )
		{
		return tuple -> consumer.accept( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 );
		}
	
	/**
	 * Returns a {@link Consumer} of {@link Tuple6} that wraps a consumer of the component values of the tuple
	 *
	 * @param consumer
	 * 	the component value consumer
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 *
	 * @return the wrapper consumer
	 */
	public static <T1, T2, T3, T4, T5, T6> Consumer<Tuple6<T1,T2,T3,T4,T5,T6>> consumer( Consumer6<T1,T2,T3,T4,T5,T6> consumer )
		{
		return tuple -> consumer.accept( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 );
		}
	
	/**
	 * Returns a {@link Consumer} of {@link Tuple7} that wraps a consumer of the component values of the tuple
	 *
	 * @param consumer
	 * 	the component value consumer
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 * @param <T7>
	 * 	the type of the seventh value
	 *
	 * @return the wrapper consumer
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Consumer<Tuple7<T1,T2,T3,T4,T5,T6,T7>> consumer( Consumer7<T1,T2,T3,T4,T5,T6,T7> consumer )
		{
		return tuple -> consumer.accept( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 , tuple._7 );
		}
	
	/**
	 * Returns a {@link Consumer} of {@link Tuple8} that wraps a consumer of the component values of the tuple
	 *
	 * @param consumer
	 * 	the component value consumer
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 * @param <T7>
	 * 	the type of the seventh value
	 * @param <T8>
	 * 	the type of the eighth value
	 *
	 * @return the wrapper consumer
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Consumer<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>> consumer( Consumer8<T1,T2,T3,T4,T5,T6,T7,T8> consumer )
		{
		return tuple -> consumer.accept( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 , tuple._7 , tuple._8 );
		}
	
	/**
	 * Returns a {@link Function} of {@link Tuple2} that wraps a function of the component values of the tuple
	 *
	 * @param function
	 * 	the component value function
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <R>
	 * 	the type of the result of the function
	 *
	 * @return the wrapper function
	 */
	public static <T1, T2, R> Function<Tuple2<T1,T2>,R> function( BiFunction<T1,T2,R> function )
		{
		return tuple -> function.apply( tuple._1 , tuple._2 );
		}
	
	/**
	 * Returns a {@link Function} of {@link Tuple3} that wraps a function of the component values of the tuple
	 *
	 * @param function
	 * 	the component value function
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <R>
	 * 	the type of the result of the function
	 *
	 * @return the wrapper function
	 */
	public static <T1, T2, T3, R> Function<Tuple3<T1,T2,T3>,R> function( Function3<T1,T2,T3,R> function )
		{
		return tuple -> function.apply( tuple._1 , tuple._2 , tuple._3 );
		}
	
	/**
	 * Returns a {@link Function} of {@link Tuple4} that wraps a function of the component values of the tuple
	 *
	 * @param function
	 * 	the component value function
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <R>
	 * 	the type of the result of the function
	 *
	 * @return the wrapper function
	 */
	public static <T1, T2, T3, T4, R> Function<Tuple4<T1,T2,T3,T4>,R> function( Function4<T1,T2,T3,T4,R> function )
		{
		return tuple -> function.apply( tuple._1 , tuple._2 , tuple._3 , tuple._4 );
		}
	
	/**
	 * Returns a {@link Function} of {@link Tuple5} that wraps a function of the component values of the tuple
	 *
	 * @param function
	 * 	the component value function
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <R>
	 * 	the type of the result of the function
	 *
	 * @return the wrapper function
	 */
	public static <T1, T2, T3, T4, T5, R> Function<Tuple5<T1,T2,T3,T4,T5>,R> function( Function5<T1,T2,T3,T4,T5,R> function )
		{
		return tuple -> function.apply( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 );
		}
	
	/**
	 * Returns a {@link Function} of {@link Tuple6} that wraps a function of the component values of the tuple
	 *
	 * @param function
	 * 	the component value function
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 * @param <R>
	 * 	the type of the result of the function
	 *
	 * @return the wrapper function
	 */
	public static <T1, T2, T3, T4, T5, T6, R> Function<Tuple6<T1,T2,T3,T4,T5,T6>,R> function( Function6<T1,T2,T3,T4,T5,T6,R> function )
		{
		return tuple -> function.apply( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 );
		}
	
	/**
	 * Returns a {@link Function} of {@link Tuple7} that wraps a function of the component values of the tuple
	 *
	 * @param function
	 * 	the component value function
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 * @param <T7>
	 * 	the type of the seventh value
	 * @param <R>
	 * 	the type of the result of the function
	 *
	 * @return the wrapper function
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, R> Function<Tuple7<T1,T2,T3,T4,T5,T6,T7>,R> function( Function7<T1,T2,T3,T4,T5,T6,T7,R> function )
		{
		return tuple -> function.apply( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 , tuple._7 );
		}
	
	/**
	 * Returns a {@link Function} of {@link Tuple8} that wraps a function of the component values of the tuple
	 *
	 * @param function
	 * 	the component value function
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 * @param <T7>
	 * 	the type of the seventh value
	 * @param <T8>
	 * 	the type of the eighth value
	 * @param <R>
	 * 	the type of the result of the function
	 *
	 * @return the wrapper function
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>,R> function( Function8<T1,T2,T3,T4,T5,T6,T7,T8,R> function )
		{
		return tuple -> function.apply( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 , tuple._7 , tuple._8 );
		}
	
	/**
	 * Returns a {@link Predicate} of {@link Tuple2} that wraps a predicate of the component values of the tuple
	 *
	 * @param predicate
	 * 	the component value predicate
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 *
	 * @return the wrapper predicate
	 */
	public static <T1, T2> Predicate<Tuple2<T1,T2>> predicate( BiPredicate<T1,T2> predicate )
		{
		return tuple -> predicate.test( tuple._1 , tuple._2 );
		}
	
	/**
	 * Returns a {@link Predicate} of {@link Tuple3} that wraps a predicate of the component values of the tuple
	 *
	 * @param predicate
	 * 	the component value predicate
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 *
	 * @return the wrapper predicate
	 */
	public static <T1, T2, T3> Predicate<Tuple3<T1,T2,T3>> predicate( Predicate3<T1,T2,T3> predicate )
		{
		return tuple -> predicate.test( tuple._1 , tuple._2 , tuple._3 );
		}
	
	/**
	 * Returns a {@link Predicate} of {@link Tuple4} that wraps a predicate of the component values of the tuple
	 *
	 * @param predicate
	 * 	the component value predicate
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 *
	 * @return the wrapper predicate
	 */
	public static <T1, T2, T3, T4> Predicate<Tuple4<T1,T2,T3,T4>> predicate( Predicate4<T1,T2,T3,T4> predicate )
		{
		return tuple -> predicate.test( tuple._1 , tuple._2 , tuple._3 , tuple._4 );
		}
	
	/**
	 * Returns a {@link Predicate} of {@link Tuple5} that wraps a predicate of the component values of the tuple
	 *
	 * @param predicate
	 * 	the component value predicate
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 *
	 * @return the wrapper predicate
	 */
	public static <T1, T2, T3, T4, T5> Predicate<Tuple5<T1,T2,T3,T4,T5>> predicate( Predicate5<T1,T2,T3,T4,T5> predicate )
		{
		return tuple -> predicate.test( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 );
		}
	
	/**
	 * Returns a {@link Predicate} of {@link Tuple6} that wraps a predicate of the component values of the tuple
	 *
	 * @param predicate
	 * 	the component value predicate
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 *
	 * @return the wrapper predicate
	 */
	public static <T1, T2, T3, T4, T5, T6> Predicate<Tuple6<T1,T2,T3,T4,T5,T6>> predicate( Predicate6<T1,T2,T3,T4,T5,T6> predicate )
		{
		return tuple -> predicate.test( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 );
		}
	
	/**
	 * Returns a {@link Predicate} of {@link Tuple7} that wraps a predicate of the component values of the tuple
	 *
	 * @param predicate
	 * 	the component value predicate
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 * @param <T7>
	 * 	the type of the seventh value
	 *
	 * @return the wrapper predicate
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Predicate<Tuple7<T1,T2,T3,T4,T5,T6,T7>> predicate( Predicate7<T1,T2,T3,T4,T5,T6,T7> predicate )
		{
		return tuple -> predicate.test( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 , tuple._7 );
		}
	
	/**
	 * Returns a {@link Predicate} of {@link Tuple8} that wraps a predicate of the component values of the tuple
	 *
	 * @param predicate
	 * 	the component value predicate
	 * @param <T1>
	 * 	the type of the first value
	 * @param <T2>
	 * 	the type of the second value
	 * @param <T3>
	 * 	the type of the third value
	 * @param <T4>
	 * 	the type of the fourth value
	 * @param <T5>
	 * 	the type of the fifth value
	 * @param <T6>
	 * 	the type of the sixth value
	 * @param <T7>
	 * 	the type of the seventh value
	 * @param <T8>
	 * 	the type of the eighth value
	 *
	 * @return the wrapper predicate
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Predicate<Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>> predicate( Predicate8<T1,T2,T3,T4,T5,T6,T7,T8> predicate )
		{
		return tuple -> predicate.test( tuple._1 , tuple._2 , tuple._3 , tuple._4 , tuple._5 , tuple._6 , tuple._7 , tuple._8 );
		}
		
	}
