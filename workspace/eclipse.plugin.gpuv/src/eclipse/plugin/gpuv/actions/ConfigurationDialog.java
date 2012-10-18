package eclipse.plugin.gpuv.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;


public class ConfigurationDialog extends Dialog {
	
	protected ConfigurationDialog(Shell  parentShell) {
		super(parentShell);
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		System.out.println("createButtonsForButtonBar");
    }
	
	protected Point getInitialSize(){
        return new Point(800,600);
    }
	
	protected Control createDialogArea(Composite parent) {
		//Set dialog title
		getShell().setText("GPUVerify Configuration");
		//Set container layout
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout());
        //Set Plain Text
        Label label = new Label(container, SWT.BORDER);
        label.setText("Label Display");
        //Set Text Area
        Text textDemo = new Text(container, SWT.BORDER);
        textDemo.setText("Text Area Demo");
        //Set drop down list
        Combo combo = new Combo(container, SWT.BORDER);
        combo.add("Combo Demo 1");
        combo.add("Combo Demo 2");
        combo.add("Combo Demo 3");
        //Set Checkbox
        Button checkBox = new Button(container, SWT.CHECK);
        checkBox.setText("CheckBox Demo");
        //Set RadioButton
        new Button(container, SWT.RADIO).setText("RadioButton Demo 1");
        new Button(container, SWT.RADIO).setText("RadioButton Demo 2");
        
        return container;
    }
	
	protected void initializeBounds(){
        Composite comp = (Composite)getButtonBar();
        super.createButton(comp, IDialogConstants.OK_ID, "Run", true).addListener(OK, new Listener(){

			@Override
			public void handleEvent(Event event) {
				System.out.println("Pass terminal command here!!!");
			}});
        super.createButton(comp, IDialogConstants.CLOSE_ID, "Cancel", false);
    }
}

