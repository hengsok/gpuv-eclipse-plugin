package eclipse.plugin.gpuv.contentassist;

import org.eclipse.jface.text.rules.*;

public class OpenCLPartitionScanner extends RuleBasedPartitionScanner {
	public final static String OpenCL_COMMENT = "__opencl_comment";

	public OpenCLPartitionScanner() {

		IToken openCLComment = new Token(OpenCL_COMMENT);

		IPredicateRule[] rules = new IPredicateRule[3];
		// Add C multi-lines comment
		rules[0] = new MultiLineRule("/*", "*/", openCLComment);
		// Add C single line comment
		rules[1] = new SingleLineRule("//", null, openCLComment);
		// Add C string
		rules[2] = new SingleLineRule("\"", "\"", openCLComment);

		setPredicateRules(rules);
	}
}
