package utils;

import gps2.SearchStrategy;

public class BoardResults {
	private String boardName;
	private SearchStrategy strategy;
	private int nodeCount;
	private long elapsedTime;
	
	public BoardResults(String boardName, SearchStrategy strategy, int nodeCount, long elapsedTime) {
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

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
