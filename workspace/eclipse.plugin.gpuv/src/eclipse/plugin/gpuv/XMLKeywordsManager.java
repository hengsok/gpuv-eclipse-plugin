package eclipse.plugin.gpuv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private static Map<String, dataNode> optionMap;

	public XMLKeywordsManager() {
		keywordTree = new RadixTreeImpl<String>();
		keywordList = new ArrayList<String>();
		optionMap = new HashMap<String, dataNode>();
		createSearchTree("keywords.xml", KEYWORD_SEARCH);
		createSearchTree("options.xml", OPTION_SEARCH);
	}

	public static List<String> getKeywords() {
		return keywordList;
	}

	public static List<String> searchPrefix(String prefix, int recordLimit,
			int searchType) {
		if (searchType == OPTION_SEARCH) {
			return getMatchingOptions(prefix.toLowerCase());
		}
		return keywordTree.searchPrefix(prefix, recordLimit);
	}

	// TODO maybe don't put options that take arguments?
	public static Set<String> getGeneralOptions() {
		Set<String> result = new LinkedHashSet<String>();
		Iterator<Map.Entry<String, dataNode>> entries = optionMap.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry<String, dataNode> entry = entries.next();
			dataNode value = entry.getValue();
			if (value.getType().equals("GENERAL")
					&& !value.getMultiple()) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	public static boolean isMultiple(String optionName) {
		return optionMap.get(optionName).getMultiple();
	}

	private static List<String> getMatchingOptions(String prefix) {
		List<String> result = new ArrayList<String>();

		Iterator<Map.Entry<String, dataNode>> entries = optionMap.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry<String, dataNode> entry = entries.next();
			dataNode value = entry.getValue();
			if (value.getDescription().toLowerCase().contains(prefix)
					|| value.getOption().toLowerCase().contains(prefix)
					|| value.getType().toLowerCase().contains(prefix)){
				result.add(entry.getKey()); // add option name
			}
		}
		return result;
	}

	private void createSearchTree(String filename, int searchType) {
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
					if (searchType == OPTION_SEARCH) { // for option search
						String option = getTagValue("option", eElement);
						String type = getTagValue("type", eElement);
						String multiple = getTagValue("multiple", eElement);
						String desc = getTagValue("description", eElement);
						String argType = getTagValue("argType", eElement);
						int argNum = Integer.parseInt(getTagValue("argNum", eElement));

						// TODO arguments
						dataNode data = new dataNode(keyword, option, argType, argNum,
								type, multiple.equals("true"), desc);
						optionMap.put(option, data);
					} else { // for editor keyword suggestion
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
			keywordList.add(str); // add C keywords
		}

	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
}
