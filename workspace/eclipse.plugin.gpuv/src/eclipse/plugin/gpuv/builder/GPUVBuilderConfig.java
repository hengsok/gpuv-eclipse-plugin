package eclipse.plugin.gpuv.builder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Configuration for builder defining regular expression, command to execute 
 *
 */

public class GPUVBuilderConfig {
	private Pattern filterPattern;
	private String command;
	
	private List<GPUVRegex> regexs;

	public GPUVBuilderConfig(){
		regexs = new ArrayList<GPUVRegex>();

		//TODO after compiling into jar, make sure path to python is still working
		String envSpecificCommands = getEnvSpecificCommands();
		if(envSpecificCommands != null){
			this.command = envSpecificCommands;
		}
		else{
			GPUVDefaultConsole.printToConsole("There is some problem invoking the GPUVerify python script." +
					" This could be due to OS issue. Please contact the developer.");
		}
		
		String filterRegexp = "^.*\\.cl$";
		this.filterPattern = Pattern.compile(filterRegexp);
		/**([A-Z]:)? => Determines the drive letter if this is a wins system. For Linux, ignore.
		 * [^:]+ => Except semi-colon, this could be anything
		 * ([0-9]+) => This could only be numbers referring to line containing error
		 * ([0-9]+) => This gives column number
		 * (.)(.*) => Give any messages
		 */
		
		String outputLineRegex1 = "^([A-Z]:)?[^:]+:([0-9]+):([0-9]+): (.)(.*)$";
		regexs.add(new GPUVRegex(outputLineRegex1, "$2", "$4$5"));

	}

	private String getEnvSpecificCommands(){
		String OS = System.getProperty("os.name").toLowerCase();
		if((OS.indexOf("win") >= 0)){
			return "cmd.exe /c " + getGPUVBinaryLocation() + File.separator 
					+ "windows" + File.separator +  "GPUVerify.bat";
		}else if((OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 )) {
			return "python " + getGPUVBinaryLocation() + File.separator 
					+ "linux" + File.separator +  "GPUVerify.py";
		}
		return null;
	}
	
	private String getGPUVBinaryLocation(){
		//Determine the location of this plugin to get location of GPUV binary
		Bundle bundle = Platform.getBundle("eclipse.plugin.gpuv");
		URL url = bundle.getEntry("GPUVerifyBinary");
		//GPUVDefaultConsole.printToConsole(url.toString());
		URL fileURL = null;
		try {
			fileURL = FileLocator.toFileURL(url);
		} catch (IOException e1) {
			GPUVDefaultConsole.printToConsole("Internal Error: Could not determine file path" +
					" of GPUVerify Binary files.");
		}
		//Append the GPUV binary folder
		String finalLocation = new File(fileURL.getPath()).getPath();
		finalLocation = finalLocation + File.separator;
		return finalLocation;
	}
	
	public List<GPUVRegex> getGPUVRegex(){
		return this.regexs;
	}

	public Pattern getFilterPattern(){
		return this.filterPattern;
	}

	public String getCommand(){
		return this.command;
	}
}
