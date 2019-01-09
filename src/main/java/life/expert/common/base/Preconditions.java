package life.expert.common.base;









import org.jetbrains.annotations.*;


import static com.google.common.base.Preconditions.*;//checkArgument
import static cyclops.function.Memoize.*;           //
import static java.util.stream.Collectors.*;        //toList
import static java.util.function.Predicate.*;       //isEqual
import static org.apache.commons.lang3.Validate.*;  //notEmpty(collection)

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.function.*;


import com.google.common.collect.*;
import com.google.gson.Gson;



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
	 * }**</pre>
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param collection
	 * 	collection in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return comma separated string with indexes of found object in collection
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	@NotNull
	public static < E > String getIndexesOfObjectsInCollection( @NotNull Collection< E > collection ,
	                                                            @NotNull Predicate< E > filter )
		{
		checkNotNull( collection ,
		              "Collection should not be null." );
		checkNotNull( filter ,
		              "Filter should not be null." );
		
		
		if( collection.isEmpty() )
			{
			return "";
			}
		
		
		return Streams.mapWithIndex( collection.stream() ,
		                             ( o , i ) -> Map.entry( "" + i ,
		                                                     o ) ).filter( e -> filter.test( e.getValue() ) ).map( Map.Entry::getKey ).collect( joining( ", " ) );
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
	 * 			                                                   getIndexesOfObjectsInMap( map ,
	 * 			                                                                             filter ) ) );
	 * }**</pre>
	 *
	 * @param <TKey>
	 * 	the type of keys in map
	 * @param <TValue>
	 * 	the type of values in map
	 * @param map
	 * 	map in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return comma separated string with keys of found entrie's values in map
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	@NotNull
	public static < TKey, TValue > String getIndexesOfObjectsInMap( @NotNull Map< TKey, TValue > map ,
	                                                                @NotNull Predicate< TValue > filter )
		{
		checkNotNull( map ,
		              "Map should not be null." );
		checkNotNull( filter ,
		              "Filter should not be null." );
		
		
		if( map.isEmpty() )
			{
			return "";
			}
		
		return map.entrySet().stream().filter( e -> filter.test( e.getValue() ) ).map( e -> "" + e.getKey() ).collect( joining( ", " ) );
		}
	
	
	
	/**
	 * helper method: quantity of found object in collection
	 * Method checks every element in collection with Predicate (usual test for nullable or empty elements)
	 *
	 * You can use the method in log methods.
	 * <pre>{@code
	 *
	 * }**</pre>
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param collection
	 * 	collection in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return quantity of found objects in collection
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	@NotNull
	public static < E > long getCountOfObjectsInCollection( @NotNull Collection< E > collection ,
	                                                        @NotNull Predicate< E > filter )
		{
		checkNotNull( collection ,
		              "Collection should not be null." );
		checkNotNull( filter ,
		              "Filter should not be null." );
		
		if( collection.isEmpty() )
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
	 * }**</pre>
	 *
	 * @param <TKey>
	 * 	the type of keys in map
	 * @param <TValue>
	 * 	the type of values in map
	 * @param map
	 * 	Map in which we will look for
	 * @param filter
	 * 	condition for find the desired object
	 *
	 * @return quantity of found objects in map (using filter by values)
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	@NotNull
	public static < TKey, TValue > long getCountOfObjectsInMap( @NotNull Map< TKey, TValue > map ,
	                                                            @NotNull Predicate< TValue > filter )
		{
		checkNotNull( map ,
		              "Map should not be null." );
		checkNotNull( filter ,
		              "Filter should not be null." );
		
		if( map.isEmpty() )
			{
			return 0;
			}
		
		
		return map.entrySet().stream().map( Map.Entry::getValue ).filter( filter ).count();
		}
	
	
	
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
	 * }**</pre>
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param filter
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
	public static < T extends Collection< E >, E > T checkCollection( @NotNull T collection ,
	                                                                  @NotNull Predicate< E > filter )
		{
		notEmpty( collection );//null+empty
		
		if( collection.stream().anyMatch( filter ) )
			{
			throw new IllegalArgumentException( String.format( "Every element of collection should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
			                                                   collection ,
			                                                   getIndexesOfObjectsInCollection( collection ,
			                                                                                    filter ) ) );
			}
		
		
		//noNullElements(collection);
		
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
	 * }**</pre>
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param filter
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
	public static < TKey, TValue > Map< TKey, TValue > checkMap( @NotNull Map< TKey, TValue > map ,
	                                                             @NotNull Predicate< TValue > filter )
		{
		notEmpty( map );//null+empty
		
		if( map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( filter ) )
			{
			throw new IllegalArgumentException( String.format( "Every element of map should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
			                                                   map ,
			                                                   getIndexesOfObjectsInMap( map ,
			                                                                             filter ) ) );
			}
		
		//noNullElements(collection);
		
		return map;
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
	 * }**</pre>
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param filter
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
	public static < T extends Collection< E >, E > T checkCollectionRaiseIllegalStateException( @NotNull T collection ,
	                                                                                            @NotNull Predicate< E > filter )
		{
		checkState( collection != null ,
		            "Collection should not be null." );
		checkState( !collection.isEmpty() ,
		            "Collection should not be empty." );
		
		
		if( collection.stream().anyMatch( filter ) )
			{
			throw new IllegalStateException( String.format( "Every element of collection should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
			                                                collection ,
			                                                getIndexesOfObjectsInCollection( collection ,
			                                                                                 filter ) ) );
			}
		
		//noNullElements(collection);
		
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
	 * }**</pre>
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param filter
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
	public static < TKey, TValue > Map< TKey, TValue > checkMapRaiseIllegalStateException( @NotNull Map< TKey, TValue > map ,
	                                                                                       @NotNull Predicate< TValue > filter )
		{
		checkState( map != null ,
		            "Map should not be null." );
		checkState( !map.isEmpty() ,
		            "Map should not be empty." );
		
		
		if( map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( filter ) )
			{
			throw new IllegalStateException( String.format( "Every element of map should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
			                                                map ,
			                                                getIndexesOfObjectsInMap( map ,
			                                                                          filter ) ) );
			}
		
		
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
	 * }**</pre>
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param filter
	 * 	the filter
	 *
	 * @return the boolean
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	@NotNull
	public static < E > boolean checkCollectionReturnBool( @NotNull Collection< E > collection ,
	                                                       @NotNull Predicate< E > filter )
		{
		///assert collection==null: "Argument should not be null." ;
		return ( collection.isEmpty() || collection.stream().anyMatch( filter ) ) ? true : false;
		
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
	 * }**</pre>
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param filter
	 * 	the filter
	 *
	 * @return the boolean
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	@NotNull
	public static < TKey, TValue > boolean checkMapReturnBool( @NotNull Map< TKey, TValue > map ,
	                                                           @NotNull Predicate< TValue > filter )
		{
		///assert collection==null: "Argument should not be null." ;
		return ( map.isEmpty() || map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( filter ) ) ? true : false;
		
		}
	
	
	
	/**
	 * Check argument of type Collection
	 * Checking every element in collection with Predicate (usual test for nullable or empty elements)
	 *
	 * Usually this method for check input args in private methods for raise assertion instead exception.
	 * <pre>{@code
	 *      checkCollectionRaiseAssertion(c,String::isBlank);
	 *      checkCollectionRaiseAssertion(c,Objects::isNull);
	 * }**</pre>
	 *
	 * @param <T>
	 * 	the type parameter
	 * @param <E>
	 * 	the type parameter
	 * @param collection
	 * 	the collection
	 * @param filter
	 * 	the filter
	 *
	 * @return the t
	 *
	 * @throws AssertionError
	 */
	@NotNull
	public static < T extends Collection< E >, E > T checkCollectionRaiseAssertion( @NotNull T collection ,
	                                                                                @NotNull Predicate< E > filter )
		{
		///assert collection==null: "Argument should not be null." ;
		assert !collection.isEmpty() : "The validated collection is empty";
		assert collection.stream().noneMatch( filter ) : String.format( "Every element of collection should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
		                                                                collection ,
		                                                                getIndexesOfObjectsInCollection( collection ,
		                                                                                                 filter ) );
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
	 * }**</pre>
	 *
	 * @param <TKey>
	 * 	the type parameter
	 * @param <TValue>
	 * 	the type parameter
	 * @param map
	 * 	the map
	 * @param filter
	 * 	the filter
	 *
	 * @return the map
	 *
	 * @throws AssertionError
	 */
	@NotNull
	public static < TKey, TValue > Map< TKey, TValue > checkMapRaiseAssertion( @NotNull Map< TKey, TValue > map ,
	                                                                           @NotNull Predicate< TValue > filter )
		{
		///assert collection==null: "Argument should not be null." ;
		assert !map.isEmpty() : "The validated collection is empty";
		assert map.entrySet().stream().map( Map.Entry::getValue ).noneMatch( filter ) : String.format( "Every element of map should not be null or empty: %s \n  Indexes of wrong elements: %s " ,
		                                                                                               map ,
		                                                                                               getIndexesOfObjectsInMap( map ,
		                                                                                                                         filter ) );
		return map;
		}
		
		
		
	}
