package life.expert.common.function;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/11
//
//--------------------------------------------------------------------------------









/**
 * in order to be able to put the code that calls checked exceptions in lambda
 *
 * @param <T1>
 * 	the type parameter
 * @param <T2>
 * 	the type parameter
 * @param <T3>
 * 	the type parameter
 * @param <T4>
 * 	the type parameter
 */
@FunctionalInterface
public interface CheckedConsumer4<T1, T2, T3, T4>
	{
	
	
	
	/**
	 * Accept.
	 *
	 * @param t1
	 * 	the t 1
	 * @param t2
	 * 	the t 2
	 * @param t3
	 * 	the t 3
	 * @param t4
	 * 	the t 4
	 *
	 * @throws Throwable
	 * 	the throwable
	 */
	public void accept( T1 t1 ,
	                    T2 t2 ,
	                    T3 t3 ,
	                    T4 t4 )
	throws Throwable;
	}