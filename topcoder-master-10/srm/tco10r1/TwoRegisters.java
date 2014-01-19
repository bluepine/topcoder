public class TwoRegisters {
	String min_f = "Z";

	public String dp(int depth, int x, int y) {
		if (!min_f.equals("Z") && depth > min_f.length()) {
			return null;
		}
		if (x <= 0 || y <= 0 || depth > 100) return null;
		if (x == 1 && y == 1) {
			return "";
		}

		String str1 = dp(depth + 1, x, y - x);
		String str2 = dp(depth + 1, x - y, y);

		if (str1 == null && str2 == null) {
			return null;
		}
		if (str1 == null) {
			return str2 + "X";
		}
		if (str2 == null) {
			return str1 + "Y";
		}
		if (str1.compareTo(str2) > 0) {
			return str1 + "Y";
		} else {
			return str2 + "X";
		}
	}

	public String minProg(int r) {
		if (r == 1) return "";
		if (r == 2) return "X";
		if (r == 3) return "XX";

		int ct = r / 2;
		for (int i = 0 ; i <= ct ; i++) {
			int a = ct - i;
			int b = r - a;
			String f = dp(0, a, b);
			if (f != null) {
				if (f.length() < min_f.length() || min_f.equals("Z")) {
					min_f = f;
				} else if (f.length() == min_f.length() && f.compareTo(min_f) < 0) {
					min_f = f;
				}
			}
			int temp = a;
			a = b;
			b = temp;
			f = dp(0, a, b);
			if (f != null) {
				if (f.length() < min_f.length() || min_f.equals("Z")) {
					min_f = f;
				} else if (f.length() == min_f.length() && f.compareTo(min_f) < 0) {
					min_f = f;
				}
			}
		}
		return min_f + "X";
	}
}