package life.expert.common.reactivestreams;



import io.vavr.Tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import static life.expert.common.base.Preconditions.*;  //checkCollection

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static reactor.core.publisher.Mono.*;
import static life.expert.common.async.LogUtils.*;        //logAtInfo
import static life.expert.common.reactivestreams.Patterns.*;
import static life.expert.common.reactivestreams.ForComprehension.*;
//import static life.expert.common.function.Patterns.*;    //for-comprehension

import static io.vavr.API.$;                            // pattern matching
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.CheckedFunction;//checked functions
import static io.vavr.API.unchecked;    //checked->unchecked
import static io.vavr.API.Function;     //lambda->Function3
import static io.vavr.API.Tuple;

//import java.util.List;                                  //usual list
//import io.vavr.collection.List;                         //immutable List
//import com.google.common.collect.*;                     //ImmutableList

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.reactivestreams
//                           wilmer 2019/07/31
//
//--------------------------------------------------------------------------------

class ForComprehensionTest
	{
	
	@BeforeEach
	void setUp()
		{
		}
	
	
	//<editor-fold desc="Flux for comprehension testing">
	@Test
	void For_flux()
		{
		var f1  = range( 1 , 10 );
		var f2  = range( 11 , 20 );
		var rez = For( f1 , f2 ).yield( Tuple::of );
		//rez.subscribe( logAtInfoConsumer("NEXT") , logAtErrorConsumer("ERROR") , logAtInfoRunnable("COMPLETE") );
		
		StepVerifier.create( rez )
		            .expectNextCount( 100 )
		            .expectComplete()
		            .verify();
		}
	//</editor-fold>
	
	}