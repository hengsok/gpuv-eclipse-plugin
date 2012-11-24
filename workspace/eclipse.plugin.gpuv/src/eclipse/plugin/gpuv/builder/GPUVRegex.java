package eclipse.plugin.gpuv.builder;

import java.util.regex.Pattern;

/**
 * Store regular expression that has been compiled into a pattern
 * with line replacement number and message replacement.
 * @author Heng Sok
 *
 */

public class GPUVRegex {
	private Pattern outputLinePattern;
	private String lineNumReplacement;
	private String msgReplacement;
	
	public GPUVRegex(String outputLineRegex, String lineNumReplacement, String msgReplacement){
		this.outputLinePattern = Pattern.compile(outputLineRegex);
		//Line is at region index 2
		this.lineNumReplacement = lineNumReplacement;
		//Msg can be in region index 4/5
		this.msgReplacement = msgReplacement;
	}
	
	public Pattern getOutputLinePattern(){
		return this.outputLinePattern;
	}
	
	public String getLineNumReplacement(){
		return this.lineNumReplacement;
	}
	
	public String getMsgReplacement(){
		return this.msgReplacement;
	}
}
