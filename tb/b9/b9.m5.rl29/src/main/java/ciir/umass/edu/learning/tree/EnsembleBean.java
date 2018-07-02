package ciir.umass.edu.learning.tree;
/**
 * EnsembleBean is used by toJson() & getJson() in Ensemble class
 * 
 * @author yyl & dsm
 *
 */

public class EnsembleBean {
	
	public int treeID = -1;
	public Float weight = null;
	public RegressionTree regTree = null;
	
	
	public int getTreeID() {
		return treeID;
	}
	public void setTreeID(int treeID) {
		this.treeID = treeID;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public RegressionTree getRegTree() {
		return regTree;
	}
	public void setRegTree(RegressionTree regTree) {
		this.regTree = regTree;
	}

}//end-class EnsembleBean
