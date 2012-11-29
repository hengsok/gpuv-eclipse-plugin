package eclipse.plugin.gpuv.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import eclipse.plugin.gpuv.builder.GPUVBuildAction;

public class RunAction implements IWorkbenchWindowActionDelegate {
	
	@Override
	public void run(IAction action) {
		GPUVBuildAction gpuvBuildAct = new GPUVBuildAction();
		if(gpuvBuildAct.isEditorReady()){
			gpuvBuildAct.executeBuild();
		}
		else{
			//Alert the user if no OpenCL file is currently opened
			MessageBox dialog = createMessageBox("Warning", 
					"Please open one OpenCL file first before attempting to run analysis.");
			dialog.open();
		}
	}
	private MessageBox createMessageBox (String title, String message) {
			MessageBox dialog = new MessageBox(new Shell(), SWT.ICON_QUESTION | SWT.OK);
			dialog.setText(title);
			dialog.setMessage(message);
			return dialog;
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
