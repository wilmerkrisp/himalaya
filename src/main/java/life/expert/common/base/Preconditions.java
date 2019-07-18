package life.expert.common.base;









import org.jetbrains.annotations.*;


import static java.util.stream.Collectors.*;        //toList

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.*;


import com.google.common.collect.*;



//@Header@
//--------------------------------------------------------------------------------
//                              life.expert.testfiles
//--------------------------------------------------------------------------------

//









/**
 * - methods for checking strings and collections (the presence of an element in the collection, the emptiness of elements, etc.)
 * as well as advanced logging methods display indices of empty elements, etc.
 *
 * - contain categories of side effects: assert (raise assertion),checkArgument (raise illegal argument exception),checkState (raise illegal state exception)
 */
public final class Preconditions
	{
	
	
	
	// width of debug info window
	private static final int DEBUG_WINDOW_NUM_COL = 80;
	
	
	
	private static final String MESSAGE_WRONG_NUMBER = "Number argument should'not be less than zero.";
	
	
	
	private static final String MESSAGE_WRONG_STRING_NULL = "String argument should'not be null.";
	
	
	
	private static final String MESSAGE_WRONG_STRING_EMPTY = "String argument should'not \"\" be empty (not null but length=0).";
	
	
	
	private static final String MESSAGE_WRONG_STRING_BLANK = "String argument should'not be \" \" blank (not empty but some spaces here).";
	
	
	
	private static final String MESSAGE_WRONG_COLLECTION = "%d wrong elements in collection \n Indexes of wrong elements: %s \n Collection: %s ";
	
	
	
	private static final String MESSAGE_WRONG_COLLECTION_NULL = "Argument(collection) should'not be null.";
	
	
	
	private static final String MESSAGE_WRONG_COLLECTION_EMPTY = "Argument(Collection) should'not be empty (not null but length=0):";
	
	
	
	private static final String MESSAGE_WRONG_MAP = "%d wrong entries in map \n Keys of wrong entries: %s \n Map: %s ";
	
	
	
	private static final String MESSAGE_WRONG_MAP_NULL = "Argument(map) should'not be null.";
	
	
	
	private static final String MESSAGE_WRONG_MAP_EMPTY = "Argument(map) should'not be empty (not null but length=0):";
	
	
	
	/**
	 * The constant goodLong.
	 */
	public static final LongPredicate goodLong = l -> l > 0;
	
	
	
	/**
	 * The constant badLong.
	 */
	public static final LongPredicate badLong = l -> l <= 0;
	
	
	
	/**
	 * The constant goodString.
	 */
	public static final Predicate<String> goodString = s -> s != null && !s.isEmpty() && !s.isBlank();
	
	
	
	/**
	 * The constant badString.
	 */
	public static final Predicate<String> badString = s -> s == null || s.isEmpty() || s.isBlank();
	
	
	
	/**
	 * The constant goodCollection.
	 */
	public static final Predicate<? extends Collection<?>> goodCollection = c -> c != null && !c.isEmpty();
	
	
	
	/**
	 * The constant badCollection.
	 */
	public static final Predicate<? extends Collection<?>> badCollection = c -> c == null || c.isEmpty();
	
	
	
	/**
	 * The constant goodMap.
	 */
	public static final Predicate<? extends Map<?,?>> goodMap = m -> m != null && !m.isEmpty();
	
	
	
	/**
	 * The constant badMap.
	 */
	public static final Predicate<? extends Map<?,?>> badMap = m -> m == null || m.isEmpty();
	
	
	
	/**
	 * None match in collection predicate.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param p
	 * 	the p
	 *
	 * @return the predicate
	 */
	@NotNull
	public static final <E> Predicate<? super Collection<E>> anyMatchInCollection( @Nullable Predicate<E> p )
		{
		return c -> c.stream()
		             .anyMatch( p == null ? Objects::isNull : p );
		}
	
	
	
	/**
	 * None match in map predicate.
	 *
	 * @param <EKey>
	 * 	the type parameter
	 * @param <EValue>
	 * 	the type parameter
	 * @param p
	 * 	the p
	 *
	 * @return the predicate
	 */
	@NotNull
	public static final <EKey, EValue> Predicate<? super Map<EKey,EValue>> anyMatchInMap( @Nullable Predicate<EValue> p )
		{
		return m -> m.entrySet()
		             .stream()
		             .map( Map.Entry::getValue )
		             .anyMatch( p == null ? Objects::isNull : p );
		}
	
	
	
	/**
	 * None match in collection predicate.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param p
	 * 	the p
	 *
	 * @return the predicate
	 */
	@NotNull
	public static final <E> Predicate<? super Collection<E>> noneMatchInCollection( @Nullable Predicate<E> p )
		{
		return anyMatchInCollection( p ).negate();
		}
	
	
	
	/**
	 * None match in map predicate.
	 *
	 * @param <EKey>
	 * 	the type parameter
	 * @param <EValue>
	 * 	the type parameter
	 * @param p
	 * 	the p
	 *
	 * @return the predicate
	 */
	@NotNull
	public static final <EKey, EValue> Predicate<? super Map<EKey,EValue>> noneMatchInMap( @Nullable Predicate<EValue> p )
		{
		return ( (Predicate<? super Map<EKey,EValue>>) anyMatchInMap( p ) ).negate();
		}
	
	
	
	@NotNull
	private static final <E> Supplier<String> wrongCollectionMessage( @NotNull Collection<E> collection ,
	                                                                  @NotNull Predicate<E> invalidElement )
		{
		return () -> String.format( MESSAGE_WRONG_COLLECTION , getCountOfObjectsInCollection( collection , invalidElement ) , getIndexesOfObjectsInCollectionForLog( collection , invalidElement ) , collection.getClass() );
		}
	
	
	
	@NotNull
	private static <EKey, EValue> Supplier<String> wrongMapMessage( @NotNull Map<EKey,EValue> map ,
	                                                                @NotNull Predicate<EValue> invalidValue )
		
		{
		return () -> String.format( MESSAGE_WRONG_MAP , getCountOfObjectsInMap( map , invalidValue ) , getKeysOfObjectsInMapForLog( map , invalidValue ) , map.getClass() );
		}
	
	
	
	//<editor-fold desc="Megabase functions">
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 *
	 * @return the e
	 */
	public static <E> boolean isCheckArgument( E argument ,
	                                           @Nullable Predicate<E> wrong )
		{
		wrong = wrong == null ? Objects::isNull : wrong;
		return wrong.test( argument );
		}
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E checkArgument( E argument ,
	                                   @Nullable Predicate<E> wrong ,
	                                   @Nullable String errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalArgumentException( errorMessage == null ? "" : errorMessage );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E checkArgument( E argument ,
	                                   @Nullable Predicate<E> wrong ,
	                                   @Nullable Supplier<String> errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalArgumentException( errorMessage == null ? "" : errorMessage.get() );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E checkState( E argument ,
	                                @Nullable Predicate<E> wrong ,
	                                @Nullable String errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalStateException( errorMessage == null ? "" : errorMessage );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E checkState( E argument ,
	                                @Nullable Predicate<E> wrong ,
	                                @Nullable Supplier<String> errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalStateException( errorMessage == null ? "" : errorMessage.get() );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E assertArgument( E argument ,
	                                    @Nullable Predicate<E> wrong ,
	                                    @Nullable String errorMessage )
		{
		assert !isCheckArgument( argument , wrong ) :
			errorMessage == null ? "" : errorMessage;
		
		return argument;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E assertArgument( E argument ,
	                                    @Nullable Predicate<E> wrong ,
	                                    @Nullable Supplier<String> errorMessage )
		{
		assert !isCheckArgument( argument , wrong ) :
			errorMessage == null ? "" : errorMessage.get();
		
		return argument;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param expression
	 * 	the expression
	 * @param errorMessage
	 * 	the error message
	 */
	public static void checkArgument( boolean expression ,
	                                  @Nullable Supplier<String> errorMessage )
		{
		if( !expression )
			{
			throw new IllegalArgumentException( errorMessage == null ? "" : errorMessage.get() );
			}
		}
	
	
	
	/**
	 * Check state, lazy message.
	 *
	 * @param expression
	 * 	the expression
	 * @param errorMessage
	 * 	the error message
	 */
	public static void checkState( boolean expression ,
	                               @Nullable Supplier<String> errorMessage )
		{
		if( !expression )
			{
			throw new IllegalStateException( errorMessage == null ? "" : errorMessage.get() );
			}
		}
	
	
	
	/**
	 * Check argument
	 *
	 * @param expression
	 * 	the expression
	 * @param errorMessage
	 * 	the error message
	 */
	public static void assertArgument( boolean expression ,
	                                   @Nullable Supplier<String> errorMessage )
		{
		assert expression :
			errorMessage == null ? "" : errorMessage.get();
		}
	
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="Generic checks">
	
	
	
	/**
	 * Check argument not null e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E checkArgumentNotNull( E argument ,
	                                          @Nullable Supplier<String> errorMessage )
		{
		checkArgument( argument , Objects::isNull , errorMessage );
		return argument;
		}
	
	
	
	/**
	 * Check state not null e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E checkStateNotNull( E argument ,
	                                       @Nullable Supplier<String> errorMessage )
		{
		checkState( argument , Objects::isNull , errorMessage );
		return argument;
		}
	
	
	
	/**
	 * Check argument not null e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param argument
	 * 	the argument
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E assertArgumentNotNull( E argument ,
	                                           @Nullable Supplier<String> errorMessage )
		{
		assertArgument( argument , Objects::isNull , errorMessage );
		return argument;
		}
	
	
	//</editor-fold>
	
	//<editor-fold desc="Check collections and maps">
	
	
	
	/**
	 * Helper method for checking input args of collection type or constructor in immutable colls
	 * Checking every element in collection with Predicate  (usual test for nullable or empty elements)
	 *
	 * If you need simply test collection for empty elements use Apache utils's Validate.notEmpty method instead
	 *
	 *
	 * For example, you can use the method (which raise IllegalArgumentException)
	 * - inside constructors of immutable objects
	 * - inside methods for testing inputs arguments
	 * <pre>{@code
	 *      checkCollection(c,String::isBlank);
	 *      checkCollection(c,Objects::isNull);
	 * }************************</pre>
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param invalidElement
	 * 	the filter
	 *
	 * @return the t
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 * @throws IllegalArgumentException
	 * 	if argument empty (or if one collection's element is empty)
	 */
	public static <E> Collection<E> checkArgument( Collection<E> collection ,
	                                               @Nullable Predicate<E> invalidElement )
		{
		checkArgument( collection );
		
		checkArgument( collection , anyMatchInCollection( invalidElement ) , wrongCollectionMessage( collection , invalidElement ) );
		return collection;
		}
	
	
	
	/**
	 * Helper method for checking input args of map type or constructor in immutable colls
	 * Checking every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * If you need simply test map for empty elements use Apache utils's Validate.notEmpty method instead
	 *
	 * For example, you can use the method (which raise IllegalArgumentException)
	 * - inside constructors of immutable objects
	 * - inside methods for testing inputs arguments
	 * <pre>{@code
	 *      checkMap(m,String::isBlank);
	 *      checkMap(m,Objects::isNull);
	 * }************************</pre>
	 *
	 * @param <EKey>
	 * 	the type parameter
	 * @param <EValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param invalidValue
	 * 	the filter
	 *
	 * @return the map
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 * @throws IllegalArgumentException
	 * 	if argument empty (or if one map's element is empty)
	 */
	public static <EKey, EValue> Map<EKey,EValue> checkArgument( Map<EKey,EValue> map ,
	                                                             @Nullable Predicate<EValue> invalidValue )
		{
		checkArgument( map );
		
		checkArgument( map , anyMatchInMap( invalidValue ) , wrongMapMessage( map , invalidValue ) );
		return map;
		}
	
	
	
	/**
	 * Helper method for strategy "return Optional instead Exception"
	 * Check argument of type Collection
	 * Checking every element in collection with Predicate  (usual test for nullable or empty elements)
	 *
	 * For example, you can use the method (if you need return optional in your method instead raise exception)
	 * - inside methods for testing inputs arguments
	 * - inside methods of mutable objects for checking object's state before execution
	 * <pre>{@code
	 *      if ( checkCollectionIsEmpty(c,String::isBlank) )
	 *              return Optional.empty();
	 *
	 *
	 *      if ( checkCollectionIsEmpty(c,Objects::isNull) )
	 *              return Optional.empty();
	 * }************************</pre>
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param invalidElement
	 * 	the filter
	 *
	 * @return the boolean
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	public static <E> boolean isCheckArgument( Collection<E> collection ,
	                                           @Nullable Predicate<E> invalidElement )
		{
		//return !( (Predicate< Collection< E > >) goodCollection ).and( noneMatchInCollection( invalidElement ) ).test( collection );
		return ( (Predicate<Collection<E>>) badCollection ).or( anyMatchInCollection( invalidElement ) )
		                                                   .test( collection );
		}
	
	
	
	/**
	 * Helper method for strategy "return Optional instead Exception"
	 * Check argument of type Map
	 * Checking every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * For example, you can use the method (if you need return optional in your method instead raise exception)
	 * - inside methods for testing inputs arguments
	 * - inside methods of mutable objects for checking object's state before execution
	 * <pre>{@code
	 *      if ( checkMapIsEmpty(m,String::isBlank) )
	 *              return Optional.empty();
	 *
	 *
	 *      if ( checkMapIsEmpty(m,Objects::isNull) )
	 *              return Optional.empty();
	 * }************************</pre>
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param invalidValue
	 * 	the filter
	 *
	 * @return the boolean
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	public static <TKey, TValue> boolean isCheckArgument( Map<TKey,TValue> map ,
	                                                      @Nullable Predicate<TValue> invalidValue )
		{
		//return !( (Predicate< Map< TKey, TValue > >) goodMap ).and( noneMatchInMap( invalidValue ) ).test( map );
		return ( (Predicate<Map<TKey,TValue>>) badMap ).or( anyMatchInMap( invalidValue ) )
		                                               .test( map );
		}
	
	
	
	/**
	 * Helper method for use in mutable objects if you want to check some object property before method work
	 * Check argument of type Collection
	 * Checking every element in collection with Predicate  (usual test for nullable or empty elements)
	 *
	 * For example, you can use the method (which raise IllegalStateException)
	 * - inside methods of mutable objects for checking object's state before execution
	 * <pre>{@code
	 *      checkCollectionRaiseIllegalStateException(c,String::isBlank);
	 *      checkCollectionRaiseIllegalStateException(c,Objects::isNull);
	 * }************************</pre>
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param invalidElement
	 * 	the filter
	 *
	 * @return the t
	 *
	 * @throws IllegalStateException
	 * 	if argument nullable
	 * @throws IllegalStateException
	 * 	if argument empty (or if one collection's element is empty)
	 */
	public static <T extends Collection<E>, E> T checkState( T collection ,
	                                                         @Nullable Predicate<E> invalidElement )
		{
		checkState( collection );
		
		checkState( collection , anyMatchInCollection( invalidElement ) , wrongCollectionMessage( collection , invalidElement ) );
		return collection;
		}
	
	
	
	/**
	 * Helper method for use in mutable objects if you want to check some object property before method work
	 * Check argument of type Map
	 * Checking every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * For example, you can use the method (which raise IllegalStateException)
	 * - inside methods of mutable objects for checking object's state before execution
	 * <pre>{@code
	 *      checkMapRaiseIllegalStateException(c,String::isBlank);
	 *      checkMapRaiseIllegalStateException(c,Objects::isNull);
	 * }************************</pre>
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param invalidValue
	 * 	the filter
	 *
	 * @return the map
	 *
	 * @throws IllegalStateException
	 * 	if argument nullable
	 * @throws IllegalStateException
	 * 	if argument empty (or if one map's element is empty)
	 */
	public static <TKey, TValue> Map<TKey,TValue> checkState( Map<TKey,TValue> map ,
	                                                          @Nullable Predicate<TValue> invalidValue )
		{
		checkState( map );
		
		checkState( map , anyMatchInMap( invalidValue ) , wrongMapMessage( map , invalidValue ) );
		return map;
		}
	
	
	
	/**
	 * Check argument of type Collection
	 * Checking every element in collection with Predicate (usual test for nullable or empty elements)
	 *
	 * Usually this method for check input args in private methods for raise assertion instead exception.
	 * <pre>{@code
	 *      checkCollectionRaiseAssertion(c,String::isBlank);
	 *      checkCollectionRaiseAssertion(c,Objects::isNull);
	 * }************************</pre>
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param invalidElement
	 * 	the filter
	 *
	 * @return the t
	 *
	 * @throws AssertionError
	 * 	if any match
	 */
	public static <T extends Collection<E>, E> T assertArgument( T collection ,
	                                                             @Nullable Predicate<E> invalidElement )
		{
		assertArgument( collection );
		
		assertArgument( collection , anyMatchInCollection( invalidElement ) , wrongCollectionMessage( collection , invalidElement ) );
		return collection;
		}
	
	
	
	/**
	 * Check argument of type Map
	 * Checking every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * Usually this method for check input args in private methods for raise assertion instead exception.
	 * <pre>{@code
	 *      checkMapRaiseAssertion(c,String::isBlank);
	 *      checkMapRaiseAssertion(c,Objects::isNull);
	 * }************************</pre>
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param invalidValue
	 * 	the filter
	 *
	 * @return the map
	 *
	 * @throws AssertionError
	 * 	if any match
	 */
	public static <TKey, TValue> Map<TKey,TValue> assertArgument( Map<TKey,TValue> map ,
	                                                              @Nullable Predicate<TValue> invalidValue )
		{
		assertArgument( map );
		
		assertArgument( map , anyMatchInMap( invalidValue ) , wrongMapMessage( map , invalidValue ) );
		return map;
		}
	
	
	
	//</editor-fold>
	
	//<editor-fold desc="Check collection and maps (subfunctions)">
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 *
	 * @return the e
	 */
	public static <E extends Collection<?>> E checkArgument( E collection )
		{
		checkArgument( collection , Objects::isNull , MESSAGE_WRONG_COLLECTION_NULL );
		checkArgument( collection , Collection::isEmpty , MESSAGE_WRONG_COLLECTION_EMPTY );
		return collection;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param collection
	 * 	the collection
	 *
	 * @return the e
	 */
	public static boolean isCheckArgument( Collection<?> collection )
		{
		return ( (Predicate<Collection<?>>) badCollection ).test( collection );
		}
	
	
	
	/**
	 * Check state, lazy message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 *
	 * @return the e
	 */
	public static <E extends Collection<?>> E checkState( E collection )
		{
		checkState( collection , Objects::isNull , MESSAGE_WRONG_COLLECTION_NULL );
		checkState( collection , Collection::isEmpty , MESSAGE_WRONG_COLLECTION_EMPTY );
		return collection;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 *
	 * @return the string
	 */
	public static <E extends Collection<?>> E assertArgument( E collection )
		{
		assertArgument( collection , Objects::isNull , MESSAGE_WRONG_COLLECTION_NULL );
		assertArgument( collection , Collection::isEmpty , MESSAGE_WRONG_COLLECTION_EMPTY );
		return collection;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 *
	 * @return the e
	 */
	public static <TKey, TValue> Map<TKey,TValue> checkArgument( Map<TKey,TValue> map )
		{
		checkArgument( map , Objects::isNull , MESSAGE_WRONG_MAP_NULL );
		checkArgument( map , Map::isEmpty , MESSAGE_WRONG_MAP_EMPTY );
		return map;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param map
	 * 	the map
	 *
	 * @return the e
	 */
	//public static < TKey, TValue > boolean isCheckArgument(  Map< TKey, TValue > map )
	public static boolean isCheckArgument( Map<?,?> map )
		{
		return ( (Predicate<Map<?,?>>) badMap ).test( map );
		}
	
	
	
	/**
	 * Check state, lazy message.
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 *
	 * @return the e
	 */
	public static <TKey, TValue> Map<TKey,TValue> checkState( Map<TKey,TValue> map )
		{
		checkState( map , Objects::isNull , MESSAGE_WRONG_MAP_NULL );
		checkState( map , Map::isEmpty , MESSAGE_WRONG_MAP_EMPTY );
		return map;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 *
	 * @return the string
	 */
	public static <TKey, TValue> Map<TKey,TValue> assertArgument( Map<TKey,TValue> map )
		{
		assertArgument( map , Objects::isNull , MESSAGE_WRONG_MAP_NULL );
		assertArgument( map , Map::isEmpty , MESSAGE_WRONG_MAP_EMPTY );
		return map;
		}
	
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="Check strings">
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the string
	 */
	public static String checkArgument( String string )
		{
		checkArgument( string , Objects::isNull , MESSAGE_WRONG_STRING_NULL );
		checkArgument( string , String::isEmpty , MESSAGE_WRONG_STRING_EMPTY );
		checkArgument( string , String::isBlank , MESSAGE_WRONG_STRING_BLANK );
		return string;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the string
	 */
	public static boolean isCheckArgument( String string )
		{
		return badString.test( string );
		}
	
	
	
	/**
	 * Check state, lazy message.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the string
	 */
	public static String checkState( String string )
		{
		checkState( string , Objects::isNull , MESSAGE_WRONG_STRING_NULL );
		checkState( string , String::isEmpty , MESSAGE_WRONG_STRING_EMPTY );
		checkState( string , String::isBlank , MESSAGE_WRONG_STRING_BLANK );
		return string;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param string
	 * 	the string
	 *
	 * @return the string
	 */
	public static String assertArgument( String string )
		{
		assertArgument( string , Objects::isNull , MESSAGE_WRONG_STRING_NULL );
		assertArgument( string , String::isEmpty , MESSAGE_WRONG_STRING_EMPTY );
		assertArgument( string , String::isBlank , MESSAGE_WRONG_STRING_BLANK );
		return string;
		}
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="Check numbers (megabase)  ">
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 *
	 * @return the e
	 */
	public static boolean isCheckArgument( long argument ,
	                                       @Nullable LongPredicate wrong )
		{
		wrong = wrong == null ? l -> l == 0 : wrong;
		return wrong.test( argument );
		}
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static long checkArgument( long argument ,
	                                  @Nullable LongPredicate wrong ,
	                                  @Nullable String errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalArgumentException( errorMessage == null ? "" : errorMessage );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static long checkArgument( long argument ,
	                                  @Nullable LongPredicate wrong ,
	                                  @Nullable Supplier<String> errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalArgumentException( errorMessage == null ? "" : errorMessage.get() );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static long checkState( long argument ,
	                               @Nullable LongPredicate wrong ,
	                               @Nullable String errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalStateException( errorMessage == null ? "" : errorMessage );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static long checkState( long argument ,
	                               @Nullable LongPredicate wrong ,
	                               @Nullable Supplier<String> errorMessage )
		{
		if( isCheckArgument( argument , wrong ) )
			{
			throw new IllegalStateException( errorMessage == null ? "" : errorMessage.get() );
			}
		return argument;
		}
	
	
	
	/**
	 * Check argument,  message.
	 *
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static long assertArgument( long argument ,
	                                   @Nullable LongPredicate wrong ,
	                                   @Nullable String errorMessage )
		{
		
		assert !isCheckArgument( argument , wrong ) :
			errorMessage == null ? "" : errorMessage;
		return argument;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param argument
	 * 	the argument
	 * @param wrong
	 * 	the that not
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static long assertArgument( long argument ,
	                                   @Nullable LongPredicate wrong ,
	                                   @Nullable Supplier<String> errorMessage )
		{
		
		assert !isCheckArgument( argument , wrong ) :
			errorMessage == null ? "" : errorMessage.get();
		return argument;
		}
	
	
	
	//</editor-fold>
	
	//<editor-fold desc="Check numbers ">
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param number
	 * 	the string
	 *
	 * @return the string
	 */
	public static long checkArgument( long number )
		{
		checkArgument( number , badLong , MESSAGE_WRONG_NUMBER );
		return number;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param number
	 * 	the string
	 *
	 * @return the string
	 */
	public static boolean isCheckArgument( long number )
		{
		return badLong.test( number );
		}
	
	
	
	/**
	 * Check state, lazy message.
	 *
	 * @param number
	 * 	the string
	 *
	 * @return the string
	 */
	public static long checkState( long number )
		{
		checkState( number , badLong , MESSAGE_WRONG_NUMBER );
		return number;
		}
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param number
	 * 	the string
	 *
	 * @return the string
	 */
	public static long assertArgument( long number )
		{
		assertArgument( number , badLong , MESSAGE_WRONG_NUMBER );
		return number;
		}
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="Log functions">
	
	
	
	/**
	 * helper method: get comma separated string with indexes of found objects in collection
	 * Method checks every element in collection with Predicate (usual test for nullable or empty elements)
	 *
	 * You can use the method in log methods (and also in checkCollection, checkCollectionRaiseIllegalStateException, checkCollectionRaiseAssertion).
	 * <pre>{@code
	 * throw new IllegalArgumentException( String.format( "Every element of collection should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
	 * 			                                                   collection ,
	 * 			                                                   getIndexesOfObjectsInCollection( collection ,
	 * 			                                                                                    filter ) ) );
	 * }************************</pre>
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param collection
	 * 	collection in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return comma separated string with indexes of found object in collection 	return "" if collection or predicate is null, or collection is empty
	 */
	@NotNull
	public static <E> String getIndexesOfObjectsInCollection( @Nullable Collection<E> collection ,
	                                                          @Nullable Predicate<E> filter )
		{
		if( collection == null || filter == null || collection.isEmpty() )
			{
			return "";
			}
		
		
		return Streams.mapWithIndex( collection.stream() , ( o , i ) -> Map.entry( "" + i , o ) )
		              .filter( e -> filter.test( e.getValue() ) )
		              .map( Map.Entry::getKey )
		              .collect( joining( ", " ) );
		}
	
	
	
	/**
	 * helper method: get comma separated string with FIRST 80 indexes of found objects in collection
	 * Method checks every element in collection with Predicate (usual test for nullable or empty elements)
	 *
	 * You can use the method in log methods (and also in checkCollection, checkCollectionRaiseIllegalStateException, checkCollectionRaiseAssertion).
	 * <pre>{@code
	 * throw new IllegalArgumentException( String.format( "Every element of collection should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
	 * 			                                                   collection ,
	 * 			                                                   getIndexesOfObjectsInCollection( collection ,
	 * 			                                                                                    filter ) ) );
	 * }************************</pre>
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param collection
	 * 	collection in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return comma separated string with indexes of found object in collection 	return "" if collection or predicate is null, or collection is empty
	 */
	@NotNull
	public static <E> String getIndexesOfObjectsInCollectionForLog( @Nullable Collection<E> collection ,
	                                                                @Nullable Predicate<E> filter )
		{
		if( collection == null || filter == null || collection.isEmpty() )
			{
			return "";
			}
		
		return Streams.mapWithIndex( collection.stream() , ( o , i ) -> Map.entry( "" + i , o ) )
		              .filter( e -> filter.test( e.getValue() ) )
		              .limit( DEBUG_WINDOW_NUM_COL )
		              .map( Map.Entry::getKey )
		              .collect( joining( ", " ) );
		}
	
	
	
	/**
	 * helper method: get comma separated string with indexes of found objects in collection
	 * Name analogicaly getIndexesOfObjectsInCollection
	 * Method checks every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * You can use the method in log methods (and also in checkMap, checkMapRaiseIllegalStateException, checkMapRaiseAssertion).
	 * <pre>{@code
	 *  throw new IllegalArgumentException( String.format( "Every element of map should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
	 * 			                                                   map ,
	 * 			                                                   getKeysOfObjectsInMap( map ,
	 * 			                                                                             filter ) ) );
	 * }************************</pre>
	 *
	 * @param <TKey>
	 * 	the type of keys in map
	 * @param <TValue>
	 * 	the type of values in map
	 * @param map
	 * 	map in which we will look for
	 * @param filterValues
	 * 	condition for find the desired object
	 *
	 * @return comma separated string with keys of found entrie's values in map 	return "" if map or predicate is null, or map is empty
	 */
	@NotNull
	public static <TKey, TValue> String getKeysOfObjectsInMap( @Nullable Map<TKey,TValue> map ,
	                                                           @Nullable Predicate<TValue> filterValues )
		{
		if( map == null || filterValues == null || map.isEmpty() )
			{
			return "";
			}
		
		
		return map.entrySet()
		          .stream()
		          .filter( e -> filterValues.test( e.getValue() ) )
		          .map( e -> "" + e.getKey() )
		          .collect( joining( ", " ) );
		}
	
	
	
	/**
	 * helper method: get comma separated string with FIRST 80 indexes of found objects in collection
	 * Name analogicaly getIndexesOfObjectsInCollection
	 * Method checks every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * You can use the method in log methods (and also in checkMap, checkMapRaiseIllegalStateException, checkMapRaiseAssertion).
	 * <pre>{@code
	 *  throw new IllegalArgumentException( String.format( "Every element of map should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
	 * 			                                                   map ,
	 * 			                                                   getKeysOfObjectsInMap( map ,
	 * 			                                                                             filter ) ) );
	 * }************************</pre>
	 *
	 * @param <TKey>
	 * 	the type of keys in map
	 * @param <TValue>
	 * 	the type of values in map
	 * @param map
	 * 	map in which we will look for
	 * @param filterValues
	 * 	condition for find the desired object
	 *
	 * @return comma separated string with keys of found entrie's values in map 	return "" if map or predicate is null, or map is empty
	 */
	@NotNull
	public static <TKey, TValue> String getKeysOfObjectsInMapForLog( @Nullable Map<TKey,TValue> map ,
	                                                                 @Nullable Predicate<TValue> filterValues )
		{
		if( map == null || filterValues == null || map.isEmpty() )
			{
			return "";
			}
		
		return map.entrySet()
		          .stream()
		          .filter( e -> filterValues.test( e.getValue() ) )
		          .limit( DEBUG_WINDOW_NUM_COL )
		          .map( e -> "" + e.getKey() )
		          .collect( joining( ", " ) );
		}
	
	
	
	/**
	 * helper method: numeric of found object in collection
	 * Method checks every element in collection with Predicate (usual test for nullable or empty elements)
	 *
	 * You can use the method in log methods.
	 * <pre>{@code
	 *
	 * }************************</pre>
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param collection
	 * 	collection in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return numeric of found objects in collection 	return 0 if collection or predicate is null, or collection is empty
	 */
	@NotNull
	public static <E> long getCountOfObjectsInCollection( @Nullable Collection<E> collection ,
	                                                      @Nullable Predicate<E> filter )
		{
		
		if( collection == null || filter == null || collection.isEmpty() )
			{
			return 0;
			}
		
		
		return Streams.mapWithIndex( collection.stream() , ( o , i ) -> Map.entry( "" + i , o ) )
		              .filter( e -> filter.test( e.getValue() ) )
		              .map( Map.Entry::getKey )
		              .count();
		}
	
	
	
	/**
	 * helper method: numeric of found object in map
	 * Method checks every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * You can use the method in log methods.
	 * <pre>{@code
	 *
	 * }************************</pre>
	 *
	 * @param <TKey>
	 * 	the type of keys in map
	 * @param <TValue>
	 * 	the type of values in map
	 * @param map
	 * 	Map in which we will look for
	 * @param filterValues
	 * 	condition for find the desired object
	 *
	 * @return numeric of found objects in map (using filter by values) 	return 0 if map or predicate is null, or map is empty
	 */
	@NotNull
	public static <TKey, TValue> long getCountOfObjectsInMap( @Nullable Map<TKey,TValue> map ,
	                                                          @Nullable Predicate<TValue> filterValues )
		{
		if( map == null || filterValues == null || map.isEmpty() )
			{
			return 0;
			}
		
		
		return map.entrySet()
		          .stream()
		          .map( Map.Entry::getValue )
		          .filter( filterValues )
		          .count();
		}
	
	//</editor-fold>
	
	
	
	}
