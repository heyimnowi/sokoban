package utils;

import java.util.ArrayList;
import java.util.List;

import gps.GPSEngine;
import gps.GPSNode;
import gps.SearchStrategy;
import model.heuristics.PBNearBGHeuristic;
import sokoban.SokobanProblem;

public class Metrics {
	
	public static List<String> filesArray;
		
	
	public static void getMetrics() {
		long startTime;
		filesArray = new ArrayList<String>();
		filesArray.add("complicated2Board");
		filesArray.add("complicatedBoard");
		filesArray.add("defaultBoard");
		filesArray.add("impossibleBoard");
		filesArray.add("solutionBoard");
		filesArray.add("simpleBoard");	
		
		System.out.print("Board");
		System.out.print(",Strategy");
		System.out.print(",Node count");
		System.out.println(",Elapsed time [ms]");
		
		for (String fileName : filesArray) {
			for (SearchStrategy strategy: SearchStrategy.values()) {
				System.out.print(fileName + "," + strategy);
				startTime = System.nanoTime();
				final SokobanProblem problem = new SokobanProblem("res/boards/" + fileName + ".txt", new PBNearBGHeuristic());
		    	final GPSEngine engine = new GPSEngine(problem, strategy);
		    	startTime = System.nanoTime();
		        engine.findSolution(startTime);
		        final long endtime = System.nanoTime();
		        
		        if (engine.isFailed()) {
		            System.out.println(",-,-");
		        }

		        if (engine.isFinished() && !engine.isFailed()) {
		            GPSNode solutionNode = engine.getSolutionNode();;
		            int nodeCount = 0;
		            do {
		                nodeCount++;
		                solutionNode = solutionNode.getParent();
		            } while (solutionNode != null);
		            System.out.print(String.format(",%d", nodeCount));
		            System.out.println(String.format(",%f", (endtime - startTime) / 10E6));
		        }
			}
		}
	}

}
