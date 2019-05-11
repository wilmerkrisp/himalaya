package life.expert.common.io;









import life.expert.common.async.LogUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

import static reactor.core.publisher.Mono.*;
import static reactor.core.scheduler.Schedulers.*;
import static life.expert.common.async.LogUtils.*;

import static io.vavr.API.*;                            //switch
import static io.vavr.Predicates.*;                     //switch - case
import static io.vavr.Patterns.*;                       //switch - case - success/failure

//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.utils.io
//                           wilmer 2019/01/23
//
//--------------------------------------------------------------------------------









/**
 * The type File helper.
 */
@Slf4j
@UtilityClass
public final class FileWrappers
	{
	
	
	
	// constant
	private static final String DEFAULT_FILENAME = "default.txt";
	
	
	
	
	
	
	
	/**
	 * for using inside streamApi
	 * hide exception and return optional
	 *
	 * @param file
	 * 	the file
	 *
	 * @return the optional
	 */
	public static Optional<URL> fileToUrl( @NotNull File file )
		{
		try
			{
			return Optional.ofNullable( file.toURI()
			                                .toURL() );
			}
		catch( MalformedURLException | NullPointerException e )
			{
			return Optional.empty();
			}
		}
	
	
	
	/**
	 * Create or retrieve file file.
	 *
	 * @param file
	 * 	the file
	 * @param directory
	 * 	the directory
	 *
	 * @return the file
	 */
	public static File createOrRetrieveFile( @Nullable final String file ,
	                                         @Nullable final Path directory )
		{
		String fileName = file;
		if( fileName == null || fileName.isBlank() )
			{
			fileName = DEFAULT_FILENAME;
			}
		
		Path defaultDir = directory;
		if( defaultDir == null )
			{
			defaultDir = Path.of( "" );
			}
		
		Path path   = Paths.get( fileName );
		Path parent = path.getParent();
		
		
		
		try
			{
			if( parent == null )
				{
				//Path build_dir = project.getBuildDir().toPath();
				//parent = defaultDir.resolve( file);
				path = defaultDir.resolve( fileName );
				log_( "Parent is null. Using default dir: {}" , path.toAbsolutePath()
				                                                    .toString() );
				}
			if( Files.notExists( path ) )
				{
				Files.createDirectories( parent );
				Files.createFile( path )
				     .toFile();
				log_( "Target file will be created: {}" , path.toAbsolutePath()
				                                              .toString() );
				}
			else
				{
				log_( "Target file will be retrieved: {}" , path.toAbsolutePath()
				                                                .toString() );
				}
			}
		catch( IOException exception )
			{
			throw new RuntimeException( "Please set correct path for file with filename. For example file  \"$buildDir/architecture/classdiagram.dot\" " , exception );
			}
		return path.toFile();
		}
	
	//<editor-fold desc="wrappers">
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param operation
	 * 	the operation
	 */
	public static void ioWrapper( @NotNull RunnableIO operation )
		{
		if( operation == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.run();
			}
		catch( IOException exception )
			{
			throw new RuntimeException( exception );
			}
		}
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param operation
	 * 	the operation
	 * @param errorMessage
	 * 	the error message
	 */
	public static void ioWrapper( @NotNull RunnableIO operation ,
	                              @NotNull String errorMessage )
		{
		if( operation == null || errorMessage == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.run();
			}
		catch( IOException exception )
			{
			throw new RuntimeException( errorMessage , exception );
			}
		}
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param operation
	 * 	the operation
	 * @param errorMessage
	 * 	the error message
	 */
	public static void ioWrapper( @NotNull RunnableIO operation ,
	                              @NotNull Supplier<String> errorMessage )
		{
		if( operation == null || errorMessage == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.run();
			}
		catch( IOException exception )
			{
			throw new RuntimeException( errorMessage.get() , exception );
			}
		}
	
	
	
	/**
	 * Io wrapper e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param operation
	 * 	the operation
	 *
	 * @return the e
	 */
	public static <E> E ioWrapper( @NotNull SupplierIO<E> operation )
		{
		if( operation == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			return operation.get();
			}
		catch( IOException exception )
			{
			throw new RuntimeException( exception );
			}
		}
	
	
	
	/**
	 * Io wrapper e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param operation
	 * 	the operation
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E ioWrapper( @NotNull SupplierIO<E> operation ,
	                               @NotNull String errorMessage )
		{
		if( operation == null || errorMessage == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			return operation.get();
			}
		catch( IOException exception )
			{
			throw new RuntimeException( errorMessage , exception );
			}
		}
	
	
	
	/**
	 * Io wrapper e.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param operation
	 * 	the operation
	 * @param errorMessage
	 * 	the error message
	 *
	 * @return the e
	 */
	public static <E> E ioWrapper( @NotNull SupplierIO<E> operation ,
	                               @NotNull Supplier<String> errorMessage )
		{
		if( operation == null || errorMessage == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			return operation.get();
			}
		catch( IOException exception )
			{
			throw new RuntimeException( errorMessage.get() , exception );
			}
		}
	
	
	
	/**
	 * Io optional optional.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param operation
	 * 	the operation
	 *
	 * @return the optional
	 */
	public static <E> Optional<E> ioOptional( @Nullable SupplierIO<E> operation )
		{
		if( operation == null )
			{
			return Optional.empty();
			}
		
		try
			{
			return Optional.ofNullable( operation.get() );
			}
		catch( IOException exception )
			{
			return Optional.empty();
			}
		}
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param input
	 * 	the input
	 * @param operation
	 * 	the operation
	 * @param errorMessage
	 * 	the error message
	 */
	public static <E> void ioWrapper( @Nullable E input ,
	                                  @NotNull ConsumerIO<E> operation ,
	                                  @NotNull String errorMessage )
		{
		if( operation == null || errorMessage == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.accept( input );
			}
		catch( IOException exception )
			{
			throw new RuntimeException( errorMessage , exception );
			}
		}
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param input
	 * 	the input
	 * @param operation
	 * 	the operation
	 */
	public static <E> void ioWrapper( @Nullable E input ,
	                                  @NotNull ConsumerIO<E> operation )
		{
		if( operation == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.accept( input );
			}
		catch( IOException exception )
			{
			throw new RuntimeException( exception );
			}
		}
	
	
	
	/**
	 * Io wrapper.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param input
	 * 	the input
	 * @param operation
	 * 	the operation
	 * @param errorMessage
	 * 	the error message
	 */
	public static <E> void ioWrapper( @Nullable E input ,
	                                  @NotNull ConsumerIO<E> operation ,
	                                  @NotNull Supplier<String> errorMessage )
		{
		if( operation == null || errorMessage == null )
			{
			throw new NullPointerException();
			}
		
		try
			{
			operation.accept( input );
			}
		catch( IOException exception )
			{
			throw new RuntimeException( errorMessage.get() , exception );
			}
		}
	
	
	
	/**
	 * Writer wrapper runnable io.
	 *
	 * @param file
	 * 	the file
	 * @param textToWrite
	 * 	the text to write
	 *
	 * @return the runnable io
	 */
	public static RunnableIO writerWrapper( @NotNull File file ,
	                                        @NotNull Supplier<String> textToWrite )
		{
		if( file == null || textToWrite == null )
			{
			throw new NullPointerException();
			}
		
		return () ->
		{
		try( final PrintWriter writer = new PrintWriter( file ) )
			{
			writer.print( textToWrite.get() );
			}
		};
		}
	//</editor-fold>
	
	}
