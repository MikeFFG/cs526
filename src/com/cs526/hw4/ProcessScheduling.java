package com.cs526.hw4;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;

public class ProcessScheduling {
	public static final int MAX_WAIT_TIME = 10;
	
	public static void main(String[] args) {
		// Use first command line arg as test input path
		String inputFile = args[0];
		
		// Load processList based on the input path given
		ArrayList<Process> processList = loadProcessListFromFile(inputFile);
		HeapAdaptablePriorityQueue<Integer, Process> priorityQueue = new HeapAdaptablePriorityQueue<>();
		
		int currentTime = 0;
		boolean running = false;
		int startTime = 0;
		Process currentlyRunning = null;
		
		while(!processList.isEmpty()) {
			Process p = getProcessWithLowestArrivalTime(processList);
			if (p.getArrivalTime() <= currentTime) {
				priorityQueue.insert(p.getPriority(), p);
				processList.remove(p);
			}
			
			if (!priorityQueue.isEmpty() && running == false) {
				currentlyRunning = priorityQueue.removeMin().getValue();
				running = true;
				startTime = currentTime;
			}
			
			if (currentlyRunning != null) {
				if (startTime + currentlyRunning.getDuration() <= currentTime) {
					printRemovedProcess(currentlyRunning, currentTime);
					running = false;
				}
			}
			
			currentTime++;
		}
		
		while(!priorityQueue.isEmpty()) {
			printRemovedProcess(priorityQueue.removeMin().getValue(), currentTime);
			currentTime++;
 		}
	}
	
	public static void printRemovedProcess(Process p, int currentTime) {
		int waitTime = currentTime - p.getArrivalTime() - p.getDuration();
		System.out.println("Process removed from queue is: id = " + p.getId() +
				", at time " + currentTime + ", wait time = " + waitTime);
		System.out.println("\tProcess id = " + p.getId());
		System.out.println("\tPriority = " + p.getPriority());
		System.out.println("\tArrival = " + p.getArrivalTime());
		System.out.println("\tDuration = " + p.getDuration());
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
	
	public static Process getProcessWithLowestArrivalTime(ArrayList<Process> processList) {
		Process earliestProcess = processList.get(0);
		for (Process process : processList) {
			if (process.getArrivalTime() < earliestProcess.getArrivalTime()) {
				earliestProcess = process;
			}
		}
		
		return earliestProcess;
	}
}
