package eclipse.plugin.gpuv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.IToken;
import org.eclipse.cdt.core.parser.util.CharArrayIntMap;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eclipse.plugin.gpuv.builder.GPUVDefaultConsole;
import eclipse.plugin.gpuv.radix.RadixTree;
import eclipse.plugin.gpuv.radix.RadixTreeImpl;

/*
 * Read in XML files (options.xml, keywords.xml, appliedOptions.xml)
 * Methods can be accessed by the class name (static).
 */
public class XMLKeywordsManager {
	public static final int KEYWORD_SEARCH = 0;
	public static final int OPTION_SEARCH = 1;
	public static final int APPLIED_OPTIONS = 2;
	private static RadixTree<String> keywordTree;
	private static List<String> keywordList;
	private static Map<String, dataNode> optionMap;
	private static String installLocation;
	private static final String appliedFoldername = "applied";
	private static Map<String, String> appliedOptionMap;

	/*
	 * Initialise its private members
	 */
	public XMLKeywordsManager() {
		//Determine the location of this plugin to get location of GPUV binary
		Bundle bundle = Platform.getBundle("eclipse.plugin.gpuv");
		URL url = bundle.getEntry("xmlFiles");
		URL fileURL = null;
		try {
			fileURL = FileLocator.toFileURL(url);
		} catch (IOException e1) {
			GPUVDefaultConsole.printToConsole("Internal Error: Could not determine file path" +
					" of \'xmlFiles\' folder");
		}
		//Get platform specific path of the folder
		installLocation = new File(fileURL.getPath()).getPath();
		
		keywordTree = new RadixTreeImpl<String>();
		keywordList = new ArrayList<String>();
		optionMap = new HashMap<String, dataNode>();
		appliedOptionMap = new HashMap<String, String>();
		readXMLByType("keywords.xml", KEYWORD_SEARCH);
		readXMLByType("options.xml", OPTION_SEARCH);
		makeAppliedOptionDir();
	}

	// returns the keyword list for OpenCL language
	public static List<String> getKeywords() {
		if(keywordList == null)
		{
			keywordList = new ArrayList<String>();
			readXMLByType("keywords.xml", KEYWORD_SEARCH);
		}
		return keywordList;
	}
	
	public static void addKeywordsOpencl(CharArrayIntMap map) {
		for(String k : XMLKeywordsManager.getKeywords())
		{
			int token = IToken.t_int;
			if(k.equals("__kernel"))
				token = IToken.t_const;
			map.put(k.toCharArray(), token);
		}
	}

	// prefix search for auto suggestion (depends on search type)
	public static List<String> searchPrefix(String prefix, int recordLimit,
			int searchType) {
		String caseInsensitive = prefix.toLowerCase();
		if (searchType == OPTION_SEARCH) {
			return getMatchingOptions(caseInsensitive);
		}
		return keywordTree.searchPrefix(caseInsensitive, recordLimit);
	}

