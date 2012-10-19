package eclipse.plugin.gpuv.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;

import org.eclipse.swt.layout.GridData; 
import org.eclipse.swt.layout.GridLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.swt.widgets.*;

public class ConfigurationDialog extends Dialog {
	
	private Set<String> selectedArgs;
	private Set <String> argList;
	private Map<String, Button> argCheckboxButtons;
	
	public ConfigurationDialog(Shell parentShell) throws IOException {
		super(parentShell);
		
		// Read in list of arguments
		ConfigurationArgumentList configArgList = new ConfigurationArgumentList();
		argList = configArgList.getArgList();
		
		this.selectedArgs = new HashSet<String>();
		for (String arg : argList)
			this.selectedArgs.add(arg);
	}
	

	protected void okPressed(){
		System.out.println("HAHA");
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginLeft = 20;
		gridLayout.marginRight = 20;
		gridLayout.marginTop = 20;

		container.setLayout(gridLayout);
		
		//Instructions text
		final Label instructionText = new Label(container, SWT.NONE);
		instructionText.setLayoutData(new GridData(GridData.BEGINNING,
				GridData.CENTER, false, false, 2, 1));
		instructionText.setText("Select the arguments to be passed to GPUVerify");
		

		//Composite to hold check box
		final Composite argCheckboxComposite = new Composite(container,
				SWT.NONE);
		final GridData gridDataSetting = new GridData(GridData.FILL,
				GridData.FILL, false, false, 2, 1);
		gridDataSetting.horizontalIndent = 20;
		argCheckboxComposite.setLayoutData(gridDataSetting);
		final GridLayout argCheckboxLayout = new GridLayout();
		argCheckboxLayout.numColumns = 2;
		argCheckboxComposite.setLayout(argCheckboxLayout);
		
		createArgCheckboxes(argCheckboxComposite);
		initContent();

		return container;
	}

	
	private void createArgCheckboxes(Composite parent) {
		argCheckboxButtons = new HashMap<String, Button>();
		for (String arg : argList){
			final String eachArg = arg;
			
			final Button button = new Button(parent, SWT.CHECK);
			button.setText(eachArg);
			argCheckboxButtons.put(eachArg, button);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (button.getSelection()){
						selectedArgs.add(eachArg);
					}
					else{
						selectedArgs.remove(eachArg);
						System.out.println("No");
					}
				}
			});
		}
	}
	
	private void initContent() {
		for (String arg : argList){
			Button button = (Button) argCheckboxButtons.get(arg);
			button.setSelection(selectedArgs.contains(arg));
		}
	}
	
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("GPUVerify Configurations");
	
	}
	

	protected void initializeBounds(){
		super.initializeBounds();
		this.getButton(IDialogConstants.OK_ID).setText("Run");
	}

	
	public Set<String> getSelectedArgs() {
		return selectedArgs;
	}
	
}

