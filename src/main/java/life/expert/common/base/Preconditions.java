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
	 * helper method: get comma separated string with indexes of found object in collection
	 *
	 * <pre>{@code
	 *
	 * }</pre>
	 *
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param filter
	 * 	condition for find the desired object
	 * @param collection
	 * 	collection in which we will look for
	 *
	 * @throws NullPointerException if argument nullable
	 *
	 * @return empty if collection is empty
	 * @return comma separated string with indexes of found object in collection
	 */
	public static < E > String getIndexesOfObjectsInCollection( Collection< E > collection ,
	                                                            Predicate< E > filter )
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
	 * helper method: get comma separated string with indexes of found object in collection
	 * name analogicaly getIndexesOfObjectsInCollection
	 *
	 * <pre>{@code
	 *
	 * }</pre>
	 *
	 *
	 * @param <TKey>
	 * 	the type of keys in map
	 * @param <TValue>
	 * 	the type of values in map
	 * @param filter
	 * 	condition for find the desired object
	 * @param map
	 * 	map in which we will look for
	 *
	 * @throws NullPointerException if argument nullable
	 *
	 * @return empty if collection is empty
	 * @return comma separated string with keys of found entrie's values in map
	 */
	public static < TKey, TValue > String getIndexesOfObjectsInMap( Map< TKey, TValue > map ,
	                                                                Predicate< TValue > filter )
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
	 *
	 * <pre>{@code
	 *
	 * }</pre>
	 *
	 *
	 * @param <E>
	 * 	the type of desired object
	 * @param filter
	 * 	condition for find the desired object
	 * @param collection
	 * 	collection in which we will look for
	 *
	 * @throws NullPointerException if argument nullable
	 *
	 * @return 0 if collection is empty
	 * @return quantity of found objects in collection
	 */
	public static < E > long getCountOfObjectsInCollection( Collection< E > collection ,
	                                                        Predicate< E > filter )
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
	 *
	 * <pre>{@code
	 *
	 * }</pre>
	 *
	 *
	 * @param <TKey>
	 * 	the type of keys in map
	 * @param <TValue>
	 * 	the type of values in map
	 * @param filter
	 * 	condition for find the desired object
	 * @param map
	 * 	Map in which we will look for
	 *
	 * @throws NullPointerException if argument nullable
	 *
	 * @return 0 if map is empty
	 * @return quantity of found objects in map (using filter by values)
	 */
	public static < TKey, TValue > long getCountOfObjectsInMap( Map< TKey, TValue > map ,
	                                                            Predicate< TValue > filter )
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
	 *
	 *
	 *
	 * checkCollection(c,String::isBlank);
	 * checkCollection(c,Objects::isNull);
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 * @throws IllegalArgumentException
	 * 	if argument empty (or if one collection's element is empty)
	 */
	public static < T extends Collection< E >, E > T checkCollection( T collection ,
	                                                                  Predicate< E > filter )
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
	 *
	 *
	 *
	 * checkMap(m,String::isBlank);
	 * checkMap(m,Objects::isNull);
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 * @throws IllegalArgumentException
	 * 	if argument empty (or if one map's element is empty)
	 */
	public static < TKey, TValue > Map< TKey, TValue > checkMap( Map< TKey, TValue > map ,
	                                                             Predicate< TValue > filter )
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
	 *
	 *
	 * checkCollectionRaiseIllegalStateException(c,String::isBlank);
	 * checkCollectionRaiseIllegalStateException(c,Objects::isNull);
	 *
	 * @throws IllegalStateException
	 * 	if argument nullable
	 * @throws IllegalStateException
	 * 	if argument empty (or if one collection's element is empty)
	 */
	public static < T extends Collection< E >, E > T checkCollectionRaiseIllegalStateException( T collection ,
	                                                                                            Predicate< E > filter )
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
	 *
	 * checkMapRaiseIllegalStateException(c,String::isBlank);
	 * checkMapRaiseIllegalStateException(c,Objects::isNull);
	 *
	 * @throws IllegalStateException
	 * 	if argument nullable
	 * @throws IllegalStateException
	 * 	if argument empty (or if one map's element is empty)
	 */
	public static < TKey, TValue > Map< TKey, TValue > checkMapRaiseIllegalStateException( Map< TKey, TValue > map ,
	                                                                                       Predicate< TValue > filter )
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
	 *
	 *
	 * if ( checkCollectionIsEmpty(c,String::isBlank) )
	 * return Optional.empty();
	 *
	 *
	 * if ( checkCollectionIsEmpty(c,Objects::isNull) )
	 * return Optional.empty();
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	public static < E > boolean checkCollectionIsWrong( Collection< E > collection ,
	                                                    Predicate< E > filter )
		{
		///assert collection==null: "Argument should not be null." ;
		return ( collection.isEmpty() || collection.stream().anyMatch( filter ) ) ? true : false;
		
		}
	
	
	
	/**
	 * Helper method for strategy "return Optional instead Exception"
	 * Check argument of type Map
	 *
	 *
	 * if ( checkMapIsEmpty(m,String::isBlank) )
	 * return Optional.empty();
	 *
	 *
	 * if ( checkMapIsEmpty(m,Objects::isNull) )
	 * return Optional.empty();
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	public static < TKey, TValue > boolean checkMapIsWrong( Map< TKey, TValue > map ,
	                                                        Predicate< TValue > filter )
		{
		///assert collection==null: "Argument should not be null." ;
		return ( map.isEmpty() || map.entrySet().stream().map( Map.Entry::getValue ).anyMatch( filter ) ) ? true : false;
		
		}
	
	
	
	/**
	 * Helper method for use in private methods
	 * Check argument of type Collection
	 *
	 * checkCollectionRaiseAssertion(c,String::isBlank);
	 * checkCollectionRaiseAssertion(c,Objects::isNull);
	 *
	 * @throws AssertionError
	 */
	public static < T extends Collection< E >, E > T checkCollectionRaiseAssertion( T collection ,
	                                                                                Predicate< E > filter )
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
	 * Helper method for use in private methods
	 * Check argument of type Map
	 *
	 * checkMapRaiseAssertion(c,String::isBlank);
	 * checkMapRaiseAssertion(c,Objects::isNull);
	 *
	 * @throws AssertionError
	 */
	public static < TKey, TValue > Map< TKey, TValue > checkMapRaiseAssertion( Map< TKey, TValue > map ,
	                                                                           Predicate< TValue > filter )
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
