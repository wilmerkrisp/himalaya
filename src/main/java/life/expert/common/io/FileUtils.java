package life.expert.common.io;









import com.google.common.flogger.FluentLogger;
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



//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.io
//                           wilmer 2019/01/23
//
//--------------------------------------------------------------------------------









/**
 * The type File helper.
 */
public final class FileHelper
	{
	
	
	
	// constant
	private static final String DEFAULT_FILENAME = "default.txt";
	
	
	
	private static final FluentLogger logger_ = FluentLogger.forEnclosingClass();
	
	
	
	private FileHelper()
		{
		super();
		
		throw new UnsupportedOperationException( "Dont use this PRIVATE constructor.Please use constructor with parameters." );
		}
	
	
	
	/**
	 * for using inside streamApi
	 * hide exception and return optional
	 *
	 * @param file
	 * 	the file
	 *
	 * @return the optional
	 */
	public static Optional< URL > fileToUrl( File file )
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
				logger_.atInfo()
				       .log( "Parent is null. Using default dir: %s" , path.toAbsolutePath() );
				}
			if( Files.notExists( path ) )
				{
				Files.createDirectories( parent );
				Files.createFile( path )
				     .toFile();
				logger_.atInfo()
				       .log( "Target file will be created: %s" , path.toAbsolutePath() );
				}
			else
				{
				logger_.atInfo()
				       .log( "Target file will be retrieved: %s" , path.toAbsolutePath() );
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
	public static void ioWrapper(@NotNull RunnableIO operation ,
	                             @NotNull String errorMessage )
		{
		if( operation == null || errorMessage==null  )
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
	public static void ioWrapper(@NotNull RunnableIO operation ,
	                             @NotNull Supplier< String > errorMessage )
		{
		if( operation == null || errorMessage==null  )
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
	public static < E > E ioWrapper(@NotNull SupplierIO< E > operation )
		{
		if( operation == null   )
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
	public static < E > E ioWrapper( SupplierIO< E > operation ,
	                                 String errorMessage )
		{
		if( operation == null || errorMessage==null  )
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
	public static < E > E ioWrapper( SupplierIO< E > operation ,
	                                 Supplier< String > errorMessage )
		{
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
	public static < E > Optional< E > ioOptional( SupplierIO< E > operation )
		{
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
	public static < E > void ioWrapper( E input ,
	                                    ConsumerIO< E > operation ,
	                                    String errorMessage )
		{
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
	public static < E > void ioWrapper( E input ,
	                                    ConsumerIO< E > operation )
		{
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
	public static < E > void ioWrapper( E input ,
	                                    ConsumerIO< E > operation ,
	                                    Supplier< String > errorMessage )
		{
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
	 * Io optional.
	 *
	 * @param <E>
	 * 	the type parameter
	 * @param input
	 * 	the input
	 * @param operation
	 * 	the operation
	 *
	 * @return the optional
	 */
	public static < E > Optional< E > ioOptional( E input ,
	                                              ConsumerIO< E > operation )
		{
		try
			{
			operation.accept( input );
			}
		catch( IOException exception )
			{
			return Optional.empty();
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
	public static RunnableIO writerWrapper( File file ,
	                                        Supplier< String > textToWrite )
		{
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
