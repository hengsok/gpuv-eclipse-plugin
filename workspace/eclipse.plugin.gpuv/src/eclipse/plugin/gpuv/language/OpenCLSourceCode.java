package eclipse.plugin.gpuv.language;

import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.dom.parser.IScannerExtensionConfiguration;
import org.eclipse.cdt.core.model.ICLanguageKeywords;
import org.eclipse.cdt.core.parser.util.CharArrayIntMap;

import eclipse.plugin.gpuv.XMLKeywordsManager;

public class OpenCLSourceCode extends GCCLanguage implements ICLanguageKeywords {

	@Override
	protected IScannerExtensionConfiguration getScannerExtensionConfiguration() {
		IScannerExtensionConfiguration sec = super
				.getScannerExtensionConfiguration();
		CharArrayIntMap ak = sec.getAdditionalKeywords();
		XMLKeywordsManager.addKeywordsOpencl(ak);
		
		return sec;
	}
}