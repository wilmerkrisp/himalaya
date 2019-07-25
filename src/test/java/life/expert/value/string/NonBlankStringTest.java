package life.expert.value.string;


import static life.expert.common.reactivestreams.Preconditions.*; //reactive check


import lombok.NonNull;//@NOTNULL

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

//import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.core.publisher.SignalType;
import reactor.function.TupleUtils;

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;
import java.util.logging.Level;

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
//                          himalaya  life.expert.value.string
//                           wilmer 2019/07/23
//
//--------------------------------------------------------------------------------
@Slf4j
class NonBlankStringTest
	{
	
	@Test
	void monoOfTest()
		{
		String input = null;
		var f = justOrEmpty( input ).filter( StringUtils::isNotBlank )
		                            .single()
		                            .onErrorMap( illegalArgumentException( "Invalid param1" ) )
		                            .log( "debug" , Level.FINE , SignalType.ON_NEXT )
		                            .map( this::someMethod_ )                           // если плохо, если $method$_  вернет null
		                            //.flatMap( nullableFunction( this::$method$_ ) )  // если нормально для  $method$_  вернуть null
		                            .single();
		
		//var ff=checkNotNull( input ).then(just("NEW VALUE"));
		var ff=just(input).then(just("NEW VALUE"));
		ff.subscribe( logAtInfoConsumer("NEXTT") , logAtErrorConsumer("ERRORR") , logAtInfoRunnable("COMPLETEE") );
		
		
		}
	
	private String someMethod_( final String string )
		{
		logAtInfo( "tested method" );
		//throw new RuntimeException( " cause " );
		return "otvet+" + string;
		//return null;
		
				}
		
	}