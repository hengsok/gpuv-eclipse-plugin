package eclipse.plugin.gpuv.contentassist;

import org.eclipse.jface.text.rules.*;

public class OpenCLPartitionScanner extends RuleBasedPartitionScanner {
	public final static String OpenCL_COMMENT = "__opencl_comment";
	public final static String OpenCL_TAG = "__opencl_tag";

	public OpenCLPartitionScanner() {

		IToken openCLComment = new Token(OpenCL_COMMENT);
		IToken tag = new Token(OpenCL_TAG);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("/*", "*/", openCLComment);
		// Add C comment
		rules[1] = new SingleLineRule("//", null, openCLComment);

		setPredicateRules(rules);
	}
}
