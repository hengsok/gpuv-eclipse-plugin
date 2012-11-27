package eclipse.plugin.gpuv.language;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.dom.parser.IScannerExtensionConfiguration;
import org.eclipse.cdt.core.dom.parser.c.ICParserExtensionConfiguration;
import org.eclipse.cdt.core.model.ICLanguageKeywords;
import org.eclipse.cdt.core.parser.ParserLanguage;
import org.eclipse.cdt.core.parser.util.ArrayUtil;

import eclipse.plugin.gpuv.XMLKeywordsManager;

public class OpenCLSourceCode extends GCCLanguage implements ICLanguageKeywords {

	@Override
	public String[] getKeywords() {
		List<String> kwl = XMLKeywordsManager.getKeywords();
		String[] kws = new String[kwl.size()];
		kwl.toArray(kws);
		
		return ArrayUtil.addAll(super.getKeywords(), kws);
	}

	@Override
	public String[] getBuiltinTypes() {
		return ArrayUtil.addAll(super.getBuiltinTypes(), new String[] { "FooType",
				"BarType" });
	}
	
	@Override
	protected IScannerExtensionConfiguration getScannerExtensionConfiguration()
	{
		//TODO: auto suggestion description box?
		
		return super.getScannerExtensionConfiguration();
	}
	
	@Override
	protected ParserLanguage getParserLanguage()
	{
		//TODO: auto suggestion description box?
		
		return super.getParserLanguage();
	}
	
	@Override 
	protected ICParserExtensionConfiguration getParserExtensionConfiguration()
	{
		//TODO: auto suggestion description box?
		
		return super.getParserExtensionConfiguration();
	}

	@Override
	public String getId() {
		return "eclipse.plugin.gpuv.language.opencl";
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		if (ICLanguageKeywords.class.equals(adapter))
			return this;
		return super.getAdapter(adapter);
	}
}