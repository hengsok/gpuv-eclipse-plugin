package eclipse.plugin.gpuv.pythoncheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonCheck {

	public static boolean isInstalled() throws IOException, InterruptedException{

		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec("python --version");

           BufferedReader stdError = new BufferedReader(new 
                InputStreamReader(proc.getErrorStream()));
           String s;
           String[]output = null;
           while ((s = stdError.readLine()) != null) {
        	   output = s.split(" ");
           }
           if (output[0].equals("Python"))
    		   return true;
           else
        	   return false;
	}
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
       	
//       	public static void main(String [ ] args) throws IOException, InterruptedException{
//       		System.out.println(isInstalled());
//       		System.out.println(checkVersion());
//       	}
}
