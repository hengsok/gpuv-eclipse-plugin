package eclipse.plugin.gpuv.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ConfigRecentlyUsedArgs {
	private final String recentArgsFilename = "/Users/hengsok/test.txt";

	public ConfigRecentlyUsedArgs(){
		
	}
	
	public void storeRecentArgs(Set<String> recentArgsToStore){
		BufferedWriter buf = null;

		try {
			buf = new BufferedWriter(new FileWriter(recentArgsFilename));
			
			for(String arg : recentArgsToStore){
				buf.write(arg);
				buf.newLine();
			}


		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (buf != null) {
					buf.flush();
					buf.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public Set<String> getRecentArgs(){
		Set<String> recentArgs = new HashSet<String>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(recentArgsFilename));

			String strLine = null;
			while ((strLine = br.readLine()) != null) {
				//check that we do not read empty line as arg
				if(!strLine.equals(""))
					recentArgs.add(strLine);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return recentArgs;
	}
	
}
