package eclipse.plugin.gpuv.radix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/*
 * XML reader layer on top of Radix Tree implementation.
 * TODO: currently same as RadixTree<String>. need to implement using xml DOM 
 */
public class XMLRadixTree {
	private RadixTree<String> rt;
	
	public XMLRadixTree(String filename){
		 this.rt = new RadixTreeImpl<String>();
		 createSearchTree(filename);
	}
	
	public ArrayList<String> searchPrefix(String prefix, int recordLimit){
		return rt.searchPrefix(prefix, recordLimit);
	}
	
	
	private void createSearchTree(String filename) {
		/*
		 * Creating a search tree for the keywords
		 */

		BufferedReader br = null;
		try {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream(filename);
			br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				if (!rt.contains(line)) {
					rt.insert(line, line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
