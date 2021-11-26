package com.aoi.combiner.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FilesReader {
	
	private static String rootPath;
	private List<File> filesList;
	
	public FilesReader(String absolutePath) {
		File dir = new File(absolutePath);
		if(dir.canRead() && dir.isDirectory()) {
			rootPath = absolutePath;
		}
	}
	
	public List<File> getFilesListByExtension(File sourceDir, String fileExtension) {
		File[] dirFiles = sourceDir.listFiles();
		
		List<File> result = new ArrayList<File>();
		
		for(int i = 0; i < dirFiles.length; i++) {
			File dirFile = dirFiles[i];
			if(dirFile.isFile() && dirFile.canRead()) {
				String ext = dirFile.getName().substring(dirFile.getName().lastIndexOf('.') + 1, dirFile.getName().length());
				if(fileExtension.contains(ext)) {
					result.add(dirFile);
				}
			}
		}
		
		return result;
	}

	public String getFileNameFromHeader(String headerStr) {
		String result = "";
		
		String[] headerInfo = headerStr.split(",");
		String fileName = "";
		for(int i = 0; i <= 1; i++) {
			String value = headerInfo[i];
			String[] namePair = value.split(":");

			if(i < 1) {
				fileName = fileName.concat(namePair[1].trim()).concat("_");
			}
			else {
				fileName = fileName.concat(namePair[1].trim());
			}
		}
		
		result = result.concat(fileName);
		return result.concat(".csv");
	}
	
	public String getHeaderStringFromFile(File file) {
		String result = "";
		if(file.exists()) {
			if(file.canRead()) {
				try(Scanner scanner = new Scanner(file)) {
					while(scanner.hasNextLine()) {
						String targetLine = scanner.nextLine();
						result = result.concat(targetLine);
						break;
					}
				}
				catch(IOException e) {
					System.err.println(e.toString());
				}				
			}
		}
		
		return result;
	}

	public List<String> getSourceContext(File file) {
		List<String> result = new ArrayList<String>();
		
		if(file.exists()) {
			if(file.canRead()) {
				int counter = 0;
				try(Scanner scanner = new Scanner(file)) {
					while(scanner.hasNextLine()) {
						String targetLine = scanner.nextLine();
						
						if(counter > 2) {
							String csvString = targetLine.replaceAll("\\t", ";");
							result.add(csvString);
						}
						
						counter++;
					}
				}
				catch(IOException e) {
					System.err.println(e.toString());
				}				
			}
		}
		
		
		return result;
	}
	
	public String getLogColumnsCSV(File file) {
		String result = "";
		if(file.exists()) {
			if(file.canRead()) {
				try(Scanner scanner = new Scanner(file)) {
					int counter = 0;
					while(scanner.hasNextLine()) {
						String targetLine = scanner.nextLine();
						// while nothing
						if(counter == 2) {
							String csvTableCells = targetLine.replaceAll("\\t", ";");
							result = result.concat(csvTableCells);
						}
						counter++;
					}
				}
				catch(IOException e) {
					System.err.println(e.toString());
				}				
			}
		}
		
		return result;
	}
	

	public List<File> getFilesList() {
		return filesList;
	}


	public void setFilesList(List<File> filesList) {
		this.filesList = filesList;
	}


	public static String getRootPath() {
		return rootPath;
	}

	public static void setRootPath(String rootPath) {
		FilesReader.rootPath = rootPath;
	}
	
	
}
