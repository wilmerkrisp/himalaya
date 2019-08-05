package life.expert.value.numeric.context;
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
