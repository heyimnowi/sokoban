package utils;

import java.util.ArrayList;
import java.util.List;

import exceptions.NonExistingFileException;
import exceptions.StrategyNotFoundException;
import gps.GPSEngine;
import gps.GPSNode;
import gps.SearchStrategy;
import model.heuristics.PBNearBGHeuristic;
import sokoban.SokobanProblem;
import sokoban.SokobanState;

public class Metrics {
	
	public static List<String> filesArray;
		
	
	public static void getMetrics() {
		filesArray = new ArrayList<String>();
		filesArray.add("complicated2Board");
		filesArray.add("complicatedBoard");
		filesArray.add("defaultBoard");
		filesArray.add("impossibleBoard");
		filesArray.add("complicated2Board");
		filesArray.add("simpleBoard");	
		
		for (String fileName : filesArray) {
			System.out.println(fileName);
			for (SearchStrategy strategy: SearchStrategy.values()) {
				System.out.println(strategy);
		    	
				final SokobanProblem problem = new SokobanProblem("res/boards/" + fileName + ".txt", new PBNearBGHeuristic());
		    	final GPSEngine engine = new GPSEngine(problem, strategy);
		    	final long startTime = System.nanoTime();
		        engine.findSolution();
		        final long endtime = System.nanoTime();

		        if (engine.isFinished() && !engine.isFailed()) {
		            GPSNode solutionNode = engine.getSolutionNode();;
		            int nodeCount = 0;
		            do {
		                nodeCount++;
		                solutionNode = solutionNode.getParent();
		            } while (solutionNode != null);
		            System.out.println(String.format("Node count: %d", nodeCount));
		            System.out.println(String.format("Elapsed time: %f ms", (endtime - startTime) / 10E6));
		        }
			}
		}
	}

}
