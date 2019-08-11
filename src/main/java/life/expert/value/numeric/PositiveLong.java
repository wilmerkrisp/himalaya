package life.expert.value.string;
//---------------------------------------------
//      ___       __        _______   ______
//     /   \     |  |      /  _____| /  __  \
//    /  ^  \    |  |     |  |  __  |  |  |  |
//   /  /_\  \   |  |     |  | |_ | |  |  |  |
//  /  _____  \  |  `----.|  |__| | |  `--'  |
// /__/     \__\ |_______| \______|  \______/
//
//               wilmer 2019/08/08
//---------------------------------------------

import com.google.common.collect.ComparisonChain;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;//@NOTNULL

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Optional;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
//import static  reactor.function.TupleUtils.*; //reactor's tuple->R INTO func->R
import static life.expert.common.function.TupleUtils.*; //vavr's tuple->R INTO func->R

import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.function.NullableUtils.*;//.map(nullableFunction)
import static life.expert.common.function.CheckedUtils.*;// .map(consumerToBoolean)
import static life.expert.common.reactivestreams.Preconditions.*; //reactive check
import static life.expert.common.reactivestreams.Patterns.*;    //reactive helper functions
import static life.expert.common.base.Objects.*;          //deepCopyOfObject
import static life.expert.common.reactivestreams.ForComprehension.*; //reactive for-comprehension

import static cyclops.control.Trampoline.more;
import static cyclops.control.Trampoline.done;

//import static io.vavr.API.*;                           //conflicts with my reactive For-comprehension

import static io.vavr.API.$;                            // pattern matching
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.*;                         //switch - case - success/failure
import static io.vavr.Predicates.*;                       //switch - case
//import static java.util.function.Predicate.*;           //isEqual streamAPI

import static io.vavr.API.CheckedFunction;//checked functions
import static io.vavr.API.unchecked;    //checked->unchecked
import static io.vavr.API.Function;     //lambda->Function3
import static io.vavr.API.Tuple;

import static io.vavr.API.Try;          //Try

import io.vavr.control.Try;                               //try
import reactor.core.publisher.Mono;

import static io.vavr.API.Failure;
import static io.vavr.API.Success;
import static io.vavr.API.Left;         //Either
import static io.vavr.API.Right;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;

import io.vavr.Tuple;
import io.vavr.Tuple1;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.value.string
//                           wilmer 2019/08/08
//
//--------------------------------------------------------------------------------

@Value
@AllArgsConstructor( access = AccessLevel.PRIVATE )
@Patterns /*pattern matching in vavr*/
@Slf4j
public final class PositiveLong
	implements Comparable<PositiveLong>
	{
	
	/**
	 * item1
	 *
	 * -- SETTER --
	 *
	 * @param item1
	 * 	item1
	 * @return item1
	 *
	 * 	-- GETTER --
	 * @return item1
	 * 	the item1
	 */
	@NonNull private final long longNumber;
	
	@Deprecated
	public static PositiveLong of( final int number )
		{
		return tryOf( number ).get();
		}
	
	public static Try<PositiveLong> tryOf( final int number )
		{
		if( number < 1 )
			return illegalArgumentFailure( "Input argument must posititve >0 ." );
		else
			return Success( new PositiveLong( number ) );
			
		}
	
	public static Optional<PositiveLong> optionalOf( final int item1 )
		{
		return tryOf( item1 ).toJavaOptional();
		}
	
	public static Mono<PositiveLong> monoOf( final int item1 )
		{
		return monoFromTry( tryOf( item1 ) );
		}
	
	@Unapply
	static Tuple1<Long> PositiveLong( PositiveLong object )
		{
		return Tuple.of( object.getLongNumber() );
		}
	
	@Override
	public String toString()
		{
		return "" + this.longNumber;
		}
	
	@Override
	public int compareTo( PositiveLong o )
		{
		return ComparisonChain.start()
		                      .compare( this.longNumber , o.longNumber )
		                      .result();
		}
	}

