package life.expert.common.async.memoizer;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.async.memoizer
//                           wilmer 2019/10/12
//
//--------------------------------------------------------------------------------

/**
 * @see "http://sebastian-millies.blogspot.de/2016/05/concurrent-recursive-function.html"
 */
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * <pre> * The type Concurrent trampolining memoizer.
 *
 * Preconditions: none
 * Postconditions: none
 * Side effects: create trhead
 * Tread safety: Not thread-safe
 *
 * </pre>
 *
 * @param <T>
 * 	the type parameter
 * @param <R>
 * 	the type parameter
 *
 * @see "http://sebastian-millies.blogspot.de/2016/05/concurrent-recursive-function.html"
 */
public class ConcurrentTrampoliningMemoizer<T, R>
	{
	private static final Executor TRAMPOLINE_ = newSingleThreadExecutor( new ThreadFactoryBuilder().setDaemon( true )
	                                                                                               .build() );
	
	private final ConcurrentMap<T,CompletableFuture<R>> memo_;
	
	/**
	 * Instantiates a new Concurrent trampolining memoizer.
	 *
	 * @param cache
	 * 	the cache
	 */
	public ConcurrentTrampoliningMemoizer( ConcurrentMap<T,CompletableFuture<R>> cache )
		{
		this.memo_ = cache;
		}
	
	/**
	 * Memoize function.
	 *
	 * @param f
	 * 	the f
	 *
	 * @return the function
	 */
	public Function<T,CompletableFuture<R>> memoize( Function<T,CompletableFuture<R>> f )
		{
		return t ->
		{
		var r = memo_.get( t );
		if( r == null )
			{
			// value not yet memoized: put a container in the map that will come to hold the value
			var compute = new CompletableFuture<R>();
			r = memo_.putIfAbsent( t , compute );
			if( r == null )
				{
				// only the thread that first has a cache miss calls the underlying function.
				// recursive asynchronous calls are bounced off the task queue inside the default executor, avoiding stack overflows.
				// the computed value is placed in the container.
				r = CompletableFuture.supplyAsync( () -> f.apply( t ) , TRAMPOLINE_ )
				                     .thenCompose( Function.identity() )
				                     .thenCompose( x ->
				                                   {
				                                   compute.complete( x );
				                                   return compute;
				                                   } );
				}
			}
		return r;
		};
		}
	}