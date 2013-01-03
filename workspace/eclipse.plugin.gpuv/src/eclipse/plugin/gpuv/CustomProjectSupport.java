package eclipse.plugin.gpuv;

import java.net.URI;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

public class CustomProjectSupport {
	/**
	 * For this marvelous project we need to: - create the default Eclipse
	 * project - add the custom project nature - create the folder structure
	 * 
	 * @param projectName
	 * @param location
	 * @param natureId
	 * @return
	 */
	public static IProject createProject(String projectName, URI location, IProgressMonitor monitor) {
		Assert.isNotNull(projectName);
		Assert.isTrue(projectName.trim().length() > 0);

		IProject project = createBaseProject(projectName, location, monitor);
		try {
			//addNature(project);

			String[] paths = { "src", "bin" }; //$NON-NLS-1$ //$NON-NLS-2$
			addToProjectStructure(project, paths);
		} catch (CoreException e) {
			e.printStackTrace();
			project = null;
		}

		return project;
	}

	/**
	 * 
	 * @param containerName
	 * @param fileName
	 * @param monitor
	 * @return
	 * @throws CoreException
	 */
	public static IFile getCurrentProjectFile(String containerName,
			String fileName) throws CoreException {
		// create a sample file
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName
					+ "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		return container.getFile(new Path(fileName));

	}

	public static String getCurrentProjectName() throws CoreException {
		return ResourcesPlugin.getWorkspace().getRoot().getProject().getName();
	}

	/**
	 * 
	 * @param message
	 * @throws CoreException
	 */
	private static void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "eclipse.plugin.gpuv",
				IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * Just do the basics: create a basic project.
	 * 
	 * @param location
	 * @param projectName
	 */
	private static IProject createBaseProject(String projectName, URI location, IProgressMonitor monitor) {
		// it is acceptable to use the ResourcesPlugin class
		monitor.beginTask("Creating Project", IProgressMonitor.UNKNOWN);
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		if (!newProject.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = newProject.getWorkspace()
					.newProjectDescription(newProject.getName());
			if (location != null
					&& ResourcesPlugin.getWorkspace().getRoot()
							.getLocationURI().equals(location)) {
				projectLocation = null;
			}

			desc.setLocationURI(projectLocation);
			try {
				newProject = CCorePlugin.getDefault().createCDTProject(desc, newProject, monitor);
				
				if (!newProject.isOpen()) {
					newProject.open(null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return newProject;
	}

	/**
	 * 
	 * @param folder
	 * @throws CoreException
	 */
	private static void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}

	/**
	 * Create a folder structure with a parent root, overlay, and a few child
	 * folders.
	 * 
	 * @param newProject
	 * @param paths
	 * @throws CoreException
	 */
	private static void addToProjectStructure(IProject newProject,
			String[] paths) throws CoreException {
		for (String path : paths) {
			IFolder etcFolders = newProject.getFolder(path);
			createFolder(etcFolders);
		}
	}

}