package life.expert.common.graph;









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









/**
 * enum simple
 * !CHANGE_ME_DESCRIPTION!
 *
 *
 *
 *
 * 1) you can use extensible interface for enums  (for example pettern operation codes/opcodes)
 * then use generic restrictions for variables of such interface type:   <T extends Enum<T> & Operation>
 *
 *
 *
 * <pre>{@code
 *
 *
 * example 1
 *
 * 	        NodeVisitState v_enum = NodeVisitState.ONE;
 * 	        v_enum.c_test = "newvalue";
 *           NodeVisitState.TWO.c_test = "newvalue";
 * 	        v_enum.f_test();
 *
 *
 * example 5
 *               import static life.expert.common.graph.NodeVisitState.*;
 *               NodeVisitState v_enum =  ONE;                  //using withowt NodeVisitState.ONE
 *
 *
 * example 2
 *
 * 		    switch( v_enum )
 *                       {
 *                       case ONE:
 *                               log_.debug( "NodeVisitState main switch 1: " );
 *                               break;
 *                       case TWO:
 *                               log_.debug( "NodeVisitState main switch 2: " );
 *                               break;
 *                       case THREE:
 *                               log_.debug( "NodeVisitState main switch 3: " );
 *                               break;
 *                       case FOUR:
 *                               log_.debug( "NodeVisitState main switch 4: " );
 *                               break;
 *                       default:
 *                               log_.debug( "NodeVisitState main DEFAULT: " );
 *                       }
 *
 *
 *
 * example 3
 *
 *               for( NodeVisitState v_i : NodeVisitState.values( ) )
 *                       {
 *                       log_.debug( "NodeVisitState main for: " + v_i +" "+v_i.c_test );
 *                       }
 *
 *
 *
 *
 * example 4
 *
 *               v_enum == v_enum.TWO
 * v_enum.equals(v_enum.TWO)
 * v_enum.compareTo(v_enum.TWO)
 *
 *
 *
 * }</pre>
 */
public enum NodeVisitState
	
	{
		
		
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE;
		
		
		
	}
