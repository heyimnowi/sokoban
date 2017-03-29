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
			System.out.print(fileName);
			for (SearchStrategy strategy: SearchStrategy.values()) {
				System.out.print("," + strategy);
		    	
				final SokobanProblem problem = new SokobanProblem("res/boards/" + fileName + ".txt", new PBNearBGHeuristic());
		    	final GPSEngine engine = new GPSEngine(problem, strategy);
		    	final long startTime = System.nanoTime();
		        engine.findSolution();
		        final long endtime = System.nanoTime();
		        
		        if (engine.isFailed()) {
		            System.out.print(",-");
		        }

		        if (engine.isFinished() && !engine.isFailed()) {
		            GPSNode solutionNode = engine.getSolutionNode();;
		            int nodeCount = 0;
		            do {
		                nodeCount++;
		                solutionNode = solutionNode.getParent();
		            } while (solutionNode != null);
		            System.out.println(String.format(",%d", nodeCount));
		            System.out.println(String.format(",%f", (endtime - startTime) / 10E6));
		        }
			}
		}
	}

}
