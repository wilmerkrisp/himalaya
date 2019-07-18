package life.expert.common.function;









import io.vavr.CheckedFunction1;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;



import lombok.NonNull;//@NOTNULL

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.function.Function3;
import reactor.function.Function4;
import reactor.function.Function5;
import reactor.function.Function6;
import reactor.function.Function7;
import reactor.function.Function8;


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
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/10
//
//--------------------------------------------------------------------------------









/**
 * wraps the null value returned by the function into an empty flow event
 */
@UtilityClass
@Slf4j
public final class NullableUtils
	{
	
	
	
	/**
	 * Nullable function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param function
	 * 	the function
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,Mono<R>> nullableFunction( Function<T,R> function )
		{
		//return t -> Mono.fromSupplier( () -> function.apply( t ) );
		return t -> fromSupplier( () -> function.apply( t ) );
		}
	
	
	
	/**
	 * Nullable bi function bi function.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param function
	 * 	the function
	 *
	 * @return the bi function
	 */
	public static <T1, T2, R> BiFunction<T1,T2,Mono<R>> nullableBiFunction( BiFunction<T1,T2,R> function )
		{
		return ( t1 , t2 ) -> fromSupplier( () -> function.apply( t1 , t2 ) );
		}
	
	
	
	/**
	 * Nullable function 3 function 3.
	 *
	 * @param <T1>
	 * 	the type parameter
	 * @param <T2>
	 * 	the type parameter
	 * @param <T3>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param function
	 * 	the function
	 *
	 * @return the function 3
	 */
	public static <T1, T2, T3, R> Function3<T1,T2,T3,Mono<R>> nullableFunction3( Function3<T1,T2,T3,R> function )
		{
		return ( t1 , t2 , t3 ) -> fromSupplier( () -> function.apply( t1 , t2 , t3 ) );
		}
	
	
	
	/**
	 * Nullable function 4 function 4.
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
	 * @param function
	 * 	the function
	 *
	 * @return the function 4
	 */
	public static <T1, T2, T3, T4, R> Function4<T1,T2,T3,T4,Mono<R>> nullableFunction4( Function4<T1,T2,T3,T4,R> function )
		{
		return ( t1 , t2 , t3 , t4 ) -> fromSupplier( () -> function.apply( t1 , t2 , t3 , t4 ) );
		}
	
	
	
	/**
	 * Nullable function 5 function 5.
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
	 * @param function
	 * 	the function
	 *
	 * @return the function 5
	 */
	public static <T1, T2, T3, T4, T5, R> Function5<T1,T2,T3,T4,T5,Mono<R>> nullableFunction5( Function5<T1,T2,T3,T4,T5,R> function )
		{
		return ( t1 , t2 , t3 , t4 , t5 ) -> fromSupplier( () -> function.apply( t1 , t2 , t3 , t4 , t5 ) );
		}
	
	
	
	/**
	 * Nullable function 6 function 6.
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
	 * @param function
	 * 	the function
	 *
	 * @return the function 6
	 */
	public static <T1, T2, T3, T4, T5, T6, R> Function6<T1,T2,T3,T4,T5,T6,Mono<R>> nullableFunction6( Function6<T1,T2,T3,T4,T5,T6,R> function )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 ) -> fromSupplier( () -> function.apply( t1 , t2 , t3 , t4 , t5 , t6 ) );
		}
	
	
	
	/**
	 * Nullable function 7 function 7.
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
	 * @param function
	 * 	the function
	 *
	 * @return the function 7
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, R> Function7<T1,T2,T3,T4,T5,T6,T7,Mono<R>> nullableFunction7( Function7<T1,T2,T3,T4,T5,T6,T7,R> function )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) -> fromSupplier( () -> function.apply( t1 , t2 , t3 , t4 , t5 , t6 , t7 ) );
		}
	
	
	
	/**
	 * Nullable function 8 function 8.
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
	 * @param function
	 * 	the function
	 *
	 * @return the function 8
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function8<T1,T2,T3,T4,T5,T6,T7,T8,Mono<R>> nullableFunction8( Function8<T1,T2,T3,T4,T5,T6,T7,T8,R> function )
		{
		return ( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) -> fromSupplier( () -> function.apply( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) );
		}
	
	
	
	/**
	 * Nullable supplier supplier.
	 *
	 * @param <R>
	 * 	the type parameter
	 * @param supplier
	 * 	the supplier
	 *
	 * @return the supplier
	 */
	public static <R> Supplier<Mono<R>> nullableSupplier( Supplier<R> supplier )
		{
		return () -> fromSupplier( supplier::get );
		
		}
		
		
	}
