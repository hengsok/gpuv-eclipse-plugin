package eclipse.plugin.gpuv.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import eclipse.plugin.gpuv.ActiveElementLocator;

public class RunAction implements IWorkbenchWindowActionDelegate {
	/*
	 * Once Run analysis button is pressed, it tries to open the 
	 * target OpenCL file on the editor view, and runs analysis. 
	 */
	@Override
	public void run(IAction action) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IFile file = new ActiveElementLocator().getSelectedFile();
		// Open the selected editor (if selected from ProjectExplorer)
		if (file != null) {
			IEditorDescriptor desc = PlatformUI.getWorkbench()
					.getEditorRegistry().getDefaultEditor(file.getName());
			try {
				page.openEditor(new FileEditorInput(file), desc.getId());
			} catch (PartInitException e1) {
				e1.printStackTrace();
			}
		}
		new RunAnalysis().runAnalysis();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}

}
