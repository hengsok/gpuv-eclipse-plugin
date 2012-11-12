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

	private Set<String> selectedArgs;
	private HashSet<String> argList; //TODO does this need to be global?
	private Map<String, Button> argCheckboxButtons;

	public ConfigDialog(Shell parentShell) throws IOException {
		super(parentShell);

		// Read in list of arguments
		ConfigArgumentList configArgList = new ConfigArgumentList("options.xml");
		ConfigRecentlyUsedArgs configRecentArgList = new ConfigRecentlyUsedArgs();
		argList = configArgList.getArgList();
		argList.addAll(configRecentArgList.getRecentArgs());

		this.selectedArgs = new HashSet<String>();

	}

	protected void okPressed() {
		//TODO need fix
		// store the arguments that're been selected for recently used list
		// later
		ConfigRecentlyUsedArgs configRecentUsed = new ConfigRecentlyUsedArgs();
		configRecentUsed.storeRecentArgs(selectedArgs);
		
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
		 * Set Text Area for auto suggestion 
		 * TODO: better display for the options
		 * (actual option + keyword)
		 */
		final Text autoSuggest = new Text(container_advanced, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH);
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
		descriptionLabel.setText("\n Use arrows to navigate \n and press enter to select\n");
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
						TableItem item = table.getSelection()[0];
						String str = item.getText();
						if (item.getChecked()) {
							item.setChecked(false);
							selectedArgs.remove(str);
							refreshSelections(selections);
						} else {
							item.setChecked(true);
							selectedArgs.add(str);
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
					Set<String> resultSet = new HashSet<String>(XMLKeywordsManager.searchPrefix(
							string, restriction, XMLKeywordsManager.OPTION_SEARCH));

					int numOfItems = resultSet.size(); //TODO remove?
					int numToShow = (8 < numOfItems) ? 8 : numOfItems;
					// max. 8 items displayed, rest scrollable
					int maxLength = autoSuggest.getSize().x;
					
					// displays only when there is an item
					if (numOfItems <= 0) {
						popupShell.setVisible(false);
					} else {
						table.removeAll();
						// add items to the table
						
						Iterator<String> it = resultSet.iterator();
						while(it.hasNext()){
							String keyword = it.next();
							maxLength = Math.max(maxLength, new GC(currShell).textExtent(keyword).x);
							TableItem ti = new TableItem(table, SWT.NONE);
							ti.setText(keyword);
							// if in the selections, make it checked.
							ti.setChecked(selectedArgs.contains(keyword));
						}
						
						// can press enter to select the first match
						table.setSelection(0);
						
						Rectangle shellBounds = currShell.getBounds();
						Rectangle textBounds = autoSuggest.getBounds();
						Rectangle containerBounds = container_advanced.getBounds();
						popupShell.setBounds(shellBounds.x + textBounds.x + containerBounds.x,
								shellBounds.y + textBounds.y + containerBounds.y
								+ textBounds.height * 2, maxLength,
								table.getItemHeight() * numToShow + 5);
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
		for (String arg : selectedArgs) {
			new TableItem(selections, SWT.NONE).setText(arg);
		}
		// only check the buttons that are in selectedArgs
		for (String arg : argCheckboxButtons.keySet()) {
			argCheckboxButtons.get(arg).setSelection(selectedArgs.contains(arg));
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
		for (String arg : argList) {
			final String eachArg = arg;
			final Button button = new Button(parent, SWT.CHECK);
			button.setText(arg);
			argCheckboxButtons.put(eachArg, button);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (button.getSelection()) {
						selectedArgs.add(eachArg);
					} else {
						selectedArgs.remove(eachArg);
					}
				}
			});
		}
	}

	private void initContent() {
		//TODO
		// Might want to set selection only for certain often used checkboxes
		// here
		// for (String arg : argList){
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
		return selectedArgs;
	}

}
