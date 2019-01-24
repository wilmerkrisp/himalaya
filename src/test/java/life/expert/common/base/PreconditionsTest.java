package life.expert.common.base;









import static org.junit.jupiter.api.Assertions.*;

import org.jetbrains.annotations.*;                     //@NotNull
import com.google.errorprone.annotations.Immutable;     //@Immutable

import com.google.common.flogger.FluentLogger;          //log

import static java.text.MessageFormat.format;           //format string

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.common.collect.*;                     //ImmutableList
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.base.Preconditions.*;   //checkArgument

import static org.apache.commons.lang3.Validate.*;      //notEmpty(collection)


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
	
	
	
	private ImmutableList< String > badList_;
	
	
	
	private ImmutableList< String > goodList_;
	
	
	
	private ImmutableMap< String, String > badMap_;
	
	
	
	private ImmutableMap< String, String > goodMap_;
	
	
	
	@BeforeEach
	void setUp()
		{
		badList_ = ImmutableList.of( "1" ,
		                             "2" ,
		                             "" );
		goodList_ = ImmutableList.of( "1" ,
		                              "2" ,
		                              "3" );
		badMap_ = ImmutableMap.of( "1" ,
		                           "one" ,
		                           "2" ,
		                           "two" ,
		                           "3" ,
		                           "" );
		goodMap_ = ImmutableMap.of( "1" ,
		                            "one" ,
		                            "2" ,
		                            "two" ,
		                            "3" ,
		                            "three" );
		}
	
	
	
	@AfterEach
	void tearDown()
		{
		}
	
	
	
	@Test
	void getIndexesOfObjectsInCollection()
		{
		String output1 = Preconditions.getIndexesOfObjectsInCollection( goodList_ ,
		                                                                String::isBlank );
		assertTrue( output1.isBlank() );
		
		String output2 = Preconditions.getIndexesOfObjectsInCollection( badList_ ,
		                                                                String::isBlank );
		assertFalse( output2.isBlank() );
		}
	
	
	
	@Test
	void getIndexesOfObjectsInCollectionForLog()
		{
		String output1 = Preconditions.getIndexesOfObjectsInCollectionForLog( goodList_ ,
		                                                                      String::isBlank );
		assertTrue( output1.isBlank() );
		
		String output2 = Preconditions.getIndexesOfObjectsInCollectionForLog( badList_ ,
		                                                                      String::isBlank );
		assertFalse( output2.isBlank() );
		}
	
	
	
	@Test
	void getKeysOfObjectsInMap()
		{
		String output1 = Preconditions.getKeysOfObjectsInMap( goodMap_ ,
		                                                      String::isBlank );
		assertTrue( output1.isBlank() );
		
		String output2 = Preconditions.getKeysOfObjectsInMap( badMap_ ,
		                                                      String::isBlank );
		assertFalse( output2.isBlank() );
		}
	
	
	
	@Test
	void getKeysOfObjectsInMapForLog()
		{
		String output1 = Preconditions.getKeysOfObjectsInMap( goodMap_ ,
		                                                      String::isBlank );
		assertTrue( output1.isBlank() );
		
		String output2 = Preconditions.getKeysOfObjectsInMap( badMap_ ,
		                                                      String::isBlank );
		assertFalse( output2.isBlank() );
		}
	
	
	
	@Test
	void getCountOfObjectsInCollection()
		{
		long output1 = Preconditions.getCountOfObjectsInCollection( goodList_ ,
		                                                            String::isBlank );
		assertTrue( output1 == 0 );
		
		long output2 = Preconditions.getCountOfObjectsInCollection( badList_ ,
		                                                            String::isBlank );
		assertFalse( output2 == 0 );
		
		
		}
	
	
	
	@Test
	void getCountOfObjectsInMap()
		{
		long output1 = Preconditions.getCountOfObjectsInMap( goodMap_ ,
		                                                     String::isBlank );
		assertTrue( output1 == 0 );
		
		long output2 = Preconditions.getCountOfObjectsInMap( badMap_ ,
		                                                     String::isBlank );
		assertFalse( output2 == 0 );
		}
	
	
	
	@Test
	void checkCollection()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkCollection( badList_ ,
		                                                                String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		
		}
	
	
	
	@Test
	void checkMap()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkMap( badMap_ ,
		                                                         String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	@Test
	void checkCollectionRaiseIllegalStateException()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkCollectionRaiseIllegalStateException( badList_ ,
		                                                                                          String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	@Test
	void checkMapRaiseIllegalStateException()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkMapRaiseIllegalStateException( badMap_ ,
		                                                                                   String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	@Test
	void checkCollectionReturnBool()
		{
		boolean output1 = Preconditions.checkCollectionReturnBool( goodList_ ,
		                                                           String::isBlank );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkCollectionReturnBool( badList_ ,
		                                                           String::isBlank );
		assertTrue( output2 );
		}
	
	
	
	@Test
	void checkMapReturnBool()
		{
		boolean output1 = Preconditions.checkMapReturnBool( goodMap_ ,
		                                                    String::isBlank );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkMapReturnBool( badMap_ ,
		                                                    String::isBlank );
		assertTrue( output2 );
		}
	
	
	
	@Test
	void checkCollectionRaiseAssertion()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkCollectionRaiseAssertion( badList_ ,
		                                                                              String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	@Test
	void checkMapRaiseAssertion()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkMapRaiseAssertion( badMap_ ,
		                                                                       String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	@Test
	void checkStateLazyMessage()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkStateLazyMessage( false ,
		                                                                      () ->
		                                                                      {
		                                                                      throw new AssertionError( "Not lazy" );
		                                                                      } );
		                                 } );
		assertNotNull( thrown.getMessage() );
		
		Preconditions.checkStateLazyMessage( true ,
		                                     () ->
		                                     {
		                                     throw new IllegalArgumentException( "Not lazy" );
		                                     } );
		}
	
	
	
	@Test
	void checkArgumentLazyMessage()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgumentLazyMessage( false ,
		                                                                         () ->
		                                                                         {
		                                                                         throw new AssertionError( "Not lazy" );
		                                                                         } );
		                                 } );
		assertNotNull( thrown.getMessage() );
		
		Preconditions.checkArgumentLazyMessage( true ,
		                                        () ->
		                                        {
		                                        throw new IllegalArgumentException( "Not lazy" );
		                                        } );
		 
		}
	}