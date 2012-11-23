package eclipse.plugin.gpuv.builder;

import java.util.regex.Pattern;

public class GPUVBuilderConfig {
	private Pattern filterPattern;
	private Pattern outputLinePattern;
	private String lineNumberReplacement;
	private String messageReplacement;
	private String severityReplacement;
	private String command;
	
	public GPUVBuilderConfig(){
		this.command = "sh /Users/hengsok/Dropbox/ComputingYear3/GPUVerify/project/runtime-New_configuration(2)/TestLK/execute2.sh \"$1\"";
		String filterRegexp = "^.*\\.cl$";
		this.filterPattern = Pattern.compile(filterRegexp);
		String outputLineRegexp = "^([A-Z]:)?[^:]+:([0-9]+):([0-9]+): (.)(.*)$";
		this.outputLinePattern = Pattern.compile(outputLineRegexp);
		this.lineNumberReplacement = "$2";
		this.messageReplacement = "$4$5";
		this.severityReplacement = "E";
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
