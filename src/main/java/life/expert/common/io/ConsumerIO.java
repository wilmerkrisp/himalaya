package life.expert.common.io;







import java.io.IOException;
import java.util.function.Consumer;

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.utils.io
//                           wilmer 2019/01/23
//
//--------------------------------------------------------------------------------









/**
 * The interface Consumer io.
 *
 * @param <T>
 * 	the type parameter
 */
@FunctionalInterface
public interface ConsumerIO<T>
	{
	
	
	
	/**
	 * Accept.
	 *
	 * @param t
	 * 	the t
	 *
	 * @throws IOException
	 * 	the io exception
	 */
	void accept(T t) throws IOException;
	
	}
