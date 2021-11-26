package com.aoi.combiner.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilesCombine {
	private BufferedWriter bufferedWriterHandler;
	
	public FilesCombine() {
		
	}
	
	public void initializeBufferedReader(File file) throws IOException {
		this.bufferedWriterHandler = new BufferedWriter(new FileWriter(file));
	}
	
	public void closeBufferedReader() throws IOException {
		if(this.bufferedWriterHandler != null) {
			this.bufferedWriterHandler.close();
		}
	}
	
	public void appendLineToCombiner(String newLine) throws IOException {
		this.bufferedWriterHandler.write(newLine);
		this.bufferedWriterHandler.newLine();
		this.bufferedWriterHandler.flush();
	}
}
