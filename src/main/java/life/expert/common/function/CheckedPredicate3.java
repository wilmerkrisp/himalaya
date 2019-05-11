package life.expert.common.function;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.function
//                           wilmer 2019/05/11
//
//--------------------------------------------------------------------------------









public interface CheckedPredicate3<T1, T2, T3>
	{
	public boolean test( T1 t1 ,
	                     T2 t2 ,
	                     T3 t3 )
	throws Throwable;
	}