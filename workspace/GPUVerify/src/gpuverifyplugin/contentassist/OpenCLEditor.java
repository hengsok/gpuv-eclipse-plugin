package gpuverifyplugin.contentassist;

import org.eclipse.ui.editors.text.TextEditor;

public class OpenCLEditor extends TextEditor {

	private ColorManager colorManager;

	public OpenCLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
