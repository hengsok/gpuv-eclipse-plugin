package eclipse.plugin.gpuv.contentassist;

import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

import eclipse.plugin.gpuv.XMLKeywordsManager;

public class OpenCLScanner extends RuleBasedScanner {

	public OpenCLScanner(ColorManager manager) {
		WordRule rule = new WordRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		});

		IToken keyword = new Token(new TextAttribute(
				manager.getColor(IOpenCLColorConstants.TAG), null, 1));

		List<String> keywords = XMLKeywordsManager.getKeywords();
		for (String str : keywords) {
			rule.addWord(str, keyword);
		}

		IRule[] rules = new IRule[2];
		// Add generic whitespace rule.
		rules[0] = new WhitespaceRule(new OpenCLWhitespaceDetector());
		// Add keywords
		rules[1] = rule;

		setRules(rules);
	}
}
