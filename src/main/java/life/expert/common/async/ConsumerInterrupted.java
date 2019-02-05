package life.expert.common.async;//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.base
//                           wilmer 2019/02/05
//
//--------------------------------------------------------------------------------









import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;









/**
 * The interface Consumer interrupted.
 *
 * @param <T>
 * 	the type parameter
 */
@FunctionalInterface
public interface ConsumerInterrupted< T >
	{
	
	
	
	/**
	 * Accept.
	 *
	 * @param t
	 * 	the t
	 *
	 * @throws InterruptedException
	 * 	the interrupted exception
	 * @throws ExecutionException
	 * 	the execution exception
	 * @throws TimeoutException
	 * 	the timeout exception
	 */
	void accept( T t )
	throws InterruptedException, ExecutionException, TimeoutException;
	
	}