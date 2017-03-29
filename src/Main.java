import exceptions.StrategyNotFoundException;
import gps.GPSEngine;
import gps.GPSNode;
import model.heuristics.PBNearBGHeuristic;
import sokoban.SokobanProblem;
import sokoban.SokobanState;
import utils.ArgsReader;
import exceptions.NonExistingFileException;

public class Main {

    public static void main(String[] args) throws StrategyNotFoundException, NonExistingFileException {

        try {        
        	final String path = ArgsReader.getFilePath(args[0]);
        	final SokobanProblem problem = new SokobanProblem(path, new PBNearBGHeuristic());
        	final GPSEngine engine = new GPSEngine(problem, ArgsReader.getStrategy(args[1]));final long startTime = System.nanoTime();
            engine.findSolution();
            final long endtime = System.nanoTime();
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
                System.out.println(String.format("Elapsed time: %f ms", (endtime - startTime) / 10E6));
            }
		} catch (StrategyNotFoundException e) {
			System.out.println("Strategy not found!");
		} catch (NonExistingFileException e) {
			System.out.println("File not found!");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid arguments!");
		}
    }

}
