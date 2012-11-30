package eclipse.plugin.gpuv.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import eclipse.plugin.gpuv.XMLKeywordsManager;

public class GPUVBuilderThread implements Runnable {
	private GPUVBuilderConfig config = null;
	private int markerSeverity = IMarker.SEVERITY_WARNING;
	public static final String MARKER_TYPE = "eclipse.plugin.gpuv.builder.nature.problem";
	private IFile fileToBuild;
	
	/**
	 * Inner class to store issue
	 *
	 */
	class Issue {
		public String message;
		public int markerSeverity;
		public int lineNumber;

		public Issue(int markerSeverity, int lineNumber, String message) {
			super();
			this.message = message;
			this.markerSeverity = markerSeverity;
			this.lineNumber = lineNumber;
		}
	}
	
	public GPUVBuilderThread(IFile fileToBuild){
		this.fileToBuild = fileToBuild;
	}

	public void run() {
		addMarkersToResource(fileToBuild);
	}
	
	/**
	 * Get config for this builder
	 * @return
	 */
	private GPUVBuilderConfig getConfig() {
		if (config == null) {
			this.config = new GPUVBuilderConfig();
		}
		return config;
	}
	
	/**
	 * Add problem markers for the OpenCL file currently in view
	 * @param resource
	 */
	private void addMarkersToResource(IResource resource) {
		//LOGGER.log(Level.INFO, "Trying " + resource.getName() + "...");
		if (resource instanceof IFile
				&& getConfig().getFilterPattern().matcher(resource.getName()).find()) {
			IFile file = (IFile) resource;
			deleteMarkers(file);
			List<Issue> issues = getIssues(file.getRawLocation().makeAbsolute());
			try {
				for (Issue i : issues) {
					IMarker marker = file.createMarker(MARKER_TYPE);
					marker.setAttribute(IMarker.MESSAGE, i.message);
					marker.setAttribute(IMarker.SEVERITY, i.markerSeverity);
					marker.setAttribute(IMarker.LINE_NUMBER, i.lineNumber);
				}
			} catch (CoreException e) {
			}
		}
	}

	/**
	 * Execute the batch file to run python file on the OpenCL file and get back
	 * the result. 
	 * @param fullPath
	 * @return list of issues
	 */
	private List<Issue> getIssues(IPath fullPath) {
		List<Issue> issues = new ArrayList<Issue>();
		String line;
		Process p;

		Set<String> options = XMLKeywordsManager.getApplicedOptionSet();
		if(options.contains("--findbugs")){
			markerSeverity = IMarker.SEVERITY_ERROR;
		}else{
			markerSeverity = IMarker.SEVERITY_WARNING;
		}
		String optionString = " ";
		
		for(String t : options){
			optionString +=  t + " ";

		}

		String command = getConfig().getCommand() + optionString + fullPath.makeAbsolute().toOSString();
		//TODO: remove this and testOptions
		GPUVDefaultConsole.printToConsole(command + "\n");
		try {
			//LOGGER.log(Level.INFO, "Execute: " + command);
			p = Runtime.getRuntime().exec(command);
		} catch (IOException e1) {
			issues.add(new Issue(IMarker.SEVERITY_ERROR, 1,
					"Couldn't execute: " + command + "\n" + e1.getMessage()));
			return issues;
		}

		//Get input stream
		BufferedReader bri = new BufferedReader(new InputStreamReader(
				p.getInputStream()));

		//analysing error stream
		BufferedReader bre = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		try {
			//analysing input stream
			int lookAheadLine = 0;
			int lineCount = 0;
			String lookAheadMsg = "";
			while ((line = bri.readLine()) != null) {
				if(!line.isEmpty()){
					//Detecting three cases handle next few lines that follows
					if (line.contains("read-write")) {
						lookAheadLine = 2;
						lineCount = 1;
						lookAheadMsg = "Possible read-write race ";
					}
					else if (line.contains("write-read")) {
						lookAheadLine = 2;
						lineCount = 1;
						lookAheadMsg = "Possible write-read race ";
					}
					else if (line.contains("write-write")) {
						lookAheadLine = 2;
						lineCount = 1;
						lookAheadMsg = "Possible write-write race ";
					}
					else{
						Issue issue = null;
						//Write-write, read-write or write-read
						if(lookAheadLine > 0 && (line.contains("write by") 
								|| line.contains("read by"))){
							issue = readIssue(line, lookAheadMsg + lineCount + ": ");
							lineCount++;
							lookAheadLine--;

							if(lookAheadLine == 0){
								//reset lookAheadMsg
								lookAheadMsg = "";
							}
						}
						else{
							issue = readIssue(line, "");
						}
						//Add to list of issues to create markers later
						if (issue != null)
							issues.add(issue);
					}
					//print to console
					GPUVDefaultConsole.printToConsole(line + "\n");

				}
			}
			bri.close();

			//analysing error stream
			while ((line = bre.readLine()) != null) {
				issues.add(new Issue(IMarker.SEVERITY_ERROR, 1, "Error stream reports: "
						+ line));
				//print to console
				GPUVDefaultConsole.printToConsole(line);
			}
			bre.close();

		} catch (IOException e) {
			issues.add(new Issue(IMarker.SEVERITY_ERROR, 1, "I/O error: "
					+ e.getMessage()));
			return issues;
		}
		//handle different exit point
		int gpuVerifyExitValue;
		try {
			gpuVerifyExitValue = p.waitFor();
		} catch (InterruptedException e) {
			issues.add(new Issue(IMarker.SEVERITY_ERROR, 1,
					"Execution error: " + e.getMessage()));
			return issues;
		}

		//indicate what error is happening or success
		String errorString = checkExitCode(gpuVerifyExitValue);
		GPUVDefaultConsole.printToConsole(errorString);

		return issues;
	}

