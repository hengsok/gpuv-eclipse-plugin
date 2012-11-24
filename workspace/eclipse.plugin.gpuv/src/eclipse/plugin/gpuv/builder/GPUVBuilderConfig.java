package eclipse.plugin.gpuv.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Configuration for builder defining regular expression, command to execute 
 * @author Heng Sok
 *
 */

public class GPUVBuilderConfig {
	private Pattern filterPattern;
	private String command;
	
	private List<GPUVRegex> regexs;

	public GPUVBuilderConfig(){
		regexs = new ArrayList<GPUVRegex>();

		//TODO after compiling into jar, make sure path to python is still working
		this.command = "cmd.exe /c " + getGPUVBinaryLocation() + "GPUVerify.bat";
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

	private String getGPUVBinaryLocation(){
		//Determine the location of this plugin to get location of GPUV binary
		Bundle bundle = Platform.getBundle("eclipse.plugin.gpuv");
		String location = "";
		try {
			location = FileLocator.getBundleFile(bundle).getAbsolutePath();
		} catch (IOException e) {
			//:TODO might need to change print to console to System.out
			GPUVDefaultConsole.printToConsole("Internal Error: Could not determine file path" +
					" of GPUVerify Binary files.");
		}

		//Append the GPUV binary folder
		String finalLocation = location + File.separator + "GPUVerifyBinary" + File.separator;
		
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
