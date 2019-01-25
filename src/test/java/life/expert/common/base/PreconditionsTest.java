package life.expert.common.base;









import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.*;                     //ImmutableList
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



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
	
	private ImmutableList< String > emptyList_;
	
	private ImmutableMap< String, String > emptyMap_;
	
	
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
		
		
		emptyList_=ImmutableList.of();
		
		emptyMap_=ImmutableMap.of();
		}
	
	
	
	@AfterEach
	void tearDown()
		{
		}
	
	
	//<editor-fold desc="Check collections and maps">
	@Test
	void checkArgument_Collection_filter()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument( badList_ ,
		                                                              String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		
		}
	
	
	
	@Test
	void checkArgument_Map_filter()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument( badMap_ ,
		                                                              String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void checkArgumentThen_Collection_filter()
		{
		boolean output1 = Preconditions.checkArgumentThen( goodList_ ,
		                                                   String::isBlank );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkArgumentThen( badList_ ,
		                                                   String::isBlank );
		assertTrue( output2 );
		}
	
	
	
	@Test
	void checkArgumentThen_Map_filter()
		{
		boolean output1 = Preconditions.checkArgumentThen( goodMap_ ,
		                                                   String::isBlank );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkArgumentThen( badMap_ ,
		                                                   String::isBlank );
		assertTrue( output2 );
		}
	
	
	
	@Test
	void checkState_Collection_filter()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( badList_ ,
		                                                           String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	@Test
	void checkState_Map_filter()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( badMap_ ,
		                                                           String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void assertArgument_Collection_filter()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgument( badList_ ,
		                                                               String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	@Test
	void assertArgument_Map_filter()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgument( badMap_ ,
		                                                               String::isBlank );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="Generic checks">
	
	@Test
	void checkArgument_bool()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument( false , ()->"test" );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	@Test
	void checkArgument_bool_LazyMessage()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument( false ,
		                                                              () ->
		                                                              {
		                                                              throw new AssertionError( "Not lazy" );
		                                                              } );
		                                 } );
		assertNotNull( thrown.getMessage() );
		
		Preconditions.checkArgument( true ,
		                             () ->
		                             {
		                             throw new IllegalArgumentException( "Not lazy" );
		                             } );
			
		}
	
	@Test
	void checkState_bool()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( false,()->"test" );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	@Test
	void checkState_bool_LazyMessage()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( false ,
		                                                           () ->
		                                                           {
		                                                           throw new AssertionError( "Not lazy" );
		                                                           } );
		                                 } );
		assertNotNull( thrown.getMessage() );
		
		Preconditions.checkState( true ,
		                          () ->
		                          {
		                          throw new IllegalArgumentException( "Not lazy" );
		                          } );
		}
	
	@Test
	void assertArgument_bool()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgument( false,()->"test"   );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	
	@Test
	void checkArgumentNotNull()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgumentNotNull( null , ()->"test" );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void checkStateNotNull()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkStateNotNull( null,()->"test" );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void assertArgumentNotNull()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgumentNotNull( null,()->"test"   );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="Check strings">
	@Test
	void checkArgument_String()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument( "  "   );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void checkArgumentThen_String()
		{
		boolean output1 = Preconditions.checkArgumentThen( "d"   );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkArgumentThen( "   " );
		assertTrue( output2 );
		}
	
	@Test
	void checkState_String()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( "    ");
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void assertArgument_String()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgument( "    ");
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="Check numbers">
	
	
	@Test
	void checkArgument_Long()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument( 0  );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void checkArgumentThen_Long()
		{
		boolean output1 = Preconditions.checkArgumentThen( 1  );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkArgumentThen( 0 );
		assertTrue( output2 );
		}
	
	@Test
	void checkState_Long()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( 0);
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void assertArgument_Long()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgument( 0);
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	//</editor-fold>
	
	//<editor-fold desc="Check collection and maps (subfunctions)">
	
	@Test
	void checkArgument_Collection()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument( emptyList_ );
		                                 } );
		assertNotNull( thrown.getMessage() );
		
		}
	
	
	@Test
	void checkArgumentThen_Collection()
		{
		boolean output1 = Preconditions.checkArgumentThen(badList_ );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkArgumentThen( emptyList_  );
		assertTrue( output2 );
		}
	
	@Test
	void checkState_Collection()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( emptyList_ );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	@Test
	void assertArgument_Collection()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgument(emptyList_ );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void checkArgument_Map()
		{
		Throwable thrown = assertThrows( IllegalArgumentException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkArgument(emptyMap_ );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	@Test
	void checkArgumentThen_Map()
		{
		boolean output1 = Preconditions.checkArgumentThen( badMap_ );
		assertFalse( output1 );
		
		boolean output2 = Preconditions.checkArgumentThen( emptyMap_);
		assertTrue( output2 );
		}
	
	@Test
	void checkState_Map()
		{
		Throwable thrown = assertThrows( IllegalStateException.class ,
		                                 () ->
		                                 {
		                                 Preconditions.checkState( emptyMap_ );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	@Test
	void assertArgument_Map()
		{
		Throwable thrown = assertThrows( AssertionError.class ,
		                                 () ->
		                                 {
		                                 Preconditions.assertArgument( emptyMap_ );
		                                 } );
		assertNotNull( thrown.getMessage() );
		}
	
	
	
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="Log functions">
	
	
	
	
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
	
	
	
	
	
	

	
	
	
	
	}