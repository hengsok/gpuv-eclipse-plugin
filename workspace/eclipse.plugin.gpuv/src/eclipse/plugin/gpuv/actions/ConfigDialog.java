package eclipse.plugin.gpuv.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import eclipse.plugin.gpuv.XMLKeywordsManager;
import eclipse.plugin.gpuv.builder.GPUVBuildAction;

public class ConfigDialog extends Dialog {

	private Map<String, String> selectedArgs;
	private Map<String, String> generalTabArgs;
	private Map<String, Button> argCheckboxButtons;

	public ConfigDialog(Shell parentShell) throws IOException {
		super(parentShell);
		// Read in list of arguments
		generalTabArgs = XMLKeywordsManager.getGeneralOptions();
		Map<String, String> appliedOptions = XMLKeywordsManager.getAppliedOptions(); 
		generalTabArgs.putAll(appliedOptions);

		// Select previously used options by default.
		this.selectedArgs = new HashMap<String, String>();
		selectedArgs.putAll(appliedOptions);
	}

	/**
	 * When Apply and Run Analysis button is pressed, invoke this method.
	 */
	protected void okPressed() {
		// store the arguments that're been selected for recently used list
		// later
		XMLKeywordsManager.applyOptions(selectedArgs);
		runAnalysis();
	}
	
	protected void runAnalysis(){
		//Once ok button is pressed, build project (Run analysis)
		GPUVBuildAction gpuvBuildAct = new GPUVBuildAction();
		
		if(gpuvBuildAct.isEditorReady()){
			close();
			gpuvBuildAct.executeBuild();
		}
		else{
			//Alert the user if no OpenCL file is currently opened
			MessageBox dialog = createMessageBox("Warning", 
					"Please open one OpenCL file first before attempting to run analysis.");
			dialog.open();
			close();
		}
	}

