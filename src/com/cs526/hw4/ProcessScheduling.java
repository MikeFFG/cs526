package com.cs526.hw4;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;

public class ProcessScheduling {
	public static void main(String[] args) {
		String inputFile = args[0];
		ArrayList<Process> processList = loadProcessListFromFile(inputFile);
	}
	
	public static ArrayList<Process> loadProcessListFromFile(String inputFile) {
		BufferedReader br = null;
		FileReader file = null;
		ArrayList<Process> processList = new ArrayList<>();
		
		try {
			file = new FileReader(inputFile);
			br = new BufferedReader(file);
			
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
				String[] individualInts = currentLine.split(" ");
				processList.add(new Process(Integer.parseInt(individualInts[0]), Integer.parseInt(individualInts[1]), 
						Integer.parseInt(individualInts[2]), Integer.parseInt(individualInts[3])));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				
				if (file != null) {
					file.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return processList;
	}
}
