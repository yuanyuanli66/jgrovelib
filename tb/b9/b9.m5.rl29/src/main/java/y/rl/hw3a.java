package y.rl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;




/**
 * //https://www.mkyong.com/java/jackson-2-convert-java-object-to-from-json/
 * Example code for convert java code to JSON
 *    - create nodes & tree 
 *    - write tree to file
 * 
 * @author dwyk
 *
 */

public class hw3a {
	public static void main(String [] args) {
		
		
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		hw3 famTree = createFamilyTree();
		
		try {
			// Convert object to JSON string and save into a file directly
			mapper.writeValue(new File("/Users/dwyk/y6/tb/po/gbm_po/treeDisp/famTree.json"), famTree);

			// Convert object to JSON string
			String jsonInString = mapper.writeValueAsString(famTree);
			System.out.println(jsonInString);

			// Convert object to JSON string and pretty print
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(famTree);
			System.out.println(jsonInString);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}//end-try-catch	
			
	}//end-main
	
	private static hw3 createFamilyTree() {

		hw3 ft0 = new hw3();
		hw3 ft1 = new hw3();
		hw3 ft2 = new hw3();
		hw3 ft3 = new hw3();
		hw3 ft4 = new hw3();
		
		
		// init all nodes
		ft0.setName("ROOT");
		ft0.setId("0");

		ft1.setName("yyl");
		ft1.setId("1");

		ft2.setName("dsm");
		ft2.setId("2");

		ft3.setName("wbs");
		ft3.setId("3");
		
		ft4.setName("kbs");
		ft4.setId("4");
		
		//create family tree struct
		List<hw3> lst1 = new ArrayList<hw3>();
		lst1.add(ft1);
		lst1.add(ft2);
		ft0.setChildren(lst1);
		
		List<hw3> lst2 = new ArrayList<hw3>();
		lst2.add(ft3);
		lst2.add(ft4);
		ft1.setChildren(lst2);


		return ft0;

	}//end-createFamilyTree function

	
}//end-class
