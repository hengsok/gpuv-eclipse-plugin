package eclipse.plugin.gpuv.contentassist;

import org.eclipse.cdt.internal.ui.editor.CEditor;

public class OpenCLEditor extends CEditor {

	private ColorManager colorManager;

	public OpenCLEditor() {
		super();
		//colorManager = new ColorManager();
        //setDocumentProvider(new OpenCLDocumentProvider());
        //setSourceViewerConfiguration(new OpenCLConfiguration(colorManager));
	}
	public void dispose() {
		//colorManager.dispose();
		super.dispose();
	}

}
