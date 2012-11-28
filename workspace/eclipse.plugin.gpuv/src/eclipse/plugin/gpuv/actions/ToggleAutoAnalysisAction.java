package eclipse.plugin.gpuv.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.internal.ide.actions.ToggleAutoBuildAction;

public class ToggleAutoAnalysisAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	
	@Override
	public void run(IAction action) {
		new ToggleAutoBuildAction(window).run();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
