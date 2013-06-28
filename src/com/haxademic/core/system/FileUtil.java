package com.haxademic.core.system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.haxademic.core.app.P;
import com.haxademic.core.debug.DebugUtil;

public class FileUtil {
	
	public static String DATA_PATH = null;
	public static String OUTPUT_PATH = null;
	public static String BIN_PATH = null;
	public static String HAX_PATH = null;
	
	// system & haxademic paths -------------------------------------------
	public static String getProjectAbsolutePath() {
		return new java.io.File("").getAbsolutePath();
	}
	
	public static String getHaxademicPath() {
		if( HAX_PATH != null ) return HAX_PATH;
		String binPath = getProjectAbsolutePath();
		Boolean hasBin = ( binPath.lastIndexOf("/bin") != -1 ) ? true : false;
		HAX_PATH = ( hasBin == true ) ? binPath.substring(0, binPath.lastIndexOf("/bin") ) : binPath;
		return HAX_PATH;
	}
	
	public static String getHaxademicBinPath() {
		if( BIN_PATH != null ) return BIN_PATH;
		BIN_PATH = getHaxademicPath().concat("/bin/");
		return BIN_PATH;
	}
	
	public static String getHaxademicDataPath() {
		if( DATA_PATH != null ) return DATA_PATH;
		DATA_PATH = getHaxademicPath().concat("/data/");
		return DATA_PATH;
	}
	
	public static String getHaxademicOutputPath() {
		if( OUTPUT_PATH != null ) return OUTPUT_PATH;
		OUTPUT_PATH = getHaxademicPath().concat("/output/");
		return OUTPUT_PATH;
	}
	
	// existance methods ----------------------------------------
	public static Boolean fileOrPathExists( String path ) {
		File f = new File( path );
		return f.exists();
	}
	
	public static Boolean fileExists( String path ) {
		return new File( path ).isFile();
	}
	
	/**
	 * Creates a new directory on the machine's filesystem
	 * @param path Directory to create
	 */
	public static void createDir( String path ) {
		File f = new File( path );
		try {
		    if( f.mkdir() ) { 
		        P.println("Directory created: "+path);
		    } else {
		        P.println("Directory was not created"+path);
		    }
		} catch(Exception e){
		    e.printStackTrace();
		} 
	}
	
	/**
	 * Finds files of a specific type within a directory
	 * @param path Directory to search
	 * @param type File extension to search for
	 */
	public static ArrayList<String> getFilesInDirOfType( String directory, String type ) {
		type = "."+type;
		File dir = new File( directory );
		String[] children = dir.list();
		ArrayList<String> filesOfType = new ArrayList<String>();
		if (children == null) {
			P.println("FileUtil error: couldn't find file or directory");
		} else {
		    for (int i=0; i < children.length; i++) {
		        String filename = children[i];
		        if( filename.indexOf( type ) != -1 ) {	
		        	P.println(filename);
		        	filesOfType.add( filename );
		        }
		    }
		}
		return filesOfType;
	}
	
	public static void getFilesInDir( String directory ) {
		File dir = new File( directory );

		String[] children = dir.list();
		if (children == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (int i=0; i<children.length; i++) {
		        // Get filename of file or directory
		        String filename = children[i];
		        DebugUtil.print( filename );
		    }
		}

		// It is also possible to filter the list of returned files.
		// This example does not return any files that start with `.'.
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return !name.startsWith(".");
		    }
		};
		children = dir.list(filter);


		// The list of files can also be retrieved as File objects
		// File[] files = dir.listFiles();

		// This filter only returns directories
		FileFilter fileFilter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.isDirectory();
		    }
		};
		File[] files = dir.listFiles(fileFilter);
		P.println( files.length );
	}
	
	/**
	 * Simple method to write text to a file
	 * @param file The filename (with full path) to write to
	 * @param text Text to write to the file
	 */
	public static final void writeTextToFile( String file, String text ) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write( text );
            writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Copies one file to another.
	 * From: http://stackoverflow.com/a/115086/352456
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile( String sourcePath, String destPath ) throws IOException {
		File sourceFile = new File( sourcePath );
		File destFile = new File( destPath );
		
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
}
