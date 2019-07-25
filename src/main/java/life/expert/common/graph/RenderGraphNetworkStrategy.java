package life.expert.common.graph;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.graph
//                           wilmer 2019/07/10
//
//--------------------------------------------------------------------------------

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Network;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * The type Render graph network strategy.
 *
 * @param <N>
 * 	the type parameter
 * @param <E>
 * 	the type parameter
 */
@RequiredArgsConstructor( staticName = "of" )
public class RenderGraphNetworkStrategy<N, E>
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
