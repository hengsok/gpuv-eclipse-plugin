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
import org.eclipse.swt.widgets.*;

public class ConfigDialog extends Dialog {
	
	private Set<String> selectedArgs;
	private Set <String> argList;
	private Map<String, Button> argCheckboxButtons;
	private Composite comp;
	private boolean isAdvanced = false;
	
	public ConfigDialog(Shell parentShell) throws IOException {
		super(parentShell);
		
		// Read in list of arguments
		ConfigArgumentList configArgList = new ConfigArgumentList();
		argList = configArgList.getArgList();
		
		this.selectedArgs = new HashSet<String>();
		
	}
	

	protected void okPressed(){
		//store the arguments that're been selected for recently used list later
		ConfigRecentlyUsedArgs configRecentUsed = new ConfigRecentlyUsedArgs();
		configRecentUsed.storeRecentArgs(selectedArgs);

		//run the arguments with shell
		ShellCommand t = new ShellCommand(); 
		try {
			t.runCommand(getSelectedArgs());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected Control createDialogArea(Composite parent) {
		//Store composite for use by createAdvancedContent()
		this.comp = parent;
		
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginLeft = 20;
		gridLayout.marginRight = 400;
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
	
		//Set container layout
		Composite container2 = (Composite) super.createDialogArea(comp);
		GridLayout gridLayout2 = new GridLayout();
		//gridLayout2.numColumns = 2;
		//gridLayout2.marginLeft = 200;
		//gridLayout2.marginRight = 200;
		gridLayout2.marginTop = 20;
		container2.setLayout(gridLayout2);
		//Set Plain Text
		Label label = new Label(container2, SWT.BORDER);
		label.setText("Search Box");
		//Set Text Area
		Text textDemo = new Text(container2, SWT.BORDER);
		textDemo.setText("Text Area Demo");
	
		final List list = new List(parent, SWT.MULTI |  SWT.V_SCROLL);
	
		list.add("Mercury");
		list.add("Venus");
		list.add("Earth");
		list.add("JavaSoft");
		list.add("Earth");
		list.add("JavaSoft");
		list.add("Earth");
		list.add("JavaSoft");
	

		 
		 //populate checkboxes
		createArgCheckboxes(argCheckboxComposite);
		initContent();

		return container;
	}
	
	private void createAdvancedContent(){
		isAdvanced = true;
		comp.dispose();
		createDialogArea(comp);
	}

	protected Control createButtonBar(final Composite parent)
	{
	    final Composite btnBar = new Composite(parent, SWT.NONE);
	    final GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    btnBar.setLayout(layout);

	    // Add an advanced button so that more options can be shown
	    final Button advancedButton = new Button(btnBar, SWT.PUSH);
	    advancedButton.setText("Advanced");
	    advancedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (advancedButton.getSelection()){
					System.out.println("hello");
					createAdvancedContent();
				}
				else{
					System.out.println("No");
				}
			}
		});

	    final GridData advancedBtn = new GridData(SWT.LEFT, SWT.CENTER, true, true);
	    advancedBtn.grabExcessHorizontalSpace = true;
	    advancedBtn.horizontalIndent = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
	    advancedButton.setLayoutData(advancedBtn);

	    // Initialise default buttons
	    final GridData defaultBtn = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
	    defaultBtn.grabExcessVerticalSpace = false;
	    defaultBtn.grabExcessHorizontalSpace = true;
	    btnBar.setLayoutData(defaultBtn);

	    btnBar.setFont(parent.getFont());
	    
	    // add the dialog's button bar to the right
	    final Control buttonCtrl = super.createButtonBar(btnBar);
	    buttonCtrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

	    return btnBar;
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
					}
				}
			});
		}
	}
	
	private void initContent() {
		//Might want to set selection only for certain often used checkboxes here
		//		for (String arg : argList){
		//			Button button = (Button) argCheckboxButtons.get(arg);
		//			button.setSelection(selectedArgs.contains(arg));
		//		}
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

