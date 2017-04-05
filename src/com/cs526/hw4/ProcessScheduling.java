package com.cs526.hw4;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Main class for implementing Process Scheduler assignment from CS526 Homework 4.
 * 
 * To pass in different input files, use the first command line argument to input
 * the path. Easiest way to do so is to put the input file in the root folder of
 * the project and just input something like "sample_input_1.txt" (no quotation marks needed).
 * 
 * Output file process_scheduling_out.txt will appear in the root folder of the project.
 * @author Michael Burke
 */
public class ProcessScheduling {
	// Maximum wait time constant
	public static final int MAX_WAIT_TIME = 10;
	private static final String FILENAME = "process_scheduling_out.txt";
	private static BufferedWriter bw = null;
	private static FileWriter fw = null;
	
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
		int currentTime = 0;					// Stores the current time 
		boolean running = false;				// Stores whether or not a process is running 
		Process currentlyRunning = null;		// Stores the currently running process
		double totalWaitTime = 0;				// Stores the total wait time
		
		// Initialize file output
		initializeFileOutput();
		
		/*
		 *  Stores the initial processList size to be used in the calculation of
		 *  the average wait time at the end of the program
		 */
		int size = processList.size();			
		
		/*
		 *  Main loop while the processList (data structure "D" in
		 *  project description/pseudocode) is not empty
		 */
		while(!processList.isEmpty() || !priorityQueue.isEmpty()) {
			// Find the process with the earliest arrival time
			if (!processList.isEmpty()) {
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
				totalWaitTime += calculateWaitTime(currentTime, currentlyRunning.getArrivalTime());
			}
			
			/*
			 * If the process that is running has finished
			 * update the running flag, the wait times and the priorities
			 */
			if (currentlyRunning != null &&
					currentlyRunning.getEndTime() <= currentTime) {
				running = false; // Update running flag
				updatePriorities(priorityQueue, currentTime); // Update priorities
			}
			
			// Increment time
			currentTime++;
		}
		
		// Print out the total and average wait times
		try {
			fw.write("Total wait time = " + totalWaitTime + "\n");
			fw.write("Average wait time = " + totalWaitTime / size);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Close file writer resources
		closeResources();
	}
	
	/**
	 * Updates the wait times and the priorities on the remaining processes
	 * @param priorityQueue - the queue to work on
	 * @param currentTime - the current time as of when this method was called
	 */
	public static void updatePriorities(
			HeapAdaptablePriorityQueue<Integer, Process> priorityQueue, int currentTime) {
		// Iterate over the items in the priorityQueue
		for (Entry<Integer, Process> e : priorityQueue.heap) {
			
			// If the wait time is greater than the MAX_WAIT_TIME, update the priority
			if (calculateWaitTime(currentTime, e.getValue().getArrivalTime()) > MAX_WAIT_TIME) {
				e.getValue().setPriority(e.getValue().getPriority() - 1);
				priorityQueue.replaceKey(e, e.getValue().getPriority());
			}
		}
	}
	
	public static int calculateWaitTime(int currentTime, int arrivalTime) {
		return currentTime - arrivalTime;
	}
	
	/**
	 * Prints the process as it is removed
	 * @param p - the process to print
	 * @param currentTime - the currentTime that the process is being removed
	 */
	public static void printRemovedProcess(Process p, int currentTime) {
		try {
			fw.write("Process removed from queue is: id = " + p.getId() +
					", at time " + p.getStartTime() + ", wait time = " +
					calculateWaitTime(currentTime, p.getArrivalTime()));
			fw.write("\nProcess id = " + p.getId());
			fw.write("\n\tPriority = " + p.getPriority());
			fw.write("\n\tArrival = " + p.getArrivalTime());
			fw.write("\n\tDuration = " + p.getDuration());
			fw.write("\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * Initialize the file writing resources
	 */
	public static void initializeFileOutput() {
		try {
			/*
			 *  Simply clears out the file from the previous run.
			 *  This is necessary so that each run doesn't keep appending
			 *  to same file. Kind of hacky, but it works.
			 */
			fw = new FileWriter(FILENAME); 
			
			// Now we set it up so we can append to the file
			fw = new FileWriter(FILENAME, true);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the file writing resources.
	 */
	public static void closeResources() {
		try {
			if (bw != null)
				bw.close();

			if (fw != null)
				fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
