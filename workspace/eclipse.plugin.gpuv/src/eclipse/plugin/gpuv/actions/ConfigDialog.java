package eclipse.plugin.gpuv.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import eclipse.plugin.gpuv.CustomProjectSupport;
import eclipse.plugin.gpuv.XMLKeywordsManager;


public class ConfigDialog extends Dialog {

	private Map<String, String> selectedArgs;
	private Set<String> argSet; // TODO does this need to be global?
	private Map<String, Button> argCheckboxButtons;

	public ConfigDialog(Shell parentShell) throws IOException {
		super(parentShell);

		// Read in list of arguments
		ConfigRecentlyUsedArgs configRecentArgList = new ConfigRecentlyUsedArgs();
		argSet = XMLKeywordsManager.getGeneralOptions();
		argSet.addAll(configRecentArgList.getRecentArgs());

		this.selectedArgs = new HashMap<String, String>();

	}

	protected void okPressed() {
		// TODO need fix
		// store the arguments that're been selected for recently used list
		// later
		ConfigRecentlyUsedArgs configRecentUsed = new ConfigRecentlyUsedArgs();
		configRecentUsed.storeRecentArgs(selectedArgs.keySet());

		String containerName = "/Test/src";
		String fileName = "main.cl";
		try {
			System.out.println(CustomProjectSupport.getCurrentProjectFile(
					containerName, fileName).getName()
					+ " used");
		} catch (CoreException e1) {
			e1.printStackTrace();
		}

		// run the arguments with shell
		ShellCommand t = new ShellCommand();
		try {
			t.runCommand(getSelectedArgs());
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		Composite container_general = new Composite(settings, SWT.NONE);
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
		 * Set Text Area for auto suggestion TODO: better display for the
		 * options (actual option + keyword)
		 */
		final Text autoSuggest = new Text(container_advanced, SWT.BORDER
				| SWT.SEARCH | SWT.ICON_SEARCH);
		autoSuggest.setMessage("Type in to search");
		GridData autoGrid = new GridData(200, SWT.DEFAULT);
		autoGrid.verticalAlignment = GridData.BEGINNING;
		autoGrid.horizontalSpan = 2;
		autoSuggest.setLayoutData(autoGrid);

		// selected option list
		final Table selections = new Table(container_advanced, SWT.CHECK
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData tableGrid = new GridData();
		tableGrid.verticalSpan = 3;
		tableGrid.widthHint = 200;
		tableGrid.heightHint = 160;
		selections.setLayoutData(tableGrid);

		Label descriptionLabel = new Label(container_advanced, SWT.BORDER);
		descriptionLabel
				.setText("\n Use arrows to navigate \n and press enter to select\n");
		GridData descGrid = new GridData(200, SWT.DEFAULT);
		descGrid.horizontalAlignment = GridData.FILL;
		descGrid.verticalAlignment = GridData.FILL;
		descGrid.horizontalSpan = 2;
		descriptionLabel.setLayoutData(descGrid);

		// number of items appearing on the suggestion list
		final int restriction = 1000;

		final Shell popupShell = new Shell(SWT.ON_TOP);
		popupShell.setLayout(new FillLayout());
		final Table table = new Table(popupShell, SWT.CHECK | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);

		final Button clearButton = new Button(container_advanced, SWT.PUSH);
		GridData buttonGrid = new GridData();
		buttonGrid.verticalAlignment = GridData.END;
		buttonGrid.horizontalAlignment = GridData.FILL;
		clearButton.setLayoutData(buttonGrid);
		clearButton.setText("Clear");

		clearButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Clear checked items from selections
				selectedArgs.clear();
				refreshSelections(selections);
			}
		});

		final Button removeButton = new Button(container_advanced, SWT.PUSH);
		removeButton.setLayoutData(buttonGrid);
		removeButton.setText("Remove");

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

