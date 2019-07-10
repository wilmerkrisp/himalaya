package life.expert.common.graph;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.graph
//                           wilmer 2019/07/10
//
//--------------------------------------------------------------------------------









import com.google.common.graph.EndpointPair;
import lombok.NonNull;

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
		
	}
