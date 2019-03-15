package life.expert.common.reactivex;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.reactivex
//                           wilmer 2019/03/05
//
//--------------------------------------------------------------------------------









import com.google.common.base.Strings;
import com.google.common.flogger.FluentLogger;

import java.util.concurrent.Callable;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;


import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import life.expert.common.async.ThreadUtils;
//import io.reactivex.functions.Consumer;









/**
 * The type Log utils.
 *
 * import static life.expert.common.reactivex.LogUtils.*;
 */
public class LogUtils
	{
	
	
	
	private static final FluentLogger logger_ = FluentLogger.forEnclosingClass();
	
	
	
	private static final long DEFAULT_DELAY_ = 1;
	
	
	//<editor-fold desc="common">
	
	
	
	/**
	 * Log at info.
	 *
	 * @param template
	 * 	the template
	 */
	public static void logAtInfo( String template )
		{
		String format_template = template == null || template.isBlank() ? "logAtInfo    thread(%s)" : template;
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , Thread.currentThread() ) );
		}
	
	
	
	/**
	 * Log at info.
	 */
	public static void logAtInfo()
		{
		logAtInfo( "" );
		}
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="log">
	
	
	
	/**
	 * Log at info consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> logAtInfoConsumer( String template )
		{
		String format_template = template == null || template.isBlank() ? "logAtInfoConsumer input(%s)    thread(%s)" : template;
		
		return ( o ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , o , Thread.currentThread() ) );
			
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
		return logAtInfoConsumer( "" );
		}
	
	
	
	/**
	 * Log at info function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,R> logAtInfoFunction( String template ,
	                                                      R returnObject )
		{
		String format_template = template == null || template.isBlank() ? "logAtInfoFunction in(%s) out(%s)    thread(%s)" : template;
		
		return ( o ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , o , returnObject , Thread.currentThread() ) );
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
		return logAtInfoFunction( "" , returnObject );
		}
	
	
	
	/**
	 * Log at info unary operator function.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 *
	 * @return the function
	 */
	public static <E> Function<E,E> logAtInfoUnaryOperator( String template )
		{
		String format_template = template == null || template.isBlank() ? "logAtInfoUnaryOperator inout(%s)    thread(%s)" : template;
		
		return ( o ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , o , Thread.currentThread() ) );
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
	public static <E> Function<E,E> logAtInfoUnaryOperator()
		{
		return logAtInfoUnaryOperator( "" );
		}
	
	
	
	/**
	 * Log at info supplier callable.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 * @param returnObject
	 * 	the return object
	 *
	 * @return the callable
	 */
	public static <E> Callable<E> logAtInfoSupplier( String template ,
	                                                 E returnObject )
		{
		String format_template = template == null || template.isBlank() ? "logAtInfoSupplier out(%s)    thread(%s)" : template;
		
		return () ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , returnObject , Thread.currentThread() ) );
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
	public static <E> Callable<E> logAtInfoSupplier( E returnObject )
		{
		return logAtInfoSupplier( returnObject );
		}
	
	
	
	/**
	 * Log at info runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 *
	 * @return the action
	 */
	public static <E> Action logAtInfoRunnable( String template )
		{
		String format_template = template == null || template.isBlank() ? "logAtInfoRunnable    thread(%s)" : template;
		
		return () ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , Thread.currentThread() ) );
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
	public static <E> Action logAtInfoRunnable()
		{
		return logAtInfoRunnable( "" );
		}
	
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="log+delay">
	
	
	
	/**
	 * Delay function e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 * @param second
	 * 	the second
	 *
	 * @return the e
	 */
	public static <E> Function<E,E> delayUnaryOperator( String template ,
	                                                    long second )
		{
		String format_template = template == null || template.isBlank() ? "delayUnaryOperator inout(%s) delay(%d)    thread(%s)" : template;
		
		return ( x ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , x , second , Thread.currentThread() ) );
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
	public static <E> Function<E,E> delayUnaryOperator( long second )
		{
		return delayUnaryOperator( "" , second );
		}
	
	
	
	/**
	 * Delay function function.
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <R>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 * @param returnObject
	 * 	the return object
	 * @param second
	 * 	the second
	 *
	 * @return the function
	 */
	public static <T, R> Function<T,R> delayFunction( String template ,
	                                                  R returnObject ,
	                                                  long second )
		{
		String format_template = template == null || template.isBlank() ? "delayFunction in(%s) out(%s) delay(%d)    thread(%s)" : template;
		
		return ( x ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , x , returnObject , second , Thread.currentThread() ) );
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
		return delayFunction( "" , returnObject , second );
		}
	
	
	
	/**
	 * Delay second consumer consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 * @param second
	 * 	the second
	 *
	 * @return the consumer
	 */
	public static <E> Consumer<E> delayConsumer( String template ,
	                                             long second )
		{
		String format_template = template == null || template.isBlank() ? "delayConsumer in(%s) delay(%d)    thread(%s)" : template;
		
		return ( x ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , x , second , Thread.currentThread() ) );
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
		return delayConsumer( "" , second );
		}
	
	
	
	/**
	 * Delay second supplier consumer.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 * @param passThought
	 * 	the pass thought
	 * @param second
	 * 	the second
	 *
	 * @return the consumer
	 */
	public static <E> Supplier<E> delaySupplier( String template ,
	                                             E passThought ,
	                                             long second )
		{
		String format_template = template == null || template.isBlank() ? "delaySupplier out(%s) delay(%d)    thread(%s)" : template;
		
		return () ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , passThought , second , Thread.currentThread() ) );
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
		return delaySupplier( "" , passThought , second );
		}
	
	
	
	/**
	 * Delay runnable action.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param template
	 * 	the template
	 * @param second
	 * 	the second
	 *
	 * @return the action
	 */
	public static <E> Action delayRunnable( String template ,
	                                        long second )
		{
		String format_template = template == null || template.isBlank() ? "delayRunnable delay(%d)    thread(%s)" : template;
		
		return () ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( format_template , second , Thread.currentThread() ) );
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
	public static <E> Action delayRunnable( long second )
		{
		return delayRunnable( "" , second );
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
		
		logger_.atInfo()
		       .log( Strings.lenientFormat( "defaultDelayUnaryOperator delay(%d)    thread(%s)" , DEFAULT_DELAY_ , Thread.currentThread() ) );
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
		logger_.atInfo()
		       .log( Strings.lenientFormat( "defaultDelayConsumer delay(%d)    thread(%s)" , DEFAULT_DELAY_ , Thread.currentThread() ) );
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
		logger_.atInfo()
		       .log( Strings.lenientFormat( "defaultDelayRunnable delay(%d)    thread(%s)" , DEFAULT_DELAY_ , Thread.currentThread() ) );
		ThreadUtils.delay( DEFAULT_DELAY_ );
		}
	
	
	//</editor-fold>
	
	
	
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
	//<editor-fold desc="proxy">
	public static <E> Consumer<E> logAtInfoConsumerProxy( Consumer<E> consumer )
		{
		String template = "Consumer input(%s)    thread(%s)";
		
		return ( o ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( template , o , Thread.currentThread() ) );
		consumer.accept( o );
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
	public static <T, R> Function<T,R> logAtInfoFunctionProxy( Function<T,R> function )
		{
		String template = "Function in(%s)     thread(%s)";
		
		return ( o ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( template , o , Thread.currentThread() ) );
		return function.apply( o );
		};
		}
	
	
	
	/**
	 * Log at info unary operator proxy function.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param operator
	 * 	the operator
	 *
	 * @return the function
	 */
	public static <E> Function<E,E> logAtInfoUnaryOperatorProxy( Function<E,E> operator )
		{
		String template = "logAtInfoUnaryOperatorProxy in(%s)     thread(%s)";
		
		return ( o ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( template , o , Thread.currentThread() ) );
		return operator.apply( o );
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
	public static <E> Callable<E> logAtInfoSupplierProxy( Callable<E> supplier )
		{
		String template = "logAtInfoSupplierProxy out(%s)    thread(%s)";
		
		return () ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( template , Thread.currentThread() ) );
		return supplier.call();
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
	public static <E> Action logAtInfoRunnableProxy( Action runnable )
		{
		String template = "logAtInfoRunnableProxy    thread(%s)";
		
		return () ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( template , Thread.currentThread() ) );
		runnable.run();
		};
		}
	
	//</editor-fold>
	
	
	
	}
