import exceptions.StrategyNotFoundException;
import gps.GPSEngine;
import gps.GPSNode;
import model.heuristics.PBNearBGHeuristic;
import sokoban.SokobanProblem;
import sokoban.SokobanState;
import utils.ArgsReader;
import utils.Metrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import exceptions.NonExistingFileException;

public class Main {

	public static Properties properties;
	public static File file;

    public static void main(String[] args) throws StrategyNotFoundException, NonExistingFileException, FileNotFoundException {    	
    	
    	try {
    		properties = getProperties();
    		String board = properties.getProperty("board");
    		String strategy = properties.getProperty("strategy");
    		if (board != null && !board.isEmpty() && !strategy.isEmpty() && strategy != null) {
    			getSolution(board, strategy);
    		} else {
    			Metrics.getMetrics(board, strategy);
    		}
		} catch (IOException e) {
			System.out.println("Cant read config.properties");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Ups!");
		}	
    }

	private static void getSolution(String fileName, String strategy) {
		try {
			final String path = ArgsReader.getFilePath(fileName);
	    	final SokobanProblem problem = new SokobanProblem(path, new PBNearBGHeuristic());
	    	final GPSEngine engine = new GPSEngine(problem, ArgsReader.getStrategy(strategy));
	    	final long startTime = System.nanoTime();
	        engine.findSolution(startTime);
	        final long endtime = System.nanoTime();

	        if (engine.isFailed()) {
	            System.out.println("No solution found");
	        }

	        if (engine.isTimeOut()) {
	            System.out.println("Time out");
	        }

	        if (engine.isFinished() && !engine.isFailed() && !engine.isTimeOut()) {
	            GPSNode solutionNode = engine.getSolutionNode();;
	            SokobanState solutionState;
	            int nodeCount = 0;
	            do {
	                nodeCount++;
	                solutionState = (SokobanState) solutionNode.getState();

	                System.out.println(solutionState.getBoard() + "\n");
	                System.out.println(String.format("Heuristic value: %d", problem.getHValue(solutionState)));

	                solutionNode = solutionNode.getParent();
	            } while (solutionNode != null);

	            System.out.println(String.format("Node count: %d", nodeCount));
	            System.out.println(String.format("Elapsed time: %f ms", (endtime - startTime) / 10E6));
	        }
		} catch (StrategyNotFoundException e) {
			System.out.println("Strategy not found!");
		} catch (NonExistingFileException e) {
			System.out.println("File not found!");
		}
	}
	
	private static Properties getProperties() throws IOException {
		Properties properties = new Properties();
		String current = new java.io.File(".").getCanonicalPath();
		file = new File(current + "/res/config.properties");
		//file = new File("./config.properties");
		FileInputStream fis = new FileInputStream(file);
		properties.load(fis);
		return properties;
	}

}
