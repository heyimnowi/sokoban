package utils;

import exceptions.HeuristicNotFoundException;
import exceptions.NonExistingFileException;
import exceptions.StrategyNotFoundException;
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
	private static List<Heuristic> heuristicArray;
	private static PrintWriter testPrintWriter;
	private static StringBuilder testStringBuilder;
	private static Map<String, Map<SearchStrategy, Set<BoardResults>>> average;
	
	public static void getMetrics(String board, String strategy, String heuristic, int iterations) throws FileNotFoundException {
		
		boardArray = new ArrayList<>();
		strategyArray = new ArrayList<>();
		heuristicArray = new ArrayList<>();
		average = new HashMap<>();
		
		try {
			getParams(board, strategy, heuristic);
			
			for (int i = 0; i < iterations; i++) {
				System.out.println("Iteration #" + (i+1));
				printHeaders(i+1);
				printMetrics(boardArray, strategyArray, heuristicArray);
				testPrintWriter.write(testStringBuilder.toString());
				testPrintWriter.close();
				System.out.println("CSV Printed");
			}
			printAverage(iterations);
		} catch (NonExistingFileException e) {
			System.out.println("Board not found!");
		} catch (StrategyNotFoundException e) {
			System.out.println("Strategy not found!");
		} catch (HeuristicNotFoundException e) {
			System.out.println("Heuristic not found!");
		} catch (Exception e) {
			System.out.println("Ups!");
		}
	}
	
	private static void getParams(String board, String strategy, String heuristic) throws NonExistingFileException, StrategyNotFoundException, HeuristicNotFoundException {
		if (board == null || board.isEmpty()){
			boardArray = FileScanner.listFiles("res/boards");
		} else {
			boardArray.add(ArgsReader.getFilePath(board));
		}

		if (strategy == null || strategy.isEmpty()) {
			strategyArray = Arrays.asList(SearchStrategy.values());
		} else {		
			strategyArray.add(ArgsReader.getStrategy(strategy));
		}
			
		if (heuristic == null || heuristic.isEmpty()) {
			heuristicArray.add(new PBBGHeuristic());
			heuristicArray.add(new PBNearBGHeuristic());
			heuristicArray.add(new SimpleHeuristic());
		} else {		
			heuristicArray.add(ArgsReader.getHeuristic(heuristic));
		}
	}

	private static void printAverage(int iterations) throws FileNotFoundException {
		PrintWriter averagePrinter = new PrintWriter(new File("average.csv"));
		StringBuilder sba = new StringBuilder();
		sba.append("Board").append(";Strategy").append(";Elapsed time [ms]\n");
		for (String boardName : average.keySet()) {
			for (SearchStrategy strategy : average.get(boardName).keySet()) {
				long elapsedTime = 0;
				for (BoardResults boardResult : average.get(boardName).get(strategy)) {
					elapsedTime = elapsedTime + boardResult.getElapsedTime();
				}

				elapsedTime = elapsedTime / average.get(boardName).get(strategy).size();
				sba.append(boardName).append(";").append(strategy.toString()).append(";").append(String.format("%f", elapsedTime / 1E6)).append('\n');
			}
		}	
        averagePrinter.write(sba.toString());
        averagePrinter.close();
        System.out.println("Average printed");
	}

	private static void printHeaders(int iteration) throws FileNotFoundException {
		testPrintWriter = new PrintWriter(new File("test_" + iteration + ".csv"));
		testStringBuilder = new StringBuilder();
		testStringBuilder.append("Board").append(";Heuristic").append(";Strategy").append(";Distance").append(";Elapsed time [ms]\n");
	}

	public static void printMetrics(List<String> boardArray, List<SearchStrategy> strategyArray, List<Heuristic> heuristicArray) throws FileNotFoundException {	
		for (Heuristic heuristic : heuristicArray) {
			printBoards(boardArray, strategyArray, heuristic);
		}
	}

	private static void printBoards(List<String> boardArray, List<SearchStrategy> strategyArray, Heuristic heuristic) {
		for (String boardName : boardArray) {
			if (!average.containsKey(boardName)) {
				average.put(boardName, new HashMap<>());
			}
			printStrategies(boardName, strategyArray, heuristic);
		}
		
	}

	private static void printStrategies(String boardName, List<SearchStrategy> strategyArray, Heuristic heuristic) {

		for (SearchStrategy searchStrategy: strategyArray) {
			System.out.println("Solving " +  boardName + " - " + heuristic + " & " + searchStrategy.toString());
			if (!average.get(boardName).containsKey(searchStrategy)) {
				average.get(boardName).put(searchStrategy, new HashSet<>());
			}
			
            final SokobanProblem problem = new SokobanProblem(boardName, heuristic);
	    	final GPSEngine engine = new GPSEngine(problem, searchStrategy);

            testStringBuilder.append(boardName).append(";"+heuristic).append(";" + searchStrategy.toString());

	    	try {
				final long elapsedTime = Main.findSolution(engine);

				if (engine.isFailed()) {
					testStringBuilder.append(";-1;-1\n");
				}

				if (engine.isFinished() && !engine.isFailed()) {
					GPSNode solutionNode = engine.getSolutionNode();
					int nodeCount = 0;

					do {
						nodeCount++;
						solutionNode = solutionNode.getParent();
					} while (solutionNode != null);

					testStringBuilder.append(String.format(";%d", nodeCount));
					testStringBuilder.append(String.format(";%f\n", elapsedTime / 1E6));
					average.get(boardName).get(searchStrategy).add(new BoardResults(boardName, searchStrategy, nodeCount, elapsedTime));
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				testStringBuilder.append(";-;-\n");
			}
		}
	}
}
