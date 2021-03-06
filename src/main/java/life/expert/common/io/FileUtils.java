package life.expert.common.io;
//-------------------------------------------------------------------------------------------------------
//  __    __   __  .___  ___.      ___       __          ___   ____    ____  ___   ____    ____  ___
// |  |  |  | |  | |   \/   |     /   \     |  |        /   \  \   \  /   / /   \  \   \  /   / /   \
// |  |__|  | |  | |  \  /  |    /  ^  \    |  |       /  ^  \  \   \/   / /  ^  \  \   \/   / /  ^  \
// |   __   | |  | |  |\/|  |   /  /_\  \   |  |      /  /_\  \  \_    _/ /  /_\  \  \_    _/ /  /_\  \
// |  |  |  | |  | |  |  |  |  /  _____  \  |  `----./  _____  \   |  |  /  _____  \   |  |  /  _____  \
// |__|  |__| |__| |__|  |__| /__/     \__\ |_______/__/     \__\  |__| /__/     \__\  |__| /__/     \__\
//
//                                            Wilmer Krisp 2019/02/05
//--------------------------------------------------------------------------------------------------------

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.BaseStream;

import static life.expert.common.async.LogUtils.logAtDebugUnaryOperator;
import static life.expert.common.function.CheckedUtils.consumerToBoolean;
import static life.expert.common.function.CheckedUtils.uncheckedFunction;
import static life.expert.common.function.NullableUtils.nullableFunction;
import static reactor.core.publisher.Mono.*;
import static reactor.core.publisher.Mono.justOrEmpty;

import static io.vavr.API.*;                              //switch
import static io.vavr.Predicates.*;                       //switch - case
import static io.vavr.Patterns.*;                         //switch - case - success/failure

/**
 * The type File utils.
 *
 *
 * <pre>{@code
 * var new_file_name = String.format( "%d.dot" , Instant.now().toEpochMilli() );
 * 		var new_file_flux = FileUtils.deleteFileToMono( "src/main/graphviz" )
 * 		                             .map( p -> p.resolve( new_file_name ) )
 * 		                             .log( "debug", Level.FINE, SignalType.ON_NEXT )
 * 		                             .flatMap( FileUtils::createFileToMono )
 * 		                             .flatMap( FileUtils::writerFromPath );
 * 		new_file_flux.subscribe( w->w.write( dot ) , logAtErrorConsumer("ERROR") , logAtInfoRunnable("COMPLETE") );
 * }*</pre>
 */
@Slf4j
@UtilityClass
public class FileUtils
	{
	
	//<editor-fold desc="read file">
	
	/**
	 * Get file as one string
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono with string or error event
	 */
	public static Mono<String> stringFromPath( Path path )
		{
		return justOrEmpty( unchecked( (Path p) -> Files.readString( p ) ).apply( path ) );
		}
	
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
	
	/**
	 * Write to path flux.
	 *
	 * <pre>{@code
	 * writeToPath( file ).subscribe( w->w.write( "dot" ) );
	 * }*</pre>
	 *
	 * @param file
	 * 	the file
	 *
	 * @return the flux
	 */
	//<editor-fold desc="write file">
	public static Mono<PrintWriter> writerFromPath( File file )
		{
		return using( () -> new PrintWriter( file ) , Mono::just , PrintWriter::close );
		}
	
	/**
	 * Write to path flux.
	 *
	 * <pre>{@code writeToPath( file ).subscribe( w->w.write( "dot" ) ); }</pre>
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the flux
	 */
	public static Mono<PrintWriter> writerFromPath( Path path )
		{
		return writerFromPath( path.toFile() );
		}
	
	/**
	 * Write to path flux.
	 *
	 * <pre>{@code writeToPath( file ).subscribe( w->w.write( "dot" ) ); }</pre>
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the flux
	 */
	public static Mono<PrintWriter> writerFromPath( String path )
		{
		return writerFromPath( Paths.get( path ) );
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
	 * Create file to mono mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Path> createFileToMono( Path path )
		{
		return createFileToMono( justOrEmpty( path ) );
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
	 * Create file to mono mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Path> createFileToMono( String path )
		{
		return createFileToMono( Paths.get( path ) );
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
	 * Create file to mono mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Path> createFileToMono( Mono<Path> path )
		{
		return path.map( uncheckedFunction( p ->
		                                    {
		                                    org.apache.commons.io.FileUtils.touch( p.toFile() );
		                                    return p;
		                                    } ) );
		}
	
	/**
	 * Gets path for new file.
	 *
	 * @param path
	 * 	the path
	 * @param defaultParentDirectory
	 * 	the default parent directory
	 * @param defaultFileName
	 * 	the default file name
	 *
	 * @return the path for new file
	 */
	public static Mono<Path> getPathForNewFile( String path ,
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
		
		return name_root.single();
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
		return createFile( getPathForNewFile( path , defaultParentDirectory , defaultFileName ) );
		}
	
	/**
	 * Create file to mono mono.
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
	public static Mono<Path> createFileToMono( String path ,
	                                           String defaultParentDirectory ,
	                                           String defaultFileName )
		{
		return createFileToMono( getPathForNewFile( path , defaultParentDirectory , defaultFileName ) );
		}
	//</editor-fold>
	
	//<editor-fold desc="delete file or directory recursively">
	
	/**
	 * delete file or directory recursively
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Boolean> deleteFile( Path path )
		{
		return deleteFile( justOrEmpty( path ) );
		}
	
	/**
	 * Delete file to mono mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Path> deleteFileToMono( Path path )
		{
		return deleteFileToMono( justOrEmpty( path ) );
		}
	
	/**
	 * delete file or directory recursively
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Boolean> deleteFile( String path )
		{
		return deleteFile( Paths.get( path ) );
		}
	
	/**
	 * Delete file to mono mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Path> deleteFileToMono( String path )
		{
		return deleteFileToMono( Paths.get( path ) );
		}
	
	/**
	 * delete file or directory recursively
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Boolean> deleteFile( Mono<Path> path )
		{
		return path.map( Path::toFile )
		           .map( consumerToBoolean( org.apache.commons.io.FileUtils::deleteQuietly ) );
		}
	
	/**
	 * Delete file to mono mono.
	 *
	 * @param path
	 * 	the path
	 *
	 * @return the mono
	 */
	public static Mono<Path> deleteFileToMono( Mono<Path> path )
		{
		return path.map( uncheckedFunction( p ->
		                                    {
		                                    org.apache.commons.io.FileUtils.deleteQuietly( p.toFile() );
		                                    return p;
		                                    } ) );
		
		//</editor-fold>
		
		}
	}
