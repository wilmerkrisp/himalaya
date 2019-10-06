package life.expert.common.async;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.async
//                           wilmer 2019/09/26
//
//--------------------------------------------------------------------------------


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

//import io.reactivex.Scheduler;
//import io.reactivex.functions.Action;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import io.reactivex.schedulers.Schedulers;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
//import io.reactivestreams.functions.Consumer;

import static life.expert.common.async.LogUtilsConstants.*;

public interface PrintUtils
	{
	/**
	 * Print.
	 ** <pre>
	 *    print     (&quot;Hi {}.&quot;, &quot;there&quot;)
	 *  print (&quot;Set {1,2,3} is not equal to {}.&quot;, &quot;1,2&quot;);
	 *  </pre>
	 *
	 * @param format
	 * 	the format
	 * @param arguments
	 * 	the arguments
	 */
	static void print( String format ,
	                   Object... arguments )
		{
		var tuple = Optional.ofNullable( MessageFormatter.arrayFormat( format , arguments ) );
		
		tuple.map( FormattingTuple::getMessage )
		     .ifPresent( System.out::println );
		
		tuple.map( FormattingTuple::getThrowable )
		     .ifPresent( err -> err.printStackTrace( System.out ) );
			
		}
	
	/**
	 * Log at info consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the consumer
	 */
	static <E> Consumer<E> printConsumer( String message )
		{
		return ( o ) ->
		{
		print( FORMAT_IN_ , Thread.currentThread() , message == null ? "printConsumer" : message , o );
		};
		}
	
	/**
	 * Log at info consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	static <E> Consumer<E> printConsumer()
		{
		return printConsumer( null );
		}
	
	/**
	 * Log at info consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the consumer
	 */
	static <E, F> BiConsumer<E,F> printBiConsumer( String message )
		{
		return ( a , b ) ->
		{
		print( FORMAT_IN2_ , Thread.currentThread() , message == null ? "printBiConsumer" : message , a , b );
		};
		}
	
	/**
	 * Log at info consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	static <E, F> BiConsumer<E,F> printBiConsumer()
		{
		return printBiConsumer( null );
		}
	
	/**
	 * Log at info function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the function
	 */
	static <T, R> Function<T,R> printFunction( String message ,
	                                           R returnObject )
		{
		return ( o ) ->
		{
		print( FORMAT_INOUT_ , Thread.currentThread() , message == null ? "printFunction" : message , o , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at info function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the function
	 */
	static <T, R> Function<T,R> printFunction( R returnObject )
		{
		return printFunction( null , returnObject );
		}
	
	/**
	 * Log at info function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <U>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the function
	 */
	static <T, U, R> BiFunction<T,U,R> printBiFunction( String message ,
	                                                    R returnObject )
		{
		return ( a , b ) ->
		{
		print( FORMAT_IN2OUT_ , Thread.currentThread() , message == null ? "printBiFunction" : message , a , b , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at info function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <U>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the function
	 */
	static <T, U, R> BiFunction<T,U,R> printBiFunction( R returnObject )
		{
		return printBiFunction( null , returnObject );
		}
	
	/**
	 * Log at info unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the function
	 */
	static <E> UnaryOperator<E> printUnaryOperator( String message )
		{
		return ( o ) ->
		{
		print( FORMAT_IN_ , Thread.currentThread() , message == null ? "printUnaryOperator" : message , o );
		return o;
		};
		}
	
	/**
	 * Log at info unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the function
	 */
	static <E> UnaryOperator<E> printUnaryOperator()
		{
		return printUnaryOperator( null );
		}
	
	/**
	 * Log at info supplier callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the callable
	 */
	static <E> Supplier<E> printSupplier( String message ,
	                                      E returnObject )
		{
		return () ->
		{
		print( FORMAT_OUT_ , Thread.currentThread() , message == null ? "printSupplier" : message , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at info supplier callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the callable
	 */
	static <E> Supplier<E> printSupplier( E returnObject )
		{
		return printSupplier( null , returnObject );
		}
	
	/**
	 * Log at info runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the action
	 */
	static <E> Runnable printRunnable( String message )
		{
		return () ->
		{
		print( FORMAT_ , Thread.currentThread() , message == null ? "printRunnable" : message );
		};
		}
	
	/**
	 * Log at info runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the action
	 */
	static <E> Runnable printRunnable()
		{
		return printRunnable( null );
		}
	
	/**
	 * Log at info.
	 *
	 * @param message
	 * 	the message
	 */
	static void print( String message )
		{
		print( FORMAT_ , Thread.currentThread() , message == null || message.isBlank() ? "print" : message );
		}
	
	/**
	 * Log at info.
	 */
	static void print()
		{
		print( "" );
		}
	
	/**
	 * Log at info consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	static <E> Consumer<E> printConsumerWrapper( Consumer<E> consumer )
		{
		return ( o ) ->
		{
		print( FORMAT_IN_ , Thread.currentThread() , "printConsumerWrapper" , o );
		consumer.accept( o );
		};
		}
	
	/**
	 * Log at info consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	static <E, F> BiConsumer<E,F> printBiConsumerWrapper( BiConsumer<E,F> consumer )
		{
		return ( a , b ) ->
		{
		print( FORMAT_IN2_ , Thread.currentThread() , "printBiConsumerWrapper" , a , b );
		consumer.accept( a , b );
		};
		}
	
	/**
	 * Log at info function proxy function.
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
	static <T, R> Function<T,R> printFunctionWrapper( Function<T,R> function )
		{
		return ( o ) ->
		{
		print( FORMAT_IN_ , Thread.currentThread() , "printFunctionWrapper" , o );
		return function.apply( o );
		};
		}
	
	/**
	 * Log at info function proxy function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <U>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param function
	 * 	the function
	 *
	 * @return the function
	 */
	static <T, U, R> BiFunction<T,U,R> printBiFunctionWrapper( BiFunction<T,U,R> function )
		{
		return ( a , b ) ->
		{
		print( FORMAT_IN2_ , Thread.currentThread() , "printBiFunctionWrapper" , a , b );
		return function.apply( a , b );
		};
		}
	
	/**
	 * Log at info supplier proxy callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param supplier
	 * 	the supplier
	 *
	 * @return the callable
	 */
	static <E> Supplier<E> printSupplierWrapper( Supplier<E> supplier )
		{
		return () ->
		{
		print( FORMAT_ , Thread.currentThread() , "printSupplierWrapper" );
		return supplier.get();
		};
		}
	
	/**
	 * Log at info runnable proxy action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the action
	 */
	static <E> Runnable printRunnableWrapper( Runnable runnable )
		{
		return () ->
		{
		print( FORMAT_ , Thread.currentThread() , "printRunnableWrapper" );
		runnable.run();
		};
		}
	}
