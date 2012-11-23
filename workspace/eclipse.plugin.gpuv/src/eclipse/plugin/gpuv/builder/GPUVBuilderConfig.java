package eclipse.plugin.gpuv.builder;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class GPUVBuilderConfig {
	private Pattern filterPattern;
	private Pattern outputLinePattern;
	private String lineNumberReplacement;
	private String messageReplacement;
	private String severityReplacement;
	private String command;

	public GPUVBuilderConfig(){


		//TODO after compiling into jar, make sure path to python is still working
		this.command = "cmd.exe /c " + getGPUVBinaryLocation() + "GPUVerify.bat";
		String filterRegexp = "^.*\\.cl$";
		this.filterPattern = Pattern.compile(filterRegexp);
		String outputLineRegexp = "^([A-Z]:)?[^:]+:([0-9]+):([0-9]+): (.)(.*)$";
		this.outputLinePattern = Pattern.compile(outputLineRegexp);
		this.lineNumberReplacement = "$2";
		this.messageReplacement = "$4$5";
		this.severityReplacement = "E";
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

	public Pattern getFilterPattern(){
		return this.filterPattern;
	}

	public Pattern getOutputLinePattern(){
		return this.outputLinePattern;
	}

	public String getLineNumberReplacement(){
		return this.lineNumberReplacement;
	}

	public String getMessageReplacement(){
		return this.messageReplacement;
	}

	public String getSeverityReplacement(){
		return this.severityReplacement;
	}

	public String getCommand(){
		return this.command;
	}
}
