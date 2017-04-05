package com.cs526.hw4;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Main class for implementing Process Scheduler assignment from CS526 Homework 4.
 * 
 * To pass in different input files, use the first command line argument to input
 * the path. Easiest way to do so is to put the input file in the root folder of
 * the project and just input something like "sample_input_1.txt" (no quotation marks needed).
 * @author Michael Burke
 *
 */
public class ProcessScheduling {
	// Maximum wait time constant
	public static final int MAX_WAIT_TIME = 10;
	
	/**
	 * Main program class
	 * @param args - pass in the path to the input file as the first arg
	 */
	public static void main(String[] args) {
		// Use first command line arg as test input path
		String inputFile = args[0];
		
		// Load processList based on the input path given
		ArrayList<Process> processList = loadProcessListFromFile(inputFile);
		HeapAdaptablePriorityQueue<Integer, Process> priorityQueue = 
				new HeapAdaptablePriorityQueue<>();
		
		// Initialize variables
		int currentTime = 1;					// Stores the current time 
		boolean running = false;				// Stores whether or not a process is running 
		Process currentlyRunning = null;		// Stores the currently running process
		double totalWaitTime = 0;				// Stores the total wait time
		/*
		 *  Stores the initial processList size to be used in the calculation of
		 *  the average wait time at the end of the program
		 */
		int size = processList.size();			
		
		/*
		 *  Main loop while the processList (data structure "D" in
		 *  project description/pseudocode) is not empty
		 */
		while(!processList.isEmpty()) {
			// Find the process with the earliest arrival time
			Process p = getProcessWithLowestArrivalTime(processList);
			
			/*
			 *  If the arrival time is less than or equal to current time, 
			 *  add process to queue (Q in description/pseudocode)
			 */
			if (p.getArrivalTime() <= currentTime) {
				// Add process to queue
				priorityQueue.insert(p.getPriority(), p);
				// Remove from processList
				processList.remove(p);
			}
			
			/*
			 *  If there are processes in the queue and nothing is running
			 *  remove the lowest priority in the queue and start running it.
			 */
			if (!priorityQueue.isEmpty() && running == false) {
				// Save the lowest priority process as the "currentlyRunning" process.
				currentlyRunning = priorityQueue.removeMin().getValue(); 
				running = true; // Set running flag to true
				currentlyRunning.setStartTime(currentTime); // Set the start time for the process
				printRemovedProcess(currentlyRunning, currentTime); 
				// Update the totalWaitTime variable
				totalWaitTime = totalWaitTime + currentTime - currentlyRunning.getArrivalTime();
			}
			
			/*
			 * If the process that is running has finished
			 * update the running flag, the wait times and the priorities
			 */
			if (currentlyRunning != null &&
					currentlyRunning.getEndTime() <= currentTime) {
				running = false; // Update running flag
				updateWaitTimesAndPriorities(priorityQueue, currentTime); // Update wait times and priorities
			}
			
			// Increment time
			currentTime++;
		}
		
		/*
		 * Once processList (or "D") is empty, run the same loop but with priorityQueue(or "Q")
		 * until it is empty
		 */
		while(!priorityQueue.isEmpty()) {
			// If no process is running:
			if (running == false) {
				// Save the lowest priority process as the "currentlyRunning" process.
				currentlyRunning = priorityQueue.removeMin().getValue();
				running = true; // Set running flag to true
				currentlyRunning.setStartTime(currentTime); // Set the start time for the process
				printRemovedProcess(currentlyRunning, currentTime); // Print the info on the process
				// Update the totalWaitTime variable
				totalWaitTime = totalWaitTime + currentTime - currentlyRunning.getArrivalTime();
			}
			
			/*
			 * If the process that is running has finished
			 * update the running flag, the wait times and the priorities
			 */
			if (currentlyRunning != null &&
					currentlyRunning.getEndTime() <= currentTime) {
				
				running = false; // Update running flag
				updateWaitTimesAndPriorities(priorityQueue, currentTime); // Update wait times and priorities
			}

			// Increment time
			currentTime++;
 		}
		
		// Print out the total and average wait times
		System.out.println("Total wait time = " + totalWaitTime);
		System.out.println("Average wait time = " + totalWaitTime / size);
	}
	
	/**
	 * Updates the wait times and the priorities on the remaining processes
	 * @param priorityQueue - the queue to work on
	 * @param currentTime - the current time as of when this method was called
	 */
	public static void updateWaitTimesAndPriorities(
			HeapAdaptablePriorityQueue<Integer, Process> priorityQueue, int currentTime) {
		// Iterate over the items in the priorityQueue
		for (Entry<Integer, Process> e : priorityQueue.heap) {
			e.getValue().setWaitTime(currentTime);	// Set the wait time based on the current wait time
			// If the wait time is greater than the MAX_WAIT_TIME, update the priority
			if (e.getValue().getWaitTime() > MAX_WAIT_TIME) {
				e.getValue().setPriority(e.getValue().getPriority() - 1);
				priorityQueue.replaceKey(e, e.getValue().getPriority());
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
		System.out.println("Process id = " + p.getId());
		System.out.println("\tPriority = " + p.getPriority());
		System.out.println("\tArrival = " + p.getArrivalTime());
		System.out.println("\tDuration = " + p.getDuration());
		System.out.println("");
	} 
	
	/**
	 * Loads the ArrayList<Process> data structure from the inputFile
	 * @param inputFile - should be the path to the input.txt file that you wish to test against
	 * @return - a new filled out ArrayList<Process> data structure
	 */
	public static ArrayList<Process> loadProcessListFromFile(String inputFile) {
		BufferedReader br = null;
		FileReader file = null;
		// This will become our main data structure "D"
		ArrayList<Process> processList = new ArrayList<>();
		
		try {
			file = new FileReader(inputFile);
			br = new BufferedReader(file);
			String currentLine;
			
			/* Read the input file line by line
			 * Then use split() to parse each line, and pass these to the
			 * Process constructor as the new Process is added to the processList
			 */
			while ((currentLine = br.readLine()) != null) {
				String[] individualInts = currentLine.split(" ");
				processList.add(new Process(Integer.parseInt(individualInts[0]), Integer.parseInt(individualInts[1]), 
						Integer.parseInt(individualInts[2]), Integer.parseInt(individualInts[3])));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Closing resources
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
	 * Searches through the processList ("D") for the process
	 * with the earliest arrival time.
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
