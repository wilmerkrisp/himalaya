package life.expert.common.io;









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









public final class FileHelper
	{
	
	
	
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
		catch( MalformedURLException e )
			{
			return Optional.empty();
			}
		}
	
	
	
	/**
	 * Create or retrieve file file.
	 *
	 * @param fileName
	 * 	the file name
	 * @param defaultDir
	 * 	the default dir
	 *
	 * @return the file
	 */
	public static File createOrRetrieveFile( final String fileName ,
	                                         Path defaultDir )
		{
		Path path   = Paths.get( fileName );
		Path parent = path.getParent();
		
		try
			{
			if( parent == null )
				{
				//Path build_dir = project.getBuildDir().toPath();
				//parent = defaultDir.resolve( "architecture" );
				path = parent.resolve( path );
				System.out.println( "Build directory used: " + path.toAbsolutePath() );
				}
			if( Files.notExists( path ) )
				{
				Files.createDirectories( parent );
				Files.createFile( path )
				     .toFile();
				System.out.println( "Target file " + path.toAbsolutePath() + " will be created." );
				}
			else
				{
				System.out.println( "Target file \"" + path.toAbsolutePath() + "\" will be retrieved." );
				}
			}
		catch( IOException exception )
			{
			throw new RuntimeException( "Please set correct path for file with filename. For example file  \"$buildDir/architecture/classdiagram.dot\" " , exception );
			}
		return path.toFile();
		}
	
	
	
	public static void ioWrapper( RunnableIO operation ,
	                        String errorMessage )
		{
		try
			{
			operation.run();
			}
		catch( IOException exception )
			{
			throw new RuntimeException( errorMessage , exception );
			}
		}
	
	
	
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
		
		
	}
