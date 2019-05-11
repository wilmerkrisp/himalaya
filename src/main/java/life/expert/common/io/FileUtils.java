package life.expert.common.io;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.io
//                           wilmer 2019/05/09
//
//--------------------------------------------------------------------------------









import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.BaseStream;

import static life.expert.common.async.LogUtils.logAtDebugUnaryOperator;
import static life.expert.common.function.CheckedUtils.consumerToBoolean;
import static life.expert.common.function.NullableUtils.nullableFunction;
import static reactor.core.publisher.Mono.*;
import static reactor.core.publisher.Mono.justOrEmpty;









/**
 * The type File utils.
 */
@Slf4j
@UtilityClass
public class FileUtils
	{
	
	//<editor-fold desc="read file">
	
	
	
	/**
	 * Lines from path flux.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the flux
	 */
	public static Flux<String> linesFromPath( Mono<Path> path )
		{
		return path.flatMapMany( FileUtils::linesFromPath );
		}
	
	
	
	/**
	 * Lines from path flux.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the flux
	 */
	public static Flux<String> linesFromPath( Path path )
		{
		return Flux.using( () -> Files.lines( path ) , Flux::fromStream , BaseStream::close );
		}
	
	
	
	/**
	 * Lines from path flux.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the flux
	 */
	public static Flux<String> linesFromPath( String path )
		{
		return linesFromPath( Paths.get( path ) );
		}
	
	//</editor-fold>
	
	
	//<editor-fold desc="create file">
	
	
	
	/**
	 * Create file mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Boolean> createFile( Path path )
		{
		return createFile( justOrEmpty( path ) );
		}
	
	
	
	/**
	 * Create file mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Boolean> createFile( String path )
		{
		return createFile( Paths.get( path ) );
		}
	
	
	
	/**
	 * Create file mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Boolean> createFile( Mono<Path> path )
		{
		return path.map( Path::toFile )
		           .map( consumerToBoolean( org.apache.commons.io.FileUtils::touch ) );
		}
	
	
	
	/**
	 * Create file mono.
	 *
	 * @param path
	 * 	the path
	 * @param defaultParentDirectory
	 * 	the default parent directory
	 * @param defaultFileName
	 * 	the default file name
	 *
	 * @return the mono
	 */
	public static Mono<Boolean> createFile( String path ,
	                                        String defaultParentDirectory ,
	                                        String defaultFileName )
		{
		
		var path_ = Flux.from( justOrEmpty( path ) )
		                .takeWhile( StringUtils::isNotBlank )
		                .map( p -> Paths.get( p ) );
		
		var default_name = Flux.from( justOrEmpty( defaultFileName ) )
		                       .takeWhile( StringUtils::isNotBlank )
		                       .map( p -> Paths.get( p ) );
		
		var default_root = Flux.from( justOrEmpty( defaultParentDirectory ) )
		                       .takeWhile( StringUtils::isNotBlank )
		                       .map( p -> Paths.get( p ) );
		
		
		var name = path_.flatMap( nullableFunction( Path::getFileName ) )
		                .switchIfEmpty( default_name );
		
		
		var root = path_.flatMap( nullableFunction( Path::getParent ) )
		                .switchIfEmpty( default_root );
		
		
		var name_root = Flux.concat( root , name )
		                    .reduce( Path::resolve );
		
		return createFile( name_root.single() );
		}
	
	
 
	
	
	
	
	
	//</editor-fold>
	
	
	
	}
