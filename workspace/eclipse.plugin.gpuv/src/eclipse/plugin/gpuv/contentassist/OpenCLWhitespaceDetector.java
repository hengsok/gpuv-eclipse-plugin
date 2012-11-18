package eclipse.plugin.gpuv.contentassist;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class OpenCLWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}