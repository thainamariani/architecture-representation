package mestrado.arquitetura.helpers;

import java.io.File;
import java.io.FilenameFilter;

public class OnlyCompleteResources implements FilenameFilter {

	public boolean accept(File dir, String name) {
		boolean resoucesCompleteNotation= false;
		boolean resoucesCompletDi = false;
		
		File[] listOfFiles = dir.listFiles(); 
		for(int i=0; i < listOfFiles.length; i++){
			String nameFile = listOfFiles[i].getName();
			if (nameFile.equalsIgnoreCase(name+".notation")) resoucesCompleteNotation = true; 
			if (nameFile.equalsIgnoreCase(name+".di")) resoucesCompletDi = true; 
		}
		
		if (resoucesCompleteNotation && resoucesCompletDi) return true;
		return false;
	}

}