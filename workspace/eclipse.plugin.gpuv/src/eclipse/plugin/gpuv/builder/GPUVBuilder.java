package eclipse.plugin.gpuv.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import eclipse.plugin.gpuv.XMLKeywordsManager;

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
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				IEditorPart activeEditor = window.getActivePage().getActiveEditor();

				// If activeEditor is not available, return
				if (activeEditor == null)
					return;
				
				IResource res = (IResource) activeEditor.getEditorInput().getAdapter(IResource.class);
				if (res == null)
					return;
				IProject activeProject = res.getProject();
				
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

		// Call the method to look for current "in the view" file and run build on it

		invokeBuildOnCurrentFile();
	}


}