	private MessageBox createMessageBox (String title, String message) {
		MessageBox dialog = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText(title);
		dialog.setMessage(message);
		return dialog;
	}
	
	
	protected Control createDialogArea(Composite parent) {
		final Shell currShell = this.getShell();
		// Disabling ESC and CR for config box.
		// The keys are used only by auto-suggestion text field.
		currShell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				} else if (e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});
		// Create Tab Folder
		final TabFolder settings = new TabFolder(parent, SWT.NULL);
		// Two Tab Options: general and advanced
		TabItem generalSetting = new TabItem(settings, SWT.NULL);
		TabItem advancedSetting = new TabItem(settings, SWT.NULL);
		// Set Folder Size
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, false,
				false);
		settings.setLayoutData(gridData);
		// Set Option title
		generalSetting.setText("General");
		advancedSetting.setText("Advanced");
		// Container for settings
		final Composite container_general = new Composite(settings, SWT.NONE);
		final Composite container_advanced = new Composite(settings, SWT.NONE);
		// Set general container layout
		GridLayout gridlayoutContainer = new GridLayout();
		gridlayoutContainer.numColumns = 3;
		container_general.setLayout(gridlayoutContainer);
		container_advanced.setLayout(gridlayoutContainer);

		// Instructions text
		Label instructionText = new Label(container_general, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false,
				false, 3, 1);
		instructionText.setLayoutData(gridData);
		instructionText
				.setText(" Select the arguments to be passed to GPUVerify ");

		// Composite to hold check box
		Composite argCheckboxComposite = new Composite(container_general,
				SWT.NONE);
		gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 1,
				1);
		gridData.horizontalIndent = 20;
		argCheckboxComposite.setLayoutData(gridData);
		final GridLayout argCheckboxLayout = new GridLayout();
		argCheckboxLayout.numColumns = 3;
		argCheckboxComposite.setLayout(argCheckboxLayout);

		/*
		 * Set Text Area for auto suggestion
		 */
		final Text autoSuggest = new Text(container_advanced, SWT.BORDER
				| SWT.SEARCH | SWT.ICON_SEARCH);
		autoSuggest.setMessage("Type in to search");
		GridData autoGrid = new GridData(200, SWT.DEFAULT);
		autoGrid.verticalAlignment = GridData.BEGINNING;
		autoGrid.horizontalSpan = 2;
		autoSuggest.setLayoutData(autoGrid);

		// selected option list
		TableViewer viewer = new TableViewer(container_advanced, SWT.MULTI | SWT.CHECK
		      | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Create the column
	    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
	            SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText("Selected Options");
        column.setWidth(200);
		
		// Make lines and make header visible
		final Table selections = viewer.getTable();
		GridData selectionGrid = new GridData();
		selectionGrid.verticalSpan = 3;
		selectionGrid.widthHint = 200;
		selectionGrid.heightHint = 160;
		selections.setLayoutData(selectionGrid);
		selections.setHeaderVisible(true);
		
		
		
		
		
		// number of items appearing on the suggestion list
		final int restriction = 1000;

		final Table table = new Table(container_advanced, SWT.SINGLE
				| SWT.BORDER | SWT.CHECK | SWT.V_SCROLL);
		GridData tableGrid = new GridData(200, SWT.DEFAULT);
		tableGrid.widthHint = 200;
		tableGrid.heightHint = 90;
		tableGrid.horizontalSpan = 2;
		table.setLayoutData(tableGrid);
		// new TableItem(table,
		// SWT.NONE).setText("\n Use arrows to navigate \n and press enter to select\n");

		// Button for clearing selected option list
		final Button clearButton = new Button(container_advanced, SWT.PUSH);
		GridData buttonGrid = new GridData();
		buttonGrid.verticalAlignment = GridData.END;
		buttonGrid.horizontalAlignment = GridData.FILL;
		clearButton.setLayoutData(buttonGrid);
		clearButton.setText("Clear");

		// remove all options when clicked
		clearButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Clear checked items from selections
				selectedArgs.clear();
				refreshSelections(selections);
			}
		});

		// Button for removing selected options from selected option list
		final Button removeButton = new Button(container_advanced, SWT.PUSH);
		removeButton.setLayoutData(buttonGrid);
		removeButton.setText("Remove");

		// remove selected options when button clicked
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Clear checked items from selections
				TableItem ti[] = selections.getItems();
				for (int i = ti.length - 1; i >= 0; i--) {
					if (ti[i].getChecked()) {
						selectedArgs.remove(ti[i].getText());
					}
				}
				refreshSelections(selections);
			}
		});

		// refreshing when switching between tabs
		settings.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				refreshSelections(selections);
			}
		});

		// Keyboard actions
		autoSuggest.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event event) {
				switch (event.keyCode) {
				case SWT.ARROW_DOWN:
					int index = (table.getSelectionIndex() + 1)
							% table.getItemCount();
					table.setSelection(index);
					event.doit = false;
					break;
				case SWT.ARROW_UP:
					index = table.getSelectionIndex() - 1;
					if (index < 0)
						index = table.getItemCount() - 1;
					table.setSelection(index);
					event.doit = false;
					break;
				case SWT.CR: // Carriage Return
					if (table.getSelectionIndex() != -1) {
						optionSelectAction(table.getSelection()[0], autoSuggest, selections);
					}
					break;
				case SWT.ESC:
					table.removeAll();
					break;
				}
			}
		});
		autoSuggest.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				String string = autoSuggest.getText();
				if (string.length() == 0) {
					table.removeAll();
				} else {
					Set<String> resultSet = new HashSet<String>(
							XMLKeywordsManager.searchPrefix(string,
									restriction,
									XMLKeywordsManager.OPTION_SEARCH));

					int numOfItems = resultSet.size();
					// max. 8 items displayed, rest hidden
					int maxLength = autoSuggest.getSize().x;

					// displays only when there is an item
					if (numOfItems <= 0) {
						table.removeAll();

					} else {
						table.removeAll();
						// add items to the table

						Iterator<String> it = resultSet.iterator();
						while (it.hasNext()) {
							String keyword = it.next();
							maxLength = Math.max(maxLength,
									new GC(currShell).textExtent(keyword).x);
							TableItem ti = null;
							if (string.equalsIgnoreCase(keyword)) {
								ti = new TableItem(table, SWT.NONE, 0);
							} else {
								ti = new TableItem(table, SWT.NONE);
							}
							ti.setText(keyword);
							// if in the selections, make it checked.
							// Grayed if it can be selected multiple times
							ti.setGrayed(XMLKeywordsManager.isMultiple(keyword));
							ti.setChecked(selectedArgs.containsValue(keyword));
						}
						// can press enter to select the first match
						table.setSelection(0);
					}
				}
			}
		});

		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.CHECK) {
					optionSelectAction((TableItem) event.item, autoSuggest, selections);
				}
			}
		});

		table.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event event) {
				optionSelectAction((TableItem) event.item, autoSuggest, selections);
			}
		});
		
		
		selections.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event event) {
				String prevOption = ((TableItem) event.item).getText();
				String baseOption = selectedArgs.get(prevOption);
				if(XMLKeywordsManager.takesInput(baseOption)){
					createArgumentDialog(prevOption, baseOption, autoSuggest, selections);
				} else {
					selectedArgs.remove(selections.getSelection()[0].getText());
					refreshSelections(selections);
					// refresh the suggestion 
					autoSuggest.setText(autoSuggest.getText());
					autoSuggest.setSelection(autoSuggest.getCharCount());
				}
			}
		});	
		
		// when ESC pressed, suggestions emptied
		selections.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.DEL) {
					selectedArgs.remove(selections.getSelection()[0].getText());
					refreshSelections(selections);
					// refresh the suggestion 
					autoSuggest.setText(autoSuggest.getText());
					autoSuggest.setSelection(autoSuggest.getCharCount());
				}
			}
		});
		
		// when ESC pressed, suggestions emptied
		table.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.ESC) {
					table.removeAll();
				}
			}
		});

		// populate checkboxes
		createArgCheckboxes(argCheckboxComposite);

		generalSetting.setControl(container_general);
		advancedSetting.setControl(container_advanced);

		refreshSelections(selections);
		
		initContent();

		return parent;
	}

	// provides an action when an option is selected
	private void optionSelectAction(final TableItem item, final Text autoSuggest, final Table selections) {
		final String baseOption = item.getText();

		if (XMLKeywordsManager.takesInput(baseOption)) {
			if (isSelectedNotMultiple(baseOption)) {
				// Already selected.
				item.setChecked(true);
				MessageBox dialog = createMessageBox("Warning", 
						"Cannot select this option multiple times!");
				dialog.open();
			} else {
				createArgumentDialog(baseOption, autoSuggest, selections);
			}
			// for options not taking arguments
		} else if (selectedArgs.containsValue(baseOption)) {
			item.setChecked(false);
			selectedArgs.remove(baseOption);
			refreshSelections(selections);
		} else {
			item.setChecked(true);
			selectedArgs.put(baseOption, baseOption);
			refreshSelections(selections);
		}
	}

	/*
	 * Creates an input dialog for option arguments.
	 * There can be String input (one textfield) or integer (three)
	 */
	private void createArgumentDialog(final String prevOption, final String baseOption, final Text autoSuggest, final Table selections) {
		// invoke argument input dialog
		final Shell dialog = new Shell(this.getShell(), SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		dialog.setText("Argument input");
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout(formLayout);

		dialog.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				// keep the suggestion open
				autoSuggest.setText(autoSuggest.getText());
				autoSuggest.setSelection(autoSuggest.getCharCount());
			}
		});
		
		final String argType = XMLKeywordsManager.getArgType(baseOption);
		final int argNum = XMLKeywordsManager.getArgNum(baseOption);

		Label label = new Label(dialog, SWT.NONE);
		if (argType.equals("String")) {
			label.setText("Type in String:");
		} else if (argType.equals("Integer")) {
			label.setText("Type in integer:");
		}
		FormData data = new FormData();
		label.setLayoutData(data);

		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("Cancel");
		data = new FormData();
		data.width = 100;
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		cancel.setLayoutData(data);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialog.close();
			}
		});

		// add appropriate number of input fields
		// according to the number of arguments
		final Text[] inputFields = new Text[argNum];
		for (int i = 0; i < argNum; i++) {
			inputFields[i] = new Text(dialog, SWT.BORDER);
			data = new FormData();
			data.width = 200 / argNum;
			if (i <= 0) {
				data.left = new FormAttachment(label, 0, SWT.DEFAULT);
			} else {
				data.left = new FormAttachment(inputFields[i - 1], 0,
						SWT.DEFAULT);
			}
			data.bottom = new FormAttachment(cancel, 0, SWT.DEFAULT);
			inputFields[i].setLayoutData(data);
		}
		inputFields[0].setFocus();

		Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("OK");
		data = new FormData();
		data.width = 100;
		data.right = new FormAttachment(cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment(100, 0);
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// parse arguments and add to
				// selectedArgs
				String resultOption = null;
				if (argType.equals("String")) {
					String arg = inputFields[0].getText();
					if(arg.length()==0){
						MessageBox dialog = createMessageBox("Warning", 
								"Please specify the argument!");
						dialog.open();								
						resultOption = null;
					} else {
						resultOption = baseOption + "\""
								+ arg + "\"";
					}
				} else if (argType.equals("Integer")) {
					resultOption = baseOption;
					try {
						for (int j = 0; j < argNum; j++) {
							int inputInt = 0;
							String inputText = inputFields[j].getText();
							if (!inputText.isEmpty()) {
								inputInt = Integer.parseInt(inputText);
							}
							resultOption = resultOption.replace(
									(char) ('X' + j) + "",
									(inputInt + ""));
						}
					} catch (NumberFormatException nfe) {
						MessageBox dialog = createMessageBox("Warning", 
								"Arguments must be integers!");
						dialog.open();								
						resultOption = null;
					}
				}
				// Okay to proceed
				if (resultOption != null) { 
					if(prevOption != null){
						// if modifying option, then remove prev one.
						selectedArgs.remove(prevOption);
					}
					selectedArgs.put(resultOption, baseOption);
					refreshSelections(selections);
					dialog.close();
				}
			}
		});
		dialog.setDefaultButton(ok);
		
		if(prevOption != null){
			// Modification prompted. 
			// provide button to remove the option.
			Button remove = new Button(dialog, SWT.PUSH);
			remove.setText("Remove");
			data = new FormData();
			data.width = 100;
			data.right = new FormAttachment(ok, 0, SWT.DEFAULT);
			data.bottom = new FormAttachment(100, 0);
			remove.setLayoutData(data);
			remove.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					selectedArgs.remove(prevOption);
					refreshSelections(selections);
					dialog.close();
				}
			});
		}
		dialog.pack();
		dialog.open();		
	}
	
	/*
	 * Overloading of createArgumentDialog() without 'prevOption' parameter
	 */
	private void createArgumentDialog(final String baseOption, final Text autoSuggest, final Table selections) {
		createArgumentDialog(null, baseOption, autoSuggest, selections);
	}
	

	// refresh selection table and checkboxes
	private void refreshSelections(Table selections) {
		selections.removeAll(); // reset
		for (String arg : selectedArgs.keySet()) {
			new TableItem(selections, SWT.NONE).setText(arg);
		}
		// only check the buttons that are in selectedArgs
		for (String arg : argCheckboxButtons.keySet()) {
			Button button = argCheckboxButtons.get(arg);
			button.setSelection(selectedArgs.containsKey(arg));

			String baseOption = generalTabArgs.get(arg);
			if (baseOption != null) {
				// set grayed only if the same option with different
				// arguments is already selected.
				if (isSelectedNotMultiple(baseOption)
						&& !selectedArgs.containsKey(arg)) {
					button.setGrayed(true);
					button.setSelection(true);
				}
			}
		}
	}

	protected Control createButtonBar(final Composite parent) {
		final Composite btnBar = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		btnBar.setLayout(layout);
		btnBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		btnBar.setFont(parent.getFont());
		
		// Initialise default buttons
		createButton(btnBar, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		createButton(btnBar, IDialogConstants.OK_ID, " Apply and analyse ", true);
		createButton(btnBar, IDialogConstants.CLIENT_ID, "Apply", false);

		return btnBar;
	}
	
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if (IDialogConstants.CLIENT_ID == buttonId) {
			//"Apply" button
			XMLKeywordsManager.applyOptions(selectedArgs);
			close();
		}
	}

	private void createArgCheckboxes(Composite parent) {
		argCheckboxButtons = new HashMap<String, Button>();
		for (final String option : generalTabArgs.keySet()) {
			// baseOption is the option without arguments specified
			final String baseOption = generalTabArgs.get(option);
			final Button button = new Button(parent, SWT.CHECK);
			button.setText(option);
			argCheckboxButtons.put(option, button);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (button.getSelection()) {
						/*
						 * check if the selected button is already in
						 * selectedArgs, and if it is, prevent.
						 */
						if (isSelectedNotMultiple(baseOption)) {
							// conflict!
							MessageBox dialog = createMessageBox("Warning", 
									"Cannot select this option multiple times!");
							dialog.open();
							button.setGrayed(true);
						} else {
							selectedArgs.put(option, baseOption);
							button.setGrayed(false);
						}
					} else {
						button.setSelection(button.getGrayed());
						selectedArgs.remove(option);
					}
				}

			});
		}
	}

	private boolean isSelectedNotMultiple(String baseOption) {
		return !XMLKeywordsManager.isMultiple(baseOption)
				&& selectedArgs.containsValue(baseOption);
	}

	private void initContent() {
		// TODO
		// Might want to set selection only for certain often used checkboxes
		// here
		// for (String arg : argSet){
		// Button button = (Button) argCheckboxButtons.get(arg);
		// button.setSelection(selectedArgs.contains(arg));
		// }
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("GPUVerify Configurations");

	}

//	protected void initializeBounds() {
//		super.initializeBounds();
//		this.getButton(IDialogConstants.OK_ID).setText("Apply and analyse");
//	}

	public Set<String> getSelectedArgs() {
		return selectedArgs.keySet();
	}

}
