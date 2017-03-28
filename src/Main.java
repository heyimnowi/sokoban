import gps.GPSEngine;
import gps.GPSNode;
import gps.SearchStrategy;
import model.heuristics.NearPBBGHeuristic;
import model.heuristics.PBBGHeuristic;
import model.heuristics.PBNearBGHeuristic;
import model.heuristics.SimpleHeuristic;
import sokoban.SokobanProblem;
import sokoban.SokobanState;

public class Main {

    public static void main(String[] args) {
        // TODO: get path from args
        final String path = "res/boards/complicatedBoard.txt";
        final SokobanProblem problem = new SokobanProblem(path, new PBNearBGHeuristic());
        // TODO: chose strategy based on args
        final GPSEngine engine = new GPSEngine(problem, SearchStrategy.GREEDY);

        final long startTime = System.nanoTime();
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
    }

}
