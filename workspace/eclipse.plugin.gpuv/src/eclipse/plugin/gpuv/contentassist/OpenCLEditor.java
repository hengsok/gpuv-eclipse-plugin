package eclipse.plugin.gpuv.contentassist;

import org.eclipse.ui.editors.text.TextEditor;

public class OpenCLEditor extends TextEditor {

	private ColorManager colorManager;

	public OpenCLEditor() {
		super();
		colorManager = new ColorManager();
		/**/
        setDocumentProvider(new OpenCLDocumentProvider());
        setSourceViewerConfiguration(new OpenCLConfiguration(colorManager));
        /**/
		/*
		setSourceViewerConfiguration(new CSourceViewerConfiguration(CUIPlugin.getDefault().getTextTools().getColorManager(),
				CUIPlugin.getDefault().getCombinedPreferenceStore(),
                null, ICPartitions.C_PARTITIONING));
		setDocumentProvider(CUIPlugin.getDefault().getDocumentProvider());
		*/
		
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
