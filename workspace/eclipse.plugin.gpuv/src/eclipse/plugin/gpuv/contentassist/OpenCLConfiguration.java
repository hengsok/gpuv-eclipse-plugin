package eclipse.plugin.gpuv.contentassist;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class OpenCLConfiguration extends SourceViewerConfiguration {
	private OpenCLDoubleClickStrategy doubleClickStrategy;
	private OpenCLTagScanner tagScanner;
	private OpenCLScanner scanner;
	private ColorManager colorManager;

	public OpenCLConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				OpenCLPartitionScanner.OpenCL_COMMENT, OpenCLPartitionScanner.OpenCL_TAG };
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new OpenCLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected OpenCLScanner getOpenCLScanner() {
		if (scanner == null) {
			scanner = new OpenCLScanner(colorManager);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(
					colorManager.getColor(IOpenCLColorConstants.DEFAULT))));
		}
		return scanner;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		   // Create content assistant
		   ContentAssistant assistant = new ContentAssistant();
		   
		   // Create content assistant processor
		   IContentAssistProcessor processor = new OpenCLContentAssistProcessor();
		   
		   // Set this processor for each supported content type
		   assistant.setContentAssistProcessor(processor, OpenCLPartitionScanner.OpenCL_TAG);
		   assistant.setContentAssistProcessor(processor, OpenCLPartitionScanner.OpenCL_COMMENT);
		   assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		   assistant.enableAutoActivation(true);
		   assistant.setAutoActivationDelay(0);
		   // Return the content assistant   
		   return assistant;
	}

	protected OpenCLTagScanner getOpenCLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new OpenCLTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(new Token(new TextAttribute(
					colorManager.getColor(IOpenCLColorConstants.TAG))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
				getOpenCLTagScanner());
		reconciler.setDamager(dr, OpenCLPartitionScanner.OpenCL_TAG);
		reconciler.setRepairer(dr, OpenCLPartitionScanner.OpenCL_TAG);

		dr = new DefaultDamagerRepairer(getOpenCLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColor(IOpenCLColorConstants.STRING)));
		reconciler.setDamager(ndr, OpenCLPartitionScanner.OpenCL_COMMENT);
		reconciler.setRepairer(ndr, OpenCLPartitionScanner.OpenCL_COMMENT);

		return reconciler;
	}

}