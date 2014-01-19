package fr.inria.wimmics.tc.practice;

import fr.inria.wimmics.tc.practice.EllysDirectoryListing;

public class PracticeMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EllysDirectoryListing a = new EllysDirectoryListing();
		String files[] = {"ContestApplet.jnlp", ".", "Image.jpg", "..", "Book.pdf", "Movie.avi"};
		String res[] = a.getFiles(files);
		
		
		System.out.print("{");
		boolean first = true;
		for(String t:res) {
			if(!first)
				System.out.print(", ");
			System.out.print(t);
			first = false;
		}
		System.out.print("}");
	}

}
