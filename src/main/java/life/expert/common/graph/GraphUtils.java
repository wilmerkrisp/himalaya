package life.expert.common.graph;









import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;



import lombok.NonNull;//@NOTNULL

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank


import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;



import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.NullableUtils.*;//.map(nullableFunction)
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
//import static life.expert.common.function.Patterns.*;    //for-comprehension
import static life.expert.common.base.Objects.*;          //deepCopyOfObject

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case
import static io.vavr.Patterns.*;                         //switch - case - success/failure
import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;



//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.graph
//                           wilmer 2019/07/11
//
//--------------------------------------------------------------------------------



//<editor-fold desc=".">
/*

- private конструктор уже включен аннотацией @UtilityClass

- сервисные методы делать статик методами на классе с private конструктором а не на интерфейсе тк методы обработки могут содержать состояние и кеши
- интрфейсы использовать только для задания типа


*/
//</editor-fold>









/**
 * service (static class)
 *
 * <pre>{@code
 *               GraphUtils.compute();
 *               var s=GraphUtils.MY_CONSTANT;
 * }</pre>
 */
@UtilityClass
@Slf4j
public final class GraphUtils
	{
	
	
	
	private static final ResourceBundle bundle_        = ResourceBundle.getBundle( "messages" );
	
	
	
	private static final String         HELLO_MESSAGE_ = bundle_.getString( "hello" );
	
	
	
	/**
	 * some constant
	 */
	public static final String MY_CONSTANT = new String( "Test string." );
	
	
	
	private static void compute_()
		{
		return;
		}
	
	
	
	/**
	 * static public method
	 *
	 * <pre>{@code
	 *           GraphUtils.compute();
	 * }</pre>
	 */
	public static void compute()
		{
		return;
		}
	
	
	
	/**
	 * static generic method
	 *
	 * <pre>{@code
	 *           GraphUtils.myCompute("stroka");
	 *           GraphUtils.myCompute(12);
	 * }</pre>
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	public static <E   /* extends super VC_ & VI_ */ /* extends super VCG_<?> & VIG_<?> */ /* extends super VCG_< E > & VIG_< E > */ /* extends super VCG_<String> & VIG_<String> */> void myCompute( @NonNull final E object )
		{
		return;
		}
		
		
		
	}
