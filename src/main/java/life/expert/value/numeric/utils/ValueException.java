package life.expert.value.utils;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.amount
//                           wilmer 2019/04/29
//
//--------------------------------------------------------------------------------









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
		super( message ,
		       cause );
		}
	}