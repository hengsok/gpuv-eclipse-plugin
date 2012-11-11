package eclipse.plugin.gpuv.contentassist;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class XMLScanner extends RuleBasedScanner {

	public XMLScanner(ColorManager manager) {
		WordRule rule = new WordRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		});
		IToken procInstr = new Token(new TextAttribute(
				manager.getColor(IXMLColorConstants.PROC_INSTR)));
		IToken comment = new Token(new TextAttribute(
				manager.getColor(IXMLColorConstants.STRING)));

		IToken keyword = new Token(new TextAttribute(manager.getColor(
				IXMLColorConstants.TAG), null, 1));

		ArrayList<String> keywords = KeywordsManager.getKeywords();
		for (String str : keywords) {
			rule.addWord(str, keyword);
		}

		IRule[] rules = new IRule[4];
		// Add rule for processing instructions
		rules[0] = new SingleLineRule("<?", "?>", procInstr);
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new XMLWhitespaceDetector());
		// Add C comment
		rules[2] = new SingleLineRule("//", null, comment);
		// Add keywords
		rules[3] = rule;

		setRules(rules);
	}
}
