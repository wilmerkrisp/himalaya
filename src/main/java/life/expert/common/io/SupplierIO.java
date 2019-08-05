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
 * Represents a supplier of results.
 *
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T>
 * 	the type of results supplied by this supplier
 *
 * @since 1.8
 */
@FunctionalInterface
public interface SupplierIO<T>
	{
	
	/**
	 * Gets a result.
	 *
	 * @return a result
	 *
	 * @throws IOException
	 * 	the io exception
	 */
	T get()
	throws IOException;
	}
