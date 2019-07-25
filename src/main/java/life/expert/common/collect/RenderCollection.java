package life.expert.common.collect;









import com.google.common.collect.Range;
import life.expert.common.async.LogUtils;
import life.expert.common.graph.RenderGraph;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static life.expert.common.async.LogUtils.logAtError;

/**
 * allows you to mark elements of a collection (for example, mark one of the elements with an asterisk) and output such a collection as a string
 *
 * <pre>{@code RenderCollection.collection( sorted_list )
 * 	            .markElement( c , "*" )
 * 	            .markRange( a , b , "()" )
 * 	            .log(); }**</pre>
 *
 * @param <T>
 * 	the type parameter
 */
public class RenderCollection<T>
	{
	
	
	
	private static final Logger logger_ = LoggerFactory.getLogger( RenderGraph.class );
	
	
	
	private @NonNull Collection<T> collection;
	
	
	
	private Map<Integer,String> markElements = new HashMap<>();
	
	
	
	private Map<Range<Integer>,String> markRanges = new HashMap<>();
	
	
	
	private RenderCollection( @NotNull Collection<T> collection )
		{
		this.collection = collection;
		}
	
	
	
	/**
	 * Collection render collection.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 *
	 * @return the render collection
	 */
	public static <E> RenderCollection<E> collection( @NonNull Collection<E> collection )
		{
		return new RenderCollection<E>( collection );
		}
	
	
	
	/**
	 * Mark element render collection.
	 *
	 * @param index
	 * 	the index
	 * @param property
	 * 	the property
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> markElement( int index ,
	                                        @NonNull String property )
		{
		markElements.put( index , property );
		
		return this;
		}
	
	
	
	/**
	 * Mark element render collection.
	 *
	 * @param markElements
	 * 	the mark elements
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> markElement( @NonNull Map<Integer,String> markElements )
		{
		markElements.putAll( markElements );
		
		return this;
		}
	
	
	
	/**
	 * Clear mark elements render collection.
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> clearMarkElements()
		{
		markElements.clear();
		
		return this;
		}
	
	
	
	/**
	 * Mark range render collection.
	 *
	 * @param range
	 * 	the range
	 * @param brackets
	 * 	the brackets
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> markRange( @NonNull Range<Integer> range ,
	                                      @NonNull String brackets )
		{
		markRanges.put( range , brackets );
		
		return this;
		}
	
	
	
	/**
	 * Mark range render collection.
	 *
	 * @param left
	 * 	the left
	 * @param right
	 * 	the right
	 * @param brackets
	 * 	the brackets
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> markRange( int left ,
	                                      int right ,
	                                      @NonNull String brackets )
		{
		markRanges.put( Range.closed( left , right ) , brackets );
		
		return this;
		}
	
	
	
	/**
	 * Mark ranges render collection.
	 *
	 * @param markRanges
	 * 	the mark ranges
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> markRanges( @NonNull Map<Range<Integer>,String> markRanges )
		{
		markRanges.putAll( markRanges );
		
		return this;
		}
	
	
	
	/**
	 * Clear mark ranges render collection.
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> clearMarkRanges()
		{
		markRanges.clear();
		
		return this;
		}
	
	
	
	/**
	 * Log render collection.
	 *
	 * @return the render collection
	 */
	public RenderCollection<T> log()
		{
		LogUtils.log( renderToString() );
		return this;
		}
	
	
	
	/**
	 * Render to string string.
	 *
	 * @return the string
	 */
	public String renderToString()
		{
		List<String> list = collection.stream()
		                              .map( i -> "" + i )
		                              .collect( toList() );
		
		var list_range = Range.closed( 0 , list.size() - 1 );
		
		for( var m : markElements.entrySet() )
			{
			var i   = m.getKey();
			var v   = m.getValue();
			var sym = v == null ? "*" : v.isBlank() ? "*" : v;
			if( !( list_range.contains( i ) ) )
				{
				logAtError( "Element marker {} out of range in Collection of size {}" , i , list_range );
				}
			list.set( i , "*" + list.get( i ) );
			}
		
		for( var m : markRanges.entrySet() )
			{
			var i   = m.getKey();
			var v   = m.getValue();
			var mid = v.length() / 2;
			var v1  = v.substring( 0 , mid );
			var v2  = v.substring( mid );
			LogUtils.log( "mid={} v1={} v2={}" , mid , v1 , v2 );
			if( !( list_range.encloses( i ) ) )
				{
				logAtError( "Bracket marker {} out of range in Collection of size {}" , i , list_range );
				}
			
			int a = i.lowerEndpoint();
			int b = i.upperEndpoint();
			list.set( a , v1 + list.get( a ) );
			list.set( b , list.get( b ) + v2 );
			}
		
		return list.toString();
		}
		
		
		
	}
