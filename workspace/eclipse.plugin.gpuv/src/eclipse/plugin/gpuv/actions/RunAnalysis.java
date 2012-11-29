package eclipse.plugin.gpuv.actions;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import eclipse.plugin.gpuv.XMLKeywordsManager;
import eclipse.plugin.gpuv.builder.GPUVBuildAction;

public class RunAnalysis {

	/*
	 * Run GPUVerify analysis on selected file.
	 * Returns false if necessary options are not selected,
	 * and true if analysis was performed.
	 */
	public boolean runAnalysis(){
		// Check if necessary options are selected;
		// --num-groups and --local-size
		Map<String, String> appliedOptions = XMLKeywordsManager.getAppliedOptions();
		if(appliedOptions.containsValue("--num_groups=(X,Y,Z)")
				&& appliedOptions.containsValue("--local_size=(X,Y,Z)")) {
			GPUVBuildAction gpuvBuildAct = new GPUVBuildAction();
			if (gpuvBuildAct.isEditorReady()) {
				gpuvBuildAct.executeBuild();
			} else {
				// Alert the user if no OpenCL file is currently opened
				MessageBox dialog = createMessageBox ("Warning", "Please open one OpenCL file " +
						"first before attempting to run analysis.");
				dialog.open();
			}
		} else {
			// Necessary options are not selected!
			MessageBox dialog = createMessageBox ("Warning", "Please select" +
					" --num-groups and --local-size options!");
			dialog.open();
			return false;
		}
		return true;
	}

	private MessageBox createMessageBox(String title, String message) {
		MessageBox dialog = new MessageBox(new Shell(), SWT.ICON_QUESTION
				| SWT.OK);
		dialog.setText(title);
		dialog.setMessage(message);
		return dialog;
	}

}
