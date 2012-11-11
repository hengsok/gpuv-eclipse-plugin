package eclipse.plugin.gpuv.contentassist;

import java.util.ArrayList;

import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;

import eclipse.plugin.gpuv.radix.XMLRadixTree;

public class KeywordsManager {
	public static ArrayList<String> getKeywords()
	{
		XMLRadixTree rt = new XMLRadixTree("keywords.xml", false);
		ArrayList<String> OpenCLKeywords = rt.searchPrefix("", 10000);
		for(String str:GCCLanguage.getDefault().getKeywords())
		{
			OpenCLKeywords.add(str);
		}
		return OpenCLKeywords;
	}
}
