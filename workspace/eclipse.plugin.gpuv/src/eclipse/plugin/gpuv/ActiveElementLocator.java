package eclipse.plugin.gpuv;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/*
 * Provide methods to get currently active elements, 
 * including project and editor.
 */
public class ActiveElementLocator {
	private IWorkbenchWindow window;
	public ActiveElementLocator() {
		this.window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	/*
	 * Return the string of xml filename for applied options
	 * specific to the currently active project & .cl file
	 * Return null if in valid.
	 */
	public String getOptionsFilename(){
		String filepath = null;
		String filepathFull = null;
		
		// check if an OpenCL file is selected from ProjectExplorer
		// if so, use the selected file's path.
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structured = (IStructuredSelection) service
				.getSelection("org.eclipse.ui.navigator.ProjectExplorer");
		if(structured != null) {
			try {
				IFile file = (IFile) structured.getFirstElement();
				IPath path = file.getLocation();
				filepathFull = file.getProject().getName() + "_" + file.getName();
			} catch (Exception ee){
				filepathFull = null;
			}
		}
		
		// Filename of the active Editor
		if(filepathFull == null){
			IEditorPart activeEditor = getActiveEditor();
			IResource res = getActiveResource();
			IProject project = getActiveProject();
			// If activeEditor is not available, return
			if (activeEditor == null || res == null || project == null) {
				return null; 
			}
			filepathFull = project.getName() + "_" + activeEditor.getTitle();
			
		}
			
		// file extension must be .cl to store options for it.
		if (filepathFull.endsWith(".cl")) {
			filepath = filepathFull.split(".cl")[0]; //name without extension
		} else {
			return null;
		}
		
		return filepath + "_options.xml";
	}
	
	public IProject getActiveProject() {
		try{
			return getActiveResource().getProject();
		} catch (NullPointerException e){
			return null;
		}
	}
	public IEditorPart getActiveEditor() {
		try{
			return window.getActivePage().getActiveEditor();
		} catch (NullPointerException e){
			return null;
		}
	}
	public IResource getActiveResource() {
		try{
			return (IResource) getActiveEditor().getEditorInput().getAdapter(IResource.class);
		} catch (NullPointerException e){
			return null;
		}
	}
}
