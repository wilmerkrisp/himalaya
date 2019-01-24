package life.expert.common.base;









import static org.junit.jupiter.api.Assertions.*;

import org.jetbrains.annotations.*;                     //@NotNull
import com.google.errorprone.annotations.Immutable;     //@Immutable

import com.google.common.flogger.FluentLogger;          //log

import static java.text.MessageFormat.format;           //format string

import java.util.ResourceBundle;

import com.google.common.collect.*;                     //ImmutableList
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.base.Preconditions.*;   //checkArgument
import static life.expert.common.base.Preconditions.*;  //checkCollection
import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)
import static life.expert.common.base.Objects.*;        //deepCopyOfObject

import java.util.function.*;                            //producer supplier

import static cyclops.function.Memoize.*;               //memoizeSupplier
import static java.util.stream.Collectors.*;            //toList streamAPI
import static java.util.function.Predicate.*;           //isEqual streamAPI



//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.base
//                           wilmer 2019/01/24
//
//--------------------------------------------------------------------------------









class PreconditionsTest
	{
	
	
	
	@BeforeEach
	void setUp()
		{
		}
	
	
	
	@AfterEach
	void tearDown()
		{
		}
	
	
	
	@Test
	void getIndexesOfObjectsInCollection()
		{
		}
	
	
	
	@Test
	void getIndexesOfObjectsInCollectionForLog()
		{
		}
	
	
	
	@Test
	void getKeysOfObjectsInMap()
		{
		}
	
	
	
	@Test
	void getKeysOfObjectsInMapForLog()
		{
		}
	
	
	
	@Test
	void getCountOfObjectsInCollection()
		{
		}
	
	
	
	@Test
	void getCountOfObjectsInMap()
		{
		}
	
	
	
	@Test
	void checkCollection()
		{
		}
	
	
	
	@Test
	void checkMap()
		{
		}
	
	
	
	@Test
	void checkCollectionRaiseIllegalStateException()
		{
		}
	
	
	
	@Test
	void checkMapRaiseIllegalStateException()
		{
		}
	
	
	
	@Test
	void checkCollectionReturnBool()
		{
		}
	
	
	
	@Test
	void checkMapReturnBool()
		{
		}
	
	
	
	@Test
	void checkCollectionRaiseAssertion()
		{
		}
	
	
	
	@Test
	void checkMapRaiseAssertion()
		{
		}
	
	
	
	@Test
	void checkStateLazyMessage()
		{
		}
	
	
	
	@Test
	void checkArgumentLazyMessage()
		{
		}
	}