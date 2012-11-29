package eclipse.plugin.gpuv.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import eclipse.plugin.gpuv.CustomProjectSupport;
import eclipse.plugin.gpuv.builder.GPUVBuilder;
import eclipse.plugin.gpuv.builder.GPUVNature;

public class NewProjectWizard extends Wizard implements INewWizard,
		IExecutableExtension, IRunnableWithProgress {

	private static final String PAGE_NAME = "New GPUVerify Project Wizard";
	private static final String WIZARD_NAME = "New GPUVerify Project Wizard";

	private WizardNewProjectCreationPage _pageOne;
	private IConfigurationElement _configurationElement;

	public NewProjectWizard() {
		super();
		setWindowTitle(WIZARD_NAME);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
	}

	@Override
	public boolean performFinish() {
		try {
			getContainer().run(false, true, this);
		} catch (InvocationTargetException e) {

		} catch (InterruptedException e) {

		}
		return true;
	}

	@Override
	public void addPages() {
		super.addPages();

		_pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
		_pageOne.setTitle("GPUVerify Project");
		_pageOne.setDescription("Please enter a project name.");

		addPage(_pageOne);
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		_configurationElement = config;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		String name = _pageOne.getProjectName();
		URI location = null;
		if (!_pageOne.useDefaults()) {
			location = _pageOne.getLocationURI();
		} // else location == null

		IProject project = CustomProjectSupport.createProject(name, location,
				monitor);

		// Add GPUV nature to the .project description file
		try {
			addNatureToProject(project, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}

		// Add GPUV builder to new project. This will add to project description
		// file.
		try {
			addBuilderToProject(project, GPUVBuilder.BUILDER_ID);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		// Add default main.cl File
		addFile(name);

		BasicNewProjectResourceWizard.updatePerspective(_configurationElement);

	}
	
	public void addFile(String name)
	{
		final String containerName = "/"+name+"/src";
		final String fileName = "main.cl";
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					monitor.beginTask("Creating " + fileName, 2);
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
							.getRoot();
					IResource resource = root
							.findMember(new Path(containerName));
					IContainer container = (IContainer) resource;
					final IFile file = container.getFile(new Path(fileName));
					try {
						InputStream stream = new ByteArrayInputStream("//main.cl\r\n//Copyright Heng Sok (hs4110), Inhyeok Kim (ik610), Yuxiang Zhou (yz4009), Myung Lee (msl09), Hin Cheng (hfc10)".getBytes());
						if (file.exists()) {
							file.setContents(stream, true, true, monitor);
						} else {
							file.create(stream, true, monitor);
						}
						stream.close();
					} catch (IOException e) {
					}
					monitor.worked(1);
					monitor.setTaskName("Opening file for editing...");
					getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
							try {
								IDE.openEditor(page, file, true);
							} catch (PartInitException e) {
							}
						}
					});
					monitor.worked(1);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error",
					realException.getMessage());
		}
	}

	/**
	 * Add a project nature to project description file.
	 * 
	 * @param project
	 *            This is the current project
	 * @throws CoreException
	 */
	public static void addNatureToProject(IProject project,
			IProgressMonitor monitor) throws CoreException {

		IProjectDescription desc = project.getDescription();
		String[] natures = desc.getNatureIds();
		for (int i = 0; i < natures.length; i++) {
			// Check whether nature id already exist
			if (GPUVNature.NATURE_ID.equals(natures[i])) {
				return;
			}
		}

		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 1, natures.length);
		newNatures[0] = GPUVNature.NATURE_ID;
		desc.setNatureIds(newNatures);
		project.setDescription(desc, monitor);
	}

	/**
	 * Add a builder to the project.
	 * 
	 * @param id
	 *            id of the builder to add
	 * @throws CoreException
	 */
	private void addBuilderToProject(IProject project, String id)
			throws CoreException {

		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		// Check whether GPUVBuilder already exist. If it is, don't add.
		boolean isBuilderExist = false;
		for (int i = 0; i < commands.length; i++) {
			if (commands[i].getBuilderName().equals(id)) {
				isBuilderExist = true;
			}
		}

		if (!isBuilderExist) {
			ICommand command = desc.newCommand();
			command.setBuilderName(id);
			ICommand[] newCommands = new ICommand[commands.length + 1];

			System.arraycopy(commands, 0, newCommands, 1, commands.length);
			newCommands[0] = command;
			desc.setBuildSpec(newCommands);
			project.setDescription(desc, null);
		}
	}

}
