package utils;

import exceptions.NonExistingFileException;
import gps.GPSEngine;
import gps.GPSNode;
import gps.SearchStrategy;
import model.heuristics.PBNearBGHeuristic;
import sokoban.Main;
import sokoban.SokobanProblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Metrics {
	
	private static List<String> boardArray;
	private static List<SearchStrategy> strategyArray;
	private static PrintWriter pw;
	private static StringBuilder sb;
	private static Map<String, Map<SearchStrategy, Set<BoardResults>>> average;

	public static void getMetrics(String board, String strategy, int iterations) throws FileNotFoundException {
		
		boardArray = new ArrayList<>();
		strategyArray = new ArrayList<>();

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
		average = new HashMap<>();

		for (int i = 0; i < iterations; i++) {
			System.out.println("Iteration #" + (i+1));
			printHeaders(i+1);
			printMetrics(boardArray, strategyArray);
		}
		printAverage(iterations);
	}
	
	private static void printAverage(int iterations) throws FileNotFoundException {
		PrintWriter averagePrinter = new PrintWriter(new File("average.csv"));
		StringBuilder sba = new StringBuilder();
		sba.append("Board").append(",Strategy").append(",Elapsed time [ms]\n");
		for (String boardName : average.keySet()) {
			for (SearchStrategy strategy : average.get(boardName).keySet()) {
				int elapsedTime = 0;
				for (BoardResults boardResult : average.get(boardName).get(strategy)) {
					elapsedTime += boardResult.getElapsedTime();
					System.out.println("un result " + boardResult.getElapsedTime());
				}
				elapsedTime = elapsedTime / iterations;
				System.out.println("Elapsed time del board average:" + elapsedTime);
				sba.append(boardName).append(",").append(strategy.toString()).append(",").append(String.format("%f\n", elapsedTime / 10E6)).append('\n');
			}
		}	
        averagePrinter.write(sba.toString());
        averagePrinter.close();
        System.out.println("Average printed");
	}

	private static void printHeaders(int iteration) throws FileNotFoundException {
		pw = new PrintWriter(new File("test_" + iteration + ".csv"));
		sb = new StringBuilder();
		sb.append("Board").append(",Strategy").append(",Distance").append(",Elapsed time [ms]\n");
	}

	public static Set<Integer> printMetrics(List<String> boardArray, List<SearchStrategy> strategyArray) throws FileNotFoundException {
		
		try {
			for (String boardName : boardArray) {
				if (!average.containsKey(boardName)) {
					average.put(boardName, new HashMap<>());
				}
				for (SearchStrategy searchStrategy: strategyArray) {
					System.out.println(average);
					if (!average.get(boardName).containsKey(searchStrategy)) {
						average.get(boardName).put(searchStrategy, new HashSet<>());
					}
                    System.out.println(String.format("Solving %s with %s", boardName, searchStrategy));
                    final SokobanProblem problem = new SokobanProblem(ArgsReader.getFilePath(boardName), new PBNearBGHeuristic());
			    	final GPSEngine engine = new GPSEngine(problem, searchStrategy);

                    sb.append(boardName).append(",").append(searchStrategy.toString());

			    	try {

						final long elapsedTime = Main.findSolution(engine);

						if (engine.isFailed()) {
							sb.append(",-1,-1\n");
						}

						if (engine.isFinished() && !engine.isFailed()) {
							GPSNode solutionNode = engine.getSolutionNode();;
							int nodeCount = 0;

							do {
								nodeCount++;
								solutionNode = solutionNode.getParent();
							} while (solutionNode != null);

							sb.append(String.format(",%d", nodeCount));
							sb.append(String.format(",%f\n", elapsedTime / 10E6));
							average.get(boardName).get(searchStrategy).add(new BoardResults(boardName, searchStrategy, nodeCount, elapsedTime));
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						sb.append(",-,-\n");
					}
				}
			}
	        pw.write(sb.toString());
	        pw.close();
			System.out.println("CSV Printed");
		} catch (NonExistingFileException e) {
			System.out.println("Board not found!");
		}
		return null;
	}

}
