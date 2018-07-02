package ciir.umass.edu.learning.tree;

import java.util.List;

import y.rl.hw3;

/**
 * SplitBean is used by toJson() & getJson() in Split class
 *   - the generated JSON format is intend to be displayed by D3
 * 
 * @author yyl & dsm
 *
 */

public class SplitBean {

	//Key attributes of a split (tree node for d3)
	protected String id;
	protected String name;
	protected String value;
	protected List<SplitBean> children;

	static int nid=0; //yyl for node id in each tree (D3 need ordered id to draw connections)


	public SplitBean() {
	}//end-constructor
	
	public SplitBean(String id, String name, String value, List<SplitBean> children) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.children = children;
	}//end-constructor

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<SplitBean> getChildren() {
		return children;
	}

	public void setChildren(List<SplitBean> children) {
		this.children = children;
	}
	

	
}//end-SplitObj class
