package gps;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;

import java.util.*;

public class GPSEngine {

	Queue<GPSNode> open;
	Map<GPSState, Integer> bestCosts;
	GPSProblem problem;
	long explosionCounter;
	long currentDepth = 0;
	boolean finished;
	boolean failed;
	GPSNode solutionNode;

	// Use this variable in open set order.
	protected SearchStrategy strategy;

	public GPSEngine(GPSProblem myProblem, SearchStrategy myStrategy) {
		open = new PriorityQueue<>((o1, o2) -> {
		    switch (myStrategy) {
                case BFS:
                    return o1.getCost() - o2.getCost();
                case DFS:
                    return o2.getCost() - o1.getCost();
                case IDDFS:
                    return o2.getCost() - o1.getCost();
                case GREEDY:
                    return myProblem.getHValue(o1.getState()) - myProblem.getHValue(o2.getState());
                case ASTAR:
                    final int f1 = o1.getCost() + myProblem.getHValue(o1.getState());
                    final int f2 = o2.getCost() + myProblem.getHValue(o2.getState());

                    return f1 - f2;
                default:
                    throw new IllegalArgumentException("Strategy not implemented");
            }
        });
		bestCosts = new HashMap<>();
		problem = myProblem;
		strategy = myStrategy;
		explosionCounter = 0;
		finished = false;
		failed = false;
	}

	public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null);
		open.add(rootNode);
		while (open.size() > 0) {
			GPSNode currentNode = open.remove();
			boolean isIDDFS = strategy == SearchStrategy.IDDFS;
            boolean iddfsCondition =  !isIDDFS || currentNode.getCost() == currentDepth;

			if (iddfsCondition && problem.isGoal(currentNode.getState())) {
				finished = true;
				solutionNode = currentNode;
				return;
			} else {
                if (isIDDFS && currentNode.getCost() == 0) {
                    currentDepth++;
                    open.add(currentNode);
                }

				explode(currentNode);
			}
		}
        failed = true;
		finished = true;
	}

	private void explode(GPSNode node) {
        Collection<GPSNode> newCandidates;
		switch (strategy) {
		case BFS:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			open.addAll(newCandidates);
			break;
		case DFS:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			open.addAll(newCandidates);
			break;
		case IDDFS:
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			if (currentDepth > node.getCost()) {
                open.addAll(newCandidates);
            }
			break;
		case GREEDY:
            if (bestCosts.containsKey(node.getState())) {
                return;
            }
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			open.addAll(newCandidates);
			break;
		case ASTAR:
			if (!isBest(node.getState(), node.getCost())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			open.addAll(newCandidates);
			break;
		}
	}

	private void addCandidates(GPSNode node, Collection<GPSNode> candidates) {
		explosionCounter++;
		updateBest(node);
		for (GPSRule rule : problem.getRules()) {
			Optional<GPSState> newState = rule.evalRule(node.getState());
			if (newState.isPresent()) {
				GPSNode newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost(), rule);
				newNode.setParent(node);
				candidates.add(newNode);
			}
		}
	}

	private boolean isBest(GPSState state, Integer cost) {
		return !bestCosts.containsKey(state) || cost < bestCosts.get(state);
	}

	private void updateBest(GPSNode node) {
		bestCosts.put(node.getState(), node.getCost());
	}

	// GETTERS FOR THE PEOPLE!

	public Queue<GPSNode> getOpen() {
		return open;
	}

	public Map<GPSState, Integer> getBestCosts() {
		return bestCosts;
	}

	public GPSProblem getProblem() {
		return problem;
	}

	public long getExplosionCounter() {
		return explosionCounter;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isFailed() {
		return failed;
	}

	public GPSNode getSolutionNode() {
		return solutionNode;
	}

	public SearchStrategy getStrategy() {
		return strategy;
	}

}
