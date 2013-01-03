package eclipse.plugin.gpuv.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * Attempt to build the project (in GPUV terms, Run analysis). 
 *
 */

public class GPUVBuildAction {
	private IEditorPart editor;
	private IResource res;
	
	/**
	 * Initialise by getting reference of editor and resource.
	 *
	 */
	public GPUVBuildAction(){
		 editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		 if(editor != null){
			 res = (IResource) editor.getEditorInput().getAdapter(IResource.class);
		 }
	}
	
	/**
	 * Check editor state
	 * @return editor is ready or not
	 */
	public boolean isEditorReady(){
		return (editor != null);
	}
	
	/**
	 * Attempt to build the project.
	 */
	public void executeBuild(){
		    if (res == null) {
		    	return;
		    }
		        
		    IProject project = res.getProject();
		    try {
				project.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
	}	
}
