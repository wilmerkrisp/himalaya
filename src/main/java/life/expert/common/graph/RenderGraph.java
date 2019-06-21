package life.expert.common.graph;









import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import life.expert.common.io.FileUtils;
import lombok.Builder;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

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
 * generic immutable class: 1 1 1
 *
 * - pattern new-call
 * - not for inheritance
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
 * }***</pre>
 *
 *
 * Every constructor/fabric can raise the exceptions:
 * throws NullPointerException if argument nullable
 * throws IllegalArgumentException if argument empty
 *
 * @param <E>
 * 	the type parameter
 */
public final class RenderGraph<E>
	{
	
	
	
	private static final String DOT_OUTPUT_FOLDER = "src/main/graphviz/";
	
	
	
	private static final Logger logger_ = LoggerFactory.getLogger( RenderGraph.class );
	
	
	
	private @NonNull Graph<E> graph;
	
	
	
	private Map<E,String> markNodes = new HashMap<>();
	
	
	
	private Map<EndpointPair<E>,String> markEdges = new HashMap<>();
	
	
	
	private RenderGraph( @NotNull Graph<E> graph )
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
		return new RenderGraph<V>( graph );
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
	public RenderGraph<E> markNode( @NonNull E node ,
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
	public RenderGraph<E> markNodes( @NonNull Map<E,String> markNodes )
		{
		markNodes.putAll( markNodes );
		
		return this;
		}
	
	
	
	/**
	 * Clear mark nodes render graph.
	 *
	 * @return the render graph
	 */
	public RenderGraph<E> clearMarkNodes()
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
	public RenderGraph<E> markEdge( @NonNull EndpointPair<E> edge ,
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
	public RenderGraph<E> markEdge( @NonNull E nodeU ,
	                                @NonNull E nodeV ,
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
	public RenderGraph<E> markEdges( @NonNull Map<EndpointPair<E>,String> markEdges )
		{
		markEdges.putAll( markEdges );
		
		return this;
		}
	
	
	
	/**
	 * Clear mark edges render graph.
	 *
	 * @return the render graph
	 */
	public RenderGraph<E> clearMarkEdges()
		{
		markEdges.clear();
		
		return this;
		}
	
	
	
	/**
	 * Log render graph.
	 *
	 * @return the render graph
	 */
	public RenderGraph<E> log()
		{
		log_( renderToString() );
		return this;
		}
	
	
	
	/**
	 * Render to string string.
	 *
	 * @return the string
	 */
	public String renderToString()
		{
		StringBuilder dot = new StringBuilder();
		dot.append( graph.isDirected() ? "digraph G{\n\tratio = fill; node [shape = circle]; \n" : "graph G{\n\tratio = fill; node [shape = circle]; edge [dir=none];\n" );
		
		
		for( E n : graph.nodes() )
			{
			var description = markNodes.getOrDefault( n , "style=filled fillcolor=gray" );
			dot.append( String.format( "\t%s [%s];\n" , n , description ) );
			}
		
		for( EndpointPair<E> e : graph.edges() )
			{
			var description = markEdges.getOrDefault( e , String.format( "label = \"%s\"" , e ) );
			dot.append( String.format( "\t%s %s %s [%s];\n" , e.nodeU() , graph.isDirected() ? "->" : "--" , e.nodeV() , description ) );
			}
		dot.append( "}\n" );
		
		return dot.toString();
		}
	
	
	
	/**
	 * Render to file render graph.
	 *
	 * @param fileName
	 * 	the file name
	 *
	 * @return the render graph
	 */
	public RenderGraph<E> renderToFile( String fileName )
		{
		var dot = renderToString();
		
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
	public RenderGraph<E> renderToFile()
		{
		
		var filename_with_datetime = String.format( "%d.dot" , Instant.now()
		                                                              .toEpochMilli() );
		//var filename_with_counter = String.format( "%d.dot" , ++file_name_counter_ );
		return renderToFile( filename_with_datetime );
		}
		
		
	}
