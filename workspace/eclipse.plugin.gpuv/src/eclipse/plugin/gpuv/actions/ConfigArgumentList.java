package eclipse.plugin.gpuv.actions;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ConfigArgumentList {
	private Set<String> argList;

	public ConfigArgumentList(String filename) throws IOException {

		argList = new HashSet<String>();
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
					if (getTagValue("type", eElement).equals("GENERAL")) {
						String option = getTagValue("option", eElement);
						if (!argList.contains(option)) {
							argList.add(option);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Set<String> getArgList() {
		return argList;
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

}
