package com.cs526.hw4;

public class Process {
	private int id;
	private int arrivalTime;
	private int duration;
	private int priority;
	
	public Process(int id, int priority, int duration, int arrivalTime) {
		this.id = id;
		this.priority = priority;
		this.duration = duration;
		this.arrivalTime = arrivalTime;
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

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
}
