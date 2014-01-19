public class RightTriangle {
	public long triangleCount(int places, int points, int a, int b, int c) {
		if (places % 2 != 0 || places <= 3) return 0;
		int rad = places / 2;
		int[] red_num = new int[places];

		for (int i = 0 ; i < points ; i++) {
			long pl = ((long) a * i * i + (long) b * i + (long) c);
			int place = (int)(pl % places);
			red_num[place]++;
		}

		for (int i = 0 ; i < places * 2 ; i++) {
			int pl = i % places;
			if (red_num[pl] >= 2) {
				red_num[(pl+1)%places] += red_num[pl] - 1;
				red_num[pl] = 1;
			}
		}

		long count = 0;
		for (int i = 0 ; i < rad ; i++) {
			if (red_num[i] >= 1 && red_num[i+rad] >= 1) {
				count += (points - 2L);
			}
		}
		return count;
	}
}