		// closing popupShell on dispose of current shell
		currShell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				popupShell.dispose();
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
					if (popupShell.isVisible()
							&& table.getSelectionIndex() != -1) {
						final TableItem item = table.getSelection()[0];
						final String str = item.getText();

						if (XMLKeywordsManager.takesInput(str)) {
							if(item.getChecked() && !item.getGrayed()){
								// Already selected.
								//TODO prevent multiple selection on non-multiple options
								// including the ones with the same name 
								System.out.println("cannot select multiple times");
							} else {
								// invoke argument input dialog
								// TODO move this to another function?
								final Shell dialog = new Shell(currShell,
										SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
								dialog.setText("Argument input");
								FormLayout formLayout = new FormLayout();
								formLayout.marginWidth = 10;
								formLayout.marginHeight = 10;
								formLayout.spacing = 10;
								dialog.setLayout(formLayout);
	
								final String argType = XMLKeywordsManager
										.getArgType(str);
								final int argNum = XMLKeywordsManager
										.getArgNum(str);
	
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
									data.width = 120 / argNum;
									if (i <= 0) {
										data.left = new FormAttachment(label, 0,
												SWT.DEFAULT);
									} else {
										data.left = new FormAttachment(
												inputFields[i - 1], 0, SWT.DEFAULT);
									}
									data.bottom = new FormAttachment(cancel, 0,
											SWT.DEFAULT);
									inputFields[i].setLayoutData(data);
								}
								inputFields[0].setFocus();
	
								Button ok = new Button(dialog, SWT.PUSH);
								ok.setText("OK");
								data = new FormData();
								data.width = 100;
								data.right = new FormAttachment(cancel, 0,
										SWT.DEFAULT);
								data.bottom = new FormAttachment(100, 0);
								ok.setLayoutData(data);
								ok.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent e) {
										// parse arguments and add to selectedArgs
										String resultOption = null;
										if (argType.equals("String")) {
											resultOption = str + "\""
													+ inputFields[0].getText()
													+ "\"";
										} else if (argType.equals("Integer")) {
											resultOption = str;
											try{
												for (int j = 0; j < argNum; j++) {
													int inputInt = 0;
														inputInt = Integer.parseInt(inputFields[j].getText());
														resultOption = resultOption.replace((char)('X' + j)+"",
																(inputInt + ""));
												}
											} catch (NumberFormatException nfe) {
												dialog.setText("Arguments must be integers!");
												resultOption = null;
											}
										}
										if (resultOption != null) { // okay to proceed
											selectedArgs.put(resultOption, str);
											refreshSelections(selections);
											dialog.close();
											// keep the popupShell open
											autoSuggest.setText(autoSuggest.getText());
											autoSuggest.setSelection(autoSuggest.getCharCount());
										}
									}
								});
								dialog.setDefaultButton(ok);
								dialog.pack();
								dialog.open();
							}
						//for options not taking arguments
						} else if (item.getChecked()) {
							item.setChecked(false);
							selectedArgs.remove(str);
							refreshSelections(selections);
						} else {
							item.setChecked(true);
							selectedArgs.put(str, str);
							refreshSelections(selections);
						}
					}
					break;
				case SWT.ESC:
					popupShell.setVisible(false);
					break;
				}
			}
		});
		autoSuggest.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				String string = autoSuggest.getText();
				if (string.length() == 0) {
					popupShell.setVisible(false);
				} else {
					Set<String> resultSet = new HashSet<String>(
							XMLKeywordsManager.searchPrefix(string,
									restriction,
									XMLKeywordsManager.OPTION_SEARCH));

					int numOfItems = resultSet.size(); // TODO remove?
					int numToShow = Math.min(8, numOfItems);
					// max. 8 items displayed, rest scrollable
					int maxLength = autoSuggest.getSize().x;

					// displays only when there is an item
					if (numOfItems <= 0) {
						popupShell.setVisible(false);
					} else {
						table.removeAll();
						// add items to the table

						Iterator<String> it = resultSet.iterator();
						while (it.hasNext()) {
							String keyword = it.next();
							maxLength = Math.max(maxLength, new GC(currShell)
									.textExtent(keyword).x);
							TableItem ti = null;
							if(string.equalsIgnoreCase(keyword)){
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

						Rectangle shellBounds = currShell.getBounds();
						Rectangle textBounds = autoSuggest.getBounds();
						Rectangle containerBounds = container_advanced
								.getBounds();
						popupShell.setBounds(shellBounds.x + textBounds.x
								+ containerBounds.x, shellBounds.y
								+ textBounds.y + containerBounds.y
								+ textBounds.height * 2, maxLength, 
								table.getItemHeight() * (numToShow+1) );
						popupShell.setVisible(true);
					}
				}
			}
		});

		table.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event event) {
				autoSuggest.setText(table.getSelection()[0].getText());
				popupShell.setVisible(false);
			}
		});
		table.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.ESC) {
					popupShell.setVisible(false);
				}
			}
		});

		Listener focusOutListener = new Listener() {
			public void handleEvent(Event event) {
				popupShell.setVisible(false);
			}
		};
		table.addListener(SWT.FocusOut, focusOutListener);
		autoSuggest.addListener(SWT.FocusOut, focusOutListener);

		currShell.addListener(SWT.Move, new Listener() {
			public void handleEvent(Event event) {
				popupShell.setVisible(false);
			}
		});

		// populate checkboxes
		createArgCheckboxes(argCheckboxComposite);

		generalSetting.setControl(container_general);
		advancedSetting.setControl(container_advanced);

		initContent();

		return settings;
	}

	// refresh selection table and checkboxes
	private void refreshSelections(Table selections) {
		selections.removeAll();
		for (String arg : selectedArgs.keySet()) {
			new TableItem(selections, SWT.NONE).setText(arg);
		}
		// only check the buttons that are in selectedArgs
		for (String arg : argCheckboxButtons.keySet()) {
			argCheckboxButtons.get(arg).setSelection(
					selectedArgs.containsValue(arg));
		}
	}

	protected Control createButtonBar(final Composite parent) {
		final Composite btnBar = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		btnBar.setLayout(layout);

		// Initialise default buttons
		final GridData defaultBtn = new GridData(SWT.FILL, SWT.BOTTOM, true,
				false);
		defaultBtn.grabExcessVerticalSpace = false;
		defaultBtn.grabExcessHorizontalSpace = true;
		btnBar.setLayoutData(defaultBtn);

		btnBar.setFont(parent.getFont());

		// add the dialog's button bar to the right
		final Control buttonCtrl = super.createButtonBar(btnBar);
		buttonCtrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false));

		return btnBar;
	}

	private void createArgCheckboxes(Composite parent) {
		argCheckboxButtons = new HashMap<String, Button>();
		for (String arg : argSet) {
			final String eachArg = arg;
			final Button button = new Button(parent, SWT.CHECK);
			button.setText(arg);
			argCheckboxButtons.put(eachArg, button);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (button.getSelection()) {
						selectedArgs.put(eachArg, eachArg);
					} else {
						selectedArgs.remove(eachArg);
					}
				}
			});
		}
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

	protected void initializeBounds() {
		super.initializeBounds();
		this.getButton(IDialogConstants.OK_ID).setText("Run");
	}

	public Set<String> getSelectedArgs() {
		return selectedArgs.keySet();
	}

}
