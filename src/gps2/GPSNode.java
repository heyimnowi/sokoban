package gps2;

import gps2.api.GPSRule;
import gps2.api.GPSState;

public class GPSNode {

	private GPSState state;

	private GPSNode parent;

	private Integer cost;

	private Integer depth;

	private GPSRule generationRule;

	public GPSNode(GPSState state, Integer cost, Integer depth, GPSRule generationRule) {
		this.state = state;
		this.cost = cost;
		this.depth = depth;
		this.generationRule = generationRule;
	}

	public GPSNode getParent() {
		return parent;
	}

	public void setParent(GPSNode parent) {
		this.parent = parent;
		this.depth = parent.getDepth() + 1;
	}

	public GPSState getState() {
		return state;
	}

	public Integer getCost() {
		return cost;
	}

	public Integer getDepth() { return depth; }


	@Override
	public String toString() {
		return state.toString();
	}

	public String getSolution() {
		if (this.parent == null) {
			return this.state.toString();
		}
		return this.parent.getSolution() + this.state.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPSNode other = (GPSNode) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	public GPSRule getGenerationRule() {
		return generationRule;
	}

	public void setGenerationRule(GPSRule generationRule) {
		this.generationRule = generationRule;
	}

}
