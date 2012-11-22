package eclipse.plugin.gpuv.contentassist;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class OpenCLConfiguration extends SourceViewerConfiguration {
	private OpenCLDoubleClickStrategy doubleClickStrategy;
	private OpenCLScanner scanner;
	private ColorManager colorManager;
	
	private static final DefaultInformationControl.IInformationPresenter
	   presenter = new DefaultInformationControl.IInformationPresenter() {
	      public String updatePresentation(Display display, String infoText,
	         TextPresentation presentation, int maxWidth, int maxHeight) {
	         int start = -1;

	         // Loop over all characters of information text
	         for (int i = 0; i < infoText.length(); i++) {
	            switch (infoText.charAt(i)) {
	               case '<' :

	                  // Remember start of tag
	                  start = i;
	                  break;
	               case '>' :
	                  if (start >= 0) {

	                    // We have found a tag and create a new style range
	                    StyleRange range = 
	                       new StyleRange(start, i - start + 1, null, null, SWT.BOLD);

	                    // Add this style range to the presentation
	                    presentation.addStyleRange(range);

	                    // Reset tag start indicator
	                    start = -1;
	                  }
	                  break;
	         }
	      }

	      // Return the information text

	      return infoText;
	   }
	};

	public OpenCLConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				OpenCLPartitionScanner.OpenCL_COMMENT};
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
		   assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		   // Create content assistant processor
		   IContentAssistProcessor processor = new OpenCLContentAssistProcessor();
		   
		   // Set this processor for each supported content type
		   assistant.setContentAssistProcessor(processor, OpenCLPartitionScanner.OpenCL_COMMENT);
		   assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		   assistant.enableAutoActivation(true);
		   assistant.setAutoActivationDelay(0);
		   // Return the content assistant   
		   return assistant;
	}

	@Override
	public IInformationControlCreator getInformationControlCreator(
			ISourceViewer sourceViewer) {
		return new IInformationControlCreator() {
		      public IInformationControl createInformationControl(Shell parent) {
		         return new DefaultInformationControl(parent,presenter);
		      }
		   };
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getOpenCLScanner());
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