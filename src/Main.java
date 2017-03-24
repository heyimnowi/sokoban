import gps.GPSEngine;
import gps.GPSNode;
import gps.SearchStrategy;
import sokoban.SokobanProblem;
import sokoban.SokobanState;

public class Main {

    public static void main(String[] args) {
        // TODO: get path from args
        final String path = "res/boards/defaultBoard.txt";
        final SokobanProblem problem = new SokobanProblem(path);
        // TODO: chose strategy based on args
        final GPSEngine engine = new GPSEngine(problem, SearchStrategy.BFS);

        engine.findSolution();
        if (engine.isFailed()) {
            System.out.println("No solution found");
        }

        if (engine.isFinished() && !engine.isFailed()) {
            GPSNode solutionNode = engine.getSolutionNode();;
            SokobanState solutionState;
            do {
                solutionState = (SokobanState) solutionNode.getState();

                System.out.println(solutionState.getBoard() + "\n");

                solutionNode = solutionNode.getParent();
            } while (solutionNode != null);
        }
    }

}
