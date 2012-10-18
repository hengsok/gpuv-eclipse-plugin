package group.plugins.frame.actions;

import java.io.*;

public class main{
	public static void main(String args[]) throws Exception{
		
		InputTerminal input = new Echo();
		Process p  = input.echoInput("echo hello world"); //pushes command line "echo hello world to terminal
		
		/*reading from the terminal*/
		BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = is.readLine()) != null)
        {
            System.out.println(line);
        }
	}

}
