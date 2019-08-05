package life.expert.common.graph;
//-------------------------------------------------------------------------------------------------------
//  __    __   __  .___  ___.      ___       __          ___   ____    ____  ___   ____    ____  ___
// |  |  |  | |  | |   \/   |     /   \     |  |        /   \  \   \  /   / /   \  \   \  /   / /   \
// |  |__|  | |  | |  \  /  |    /  ^  \    |  |       /  ^  \  \   \/   / /  ^  \  \   \/   / /  ^  \
// |   __   | |  | |  |\/|  |   /  /_\  \   |  |      /  /_\  \  \_    _/ /  /_\  \  \_    _/ /  /_\  \
// |  |  |  | |  | |  |  |  |  /  _____  \  |  `----./  _____  \   |  |  /  _____  \   |  |  /  _____  \
// |__|  |__| |__| |__|  |__| /__/     \__\ |_______/__/     \__\  |__| /__/     \__\  |__| /__/     \__\
//
//                                            Wilmer Krisp 2019/02/05
//--------------------------------------------------------------------------------------------------------

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.Network;
import com.google.common.graph.ValueGraph;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * The interface Render graph strategy.
 *
 * @param <N>
 * 	the type parameter
 */
public interface RenderGraphStrategy<N>
	{
	
	/**
	 * Is directed boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDirected();
	
	/**
	 * Render to string string.
	 *
	 * @param markNodes
	 * 	the mark nodes
	 * @param markEdges
	 * 	the mark edges
	 *
	 * @return the string
	 */
	public String renderToString( @NonNull Map<N,String> markNodes ,
	                              @NonNull Map<EndpointPair<N>,String> markEdges );
	
	/**
	 * The type Render graph default strategy.
	 *
	 * @param <N>
	 * 	the type parameter
	 */
	@RequiredArgsConstructor( staticName = "of" )
	class DefaultStrategy<N>
		implements RenderGraphStrategy<N>
		{
		
		private @NonNull Graph<N> graph;
		
		@Override
		public boolean isDirected()
			{
			return graph.isDirected();
			}
		
		@Override
		public String renderToString( @NonNull Map<N,String> markNodes ,
		                              @NonNull Map<EndpointPair<N>,String> markEdges )
			{
			StringBuilder dot = new StringBuilder();
			dot.append( graph.isDirected() ? "digraph G{\n\tratio = fill; node [shape = circle]; \n" : "graph G{\n\tratio = fill; node [shape = circle]; edge [dir=none];\n" );
			
			for( N n : graph.nodes() )
				{
				var description = markNodes.getOrDefault( n , "style=filled fillcolor=gray" );
				dot.append( String.format( "\t%s [%s];\n" , n , description ) );
				}
			
			for( EndpointPair<N> e : graph.edges() )
				{
				var description = markEdges.getOrDefault( e , String.format( "label = \"%s\"" , e ) );
				dot.append( String.format( "\t%s %s %s [%s];\n" , e.nodeU() , graph.isDirected() ? "->" : "--" , e.nodeV() , description ) );
				}
			dot.append( "}\n" );
			
			return dot.toString();
			}
		}
	
	/**
	 * The type Render graph network strategy.
	 *
	 * @param <N>
	 * 	the type parameter
	 * @param <E>
	 * 	the type parameter
	 */
	@RequiredArgsConstructor( staticName = "of" )
	class NetworkStrategy<N, E>
		implements RenderGraphStrategy<N>
		{
		
		private @NonNull Network<N,E> graph;
		
		@Override
		public boolean isDirected()
			{
			return false;
			}
		
		@Override
		public String renderToString( @NonNull Map<N,String> markNodes ,
		                              @NonNull Map<EndpointPair<N>,String> markEdges )
			{
			StringBuilder dot = new StringBuilder();
			dot.append( graph.isDirected() ? "digraph G{\n\tratio = fill; node [shape = circle]; \n" : "graph G{\n\tratio = fill; node [shape = circle]; edge [dir=none];\n" );
			
			for( N n : graph.nodes() )
				{
				var description = markNodes.getOrDefault( n , "style=filled fillcolor=gray" );
				dot.append( String.format( "\t%s [%s];\n" , n , description ) );
				}
			
			//difference here
			for( E e : graph.edges() )
				{
				var p           = graph.incidentNodes( e );
				var value       = String.format( "label = \"%s\"" , e );
				var description = markEdges.getOrDefault( e , "" );
				dot.append( String.format( "\t%s %s %s [%s %s];\n" , p.nodeU() , graph.isDirected() ? "->" : "--" , p.nodeV() , value , description ) );
				}
			dot.append( "}\n" );
			
			return dot.toString();
			}
		}
	
	/**
	 * The type Render graph value strategy.
	 *
	 * @param <N>
	 * 	the type parameter
	 * @param <V>
	 * 	the type parameter
	 */
	@RequiredArgsConstructor( staticName = "of" )
	class ValueStrategy<N, V>
		implements RenderGraphStrategy<N>
		{
		
		private @NonNull ValueGraph<N,V> graph;
		
		@Override
		public boolean isDirected()
			{
			return false;
			}
		
		@Override
		public String renderToString( @NonNull Map<N,String> markNodes ,
		                              @NonNull Map<EndpointPair<N>,String> markEdges )
			{
			StringBuilder dot = new StringBuilder();
			dot.append( graph.isDirected() ? "digraph G{\n\tratio = fill; node [shape = circle]; \n" : "graph G{\n\tratio = fill; node [shape = circle]; edge [dir=none];\n" );
			
			for( N n : graph.nodes() )
				{
				var description = markNodes.getOrDefault( n , "style=filled fillcolor=gray" );
				dot.append( String.format( "\t%s [%s];\n" , n , description ) );
				}
			
			for( EndpointPair<N> e : graph.edges() )
				{
				String value = graph.edgeValue( e )
				                    .isPresent() ? String.format( "label = \"%s\"" , graph.edgeValue( e )
				                                                                          .get() ) : ""; //difference is only here
				var description = markEdges.getOrDefault( e , "" );
				dot.append( String.format( "\t%s %s %s [%s %s];\n" , e.nodeU() , graph.isDirected() ? "->" : "--" , e.nodeV() , value , description ) );
				}
			dot.append( "}\n" );
			
			return dot.toString();
			}
		}
	}
