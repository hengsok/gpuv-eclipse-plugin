package eclipse.plugin.gpuv.radix;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/*
 * XML reader layer on top of Radix Tree implementation.
 * TODO: need to contain information of each keyword in the tree as 'value' -> class?
 * TODO: need to clean up keywords (charn -> char8 ... and Abstract Data Types -> .... ) 
 * TODO 3: apply a design pattern for setting values ... including what tags to use as Key and Value pair
 */
public class XMLRadixTree {
	private RadixTree<String> rt;
	private boolean isOptionSearch;

	public XMLRadixTree(String filename, boolean isOptionSearch) {
		this.rt = new RadixTreeImpl<String>();
		this.isOptionSearch = isOptionSearch;
		createSearchTree(filename);
	}

	public ArrayList<String> searchPrefix(String prefix, int recordLimit) {
		if(isOptionSearch){
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
					if (isOptionSearch) { // for option search
						String option = getTagValue("option", eElement);
						
						// attach all searchKeys to options 
						NodeList searchKeys = eElement.getElementsByTagName("searchKey");
						for(int i=0; i< searchKeys.getLength(); i++){
							String searchKey = searchKeys.item(i).getTextContent().toLowerCase();
							if (!rt.contains(searchKey)) {
								rt.insert(searchKey, option);
							}
						}
						// add the option itself
						if (!rt.contains(option)) {
							rt.insert(option, option);
						}
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
