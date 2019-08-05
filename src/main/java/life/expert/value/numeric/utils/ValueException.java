package life.expert.value.numeric.utils;
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

/**
 * The type Value exception.
 */
public class ValueException
	extends RuntimeException
	{
	
	/**
	 * Creates an instance.
	 *
	 * @param message
	 * 	the message
	 */
	public ValueException( String message )
		{
		super( message );
		}
	
	/**
	 * Creates an instance with the specified detail message and cause.
	 *
	 * @param message
	 * 	the detail message (which is saved for later retrieval by the        {@link Throwable#getMessage()} method).
	 * @param cause
	 * 	the cause (which is saved for later retrieval by the        {@link Throwable#getCause()} method). (A    <b>null   </b> value 	is permitted, and indicates that the cause is nonexistent or 	unknown.)
	 */
	public ValueException( String message ,
	                       Throwable cause )
		{
		super( message , cause );
		}
	}