package life.expert.common.reactivestreams;



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

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case
import static io.vavr.Patterns.*;                         //switch - case - success/failure
import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/18
//
//--------------------------------------------------------------------------------

/**
 * auxiliary static functions with arguments - several Mono
 *
 * - functional for-comprehension pattern for reactive flows
 * - required to convert a null value returned by a function to an empty flow event
 * - at the first null value returned, the chain of nested calls stops
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
	
	//<editor-fold desc="For-comprehension">
	
	//	/**
	//	 * A shortcut for {@code  ts).flatMap(f)} which allows us to write real for-comprehensions using
	//	 * {@code For(...).yield(...)}.
	//	 * <p>
	//	 * Example:
	//	 * <pre><code>
	//	 * For(getPersons(), person -&gt;
	//	 *     For(person.getTweets(), tweet -&gt;
	//	 *         For(tweet.getReplies())
	//	 *             .yield(reply -&gt; person + ", " + tweet + ", " + reply)));
	//	 * </code></pre>
	//	 *
	//	 * @param <T>
	//	 * 	element type of {@code ts}
	//	 * @param <U>
	//	 * 	component type of the resulting {@code Mono}
	//	 * @param ts
	//	 * 	An  Mono
	//	 * @param f
	//	 * 	A function {@code T -> Mono<U>}
	//	 *
	//	 * @return A new Mono
	//	 */
	//	public static <T, U> Mono<U> For( Mono<T> ts ,
	//	                                  Function<? super T,? extends Mono<U>> f )
	//		{
	//		return ts.flatMap( f );
	//		}
	
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
	public static <T1> For1<T1> For( Mono<T1> ts1 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		return new For1<>( ts1 );
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
	public static <T1, T2> For2<T1,T2> For( Mono<T1> ts1 ,
	                                        Mono<T2> ts2 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		Objects.requireNonNull( ts2 , "ts2 is null" );
		return new For2<>( ts1 , ts2 );
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
	public static <T1, T2, T3> For3<T1,T2,T3> For( Mono<T1> ts1 ,
	                                               Mono<T2> ts2 ,
	                                               Mono<T3> ts3 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		Objects.requireNonNull( ts2 , "ts2 is null" );
		Objects.requireNonNull( ts3 , "ts3 is null" );
		return new For3<>( ts1 , ts2 , ts3 );
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
	public static <T1, T2, T3, T4> For4<T1,T2,T3,T4> For( Mono<T1> ts1 ,
	                                                      Mono<T2> ts2 ,
	                                                      Mono<T3> ts3 ,
	                                                      Mono<T4> ts4 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		Objects.requireNonNull( ts2 , "ts2 is null" );
		Objects.requireNonNull( ts3 , "ts3 is null" );
		Objects.requireNonNull( ts4 , "ts4 is null" );
		return new For4<>( ts1 , ts2 , ts3 , ts4 );
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
	public static <T1, T2, T3, T4, T5> For5<T1,T2,T3,T4,T5> For( Mono<T1> ts1 ,
	                                                             Mono<T2> ts2 ,
	                                                             Mono<T3> ts3 ,
	                                                             Mono<T4> ts4 ,
	                                                             Mono<T5> ts5 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		Objects.requireNonNull( ts2 , "ts2 is null" );
		Objects.requireNonNull( ts3 , "ts3 is null" );
		Objects.requireNonNull( ts4 , "ts4 is null" );
		Objects.requireNonNull( ts5 , "ts5 is null" );
		return new For5<>( ts1 , ts2 , ts3 , ts4 , ts5 );
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
	public static <T1, T2, T3, T4, T5, T6> For6<T1,T2,T3,T4,T5,T6> For( Mono<T1> ts1 ,
	                                                                    Mono<T2> ts2 ,
	                                                                    Mono<T3> ts3 ,
	                                                                    Mono<T4> ts4 ,
	                                                                    Mono<T5> ts5 ,
	                                                                    Mono<T6> ts6 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		Objects.requireNonNull( ts2 , "ts2 is null" );
		Objects.requireNonNull( ts3 , "ts3 is null" );
		Objects.requireNonNull( ts4 , "ts4 is null" );
		Objects.requireNonNull( ts5 , "ts5 is null" );
		Objects.requireNonNull( ts6 , "ts6 is null" );
		return new For6<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 );
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
	public static <T1, T2, T3, T4, T5, T6, T7> For7<T1,T2,T3,T4,T5,T6,T7> For( Mono<T1> ts1 ,
	                                                                           Mono<T2> ts2 ,
	                                                                           Mono<T3> ts3 ,
	                                                                           Mono<T4> ts4 ,
	                                                                           Mono<T5> ts5 ,
	                                                                           Mono<T6> ts6 ,
	                                                                           Mono<T7> ts7 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		Objects.requireNonNull( ts2 , "ts2 is null" );
		Objects.requireNonNull( ts3 , "ts3 is null" );
		Objects.requireNonNull( ts4 , "ts4 is null" );
		Objects.requireNonNull( ts5 , "ts5 is null" );
		Objects.requireNonNull( ts6 , "ts6 is null" );
		Objects.requireNonNull( ts7 , "ts7 is null" );
		return new For7<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 , ts7 );
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
	public static <T1, T2, T3, T4, T5, T6, T7, T8> For8<T1,T2,T3,T4,T5,T6,T7,T8> For( Mono<T1> ts1 ,
	                                                                                  Mono<T2> ts2 ,
	                                                                                  Mono<T3> ts3 ,
	                                                                                  Mono<T4> ts4 ,
	                                                                                  Mono<T5> ts5 ,
	                                                                                  Mono<T6> ts6 ,
	                                                                                  Mono<T7> ts7 ,
	                                                                                  Mono<T8> ts8 )
		{
		Objects.requireNonNull( ts1 , "ts1 is null" );
		Objects.requireNonNull( ts2 , "ts2 is null" );
		Objects.requireNonNull( ts3 , "ts3 is null" );
		Objects.requireNonNull( ts4 , "ts4 is null" );
		Objects.requireNonNull( ts5 , "ts5 is null" );
		Objects.requireNonNull( ts6 , "ts6 is null" );
		Objects.requireNonNull( ts7 , "ts7 is null" );
		Objects.requireNonNull( ts8 , "ts8 is null" );
		return new For8<>( ts1 , ts2 , ts3 , ts4 , ts5 , ts6 , ts7 , ts8 );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="For-comprehension helper classes">
	
	/**
	 * For-comprehension with one Mono.
	 *
	 * @param <T1>
	 * 	the type parameter
	 */
	public static class For1<T1>
		{
		
		private final Mono<T1> ts1;
		
		private For1( Mono<T1> ts1 )
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
			Objects.requireNonNull( f , "f is null" );
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
	public static class For2<T1, T2>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private For2( Mono<T1> ts1 ,
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
			Objects.requireNonNull( f , "f is null" );
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
	public static class For3<T1, T2, T3>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private For3( Mono<T1> ts1 ,
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
			Objects.requireNonNull( f , "f is null" );
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
	public static class For4<T1, T2, T3, T4>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private For4( Mono<T1> ts1 ,
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
			Objects.requireNonNull( f , "f is null" );
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
	public static class For5<T1, T2, T3, T4, T5>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private For5( Mono<T1> ts1 ,
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
			Objects.requireNonNull( f , "f is null" );
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
	public static class For6<T1, T2, T3, T4, T5, T6>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private final Mono<T6> ts6;
		
		private For6( Mono<T1> ts1 ,
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
			Objects.requireNonNull( f , "f is null" );
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
	public static class For7<T1, T2, T3, T4, T5, T6, T7>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private final Mono<T6> ts6;
		
		private final Mono<T7> ts7;
		
		private For7( Mono<T1> ts1 ,
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
			Objects.requireNonNull( f , "f is null" );
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
	public static class For8<T1, T2, T3, T4, T5, T6, T7, T8>
		{
		
		private final Mono<T1> ts1;
		
		private final Mono<T2> ts2;
		
		private final Mono<T3> ts3;
		
		private final Mono<T4> ts4;
		
		private final Mono<T5> ts5;
		
		private final Mono<T6> ts6;
		
		private final Mono<T7> ts7;
		
		private final Mono<T8> ts8;
		
		private For8( Mono<T1> ts1 ,
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
			Objects.requireNonNull( f , "f is null" );
			return ts1.flatMap( t1 -> ts2.flatMap( t2 -> ts3.flatMap( t3 -> ts4.flatMap( t4 -> ts5.flatMap( t5 -> ts6.flatMap( t6 -> ts7.flatMap( t7 -> ts8.map( t8 -> f.apply( t1 , t2 , t3 , t4 , t5 , t6 , t7 , t8 ) ) ) ) ) ) ) ) );
			}
			
		}
	//</editor-fold>
	
	}


 