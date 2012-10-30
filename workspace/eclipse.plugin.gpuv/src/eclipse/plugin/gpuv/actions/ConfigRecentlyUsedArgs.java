package eclipse.plugin.gpuv.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class ConfigRecentlyUsedArgs {
	private final String recentArgsFilename = "recentArgs.txt";

	public ConfigRecentlyUsedArgs(){
		
	}
	
	public void storeRecentArgs(Set<String> recentArgsToStore){
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(this.getClass().getClassLoader().getResource(recentArgsFilename).toString()));
			
			for(String arg : recentArgsToStore){
				fos.write(arg.getBytes());
				fos.write("\r\n".getBytes());
			}


		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
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
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(recentArgsFilename);
			br = new BufferedReader(new InputStreamReader(is));

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
