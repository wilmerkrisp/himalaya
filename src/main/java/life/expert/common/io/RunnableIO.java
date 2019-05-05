package life.expert.common.io;
//@Header@
//--------------------------------------------------------------------------------
//
//                          archidoc  life.expert
//                           wilmer 2019/01/30
//
//--------------------------------------------------------------------------------









import java.io.IOException;









/**
 * The interface Runnable io.
 */
@FunctionalInterface
public interface RunnableIO
	{
	
	
	
	/**
	 * When an object implementing interface <pre>{@code Runnable}</pre> is used
	 * to create a thread, starting the thread causes the object's
	 * <pre>{@code run}</pre> method to be called in that separately executing
	 * thread.
	 *
	 * The general contract of the method <pre>{@code run}</pre> is that it may
	 * take any action whatsoever.
	 *
	 * @throws IOException
	 * 	the io exception
	 * @see java.lang.Thread#run() java.lang.Thread#run()
	 */
	public abstract void run()
	throws IOException;
	}