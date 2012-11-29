package eclipse.plugin.gpuv.actions;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.actions.ToggleAutoBuildAction;

public class ToggleAutoAnalysisAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * Creates a new ToggleAutoAnalysisAction
	 * 
	 * @param window
	 *            The window for parenting dialogs associated with this action
	 */
	public ToggleAutoAnalysisAction() {
		setChecked(ResourcesPlugin.getWorkspace().isAutoBuilding());
		System.out.println(ResourcesPlugin.getWorkspace().isAutoBuilding());
	}
	//TODO make the menu checked depending on condition

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	public void dispose() {
		// nothing to dispose
	}

	@Override
	public void run(IAction action) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceDescription description = workspace.getDescription();
        description.setAutoBuilding(!description.isAutoBuilding());
        try {
            workspace.setDescription(description);
        } catch (CoreException e) {
            ErrorDialog.openError(window.getShell(), null, null, e.getStatus());
        }
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
