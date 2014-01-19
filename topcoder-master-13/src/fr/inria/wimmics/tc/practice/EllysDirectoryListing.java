package fr.inria.wimmics.tc.practice;
//srm534.div2

public class EllysDirectoryListing {
	public String[] getFiles(String[] files) {
		
		int total = files.length;
		
		if((files[total-1].equals(".") && files[total-2].equals("..")) || (files[total-2].equals(".") && files[total-1].equals("..")))
			return files;
		
		for(int i=0;i<total;i++) {
			if(files[i].equals(".") || files[i].equals("..")) {
				String tmp = files[i];
				files[i] = files[total-1];
				files[total-1] = tmp;
				break;
			}
		}
		
		if((files[total-1].equals(".") && files[total-2].equals("..")) || (files[total-2].equals(".") && files[total-1].equals("..")))
			return files;
		
		for(int i=0;i<total;i++) {
			if(files[i].equals(".") || files[i].equals("..")) {
				String tmp = files[i];
				files[i] = files[total-2];
				files[total-2] = tmp;
				break;
			}
		}

		return files;
		
	}
}
