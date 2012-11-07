package eclipse.plugin.gpuv.radix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/*
 * XML reader layer on top of Radix Tree implementation.
 * TODO: need to contain information of each keyword in the tree as 'value' -> class?
 * TODO: need to clean up keywords (charn -> char8 ... and Abstract Data Types -> .... ) 
 * TODO 3: apply a design pattern for setting values ... including what tags to use as Key and Value pair
 */
public class XMLRadixTree {
	private RadixTree<String> rt;
	private boolean caseSensitive;

	public XMLRadixTree(String filename, boolean caseSensitive) {
		this.rt = new RadixTreeImpl<String>();
		this.caseSensitive = caseSensitive;
		createSearchTree(filename);
	}

	public ArrayList<String> searchPrefix(String prefix, int recordLimit) {
		if(!caseSensitive){
			prefix = prefix.toLowerCase();
		}
		return rt.searchPrefix(prefix, recordLimit);
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
					if (caseSensitive) {
						if (!rt.contains(keyword)) {
							rt.insert(keyword, keyword);
						}
					} else {
						String lowerCase = keyword.toLowerCase();
						if (!rt.contains(lowerCase)) {
							rt.insert(lowerCase, keyword);
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
