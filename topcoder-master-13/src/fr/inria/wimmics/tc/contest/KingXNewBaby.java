package fr.inria.wimmics.tc.contest;
//.srm537
public class KingXNewBaby {

	boolean isVowel(char ch) {
		if(ch=='a' || ch=='e' || ch=='i' || ch=='o' || ch=='u')
			return true;
		return false;
	}
	public String isValid(String name) {
		if(name.length()>8 || name.length()<8)
			return "NO";
		int count=0;
		
		for(int i=0;i<name.length();i++) {
			if(isVowel(name.charAt(i))) {
				count++;
			}
		}
		if(count>2 || count<2 ) {
			return "NO";
		}
		
		char chv=0;
		int cc=0;
		for(int i=0;i<name.length();i++) {
			if(isVowel(name.charAt(i))) {
				cc++;
				if(cc==2) 
				{
					if(chv!=name.charAt(i))
						return "NO";
				}
				chv = name.charAt(i);
				
			}
		}
		return "YES";
		
	}


}
