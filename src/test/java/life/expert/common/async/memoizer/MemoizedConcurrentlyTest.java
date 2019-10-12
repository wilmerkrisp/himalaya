package life.expert.common.async.memoizer;



import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

//---------------------------------------------
//      ___       __        _______   ______
//     /   \     |  |      /  _____| /  __  \
//    /  ^  \    |  |     |  |  __  |  |  |  |
//   /  /_\  \   |  |     |  | |_ | |  |  |  |
//  /  _____  \  |  `----.|  |__| | |  `--'  |
// /__/     \__\ |_______| \______|  \______/
//
//               wilmer 2019/10/12
//---------------------------------------------
//import static life.expert.common.base.Preconditions.*;  //checkCollection
//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R
//import static io.vavr.API.*;                           //conflicts with my reactive For-comprehension
//import static java.util.function.Predicate.*;           //isEqual streamAPI

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

class MemoizedConcurrentlyTest
	{
	

	
	public static class Fibonacci
		{
		private static final CompletableFuture<Long> ONE = CompletableFuture.completedFuture( 1L );
		
		private final Function<Long,CompletableFuture<Long>> fibMem;
		
		public Fibonacci()
			{
			fibMem = MemoizedConcurrently.of( this::fib );
			}
		
		public CompletableFuture<Long> fib( Long n )
			{
			if( n <= 2 )
				return ONE;
			return fibMem.apply( n - 1 )
			             .thenCompose( x -> fibMem.apply( n - 2 )
			                                      .thenApply( y -> x + y ) );
			}
		}
	
	@Test
	void fibonachiMemoized5Test()
		{
		
//		System.out.println( new Fibonacci().fib( 50L )
		//		                                   .join() ); //20365011074
		long l=new Fibonacci().fib( 50L )
		                     .join();
		assertEquals( l ,12586269025L );
		}
		
	}