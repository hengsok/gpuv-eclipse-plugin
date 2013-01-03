package eclipse.plugin.gpuv;

import org.eclipse.cdt.ui.text.ICTokenScanner;
import org.eclipse.cdt.ui.text.ITokenStoreFactory;
import org.eclipse.cdt.ui.text.contentassist.ICompletionProposalComputer;
import org.eclipse.cdt.ui.text.doctools.IDocCommentDictionary;
import org.eclipse.cdt.ui.text.doctools.IDocCommentViewerConfiguration;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;

public class SinglelineDocCommentViewerConfiguration implements
		IDocCommentViewerConfiguration {

	public SinglelineDocCommentViewerConfiguration() {
	}

	@Override
	public boolean isDocumentationComment(IDocument doc, int offset, int length) {
		return false;
	}

	@Override
	public ICTokenScanner createCommentScanner(
			ITokenStoreFactory tokenStoreFactory) {
		return null;
	}

	@Override
	public IAutoEditStrategy createAutoEditStrategy() {
		return null;
	}

	@Override
	public ITextDoubleClickStrategy createDoubleClickStrategy() {
		return null;
	}

	@Override
	public ICompletionProposalComputer createProposalComputer() {
		return null;
	}

	@Override
	public IDocCommentDictionary getSpellingDictionary() {
		return null;
	}

}
