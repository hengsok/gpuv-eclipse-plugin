package eclipse.plugin.gpuv.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class GPUVBuilder extends IncrementalProjectBuilder {
	
	public static final String BUILDER_ID = "eclipse.plugin.gpuv.builder.GPUVBuilder";
	public static final String MARKER_TYPE = "eclipse.plugin.gpuv.builder.nature.problem";

//	private final static Logger LOGGER = Logger.getLogger(GPUVBuilder.class
//			.getName());

	private GPUVBuilderConfig config = null;
	
	class DeltaVisitor implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				addMarkersToResource(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				addMarkersToResource(resource);
				break;
			}
			// return true to continue visiting children.
			return true;
		}
	}

	class ResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			addMarkersToResource(resource);
			// return true to continue visiting children.
			return true;
		}
	}
	
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

	private GPUVBuilderConfig getConfig() {
		if (config == null) {
			this.config = new GPUVBuilderConfig();
		}
		return config;
	}

	protected IProject[] build(int kind, Map<String,String> args, IProgressMonitor monitor)
			throws CoreException {
		// GPUV: Requesting the builder to forget last build state
		super.forgetLastBuiltState();
		//invoke full build
		fullBuild(monitor);
		return null;
	}
	
	void addMarkersToResource(IResource resource) {
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

	private List<Issue> getIssues(IPath fullPath) {
		//LOGGER.log(Level.INFO, "Will generate issues for " + fullPath);
		List<Issue> issues = new ArrayList<Issue>();
		String line;
		Process p;
		String command = getConfig().getCommand() + " --local_size=1024 --num_groups=2 " + fullPath.makeAbsolute().toOSString();
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
					//TODO Might need to change to System.out
					GPUVDefaultConsole.printToConsole(line + "\n");
					//System.out.println(line);
				}
			}
			bri.close();
			
			//analysing error stream
			while ((line = bre.readLine()) != null) {
				issues.add(new Issue(IMarker.SEVERITY_ERROR, 1, "Error stream reports: "
						+ line));
				//print to console
				//TODO Might need to change to System.out
				GPUVDefaultConsole.printToConsole(line);
			}
			bre.close();
			
		} catch (IOException e) {
			issues.add(new Issue(IMarker.SEVERITY_ERROR, 1, "I/O error: "
					+ e.getMessage()));
			return issues;
		}
		//TODO handle different exit point
		int gpuVerifyExitValue;
		try {
			gpuVerifyExitValue = p.waitFor();
		} catch (InterruptedException e) {
			issues.add(new Issue(IMarker.SEVERITY_ERROR, 1,
					"Execution error: " + e.getMessage()));
			return issues;
		}
		return issues;
	}

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
					
					//Define the types of marker to use here
					int markerSeverity = IMarker.SEVERITY_ERROR;
					
					
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

	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			//TODO: if we want to run only certain file, have to change ResourceVisitor here
			getProject().accept(new ResourceVisitor());
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {

		delta.accept(new DeltaVisitor());
	}
}
