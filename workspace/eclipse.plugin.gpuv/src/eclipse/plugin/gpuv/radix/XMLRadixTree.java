package eclipse.plugin.gpuv.radix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/*
 * XML reader layer on top of Radix Tree implementation.
 * TODO 1: need to contain information of each keyword in the tree as 'value' -> class?
 * TODO 2: need to clean up keywords (charn -> char8 ... and Abstract Data Types -> .... ) 
 * TODO 3: static constants to specify which type of search to use (e.g. OPTION or PREFIX)
 * TODO 4: search description, or use searchkeys? which one is more practical? 
 */
public class XMLRadixTree {
	private RadixTree<String> rt;
	private List<String> optionList;
	private boolean isOptionSearch;

	public XMLRadixTree(String filename, boolean isOptionSearch) {
		this.rt = new RadixTreeImpl<String>();
		this.optionList = new ArrayList<String>();
		this.isOptionSearch = isOptionSearch;
		createSearchTree(filename);
	}

	public ArrayList<String> searchPrefix(String prefix, int recordLimit) { //TODO change ArrayList to List? 
		if(isOptionSearch){
			return getMatchingOptions(prefix.toLowerCase());
		}
		return rt.searchPrefix(prefix, recordLimit);
	}

	private ArrayList<String> getMatchingOptions(String prefix) {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> it = optionList.iterator();
		while(it.hasNext()){
			String full = it.next();
			if(full.toLowerCase().contains(prefix)){ //matches some text
				result.add(full.split(" ")[0]); //add option name
			}
		}
		return result;
	}

	private void createSearchTree(String filename) {
		/*
		 * Creating a search tree for the keywords
		 */
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(this.getClass().getClassLoader()
					.getResourceAsStream(filename));
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("keyword");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String keyword = getTagValue("name", eElement);
					if (isOptionSearch) { // for option search
						String option = getTagValue("option", eElement);
						// attach all searchKeys to options 
						String desc = getTagValue("description", eElement);
						optionList.add(option + " " + desc);
					} else { //for editor keyword suggestion
						if (!rt.contains(keyword)) {
							rt.insert(keyword, keyword);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

}
