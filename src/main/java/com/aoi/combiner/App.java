package com.aoi.combiner;

public class App {

	public static AppFactory factory;
	
	public static void main(String[] args) {

		factory = AppFactory.getInstance();
		
		String rootPath = factory.getRootPath();
		
		
		System.out.println(String.format("RootPath = %s", rootPath));
		System.out.println("File content below...");

		factory.initializeCombiningProcess();
	}

}
