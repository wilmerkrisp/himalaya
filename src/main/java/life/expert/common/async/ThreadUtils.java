package life.expert.common.async;






import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.oath.cyclops.internal.stream.spliterators.push.Operator;
import life.expert.common.io.ConsumerIO;
import life.expert.common.io.RunnableIO;
import life.expert.common.io.SupplierIO;
import org.jetbrains.annotations.*;                     //@NotNull
import com.google.errorprone.annotations.Immutable;     //@Immutable

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import com.google.common.collect.*;                     //ImmutableList

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.utils.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)
import static life.expert.common.base.Objects.*;        //deepCopyOfObject

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.*;                            //producer supplier

import static cyclops.function.Memoize.*;               //memoizeSupplier
import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;



//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.utils.async
//                           wilmer 2019/02/05
//
//--------------------------------------------------------------------------------









/**
 * interface static
 * !CHANGE_ME_DESCRIPTION!
 *
 * - сервисные методы делать статик методами на классе с private конструктором а не на интерфейсе тк методы обработки могут содержать состояние и кеши
 * - интрфейсы использовать только для задания типа
 *
 *
 *  <pre>{@code
 *
 *
 *
 *
 * example 2
 *
 *               ThreadUtils.fs_service();
 *               v_1=ThreadUtils.s_const;
 *
 *
 *
 *
 * }**********</pre>
 */
public final class ThreadUtils
	{
	
	
	
	// constant
	private static final int WAIT_TIME_RATIO_FOR_WAITING_TASKS = 100;
	
	
 
	
	
	
	private ThreadUtils()
		{
		super();
		
		
		throw new UnsupportedOperationException( "Dont use this PRIVATE constructor.Please use constructor with parameters." );
		}
	
	
	

	
	
	//<editor-fold desc="utils">
	
	
	//
	//	/**
	//	 * Fixed scheduler.
	//	 *
	//	 * @param name
	//	 * 	the name
	//	 * @param size
	//	 * 	the size
	//	 *
	//	 * @return the scheduler
	//	 */
	//	public static Scheduler fixed( String name ,
	//	                               int size )
	//		{
	//		return Schedulers.from( ThreadUtils.executorCustom( name , size ) );
	//		}
	
	
	/**
	 * Delay.
	 *
	 * @param second
	 * 	the second
	 */
	public static void delay( long second )
		{
		interruptedWrapper( () -> Thread.sleep( 1000 * second ) );
		}
	
	//</editor-fold>
	
	
	//<editor-fold desc="executors">
	
	
	
	/**
	 * Executor executor.
	 *
	 * @param name
	 * 	the name
	 * @param size
	 * 	the size
	 * @param waitTimeRatio
	 * 	the wait time ratio
	 *
	 * @return the executor
	 */
	 
	public static Executor executorDaemon( String name ,
	                                       int size ,
	                                       int waitTimeRatio )
		{
		ThreadFactory thread_factory = new ThreadFactoryBuilder().setNameFormat( name == null ? "pool%d" : name + "%d" )
		                                                         .setDaemon( true )
		                                                         .build();
		int thr_num = Runtime.getRuntime()
		                     .availableProcessors() * ( waitTimeRatio == 0 ? 1 : waitTimeRatio );
		return Executors.newFixedThreadPool( Math.min( size , thr_num ) , thread_factory );
		}
	
	
	
	/**
	 * Executor daemon executor.
	 *
	 * @param size
	 * 	the size
	 * @param waitTimeRatio
	 * 	the wait time ratio
	 *
	 * @return the executor
	 */
	 
	public static Executor executorDaemon( int size ,
	                                       int waitTimeRatio )
		{
		return executorDaemon( null , size , waitTimeRatio );
		}
	
	
	
	/**
	 * Executor executor.
	 *
	 * @param size
	 * 	the size
	 *
	 * @return the executor
	 */
	 
	public static Executor executorForWaitingTasks( int size )
		{
		return executorDaemon( size , WAIT_TIME_RATIO_FOR_WAITING_TASKS );
		}
	
	
	
	/**
	 * Executor custom executor.
	 *
	 * @param name
	 * 	the name
	 * @param size
	 * 	the size
	 *
	 * @return the executor
	 */
	 
	public static Executor executorCustom( String name ,
	                                       int size )
		{
		ThreadFactory thread_factory = new ThreadFactoryBuilder().setNameFormat( name == null ? "pool%d" : name + "%d" )
		                                                         .setDaemon( true )
		                                                         .build();
		return new ThreadPoolExecutor( size , size , 0L , TimeUnit.MILLISECONDS , new LinkedBlockingQueue<>( 1000 ) , thread_factory );
		}
	
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="wrappers">
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param operation
	 * 	the operation
	 */
	public static void interruptedWrapper(  RunnableInterrupted operation )
		{
		if( operation == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.run();
			}
		catch( InterruptedException | ExecutionException | TimeoutException exception )
			{
			throw new RuntimeException( exception );
			}
		}
	
	
	
	/**
	 * Io wrapper e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param operation
	 * 	the operation
	 *
	 * @return the e
	 */
	public static <E> E interruptedWrapper(   SupplierInterrupted<E> operation )
		{
		if( operation == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			return operation.get();
			}
		catch( InterruptedException | ExecutionException | TimeoutException exception )
			{
			throw new RuntimeException( exception );
			}
		}
	
	
	
	/**
	 * Io optional optional.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param operation
	 * 	the operation
	 *
	 * @return the optional
	 */
	public static <E> Optional<E> interruptedOptional(  SupplierInterrupted<E> operation )
		{
		if( operation == null )
			{
			return Optional.empty();
			}
		try
			{
			return Optional.ofNullable( operation.get() );
			}
		catch( InterruptedException | ExecutionException | TimeoutException exception )
			{
			return Optional.empty();
			}
		}
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param input
	 * 	the input
	 * @param operation
	 * 	the operation
	 */
	public static <E> void interruptedWrapper(  E input ,
	                                            ConsumerInterrupted<E> operation )
		{
		if( operation == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.accept( input );
			}
		catch( InterruptedException | ExecutionException | TimeoutException exception )
			{
			throw new RuntimeException( exception );
			}
		}
	//</editor-fold>
	
	
	
	}
