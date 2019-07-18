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
 * @param <T5>
 * 	the type parameter
 * @param <T6>
 * 	the type parameter
 */
@FunctionalInterface
public interface CheckedPredicate6<T1, T2, T3, T4, T5, T6>
	{
	
	
	
	/**
	 * Test boolean.
	 *
	 * @param t1
	 * 	the t 1
	 * @param t2
	 * 	the t 2
	 * @param t3
	 * 	the t 3
	 * @param t4
	 * 	the t 4
	 * @param t5
	 * 	the t 5
	 * @param t6
	 * 	the t 6
	 *
	 * @return the boolean
	 *
	 * @throws Throwable
	 * 	the throwable
	 */
	public boolean test( T1 t1 ,
	                     T2 t2 ,
	                     T3 t3 ,
	                     T4 t4 ,
	                     T5 t5 ,
	                     T6 t6 )
	throws Throwable;
	}
