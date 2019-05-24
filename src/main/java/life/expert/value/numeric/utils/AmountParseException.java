package life.expert.value.numeric.utils;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.utils
//                           wilmer 2019/05/02
//
//--------------------------------------------------------------------------------









import java.util.Objects;









/**
 * Signals that an error has been reached unexpectedly while parsing.
 */
public class AmountParseException
	extends ValueException
	{
	
	
	
	/**
	 * The zero-based character offset into the string being parsed at which the
	 * error was found during parsing.
	 *
	 * @serial
	 */
	private int errorIndex;
	
	
	
	/**
	 * The original input data.
	 */
	private CharSequence data;
	
	
	
	/**
	 * Constructs a AmountParseException with the specified detail message,
	 * parsed text and index. A detail message is a String that describes this
	 * particular exception.
	 *
	 * @param message
	 * 	the detail message
	 * @param parsedData
	 * 	the parsed text, should not be null
	 * @param errorIndex
	 * 	the position where the error is found while parsing.
	 */
	public AmountParseException( String message ,
	                             CharSequence parsedData ,
	                             int errorIndex )
		{
		super( message );
		if( errorIndex > parsedData.length() )
			{
			throw new IllegalArgumentException( "Invalid error index > input.length" );
			}
		this.data = parsedData;
		this.errorIndex = errorIndex;
		}
	
	
	
	/**
	 * Constructs a MonetaryParseException with the parsed text and offset. A
	 * detail message is a String that describes this particular exception.
	 *
	 * @param parsedData
	 * 	the parsed text, should not be null
	 * @param errorIndex
	 * 	the position where the error is found while parsing.
	 */
	public AmountParseException( CharSequence parsedData ,
	                             int errorIndex )
		{
		super( "Parse Error" );
		if( errorIndex > parsedData.length() )
			{
			throw new IllegalArgumentException( "Invalid error index > input.length" );
			}
		this.data = parsedData;
		this.errorIndex = errorIndex;
		}
	
	
	
	/**
	 * Returns the index where the error was found.
	 *
	 * @return the index where the error was found
	 */
	public int getErrorIndex()
		{
		return errorIndex;
		}
	
	
	
	/**
	 * Returns the string that was being parsed.
	 *
	 * @return the parsed input string, or {@code null}, if {@code null} was passed as 	input.
	 */
	public String getInput()
		{
		
		if( Objects.isNull( data ) )
			{
			return null;
			}
		return data.toString();
		}
		
	}
