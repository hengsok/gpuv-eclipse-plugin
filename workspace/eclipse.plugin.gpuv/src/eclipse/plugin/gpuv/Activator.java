package eclipse.plugin.gpuv;


import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import eclipse.plugin.gpuv.builder.GPUVBuilderConfig;
import eclipse.plugin.gpuv.builder.GPUVDefaultConsole;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "eclipse.plugin.gpuv"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		Bundle bundle = Platform.getBundle("eclipse.plugin.gpuv");
		URL url = bundle.getEntry("xmlFiles/options.xml");
		URL fileURL = org.eclipse.core.runtime.FileLocator.toFileURL(url);
		//String location = FileLocator.getBundleFile(context.getBundle()).getAbsolutePath();
		
		GPUVDefaultConsole.printToConsole("This is using console printing " + fileURL.toURI());
		//new XMLKeywordsManager(location); // read necessary xml files
		GPUVBuilderConfig newd = new GPUVBuilderConfig();
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
