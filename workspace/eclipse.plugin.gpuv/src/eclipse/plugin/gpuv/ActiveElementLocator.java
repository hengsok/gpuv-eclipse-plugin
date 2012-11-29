package eclipse.plugin.gpuv;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
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
		if(this.window == null){ // dealing with threads
			this.window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		}
	}
	
	public IFile getSelectedFile() {
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structured = (IStructuredSelection) service
				.getSelection("org.eclipse.ui.navigator.ProjectExplorer");

		if(structured != null) {
			try {
				return (IFile) structured.getFirstElement();
			} catch (Exception ee){
				return null;
			}
		}
		return null;
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
		
		IFile file = getSelectedFile();
		if(file != null){
			filepathFull = file.getProject().getName() + "_" + file.getName();
		} else {
			filepathFull = null;
		}
		//TODO one active and one selected? which to choose? 
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