	/*
	 * Only return general options that does not take arguments
	 * (for general tab)
	 */
	public static Map<String, String> getGeneralOptions() {
		Map<String, String> result = new HashMap<String, String>();
		Iterator<Map.Entry<String, dataNode>> entries = optionMap.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry<String, dataNode> entry = entries.next();
			dataNode value = entry.getValue();
			if (value.getType().equals("GENERAL") && value.getArgNum() == 0) {
				String key = entry.getKey();
				result.put(key, key);
			}
		}
		return result;
	}

	// Returns true if the option can be selected multiple times
	public static boolean isMultiple(String optionName) {
		return optionMap.get(optionName).getMultiple();
	}
	// Returns true if the option takes argument input
	public static boolean takesInput(String optionName) {
		return optionMap.get(optionName).getArgNum() != 0;
	}
	// Returns the argument type of the option
	public static String getArgType(String optionName) {
		return optionMap.get(optionName).getArgType();
	}
	// Returns the number of arguments for the option
	public static int getArgNum(String optionName) {
		return optionMap.get(optionName).getArgNum();
	}
	
	// Auxiliary function for searching prefix match for options 
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

	// Make a directory for applied option xml files at initial run
	private void makeAppliedOptionDir() {
		File appliedFolder = new File(installLocation + File.separator + appliedFoldername);
		if (!appliedFolder.exists()) {
			appliedFolder.mkdir();
		}
	}

	// Write applied options to an xml file using DOM method
	public static void applyOptions(Map<String, String> optionsToStore) {
		String appliedFilename = new ActiveElementLocator().getOptionsFilename();
		if(appliedFilename == null){
			// cannot get the filename
			GPUVDefaultConsole.printToConsole("Internal Error: Cannot locate the file!");
			return;
		}
		try {
			File xmlFile = new File(installLocation + File.separator
					+ appliedFoldername
					+ File.separator + appliedFilename);

			// delete existing file
			if (xmlFile.exists()) {
				xmlFile.delete();
			}
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("keywords");
			doc.appendChild(rootElement);

			for (String key : optionsToStore.keySet()) {
				String value = optionsToStore.get(key);
				// keyword elements
				Element keyword = doc.createElement("keyword");
				rootElement.appendChild(keyword);

				// name elements
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(optionMap.get(value)
						.getName()));
				keyword.appendChild(name);

				// option elements
				Element option = doc.createElement("option");
				option.appendChild(doc.createTextNode(optionMap.get(value)
						.getOption()));
				keyword.appendChild(option);

				// actual option elements (with arguments substituted)
				Element actualOption = doc.createElement("actualOption");
				actualOption.appendChild(doc.createTextNode(key));
				keyword.appendChild(actualOption);

				// argType elements
				Element argType = doc.createElement("argType");
				argType.appendChild(doc.createTextNode(optionMap.get(value)
						.getArgType()));
				keyword.appendChild(argType);

				// argNum elements
				Element argNum = doc.createElement("argNum");
				argNum.appendChild(doc.createTextNode(""
						+ optionMap.get(value).getArgNum()));
				keyword.appendChild(argNum);

				// type elements
				Element type = doc.createElement("type");
				type.appendChild(doc.createTextNode(optionMap.get(value)
						.getType()));
				keyword.appendChild(type);

				// multiple elements
				Element multiple = doc.createElement("multiple");
				multiple.appendChild(doc.createTextNode(""
						+ optionMap.get(value).getMultiple()));
				keyword.appendChild(multiple);

				// description elements
				Element description = doc.createElement("description");
				description.appendChild(doc.createTextNode(optionMap.get(value)
						.getDescription()));
				keyword.appendChild(description);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(xmlFile);

				transformer.transform(source, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Read in appliedOptions.xml on each config box invocation. In one session,
	 * all used options are kept in appliedOptionSet, and is only flushed when
	 * Eclipse is restarted.
	 */
	public static Map<String, String> getAppliedOptions() {
		String appliedFilename = new ActiveElementLocator().getOptionsFilename();
		if(appliedFilename == null){
			// cannot get the filename
			GPUVDefaultConsole.printToConsole("Internal Error: Cannot locate the file!");
			return appliedOptionMap;
		}
		readXMLByType(appliedFoldername + File.separator
				+ appliedFilename,
				APPLIED_OPTIONS);
		return appliedOptionMap;
	}

	/*
	 * Read in specified xml files and create sets or lists depending on the
	 * type of search required (OPTION_SEARCH, KEYWORD_SEARCH, APPLIED_OPTIONS)
	 */
	private static void readXMLByType(String filename, int searchType) {
		try {
			File xmlFile = new File(installLocation + File.separator
					+ filename);
			if (!xmlFile.exists()) {
				return; // don't read if file does not exist
			}
			appliedOptionMap.clear(); //flush

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			// Need to check correctness of the xml file.
			Document doc = null;
			try {
				doc = dBuilder.parse(xmlFile);
			} catch (SAXException se) {
				// Incorrect xml file.
				GPUVDefaultConsole.printToConsole("Internal Error: !" +
						"XML File " + xmlFile.getName()
						+ " is corrupted!");
				System.exit(1);
			} catch (Exception e) {
				// Other errors
				GPUVDefaultConsole.printToConsole("Internal Error: !" +
						"An error occured while reading file!");
				System.exit(1);
			}

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("keyword");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String keyword = getTagValue("name", eElement);
					// save tag values according to search type 
					if (searchType == OPTION_SEARCH) { // for option search
						String option = getTagValue("option", eElement);
						String type = getTagValue("type", eElement);
						String multiple = getTagValue("multiple", eElement);
						String desc = getTagValue("description", eElement);
						String argType = getTagValue("argType", eElement);
						int argNum = Integer.parseInt(getTagValue("argNum",
								eElement));

						dataNode data = new dataNode(keyword, option, argType,
								argNum, type, multiple.equals("true"), desc);
						optionMap.put(option, data);
					} else if (searchType == APPLIED_OPTIONS) {
						// for applied options
						String option = getTagValue("option", eElement);
						String actualOption = getTagValue("actualOption",
								eElement);
						appliedOptionMap.put(actualOption, option);
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
	}

	// get tag value from DOM xml structure
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
}
