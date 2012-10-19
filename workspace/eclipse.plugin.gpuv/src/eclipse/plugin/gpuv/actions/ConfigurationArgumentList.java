package eclipse.plugin.gpuv.actions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationArgumentList {
	private Set <String> argList;

	public ConfigurationArgumentList() throws IOException{

		
		try{
			argList = new HashSet<String>();
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("argList.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String strLine;
			
			while ((strLine = br.readLine()) != null)   {
				argList.add(strLine);
			}
			is.close();
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
	}

	public Set<String> getArgList(){
		return argList;
	}
	
}
