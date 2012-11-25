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
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isDocumentationComment(IDocument doc, int offset, int length) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ICTokenScanner createCommentScanner(
			ITokenStoreFactory tokenStoreFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAutoEditStrategy createAutoEditStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITextDoubleClickStrategy createDoubleClickStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICompletionProposalComputer createProposalComputer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocCommentDictionary getSpellingDictionary() {
		// TODO Auto-generated method stub
		return null;
	}

}
