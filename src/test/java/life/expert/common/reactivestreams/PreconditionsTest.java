package life.expert.common.reactivestreams;



import static org.junit.jupiter.api.Assertions.*;

import lombok.NonNull;//@NOTNULL

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.*;   //checkArgument
//import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)

import org.apache.commons.lang3.StringUtils;            //isNotBlank
import reactor.function.TupleUtils;

import java.util.function.*;                            //producer supplier

import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.NullableUtils.*;//.map(nullableFunction)
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
import static life.expert.common.reactivestreams.Preconditions.*; //reactive check
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
//                          himalaya  life.expert.common.reactivestreams
//                           wilmer 2019/07/23
//
//--------------------------------------------------------------------------------

class PreconditionsTest
	{
	
	@Test
	void checkArgument1Test()
		{
		String input = "SUPERVALUE";
		var    f     = checkArgument( input , StringUtils::isNotBlank , "EMPTY" );
		f.subscribe( logAtInfoConsumer( "NEXT" ) , logAtErrorConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
		
		//f.map( TupleUtils.function( this::$method$_ ) ).single() ;
		}
	
	@Test
	void checkArgument2Test()
		{
		String input1 = "1";
		String input2 = "2";
		var    f      = checkArgument( input1 , input2 , ( x , y ) -> false , "EMPTY" );
		f.subscribe( logAtInfoConsumer( "NEXT" ) , logAtErrorConsumer( "ERROR" ) , logAtInfoRunnable( "COMPLETE" ) );
		
		//f.map( TupleUtils.function( this::$method$_ ) ).single() ;
		}
		
	}