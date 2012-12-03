package eclipse.plugin.gpuv.builder;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import eclipse.plugin.gpuv.ActiveElementLocator;
import eclipse.plugin.gpuv.pythoncheck.PythonCheck;

public class GPUVBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "eclipse.plugin.gpuv.builder.GPUVBuilder";
	public static final String MARKER_TYPE = "eclipse.plugin.gpuv.builder.nature.problem";

	//	private final static Logger LOGGER = Logger.getLogger(GPUVBuilder.class
	//			.getName());

	

	protected IProject[] build(int kind, Map<String,String> args, IProgressMonitor monitor)
			throws CoreException {
		// GPUV: Requesting the builder to forget last build state
		super.forgetLastBuiltState();
		//invoke full build
		fullBuild(monitor);
		return null;
	}

	

	/**
	 * In this method, the active page that is currently in view is returned.
	 * Markers will be added for any errors appearing from running analysis(build)
	 * on this file.
	 * To implement running analysis(build) on multiple opened files or files in
	 * src dir. Modify this method and once you get hold of the files you want
	 * to run build on, pass it to addMarkersToResource()
	 */
	private void invokeBuildOnCurrentFile(){
		// Get the file that is currently "in the view(visible)" and call
		// addMarkersToResource start adding problem markers
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			public void run() {
				ActiveElementLocator ael = new ActiveElementLocator();
				IEditorPart activeEditor = ael.getActiveEditor();
				IResource res = ael.getActiveResource();
				IProject activeProject = ael.getActiveProject();

				// If activeEditor is not available, return
				if (activeEditor == null || res == null || activeProject == null)
					return;
				
				//Check so that build only current project and not all
				//opened projects
				if(activeProject.getName().equals(getProject().getName())){
					IFile fileToBuild = (IFile) activeEditor.getEditorInput().getAdapter(IFile.class);
					if (fileToBuild != null) {
						GPUVBuilderThread gpuvBuilderThread = new GPUVBuilderThread(fileToBuild); 
						Thread gpuvBThread = new Thread(gpuvBuilderThread);
						gpuvBThread.start();
					}
				}
			}
		});
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {

		if(PythonCheck.isInstalled()){
			// Call the method to look for current "in the view" file and run build on it
			invokeBuildOnCurrentFile();
		}
		else{
			GPUVDefaultConsole.printToConsole("It appears that you do not have Python installed." +
					" Please check that you have set up the path correctly in the environment " +
					"variables.");
		}
	}
}
