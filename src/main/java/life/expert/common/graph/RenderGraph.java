package life.expert.common.graph;



import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.Network;
import com.google.common.graph.ValueGraph;
import life.expert.common.async.LogUtils;
import life.expert.common.io.FileUtils;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static life.expert.common.async.LogUtils.*;
import static life.expert.common.async.LogUtils.logAtInfoRunnable;
import static reactor.core.publisher.Mono.just;

//import static life.expert.common.base.Preconditions.*;  //checkCollection
//import static life.expert.common.function.Patterns.*;    //for-comprehension

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          simpliest  life.expert.algo.research.utils
//                           wilmer 2019/06/20
//
//--------------------------------------------------------------------------------

/**
 * The class allows you to mark some vertices or edges of the graph when outputting to a .dot file
 * Builder pattern.

 *
 * <pre>{@code
 * MutableGraph<String> g = GraphBuilder.undirected()
 * .build();
 * g.putEdge( "A" , "B" );
 * g.putEdge( "A" , "C" );
 * g.putEdge( "B" , "C" );
 * g.addNode( "D" );
 *
 *
 * RenderGraph.graph( g )
 * .log()
 * .markNode( "B" , "fillcolor=red" )
 * .markEdge( "B" , "C" , "color=red width=5" )
 * .renderToFile(false);
 * }****</pre>
 *
 *
 * Every constructor/fabric can raise the exceptions:
 * throws NullPointerException if argument nullable
 * throws IllegalArgumentException if argument empty
 *
 * @param <N>
 * 	the type parameter
 */
public final class RenderGraph<N>
	{
	
	private static final String DOT_OUTPUT_FOLDER = "src/main/graphviz/";
	
	private static final Logger logger_ = LoggerFactory.getLogger( RenderGraph.class );
	
	private @NonNull RenderGraphStrategy graph;
	
	private Map<N,String> markNodes = new HashMap<>();
	
	private Map<EndpointPair<N>,String> markEdges = new HashMap<>();
	
	private RenderGraph( @NotNull RenderGraphStrategy graph )
		{
		this.graph = graph;
		}
	
	/**
	 * Graph render graph.
	 *
	 * @param <V>
	 * 	the type parameter
	 * @param graph
	 * 	the graph
	 *
	 * @return the render graph
	 */
	public static <V> RenderGraph<V> graph( @NonNull Graph<V> graph )
		{
		return new RenderGraph<V>( RenderGraphStrategy.DefaultStrategy.of( graph ) );
		}
	
	/**
	 * Graph render graph.
	 *
	 * @param <V>
	 * 	the type parameter
	 * @param <E>
	 * 	the type parameter
	 * @param graph
	 * 	the graph
	 *
	 * @return the render graph
	 */
	public static <V, E> RenderGraph<V> graph( @NonNull Network<V,E> graph )
		{
		return new RenderGraph<V>( RenderGraphStrategy.NetworkStrategy.of( graph ) );
		}
	
	/**
	 * Graph render graph.
	 *
	 * @param <V>
	 * 	the type parameter
	 * @param <I>
	 * 	the type parameter
	 * @param graph
	 * 	the graph
	 *
	 * @return the render graph
	 */
	public static <V, I> RenderGraph<V> graph( @NonNull ValueGraph<V,I> graph )
		{
		return new RenderGraph<V>( RenderGraphStrategy.ValueStrategy.of( graph ) );
		}
	
	/**
	 * Delete rendered graph folder.
	 */
	public static void deleteRenderedGraphFolder()
		{
		
		FileUtils.deleteFile( DOT_OUTPUT_FOLDER )
		         .subscribe();
		}
	
	/**
	 * Mark node render graph.
	 *
	 * @param node
	 * 	the node
	 * @param property
	 * 	the property
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> markNode( @NonNull N node ,
	                                @NonNull String property )
		{
		markNodes.put( node , property );
		
		return this;
		}
	
	/**
	 * Mark nodes render graph.
	 *
	 * @param markNodes
	 * 	the mark nodes
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> markNodes( @NonNull Map<N,String> markNodes )
		{
		markNodes.putAll( markNodes );
		
		return this;
		}
	
	/**
	 * Clear mark nodes render graph.
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> clearMarkNodes()
		{
		markNodes.clear();
		
		return this;
		}
	
	/**
	 * Mark edge render graph.
	 *
	 * @param edge
	 * 	the edge
	 * @param property
	 * 	the property
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> markEdge( @NonNull EndpointPair<N> edge ,
	                                @NonNull String property )
		{
		markEdges.put( edge , property );
		
		return this;
		}
	
	/**
	 * Mark edge render graph.
	 *
	 * @param nodeU
	 * 	the node u
	 * @param nodeV
	 * 	the node v
	 * @param property
	 * 	the property
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> markEdge( @NonNull N nodeU ,
	                                @NonNull N nodeV ,
	                                @NonNull String property )
		{
		var edge = graph.isDirected() ? EndpointPair.ordered( nodeU , nodeV ) : EndpointPair.unordered( nodeU , nodeV );
		
		markEdges.put( edge , property );
		
		return this;
		}
	
	/**
	 * Mark edges render graph.
	 *
	 * @param markEdges
	 * 	the mark edges
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> markEdges( @NonNull Map<EndpointPair<N>,String> markEdges )
		{
		markEdges.putAll( markEdges );
		
		return this;
		}
	
	/**
	 * Clear mark edges render graph.
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> clearMarkEdges()
		{
		markEdges.clear();
		
		return this;
		}
	
	/**
	 * Log render graph.
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> buildToLog()
		{
		LogUtils.log( buildToString() );
		return this;
		}
	
	/**
	 * Render to string string.
	 *
	 * @return the string
	 */
	public String buildToString()
		{
		return graph.renderToString( markNodes , markEdges );
		}
	
	/**
	 * Render to file render graph.
	 *
	 * @param fileName
	 * 	the file name
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> buildToFile( String fileName )
		{
		var dot = buildToString();
		
		var new_file_flux = just( DOT_OUTPUT_FOLDER + fileName )
			                    //.log( "debug", Level.FINE, SignalType.ON_NEXT )
			                    .flatMap( FileUtils::createFileToMono )
			                    .flatMap( FileUtils::writerFromPath );
		new_file_flux.subscribe( w -> w.write( dot ) , logAtErrorConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
		
		return this;
		}
	
	/**
	 * Render to file render graph.
	 *
	 * @return the render graph
	 */
	public RenderGraph<N> buildToFile()
		{
		
		var filename_with_datetime = String.format( "%d.dot" , Instant.now()
		                                                              .toEpochMilli() );
		//var filename_with_counter = String.format( "%d.dot" , ++file_name_counter_ );
		return buildToFile( filename_with_datetime );
		}
		
	}
