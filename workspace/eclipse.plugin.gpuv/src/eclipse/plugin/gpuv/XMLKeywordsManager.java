package eclipse.plugin.gpuv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
	public static final int RECENT_OPTIONS = 2;
	private static RadixTree<String> keywordTree;
	private static List<String> keywordList;
	private static Map<String, dataNode> optionMap;
	private static String installLocation;
	private static String foldername;
	private static final String recentFilename = "recentArgs.xml";
	
	private static HashSet<String> recentArgSet; //TODO necessary?

	public XMLKeywordsManager(String location) {
		keywordTree = new RadixTreeImpl<String>();
		keywordList = new ArrayList<String>();
		optionMap = new HashMap<String, dataNode>();
		installLocation = location;
		foldername = "."; // TODO gather all files
		recentArgSet = new LinkedHashSet<String>();
		readXMLByType("keywords.xml", KEYWORD_SEARCH);
		readXMLByType("options.xml", OPTION_SEARCH);
	}

	public static List<String> getKeywords() {
		return keywordList;
	}

	public static List<String> searchPrefix(String prefix, int recordLimit,
			int searchType) {
		String caseInsensitive = prefix.toLowerCase();
		if (searchType == OPTION_SEARCH) {
			return getMatchingOptions(caseInsensitive);
		}
		return keywordTree.searchPrefix(caseInsensitive, recordLimit);
	}

	// TODO maybe don't put options that take arguments?
	public static Set<String> getGeneralOptions() {
		Set<String> result = new LinkedHashSet<String>();
		Iterator<Map.Entry<String, dataNode>> entries = optionMap.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry<String, dataNode> entry = entries.next();
			dataNode value = entry.getValue();
			if (value.getType().equals("GENERAL") && value.getArgNum() == 0) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	public static boolean isMultiple(String optionName) {
		return optionMap.get(optionName).getMultiple();
	}

	public static boolean takesInput(String optionName) {
		return optionMap.get(optionName).getArgNum() != 0;
	}

	public static String getArgType(String optionName) {
		return optionMap.get(optionName).getArgType();
	}

	public static int getArgNum(String optionName) {
		return optionMap.get(optionName).getArgNum();
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
					|| value.getType().toLowerCase().contains(prefix)) {
				result.add(entry.getKey()); // add option name
			}
		}
		return result;
	}

	public static void storeRecentArgs(Map<String, String> recentArgsToStore) {
		try {
			File file = new File(installLocation + File.separator + foldername
					+ File.separator + recentFilename);

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("keywords");
			doc.appendChild(rootElement);

			for (String arg : recentArgsToStore.keySet()) {

				// keyword elements
				Element keyword = doc.createElement("keyword");
				rootElement.appendChild(keyword);

				// name elements
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(optionMap.get(arg).getName()));
				keyword.appendChild(name);

				// option elements
				Element option = doc.createElement("option");
				option.appendChild(doc.createTextNode(optionMap.get(arg).getOption()));
				keyword.appendChild(option);

				// argType elements
				Element argType = doc.createElement("argType");
				argType.appendChild(doc.createTextNode(optionMap.get(arg).getArgType()));
				keyword.appendChild(argType);
				
				// argNum elements
				Element argNum = doc.createElement("argNum");
				argNum.appendChild(doc.createTextNode(""+optionMap.get(arg).getArgNum()));
				keyword.appendChild(argNum);
				
				// type elements
				Element type = doc.createElement("type");
				type.appendChild(doc.createTextNode(optionMap.get(arg).getType()));
				keyword.appendChild(type);
				
				// multiple elements
				Element multiple = doc.createElement("multiple");
				multiple.appendChild(doc.createTextNode(""+ optionMap.get(arg).getMultiple()));
				keyword.appendChild(multiple);
				
				// description elements
				Element description = doc.createElement("description");
				description.appendChild(doc.createTextNode(optionMap.get(arg).getDescription()));
				keyword.appendChild(description);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(file);

				transformer.transform(source, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 
	 * read in recentArgs.xml on each config box invocation.
	 * In one session, all used options are kept in recentArgSet, 
	 * and is only flushed when Eclipse is restarted. 
	 */
	public static HashSet<String> getRecentArgs() {
		readXMLByType(recentFilename, RECENT_OPTIONS);
		return recentArgSet;
	}

	private static void readXMLByType(String filename, int searchType) {
		/*
		 * Creating a search tree for the keywords
		 */
		try {
			File xmlFile = new File(installLocation + File.separator
					+ foldername + File.separator + filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
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
						int argNum = Integer.parseInt(getTagValue("argNum",
								eElement));

						// TODO arguments
						dataNode data = new dataNode(keyword, option, argType,
								argNum, type, multiple.equals("true"), desc);
						optionMap.put(option, data);
					} else if(searchType == RECENT_OPTIONS) { //TODO merge with OPTION_SEARCH
						// for recently used options
						String option = getTagValue("option", eElement);
						recentArgSet.add(option);
					} else { // for editor keyword suggestion
						String lowerKeyword = keyword.toLowerCase();
						if (!keywordTree.contains(lowerKeyword)) {
							keywordTree.insert(lowerKeyword, keyword);
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
