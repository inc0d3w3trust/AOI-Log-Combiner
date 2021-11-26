package com.aoi.combiner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.aoi.combiner.file.FilesCombine;
import com.aoi.combiner.file.FilesReader;


public class AppFactory {
	private static AppFactory instance = null;
	public final static String dirSeparator = "/";
	protected final static int BUFFER_SIZE = 1024;
	protected FilesReader filesReader;
	
	private AppFactory() {
		filesReader = new FilesReader(getRootPath());
	}
	
	public static AppFactory getInstance() {
		//Double check locking pattern
		if(instance == null) { //Check for the first time
			
			synchronized (AppFactory.class) { //Check for the second time
				
				if(instance == null)
					instance = new AppFactory();
			}
		}
		return instance;		
	}
	
	public boolean isWindowsOS() {
		boolean result = false;
		String windowsOS = "windows";
		String osName = System.getProperty("os.name");
		
		if( osName.matches("(?i:^" + windowsOS + ".*)")) {
			result = true;
		}
		return result;
	}

	public String normalizePath(String dirPath) {
		String result = "";
		result = dirPath.trim().replaceAll("\\\\", "/");
		result = replaceBackToForwardSlashes(result);
		return (result.endsWith("/")) ? result.substring(0, result.length() -1) : result;
	}
	
	public String replaceBackToForwardSlashes(String dirPath) {
		return dirPath.replaceAll("\\\\", "/");
	}
	
	public String getRootPath() {
		String rootPath = "";
		int upToFolderPath = 0;
		
		try {
			rootPath = Thread.currentThread()
					.getContextClassLoader()
					.getResource("").getPath();
		}
		catch(NullPointerException e) {
			try {
				rootPath = new File(".").getCanonicalPath();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		rootPath = replaceBackToForwardSlashes( rootPath );
		if(isWindowsOS()) {
			if(rootPath.startsWith(dirSeparator)) 
				rootPath = rootPath.substring(1);
		}
		
		String[] splited = rootPath.split(dirSeparator);  
		StringBuilder stringBuilder = new StringBuilder();
		
		int counter = 0;
		for(String dir : splited) {
			if(isWindowsOS()) {
				if(counter == splited.length - upToFolderPath) 
					break;
				if(dir.length() > 0) {
					stringBuilder.append(dir);
					stringBuilder.append(dirSeparator);
				}
			}
			counter++;
		}

		if(isWindowsOS()) {
			rootPath = stringBuilder.toString();
		}
		return rootPath.substring(0, rootPath.lastIndexOf(dirSeparator));
	}
	
	
	public int getFilesCount() {
		List<File> filesList = filesReader.getFilesListByExtension(new File(getRootPath()), "txt");
		filesReader.setFilesList(filesList);
		
		return filesList.size();
	}
	
	public void initializeCombiningProcess() {
		
		List<File> filesList = filesReader.getFilesListByExtension(new File(getRootPath()), "txt");
		if(filesList.isEmpty()) {
			System.out.println("AOI Logs Combiner does not found a files to run.");
			System.exit(0);
		}

		String logHeaderString = filesReader.getHeaderStringFromFile(filesList.get(0));
		String logColumnsCSV = filesReader.getLogColumnsCSV(filesList.get(0));
		String combinerFileName = filesReader.getFileNameFromHeader(logHeaderString);
		
		File logCombinerFileHandler = new File(getRootPath()
				.concat(dirSeparator)
				.concat(combinerFileName));
		
		FilesCombine fileCombine = new FilesCombine();
		try {
			fileCombine.initializeBufferedReader(logCombinerFileHandler);
			fileCombine.appendLineToCombiner(logColumnsCSV);
			
			for(int i = 0; i < filesList.size(); i++) {
				List<String> contextList = filesReader.getSourceContext(filesList.get(i));
				if(!contextList.isEmpty()) {
					for(String contextLine : contextList) {
						fileCombine.appendLineToCombiner(contextLine);
					}
				}
				
				System.out.println(String.format("Read file counter: %d / %d", i + 1, filesList.size() ));
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(fileCombine != null)
					fileCombine.closeBufferedReader();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		System.out.println(String.format("Combiner file name: %s", combinerFileName));
		
	}

}
