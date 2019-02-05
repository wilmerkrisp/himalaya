package life.expert.common.async;//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.async
//                           wilmer 2019/02/05
//
//--------------------------------------------------------------------------------









import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;









/**
 * The interface Runnable interrupted.
 */
@FunctionalInterface
public interface RunnableInterrupted
	{
	
	
	
	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @throws InterruptedException
	 * 	the interrupted exception
	 * @throws ExecutionException
	 * 	the execution exception
	 * @throws TimeoutException
	 * 	the timeout exception
	 * @see java.lang.Thread#run() java.lang.Thread#run()java.lang.Thread#run()java.lang.Thread#run()
	 */
	public abstract void run()
	throws InterruptedException, ExecutionException, TimeoutException;
	}