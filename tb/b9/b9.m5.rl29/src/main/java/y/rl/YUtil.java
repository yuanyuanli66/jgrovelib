package y.rl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class YUtil {
	
	public static ObjectMapper mapper = new ObjectMapper();
	static {
		
		mapper.setSerializationInclusion(Include.NON_NULL);
		
	}


}
