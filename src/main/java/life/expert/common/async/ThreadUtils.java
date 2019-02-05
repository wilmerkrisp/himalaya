package life.expert.common.async;









import org.jetbrains.annotations.*;                     //@NotNull
import com.google.errorprone.annotations.Immutable;     //@Immutable

import com.google.common.flogger.FluentLogger;          //log

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import com.google.common.collect.*;                     //ImmutableList

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)
import static life.expert.common.base.Objects.*;        //deepCopyOfObject

import java.util.function.*;                            //producer supplier

import static cyclops.function.Memoize.*;               //memoizeSupplier
import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;



//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.async
//                           wilmer 2019/02/05
//
//--------------------------------------------------------------------------------









/**
 * interface static
 * !CHANGE_ME_DESCRIPTION!
 *
 * - сервисные методы делать статик методами на классе с private конструктором а не на интерфейсе тк методы обработки могут содержать состояние и кеши
 * - интрфейсы использовать только для задания типа
 *
 *
 * <pre>{@code
 *
 *
 *
 *
 * example 2
 *
 *               ThreadUtils.fs_service();
 *               v_1=ThreadUtils.s_const;
 *
 *
 *
 *
 * }</pre>
 */
public final class ThreadUtils
	{
	
	
	
	private ThreadUtils()
		{
		super();
		
		
		throw new UnsupportedOperationException( "Dont use this PRIVATE constructor.Please use constructor with parameters." );
		}
	
	
	
	/**
	 * constants
	 * !CHANGE_ME_DESCRIPTION!
	 *
	 * <pre>{@code
	 *
	 *
	 * example 1
	 *
	 *           s_const (THIS not allowed)
	 *
	 *
	 * }</pre>
	 */
	public static final String stringOne = new String( "Test string." );
	
	
	
	private static void fs_service1_()
		{
		
		
		return;
		}
	
	
	
	/**
	 * method public
	 * !CHANGE_ME_DESCRIPTION!
	 *
	 * <pre>{@code
	 *
	 *
	 * example 1
	 *
	 *           ThreadUtils.fs_service();  (THIS not allowed)
	 *
	 *
	 *
	 * }</pre>
	 */
	public static void fs_service2()
		{
		
		
		return;
		}
	
	
	
	/**
	 * STATIC method test with argument of ANYTYPE
	 * STATIC generic methods allowed
	 * !CHANGE_ME_DESCRIPTION!
	 *
	 * <pre>{@code
	 *
	 *
	 * example 1
	 *
	 *           ThreadUtils.fg_service("stroka");
	 *           ThreadUtils.fg_service(12);
	 *
	 *
	 *
	 * }</pre>
	 */
	public static < E   /* extends super VC_ & VI_ */ /* extends super VCG_<?> & VIG_<?> */ /* extends super VCG_< E > & VIG_< E > */ /* extends super VCG_<String> & VIG_<String> */ > void fg_service( @NotNull final E p_1 )
		{
		
		
		
		return;
		}
		
		
		
	}
