package life.expert.common.async.memoizer;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.async.memoizer
//                           wilmer 2019/10/12
//
//--------------------------------------------------------------------------------

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * <pre>
 * Marker interface for concurrently memoized functions.
 *
 * Usage example with good old Fibonacci sequence (doesn't actually show concurrent calls, but should be enough to illustrate the use):
 * {@code
 * public static class Fibonacci
 *      {
 * 	private static final CompletableFuture<Long> ONE = CompletableFuture.completedFuture( 1L );
 * 	private final Function<Long,CompletableFuture<Long>> fibMem;
 *
 * 	public Fibonacci()
 *                {
 * 		  fibMem = MemoizedConcurrently.of( this::fib );
 *                }
 *
 * 	public CompletableFuture<Long> fib( Long n )
 *                {
 * 		if( n <= 2 )
 * 			return ONE;
 * 		return fibMem.apply( n - 1 )
 * 		             .thenCompose( x -> fibMem.apply( n - 2 )
 * 		                                      .thenApply( y -> x + y ) );
 *                }
 *        }
 *
 *  System.out.println( new Fibonacci().fib( 50L ).join() );
 *        }</pre>
 */
public interface MemoizedConcurrently
	{
	
	/**
	 * Lift the given function to a thread-safe, concurrently memoizing version. The returned function computes the
	 * return value for a given argument only once. On subsequent calls given the same argument the memoized value
	 * is returned. The returned function has a few interesting properties:
	 * <ul>
	 * <li>The function does not permit {@code null} values.
	 * <li>Different threads will always wind up using the same value instances, so the function may compute values
	 * that are supposed to be singletons.
	 * <li>Concurrent callers won't block each other.
	 * </ul>
	 * This method is idempotent, i. e. applying it to an already concurrently memoized function will return the
	 * function unchanged.
	 *
	 * @param <T>
	 * 	the type parameter of function input argument
	 * @param <R>
	 * 	the type parameter of function output argument
	 * @param function
	 * 	a possibly recursive (asynchronous) function
	 *
	 * @return the memoizing equivalent
	 */
	static <T, R> Function<T,CompletableFuture<R>> of( Function<T,CompletableFuture<R>> function )
		{
		if( function == null )
			throw new IllegalArgumentException( "Lambda-function must not be null" );
		
		if( function instanceof MemoizedConcurrently )
			return function; // make this method idempotent
		
		var cache    = new ConcurrentHashMap<T,CompletableFuture<R>>();  // the simplest possible cache
		var memoizer = new ConcurrentTrampoliningMemoizer<T,R>( cache );
		var memoized = memoizer.memoize( function );
		return (Function<T,CompletableFuture<R>> & MemoizedConcurrently) memoized::apply; // mark as memoized using intersection type
		}
	}
