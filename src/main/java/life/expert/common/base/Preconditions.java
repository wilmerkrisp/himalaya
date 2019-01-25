package life.expert.common.base;









import org.jetbrains.annotations.*;


import static java.util.stream.Collectors.*;        //toList

import java.util.Collection;
import java.util.Map;
import java.util.function.*;


import com.google.common.collect.*;



//@Header@
//--------------------------------------------------------------------------------
//                              life.expert.testfiles
//--------------------------------------------------------------------------------
//  norm3
//
//  Copyright 2018 Wilmer Krisp
//  Licensed under the Apache License, Version 2.0
//
//  Author: wilmer
//  Created: 2018/12/06
//









/**
 * The type Preconditions.
 */
public final class Preconditions
	{
	
	
	
	// width of debug info window
	private static final int DEBUG_WINDOW_NUM_COL = 80;
	
	
	
	// constant
	private static final String MESSAGE_WRONG_COLLECTION = "%d wrong elements in collection \n Indexes of wrong elements: %s \n Collection: %s ";
	
	
	
	private static final String MESSAGE_WRONG_MAP = "%d wrong entries in map \n Keys of wrong entries: %s \n Map: %s ";
	
	
	
	private static final String MESSAGE_WRONG_STRING_NULL  = "String argument should'not be null.";
	
	
	
	private static final String MESSAGE_WRONG_STRING_EMPTY = "String argument should'not \"\" be empty (not null but length=0).";
	
	
	
	private static final String MESSAGE_WRONG_STRING_BLANK = "String argument should'not be \" \" blank (not empty but some spaces here).";
	
	
	
	private static final String MESSAGE_WRONG_NUMBER       = "Number argument should'not be less than zero.";
	
	
	
	private static final String MESSAGE_WRONG_COLLECTION_NULL  = "Argument(collection) should'not be null.";
	
	
	
	private static final String MESSAGE_WRONG_COLLECTION_EMPTY = "Argument(Collection) should'not be empty (not null but length=0):";
	
	
	
	private static final String MESSAGE_WRONG_MAP_NULL  = "Argument(map) should'not be null.";
	
	
	
	private static final String MESSAGE_WRONG_MAP_EMPTY = "Argument(map) should'not be empty (not null but length=0):";
	
	
	//<editor-fold desc="Check collections and maps">
	
	
	
	/**
	 * Helper method for checking input args of collection type or constructor in immutable colls
	 * Checking every element in collection with Predicate  (usual test for nullable or empty elements)
	 *
	 * If you need simply test collection for empty elements use Apache common's Validate.notEmpty method instead
	 *
	 *
	 * For example, you can use the method (which raise IllegalArgumentException)
	 * - inside constructors of immutable objects
	 * - inside methods for testing inputs arguments
	 * <pre>{@code
	 *      checkCollection(c,String::isBlank);
	 *      checkCollection(c,Objects::isNull);
	 * }***************</pre>
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
	 * @throws NullPointerException
	 * 	if argument nullable
	 * @throws IllegalArgumentException
	 * 	if argument empty (or if one collection's element is empty)
	 */
	@NotNull
	public static < T extends Collection< E >, E > T checkArgument( @NotNull T collection ,
	                                                                @NotNull Predicate< E > invalidElement )
		{
		checkArgument( collection );
		
		checkArgument( !collection.stream().anyMatch( invalidElement ) ,
		               () -> String.format( MESSAGE_WRONG_COLLECTION ,
		                                    getCountOfObjectsInCollection( collection ,
		                                                                   invalidElement ) ,
		                                    getIndexesOfObjectsInCollectionForLog( collection ,
		                                                                           invalidElement ) ,
		                                    collection.getClass() ) );
		
		
		return collection;
		}
	
	
	
	/**
	 * Helper method for checking input args of map type or constructor in immutable colls
	 * Checking every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * If you need simply test map for empty elements use Apache common's Validate.notEmpty method instead
	 *
	 * For example, you can use the method (which raise IllegalArgumentException)
	 * - inside constructors of immutable objects
	 * - inside methods for testing inputs arguments
	 * <pre>{@code
	 *      checkMap(m,String::isBlank);
	 *      checkMap(m,Objects::isNull);
	 * }***************</pre>
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
	 * @throws NullPointerException
	 * 	if argument nullable
	 * @throws IllegalArgumentException
	 * 	if argument empty (or if one map's element is empty)
	 */
	@NotNull
	public static < TKey, TValue > Map< TKey, TValue > checkArgument( @NotNull Map< TKey, TValue > map ,
	                                                                  @NotNull Predicate< TValue > invalidValue )
		{
		checkArgument( map );
		
		checkArgument( !map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( invalidValue ) ,
		               () -> String.format( MESSAGE_WRONG_MAP ,
		                                    getCountOfObjectsInMap( map ,
		                                                            invalidValue ) ,
		                                    getKeysOfObjectsInMapForLog( map ,
		                                                                 invalidValue ) ,
		                                    map.getClass() ) );
		
		
		
		//noNullElements(collection);
		
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
	 * }***************</pre>
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
	@NotNull
	public static < E > boolean checkArgumentThen( @NotNull Collection< E > collection ,
	                                               @NotNull Predicate< E > invalidElement )
		{
		///assert collection==null: "Argument should not be null." ;
		return ( collection.isEmpty() || collection.stream().anyMatch( invalidElement ) ) ? true : false;
		
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
	 * }***************</pre>
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
	@NotNull
	public static < TKey, TValue > boolean checkArgumentThen( @NotNull Map< TKey, TValue > map ,
	                                                          @NotNull Predicate< TValue > invalidValue )
		{
		///assert collection==null: "Argument should not be null." ;
		return ( map.isEmpty() || map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( invalidValue ) ) ? true : false;
		
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
	 * }***************</pre>
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
	@NotNull
	public static < T extends Collection< E >, E > T checkState( @NotNull T collection ,
	                                                             @NotNull Predicate< E > invalidElement )
		{
		checkState( collection );
		
		checkState( !collection.stream().anyMatch( invalidElement ) ,
		            () -> String.format( MESSAGE_WRONG_COLLECTION ,
		                                 getCountOfObjectsInCollection( collection ,
		                                                                invalidElement ) ,
		                                 getIndexesOfObjectsInCollectionForLog( collection ,
		                                                                        invalidElement ) ,
		                                 collection.getClass() ) );
		
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
	 * }***************</pre>
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
	@NotNull
	public static < TKey, TValue > Map< TKey, TValue > checkState( @NotNull Map< TKey, TValue > map ,
	                                                               @NotNull Predicate< TValue > invalidValue )
		{
		checkState( map );
		
		checkState( !map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( invalidValue ) ,
		            () -> String.format( MESSAGE_WRONG_MAP ,
		                                 getCountOfObjectsInMap( map ,
		                                                         invalidValue ) ,
		                                 getKeysOfObjectsInMapForLog( map ,
		                                                              invalidValue ),
		                                 map.getClass() ) );
		
		
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
	 * }***************</pre>
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
	@NotNull
	public static < T extends Collection< E >, E > T assertArgument( @NotNull T collection ,
	                                                                 @NotNull Predicate< E > invalidElement )
		{
		assertArgument( collection );
		
		assert !collection.stream().anyMatch( invalidElement ) : String.format( MESSAGE_WRONG_COLLECTION ,
		
		                                                                        getCountOfObjectsInCollection( collection ,
		                                                                                                       invalidElement ) ,
		                                                                        getIndexesOfObjectsInCollectionForLog( collection ,
		                                                                                                               invalidElement ) ,
		                                                                        collection.getClass() );
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
	 * }***************</pre>
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
	@NotNull
	public static < TKey, TValue > Map< TKey, TValue > assertArgument( @NotNull Map< TKey, TValue > map ,
	                                                                   @NotNull Predicate< TValue > invalidValue )
		{
		assertArgument( map );
		
		assert !map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( invalidValue ) : String.format( MESSAGE_WRONG_MAP ,
		                                                                                                     getCountOfObjectsInMap( map ,
		                                                                                                                             invalidValue ) ,
		                                                                                                     getKeysOfObjectsInMapForLog( map ,
		                                                                                                                                  invalidValue ) ,
		                                                                                                     map.getClass() );
		return map;
		}
	
	
	
	//</editor-fold>
	
	
	
	//<editor-fold desc="Generic checks">
	
	
	
	/**
	 * Check argument, lazy message.
	 *
	 * @param expression
	 * 	the expression
	 * @param errorMessage
	 * 	the error message
	 */
	public static void checkArgument( boolean expression ,
	                                  Supplier< String > errorMessage )
		{
		if( !expression )
			{
			throw new IllegalArgumentException( errorMessage.get() );
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
	                               Supplier< String > errorMessage )
		{
		if( !expression )
			{
			throw new IllegalStateException( errorMessage.get() );
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
	                                   Supplier< String > errorMessage )
		{
		assert expression : errorMessage.get();
		
		}
	
	
	
	/**
	 * Check argument not null e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param reference
	 * 	the reference
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static < E > E checkArgumentNotNull( E reference ,
	                                            Supplier< String > errorMessage )
		{
		if( reference == null )
			{
			throw new IllegalArgumentException( errorMessage.get() );
			}
		return reference;
		}
	
	
	
	/**
	 * Check state not null e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param reference
	 * 	the reference
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static < E > E checkStateNotNull( E reference ,
	                                         Supplier< String > errorMessage )
		{
		if( reference == null )
			{
			throw new IllegalStateException( errorMessage.get() );
			}
		return reference;
		}
	
	
	
	/**
	 * Check argument not null e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param reference
	 * 	the reference
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static < E > E assertArgumentNotNull( E reference ,
	                                             Supplier< String > errorMessage )
		{
		
		assert reference != null : errorMessage.get();
		
		
		return reference;
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
		if( string == null )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_STRING_NULL );
			}
		
		if( string.isEmpty() )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_STRING_EMPTY );
			}
		
		if( string.isBlank() )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_STRING_BLANK );
			}
		
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
	public static boolean checkArgumentThen( String string )
		{
		
		return ( string == null || string.isEmpty() || string.isBlank() ) ? true : false;
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
		
		if( string == null )
			{
			throw new IllegalStateException( MESSAGE_WRONG_STRING_NULL );
			}
		
		if( string.isEmpty() )
			{
			throw new IllegalStateException( MESSAGE_WRONG_STRING_EMPTY );
			}
		
		if( string.isBlank() )
			{
			throw new IllegalStateException( MESSAGE_WRONG_STRING_BLANK );
			}
		
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
		assert ( string != null ) && ( !string.isBlank() ) : "String argument should'not be blank";
		
		
		
		assert !( string == null ) : ( MESSAGE_WRONG_STRING_NULL );
		
		
		assert !( string.isEmpty() ) : ( MESSAGE_WRONG_STRING_EMPTY );
		
		
		assert !( string.isBlank() ) : ( MESSAGE_WRONG_STRING_BLANK );
		
		
		
		return string;
		}
	
	//</editor-fold>
	
	//<editor-fold desc="Check numbers">
	
	
	
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
		if( number <= 0 )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_NUMBER );
			}
		
		
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
	public static boolean checkArgumentThen( long number )
		{
		
		return ( number <= 0 ) ? true : false;
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
		if( number <= 0 )
			{
			throw new IllegalStateException( MESSAGE_WRONG_NUMBER );
			}
		
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
		assert ( number > 0 ) : MESSAGE_WRONG_NUMBER;
		
		return number;
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
	public static < E extends Collection< ? > > E checkArgument( @NotNull E collection )
		{
		if( collection == null )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_COLLECTION_NULL );
			}
		
		if( collection.isEmpty() )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_COLLECTION_EMPTY + collection.getClass() );
			}
		
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
	 * @return the e
	 */
	public static < E extends Collection< ? > > boolean checkArgumentThen( @NotNull E collection )
		{
		return ( collection == null || collection.isEmpty() ) ? true : false;
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
	public static < E extends Collection< ? > > E checkState( @NotNull E collection )
		{
		if( collection == null )
			{
			throw new IllegalStateException( MESSAGE_WRONG_COLLECTION_NULL );
			}
		
		if( collection.isEmpty() )
			{
			throw new IllegalStateException( MESSAGE_WRONG_COLLECTION_EMPTY + collection.getClass() );
			}
		
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
	public static < E extends Collection< ? > > E assertArgument( @NotNull E collection )
		{
		assert !( collection == null ) : ( MESSAGE_WRONG_COLLECTION_NULL );
		
		
		assert !( collection.isEmpty() ) : MESSAGE_WRONG_COLLECTION_EMPTY + collection.getClass();
		
		
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
	public static < TKey, TValue > Map< TKey, TValue > checkArgument( @NotNull Map< TKey, TValue > map )
		{
		if( map == null )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_MAP_NULL );
			}
		
		if( map.isEmpty() )
			{
			throw new IllegalArgumentException( MESSAGE_WRONG_MAP_EMPTY + map.getClass() );
			}
		
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
	 * @return the e
	 */
	public static < TKey, TValue > boolean checkArgumentThen( @NotNull Map< TKey, TValue > map )
		{
		return ( map == null || map.isEmpty() ) ? true : false;
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
	public static < TKey, TValue > Map< TKey, TValue > checkState( @NotNull Map< TKey, TValue > map )
		{
		if( map == null )
			{
			throw new IllegalStateException( MESSAGE_WRONG_MAP_NULL );
			}
		
		if( map.isEmpty() )
			{
			throw new IllegalStateException( MESSAGE_WRONG_MAP_EMPTY + map.getClass() );
			}
		
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
	public static < TKey, TValue > Map< TKey, TValue > assertArgument( @NotNull Map< TKey, TValue > map )
		{
		assert !( map == null ) : ( MESSAGE_WRONG_MAP_NULL );
		
		
		assert !( map.isEmpty() ) : MESSAGE_WRONG_MAP_EMPTY + map.getClass();
		
		
		return map;
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
	 * }***************</pre>
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
	public static < E > String getIndexesOfObjectsInCollection( @Nullable Collection< E > collection ,
	                                                            @Nullable Predicate< E > filter )
		{
		if( collection == null || filter == null || collection.isEmpty() )
			{
			return "";
			}
		
		
		return Streams.mapWithIndex( collection.stream() ,
		                             ( o , i ) -> Map.entry( "" + i ,
		                                                     o ) ).filter( e -> filter.test( e.getValue() ) ).map( Map.Entry::getKey ).collect( joining( ", " ) );
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
	 * }***************</pre>
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
	public static < E > String getIndexesOfObjectsInCollectionForLog( @Nullable Collection< E > collection ,
	                                                                  @Nullable Predicate< E > filter )
		{
		if( collection == null || filter == null || collection.isEmpty() )
			{
			return "";
			}
		
		return Streams.mapWithIndex( collection.stream() ,
		                             ( o , i ) -> Map.entry( "" + i ,
		                                                     o ) ).filter( e -> filter.test( e.getValue() ) ).limit( DEBUG_WINDOW_NUM_COL ).map( Map.Entry::getKey ).collect( joining( ", " ) );
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
	 * }***************</pre>
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
	public static < TKey, TValue > String getKeysOfObjectsInMap( @Nullable Map< TKey, TValue > map ,
	                                                             @Nullable Predicate< TValue > filterValues )
		{
		if( map == null || map == null || map.isEmpty() )
			{
			return "";
			}
		
		
		return map.entrySet().stream().filter( e -> filterValues.test( e.getValue() ) ).map( e -> "" + e.getKey() ).collect( joining( ", " ) );
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
	 * }***************</pre>
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
	public static < TKey, TValue > String getKeysOfObjectsInMapForLog( @Nullable Map< TKey, TValue > map ,
	                                                                   @Nullable Predicate< TValue > filterValues )
		{
		if( map == null || map == null || map.isEmpty() )
			{
			return "";
			}
		
		return map.entrySet().stream().filter( e -> filterValues.test( e.getValue() ) ).limit( DEBUG_WINDOW_NUM_COL ).map( e -> "" + e.getKey() ).collect( joining( ", " ) );
		}
	
	
	
	/**
	 * helper method: quantity of found object in collection
	 * Method checks every element in collection with Predicate (usual test for nullable or empty elements)
	 *
	 * You can use the method in log methods.
	 * <pre>{@code
	 *
	 * }***************</pre>
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param collection
	 * 	collection in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return quantity of found objects in collection 	return 0 if collection or predicate is null, or collection is empty
	 */
	@NotNull
	public static < E > long getCountOfObjectsInCollection( @Nullable Collection< E > collection ,
	                                                        @Nullable Predicate< E > filter )
		{
		
		if( collection == null || filter == null || collection.isEmpty() )
			{
			return 0;
			}
		
		
		return Streams.mapWithIndex( collection.stream() ,
		                             ( o , i ) -> Map.entry( "" + i ,
		                                                     o ) ).filter( e -> filter.test( e.getValue() ) ).map( Map.Entry::getKey ).count();
		}
	
	
	
	/**
	 * helper method: quantity of found object in map
	 * Method checks every element in map with Predicate (usual test for nullable or empty entries values)
	 *
	 * You can use the method in log methods.
	 * <pre>{@code
	 *
	 * }***************</pre>
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
	 * @return quantity of found objects in map (using filter by values) 	return 0 if map or predicate is null, or map is empty
	 */
	@NotNull
	public static < TKey, TValue > long getCountOfObjectsInMap( @Nullable Map< TKey, TValue > map ,
	                                                            @Nullable Predicate< TValue > filterValues )
		{
		if( map == null || map == null || map.isEmpty() )
			{
			return 0;
			}
		
		
		return map.entrySet().stream().map( Map.Entry::getValue ).filter( filterValues ).count();
		}
	
	//</editor-fold>
	
	
	
	}
