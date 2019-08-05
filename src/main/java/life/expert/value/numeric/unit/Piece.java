package life.expert.value.numeric.unit;
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

import com.google.common.collect.ComparisonChain;
import life.expert.value.numeric.context.PieceContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

/**
 * The type Piece.
 */
@Value
@AllArgsConstructor( staticName = "of" )
@Builder( toBuilder = true )
public class Piece
	implements Unit,
	           Comparable<Unit>
	{
	
	/**
	 * The constant DEFAULT_CONTEXT.
	 */
	public static final PieceContext DEFAULT_CONTEXT = PieceContext.of( Map.of() );
	
	@NonNull private final String code;
	
	//@NonNull
	@Builder.Default private final int numericCode = -1;
	
	//@NonNull
	@Builder.Default private final int defaultFractionDigits = 0;
	
	@NonNull
	@Builder.Default
	private final PieceContext context = DEFAULT_CONTEXT;
	
	/**
	 * Create piece instance with given unit code.
	 *
	 * @param code
	 * 	the unit code
	 *
	 * @return the piece
	 */
	public static final Piece of( @NonNull final String code )
		{
		return builder().code( code )
		                .build();
		}
	
	@Override
	public int compareTo( Unit o )
		{
		return ComparisonChain.start()
		                      .compare( this.getCode() , o.getCode() )
		                      .result();
		}
		
	}
