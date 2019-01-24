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
 * Objects
 */
public final class Objects
	{
	
	
	
	//using cyclop's lazy load
	private static final Supplier< Gson > gson_ = memoizeSupplier( Gson::new );
	
	
	
	/**
	 * helper method: deep copy with google gson
	 *
	 * <pre>{@code
	 * this.item2(MyCommon.deepCopyOf( this.item2() ,  List.class ) );
	 * }</pre>
	 *
	 * @param <E>
	 * 	the type of copied object
	 * @param copied
	 * 	the copied object
	 * @param classOfObject
	 * 	the class-literal of object. Type tag.
	 *
	 * @return the copy
	 *
	 * @throws NullPointerException
	 * 	if argument nullable
	 */
	@NotNull
	public static < E > E deepCopyOfObject( @NotNull E copied ,
	                                        @NotNull Class< ? > classOfObject )
		{
		checkNotNull( copied ,
		              "Сopied object should not be null." );
		checkNotNull( classOfObject ,
		             "Please write class of copied object. For example: List.class" );
		
		Gson g = gson_.get();
		return (E) g.fromJson( g.toJson( copied ) ,
		                       classOfObject );
		}
		
		
		
	}
