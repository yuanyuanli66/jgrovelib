package ciir.umass.edu.learning.tree;

import java.util.List;

public class RegressionTreeBean {
	//Member variables and functions 
	public Split root = null;
	public List<Split> leaves = null;
	
	
	public Split getRoot() {
		return root;
	}
	public void setRoot(Split root) {
		this.root = root;
	}
	public List<Split> getLeaves() {
		return leaves;
	}
	public void setLeaves(List<Split> leaves) {
		this.leaves = leaves;
	}


}