	/**
	 * This will check for each exit value and return the corresponding result
	 * @param gpuVerifyExitValue
	 * @return Message to display for each exit value retrieved from python file
	 */
	private String checkExitCode(int gpuVerifyExitValue){
		String errorString;
		switch (gpuVerifyExitValue) {
		case 0:  errorString = "Analysis is run successfully.";
		break;
		case 1:  errorString = "There is a command line error.";
		break;
		case 2:  errorString = "There is a CLANG error. Please contact the developer.";
		break;
		case 3:  errorString = "There is an OPT error. Please contact the developer.";
		break;
		case 4:  errorString = "There is a Bugle error. Please contact the developer.";
		break;
		case 5:  errorString = "There is a GPUVerifyVCGen error. Please contact the developer.";
		break;
		case 6:  errorString = "There is an Boogie error. ";
		break;
		default: errorString = "";
		break;
		}
		return errorString;
	}

	/**
	 * Analyse the line by passing it to a simple parser. Extract the line error
	 * using the our very own self-built error parser. This uses regex.
	 * 
	 */
	private Issue readIssue(String line, String appendAdditionalMsg) {
		//LOGGER.log(Level.INFO, "Parsing line: " + line);
		try {
			//go through each regex and try to match anyone of them with this line
			for(GPUVRegex g: config.getGPUVRegex()){
				Matcher matcher = g.getOutputLinePattern().matcher(line);
				if (matcher.find()) {
					int lineNumber = Integer.parseInt(matcher
							.replaceAll(g.getLineNumReplacement()));

					String message = matcher
							.replaceAll(g.getMsgReplacement());

					//trim message to remove whitespace in front or at back of string
					message = message.trim();

					//Append any additional messages
					message = appendAdditionalMsg + message;

					Issue issue = new Issue(markerSeverity, lineNumber, message);
					return issue;
				}
			}

		} catch (RuntimeException e) {
			return new Issue(IMarker.SEVERITY_ERROR, 1,
					"Error at this line: " + line + "\n" + e.getMessage());
		}
		return null;
	}

	/**
	 * This will be called before new markers are added. Delete existing ones.
	 * @param file openCL file to delete markers
	 */
	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}
}
