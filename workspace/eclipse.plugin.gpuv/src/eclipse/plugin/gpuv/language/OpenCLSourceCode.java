package eclipse.plugin.gpuv.language;

import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.model.ICLanguageKeywords;
import org.eclipse.cdt.core.parser.util.ArrayUtil;

public class OpenCLSourceCode extends GCCLanguage implements ICLanguageKeywords {

	@Override
	public String[] getKeywords() {
		System.out.println("Called keywords");
		return ArrayUtil.addAll(super.getKeywords(), new String[] { "hello",
				"printme" });
	}

	@Override
	public String[] getBuiltinTypes() {
		System.out.println("Called keywords");
		return ArrayUtil.addAll(super.getBuiltinTypes(), new String[] { "Lambros",
				"Petrou" });
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