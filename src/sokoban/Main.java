package sokoban;

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
import java.util.concurrent.*;

import exceptions.NonExistingFileException;

public class Main {

    private static final int CUT_CONDITION_TIME = 5;

    public static void main(String[] args) throws StrategyNotFoundException, NonExistingFileException, FileNotFoundException {
    	try {
            final Properties properties = getProperties();
    		final String board = properties.getProperty("board");
    		final String strategy = properties.getProperty("strategy");
    		final String iterationsString = properties.getProperty("iterations");
    		int iterations = 1;
    		
    		if (!iterationsString.isEmpty() && iterationsString != null) {
    			iterations = Integer.valueOf(iterationsString);
    		}
    		
    		if (board != null && !board.isEmpty() && strategy != null && !strategy.isEmpty()) {
    			getSolution(board, strategy);
    		} else {
    			if (iterations == 0) {
    				iterations = 1;
    			}
    			Metrics.getMetrics(board, strategy, iterations);
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
            final long elapsedTime = findSolution(engine);

	        if (engine.isFailed()) {
	            System.out.println("No solution found");
	        }

	        if (engine.isFinished() && !engine.isFailed()) {
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
	            System.out.println(String.format("Elapsed time: %f ms", elapsedTime / 10E6));
	        }
		} catch (StrategyNotFoundException e) {
			System.out.println("Strategy not found!");
		} catch (NonExistingFileException e) {
			System.out.println("File not found!");
		} catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Time out");
        }
    }
	
	private static Properties getProperties() throws IOException {
		final Properties properties = new Properties();
		final String current = new java.io.File(".").getCanonicalPath();
        final File file = new File(current + "/res/config.properties");
		//file = new File("./config.properties");
		final FileInputStream fis = new FileInputStream(file);

		properties.load(fis);

		return properties;
	}

	public static long findSolution(final GPSEngine engine) throws InterruptedException, ExecutionException, TimeoutException {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final Future<?> future = service.submit(engine::findSolution);
        final long startTime = System.nanoTime(); // It should go before future.get(...) but it's prettier like this :)

        service.shutdown();
        future.get(CUT_CONDITION_TIME, TimeUnit.MINUTES);
        service.shutdownNow();

        return System.nanoTime() - startTime;
    }

}
