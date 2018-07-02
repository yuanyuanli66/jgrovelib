package d.rl;

import java.util.*;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;


class BookInfo {
	
	public BookInfo(String author, String isbn) {
		
	}
	
}

public class TrieTry {
	
	
	public static void main(String [] args) {
		
		Map<String, BookInfo> library = new LinkedHashMap<>();
		/* Load your regualr map with data */
		library.put("To Kill a Mockingbird", new BookInfo("Harper Lee", "978-0061120084"));
		library.put("Pride and Prejudice", new BookInfo("Jane Austen", "978-0679783268"));
		library.put("To Kill A Warlock", new BookInfo("H.P. Mallory", "978-xxxxxxxx"));
		library.put("To Kill For", new BookInfo("A.J. Carella", "978-xxxxxxxx"));
		library.put("To Kill For", new BookInfo("A.J. Carella", "978-xxxxxxxx"));
		
		
		/* PatriciaTrie map*/
		Trie<String, BookInfo> aTrie = new PatriciaTrie<>(library);		
		SortedMap<String,BookInfo> sm = aTrie.prefixMap("To");
		for( String aKey : sm.keySet() ) {
			System.out.println( sm.get(aKey) );
		}
		/* Subset of data based on your query string "To Kill" */
		//SortedMap<String, BookInfo> entries = WordList.wordMapTrie.prefixMap("To Kill");

		
	}

}
