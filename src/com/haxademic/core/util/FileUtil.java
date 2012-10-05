package com.haxademic.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

import com.haxademic.app.P;
import com.haxademic.core.debug.DebugUtil;

public class FileUtil {
	public static String getProjectAbsolutePath() {
		return new java.io.File("").getAbsolutePath();
	}
	
	public static ArrayList<String> getFilesInDirOfType( String directory, String type ) {
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
}
