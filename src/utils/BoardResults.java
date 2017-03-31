package utils;

import gps.SearchStrategy;

public class BoardResults {
	private String boardName;
	private SearchStrategy strategy;
	private int nodeCount;
	private double elapsedTime;
	
	public BoardResults(String boardName, SearchStrategy strategy, int nodeCount, double elapsedTime) {
		this.boardName = boardName;
		this.nodeCount = nodeCount;
		this.elapsedTime = elapsedTime;
		this.strategy = strategy;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
