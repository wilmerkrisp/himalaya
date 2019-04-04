package life.expert.common.async;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.reactivestreams
//                           wilmer 2019/03/05
//
//--------------------------------------------------------------------------------









import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
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
import life.expert.common.async.ThreadUtils;
//import io.reactivestreams.functions.Consumer;









/**
 * The type Log utils.
 *
 * import static life.expert.common.async.LogUtils.*;
 */
public class LogUtils
	{
	
	
	
	private static final Logger logger_ = LoggerFactory.getLogger( LogUtils.class );
	
	
	
	private static final void log_( String format ,
	                                Object... arguments )
		{
		logger_.info( format , arguments );
		}
	
	
	
	private static final void logAtError_( String message )
		{
		logger_.error( message );
		}
	
	
	
	private static final long DEFAULT_DELAY_ = 1;
	
	
	
	private static final String FORMAT_ = "{}   {}";
	
	
	
	private static final String FORMAT_DELAY_ = "{}   {} delay({})";
	
	
	
	private static final String FORMAT_IN_ = "{}   {} in ({})";
	
	
	
	private static final String FORMAT_IN2_ = "{}   {} in ({}) in ({})";
	
	
	
	private static final String FORMAT_IN_DELAY_ = "{}   {} in ({}) delay({})";
	
	
	
	private static final String FORMAT_IN2_DELAY_ = "{}   {} in ({}) in ({}) delay({})";
	
	
	
	private static final String FORMAT_OUT_ = "{}   {} out({})";
	
	
	
	private static final String FORMAT_OUT_DELAY_ = "{}   {} out({}) delay({})";
	
	
	
	private static final String FORMAT_INOUT_ = "{}   {} in ({}) out({})";
	
	
	
	private static final String FORMAT_IN2OUT_ = "{}   {} in ({}) in ({}) out({})";
	
	
	
	private static final String FORMAT_INOUT_DELAY_ = "{}   {} in ({}) out({}) delay({})";
	
	
	
	private static final String FORMAT_IN2OUT_DELAY_ = "{}   {} in ({}) in ({}) out({}) delay({})";
	
	
	
	//<editor-fold desc="log">
	
	
	
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
		log_( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtInfoConsumer" : message , o );
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
		log_( FORMAT_IN2_ , Thread.currentThread() , message == null ? "logAtInfoConsumer" : message , a , b );
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
		log_( FORMAT_INOUT_ , Thread.currentThread() , message == null ? "logAtInfoFunction" : message , o , returnObject );
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
		log_( FORMAT_IN2OUT_ , Thread.currentThread() , message == null ? "logAtInfoFunction" : message , a , b , returnObject );
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
		log_( FORMAT_IN_ , Thread.currentThread() , message == null ? "logAtInfoUnaryOperator" : message , o );
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
		log_( FORMAT_OUT_ , Thread.currentThread() , message == null ? "logAtInfoSupplier" : message , returnObject );
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
		log_( FORMAT_ , Thread.currentThread() , message == null ? "logAtInfoRunnable" : message );
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
		log_( FORMAT_IN_DELAY_ , Thread.currentThread() , message == null ? "delayUnaryOperator" : message , x , second );
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
		log_( FORMAT_INOUT_DELAY_ , Thread.currentThread() , message == null ? "delayFunction" : message , x , returnObject , second );
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
		log_( FORMAT_IN2OUT_DELAY_ , Thread.currentThread() , message == null ? "delayFunction" : message , a , b , returnObject , second );
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
		log_( FORMAT_IN_DELAY_ , Thread.currentThread() , message == null ? "delayConsumer" : message , x , second );
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
		log_( FORMAT_IN2_DELAY_ , Thread.currentThread() , message == null ? "delayConsumer" : message , a , b , second );
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
		log_( FORMAT_OUT_DELAY_ , Thread.currentThread() , message == null ? "delaySupplier" : message , passThought , second );
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
		log_( FORMAT_DELAY_ , Thread.currentThread() , message == null ? "delayRunnable" : message , second );
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
		
		log_( FORMAT_OUT_DELAY_ , Thread.currentThread() , message == null ? "delaySupplierWithError" : message , passThought , second );
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
		log_( FORMAT_IN_DELAY_ , Thread.currentThread() , message == null ? "delayUnaryOperatorWithError" : message , x , second );
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
		log_( FORMAT_INOUT_DELAY_ , Thread.currentThread() , message == null ? "delayFunctionWithError" : message , x , returnObject , second );
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
		log_( FORMAT_INOUT_DELAY_ , Thread.currentThread() , "defaultDelayUnaryOperator" , object , object , DEFAULT_DELAY_ );
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
		log_( FORMAT_IN_DELAY_ , Thread.currentThread() , "defaultDelayConsumer" , object , DEFAULT_DELAY_ );
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
		log_( FORMAT_IN2_DELAY_ , Thread.currentThread() , "defaultDelayBiConsumer" , left , right , DEFAULT_DELAY_ );
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
		log_( FORMAT_DELAY_ , Thread.currentThread() , "defaultDelayRunnable" , DEFAULT_DELAY_ );
		ThreadUtils.delay( DEFAULT_DELAY_ );
		}
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="decorator">
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
		log_( FORMAT_ , Thread.currentThread() , message == null || message.isBlank() ? "logAtInfo" : message );
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
		log_( FORMAT_IN_ , Thread.currentThread() , "logAtInfoConsumerWrapper" , o );
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
		log_( FORMAT_IN2_ , Thread.currentThread() , "logAtInfoBiConsumerWrapper" , a , b );
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
		log_( FORMAT_IN_ , Thread.currentThread() , "logAtInfoFunctionWrapper" , o );
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
		log_( FORMAT_IN2_ , Thread.currentThread() , "logAtInfoBiFunctionWrapper" , a , b );
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
		log_( FORMAT_ , Thread.currentThread() , "logAtInfoSupplierWrapper" );
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
		log_( FORMAT_ , Thread.currentThread() , "logAtInfoRunnableWrapper" );
		runnable.run();
		};
		}
	
	//</editor-fold>
	
	
	
	}
