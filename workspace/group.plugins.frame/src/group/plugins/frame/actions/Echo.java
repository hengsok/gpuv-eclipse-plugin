package group.plugins.frame.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Echo implements InputTerminal{
	public Echo(){
	}
	
	public Process echoInput(String command) throws IOException{
		
		Runtime r = Runtime.getRuntime();
		String[] nargs = { "sh", "-c", command };
		Process p = r.exec(nargs);
		
		return p;
	}
	
	
}
