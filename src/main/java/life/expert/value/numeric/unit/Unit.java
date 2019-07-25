package life.expert.value.numeric.unit;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.numeric.unit
//                           wilmer 2019/04/29
//
//--------------------------------------------------------------------------------

import life.expert.value.numeric.context.Context;
import life.expert.value.numeric.context.PieceContext;

/**
 * A unit of values.
 *
 * This interface represents a unit of values such as the Kg, Cm,
 * pins or other. It provides interoperability between different
 * implementations.
 *
 * Values can be distinguished by separate {@link #getCode()} ()} codes,
 * Implementation specification
 * Implementation of this class
 * <ul>
 * <li>are required to implement {@code equals/hashCode} considering the
 * concrete implementation type and unit code.
 * <li>are required to be thread-safe
 * <li>are required to be immutable
 * <li>are required to be comparable
 *
 * </ul>
 *
 * @version 1.0
 */
public interface Unit
	extends Comparable<Unit>
	{
	
	/**
	 * Gets the unique unit code, the effective code depends on the
	 * unit.
	 *
	 * Since each unit is identified by this code, the unit code is
	 * required to be defined for every {@link Unit} and not
	 * {@code null} or empty.
	 *
	 * @return the unit code, never {@code null}.
	 */
	String getCode();
	
	/**
	 * Gets a numeric unit code. within the ISO name space, this equals
	 * to the ISO numeric code. In other unit name spaces this number may be
	 * different, or even undefined (-1).
	 *
	 * The numeric code is an optional alternative to the standard unit
	 * code. If defined, the numeric code is required to be unique.
	 *
	 * @return the numeric unit code
	 */
	int getNumericCode();
	
	/**
	 * Gets the number of fractional digits typically used by this unit.
	 *
	 * Different units have different numbers of fractional digits by
	 * default.
	 *
	 * @return the fractional digits, from 0 to 9 (normally 0, 2 or 3), or 0 for 	pseudo-units.
	 */
	int getDefaultFractionDigits();
	
	/**
	 * Returns the {@link PieceContext} of a unit. This context contains additional information
	 * about the type and capabilities of a Unit, e.g. its provider and more.
	 *
	 * @return the unit's context, never null.
	 */
	Context getContext();
	
	}
