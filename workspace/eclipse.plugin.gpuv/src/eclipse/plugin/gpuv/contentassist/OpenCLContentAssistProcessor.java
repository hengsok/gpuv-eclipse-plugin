package eclipse.plugin.gpuv.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;

import eclipse.plugin.gpuv.XMLKeywordsManager;

public class OpenCLContentAssistProcessor implements IContentAssistProcessor {

	private final static String[] STYLETAGS = new String[] { "b", "i", "code",
			"strong" };
	private final static String[] STYLELABELS = new String[] { "bold",
			"italic", "code", "strong" };

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int documentOffset) {
		// Retrieve current document
		IDocument doc = viewer.getDocument();

		// Retrieve current selection range
		Point selectedRange = viewer.getSelectedRange();

		List<CompletionProposal> propList = new ArrayList<CompletionProposal>();

		if (selectedRange.y > 0) {
			try {
				// Retrieve selected text
				String text = doc.get(selectedRange.x, selectedRange.y);

				// Compute completion proposals
				computeStyleProposals(text, selectedRange, propList);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else {
			// Retrieve qualifier
			String qualifier = getQualifier(doc, documentOffset);

			// Compute completion proposals
			computeStructureProposals(qualifier, documentOffset, propList);
		}
		// Create completion proposal array
		ICompletionProposal[] proposals = new ICompletionProposal[propList
				.size()];

		// and fill with list elements
		propList.toArray(proposals);

		// Return the proposals
		return proposals;
	}

	private void computeStyleProposals(String selectedText,
			Point selectedRange, List<CompletionProposal> propList) {
		// Loop through all styles
		for (int i = 0; i < STYLETAGS.length; i++) {
			String tag = STYLETAGS[i];

			// Compute replacement text
			String replacement = "<" + tag + ">" + selectedText + "</" + tag
					+ ">";

			// Derive cursor position
			int cursor = tag.length() + 2;

			// Compute a suitable context information
			IContextInformation contextInfo = new ContextInformation(null,
					STYLELABELS[i] + " Style");

			// Construct proposal
			CompletionProposal proposal = new CompletionProposal(replacement,
					selectedRange.x, selectedRange.y, cursor, null,
					STYLELABELS[i], contextInfo, replacement);

			// and add to result list
			propList.add(proposal);
		}

	}

	/*
	 * Read in keywords and find suggestions
	 */
	private void computeStructureProposals(String qualifier,
			int documentOffset, List<CompletionProposal> propList) {
		int qlen = qualifier.length();
		List<String> prefixes = XMLKeywordsManager.searchPrefix(qualifier,
				1000, XMLKeywordsManager.KEYWORD_SEARCH);

		// Loop through all proposals
		for (String arg : prefixes) {
			// Construct proposal
			CompletionProposal proposal = new CompletionProposal(arg,
					documentOffset - qlen, qlen, arg.length());
			// and add to result list
			propList.add(proposal);
		}
	}

	/*
	 * Obtain the prefix string from the location of the cursor
	 */
	private String getQualifier(IDocument doc, int documentOffset) {
		StringBuffer buf = new StringBuffer();
		int startOffset;
		try {
			// don't go over a line
			startOffset = doc
					.getLineOffset(doc.getLineOfOffset(documentOffset));
			while (startOffset < documentOffset) {
				// read in a complete word, and return.
				char c = doc.getChar(--documentOffset);
				if (Character.isWhitespace(c)) {
					break;
				}
				// Collect each character
				buf.append(c);
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		return buf.reverse().toString();

	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * define characters which invokes auto-popup of suggestion box
	 */
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '_' };
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return new char[] { '_' };
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

}
