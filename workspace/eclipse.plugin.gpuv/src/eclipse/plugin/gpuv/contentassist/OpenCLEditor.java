package eclipse.plugin.gpuv.contentassist;

import org.eclipse.cdt.internal.ui.editor.CEditor;
import org.eclipse.ui.editors.text.TextEditor;

public class OpenCLEditor extends CEditor {

	private ColorManager colorManager;

	public OpenCLEditor() {
		super();
		colorManager = new ColorManager();
        setDocumentProvider(new OpenCLDocumentProvider());
        
        setSourceViewerConfiguration(new OpenCLConfiguration(colorManager));
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
