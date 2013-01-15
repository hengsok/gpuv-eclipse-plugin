package eclipse.plugin.gpuv.pythoncheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * To check for whether python is installed and the version of it on current computer
 *
 */
public class PythonCheck {

	
	public PythonCheck(){
	}
	
	/**
	 * Test whether python is installed
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean isInstalled(){
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			proc = rt.exec("python --version");
			
			BufferedReader stdError = new BufferedReader(new 
					InputStreamReader(proc.getErrorStream()));
			String s;
			String[]output = null;
			//output[0] = "null";

			while ((s = stdError.readLine()) != null) {
				output = s.split(" ");
			}
			
			if (output[0].equals("Python"))
				return true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("You do not have python installed.");
		return false;
	}
	
	/**
	 * return version of python
	 * @return
	 * @throws IOException
	 */
	public static String checkVersion() throws IOException{

		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec("python --version");

		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(proc.getErrorStream()));
		String s;
		String[]output = null;
		while ((s = stdError.readLine()) != null) {
			output = s.split(" ");
		}
		return output[1];
	}
}
