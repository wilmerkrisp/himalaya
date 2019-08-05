package life.expert.value.numeric.utils;
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

import life.expert.value.numeric.unit.Piece;

import java.util.Locale;
import java.util.Objects;

/**
 * Exception thrown when a unit code cannot be resolved into a {@link life.expert.value.numeric.unit.Unit}.
 */
public class UnknownUnitException
	extends ValueException
	{
	
	/**
	 * The invalid currency code requested.
	 */
	private final String currencyCode;
	
	/**
	 * The invalid {@link Locale} requested.
	 */
	private final Locale locale;
	
	/**
	 * Creates a new exception instance when a {@link  life.expert.value.numeric.unit.Unit} could not be evaluated given a
	 * unit code.
	 *
	 * @param code
	 * 	The unknown currency code (the message is constructed automatically), not null.
	 *
	 * @see Piece#getCode() Piece#getCode()
	 */
	public UnknownUnitException( String code )
		{
		super( "Unknown currency code: " + code );
		this.currencyCode = code;
		this.locale = null;
		}
	
	/**
	 * Creates a new exception instance when a {@link life.expert.value.numeric.unit.Unit} could not be evaluated given a
	 * (country) {@link Locale}.
	 *
	 * @param locale
	 * 	The unknown {@link Locale}, for which a {@link life.expert.value.numeric.unit.Unit} was queried (the 	message is constructed automatically), not null.
	 *
	 * @see Piece#getCode() Piece#getCode()
	 */
	public UnknownUnitException( Locale locale )
		{
		super( "No currency for found for Locale: " + locale );
		this.locale = locale;
		this.currencyCode = null;
		}
	
	/**
	 * Access the invalid currency code.
	 *
	 * @return the invalid currency code, or {@code null}.
	 */
	public String getCurrencyCode()
		{
		return currencyCode;
		}
	
	/**
	 * Access the invalid {@link Locale}.
	 *
	 * @return the invalid {@link Locale}, or {@code null}.
	 */
	public Locale getLocale()
		{
		return locale;
		}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
		{
		if( Objects.isNull( locale ) )
			{
			return "UnknownCurrencyException [currencyCode=" + currencyCode + "]";
			}
		else
			{
			return "UnknownCurrencyException [locale=" + locale + "]";
			}
		}
		
	}
