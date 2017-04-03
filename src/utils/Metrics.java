package utils;

import exceptions.NonExistingFileException;
import gps.GPSEngine;
import gps.GPSNode;
import gps.SearchStrategy;
import model.heuristics.Heuristic;
import model.heuristics.PBBGHeuristic;
import model.heuristics.PBNearBGHeuristic;
import model.heuristics.SimpleHeuristic;
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

	private static Heuristic heuristic;
	private static String heuristicFile;
	
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

		for (int j = 0; j < 3; j++) {
			heuristic = getHeuristic(j);
			System.out.println("heur num: " + j);
			for (int i = 0; i < iterations; i++) {
				System.out.println("Iteration #" + (i+1));
				printHeaders(i+1);
				printMetrics(boardArray, strategyArray);
			}
		}

		printAverage(iterations);
	}
	
	private static Heuristic getHeuristic(int i) {
		switch (i) {
		case 0:
			heuristicFile = "Simple";
			return new SimpleHeuristic();
		case 1:
			heuristicFile = "PBBG";
			return new PBBGHeuristic();
		case 2:
			heuristicFile = "PBNearBG";
			return new PBNearBGHeuristic();
		}
		return null;
	}
	
	private static void printAverage(int iterations) throws FileNotFoundException {
		PrintWriter averagePrinter = new PrintWriter(new File("average" + heuristicFile + "Heuristic.csv"));
		StringBuilder sba = new StringBuilder();
		sba.append("Board").append(",Strategy").append(",Elapsed time [ms]\n");
		for (String boardName : average.keySet()) {
			for (SearchStrategy strategy : average.get(boardName).keySet()) {
				long elapsedTime = 0;
				for (BoardResults boardResult : average.get(boardName).get(strategy)) {
					elapsedTime += boardResult.getElapsedTime();
				}
				if (average.get(boardName).get(strategy).size() != 0) {
					elapsedTime = elapsedTime / average.get(boardName).get(strategy).size();
				}
				sba.append(boardName).append(";").append(strategy.toString()).append(";").append(String.format("%f", elapsedTime / 10E6)).append('\n');
			}
		}	
        averagePrinter.write(sba.toString());
        averagePrinter.close();
        System.out.println("Average printed");
	}

	private static void printHeaders(int iteration) throws FileNotFoundException {
		pw = new PrintWriter(new File("test_" + iteration + heuristicFile + "Heuristic.csv"));
		sb = new StringBuilder();
		sb.append("Board").append(";Strategy").append(";Distance").append(";Elapsed time [ms]\n");
	}

	public static Set<Integer> printMetrics(List<String> boardArray, List<SearchStrategy> strategyArray) throws FileNotFoundException {
		
		try {
			for (String boardName : boardArray) {
				if (!average.containsKey(boardName)) {
					average.put(boardName, new HashMap<>());
				}
				for (SearchStrategy searchStrategy: strategyArray) {
					if (!average.get(boardName).containsKey(searchStrategy)) {
						average.get(boardName).put(searchStrategy, new HashSet<>());
					}
                    final SokobanProblem problem = new SokobanProblem(ArgsReader.getFilePath(boardName), heuristic);
			    	final GPSEngine engine = new GPSEngine(problem, searchStrategy);

                    sb.append(boardName).append(";").append(searchStrategy.toString());

			    	try {
						final long elapsedTime = Main.findSolution(engine);

						if (engine.isFailed()) {
							sb.append(";-1;-1\n");
						}

						if (engine.isFinished() && !engine.isFailed()) {
							GPSNode solutionNode = engine.getSolutionNode();
							int nodeCount = 0;

							do {
								nodeCount++;
								solutionNode = solutionNode.getParent();
							} while (solutionNode != null);

							sb.append(String.format(";%d", nodeCount));
							sb.append(String.format(";%f\n", elapsedTime / 1E6));
							average.get(boardName).get(searchStrategy).add(new BoardResults(boardName, searchStrategy, nodeCount, elapsedTime / 1E6));
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						sb.append(";-;-\n");
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
