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
		
		// Initialize variables
		int currentTime = 1;
		boolean running = false;
		Process currentlyRunning = null;
		
		// Main loop
		while(!processList.isEmpty()) {
			// Find the process with the earliest arrival time
			Process p = getProcessWithLowestArrivalTime(processList);
			
			// If the arrival time is less than current time, add process to queue
			if (p.getArrivalTime() <= currentTime) {
				priorityQueue.insert(p.getPriority(), p);
				processList.remove(p);
			}
			
			/*
			 *  If there are processes in the queue and nothing is running
			 *  remove the lowest priority in the queue and start running it.
			 */
			if (!priorityQueue.isEmpty() && running == false) {
				currentlyRunning = priorityQueue.removeMin().getValue();
				running = true;
				currentlyRunning.setStartTime(currentTime);
				printRemovedProcess(currentlyRunning, currentTime);
			}
			
			/*
			 * If the process that is running has finished
			 * update the running flag, the wait times and the priorities
			 */
			if (currentlyRunning != null &&
					currentlyRunning.getEndTime() <= currentTime) {
				
				running = false;
				updateWaitTimesAndPriorities(processList, currentTime);
			}
			
			// Increment time
			currentTime++;
		}
		
		while(!priorityQueue.isEmpty()) {
			if (running == false) {
				currentlyRunning = priorityQueue.removeMin().getValue();
				running = true;
				currentlyRunning.setStartTime(currentTime);
				printRemovedProcess(priorityQueue.removeMin().getValue(), currentTime);
			}
			
			if (currentlyRunning != null &&
					currentlyRunning.getEndTime() <= currentTime) {
				
				running = false;
				updateWaitTimesAndPriorities(processList, currentTime);
			}

			currentTime++;
 		}
	}
	
	public static void updateWaitTimesAndPriorities(ArrayList<Process> processList, int currentTime) {
		for (Process p : processList) {
			p.setWaitTime(currentTime);
			if (p.getWaitTime() > MAX_WAIT_TIME) {
				p.setPriority(p.getPriority() - 1);
			}
		}
	}
	
	
	/**
	 * Prints the process as it is removed
	 * @param p - the process to print
	 * @param currentTime - the currentTime that the process is being removed
	 */
	public static void printRemovedProcess(Process p, int currentTime) {
		int waitTime = currentTime - p.getArrivalTime();
		System.out.println("Process removed from queue is: id = " + p.getId() +
				", at time " + p.getStartTime() + ", wait time = " + waitTime);
		System.out.println("\tProcess id = " + p.getId());
		System.out.println("\tPriority = " + p.getPriority());
		System.out.println("\tArrival = " + p.getArrivalTime());
		System.out.println("\tDuration = " + p.getDuration());
	} 
	
	/**
	 * Loads the ArrayList<Process> data structure from the inputFile
	 * @param inputFile - should be the path to the input.txt file that you wish to test against
	 * @return - a new filled out ArrayList<Process> data structure
	 */
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
	
	/**
	 * 
	 * @param processList - the processList to work on
	 * @return - the Process from the processList with the lowest arrival time
	 */
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
