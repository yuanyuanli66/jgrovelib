package y.rl;

import java.util.List;
/**
 * Basic code for creating a JSON object
 * 
 * 
 * @author dwyk
 *
 */
public class hw3 {

	protected String id;
	protected String name;
	protected String type;
	protected List<hw3> children;

	public hw3() {
	}//end-constructor
	
	public hw3(String id, String name, String type, List<hw3> children) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<hw3> getChildren() {
		return children;
	}

	public void setChildren(List<hw3> children) {
		this.children = children;
	}
	
}//end-class