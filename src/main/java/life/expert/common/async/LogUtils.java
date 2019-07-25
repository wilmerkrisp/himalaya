package life.expert.common.async;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.utils.reactivestreams
//                           wilmer 2019/03/05
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

/**
 * The type Log utils.
 *
 * import static life.expert.utils.async.LogUtils.*;
 */
public class LogUtils
	{
	
	private static final Logger logger_ = LoggerFactory.getLogger( LogUtils.class );
	
	/**
	 * Log.
	 *
	 * @param format
	 * 	the format
	 * @param arguments
	 * 	the arguments
	 */
	public static final void log( String format ,
	                              Object... arguments )
		{
		logger_.info( format , arguments );
		}
	
	/**
	 * Print.
	 *
	 * @param format
	 * 	the format
	 * @param arguments
	 * 	the arguments
	 */
	public static final void print( String format ,
	                                Object... arguments )
		{
		var tuple = Optional.ofNullable( MessageFormatter.arrayFormat( format , arguments ) );
		
		tuple.map( FormattingTuple::getMessage )
		     .ifPresent( msg -> System.out.println( msg ) );
		
		tuple.map( FormattingTuple::getThrowable )
		     .ifPresent( err -> err.printStackTrace( System.out ) );
			
		}
	
	/**
	 * Log at warning.
	 *
	 * @param format
	 * 	the format
	 * @param arguments
	 * 	the arguments
	 */
	public static final void logAtWarning( String format ,
	                                       Object... arguments )
		{
		logger_.warn( format , arguments );
		}
	
	/**
	 * Log at error.
	 *
	 * @param format
	 * 	the format
	 * @param arguments
	 * 	the arguments
	 */
	public static final void logAtError( String format ,
	                                     Object... arguments )
		{
		logger_.error( format , arguments );
		}
	
	/**
	 * Log at debug.
	 *
	 * @param format
	 * 	the format
	 * @param arguments
	 * 	the arguments
	 */
	public static final void logAtDebug_( String format ,
	                                      Object... arguments )
		{
		logger_.debug( format , arguments );
		}
	
	private static final long DEFAULT_DELAY_ = 1;
	
	private static final String FORMAT_ = "{}   {}";
	
	private static final String FORMAT_DELAY_ = "{}   {} delay({})";
	
	private static final String FORMAT_IN_ = "{}   {} in({})";
	
	private static final String FORMAT_IN2_ = "{}   {} in({}) in({})";
	
	private static final String FORMAT_IN_DELAY_ = "{}   {} in({}) delay({})";
	
	private static final String FORMAT_IN2_DELAY_ = "{}   {} in({}) in({}) delay({})";
	
	private static final String FORMAT_OUT_ = "{}   {} out({})";
	
	private static final String FORMAT_OUT_DELAY_ = "{}   {} out({}) delay({})";
	
	private static final String FORMAT_INOUT_ = "{}   {} in({}) out({})";
	
	private static final String FORMAT_IN2OUT_ = "{}   {} in({}) in({}) out({})";
	
	private static final String FORMAT_INOUT_DELAY_ = "{}   {} in({}) out({}) delay({})";
	
	private static final String FORMAT_IN2OUT_DELAY_ = "{}   {} in({}) in({}) out({}) delay({})";
	
	//<editor-fold desc="print">
	
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
	public static <E> Consumer<E> printConsumer( String message )
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
	public static <E> Consumer<E> printConsumer()
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
	public static <E, F> BiConsumer<E,F> printBiConsumer( String message )
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
	public static <E, F> BiConsumer<E,F> printBiConsumer()
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
	public static <T, R> Function<T,R> printFunction( String message ,
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
	public static <T, R> Function<T,R> printFunction( R returnObject )
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
	public static <T, U, R> BiFunction<T,U,R> printBiFunction( String message ,
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
	public static <T, U, R> BiFunction<T,U,R> printBiFunction( R returnObject )
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
	public static <E> UnaryOperator<E> printUnaryOperator( String message )
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
	public static <E> UnaryOperator<E> printUnaryOperator()
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
	public static <E> Supplier<E> printSupplier( String message ,
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
	public static <E> Supplier<E> printSupplier( E returnObject )
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
	public static <E> Runnable printRunnable( String message )
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
	public static <E> Runnable printRunnable()
		{
		return printRunnable( null );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="print decorator">
	/**
	 * Log at info consumer proxy consumer.
	 *
	 * @param <E>
	 *  the type parameter
	 * @param consumer
	 *  the consumer
	 *
	 * @return the consumer
	 */
	
	/**
	 * Log at info.
	 *
	 * @param message
	 * 	the message
	 */
	public static void print( String message )
		{
		print( FORMAT_ , Thread.currentThread() , message == null || message.isBlank() ? "print" : message );
		}
	
	/**
	 * Log at info.
	 */
	public static void print()
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
	public static <E> Consumer<E> printConsumerWrapper( Consumer<E> consumer )
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
	public static <E, F> BiConsumer<E,F> printBiConsumerWrapper( BiConsumer<E,F> consumer )
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
	public static <T, R> Function<T,R> printFunctionWrapper( Function<T,R> function )
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
	public static <T, U, R> BiFunction<T,U,R> printBiFunctionWrapper( BiFunction<T,U,R> function )
		{
		return ( a , b ) ->
		{
		print( FORMAT_IN2_ , Thread.currentThread() , "printBiFunctionWrapper" , a , b );
		return function.apply( a , b );
		};
		}
	
	//  /**
	//   * Log at info unary operator proxy function.
	//   *
	//   * @param <E>
	//   *  the type parameter
	//   * @param operator
	//   *  the operator
	//   *
	//   * @return the function
	//   */
	//  public static <E> Function<E,E> print( Function<E,E> operator )
	//    {
	//    String template = "[{}]   printUnaryOperatorProxy in({})";
	//
	//    return ( o ) ->
	//    {
	//    logger_.atInfo()
	//           .log(   template , o , Thread.currentThread() ) );
	//    return operator.apply( o );
	//    };
	//    }
	
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
	public static <E> Supplier<E> printSupplierWrapper( Supplier<E> supplier )
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
	public static <E> Runnable printRunnableWrapper( Runnable runnable )
		{
		return () ->
		{
		print( FORMAT_ , Thread.currentThread() , "printRunnableWrapper" );
		runnable.run();
		};
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtInfo">
	
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
	public static <E> Consumer<E> logAtInfoConsumer( String message )
		{
		return ( o ) ->
		{
		log( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtInfoConsumer" : message , o );
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
	public static <E> Consumer<E> logAtInfoConsumer()
		{
		return logAtInfoConsumer( null );
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
	public static <E, F> BiConsumer<E,F> logAtInfoBiConsumer( String message )
		{
		return ( a , b ) ->
		{
		log( FORMAT_IN2_ , Thread.currentThread() , message == null ? "logAtInfoBiConsumer" : message , a , b );
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
	public static <E, F> BiConsumer<E,F> logAtInfoBiConsumer()
		{
		return logAtInfoBiConsumer( null );
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
	public static <T, R> Function<T,R> logAtInfoFunction( String message ,
	                                                      R returnObject )
		{
		return ( o ) ->
		{
		log( FORMAT_INOUT_ , Thread.currentThread() , message == null ? "logAtInfoFunction" : message , o , returnObject );
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
	public static <T, R> Function<T,R> logAtInfoFunction( R returnObject )
		{
		return logAtInfoFunction( null , returnObject );
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
	public static <T, U, R> BiFunction<T,U,R> logAtInfoBiFunction( String message ,
	                                                               R returnObject )
		{
		return ( a , b ) ->
		{
		log( FORMAT_IN2OUT_ , Thread.currentThread() , message == null ? "logAtInfoBiFunction" : message , a , b , returnObject );
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
	public static <T, U, R> BiFunction<T,U,R> logAtInfoBiFunction( R returnObject )
		{
		return logAtInfoBiFunction( null , returnObject );
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
	public static <E> UnaryOperator<E> logAtInfoUnaryOperator( String message )
		{
		return ( o ) ->
		{
		log( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtInfoUnaryOperator" : message , o );
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
	public static <E> UnaryOperator<E> logAtInfoUnaryOperator()
		{
		return logAtInfoUnaryOperator( null );
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
	public static <E> Supplier<E> logAtInfoSupplier( String message ,
	                                                 E returnObject )
		{
		return () ->
		{
		log( FORMAT_OUT_ , Thread.currentThread() , message == null ? "logAtInfoSupplier" : message , returnObject );
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
	public static <E> Supplier<E> logAtInfoSupplier( E returnObject )
		{
		return logAtInfoSupplier( null , returnObject );
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
	public static <E> Runnable logAtInfoRunnable( String message )
		{
		return () ->
		{
		log( FORMAT_ , Thread.currentThread() , message == null ? "logAtInfoRunnable" : message );
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
	public static <E> Runnable logAtInfoRunnable()
		{
		return logAtInfoRunnable( null );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtInfo decorator">
	/**
	 * Log at info consumer proxy consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	
	/**
	 * Log at info.
	 *
	 * @param message
	 * 	the message
	 */
	public static void logAtInfo( String message )
		{
		log( FORMAT_ , Thread.currentThread() , message == null || message.isBlank() ? "logAtInfo" : message );
		}
	
	/**
	 * Log at info.
	 */
	public static void logAtInfo()
		{
		logAtInfo( "" );
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
	public static <E> Consumer<E> logAtInfoConsumerWrapper( Consumer<E> consumer )
		{
		return ( o ) ->
		{
		log( FORMAT_IN_ , Thread.currentThread() , "logAtInfoConsumerWrapper" , o );
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
	public static <E, F> BiConsumer<E,F> logAtInfoBiConsumerWrapper( BiConsumer<E,F> consumer )
		{
		return ( a , b ) ->
		{
		log( FORMAT_IN2_ , Thread.currentThread() , "logAtInfoBiConsumerWrapper" , a , b );
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
	public static <T, R> Function<T,R> logAtInfoFunctionWrapper( Function<T,R> function )
		{
		return ( o ) ->
		{
		log( FORMAT_IN_ , Thread.currentThread() , "logAtInfoFunctionWrapper" , o );
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
	public static <T, U, R> BiFunction<T,U,R> logAtInfoBiFunctionWrapper( BiFunction<T,U,R> function )
		{
		return ( a , b ) ->
		{
		log( FORMAT_IN2_ , Thread.currentThread() , "logAtInfoBiFunctionWrapper" , a , b );
		return function.apply( a , b );
		};
		}
	
	//	/**
	//	 * Log at info unary operator proxy function.
	//	 *
	//	 * @param <E>
	//	 * 	the type parameter
	//	 * @param operator
	//	 * 	the operator
	//	 *
	//	 * @return the function
	//	 */
	//	public static <E> Function<E,E> logAtInfo( Function<E,E> operator )
	//		{
	//		String template = "[{}]   logAtInfoUnaryOperatorProxy in({})";
	//
	//		return ( o ) ->
	//		{
	//		logger_.atInfo()
	//		       .log(   template , o , Thread.currentThread() ) );
	//		return operator.apply( o );
	//		};
	//		}
	
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
	public static <E> Supplier<E> logAtInfoSupplierWrapper( Supplier<E> supplier )
		{
		return () ->
		{
		log( FORMAT_ , Thread.currentThread() , "logAtInfoSupplierWrapper" );
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
	public static <E> Runnable logAtInfoRunnableWrapper( Runnable runnable )
		{
		return () ->
		{
		log( FORMAT_ , Thread.currentThread() , "logAtInfoRunnableWrapper" );
		runnable.run();
		};
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtError">
	
	/**
	 * Log at error consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtErrorConsumer( String message )
		{
		return ( o ) ->
		{
		logAtError( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtErrorConsumer" : message , o );
		};
		}
	
	/**
	 * Log at error consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtErrorConsumer()
		{
		return logAtErrorConsumer( null );
		}
	
	/**
	 * Log at error consumer consumer.
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
	public static <E, F> BiConsumer<E,F> logAtErrorBiConsumer( String message )
		{
		return ( a , b ) ->
		{
		logAtError( FORMAT_IN2_ , Thread.currentThread() , message == null ? "logAtErrorBiConsumer" : message , a , b );
		};
		}
	
	/**
	 * Log at error consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	public static <E, F> BiConsumer<E,F> logAtErrorBiConsumer()
		{
		return logAtErrorBiConsumer( null );
		}
	
	/**
	 * Log at error function function.
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
	public static <T, R> Function<T,R> logAtErrorFunction( String message ,
	                                                       R returnObject )
		{
		return ( o ) ->
		{
		logAtError( FORMAT_INOUT_ , Thread.currentThread() , message == null ? "logAtErrorFunction" : message , o , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at error function function.
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
	public static <T, R> Function<T,R> logAtErrorFunction( R returnObject )
		{
		return logAtErrorFunction( null , returnObject );
		}
	
	/**
	 * Log at error function function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtErrorBiFunction( String message ,
	                                                                R returnObject )
		{
		return ( a , b ) ->
		{
		logAtError( FORMAT_IN2OUT_ , Thread.currentThread() , message == null ? "logAtErrorBiFunction" : message , a , b , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at error function function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtErrorBiFunction( R returnObject )
		{
		return logAtErrorBiFunction( null , returnObject );
		}
	
	/**
	 * Log at error unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the function
	 */
	public static <E> UnaryOperator<E> logAtErrorUnaryOperator( String message )
		{
		return ( o ) ->
		{
		logAtError( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtErrorUnaryOperator" : message , o );
		return o;
		};
		}
	
	/**
	 * Log at error unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the function
	 */
	public static <E> UnaryOperator<E> logAtErrorUnaryOperator()
		{
		return logAtErrorUnaryOperator( null );
		}
	
	/**
	 * Log at error supplier callable.
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
	public static <E> Supplier<E> logAtErrorSupplier( String message ,
	                                                  E returnObject )
		{
		return () ->
		{
		logAtError( FORMAT_OUT_ , Thread.currentThread() , message == null ? "logAtErrorSupplier" : message , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at error supplier callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the callable
	 */
	public static <E> Supplier<E> logAtErrorSupplier( E returnObject )
		{
		return logAtErrorSupplier( null , returnObject );
		}
	
	/**
	 * Log at error runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtErrorRunnable( String message )
		{
		return () ->
		{
		logAtError( FORMAT_ , Thread.currentThread() , message == null ? "logAtErrorRunnable" : message );
		};
		}
	
	/**
	 * Log at error runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtErrorRunnable()
		{
		return logAtErrorRunnable( null );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtError decorator">
	/**
	 * Log at error consumer proxy consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	
	/**
	 * Log at error.
	 *
	 * @param message
	 * 	the message
	 */
	public static void logAtError( String message )
		{
		logAtError( FORMAT_ , Thread.currentThread() , message == null || message.isBlank() ? "logAtError" : message );
		}
	
	/**
	 * Log at error.
	 */
	public static void logAtError()
		{
		logAtError( "" );
		}
	
	/**
	 * Log at error consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtErrorConsumerWrapper( Consumer<E> consumer )
		{
		return ( o ) ->
		{
		logAtError( FORMAT_IN_ , Thread.currentThread() , "logAtErrorConsumerWrapper" , o );
		consumer.accept( o );
		};
		}
	
	/**
	 * Log at error consumer.
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
	public static <E, F> BiConsumer<E,F> logAtErrorBiConsumerWrapper( BiConsumer<E,F> consumer )
		{
		return ( a , b ) ->
		{
		logAtError( FORMAT_IN2_ , Thread.currentThread() , "logAtErrorBiConsumerWrapper" , a , b );
		consumer.accept( a , b );
		};
		}
	
	/**
	 * Log at error function proxy function.
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
	public static <T, R> Function<T,R> logAtErrorFunctionWrapper( Function<T,R> function )
		{
		return ( o ) ->
		{
		logAtError( FORMAT_IN_ , Thread.currentThread() , "logAtErrorFunctionWrapper" , o );
		return function.apply( o );
		};
		}
	
	/**
	 * Log at error function proxy function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtErrorBiFunctionWrapper( BiFunction<T,U,R> function )
		{
		return ( a , b ) ->
		{
		logAtError( FORMAT_IN2_ , Thread.currentThread() , "logAtErrorBiFunctionWrapper" , a , b );
		return function.apply( a , b );
		};
		}
	
	//	/**
	//	 * Log at error unary operator proxy function.
	//	 *
	//	 * @param <E>
	//	 * 	the type parameter
	//	 * @param operator
	//	 * 	the operator
	//	 *
	//	 * @return the function
	//	 */
	//	public static <E> Function<E,E> logAtError( Function<E,E> operator )
	//		{
	//		String template = "[{}]   logAtErrorUnaryOperatorProxy in({})";
	//
	//		return ( o ) ->
	//		{
	//		logger_.atInfo()
	//		       .log(   template , o , Thread.currentThread() ) );
	//		return operator.apply( o );
	//		};
	//		}
	
	/**
	 * Log at error supplier proxy callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param supplier
	 * 	the supplier
	 *
	 * @return the callable
	 */
	public static <E> Supplier<E> logAtErrorSupplierWrapper( Supplier<E> supplier )
		{
		return () ->
		{
		logAtError( FORMAT_ , Thread.currentThread() , "logAtErrorSupplierWrapper" );
		return supplier.get();
		};
		}
	
	/**
	 * Log at error runnable proxy action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtErrorRunnableWrapper( Runnable runnable )
		{
		return () ->
		{
		logAtError( FORMAT_ , Thread.currentThread() , "logAtErrorRunnableWrapper" );
		runnable.run();
		};
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtWarning">
	
	/**
	 * Log at warning consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtWarningConsumer( String message )
		{
		return ( o ) ->
		{
		logAtWarning( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtWarningConsumer" : message , o );
		};
		}
	
	/**
	 * Log at warning consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtWarningConsumer()
		{
		return logAtWarningConsumer( null );
		}
	
	/**
	 * Log at warning consumer consumer.
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
	public static <E, F> BiConsumer<E,F> logAtWarningBiConsumer( String message )
		{
		return ( a , b ) ->
		{
		logAtWarning( FORMAT_IN2_ , Thread.currentThread() , message == null ? "logAtWarningBiConsumer" : message , a , b );
		};
		}
	
	/**
	 * Log at warning consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	public static <E, F> BiConsumer<E,F> logAtWarningBiConsumer()
		{
		return logAtWarningBiConsumer( null );
		}
	
	/**
	 * Log at warning function function.
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
	public static <T, R> Function<T,R> logAtWarningFunction( String message ,
	                                                         R returnObject )
		{
		return ( o ) ->
		{
		logAtWarning( FORMAT_INOUT_ , Thread.currentThread() , message == null ? "logAtWarningFunction" : message , o , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at warning function function.
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
	public static <T, R> Function<T,R> logAtWarningFunction( R returnObject )
		{
		return logAtWarningFunction( null , returnObject );
		}
	
	/**
	 * Log at warning function function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtWarningBiFunction( String message ,
	                                                                  R returnObject )
		{
		return ( a , b ) ->
		{
		logAtWarning( FORMAT_IN2OUT_ , Thread.currentThread() , message == null ? "logAtWarningBiFunction" : message , a , b , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at warning function function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtWarningBiFunction( R returnObject )
		{
		return logAtWarningBiFunction( null , returnObject );
		}
	
	/**
	 * Log at warning unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the function
	 */
	public static <E> UnaryOperator<E> logAtWarningUnaryOperator( String message )
		{
		return ( o ) ->
		{
		logAtWarning( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtWarningUnaryOperator" : message , o );
		return o;
		};
		}
	
	/**
	 * Log at warning unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the function
	 */
	public static <E> UnaryOperator<E> logAtWarningUnaryOperator()
		{
		return logAtWarningUnaryOperator( null );
		}
	
	/**
	 * Log at warning supplier callable.
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
	public static <E> Supplier<E> logAtWarningSupplier( String message ,
	                                                    E returnObject )
		{
		return () ->
		{
		logAtWarning( FORMAT_OUT_ , Thread.currentThread() , message == null ? "logAtWarningSupplier" : message , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at warning supplier callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the callable
	 */
	public static <E> Supplier<E> logAtWarningSupplier( E returnObject )
		{
		return logAtWarningSupplier( null , returnObject );
		}
	
	/**
	 * Log at warning runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtWarningRunnable( String message )
		{
		return () ->
		{
		logAtWarning( FORMAT_ , Thread.currentThread() , message == null ? "logAtWarningRunnable" : message );
		};
		}
	
	/**
	 * Log at warning runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtWarningRunnable()
		{
		return logAtWarningRunnable( null );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtWarning decorator">
	/**
	 * Log at warning consumer proxy consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	
	/**
	 * Log at warning.
	 *
	 * @param message
	 * 	the message
	 */
	public static void logAtWarning( String message )
		{
		logAtWarning( FORMAT_ , Thread.currentThread() , message == null || message.isBlank() ? "logAtWarning" : message );
		}
	
	/**
	 * Log at warning.
	 */
	public static void logAtWarning()
		{
		logAtWarning( "" );
		}
	
	/**
	 * Log at warning consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtWarningConsumerWrapper( Consumer<E> consumer )
		{
		return ( o ) ->
		{
		logAtWarning( FORMAT_IN_ , Thread.currentThread() , "logAtWarningConsumerWrapper" , o );
		consumer.accept( o );
		};
		}
	
	/**
	 * Log at warning consumer.
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
	public static <E, F> BiConsumer<E,F> logAtWarningBiConsumerWrapper( BiConsumer<E,F> consumer )
		{
		return ( a , b ) ->
		{
		logAtWarning( FORMAT_IN2_ , Thread.currentThread() , "logAtWarningBiConsumerWrapper" , a , b );
		consumer.accept( a , b );
		};
		}
	
	/**
	 * Log at warning function proxy function.
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
	public static <T, R> Function<T,R> logAtWarningFunctionWrapper( Function<T,R> function )
		{
		return ( o ) ->
		{
		logAtWarning( FORMAT_IN_ , Thread.currentThread() , "logAtWarningFunctionWrapper" , o );
		return function.apply( o );
		};
		}
	
	/**
	 * Log at warning function proxy function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtWarningBiFunctionWrapper( BiFunction<T,U,R> function )
		{
		return ( a , b ) ->
		{
		logAtWarning( FORMAT_IN2_ , Thread.currentThread() , "logAtWarningBiFunctionWrapper" , a , b );
		return function.apply( a , b );
		};
		}
	
	//	/**
	//	 * Log at warning unary operator proxy function.
	//	 *
	//	 * @param <E>
	//	 * 	the type parameter
	//	 * @param operator
	//	 * 	the operator
	//	 *
	//	 * @return the function
	//	 */
	//	public static <E> Function<E,E> logAtWarning( Function<E,E> operator )
	//		{
	//		String template = "[{}]   logAtWarningUnaryOperatorProxy in({})";
	//
	//		return ( o ) ->
	//		{
	//		logger_.atInfo()
	//		       .log(   template , o , Thread.currentThread() ) );
	//		return operator.apply( o );
	//		};
	//		}
	
	/**
	 * Log at warning supplier proxy callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param supplier
	 * 	the supplier
	 *
	 * @return the callable
	 */
	public static <E> Supplier<E> logAtWarningSupplierWrapper( Supplier<E> supplier )
		{
		return () ->
		{
		logAtWarning( FORMAT_ , Thread.currentThread() , "logAtWarningSupplierWrapper" );
		return supplier.get();
		};
		}
	
	/**
	 * Log at warning runnable proxy action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtWarningRunnableWrapper( Runnable runnable )
		{
		return () ->
		{
		logAtWarning( FORMAT_ , Thread.currentThread() , "logAtWarningRunnableWrapper" );
		runnable.run();
		};
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtDebug">
	
	/**
	 * Log at debug consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtDebugConsumer( String message )
		{
		return ( o ) ->
		{
		logAtDebug_( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtDebugConsumer" : message , o );
		};
		}
	
	/**
	 * Log at debug consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtDebugConsumer()
		{
		return logAtDebugConsumer( null );
		}
	
	/**
	 * Log at debug consumer consumer.
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
	public static <E, F> BiConsumer<E,F> logAtDebugBiConsumer( String message )
		{
		return ( a , b ) ->
		{
		logAtDebug_( FORMAT_IN2_ , Thread.currentThread() , message == null ? "logAtDebugBiConsumer" : message , a , b );
		};
		}
	
	/**
	 * Log at debug consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 *
	 * @return the consumer
	 */
	public static <E, F> BiConsumer<E,F> logAtDebugBiConsumer()
		{
		return logAtDebugBiConsumer( null );
		}
	
	/**
	 * Log at debug function function.
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
	public static <T, R> Function<T,R> logAtDebugFunction( String message ,
	                                                       R returnObject )
		{
		return ( o ) ->
		{
		logAtDebug_( FORMAT_INOUT_ , Thread.currentThread() , message == null ? "logAtDebugFunction" : message , o , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at debug function function.
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
	public static <T, R> Function<T,R> logAtDebugFunction( R returnObject )
		{
		return logAtDebugFunction( null , returnObject );
		}
	
	/**
	 * Log at debug function function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtDebugBiFunction( String message ,
	                                                                R returnObject )
		{
		return ( a , b ) ->
		{
		logAtDebug_( FORMAT_IN2OUT_ , Thread.currentThread() , message == null ? "logAtDebugBiFunction" : message , a , b , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at debug function function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtDebugBiFunction( R returnObject )
		{
		return logAtDebugBiFunction( null , returnObject );
		}
	
	/**
	 * Log at debug unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the function
	 */
	public static <E> UnaryOperator<E> logAtDebugUnaryOperator( String message )
		{
		return ( o ) ->
		{
		logAtDebug_( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtDebugUnaryOperator" : message , o );
		return o;
		};
		}
	
	/**
	 * Log at debug unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the function
	 */
	public static <E> UnaryOperator<E> logAtDebugUnaryOperator()
		{
		return logAtDebugUnaryOperator( null );
		}
	
	/**
	 * Log at debug supplier callable.
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
	public static <E> Supplier<E> logAtDebugSupplier( String message ,
	                                                  E returnObject )
		{
		return () ->
		{
		logAtDebug_( FORMAT_OUT_ , Thread.currentThread() , message == null ? "logAtDebugSupplier" : message , returnObject );
		return returnObject;
		};
		}
	
	/**
	 * Log at debug supplier callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the callable
	 */
	public static <E> Supplier<E> logAtDebugSupplier( E returnObject )
		{
		return logAtDebugSupplier( null , returnObject );
		}
	
	/**
	 * Log at debug runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtDebugRunnable( String message )
		{
		return () ->
		{
		logAtDebug_( FORMAT_ , Thread.currentThread() , message == null ? "logAtDebugRunnable" : message );
		};
		}
	
	/**
	 * Log at debug runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtDebugRunnable()
		{
		return logAtDebugRunnable( null );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="logAtDebug decorator">
	/**
	 * Log at debug consumer proxy consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	
	/**
	 * Log at debug.
	 *
	 * @param message
	 * 	the message
	 */
	public static void logAtDebug( String message )
		{
		logAtDebug_( FORMAT_ , Thread.currentThread() , message == null || message.isBlank() ? "logAtDebug" : message );
		}
	
	/**
	 * Log at debug.
	 */
	public static void logAtDebug()
		{
		logAtDebug( "" );
		}
	
	/**
	 * Log at debug consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param consumer
	 * 	the consumer
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtDebugConsumerWrapper( Consumer<E> consumer )
		{
		return ( o ) ->
		{
		logAtDebug_( FORMAT_IN_ , Thread.currentThread() , "logAtDebugConsumerWrapper" , o );
		consumer.accept( o );
		};
		}
	
	/**
	 * Log at debug consumer.
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
	public static <E, F> BiConsumer<E,F> logAtDebugBiConsumerWrapper( BiConsumer<E,F> consumer )
		{
		return ( a , b ) ->
		{
		logAtDebug_( FORMAT_IN2_ , Thread.currentThread() , "logAtDebugBiConsumerWrapper" , a , b );
		consumer.accept( a , b );
		};
		}
	
	/**
	 * Log at debug function proxy function.
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
	public static <T, R> Function<T,R> logAtDebugFunctionWrapper( Function<T,R> function )
		{
		return ( o ) ->
		{
		logAtDebug_( FORMAT_IN_ , Thread.currentThread() , "logAtDebugFunctionWrapper" , o );
		return function.apply( o );
		};
		}
	
	/**
	 * Log at debug function proxy function.
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
	public static <T, U, R> BiFunction<T,U,R> logAtDebugBiFunctionWrapper( BiFunction<T,U,R> function )
		{
		return ( a , b ) ->
		{
		logAtDebug_( FORMAT_IN2_ , Thread.currentThread() , "logAtDebugBiFunctionWrapper" , a , b );
		return function.apply( a , b );
		};
		}
	
	//	/**
	//	 * Log at debug unary operator proxy function.
	//	 *
	//	 * @param <E>
	//	 * 	the type parameter
	//	 * @param operator
	//	 * 	the operator
	//	 *
	//	 * @return the function
	//	 */
	//	public static <E> Function<E,E> logAtDebug( Function<E,E> operator )
	//		{
	//		String template = "[{}]   logAtDebugUnaryOperatorProxy in({})";
	//
	//		return ( o ) ->
	//		{
	//		logger_.atInfo()
	//		       .log(   template , o , Thread.currentThread() ) );
	//		return operator.apply( o );
	//		};
	//		}
	
	/**
	 * Log at debug supplier proxy callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param supplier
	 * 	the supplier
	 *
	 * @return the callable
	 */
	public static <E> Supplier<E> logAtDebugSupplierWrapper( Supplier<E> supplier )
		{
		return () ->
		{
		logAtDebug_( FORMAT_ , Thread.currentThread() , "logAtDebugSupplierWrapper" );
		return supplier.get();
		};
		}
	
	/**
	 * Log at debug runnable proxy action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param runnable
	 * 	the runnable
	 *
	 * @return the action
	 */
	public static <E> Runnable logAtDebugRunnableWrapper( Runnable runnable )
		{
		return () ->
		{
		logAtDebug_( FORMAT_ , Thread.currentThread() , "logAtDebugRunnableWrapper" );
		runnable.run();
		};
		}
	
	//</editor-fold>
	
	//<editor-fold desc="log+delay">
	
	/**
	 * Delay function e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param second
	 * 	the second
	 *
	 * @return the e
	 */
	public static <E> UnaryOperator<E> delayUnaryOperator( String message ,
	                                                       long second )
		{
		return ( x ) ->
		{
		log( FORMAT_IN_DELAY_ , Thread.currentThread() , message == null ? "delayUnaryOperator" : message , x , second );
		ThreadUtils.delay( second );
		return x;
		};
		}
	
	/**
	 * Delay unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param second
	 * 	the second
	 *
	 * @return the function
	 */
	public static <E> UnaryOperator<E> delayUnaryOperator( long second )
		{
		return delayUnaryOperator( null , second );
		}
	
	/**
	 * Delay function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param returnObject
	 * 	the return object
	 * @param second
	 * 	the second
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,R> delayFunction( String message ,
	                                                  R returnObject ,
	                                                  long second )
		{
		return ( x ) ->
		{
		log( FORMAT_INOUT_DELAY_ , Thread.currentThread() , message == null ? "delayFunction" : message , x , returnObject , second );
		ThreadUtils.delay( second );
		return returnObject;
		};
		}
	
	/**
	 * Delay function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 * @param second
	 * 	the second
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,R> delayFunction( R returnObject ,
	                                                  long second )
		{
		return delayFunction( null , returnObject , second );
		}
	
	/**
	 * Delay function function.
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
	 * @param second
	 * 	the second
	 *
	 * @return the function
	 */
	public static <T, U, R> BiFunction<T,U,R> delayBiFunction( String message ,
	                                                           R returnObject ,
	                                                           long second )
		{
		return ( a , b ) ->
		{
		log( FORMAT_IN2OUT_DELAY_ , Thread.currentThread() , message == null ? "delayFunction" : message , a , b , returnObject , second );
		ThreadUtils.delay( second );
		return returnObject;
		};
		}
	
	/**
	 * Delay function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <U>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param returnObject
	 * 	the return object
	 * @param second
	 * 	the second
	 *
	 * @return the function
	 */
	public static <T, U, R> BiFunction<T,U,R> delayBiFunction( R returnObject ,
	                                                           long second )
		{
		return delayBiFunction( null , returnObject , second );
		}
	
	/**
	 * Delay second consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param second
	 * 	the second
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> delayConsumer( String message ,
	                                             long second )
		{
		return ( x ) ->
		{
		log( FORMAT_IN_DELAY_ , Thread.currentThread() , message == null ? "delayConsumer" : message , x , second );
		ThreadUtils.delay( second );
		};
		}
	
	/**
	 * Delay consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param second
	 * 	the second
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> delayConsumer( long second )
		{
		return delayConsumer( null , second );
		}
	
	/**
	 * Delay second consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param second
	 * 	the second
	 *
	 * @return the consumer
	 */
	public static <E, F> BiConsumer<E,F> delayBiConsumer( String message ,
	                                                      long second )
		{
		return ( a , b ) ->
		{
		log( FORMAT_IN2_DELAY_ , Thread.currentThread() , message == null ? "delayConsumer" : message , a , b , second );
		ThreadUtils.delay( second );
		};
		}
	
	/**
	 * Delay consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 * @param second
	 * 	the second
	 *
	 * @return the consumer
	 */
	public static <E, F> BiConsumer<E,F> delayBiConsumer( long second )
		{
		return delayBiConsumer( null , second );
		}
	
	/**
	 * Delay second supplier consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param passThought
	 * 	the pass thought
	 * @param second
	 * 	the second
	 *
	 * @return the consumer
	 */
	public static <E> Supplier<E> delaySupplier( String message ,
	                                             E passThought ,
	                                             long second )
		{
		return () ->
		{
		log( FORMAT_OUT_DELAY_ , Thread.currentThread() , message == null ? "delaySupplier" : message , passThought , second );
		ThreadUtils.delay( second );
		return passThought;
		};
		}
	
	/**
	 * Delay supplier supplier.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param passThought
	 * 	the pass thought
	 * @param second
	 * 	the second
	 *
	 * @return the supplier
	 */
	public static <E> Supplier<E> delaySupplier( E passThought ,
	                                             long second )
		{
		return delaySupplier( null , passThought , second );
		}
	
	/**
	 * Delay runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param second
	 * 	the second
	 *
	 * @return the action
	 */
	public static <E> Runnable delayRunnable( String message ,
	                                          long second )
		{
		return () ->
		{
		log( FORMAT_DELAY_ , Thread.currentThread() , message == null ? "delayRunnable" : message , second );
		ThreadUtils.delay( second );
		};
		}
	
	/**
	 * Delay runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param second
	 * 	the second
	 *
	 * @return the action
	 */
	public static <E> Runnable delayRunnable( long second )
		{
		return delayRunnable( null , second );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="log+delay+error">
	
	/**
	 * Delay supplier with error supplier.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param passThought
	 * 	the pass thought
	 * @param errValueForCompare
	 * 	the err value for compare
	 * @param second
	 * 	the second
	 *
	 * @return the supplier
	 */
	public static <E> Supplier<E> delaySupplierWithError( String message ,
	                                                      E passThought ,
	                                                      E errValueForCompare ,
	                                                      long second )
		{
		return () ->
		{
		
		log( FORMAT_OUT_DELAY_ , Thread.currentThread() , message == null ? "delaySupplierWithError" : message , passThought , second );
		ThreadUtils.delay( second );
		if( passThought.equals( errValueForCompare ) )
			{
			throw new IllegalArgumentException( "delaySupplierWithError" );
			}
		return passThought;
		};
		}
	
	/**
	 * Delay function e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param errValueForCompare
	 * 	the err value for compare
	 * @param second
	 * 	the second
	 *
	 * @return the e
	 */
	public static <E> UnaryOperator<E> delayUnaryOperatorWithError( String message ,
	                                                                E errValueForCompare ,
	                                                                long second )
		{
		return ( x ) ->
		{
		log( FORMAT_IN_DELAY_ , Thread.currentThread() , message == null ? "delayUnaryOperatorWithError" : message , x , second );
		ThreadUtils.delay( second );
		if( x.equals( errValueForCompare ) )
			{
			throw new IllegalArgumentException( "delaySupplierWithError" );
			}
		return x;
		};
		}
	
	/**
	 * Delay function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param message
	 * 	the message
	 * @param errValueForCompare
	 * 	the err value for compare
	 * @param returnObject
	 * 	the return object
	 * @param second
	 * 	the second
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,R> delayFunctionWithError( String message ,
	                                                           T errValueForCompare ,
	                                                           R returnObject ,
	                                                           long second )
		{
		return ( x ) ->
		{
		log( FORMAT_INOUT_DELAY_ , Thread.currentThread() , message == null ? "delayFunctionWithError" : message , x , returnObject , second );
		ThreadUtils.delay( second );
		if( x.equals( errValueForCompare ) )
			{
			throw new IllegalArgumentException( "delayFunctionWithError" );
			}
		return returnObject;
		};
		}
	
	//</editor-fold>
	
	//<editor-fold desc="log+delay+default">
	
	/**
	 * Delay function e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param object
	 * 	the object
	 *
	 * @return the e
	 */
	public static <E> E defaultDelayUnaryOperator( E object )
		{
		log( FORMAT_INOUT_DELAY_ , Thread.currentThread() , "defaultDelayUnaryOperator" , object , object , DEFAULT_DELAY_ );
		ThreadUtils.delay( DEFAULT_DELAY_ );
		return object;
		}
	
	/**
	 * Delay second consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param object
	 * 	the object
	 */
	public static <E> void defaultDelayConsumer( E object )
		{
		log( FORMAT_IN_DELAY_ , Thread.currentThread() , "defaultDelayConsumer" , object , DEFAULT_DELAY_ );
		ThreadUtils.delay( DEFAULT_DELAY_ );
		
		}
	
	/**
	 * Delay second consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param <F>
	 * 	the type parameter
	 * @param left
	 * 	the left
	 * @param right
	 * 	the right
	 */
	public static <E, F> void defaultDelayBiConsumer( E left ,
	                                                  F right )
		{
		log( FORMAT_IN2_DELAY_ , Thread.currentThread() , "defaultDelayBiConsumer" , left , right , DEFAULT_DELAY_ );
		ThreadUtils.delay( DEFAULT_DELAY_ );
		
		}
	
	/**
	 * Delay runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 */
	public static <E> void defaultDelayRunnable()
		{
		log( FORMAT_DELAY_ , Thread.currentThread() , "defaultDelayRunnable" , DEFAULT_DELAY_ );
		ThreadUtils.delay( DEFAULT_DELAY_ );
		}
	
	//</editor-fold>
	
	}
