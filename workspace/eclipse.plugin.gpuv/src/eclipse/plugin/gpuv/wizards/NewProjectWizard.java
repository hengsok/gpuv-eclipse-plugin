package eclipse.plugin.gpuv.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import eclipse.plugin.gpuv.CustomProjectSupport;

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
		// TODO Auto-generated method stub
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

		CustomProjectSupport.createProject(name, location, monitor);

		BasicNewProjectResourceWizard.updatePerspective(_configurationElement);

	}

}
