package eclipse.plugin.gpuv.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import eclipse.plugin.gpuv.radix.RadixTree;
import eclipse.plugin.gpuv.radix.RadixTreeImpl;

public class ConfigDialog extends Dialog {

	private Set<String> selectedArgs;
	private Set<String> argList;
	private Map<String, Button> argCheckboxButtons;

	public ConfigDialog(Shell parentShell) throws IOException {
		super(parentShell);

		// Read in list of arguments
		ConfigArgumentList configArgList = new ConfigArgumentList();
		ConfigRecentlyUsedArgs configRecentArgList = new ConfigRecentlyUsedArgs();
		argList = configArgList.getArgList();
		argList.addAll(configRecentArgList.getRecentArgs());

		this.selectedArgs = new HashSet<String>();

	}

	protected void okPressed() {
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
			// TODO Auto-generated catch block
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
		TabFolder settings = new TabFolder(parent, SWT.NULL);
		// Two Tab Options: general and advanced
		TabItem generalSetting = new TabItem(settings, SWT.NULL);
		TabItem advancedSetting = new TabItem(settings, SWT.NULL);
		// Set Folder Size
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, false,
				false);
		// gridData.heightHint = 600;
		// gridData.widthHint = 800;
		settings.setLayoutData(gridData);
		// Set Option title
		generalSetting.setText("General");
		advancedSetting.setText("Advanced");
		// Container for settings
		Composite container_general = new Composite(settings, SWT.NONE);
		Composite container_advanced = new Composite(settings, SWT.NONE);
		// Set general container layout
		GridLayout gridlayoutContainer = new GridLayout();
		gridlayoutContainer.numColumns = 2;
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
		argCheckboxLayout.numColumns = 2;
		argCheckboxComposite.setLayout(argCheckboxLayout);

		// Set labels for searchbox and selections
		Label searchLabel = new Label(container_advanced, SWT.BORDER);
		searchLabel.setText("Option search Box");
		Label selectionLabel = new Label(container_advanced, SWT.BORDER);
		selectionLabel.setText("Selected options: ");

		/*
		 * Set Text Area for auto suggestion TODO 1: connect general tab and
		 * advanced tab selection list TODO 2: better display for the options
		 * (actual option + keyword)
		 */
		final Text autoSuggest = new Text(container_advanced, SWT.BORDER);
		GridData autoGrid = new GridData(150, SWT.DEFAULT);
		autoGrid.verticalAlignment = GridData.BEGINNING;
		autoSuggest.setLayoutData(autoGrid);

		// selected option list
		final Table selections = new Table(container_advanced, SWT.CHECK
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData tableGrid = new GridData();
		tableGrid.verticalSpan = 2;
		tableGrid.widthHint = 150;
		tableGrid.heightHint = 150;
		selections.setLayoutData(tableGrid);

		// number of items appearing on the suggestion list
		final int restriction = 100;

		final Shell popupShell = new Shell(SWT.ON_TOP);
		popupShell.setLayout(new FillLayout());
		final Table table = new Table(popupShell, SWT.CHECK | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);

		final Button removeButton = new Button(container_advanced, SWT.PUSH);
		GridData removeGrid = new GridData();
		removeGrid.verticalAlignment = GridData.END;
		removeGrid.horizontalAlignment = GridData.END;
		removeButton.setLayoutData(removeGrid);
		removeButton.setText("Remove");

		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Clear checked items from selections
				TableItem ti[] = selections.getItems();
				for (int i = ti.length - 1; i >= 0; i--) {
					if (ti[i].getChecked()) {
						selections.remove(i);
					}
				}
			}
		});

		currShell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				popupShell.dispose();
			}
		});

		final RadixTree<String> rt = createSearchTree("keywords.txt");

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
							removeFromSelection(str, selections);
						} else {
							item.setChecked(true);
							addToSelection(str, selections);
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
					ArrayList<String> keywords = rt.searchPrefix(
							string.toLowerCase(), restriction);
					int numOfItems = keywords.size();
					int numToShow = (8 < numOfItems) ? 8 : numOfItems;
					// max. 8 items displayed, rest scrollable

					// displays only when there is an item
					if (numOfItems <= 0) {
						popupShell.setVisible(false);
					} else {
						table.removeAll();

						// add items to the table
						for (int i = 0; i < numOfItems; i++) {
							TableItem ti = new TableItem(table, SWT.NONE);
							ti.setText(keywords.get(i));
							// if in the selections, make it checked.
							ti.setChecked(isInSelection(keywords.get(i),
									selections));
						}

						// can press enter to select the first match
						table.setSelection(0);

						final Rectangle shellBounds = currShell.getBounds();
						Rectangle textBounds = autoSuggest.getBounds();
						popupShell.setBounds(2 + textBounds.x + shellBounds.x,
								textBounds.y + textBounds.height * 3
										+ shellBounds.y, textBounds.width,
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
		/* ********************************* */

		// populate checkboxes
		createArgCheckboxes(argCheckboxComposite);

		generalSetting.setControl(container_general);
		advancedSetting.setControl(container_advanced);

		initContent();

		return settings;
	}

	// checks if the str is already selected,
	// add to the table 'selections' if not.
	private void addToSelection(String str, Table selections) {
		// if exists, skip
		TableItem ti[] = selections.getItems();
		for (int i = 0; i < ti.length; i++) {
			if (ti[i].getText().equals(str)) {
				return; // exists, skip.
			}
		}
		// if not, add to table
		new TableItem(selections, SWT.NONE).setText(str);
	}

	private void removeFromSelection(String str, Table selections) {
		TableItem ti[] = selections.getItems();
		for (int i = 0; i < ti.length; i++) {
			if (ti[i].getText().equals(str)) {
				selections.remove(i);
			}
		}
	}

	private boolean isInSelection(String str, Table selections) {
		TableItem ti[] = selections.getItems();
		for (int i = 0; i < ti.length; i++) {
			if (ti[i].getText().equals(str)) {
				return true;
			}
		}
		return false;
	}

	private RadixTree<String> createSearchTree(String filename) {
		/*
		 * Creating a search tree for the keywords
		 */
		RadixTree<String> rt = new RadixTreeImpl<String>();

		BufferedReader br = null;
		try {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream(filename);
			br = new BufferedReader(new InputStreamReader(is));
			String line;
			String key;
			while ((line = br.readLine()) != null) {
				key = line.toLowerCase();
				if (!rt.contains(key)) {
					rt.insert(key, line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return rt;
	}

	protected Control createButtonBar(final Composite parent) {
		final Composite btnBar = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		btnBar.setLayout(layout);

		// Add an advanced button so that more options can be shown
		// TODO: remove unnecessary button
		// final Button advancedButton = new Button(btnBar, SWT.PUSH);
		// advancedButton.setText("Advanced");
		// advancedButton.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(SelectionEvent e) {
		// if (advancedButton.getSelection()) {
		// System.out.println("hello");
		// } else {
		// System.out.println("No");
		// }
		// }
		// });

		// final GridData advancedBtn = new GridData(SWT.LEFT, SWT.CENTER, true,
		// true);
		// advancedBtn.grabExcessHorizontalSpace = true;
		// advancedBtn.horizontalIndent =
		// convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		// advancedButton.setLayoutData(advancedBtn);

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
			button.setText(eachArg);
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
