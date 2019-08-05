package life.expert.common.io;
//-------------------------------------------------------------------------------------------------------
//  __    __   __  .___  ___.      ___       __          ___   ____    ____  ___   ____    ____  ___
// |  |  |  | |  | |   \/   |     /   \     |  |        /   \  \   \  /   / /   \  \   \  /   / /   \
// |  |__|  | |  | |  \  /  |    /  ^  \    |  |       /  ^  \  \   \/   / /  ^  \  \   \/   / /  ^  \
// |   __   | |  | |  |\/|  |   /  /_\  \   |  |      /  /_\  \  \_    _/ /  /_\  \  \_    _/ /  /_\  \
// |  |  |  | |  | |  |  |  |  /  _____  \  |  `----./  _____  \   |  |  /  _____  \   |  |  /  _____  \
// |__|  |__| |__| |__|  |__| /__/     \__\ |_______/__/     \__\  |__| /__/     \__\  |__| /__/     \__\
//
//                                            Wilmer Krisp 2019/02/05
//--------------------------------------------------------------------------------------------------------

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