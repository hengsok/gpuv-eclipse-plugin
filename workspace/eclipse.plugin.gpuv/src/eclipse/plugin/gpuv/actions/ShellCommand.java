package eclipse.plugin.gpuv.actions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eclipse.plugin.gpuv.views.IViewDisplayResultUpdate;
import eclipse.plugin.gpuv.views.ViewDisplayResultUpdate;


public class ShellCommand {
	public void runCommand(String cmd) throws IOException {
	    Runtime r = Runtime.getRuntime();
	    String command = cmd;
	    String[] nargs = { "sh", "-c", command };
	    Process p = r.exec(nargs);
	    
	    
	    /* Output to console */
	    BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String line;
	    IViewDisplayResultUpdate v = new ViewDisplayResultUpdate();
	    v.clearItems();
	    while ((line = is.readLine()) != null)
	    {
	    	v.addViewItems(line);
	    }
		v.printItems();
	    
	}
}
