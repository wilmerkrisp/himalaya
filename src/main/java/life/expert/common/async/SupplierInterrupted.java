package life.expert.common.async;//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.utils.async
//                           wilmer 2019/02/05
//
//--------------------------------------------------------------------------------









import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;









/**
 * The interface Supplier interrupted.
 * in order to be able to put the code that calls checked exceptions in lambda
 *
 * @param <T>
 * 	the type parameter
 */
@FunctionalInterface
public interface SupplierInterrupted< T >
	{
	
	
	
	/**
	 * Gets a result.
	 *
	 * @return a result
	 *
	 * @throws InterruptedException
	 * 	the interrupted exception
	 * @throws ExecutionException
	 * 	the execution exception
	 * @throws TimeoutException
	 * 	the timeout exception
	 */
	T get()
	throws InterruptedException, ExecutionException, TimeoutException;
	}
