package life.expert.value.numeric.context;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.unit
//                           wilmer 2019/04/29
//
//--------------------------------------------------------------------------------

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

/**
 * The type Unit context.
 */
@Value
@AllArgsConstructor( staticName = "of" )
@Builder( toBuilder = true )
public class PieceContext
	implements Context
	{
	
	/**
	 * -- SETTER --
	 *
	 * @param traits
	 * 	traits
	 * @return traits
	 *
	 * 	-- GETTER --
	 * @return map  with additional information about numeric amount.
	 */
	@NonNull
	@Singular
	private final Map<String,Object> traits;
	
	}
