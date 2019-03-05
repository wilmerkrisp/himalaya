package life.expert.common.reactivex;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.reactivex
//                           wilmer 2019/03/05
//
//--------------------------------------------------------------------------------









import com.google.common.base.Strings;
import com.google.common.flogger.FluentLogger;

import java.util.function.Consumer;


import io.reactivex.functions.Action;
//import io.reactivex.functions.Consumer;






public class LogUtils
	{
	private static final FluentLogger logger_ = FluentLogger.forEnclosingClass();
	
	
	public static <E> Consumer<E> logAtInfoConsumer( String template )
		{
		
		return ( o ) ->
		{
		logger_.atInfo()
		       .log( Strings.lenientFormat( template , o ) );
		};
		}
	
	
	
	public static <E> Action logAtInfoRunnable( String template )
		{
		
		return () ->
		{
		logger_.atInfo()
		       .log( template );
		};
		}
	
	}
