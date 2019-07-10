package life.expert.common.graph;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.graph
//                           wilmer 2019/07/10
//
//--------------------------------------------------------------------------------









import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;









/**
 * The type Render graph default strategy.
 *
 * @param <N>
 * 	the type parameter
 */
@RequiredArgsConstructor( staticName = "of" )
public class RenderGraphDefaultStrategy<N>
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
