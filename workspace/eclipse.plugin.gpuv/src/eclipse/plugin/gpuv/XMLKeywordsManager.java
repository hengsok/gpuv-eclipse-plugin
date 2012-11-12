package eclipse.plugin.gpuv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import eclipse.plugin.gpuv.radix.RadixTree;
import eclipse.plugin.gpuv.radix.RadixTreeImpl;

/*
 * XML reader layer on top of Radix Tree implementation.
 * TODO 1: need to contain information of each keyword in the tree as 'value' -> class?
 * TODO 2: need to clean up keywords (charn -> char8 ... and Abstract Data Types -> .... ) 
 * TODO 3: need to merge ConfigArgumentlist, ConfigRecently...
 */

/*
 * Read in xml files (options.xml, keywords.xml) and store them once (in Activator).
 * Methods can be accessed by the class name (static).
 */
public class XMLKeywordsManager {
	public static final int KEYWORD_SEARCH = 0;
	public static final int OPTION_SEARCH = 1;
	private static RadixTree<String> keywordTree;
	private static List<String> keywordList;
	private static List<String> optionList;

	public XMLKeywordsManager() {
		keywordTree = new RadixTreeImpl<String>();
		keywordList = new ArrayList<String>();
		optionList = new ArrayList<String>();
		createSearchTree("keywords.xml", KEYWORD_SEARCH);
		createSearchTree("options.xml", OPTION_SEARCH);
	}

	public static List<String> getKeywords(){
		return keywordList;
	}
	
	public static List<String> searchPrefix(String prefix, int recordLimit, int type) {
		if(type == OPTION_SEARCH){
			return getMatchingOptions(prefix.toLowerCase());
		}
		return keywordTree.searchPrefix(prefix, recordLimit);
	}

	private static List<String> getMatchingOptions(String prefix) {
		List<String> result = new ArrayList<String>();
		Iterator<String> it = optionList.iterator();
		while(it.hasNext()){
			String full = it.next();
			if(full.toLowerCase().contains(prefix)){ //matches some text
				result.add(full.split(" ")[0]); //add option name
			}
		}
		return result;
	}

	private void createSearchTree(String filename, int type) {
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
					if (type == OPTION_SEARCH) { // for option search
						String option = getTagValue("option", eElement);
						// attach all searchKeys to options 
						String desc = getTagValue("description", eElement);
						optionList.add(option + " " + desc);
					} else { //for editor keyword suggestion
						if (!keywordTree.contains(keyword)) {
							keywordTree.insert(keyword, keyword);
							keywordList.add(keyword);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String str : GCCLanguage.getDefault().getKeywords()) {
			keywordList.add(str);  // add C keywords
		}
		
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

}
