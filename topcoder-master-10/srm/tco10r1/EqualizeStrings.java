public class EqualizeStrings {

	public String getEq(String s, String t) {
		if (s.equals(t)) {
			return s;
		}
		int size = s.length();
		String str = "";
		for (int i = 0 ; i < size ; i++)
		{
			int dist = Math.abs(s.charAt(i) - t.charAt(i));
			if (dist < 13) {
				if (s.charAt(i) < t.charAt(i)) {
					str += String.valueOf(s.charAt(i));
				} else {
					str += String.valueOf(t.charAt(i));
				}
			} else {
				str += "a";
			}
		}
		return str;
	}

}
