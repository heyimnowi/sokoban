package gps2;

import java.util.*;

import gps2.api.GPSProblem;
import gps2.api.GPSRule;
import gps2.api.GPSState;

public class GPSEngine {

	Deque<GPSNode> open;
	Map<GPSState, Integer> bestCosts;
	GPSProblem problem;
	long explosionCounter;
	boolean finished;
	boolean failed;
	GPSNode solutionNode;

	//My implementation!
	public GPSNode rootNode;

	// Use this variable in open set order.
	protected SearchStrategy strategy;

	public GPSEngine(GPSProblem myProblem, SearchStrategy myStrategy) {
		open = new LinkedList<>();
		bestCosts = new HashMap<>();
		problem = myProblem;
		strategy = myStrategy;
		explosionCounter = 0;
		finished = false;
		failed = false;
	}

	public void findSolution() {
		int maxDepth = 0, ignoredNodes = 0;
		while ((strategy == SearchStrategy.IDDFS && ignoredNodes != 0 )|| maxDepth == 0) {
			//   GPSNode rootNode = new GPSNode(problem.getInitState(), 0);
			ignoredNodes = 0;
			GPSNode rootNode = new GPSNode(problem.getInitState(), 0, 0, null);
			open.add(rootNode);

			while (open.size() > 0) {
				GPSNode currentNode = open.remove();
				if (problem.isGoal(currentNode.getState())) {
					finished = true;
					solutionNode = currentNode;
					return;
				} else {
					if (strategy != SearchStrategy.IDDFS || currentNode.getDepth() <= maxDepth) {
						explode(currentNode);
					} else {
						ignoredNodes++;
					}
				}
			}
			maxDepth++;
			bestCosts.clear();
		}
		if(!finished){
			failed = true;
		}
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
				newCandidates.forEach( n -> open.addLast(n));
				break;
			case IDDFS:
			case DFS:
				if (bestCosts.containsKey(node.getState())) {
					return;
				}
				newCandidates = new ArrayList<>();
				addCandidates(node, newCandidates);
				newCandidates.forEach( n -> open.addFirst(n));
				break;
			case GREEDY:
				if (bestCosts.containsKey(node.getState())) {
					return;
				}
				newCandidates = new PriorityQueue<>(new Comparator<GPSNode>() {
					@Override
					public int compare(GPSNode o1, GPSNode o2) {
						return getProblem().getHValue(o2.getState()) - getProblem().getHValue(o1.getState());
					}
				});
				addCandidates(node, newCandidates);
				newCandidates.forEach( n -> open.addFirst(n));
				break;
			case ASTAR:
				if (!isBest(node.getState(), node.getCost())) {
					return;
				}
				newCandidates = new PriorityQueue<>(new Comparator<GPSNode>() {
					@Override
					public int compare(GPSNode o1, GPSNode o2) {
						return (o2.getCost() + getProblem().getHValue(o2.getState())) - (getProblem().getHValue(o1.getState()) + o1.getCost());
					}
				});
				addCandidates(node, newCandidates);
				newCandidates.forEach( n -> open.addFirst(n));
				break;
		}
	}

	private void addCandidates(GPSNode node, Collection<GPSNode> candidates) {
		explosionCounter++;
		updateBest(node);
		for (GPSRule rule : problem.getRules()) {
			Optional<GPSState> newState = rule.evalRule(node.getState());
			if (newState.isPresent()) {
				GPSNode newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost(), 0, rule);
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
