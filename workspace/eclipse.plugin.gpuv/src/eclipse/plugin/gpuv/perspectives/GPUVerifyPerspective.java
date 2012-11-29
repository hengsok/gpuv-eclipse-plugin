package eclipse.plugin.gpuv.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class GPUVerifyPerspective implements IPerspectiveFactory {


    private final static String ID_PROJECT_WIZARD = "eclipse.plugin.gpuv.wizard.newProjectWizard";
    private final static String ID_LATEX_FILE_WIZARD = "eclipse.plugin.gpuv.wizards.NewFileWizard";

    public void createInitialLayout(IPageLayout layout) {
  

        //Add project and Latex file creation wizards to menu
        layout.addNewWizardShortcut(ID_PROJECT_WIZARD);
        layout.addNewWizardShortcut(ID_LATEX_FILE_WIZARD);
    }


}
