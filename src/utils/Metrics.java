package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exceptions.NonExistingFileException;
import gps.GPSEngine;
import gps.GPSNode;
import gps.SearchStrategy;
import model.heuristics.PBNearBGHeuristic;
import sokoban.SokobanProblem;

public class Metrics {
	
	private static List<String> boardArray;
	private static List<SearchStrategy> strategyArray;
	private static PrintWriter pw;
	private static StringBuilder sb;

	public static void getMetrics(String board, String strategy) throws FileNotFoundException {
		printHeaders();
		
		boardArray = new ArrayList<String>();
		strategyArray = new ArrayList<SearchStrategy>();

		
		if (board == null || board.isEmpty()){
			boardArray = FileScanner.listFiles("res/boards");
		} else {
			boardArray.add(board);
		}
		
		if (strategy == null || strategy.isEmpty()) {
			strategyArray = Arrays.asList(SearchStrategy.values());
		} else {		
			strategyArray.add(SearchStrategy.valueOf(strategy.toUpperCase()));
		}
		
		printMetrics(boardArray, strategyArray);
	}
	
	private static void printHeaders() throws FileNotFoundException {
		pw = new PrintWriter(new File("test" + System.nanoTime() + ".csv"));
		sb = new StringBuilder();
		sb.append("Board");
		sb.append(",Strategy");
		sb.append(",Distance");
		sb.append(",Elapsed time [ms]\n");	
	}

	public static void printMetrics(List<String> boardArray, List<SearchStrategy> strategyArray) throws FileNotFoundException {
		try {
			long startTime;
			for (String fileName : boardArray) {
				for (SearchStrategy searchStrategy: strategyArray) {
					sb.append(fileName + "," + searchStrategy);
					startTime = System.nanoTime();
					final SokobanProblem problem = new SokobanProblem(ArgsReader.getFilePath(fileName), new PBNearBGHeuristic());
			    	final GPSEngine engine = new GPSEngine(problem, searchStrategy);
			    	startTime = System.nanoTime();
			        engine.findSolution(startTime);
			        final long endtime = System.nanoTime();
			        
			        if (engine.isFailed() || !engine.isTimeOut()) {
			            sb.append(",-,-\n");
			        }

			        if (engine.isFinished() && !engine.isFailed() && !engine.isTimeOut()) {
			            GPSNode solutionNode = engine.getSolutionNode();;
			            int nodeCount = 0;
			            do {
			                nodeCount++;
			                solutionNode = solutionNode.getParent();
			            } while (solutionNode != null);
			            sb.append(String.format(",%d", nodeCount));
			            sb.append(String.format(",%f", (endtime - startTime) / 10E6) + '\n');
			        }
				}
			}
	        pw.write(sb.toString());
	        pw.close();
			System.out.println("CSV Printed");
		} catch (NonExistingFileException e) {
			System.out.println("Board not found!");
		}
	}

}
