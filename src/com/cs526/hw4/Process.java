package com.cs526.hw4;

/**
 * Model class to save information on individual
 * processes.
 * 
 * @author Michael Burke
 *
 */
public class Process {
	private int id;
	private int arrivalTime;
	private int duration;
	private int priority;
	private int waitTime;
	private int startTime;
	
	public Process(int id, int priority, int duration, int arrivalTime) {
		this.id = id;
		this.priority = priority;
		this.duration = duration;
		this.arrivalTime = arrivalTime;
	}
	
	public void setWaitTime(int currentTime) {
		if (currentTime - arrivalTime > 0) {
			waitTime = currentTime - arrivalTime;
		}
	}
	
	public int getWaitTime() {
		return waitTime;
	}
	
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getEndTime() {
		return startTime + duration - 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getPriority() {
		return priority;
	}

	/**
	 * Sets the priority internally on the class
	 * @param priority - the new priority
	 */
	public void setPriority(int priority) {
		if (priority >= 1 && priority <= 10) {
			this.priority = priority;
		} else if (priority < 1){
			this.priority = 1;
		} else {
			this.priority = 10;
		}
	}
	
	
}
