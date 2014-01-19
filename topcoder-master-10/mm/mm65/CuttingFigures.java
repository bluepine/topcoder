import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CuttingFigures {
	public int width = 0;
	public int height = 0;
	public int[][] field;
	public int KK;
	public int[] weight;

	public int nth_order;
	public int order_count;

	public int field_left;
	public int field_used;
	public int[] field_shape_left;

	public boolean mode = false;
	public boolean find_yobun = true;
	public boolean first_search = false;
	public boolean strict_yobun = true;

	public int total;
	public int shape_num;

	public long total_time;

	public String[][] shape_list;

	public ArrayList<String> use_shape;
	public ArrayList<Integer> use_shape_x;
	public ArrayList<Integer> use_shape_y;

	public ArrayList<String> best_use_shape;
	public ArrayList<Integer> best_use_shape_x;
	public ArrayList<Integer> best_use_shape_y;
	public int[][] best_field;
	public int[] best_usage;

	public void setup(int K) {
		switch (K) {
			case 3:
				shape_list = setup3();
				break;
			case 4:
				shape_list = setup4();
				break;
			case 5:
				shape_list = setup5();
				break;
			case 6:
				shape_list = setup6();
				break;
			case 7:
				shape_list = setup7();
				break;
			case 8:
				shape_list = setup8();
				break;
			case 9:
				shape_list = setup9();
				break;
			case 10:
				shape_list = setup10();
				break;
		}
	}

	public int simulation(int[][] field, int[] usage, int nth, int ath, int from, int to, ArrayList<String> use_shape, ArrayList<Integer> use_shape_x, ArrayList<Integer> use_shape_y) throws Exception {
		int width = field[0].length;
		int height = field.length;

		int idx = 0;
		for (int arr_idx : usage) {
			if (System.currentTimeMillis() - total_time >= 17000) {
				// TODO:
				throw new Exception();
			}
			idx++;
			if (idx < from) continue;
			if (idx >= to) break;
			int shape_num = shape_list[arr_idx].length;
			String shape = shape_list[arr_idx][(int)(Math.random() * shape_num)];
			int[][] figure = convert_array(shape);
			int f_width = figure[0].length;
			int f_height = figure.length;

			boolean found_place = false;
			shape:
			for (int i = 0 ; i <= height - f_height ; i++) {
				for (int j = 0 ; j <= width - f_width ; j++) {
					boolean found = true;
					check:
					for (int k = 0 ; k < f_height ; k++) {
						for (int l = 0 ; l < f_width ; l++) {
							if (figure[k][l] == 1 && field[i+k][j+l] != 0) {
								found = false;
								break check;
							}
						}
					}

					if (found) {
						int[][] kari_field = clone2(field);
						for (int k = 0 ; k < f_height ; k++) {
							for (int l = 0 ; l < f_width ; l++) {
								kari_field[i+k][j+l] += figure[k][l];
							}
						}

						int from_x = (j == 0) ? 0 : j - 1, to_x = (j == width - f_width) ? width - 1 : j + f_width;
						int from_y = (i == 0) ? 0 : i - 1, to_y = (i == height - f_height) ? height - 1 : i + f_height;
						int total_yobun = yobun(kari_field, from_x, to_x, from_y, to_y);
						if (total_yobun <= 0) {
							use_shape.add(shape);
							use_shape_x.add(j);
							use_shape_y.add(i);

							for (int k = 0 ; k < f_height ; k++) {
								for (int l = 0 ; l < f_width ; l++) {
									field[i+k][j+l] += figure[k][l];
								}
							}
							found_place = true;
							break shape;
						}
					}
				}
			}

			if (!found_place) {
				usage[idx - 1] = -1;
			}
		}

		int yobun = 0;
		for (int i = 0 ; i < height * nth / ath ; i++) {
			for (int j = 0 ; j < width ; j++) {
				if (field[i][j] == 0) {
					yobun++;
				}
			}
		}
		return yobun;
	}

	@SuppressWarnings("unchecked")
	public void estimate_usage() {
		int shape_list_length = shape_list.length;
		double average_weight = 0;
		for (int i = 0 ; i < shape_list_length ; i++) {
			average_weight += weight[i];
		}
		average_weight /= shape_list_length;

		int[] left = new int[shape_list.length];
		int left_index = 0, total_left = 0;

		double base = field_left / shape_num;
		for (String[] shapearr : shape_list) {
			int use = (int)(base * shapearr.length * weight[left_index] / average_weight) + 2;
			total_left += use;
			left[left_index] = use;
			left_index++;
		}

		int[] usage = new int[total_left];
		int total_index = 0;
		for (int i = 0 ; i < left_index ; i++) {
			for (int j = 0 ; j < left[i] ; j++) {
				usage[total_index] = i;
				total_index++;
			}
		}

		int best_yobun = 99999;
		int from = 0;
		int to = total_index;

		best_field = clone2(field);
		best_usage = usage.clone();
		best_use_shape = new ArrayList<String>();
		best_use_shape_x = new ArrayList<Integer>();
		best_use_shape_y = new ArrayList<Integer>();

		int divide = 4;
		for (int n = 1 ; n <= divide ; n++) {
			best_yobun = 99999;
			from = total_index * (n - 1) / divide;
			long start_time = System.currentTimeMillis();
			int[][] from_field = clone2(best_field);
			int[] from_usage = best_usage.clone();
			ArrayList<String> from_shape = (ArrayList<String>) best_use_shape.clone();
			ArrayList<Integer> from_shape_x = (ArrayList<Integer>) best_use_shape_x.clone();
			ArrayList<Integer> from_shape_y = (ArrayList<Integer>) best_use_shape_y.clone();
			while (System.currentTimeMillis() - start_time <= 4000) {
				ArrayList<String> use_shape = (ArrayList<String>) from_shape.clone();
				ArrayList<Integer> use_shape_x = (ArrayList<Integer>) from_shape_x.clone();
				ArrayList<Integer> use_shape_y = (ArrayList<Integer>) from_shape_y.clone();
				int[][] use_field = clone2(from_field);
				usage = from_usage.clone();
				for (int i = 0 ; i < total_index ; i++) {
					int idx1 = (int)(Math.random() * (to - from)) + from;
					int idx2 = (int)(Math.random() * (to - from)) + from;
					int temp = usage[idx1];
					usage[idx1] = usage[idx2];
					usage[idx2] = temp;
				}
				int result = 0;
				try {
					result = simulation(use_field, usage, n, divide, total_index * (n - 1) / divide, total_index * n / divide, use_shape, use_shape_x, use_shape_y);
				} catch (Exception e) {
					mode = true;
					find_yobun = false;
					break;
				}
				if (result < best_yobun) {
					best_yobun = result;
					best_use_shape = (ArrayList<String>) use_shape.clone();
					best_use_shape_x = (ArrayList<Integer>) use_shape_x.clone();
					best_use_shape_y = (ArrayList<Integer>) use_shape_y.clone();
					best_field = clone2(use_field);
					best_usage = usage.clone();
					// TODO:
					// System.err.println(best_yobun);
				}
			}
		}

		field_shape_left = new int[shape_list.length];
		for (int use : best_usage) {
			if (use >= 0) {
				field_shape_left[use]++;
			}
		}

		int add_all = best_yobun / KK / shape_list.length + 1;
		for (int i = 0 ; i < shape_list.length ; i++) {
			field_shape_left[i] += add_all;
		}

		/**
			for (int i = 0 ; i < best_use_shape.size() ; i++) {
				System.err.println("(" + best_use_shape_x.get(i) + "," + best_use_shape_y.get(i) + ") = " + best_use_shape.get(i));
			}
			for (int i = 0 ; i < height ; i++) {
				for (int j = 0 ; j < width ; j++) {
					System.err.print(best_field[i][j]);
				}
				System.err.println();
			}
		 */
	}

	public int init(String[] plate, int K, int orderCnt) {
		total_time = System.currentTimeMillis();
		width = plate[0].length();
		height = plate.length;
		field = new int[height][width];
		KK = K;
		shape_num = 0;
		order_count = orderCnt;

		setup(K);
		for (String[] shapearr : shape_list) {
			shape_num += shapearr.length;
		}

		int bad = 0;
		for (int i = 0 ; i < height ; i++) {
			for (int j = 0 ; j < width ; j++) {
				char x = plate[i].charAt(j);
				switch (x) {
					case 'X':
						field[i][j] = 1;
						bad++;
						break;
				}
			}
		}
		field_left = (width * height - bad) / K;
		field_used = 0;

		if (K <= 9) {
			first_search = true;
			weight = new int[shape_list.length];
		}
		if (K >= 10) {
			mode = true;
		} else {
		}

		int take_order = (width * height - bad) / K;
		threshold = (1 - ((double)take_order / orderCnt)) * 100;
		min_threshold = threshold - 3;

		return 0;
	}

	public double threshold = 0.0f;
	public double min_threshold = 0.0f;
	public double threshold_shape[];

	public int[] goriosi(String[] figure, int shapearr_index, boolean strict) {
		int f_width = figure[0].length();
		int f_height = figure.length;
		int[][] base_shape = convert_shape(figure, f_height, f_width);

		if (System.currentTimeMillis() - total_time >= 19000) {
			// TODO:
			find_yobun = false;
		}


		for (int h = 0 ; h < 1 ; h++) {
		for (int v = 0 ; v < 1 ; v++) {
		for (int x = 0 ; x < 4 ; x++) {
			int[][] shape = clone2(base_shape);
			for (int t = 0 ; t < x ; t++) {
				shape = rotate_shape(shape);
			}
			if (h == 1) {
				shape = hmirror_shape(shape);
			}
			if (v == 1) {
				shape = vmirror_shape(shape);
			}
			f_width = shape[0].length;
			f_height = shape.length;


			for (int i = 0 ; i <= height - f_height ; i++) {
				for (int j = 0 ; j <= width - f_width ; j++) {
					boolean found = true;
					check:
					for (int k = 0 ; k < f_height ; k++) {
						for (int l = 0 ; l < f_width ; l++) {
							if (shape[k][l] == 1 && field[i+k][j+l] != 0) {
								found = false;
								break check;
							}
						}
					}

					if (found) {
						int total_yobun = 0;
						if (find_yobun) {
							int kari_field[][] = clone2(field);
							for (int k = 0 ; k < f_height ; k++) {
								for (int l = 0 ; l < f_width ; l++) {
									kari_field[i+k][j+l] += shape[k][l];
								}
							}
							int from_x = (j == 0) ? 0 : j - 1, to_x = (j == width - f_width) ? width - 1 : j + f_width;
							int from_y = (i == 0) ? 0 : i - 1, to_y = (i == height - f_height) ? height - 1 : i + f_height;

							total_yobun = yobun(kari_field, from_x, to_x, from_y, to_y);
						}

						int allow = strict ? 0 : allow_yobun[KK];
						if (total_yobun <= allow) {
							for (int k = 0 ; k < f_height ; k++) {
								for (int l = 0 ; l < f_width ; l++) {
									field[i+k][j+l] += shape[k][l];
								}
							}
							field_left--;
							if (shapearr_index >= 0 && KK <= 8) {
								field_shape_left[shapearr_index]--;
								if (field_shape_left[shapearr_index] <= 0) {
									field_shape_left[shapearr_index] = 1;
								}
							}

							return new int[]{x, v, h, i, j};
						}
					}
				}
			}
		}
		}
		}

		if (strict_yobun) {
			strict_yobun = false;
		} else {
			if (threshold <= min_threshold - 5) {
				allow_yobun[KK] += 1;
			}
			if (threshold <= min_threshold - 25) {
				find_yobun = false;
			}
		}

		return new int[0];
	}
	public int[] processOrder(String[] figure, int income) {
		nth_order++;
		threshold = (1 - ((double)field_left / (order_count - nth_order))) * 100;

		String shape = "";
		for (String line : figure) {
			shape += (line + "/");
		}

		int shapearr_index = 0;
		boolean found = false;
		shape_search:
		for (String[] shapearr : shape_list) {
			for (String s : shapearr) {
				if (s.equals(shape)) {
					found = true;
					break shape_search;
				}
			}
			shapearr_index++;
		}

		if (first_search) {
			weight[shapearr_index]++;
			if (nth_order * 50 / order_count >= KK) {
//				for (int i = 0 ; i < shape_list.length ; i++) {
//					System.err.print(weight[i] + "/");
//				}
//				System.err.println();

				estimate_usage();
				first_search = false;
				return new int[0];
			}
			if (income < threshold) {
				return new int[0];
			}
			return goriosi(figure, -1, true);
		}

		if (KK >= 10) {
			if (income < threshold) {
				return new int[0];
			}
		} else {
//			int total_weight = 0;
//			for (int w : weight) {
//				total_weight += w;
//			}
//			int left_order = (order_count - nth_order) * weight[shapearr_index] / total_weight;
//			threshold = (1 - ((double)field_shape_left[shapearr_index] / left_order)) * 100;

			if (income < threshold) {
				return new int[0];
			}
		}

		if (mode) {
			return goriosi(figure, shapearr_index, strict_yobun);
		}

		String to_shape = "";
		for (String s : shape_list[shapearr_index]) {
			int idx = best_use_shape.indexOf(s);
			if (idx != -1) {
		 		int to_srm = 0;
		 		int to_shm = 0;
		 		int to_svm = 0;

				boolean found_shape = false;

				find_shape:
				for (int h = 0 ; h <= 1 ; h++) {
					for (int v = 0 ; v <= 1 ; v++) {
						for (int x = 0 ; x < 4 ; x++) {
							String c_shape = shape;
							for (int t = 0 ; t < x ; t++) {
								c_shape = rotate_string(c_shape);
							}
							if (v == 1) {
								c_shape = vmirror_string(c_shape);
							}
							if (h == 1) {
								c_shape = hmirror_string(c_shape);
							}
							if (s.equals(c_shape)) {
								to_shape = s;
								found_shape = true;
								to_srm = x;
								to_svm = v;
								to_shm = h;
								break find_shape;
							}
						}
					}
				}
				if (!found_shape) {
					continue;
				}

				int x = best_use_shape_x.get(idx);
		 		int y = best_use_shape_y.get(idx);
		 		best_use_shape.remove(idx);
		 		best_use_shape_x.remove(idx);
		 		best_use_shape_y.remove(idx);

		 		figure = to_shape.split("/");
				int f_width = figure[0].length();
				int f_height = figure.length;
				int[][] fig = convert_shape(figure, f_height, f_width);
				for (int k = 0 ; k < f_height ; k++) {
					for (int l = 0 ; l < f_width ; l++) {
						field[y+k][x+l] += fig[k][l];
					}
				}
		 		field_left--;
				if (shapearr_index >= 0 && KK <= 8) {
					field_shape_left[shapearr_index]--;
					if (field_shape_left[shapearr_index] <= 0) {
						field_shape_left[shapearr_index] = 1;
					}
				}

		 		return new int[] {to_srm, to_shm, to_svm, y, x};
			}
		}

		if (threshold <= min_threshold) {
			System.err.println("goriosi.");
			mode = true;
		}
		return new int[0];
	}

	public int[][] convert_shape(String[] figure, int h, int w) {
		int[][] shape = new int[h][w];
		for (int i = 0 ; i < h ; i++) {
			for (int j = 0 ; j < w ; j++) {
				char x = figure[i].charAt(j);
				switch (x) {
					case 'X':
						shape[i][j] = 1;
						break;
				}
			}
		}
		return shape;
	}

	public int yobun(int[][] field, int from_x, int to_x, int from_y, int to_y) {
		int total_yobun = 0;
		search_yobun:
		for (int y = from_y ; y <= to_y ; y++) {
			for (int ax = from_x ; ax <= to_x ; ax++) {
				if (field[y][ax] == 0) {
					try {
						int yobun = count_yobun(field, y, ax, 0);
						if (yobun != 0) {
							total_yobun += yobun;
						}
					} catch (Exception e) {
						field[y][ax] = 0;
						try {
							count_yobun(field, y, ax, 0);
						} catch (Exception e1) {
						}
					}
				}
			}
		}
		return total_yobun;
	}

	public int count_yobun(int[][] field, int y, int x, int count) throws Exception {
		if (x < 0 || x >= width) return count;
		if (y < 0 || y >= height) return count;
		if (field[y][x] != 0) return count;
		field[y][x] = -1;

		count =
			count_yobun(field, y+1, x, count) +
			count_yobun(field, y-1, x, count) +
			count_yobun(field, y, x+1, count) +
			count_yobun(field, y, x-1, count) + 1;
		if (count >= KK) {
			throw new Exception();
		}

		return count;
	}

	public int[][] clone2(int[][] a) {
		int height = a.length;
		int width = a[0].length;
		int[][] b = new int[height][width];
		for (int i = 0 ; i < height ; i++) {
			b[i] = a[i].clone();
		}
		return b;
	}

	public String rotate_string(String shape) {
		int[][] array = convert_array(shape);
		array = rotate_shape(array);
		return convert_string(array);
	}

	public int[][] rotate_shape(int[][] shape) {
		return rotate_shape(shape, shape.length, shape[0].length);
	}

	public int[][] rotate_shape(int[][] shape, int h, int w) {
		int[][] new_shape = new int[w][h];
		for (int i = 0 ; i < h ; i++) {
			for (int j = 0 ; j < w ; j++) {
				new_shape[j][h-1-i] = shape[i][j];
			}
		}
		return new_shape;
	}

	public String hmirror_string(String shape) {
		int[][] array = convert_array(shape);
		array = hmirror_shape(array);
		return convert_string(array);
	}

	public int[][] hmirror_shape(int[][] shape) {
		return hmirror_shape(shape, shape.length, shape[0].length);
	}

	public int[][] hmirror_shape(int[][] shape, int h, int w) {
		int[][] new_shape = new int[h][w];
		for (int i = 0 ; i < h ; i++) {
			for (int j = 0 ; j < w ; j++) {
				new_shape[i][w-1-j] = shape[i][j];
			}
		}
		return new_shape;
	}

	public String vmirror_string(String shape) {
		int[][] array = convert_array(shape);
		array = hmirror_shape(array);
		return convert_string(array);
	}

	public static int[][] vmirror_shape(int[][] shape) {
		return vmirror_shape(shape, shape.length, shape[0].length);
	}

	public static int[][] vmirror_shape(int[][] shape, int h, int w) {
		int[][] new_shape = new int[h][w];
		for (int i = 0 ; i < h ; i++) {
			for (int j = 0 ; j < w ; j++) {
				new_shape[h-1-i][j] = shape[i][j];
			}
		}
		return new_shape;
	}

	public int[][] convert_array(String shape) {
		String[] arr = shape.split("/");
		int width = arr[0].length();
		int height = arr.length;
		int[][] array = new int[height][width];
		for (int i = 0 ; i < height ; i++) {
			for (int j = 0 ; j < width ; j++) {
				if (arr[i].charAt(j) == 'X') {
					array[i][j] = 1;
				}
			}
		}
		return array;
	}

	public String convert_string(int[][] field) {
		int height = field.length;
		int width = field[0].length;

		int minx = width, maxx = 0, miny = height, maxy = 0;
		for (int i = 0 ; i < height ; i++) {
			for (int j = 0 ; j < width ; j++) {
				if (field[i][j] == 1) {
					if (i < miny) {
						miny = i;
					}
					if (maxy < i) {
						maxy = i;
					}
					if (j < minx) {
						minx = j;
					}
					if (maxx < j) {
						maxx = j;
					}
				}
			}
		}

		String result = "";
		for (int i = miny ; i <= maxy ; i++) {
			for (int j = minx ; j <= maxx ; j++) {
				if (field[i][j] == 1) {
					result += "X";
				} else {
					result += ".";
				}
			}
			result += "/";
		}
		return result;
	}

	// -----------//
	// ForTest    //
	// -----------//
	public static void main(String[] args) {
		BufferedReader in;
		BufferedWriter out;
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(System.out));
		try {
			CuttingFigures cut = new CuttingFigures();

			int H = Integer.valueOf(in.readLine());
			String[] plate = new String[H];
			for (int i = 0 ; i < H ; i++) {
				plate[i] = in.readLine();
			}
			int K = Integer.valueOf(in.readLine());
			int orderCnt = Integer.valueOf(in.readLine());

			long start_time = System.currentTimeMillis();

			cut.init(plate, K, orderCnt);

			for (int orderId = 0 ; orderId < orderCnt ; orderId++) {
				int fH = Integer.valueOf(in.readLine());
				String[] figure = new String[fH];
				for (int i = 0 ; i < fH ; i++) {
					figure[i] = in.readLine();
				}
				int income = Integer.valueOf(in.readLine());

				int[] ret = cut.processOrder(figure, income);

				out.write(ret.length + "\n");
				for (int i = 0 ; i < ret.length ; i++) {
					out.write(ret[i] + "\n");
				}
				out.flush();
			}


			System.err.println("elapsed time / " + (System.currentTimeMillis() - start_time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[][] setup3() {
		String[][] shape = {
				{"XXX/","X/X/X/"},
				{"X./XX/","XX/X./","XX/.X/",".X/XX/"},
		};
		return shape;
	}
	public String[][] setup4() {
		String[][] shape = {
			{"XXXX/","X/X/X/X/"},
			{"X../XXX/","XX/X./X./","XXX/..X/",".X/.X/XX/","..X/XXX/","X./X./XX/","XXX/X../","XX/.X/.X/"},
			{".X./XXX/","X./XX/X./","XXX/.X./",".X/XX/.X/"},
			{"XX./.XX/",".X/XX/X./",".XX/XX./","X./XX/.X/"},
			{"XX/XX/"},
		};
		return shape;
	}
	public String[][] setup5() {
		String[][] shape = {
			{"XXXXX/","X/X/X/X/X/"},
			{"X.../XXXX/","XX/X./X./X./","XXXX/...X/",".X/.X/.X/XX/","...X/XXXX/","X./X./X./XX/","XXXX/X.../","XX/.X/.X/.X/"},
			{".X../XXXX/","X./XX/X./X./","XXXX/..X./",".X/.X/XX/.X/","..X./XXXX/","X./X./XX/X./","XXXX/.X../",".X/XX/.X/.X/"},
			{"XX../.XXX/",".X/XX/X./X./","XXX./..XX/",".X/.X/XX/X./","..XX/XXX./","X./X./XX/.X/",".XXX/XX../","X./XX/.X/.X/"},
			{"XX./XXX/","XX/XX/X./","XXX/.XX/",".X/XX/XX/",".XX/XXX/","X./XX/XX/","XXX/XX./","XX/XX/.X/"},
			{"X../X../XXX/","XXX/X../X../","XXX/..X/..X/","..X/..X/XXX/"},
			{"X../XXX/X../","XXX/.X./.X./","..X/XXX/..X/",".X./.X./XXX/"},
			{"X../XXX/.X./",".XX/XX./.X./",".X./XXX/..X/",".X./.XX/XX./","..X/XXX/.X./",".X./XX./.XX/",".X./XXX/X../","XX./.XX/.X./"},
			{"X.X/XXX/","XX/X./XX/","XXX/X.X/","XX/.X/XX/"},
			{"X../XXX/..X/",".XX/.X./XX./","..X/XXX/X../","XX./.X./.XX/"},
			{".X./XXX/.X./"},
			{"X../XX./.XX/",".XX/XX./X../","XX./.XX/..X/","..X/.XX/XX./"},
		};
		return shape;
	}
	public String[][] setup6() {
		String[][] shape = {
			{"XXXXXX/","X/X/X/X/X/X/"},
			{"X..../XXXXX/","XX/X./X./X./X./","XXXXX/....X/",".X/.X/.X/.X/XX/","....X/XXXXX/","X./X./X./X./XX/","XXXXX/X..../","XX/.X/.X/.X/.X/"},
			{".X.../XXXXX/","X./XX/X./X./X./","XXXXX/...X./",".X/.X/.X/XX/.X/","...X./XXXXX/","X./X./X./XX/X./","XXXXX/.X.../",".X/XX/.X/.X/.X/"},
			{"..X../XXXXX/","X./X./XX/X./X./","XXXXX/..X../",".X/.X/XX/.X/.X/"},
			{"XX.../.XXXX/",".X/XX/X./X./X./","XXXX./...XX/",".X/.X/.X/XX/X./","...XX/XXXX./","X./X./X./XX/.X/",".XXXX/XX.../","X./XX/.X/.X/.X/"},
			{"XX../XXXX/","XX/XX/X./X./","XXXX/..XX/",".X/.X/XX/XX/","..XX/XXXX/","X./X./XX/XX/","XXXX/XX../","XX/XX/.X/.X/"},
			{"X.../X.../XXXX/","XXX/X../X../X../","XXXX/...X/...X/","..X/..X/..X/XXX/","...X/...X/XXXX/","X../X../X../XXX/","XXXX/X.../X.../","XXX/..X/..X/..X/"},
			{"X.../XXXX/X.../","XXX/.X./.X./.X./","...X/XXXX/...X/",".X./.X./.X./XXX/"},
			{"X.../XXXX/.X../",".XX/XX./.X./.X./","..X./XXXX/...X/",".X./.X./.XX/XX./","...X/XXXX/..X./",".X./.X./XX./.XX/",".X../XXXX/X.../","XX./.XX/.X./.X./"},
			{"X.X./XXXX/","XX/X./XX/X./","XXXX/.X.X/",".X/XX/.X/XX/",".X.X/XXXX/","X./XX/X./XX/","XXXX/X.X./","XX/.X/XX/.X/"},
			{"X.../XXXX/..X./",".XX/.X./XX./.X./",".X../XXXX/...X/",".X./.XX/.X./XX./","...X/XXXX/.X../",".X./XX./.X./.XX/","..X./XXXX/X.../","XX./.X./.XX/.X./"},
			{"X..X/XXXX/","XX/X./X./XX/","XXXX/X..X/","XX/.X/.X/XX/"},
			{"X.../XXXX/...X/",".XX/.X./.X./XX./","...X/XXXX/X.../","XX./.X./.X./.XX/"},
			{".XX./XXXX/","X./XX/XX/X./","XXXX/.XX./",".X/XX/XX/.X/"},
			{".X../.X../XXXX/","X../XXX/X../X../","XXXX/..X./..X./","..X/..X/XXX/..X/","..X./..X./XXXX/","X../X../XXX/X../","XXXX/.X../.X../","..X/XXX/..X/..X/"},
			{".X../XXXX/.X../",".X./XXX/.X./.X./","..X./XXXX/..X./",".X./.X./XXX/.X./"},
			{".X../XXXX/..X./",".X./.XX/XX./.X./","..X./XXXX/.X../",".X./XX./.XX/.X./"},
			{"XXX../..XXX/",".X/.X/XX/X./X./","..XXX/XXX../","X./X./XX/.X/.X/"},
			{"X.../XX../.XXX/",".XX/XX./X../X../","XXX./..XX/...X/","..X/..X/.XX/XX./","...X/..XX/XXX./","X../X../XX./.XX/",".XXX/XX../X.../","XX./.XX/..X/..X/"},
			{"XXX./.XXX/",".X/XX/XX/X./",".XXX/XXX./","X./XX/XX/.X/"},
			{".X../XX../.XXX/",".X./XXX/X../X../","XXX./..XX/..X./","..X/..X/XXX/.X./","..X./..XX/XXX./","X../X../XXX/.X./",".XXX/XX../.X../",".X./XXX/..X/..X/"},
			{"XX../.XXX/.X../","..X/XXX/.X./.X./","..X./XXX./..XX/",".X./.X./XXX/X../","..XX/XXX./..X./",".X./.X./XXX/..X/",".X../.XXX/XX../","X../XXX/.X./.X./"},
			{"XX../.XXX/..X./","..X/.XX/XX./.X./",".X../XXX./..XX/",".X./.XX/XX./X../","..XX/XXX./.X../",".X./XX./.XX/..X/","..X./.XXX/XX../","X../XX./.XX/.X./"},
			{"XX.X/.XXX/",".X/XX/X./XX/","XXX./X.XX/","XX/.X/XX/X./","X.XX/XXX./","XX/X./XX/.X/",".XXX/XX.X/","X./XX/.X/XX/"},
			{"XX../.XXX/...X/","..X/.XX/.X./XX./","X.../XXX./..XX/",".XX/.X./XX./X../","..XX/XXX./X.../","XX./.X./.XX/..X/","...X/.XXX/XX../","X../XX./.X./.XX/"},
			{"X../XX./XXX/","XXX/XX./X../","XXX/.XX/..X/","..X/.XX/XXX/"},
			{"XXX/XXX/","XX/XX/XX/"},
			{".X./XX./XXX/","XX./XXX/X../","XXX/.XX/.X./","..X/XXX/.XX/",".X./.XX/XXX/","X../XXX/XX./","XXX/XX./.X./",".XX/XXX/..X/"},
			{"XX./XXX/.X./",".XX/XXX/.X./",".X./XXX/.XX/",".X./XXX/XX./"},
			{"XX./XXX/..X/",".XX/.XX/XX./","X../XXX/.XX/",".XX/XX./XX./",".XX/XXX/X../","XX./.XX/.XX/","..X/XXX/XX./","XX./XX./.XX/"},
			{"XX../.X../.XXX/","..X/XXX/X../X../","XXX./..X./..XX/","..X/..X/XXX/X../","..XX/..X./XXX./","X../X../XXX/..X/",".XXX/.X../XX../","X../XXX/..X/..X/"},
			{"XX./X../XXX/","XXX/X.X/X../","XXX/..X/.XX/","..X/X.X/XXX/",".XX/..X/XXX/","X../X.X/XXX/","XXX/X../XX./","XXX/X.X/..X/"},
			{"X.X/XXX/X../","XXX/.X./.XX/","..X/XXX/X.X/","XX./.X./XXX/","X.X/XXX/..X/",".XX/.X./XXX/","X../XXX/X.X/","XXX/.X./XX./"},
			{"X.X/XXX/.X./",".XX/XX./.XX/",".X./XXX/X.X/","XX./.XX/XX./"},
			{"XX../.XX./..XX/","..X/.XX/XX./X../","..XX/.XX./XX../","X../XX./.XX/..X/"},
		};
		return shape;
	}
	public String[][] setup7() {
		String[][] shape = {
			{"XXXXXXX/","X/X/X/X/X/X/X/"},
			{"X...../XXXXXX/","XX/X./X./X./X./X./","XXXXXX/.....X/",".X/.X/.X/.X/.X/XX/",".....X/XXXXXX/","X./X./X./X./X./XX/","XXXXXX/X...../","XX/.X/.X/.X/.X/.X/"},
			{".X..../XXXXXX/","X./XX/X./X./X./X./","XXXXXX/....X./",".X/.X/.X/.X/XX/.X/","....X./XXXXXX/","X./X./X./X./XX/X./","XXXXXX/.X..../",".X/XX/.X/.X/.X/.X/"},
			{"..X.../XXXXXX/","X./X./XX/X./X./X./","XXXXXX/...X../",".X/.X/.X/XX/.X/.X/","...X../XXXXXX/","X./X./X./XX/X./X./","XXXXXX/..X.../",".X/.X/XX/.X/.X/.X/"},
			{"XX..../.XXXXX/",".X/XX/X./X./X./X./","XXXXX./....XX/",".X/.X/.X/.X/XX/X./","....XX/XXXXX./","X./X./X./X./XX/.X/",".XXXXX/XX..../","X./XX/.X/.X/.X/.X/"},
			{"XX.../XXXXX/","XX/XX/X./X./X./","XXXXX/...XX/",".X/.X/.X/XX/XX/","...XX/XXXXX/","X./X./X./XX/XX/","XXXXX/XX.../","XX/XX/.X/.X/.X/"},
			{"X..../X..../XXXXX/","XXX/X../X../X../X../","XXXXX/....X/....X/","..X/..X/..X/..X/XXX/","....X/....X/XXXXX/","X../X../X../X../XXX/","XXXXX/X..../X..../","XXX/..X/..X/..X/..X/"},
			{"X..../XXXXX/X..../","XXX/.X./.X./.X./.X./","....X/XXXXX/....X/",".X./.X./.X./.X./XXX/"},
			{"X..../XXXXX/.X.../",".XX/XX./.X./.X./.X./","...X./XXXXX/....X/",".X./.X./.X./.XX/XX./","....X/XXXXX/...X./",".X./.X./.X./XX./.XX/",".X.../XXXXX/X..../","XX./.XX/.X./.X./.X./"},
			{"X.X../XXXXX/","XX/X./XX/X./X./","XXXXX/..X.X/",".X/.X/XX/.X/XX/","..X.X/XXXXX/","X./X./XX/X./XX/","XXXXX/X.X../","XX/.X/XX/.X/.X/"},
			{"X..../XXXXX/..X../",".XX/.X./XX./.X./.X./","..X../XXXXX/....X/",".X./.X./.XX/.X./XX./","....X/XXXXX/..X../",".X./.X./XX./.X./.XX/","..X../XXXXX/X..../","XX./.X./.XX/.X./.X./"},
			{"X..X./XXXXX/","XX/X./X./XX/X./","XXXXX/.X..X/",".X/XX/.X/.X/XX/",".X..X/XXXXX/","X./XX/X./X./XX/","XXXXX/X..X./","XX/.X/.X/XX/.X/"},
			{"X..../XXXXX/...X./",".XX/.X./.X./XX./.X./",".X.../XXXXX/....X/",".X./.XX/.X./.X./XX./","....X/XXXXX/.X.../",".X./XX./.X./.X./.XX/","...X./XXXXX/X..../","XX./.X./.X./.XX/.X./"},
			{"X...X/XXXXX/","XX/X./X./X./XX/","XXXXX/X...X/","XX/.X/.X/.X/XX/"},
			{"X..../XXXXX/....X/",".XX/.X./.X./.X./XX./","....X/XXXXX/X..../","XX./.X./.X./.X./.XX/"},
			{".XX../XXXXX/","X./XX/XX/X./X./","XXXXX/..XX./",".X/.X/XX/XX/.X/","..XX./XXXXX/","X./X./XX/XX/X./","XXXXX/.XX../",".X/XX/XX/.X/.X/"},
			{".X.../.X.../XXXXX/","X../XXX/X../X../X../","XXXXX/...X./...X./","..X/..X/..X/XXX/..X/","...X./...X./XXXXX/","X../X../X../XXX/X../","XXXXX/.X.../.X.../","..X/XXX/..X/..X/..X/"},
			{".X.../XXXXX/.X.../",".X./XXX/.X./.X./.X./","...X./XXXXX/...X./",".X./.X./.X./XXX/.X./"},
			{".X.../XXXXX/..X../",".X./.XX/XX./.X./.X./","..X../XXXXX/...X./",".X./.X./.XX/XX./.X./","...X./XXXXX/..X../",".X./.X./XX./.XX/.X./","..X../XXXXX/.X.../",".X./XX./.XX/.X./.X./"},
			{".X.X./XXXXX/","X./XX/X./XX/X./","XXXXX/.X.X./",".X/XX/.X/XX/.X/"},
			{".X.../XXXXX/...X./",".X./.XX/.X./XX./.X./","...X./XXXXX/.X.../",".X./XX./.X./.XX/.X./"},
			{"..X../..X../XXXXX/","X../X../XXX/X../X../","XXXXX/..X../..X../","..X/..X/XXX/..X/..X/"},
			{"..X../XXXXX/..X../",".X./.X./XXX/.X./.X./"},
			{"XXX.../..XXXX/",".X/.X/XX/X./X./X./","XXXX../...XXX/",".X/.X/.X/XX/X./X./","...XXX/XXXX../","X./X./X./XX/.X/.X/","..XXXX/XXX.../","X./X./XX/.X/.X/.X/"},
			{"X..../XX.../.XXXX/",".XX/XX./X../X../X../","XXXX./...XX/....X/","..X/..X/..X/.XX/XX./","....X/...XX/XXXX./","X../X../X../XX./.XX/",".XXXX/XX.../X..../","XX./.XX/..X/..X/..X/"},
			{"XXX../.XXXX/",".X/XX/XX/X./X./","XXXX./..XXX/",".X/.X/XX/XX/X./","..XXX/XXXX./","X./X./XX/XX/.X/",".XXXX/XXX../","X./XX/XX/.X/.X/"},
			{".X.../XX.../.XXXX/",".X./XXX/X../X../X../","XXXX./...XX/...X./","..X/..X/..X/XXX/.X./","...X./...XX/XXXX./","X../X../X../XXX/.X./",".XXXX/XX.../.X.../",".X./XXX/..X/..X/..X/"},
			{"XX.../.XXXX/.X.../","..X/XXX/.X./.X./.X./","...X./XXXX./...XX/",".X./.X./.X./XXX/X../","...XX/XXXX./...X./",".X./.X./.X./XXX/..X/",".X.../.XXXX/XX.../","X../XXX/.X./.X./.X./"},
			{"XX.../.XXXX/..X../","..X/.XX/XX./.X./.X./","..X../XXXX./...XX/",".X./.X./.XX/XX./X../","...XX/XXXX./..X../",".X./.X./XX./.XX/..X/","..X../.XXXX/XX.../","X../XX./.XX/.X./.X./"},
			{"XX.X./.XXXX/",".X/XX/X./XX/X./","XXXX./.X.XX/",".X/XX/.X/XX/X./",".X.XX/XXXX./","X./XX/X./XX/.X/",".XXXX/XX.X./","X./XX/.X/XX/.X/"},
			{"XX.../.XXXX/...X./","..X/.XX/.X./XX./.X./",".X.../XXXX./...XX/",".X./.XX/.X./XX./X../","...XX/XXXX./.X.../",".X./XX./.X./.XX/..X/","...X./.XXXX/XX.../","X../XX./.X./.XX/.X./"},
			{"XX..X/.XXXX/",".X/XX/X./X./XX/","XXXX./X..XX/","XX/.X/.X/XX/X./","X..XX/XXXX./","XX/X./X./XX/.X/",".XXXX/XX..X/","X./XX/.X/.X/XX/"},
			{"XX.../.XXXX/....X/","..X/.XX/.X./.X./XX./","X..../XXXX./...XX/",".XX/.X./.X./XX./X../","...XX/XXXX./X..../","XX./.X./.X./.XX/..X/","....X/.XXXX/XX.../","X../XX./.X./.X./.XX/"},
			{"X.../XX../XXXX/","XXX/XX./X../X../","XXXX/..XX/...X/","..X/..X/.XX/XXX/","...X/..XX/XXXX/","X../X../XX./XXX/","XXXX/XX../X.../","XXX/.XX/..X/..X/"},
			{"XXX./XXXX/","XX/XX/XX/X./","XXXX/.XXX/",".X/XX/XX/XX/",".XXX/XXXX/","X./XX/XX/XX/","XXXX/XXX./","XX/XX/XX/.X/"},
			{".X../XX../XXXX/","XX./XXX/X../X../","XXXX/..XX/..X./","..X/..X/XXX/.XX/","..X./..XX/XXXX/","X../X../XXX/XX./","XXXX/XX../.X../",".XX/XXX/..X/..X/"},
			{"XX../XXXX/X.../","XXX/.XX/.X./.X./","...X/XXXX/..XX/",".X./.X./XX./XXX/","..XX/XXXX/...X/",".X./.X./.XX/XXX/","X.../XXXX/XX../","XXX/XX./.X./.X./"},
			{"XX../XXXX/.X../",".XX/XXX/.X./.X./","..X./XXXX/..XX/",".X./.X./XXX/XX./","..XX/XXXX/..X./",".X./.X./XXX/.XX/",".X../XXXX/XX../","XX./XXX/.X./.X./"},
			{"XX../XXXX/..X./",".XX/.XX/XX./.X./",".X../XXXX/..XX/",".X./.XX/XX./XX./","..XX/XXXX/.X../",".X./XX./.XX/.XX/","..X./XXXX/XX../","XX./XX./.XX/.X./"},
			{"XX.X/XXXX/","XX/XX/X./XX/","XXXX/X.XX/","XX/.X/XX/XX/","X.XX/XXXX/","XX/X./XX/XX/","XXXX/XX.X/","XX/XX/.X/XX/"},
			{"XX../XXXX/...X/",".XX/.XX/.X./XX./","X.../XXXX/..XX/",".XX/.X./XX./XX./","..XX/XXXX/X.../","XX./.X./.XX/.XX/","...X/XXXX/XX../","XX./XX./.X./.XX/"},
			{"XX.../.X.../.XXXX/","..X/XXX/X../X../X../","XXXX./...X./...XX/","..X/..X/..X/XXX/X../","...XX/...X./XXXX./","X../X../X../XXX/..X/",".XXXX/.X.../XX.../","X../XXX/..X/..X/..X/"},
			{"XX../X.../XXXX/","XXX/X.X/X../X../","XXXX/...X/..XX/","..X/..X/X.X/XXX/","..XX/...X/XXXX/","X../X../X.X/XXX/","XXXX/X.../XX../","XXX/X.X/..X/..X/"},
			{"X.../X.../X.../XXXX/","XXXX/X.../X.../X.../","XXXX/...X/...X/...X/","...X/...X/...X/XXXX/"},
			{"X.../X.../XXXX/X.../","XXXX/.X../.X../.X../","...X/XXXX/...X/...X/","..X./..X./..X./XXXX/","...X/...X/XXXX/...X/",".X../.X../.X../XXXX/","X.../XXXX/X.../X.../","XXXX/..X./..X./..X./"},
			{"X.../X.../XXXX/.X../",".XXX/XX../.X../.X../","..X./XXXX/...X/...X/","..X./..X./..XX/XXX./","...X/...X/XXXX/..X./",".X../.X../XX../.XXX/",".X../XXXX/X.../X.../","XXX./..XX/..X./..X./"},
			{"X.../X.X./XXXX/","XXX/X../XX./X../","XXXX/.X.X/...X/","..X/.XX/..X/XXX/","...X/.X.X/XXXX/","X../XX./X../XXX/","XXXX/X.X./X.../","XXX/..X/.XX/..X/"},
			{"X.../X.../XXXX/..X./",".XXX/.X../XX../.X../",".X../XXXX/...X/...X/","..X./..XX/..X./XXX./","...X/...X/XXXX/.X../",".X../XX../.X../.XXX/","..X./XXXX/X.../X.../","XXX./..X./..XX/..X./"},
			{"X.../X..X/XXXX/","XXX/X../X../XX./","XXXX/X..X/...X/",".XX/..X/..X/XXX/","...X/X..X/XXXX/","XX./X../X../XXX/","XXXX/X..X/X.../","XXX/..X/..X/.XX/"},
			{"X.../X.../XXXX/...X/",".XXX/.X../.X../XX../","X.../XXXX/...X/...X/","..XX/..X./..X./XXX./","...X/...X/XXXX/X.../","XX../.X../.X../.XXX/","...X/XXXX/X.../X.../","XXX./..X./..X./..XX/"},
			{"X.X./XXXX/X.../","XXX/.X./.XX/.X./","...X/XXXX/.X.X/",".X./XX./.X./XXX/",".X.X/XXXX/...X/",".X./.XX/.X./XXX/","X.../XXXX/X.X./","XXX/.X./XX./.X./"},
			{"X..X/XXXX/X.../","XXX/.X./.X./.XX/","...X/XXXX/X..X/","XX./.X./.X./XXX/","X..X/XXXX/...X/",".XX/.X./.X./XXX/","X.../XXXX/X..X/","XXX/.X./.X./XX./"},
			{"X.X./XXXX/.X../",".XX/XX./.XX/.X./","..X./XXXX/.X.X/",".X./XX./.XX/XX./",".X.X/XXXX/..X./",".X./.XX/XX./.XX/",".X../XXXX/X.X./","XX./.XX/XX./.X./"},
			{"X.../XXXX/.XX./",".XX/XX./XX./.X./",".XX./XXXX/...X/",".X./.XX/.XX/XX./","...X/XXXX/.XX./",".X./XX./XX./.XX/",".XX./XXXX/X.../","XX./.XX/.XX/.X./"},
			{"X..X/XXXX/.X../",".XX/XX./.X./.XX/","..X./XXXX/X..X/","XX./.X./.XX/XX./","X..X/XXXX/..X./",".XX/.X./XX./.XX/",".X../XXXX/X..X/","XX./.XX/.X./XX./"},
			{"X.../XXXX/.X.X/",".XX/XX./.X./XX./","X.X./XXXX/...X/",".XX/.X./.XX/XX./","...X/XXXX/X.X./","XX./.X./XX./.XX/",".X.X/XXXX/X.../","XX./.XX/.X./.XX/"},
			{"X.../XXXX/.X../.X../","..XX/XXX./..X./..X./","..X./..X./XXXX/...X/",".X../.X../.XXX/XX../","...X/XXXX/..X./..X./","..X./..X./XXX./..XX/",".X../.X../XXXX/X.../","XX../.XXX/.X../.X../"},
			{"..X./X.X./XXXX/","XX./X../XXX/X../","XXXX/.X.X/.X../","..X/XXX/..X/.XX/",".X../.X.X/XXXX/","X../XXX/X../XX./","XXXX/X.X./..X./",".XX/..X/XXX/..X/"},
			{"X.X./XXXX/..X./",".XX/.X./XXX/.X./",".X../XXXX/.X.X/",".X./XXX/.X./XX./",".X.X/XXXX/.X../",".X./XXX/.X./.XX/","..X./XXXX/X.X./","XX./.X./XXX/.X./"},
			{"X.../XXXX/..X./..X./","..XX/..X./XXX./..X./",".X../.X../XXXX/...X/",".X../.XXX/.X../XX../","...X/XXXX/.X../.X../","..X./XXX./..X./..XX/","..X./..X./XXXX/X.../","XX../.X../.XXX/.X../"},
			{".X../.XX./XXXX/","X../XXX/XX./X../","XXXX/.XX./..X./","..X/.XX/XXX/..X/","..X./.XX./XXXX/","X../XX./XXX/X../","XXXX/.XX./.X../","..X/XXX/.XX/..X/"},
			{".XX./XXXX/.X../",".X./XXX/.XX/.X./","..X./XXXX/.XX./",".X./XX./XXX/.X./",".XX./XXXX/..X./",".X./.XX/XXX/.X./",".X../XXXX/.XX./",".X./XXX/XX./.X./"},
			{"XX../.X../XXXX/","X.X/XXX/X../X../","XXXX/..X./..XX/","..X/..X/XXX/X.X/","..XX/..X./XXXX/","X../X../XXX/X.X/","XXXX/.X../XX../","X.X/XXX/..X/..X/"},
			{".XX./.X../XXXX/","X../XXX/X.X/X../","XXXX/..X./.XX./","..X/X.X/XXX/..X/",".XX./..X./XXXX/","X../X.X/XXX/X../","XXXX/.X../.XX./","..X/XXX/X.X/..X/"},
			{".X../.X../XXXX/.X../",".X../XXXX/.X../.X../","..X./XXXX/..X./..X./","..X./..X./XXXX/..X./"},
			{".X../.X../XXXX/..X./",".X../.XXX/XX../.X../",".X../XXXX/..X./..X./","..X./..XX/XXX./..X./","..X./..X./XXXX/.X../",".X../XX../.XXX/.X../","..X./XXXX/.X../.X../","..X./XXX./..XX/..X./"},
			{"X..../XXX../..XXX/",".XX/.X./XX./X../X../","XXX../..XXX/....X/","..X/..X/.XX/.X./XX./","....X/..XXX/XXX../","X../X../XX./.X./.XX/","..XXX/XXX../X..../","XX./.X./.XX/..X/..X/"},
			{"XXX../X.XXX/","XX/.X/XX/X./X./","XXX.X/..XXX/",".X/.X/XX/X./XX/","..XXX/XXX.X/","X./X./XX/.X/XX/","X.XXX/XXX../","XX/X./XX/.X/.X/"},
			{".X.../XXX../..XXX/",".X./.XX/XX./X../X../","XXX../..XXX/...X./","..X/..X/.XX/XX./.X./","...X./..XXX/XXX../","X../X../XX./.XX/.X./","..XXX/XXX../.X.../",".X./XX./.XX/..X/..X/"},
			{"..X../XXX../..XXX/",".X./.X./XXX/X../X../","XXX../..XXX/..X../","..X/..X/XXX/.X./.X./","..X../..XXX/XXX../","X../X../XXX/.X./.X./","..XXX/XXX../..X../",".X./.X./XXX/..X/..X/"},
			{"XX.../.XX../..XXX/","..X/.XX/XX./X../X../","XXX../..XX./...XX/","..X/..X/.XX/XX./X../","...XX/..XX./XXX../","X../X../XX./.XX/..X/","..XXX/.XX../XX.../","X../XX./.XX/..X/..X/"},
			{"XX../XX../.XXX/",".XX/XXX/X../X../","XXX./..XX/..XX/","..X/..X/XXX/XX./","..XX/..XX/XXX./","X../X../XXX/.XX/",".XXX/XX../XX../","XX./XXX/..X/..X/"},
			{"X.../X.../XX../.XXX/",".XXX/XX../X.../X.../","XXX./..XX/...X/...X/","...X/...X/..XX/XXX./"},
			{"X.../XXX./.XXX/",".XX/XX./XX./X../","XXX./.XXX/...X/","..X/.XX/.XX/XX./","...X/.XXX/XXX./","X../XX./XX./.XX/",".XXX/XXX./X.../","XX./.XX/.XX/..X/"},
			{"X.../XX../.XXX/.X../","..XX/XXX./.X../.X../","..X./XXX./..XX/...X/","..X./..X./.XXX/XX../","...X/..XX/XXX./..X./",".X../.X../XXX./..XX/",".X../.XXX/XX../X.../","XX../.XXX/..X./..X./"},
			{"X.../XX../.XXX/..X./","..XX/.XX./XX../.X../",".X../XXX./..XX/...X/","..X./..XX/.XX./XX../","...X/..XX/XXX./.X../",".X../XX../.XX./..XX/","..X./.XXX/XX../X.../","XX../.XX./..XX/..X./"},
			{"X.../XX.X/.XXX/",".XX/XX./X../XX./","XXX./X.XX/...X/",".XX/..X/.XX/XX./","...X/X.XX/XXX./","XX./X../XX./.XX/",".XXX/XX.X/X.../","XX./.XX/..X/.XX/"},
			{"X.../XX../.XXX/...X/","..XX/.XX./.X../XX../","X.../XXX./..XX/...X/","..XX/..X./.XX./XX../","...X/..XX/XXX./X.../","XX../.X../.XX./..XX/","...X/.XXX/XX../X.../","XX../.XX./..X./..XX/"},
			{".X../XXX./.XXX/",".X./XXX/XX./X../","XXX./.XXX/..X./","..X/.XX/XXX/.X./","..X./.XXX/XXX./","X../XX./XXX/.X./",".XXX/XXX./.X../",".X./XXX/.XX/..X/"},
			{"..X./XXX./.XXX/",".X./XX./XXX/X../","XXX./.XXX/.X../","..X/XXX/.XX/.X./",".X../.XXX/XXX./","X../XXX/XX./.X./",".XXX/XXX./..X./",".X./.XX/XXX/..X/"},
			{".XX./XX../.XXX/",".X./XXX/X.X/X../","XXX./..XX/.XX./","..X/X.X/XXX/.X./",".XX./..XX/XXX./","X../X.X/XXX/.X./",".XXX/XX../.XX./",".X./XXX/X.X/..X/"},
			{".X../XX../.XXX/..X./","..X./.XXX/XX../.X../",".X../XXX./..XX/..X./","..X./..XX/XXX./.X../"},
			{".X../XX.X/.XXX/",".X./XXX/X../XX./","XXX./X.XX/..X./",".XX/..X/XXX/.X./","..X./X.XX/XXX./","XX./X../XXX/.X./",".XXX/XX.X/.X../",".X./XXX/..X/.XX/"},
			{".X../XX../.XXX/...X/","..X./.XXX/.X../XX../","X.../XXX./..XX/..X./","..XX/..X./XXX./.X../","..X./..XX/XXX./X.../","XX../.X../.XXX/..X./","...X/.XXX/XX../.X../",".X../XXX./..X./..XX/"},
			{"XX../.XXX/.XX./","..X/XXX/XX./.X./",".XX./XXX./..XX/",".X./.XX/XXX/X../","..XX/XXX./.XX./",".X./XX./XXX/..X/",".XX./.XXX/XX../","X../XXX/.XX/.X./"},
			{"XX.X/.XXX/.X../","..X/XXX/.X./.XX/","..X./XXX./X.XX/","XX./.X./XXX/X../","X.XX/XXX./..X./",".XX/.X./XXX/..X/",".X../.XXX/XX.X/","X../XXX/.X./XX./"},
			{"XX../.XXX/.X.X/","..X/XXX/.X./XX./","X.X./XXX./..XX/",".XX/.X./XXX/X../","..XX/XXX./X.X./","XX./.X./XXX/..X/",".X.X/.XXX/XX../","X../XXX/.X./.XX/"},
			{"XX../.XXX/XX../","X.X/XXX/.X./.X./","..XX/XXX./..XX/",".X./.X./XXX/X.X/"},
			{"XX.X/.XXX/..X./","..X/.XX/XX./.XX/",".X../XXX./X.XX/","XX./.XX/XX./X../","X.XX/XXX./.X../",".XX/XX./.XX/..X/","..X./.XXX/XX.X/","X../XX./.XX/XX./"},
			{"XX../.XXX/..XX/","..X/.XX/XX./XX./","XX../XXX./..XX/",".XX/.XX/XX./X../","..XX/XXX./XX../","XX./XX./.XX/..X/","..XX/.XXX/XX../","X../XX./.XX/.XX/"},
			{"XX.XX/.XXX./",".X/XX/X./XX/.X/",".XXX./XX.XX/","X./XX/.X/XX/X./"},
			{"...X/XX.X/.XXX/",".X./XX./X../XXX/","XXX./X.XX/X.../","XXX/..X/.XX/.X./","X.../X.XX/XXX./","XXX/X../XX./.X./",".XXX/XX.X/...X/",".X./.XX/..X/XXX/"},
			{"XX.X/.XXX/...X/","..X/.XX/.X./XXX/","X.../XXX./X.XX/","XXX/.X./XX./X../","X.XX/XXX./X.../","XXX/.X./.XX/..X/","...X/.XXX/XX.X/","X../XX./.X./XXX/"},
			{"XX.../.XXX./...XX/","..X/.XX/.X./XX./X../","...XX/.XXX./XX.../","X../XX./.X./.XX/..X/"},
			{"XX../.XXX/...X/...X/","...X/..XX/..X./XXX./","X.../X.../XXX./..XX/",".XXX/.X../XX../X.../","..XX/XXX./X.../X.../","XXX./..X./..XX/...X/","...X/...X/.XXX/XX../","X.../XX../.X../.XXX/"},
			{"XX../.XX./.XXX/","..X/XXX/XX./X../","XXX./.XX./..XX/","..X/.XX/XXX/X../","..XX/.XX./XXX./","X../XX./XXX/..X/",".XXX/.XX./XX../","X../XXX/.XX/..X/"},
			{"XX./XX./XXX/","XXX/XXX/X../","XXX/.XX/.XX/","..X/XXX/XXX/",".XX/.XX/XXX/","X../XXX/XXX/","XXX/XX./XX./","XXX/XXX/..X/"},
			{".X./XXX/XXX/","XX./XXX/XX./","XXX/XXX/.X./",".XX/XXX/.XX/"},
			{".XX/XX./XXX/","XX./XXX/X.X/","XXX/.XX/XX./","X.X/XXX/.XX/","XX./.XX/XXX/","X.X/XXX/XX./","XXX/XX./.XX/",".XX/XXX/X.X/"},
			{"XX./XXX/.XX/",".XX/XXX/XX./"},
			{"XXX../..X../..XXX/","..X/..X/XXX/X../X../","..XXX/..X../XXX../","X../X../XXX/..X/..X/"},
			{"XXX./.X../.XXX/","..X/XXX/X.X/X../","XXX./..X./.XXX/","..X/X.X/XXX/X../",".XXX/..X./XXX./","X../X.X/XXX/..X/",".XXX/.X../XXX./","X../XXX/X.X/..X/"},
			{"XX../.X.X/.XXX/","..X/XXX/X../XX./","XXX./X.X./..XX/",".XX/..X/XXX/X../","..XX/X.X./XXX./","XX./X../XXX/..X/",".XXX/.X.X/XX../","X../XXX/..X/.XX/"},
			{"XX../.X../.XXX/...X/","...X/.XXX/.X../XX../","X.../XXX./..X./..XX/","..XX/..X./XXX./X.../"},
			{"XXX/X../XXX/","XXX/X.X/X.X/","XXX/..X/XXX/","X.X/X.X/XXX/"},
			{"XX./X.X/XXX/","XXX/X.X/XX./","XXX/X.X/.XX/",".XX/X.X/XXX/"},
			{"X.X/XXX/X.X/","XXX/.X./XXX/"},
			{"X.../XX../.XX./..XX/","..XX/.XX./XX../X.../","XX../.XX./..XX/...X/","...X/..XX/.XX./XX../"},
		};
		return shape;
		}
	public String[][] setup8() {
		String[][] shape = {
			{"XXXXXXXX/","X/X/X/X/X/X/X/X/"},
			{"X....../XXXXXXX/","XX/X./X./X./X./X./X./","XXXXXXX/......X/",".X/.X/.X/.X/.X/.X/XX/","......X/XXXXXXX/","X./X./X./X./X./X./XX/","XXXXXXX/X....../","XX/.X/.X/.X/.X/.X/.X/"},
			{".X...../XXXXXXX/","X./XX/X./X./X./X./X./","XXXXXXX/.....X./",".X/.X/.X/.X/.X/XX/.X/",".....X./XXXXXXX/","X./X./X./X./X./XX/X./","XXXXXXX/.X...../",".X/XX/.X/.X/.X/.X/.X/"},
			{"..X..../XXXXXXX/","X./X./XX/X./X./X./X./","XXXXXXX/....X../",".X/.X/.X/.X/XX/.X/.X/","....X../XXXXXXX/","X./X./X./X./XX/X./X./","XXXXXXX/..X..../",".X/.X/XX/.X/.X/.X/.X/"},
			{"...X.../XXXXXXX/","X./X./X./XX/X./X./X./","XXXXXXX/...X.../",".X/.X/.X/XX/.X/.X/.X/"},
			{"XX...../.XXXXXX/",".X/XX/X./X./X./X./X./","XXXXXX./.....XX/",".X/.X/.X/.X/.X/XX/X./",".....XX/XXXXXX./","X./X./X./X./X./XX/.X/",".XXXXXX/XX...../","X./XX/.X/.X/.X/.X/.X/"},
			{"XX..../XXXXXX/","XX/XX/X./X./X./X./","XXXXXX/....XX/",".X/.X/.X/.X/XX/XX/","....XX/XXXXXX/","X./X./X./X./XX/XX/","XXXXXX/XX..../","XX/XX/.X/.X/.X/.X/"},
			{"X...../X...../XXXXXX/","XXX/X../X../X../X../X../","XXXXXX/.....X/.....X/","..X/..X/..X/..X/..X/XXX/",".....X/.....X/XXXXXX/","X../X../X../X../X../XXX/","XXXXXX/X...../X...../","XXX/..X/..X/..X/..X/..X/"},
			{"X...../XXXXXX/X...../","XXX/.X./.X./.X./.X./.X./",".....X/XXXXXX/.....X/",".X./.X./.X./.X./.X./XXX/"},
			{"X...../XXXXXX/.X..../",".XX/XX./.X./.X./.X./.X./","....X./XXXXXX/.....X/",".X./.X./.X./.X./.XX/XX./",".....X/XXXXXX/....X./",".X./.X./.X./.X./XX./.XX/",".X..../XXXXXX/X...../","XX./.XX/.X./.X./.X./.X./"},
			{"X.X.../XXXXXX/","XX/X./XX/X./X./X./","XXXXXX/...X.X/",".X/.X/.X/XX/.X/XX/","...X.X/XXXXXX/","X./X./X./XX/X./XX/","XXXXXX/X.X.../","XX/.X/XX/.X/.X/.X/"},
			{"X...../XXXXXX/..X.../",".XX/.X./XX./.X./.X./.X./","...X../XXXXXX/.....X/",".X./.X./.X./.XX/.X./XX./",".....X/XXXXXX/...X../",".X./.X./.X./XX./.X./.XX/","..X.../XXXXXX/X...../","XX./.X./.XX/.X./.X./.X./"},
			{"X..X../XXXXXX/","XX/X./X./XX/X./X./","XXXXXX/..X..X/",".X/.X/XX/.X/.X/XX/","..X..X/XXXXXX/","X./X./XX/X./X./XX/","XXXXXX/X..X../","XX/.X/.X/XX/.X/.X/"},
			{"X...../XXXXXX/...X../",".XX/.X./.X./XX./.X./.X./","..X.../XXXXXX/.....X/",".X./.X./.XX/.X./.X./XX./",".....X/XXXXXX/..X.../",".X./.X./XX./.X./.X./.XX/","...X../XXXXXX/X...../","XX./.X./.X./.XX/.X./.X./"},
			{"X...X./XXXXXX/","XX/X./X./X./XX/X./","XXXXXX/.X...X/",".X/XX/.X/.X/.X/XX/",".X...X/XXXXXX/","X./XX/X./X./X./XX/","XXXXXX/X...X./","XX/.X/.X/.X/XX/.X/"},
			{"X...../XXXXXX/....X./",".XX/.X./.X./.X./XX./.X./",".X..../XXXXXX/.....X/",".X./.XX/.X./.X./.X./XX./",".....X/XXXXXX/.X..../",".X./XX./.X./.X./.X./.XX/","....X./XXXXXX/X...../","XX./.X./.X./.X./.XX/.X./"},
			{"X....X/XXXXXX/","XX/X./X./X./X./XX/","XXXXXX/X....X/","XX/.X/.X/.X/.X/XX/"},
			{"X...../XXXXXX/.....X/",".XX/.X./.X./.X./.X./XX./",".....X/XXXXXX/X...../","XX./.X./.X./.X./.X./.XX/"},
			{".XX.../XXXXXX/","X./XX/XX/X./X./X./","XXXXXX/...XX./",".X/.X/.X/XX/XX/.X/","...XX./XXXXXX/","X./X./X./XX/XX/X./","XXXXXX/.XX.../",".X/XX/XX/.X/.X/.X/"},
			{".X..../.X..../XXXXXX/","X../XXX/X../X../X../X../","XXXXXX/....X./....X./","..X/..X/..X/..X/XXX/..X/","....X./....X./XXXXXX/","X../X../X../X../XXX/X../","XXXXXX/.X..../.X..../","..X/XXX/..X/..X/..X/..X/"},
			{".X..../XXXXXX/.X..../",".X./XXX/.X./.X./.X./.X./","....X./XXXXXX/....X./",".X./.X./.X./.X./XXX/.X./"},
			{".X..../XXXXXX/..X.../",".X./.XX/XX./.X./.X./.X./","...X../XXXXXX/....X./",".X./.X./.X./.XX/XX./.X./","....X./XXXXXX/...X../",".X./.X./.X./XX./.XX/.X./","..X.../XXXXXX/.X..../",".X./XX./.XX/.X./.X./.X./"},
			{".X.X../XXXXXX/","X./XX/X./XX/X./X./","XXXXXX/..X.X./",".X/.X/XX/.X/XX/.X/","..X.X./XXXXXX/","X./X./XX/X./XX/X./","XXXXXX/.X.X../",".X/XX/.X/XX/.X/.X/"},
			{".X..../XXXXXX/...X../",".X./.XX/.X./XX./.X./.X./","..X.../XXXXXX/....X./",".X./.X./.XX/.X./XX./.X./","....X./XXXXXX/..X.../",".X./.X./XX./.X./.XX/.X./","...X../XXXXXX/.X..../",".X./XX./.X./.XX/.X./.X./"},
			{".X..X./XXXXXX/","X./XX/X./X./XX/X./","XXXXXX/.X..X./",".X/XX/.X/.X/XX/.X/"},
			{".X..../XXXXXX/....X./",".X./.XX/.X./.X./XX./.X./","....X./XXXXXX/.X..../",".X./XX./.X./.X./.XX/.X./"},
			{"..XX../XXXXXX/","X./X./XX/XX/X./X./","XXXXXX/..XX../",".X/.X/XX/XX/.X/.X/"},
			{"..X.../..X.../XXXXXX/","X../X../XXX/X../X../X../","XXXXXX/...X../...X../","..X/..X/..X/XXX/..X/..X/","...X../...X../XXXXXX/","X../X../X../XXX/X../X../","XXXXXX/..X.../..X.../","..X/..X/XXX/..X/..X/..X/"},
			{"..X.../XXXXXX/..X.../",".X./.X./XXX/.X./.X./.X./","...X../XXXXXX/...X../",".X./.X./.X./XXX/.X./.X./"},
			{"..X.../XXXXXX/...X../",".X./.X./.XX/XX./.X./.X./","...X../XXXXXX/..X.../",".X./.X./XX./.XX/.X./.X./"},
			{"XXX..../..XXXXX/",".X/.X/XX/X./X./X./X./","XXXXX../....XXX/",".X/.X/.X/.X/XX/X./X./","....XXX/XXXXX../","X./X./X./X./XX/.X/.X/","..XXXXX/XXX..../","X./X./XX/.X/.X/.X/.X/"},
			{"X...../XX..../.XXXXX/",".XX/XX./X../X../X../X../","XXXXX./....XX/.....X/","..X/..X/..X/..X/.XX/XX./",".....X/....XX/XXXXX./","X../X../X../X../XX./.XX/",".XXXXX/XX..../X...../","XX./.XX/..X/..X/..X/..X/"},
			{"XXX.../.XXXXX/",".X/XX/XX/X./X./X./","XXXXX./...XXX/",".X/.X/.X/XX/XX/X./","...XXX/XXXXX./","X./X./X./XX/XX/.X/",".XXXXX/XXX.../","X./XX/XX/.X/.X/.X/"},
			{".X..../XX..../.XXXXX/",".X./XXX/X../X../X../X../","XXXXX./....XX/....X./","..X/..X/..X/..X/XXX/.X./","....X./....XX/XXXXX./","X../X../X../X../XXX/.X./",".XXXXX/XX..../.X..../",".X./XXX/..X/..X/..X/..X/"},
			{"XX..../.XXXXX/.X..../","..X/XXX/.X./.X./.X./.X./","....X./XXXXX./....XX/",".X./.X./.X./.X./XXX/X../","....XX/XXXXX./....X./",".X./.X./.X./.X./XXX/..X/",".X..../.XXXXX/XX..../","X../XXX/.X./.X./.X./.X./"},
			{"XX..../.XXXXX/..X.../","..X/.XX/XX./.X./.X./.X./","...X../XXXXX./....XX/",".X./.X./.X./.XX/XX./X../","....XX/XXXXX./...X../",".X./.X./.X./XX./.XX/..X/","..X.../.XXXXX/XX..../","X../XX./.XX/.X./.X./.X./"},
			{"XX.X../.XXXXX/",".X/XX/X./XX/X./X./","XXXXX./..X.XX/",".X/.X/XX/.X/XX/X./","..X.XX/XXXXX./","X./X./XX/X./XX/.X/",".XXXXX/XX.X../","X./XX/.X/XX/.X/.X/"},
			{"XX..../.XXXXX/...X../","..X/.XX/.X./XX./.X./.X./","..X.../XXXXX./....XX/",".X./.X./.XX/.X./XX./X../","....XX/XXXXX./..X.../",".X./.X./XX./.X./.XX/..X/","...X../.XXXXX/XX..../","X../XX./.X./.XX/.X./.X./"},
			{"XX..X./.XXXXX/",".X/XX/X./X./XX/X./","XXXXX./.X..XX/",".X/XX/.X/.X/XX/X./",".X..XX/XXXXX./","X./XX/X./X./XX/.X/",".XXXXX/XX..X./","X./XX/.X/.X/XX/.X/"},
			{"XX..../.XXXXX/....X./","..X/.XX/.X./.X./XX./.X./",".X..../XXXXX./....XX/",".X./.XX/.X./.X./XX./X../","....XX/XXXXX./.X..../",".X./XX./.X./.X./.XX/..X/","....X./.XXXXX/XX..../","X../XX./.X./.X./.XX/.X./"},
			{"XX...X/.XXXXX/",".X/XX/X./X./X./XX/","XXXXX./X...XX/","XX/.X/.X/.X/XX/X./","X...XX/XXXXX./","XX/X./X./X./XX/.X/",".XXXXX/XX...X/","X./XX/.X/.X/.X/XX/"},
			{"XX..../.XXXXX/.....X/","..X/.XX/.X./.X./.X./XX./","X...../XXXXX./....XX/",".XX/.X./.X./.X./XX./X../","....XX/XXXXX./X...../","XX./.X./.X./.X./.XX/..X/",".....X/.XXXXX/XX..../","X../XX./.X./.X./.X./.XX/"},
			{"X..../XX.../XXXXX/","XXX/XX./X../X../X../","XXXXX/...XX/....X/","..X/..X/..X/.XX/XXX/","....X/...XX/XXXXX/","X../X../X../XX./XXX/","XXXXX/XX.../X..../","XXX/.XX/..X/..X/..X/"},
			{"XXX../XXXXX/","XX/XX/XX/X./X./","XXXXX/..XXX/",".X/.X/XX/XX/XX/","..XXX/XXXXX/","X./X./XX/XX/XX/","XXXXX/XXX../","XX/XX/XX/.X/.X/"},
			{".X.../XX.../XXXXX/","XX./XXX/X../X../X../","XXXXX/...XX/...X./","..X/..X/..X/XXX/.XX/","...X./...XX/XXXXX/","X../X../X../XXX/XX./","XXXXX/XX.../.X.../",".XX/XXX/..X/..X/..X/"},
			{"XX.../XXXXX/X..../","XXX/.XX/.X./.X./.X./","....X/XXXXX/...XX/",".X./.X./.X./XX./XXX/","...XX/XXXXX/....X/",".X./.X./.X./.XX/XXX/","X..../XXXXX/XX.../","XXX/XX./.X./.X./.X./"},
			{"XX.../XXXXX/.X.../",".XX/XXX/.X./.X./.X./","...X./XXXXX/...XX/",".X./.X./.X./XXX/XX./","...XX/XXXXX/...X./",".X./.X./.X./XXX/.XX/",".X.../XXXXX/XX.../","XX./XXX/.X./.X./.X./"},
			{"XX.../XXXXX/..X../",".XX/.XX/XX./.X./.X./","..X../XXXXX/...XX/",".X./.X./.XX/XX./XX./","...XX/XXXXX/..X../",".X./.X./XX./.XX/.XX/","..X../XXXXX/XX.../","XX./XX./.XX/.X./.X./"},
			{"XX.X./XXXXX/","XX/XX/X./XX/X./","XXXXX/.X.XX/",".X/XX/.X/XX/XX/",".X.XX/XXXXX/","X./XX/X./XX/XX/","XXXXX/XX.X./","XX/XX/.X/XX/.X/"},
			{"XX.../XXXXX/...X./",".XX/.XX/.X./XX./.X./",".X.../XXXXX/...XX/",".X./.XX/.X./XX./XX./","...XX/XXXXX/.X.../",".X./XX./.X./.XX/.XX/","...X./XXXXX/XX.../","XX./XX./.X./.XX/.X./"},
			{"XX..X/XXXXX/","XX/XX/X./X./XX/","XXXXX/X..XX/","XX/.X/.X/XX/XX/","X..XX/XXXXX/","XX/X./X./XX/XX/","XXXXX/XX..X/","XX/XX/.X/.X/XX/"},
			{"XX.../XXXXX/....X/",".XX/.XX/.X./.X./XX./","X..../XXXXX/...XX/",".XX/.X./.X./XX./XX./","...XX/XXXXX/X..../","XX./.X./.X./.XX/.XX/","....X/XXXXX/XX.../","XX./XX./.X./.X./.XX/"},
			{"XX..../.X..../.XXXXX/","..X/XXX/X../X../X../X../","XXXXX./....X./....XX/","..X/..X/..X/..X/XXX/X../","....XX/....X./XXXXX./","X../X../X../X../XXX/..X/",".XXXXX/.X..../XX..../","X../XXX/..X/..X/..X/..X/"},
			{"XX.../X..../XXXXX/","XXX/X.X/X../X../X../","XXXXX/....X/...XX/","..X/..X/..X/X.X/XXX/","...XX/....X/XXXXX/","X../X../X../X.X/XXX/","XXXXX/X..../XX.../","XXX/X.X/..X/..X/..X/"},
			{"X..../X..../X..../XXXXX/","XXXX/X.../X.../X.../X.../","XXXXX/....X/....X/....X/","...X/...X/...X/...X/XXXX/","....X/....X/....X/XXXXX/","X.../X.../X.../X.../XXXX/","XXXXX/X..../X..../X..../","XXXX/...X/...X/...X/...X/"},
			{"X..../X..../XXXXX/X..../","XXXX/.X../.X../.X../.X../","....X/XXXXX/....X/....X/","..X./..X./..X./..X./XXXX/","....X/....X/XXXXX/....X/",".X../.X../.X../.X../XXXX/","X..../XXXXX/X..../X..../","XXXX/..X./..X./..X./..X./"},
			{"X..../X..../XXXXX/.X.../",".XXX/XX../.X../.X../.X../","...X./XXXXX/....X/....X/","..X./..X./..X./..XX/XXX./","....X/....X/XXXXX/...X./",".X../.X../.X../XX../.XXX/",".X.../XXXXX/X..../X..../","XXX./..XX/..X./..X./..X./"},
			{"X..../X.X../XXXXX/","XXX/X../XX./X../X../","XXXXX/..X.X/....X/","..X/..X/.XX/..X/XXX/","....X/..X.X/XXXXX/","X../X../XX./X../XXX/","XXXXX/X.X../X..../","XXX/..X/.XX/..X/..X/"},
			{"X..../X..../XXXXX/..X../",".XXX/.X../XX../.X../.X../","..X../XXXXX/....X/....X/","..X./..X./..XX/..X./XXX./","....X/....X/XXXXX/..X../",".X../.X../XX../.X../.XXX/","..X../XXXXX/X..../X..../","XXX./..X./..XX/..X./..X./"},
			{"X..../X..X./XXXXX/","XXX/X../X../XX./X../","XXXXX/.X..X/....X/","..X/.XX/..X/..X/XXX/","....X/.X..X/XXXXX/","X../XX./X../X../XXX/","XXXXX/X..X./X..../","XXX/..X/..X/.XX/..X/"},
			{"X..../X..../XXXXX/...X./",".XXX/.X../.X../XX../.X../",".X.../XXXXX/....X/....X/","..X./..XX/..X./..X./XXX./","....X/....X/XXXXX/.X.../",".X../XX../.X../.X../.XXX/","...X./XXXXX/X..../X..../","XXX./..X./..X./..XX/..X./"},
			{"X..../X...X/XXXXX/","XXX/X../X../X../XX./","XXXXX/X...X/....X/",".XX/..X/..X/..X/XXX/","....X/X...X/XXXXX/","XX./X../X../X../XXX/","XXXXX/X...X/X..../","XXX/..X/..X/..X/.XX/"},
			{"X..../X..../XXXXX/....X/",".XXX/.X../.X../.X../XX../","X..../XXXXX/....X/....X/","..XX/..X./..X./..X./XXX./","....X/....X/XXXXX/X..../","XX../.X../.X../.X../.XXX/","....X/XXXXX/X..../X..../","XXX./..X./..X./..X./..XX/"},
			{"X.X../XXXXX/X..../","XXX/.X./.XX/.X./.X./","....X/XXXXX/..X.X/",".X./.X./XX./.X./XXX/","..X.X/XXXXX/....X/",".X./.X./.XX/.X./XXX/","X..../XXXXX/X.X../","XXX/.X./XX./.X./.X./"},
			{"X..X./XXXXX/X..../","XXX/.X./.X./.XX/.X./","....X/XXXXX/.X..X/",".X./XX./.X./.X./XXX/",".X..X/XXXXX/....X/",".X./.XX/.X./.X./XXX/","X..../XXXXX/X..X./","XXX/.X./.X./XX./.X./"},
			{"X...X/XXXXX/X..../","XXX/.X./.X./.X./.XX/","....X/XXXXX/X...X/","XX./.X./.X./.X./XXX/","X...X/XXXXX/....X/",".XX/.X./.X./.X./XXX/","X..../XXXXX/X...X/","XXX/.X./.X./.X./XX./"},
			{"X.X../XXXXX/.X.../",".XX/XX./.XX/.X./.X./","...X./XXXXX/..X.X/",".X./.X./XX./.XX/XX./","..X.X/XXXXX/...X./",".X./.X./.XX/XX./.XX/",".X.../XXXXX/X.X../","XX./.XX/XX./.X./.X./"},
			{"X..../XXXXX/.XX../",".XX/XX./XX./.X./.X./","..XX./XXXXX/....X/",".X./.X./.XX/.XX/XX./","....X/XXXXX/..XX./",".X./.X./XX./XX./.XX/",".XX../XXXXX/X..../","XX./.XX/.XX/.X./.X./"},
			{"X..X./XXXXX/.X.../",".XX/XX./.X./.XX/.X./","...X./XXXXX/.X..X/",".X./XX./.X./.XX/XX./",".X..X/XXXXX/...X./",".X./.XX/.X./XX./.XX/",".X.../XXXXX/X..X./","XX./.XX/.X./XX./.X./"},
			{"X..../XXXXX/.X.X./",".XX/XX./.X./XX./.X./",".X.X./XXXXX/....X/",".X./.XX/.X./.XX/XX./","....X/XXXXX/.X.X./",".X./XX./.X./XX./.XX/",".X.X./XXXXX/X..../","XX./.XX/.X./.XX/.X./"},
			{"X...X/XXXXX/.X.../",".XX/XX./.X./.X./.XX/","...X./XXXXX/X...X/","XX./.X./.X./.XX/XX./","X...X/XXXXX/...X./",".XX/.X./.X./XX./.XX/",".X.../XXXXX/X...X/","XX./.XX/.X./.X./XX./"},
			{"X..../XXXXX/.X..X/",".XX/XX./.X./.X./XX./","X..X./XXXXX/....X/",".XX/.X./.X./.XX/XX./","....X/XXXXX/X..X./","XX./.X./.X./XX./.XX/",".X..X/XXXXX/X..../","XX./.XX/.X./.X./.XX/"},
			{"X..../XXXXX/.X.../.X.../","..XX/XXX./..X./..X./..X./","...X./...X./XXXXX/....X/",".X../.X../.X../.XXX/XX../","....X/XXXXX/...X./...X./","..X./..X./..X./XXX./..XX/",".X.../.X.../XXXXX/X..../","XX../.XXX/.X../.X../.X../"},
			{"X.XX./XXXXX/","XX/X./XX/XX/X./","XXXXX/.XX.X/",".X/XX/XX/.X/XX/",".XX.X/XXXXX/","X./XX/XX/X./XX/","XXXXX/X.XX./","XX/.X/XX/XX/.X/"},
			{"..X../X.X../XXXXX/","XX./X../XXX/X../X../","XXXXX/..X.X/..X../","..X/..X/XXX/..X/.XX/","..X../..X.X/XXXXX/","X../X../XXX/X../XX./","XXXXX/X.X../..X../",".XX/..X/XXX/..X/..X/"},
			{"X.X../XXXXX/..X../",".XX/.X./XXX/.X./.X./","..X../XXXXX/..X.X/",".X./.X./XXX/.X./XX./","..X.X/XXXXX/..X../",".X./.X./XXX/.X./.XX/","..X../XXXXX/X.X../","XX./.X./XXX/.X./.X./"},
			{"X.X../XXXXX/...X./",".XX/.X./.XX/XX./.X./",".X.../XXXXX/..X.X/",".X./.XX/XX./.X./XX./","..X.X/XXXXX/.X.../",".X./XX./.XX/.X./.XX/","...X./XXXXX/X.X../","XX./.X./XX./.XX/.X./"},
			{"X.X.X/XXXXX/","XX/X./XX/X./XX/","XXXXX/X.X.X/","XX/.X/XX/.X/XX/"},
			{"X.X../XXXXX/....X/",".XX/.X./.XX/.X./XX./","X..../XXXXX/..X.X/",".XX/.X./XX./.X./XX./","..X.X/XXXXX/X..../","XX./.X./.XX/.X./.XX/","....X/XXXXX/X.X../","XX./.X./XX./.X./.XX/"},
			{"X..X./XXXXX/..X../",".XX/.X./XX./.XX/.X./","..X../XXXXX/.X..X/",".X./XX./.XX/.X./XX./",".X..X/XXXXX/..X../",".X./.XX/XX./.X./.XX/","..X../XXXXX/X..X./","XX./.X./.XX/XX./.X./"},
			{"X..../XXXXX/..XX./",".XX/.X./XX./XX./.X./",".XX../XXXXX/....X/",".X./.XX/.XX/.X./XX./","....X/XXXXX/.XX../",".X./XX./XX./.X./.XX/","..XX./XXXXX/X..../","XX./.X./.XX/.XX/.X./"},
			{"X...X/XXXXX/..X../",".XX/.X./XX./.X./.XX/","..X../XXXXX/X...X/","XX./.X./.XX/.X./XX./"},
			{"X..../XXXXX/..X../..X../","..XX/..X./XXX./..X./..X./","..X../..X../XXXXX/....X/",".X../.X../.XXX/.X../XX../","....X/XXXXX/..X../..X../","..X./..X./XXX./..X./..XX/","..X../..X../XXXXX/X..../","XX../.X../.XXX/.X../.X../"},
			{"...X./X..X./XXXXX/","XX./X../X../XXX/X../","XXXXX/.X..X/.X.../","..X/XXX/..X/..X/.XX/",".X.../.X..X/XXXXX/","X../XXX/X../X../XX./","XXXXX/X..X./...X./",".XX/..X/..X/XXX/..X/"},
			{"X..X./XXXXX/...X./",".XX/.X./.X./XXX/.X./",".X.../XXXXX/.X..X/",".X./XXX/.X./.X./XX./",".X..X/XXXXX/.X.../",".X./XXX/.X./.X./.XX/","...X./XXXXX/X..X./","XX./.X./.X./XXX/.X./"},
			{"X..../XXXXX/...X./...X./","..XX/..X./..X./XXX./..X./",".X.../.X.../XXXXX/....X/",".X../.XXX/.X../.X../XX../","....X/XXXXX/.X.../.X.../","..X./XXX./..X./..X./..XX/","...X./...X./XXXXX/X..../","XX../.X../.X../.XXX/.X../"},
			{".X.../.XX../XXXXX/","X../XXX/XX./X../X../","XXXXX/..XX./...X./","..X/..X/.XX/XXX/..X/","...X./..XX./XXXXX/","X../X../XX./XXX/X../","XXXXX/.XX../.X.../","..X/XXX/.XX/..X/..X/"},
			{".XXX./XXXXX/","X./XX/XX/XX/X./","XXXXX/.XXX./",".X/XX/XX/XX/.X/"},
			{"..X../.XX../XXXXX/","X../XX./XXX/X../X../","XXXXX/..XX./..X../","..X/..X/XXX/.XX/..X/","..X../..XX./XXXXX/","X../X../XXX/XX./X../","XXXXX/.XX../..X../","..X/.XX/XXX/..X/..X/"},
			{".XX../XXXXX/.X.../",".X./XXX/.XX/.X./.X./","...X./XXXXX/..XX./",".X./.X./XX./XXX/.X./","..XX./XXXXX/...X./",".X./.X./.XX/XXX/.X./",".X.../XXXXX/.XX../",".X./XXX/XX./.X./.X./"},
			{".XX../XXXXX/..X../",".X./.XX/XXX/.X./.X./","..X../XXXXX/..XX./",".X./.X./XXX/XX./.X./","..XX./XXXXX/..X../",".X./.X./XXX/.XX/.X./","..X../XXXXX/.XX../",".X./XX./XXX/.X./.X./"},
			{".XX../XXXXX/...X./",".X./.XX/.XX/XX./.X./",".X.../XXXXX/..XX./",".X./.XX/XX./XX./.X./","..XX./XXXXX/.X.../",".X./XX./.XX/.XX/.X./","...X./XXXXX/.XX../",".X./XX./XX./.XX/.X./"},
			{"XX.../.X.../XXXXX/","X.X/XXX/X../X../X../","XXXXX/...X./...XX/","..X/..X/..X/XXX/X.X/","...XX/...X./XXXXX/","X../X../X../XXX/X.X/","XXXXX/.X.../XX.../","X.X/XXX/..X/..X/..X/"},
			{".XX../.X.../XXXXX/","X../XXX/X.X/X../X../","XXXXX/...X./..XX./","..X/..X/X.X/XXX/..X/","..XX./...X./XXXXX/","X../X../X.X/XXX/X../","XXXXX/.X.../.XX../","..X/XXX/X.X/..X/..X/"},
			{".X.../.X.../.X.../XXXXX/","X.../XXXX/X.../X.../X.../","XXXXX/...X./...X./...X./","...X/...X/...X/XXXX/...X/","...X./...X./...X./XXXXX/","X.../X.../X.../XXXX/X.../","XXXXX/.X.../.X.../.X.../","...X/XXXX/...X/...X/...X/"},
			{".X.../.X.../XXXXX/.X.../",".X../XXXX/.X../.X../.X../","...X./XXXXX/...X./...X./","..X./..X./..X./XXXX/..X./","...X./...X./XXXXX/...X./",".X../.X../.X../XXXX/.X../",".X.../XXXXX/.X.../.X.../","..X./XXXX/..X./..X./..X./"},
			{".X.../.X.../XXXXX/..X../",".X../.XXX/XX../.X../.X../","..X../XXXXX/...X./...X./","..X./..X./..XX/XXX./..X./","...X./...X./XXXXX/..X../",".X../.X../XX../.XXX/.X../","..X../XXXXX/.X.../.X.../","..X./XXX./..XX/..X./..X./"},
			{".X.../.X.X./XXXXX/","X../XXX/X../XX./X../","XXXXX/.X.X./...X./","..X/.XX/..X/XXX/..X/","...X./.X.X./XXXXX/","X../XX./X../XXX/X../","XXXXX/.X.X./.X.../","..X/XXX/..X/.XX/..X/"},
			{".X.../.X.../XXXXX/...X./",".X../.XXX/.X../XX../.X../",".X.../XXXXX/...X./...X./","..X./..XX/..X./XXX./..X./","...X./...X./XXXXX/.X.../",".X../XX../.X../.XXX/.X../","...X./XXXXX/.X.../.X.../","..X./XXX./..X./..XX/..X./"},
			{".X.X./XXXXX/.X.../",".X./XXX/.X./.XX/.X./","...X./XXXXX/.X.X./",".X./XX./.X./XXX/.X./",".X.X./XXXXX/...X./",".X./.XX/.X./XXX/.X./",".X.../XXXXX/.X.X./",".X./XXX/.X./XX./.X./"},
			{".X.X./XXXXX/..X../",".X./.XX/XX./.XX/.X./","..X../XXXXX/.X.X./",".X./XX./.XX/XX./.X./"},
			{".X.../XXXXX/..X../..X../","..X./..XX/XXX./..X./..X./","..X../..X../XXXXX/...X./",".X../.X../.XXX/XX../.X../","...X./XXXXX/..X../..X../","..X./..X./XXX./..XX/..X./","..X../..X../XXXXX/.X.../",".X../XX../.XXX/.X../.X../"},
			{".XX../..X../XXXXX/","X../X.X/XXX/X../X../","XXXXX/..X../..XX./","..X/..X/XXX/X.X/..X/","..XX./..X../XXXXX/","X../X../XXX/X.X/X../","XXXXX/..X../.XX../","..X/X.X/XXX/..X/..X/"},
			{"..X../..X../..X../XXXXX/","X.../X.../XXXX/X.../X.../","XXXXX/..X../..X../..X../","...X/...X/XXXX/...X/...X/"},
			{"..X../..X../XXXXX/..X../",".X../.X../XXXX/.X../.X../","..X../XXXXX/..X../..X../","..X./..X./XXXX/..X./..X./"},
			{"XXXX.../...XXXX/",".X/.X/.X/XX/X./X./X./","...XXXX/XXXX.../","X./X./X./XX/.X/.X/.X/"},
			{"X...../XXX.../..XXXX/",".XX/.X./XX./X../X../X../","XXXX../...XXX/.....X/","..X/..X/..X/.XX/.X./XX./",".....X/...XXX/XXXX../","X../X../X../XX./.X./.XX/","..XXXX/XXX.../X...../","XX./.X./.XX/..X/..X/..X/"},
			{"XXX.../X.XXXX/","XX/.X/XX/X./X./X./","XXXX.X/...XXX/",".X/.X/.X/XX/X./XX/","...XXX/XXXX.X/","X./X./X./XX/.X/XX/","X.XXXX/XXX.../","XX/X./XX/.X/.X/.X/"},
			{".X..../XXX.../..XXXX/",".X./.XX/XX./X../X../X../","XXXX../...XXX/....X./","..X/..X/..X/.XX/XX./.X./","....X./...XXX/XXXX../","X../X../X../XX./.XX/.X./","..XXXX/XXX.../.X..../",".X./XX./.XX/..X/..X/..X/"},
			{"XXXX../..XXXX/",".X/.X/XX/XX/X./X./","..XXXX/XXXX../","X./X./XX/XX/.X/.X/"},
			{"..X.../XXX.../..XXXX/",".X./.X./XXX/X../X../X../","XXXX../...XXX/...X../","..X/..X/..X/XXX/.X./.X./","...X../...XXX/XXXX../","X../X../X../XXX/.X./.X./","..XXXX/XXX.../..X.../",".X./.X./XXX/..X/..X/..X/"},
			{"XXX.../..XXXX/..X.../","..X/..X/XXX/.X./.X./.X./","...X../XXXX../...XXX/",".X./.X./.X./XXX/X../X../","...XXX/XXXX../...X../",".X./.X./.X./XXX/..X/..X/","..X.../..XXXX/XXX.../","X../X../XXX/.X./.X./.X./"},
			{"XXX.../..XXXX/...X../","..X/..X/.XX/XX./.X./.X./","..X.../XXXX../...XXX/",".X./.X./.XX/XX./X../X../","...XXX/XXXX../..X.../",".X./.X./XX./.XX/..X/..X/","...X../..XXXX/XXX.../","X../X../XX./.XX/.X./.X./"},
			{"XXX.X./..XXXX/",".X/.X/XX/X./XX/X./","XXXX../.X.XXX/",".X/XX/.X/XX/X./X./",".X.XXX/XXXX../","X./XX/X./XX/.X/.X/","..XXXX/XXX.X./","X./X./XX/.X/XX/.X/"},
			{"XXX.../..XXXX/....X./","..X/..X/.XX/.X./XX./.X./",".X..../XXXX../...XXX/",".X./.XX/.X./XX./X../X../","...XXX/XXXX../.X..../",".X./XX./.X./.XX/..X/..X/","....X./..XXXX/XXX.../","X../X../XX./.X./.XX/.X./"},
			{"XXX..X/..XXXX/",".X/.X/XX/X./X./XX/","XXXX../X..XXX/","XX/.X/.X/XX/X./X./","X..XXX/XXXX../","XX/X./X./XX/.X/.X/","..XXXX/XXX..X/","X./X./XX/.X/.X/XX/"},
			{"XXX.../..XXXX/.....X/","..X/..X/.XX/.X./.X./XX./","X...../XXXX../...XXX/",".XX/.X./.X./XX./X../X../","...XXX/XXXX../X...../","XX./.X./.X./.XX/..X/..X/",".....X/..XXXX/XXX.../","X../X../XX./.X./.X./.XX/"},
			{"XX..../.XX.../..XXXX/","..X/.XX/XX./X../X../X../","XXXX../...XX./....XX/","..X/..X/..X/.XX/XX./X../","....XX/...XX./XXXX../","X../X../X../XX./.XX/..X/","..XXXX/.XX.../XX..../","X../XX./.XX/..X/..X/..X/"},
			{"XX.../XX.../.XXXX/",".XX/XXX/X../X../X../","XXXX./...XX/...XX/","..X/..X/..X/XXX/XX./","...XX/...XX/XXXX./","X../X../X../XXX/.XX/",".XXXX/XX.../XX.../","XX./XXX/..X/..X/..X/"},
			{"X..../X..../XX.../.XXXX/",".XXX/XX../X.../X.../X.../","XXXX./...XX/....X/....X/","...X/...X/...X/..XX/XXX./","....X/....X/...XX/XXXX./","X.../X.../X.../XX../.XXX/",".XXXX/XX.../X..../X..../","XXX./..XX/...X/...X/...X/"},
			{"X..../XXX../.XXXX/",".XX/XX./XX./X../X../","XXXX./..XXX/....X/","..X/..X/.XX/.XX/XX./","....X/..XXX/XXXX./","X../X../XX./XX./.XX/",".XXXX/XXX../X..../","XX./.XX/.XX/..X/..X/"},
			{"X..../XX.../.XXXX/.X.../","..XX/XXX./.X../.X../.X../","...X./XXXX./...XX/....X/","..X./..X./..X./.XXX/XX../","....X/...XX/XXXX./...X./",".X../.X../.X../XXX./..XX/",".X.../.XXXX/XX.../X..../","XX../.XXX/..X./..X./..X./"},
			{"X..../XX.../.XXXX/..X../","..XX/.XX./XX../.X../.X../","..X../XXXX./...XX/....X/","..X./..X./..XX/.XX./XX../","....X/...XX/XXXX./..X../",".X../.X../XX../.XX./..XX/","..X../.XXXX/XX.../X..../","XX../.XX./..XX/..X./..X./"},
			{"X..../XX.X./.XXXX/",".XX/XX./X../XX./X../","XXXX./.X.XX/....X/","..X/.XX/..X/.XX/XX./","....X/.X.XX/XXXX./","X../XX./X../XX./.XX/",".XXXX/XX.X./X..../","XX./.XX/..X/.XX/..X/"},
			{"X..../XX.../.XXXX/...X./","..XX/.XX./.X../XX../.X../",".X.../XXXX./...XX/....X/","..X./..XX/..X./.XX./XX../","....X/...XX/XXXX./.X.../",".X../XX../.X../.XX./..XX/","...X./.XXXX/XX.../X..../","XX../.XX./..X./..XX/..X./"},
			{"X..../XX..X/.XXXX/",".XX/XX./X../X../XX./","XXXX./X..XX/....X/",".XX/..X/..X/.XX/XX./","....X/X..XX/XXXX./","XX./X../X../XX./.XX/",".XXXX/XX..X/X..../","XX./.XX/..X/..X/.XX/"},
			{"X..../XX.../.XXXX/....X/","..XX/.XX./.X../.X../XX../","X..../XXXX./...XX/....X/","..XX/..X./..X./.XX./XX../","....X/...XX/XXXX./X..../","XX../.X../.X../.XX./..XX/","....X/.XXXX/XX.../X..../","XX../.XX./..X./..X./..XX/"},
			{".X.../XXX../.XXXX/",".X./XXX/XX./X../X../","XXXX./..XXX/...X./","..X/..X/.XX/XXX/.X./","...X./..XXX/XXXX./","X../X../XX./XXX/.X./",".XXXX/XXX../.X.../",".X./XXX/.XX/..X/..X/"},
			{"XXXX./.XXXX/",".X/XX/XX/XX/X./",".XXXX/XXXX./","X./XX/XX/XX/.X/"},
			{"..X../XXX../.XXXX/",".X./XX./XXX/X../X../","XXXX./..XXX/..X../","..X/..X/XXX/.XX/.X./","..X../..XXX/XXXX./","X../X../XXX/XX./.X./",".XXXX/XXX../..X../",".X./.XX/XXX/..X/..X/"},
			{"XXX../.XXXX/.X.../","..X/XXX/.XX/.X./.X./","...X./XXXX./..XXX/",".X./.X./XX./XXX/X../","..XXX/XXXX./...X./",".X./.X./.XX/XXX/..X/",".X.../.XXXX/XXX../","X../XXX/XX./.X./.X./"},
			{"XXX../.XXXX/..X../","..X/.XX/XXX/.X./.X./","..X../XXXX./..XXX/",".X./.X./XXX/XX./X../","..XXX/XXXX./..X../",".X./.X./XXX/.XX/..X/","..X../.XXXX/XXX../","X../XX./XXX/.X./.X./"},
			{"XXX../.XXXX/...X./","..X/.XX/.XX/XX./.X./",".X.../XXXX./..XXX/",".X./.XX/XX./XX./X../","..XXX/XXXX./.X.../",".X./XX./.XX/.XX/..X/","...X./.XXXX/XXX../","X../XX./XX./.XX/.X./"},
			{"XXX.X/.XXXX/",".X/XX/XX/X./XX/","XXXX./X.XXX/","XX/.X/XX/XX/X./","X.XXX/XXXX./","XX/X./XX/XX/.X/",".XXXX/XXX.X/","X./XX/XX/.X/XX/"},
			{"XXX../.XXXX/....X/","..X/.XX/.XX/.X./XX./","X..../XXXX./..XXX/",".XX/.X./XX./XX./X../","..XXX/XXXX./X..../","XX./.X./.XX/.XX/..X/","....X/.XXXX/XXX../","X../XX./XX./.X./.XX/"},
			{".XX../XX.../.XXXX/",".X./XXX/X.X/X../X../","XXXX./...XX/..XX./","..X/..X/X.X/XXX/.X./","..XX./...XX/XXXX./","X../X../X.X/XXX/.X./",".XXXX/XX.../.XX../",".X./XXX/X.X/..X/..X/"},
			{".X.../.X.../XX.../.XXXX/",".X../XXXX/X.../X.../X.../","XXXX./...XX/...X./...X./","...X/...X/...X/XXXX/..X./","...X./...X./...XX/XXXX./","X.../X.../X.../XXXX/.X../",".XXXX/XX.../.X.../.X.../","..X./XXXX/...X/...X/...X/"},
			{".X.../XX.../.XXXX/.X.../","..X./XXXX/.X../.X../.X../","...X./XXXX./...XX/...X./","..X./..X./..X./XXXX/.X../","...X./...XX/XXXX./...X./",".X../.X../.X../XXXX/..X./",".X.../.XXXX/XX.../.X.../",".X../XXXX/..X./..X./..X./"},
			{".X.../XX.../.XXXX/..X../","..X./.XXX/XX../.X../.X../","..X../XXXX./...XX/...X./","..X./..X./..XX/XXX./.X../","...X./...XX/XXXX./..X../",".X../.X../XX../.XXX/..X./","..X../.XXXX/XX.../.X.../",".X../XXX./..XX/..X./..X./"},
			{".X.../XX.X./.XXXX/",".X./XXX/X../XX./X../","XXXX./.X.XX/...X./","..X/.XX/..X/XXX/.X./","...X./.X.XX/XXXX./","X../XX./X../XXX/.X./",".XXXX/XX.X./.X.../",".X./XXX/..X/.XX/..X/"},
			{".X.../XX.../.XXXX/...X./","..X./.XXX/.X../XX../.X../",".X.../XXXX./...XX/...X./","..X./..XX/..X./XXX./.X../","...X./...XX/XXXX./.X.../",".X../XX../.X../.XXX/..X./","...X./.XXXX/XX.../.X.../",".X../XXX./..X./..XX/..X./"},
			{".X.../XX..X/.XXXX/",".X./XXX/X../X../XX./","XXXX./X..XX/...X./",".XX/..X/..X/XXX/.X./","...X./X..XX/XXXX./","XX./X../X../XXX/.X./",".XXXX/XX..X/.X.../",".X./XXX/..X/..X/.XX/"},
			{".X.../XX.../.XXXX/....X/","..X./.XXX/.X../.X../XX../","X..../XXXX./...XX/...X./","..XX/..X./..X./XXX./.X../","...X./...XX/XXXX./X..../","XX../.X../.X../.XXX/..X./","....X/.XXXX/XX.../.X.../",".X../XXX./..X./..X./..XX/"},
			{"XX.../.XXXX/.XX../","..X/XXX/XX./.X./.X./","..XX./XXXX./...XX/",".X./.X./.XX/XXX/X../","...XX/XXXX./..XX./",".X./.X./XX./XXX/..X/",".XX../.XXXX/XX.../","X../XXX/.XX/.X./.X./"},
			{"XX.X./.XXXX/.X.../","..X/XXX/.X./.XX/.X./","...X./XXXX./.X.XX/",".X./XX./.X./XXX/X../",".X.XX/XXXX./...X./",".X./.XX/.X./XXX/..X/",".X.../.XXXX/XX.X./","X../XXX/.X./XX./.X./"},
			{"XX.../.XXXX/.X.X./","..X/XXX/.X./XX./.X./",".X.X./XXXX./...XX/",".X./.XX/.X./XXX/X../","...XX/XXXX./.X.X./",".X./XX./.X./XXX/..X/",".X.X./.XXXX/XX.../","X../XXX/.X./.XX/.X./"},
			{"XX..X/.XXXX/.X.../","..X/XXX/.X./.X./.XX/","...X./XXXX./X..XX/","XX./.X./.X./XXX/X../","X..XX/XXXX./...X./",".XX/.X./.X./XXX/..X/",".X.../.XXXX/XX..X/","X../XXX/.X./.X./XX./"},
			{"XX.../.XXXX/.X..X/","..X/XXX/.X./.X./XX./","X..X./XXXX./...XX/",".XX/.X./.X./XXX/X../","...XX/XXXX./X..X./","XX./.X./.X./XXX/..X/",".X..X/.XXXX/XX.../","X../XXX/.X./.X./.XX/"},
			{"XX.../.XXXX/XX.../","X.X/XXX/.X./.X./.X./","...XX/XXXX./...XX/",".X./.X./.X./XXX/X.X/"},
			{"XX.../.XXXX/.X.../.X.../","...X/XXXX/..X./..X./..X./","...X./...X./XXXX./...XX/",".X../.X../.X../XXXX/X.../","...XX/XXXX./...X./...X./","..X./..X./..X./XXXX/...X/",".X.../.X.../.XXXX/XX.../","X.../XXXX/.X../.X../.X../"},
			{"XX.X./.XXXX/..X../","..X/.XX/XX./.XX/.X./","..X../XXXX./.X.XX/",".X./XX./.XX/XX./X../",".X.XX/XXXX./..X../",".X./.XX/XX./.XX/..X/","..X../.XXXX/XX.X./","X../XX./.XX/XX./.X./"},
			{"XX.../.XXXX/..XX./","..X/.XX/XX./XX./.X./",".XX../XXXX./...XX/",".X./.XX/.XX/XX./X../","...XX/XXXX./.XX../",".X./XX./XX./.XX/..X/","..XX./.XXXX/XX.../","X../XX./.XX/.XX/.X./"},
			{"XX..X/.XXXX/..X../","..X/.XX/XX./.X./.XX/","..X../XXXX./X..XX/","XX./.X./.XX/XX./X../","X..XX/XXXX./..X../",".XX/.X./XX./.XX/..X/","..X../.XXXX/XX..X/","X../XX./.XX/.X./XX./"},
			{"XX.../.XXXX/..X.X/","..X/.XX/XX./.X./XX./","X.X../XXXX./...XX/",".XX/.X./.XX/XX./X../","...XX/XXXX./X.X../","XX./.X./XX./.XX/..X/","..X.X/.XXXX/XX.../","X../XX./.XX/.X./.XX/"},
			{"XX.../.XXXX/..X../..X../","...X/..XX/XXX./..X./..X./","..X../..X../XXXX./...XX/",".X../.X../.XXX/XX../X.../","...XX/XXXX./..X../..X../","..X./..X./XXX./..XX/...X/","..X../..X../.XXXX/XX.../","X.../XX../.XXX/.X../.X../"},
			{"XX.XX/.XXXX/",".X/XX/X./XX/XX/","XXXX./XX.XX/","XX/XX/.X/XX/X./","XX.XX/XXXX./","XX/XX/X./XX/.X/",".XXXX/XX.XX/","X./XX/.X/XX/XX/"},
			{"...X./XX.X./.XXXX/",".X./XX./X../XXX/X../","XXXX./.X.XX/.X.../","..X/XXX/..X/.XX/.X./",".X.../.X.XX/XXXX./","X../XXX/X../XX./.X./",".XXXX/XX.X./...X./",".X./.XX/..X/XXX/..X/"},
			{"XX.X./.XXXX/...X./","..X/.XX/.X./XXX/.X./",".X.../XXXX./.X.XX/",".X./XXX/.X./XX./X../",".X.XX/XXXX./.X.../",".X./XXX/.X./.XX/..X/","...X./.XXXX/XX.X./","X../XX./.X./XXX/.X./"},
			{"XX.X./.XXXX/....X/","..X/.XX/.X./.XX/XX./","X..../XXXX./.X.XX/",".XX/XX./.X./XX./X../",".X.XX/XXXX./X..../","XX./.XX/.X./.XX/..X/","....X/.XXXX/XX.X./","X../XX./.X./XX./.XX/"},
			{"XX..X/.XXXX/...X./","..X/.XX/.X./XX./.XX/",".X.../XXXX./X..XX/","XX./.XX/.X./XX./X../","X..XX/XXXX./.X.../",".XX/XX./.X./.XX/..X/","...X./.XXXX/XX..X/","X../XX./.X./.XX/XX./"},
			{"XX.../.XXXX/...XX/","..X/.XX/.X./XX./XX./","XX.../XXXX./...XX/",".XX/.XX/.X./XX./X../","...XX/XXXX./XX.../","XX./XX./.X./.XX/..X/","...XX/.XXXX/XX.../","X../XX./.X./.XX/.XX/"},
			{"XX.../.XXXX/...X./...X./","...X/..XX/..X./XXX./..X./",".X.../.X.../XXXX./...XX/",".X../.XXX/.X../XX../X.../","...XX/XXXX./.X.../.X.../","..X./XXX./..X./..XX/...X/","...X./...X./.XXXX/XX.../","X.../XX../.X../.XXX/.X../"},
			{"XX..XX/.XXXX./",".X/XX/X./X./XX/.X/",".XXXX./XX..XX/","X./XX/.X/.X/XX/X./"},
			{"....X/XX..X/.XXXX/",".X./XX./X../X../XXX/","XXXX./X..XX/X..../","XXX/..X/..X/.XX/.X./","X..../X..XX/XXXX./","XXX/X../X../XX./.X./",".XXXX/XX..X/....X/",".X./.XX/..X/..X/XXX/"},
			{"XX..X/.XXXX/....X/","..X/.XX/.X./.X./XXX/","X..../XXXX./X..XX/","XXX/.X./.X./XX./X../","X..XX/XXXX./X..../","XXX/.X./.X./.XX/..X/","....X/.XXXX/XX..X/","X../XX./.X./.X./XXX/"},
			{"XX..../.XXXX./....XX/","..X/.XX/.X./.X./XX./X../","....XX/.XXXX./XX..../","X../XX./.X./.X./.XX/..X/"},
			{"XX.../.XXXX/....X/....X/","...X/..XX/..X./..X./XXX./","X..../X..../XXXX./...XX/",".XXX/.X../.X../XX../X.../","...XX/XXXX./X..../X..../","XXX./..X./..X./..XX/...X/","....X/....X/.XXXX/XX.../","X.../XX../.X../.X../.XXX/"},
			{"XX.../.XX../.XXXX/","..X/XXX/XX./X../X../","XXXX./..XX./...XX/","..X/..X/.XX/XXX/X../","...XX/..XX./XXXX./","X../X../XX./XXX/..X/",".XXXX/.XX../XX.../","X../XXX/.XX/..X/..X/"},
			{"XX../XX../XXXX/","XXX/XXX/X../X../","XXXX/..XX/..XX/","..X/..X/XXX/XXX/","..XX/..XX/XXXX/","X../X../XXX/XXX/","XXXX/XX../XX../","XXX/XXX/..X/..X/"},
			{"X.../X.../XX../XXXX/","XXXX/XX../X.../X.../","XXXX/..XX/...X/...X/","...X/...X/..XX/XXXX/"},
			{"X.../XXX./XXXX/","XXX/XX./XX./X../","XXXX/.XXX/...X/","..X/.XX/.XX/XXX/","...X/.XXX/XXXX/","X../XX./XX./XXX/","XXXX/XXX./X.../","XXX/.XX/.XX/..X/"},
			{"X.../XX../XXXX/X.../","XXXX/.XX./.X../.X../","...X/XXXX/..XX/...X/","..X./..X./.XX./XXXX/","...X/..XX/XXXX/...X/",".X../.X../.XX./XXXX/","X.../XXXX/XX../X.../","XXXX/.XX./..X./..X./"},
			{"X.../XX../XXXX/.X../",".XXX/XXX./.X../.X../","..X./XXXX/..XX/...X/","..X./..X./.XXX/XXX./","...X/..XX/XXXX/..X./",".X../.X../XXX./.XXX/",".X../XXXX/XX../X.../","XXX./.XXX/..X./..X./"},
			{"X.../XX../XXXX/..X./",".XXX/.XX./XX../.X../",".X../XXXX/..XX/...X/","..X./..XX/.XX./XXX./","...X/..XX/XXXX/.X../",".X../XX../.XX./.XXX/","..X./XXXX/XX../X.../","XXX./.XX./..XX/..X./"},
			{"X.../XX.X/XXXX/","XXX/XX./X../XX./","XXXX/X.XX/...X/",".XX/..X/.XX/XXX/","...X/X.XX/XXXX/","XX./X../XX./XXX/","XXXX/XX.X/X.../","XXX/.XX/..X/.XX/"},
			{"X.../XX../XXXX/...X/",".XXX/.XX./.X../XX../","X.../XXXX/..XX/...X/","..XX/..X./.XX./XXX./","...X/..XX/XXXX/X.../","XX../.X../.XX./.XXX/","...X/XXXX/XX../X.../","XXX./.XX./..X./..XX/"},
			{".X../XXX./XXXX/","XX./XXX/XX./X../","XXXX/.XXX/..X./","..X/.XX/XXX/.XX/","..X./.XXX/XXXX/","X../XX./XXX/XX./","XXXX/XXX./.X../",".XX/XXX/.XX/..X/"},
			{"XXXX/XXXX/","XX/XX/XX/XX/"},
			{"..X./XXX./XXXX/","XX./XX./XXX/X../","XXXX/.XXX/.X../","..X/XXX/.XX/.XX/",".X../.XXX/XXXX/","X../XXX/XX./XX./","XXXX/XXX./..X./",".XX/.XX/XXX/..X/"},
			{"XXX./XXXX/X.../","XXX/.XX/.XX/.X./","...X/XXXX/.XXX/",".X./XX./XX./XXX/",".XXX/XXXX/...X/",".X./.XX/.XX/XXX/","X.../XXXX/XXX./","XXX/XX./XX./.X./"},
			{"XXX./XXXX/.X../",".XX/XXX/.XX/.X./","..X./XXXX/.XXX/",".X./XX./XXX/XX./",".XXX/XXXX/..X./",".X./.XX/XXX/.XX/",".X../XXXX/XXX./","XX./XXX/XX./.X./"},
			{"XXX./XXXX/..X./",".XX/.XX/XXX/.X./",".X../XXXX/.XXX/",".X./XXX/XX./XX./",".XXX/XXXX/.X../",".X./XXX/.XX/.XX/","..X./XXXX/XXX./","XX./XX./XXX/.X./"},
			{"XXX./XXXX/...X/",".XX/.XX/.XX/XX./","X.../XXXX/.XXX/",".XX/XX./XX./XX./",".XXX/XXXX/X.../","XX./.XX/.XX/.XX/","...X/XXXX/XXX./","XX./XX./XX./.XX/"},
			{".XX./XX../XXXX/","XX./XXX/X.X/X../","XXXX/..XX/.XX./","..X/X.X/XXX/.XX/",".XX./..XX/XXXX/","X../X.X/XXX/XX./","XXXX/XX../.XX./",".XX/XXX/X.X/..X/"},
			{".X../.X../XX../XXXX/","XX../XXXX/X.../X.../","XXXX/..XX/..X./..X./","...X/...X/XXXX/..XX/","..X./..X./..XX/XXXX/","X.../X.../XXXX/XX../","XXXX/XX../.X../.X../","..XX/XXXX/...X/...X/"},
			{".X../XX../XXXX/X.../","XXX./.XXX/.X../.X../","...X/XXXX/..XX/..X./","..X./..X./XXX./.XXX/","..X./..XX/XXXX/...X/",".X../.X../.XXX/XXX./","X.../XXXX/XX../.X../",".XXX/XXX./..X./..X./"},
			{".X../XX../XXXX/.X../",".XX./XXXX/.X../.X../","..X./XXXX/..XX/..X./","..X./..X./XXXX/.XX./","..X./..XX/XXXX/..X./",".X../.X../XXXX/.XX./",".X../XXXX/XX../.X../",".XX./XXXX/..X./..X./"},
			{".X../XX../XXXX/..X./",".XX./.XXX/XX../.X../",".X../XXXX/..XX/..X./","..X./..XX/XXX./.XX./","..X./..XX/XXXX/.X../",".X../XX../.XXX/.XX./","..X./XXXX/XX../.X../",".XX./XXX./..XX/..X./"},
			{".X../XX.X/XXXX/","XX./XXX/X../XX./","XXXX/X.XX/..X./",".XX/..X/XXX/.XX/","..X./X.XX/XXXX/","XX./X../XXX/XX./","XXXX/XX.X/.X../",".XX/XXX/..X/.XX/"},
			{".X../XX../XXXX/...X/",".XX./.XXX/.X../XX../","X.../XXXX/..XX/..X./","..XX/..X./XXX./.XX./","..X./..XX/XXXX/X.../","XX../.X../.XXX/.XX./","...X/XXXX/XX../.X../",".XX./XXX./..X./..XX/"},
			{"XX../XXXX/XX../","XXX/XXX/.X./.X./","..XX/XXXX/..XX/",".X./.X./XXX/XXX/"},
			{"XX../XXXX/X.X./","XXX/.XX/XX./.X./",".X.X/XXXX/..XX/",".X./.XX/XX./XXX/","..XX/XXXX/.X.X/",".X./XX./.XX/XXX/","X.X./XXXX/XX../","XXX/XX./.XX/.X./"},
			{"XX.X/XXXX/X.../","XXX/.XX/.X./.XX/","...X/XXXX/X.XX/","XX./.X./XX./XXX/","X.XX/XXXX/...X/",".XX/.X./.XX/XXX/","X.../XXXX/XX.X/","XXX/XX./.X./XX./"},
			{"XX../XXXX/X..X/","XXX/.XX/.X./XX./","X..X/XXXX/..XX/",".XX/.X./XX./XXX/","..XX/XXXX/X..X/","XX./.X./.XX/XXX/","X..X/XXXX/XX../","XXX/XX./.X./.XX/"},
			{"XX../XXXX/.XX./",".XX/XXX/XX./.X./",".XX./XXXX/..XX/",".X./.XX/XXX/XX./","..XX/XXXX/.XX./",".X./XX./XXX/.XX/",".XX./XXXX/XX../","XX./XXX/.XX/.X./"},
			{"XX.X/XXXX/.X../",".XX/XXX/.X./.XX/","..X./XXXX/X.XX/","XX./.X./XXX/XX./","X.XX/XXXX/..X./",".XX/.X./XXX/.XX/",".X../XXXX/XX.X/","XX./XXX/.X./XX./"},
			{"XX../XXXX/.X.X/",".XX/XXX/.X./XX./","X.X./XXXX/..XX/",".XX/.X./XXX/XX./","..XX/XXXX/X.X./","XX./.X./XXX/.XX/",".X.X/XXXX/XX../","XX./XXX/.X./.XX/"},
			{"XX../XXXX/.X../.X../","..XX/XXXX/..X./..X./","..X./..X./XXXX/..XX/",".X../.X../XXXX/XX../"},
			{"XX.X/XXXX/..X./",".XX/.XX/XX./.XX/",".X../XXXX/X.XX/","XX./.XX/XX./XX./","X.XX/XXXX/.X../",".XX/XX./.XX/.XX/","..X./XXXX/XX.X/","XX./XX./.XX/XX./"},
			{"XX../XXXX/..XX/",".XX/.XX/XX./XX./","..XX/XXXX/XX../","XX./XX./.XX/.XX/"},
			{"XX../XXXX/..X./..X./","..XX/..XX/XXX./..X./",".X../.X../XXXX/..XX/",".X../.XXX/XX../XX../","..XX/XXXX/.X../.X../","..X./XXX./..XX/..XX/","..X./..X./XXXX/XX../","XX../XX../.XXX/.X../"},
			{"...X/XX.X/XXXX/","XX./XX./X../XXX/","XXXX/X.XX/X.../","XXX/..X/.XX/.XX/","X.../X.XX/XXXX/","XXX/X../XX./XX./","XXXX/XX.X/...X/",".XX/.XX/..X/XXX/"},
			{"XX.X/XXXX/...X/",".XX/.XX/.X./XXX/","X.../XXXX/X.XX/","XXX/.X./XX./XX./","X.XX/XXXX/X.../","XXX/.X./.XX/.XX/","...X/XXXX/XX.X/","XX./XX./.X./XXX/"},
			{"XX../XXXX/...X/...X/","..XX/..XX/..X./XXX./","X.../X.../XXXX/..XX/",".XXX/.X../XX../XX../","..XX/XXXX/X.../X.../","XXX./..X./..XX/..XX/","...X/...X/XXXX/XX../","XX../XX../.X../.XXX/"},
			{"XXX.../..X.../..XXXX/","..X/..X/XXX/X../X../X../","XXXX../...X../...XXX/","..X/..X/..X/XXX/X../X../","...XXX/...X../XXXX../","X../X../X../XXX/..X/..X/","..XXXX/..X.../XXX.../","X../X../XXX/..X/..X/..X/"},
			{"X..../XX.../.X.../.XXXX/","..XX/XXX./X.../X.../X.../","XXXX./...X./...XX/....X/","...X/...X/...X/.XXX/XX../","....X/...XX/...X./XXXX./","X.../X.../X.../XXX./..XX/",".XXXX/.X.../XX.../X..../","XX../.XXX/...X/...X/...X/"},
			{"XXX../.X.../.XXXX/","..X/XXX/X.X/X../X../","XXXX./...X./..XXX/","..X/..X/X.X/XXX/X../","..XXX/...X./XXXX./","X../X../X.X/XXX/..X/",".XXXX/.X.../XXX../","X../XXX/X.X/..X/..X/"},
			{".X.../XX.../.X.../.XXXX/","..X./XXXX/X.../X.../X.../","XXXX./...X./...XX/...X./","...X/...X/...X/XXXX/.X../","...X./...XX/...X./XXXX./","X.../X.../X.../XXXX/..X./",".XXXX/.X.../XX.../.X.../",".X../XXXX/...X/...X/...X/"},
			{"XX.../.X.../.XXXX/.X.../","...X/XXXX/.X../.X../.X../","...X./XXXX./...X./...XX/","..X./..X./..X./XXXX/X.../","...XX/...X./XXXX./...X./",".X../.X../.X../XXXX/...X/",".X.../.XXXX/.X.../XX.../","X.../XXXX/..X./..X./..X./"},
			{"XX.../.X.../.XXXX/..X../","...X/.XXX/XX../.X../.X../","..X../XXXX./...X./...XX/","..X./..X./..XX/XXX./X.../","...XX/...X./XXXX./..X../",".X../.X../XX../.XXX/...X/","..X../.XXXX/.X.../XX.../","X.../XXX./..XX/..X./..X./"},
			{"XX.../.X.X./.XXXX/","..X/XXX/X../XX./X../","XXXX./.X.X./...XX/","..X/.XX/..X/XXX/X../","...XX/.X.X./XXXX./","X../XX./X../XXX/..X/",".XXXX/.X.X./XX.../","X../XXX/..X/.XX/..X/"},
			{"XX.../.X.../.XXXX/...X./","...X/.XXX/.X../XX../.X../",".X.../XXXX./...X./...XX/","..X./..XX/..X./XXX./X.../","...XX/...X./XXXX./.X.../",".X../XX../.X../.XXX/...X/","...X./.XXXX/.X.../XX.../","X.../XXX./..X./..XX/..X./"},
			{"XX.../.X..X/.XXXX/","..X/XXX/X../X../XX./","XXXX./X..X./...XX/",".XX/..X/..X/XXX/X../","...XX/X..X./XXXX./","XX./X../X../XXX/..X/",".XXXX/.X..X/XX.../","X../XXX/..X/..X/.XX/"},
			{"XX.../.X.../.XXXX/....X/","...X/.XXX/.X../.X../XX../","X..../XXXX./...X./...XX/","..XX/..X./..X./XXX./X.../","...XX/...X./XXXX./X..../","XX../.X../.X../.XXX/...X/","....X/.XXXX/.X.../XX.../","X.../XXX./..X./..X./..XX/"},
			{"X.../XX../X.../XXXX/","XXXX/X.X./X.../X.../","XXXX/...X/..XX/...X/","...X/...X/.X.X/XXXX/","...X/..XX/...X/XXXX/","X.../X.../X.X./XXXX/","XXXX/X.../XX../X.../","XXXX/.X.X/...X/...X/"},
			{"XXX./X.../XXXX/","XXX/X.X/X.X/X../","XXXX/...X/.XXX/","..X/X.X/X.X/XXX/",".XXX/...X/XXXX/","X../X.X/X.X/XXX/","XXXX/X.../XXX./","XXX/X.X/X.X/..X/"},
			{".X../XX../X.../XXXX/","XXX./X.XX/X.../X.../","XXXX/...X/..XX/..X./","...X/...X/XX.X/.XXX/","..X./..XX/...X/XXXX/","X.../X.../X.XX/XXX./","XXXX/X.../XX../.X../",".XXX/XX.X/...X/...X/"},
			{"XX../X.../XXXX/X.../","XXXX/.X.X/.X../.X../","...X/XXXX/...X/..XX/","..X./..X./X.X./XXXX/","..XX/...X/XXXX/...X/",".X../.X../.X.X/XXXX/","X.../XXXX/X.../XX../","XXXX/X.X./..X./..X./"},
			{"XX../X.../XXXX/.X../",".XXX/XX.X/.X../.X../","..X./XXXX/...X/..XX/","..X./..X./X.XX/XXX./","..XX/...X/XXXX/..X./",".X../.X../XX.X/.XXX/",".X../XXXX/X.../XX../","XXX./X.XX/..X./..X./"},
			{"XX../X.X./XXXX/","XXX/X.X/XX./X../","XXXX/.X.X/..XX/","..X/.XX/X.X/XXX/","..XX/.X.X/XXXX/","X../XX./X.X/XXX/","XXXX/X.X./XX../","XXX/X.X/.XX/..X/"},
			{"XX../X.../XXXX/..X./",".XXX/.X.X/XX../.X../",".X../XXXX/...X/..XX/","..X./..XX/X.X./XXX./","..XX/...X/XXXX/.X../",".X../XX../.X.X/.XXX/","..X./XXXX/X.../XX../","XXX./X.X./..XX/..X./"},
			{"XX../X..X/XXXX/","XXX/X.X/X../XX./","XXXX/X..X/..XX/",".XX/..X/X.X/XXX/","..XX/X..X/XXXX/","XX./X../X.X/XXX/","XXXX/X..X/XX../","XXX/X.X/..X/.XX/"},
			{"XX../X.../XXXX/...X/",".XXX/.X.X/.X../XX../","X.../XXXX/...X/..XX/","..XX/..X./X.X./XXX./","..XX/...X/XXXX/X.../","XX../.X../.X.X/.XXX/","...X/XXXX/X.../XX../","XXX./X.X./..X./..XX/"},
			{"XX.../.X.../.X.../.XXXX/","...X/XXXX/X.../X.../X.../","XXXX./...X./...X./...XX/","...X/...X/...X/XXXX/X.../","...XX/...X./...X./XXXX./","X.../X.../X.../XXXX/...X/",".XXXX/.X.../.X.../XX.../","X.../XXXX/...X/...X/...X/"},
			{"XX../X.../X.../XXXX/","XXXX/X..X/X.../X.../","XXXX/...X/...X/..XX/","...X/...X/X..X/XXXX/","..XX/...X/...X/XXXX/","X.../X.../X..X/XXXX/","XXXX/X.../X.../XX../","XXXX/X..X/...X/...X/"},
			{"X.../X.X./XXXX/X.../","XXXX/.X../.XX./.X../","...X/XXXX/.X.X/...X/","..X./.XX./..X./XXXX/","...X/.X.X/XXXX/...X/",".X../.XX./.X../XXXX/","X.../XXXX/X.X./X.../","XXXX/..X./.XX./..X./"},
			{"X.../X.../XXXX/X.X./","XXXX/.X../XX../.X../",".X.X/XXXX/...X/...X/","..X./..XX/..X./XXXX/","...X/...X/XXXX/.X.X/",".X../XX../.X../XXXX/","X.X./XXXX/X.../X.../","XXXX/..X./..XX/..X./"},
			{"X.../X..X/XXXX/X.../","XXXX/.X../.X../.XX./","...X/XXXX/X..X/...X/",".XX./..X./..X./XXXX/","...X/X..X/XXXX/...X/",".XX./.X../.X../XXXX/","X.../XXXX/X..X/X.../","XXXX/..X./..X./.XX./"},
			{"X.../X.../XXXX/X..X/","XXXX/.X../.X../XX../","X..X/XXXX/...X/...X/","..XX/..X./..X./XXXX/","...X/...X/XXXX/X..X/","XX../.X../.X../XXXX/","X..X/XXXX/X.../X.../","XXXX/..X./..X./..XX/"},
			{"X.../X.X./XXXX/.X../",".XXX/XX../.XX./.X../","..X./XXXX/.X.X/...X/","..X./.XX./..XX/XXX./","...X/.X.X/XXXX/..X./",".X../.XX./XX../.XXX/",".X../XXXX/X.X./X.../","XXX./..XX/.XX./..X./"},
			{"X.../X.../XXXX/.XX./",".XXX/XX../XX../.X../",".XX./XXXX/...X/...X/","..X./..XX/..XX/XXX./","...X/...X/XXXX/.XX./",".X../XX../XX../.XXX/",".XX./XXXX/X.../X.../","XXX./..XX/..XX/..X./"},
			{"X.../X..X/XXXX/.X../",".XXX/XX../.X../.XX./","..X./XXXX/X..X/...X/",".XX./..X./..XX/XXX./","...X/X..X/XXXX/..X./",".XX./.X../XX../.XXX/",".X../XXXX/X..X/X.../","XXX./..XX/..X./.XX./"},
			{"X.../X.../XXXX/.X.X/",".XXX/XX../.X../XX../","X.X./XXXX/...X/...X/","..XX/..X./..XX/XXX./","...X/...X/XXXX/X.X./","XX../.X../XX../.XXX/",".X.X/XXXX/X.../X.../","XXX./..XX/..X./..XX/"},
			{"X.../X.../XXXX/.X../.X../","..XXX/XXX../..X../..X../","..X./..X./XXXX/...X/...X/","..X../..X../..XXX/XXX../","...X/...X/XXXX/..X./..X./","..X../..X../XXX../..XXX/",".X../.X../XXXX/X.../X.../","XXX../..XXX/..X../..X../"},
			{"X.X./X.X./XXXX/","XXX/X../XXX/X../","XXXX/.X.X/.X.X/","..X/XXX/..X/XXX/",".X.X/.X.X/XXXX/","X../XXX/X../XXX/","XXXX/X.X./X.X./","XXX/..X/XXX/..X/"},
			{"X.../X.X./XXXX/..X./",".XXX/.X../XXX./.X../",".X../XXXX/.X.X/...X/","..X./.XXX/..X./XXX./","...X/.X.X/XXXX/.X../",".X../XXX./.X../.XXX/","..X./XXXX/X.X./X.../","XXX./..X./.XXX/..X./"},
			{"X.../X.X./XXXX/...X/",".XXX/.X../.XX./XX../","X.../XXXX/.X.X/...X/","..XX/.XX./..X./XXX./","...X/.X.X/XXXX/X.../","XX../.XX./.X../.XXX/","...X/XXXX/X.X./X.../","XXX./..X./.XX./..XX/"},
			{"X.../X..X/XXXX/..X./",".XXX/.X../XX../.XX./",".X../XXXX/X..X/...X/",".XX./..XX/..X./XXX./","...X/X..X/XXXX/.X../",".XX./XX../.X../.XXX/","..X./XXXX/X..X/X.../","XXX./..X./..XX/.XX./"},
			{"X.../X.../XXXX/..X./..X./","..XXX/..X../XXX../..X../",".X../.X../XXXX/...X/...X/","..X../..XXX/..X../XXX../","...X/...X/XXXX/.X../.X../","..X../XXX../..X../..XXX/","..X./..X./XXXX/X.../X.../","XXX../..X../..XXX/..X../"},
			{"X..X/X..X/XXXX/","XXX/X../X../XXX/","XXXX/X..X/X..X/","XXX/..X/..X/XXX/"},
			{"X.../X..X/XXXX/...X/",".XXX/.X../.X../XXX./","X.../XXXX/X..X/...X/",".XXX/..X./..X./XXX./","...X/X..X/XXXX/X.../","XXX./.X../.X../.XXX/","...X/XXXX/X..X/X.../","XXX./..X./..X./.XXX/"},
			{"X.../X.../XXXX/...X/...X/","..XXX/..X../..X../XXX../","...X/...X/XXXX/X.../X.../","XXX../..X../..X../..XXX/"},
			{"..X./X.X./XXXX/X.../","XXX./.X../.XXX/.X../","...X/XXXX/.X.X/.X../","..X./XXX./..X./.XXX/",".X../.X.X/XXXX/...X/",".X../.XXX/.X../XXX./","X.../XXXX/X.X./..X./",".XXX/..X./XXX./..X./"},
			{"X.X./XXXX/X.X./","XXX/.X./XXX/.X./",".X.X/XXXX/.X.X/",".X./XXX/.X./XXX/"},
			{"X.X./XXXX/X..X/","XXX/.X./.XX/XX./","X..X/XXXX/.X.X/",".XX/XX./.X./XXX/",".X.X/XXXX/X..X/","XX./.XX/.X./XXX/","X..X/XXXX/X.X./","XXX/.X./XX./.XX/"},
			{"X..X/XXXX/X..X/","XXX/.X./.X./XXX/"},
			{"..X./X.X./XXXX/.X../",".XX./XX../.XXX/.X../","..X./XXXX/.X.X/.X../","..X./XXX./..XX/.XX./",".X../.X.X/XXXX/..X./",".X../.XXX/XX../.XX./",".X../XXXX/X.X./..X./",".XX./..XX/XXX./..X./"},
			{"X.X./XXXX/.XX./",".XX/XX./XXX/.X./",".XX./XXXX/.X.X/",".X./XXX/.XX/XX./",".X.X/XXXX/.XX./",".X./XXX/XX./.XX/",".XX./XXXX/X.X./","XX./.XX/XXX/.X./"},
			{"X.X./XXXX/.X.X/",".XX/XX./.XX/XX./",".X.X/XXXX/X.X./","XX./.XX/XX./.XX/"},
			{"X.X./XXXX/.X../.X../","..XX/XXX./..XX/..X./","..X./..X./XXXX/.X.X/",".X../XX../.XXX/XX../",".X.X/XXXX/..X./..X./","..X./..XX/XXX./..XX/",".X../.X../XXXX/X.X./","XX../.XXX/XX../.X../"},
			{"X..X/XXXX/.XX./",".XX/XX./XX./.XX/",".XX./XXXX/X..X/","XX./.XX/.XX/XX./"},
			{"X.../XXXX/.XX./.X../","..XX/XXX./.XX./..X./","..X./.XX./XXXX/...X/",".X../.XX./.XXX/XX../","...X/XXXX/.XX./..X./","..X./.XX./XXX./..XX/",".X../.XX./XXXX/X.../","XX../.XXX/.XX./.X../"},
			{"X.../XXXX/.XX./..X./","..XX/.XX./XXX./..X./",".X../.XX./XXXX/...X/",".X../.XXX/.XX./XX../","...X/XXXX/.XX./.X../","..X./XXX./.XX./..XX/","..X./.XX./XXXX/X.../","XX../.XX./.XXX/.X../"},
			{"X..X/XXXX/.X../.X../","..XX/XXX./..X./..XX/","..X./..X./XXXX/X..X/","XX../.X../.XXX/XX../","X..X/XXXX/..X./..X./","..XX/..X./XXX./..XX/",".X../.X../XXXX/X..X/","XX../.XXX/.X../XX../"},
			{"X.../XXXX/.X.X/.X../","..XX/XXX./..X./.XX./","..X./X.X./XXXX/...X/",".XX./.X../.XXX/XX../","...X/XXXX/X.X./..X./",".XX./..X./XXX./..XX/",".X../.X.X/XXXX/X.../","XX../.XXX/.X../.XX./"},
			{"X.../XXXX/.X../XX../","X.XX/XXX./..X./..X./","..XX/..X./XXXX/...X/",".X../.X../.XXX/XX.X/","...X/XXXX/..X./..XX/","..X./..X./XXX./X.XX/","XX../.X../XXXX/X.../","XX.X/.XXX/.X../.X../"},
			{"X.../XXXX/.X../.XX./","..XX/XXX./X.X./..X./",".XX./..X./XXXX/...X/",".X../.X.X/.XXX/XX../","...X/XXXX/..X./.XX./","..X./X.X./XXX./..XX/",".XX./.X../XXXX/X.../","XX../.XXX/.X.X/.X../"},
			{".XX./X.X./XXXX/","XX./X.X/XXX/X../","XXXX/.X.X/.XX./","..X/XXX/X.X/.XX/",".XX./.X.X/XXXX/","X../XXX/X.X/XX./","XXXX/X.X./.XX./",".XX/X.X/XXX/..X/"},
			{"..XX/X.X./XXXX/","XX./X../XXX/X.X/","XXXX/.X.X/XX../","X.X/XXX/..X/.XX/","XX../.X.X/XXXX/","X.X/XXX/X../XX./","XXXX/X.X./..XX/",".XX/..X/XXX/X.X/"},
			{"..X./X.X./XXXX/..X./",".XX./.X../XXXX/.X../",".X../XXXX/.X.X/.X../","..X./XXXX/..X./.XX./",".X../.X.X/XXXX/.X../",".X../XXXX/.X../.XX./","..X./XXXX/X.X./..X./",".XX./..X./XXXX/..X./"},
			{"X.X./XXXX/..X./..X./","..XX/..X./XXXX/..X./",".X../.X../XXXX/.X.X/",".X../XXXX/.X../XX../",".X.X/XXXX/.X../.X../","..X./XXXX/..X./..XX/","..X./..X./XXXX/X.X./","XX../.X../XXXX/.X../"},
			{"X.../XXXX/..X./.XX./","..XX/X.X./XXX./..X./",".XX./.X../XXXX/...X/",".X../.XXX/.X.X/XX../","...X/XXXX/.X../.XX./","..X./XXX./X.X./..XX/",".XX./..X./XXXX/X.../","XX../.X.X/.XXX/.X../"},
			{"X.../XXXX/..X./..XX/","..XX/..X./XXX./X.X./","XX../.X../XXXX/...X/",".X.X/.XXX/.X../XX../","...X/XXXX/.X../XX../","X.X./XXX./..X./..XX/","..XX/..X./XXXX/X.../","XX../.X../.XXX/.X.X/"},
			{"XX../.XX./XXXX/","X.X/XXX/XX./X../","XXXX/.XX./..XX/","..X/.XX/XXX/X.X/","..XX/.XX./XXXX/","X../XX./XXX/X.X/","XXXX/.XX./XX../","X.X/XXX/.XX/..X/"},
			{".XX./.XX./XXXX/","X../XXX/XXX/X../","XXXX/.XX./.XX./","..X/XXX/XXX/..X/"},
			{".X../.XX./XXXX/.X../",".X../XXXX/.XX./.X../","..X./XXXX/.XX./..X./","..X./.XX./XXXX/..X./"},
			{".X../.XX./XXXX/..X./",".X../.XXX/XXX./.X../",".X../XXXX/.XX./..X./","..X./.XXX/XXX./..X./","..X./.XX./XXXX/.X../",".X../XXX./.XXX/.X../","..X./XXXX/.XX./.X../","..X./XXX./.XXX/..X./"},
			{".XX./XXXX/.XX./",".X./XXX/XXX/.X./"},
			{"XXX../..X../.XXXX/","..X/X.X/XXX/X../X../","XXXX./..X../..XXX/","..X/..X/XXX/X.X/X../","..XXX/..X../XXXX./","X../X../XXX/X.X/..X/",".XXXX/..X../XXX../","X../X.X/XXX/..X/..X/"},
			{"X.../XX../.X../XXXX/","X.XX/XXX./X.../X.../","XXXX/..X./..XX/...X/","...X/...X/.XXX/XX.X/","...X/..XX/..X./XXXX/","X.../X.../XXX./X.XX/","XXXX/.X../XX../X.../","XX.X/.XXX/...X/...X/"},
			{"XXX./.X../XXXX/","X.X/XXX/X.X/X../","XXXX/..X./.XXX/","..X/X.X/XXX/X.X/",".XXX/..X./XXXX/","X../X.X/XXX/X.X/","XXXX/.X../XXX./","X.X/XXX/X.X/..X/"},
			{"XX../.X../XXXX/..X./",".X.X/.XXX/XX../.X../",".X../XXXX/..X./..XX/","..X./..XX/XXX./X.X./","..XX/..X./XXXX/.X../",".X../XX../.XXX/.X.X/","..X./XXXX/.X../XX../","X.X./XXX./..XX/..X./"},
			{".XXX/.X../XXXX/","X../XXX/X.X/X.X/","XXXX/..X./XXX./","X.X/X.X/XXX/..X/","XXX./..X./XXXX/","X.X/X.X/XXX/X../","XXXX/.X../.XXX/","..X/XXX/X.X/X.X/"},
			{"..X./.XX./.X../XXXX/","X.../XXX./X.XX/X.../","XXXX/..X./.XX./.X../","...X/XX.X/.XXX/...X/",".X../.XX./..X./XXXX/","X.../X.XX/XXX./X.../","XXXX/.X../.XX./..X./","...X/.XXX/XX.X/...X/"},
			{".XX./.X../XXXX/..X./",".X../.XXX/XX.X/.X../",".X../XXXX/..X./.XX./","..X./X.XX/XXX./..X./",".XX./..X./XXXX/.X../",".X../XX.X/.XXX/.X../","..X./XXXX/.X../.XX./","..X./XXX./X.XX/..X./"},
			{".X../.X../XXXX/..X./..X./","..X../..XXX/XXX../..X../","..X./..X./XXXX/.X../.X../","..X../XXX../..XXX/..X../"},
			{"XX..../.XXX../...XXX/","..X/.XX/.X./XX./X../X../","XXX.../..XXX./....XX/","..X/..X/.XX/.X./XX./X../","....XX/..XXX./XXX.../","X../X../XX./.X./.XX/..X/","...XXX/.XXX../XX..../","X../XX./.X./.XX/..X/..X/"},
			{"XX.../XXX../..XXX/",".XX/.XX/XX./X../X../","XXX../..XXX/...XX/","..X/..X/.XX/XX./XX./","...XX/..XXX/XXX../","X../X../XX./.XX/.XX/","..XXX/XXX../XX.../","XX./XX./.XX/..X/..X/"},
			{"X..../X..../XXX../..XXX/",".XXX/.X../XX../X.../X.../","XXX../..XXX/....X/....X/","...X/...X/..XX/..X./XXX./","....X/....X/..XXX/XXX../","X.../X.../XX../.X../.XXX/","..XXX/XXX../X..../X..../","XXX./..X./..XX/...X/...X/"},
			{"X..../XXX../X.XXX/","XXX/.X./XX./X../X../","XXX.X/..XXX/....X/","..X/..X/.XX/.X./XXX/","....X/..XXX/XXX.X/","X../X../XX./.X./XXX/","X.XXX/XXX../X..../","XXX/.X./.XX/..X/..X/"},
			{"X.X../XXX../..XXX/",".XX/.X./XXX/X../X../","XXX../..XXX/..X.X/","..X/..X/XXX/.X./XX./","..X.X/..XXX/XXX../","X../X../XXX/.X./.XX/","..XXX/XXX../X.X../","XX./.X./XXX/..X/..X/"},
			{"X..../XXX../..XXX/..X../","..XX/..X./XXX./.X../.X../","..X../XXX../..XXX/....X/","..X./..X./.XXX/.X../XX../","....X/..XXX/XXX../..X../",".X../.X../XXX./..X./..XX/","..X../..XXX/XXX../X..../","XX../.X../.XXX/..X./..X./"},
			{"X..../XXX../..XXX/...X./","..XX/..X./.XX./XX../.X../",".X.../XXX../..XXX/....X/","..X./..XX/.XX./.X../XX../","....X/..XXX/XXX../.X.../",".X../XX../.XX./..X./..XX/","...X./..XXX/XXX../X..../","XX../.X../.XX./..XX/..X./"},
			{"X..../XXX.X/..XXX/",".XX/.X./XX./X../XX./","XXX../X.XXX/....X/",".XX/..X/.XX/.X./XX./","....X/X.XXX/XXX../","XX./X../XX./.X./.XX/","..XXX/XXX.X/X..../","XX./.X./.XX/..X/.XX/"},
			{"X..../XXX../..XXX/....X/","..XX/..X./.XX./.X../XX../","....X/..XXX/XXX../X..../","XX../.X../.XX./..X./..XX/"},
			{".X.../XXX../X.XXX/","XX./.XX/XX./X../X../","XXX.X/..XXX/...X./","..X/..X/.XX/XX./.XX/","...X./..XXX/XXX.X/","X../X../XX./.XX/XX./","X.XXX/XXX../.X.../",".XX/XX./.XX/..X/..X/"},
			{"..X../XXX../X.XXX/","XX./.X./XXX/X../X../","XXX.X/..XXX/..X../","..X/..X/XXX/.X./.XX/","..X../..XXX/XXX.X/","X../X../XXX/.X./XX./","X.XXX/XXX../..X../",".XX/.X./XXX/..X/..X/"},
			{".XXX../XX.XXX/","X./XX/.X/XX/X./X./","XXX.XX/..XXX./",".X/.X/XX/X./XX/.X/","..XXX./XXX.XX/","X./X./XX/.X/XX/X./","XX.XXX/.XXX../",".X/XX/X./XX/.X/.X/"},
			{"XXX../X.XXX/X..../","XXX/..X/.XX/.X./.X./","....X/XXX.X/..XXX/",".X./.X./XX./X../XXX/","..XXX/XXX.X/....X/",".X./.X./.XX/..X/XXX/","X..../X.XXX/XXX../","XXX/X../XX./.X./.X./"},
			{"XXX../X.XXX/..X../",".XX/..X/XXX/.X./.X./","..X../XXX.X/..XXX/",".X./.X./XXX/X../XX./","..XXX/XXX.X/..X../",".X./.X./XXX/..X/.XX/","..X../X.XXX/XXX../","XX./X../XXX/.X./.X./"},
			{"XXX../X.XXX/...X./",".XX/..X/.XX/XX./.X./",".X.../XXX.X/..XXX/",".X./.XX/XX./X../XX./","..XXX/XXX.X/.X.../",".X./XX./.XX/..X/.XX/","...X./X.XXX/XXX../","XX./X../XX./.XX/.X./"},
			{"XXX.X/X.XXX/","XX/.X/XX/X./XX/","X.XXX/XXX.X/","XX/X./XX/.X/XX/"},
			{".XX../XXX../..XXX/",".X./.XX/XXX/X../X../","XXX../..XXX/..XX./","..X/..X/XXX/XX./.X./","..XX./..XXX/XXX../","X../X../XXX/.XX/.X./","..XXX/XXX../.XX../",".X./XX./XXX/..X/..X/"},
			{".X.../.X.../XXX../..XXX/",".X../.XXX/XX../X.../X.../","XXX../..XXX/...X./...X./","...X/...X/..XX/XXX./..X./","...X./...X./..XXX/XXX../","X.../X.../XX../.XXX/.X../","..XXX/XXX../.X.../.X.../","..X./XXX./..XX/...X/...X/"},
			{".X.../XXX../..XXX/..X../","..X./..XX/XXX./.X../.X../","..X../XXX../..XXX/...X./","..X./..X./.XXX/XX../.X../","...X./..XXX/XXX../..X../",".X../.X../XXX./..XX/..X./","..X../..XXX/XXX../.X.../",".X../XX../.XXX/..X./..X./"},
			{".X.../XXX../..XXX/...X./","..X./..XX/.XX./XX../.X../","...X./..XXX/XXX../.X.../",".X../XX../.XX./..XX/..X./"},
			{"..XX./XXX../..XXX/",".X./.X./XXX/X.X/X../","XXX../..XXX/.XX../","..X/X.X/XXX/.X./.X./",".XX../..XXX/XXX../","X../X.X/XXX/.X./.X./","..XXX/XXX../..XX./",".X./.X./XXX/X.X/..X/"},
			{"XXX.../..XX../...XXX/","..X/..X/.XX/XX./X../X../","...XXX/..XX../XXX.../","X../X../XX./.XX/..X/..X/"},
			{"X..../XX.../.XX../..XXX/","..XX/.XX./XX../X.../X.../","XXX../..XX./...XX/....X/","...X/...X/..XX/.XX./XX../","....X/...XX/..XX./XXX../","X.../X.../XX../.XX./..XX/","..XXX/.XX../XX.../X..../","XX../.XX./..XX/...X/...X/"},
			{"XXX../.XX../..XXX/","..X/.XX/XXX/X../X../","XXX../..XX./..XXX/","..X/..X/XXX/XX./X../","..XXX/..XX./XXX../","X../X../XXX/.XX/..X/","..XXX/.XX../XXX../","X../XX./XXX/..X/..X/"},
			{".X.../XX.../.XX../..XXX/","..X./.XXX/XX../X.../X.../","XXX../..XX./...XX/...X./","...X/...X/..XX/XXX./.X../","...X./...XX/..XX./XXX../","X.../X.../XX../.XXX/..X./","..XXX/.XX../XX.../.X.../",".X../XXX./..XX/...X/...X/"},
			{"XX.../.XXX./..XXX/","..X/.XX/XX./XX./X../","XXX../.XXX./...XX/","..X/.XX/.XX/XX./X../","...XX/.XXX./XXX../","X../XX./XX./.XX/..X/","..XXX/.XXX./XX.../","X../XX./.XX/.XX/..X/"},
			{"XX.../.XX../..XXX/..X../","...X/..XX/XXX./.X../.X../","..X../XXX../..XX./...XX/","..X./..X./.XXX/XX../X.../","...XX/..XX./XXX../..X../",".X../.X../XXX./..XX/...X/","..X../..XXX/.XX../XX.../","X.../XX../.XXX/..X./..X./"},
			{"XX.../.XX../..XXX/...X./","...X/..XX/.XX./XX../.X../",".X.../XXX../..XX./...XX/","..X./..XX/.XX./XX../X.../","...XX/..XX./XXX../.X.../",".X../XX../.XX./..XX/...X/","...X./..XXX/.XX../XX.../","X.../XX../.XX./..XX/..X./"},
			{"XX.../.XX.X/..XXX/","..X/.XX/XX./X../XX./","XXX../X.XX./...XX/",".XX/..X/.XX/XX./X../","...XX/X.XX./XXX../","XX./X../XX./.XX/..X/","..XXX/.XX.X/XX.../","X../XX./.XX/..X/.XX/"},
			{"XX.../.XX../..XXX/....X/","...X/..XX/.XX./.X../XX../","X..../XXX../..XX./...XX/","..XX/..X./.XX./XX../X.../","...XX/..XX./XXX../X..../","XX../.X../.XX./..XX/...X/","....X/..XXX/.XX../XX.../","X.../XX../.XX./..X./..XX/"},
			{"X.../XX../XX../.XXX/",".XXX/XXX./X.../X.../","XXX./..XX/..XX/...X/","...X/...X/.XXX/XXX./","...X/..XX/..XX/XXX./","X.../X.../XXX./.XXX/",".XXX/XX../XX../X.../","XXX./.XXX/...X/...X/"},
			{"XXX./XX../.XXX/",".XX/XXX/X.X/X../","XXX./..XX/.XXX/","..X/X.X/XXX/XX./",".XXX/..XX/XXX./","X../X.X/XXX/.XX/",".XXX/XX../XXX./","XX./XXX/X.X/..X/"},
			{"XX../XXX./.XXX/",".XX/XXX/XX./X../","XXX./.XXX/..XX/","..X/.XX/XXX/XX./","..XX/.XXX/XXX./","X../XX./XXX/.XX/",".XXX/XXX./XX../","XX./XXX/.XX/..X/"},
			{"XX../XX../.XXX/..X./","..XX/.XXX/XX../.X../",".X../XXX./..XX/..XX/","..X./..XX/XXX./XX../","..XX/..XX/XXX./.X../",".X../XX../.XXX/..XX/","..X./.XXX/XX../XX../","XX../XXX./..XX/..X./"},
			{"XX../XX.X/.XXX/",".XX/XXX/X../XX./","XXX./X.XX/..XX/",".XX/..X/XXX/XX./","..XX/X.XX/XXX./","XX./X../XXX/.XX/",".XXX/XX.X/XX../","XX./XXX/..X/.XX/"},
			{"XX../XX../.XXX/...X/","..XX/.XXX/.X../XX../","X.../XXX./..XX/..XX/","..XX/..X./XXX./XX../","..XX/..XX/XXX./X.../","XX../.X../.XXX/..XX/","...X/.XXX/XX../XX../","XX../XXX./..X./..XX/"},
			{"XX.../.X.../.XX../..XXX/","...X/.XXX/XX../X.../X.../","XXX../..XX./...X./...XX/","...X/...X/..XX/XXX./X.../","...XX/...X./..XX./XXX../","X.../X.../XX../.XXX/...X/","..XXX/.XX../.X.../XX.../","X.../XXX./..XX/...X/...X/"},
			{"XX../X.../XX../.XXX/",".XXX/XX.X/X.../X.../","XXX./..XX/...X/..XX/","...X/...X/X.XX/XXX./","..XX/...X/..XX/XXX./","X.../X.../XX.X/.XXX/",".XXX/XX../X.../XX../","XXX./X.XX/...X/...X/"},
			{"X.X./XXX./.XXX/",".XX/XX./XXX/X../","XXX./.XXX/.X.X/","..X/XXX/.XX/XX./",".X.X/.XXX/XXX./","X../XXX/XX./.XX/",".XXX/XXX./X.X./","XX./.XX/XXX/..X/"},
			{"X.../XXX./.XXX/.X../","..XX/XXX./.XX./.X../","..X./XXX./.XXX/...X/","..X./.XX./.XXX/XX../","...X/.XXX/XXX./..X./",".X../.XX./XXX./..XX/",".X../.XXX/XXX./X.../","XX../.XXX/.XX./..X./"},
			{"X.../XXX./.XXX/..X./","..XX/.XX./XXX./.X../",".X../XXX./.XXX/...X/","..X./.XXX/.XX./XX../","...X/.XXX/XXX./.X../",".X../XXX./.XX./..XX/","..X./.XXX/XXX./X.../","XX../.XX./.XXX/..X./"},
			{"X.../XXX./.XXX/...X/","..XX/.XX./.XX./XX../","...X/.XXX/XXX./X.../","XX../.XX./.XX./..XX/"},
			{"X.../XX../.XXX/.XX./","..XX/XXX./XX../.X../",".XX./XXX./..XX/...X/","..X./..XX/.XXX/XX../","...X/..XX/XXX./.XX./",".X../XX../XXX./..XX/",".XX./.XXX/XX../X.../","XX../.XXX/..XX/..X./"},
			{"X.../XX.X/.XXX/.X../","..XX/XXX./.X../.XX./","..X./XXX./X.XX/...X/",".XX./..X./.XXX/XX../","...X/X.XX/XXX./..X./",".XX./.X../XXX./..XX/",".X../.XXX/XX.X/X.../","XX../.XXX/..X./.XX./"},
			{"X.../XX../.XXX/.X.X/","..XX/XXX./.X../XX../","X.X./XXX./..XX/...X/","..XX/..X./.XXX/XX../","...X/..XX/XXX./X.X./","XX../.X../XXX./..XX/",".X.X/.XXX/XX../X.../","XX../.XXX/..X./..XX/"},
			{"X.../XX../.XXX/XX../","X.XX/XXX./.X../.X../","..XX/XXX./..XX/...X/","..X./..X./.XXX/XX.X/","...X/..XX/XXX./..XX/",".X../.X../XXX./X.XX/","XX../.XXX/XX../X.../","XX.X/.XXX/..X./..X./"},
			{"X.../XX.X/.XXX/..X./","..XX/.XX./XX../.XX./",".X../XXX./X.XX/...X/",".XX./..XX/.XX./XX../","...X/X.XX/XXX./.X../",".XX./XX../.XX./..XX/","..X./.XXX/XX.X/X.../","XX../.XX./..XX/.XX./"},
			{"X.../XX../.XXX/..XX/","..XX/.XX./XX../XX../","XX../XXX./..XX/...X/","..XX/..XX/.XX./XX../","...X/..XX/XXX./XX../","XX../XX../.XX./..XX/","..XX/.XXX/XX../X.../","XX../.XX./..XX/..XX/"},
			{"X..../XX.XX/.XXX./",".XX/XX./X../XX./.X./",".XXX./XX.XX/....X/",".X./.XX/..X/.XX/XX./","....X/XX.XX/.XXX./",".X./XX./X../XX./.XX/",".XXX./XX.XX/X..../","XX./.XX/..X/.XX/.X./"},
			{"X..X/XX.X/.XXX/",".XX/XX./X../XXX/","XXX./X.XX/X..X/","XXX/..X/.XX/XX./","X..X/X.XX/XXX./","XXX/X../XX./.XX/",".XXX/XX.X/X..X/","XX./.XX/..X/XXX/"},
			{"X.../XX.X/.XXX/...X/","..XX/.XX./.X../XXX./","X.../XXX./X.XX/...X/",".XXX/..X./.XX./XX../","...X/X.XX/XXX./X.../","XXX./.X../.XX./..XX/","...X/.XXX/XX.X/X.../","XX../.XX./..X./.XXX/"},
			{"X..../XX.../.XXX./...XX/","..XX/.XX./.X../XX../X.../","XX.../.XXX./...XX/....X/","...X/..XX/..X./.XX./XX../","....X/...XX/.XXX./XX.../","X.../XX../.X../.XX./..XX/","...XX/.XXX./XX.../X..../","XX../.XX./..X./..XX/...X/"},
			{"X.../XX../.XXX/...X/...X/","...XX/..XX./..X../XXX../","X.../X.../XXX./..XX/...X/","..XXX/..X../.XX../XX.../","...X/..XX/XXX./X.../X.../","XXX../..X../..XX./...XX/","...X/...X/.XXX/XX../X.../","XX.../.XX../..X../..XXX/"},
			{".XX./XXX./.XXX/",".X./XXX/XXX/X../","XXX./.XXX/.XX./","..X/XXX/XXX/.X./",".XX./.XXX/XXX./","X../XXX/XXX/.X./",".XXX/XXX./.XX./",".X./XXX/XXX/..X/"},
			{".X../XXX./.XXX/..X./","..X./.XXX/XXX./.X../"},
			{"..XX/XXX./.XXX/",".X./XX./XXX/X.X/","XXX./.XXX/XX../","X.X/XXX/.XX/.X./","XX../.XXX/XXX./","X.X/XXX/XX./.X./",".XXX/XXX./..XX/",".X./.XX/XXX/X.X/"},
			{"..X./XXX./.XXX/.X../",".X../.XXX/XXX./..X./"},
			{".XXX/XX../.XXX/",".X./XXX/X.X/X.X/","XXX./..XX/XXX./","X.X/X.X/XXX/.X./"},
			{"..X./.XX./XX../.XXX/",".X../XXX./X.XX/X.../","XXX./..XX/.XX./.X../","...X/XX.X/.XXX/..X./",".X../.XX./..XX/XXX./","X.../X.XX/XXX./.X../",".XXX/XX../.XX./..X./","..X./.XXX/XX.X/...X/"},
			{".XX./XX../.XXX/..X./","..X./.XXX/XX.X/.X../",".X../XXX./..XX/.XX./","..X./X.XX/XXX./.X../",".XX./..XX/XXX./.X../",".X../XX.X/.XXX/..X./","..X./.XXX/XX../.XX./",".X../XXX./X.XX/..X./"},
			{".XX./XX.X/.XXX/",".X./XXX/X.X/XX./","XXX./X.XX/.XX./",".XX/X.X/XXX/.X./",".XX./X.XX/XXX./","XX./X.X/XXX/.X./",".XXX/XX.X/.XX./",".X./XXX/X.X/.XX/"},
			{".XX./XX../.XXX/...X/","..X./.XXX/.X.X/XX../","X.../XXX./..XX/.XX./","..XX/X.X./XXX./.X../",".XX./..XX/XXX./X.../","XX../.X.X/.XXX/..X./","...X/.XXX/XX../.XX./",".X../XXX./X.X./..XX/"},
			{".X.../XX.XX/.XXX./",".X./XXX/X../XX./.X./",".XXX./XX.XX/...X./",".X./.XX/..X/XXX/.X./","...X./XX.XX/.XXX./",".X./XX./X../XXX/.X./",".XXX./XX.XX/.X.../",".X./XXX/..X/.XX/.X./"},
			{".X.X/XX.X/.XXX/",".X./XXX/X../XXX/","XXX./X.XX/X.X./","XXX/..X/XXX/.X./","X.X./X.XX/XXX./","XXX/X../XXX/.X./",".XXX/XX.X/.X.X/",".X./XXX/..X/XXX/"},
			{".X../XX.X/.XXX/...X/","..X./.XXX/.X../XXX./","X.../XXX./X.XX/..X./",".XXX/..X./XXX./.X../","..X./X.XX/XXX./X.../","XXX./.X../.XXX/..X./","...X/.XXX/XX.X/.X../",".X../XXX./..X./.XXX/"},
			{".X.../XX.../.XXX./...XX/","..X./.XXX/.X../XX../X.../","XX.../.XXX./...XX/...X./","...X/..XX/..X./XXX./.X../","...X./...XX/.XXX./XX.../","X.../XX../.X../.XXX/..X./","...XX/.XXX./XX.../.X.../",".X../XXX./..X./..XX/...X/"},
			{".X../XX../.XXX/...X/...X/","...X./..XXX/..X../XXX../","X.../X.../XXX./..XX/..X./","..XXX/..X../XXX../.X.../","..X./..XX/XXX./X.../X.../","XXX../..X../..XXX/...X./","...X/...X/.XXX/XX../.X../",".X.../XXX../..X../..XXX/"},
			{"XX.X/.XXX/.XX./","..X/XXX/XX./.XX/",".XX./XXX./X.XX/","XX./.XX/XXX/X../","X.XX/XXX./.XX./",".XX/XX./XXX/..X/",".XX./.XXX/XX.X/","X../XXX/.XX/XX./"},
			{"XX../.XXX/.XXX/","..X/XXX/XX./XX./","XXX./XXX./..XX/",".XX/.XX/XXX/X../","..XX/XXX./XXX./","XX./XX./XXX/..X/",".XXX/.XXX/XX../","X../XXX/.XX/.XX/"},
			{"XX.XX/.XXX./.X.../","..X/XXX/.X./.XX/..X/","...X./.XXX./XX.XX/","X../XX./.X./XXX/X../","XX.XX/.XXX./...X./","..X/.XX/.X./XXX/..X/",".X.../.XXX./XX.XX/","X../XXX/.X./XX./X../"},
			{"...X/XX.X/.XXX/.X../","..X./XXX./.X../.XXX/","..X./XXX./X.XX/X.../","XXX./..X./.XXX/.X../","X.../X.XX/XXX./..X./",".XXX/.X../XXX./..X./",".X../.XXX/XX.X/...X/",".X../.XXX/..X./XXX./"},
			{"XX.X/.XXX/.X.X/","..X/XXX/.X./XXX/","X.X./XXX./X.XX/","XXX/.X./XXX/X../","X.XX/XXX./X.X./","XXX/.X./XXX/..X/",".X.X/.XXX/XX.X/","X../XXX/.X./XXX/"},
			{"XX.X/.XXX/XX../","X.X/XXX/.X./.XX/","..XX/XXX./X.XX/","XX./.X./XXX/X.X/","X.XX/XXX./..XX/",".XX/.X./XXX/X.X/","XX../.XXX/XX.X/","X.X/XXX/.X./XX./"},
			{"XX.../.XXX./.X.XX/","..X/XXX/.X./XX./X../","XX.X./.XXX./...XX/","..X/.XX/.X./XXX/X../","...XX/.XXX./XX.X./","X../XX./.X./XXX/..X/",".X.XX/.XXX./XX.../","X../XXX/.X./.XX/..X/"},
			{"XX../.XXX/.X.X/...X/","...X/.XXX/..X./XXX./","X.../X.X./XXX./..XX/",".XXX/.X../XXX./X.../","..XX/XXX./X.X./X.../","XXX./..X./.XXX/...X/","...X/.X.X/.XXX/XX../","X.../XXX./.X../.XXX/"},
			{"XX.XX/.XXX./..X../","..X/.XX/XX./.XX/..X/","..X../.XXX./XX.XX/","X../XX./.XX/XX./X../"},
			{"XX.X/.XXX/..XX/","..X/.XX/XX./XXX/","XX../XXX./X.XX/","XXX/.XX/XX./X../","X.XX/XXX./XX../","XXX/XX./.XX/..X/","..XX/.XXX/XX.X/","X../XX./.XX/XXX/"},
			{"XX../.XXX/..XX/...X/","...X/..XX/.XX./XXX./","X.../XX../XXX./..XX/",".XXX/.XX./XX../X.../","..XX/XXX./XX../X.../","XXX./.XX./..XX/...X/","...X/..XX/.XXX/XX../","X.../XX../.XX./.XXX/"},
			{"..XX/XX.X/.XXX/",".X./XX./X.X/XXX/","XXX./X.XX/XX../","XXX/X.X/.XX/.X./","XX../X.XX/XXX./","XXX/X.X/XX./.X./",".XXX/XX.X/..XX/",".X./.XX/X.X/XXX/"},
			{"...XX/XX.X./.XXX./",".X./XX./X../XXX/..X/",".XXX./.X.XX/XX.../","X../XXX/..X/.XX/.X./","XX.../.X.XX/.XXX./","..X/XXX/X../XX./.X./",".XXX./XX.X./...XX/",".X./.XX/..X/XXX/X../"},
			{"XX../.XXX/...X/..XX/","...X/..XX/X.X./XXX./","XX../X.../XXX./..XX/",".XXX/.X.X/XX../X.../","..XX/XXX./X.../XX../","XXX./X.X./..XX/...X/","..XX/...X/.XXX/XX../","X.../XX../.X.X/.XXX/"},
			{"XX.../.XXX./...X./...XX/","...X/..XX/..X./XXX./X.../","XX.../.X.../.XXX./...XX/","...X/.XXX/.X../XX../X.../","...XX/.XXX./.X.../XX.../","X.../XXX./..X./..XX/...X/","...XX/...X./.XXX./XX.../","X.../XX../.X../.XXX/...X/"},
			{"XXX./.XX./.XXX/","..X/XXX/XXX/X../",".XXX/.XX./XXX./","X../XXX/XXX/..X/"},
			{"XX../.XX./.XXX/...X/","...X/.XXX/.XX./XX../","X.../XXX./.XX./..XX/","..XX/.XX./XXX./X.../"},
			{"XXX/XX./XXX/","XXX/XXX/X.X/","XXX/.XX/XXX/","X.X/XXX/XXX/"},
			{"XX./XXX/XXX/","XXX/XXX/XX./","XXX/XXX/.XX/",".XX/XXX/XXX/"},
			{"X..../XXX../..X../..XXX/","..XX/..X./XXX./X.../X.../","XXX../..X../..XXX/....X/","...X/...X/.XXX/.X../XX../","....X/..XXX/..X../XXX../","X.../X.../XXX./..X./..XX/","..XXX/..X../XXX../X..../","XX../.X../.XXX/...X/...X/"},
			{"XXX../X.X../..XXX/",".XX/..X/XXX/X../X../","XXX../..X.X/..XXX/","..X/..X/XXX/X../XX./","..XXX/..X.X/XXX../","X../X../XXX/..X/.XX/","..XXX/X.X../XXX../","XX./X../XXX/..X/..X/"},
			{"XXX./.X.X/.XXX/","..X/XXX/X.X/XX./","XXX./X.X./.XXX/",".XX/X.X/XXX/X../",".XXX/X.X./XXX./","XX./X.X/XXX/..X/",".XXX/.X.X/XXX./","X../XXX/X.X/.XX/"},
			{"XXX./.X../.XXX/...X/","...X/.XXX/.X.X/XX../","X.../XXX./..X./.XXX/","..XX/X.X./XXX./X.../",".XXX/..X./XXX./X.../","XX../.X.X/.XXX/...X/","...X/.XXX/.X../XXX./","X.../XXX./X.X./..XX/"},
			{"XX.X/.X.X/.XXX/","..X/XXX/X../XXX/","XXX./X.X./X.XX/","XXX/..X/XXX/X../","X.XX/X.X./XXX./","XXX/X../XXX/..X/",".XXX/.X.X/XX.X/","X../XXX/..X/XXX/"},
			{"XXX/X.X/XXX/"},
			{"XX.../.XX../..XX./...XX/","...X/..XX/.XX./XX../X.../","...XX/..XX./.XX../XX.../","X.../XX../.XX./..XX/...X/"},
		};
		return shape;
	}


	public String[][] setup9() {
		String[][] shape = new String[1285][];
		shape[0] = new String[]{"XXXXXXXXX/","X/X/X/X/X/X/X/X/X/"};
		shape[1] = new String[]{"X......./XXXXXXXX/","XX/X./X./X./X./X./X./X./","XXXXXXXX/.......X/",".X/.X/.X/.X/.X/.X/.X/XX/",".......X/XXXXXXXX/","X./X./X./X./X./X./X./XX/","XXXXXXXX/X......./","XX/.X/.X/.X/.X/.X/.X/.X/"};
		shape[2] = new String[]{".X....../XXXXXXXX/","X./XX/X./X./X./X./X./X./","XXXXXXXX/......X./",".X/.X/.X/.X/.X/.X/XX/.X/","......X./XXXXXXXX/","X./X./X./X./X./X./XX/X./","XXXXXXXX/.X....../",".X/XX/.X/.X/.X/.X/.X/.X/"};
		shape[3] = new String[]{"..X...../XXXXXXXX/","X./X./XX/X./X./X./X./X./","XXXXXXXX/.....X../",".X/.X/.X/.X/.X/XX/.X/.X/",".....X../XXXXXXXX/","X./X./X./X./X./XX/X./X./","XXXXXXXX/..X...../",".X/.X/XX/.X/.X/.X/.X/.X/"};
		shape[4] = new String[]{"...X..../XXXXXXXX/","X./X./X./XX/X./X./X./X./","XXXXXXXX/....X.../",".X/.X/.X/.X/XX/.X/.X/.X/","....X.../XXXXXXXX/","X./X./X./X./XX/X./X./X./","XXXXXXXX/...X..../",".X/.X/.X/XX/.X/.X/.X/.X/"};
		shape[5] = new String[]{"XX....../.XXXXXXX/",".X/XX/X./X./X./X./X./X./","XXXXXXX./......XX/",".X/.X/.X/.X/.X/.X/XX/X./","......XX/XXXXXXX./","X./X./X./X./X./X./XX/.X/",".XXXXXXX/XX....../","X./XX/.X/.X/.X/.X/.X/.X/"};
		shape[6] = new String[]{"XX...../XXXXXXX/","XX/XX/X./X./X./X./X./","XXXXXXX/.....XX/",".X/.X/.X/.X/.X/XX/XX/",".....XX/XXXXXXX/","X./X./X./X./X./XX/XX/","XXXXXXX/XX...../","XX/XX/.X/.X/.X/.X/.X/"};
		shape[7] = new String[]{"X....../X....../XXXXXXX/","XXX/X../X../X../X../X../X../","XXXXXXX/......X/......X/","..X/..X/..X/..X/..X/..X/XXX/","......X/......X/XXXXXXX/","X../X../X../X../X../X../XXX/","XXXXXXX/X....../X....../","XXX/..X/..X/..X/..X/..X/..X/"};
		shape[8] = new String[]{"X....../XXXXXXX/X....../","XXX/.X./.X./.X./.X./.X./.X./","......X/XXXXXXX/......X/",".X./.X./.X./.X./.X./.X./XXX/"};
		shape[9] = new String[]{"X....../XXXXXXX/.X...../",".XX/XX./.X./.X./.X./.X./.X./",".....X./XXXXXXX/......X/",".X./.X./.X./.X./.X./.XX/XX./","......X/XXXXXXX/.....X./",".X./.X./.X./.X./.X./XX./.XX/",".X...../XXXXXXX/X....../","XX./.XX/.X./.X./.X./.X./.X./"};
		shape[10] = new String[]{"X.X..../XXXXXXX/","XX/X./XX/X./X./X./X./","XXXXXXX/....X.X/",".X/.X/.X/.X/XX/.X/XX/","....X.X/XXXXXXX/","X./X./X./X./XX/X./XX/","XXXXXXX/X.X..../","XX/.X/XX/.X/.X/.X/.X/"};
		shape[11] = new String[]{"X....../XXXXXXX/..X..../",".XX/.X./XX./.X./.X./.X./.X./","....X../XXXXXXX/......X/",".X./.X./.X./.X./.XX/.X./XX./","......X/XXXXXXX/....X../",".X./.X./.X./.X./XX./.X./.XX/","..X..../XXXXXXX/X....../","XX./.X./.XX/.X./.X./.X./.X./"};
		shape[12] = new String[]{"X..X.../XXXXXXX/","XX/X./X./XX/X./X./X./","XXXXXXX/...X..X/",".X/.X/.X/XX/.X/.X/XX/","...X..X/XXXXXXX/","X./X./X./XX/X./X./XX/","XXXXXXX/X..X.../","XX/.X/.X/XX/.X/.X/.X/"};
		shape[13] = new String[]{"X....../XXXXXXX/...X.../",".XX/.X./.X./XX./.X./.X./.X./","...X.../XXXXXXX/......X/",".X./.X./.X./.XX/.X./.X./XX./","......X/XXXXXXX/...X.../",".X./.X./.X./XX./.X./.X./.XX/","...X.../XXXXXXX/X....../","XX./.X./.X./.XX/.X./.X./.X./"};
		shape[14] = new String[]{"X...X../XXXXXXX/","XX/X./X./X./XX/X./X./","XXXXXXX/..X...X/",".X/.X/XX/.X/.X/.X/XX/","..X...X/XXXXXXX/","X./X./XX/X./X./X./XX/","XXXXXXX/X...X../","XX/.X/.X/.X/XX/.X/.X/"};
		shape[15] = new String[]{"X....../XXXXXXX/....X../",".XX/.X./.X./.X./XX./.X./.X./","..X..../XXXXXXX/......X/",".X./.X./.XX/.X./.X./.X./XX./","......X/XXXXXXX/..X..../",".X./.X./XX./.X./.X./.X./.XX/","....X../XXXXXXX/X....../","XX./.X./.X./.X./.XX/.X./.X./"};
		shape[16] = new String[]{"X....X./XXXXXXX/","XX/X./X./X./X./XX/X./","XXXXXXX/.X....X/",".X/XX/.X/.X/.X/.X/XX/",".X....X/XXXXXXX/","X./XX/X./X./X./X./XX/","XXXXXXX/X....X./","XX/.X/.X/.X/.X/XX/.X/"};
		shape[17] = new String[]{"X....../XXXXXXX/.....X./",".XX/.X./.X./.X./.X./XX./.X./",".X...../XXXXXXX/......X/",".X./.XX/.X./.X./.X./.X./XX./","......X/XXXXXXX/.X...../",".X./XX./.X./.X./.X./.X./.XX/",".....X./XXXXXXX/X....../","XX./.X./.X./.X./.X./.XX/.X./"};
		shape[18] = new String[]{"X.....X/XXXXXXX/","XX/X./X./X./X./X./XX/","XXXXXXX/X.....X/","XX/.X/.X/.X/.X/.X/XX/"};
		shape[19] = new String[]{"X....../XXXXXXX/......X/",".XX/.X./.X./.X./.X./.X./XX./","......X/XXXXXXX/X....../","XX./.X./.X./.X./.X./.X./.XX/"};
		shape[20] = new String[]{".XX..../XXXXXXX/","X./XX/XX/X./X./X./X./","XXXXXXX/....XX./",".X/.X/.X/.X/XX/XX/.X/","....XX./XXXXXXX/","X./X./X./X./XX/XX/X./","XXXXXXX/.XX..../",".X/XX/XX/.X/.X/.X/.X/"};
		shape[21] = new String[]{".X...../.X...../XXXXXXX/","X../XXX/X../X../X../X../X../","XXXXXXX/.....X./.....X./","..X/..X/..X/..X/..X/XXX/..X/",".....X./.....X./XXXXXXX/","X../X../X../X../X../XXX/X../","XXXXXXX/.X...../.X...../","..X/XXX/..X/..X/..X/..X/..X/"};
		shape[22] = new String[]{".X...../XXXXXXX/.X...../",".X./XXX/.X./.X./.X./.X./.X./",".....X./XXXXXXX/.....X./",".X./.X./.X./.X./.X./XXX/.X./"};
		shape[23] = new String[]{".X...../XXXXXXX/..X..../",".X./.XX/XX./.X./.X./.X./.X./","....X../XXXXXXX/.....X./",".X./.X./.X./.X./.XX/XX./.X./",".....X./XXXXXXX/....X../",".X./.X./.X./.X./XX./.XX/.X./","..X..../XXXXXXX/.X...../",".X./XX./.XX/.X./.X./.X./.X./"};
		shape[24] = new String[]{".X.X.../XXXXXXX/","X./XX/X./XX/X./X./X./","XXXXXXX/...X.X./",".X/.X/.X/XX/.X/XX/.X/","...X.X./XXXXXXX/","X./X./X./XX/X./XX/X./","XXXXXXX/.X.X.../",".X/XX/.X/XX/.X/.X/.X/"};
		shape[25] = new String[]{".X...../XXXXXXX/...X.../",".X./.XX/.X./XX./.X./.X./.X./","...X.../XXXXXXX/.....X./",".X./.X./.X./.XX/.X./XX./.X./",".....X./XXXXXXX/...X.../",".X./.X./.X./XX./.X./.XX/.X./","...X.../XXXXXXX/.X...../",".X./XX./.X./.XX/.X./.X./.X./"};
		shape[26] = new String[]{".X..X../XXXXXXX/","X./XX/X./X./XX/X./X./","XXXXXXX/..X..X./",".X/.X/XX/.X/.X/XX/.X/","..X..X./XXXXXXX/","X./X./XX/X./X./XX/X./","XXXXXXX/.X..X../",".X/XX/.X/.X/XX/.X/.X/"};
		shape[27] = new String[]{".X...../XXXXXXX/....X../",".X./.XX/.X./.X./XX./.X./.X./","..X..../XXXXXXX/.....X./",".X./.X./.XX/.X./.X./XX./.X./",".....X./XXXXXXX/..X..../",".X./.X./XX./.X./.X./.XX/.X./","....X../XXXXXXX/.X...../",".X./XX./.X./.X./.XX/.X./.X./"};
		shape[28] = new String[]{".X...X./XXXXXXX/","X./XX/X./X./X./XX/X./","XXXXXXX/.X...X./",".X/XX/.X/.X/.X/XX/.X/"};
		shape[29] = new String[]{".X...../XXXXXXX/.....X./",".X./.XX/.X./.X./.X./XX./.X./",".....X./XXXXXXX/.X...../",".X./XX./.X./.X./.X./.XX/.X./"};
		shape[30] = new String[]{"..XX.../XXXXXXX/","X./X./XX/XX/X./X./X./","XXXXXXX/...XX../",".X/.X/.X/XX/XX/.X/.X/","...XX../XXXXXXX/","X./X./X./XX/XX/X./X./","XXXXXXX/..XX.../",".X/.X/XX/XX/.X/.X/.X/"};
		shape[31] = new String[]{"..X..../..X..../XXXXXXX/","X../X../XXX/X../X../X../X../","XXXXXXX/....X../....X../","..X/..X/..X/..X/XXX/..X/..X/","....X../....X../XXXXXXX/","X../X../X../X../XXX/X../X../","XXXXXXX/..X..../..X..../","..X/..X/XXX/..X/..X/..X/..X/"};
		shape[32] = new String[]{"..X..../XXXXXXX/..X..../",".X./.X./XXX/.X./.X./.X./.X./","....X../XXXXXXX/....X../",".X./.X./.X./.X./XXX/.X./.X./"};
		shape[33] = new String[]{"..X..../XXXXXXX/...X.../",".X./.X./.XX/XX./.X./.X./.X./","...X.../XXXXXXX/....X../",".X./.X./.X./.XX/XX./.X./.X./","....X../XXXXXXX/...X.../",".X./.X./.X./XX./.XX/.X./.X./","...X.../XXXXXXX/..X..../",".X./.X./XX./.XX/.X./.X./.X./"};
		shape[34] = new String[]{"..X.X../XXXXXXX/","X./X./XX/X./XX/X./X./","XXXXXXX/..X.X../",".X/.X/XX/.X/XX/.X/.X/"};
		shape[35] = new String[]{"..X..../XXXXXXX/....X../",".X./.X./.XX/.X./XX./.X./.X./","....X../XXXXXXX/..X..../",".X./.X./XX./.X./.XX/.X./.X./"};
		shape[36] = new String[]{"...X.../...X.../XXXXXXX/","X../X../X../XXX/X../X../X../","XXXXXXX/...X.../...X.../","..X/..X/..X/XXX/..X/..X/..X/"};
		shape[37] = new String[]{"...X.../XXXXXXX/...X.../",".X./.X./.X./XXX/.X./.X./.X./"};
		shape[38] = new String[]{"XXX...../..XXXXXX/",".X/.X/XX/X./X./X./X./X./","XXXXXX../.....XXX/",".X/.X/.X/.X/.X/XX/X./X./",".....XXX/XXXXXX../","X./X./X./X./X./XX/.X/.X/","..XXXXXX/XXX...../","X./X./XX/.X/.X/.X/.X/.X/"};
		shape[39] = new String[]{"X....../XX...../.XXXXXX/",".XX/XX./X../X../X../X../X../","XXXXXX./.....XX/......X/","..X/..X/..X/..X/..X/.XX/XX./","......X/.....XX/XXXXXX./","X../X../X../X../X../XX./.XX/",".XXXXXX/XX...../X....../","XX./.XX/..X/..X/..X/..X/..X/"};
		shape[40] = new String[]{"XXX..../.XXXXXX/",".X/XX/XX/X./X./X./X./","XXXXXX./....XXX/",".X/.X/.X/.X/XX/XX/X./","....XXX/XXXXXX./","X./X./X./X./XX/XX/.X/",".XXXXXX/XXX..../","X./XX/XX/.X/.X/.X/.X/"};
		shape[41] = new String[]{".X...../XX...../.XXXXXX/",".X./XXX/X../X../X../X../X../","XXXXXX./.....XX/.....X./","..X/..X/..X/..X/..X/XXX/.X./",".....X./.....XX/XXXXXX./","X../X../X../X../X../XXX/.X./",".XXXXXX/XX...../.X...../",".X./XXX/..X/..X/..X/..X/..X/"};
		shape[42] = new String[]{"XX...../.XXXXXX/.X...../","..X/XXX/.X./.X./.X./.X./.X./",".....X./XXXXXX./.....XX/",".X./.X./.X./.X./.X./XXX/X../",".....XX/XXXXXX./.....X./",".X./.X./.X./.X./.X./XXX/..X/",".X...../.XXXXXX/XX...../","X../XXX/.X./.X./.X./.X./.X./"};
		shape[43] = new String[]{"XX...../.XXXXXX/..X..../","..X/.XX/XX./.X./.X./.X./.X./","....X../XXXXXX./.....XX/",".X./.X./.X./.X./.XX/XX./X../",".....XX/XXXXXX./....X../",".X./.X./.X./.X./XX./.XX/..X/","..X..../.XXXXXX/XX...../","X../XX./.XX/.X./.X./.X./.X./"};
		shape[44] = new String[]{"XX.X.../.XXXXXX/",".X/XX/X./XX/X./X./X./","XXXXXX./...X.XX/",".X/.X/.X/XX/.X/XX/X./","...X.XX/XXXXXX./","X./X./X./XX/X./XX/.X/",".XXXXXX/XX.X.../","X./XX/.X/XX/.X/.X/.X/"};
		shape[45] = new String[]{"XX...../.XXXXXX/...X.../","..X/.XX/.X./XX./.X./.X./.X./","...X.../XXXXXX./.....XX/",".X./.X./.X./.XX/.X./XX./X../",".....XX/XXXXXX./...X.../",".X./.X./.X./XX./.X./.XX/..X/","...X.../.XXXXXX/XX...../","X../XX./.X./.XX/.X./.X./.X./"};
		shape[46] = new String[]{"XX..X../.XXXXXX/",".X/XX/X./X./XX/X./X./","XXXXXX./..X..XX/",".X/.X/XX/.X/.X/XX/X./","..X..XX/XXXXXX./","X./X./XX/X./X./XX/.X/",".XXXXXX/XX..X../","X./XX/.X/.X/XX/.X/.X/"};
		shape[47] = new String[]{"XX...../.XXXXXX/....X../","..X/.XX/.X./.X./XX./.X./.X./","..X..../XXXXXX./.....XX/",".X./.X./.XX/.X./.X./XX./X../",".....XX/XXXXXX./..X..../",".X./.X./XX./.X./.X./.XX/..X/","....X../.XXXXXX/XX...../","X../XX./.X./.X./.XX/.X./.X./"};
		shape[48] = new String[]{"XX...X./.XXXXXX/",".X/XX/X./X./X./XX/X./","XXXXXX./.X...XX/",".X/XX/.X/.X/.X/XX/X./",".X...XX/XXXXXX./","X./XX/X./X./X./XX/.X/",".XXXXXX/XX...X./","X./XX/.X/.X/.X/XX/.X/"};
		shape[49] = new String[]{"XX...../.XXXXXX/.....X./","..X/.XX/.X./.X./.X./XX./.X./",".X...../XXXXXX./.....XX/",".X./.XX/.X./.X./.X./XX./X../",".....XX/XXXXXX./.X...../",".X./XX./.X./.X./.X./.XX/..X/",".....X./.XXXXXX/XX...../","X../XX./.X./.X./.X./.XX/.X./"};
		shape[50] = new String[]{"XX....X/.XXXXXX/",".X/XX/X./X./X./X./XX/","XXXXXX./X....XX/","XX/.X/.X/.X/.X/XX/X./","X....XX/XXXXXX./","XX/X./X./X./X./XX/.X/",".XXXXXX/XX....X/","X./XX/.X/.X/.X/.X/XX/"};
		shape[51] = new String[]{"XX...../.XXXXXX/......X/","..X/.XX/.X./.X./.X./.X./XX./","X....../XXXXXX./.....XX/",".XX/.X./.X./.X./.X./XX./X../",".....XX/XXXXXX./X....../","XX./.X./.X./.X./.X./.XX/..X/","......X/.XXXXXX/XX...../","X../XX./.X./.X./.X./.X./.XX/"};
		shape[52] = new String[]{"X...../XX..../XXXXXX/","XXX/XX./X../X../X../X../","XXXXXX/....XX/.....X/","..X/..X/..X/..X/.XX/XXX/",".....X/....XX/XXXXXX/","X../X../X../X../XX./XXX/","XXXXXX/XX..../X...../","XXX/.XX/..X/..X/..X/..X/"};
		shape[53] = new String[]{"XXX.../XXXXXX/","XX/XX/XX/X./X./X./","XXXXXX/...XXX/",".X/.X/.X/XX/XX/XX/","...XXX/XXXXXX/","X./X./X./XX/XX/XX/","XXXXXX/XXX.../","XX/XX/XX/.X/.X/.X/"};
		shape[54] = new String[]{".X..../XX..../XXXXXX/","XX./XXX/X../X../X../X../","XXXXXX/....XX/....X./","..X/..X/..X/..X/XXX/.XX/","....X./....XX/XXXXXX/","X../X../X../X../XXX/XX./","XXXXXX/XX..../.X..../",".XX/XXX/..X/..X/..X/..X/"};
		shape[55] = new String[]{"XX..../XXXXXX/X...../","XXX/.XX/.X./.X./.X./.X./",".....X/XXXXXX/....XX/",".X./.X./.X./.X./XX./XXX/","....XX/XXXXXX/.....X/",".X./.X./.X./.X./.XX/XXX/","X...../XXXXXX/XX..../","XXX/XX./.X./.X./.X./.X./"};
		shape[56] = new String[]{"XX..../XXXXXX/.X..../",".XX/XXX/.X./.X./.X./.X./","....X./XXXXXX/....XX/",".X./.X./.X./.X./XXX/XX./","....XX/XXXXXX/....X./",".X./.X./.X./.X./XXX/.XX/",".X..../XXXXXX/XX..../","XX./XXX/.X./.X./.X./.X./"};
		shape[57] = new String[]{"XX..../XXXXXX/..X.../",".XX/.XX/XX./.X./.X./.X./","...X../XXXXXX/....XX/",".X./.X./.X./.XX/XX./XX./","....XX/XXXXXX/...X../",".X./.X./.X./XX./.XX/.XX/","..X.../XXXXXX/XX..../","XX./XX./.XX/.X./.X./.X./"};
		shape[58] = new String[]{"XX.X../XXXXXX/","XX/XX/X./XX/X./X./","XXXXXX/..X.XX/",".X/.X/XX/.X/XX/XX/","..X.XX/XXXXXX/","X./X./XX/X./XX/XX/","XXXXXX/XX.X../","XX/XX/.X/XX/.X/.X/"};
		shape[59] = new String[]{"XX..../XXXXXX/...X../",".XX/.XX/.X./XX./.X./.X./","..X.../XXXXXX/....XX/",".X./.X./.XX/.X./XX./XX./","....XX/XXXXXX/..X.../",".X./.X./XX./.X./.XX/.XX/","...X../XXXXXX/XX..../","XX./XX./.X./.XX/.X./.X./"};
		shape[60] = new String[]{"XX..X./XXXXXX/","XX/XX/X./X./XX/X./","XXXXXX/.X..XX/",".X/XX/.X/.X/XX/XX/",".X..XX/XXXXXX/","X./XX/X./X./XX/XX/","XXXXXX/XX..X./","XX/XX/.X/.X/XX/.X/"};
		shape[61] = new String[]{"XX..../XXXXXX/....X./",".XX/.XX/.X./.X./XX./.X./",".X..../XXXXXX/....XX/",".X./.XX/.X./.X./XX./XX./","....XX/XXXXXX/.X..../",".X./XX./.X./.X./.XX/.XX/","....X./XXXXXX/XX..../","XX./XX./.X./.X./.XX/.X./"};
		shape[62] = new String[]{"XX...X/XXXXXX/","XX/XX/X./X./X./XX/","XXXXXX/X...XX/","XX/.X/.X/.X/XX/XX/","X...XX/XXXXXX/","XX/X./X./X./XX/XX/","XXXXXX/XX...X/","XX/XX/.X/.X/.X/XX/"};
		shape[63] = new String[]{"XX..../XXXXXX/.....X/",".XX/.XX/.X./.X./.X./XX./","X...../XXXXXX/....XX/",".XX/.X./.X./.X./XX./XX./","....XX/XXXXXX/X...../","XX./.X./.X./.X./.XX/.XX/",".....X/XXXXXX/XX..../","XX./XX./.X./.X./.X./.XX/"};
		shape[64] = new String[]{"XX...../.X...../.XXXXXX/","..X/XXX/X../X../X../X../X../","XXXXXX./.....X./.....XX/","..X/..X/..X/..X/..X/XXX/X../",".....XX/.....X./XXXXXX./","X../X../X../X../X../XXX/..X/",".XXXXXX/.X...../XX...../","X../XXX/..X/..X/..X/..X/..X/"};
		shape[65] = new String[]{"XX..../X...../XXXXXX/","XXX/X.X/X../X../X../X../","XXXXXX/.....X/....XX/","..X/..X/..X/..X/X.X/XXX/","....XX/.....X/XXXXXX/","X../X../X../X../X.X/XXX/","XXXXXX/X...../XX..../","XXX/X.X/..X/..X/..X/..X/"};
		shape[66] = new String[]{"X...../X...../X...../XXXXXX/","XXXX/X.../X.../X.../X.../X.../","XXXXXX/.....X/.....X/.....X/","...X/...X/...X/...X/...X/XXXX/",".....X/.....X/.....X/XXXXXX/","X.../X.../X.../X.../X.../XXXX/","XXXXXX/X...../X...../X...../","XXXX/...X/...X/...X/...X/...X/"};
		shape[67] = new String[]{"X...../X...../XXXXXX/X...../","XXXX/.X../.X../.X../.X../.X../",".....X/XXXXXX/.....X/.....X/","..X./..X./..X./..X./..X./XXXX/",".....X/.....X/XXXXXX/.....X/",".X../.X../.X../.X../.X../XXXX/","X...../XXXXXX/X...../X...../","XXXX/..X./..X./..X./..X./..X./"};
		shape[68] = new String[]{"X...../X...../XXXXXX/.X..../",".XXX/XX../.X../.X../.X../.X../","....X./XXXXXX/.....X/.....X/","..X./..X./..X./..X./..XX/XXX./",".....X/.....X/XXXXXX/....X./",".X../.X../.X../.X../XX../.XXX/",".X..../XXXXXX/X...../X...../","XXX./..XX/..X./..X./..X./..X./"};
		shape[69] = new String[]{"X...../X.X.../XXXXXX/","XXX/X../XX./X../X../X../","XXXXXX/...X.X/.....X/","..X/..X/..X/.XX/..X/XXX/",".....X/...X.X/XXXXXX/","X../X../X../XX./X../XXX/","XXXXXX/X.X.../X...../","XXX/..X/.XX/..X/..X/..X/"};
		shape[70] = new String[]{"X...../X...../XXXXXX/..X.../",".XXX/.X../XX../.X../.X../.X../","...X../XXXXXX/.....X/.....X/","..X./..X./..X./..XX/..X./XXX./",".....X/.....X/XXXXXX/...X../",".X../.X../.X../XX../.X../.XXX/","..X.../XXXXXX/X...../X...../","XXX./..X./..XX/..X./..X./..X./"};
		shape[71] = new String[]{"X...../X..X../XXXXXX/","XXX/X../X../XX./X../X../","XXXXXX/..X..X/.....X/","..X/..X/.XX/..X/..X/XXX/",".....X/..X..X/XXXXXX/","X../X../XX./X../X../XXX/","XXXXXX/X..X../X...../","XXX/..X/..X/.XX/..X/..X/"};
		shape[72] = new String[]{"X...../X...../XXXXXX/...X../",".XXX/.X../.X../XX../.X../.X../","..X.../XXXXXX/.....X/.....X/","..X./..X./..XX/..X./..X./XXX./",".....X/.....X/XXXXXX/..X.../",".X../.X../XX../.X../.X../.XXX/","...X../XXXXXX/X...../X...../","XXX./..X./..X./..XX/..X./..X./"};
		shape[73] = new String[]{"X...../X...X./XXXXXX/","XXX/X../X../X../XX./X../","XXXXXX/.X...X/.....X/","..X/.XX/..X/..X/..X/XXX/",".....X/.X...X/XXXXXX/","X../XX./X../X../X../XXX/","XXXXXX/X...X./X...../","XXX/..X/..X/..X/.XX/..X/"};
		shape[74] = new String[]{"X...../X...../XXXXXX/....X./",".XXX/.X../.X../.X../XX../.X../",".X..../XXXXXX/.....X/.....X/","..X./..XX/..X./..X./..X./XXX./",".....X/.....X/XXXXXX/.X..../",".X../XX../.X../.X../.X../.XXX/","....X./XXXXXX/X...../X...../","XXX./..X./..X./..X./..XX/..X./"};
		shape[75] = new String[]{"X...../X....X/XXXXXX/","XXX/X../X../X../X../XX./","XXXXXX/X....X/.....X/",".XX/..X/..X/..X/..X/XXX/",".....X/X....X/XXXXXX/","XX./X../X../X../X../XXX/","XXXXXX/X....X/X...../","XXX/..X/..X/..X/..X/.XX/"};
		shape[76] = new String[]{"X...../X...../XXXXXX/.....X/",".XXX/.X../.X../.X../.X../XX../","X...../XXXXXX/.....X/.....X/","..XX/..X./..X./..X./..X./XXX./",".....X/.....X/XXXXXX/X...../","XX../.X../.X../.X../.X../.XXX/",".....X/XXXXXX/X...../X...../","XXX./..X./..X./..X./..X./..XX/"};
		shape[77] = new String[]{"X.X.../XXXXXX/X...../","XXX/.X./.XX/.X./.X./.X./",".....X/XXXXXX/...X.X/",".X./.X./.X./XX./.X./XXX/","...X.X/XXXXXX/.....X/",".X./.X./.X./.XX/.X./XXX/","X...../XXXXXX/X.X.../","XXX/.X./XX./.X./.X./.X./"};
		shape[78] = new String[]{"X..X../XXXXXX/X...../","XXX/.X./.X./.XX/.X./.X./",".....X/XXXXXX/..X..X/",".X./.X./XX./.X./.X./XXX/","..X..X/XXXXXX/.....X/",".X./.X./.XX/.X./.X./XXX/","X...../XXXXXX/X..X../","XXX/.X./.X./XX./.X./.X./"};
		shape[79] = new String[]{"X...X./XXXXXX/X...../","XXX/.X./.X./.X./.XX/.X./",".....X/XXXXXX/.X...X/",".X./XX./.X./.X./.X./XXX/",".X...X/XXXXXX/.....X/",".X./.XX/.X./.X./.X./XXX/","X...../XXXXXX/X...X./","XXX/.X./.X./.X./XX./.X./"};
		shape[80] = new String[]{"X....X/XXXXXX/X...../","XXX/.X./.X./.X./.X./.XX/",".....X/XXXXXX/X....X/","XX./.X./.X./.X./.X./XXX/","X....X/XXXXXX/.....X/",".XX/.X./.X./.X./.X./XXX/","X...../XXXXXX/X....X/","XXX/.X./.X./.X./.X./XX./"};
		shape[81] = new String[]{"X.X.../XXXXXX/.X..../",".XX/XX./.XX/.X./.X./.X./","....X./XXXXXX/...X.X/",".X./.X./.X./XX./.XX/XX./","...X.X/XXXXXX/....X./",".X./.X./.X./.XX/XX./.XX/",".X..../XXXXXX/X.X.../","XX./.XX/XX./.X./.X./.X./"};
		shape[82] = new String[]{"X...../XXXXXX/.XX.../",".XX/XX./XX./.X./.X./.X./","...XX./XXXXXX/.....X/",".X./.X./.X./.XX/.XX/XX./",".....X/XXXXXX/...XX./",".X./.X./.X./XX./XX./.XX/",".XX.../XXXXXX/X...../","XX./.XX/.XX/.X./.X./.X./"};
		shape[83] = new String[]{"X..X../XXXXXX/.X..../",".XX/XX./.X./.XX/.X./.X./","....X./XXXXXX/..X..X/",".X./.X./XX./.X./.XX/XX./","..X..X/XXXXXX/....X./",".X./.X./.XX/.X./XX./.XX/",".X..../XXXXXX/X..X../","XX./.XX/.X./XX./.X./.X./"};
		shape[84] = new String[]{"X...../XXXXXX/.X.X../",".XX/XX./.X./XX./.X./.X./","..X.X./XXXXXX/.....X/",".X./.X./.XX/.X./.XX/XX./",".....X/XXXXXX/..X.X./",".X./.X./XX./.X./XX./.XX/",".X.X../XXXXXX/X...../","XX./.XX/.X./.XX/.X./.X./"};
		shape[85] = new String[]{"X...X./XXXXXX/.X..../",".XX/XX./.X./.X./.XX/.X./","....X./XXXXXX/.X...X/",".X./XX./.X./.X./.XX/XX./",".X...X/XXXXXX/....X./",".X./.XX/.X./.X./XX./.XX/",".X..../XXXXXX/X...X./","XX./.XX/.X./.X./XX./.X./"};
		shape[86] = new String[]{"X...../XXXXXX/.X..X./",".XX/XX./.X./.X./XX./.X./",".X..X./XXXXXX/.....X/",".X./.XX/.X./.X./.XX/XX./",".....X/XXXXXX/.X..X./",".X./XX./.X./.X./XX./.XX/",".X..X./XXXXXX/X...../","XX./.XX/.X./.X./.XX/.X./"};
		shape[87] = new String[]{"X....X/XXXXXX/.X..../",".XX/XX./.X./.X./.X./.XX/","....X./XXXXXX/X....X/","XX./.X./.X./.X./.XX/XX./","X....X/XXXXXX/....X./",".XX/.X./.X./.X./XX./.XX/",".X..../XXXXXX/X....X/","XX./.XX/.X./.X./.X./XX./"};
		shape[88] = new String[]{"X...../XXXXXX/.X...X/",".XX/XX./.X./.X./.X./XX./","X...X./XXXXXX/.....X/",".XX/.X./.X./.X./.XX/XX./",".....X/XXXXXX/X...X./","XX./.X./.X./.X./XX./.XX/",".X...X/XXXXXX/X...../","XX./.XX/.X./.X./.X./.XX/"};
		shape[89] = new String[]{"X...../XXXXXX/.X..../.X..../","..XX/XXX./..X./..X./..X./..X./","....X./....X./XXXXXX/.....X/",".X../.X../.X../.X../.XXX/XX../",".....X/XXXXXX/....X./....X./","..X./..X./..X./..X./XXX./..XX/",".X..../.X..../XXXXXX/X...../","XX../.XXX/.X../.X../.X../.X../"};
		shape[90] = new String[]{"X.XX../XXXXXX/","XX/X./XX/XX/X./X./","XXXXXX/..XX.X/",".X/.X/XX/XX/.X/XX/","..XX.X/XXXXXX/","X./X./XX/XX/X./XX/","XXXXXX/X.XX../","XX/.X/XX/XX/.X/.X/"};
		shape[91] = new String[]{"..X.../X.X.../XXXXXX/","XX./X../XXX/X../X../X../","XXXXXX/...X.X/...X../","..X/..X/..X/XXX/..X/.XX/","...X../...X.X/XXXXXX/","X../X../X../XXX/X../XX./","XXXXXX/X.X.../..X.../",".XX/..X/XXX/..X/..X/..X/"};
		shape[92] = new String[]{"X.X.../XXXXXX/..X.../",".XX/.X./XXX/.X./.X./.X./","...X../XXXXXX/...X.X/",".X./.X./.X./XXX/.X./XX./","...X.X/XXXXXX/...X../",".X./.X./.X./XXX/.X./.XX/","..X.../XXXXXX/X.X.../","XX./.X./XXX/.X./.X./.X./"};
		shape[93] = new String[]{"X.X.../XXXXXX/...X../",".XX/.X./.XX/XX./.X./.X./","..X.../XXXXXX/...X.X/",".X./.X./.XX/XX./.X./XX./","...X.X/XXXXXX/..X.../",".X./.X./XX./.XX/.X./.XX/","...X../XXXXXX/X.X.../","XX./.X./XX./.XX/.X./.X./"};
		shape[94] = new String[]{"X.X.X./XXXXXX/","XX/X./XX/X./XX/X./","XXXXXX/.X.X.X/",".X/XX/.X/XX/.X/XX/",".X.X.X/XXXXXX/","X./XX/X./XX/X./XX/","XXXXXX/X.X.X./","XX/.X/XX/.X/XX/.X/"};
		shape[95] = new String[]{"X.X.../XXXXXX/....X./",".XX/.X./.XX/.X./XX./.X./",".X..../XXXXXX/...X.X/",".X./.XX/.X./XX./.X./XX./","...X.X/XXXXXX/.X..../",".X./XX./.X./.XX/.X./.XX/","....X./XXXXXX/X.X.../","XX./.X./XX./.X./.XX/.X./"};
		shape[96] = new String[]{"X.X..X/XXXXXX/","XX/X./XX/X./X./XX/","XXXXXX/X..X.X/","XX/.X/.X/XX/.X/XX/","X..X.X/XXXXXX/","XX/X./X./XX/X./XX/","XXXXXX/X.X..X/","XX/.X/XX/.X/.X/XX/"};
		shape[97] = new String[]{"X.X.../XXXXXX/.....X/",".XX/.X./.XX/.X./.X./XX./","X...../XXXXXX/...X.X/",".XX/.X./.X./XX./.X./XX./","...X.X/XXXXXX/X...../","XX./.X./.X./.XX/.X./.XX/",".....X/XXXXXX/X.X.../","XX./.X./XX./.X./.X./.XX/"};
		shape[98] = new String[]{"X..X../XXXXXX/..X.../",".XX/.X./XX./.XX/.X./.X./","...X../XXXXXX/..X..X/",".X./.X./XX./.XX/.X./XX./","..X..X/XXXXXX/...X../",".X./.X./.XX/XX./.X./.XX/","..X.../XXXXXX/X..X../","XX./.X./.XX/XX./.X./.X./"};
		shape[99] = new String[]{"X...../XXXXXX/..XX../",".XX/.X./XX./XX./.X./.X./","..XX../XXXXXX/.....X/",".X./.X./.XX/.XX/.X./XX./",".....X/XXXXXX/..XX../",".X./.X./XX./XX./.X./.XX/","..XX../XXXXXX/X...../","XX./.X./.XX/.XX/.X./.X./"};
		shape[100] = new String[]{"X...X./XXXXXX/..X.../",".XX/.X./XX./.X./.XX/.X./","...X../XXXXXX/.X...X/",".X./XX./.X./.XX/.X./XX./",".X...X/XXXXXX/...X../",".X./.XX/.X./XX./.X./.XX/","..X.../XXXXXX/X...X./","XX./.X./.XX/.X./XX./.X./"};
		shape[101] = new String[]{"X...../XXXXXX/..X.X./",".XX/.X./XX./.X./XX./.X./",".X.X../XXXXXX/.....X/",".X./.XX/.X./.XX/.X./XX./",".....X/XXXXXX/.X.X../",".X./XX./.X./XX./.X./.XX/","..X.X./XXXXXX/X...../","XX./.X./.XX/.X./.XX/.X./"};
		shape[102] = new String[]{"X....X/XXXXXX/..X.../",".XX/.X./XX./.X./.X./.XX/","...X../XXXXXX/X....X/","XX./.X./.X./.XX/.X./XX./","X....X/XXXXXX/...X../",".XX/.X./.X./XX./.X./.XX/","..X.../XXXXXX/X....X/","XX./.X./.XX/.X./.X./XX./"};
		shape[103] = new String[]{"X...../XXXXXX/..X..X/",".XX/.X./XX./.X./.X./XX./","X..X../XXXXXX/.....X/",".XX/.X./.X./.XX/.X./XX./",".....X/XXXXXX/X..X../","XX./.X./.X./XX./.X./.XX/","..X..X/XXXXXX/X...../","XX./.X./.XX/.X./.X./.XX/"};
		shape[104] = new String[]{"X...../XXXXXX/..X.../..X.../","..XX/..X./XXX./..X./..X./..X./","...X../...X../XXXXXX/.....X/",".X../.X../.X../.XXX/.X../XX../",".....X/XXXXXX/...X../...X../","..X./..X./..X./XXX./..X./..XX/","..X.../..X.../XXXXXX/X...../","XX../.X../.XXX/.X../.X../.X../"};
		shape[105] = new String[]{"X..XX./XXXXXX/","XX/X./X./XX/XX/X./","XXXXXX/.XX..X/",".X/XX/XX/.X/.X/XX/",".XX..X/XXXXXX/","X./XX/XX/X./X./XX/","XXXXXX/X..XX./","XX/.X/.X/XX/XX/.X/"};
		shape[106] = new String[]{"...X../X..X../XXXXXX/","XX./X../X../XXX/X../X../","XXXXXX/..X..X/..X.../","..X/..X/XXX/..X/..X/.XX/","..X.../..X..X/XXXXXX/","X../X../XXX/X../X../XX./","XXXXXX/X..X../...X../",".XX/..X/..X/XXX/..X/..X/"};
		shape[107] = new String[]{"X..X../XXXXXX/...X../",".XX/.X./.X./XXX/.X./.X./","..X.../XXXXXX/..X..X/",".X./.X./XXX/.X./.X./XX./","..X..X/XXXXXX/..X.../",".X./.X./XXX/.X./.X./.XX/","...X../XXXXXX/X..X../","XX./.X./.X./XXX/.X./.X./"};
		shape[108] = new String[]{"X..X../XXXXXX/....X./",".XX/.X./.X./.XX/XX./.X./",".X..../XXXXXX/..X..X/",".X./.XX/XX./.X./.X./XX./","..X..X/XXXXXX/.X..../",".X./XX./.XX/.X./.X./.XX/","....X./XXXXXX/X..X../","XX./.X./.X./XX./.XX/.X./"};
		shape[109] = new String[]{"X...X./XXXXXX/...X../",".XX/.X./.X./XX./.XX/.X./","..X.../XXXXXX/.X...X/",".X./XX./.XX/.X./.X./XX./",".X...X/XXXXXX/..X.../",".X./.XX/XX./.X./.X./.XX/","...X../XXXXXX/X...X./","XX./.X./.X./.XX/XX./.X./"};
		shape[110] = new String[]{"X...../XXXXXX/...XX./",".XX/.X./.X./XX./XX./.X./",".XX.../XXXXXX/.....X/",".X./.XX/.XX/.X./.X./XX./",".....X/XXXXXX/.XX.../",".X./XX./XX./.X./.X./.XX/","...XX./XXXXXX/X...../","XX./.X./.X./.XX/.XX/.X./"};
		shape[111] = new String[]{"X...../XXXXXX/...X../...X../","..XX/..X./..X./XXX./..X./..X./","..X.../..X.../XXXXXX/.....X/",".X../.X../.XXX/.X../.X../XX../",".....X/XXXXXX/..X.../..X.../","..X./..X./XXX./..X./..X./..XX/","...X../...X../XXXXXX/X...../","XX../.X../.X../.XXX/.X../.X../"};
		shape[112] = new String[]{"....X./X...X./XXXXXX/","XX./X../X../X../XXX/X../","XXXXXX/.X...X/.X..../","..X/XXX/..X/..X/..X/.XX/",".X..../.X...X/XXXXXX/","X../XXX/X../X../X../XX./","XXXXXX/X...X./....X./",".XX/..X/..X/..X/XXX/..X/"};
		shape[113] = new String[]{"X...X./XXXXXX/....X./",".XX/.X./.X./.X./XXX/.X./",".X..../XXXXXX/.X...X/",".X./XXX/.X./.X./.X./XX./",".X...X/XXXXXX/.X..../",".X./XXX/.X./.X./.X./.XX/","....X./XXXXXX/X...X./","XX./.X./.X./.X./XXX/.X./"};
		shape[114] = new String[]{"X...../XXXXXX/....X./....X./","..XX/..X./..X./..X./XXX./..X./",".X..../.X..../XXXXXX/.....X/",".X../.XXX/.X../.X../.X../XX../",".....X/XXXXXX/.X..../.X..../","..X./XXX./..X./..X./..X./..XX/","....X./....X./XXXXXX/X...../","XX../.X../.X../.X../.XXX/.X../"};
		shape[115] = new String[]{".X..../.XX.../XXXXXX/","X../XXX/XX./X../X../X../","XXXXXX/...XX./....X./","..X/..X/..X/.XX/XXX/..X/","....X./...XX./XXXXXX/","X../X../X../XX./XXX/X../","XXXXXX/.XX.../.X..../","..X/XXX/.XX/..X/..X/..X/"};
		shape[116] = new String[]{".XXX../XXXXXX/","X./XX/XX/XX/X./X./","XXXXXX/..XXX./",".X/.X/XX/XX/XX/.X/","..XXX./XXXXXX/","X./X./XX/XX/XX/X./","XXXXXX/.XXX../",".X/XX/XX/XX/.X/.X/"};
		shape[117] = new String[]{"..X.../.XX.../XXXXXX/","X../XX./XXX/X../X../X../","XXXXXX/...XX./...X../","..X/..X/..X/XXX/.XX/..X/","...X../...XX./XXXXXX/","X../X../X../XXX/XX./X../","XXXXXX/.XX.../..X.../","..X/.XX/XXX/..X/..X/..X/"};
		shape[118] = new String[]{".XX.../XXXXXX/.X..../",".X./XXX/.XX/.X./.X./.X./","....X./XXXXXX/...XX./",".X./.X./.X./XX./XXX/.X./","...XX./XXXXXX/....X./",".X./.X./.X./.XX/XXX/.X./",".X..../XXXXXX/.XX.../",".X./XXX/XX./.X./.X./.X./"};
		shape[119] = new String[]{".XX.../XXXXXX/..X.../",".X./.XX/XXX/.X./.X./.X./","...X../XXXXXX/...XX./",".X./.X./.X./XXX/XX./.X./","...XX./XXXXXX/...X../",".X./.X./.X./XXX/.XX/.X./","..X.../XXXXXX/.XX.../",".X./XX./XXX/.X./.X./.X./"};
		shape[120] = new String[]{".XX.../XXXXXX/...X../",".X./.XX/.XX/XX./.X./.X./","..X.../XXXXXX/...XX./",".X./.X./.XX/XX./XX./.X./","...XX./XXXXXX/..X.../",".X./.X./XX./.XX/.XX/.X./","...X../XXXXXX/.XX.../",".X./XX./XX./.XX/.X./.X./"};
		shape[121] = new String[]{".XX.X./XXXXXX/","X./XX/XX/X./XX/X./","XXXXXX/.X.XX./",".X/XX/.X/XX/XX/.X/",".X.XX./XXXXXX/","X./XX/X./XX/XX/X./","XXXXXX/.XX.X./",".X/XX/XX/.X/XX/.X/"};
		shape[122] = new String[]{".XX.../XXXXXX/....X./",".X./.XX/.XX/.X./XX./.X./",".X..../XXXXXX/...XX./",".X./.XX/.X./XX./XX./.X./","...XX./XXXXXX/.X..../",".X./XX./.X./.XX/.XX/.X./","....X./XXXXXX/.XX.../",".X./XX./XX./.X./.XX/.X./"};
		shape[123] = new String[]{"XX..../.X..../XXXXXX/","X.X/XXX/X../X../X../X../","XXXXXX/....X./....XX/","..X/..X/..X/..X/XXX/X.X/","....XX/....X./XXXXXX/","X../X../X../X../XXX/X.X/","XXXXXX/.X..../XX..../","X.X/XXX/..X/..X/..X/..X/"};
		shape[124] = new String[]{".XX.../.X..../XXXXXX/","X../XXX/X.X/X../X../X../","XXXXXX/....X./...XX./","..X/..X/..X/X.X/XXX/..X/","...XX./....X./XXXXXX/","X../X../X../X.X/XXX/X../","XXXXXX/.X..../.XX.../","..X/XXX/X.X/..X/..X/..X/"};
		shape[125] = new String[]{".X..../.X..../.X..../XXXXXX/","X.../XXXX/X.../X.../X.../X.../","XXXXXX/....X./....X./....X./","...X/...X/...X/...X/XXXX/...X/","....X./....X./....X./XXXXXX/","X.../X.../X.../X.../XXXX/X.../","XXXXXX/.X..../.X..../.X..../","...X/XXXX/...X/...X/...X/...X/"};
		shape[126] = new String[]{".X..../.X..../XXXXXX/.X..../",".X../XXXX/.X../.X../.X../.X../","....X./XXXXXX/....X./....X./","..X./..X./..X./..X./XXXX/..X./","....X./....X./XXXXXX/....X./",".X../.X../.X../.X../XXXX/.X../",".X..../XXXXXX/.X..../.X..../","..X./XXXX/..X./..X./..X./..X./"};
		shape[127] = new String[]{".X..../.X..../XXXXXX/..X.../",".X../.XXX/XX../.X../.X../.X../","...X../XXXXXX/....X./....X./","..X./..X./..X./..XX/XXX./..X./","....X./....X./XXXXXX/...X../",".X../.X../.X../XX../.XXX/.X../","..X.../XXXXXX/.X..../.X..../","..X./XXX./..XX/..X./..X./..X./"};
		shape[128] = new String[]{".X..../.X.X../XXXXXX/","X../XXX/X../XX./X../X../","XXXXXX/..X.X./....X./","..X/..X/.XX/..X/XXX/..X/","....X./..X.X./XXXXXX/","X../X../XX./X../XXX/X../","XXXXXX/.X.X../.X..../","..X/XXX/..X/.XX/..X/..X/"};
		shape[129] = new String[]{".X..../.X..../XXXXXX/...X../",".X../.XXX/.X../XX../.X../.X../","..X.../XXXXXX/....X./....X./","..X./..X./..XX/..X./XXX./..X./","....X./....X./XXXXXX/..X.../",".X../.X../XX../.X../.XXX/.X../","...X../XXXXXX/.X..../.X..../","..X./XXX./..X./..XX/..X./..X./"};
		shape[130] = new String[]{".X..../.X..X./XXXXXX/","X../XXX/X../X../XX./X../","XXXXXX/.X..X./....X./","..X/.XX/..X/..X/XXX/..X/","....X./.X..X./XXXXXX/","X../XX./X../X../XXX/X../","XXXXXX/.X..X./.X..../","..X/XXX/..X/..X/.XX/..X/"};
		shape[131] = new String[]{".X..../.X..../XXXXXX/....X./",".X../.XXX/.X../.X../XX../.X../",".X..../XXXXXX/....X./....X./","..X./..XX/..X./..X./XXX./..X./","....X./....X./XXXXXX/.X..../",".X../XX../.X../.X../.XXX/.X../","....X./XXXXXX/.X..../.X..../","..X./XXX./..X./..X./..XX/..X./"};
		shape[132] = new String[]{".X.X../XXXXXX/.X..../",".X./XXX/.X./.XX/.X./.X./","....X./XXXXXX/..X.X./",".X./.X./XX./.X./XXX/.X./","..X.X./XXXXXX/....X./",".X./.X./.XX/.X./XXX/.X./",".X..../XXXXXX/.X.X../",".X./XXX/.X./XX./.X./.X./"};
		shape[133] = new String[]{".X..X./XXXXXX/.X..../",".X./XXX/.X./.X./.XX/.X./","....X./XXXXXX/.X..X./",".X./XX./.X./.X./XXX/.X./",".X..X./XXXXXX/....X./",".X./.XX/.X./.X./XXX/.X./",".X..../XXXXXX/.X..X./",".X./XXX/.X./.X./XX./.X./"};
		shape[134] = new String[]{".X.X../XXXXXX/..X.../",".X./.XX/XX./.XX/.X./.X./","...X../XXXXXX/..X.X./",".X./.X./XX./.XX/XX./.X./","..X.X./XXXXXX/...X../",".X./.X./.XX/XX./.XX/.X./","..X.../XXXXXX/.X.X../",".X./XX./.XX/XX./.X./.X./"};
		shape[135] = new String[]{".X..../XXXXXX/..XX../",".X./.XX/XX./XX./.X./.X./","..XX../XXXXXX/....X./",".X./.X./.XX/.XX/XX./.X./","....X./XXXXXX/..XX../",".X./.X./XX./XX./.XX/.X./","..XX../XXXXXX/.X..../",".X./XX./.XX/.XX/.X./.X./"};
		shape[136] = new String[]{".X..X./XXXXXX/..X.../",".X./.XX/XX./.X./.XX/.X./","...X../XXXXXX/.X..X./",".X./XX./.X./.XX/XX./.X./",".X..X./XXXXXX/...X../",".X./.XX/.X./XX./.XX/.X./","..X.../XXXXXX/.X..X./",".X./XX./.XX/.X./XX./.X./"};
		shape[137] = new String[]{".X..../XXXXXX/..X.X./",".X./.XX/XX./.X./XX./.X./",".X.X../XXXXXX/....X./",".X./.XX/.X./.XX/XX./.X./","....X./XXXXXX/.X.X../",".X./XX./.X./XX./.XX/.X./","..X.X./XXXXXX/.X..../",".X./XX./.XX/.X./.XX/.X./"};
		shape[138] = new String[]{".X..../XXXXXX/..X.../..X.../","..X./..XX/XXX./..X./..X./..X./","...X../...X../XXXXXX/....X./",".X../.X../.X../.XXX/XX../.X../","....X./XXXXXX/...X../...X../","..X./..X./..X./XXX./..XX/..X./","..X.../..X.../XXXXXX/.X..../",".X../XX../.XXX/.X../.X../.X../"};
		shape[139] = new String[]{"...X../.X.X../XXXXXX/","X../XX./X../XXX/X../X../","XXXXXX/..X.X./..X.../","..X/..X/XXX/..X/.XX/..X/","..X.../..X.X./XXXXXX/","X../X../XXX/X../XX./X../","XXXXXX/.X.X../...X../","..X/.XX/..X/XXX/..X/..X/"};
		shape[140] = new String[]{".X.X../XXXXXX/...X../",".X./.XX/.X./XXX/.X./.X./","..X.../XXXXXX/..X.X./",".X./.X./XXX/.X./XX./.X./","..X.X./XXXXXX/..X.../",".X./.X./XXX/.X./.XX/.X./","...X../XXXXXX/.X.X../",".X./XX./.X./XXX/.X./.X./"};
		shape[141] = new String[]{".X..../XXXXXX/...X../...X../","..X./..XX/..X./XXX./..X./..X./","..X.../..X.../XXXXXX/....X./",".X../.X../.XXX/.X../XX../.X../","....X./XXXXXX/..X.../..X.../","..X./..X./XXX./..X./..XX/..X./","...X../...X../XXXXXX/.X..../",".X../XX../.X../.XXX/.X../.X../"};
		shape[142] = new String[]{"..X.../..XX../XXXXXX/","X../X../XXX/XX./X../X../","XXXXXX/..XX../...X../","..X/..X/.XX/XXX/..X/..X/","...X../..XX../XXXXXX/","X../X../XX./XXX/X../X../","XXXXXX/..XX../..X.../","..X/..X/XXX/.XX/..X/..X/"};
		shape[143] = new String[]{"..XX../XXXXXX/..X.../",".X./.X./XXX/.XX/.X./.X./","...X../XXXXXX/..XX../",".X./.X./XX./XXX/.X./.X./","..XX../XXXXXX/...X../",".X./.X./.XX/XXX/.X./.X./","..X.../XXXXXX/..XX../",".X./.X./XXX/XX./.X./.X./"};
		shape[144] = new String[]{".XX.../..X.../XXXXXX/","X../X.X/XXX/X../X../X../","XXXXXX/...X../...XX./","..X/..X/..X/XXX/X.X/..X/","...XX./...X../XXXXXX/","X../X../X../XXX/X.X/X../","XXXXXX/..X.../.XX.../","..X/X.X/XXX/..X/..X/..X/"};
		shape[145] = new String[]{"..XX../..X.../XXXXXX/","X../X../XXX/X.X/X../X../","XXXXXX/...X../..XX../","..X/..X/X.X/XXX/..X/..X/","..XX../...X../XXXXXX/","X../X../X.X/XXX/X../X../","XXXXXX/..X.../..XX../","..X/..X/XXX/X.X/..X/..X/"};
		shape[146] = new String[]{"..X.../..X.../..X.../XXXXXX/","X.../X.../XXXX/X.../X.../X.../","XXXXXX/...X../...X../...X../","...X/...X/...X/XXXX/...X/...X/","...X../...X../...X../XXXXXX/","X.../X.../X.../XXXX/X.../X.../","XXXXXX/..X.../..X.../..X.../","...X/...X/XXXX/...X/...X/...X/"};
		shape[147] = new String[]{"..X.../..X.../XXXXXX/..X.../",".X../.X../XXXX/.X../.X../.X../","...X../XXXXXX/...X../...X../","..X./..X./..X./XXXX/..X./..X./","...X../...X../XXXXXX/...X../",".X../.X../.X../XXXX/.X../.X../","..X.../XXXXXX/..X.../..X.../","..X./..X./XXXX/..X./..X./..X./"};
		shape[148] = new String[]{"..X.../..X.../XXXXXX/...X../",".X../.X../.XXX/XX../.X../.X../","..X.../XXXXXX/...X../...X../","..X./..X./..XX/XXX./..X./..X./","...X../...X../XXXXXX/..X.../",".X../.X../XX../.XXX/.X../.X../","...X../XXXXXX/..X.../..X.../","..X./..X./XXX./..XX/..X./..X./"};
		shape[149] = new String[]{"XXXX..../...XXXXX/",".X/.X/.X/XX/X./X./X./X./","XXXXX.../....XXXX/",".X/.X/.X/.X/XX/X./X./X./","....XXXX/XXXXX.../","X./X./X./X./XX/.X/.X/.X/","...XXXXX/XXXX..../","X./X./X./XX/.X/.X/.X/.X/"};
		shape[150] = new String[]{"X....../XXX..../..XXXXX/",".XX/.X./XX./X../X../X../X../","XXXXX../....XXX/......X/","..X/..X/..X/..X/.XX/.X./XX./","......X/....XXX/XXXXX../","X../X../X../X../XX./.X./.XX/","..XXXXX/XXX..../X....../","XX./.X./.XX/..X/..X/..X/..X/"};
		shape[151] = new String[]{"XXX..../X.XXXXX/","XX/.X/XX/X./X./X./X./","XXXXX.X/....XXX/",".X/.X/.X/.X/XX/X./XX/","....XXX/XXXXX.X/","X./X./X./X./XX/.X/XX/","X.XXXXX/XXX..../","XX/X./XX/.X/.X/.X/.X/"};
		shape[152] = new String[]{".X...../XXX..../..XXXXX/",".X./.XX/XX./X../X../X../X../","XXXXX../....XXX/.....X./","..X/..X/..X/..X/.XX/XX./.X./",".....X./....XXX/XXXXX../","X../X../X../X../XX./.XX/.X./","..XXXXX/XXX..../.X...../",".X./XX./.XX/..X/..X/..X/..X/"};
		shape[153] = new String[]{"XXXX.../..XXXXX/",".X/.X/XX/XX/X./X./X./","XXXXX../...XXXX/",".X/.X/.X/XX/XX/X./X./","...XXXX/XXXXX../","X./X./X./XX/XX/.X/.X/","..XXXXX/XXXX.../","X./X./XX/XX/.X/.X/.X/"};
		shape[154] = new String[]{"..X..../XXX..../..XXXXX/",".X./.X./XXX/X../X../X../X../","XXXXX../....XXX/....X../","..X/..X/..X/..X/XXX/.X./.X./","....X../....XXX/XXXXX../","X../X../X../X../XXX/.X./.X./","..XXXXX/XXX..../..X..../",".X./.X./XXX/..X/..X/..X/..X/"};
		shape[155] = new String[]{"XXX..../..XXXXX/..X..../","..X/..X/XXX/.X./.X./.X./.X./","....X../XXXXX../....XXX/",".X./.X./.X./.X./XXX/X../X../","....XXX/XXXXX../....X../",".X./.X./.X./.X./XXX/..X/..X/","..X..../..XXXXX/XXX..../","X../X../XXX/.X./.X./.X./.X./"};
		shape[156] = new String[]{"XXX..../..XXXXX/...X.../","..X/..X/.XX/XX./.X./.X./.X./","...X.../XXXXX../....XXX/",".X./.X./.X./.XX/XX./X../X../","....XXX/XXXXX../...X.../",".X./.X./.X./XX./.XX/..X/..X/","...X.../..XXXXX/XXX..../","X../X../XX./.XX/.X./.X./.X./"};
		shape[157] = new String[]{"XXX.X../..XXXXX/",".X/.X/XX/X./XX/X./X./","XXXXX../..X.XXX/",".X/.X/XX/.X/XX/X./X./","..X.XXX/XXXXX../","X./X./XX/X./XX/.X/.X/","..XXXXX/XXX.X../","X./X./XX/.X/XX/.X/.X/"};
		shape[158] = new String[]{"XXX..../..XXXXX/....X../","..X/..X/.XX/.X./XX./.X./.X./","..X..../XXXXX../....XXX/",".X./.X./.XX/.X./XX./X../X../","....XXX/XXXXX../..X..../",".X./.X./XX./.X./.XX/..X/..X/","....X../..XXXXX/XXX..../","X../X../XX./.X./.XX/.X./.X./"};
		shape[159] = new String[]{"XXX..X./..XXXXX/",".X/.X/XX/X./X./XX/X./","XXXXX../.X..XXX/",".X/XX/.X/.X/XX/X./X./",".X..XXX/XXXXX../","X./XX/X./X./XX/.X/.X/","..XXXXX/XXX..X./","X./X./XX/.X/.X/XX/.X/"};
		shape[160] = new String[]{"XXX..../..XXXXX/.....X./","..X/..X/.XX/.X./.X./XX./.X./",".X...../XXXXX../....XXX/",".X./.XX/.X./.X./XX./X../X../","....XXX/XXXXX../.X...../",".X./XX./.X./.X./.XX/..X/..X/",".....X./..XXXXX/XXX..../","X../X../XX./.X./.X./.XX/.X./"};
		shape[161] = new String[]{"XXX...X/..XXXXX/",".X/.X/XX/X./X./X./XX/","XXXXX../X...XXX/","XX/.X/.X/.X/XX/X./X./","X...XXX/XXXXX../","XX/X./X./X./XX/.X/.X/","..XXXXX/XXX...X/","X./X./XX/.X/.X/.X/XX/"};
		shape[162] = new String[]{"XXX..../..XXXXX/......X/","..X/..X/.XX/.X./.X./.X./XX./","X....../XXXXX../....XXX/",".XX/.X./.X./.X./XX./X../X../","....XXX/XXXXX../X....../","XX./.X./.X./.X./.XX/..X/..X/","......X/..XXXXX/XXX..../","X../X../XX./.X./.X./.X./.XX/"};
		shape[163] = new String[]{"XX...../.XX..../..XXXXX/","..X/.XX/XX./X../X../X../X../","XXXXX../....XX./.....XX/","..X/..X/..X/..X/.XX/XX./X../",".....XX/....XX./XXXXX../","X../X../X../X../XX./.XX/..X/","..XXXXX/.XX..../XX...../","X../XX./.XX/..X/..X/..X/..X/"};
		shape[164] = new String[]{"XX..../XX..../.XXXXX/",".XX/XXX/X../X../X../X../","XXXXX./....XX/....XX/","..X/..X/..X/..X/XXX/XX./","....XX/....XX/XXXXX./","X../X../X../X../XXX/.XX/",".XXXXX/XX..../XX..../","XX./XXX/..X/..X/..X/..X/"};
		shape[165] = new String[]{"X...../X...../XX..../.XXXXX/",".XXX/XX../X.../X.../X.../X.../","XXXXX./....XX/.....X/.....X/","...X/...X/...X/...X/..XX/XXX./",".....X/.....X/....XX/XXXXX./","X.../X.../X.../X.../XX../.XXX/",".XXXXX/XX..../X...../X...../","XXX./..XX/...X/...X/...X/...X/"};
		shape[166] = new String[]{"X...../XXX.../.XXXXX/",".XX/XX./XX./X../X../X../","XXXXX./...XXX/.....X/","..X/..X/..X/.XX/.XX/XX./",".....X/...XXX/XXXXX./","X../X../X../XX./XX./.XX/",".XXXXX/XXX.../X...../","XX./.XX/.XX/..X/..X/..X/"};
		shape[167] = new String[]{"X...../XX..../.XXXXX/.X..../","..XX/XXX./.X../.X../.X../.X../","....X./XXXXX./....XX/.....X/","..X./..X./..X./..X./.XXX/XX../",".....X/....XX/XXXXX./....X./",".X../.X../.X../.X../XXX./..XX/",".X..../.XXXXX/XX..../X...../","XX../.XXX/..X./..X./..X./..X./"};
		shape[168] = new String[]{"X...../XX..../.XXXXX/..X.../","..XX/.XX./XX../.X../.X../.X../","...X../XXXXX./....XX/.....X/","..X./..X./..X./..XX/.XX./XX../",".....X/....XX/XXXXX./...X../",".X../.X../.X../XX../.XX./..XX/","..X.../.XXXXX/XX..../X...../","XX../.XX./..XX/..X./..X./..X./"};
		shape[169] = new String[]{"X...../XX.X../.XXXXX/",".XX/XX./X../XX./X../X../","XXXXX./..X.XX/.....X/","..X/..X/.XX/..X/.XX/XX./",".....X/..X.XX/XXXXX./","X../X../XX./X../XX./.XX/",".XXXXX/XX.X../X...../","XX./.XX/..X/.XX/..X/..X/"};
		shape[170] = new String[]{"X...../XX..../.XXXXX/...X../","..XX/.XX./.X../XX../.X../.X../","..X.../XXXXX./....XX/.....X/","..X./..X./..XX/..X./.XX./XX../",".....X/....XX/XXXXX./..X.../",".X../.X../XX../.X../.XX./..XX/","...X../.XXXXX/XX..../X...../","XX../.XX./..X./..XX/..X./..X./"};
		shape[171] = new String[]{"X...../XX..X./.XXXXX/",".XX/XX./X../X../XX./X../","XXXXX./.X..XX/.....X/","..X/.XX/..X/..X/.XX/XX./",".....X/.X..XX/XXXXX./","X../XX./X../X../XX./.XX/",".XXXXX/XX..X./X...../","XX./.XX/..X/..X/.XX/..X/"};
		shape[172] = new String[]{"X...../XX..../.XXXXX/....X./","..XX/.XX./.X../.X../XX../.X../",".X..../XXXXX./....XX/.....X/","..X./..XX/..X./..X./.XX./XX../",".....X/....XX/XXXXX./.X..../",".X../XX../.X../.X../.XX./..XX/","....X./.XXXXX/XX..../X...../","XX../.XX./..X./..X./..XX/..X./"};
		shape[173] = new String[]{"X...../XX...X/.XXXXX/",".XX/XX./X../X../X../XX./","XXXXX./X...XX/.....X/",".XX/..X/..X/..X/.XX/XX./",".....X/X...XX/XXXXX./","XX./X../X../X../XX./.XX/",".XXXXX/XX...X/X...../","XX./.XX/..X/..X/..X/.XX/"};
		shape[174] = new String[]{"X...../XX..../.XXXXX/.....X/","..XX/.XX./.X../.X../.X../XX../","X...../XXXXX./....XX/.....X/","..XX/..X./..X./..X./.XX./XX../",".....X/....XX/XXXXX./X...../","XX../.X../.X../.X../.XX./..XX/",".....X/.XXXXX/XX..../X...../","XX../.XX./..X./..X./..X./..XX/"};
		shape[175] = new String[]{".X..../XXX.../.XXXXX/",".X./XXX/XX./X../X../X../","XXXXX./...XXX/....X./","..X/..X/..X/.XX/XXX/.X./","....X./...XXX/XXXXX./","X../X../X../XX./XXX/.X./",".XXXXX/XXX.../.X..../",".X./XXX/.XX/..X/..X/..X/"};
		shape[176] = new String[]{"XXXX../.XXXXX/",".X/XX/XX/XX/X./X./","XXXXX./..XXXX/",".X/.X/XX/XX/XX/X./","..XXXX/XXXXX./","X./X./XX/XX/XX/.X/",".XXXXX/XXXX../","X./XX/XX/XX/.X/.X/"};
		shape[177] = new String[]{"..X.../XXX.../.XXXXX/",".X./XX./XXX/X../X../X../","XXXXX./...XXX/...X../","..X/..X/..X/XXX/.XX/.X./","...X../...XXX/XXXXX./","X../X../X../XXX/XX./.X./",".XXXXX/XXX.../..X.../",".X./.XX/XXX/..X/..X/..X/"};
		shape[178] = new String[]{"XXX.../.XXXXX/.X..../","..X/XXX/.XX/.X./.X./.X./","....X./XXXXX./...XXX/",".X./.X./.X./XX./XXX/X../","...XXX/XXXXX./....X./",".X./.X./.X./.XX/XXX/..X/",".X..../.XXXXX/XXX.../","X../XXX/XX./.X./.X./.X./"};
		shape[179] = new String[]{"XXX.../.XXXXX/..X.../","..X/.XX/XXX/.X./.X./.X./","...X../XXXXX./...XXX/",".X./.X./.X./XXX/XX./X../","...XXX/XXXXX./...X../",".X./.X./.X./XXX/.XX/..X/","..X.../.XXXXX/XXX.../","X../XX./XXX/.X./.X./.X./"};
		shape[180] = new String[]{"XXX.../.XXXXX/...X../","..X/.XX/.XX/XX./.X./.X./","..X.../XXXXX./...XXX/",".X./.X./.XX/XX./XX./X../","...XXX/XXXXX./..X.../",".X./.X./XX./.XX/.XX/..X/","...X../.XXXXX/XXX.../","X../XX./XX./.XX/.X./.X./"};
		shape[181] = new String[]{"XXX.X./.XXXXX/",".X/XX/XX/X./XX/X./","XXXXX./.X.XXX/",".X/XX/.X/XX/XX/X./",".X.XXX/XXXXX./","X./XX/X./XX/XX/.X/",".XXXXX/XXX.X./","X./XX/XX/.X/XX/.X/"};
		shape[182] = new String[]{"XXX.../.XXXXX/....X./","..X/.XX/.XX/.X./XX./.X./",".X..../XXXXX./...XXX/",".X./.XX/.X./XX./XX./X../","...XXX/XXXXX./.X..../",".X./XX./.X./.XX/.XX/..X/","....X./.XXXXX/XXX.../","X../XX./XX./.X./.XX/.X./"};
		shape[183] = new String[]{"XXX..X/.XXXXX/",".X/XX/XX/X./X./XX/","XXXXX./X..XXX/","XX/.X/.X/XX/XX/X./","X..XXX/XXXXX./","XX/X./X./XX/XX/.X/",".XXXXX/XXX..X/","X./XX/XX/.X/.X/XX/"};
		shape[184] = new String[]{"XXX.../.XXXXX/.....X/","..X/.XX/.XX/.X./.X./XX./","X...../XXXXX./...XXX/",".XX/.X./.X./XX./XX./X../","...XXX/XXXXX./X...../","XX./.X./.X./.XX/.XX/..X/",".....X/.XXXXX/XXX.../","X../XX./XX./.X./.X./.XX/"};
		shape[185] = new String[]{".XX.../XX..../.XXXXX/",".X./XXX/X.X/X../X../X../","XXXXX./....XX/...XX./","..X/..X/..X/X.X/XXX/.X./","...XX./....XX/XXXXX./","X../X../X../X.X/XXX/.X./",".XXXXX/XX..../.XX.../",".X./XXX/X.X/..X/..X/..X/"};
		shape[186] = new String[]{".X..../.X..../XX..../.XXXXX/",".X../XXXX/X.../X.../X.../X.../","XXXXX./....XX/....X./....X./","...X/...X/...X/...X/XXXX/..X./","....X./....X./....XX/XXXXX./","X.../X.../X.../X.../XXXX/.X../",".XXXXX/XX..../.X..../.X..../","..X./XXXX/...X/...X/...X/...X/"};
		shape[187] = new String[]{".X..../XX..../.XXXXX/.X..../","..X./XXXX/.X../.X../.X../.X../","....X./XXXXX./....XX/....X./","..X./..X./..X./..X./XXXX/.X../","....X./....XX/XXXXX./....X./",".X../.X../.X../.X../XXXX/..X./",".X..../.XXXXX/XX..../.X..../",".X../XXXX/..X./..X./..X./..X./"};
		shape[188] = new String[]{".X..../XX..../.XXXXX/..X.../","..X./.XXX/XX../.X../.X../.X../","...X../XXXXX./....XX/....X./","..X./..X./..X./..XX/XXX./.X../","....X./....XX/XXXXX./...X../",".X../.X../.X../XX../.XXX/..X./","..X.../.XXXXX/XX..../.X..../",".X../XXX./..XX/..X./..X./..X./"};
		shape[189] = new String[]{".X..../XX.X../.XXXXX/",".X./XXX/X../XX./X../X../","XXXXX./..X.XX/....X./","..X/..X/.XX/..X/XXX/.X./","....X./..X.XX/XXXXX./","X../X../XX./X../XXX/.X./",".XXXXX/XX.X../.X..../",".X./XXX/..X/.XX/..X/..X/"};
		shape[190] = new String[]{".X..../XX..../.XXXXX/...X../","..X./.XXX/.X../XX../.X../.X../","..X.../XXXXX./....XX/....X./","..X./..X./..XX/..X./XXX./.X../","....X./....XX/XXXXX./..X.../",".X../.X../XX../.X../.XXX/..X./","...X../.XXXXX/XX..../.X..../",".X../XXX./..X./..XX/..X./..X./"};
		shape[191] = new String[]{".X..../XX..X./.XXXXX/",".X./XXX/X../X../XX./X../","XXXXX./.X..XX/....X./","..X/.XX/..X/..X/XXX/.X./","....X./.X..XX/XXXXX./","X../XX./X../X../XXX/.X./",".XXXXX/XX..X./.X..../",".X./XXX/..X/..X/.XX/..X/"};
		shape[192] = new String[]{".X..../XX..../.XXXXX/....X./","..X./.XXX/.X../.X../XX../.X../",".X..../XXXXX./....XX/....X./","..X./..XX/..X./..X./XXX./.X../","....X./....XX/XXXXX./.X..../",".X../XX../.X../.X../.XXX/..X./","....X./.XXXXX/XX..../.X..../",".X../XXX./..X./..X./..XX/..X./"};
		shape[193] = new String[]{".X..../XX...X/.XXXXX/",".X./XXX/X../X../X../XX./","XXXXX./X...XX/....X./",".XX/..X/..X/..X/XXX/.X./","....X./X...XX/XXXXX./","XX./X../X../X../XXX/.X./",".XXXXX/XX...X/.X..../",".X./XXX/..X/..X/..X/.XX/"};
		shape[194] = new String[]{".X..../XX..../.XXXXX/.....X/","..X./.XXX/.X../.X../.X../XX../","X...../XXXXX./....XX/....X./","..XX/..X./..X./..X./XXX./.X../","....X./....XX/XXXXX./X...../","XX../.X../.X../.X../.XXX/..X./",".....X/.XXXXX/XX..../.X..../",".X../XXX./..X./..X./..X./..XX/"};
		shape[195] = new String[]{"XX..../.XXXXX/.XX.../","..X/XXX/XX./.X./.X./.X./","...XX./XXXXX./....XX/",".X./.X./.X./.XX/XXX/X../","....XX/XXXXX./...XX./",".X./.X./.X./XX./XXX/..X/",".XX.../.XXXXX/XX..../","X../XXX/.XX/.X./.X./.X./"};
		shape[196] = new String[]{"XX.X../.XXXXX/.X..../","..X/XXX/.X./.XX/.X./.X./","....X./XXXXX./..X.XX/",".X./.X./XX./.X./XXX/X../","..X.XX/XXXXX./....X./",".X./.X./.XX/.X./XXX/..X/",".X..../.XXXXX/XX.X../","X../XXX/.X./XX./.X./.X./"};
		shape[197] = new String[]{"XX..../.XXXXX/.X.X../","..X/XXX/.X./XX./.X./.X./","..X.X./XXXXX./....XX/",".X./.X./.XX/.X./XXX/X../","....XX/XXXXX./..X.X./",".X./.X./XX./.X./XXX/..X/",".X.X../.XXXXX/XX..../","X../XXX/.X./.XX/.X./.X./"};
		shape[198] = new String[]{"XX..X./.XXXXX/.X..../","..X/XXX/.X./.X./.XX/.X./","....X./XXXXX./.X..XX/",".X./XX./.X./.X./XXX/X../",".X..XX/XXXXX./....X./",".X./.XX/.X./.X./XXX/..X/",".X..../.XXXXX/XX..X./","X../XXX/.X./.X./XX./.X./"};
		shape[199] = new String[]{"XX..../.XXXXX/.X..X./","..X/XXX/.X./.X./XX./.X./",".X..X./XXXXX./....XX/",".X./.XX/.X./.X./XXX/X../","....XX/XXXXX./.X..X./",".X./XX./.X./.X./XXX/..X/",".X..X./.XXXXX/XX..../","X../XXX/.X./.X./.XX/.X./"};
		shape[200] = new String[]{"XX...X/.XXXXX/.X..../","..X/XXX/.X./.X./.X./.XX/","....X./XXXXX./X...XX/","XX./.X./.X./.X./XXX/X../","X...XX/XXXXX./....X./",".XX/.X./.X./.X./XXX/..X/",".X..../.XXXXX/XX...X/","X../XXX/.X./.X./.X./XX./"};
		shape[201] = new String[]{"XX..../.XXXXX/.X...X/","..X/XXX/.X./.X./.X./XX./","X...X./XXXXX./....XX/",".XX/.X./.X./.X./XXX/X../","....XX/XXXXX./X...X./","XX./.X./.X./.X./XXX/..X/",".X...X/.XXXXX/XX..../","X../XXX/.X./.X./.X./.XX/"};
		shape[202] = new String[]{"XX..../.XXXXX/XX..../","X.X/XXX/.X./.X./.X./.X./","....XX/XXXXX./....XX/",".X./.X./.X./.X./XXX/X.X/"};
		shape[203] = new String[]{"XX..../.XXXXX/.X..../.X..../","...X/XXXX/..X./..X./..X./..X./","....X./....X./XXXXX./....XX/",".X../.X../.X../.X../XXXX/X.../","....XX/XXXXX./....X./....X./","..X./..X./..X./..X./XXXX/...X/",".X..../.X..../.XXXXX/XX..../","X.../XXXX/.X../.X../.X../.X../"};
		shape[204] = new String[]{"XX.X../.XXXXX/..X.../","..X/.XX/XX./.XX/.X./.X./","...X../XXXXX./..X.XX/",".X./.X./XX./.XX/XX./X../","..X.XX/XXXXX./...X../",".X./.X./.XX/XX./.XX/..X/","..X.../.XXXXX/XX.X../","X../XX./.XX/XX./.X./.X./"};
		shape[205] = new String[]{"XX..../.XXXXX/..XX../","..X/.XX/XX./XX./.X./.X./","..XX../XXXXX./....XX/",".X./.X./.XX/.XX/XX./X../","....XX/XXXXX./..XX../",".X./.X./XX./XX./.XX/..X/","..XX../.XXXXX/XX..../","X../XX./.XX/.XX/.X./.X./"};
		shape[206] = new String[]{"XX..X./.XXXXX/..X.../","..X/.XX/XX./.X./.XX/.X./","...X../XXXXX./.X..XX/",".X./XX./.X./.XX/XX./X../",".X..XX/XXXXX./...X../",".X./.XX/.X./XX./.XX/..X/","..X.../.XXXXX/XX..X./","X../XX./.XX/.X./XX./.X./"};
		shape[207] = new String[]{"XX..../.XXXXX/..X.X./","..X/.XX/XX./.X./XX./.X./",".X.X../XXXXX./....XX/",".X./.XX/.X./.XX/XX./X../","....XX/XXXXX./.X.X../",".X./XX./.X./XX./.XX/..X/","..X.X./.XXXXX/XX..../","X../XX./.XX/.X./.XX/.X./"};
		shape[208] = new String[]{"XX...X/.XXXXX/..X.../","..X/.XX/XX./.X./.X./.XX/","...X../XXXXX./X...XX/","XX./.X./.X./.XX/XX./X../","X...XX/XXXXX./...X../",".XX/.X./.X./XX./.XX/..X/","..X.../.XXXXX/XX...X/","X../XX./.XX/.X./.X./XX./"};
		shape[209] = new String[]{"XX..../.XXXXX/..X..X/","..X/.XX/XX./.X./.X./XX./","X..X../XXXXX./....XX/",".XX/.X./.X./.XX/XX./X../","....XX/XXXXX./X..X../","XX./.X./.X./XX./.XX/..X/","..X..X/.XXXXX/XX..../","X../XX./.XX/.X./.X./.XX/"};
		shape[210] = new String[]{"XX..../.XXXXX/..X.../..X.../","...X/..XX/XXX./..X./..X./..X./","...X../...X../XXXXX./....XX/",".X../.X../.X../.XXX/XX../X.../","....XX/XXXXX./...X../...X../","..X./..X./..X./XXX./..XX/...X/","..X.../..X.../.XXXXX/XX..../","X.../XX../.XXX/.X../.X../.X../"};
		shape[211] = new String[]{"XX.XX./.XXXXX/",".X/XX/X./XX/XX/X./","XXXXX./.XX.XX/",".X/XX/XX/.X/XX/X./",".XX.XX/XXXXX./","X./XX/XX/X./XX/.X/",".XXXXX/XX.XX./","X./XX/.X/XX/XX/.X/"};
		shape[212] = new String[]{"...X../XX.X../.XXXXX/",".X./XX./X../XXX/X../X../","XXXXX./..X.XX/..X.../","..X/..X/XXX/..X/.XX/.X./","..X.../..X.XX/XXXXX./","X../X../XXX/X../XX./.X./",".XXXXX/XX.X../...X../",".X./.XX/..X/XXX/..X/..X/"};
		shape[213] = new String[]{"XX.X../.XXXXX/...X../","..X/.XX/.X./XXX/.X./.X./","..X.../XXXXX./..X.XX/",".X./.X./XXX/.X./XX./X../","..X.XX/XXXXX./..X.../",".X./.X./XXX/.X./.XX/..X/","...X../.XXXXX/XX.X../","X../XX./.X./XXX/.X./.X./"};
		shape[214] = new String[]{"XX.X../.XXXXX/....X./","..X/.XX/.X./.XX/XX./.X./",".X..../XXXXX./..X.XX/",".X./.XX/XX./.X./XX./X../","..X.XX/XXXXX./.X..../",".X./XX./.XX/.X./.XX/..X/","....X./.XXXXX/XX.X../","X../XX./.X./XX./.XX/.X./"};
		shape[215] = new String[]{"XX.X.X/.XXXXX/",".X/XX/X./XX/X./XX/","XXXXX./X.X.XX/","XX/.X/XX/.X/XX/X./","X.X.XX/XXXXX./","XX/X./XX/X./XX/.X/",".XXXXX/XX.X.X/","X./XX/.X/XX/.X/XX/"};
		shape[216] = new String[]{"XX.X../.XXXXX/.....X/","..X/.XX/.X./.XX/.X./XX./","X...../XXXXX./..X.XX/",".XX/.X./XX./.X./XX./X../","..X.XX/XXXXX./X...../","XX./.X./.XX/.X./.XX/..X/",".....X/.XXXXX/XX.X../","X../XX./.X./XX./.X./.XX/"};
		shape[217] = new String[]{"XX..X./.XXXXX/...X../","..X/.XX/.X./XX./.XX/.X./","..X.../XXXXX./.X..XX/",".X./XX./.XX/.X./XX./X../",".X..XX/XXXXX./..X.../",".X./.XX/XX./.X./.XX/..X/","...X../.XXXXX/XX..X./","X../XX./.X./.XX/XX./.X./"};
		shape[218] = new String[]{"XX..../.XXXXX/...XX./","..X/.XX/.X./XX./XX./.X./",".XX.../XXXXX./....XX/",".X./.XX/.XX/.X./XX./X../","....XX/XXXXX./.XX.../",".X./XX./XX./.X./.XX/..X/","...XX./.XXXXX/XX..../","X../XX./.X./.XX/.XX/.X./"};
		shape[219] = new String[]{"XX...X/.XXXXX/...X../","..X/.XX/.X./XX./.X./.XX/","..X.../XXXXX./X...XX/","XX./.X./.XX/.X./XX./X../","X...XX/XXXXX./..X.../",".XX/.X./XX./.X./.XX/..X/","...X../.XXXXX/XX...X/","X../XX./.X./.XX/.X./XX./"};
		shape[220] = new String[]{"XX..../.XXXXX/...X.X/","..X/.XX/.X./XX./.X./XX./","X.X.../XXXXX./....XX/",".XX/.X./.XX/.X./XX./X../","....XX/XXXXX./X.X.../","XX./.X./XX./.X./.XX/..X/","...X.X/.XXXXX/XX..../","X../XX./.X./.XX/.X./.XX/"};
		shape[221] = new String[]{"XX..../.XXXXX/...X../...X../","...X/..XX/..X./XXX./..X./..X./","..X.../..X.../XXXXX./....XX/",".X../.X../.XXX/.X../XX../X.../","....XX/XXXXX./..X.../..X.../","..X./..X./XXX./..X./..XX/...X/","...X../...X../.XXXXX/XX..../","X.../XX../.X../.XXX/.X../.X../"};
		shape[222] = new String[]{"XX..XX/.XXXXX/",".X/XX/X./X./XX/XX/","XXXXX./XX..XX/","XX/XX/.X/.X/XX/X./","XX..XX/XXXXX./","XX/XX/X./X./XX/.X/",".XXXXX/XX..XX/","X./XX/.X/.X/XX/XX/"};
		shape[223] = new String[]{"....X./XX..X./.XXXXX/",".X./XX./X../X../XXX/X../","XXXXX./.X..XX/.X..../","..X/XXX/..X/..X/.XX/.X./",".X..../.X..XX/XXXXX./","X../XXX/X../X../XX./.X./",".XXXXX/XX..X./....X./",".X./.XX/..X/..X/XXX/..X/"};
		shape[224] = new String[]{"XX..X./.XXXXX/....X./","..X/.XX/.X./.X./XXX/.X./",".X..../XXXXX./.X..XX/",".X./XXX/.X./.X./XX./X../",".X..XX/XXXXX./.X..../",".X./XXX/.X./.X./.XX/..X/","....X./.XXXXX/XX..X./","X../XX./.X./.X./XXX/.X./"};
		shape[225] = new String[]{"XX..X./.XXXXX/.....X/","..X/.XX/.X./.X./.XX/XX./","X...../XXXXX./.X..XX/",".XX/XX./.X./.X./XX./X../",".X..XX/XXXXX./X...../","XX./.XX/.X./.X./.XX/..X/",".....X/.XXXXX/XX..X./","X../XX./.X./.X./XX./.XX/"};
		shape[226] = new String[]{"XX...X/.XXXXX/....X./","..X/.XX/.X./.X./XX./.XX/",".X..../XXXXX./X...XX/","XX./.XX/.X./.X./XX./X../","X...XX/XXXXX./.X..../",".XX/XX./.X./.X./.XX/..X/","....X./.XXXXX/XX...X/","X../XX./.X./.X./.XX/XX./"};
		shape[227] = new String[]{"XX..../.XXXXX/....XX/","..X/.XX/.X./.X./XX./XX./","XX..../XXXXX./....XX/",".XX/.XX/.X./.X./XX./X../","....XX/XXXXX./XX..../","XX./XX./.X./.X./.XX/..X/","....XX/.XXXXX/XX..../","X../XX./.X./.X./.XX/.XX/"};
		shape[228] = new String[]{"XX..../.XXXXX/....X./....X./","...X/..XX/..X./..X./XXX./..X./",".X..../.X..../XXXXX./....XX/",".X../.XXX/.X../.X../XX../X.../","....XX/XXXXX./.X..../.X..../","..X./XXX./..X./..X./..XX/...X/","....X./....X./.XXXXX/XX..../","X.../XX../.X../.X../.XXX/.X../"};
		shape[229] = new String[]{"XX...XX/.XXXXX./",".X/XX/X./X./X./XX/.X/",".XXXXX./XX...XX/","X./XX/.X/.X/.X/XX/X./"};
		shape[230] = new String[]{".....X/XX...X/.XXXXX/",".X./XX./X../X../X../XXX/","XXXXX./X...XX/X...../","XXX/..X/..X/..X/.XX/.X./","X...../X...XX/XXXXX./","XXX/X../X../X../XX./.X./",".XXXXX/XX...X/.....X/",".X./.XX/..X/..X/..X/XXX/"};
		shape[231] = new String[]{"XX...X/.XXXXX/.....X/","..X/.XX/.X./.X./.X./XXX/","X...../XXXXX./X...XX/","XXX/.X./.X./.X./XX./X../","X...XX/XXXXX./X...../","XXX/.X./.X./.X./.XX/..X/",".....X/.XXXXX/XX...X/","X../XX./.X./.X./.X./XXX/"};
		shape[232] = new String[]{"XX...../.XXXXX./.....XX/","..X/.XX/.X./.X./.X./XX./X../",".....XX/.XXXXX./XX...../","X../XX./.X./.X./.X./.XX/..X/"};
		shape[233] = new String[]{"XX..../.XXXXX/.....X/.....X/","...X/..XX/..X./..X./..X./XXX./","X...../X...../XXXXX./....XX/",".XXX/.X../.X../.X../XX../X.../","....XX/XXXXX./X...../X...../","XXX./..X./..X./..X./..XX/...X/",".....X/.....X/.XXXXX/XX..../","X.../XX../.X../.X../.X../.XXX/"};
		shape[234] = new String[]{"XX..../.XX.../.XXXXX/","..X/XXX/XX./X../X../X../","XXXXX./...XX./....XX/","..X/..X/..X/.XX/XXX/X../","....XX/...XX./XXXXX./","X../X../X../XX./XXX/..X/",".XXXXX/.XX.../XX..../","X../XXX/.XX/..X/..X/..X/"};
		shape[235] = new String[]{"XX.../XX.../XXXXX/","XXX/XXX/X../X../X../","XXXXX/...XX/...XX/","..X/..X/..X/XXX/XXX/","...XX/...XX/XXXXX/","X../X../X../XXX/XXX/","XXXXX/XX.../XX.../","XXX/XXX/..X/..X/..X/"};
		shape[236] = new String[]{"X..../X..../XX.../XXXXX/","XXXX/XX../X.../X.../X.../","XXXXX/...XX/....X/....X/","...X/...X/...X/..XX/XXXX/","....X/....X/...XX/XXXXX/","X.../X.../X.../XX../XXXX/","XXXXX/XX.../X..../X..../","XXXX/..XX/...X/...X/...X/"};
		shape[237] = new String[]{"X..../XXX../XXXXX/","XXX/XX./XX./X../X../","XXXXX/..XXX/....X/","..X/..X/.XX/.XX/XXX/","....X/..XXX/XXXXX/","X../X../XX./XX./XXX/","XXXXX/XXX../X..../","XXX/.XX/.XX/..X/..X/"};
		shape[238] = new String[]{"X..../XX.../XXXXX/X..../","XXXX/.XX./.X../.X../.X../","....X/XXXXX/...XX/....X/","..X./..X./..X./.XX./XXXX/","....X/...XX/XXXXX/....X/",".X../.X../.X../.XX./XXXX/","X..../XXXXX/XX.../X..../","XXXX/.XX./..X./..X./..X./"};
		shape[239] = new String[]{"X..../XX.../XXXXX/.X.../",".XXX/XXX./.X../.X../.X../","...X./XXXXX/...XX/....X/","..X./..X./..X./.XXX/XXX./","....X/...XX/XXXXX/...X./",".X../.X../.X../XXX./.XXX/",".X.../XXXXX/XX.../X..../","XXX./.XXX/..X./..X./..X./"};
		shape[240] = new String[]{"X..../XX.../XXXXX/..X../",".XXX/.XX./XX../.X../.X../","..X../XXXXX/...XX/....X/","..X./..X./..XX/.XX./XXX./","....X/...XX/XXXXX/..X../",".X../.X../XX../.XX./.XXX/","..X../XXXXX/XX.../X..../","XXX./.XX./..XX/..X./..X./"};
		shape[241] = new String[]{"X..../XX.X./XXXXX/","XXX/XX./X../XX./X../","XXXXX/.X.XX/....X/","..X/.XX/..X/.XX/XXX/","....X/.X.XX/XXXXX/","X../XX./X../XX./XXX/","XXXXX/XX.X./X..../","XXX/.XX/..X/.XX/..X/"};
		shape[242] = new String[]{"X..../XX.../XXXXX/...X./",".XXX/.XX./.X../XX../.X../",".X.../XXXXX/...XX/....X/","..X./..XX/..X./.XX./XXX./","....X/...XX/XXXXX/.X.../",".X../XX../.X../.XX./.XXX/","...X./XXXXX/XX.../X..../","XXX./.XX./..X./..XX/..X./"};
		shape[243] = new String[]{"X..../XX..X/XXXXX/","XXX/XX./X../X../XX./","XXXXX/X..XX/....X/",".XX/..X/..X/.XX/XXX/","....X/X..XX/XXXXX/","XX./X../X../XX./XXX/","XXXXX/XX..X/X..../","XXX/.XX/..X/..X/.XX/"};
		shape[244] = new String[]{"X..../XX.../XXXXX/....X/",".XXX/.XX./.X../.X../XX../","X..../XXXXX/...XX/....X/","..XX/..X./..X./.XX./XXX./","....X/...XX/XXXXX/X..../","XX../.X../.X../.XX./.XXX/","....X/XXXXX/XX.../X..../","XXX./.XX./..X./..X./..XX/"};
		shape[245] = new String[]{".X.../XXX../XXXXX/","XX./XXX/XX./X../X../","XXXXX/..XXX/...X./","..X/..X/.XX/XXX/.XX/","...X./..XXX/XXXXX/","X../X../XX./XXX/XX./","XXXXX/XXX../.X.../",".XX/XXX/.XX/..X/..X/"};
		shape[246] = new String[]{"XXXX./XXXXX/","XX/XX/XX/XX/X./","XXXXX/.XXXX/",".X/XX/XX/XX/XX/",".XXXX/XXXXX/","X./XX/XX/XX/XX/","XXXXX/XXXX./","XX/XX/XX/XX/.X/"};
		shape[247] = new String[]{"..X../XXX../XXXXX/","XX./XX./XXX/X../X../","XXXXX/..XXX/..X../","..X/..X/XXX/.XX/.XX/","..X../..XXX/XXXXX/","X../X../XXX/XX./XX./","XXXXX/XXX../..X../",".XX/.XX/XXX/..X/..X/"};
		shape[248] = new String[]{"XXX../XXXXX/X..../","XXX/.XX/.XX/.X./.X./","....X/XXXXX/..XXX/",".X./.X./XX./XX./XXX/","..XXX/XXXXX/....X/",".X./.X./.XX/.XX/XXX/","X..../XXXXX/XXX../","XXX/XX./XX./.X./.X./"};
		shape[249] = new String[]{"XXX../XXXXX/.X.../",".XX/XXX/.XX/.X./.X./","...X./XXXXX/..XXX/",".X./.X./XX./XXX/XX./","..XXX/XXXXX/...X./",".X./.X./.XX/XXX/.XX/",".X.../XXXXX/XXX../","XX./XXX/XX./.X./.X./"};
		shape[250] = new String[]{"XXX../XXXXX/..X../",".XX/.XX/XXX/.X./.X./","..X../XXXXX/..XXX/",".X./.X./XXX/XX./XX./","..XXX/XXXXX/..X../",".X./.X./XXX/.XX/.XX/","..X../XXXXX/XXX../","XX./XX./XXX/.X./.X./"};
		shape[251] = new String[]{"XXX../XXXXX/...X./",".XX/.XX/.XX/XX./.X./",".X.../XXXXX/..XXX/",".X./.XX/XX./XX./XX./","..XXX/XXXXX/.X.../",".X./XX./.XX/.XX/.XX/","...X./XXXXX/XXX../","XX./XX./XX./.XX/.X./"};
		shape[252] = new String[]{"XXX.X/XXXXX/","XX/XX/XX/X./XX/","XXXXX/X.XXX/","XX/.X/XX/XX/XX/","X.XXX/XXXXX/","XX/X./XX/XX/XX/","XXXXX/XXX.X/","XX/XX/XX/.X/XX/"};
		shape[253] = new String[]{"XXX../XXXXX/....X/",".XX/.XX/.XX/.X./XX./","X..../XXXXX/..XXX/",".XX/.X./XX./XX./XX./","..XXX/XXXXX/X..../","XX./.X./.XX/.XX/.XX/","....X/XXXXX/XXX../","XX./XX./XX./.X./.XX/"};
		shape[254] = new String[]{".XX../XX.../XXXXX/","XX./XXX/X.X/X../X../","XXXXX/...XX/..XX./","..X/..X/X.X/XXX/.XX/","..XX./...XX/XXXXX/","X../X../X.X/XXX/XX./","XXXXX/XX.../.XX../",".XX/XXX/X.X/..X/..X/"};
		shape[255] = new String[]{".X.../.X.../XX.../XXXXX/","XX../XXXX/X.../X.../X.../","XXXXX/...XX/...X./...X./","...X/...X/...X/XXXX/..XX/","...X./...X./...XX/XXXXX/","X.../X.../X.../XXXX/XX../","XXXXX/XX.../.X.../.X.../","..XX/XXXX/...X/...X/...X/"};
		shape[256] = new String[]{".X.../XX.../XXXXX/X..../","XXX./.XXX/.X../.X../.X../","....X/XXXXX/...XX/...X./","..X./..X./..X./XXX./.XXX/","...X./...XX/XXXXX/....X/",".X../.X../.X../.XXX/XXX./","X..../XXXXX/XX.../.X.../",".XXX/XXX./..X./..X./..X./"};
		shape[257] = new String[]{".X.../XX.../XXXXX/.X.../",".XX./XXXX/.X../.X../.X../","...X./XXXXX/...XX/...X./","..X./..X./..X./XXXX/.XX./","...X./...XX/XXXXX/...X./",".X../.X../.X../XXXX/.XX./",".X.../XXXXX/XX.../.X.../",".XX./XXXX/..X./..X./..X./"};
		shape[258] = new String[]{".X.../XX.../XXXXX/..X../",".XX./.XXX/XX../.X../.X../","..X../XXXXX/...XX/...X./","..X./..X./..XX/XXX./.XX./","...X./...XX/XXXXX/..X../",".X../.X../XX../.XXX/.XX./","..X../XXXXX/XX.../.X.../",".XX./XXX./..XX/..X./..X./"};
		shape[259] = new String[]{".X.../XX.X./XXXXX/","XX./XXX/X../XX./X../","XXXXX/.X.XX/...X./","..X/.XX/..X/XXX/.XX/","...X./.X.XX/XXXXX/","X../XX./X../XXX/XX./","XXXXX/XX.X./.X.../",".XX/XXX/..X/.XX/..X/"};
		shape[260] = new String[]{".X.../XX.../XXXXX/...X./",".XX./.XXX/.X../XX../.X../",".X.../XXXXX/...XX/...X./","..X./..XX/..X./XXX./.XX./","...X./...XX/XXXXX/.X.../",".X../XX../.X../.XXX/.XX./","...X./XXXXX/XX.../.X.../",".XX./XXX./..X./..XX/..X./"};
		shape[261] = new String[]{".X.../XX..X/XXXXX/","XX./XXX/X../X../XX./","XXXXX/X..XX/...X./",".XX/..X/..X/XXX/.XX/","...X./X..XX/XXXXX/","XX./X../X../XXX/XX./","XXXXX/XX..X/.X.../",".XX/XXX/..X/..X/.XX/"};
		shape[262] = new String[]{".X.../XX.../XXXXX/....X/",".XX./.XXX/.X../.X../XX../","X..../XXXXX/...XX/...X./","..XX/..X./..X./XXX./.XX./","...X./...XX/XXXXX/X..../","XX../.X../.X../.XXX/.XX./","....X/XXXXX/XX.../.X.../",".XX./XXX./..X./..X./..XX/"};
		shape[263] = new String[]{"XX.../XXXXX/XX.../","XXX/XXX/.X./.X./.X./","...XX/XXXXX/...XX/",".X./.X./.X./XXX/XXX/"};
		shape[264] = new String[]{"XX.../XXXXX/X.X../","XXX/.XX/XX./.X./.X./","..X.X/XXXXX/...XX/",".X./.X./.XX/XX./XXX/","...XX/XXXXX/..X.X/",".X./.X./XX./.XX/XXX/","X.X../XXXXX/XX.../","XXX/XX./.XX/.X./.X./"};
		shape[265] = new String[]{"XX.X./XXXXX/X..../","XXX/.XX/.X./.XX/.X./","....X/XXXXX/.X.XX/",".X./XX./.X./XX./XXX/",".X.XX/XXXXX/....X/",".X./.XX/.X./.XX/XXX/","X..../XXXXX/XX.X./","XXX/XX./.X./XX./.X./"};
		shape[266] = new String[]{"XX.../XXXXX/X..X./","XXX/.XX/.X./XX./.X./",".X..X/XXXXX/...XX/",".X./.XX/.X./XX./XXX/","...XX/XXXXX/.X..X/",".X./XX./.X./.XX/XXX/","X..X./XXXXX/XX.../","XXX/XX./.X./.XX/.X./"};
		shape[267] = new String[]{"XX..X/XXXXX/X..../","XXX/.XX/.X./.X./.XX/","....X/XXXXX/X..XX/","XX./.X./.X./XX./XXX/","X..XX/XXXXX/....X/",".XX/.X./.X./.XX/XXX/","X..../XXXXX/XX..X/","XXX/XX./.X./.X./XX./"};
		shape[268] = new String[]{"XX.../XXXXX/X...X/","XXX/.XX/.X./.X./XX./","X...X/XXXXX/...XX/",".XX/.X./.X./XX./XXX/","...XX/XXXXX/X...X/","XX./.X./.X./.XX/XXX/","X...X/XXXXX/XX.../","XXX/XX./.X./.X./.XX/"};
		shape[269] = new String[]{"XX.../XXXXX/X..../X..../","XXXX/..XX/..X./..X./..X./","....X/....X/XXXXX/...XX/",".X../.X../.X../XX../XXXX/","...XX/XXXXX/....X/....X/","..X./..X./..X./..XX/XXXX/","X..../X..../XXXXX/XX.../","XXXX/XX../.X../.X../.X../"};
		shape[270] = new String[]{"XX.../XXXXX/.XX../",".XX/XXX/XX./.X./.X./","..XX./XXXXX/...XX/",".X./.X./.XX/XXX/XX./","...XX/XXXXX/..XX./",".X./.X./XX./XXX/.XX/",".XX../XXXXX/XX.../","XX./XXX/.XX/.X./.X./"};
		shape[271] = new String[]{"XX.X./XXXXX/.X.../",".XX/XXX/.X./.XX/.X./","...X./XXXXX/.X.XX/",".X./XX./.X./XXX/XX./",".X.XX/XXXXX/...X./",".X./.XX/.X./XXX/.XX/",".X.../XXXXX/XX.X./","XX./XXX/.X./XX./.X./"};
		shape[272] = new String[]{"XX.../XXXXX/.X.X./",".XX/XXX/.X./XX./.X./",".X.X./XXXXX/...XX/",".X./.XX/.X./XXX/XX./","...XX/XXXXX/.X.X./",".X./XX./.X./XXX/.XX/",".X.X./XXXXX/XX.../","XX./XXX/.X./.XX/.X./"};
		shape[273] = new String[]{"XX..X/XXXXX/.X.../",".XX/XXX/.X./.X./.XX/","...X./XXXXX/X..XX/","XX./.X./.X./XXX/XX./","X..XX/XXXXX/...X./",".XX/.X./.X./XXX/.XX/",".X.../XXXXX/XX..X/","XX./XXX/.X./.X./XX./"};
		shape[274] = new String[]{"XX.../XXXXX/.X..X/",".XX/XXX/.X./.X./XX./","X..X./XXXXX/...XX/",".XX/.X./.X./XXX/XX./","...XX/XXXXX/X..X./","XX./.X./.X./XXX/.XX/",".X..X/XXXXX/XX.../","XX./XXX/.X./.X./.XX/"};
		shape[275] = new String[]{"XX.../XXXXX/.X.../.X.../","..XX/XXXX/..X./..X./..X./","...X./...X./XXXXX/...XX/",".X../.X../.X../XXXX/XX../","...XX/XXXXX/...X./...X./","..X./..X./..X./XXXX/..XX/",".X.../.X.../XXXXX/XX.../","XX../XXXX/.X../.X../.X../"};
		shape[276] = new String[]{"XX.X./XXXXX/..X../",".XX/.XX/XX./.XX/.X./","..X../XXXXX/.X.XX/",".X./XX./.XX/XX./XX./",".X.XX/XXXXX/..X../",".X./.XX/XX./.XX/.XX/","..X../XXXXX/XX.X./","XX./XX./.XX/XX./.X./"};
		shape[277] = new String[]{"XX.../XXXXX/..XX./",".XX/.XX/XX./XX./.X./",".XX../XXXXX/...XX/",".X./.XX/.XX/XX./XX./","...XX/XXXXX/.XX../",".X./XX./XX./.XX/.XX/","..XX./XXXXX/XX.../","XX./XX./.XX/.XX/.X./"};
		shape[278] = new String[]{"XX..X/XXXXX/..X../",".XX/.XX/XX./.X./.XX/","..X../XXXXX/X..XX/","XX./.X./.XX/XX./XX./","X..XX/XXXXX/..X../",".XX/.X./XX./.XX/.XX/","..X../XXXXX/XX..X/","XX./XX./.XX/.X./XX./"};
		shape[279] = new String[]{"XX.../XXXXX/..X.X/",".XX/.XX/XX./.X./XX./","X.X../XXXXX/...XX/",".XX/.X./.XX/XX./XX./","...XX/XXXXX/X.X../","XX./.X./XX./.XX/.XX/","..X.X/XXXXX/XX.../","XX./XX./.XX/.X./.XX/"};
		shape[280] = new String[]{"XX.../XXXXX/..X../..X../","..XX/..XX/XXX./..X./..X./","..X../..X../XXXXX/...XX/",".X../.X../.XXX/XX../XX../","...XX/XXXXX/..X../..X../","..X./..X./XXX./..XX/..XX/","..X../..X../XXXXX/XX.../","XX../XX../.XXX/.X../.X../"};
		shape[281] = new String[]{"XX.XX/XXXXX/","XX/XX/X./XX/XX/","XXXXX/XX.XX/","XX/XX/.X/XX/XX/"};
		shape[282] = new String[]{"...X./XX.X./XXXXX/","XX./XX./X../XXX/X../","XXXXX/.X.XX/.X.../","..X/XXX/..X/.XX/.XX/",".X.../.X.XX/XXXXX/","X../XXX/X../XX./XX./","XXXXX/XX.X./...X./",".XX/.XX/..X/XXX/..X/"};
		shape[283] = new String[]{"XX.X./XXXXX/...X./",".XX/.XX/.X./XXX/.X./",".X.../XXXXX/.X.XX/",".X./XXX/.X./XX./XX./",".X.XX/XXXXX/.X.../",".X./XXX/.X./.XX/.XX/","...X./XXXXX/XX.X./","XX./XX./.X./XXX/.X./"};
		shape[284] = new String[]{"XX.X./XXXXX/....X/",".XX/.XX/.X./.XX/XX./","X..../XXXXX/.X.XX/",".XX/XX./.X./XX./XX./",".X.XX/XXXXX/X..../","XX./.XX/.X./.XX/.XX/","....X/XXXXX/XX.X./","XX./XX./.X./XX./.XX/"};
		shape[285] = new String[]{"XX..X/XXXXX/...X./",".XX/.XX/.X./XX./.XX/",".X.../XXXXX/X..XX/","XX./.XX/.X./XX./XX./","X..XX/XXXXX/.X.../",".XX/XX./.X./.XX/.XX/","...X./XXXXX/XX..X/","XX./XX./.X./.XX/XX./"};
		shape[286] = new String[]{"XX.../XXXXX/...XX/",".XX/.XX/.X./XX./XX./","...XX/XXXXX/XX.../","XX./XX./.X./.XX/.XX/"};
		shape[287] = new String[]{"XX.../XXXXX/...X./...X./","..XX/..XX/..X./XXX./..X./",".X.../.X.../XXXXX/...XX/",".X../.XXX/.X../XX../XX../","...XX/XXXXX/.X.../.X.../","..X./XXX./..X./..XX/..XX/","...X./...X./XXXXX/XX.../","XX../XX../.X../.XXX/.X../"};
		shape[288] = new String[]{"....X/XX..X/XXXXX/","XX./XX./X../X../XXX/","XXXXX/X..XX/X..../","XXX/..X/..X/.XX/.XX/","X..../X..XX/XXXXX/","XXX/X../X../XX./XX./","XXXXX/XX..X/....X/",".XX/.XX/..X/..X/XXX/"};
		shape[289] = new String[]{"XX..X/XXXXX/....X/",".XX/.XX/.X./.X./XXX/","X..../XXXXX/X..XX/","XXX/.X./.X./XX./XX./","X..XX/XXXXX/X..../","XXX/.X./.X./.XX/.XX/","....X/XXXXX/XX..X/","XX./XX./.X./.X./XXX/"};
		shape[290] = new String[]{"XX.../XXXXX/....X/....X/","..XX/..XX/..X./..X./XXX./","X..../X..../XXXXX/...XX/",".XXX/.X../.X../XX../XX../","...XX/XXXXX/X..../X..../","XXX./..X./..X./..XX/..XX/","....X/....X/XXXXX/XX.../","XX../XX../.X../.X../.XXX/"};
		shape[291] = new String[]{"XXX..../..X..../..XXXXX/","..X/..X/XXX/X../X../X../X../","XXXXX../....X../....XXX/","..X/..X/..X/..X/XXX/X../X../","....XXX/....X../XXXXX../","X../X../X../X../XXX/..X/..X/","..XXXXX/..X..../XXX..../","X../X../XXX/..X/..X/..X/..X/"};
		shape[292] = new String[]{"X...../XX..../.X..../.XXXXX/","..XX/XXX./X.../X.../X.../X.../","XXXXX./....X./....XX/.....X/","...X/...X/...X/...X/.XXX/XX../",".....X/....XX/....X./XXXXX./","X.../X.../X.../X.../XXX./..XX/",".XXXXX/.X..../XX..../X...../","XX../.XXX/...X/...X/...X/...X/"};
		shape[293] = new String[]{"XXX.../.X..../.XXXXX/","..X/XXX/X.X/X../X../X../","XXXXX./....X./...XXX/","..X/..X/..X/X.X/XXX/X../","...XXX/....X./XXXXX./","X../X../X../X.X/XXX/..X/",".XXXXX/.X..../XXX.../","X../XXX/X.X/..X/..X/..X/"};
		shape[294] = new String[]{".X..../XX..../.X..../.XXXXX/","..X./XXXX/X.../X.../X.../X.../","XXXXX./....X./....XX/....X./","...X/...X/...X/...X/XXXX/.X../","....X./....XX/....X./XXXXX./","X.../X.../X.../X.../XXXX/..X./",".XXXXX/.X..../XX..../.X..../",".X../XXXX/...X/...X/...X/...X/"};
		shape[295] = new String[]{"XX..../.X..../.XXXXX/.X..../","...X/XXXX/.X../.X../.X../.X../","....X./XXXXX./....X./....XX/","..X./..X./..X./..X./XXXX/X.../","....XX/....X./XXXXX./....X./",".X../.X../.X../.X../XXXX/...X/",".X..../.XXXXX/.X..../XX..../","X.../XXXX/..X./..X./..X./..X./"};
		shape[296] = new String[]{"XX..../.X..../.XXXXX/..X.../","...X/.XXX/XX../.X../.X../.X../","...X../XXXXX./....X./....XX/","..X./..X./..X./..XX/XXX./X.../","....XX/....X./XXXXX./...X../",".X../.X../.X../XX../.XXX/...X/","..X.../.XXXXX/.X..../XX..../","X.../XXX./..XX/..X./..X./..X./"};
		shape[297] = new String[]{"XX..../.X.X../.XXXXX/","..X/XXX/X../XX./X../X../","XXXXX./..X.X./....XX/","..X/..X/.XX/..X/XXX/X../","....XX/..X.X./XXXXX./","X../X../XX./X../XXX/..X/",".XXXXX/.X.X../XX..../","X../XXX/..X/.XX/..X/..X/"};
		shape[298] = new String[]{"XX..../.X..../.XXXXX/...X../","...X/.XXX/.X../XX../.X../.X../","..X.../XXXXX./....X./....XX/","..X./..X./..XX/..X./XXX./X.../","....XX/....X./XXXXX./..X.../",".X../.X../XX../.X../.XXX/...X/","...X../.XXXXX/.X..../XX..../","X.../XXX./..X./..XX/..X./..X./"};
		shape[299] = new String[]{"XX..../.X..X./.XXXXX/","..X/XXX/X../X../XX./X../","XXXXX./.X..X./....XX/","..X/.XX/..X/..X/XXX/X../","....XX/.X..X./XXXXX./","X../XX./X../X../XXX/..X/",".XXXXX/.X..X./XX..../","X../XXX/..X/..X/.XX/..X/"};
		shape[300] = new String[]{"XX..../.X..../.XXXXX/....X./","...X/.XXX/.X../.X../XX../.X../",".X..../XXXXX./....X./....XX/","..X./..XX/..X./..X./XXX./X.../","....XX/....X./XXXXX./.X..../",".X../XX../.X../.X../.XXX/...X/","....X./.XXXXX/.X..../XX..../","X.../XXX./..X./..X./..XX/..X./"};
		shape[301] = new String[]{"XX..../.X...X/.XXXXX/","..X/XXX/X../X../X../XX./","XXXXX./X...X./....XX/",".XX/..X/..X/..X/XXX/X../","....XX/X...X./XXXXX./","XX./X../X../X../XXX/..X/",".XXXXX/.X...X/XX..../","X../XXX/..X/..X/..X/.XX/"};
		shape[302] = new String[]{"XX..../.X..../.XXXXX/.....X/","...X/.XXX/.X../.X../.X../XX../","X...../XXXXX./....X./....XX/","..XX/..X./..X./..X./XXX./X.../","....XX/....X./XXXXX./X...../","XX../.X../.X../.X../.XXX/...X/",".....X/.XXXXX/.X..../XX..../","X.../XXX./..X./..X./..X./..XX/"};
		shape[303] = new String[]{"X..../XX.../X..../XXXXX/","XXXX/X.X./X.../X.../X.../","XXXXX/....X/...XX/....X/","...X/...X/...X/.X.X/XXXX/","....X/...XX/....X/XXXXX/","X.../X.../X.../X.X./XXXX/","XXXXX/X..../XX.../X..../","XXXX/.X.X/...X/...X/...X/"};
		shape[304] = new String[]{"XXX../X..../XXXXX/","XXX/X.X/X.X/X../X../","XXXXX/....X/..XXX/","..X/..X/X.X/X.X/XXX/","..XXX/....X/XXXXX/","X../X../X.X/X.X/XXX/","XXXXX/X..../XXX../","XXX/X.X/X.X/..X/..X/"};
		shape[305] = new String[]{".X.../XX.../X..../XXXXX/","XXX./X.XX/X.../X.../X.../","XXXXX/....X/...XX/...X./","...X/...X/...X/XX.X/.XXX/","...X./...XX/....X/XXXXX/","X.../X.../X.../X.XX/XXX./","XXXXX/X..../XX.../.X.../",".XXX/XX.X/...X/...X/...X/"};
		shape[306] = new String[]{"XX.../X..../XXXXX/X..../","XXXX/.X.X/.X../.X../.X../","....X/XXXXX/....X/...XX/","..X./..X./..X./X.X./XXXX/","...XX/....X/XXXXX/....X/",".X../.X../.X../.X.X/XXXX/","X..../XXXXX/X..../XX.../","XXXX/X.X./..X./..X./..X./"};
		shape[307] = new String[]{"XX.../X..../XXXXX/.X.../",".XXX/XX.X/.X../.X../.X../","...X./XXXXX/....X/...XX/","..X./..X./..X./X.XX/XXX./","...XX/....X/XXXXX/...X./",".X../.X../.X../XX.X/.XXX/",".X.../XXXXX/X..../XX.../","XXX./X.XX/..X./..X./..X./"};
		shape[308] = new String[]{"XX.../X.X../XXXXX/","XXX/X.X/XX./X../X../","XXXXX/..X.X/...XX/","..X/..X/.XX/X.X/XXX/","...XX/..X.X/XXXXX/","X../X../XX./X.X/XXX/","XXXXX/X.X../XX.../","XXX/X.X/.XX/..X/..X/"};
		shape[309] = new String[]{"XX.../X..../XXXXX/..X../",".XXX/.X.X/XX../.X../.X../","..X../XXXXX/....X/...XX/","..X./..X./..XX/X.X./XXX./","...XX/....X/XXXXX/..X../",".X../.X../XX../.X.X/.XXX/","..X../XXXXX/X..../XX.../","XXX./X.X./..XX/..X./..X./"};
		shape[310] = new String[]{"XX.../X..X./XXXXX/","XXX/X.X/X../XX./X../","XXXXX/.X..X/...XX/","..X/.XX/..X/X.X/XXX/","...XX/.X..X/XXXXX/","X../XX./X../X.X/XXX/","XXXXX/X..X./XX.../","XXX/X.X/..X/.XX/..X/"};
		shape[311] = new String[]{"XX.../X..../XXXXX/...X./",".XXX/.X.X/.X../XX../.X../",".X.../XXXXX/....X/...XX/","..X./..XX/..X./X.X./XXX./","...XX/....X/XXXXX/.X.../",".X../XX../.X../.X.X/.XXX/","...X./XXXXX/X..../XX.../","XXX./X.X./..X./..XX/..X./"};
		shape[312] = new String[]{"XX.../X...X/XXXXX/","XXX/X.X/X../X../XX./","XXXXX/X...X/...XX/",".XX/..X/..X/X.X/XXX/","...XX/X...X/XXXXX/","XX./X../X../X.X/XXX/","XXXXX/X...X/XX.../","XXX/X.X/..X/..X/.XX/"};
		shape[313] = new String[]{"XX.../X..../XXXXX/....X/",".XXX/.X.X/.X../.X../XX../","X..../XXXXX/....X/...XX/","..XX/..X./..X./X.X./XXX./","...XX/....X/XXXXX/X..../","XX../.X../.X../.X.X/.XXX/","....X/XXXXX/X..../XX.../","XXX./X.X./..X./..X./..XX/"};
		shape[314] = new String[]{"XX..../.X..../.X..../.XXXXX/","...X/XXXX/X.../X.../X.../X.../","XXXXX./....X./....X./....XX/","...X/...X/...X/...X/XXXX/X.../","....XX/....X./....X./XXXXX./","X.../X.../X.../X.../XXXX/...X/",".XXXXX/.X..../.X..../XX..../","X.../XXXX/...X/...X/...X/...X/"};
		shape[315] = new String[]{"XX.../X..../X..../XXXXX/","XXXX/X..X/X.../X.../X.../","XXXXX/....X/....X/...XX/","...X/...X/...X/X..X/XXXX/","...XX/....X/....X/XXXXX/","X.../X.../X.../X..X/XXXX/","XXXXX/X..../X..../XX.../","XXXX/X..X/...X/...X/...X/"};
		shape[316] = new String[]{"X..../X..../X..../X..../XXXXX/","XXXXX/X..../X..../X..../X..../","XXXXX/....X/....X/....X/....X/","....X/....X/....X/....X/XXXXX/"};
		shape[317] = new String[]{"X..../X..../X..../XXXXX/X..../","XXXXX/.X.../.X.../.X.../.X.../","....X/XXXXX/....X/....X/....X/","...X./...X./...X./...X./XXXXX/","....X/....X/....X/XXXXX/....X/",".X.../.X.../.X.../.X.../XXXXX/","X..../XXXXX/X..../X..../X..../","XXXXX/...X./...X./...X./...X./"};
		shape[318] = new String[]{"X..../X..../X..../XXXXX/.X.../",".XXXX/XX.../.X.../.X.../.X.../","...X./XXXXX/....X/....X/....X/","...X./...X./...X./...XX/XXXX./","....X/....X/....X/XXXXX/...X./",".X.../.X.../.X.../XX.../.XXXX/",".X.../XXXXX/X..../X..../X..../","XXXX./...XX/...X./...X./...X./"};
		shape[319] = new String[]{"X..../X..../X.X../XXXXX/","XXXX/X.../XX../X.../X.../","XXXXX/..X.X/....X/....X/","...X/...X/..XX/...X/XXXX/","....X/....X/..X.X/XXXXX/","X.../X.../XX../X.../XXXX/","XXXXX/X.X../X..../X..../","XXXX/...X/..XX/...X/...X/"};
		shape[320] = new String[]{"X..../X..../X..../XXXXX/..X../",".XXXX/.X.../XX.../.X.../.X.../","..X../XXXXX/....X/....X/....X/","...X./...X./...XX/...X./XXXX./","....X/....X/....X/XXXXX/..X../",".X.../.X.../XX.../.X.../.XXXX/","..X../XXXXX/X..../X..../X..../","XXXX./...X./...XX/...X./...X./"};
		shape[321] = new String[]{"X..../X..../X..X./XXXXX/","XXXX/X.../X.../XX../X.../","XXXXX/.X..X/....X/....X/","...X/..XX/...X/...X/XXXX/","....X/....X/.X..X/XXXXX/","X.../XX../X.../X.../XXXX/","XXXXX/X..X./X..../X..../","XXXX/...X/...X/..XX/...X/"};
		shape[322] = new String[]{"X..../X..../X..../XXXXX/...X./",".XXXX/.X.../.X.../XX.../.X.../",".X.../XXXXX/....X/....X/....X/","...X./...XX/...X./...X./XXXX./","....X/....X/....X/XXXXX/.X.../",".X.../XX.../.X.../.X.../.XXXX/","...X./XXXXX/X..../X..../X..../","XXXX./...X./...X./...XX/...X./"};
		shape[323] = new String[]{"X..../X..../X...X/XXXXX/","XXXX/X.../X.../X.../XX../","XXXXX/X...X/....X/....X/","..XX/...X/...X/...X/XXXX/","....X/....X/X...X/XXXXX/","XX../X.../X.../X.../XXXX/","XXXXX/X...X/X..../X..../","XXXX/...X/...X/...X/..XX/"};
		shape[324] = new String[]{"X..../X..../X..../XXXXX/....X/",".XXXX/.X.../.X.../.X.../XX.../","X..../XXXXX/....X/....X/....X/","...XX/...X./...X./...X./XXXX./","....X/....X/....X/XXXXX/X..../","XX.../.X.../.X.../.X.../.XXXX/","....X/XXXXX/X..../X..../X..../","XXXX./...X./...X./...X./...XX/"};
		shape[325] = new String[]{"X..../X.X../XXXXX/X..../","XXXX/.X../.XX./.X../.X../","....X/XXXXX/..X.X/....X/","..X./..X./.XX./..X./XXXX/","....X/..X.X/XXXXX/....X/",".X../.X../.XX./.X../XXXX/","X..../XXXXX/X.X../X..../","XXXX/..X./.XX./..X./..X./"};
		shape[326] = new String[]{"X..../X..../XXXXX/X.X../","XXXX/.X../XX../.X../.X../","..X.X/XXXXX/....X/....X/","..X./..X./..XX/..X./XXXX/","....X/....X/XXXXX/..X.X/",".X../.X../XX../.X../XXXX/","X.X../XXXXX/X..../X..../","XXXX/..X./..XX/..X./..X./"};
		shape[327] = new String[]{"X..../X..X./XXXXX/X..../","XXXX/.X../.X../.XX./.X../","....X/XXXXX/.X..X/....X/","..X./.XX./..X./..X./XXXX/","....X/.X..X/XXXXX/....X/",".X../.XX./.X../.X../XXXX/","X..../XXXXX/X..X./X..../","XXXX/..X./..X./.XX./..X./"};
		shape[328] = new String[]{"X..../X..../XXXXX/X..X./","XXXX/.X../.X../XX../.X../",".X..X/XXXXX/....X/....X/","..X./..XX/..X./..X./XXXX/","....X/....X/XXXXX/.X..X/",".X../XX../.X../.X../XXXX/","X..X./XXXXX/X..../X..../","XXXX/..X./..X./..XX/..X./"};
		shape[329] = new String[]{"X..../X...X/XXXXX/X..../","XXXX/.X../.X../.X../.XX./","....X/XXXXX/X...X/....X/",".XX./..X./..X./..X./XXXX/","....X/X...X/XXXXX/....X/",".XX./.X../.X../.X../XXXX/","X..../XXXXX/X...X/X..../","XXXX/..X./..X./..X./.XX./"};
		shape[330] = new String[]{"X..../X..../XXXXX/X...X/","XXXX/.X../.X../.X../XX../","X...X/XXXXX/....X/....X/","..XX/..X./..X./..X./XXXX/","....X/....X/XXXXX/X...X/","XX../.X../.X../.X../XXXX/","X...X/XXXXX/X..../X..../","XXXX/..X./..X./..X./..XX/"};
		shape[331] = new String[]{"X..../X..../XXXXX/X..../X..../","XXXXX/..X../..X../..X../..X../","....X/....X/XXXXX/....X/....X/","..X../..X../..X../..X../XXXXX/"};
		shape[332] = new String[]{"X..../X.X../XXXXX/.X.../",".XXX/XX../.XX./.X../.X../","...X./XXXXX/..X.X/....X/","..X./..X./.XX./..XX/XXX./","....X/..X.X/XXXXX/...X./",".X../.X../.XX./XX../.XXX/",".X.../XXXXX/X.X../X..../","XXX./..XX/.XX./..X./..X./"};
		shape[333] = new String[]{"X..../X..../XXXXX/.XX../",".XXX/XX../XX../.X../.X../","..XX./XXXXX/....X/....X/","..X./..X./..XX/..XX/XXX./","....X/....X/XXXXX/..XX./",".X../.X../XX../XX../.XXX/",".XX../XXXXX/X..../X..../","XXX./..XX/..XX/..X./..X./"};
		shape[334] = new String[]{"X..../X..X./XXXXX/.X.../",".XXX/XX../.X../.XX./.X../","...X./XXXXX/.X..X/....X/","..X./.XX./..X./..XX/XXX./","....X/.X..X/XXXXX/...X./",".X../.XX./.X../XX../.XXX/",".X.../XXXXX/X..X./X..../","XXX./..XX/..X./.XX./..X./"};
		shape[335] = new String[]{"X..../X..../XXXXX/.X.X./",".XXX/XX../.X../XX../.X../",".X.X./XXXXX/....X/....X/","..X./..XX/..X./..XX/XXX./","....X/....X/XXXXX/.X.X./",".X../XX../.X../XX../.XXX/",".X.X./XXXXX/X..../X..../","XXX./..XX/..X./..XX/..X./"};
		shape[336] = new String[]{"X..../X...X/XXXXX/.X.../",".XXX/XX../.X../.X../.XX./","...X./XXXXX/X...X/....X/",".XX./..X./..X./..XX/XXX./","....X/X...X/XXXXX/...X./",".XX./.X../.X../XX../.XXX/",".X.../XXXXX/X...X/X..../","XXX./..XX/..X./..X./.XX./"};
		shape[337] = new String[]{"X..../X..../XXXXX/.X..X/",".XXX/XX../.X../.X../XX../","X..X./XXXXX/....X/....X/","..XX/..X./..X./..XX/XXX./","....X/....X/XXXXX/X..X./","XX../.X../.X../XX../.XXX/",".X..X/XXXXX/X..../X..../","XXX./..XX/..X./..X./..XX/"};
		shape[338] = new String[]{"X..../X..../XXXXX/.X.../.X.../","..XXX/XXX../..X../..X../..X../","...X./...X./XXXXX/....X/....X/","..X../..X../..X../..XXX/XXX../","....X/....X/XXXXX/...X./...X./","..X../..X../..X../XXX../..XXX/",".X.../.X.../XXXXX/X..../X..../","XXX../..XXX/..X../..X../..X../"};
		shape[339] = new String[]{"X..../X.XX./XXXXX/","XXX/X../XX./XX./X../","XXXXX/.XX.X/....X/","..X/.XX/.XX/..X/XXX/","....X/.XX.X/XXXXX/","X../XX./XX./X../XXX/","XXXXX/X.XX./X..../","XXX/..X/.XX/.XX/..X/"};
		shape[340] = new String[]{"X.X../X.X../XXXXX/","XXX/X../XXX/X../X../","XXXXX/..X.X/..X.X/","..X/..X/XXX/..X/XXX/","..X.X/..X.X/XXXXX/","X../X../XXX/X../XXX/","XXXXX/X.X../X.X../","XXX/..X/XXX/..X/..X/"};
		shape[341] = new String[]{"X..../X.X../XXXXX/..X../",".XXX/.X../XXX./.X../.X../","..X../XXXXX/..X.X/....X/","..X./..X./.XXX/..X./XXX./","....X/..X.X/XXXXX/..X../",".X../.X../XXX./.X../.XXX/","..X../XXXXX/X.X../X..../","XXX./..X./.XXX/..X./..X./"};
		shape[342] = new String[]{"X..../X.X../XXXXX/...X./",".XXX/.X../.XX./XX../.X../",".X.../XXXXX/..X.X/....X/","..X./..XX/.XX./..X./XXX./","....X/..X.X/XXXXX/.X.../",".X../XX../.XX./.X../.XXX/","...X./XXXXX/X.X../X..../","XXX./..X./.XX./..XX/..X./"};
		shape[343] = new String[]{"X..../X.X.X/XXXXX/","XXX/X../XX./X../XX./","XXXXX/X.X.X/....X/",".XX/..X/.XX/..X/XXX/","....X/X.X.X/XXXXX/","XX./X../XX./X../XXX/","XXXXX/X.X.X/X..../","XXX/..X/.XX/..X/.XX/"};
		shape[344] = new String[]{"X..../X.X../XXXXX/....X/",".XXX/.X../.XX./.X../XX../","X..../XXXXX/..X.X/....X/","..XX/..X./.XX./..X./XXX./","....X/..X.X/XXXXX/X..../","XX../.X../.XX./.X../.XXX/","....X/XXXXX/X.X../X..../","XXX./..X./.XX./..X./..XX/"};
		shape[345] = new String[]{"X..../X..X./XXXXX/..X../",".XXX/.X../XX../.XX./.X../","..X../XXXXX/.X..X/....X/","..X./.XX./..XX/..X./XXX./","....X/.X..X/XXXXX/..X../",".X../.XX./XX../.X../.XXX/","..X../XXXXX/X..X./X..../","XXX./..X./..XX/.XX./..X./"};
		shape[346] = new String[]{"X..../X..../XXXXX/..XX./",".XXX/.X../XX../XX../.X../",".XX../XXXXX/....X/....X/","..X./..XX/..XX/..X./XXX./","....X/....X/XXXXX/.XX../",".X../XX../XX../.X../.XXX/","..XX./XXXXX/X..../X..../","XXX./..X./..XX/..XX/..X./"};
		shape[347] = new String[]{"X..../X...X/XXXXX/..X../",".XXX/.X../XX../.X../.XX./","..X../XXXXX/X...X/....X/",".XX./..X./..XX/..X./XXX./","....X/X...X/XXXXX/..X../",".XX./.X../XX../.X../.XXX/","..X../XXXXX/X...X/X..../","XXX./..X./..XX/..X./.XX./"};
		shape[348] = new String[]{"X..../X..../XXXXX/..X.X/",".XXX/.X../XX../.X../XX../","X.X../XXXXX/....X/....X/","..XX/..X./..XX/..X./XXX./","....X/....X/XXXXX/X.X../","XX../.X../XX../.X../.XXX/","..X.X/XXXXX/X..../X..../","XXX./..X./..XX/..X./..XX/"};
		shape[349] = new String[]{"X..../X..../XXXXX/..X../..X../","..XXX/..X../XXX../..X../..X../","..X../..X../XXXXX/....X/....X/","..X../..X../..XXX/..X../XXX../","....X/....X/XXXXX/..X../..X../","..X../..X../XXX../..X../..XXX/","..X../..X../XXXXX/X..../X..../","XXX../..X../..XXX/..X../..X../"};
		shape[350] = new String[]{"X..X./X..X./XXXXX/","XXX/X../X../XXX/X../","XXXXX/.X..X/.X..X/","..X/XXX/..X/..X/XXX/",".X..X/.X..X/XXXXX/","X../XXX/X../X../XXX/","XXXXX/X..X./X..X./","XXX/..X/..X/XXX/..X/"};
		shape[351] = new String[]{"X..../X..X./XXXXX/...X./",".XXX/.X../.X../XXX./.X../",".X.../XXXXX/.X..X/....X/","..X./.XXX/..X./..X./XXX./","....X/.X..X/XXXXX/.X.../",".X../XXX./.X../.X../.XXX/","...X./XXXXX/X..X./X..../","XXX./..X./..X./.XXX/..X./"};
		shape[352] = new String[]{"X..../X..X./XXXXX/....X/",".XXX/.X../.X../.XX./XX../","X..../XXXXX/.X..X/....X/","..XX/.XX./..X./..X./XXX./","....X/.X..X/XXXXX/X..../","XX../.XX./.X../.X../.XXX/","....X/XXXXX/X..X./X..../","XXX./..X./..X./.XX./..XX/"};
		shape[353] = new String[]{"X..../X...X/XXXXX/...X./",".XXX/.X../.X../XX../.XX./",".X.../XXXXX/X...X/....X/",".XX./..XX/..X./..X./XXX./","....X/X...X/XXXXX/.X.../",".XX./XX../.X../.X../.XXX/","...X./XXXXX/X...X/X..../","XXX./..X./..X./..XX/.XX./"};
		shape[354] = new String[]{"X..../X..../XXXXX/...X./...X./","..XXX/..X../..X../XXX../..X../",".X.../.X.../XXXXX/....X/....X/","..X../..XXX/..X../..X../XXX../","....X/....X/XXXXX/.X.../.X.../","..X../XXX../..X../..X../..XXX/","...X./...X./XXXXX/X..../X..../","XXX../..X../..X../..XXX/..X../"};
		shape[355] = new String[]{"X...X/X...X/XXXXX/","XXX/X../X../X../XXX/","XXXXX/X...X/X...X/","XXX/..X/..X/..X/XXX/"};
		shape[356] = new String[]{"X..../X...X/XXXXX/....X/",".XXX/.X../.X../.X../XXX./","X..../XXXXX/X...X/....X/",".XXX/..X./..X./..X./XXX./","....X/X...X/XXXXX/X..../","XXX./.X../.X../.X../.XXX/","....X/XXXXX/X...X/X..../","XXX./..X./..X./..X./.XXX/"};
		shape[357] = new String[]{"X..../X..../XXXXX/....X/....X/","..XXX/..X../..X../..X../XXX../","....X/....X/XXXXX/X..../X..../","XXX../..X../..X../..X../..XXX/"};
		shape[358] = new String[]{"X.XX./XXXXX/X..../","XXX/.X./.XX/.XX/.X./","....X/XXXXX/.XX.X/",".X./XX./XX./.X./XXX/",".XX.X/XXXXX/....X/",".X./.XX/.XX/.X./XXX/","X..../XXXXX/X.XX./","XXX/.X./XX./XX./.X./"};
		shape[359] = new String[]{"..X../X.X../XXXXX/X..../","XXX./.X../.XXX/.X../.X../","....X/XXXXX/..X.X/..X../","..X./..X./XXX./..X./.XXX/","..X../..X.X/XXXXX/....X/",".X../.X../.XXX/.X../XXX./","X..../XXXXX/X.X../..X../",".XXX/..X./XXX./..X./..X./"};
		shape[360] = new String[]{"X.X../XXXXX/X.X../","XXX/.X./XXX/.X./.X./","..X.X/XXXXX/..X.X/",".X./.X./XXX/.X./XXX/"};
		shape[361] = new String[]{"X.X../XXXXX/X..X./","XXX/.X./.XX/XX./.X./",".X..X/XXXXX/..X.X/",".X./.XX/XX./.X./XXX/","..X.X/XXXXX/.X..X/",".X./XX./.XX/.X./XXX/","X..X./XXXXX/X.X../","XXX/.X./XX./.XX/.X./"};
		shape[362] = new String[]{"X.X.X/XXXXX/X..../","XXX/.X./.XX/.X./.XX/","....X/XXXXX/X.X.X/","XX./.X./XX./.X./XXX/","X.X.X/XXXXX/....X/",".XX/.X./.XX/.X./XXX/","X..../XXXXX/X.X.X/","XXX/.X./XX./.X./XX./"};
		shape[363] = new String[]{"X.X../XXXXX/X...X/","XXX/.X./.XX/.X./XX./","X...X/XXXXX/..X.X/",".XX/.X./XX./.X./XXX/","..X.X/XXXXX/X...X/","XX./.X./.XX/.X./XXX/","X...X/XXXXX/X.X../","XXX/.X./XX./.X./.XX/"};
		shape[364] = new String[]{"...X./X..X./XXXXX/X..../","XXX./.X../.X../.XXX/.X../","....X/XXXXX/.X..X/.X.../","..X./XXX./..X./..X./.XXX/",".X.../.X..X/XXXXX/....X/",".X../.XXX/.X../.X../XXX./","X..../XXXXX/X..X./...X./",".XXX/..X./..X./XXX./..X./"};
		shape[365] = new String[]{"X..X./XXXXX/X..X./","XXX/.X./.X./XXX/.X./",".X..X/XXXXX/.X..X/",".X./XXX/.X./.X./XXX/"};
		shape[366] = new String[]{"X..X./XXXXX/X...X/","XXX/.X./.X./.XX/XX./","X...X/XXXXX/.X..X/",".XX/XX./.X./.X./XXX/",".X..X/XXXXX/X...X/","XX./.XX/.X./.X./XXX/","X...X/XXXXX/X..X./","XXX/.X./.X./XX./.XX/"};
		shape[367] = new String[]{"X...X/XXXXX/X...X/","XXX/.X./.X./.X./XXX/"};
		shape[368] = new String[]{"X.XX./XXXXX/.X.../",".XX/XX./.XX/.XX/.X./","...X./XXXXX/.XX.X/",".X./XX./XX./.XX/XX./",".XX.X/XXXXX/...X./",".X./.XX/.XX/XX./.XX/",".X.../XXXXX/X.XX./","XX./.XX/XX./XX./.X./"};
		shape[369] = new String[]{"..X../X.X../XXXXX/.X.../",".XX./XX../.XXX/.X../.X../","...X./XXXXX/..X.X/..X../","..X./..X./XXX./..XX/.XX./","..X../..X.X/XXXXX/...X./",".X../.X../.XXX/XX../.XX./",".X.../XXXXX/X.X../..X../",".XX./..XX/XXX./..X./..X./"};
		shape[370] = new String[]{"X.X../XXXXX/.XX../",".XX/XX./XXX/.X./.X./","..XX./XXXXX/..X.X/",".X./.X./XXX/.XX/XX./","..X.X/XXXXX/..XX./",".X./.X./XXX/XX./.XX/",".XX../XXXXX/X.X../","XX./.XX/XXX/.X./.X./"};
		shape[371] = new String[]{"X.X../XXXXX/.X.X./",".XX/XX./.XX/XX./.X./",".X.X./XXXXX/..X.X/",".X./.XX/XX./.XX/XX./","..X.X/XXXXX/.X.X./",".X./XX./.XX/XX./.XX/",".X.X./XXXXX/X.X../","XX./.XX/XX./.XX/.X./"};
		shape[372] = new String[]{"X.X.X/XXXXX/.X.../",".XX/XX./.XX/.X./.XX/","...X./XXXXX/X.X.X/","XX./.X./XX./.XX/XX./","X.X.X/XXXXX/...X./",".XX/.X./.XX/XX./.XX/",".X.../XXXXX/X.X.X/","XX./.XX/XX./.X./XX./"};
		shape[373] = new String[]{"X.X../XXXXX/.X..X/",".XX/XX./.XX/.X./XX./","X..X./XXXXX/..X.X/",".XX/.X./XX./.XX/XX./","..X.X/XXXXX/X..X./","XX./.X./.XX/XX./.XX/",".X..X/XXXXX/X.X../","XX./.XX/XX./.X./.XX/"};
		shape[374] = new String[]{"X.X../XXXXX/.X.../.X.../","..XX/XXX./..XX/..X./..X./","...X./...X./XXXXX/..X.X/",".X../.X../XX../.XXX/XX../","..X.X/XXXXX/...X./...X./","..X./..X./..XX/XXX./..XX/",".X.../.X.../XXXXX/X.X../","XX../.XXX/XX../.X../.X../"};
		shape[375] = new String[]{"X..X./XXXXX/.XX../",".XX/XX./XX./.XX/.X./","..XX./XXXXX/.X..X/",".X./XX./.XX/.XX/XX./",".X..X/XXXXX/..XX./",".X./.XX/XX./XX./.XX/",".XX../XXXXX/X..X./","XX./.XX/.XX/XX./.X./"};
		shape[376] = new String[]{"X..../XXXXX/.XXX./",".XX/XX./XX./XX./.X./",".XXX./XXXXX/....X/",".X./.XX/.XX/.XX/XX./","....X/XXXXX/.XXX./",".X./XX./XX./XX./.XX/",".XXX./XXXXX/X..../","XX./.XX/.XX/.XX/.X./"};
		shape[377] = new String[]{"X...X/XXXXX/.XX../",".XX/XX./XX./.X./.XX/","..XX./XXXXX/X...X/","XX./.X./.XX/.XX/XX./","X...X/XXXXX/..XX./",".XX/.X./XX./XX./.XX/",".XX../XXXXX/X...X/","XX./.XX/.XX/.X./XX./"};
		shape[378] = new String[]{"X..../XXXXX/.XX.X/",".XX/XX./XX./.X./XX./","X.XX./XXXXX/....X/",".XX/.X./.XX/.XX/XX./","....X/XXXXX/X.XX./","XX./.X./XX./XX./.XX/",".XX.X/XXXXX/X..../","XX./.XX/.XX/.X./.XX/"};
		shape[379] = new String[]{"X..../XXXXX/.XX../.X.../","..XX/XXX./.XX./..X./..X./","...X./..XX./XXXXX/....X/",".X../.X../.XX./.XXX/XX../","....X/XXXXX/..XX./...X./","..X./..X./.XX./XXX./..XX/",".X.../.XX../XXXXX/X..../","XX../.XXX/.XX./.X../.X../"};
		shape[380] = new String[]{"X..../XXXXX/.XX../..X../","..XX/.XX./XXX./..X./..X./","..X../..XX./XXXXX/....X/",".X../.X../.XXX/.XX./XX../","....X/XXXXX/..XX./..X../","..X./..X./XXX./.XX./..XX/","..X../.XX../XXXXX/X..../","XX../.XX./.XXX/.X../.X../"};
		shape[381] = new String[]{"...X./X..X./XXXXX/.X.../",".XX./XX../.X../.XXX/.X../","...X./XXXXX/.X..X/.X.../","..X./XXX./..X./..XX/.XX./",".X.../.X..X/XXXXX/...X./",".X../.XXX/.X../XX../.XX./",".X.../XXXXX/X..X./...X./",".XX./..XX/..X./XXX./..X./"};
		shape[382] = new String[]{"X..X./XXXXX/.X.X./",".XX/XX./.X./XXX/.X./",".X.X./XXXXX/.X..X/",".X./XXX/.X./.XX/XX./",".X..X/XXXXX/.X.X./",".X./XXX/.X./XX./.XX/",".X.X./XXXXX/X..X./","XX./.XX/.X./XXX/.X./"};
		shape[383] = new String[]{"X..X./XXXXX/.X..X/",".XX/XX./.X./.XX/XX./",".X..X/XXXXX/X..X./","XX./.XX/.X./XX./.XX/"};
		shape[384] = new String[]{"X..X./XXXXX/.X.../.X.../","..XX/XXX./..X./..XX/..X./","...X./...X./XXXXX/.X..X/",".X../XX../.X../.XXX/XX../",".X..X/XXXXX/...X./...X./","..X./..XX/..X./XXX./..XX/",".X.../.X.../XXXXX/X..X./","XX../.XXX/.X../XX../.X../"};
		shape[385] = new String[]{"X...X/XXXXX/.X.X./",".XX/XX./.X./XX./.XX/",".X.X./XXXXX/X...X/","XX./.XX/.X./.XX/XX./"};
		shape[386] = new String[]{"X..../XXXXX/.X.X./.X.../","..XX/XXX./..X./.XX./..X./","...X./.X.X./XXXXX/....X/",".X../.XX./.X../.XXX/XX../","....X/XXXXX/.X.X./...X./","..X./.XX./..X./XXX./..XX/",".X.../.X.X./XXXXX/X..../","XX../.XXX/.X../.XX./.X../"};
		shape[387] = new String[]{"X..../XXXXX/.X.X./...X./","..XX/.XX./..X./XXX./..X./",".X.../.X.X./XXXXX/....X/",".X../.XXX/.X../.XX./XX../","....X/XXXXX/.X.X./.X.../","..X./XXX./..X./.XX./..XX/","...X./.X.X./XXXXX/X..../","XX../.XX./.X../.XXX/.X../"};
		shape[388] = new String[]{"X...X/XXXXX/.X.../.X.../","..XX/XXX./..X./..X./..XX/","...X./...X./XXXXX/X...X/","XX../.X../.X../.XXX/XX../","X...X/XXXXX/...X./...X./","..XX/..X./..X./XXX./..XX/",".X.../.X.../XXXXX/X...X/","XX../.XXX/.X../.X../XX../"};
		shape[389] = new String[]{"X..../XXXXX/.X..X/.X.../","..XX/XXX./..X./..X./.XX./","...X./X..X./XXXXX/....X/",".XX./.X../.X../.XXX/XX../","....X/XXXXX/X..X./...X./",".XX./..X./..X./XXX./..XX/",".X.../.X..X/XXXXX/X..../","XX../.XXX/.X../.X../.XX./"};
		shape[390] = new String[]{"X..../XXXXX/.X.../XX.../","X.XX/XXX./..X./..X./..X./","...XX/...X./XXXXX/....X/",".X../.X../.X../.XXX/XX.X/","....X/XXXXX/...X./...XX/","..X./..X./..X./XXX./X.XX/","XX.../.X.../XXXXX/X..../","XX.X/.XXX/.X../.X../.X../"};
		shape[391] = new String[]{"X..../XXXXX/.X.../.XX../","..XX/XXX./X.X./..X./..X./","..XX./...X./XXXXX/....X/",".X../.X../.X.X/.XXX/XX../","....X/XXXXX/...X./..XX./","..X./..X./X.X./XXX./..XX/",".XX../.X.../XXXXX/X..../","XX../.XXX/.X.X/.X../.X../"};
		shape[392] = new String[]{"X..../XXXXX/.X.../.X.../.X.../","...XX/XXXX./...X./...X./...X./","...X./...X./...X./XXXXX/....X/",".X.../.X.../.X.../.XXXX/XX.../","....X/XXXXX/...X./...X./...X./","...X./...X./...X./XXXX./...XX/",".X.../.X.../.X.../XXXXX/X..../","XX.../.XXXX/.X.../.X.../.X.../"};
		shape[393] = new String[]{"..X../X.XX./XXXXX/","XX./X../XXX/XX./X../","XXXXX/.XX.X/..X../","..X/.XX/XXX/..X/.XX/","..X../.XX.X/XXXXX/","X../XX./XXX/X../XX./","XXXXX/X.XX./..X../",".XX/..X/XXX/.XX/..X/"};
		shape[394] = new String[]{"...X./X.XX./XXXXX/","XX./X../XX./XXX/X../","XXXXX/.XX.X/.X.../","..X/XXX/.XX/..X/.XX/",".X.../.XX.X/XXXXX/","X../XXX/XX./X../XX./","XXXXX/X.XX./...X./",".XX/..X/.XX/XXX/..X/"};
		shape[395] = new String[]{"X.XX./XXXXX/..X../",".XX/.X./XXX/.XX/.X./","..X../XXXXX/.XX.X/",".X./XX./XXX/.X./XX./",".XX.X/XXXXX/..X../",".X./.XX/XXX/.X./.XX/","..X../XXXXX/X.XX./","XX./.X./XXX/XX./.X./"};
		shape[396] = new String[]{"X.XX./XXXXX/...X./",".XX/.X./.XX/XXX/.X./",".X.../XXXXX/.XX.X/",".X./XXX/XX./.X./XX./",".XX.X/XXXXX/.X.../",".X./XXX/.XX/.X./.XX/","...X./XXXXX/X.XX./","XX./.X./XX./XXX/.X./"};
		shape[397] = new String[]{".XX../X.X../XXXXX/","XX./X.X/XXX/X../X../","XXXXX/..X.X/..XX./","..X/..X/XXX/X.X/.XX/","..XX./..X.X/XXXXX/","X../X../XXX/X.X/XX./","XXXXX/X.X../.XX../",".XX/X.X/XXX/..X/..X/"};
		shape[398] = new String[]{"..XX./X.X../XXXXX/","XX./X../XXX/X.X/X../","XXXXX/..X.X/.XX../","..X/X.X/XXX/..X/.XX/",".XX../..X.X/XXXXX/","X../X.X/XXX/X../XX./","XXXXX/X.X../..XX./",".XX/..X/XXX/X.X/..X/"};
		shape[399] = new String[]{"..X../..X../X.X../XXXXX/","XX../X.../XXXX/X.../X.../","XXXXX/..X.X/..X../..X../","...X/...X/XXXX/...X/..XX/","..X../..X../..X.X/XXXXX/","X.../X.../XXXX/X.../XX../","XXXXX/X.X../..X../..X../","..XX/...X/XXXX/...X/...X/"};
		shape[400] = new String[]{"..X../X.X../XXXXX/..X../",".XX./.X../XXXX/.X../.X../","..X../XXXXX/..X.X/..X../","..X./..X./XXXX/..X./.XX./","..X../..X.X/XXXXX/..X../",".X../.X../XXXX/.X../.XX./","..X../XXXXX/X.X../..X../",".XX./..X./XXXX/..X./..X./"};
		shape[401] = new String[]{"..X../X.X../XXXXX/...X./",".XX./.X../.XXX/XX../.X../",".X.../XXXXX/..X.X/..X../","..X./..XX/XXX./..X./.XX./","..X../..X.X/XXXXX/.X.../",".X../XX../.XXX/.X../.XX./","...X./XXXXX/X.X../..X../",".XX./..X./XXX./..XX/..X./"};
		shape[402] = new String[]{"..X../X.X.X/XXXXX/","XX./X../XXX/X../XX./","XXXXX/X.X.X/..X../",".XX/..X/XXX/..X/.XX/"};
		shape[403] = new String[]{"..X../X.X../XXXXX/....X/",".XX./.X../.XXX/.X../XX../","X..../XXXXX/..X.X/..X../","..XX/..X./XXX./..X./.XX./","..X../..X.X/XXXXX/X..../","XX../.X../.XXX/.X../.XX./","....X/XXXXX/X.X../..X../",".XX./..X./XXX./..X./..XX/"};
		shape[404] = new String[]{"X.X../XXXXX/..XX./",".XX/.X./XXX/XX./.X./",".XX../XXXXX/..X.X/",".X./.XX/XXX/.X./XX./","..X.X/XXXXX/.XX../",".X./XX./XXX/.X./.XX/","..XX./XXXXX/X.X../","XX./.X./XXX/.XX/.X./"};
		shape[405] = new String[]{"X.X.X/XXXXX/..X../",".XX/.X./XXX/.X./.XX/","..X../XXXXX/X.X.X/","XX./.X./XXX/.X./XX./"};
		shape[406] = new String[]{"X.X../XXXXX/..X.X/",".XX/.X./XXX/.X./XX./","..X.X/XXXXX/X.X../","XX./.X./XXX/.X./.XX/"};
		shape[407] = new String[]{"X.X../XXXXX/..X../..X../","..XX/..X./XXXX/..X./..X./","..X../..X../XXXXX/..X.X/",".X../.X../XXXX/.X../XX../","..X.X/XXXXX/..X../..X../","..X./..X./XXXX/..X./..XX/","..X../..X../XXXXX/X.X../","XX../.X../XXXX/.X../.X../"};
		shape[408] = new String[]{"X.X../XXXXX/...X./...X./","..XX/..X./..XX/XXX./..X./",".X.../.X.../XXXXX/..X.X/",".X../.XXX/XX../.X../XX../","..X.X/XXXXX/.X.../.X.../","..X./XXX./..XX/..X./..XX/","...X./...X./XXXXX/X.X../","XX../.X../XX../.XXX/.X../"};
		shape[409] = new String[]{"...X./X..X./XXXXX/..X../",".XX./.X../XX../.XXX/.X../","..X../XXXXX/.X..X/.X.../","..X./XXX./..XX/..X./.XX./",".X.../.X..X/XXXXX/..X../",".X../.XXX/XX../.X../.XX./","..X../XXXXX/X..X./...X./",".XX./..X./..XX/XXX./..X./"};
		shape[410] = new String[]{"X..X./XXXXX/..XX./",".XX/.X./XX./XXX/.X./",".XX../XXXXX/.X..X/",".X./XXX/.XX/.X./XX./",".X..X/XXXXX/.XX../",".X./XXX/XX./.X./.XX/","..XX./XXXXX/X..X./","XX./.X./.XX/XXX/.X./"};
		shape[411] = new String[]{"X..X./XXXXX/..X../..X../","..XX/..X./XXX./..XX/..X./","..X../..X../XXXXX/.X..X/",".X../XX../.XXX/.X../XX../",".X..X/XXXXX/..X../..X../","..X./..XX/XXX./..X./..XX/","..X../..X../XXXXX/X..X./","XX../.X../.XXX/XX../.X../"};
		shape[412] = new String[]{"X..../XXXXX/..XX./..X../","..XX/..X./XXX./.XX./..X./","..X../.XX../XXXXX/....X/",".X../.XX./.XXX/.X../XX../","....X/XXXXX/.XX../..X../","..X./.XX./XXX./..X./..XX/","..X../..XX./XXXXX/X..../","XX../.X../.XXX/.XX./.X../"};
		shape[413] = new String[]{"X..../XXXXX/..XX./...X./","..XX/..X./.XX./XXX./..X./",".X.../.XX../XXXXX/....X/",".X../.XXX/.XX./.X../XX../","....X/XXXXX/.XX../.X.../","..X./XXX./.XX./..X./..XX/","...X./..XX./XXXXX/X..../","XX../.X../.XX./.XXX/.X../"};
		shape[414] = new String[]{"X...X/XXXXX/..X../..X../","..XX/..X./XXX./..X./..XX/","..X../..X../XXXXX/X...X/","XX../.X../.XXX/.X../XX../"};
		shape[415] = new String[]{"X..../XXXXX/..X../.XX../","..XX/X.X./XXX./..X./..X./","..XX./..X../XXXXX/....X/",".X../.X../.XXX/.X.X/XX../","....X/XXXXX/..X../..XX./","..X./..X./XXX./X.X./..XX/",".XX../..X../XXXXX/X..../","XX../.X.X/.XXX/.X../.X../"};
		shape[416] = new String[]{"X..../XXXXX/..X../..XX./","..XX/..X./XXX./X.X./..X./",".XX../..X../XXXXX/....X/",".X../.X.X/.XXX/.X../XX../","....X/XXXXX/..X../.XX../","..X./X.X./XXX./..X./..XX/","..XX./..X../XXXXX/X..../","XX../.X../.XXX/.X.X/.X../"};
		shape[417] = new String[]{"X..../XXXXX/..X../..X../..X../","...XX/...X./XXXX./...X./...X./","..X../..X../..X../XXXXX/....X/",".X.../.X.../.XXXX/.X.../XX.../","....X/XXXXX/..X../..X../..X../","...X./...X./XXXX./...X./...XX/","..X../..X../..X../XXXXX/X..../","XX.../.X.../.XXXX/.X.../.X.../"};
		shape[418] = new String[]{"..XX./X..X./XXXXX/","XX./X../X.X/XXX/X../","XXXXX/.X..X/.XX../","..X/XXX/X.X/..X/.XX/",".XX../.X..X/XXXXX/","X../XXX/X.X/X../XX./","XXXXX/X..X./..XX./",".XX/..X/X.X/XXX/..X/"};
		shape[419] = new String[]{"...XX/X..X./XXXXX/","XX./X../X../XXX/X.X/","XXXXX/.X..X/XX.../","X.X/XXX/..X/..X/.XX/","XX.../.X..X/XXXXX/","X.X/XXX/X../X../XX./","XXXXX/X..X./...XX/",".XX/..X/..X/XXX/X.X/"};
		shape[420] = new String[]{"...X./...X./X..X./XXXXX/","XX../X.../X.../XXXX/X.../","XXXXX/.X..X/.X.../.X.../","...X/XXXX/...X/...X/..XX/",".X.../.X.../.X..X/XXXXX/","X.../XXXX/X.../X.../XX../","XXXXX/X..X./...X./...X./","..XX/...X/...X/XXXX/...X/"};
		shape[421] = new String[]{"...X./X..X./XXXXX/...X./",".XX./.X../.X../XXXX/.X../",".X.../XXXXX/.X..X/.X.../","..X./XXXX/..X./..X./.XX./",".X.../.X..X/XXXXX/.X.../",".X../XXXX/.X../.X../.XX./","...X./XXXXX/X..X./...X./",".XX./..X./..X./XXXX/..X./"};
		shape[422] = new String[]{"X..X./XXXXX/...X./...X./","..XX/..X./..X./XXXX/..X./",".X.../.X.../XXXXX/.X..X/",".X../XXXX/.X../.X../XX../",".X..X/XXXXX/.X.../.X.../","..X./XXXX/..X./..X./..XX/","...X./...X./XXXXX/X..X./","XX../.X../.X../XXXX/.X../"};
		shape[423] = new String[]{"X..../XXXXX/...X./..XX./","..XX/..X./X.X./XXX./..X./",".XX../.X.../XXXXX/....X/",".X../.XXX/.X.X/.X../XX../","....X/XXXXX/.X.../.XX../","..X./XXX./X.X./..X./..XX/","..XX./...X./XXXXX/X..../","XX../.X../.X.X/.XXX/.X../"};
		shape[424] = new String[]{"X..../XXXXX/...X./...XX/","..XX/..X./..X./XXX./X.X./","XX.../.X.../XXXXX/....X/",".X.X/.XXX/.X../.X../XX../","....X/XXXXX/.X.../XX.../","X.X./XXX./..X./..X./..XX/","...XX/...X./XXXXX/X..../","XX../.X../.X../.XXX/.X.X/"};
		shape[425] = new String[]{"X..../XXXXX/...X./...X./...X./","...XX/...X./...X./XXXX./...X./",".X.../.X.../.X.../XXXXX/....X/",".X.../.XXXX/.X.../.X.../XX.../","....X/XXXXX/.X.../.X.../.X.../","...X./XXXX./...X./...X./...XX/","...X./...X./...X./XXXXX/X..../","XX.../.X.../.X.../.XXXX/.X.../"};
		shape[426] = new String[]{"XX.../.XX../XXXXX/","X.X/XXX/XX./X../X../","XXXXX/..XX./...XX/","..X/..X/.XX/XXX/X.X/","...XX/..XX./XXXXX/","X../X../XX./XXX/X.X/","XXXXX/.XX../XX.../","X.X/XXX/.XX/..X/..X/"};
		shape[427] = new String[]{".XX../.XX../XXXXX/","X../XXX/XXX/X../X../","XXXXX/..XX./..XX./","..X/..X/XXX/XXX/..X/","..XX./..XX./XXXXX/","X../X../XXX/XXX/X../","XXXXX/.XX../.XX../","..X/XXX/XXX/..X/..X/"};
		shape[428] = new String[]{".X.../.X.../.XX../XXXXX/","X.../XXXX/XX../X.../X.../","XXXXX/..XX./...X./...X./","...X/...X/..XX/XXXX/...X/","...X./...X./..XX./XXXXX/","X.../X.../XX../XXXX/X.../","XXXXX/.XX../.X.../.X.../","...X/XXXX/..XX/...X/...X/"};
		shape[429] = new String[]{".X.../.XXX./XXXXX/","X../XXX/XX./XX./X../","XXXXX/.XXX./...X./","..X/.XX/.XX/XXX/..X/","...X./.XXX./XXXXX/","X../XX./XX./XXX/X../","XXXXX/.XXX./.X.../","..X/XXX/.XX/.XX/..X/"};
		shape[430] = new String[]{".X.../.XX../XXXXX/.X.../",".X../XXXX/.XX./.X../.X../","...X./XXXXX/..XX./...X./","..X./..X./.XX./XXXX/..X./","...X./..XX./XXXXX/...X./",".X../.X../.XX./XXXX/.X../",".X.../XXXXX/.XX../.X.../","..X./XXXX/.XX./..X./..X./"};
		shape[431] = new String[]{".X.../.XX../XXXXX/..X../",".X../.XXX/XXX./.X../.X../","..X../XXXXX/..XX./...X./","..X./..X./.XXX/XXX./..X./","...X./..XX./XXXXX/..X../",".X../.X../XXX./.XXX/.X../","..X../XXXXX/.XX../.X.../","..X./XXX./.XXX/..X./..X./"};
		shape[432] = new String[]{".X.../.XX../XXXXX/...X./",".X../.XXX/.XX./XX../.X../",".X.../XXXXX/..XX./...X./","..X./..XX/.XX./XXX./..X./","...X./..XX./XXXXX/.X.../",".X../XX../.XX./.XXX/.X../","...X./XXXXX/.XX../.X.../","..X./XXX./.XX./..XX/..X./"};
		shape[433] = new String[]{"..X../.XXX./XXXXX/","X../XX./XXX/XX./X../","XXXXX/.XXX./..X../","..X/.XX/XXX/.XX/..X/"};
		shape[434] = new String[]{".XXX./XXXXX/.X.../",".X./XXX/.XX/.XX/.X./","...X./XXXXX/.XXX./",".X./XX./XX./XXX/.X./",".XXX./XXXXX/...X./",".X./.XX/.XX/XXX/.X./",".X.../XXXXX/.XXX./",".X./XXX/XX./XX./.X./"};
		shape[435] = new String[]{".XXX./XXXXX/..X../",".X./.XX/XXX/.XX/.X./","..X../XXXXX/.XXX./",".X./XX./XXX/XX./.X./"};
		shape[436] = new String[]{"..XX./.XX../XXXXX/","X../XX./XXX/X.X/X../","XXXXX/..XX./.XX../","..X/X.X/XXX/.XX/..X/",".XX../..XX./XXXXX/","X../X.X/XXX/XX./X../","XXXXX/.XX../..XX./","..X/.XX/XXX/X.X/..X/"};
		shape[437] = new String[]{"..X../..X../.XX../XXXXX/","X.../XX../XXXX/X.../X.../","XXXXX/..XX./..X../..X../","...X/...X/XXXX/..XX/...X/","..X../..X../..XX./XXXXX/","X.../X.../XXXX/XX../X.../","XXXXX/.XX../..X../..X../","...X/..XX/XXXX/...X/...X/"};
		shape[438] = new String[]{"..X../.XX../XXXXX/.X.../",".X../XXX./.XXX/.X../.X../","...X./XXXXX/..XX./..X../","..X./..X./XXX./.XXX/..X./","..X../..XX./XXXXX/...X./",".X../.X../.XXX/XXX./.X../",".X.../XXXXX/.XX../..X../","..X./.XXX/XXX./..X./..X./"};
		shape[439] = new String[]{"..X../.XX../XXXXX/..X../",".X../.XX./XXXX/.X../.X../","..X../XXXXX/..XX./..X../","..X./..X./XXXX/.XX./..X./","..X../..XX./XXXXX/..X../",".X../.X../XXXX/.XX./.X../","..X../XXXXX/.XX../..X../","..X./.XX./XXXX/..X./..X./"};
		shape[440] = new String[]{"..X../.XX../XXXXX/...X./",".X../.XX./.XXX/XX../.X../",".X.../XXXXX/..XX./..X../","..X./..XX/XXX./.XX./..X./","..X../..XX./XXXXX/.X.../",".X../XX../.XXX/.XX./.X../","...X./XXXXX/.XX../..X../","..X./.XX./XXX./..XX/..X./"};
		shape[441] = new String[]{".XX../XXXXX/.XX../",".X./XXX/XXX/.X./.X./","..XX./XXXXX/..XX./",".X./.X./XXX/XXX/.X./"};
		shape[442] = new String[]{".XX../XXXXX/.X.X./",".X./XXX/.XX/XX./.X./",".X.X./XXXXX/..XX./",".X./.XX/XX./XXX/.X./","..XX./XXXXX/.X.X./",".X./XX./.XX/XXX/.X./",".X.X./XXXXX/.XX../",".X./XXX/XX./.XX/.X./"};
		shape[443] = new String[]{".XX../XXXXX/.X.../.X.../","..X./XXXX/..XX/..X./..X./","...X./...X./XXXXX/..XX./",".X../.X../XX../XXXX/.X../","..XX./XXXXX/...X./...X./","..X./..X./..XX/XXXX/..X./",".X.../.X.../XXXXX/.XX../",".X../XXXX/XX../.X../.X../"};
		shape[444] = new String[]{".XX../XXXXX/..XX./",".X./.XX/XXX/XX./.X./","..XX./XXXXX/.XX../",".X./XX./XXX/.XX/.X./"};
		shape[445] = new String[]{".XX../XXXXX/..X../..X../","..X./..XX/XXXX/..X./..X./","..X../..X../XXXXX/..XX./",".X../.X../XXXX/XX../.X../","..XX./XXXXX/..X../..X../","..X./..X./XXXX/..XX/..X./","..X../..X../XXXXX/.XX../",".X../XX../XXXX/.X../.X../"};
		shape[446] = new String[]{".XX../XXXXX/...X./...X./","..X./..XX/..XX/XXX./..X./",".X.../.X.../XXXXX/..XX./",".X../.XXX/XX../XX../.X../","..XX./XXXXX/.X.../.X.../","..X./XXX./..XX/..XX/..X./","...X./...X./XXXXX/.XX../",".X../XX../XX../.XXX/.X../"};
		shape[447] = new String[]{"XXX.../..X.../.XXXXX/","..X/X.X/XXX/X../X../X../","XXXXX./...X../...XXX/","..X/..X/..X/XXX/X.X/X../","...XXX/...X../XXXXX./","X../X../X../XXX/X.X/..X/",".XXXXX/..X.../XXX.../","X../X.X/XXX/..X/..X/..X/"};
		shape[448] = new String[]{"X..../XX.../.X.../XXXXX/","X.XX/XXX./X.../X.../X.../","XXXXX/...X./...XX/....X/","...X/...X/...X/.XXX/XX.X/","....X/...XX/...X./XXXXX/","X.../X.../X.../XXX./X.XX/","XXXXX/.X.../XX.../X..../","XX.X/.XXX/...X/...X/...X/"};
		shape[449] = new String[]{"XXX../.X.../XXXXX/","X.X/XXX/X.X/X../X../","XXXXX/...X./..XXX/","..X/..X/X.X/XXX/X.X/","..XXX/...X./XXXXX/","X../X../X.X/XXX/X.X/","XXXXX/.X.../XXX../","X.X/XXX/X.X/..X/..X/"};
		shape[450] = new String[]{".X.../XX.../.X.../XXXXX/","X.X./XXXX/X.../X.../X.../","XXXXX/...X./...XX/...X./","...X/...X/...X/XXXX/.X.X/","...X./...XX/...X./XXXXX/","X.../X.../X.../XXXX/X.X./","XXXXX/.X.../XX.../.X.../",".X.X/XXXX/...X/...X/...X/"};
		shape[451] = new String[]{"XX.../.X.../XXXXX/.X.../",".X.X/XXXX/.X../.X../.X../","...X./XXXXX/...X./...XX/","..X./..X./..X./XXXX/X.X./","...XX/...X./XXXXX/...X./",".X../.X../.X../XXXX/.X.X/",".X.../XXXXX/.X.../XX.../","X.X./XXXX/..X./..X./..X./"};
		shape[452] = new String[]{"XX.../.X.../XXXXX/..X../",".X.X/.XXX/XX../.X../.X../","..X../XXXXX/...X./...XX/","..X./..X./..XX/XXX./X.X./","...XX/...X./XXXXX/..X../",".X../.X../XX../.XXX/.X.X/","..X../XXXXX/.X.../XX.../","X.X./XXX./..XX/..X./..X./"};
		shape[453] = new String[]{"XX.../.X.X./XXXXX/","X.X/XXX/X../XX./X../","XXXXX/.X.X./...XX/","..X/.XX/..X/XXX/X.X/","...XX/.X.X./XXXXX/","X../XX./X../XXX/X.X/","XXXXX/.X.X./XX.../","X.X/XXX/..X/.XX/..X/"};
		shape[454] = new String[]{"XX.../.X.../XXXXX/...X./",".X.X/.XXX/.X../XX../.X../",".X.../XXXXX/...X./...XX/","..X./..XX/..X./XXX./X.X./","...XX/...X./XXXXX/.X.../",".X../XX../.X../.XXX/.X.X/","...X./XXXXX/.X.../XX.../","X.X./XXX./..X./..XX/..X./"};
		shape[455] = new String[]{".X.../.XX../.X.../XXXXX/","X.../XXXX/X.X./X.../X.../","XXXXX/...X./..XX./...X./","...X/...X/.X.X/XXXX/...X/","...X./..XX./...X./XXXXX/","X.../X.../X.X./XXXX/X.../","XXXXX/.X.../.XX../.X.../","...X/XXXX/.X.X/...X/...X/"};
		shape[456] = new String[]{".XXX./.X.../XXXXX/","X../XXX/X.X/X.X/X../","XXXXX/...X./.XXX./","..X/X.X/X.X/XXX/..X/",".XXX./...X./XXXXX/","X../X.X/X.X/XXX/X../","XXXXX/.X.../.XXX./","..X/XXX/X.X/X.X/..X/"};
		shape[457] = new String[]{"..X../.XX../.X.../XXXXX/","X.../XXX./X.XX/X.../X.../","XXXXX/...X./..XX./..X../","...X/...X/XX.X/.XXX/...X/","..X../..XX./...X./XXXXX/","X.../X.../X.XX/XXX./X.../","XXXXX/.X.../.XX../..X../","...X/.XXX/XX.X/...X/...X/"};
		shape[458] = new String[]{".XX../.X.../XXXXX/.X.../",".X../XXXX/.X.X/.X../.X../","...X./XXXXX/...X./..XX./","..X./..X./X.X./XXXX/..X./","..XX./...X./XXXXX/...X./",".X../.X../.X.X/XXXX/.X../",".X.../XXXXX/.X.../.XX../","..X./XXXX/X.X./..X./..X./"};
		shape[459] = new String[]{".XX../.X.../XXXXX/..X../",".X../.XXX/XX.X/.X../.X../","..X../XXXXX/...X./..XX./","..X./..X./X.XX/XXX./..X./","..XX./...X./XXXXX/..X../",".X../.X../XX.X/.XXX/.X../","..X../XXXXX/.X.../.XX../","..X./XXX./X.XX/..X./..X./"};
		shape[460] = new String[]{".XX../.X.X./XXXXX/","X../XXX/X.X/XX./X../","XXXXX/.X.X./..XX./","..X/.XX/X.X/XXX/..X/","..XX./.X.X./XXXXX/","X../XX./X.X/XXX/X../","XXXXX/.X.X./.XX../","..X/XXX/X.X/.XX/..X/"};
		shape[461] = new String[]{".XX../.X.../XXXXX/...X./",".X../.XXX/.X.X/XX../.X../",".X.../XXXXX/...X./..XX./","..X./..XX/X.X./XXX./..X./","..XX./...X./XXXXX/.X.../",".X../XX../.X.X/.XXX/.X../","...X./XXXXX/.X.../.XX../","..X./XXX./X.X./..XX/..X./"};
		shape[462] = new String[]{"XX.../.X.../.X.../XXXXX/","X..X/XXXX/X.../X.../X.../","XXXXX/...X./...X./...XX/","...X/...X/...X/XXXX/X..X/","...XX/...X./...X./XXXXX/","X.../X.../X.../XXXX/X..X/","XXXXX/.X.../.X.../XX.../","X..X/XXXX/...X/...X/...X/"};
		shape[463] = new String[]{".XX../.X.../.X.../XXXXX/","X.../XXXX/X..X/X.../X.../","XXXXX/...X./...X./..XX./","...X/...X/X..X/XXXX/...X/","..XX./...X./...X./XXXXX/","X.../X.../X..X/XXXX/X.../","XXXXX/.X.../.X.../.XX../","...X/XXXX/X..X/...X/...X/"};
		shape[464] = new String[]{".X.../.X.../.X.../XXXXX/.X.../",".X.../XXXXX/.X.../.X.../.X.../","...X./XXXXX/...X./...X./...X./","...X./...X./...X./XXXXX/...X./"};
		shape[465] = new String[]{".X.../.X.../.X.../XXXXX/..X../",".X.../.XXXX/XX.../.X.../.X.../","..X../XXXXX/...X./...X./...X./","...X./...X./...XX/XXXX./...X./","...X./...X./...X./XXXXX/..X../",".X.../.X.../XX.../.XXXX/.X.../","..X../XXXXX/.X.../.X.../.X.../","...X./XXXX./...XX/...X./...X./"};
		shape[466] = new String[]{".X.../.X.../.X.X./XXXXX/","X.../XXXX/X.../XX../X.../","XXXXX/.X.X./...X./...X./","...X/..XX/...X/XXXX/...X/","...X./...X./.X.X./XXXXX/","X.../XX../X.../XXXX/X.../","XXXXX/.X.X./.X.../.X.../","...X/XXXX/...X/..XX/...X/"};
		shape[467] = new String[]{".X.../.X.../.X.../XXXXX/...X./",".X.../.XXXX/.X.../XX.../.X.../",".X.../XXXXX/...X./...X./...X./","...X./...XX/...X./XXXX./...X./","...X./...X./...X./XXXXX/.X.../",".X.../XX.../.X.../.XXXX/.X.../","...X./XXXXX/.X.../.X.../.X.../","...X./XXXX./...X./...XX/...X./"};
		shape[468] = new String[]{".X.../.X.X./XXXXX/.X.../",".X../XXXX/.X../.XX./.X../","...X./XXXXX/.X.X./...X./","..X./.XX./..X./XXXX/..X./","...X./.X.X./XXXXX/...X./",".X../.XX./.X../XXXX/.X../",".X.../XXXXX/.X.X./.X.../","..X./XXXX/..X./.XX./..X./"};
		shape[469] = new String[]{".X.../.X.../XXXXX/.X.X./",".X../XXXX/.X../XX../.X../",".X.X./XXXXX/...X./...X./","..X./..XX/..X./XXXX/..X./","...X./...X./XXXXX/.X.X./",".X../XX../.X../XXXX/.X../",".X.X./XXXXX/.X.../.X.../","..X./XXXX/..X./..XX/..X./"};
		shape[470] = new String[]{".X.../.X.../XXXXX/.X.../.X.../","..X../XXXXX/..X../..X../..X../","...X./...X./XXXXX/...X./...X./","..X../..X../..X../XXXXX/..X../"};
		shape[471] = new String[]{".X.../.X.X./XXXXX/..X../",".X../.XXX/XX../.XX./.X../","..X../XXXXX/.X.X./...X./","..X./.XX./..XX/XXX./..X./","...X./.X.X./XXXXX/..X../",".X../.XX./XX../.XXX/.X../","..X../XXXXX/.X.X./.X.../","..X./XXX./..XX/.XX./..X./"};
		shape[472] = new String[]{".X.../.X.../XXXXX/..X../..X../","..X../..XXX/XXX../..X../..X../","..X../..X../XXXXX/...X./...X./","..X../..X../..XXX/XXX../..X../","...X./...X./XXXXX/..X../..X../","..X../..X../XXX../..XXX/..X../","..X../..X../XXXXX/.X.../.X.../","..X../XXX../..XXX/..X../..X../"};
		shape[473] = new String[]{".X.X./.X.X./XXXXX/","X../XXX/X../XXX/X../","XXXXX/.X.X./.X.X./","..X/XXX/..X/XXX/..X/"};
		shape[474] = new String[]{".X.../.X.X./XXXXX/...X./",".X../.XXX/.X../XXX./.X../",".X.../XXXXX/.X.X./...X./","..X./.XXX/..X./XXX./..X./","...X./.X.X./XXXXX/.X.../",".X../XXX./.X../.XXX/.X../","...X./XXXXX/.X.X./.X.../","..X./XXX./..X./.XXX/..X./"};
		shape[475] = new String[]{".X.../.X.../XXXXX/...X./...X./","..X../..XXX/..X../XXX../..X../","...X./...X./XXXXX/.X.../.X.../","..X../XXX../..X../..XXX/..X../"};
		shape[476] = new String[]{".X.X./XXXXX/.X.X./",".X./XXX/.X./XXX/.X./"};
		shape[477] = new String[]{".X.X./XXXXX/..X../..X../","..X./..XX/XXX./..XX/..X./","..X../..X../XXXXX/.X.X./",".X../XX../.XXX/XX../.X../"};
		shape[478] = new String[]{".X.../XXXXX/..X../.XX../","..X./X.XX/XXX./..X./..X./","..XX./..X../XXXXX/...X./",".X../.X../.XXX/XX.X/.X../","...X./XXXXX/..X../..XX./","..X./..X./XXX./X.XX/..X./",".XX../..X../XXXXX/.X.../",".X../XX.X/.XXX/.X../.X../"};
		shape[479] = new String[]{".X.../XXXXX/..X../..XX./","..X./..XX/XXX./X.X./..X./",".XX../..X../XXXXX/...X./",".X../.X.X/.XXX/XX../.X../","...X./XXXXX/..X../.XX../","..X./X.X./XXX./..XX/..X./","..XX./..X../XXXXX/.X.../",".X../XX../.XXX/.X.X/.X../"};
		shape[480] = new String[]{".X.../XXXXX/..X../..X../..X../","...X./...XX/XXXX./...X./...X./","..X../..X../..X../XXXXX/...X./",".X.../.X.../.XXXX/XX.../.X.../","...X./XXXXX/..X../..X../..X../","...X./...X./XXXX./...XX/...X./","..X../..X../..X../XXXXX/.X.../",".X.../XX.../.XXXX/.X.../.X.../"};
		shape[481] = new String[]{"XXX../..X../XXXXX/","X.X/X.X/XXX/X../X../","XXXXX/..X../..XXX/","..X/..X/XXX/X.X/X.X/","..XXX/..X../XXXXX/","X../X../XXX/X.X/X.X/","XXXXX/..X../XXX../","X.X/X.X/XXX/..X/..X/"};
		shape[482] = new String[]{".X.../.XX../..X../XXXXX/","X.../X.XX/XXX./X.../X.../","XXXXX/..X../..XX./...X./","...X/...X/.XXX/XX.X/...X/","...X./..XX./..X../XXXXX/","X.../X.../XXX./X.XX/X.../","XXXXX/..X../.XX../.X.../","...X/XX.X/.XXX/...X/...X/"};
		shape[483] = new String[]{".XXX./..X../XXXXX/","X../X.X/XXX/X.X/X../","XXXXX/..X../.XXX./","..X/X.X/XXX/X.X/..X/"};
		shape[484] = new String[]{"..X../.XX../..X../XXXXX/","X.../X.X./XXXX/X.../X.../","XXXXX/..X../..XX./..X../","...X/...X/XXXX/.X.X/...X/","..X../..XX./..X../XXXXX/","X.../X.../XXXX/X.X./X.../","XXXXX/..X../.XX../..X../","...X/.X.X/XXXX/...X/...X/"};
		shape[485] = new String[]{".XX../..X../XXXXX/..X../",".X../.X.X/XXXX/.X../.X../","..X../XXXXX/..X../..XX./","..X./..X./XXXX/X.X./..X./","..XX./..X../XXXXX/..X../",".X../.X../XXXX/.X.X/.X../","..X../XXXXX/..X../.XX../","..X./X.X./XXXX/..X./..X./"};
		shape[486] = new String[]{".XX../..X../..X../XXXXX/","X.../X..X/XXXX/X.../X.../","XXXXX/..X../..X../..XX./","...X/...X/XXXX/X..X/...X/","..XX./..X../..X../XXXXX/","X.../X.../XXXX/X..X/X.../","XXXXX/..X../..X../.XX../","...X/X..X/XXXX/...X/...X/"};
		shape[487] = new String[]{"..X../..X../XXXXX/..X../..X../"};
		shape[488] = new String[]{"X....../XXXX.../...XXXX/",".XX/.X./.X./XX./X../X../X../","XXXX.../...XXXX/......X/","..X/..X/..X/.XX/.X./.X./XX./","......X/...XXXX/XXXX.../","X../X../X../XX./.X./.X./.XX/","...XXXX/XXXX.../X....../","XX./.X./.X./.XX/..X/..X/..X/"};
		shape[489] = new String[]{"XXXX.../X..XXXX/","XX/.X/.X/XX/X./X./X./","XXXX..X/...XXXX/",".X/.X/.X/XX/X./X./XX/","...XXXX/XXXX..X/","X./X./X./XX/.X/.X/XX/","X..XXXX/XXXX.../","XX/X./X./XX/.X/.X/.X/"};
		shape[490] = new String[]{".X...../XXXX.../...XXXX/",".X./.XX/.X./XX./X../X../X../","XXXX.../...XXXX/.....X./","..X/..X/..X/.XX/.X./XX./.X./",".....X./...XXXX/XXXX.../","X../X../X../XX./.X./.XX/.X./","...XXXX/XXXX.../.X...../",".X./XX./.X./.XX/..X/..X/..X/"};
		shape[491] = new String[]{"XXXX.../.X.XXXX/",".X/XX/.X/XX/X./X./X./","XXXX.X./...XXXX/",".X/.X/.X/XX/X./XX/X./","...XXXX/XXXX.X./","X./X./X./XX/.X/XX/.X/",".X.XXXX/XXXX.../","X./XX/X./XX/.X/.X/.X/"};
		shape[492] = new String[]{"..X..../XXXX.../...XXXX/",".X./.X./.XX/XX./X../X../X../","XXXX.../...XXXX/....X../","..X/..X/..X/.XX/XX./.X./.X./","....X../...XXXX/XXXX.../","X../X../X../XX./.XX/.X./.X./","...XXXX/XXXX.../..X..../",".X./.X./XX./.XX/..X/..X/..X/"};
		shape[493] = new String[]{"...X.../XXXX.../...XXXX/",".X./.X./.X./XXX/X../X../X../","XXXX.../...XXXX/...X.../","..X/..X/..X/XXX/.X./.X./.X./","...X.../...XXXX/XXXX.../","X../X../X../XXX/.X./.X./.X./","...XXXX/XXXX.../...X.../",".X./.X./.X./XXX/..X/..X/..X/"};
		shape[494] = new String[]{"XX...../.XXX.../...XXXX/","..X/.XX/.X./XX./X../X../X../","XXXX.../...XXX./.....XX/","..X/..X/..X/.XX/.X./XX./X../",".....XX/...XXX./XXXX.../","X../X../X../XX./.X./.XX/..X/","...XXXX/.XXX.../XX...../","X../XX./.X./.XX/..X/..X/..X/"};
		shape[495] = new String[]{"XX..../XXX.../..XXXX/",".XX/.XX/XX./X../X../X../","XXXX../...XXX/....XX/","..X/..X/..X/.XX/XX./XX./","....XX/...XXX/XXXX../","X../X../X../XX./.XX/.XX/","..XXXX/XXX.../XX..../","XX./XX./.XX/..X/..X/..X/"};
		shape[496] = new String[]{"X...../X...../XXX.../..XXXX/",".XXX/.X../XX../X.../X.../X.../","XXXX../...XXX/.....X/.....X/","...X/...X/...X/..XX/..X./XXX./",".....X/.....X/...XXX/XXXX../","X.../X.../X.../XX../.X../.XXX/","..XXXX/XXX.../X...../X...../","XXX./..X./..XX/...X/...X/...X/"};
		shape[497] = new String[]{"X...../XXX.../X.XXXX/","XXX/.X./XX./X../X../X../","XXXX.X/...XXX/.....X/","..X/..X/..X/.XX/.X./XXX/",".....X/...XXX/XXXX.X/","X../X../X../XX./.X./XXX/","X.XXXX/XXX.../X...../","XXX/.X./.XX/..X/..X/..X/"};
		shape[498] = new String[]{"X...../XXXX../..XXXX/",".XX/.X./XX./XX./X../X../","XXXX../..XXXX/.....X/","..X/..X/.XX/.XX/.X./XX./",".....X/..XXXX/XXXX../","X../X../XX./XX./.X./.XX/","..XXXX/XXXX../X...../","XX./.X./.XX/.XX/..X/..X/"};
		shape[499] = new String[]{"X.X.../XXX.../..XXXX/",".XX/.X./XXX/X../X../X../","XXXX../...XXX/...X.X/","..X/..X/..X/XXX/.X./XX./","...X.X/...XXX/XXXX../","X../X../X../XXX/.X./.XX/","..XXXX/XXX.../X.X.../","XX./.X./XXX/..X/..X/..X/"};
		shape[500] = new String[]{"X...../XXX.../..XXXX/..X.../","..XX/..X./XXX./.X../.X../.X../","...X../XXXX../...XXX/.....X/","..X./..X./..X./.XXX/.X../XX../",".....X/...XXX/XXXX../...X../",".X../.X../.X../XXX./..X./..XX/","..X.../..XXXX/XXX.../X...../","XX../.X../.XXX/..X./..X./..X./"};

		shape = setup91(shape);
		shape = setup92(shape);

		return shape;
	}
	public String[][] setup91(String[][] shape) {
		shape[501] = new String[]{"X...../XXX.../..XXXX/...X../","..XX/..X./.XX./XX../.X../.X../","..X.../XXXX../...XXX/.....X/","..X./..X./..XX/.XX./.X../XX../",".....X/...XXX/XXXX../..X.../",".X../.X../XX../.XX./..X./..XX/","...X../..XXXX/XXX.../X...../","XX../.X../.XX./..XX/..X./..X./"};
		shape[502] = new String[]{"X...../XXX.X./..XXXX/",".XX/.X./XX./X../XX./X../","XXXX../.X.XXX/.....X/","..X/.XX/..X/.XX/.X./XX./",".....X/.X.XXX/XXXX../","X../XX./X../XX./.X./.XX/","..XXXX/XXX.X./X...../","XX./.X./.XX/..X/.XX/..X/"};
		shape[503] = new String[]{"X...../XXX.../..XXXX/....X./","..XX/..X./.XX./.X../XX../.X../",".X..../XXXX../...XXX/.....X/","..X./..XX/..X./.XX./.X../XX../",".....X/...XXX/XXXX../.X..../",".X../XX../.X../.XX./..X./..XX/","....X./..XXXX/XXX.../X...../","XX../.X../.XX./..X./..XX/..X./"};
		shape[504] = new String[]{"X...../XXX..X/..XXXX/",".XX/.X./XX./X../X../XX./","XXXX../X..XXX/.....X/",".XX/..X/..X/.XX/.X./XX./",".....X/X..XXX/XXXX../","XX./X../X../XX./.X./.XX/","..XXXX/XXX..X/X...../","XX./.X./.XX/..X/..X/.XX/"};
		shape[505] = new String[]{"X...../XXX.../..XXXX/.....X/","..XX/..X./.XX./.X../.X../XX../","X...../XXXX../...XXX/.....X/","..XX/..X./..X./.XX./.X../XX../",".....X/...XXX/XXXX../X...../","XX../.X../.X../.XX./..X./..XX/",".....X/..XXXX/XXX.../X...../","XX../.X../.XX./..X./..X./..XX/"};
		shape[506] = new String[]{".X..../XXX.../X.XXXX/","XX./.XX/XX./X../X../X../","XXXX.X/...XXX/....X./","..X/..X/..X/.XX/XX./.XX/","....X./...XXX/XXXX.X/","X../X../X../XX./.XX/XX./","X.XXXX/XXX.../.X..../",".XX/XX./.XX/..X/..X/..X/"};
		shape[507] = new String[]{"XXXX../X.XXXX/","XX/.X/XX/XX/X./X./","XXXX.X/..XXXX/",".X/.X/XX/XX/X./XX/","..XXXX/XXXX.X/","X./X./XX/XX/.X/XX/","X.XXXX/XXXX../","XX/X./XX/XX/.X/.X/"};
		shape[508] = new String[]{"..X.../XXX.../X.XXXX/","XX./.X./XXX/X../X../X../","XXXX.X/...XXX/...X../","..X/..X/..X/XXX/.X./.XX/","...X../...XXX/XXXX.X/","X../X../X../XXX/.X./XX./","X.XXXX/XXX.../..X.../",".XX/.X./XXX/..X/..X/..X/"};
		shape[509] = new String[]{".XXX.../XX.XXXX/","X./XX/.X/XX/X./X./X./","XXXX.XX/...XXX./",".X/.X/.X/XX/X./XX/.X/","...XXX./XXXX.XX/","X./X./X./XX/.X/XX/X./","XX.XXXX/.XXX.../",".X/XX/X./XX/.X/.X/.X/"};
		shape[510] = new String[]{"XXX.../X.XXXX/X...../","XXX/..X/.XX/.X./.X./.X./",".....X/XXXX.X/...XXX/",".X./.X./.X./XX./X../XXX/","...XXX/XXXX.X/.....X/",".X./.X./.X./.XX/..X/XXX/","X...../X.XXXX/XXX.../","XXX/X../XX./.X./.X./.X./"};
		shape[511] = new String[]{"XXX.../X.XXXX/..X.../",".XX/..X/XXX/.X./.X./.X./","...X../XXXX.X/...XXX/",".X./.X./.X./XXX/X../XX./","...XXX/XXXX.X/...X../",".X./.X./.X./XXX/..X/.XX/","..X.../X.XXXX/XXX.../","XX./X../XXX/.X./.X./.X./"};
		shape[512] = new String[]{"XXX.../X.XXXX/...X../",".XX/..X/.XX/XX./.X./.X./","..X.../XXXX.X/...XXX/",".X./.X./.XX/XX./X../XX./","...XXX/XXXX.X/..X.../",".X./.X./XX./.XX/..X/.XX/","...X../X.XXXX/XXX.../","XX./X../XX./.XX/.X./.X./"};
		shape[513] = new String[]{"XXX.X./X.XXXX/","XX/.X/XX/X./XX/X./","XXXX.X/.X.XXX/",".X/XX/.X/XX/X./XX/",".X.XXX/XXXX.X/","X./XX/X./XX/.X/XX/","X.XXXX/XXX.X./","XX/X./XX/.X/XX/.X/"};
		shape[514] = new String[]{"XXX.../X.XXXX/....X./",".XX/..X/.XX/.X./XX./.X./",".X..../XXXX.X/...XXX/",".X./.XX/.X./XX./X../XX./","...XXX/XXXX.X/.X..../",".X./XX./.X./.XX/..X/.XX/","....X./X.XXXX/XXX.../","XX./X../XX./.X./.XX/.X./"};
		shape[515] = new String[]{"XXX..X/X.XXXX/","XX/.X/XX/X./X./XX/","XXXX.X/X..XXX/","XX/.X/.X/XX/X./XX/","X..XXX/XXXX.X/","XX/X./X./XX/.X/XX/","X.XXXX/XXX..X/","XX/X./XX/.X/.X/XX/"};
		shape[516] = new String[]{"XXX.../X.XXXX/.....X/",".XX/..X/.XX/.X./.X./XX./","X...../XXXX.X/...XXX/",".XX/.X./.X./XX./X../XX./","...XXX/XXXX.X/X...../","XX./.X./.X./.XX/..X/.XX/",".....X/X.XXXX/XXX.../","XX./X../XX./.X./.X./.XX/"};
		shape[517] = new String[]{".XX.../XXX.../..XXXX/",".X./.XX/XXX/X../X../X../","XXXX../...XXX/...XX./","..X/..X/..X/XXX/XX./.X./","...XX./...XXX/XXXX../","X../X../X../XXX/.XX/.X./","..XXXX/XXX.../.XX.../",".X./XX./XXX/..X/..X/..X/"};
		shape[518] = new String[]{".X..../.X..../XXX.../..XXXX/",".X../.XXX/XX../X.../X.../X.../","XXXX../...XXX/....X./....X./","...X/...X/...X/..XX/XXX./..X./","....X./....X./...XXX/XXXX../","X.../X.../X.../XX../.XXX/.X../","..XXXX/XXX.../.X..../.X..../","..X./XXX./..XX/...X/...X/...X/"};
		shape[519] = new String[]{".X..../XXXX../..XXXX/",".X./.XX/XX./XX./X../X../","XXXX../..XXXX/....X./","..X/..X/.XX/.XX/XX./.X./","....X./..XXXX/XXXX../","X../X../XX./XX./.XX/.X./","..XXXX/XXXX../.X..../",".X./XX./.XX/.XX/..X/..X/"};
		shape[520] = new String[]{".X..../XXX.../..XXXX/..X.../","..X./..XX/XXX./.X../.X../.X../","...X../XXXX../...XXX/....X./","..X./..X./..X./.XXX/XX../.X../","....X./...XXX/XXXX../...X../",".X../.X../.X../XXX./..XX/..X./","..X.../..XXXX/XXX.../.X..../",".X../XX../.XXX/..X./..X./..X./"};
		shape[521] = new String[]{".X..../XXX.../..XXXX/...X../","..X./..XX/.XX./XX../.X../.X../","..X.../XXXX../...XXX/....X./","..X./..X./..XX/.XX./XX../.X../","....X./...XXX/XXXX../..X.../",".X../.X../XX../.XX./..XX/..X./","...X../..XXXX/XXX.../.X..../",".X../XX../.XX./..XX/..X./..X./"};
		shape[522] = new String[]{".X..../XXX.X./..XXXX/",".X./.XX/XX./X../XX./X../","XXXX../.X.XXX/....X./","..X/.XX/..X/.XX/XX./.X./","....X./.X.XXX/XXXX../","X../XX./X../XX./.XX/.X./","..XXXX/XXX.X./.X..../",".X./XX./.XX/..X/.XX/..X/"};
		shape[523] = new String[]{".X..../XXX.../..XXXX/....X./","..X./..XX/.XX./.X../XX../.X../",".X..../XXXX../...XXX/....X./","..X./..XX/..X./.XX./XX../.X../","....X./...XXX/XXXX../.X..../",".X../XX../.X../.XX./..XX/..X./","....X./..XXXX/XXX.../.X..../",".X../XX../.XX./..X./..XX/..X./"};
		shape[524] = new String[]{".X..../XXX..X/..XXXX/",".X./.XX/XX./X../X../XX./","XXXX../X..XXX/....X./",".XX/..X/..X/.XX/XX./.X./","....X./X..XXX/XXXX../","XX./X../X../XX./.XX/.X./","..XXXX/XXX..X/.X..../",".X./XX./.XX/..X/..X/.XX/"};
		shape[525] = new String[]{".X..../XXX.../..XXXX/.....X/","..X./..XX/.XX./.X../.X../XX../","X...../XXXX../...XXX/....X./","..XX/..X./..X./.XX./XX../.X../","....X./...XXX/XXXX../X...../","XX../.X../.X../.XX./..XX/..X./",".....X/..XXXX/XXX.../.X..../",".X../XX../.XX./..X./..X./..XX/"};
		shape[526] = new String[]{"..X.../XXXX../..XXXX/",".X./.X./XXX/XX./X../X../","XXXX../..XXXX/...X../","..X/..X/.XX/XXX/.X./.X./","...X../..XXXX/XXXX../","X../X../XX./XXX/.X./.X./","..XXXX/XXXX../..X.../",".X./.X./XXX/.XX/..X/..X/"};
		shape[527] = new String[]{"...X../XXXX../..XXXX/",".X./.X./XX./XXX/X../X../","XXXX../..XXXX/..X.../","..X/..X/XXX/.XX/.X./.X./","..X.../..XXXX/XXXX../","X../X../XXX/XX./.X./.X./","..XXXX/XXXX../...X../",".X./.X./.XX/XXX/..X/..X/"};
		shape[528] = new String[]{"..XX../XXX.../..XXXX/",".X./.X./XXX/X.X/X../X../","XXXX../...XXX/..XX../","..X/..X/X.X/XXX/.X./.X./","..XX../...XXX/XXXX../","X../X../X.X/XXX/.X./.X./","..XXXX/XXX.../..XX../",".X./.X./XXX/X.X/..X/..X/"};
		shape[529] = new String[]{"..X.../..X.../XXX.../..XXXX/",".X../.X../XXXX/X.../X.../X.../","XXXX../...XXX/...X../...X../","...X/...X/...X/XXXX/..X./..X./","...X../...X../...XXX/XXXX../","X.../X.../X.../XXXX/.X../.X../","..XXXX/XXX.../..X.../..X.../","..X./..X./XXXX/...X/...X/...X/"};
		shape[530] = new String[]{"..X.../XXX.../..XXXX/..X.../","..X./..X./XXXX/.X../.X../.X../","...X../XXXX../...XXX/...X../","..X./..X./..X./XXXX/.X../.X../","...X../...XXX/XXXX../...X../",".X../.X../.X../XXXX/..X./..X./","..X.../..XXXX/XXX.../..X.../",".X../.X../XXXX/..X./..X./..X./"};
		shape[531] = new String[]{"..X.../XXX.../..XXXX/...X../","..X./..X./.XXX/XX../.X../.X../","..X.../XXXX../...XXX/...X../","..X./..X./..XX/XXX./.X../.X../","...X../...XXX/XXXX../..X.../",".X../.X../XX../.XXX/..X./..X./","...X../..XXXX/XXX.../..X.../",".X../.X../XXX./..XX/..X./..X./"};
		shape[532] = new String[]{"..X.../XXX.X./..XXXX/",".X./.X./XXX/X../XX./X../","XXXX../.X.XXX/...X../","..X/.XX/..X/XXX/.X./.X./","...X../.X.XXX/XXXX../","X../XX./X../XXX/.X./.X./","..XXXX/XXX.X./..X.../",".X./.X./XXX/..X/.XX/..X/"};
		shape[533] = new String[]{"..X.../XXX.../..XXXX/....X./","..X./..X./.XXX/.X../XX../.X../",".X..../XXXX../...XXX/...X../","..X./..XX/..X./XXX./.X../.X../","...X../...XXX/XXXX../.X..../",".X../XX../.X../.XXX/..X./..X./","....X./..XXXX/XXX.../..X.../",".X../.X../XXX./..X./..XX/..X./"};
		shape[534] = new String[]{"..X.../XXX..X/..XXXX/",".X./.X./XXX/X../X../XX./","XXXX../X..XXX/...X../",".XX/..X/..X/XXX/.X./.X./","...X../X..XXX/XXXX../","XX./X../X../XXX/.X./.X./","..XXXX/XXX..X/..X.../",".X./.X./XXX/..X/..X/.XX/"};
		shape[535] = new String[]{"..X.../XXX.../..XXXX/.....X/","..X./..X./.XXX/.X../.X../XX../","X...../XXXX../...XXX/...X../","..XX/..X./..X./XXX./.X../.X../","...X../...XXX/XXXX../X...../","XX../.X../.X../.XXX/..X./..X./",".....X/..XXXX/XXX.../..X.../",".X../.X../XXX./..X./..X./..XX/"};
		shape[536] = new String[]{"XXX.../..XXXX/..XX../","..X/..X/XXX/XX./.X./.X./","..XX../XXXX../...XXX/",".X./.X./.XX/XXX/X../X../","...XXX/XXXX../..XX../",".X./.X./XX./XXX/..X/..X/","..XX../..XXXX/XXX.../","X../X../XXX/.XX/.X./.X./"};
		shape[537] = new String[]{"XXX.X./..XXXX/..X.../","..X/..X/XXX/.X./.XX/.X./","...X../XXXX../.X.XXX/",".X./XX./.X./XXX/X../X../",".X.XXX/XXXX../...X../",".X./.XX/.X./XXX/..X/..X/","..X.../..XXXX/XXX.X./","X../X../XXX/.X./XX./.X./"};
		shape[538] = new String[]{"XXX.../..XXXX/..X.X./","..X/..X/XXX/.X./XX./.X./",".X.X../XXXX../...XXX/",".X./.XX/.X./XXX/X../X../","...XXX/XXXX../.X.X../",".X./XX./.X./XXX/..X/..X/","..X.X./..XXXX/XXX.../","X../X../XXX/.X./.XX/.X./"};
		shape[539] = new String[]{"XXX..X/..XXXX/..X.../","..X/..X/XXX/.X./.X./.XX/","...X../XXXX../X..XXX/","XX./.X./.X./XXX/X../X../","X..XXX/XXXX../...X../",".XX/.X./.X./XXX/..X/..X/","..X.../..XXXX/XXX..X/","X../X../XXX/.X./.X./XX./"};
		shape[540] = new String[]{"XXX.../..XXXX/..X..X/","..X/..X/XXX/.X./.X./XX./","X..X../XXXX../...XXX/",".XX/.X./.X./XXX/X../X../","...XXX/XXXX../X..X../","XX./.X./.X./XXX/..X/..X/","..X..X/..XXXX/XXX.../","X../X../XXX/.X./.X./.XX/"};
		shape[541] = new String[]{"XXX.../..XXXX/.XX.../","..X/X.X/XXX/.X./.X./.X./","...XX./XXXX../...XXX/",".X./.X./.X./XXX/X.X/X../","...XXX/XXXX../...XX./",".X./.X./.X./XXX/X.X/..X/",".XX.../..XXXX/XXX.../","X../X.X/XXX/.X./.X./.X./"};
		shape[542] = new String[]{"XXX.../..XXXX/..X.../..X.../","...X/...X/XXXX/..X./..X./..X./","...X../...X../XXXX../...XXX/",".X../.X../.X../XXXX/X.../X.../","...XXX/XXXX../...X../...X../","..X./..X./..X./XXXX/...X/...X/","..X.../..X.../..XXXX/XXX.../","X.../X.../XXXX/.X../.X../.X../"};
		shape[543] = new String[]{"XXX.X./..XXXX/...X../","..X/..X/.XX/XX./.XX/.X./","..X.../XXXX../.X.XXX/",".X./XX./.XX/XX./X../X../",".X.XXX/XXXX../..X.../",".X./.XX/XX./.XX/..X/..X/","...X../..XXXX/XXX.X./","X../X../XX./.XX/XX./.X./"};
		shape[544] = new String[]{"XXX.../..XXXX/...XX./","..X/..X/.XX/XX./XX./.X./",".XX.../XXXX../...XXX/",".X./.XX/.XX/XX./X../X../","...XXX/XXXX../.XX.../",".X./XX./XX./.XX/..X/..X/","...XX./..XXXX/XXX.../","X../X../XX./.XX/.XX/.X./"};
		shape[545] = new String[]{"XXX..X/..XXXX/...X../","..X/..X/.XX/XX./.X./.XX/","..X.../XXXX../X..XXX/","XX./.X./.XX/XX./X../X../","X..XXX/XXXX../..X.../",".XX/.X./XX./.XX/..X/..X/","...X../..XXXX/XXX..X/","X../X../XX./.XX/.X./XX./"};
		shape[546] = new String[]{"XXX.../..XXXX/...X.X/","..X/..X/.XX/XX./.X./XX./","X.X.../XXXX../...XXX/",".XX/.X./.XX/XX./X../X../","...XXX/XXXX../X.X.../","XX./.X./XX./.XX/..X/..X/","...X.X/..XXXX/XXX.../","X../X../XX./.XX/.X./.XX/"};
		shape[547] = new String[]{"XXX.../..XXXX/...X../...X../","...X/...X/..XX/XXX./..X./..X./","..X.../..X.../XXXX../...XXX/",".X../.X../.XXX/XX../X.../X.../","...XXX/XXXX../..X.../..X.../","..X./..X./XXX./..XX/...X/...X/","...X../...X../..XXXX/XXX.../","X.../X.../XX../.XXX/.X../.X../"};
		shape[548] = new String[]{"XXX.XX/..XXXX/",".X/.X/XX/X./XX/XX/","XXXX../XX.XXX/","XX/XX/.X/XX/X./X./","XX.XXX/XXXX../","XX/XX/X./XX/.X/.X/","..XXXX/XXX.XX/","X./X./XX/.X/XX/XX/"};
		shape[549] = new String[]{"....X./XXX.X./..XXXX/",".X./.X./XX./X../XXX/X../","XXXX../.X.XXX/.X..../","..X/XXX/..X/.XX/.X./.X./",".X..../.X.XXX/XXXX../","X../XXX/X../XX./.X./.X./","..XXXX/XXX.X./....X./",".X./.X./.XX/..X/XXX/..X/"};
		shape[550] = new String[]{"XXX.X./..XXXX/....X./","..X/..X/.XX/.X./XXX/.X./",".X..../XXXX../.X.XXX/",".X./XXX/.X./XX./X../X../",".X.XXX/XXXX../.X..../",".X./XXX/.X./.XX/..X/..X/","....X./..XXXX/XXX.X./","X../X../XX./.X./XXX/.X./"};
		shape[551] = new String[]{"XXX.X./..XXXX/.....X/","..X/..X/.XX/.X./.XX/XX./","X...../XXXX../.X.XXX/",".XX/XX./.X./XX./X../X../",".X.XXX/XXXX../X...../","XX./.XX/.X./.XX/..X/..X/",".....X/..XXXX/XXX.X./","X../X../XX./.X./XX./.XX/"};
		shape[552] = new String[]{"XXX..X/..XXXX/....X./","..X/..X/.XX/.X./XX./.XX/",".X..../XXXX../X..XXX/","XX./.XX/.X./XX./X../X../","X..XXX/XXXX../.X..../",".XX/XX./.X./.XX/..X/..X/","....X./..XXXX/XXX..X/","X../X../XX./.X./.XX/XX./"};
		shape[553] = new String[]{"XXX.../..XXXX/....XX/","..X/..X/.XX/.X./XX./XX./","XX..../XXXX../...XXX/",".XX/.XX/.X./XX./X../X../","...XXX/XXXX../XX..../","XX./XX./.X./.XX/..X/..X/","....XX/..XXXX/XXX.../","X../X../XX./.X./.XX/.XX/"};
		shape[554] = new String[]{"XXX.../..XXXX/....X./....X./","...X/...X/..XX/..X./XXX./..X./",".X..../.X..../XXXX../...XXX/",".X../.XXX/.X../XX../X.../X.../","...XXX/XXXX../.X..../.X..../","..X./XXX./..X./..XX/...X/...X/","....X./....X./..XXXX/XXX.../","X.../X.../XX../.X../.XXX/.X../"};
		shape[555] = new String[]{"XXX..XX/..XXXX./",".X/.X/XX/X./X./XX/.X/",".XXXX../XX..XXX/","X./XX/.X/.X/XX/X./X./","XX..XXX/.XXXX../",".X/XX/X./X./XX/.X/.X/","..XXXX./XXX..XX/","X./X./XX/.X/.X/XX/X./"};
		shape[556] = new String[]{".....X/XXX..X/..XXXX/",".X./.X./XX./X../X../XXX/","XXXX../X..XXX/X...../","XXX/..X/..X/.XX/.X./.X./","X...../X..XXX/XXXX../","XXX/X../X../XX./.X./.X./","..XXXX/XXX..X/.....X/",".X./.X./.XX/..X/..X/XXX/"};
		shape[557] = new String[]{"XXX..X/..XXXX/.....X/","..X/..X/.XX/.X./.X./XXX/","X...../XXXX../X..XXX/","XXX/.X./.X./XX./X../X../","X..XXX/XXXX../X...../","XXX/.X./.X./.XX/..X/..X/",".....X/..XXXX/XXX..X/","X../X../XX./.X./.X./XXX/"};
		shape[558] = new String[]{"XXX..../..XXXX./.....XX/","..X/..X/.XX/.X./.X./XX./X../","XX...../.XXXX../....XXX/","..X/.XX/.X./.X./XX./X../X../","....XXX/.XXXX../XX...../","X../XX./.X./.X./.XX/..X/..X/",".....XX/..XXXX./XXX..../","X../X../XX./.X./.X./.XX/..X/"};
		shape[559] = new String[]{"XXX.../..XXXX/.....X/.....X/","...X/...X/..XX/..X./..X./XXX./","X...../X...../XXXX../...XXX/",".XXX/.X../.X../XX../X.../X.../","...XXX/XXXX../X...../X...../","XXX./..X./..X./..XX/...X/...X/",".....X/.....X/..XXXX/XXX.../","X.../X.../XX../.X../.X../.XXX/"};
		shape[560] = new String[]{"XXX..../..XX.../...XXXX/","..X/..X/.XX/XX./X../X../X../","XXXX.../...XX../....XXX/","..X/..X/..X/.XX/XX./X../X../","....XXX/...XX../XXXX.../","X../X../X../XX./.XX/..X/..X/","...XXXX/..XX.../XXX..../","X../X../XX./.XX/..X/..X/..X/"};
		shape[561] = new String[]{"X...../XX..../.XX.../..XXXX/","..XX/.XX./XX../X.../X.../X.../","XXXX../...XX./....XX/.....X/","...X/...X/...X/..XX/.XX./XX../",".....X/....XX/...XX./XXXX../","X.../X.../X.../XX../.XX./..XX/","..XXXX/.XX.../XX..../X...../","XX../.XX./..XX/...X/...X/...X/"};
		shape[562] = new String[]{"XXX.../.XX.../..XXXX/","..X/.XX/XXX/X../X../X../","XXXX../...XX./...XXX/","..X/..X/..X/XXX/XX./X../","...XXX/...XX./XXXX../","X../X../X../XXX/.XX/..X/","..XXXX/.XX.../XXX.../","X../XX./XXX/..X/..X/..X/"};
		shape[563] = new String[]{".X..../XX..../.XX.../..XXXX/","..X./.XXX/XX../X.../X.../X.../","XXXX../...XX./....XX/....X./","...X/...X/...X/..XX/XXX./.X../","....X./....XX/...XX./XXXX../","X.../X.../X.../XX../.XXX/..X./","..XXXX/.XX.../XX..../.X..../",".X../XXX./..XX/...X/...X/...X/"};
		shape[564] = new String[]{"XX..../.XXX../..XXXX/","..X/.XX/XX./XX./X../X../","XXXX../..XXX./....XX/","..X/..X/.XX/.XX/XX./X../","....XX/..XXX./XXXX../","X../X../XX./XX./.XX/..X/","..XXXX/.XXX../XX..../","X../XX./.XX/.XX/..X/..X/"};
		shape[565] = new String[]{"XX..../.XX.../..XXXX/..X.../","...X/..XX/XXX./.X../.X../.X../","...X../XXXX../...XX./....XX/","..X./..X./..X./.XXX/XX../X.../","....XX/...XX./XXXX../...X../",".X../.X../.X../XXX./..XX/...X/","..X.../..XXXX/.XX.../XX..../","X.../XX../.XXX/..X./..X./..X./"};
		shape[566] = new String[]{"XX..../.XX.../..XXXX/...X../","...X/..XX/.XX./XX../.X../.X../","..X.../XXXX../...XX./....XX/","..X./..X./..XX/.XX./XX../X.../","....XX/...XX./XXXX../..X.../",".X../.X../XX../.XX./..XX/...X/","...X../..XXXX/.XX.../XX..../","X.../XX../.XX./..XX/..X./..X./"};
		shape[567] = new String[]{"XX..../.XX.X./..XXXX/","..X/.XX/XX./X../XX./X../","XXXX../.X.XX./....XX/","..X/.XX/..X/.XX/XX./X../","....XX/.X.XX./XXXX../","X../XX./X../XX./.XX/..X/","..XXXX/.XX.X./XX..../","X../XX./.XX/..X/.XX/..X/"};
		shape[568] = new String[]{"XX..../.XX.../..XXXX/....X./","...X/..XX/.XX./.X../XX../.X../",".X..../XXXX../...XX./....XX/","..X./..XX/..X./.XX./XX../X.../","....XX/...XX./XXXX../.X..../",".X../XX../.X../.XX./..XX/...X/","....X./..XXXX/.XX.../XX..../","X.../XX../.XX./..X./..XX/..X./"};
		shape[569] = new String[]{"XX..../.XX..X/..XXXX/","..X/.XX/XX./X../X../XX./","XXXX../X..XX./....XX/",".XX/..X/..X/.XX/XX./X../","....XX/X..XX./XXXX../","XX./X../X../XX./.XX/..X/","..XXXX/.XX..X/XX..../","X../XX./.XX/..X/..X/.XX/"};
		shape[570] = new String[]{"XX..../.XX.../..XXXX/.....X/","...X/..XX/.XX./.X../.X../XX../","X...../XXXX../...XX./....XX/","..XX/..X./..X./.XX./XX../X.../","....XX/...XX./XXXX../X...../","XX../.X../.X../.XX./..XX/...X/",".....X/..XXXX/.XX.../XX..../","X.../XX../.XX./..X./..X./..XX/"};
		shape[571] = new String[]{"X..../XX.../XX.../.XXXX/",".XXX/XXX./X.../X.../X.../","XXXX./...XX/...XX/....X/","...X/...X/...X/.XXX/XXX./","....X/...XX/...XX/XXXX./","X.../X.../X.../XXX./.XXX/",".XXXX/XX.../XX.../X..../","XXX./.XXX/...X/...X/...X/"};
		shape[572] = new String[]{"XXX../XX.../.XXXX/",".XX/XXX/X.X/X../X../","XXXX./...XX/..XXX/","..X/..X/X.X/XXX/XX./","..XXX/...XX/XXXX./","X../X../X.X/XXX/.XX/",".XXXX/XX.../XXX../","XX./XXX/X.X/..X/..X/"};
		shape[573] = new String[]{".X.../XX.../XX.../.XXXX/",".XX./XXXX/X.../X.../X.../","XXXX./...XX/...XX/...X./","...X/...X/...X/XXXX/.XX./","...X./...XX/...XX/XXXX./","X.../X.../X.../XXXX/.XX./",".XXXX/XX.../XX.../.X.../",".XX./XXXX/...X/...X/...X/"};
		shape[574] = new String[]{"XX.../XXX../.XXXX/",".XX/XXX/XX./X../X../","XXXX./..XXX/...XX/","..X/..X/.XX/XXX/XX./","...XX/..XXX/XXXX./","X../X../XX./XXX/.XX/",".XXXX/XXX../XX.../","XX./XXX/.XX/..X/..X/"};
		shape[575] = new String[]{"XX.../XX.../.XXXX/.X.../","..XX/XXXX/.X../.X../.X../","...X./XXXX./...XX/...XX/","..X./..X./..X./XXXX/XX../","...XX/...XX/XXXX./...X./",".X../.X../.X../XXXX/..XX/",".X.../.XXXX/XX.../XX.../","XX../XXXX/..X./..X./..X./"};
		shape[576] = new String[]{"XX.../XX.../.XXXX/..X../","..XX/.XXX/XX../.X../.X../","..X../XXXX./...XX/...XX/","..X./..X./..XX/XXX./XX../","...XX/...XX/XXXX./..X../",".X../.X../XX../.XXX/..XX/","..X../.XXXX/XX.../XX.../","XX../XXX./..XX/..X./..X./"};
		shape[577] = new String[]{"XX.../XX.X./.XXXX/",".XX/XXX/X../XX./X../","XXXX./.X.XX/...XX/","..X/.XX/..X/XXX/XX./","...XX/.X.XX/XXXX./","X../XX./X../XXX/.XX/",".XXXX/XX.X./XX.../","XX./XXX/..X/.XX/..X/"};
		shape[578] = new String[]{"XX.../XX.../.XXXX/...X./","..XX/.XXX/.X../XX../.X../",".X.../XXXX./...XX/...XX/","..X./..XX/..X./XXX./XX../","...XX/...XX/XXXX./.X.../",".X../XX../.X../.XXX/..XX/","...X./.XXXX/XX.../XX.../","XX../XXX./..X./..XX/..X./"};
		shape[579] = new String[]{"XX.../XX..X/.XXXX/",".XX/XXX/X../X../XX./","XXXX./X..XX/...XX/",".XX/..X/..X/XXX/XX./","...XX/X..XX/XXXX./","XX./X../X../XXX/.XX/",".XXXX/XX..X/XX.../","XX./XXX/..X/..X/.XX/"};
		shape[580] = new String[]{"XX.../XX.../.XXXX/....X/","..XX/.XXX/.X../.X../XX../","X..../XXXX./...XX/...XX/","..XX/..X./..X./XXX./XX../","...XX/...XX/XXXX./X..../","XX../.X../.X../.XXX/..XX/","....X/.XXXX/XX.../XX.../","XX../XXX./..X./..X./..XX/"};
		shape[581] = new String[]{"XX..../.X..../.XX.../..XXXX/","...X/.XXX/XX../X.../X.../X.../","XXXX../...XX./....X./....XX/","...X/...X/...X/..XX/XXX./X.../","....XX/....X./...XX./XXXX../","X.../X.../X.../XX../.XXX/...X/","..XXXX/.XX.../.X..../XX..../","X.../XXX./..XX/...X/...X/...X/"};
		shape[582] = new String[]{"XX.../X..../XX.../.XXXX/",".XXX/XX.X/X.../X.../X.../","XXXX./...XX/....X/...XX/","...X/...X/...X/X.XX/XXX./","...XX/....X/...XX/XXXX./","X.../X.../X.../XX.X/.XXX/",".XXXX/XX.../X..../XX.../","XXX./X.XX/...X/...X/...X/"};
		shape[583] = new String[]{"X..../X..../X..../XX.../.XXXX/",".XXXX/XX.../X..../X..../X..../","XXXX./...XX/....X/....X/....X/","....X/....X/....X/...XX/XXXX./"};
		shape[584] = new String[]{"X..../X..../XXX../.XXXX/",".XXX/XX../XX../X.../X.../","XXXX./..XXX/....X/....X/","...X/...X/..XX/..XX/XXX./","....X/....X/..XXX/XXXX./","X.../X.../XX../XX../.XXX/",".XXXX/XXX../X..../X..../","XXX./..XX/..XX/...X/...X/"};
		shape[585] = new String[]{"X..../X..../XX.../.XXXX/.X.../","..XXX/XXX../.X.../.X.../.X.../","...X./XXXX./...XX/....X/....X/","...X./...X./...X./..XXX/XXX../","....X/....X/...XX/XXXX./...X./",".X.../.X.../.X.../XXX../..XXX/",".X.../.XXXX/XX.../X..../X..../","XXX../..XXX/...X./...X./...X./"};
		shape[586] = new String[]{"X..../X..../XX.../.XXXX/..X../","..XXX/.XX../XX.../.X.../.X.../","..X../XXXX./...XX/....X/....X/","...X./...X./...XX/..XX./XXX../","....X/....X/...XX/XXXX./..X../",".X.../.X.../XX.../.XX../..XXX/","..X../.XXXX/XX.../X..../X..../","XXX../..XX./...XX/...X./...X./"};
		shape[587] = new String[]{"X..../X..../XX.X./.XXXX/",".XXX/XX../X.../XX../X.../","XXXX./.X.XX/....X/....X/","...X/..XX/...X/..XX/XXX./","....X/....X/.X.XX/XXXX./","X.../XX../X.../XX../.XXX/",".XXXX/XX.X./X..../X..../","XXX./..XX/...X/..XX/...X/"};
		shape[588] = new String[]{"X..../X..../XX.../.XXXX/...X./","..XXX/.XX../.X.../XX.../.X.../",".X.../XXXX./...XX/....X/....X/","...X./...XX/...X./..XX./XXX../","....X/....X/...XX/XXXX./.X.../",".X.../XX.../.X.../.XX../..XXX/","...X./.XXXX/XX.../X..../X..../","XXX../..XX./...X./...XX/...X./"};
		shape[589] = new String[]{"X..../X..../XX..X/.XXXX/",".XXX/XX../X.../X.../XX../","XXXX./X..XX/....X/....X/","..XX/...X/...X/..XX/XXX./","....X/....X/X..XX/XXXX./","XX../X.../X.../XX../.XXX/",".XXXX/XX..X/X..../X..../","XXX./..XX/...X/...X/..XX/"};
		shape[590] = new String[]{"X..../X..../XX.../.XXXX/....X/","..XXX/.XX../.X.../.X.../XX.../","X..../XXXX./...XX/....X/....X/","...XX/...X./...X./..XX./XXX../","....X/....X/...XX/XXXX./X..../","XX.../.X.../.X.../.XX../..XXX/","....X/.XXXX/XX.../X..../X..../","XXX../..XX./...X./...X./...XX/"};
		shape[591] = new String[]{"X..../XXXX./.XXXX/",".XX/XX./XX./XX./X../","XXXX./.XXXX/....X/","..X/.XX/.XX/.XX/XX./","....X/.XXXX/XXXX./","X../XX./XX./XX./.XX/",".XXXX/XXXX./X..../","XX./.XX/.XX/.XX/..X/"};
		shape[592] = new String[]{"X.X../XXX../.XXXX/",".XX/XX./XXX/X../X../","XXXX./..XXX/..X.X/","..X/..X/XXX/.XX/XX./","..X.X/..XXX/XXXX./","X../X../XXX/XX./.XX/",".XXXX/XXX../X.X../","XX./.XX/XXX/..X/..X/"};
		shape[593] = new String[]{"X..../XXX../.XXXX/.X.../","..XX/XXX./.XX./.X../.X../","...X./XXXX./..XXX/....X/","..X./..X./.XX./.XXX/XX../","....X/..XXX/XXXX./...X./",".X../.X../.XX./XXX./..XX/",".X.../.XXXX/XXX../X..../","XX../.XXX/.XX./..X./..X./"};
		shape[594] = new String[]{"X..../XXX../.XXXX/..X../","..XX/.XX./XXX./.X../.X../","..X../XXXX./..XXX/....X/","..X./..X./.XXX/.XX./XX../","....X/..XXX/XXXX./..X../",".X../.X../XXX./.XX./..XX/","..X../.XXXX/XXX../X..../","XX../.XX./.XXX/..X./..X./"};
		shape[595] = new String[]{"X..../XXX../.XXXX/...X./","..XX/.XX./.XX./XX../.X../",".X.../XXXX./..XXX/....X/","..X./..XX/.XX./.XX./XX../","....X/..XXX/XXXX./.X.../",".X../XX../.XX./.XX./..XX/","...X./.XXXX/XXX../X..../","XX../.XX./.XX./..XX/..X./"};
		shape[596] = new String[]{"X..../XXX.X/.XXXX/",".XX/XX./XX./X../XX./","XXXX./X.XXX/....X/",".XX/..X/.XX/.XX/XX./","....X/X.XXX/XXXX./","XX./X../XX./XX./.XX/",".XXXX/XXX.X/X..../","XX./.XX/.XX/..X/.XX/"};
		shape[597] = new String[]{"X..../XXX../.XXXX/....X/","..XX/.XX./.XX./.X../XX../","X..../XXXX./..XXX/....X/","..XX/..X./.XX./.XX./XX../","....X/..XXX/XXXX./X..../","XX../.X../.XX./.XX./..XX/","....X/.XXXX/XXX../X..../","XX../.XX./.XX./..X./..XX/"};
		shape[598] = new String[]{"X..../XX.../.XXXX/.XX../","..XX/XXX./XX../.X../.X../","..XX./XXXX./...XX/....X/","..X./..X./..XX/.XXX/XX../","....X/...XX/XXXX./..XX./",".X../.X../XX../XXX./..XX/",".XX../.XXXX/XX.../X..../","XX../.XXX/..XX/..X./..X./"};
		shape[599] = new String[]{"X..../XX.X./.XXXX/.X.../","..XX/XXX./.X../.XX./.X../","...X./XXXX./.X.XX/....X/","..X./.XX./..X./.XXX/XX../","....X/.X.XX/XXXX./...X./",".X../.XX./.X../XXX./..XX/",".X.../.XXXX/XX.X./X..../","XX../.XXX/..X./.XX./..X./"};
		shape[600] = new String[]{"X..../XX.../.XXXX/.X.X./","..XX/XXX./.X../XX../.X../",".X.X./XXXX./...XX/....X/","..X./..XX/..X./.XXX/XX../","....X/...XX/XXXX./.X.X./",".X../XX../.X../XXX./..XX/",".X.X./.XXXX/XX.../X..../","XX../.XXX/..X./..XX/..X./"};
		shape[601] = new String[]{"X..../XX..X/.XXXX/.X.../","..XX/XXX./.X../.X../.XX./","...X./XXXX./X..XX/....X/",".XX./..X./..X./.XXX/XX../","....X/X..XX/XXXX./...X./",".XX./.X../.X../XXX./..XX/",".X.../.XXXX/XX..X/X..../","XX../.XXX/..X./..X./.XX./"};
		shape[602] = new String[]{"X..../XX.../.XXXX/.X..X/","..XX/XXX./.X../.X../XX../","X..X./XXXX./...XX/....X/","..XX/..X./..X./.XXX/XX../","....X/...XX/XXXX./X..X./","XX../.X../.X../XXX./..XX/",".X..X/.XXXX/XX.../X..../","XX../.XXX/..X./..X./..XX/"};
		shape[603] = new String[]{"X..../XX.../.XXXX/XX.../","X.XX/XXX./.X../.X../.X../","...XX/XXXX./...XX/....X/","..X./..X./..X./.XXX/XX.X/","....X/...XX/XXXX./...XX/",".X../.X../.X../XXX./X.XX/","XX.../.XXXX/XX.../X..../","XX.X/.XXX/..X./..X./..X./"};
		shape[604] = new String[]{"X..../XX.../.XXXX/.X.../.X.../","...XX/XXXX./..X../..X../..X../","...X./...X./XXXX./...XX/....X/","..X../..X../..X../.XXXX/XX.../","....X/...XX/XXXX./...X./...X./","..X../..X../..X../XXXX./...XX/",".X.../.X.../.XXXX/XX.../X..../","XX.../.XXXX/..X../..X../..X../"};
		shape[605] = new String[]{"X..../XX.X./.XXXX/..X../","..XX/.XX./XX../.XX./.X../","..X../XXXX./.X.XX/....X/","..X./.XX./..XX/.XX./XX../","....X/.X.XX/XXXX./..X../",".X../.XX./XX../.XX./..XX/","..X../.XXXX/XX.X./X..../","XX../.XX./..XX/.XX./..X./"};
		shape[606] = new String[]{"X..../XX.../.XXXX/..XX./","..XX/.XX./XX../XX../.X../",".XX../XXXX./...XX/....X/","..X./..XX/..XX/.XX./XX../","....X/...XX/XXXX./.XX../",".X../XX../XX../.XX./..XX/","..XX./.XXXX/XX.../X..../","XX../.XX./..XX/..XX/..X./"};
		shape[607] = new String[]{"X..../XX..X/.XXXX/..X../","..XX/.XX./XX../.X../.XX./","..X../XXXX./X..XX/....X/",".XX./..X./..XX/.XX./XX../","....X/X..XX/XXXX./..X../",".XX./.X../XX../.XX./..XX/","..X../.XXXX/XX..X/X..../","XX../.XX./..XX/..X./.XX./"};
		shape[608] = new String[]{"X..../XX.../.XXXX/..X.X/","..XX/.XX./XX../.X../XX../","X.X../XXXX./...XX/....X/","..XX/..X./..XX/.XX./XX../","....X/...XX/XXXX./X.X../","XX../.X../XX../.XX./..XX/","..X.X/.XXXX/XX.../X..../","XX../.XX./..XX/..X./..XX/"};
		shape[609] = new String[]{"X..../XX.../.XXXX/..X../..X../","...XX/..XX./XXX../..X../..X../","..X../..X../XXXX./...XX/....X/","..X../..X../..XXX/.XX../XX.../","....X/...XX/XXXX./..X../..X../","..X../..X../XXX../..XX./...XX/","..X../..X../.XXXX/XX.../X..../","XX.../.XX../..XXX/..X../..X../"};
		shape[610] = new String[]{"X..../XX.XX/.XXXX/",".XX/XX./X../XX./XX./","XXXX./XX.XX/....X/",".XX/.XX/..X/.XX/XX./","....X/XX.XX/XXXX./","XX./XX./X../XX./.XX/",".XXXX/XX.XX/X..../","XX./.XX/..X/.XX/.XX/"};
		shape[611] = new String[]{"X..X./XX.X./.XXXX/",".XX/XX./X../XXX/X../","XXXX./.X.XX/.X..X/","..X/XXX/..X/.XX/XX./",".X..X/.X.XX/XXXX./","X../XXX/X../XX./.XX/",".XXXX/XX.X./X..X./","XX./.XX/..X/XXX/..X/"};
		shape[612] = new String[]{"X..../XX.X./.XXXX/...X./","..XX/.XX./.X../XXX./.X../",".X.../XXXX./.X.XX/....X/","..X./.XXX/..X./.XX./XX../","....X/.X.XX/XXXX./.X.../",".X../XXX./.X../.XX./..XX/","...X./.XXXX/XX.X./X..../","XX../.XX./..X./.XXX/..X./"};
		shape[613] = new String[]{"X..../XX.X./.XXXX/....X/","..XX/.XX./.X../.XX./XX../","X..../XXXX./.X.XX/....X/","..XX/.XX./..X./.XX./XX../","....X/.X.XX/XXXX./X..../","XX../.XX./.X../.XX./..XX/","....X/.XXXX/XX.X./X..../","XX../.XX./..X./.XX./..XX/"};
		shape[614] = new String[]{"X..../XX..X/.XXXX/...X./","..XX/.XX./.X../XX../.XX./",".X.../XXXX./X..XX/....X/",".XX./..XX/..X./.XX./XX../","....X/X..XX/XXXX./.X.../",".XX./XX../.X../.XX./..XX/","...X./.XXXX/XX..X/X..../","XX../.XX./..X./..XX/.XX./"};
		shape[615] = new String[]{"X..../XX.../.XXXX/...XX/","..XX/.XX./.X../XX../XX../","XX.../XXXX./...XX/....X/","..XX/..XX/..X./.XX./XX../","....X/...XX/XXXX./XX.../","XX../XX../.X../.XX./..XX/","...XX/.XXXX/XX.../X..../","XX../.XX./..X./..XX/..XX/"};
		shape[616] = new String[]{"X..../XX.../.XXXX/...X./...X./","...XX/..XX./..X../XXX../..X../",".X.../.X.../XXXX./...XX/....X/","..X../..XXX/..X../.XX../XX.../","....X/...XX/XXXX./.X.../.X.../","..X../XXX../..X../..XX./...XX/","...X./...X./.XXXX/XX.../X..../","XX.../.XX../..X../..XXX/..X../"};
		shape[617] = new String[]{"X...../XX..XX/.XXXX./",".XX/XX./X../X../XX./.X./",".XXXX./XX..XX/.....X/",".X./.XX/..X/..X/.XX/XX./",".....X/XX..XX/.XXXX./",".X./XX./X../X../XX./.XX/",".XXXX./XX..XX/X...../","XX./.XX/..X/..X/.XX/.X./"};
		shape[618] = new String[]{"X...X/XX..X/.XXXX/",".XX/XX./X../X../XXX/","XXXX./X..XX/X...X/","XXX/..X/..X/.XX/XX./","X...X/X..XX/XXXX./","XXX/X../X../XX./.XX/",".XXXX/XX..X/X...X/","XX./.XX/..X/..X/XXX/"};
		shape[619] = new String[]{"X..../XX..X/.XXXX/....X/","..XX/.XX./.X../.X../XXX./","X..../XXXX./X..XX/....X/",".XXX/..X./..X./.XX./XX../","....X/X..XX/XXXX./X..../","XXX./.X../.X../.XX./..XX/","....X/.XXXX/XX..X/X..../","XX../.XX./..X./..X./.XXX/"};
		shape[620] = new String[]{"X...../XX..../.XXXX./....XX/","..XX/.XX./.X../.X../XX../X.../","XX..../.XXXX./....XX/.....X/","...X/..XX/..X./..X./.XX./XX../",".....X/....XX/.XXXX./XX..../","X.../XX../.X../.X../.XX./..XX/","....XX/.XXXX./XX..../X...../","XX../.XX./..X./..X./..XX/...X/"};
		shape[621] = new String[]{"X..../XX.../.XXXX/....X/....X/","...XX/..XX./..X../..X../XXX../","X..../X..../XXXX./...XX/....X/","..XXX/..X../..X../.XX../XX.../","....X/...XX/XXXX./X..../X..../","XXX../..X../..X../..XX./...XX/","....X/....X/.XXXX/XX.../X..../","XX.../.XX../..X../..X../..XXX/"};
		shape[622] = new String[]{".XX../XXX../.XXXX/",".X./XXX/XXX/X../X../","XXXX./..XXX/..XX./","..X/..X/XXX/XXX/.X./","..XX./..XXX/XXXX./","X../X../XXX/XXX/.X./",".XXXX/XXX../.XX../",".X./XXX/XXX/..X/..X/"};
		shape[623] = new String[]{".X.../.X.../XXX../.XXXX/",".X../XXXX/XX../X.../X.../","XXXX./..XXX/...X./...X./","...X/...X/..XX/XXXX/..X./","...X./...X./..XXX/XXXX./","X.../X.../XX../XXXX/.X../",".XXXX/XXX../.X.../.X.../","..X./XXXX/..XX/...X/...X/"};
		shape[624] = new String[]{".X.../XXXX./.XXXX/",".X./XXX/XX./XX./X../","XXXX./.XXXX/...X./","..X/.XX/.XX/XXX/.X./","...X./.XXXX/XXXX./","X../XX./XX./XXX/.X./",".XXXX/XXXX./.X.../",".X./XXX/.XX/.XX/..X/"};
		shape[625] = new String[]{".X.../XXX../.XXXX/.X.../","..X./XXXX/.XX./.X../.X../","...X./XXXX./..XXX/...X./","..X./..X./.XX./XXXX/.X../","...X./..XXX/XXXX./...X./",".X../.X../.XX./XXXX/..X./",".X.../.XXXX/XXX../.X.../",".X../XXXX/.XX./..X./..X./"};
		shape[626] = new String[]{".X.../XXX../.XXXX/..X../","..X./.XXX/XXX./.X../.X../","..X../XXXX./..XXX/...X./","..X./..X./.XXX/XXX./.X../","...X./..XXX/XXXX./..X../",".X../.X../XXX./.XXX/..X./","..X../.XXXX/XXX../.X.../",".X../XXX./.XXX/..X./..X./"};
		shape[627] = new String[]{".X.../XXX../.XXXX/...X./","..X./.XXX/.XX./XX../.X../",".X.../XXXX./..XXX/...X./","..X./..XX/.XX./XXX./.X../","...X./..XXX/XXXX./.X.../",".X../XX../.XX./.XXX/..X./","...X./.XXXX/XXX../.X.../",".X../XXX./.XX./..XX/..X./"};
		shape[628] = new String[]{".X.../XXX.X/.XXXX/",".X./XXX/XX./X../XX./","XXXX./X.XXX/...X./",".XX/..X/.XX/XXX/.X./","...X./X.XXX/XXXX./","XX./X../XX./XXX/.X./",".XXXX/XXX.X/.X.../",".X./XXX/.XX/..X/.XX/"};
		shape[629] = new String[]{".X.../XXX../.XXXX/....X/","..X./.XXX/.XX./.X../XX../","X..../XXXX./..XXX/...X./","..XX/..X./.XX./XXX./.X../","...X./..XXX/XXXX./X..../","XX../.X../.XX./.XXX/..X./","....X/.XXXX/XXX../.X.../",".X../XXX./.XX./..X./..XX/"};
		shape[630] = new String[]{"..X../XXXX./.XXXX/",".X./XX./XXX/XX./X../","XXXX./.XXXX/..X../","..X/.XX/XXX/.XX/.X./","..X../.XXXX/XXXX./","X../XX./XXX/XX./.X./",".XXXX/XXXX./..X../",".X./.XX/XXX/.XX/..X/"};
		shape[631] = new String[]{"...X./XXXX./.XXXX/",".X./XX./XX./XXX/X../","XXXX./.XXXX/.X.../","..X/XXX/.XX/.XX/.X./",".X.../.XXXX/XXXX./","X../XXX/XX./XX./.X./",".XXXX/XXXX./...X./",".X./.XX/.XX/XXX/..X/"};
		shape[632] = new String[]{"..XX./XXX../.XXXX/",".X./XX./XXX/X.X/X../","XXXX./..XXX/.XX../","..X/X.X/XXX/.XX/.X./",".XX../..XXX/XXXX./","X../X.X/XXX/XX./.X./",".XXXX/XXX../..XX./",".X./.XX/XXX/X.X/..X/"};
		shape[633] = new String[]{"..X../..X../XXX../.XXXX/",".X../XX../XXXX/X.../X.../","XXXX./..XXX/..X../..X../","...X/...X/XXXX/..XX/..X./","..X../..X../..XXX/XXXX./","X.../X.../XXXX/XX../.X../",".XXXX/XXX../..X../..X../","..X./..XX/XXXX/...X/...X/"};
		shape[634] = new String[]{"..X../XXX../.XXXX/.X.../","..X./XXX./.XXX/.X../.X../","...X./XXXX./..XXX/..X../","..X./..X./XXX./.XXX/.X../","..X../..XXX/XXXX./...X./",".X../.X../.XXX/XXX./..X./",".X.../.XXXX/XXX../..X../",".X../.XXX/XXX./..X./..X./"};
		shape[635] = new String[]{"..X../XXX../.XXXX/..X../","..X./.XX./XXXX/.X../.X../","..X../XXXX./..XXX/..X../","..X./..X./XXXX/.XX./.X../","..X../..XXX/XXXX./..X../",".X../.X../XXXX/.XX./..X./","..X../.XXXX/XXX../..X../",".X../.XX./XXXX/..X./..X./"};
		shape[636] = new String[]{"..X../XXX../.XXXX/...X./","..X./.XX./.XXX/XX../.X../",".X.../XXXX./..XXX/..X../","..X./..XX/XXX./.XX./.X../","..X../..XXX/XXXX./.X.../",".X../XX../.XXX/.XX./..X./","...X./.XXXX/XXX../..X../",".X../.XX./XXX./..XX/..X./"};
		shape[637] = new String[]{"..X../XXX.X/.XXXX/",".X./XX./XXX/X../XX./","XXXX./X.XXX/..X../",".XX/..X/XXX/.XX/.X./","..X../X.XXX/XXXX./","XX./X../XXX/XX./.X./",".XXXX/XXX.X/..X../",".X./.XX/XXX/..X/.XX/"};
		shape[638] = new String[]{"..X../XXX../.XXXX/....X/","..X./.XX./.XXX/.X../XX../","X..../XXXX./..XXX/..X../","..XX/..X./XXX./.XX./.X../","..X../..XXX/XXXX./X..../","XX../.X../.XXX/.XX./..X./","....X/.XXXX/XXX../..X../",".X../.XX./XXX./..X./..XX/"};
		shape[639] = new String[]{"XXX../.XXXX/.XX../","..X/XXX/XXX/.X./.X./","..XX./XXXX./..XXX/",".X./.X./XXX/XXX/X../","..XXX/XXXX./..XX./",".X./.X./XXX/XXX/..X/",".XX../.XXXX/XXX../","X../XXX/XXX/.X./.X./"};
		shape[640] = new String[]{"XXX../.XXXX/.X.X./","..X/XXX/.XX/XX./.X./",".X.X./XXXX./..XXX/",".X./.XX/XX./XXX/X../","..XXX/XXXX./.X.X./",".X./XX./.XX/XXX/..X/",".X.X./.XXXX/XXX../","X../XXX/XX./.XX/.X./"};
		shape[641] = new String[]{"XXX.X/.XXXX/.X.../","..X/XXX/.XX/.X./.XX/","...X./XXXX./X.XXX/","XX./.X./XX./XXX/X../","X.XXX/XXXX./...X./",".XX/.X./.XX/XXX/..X/",".X.../.XXXX/XXX.X/","X../XXX/XX./.X./XX./"};
		shape[642] = new String[]{"XXX../.XXXX/.X..X/","..X/XXX/.XX/.X./XX./","X..X./XXXX./..XXX/",".XX/.X./XX./XXX/X../","..XXX/XXXX./X..X./","XX./.X./.XX/XXX/..X/",".X..X/.XXXX/XXX../","X../XXX/XX./.X./.XX/"};
		shape[643] = new String[]{"XXX../.XXXX/XX.../","X.X/XXX/.XX/.X./.X./","...XX/XXXX./..XXX/",".X./.X./XX./XXX/X.X/","..XXX/XXXX./...XX/",".X./.X./.XX/XXX/X.X/","XX.../.XXXX/XXX../","X.X/XXX/XX./.X./.X./"};
		shape[644] = new String[]{"XXX../.XXXX/.X.../.X.../","...X/XXXX/..XX/..X./..X./","...X./...X./XXXX./..XXX/",".X../.X../XX../XXXX/X.../","..XXX/XXXX./...X./...X./","..X./..X./..XX/XXXX/...X/",".X.../.X.../.XXXX/XXX../","X.../XXXX/XX../.X../.X../"};
		shape[645] = new String[]{"XXX../.XXXX/..XX./","..X/.XX/XXX/XX./.X./",".XX../XXXX./..XXX/",".X./.XX/XXX/XX./X../","..XXX/XXXX./.XX../",".X./XX./XXX/.XX/..X/","..XX./.XXXX/XXX../","X../XX./XXX/.XX/.X./"};
		shape[646] = new String[]{"XXX.X/.XXXX/..X../","..X/.XX/XXX/.X./.XX/","..X../XXXX./X.XXX/","XX./.X./XXX/XX./X../","X.XXX/XXXX./..X../",".XX/.X./XXX/.XX/..X/","..X../.XXXX/XXX.X/","X../XX./XXX/.X./XX./"};
		shape[647] = new String[]{"XXX../.XXXX/..X.X/","..X/.XX/XXX/.X./XX./","X.X../XXXX./..XXX/",".XX/.X./XXX/XX./X../","..XXX/XXXX./X.X../","XX./.X./XXX/.XX/..X/","..X.X/.XXXX/XXX../","X../XX./XXX/.X./.XX/"};
		shape[648] = new String[]{"XXX../.XXXX/..X../..X../","...X/..XX/XXXX/..X./..X./","..X../..X../XXXX./..XXX/",".X../.X../XXXX/XX../X.../","..XXX/XXXX./..X../..X../","..X./..X./XXXX/..XX/...X/","..X../..X../.XXXX/XXX../","X.../XX../XXXX/.X../.X../"};
		shape[649] = new String[]{"XXX.X/.XXXX/...X./","..X/.XX/.XX/XX./.XX/",".X.../XXXX./X.XXX/","XX./.XX/XX./XX./X../","X.XXX/XXXX./.X.../",".XX/XX./.XX/.XX/..X/","...X./.XXXX/XXX.X/","X../XX./XX./.XX/XX./"};
		shape[650] = new String[]{"XXX../.XXXX/...XX/","..X/.XX/.XX/XX./XX./","XX.../XXXX./..XXX/",".XX/.XX/XX./XX./X../","..XXX/XXXX./XX.../","XX./XX./.XX/.XX/..X/","...XX/.XXXX/XXX../","X../XX./XX./.XX/.XX/"};
		shape[651] = new String[]{"XXX../.XXXX/...X./...X./","...X/..XX/..XX/XXX./..X./",".X.../.X.../XXXX./..XXX/",".X../.XXX/XX../XX../X.../","..XXX/XXXX./.X.../.X.../","..X./XXX./..XX/..XX/...X/","...X./...X./.XXXX/XXX../","X.../XX../XX../.XXX/.X../"};
		shape[652] = new String[]{"XXX.XX/.XXXX./",".X/XX/XX/X./XX/.X/",".XXXX./XX.XXX/","X./XX/.X/XX/XX/X./","XX.XXX/.XXXX./",".X/XX/X./XX/XX/.X/",".XXXX./XXX.XX/","X./XX/XX/.X/XX/X./"};
		shape[653] = new String[]{"....X/XXX.X/.XXXX/",".X./XX./XX./X../XXX/","XXXX./X.XXX/X..../","XXX/..X/.XX/.XX/.X./","X..../X.XXX/XXXX./","XXX/X../XX./XX./.X./",".XXXX/XXX.X/....X/",".X./.XX/.XX/..X/XXX/"};
		shape[654] = new String[]{"XXX.X/.XXXX/....X/","..X/.XX/.XX/.X./XXX/","X..../XXXX./X.XXX/","XXX/.X./XX./XX./X../","X.XXX/XXXX./X..../","XXX/.X./.XX/.XX/..X/","....X/.XXXX/XXX.X/","X../XX./XX./.X./XXX/"};
		shape[655] = new String[]{"XXX.../.XXXX./....XX/","..X/.XX/.XX/.X./XX./X../","XX..../.XXXX./...XXX/","..X/.XX/.X./XX./XX./X../","...XXX/.XXXX./XX..../","X../XX./.X./.XX/.XX/..X/","....XX/.XXXX./XXX.../","X../XX./XX./.X./.XX/..X/"};
		shape[656] = new String[]{"XXX../.XXXX/....X/....X/","...X/..XX/..XX/..X./XXX./","X..../X..../XXXX./..XXX/",".XXX/.X../XX../XX../X.../","..XXX/XXXX./X..../X..../","XXX./..X./..XX/..XX/...X/","....X/....X/.XXXX/XXX../","X.../XX../XX../.X../.XXX/"};
		shape[657] = new String[]{".X.../.XX../XX.../.XXXX/",".X../XXXX/X.X./X.../X.../","XXXX./...XX/..XX./...X./","...X/...X/.X.X/XXXX/..X./","...X./..XX./...XX/XXXX./","X.../X.../X.X./XXXX/.X../",".XXXX/XX.../.XX../.X.../","..X./XXXX/.X.X/...X/...X/"};
		shape[658] = new String[]{".XXX./XX.../.XXXX/",".X./XXX/X.X/X.X/X../","XXXX./...XX/.XXX./","..X/X.X/X.X/XXX/.X./",".XXX./...XX/XXXX./","X../X.X/X.X/XXX/.X./",".XXXX/XX.../.XXX./",".X./XXX/X.X/X.X/..X/"};
		shape[659] = new String[]{"..X../.XX../XX.../.XXXX/",".X../XXX./X.XX/X.../X.../","XXXX./...XX/..XX./..X../","...X/...X/XX.X/.XXX/..X./","..X../..XX./...XX/XXXX./","X.../X.../X.XX/XXX./.X../",".XXXX/XX.../.XX../..X../","..X./.XXX/XX.X/...X/...X/"};
		shape[660] = new String[]{".XX../XX.../.XXXX/.X.../","..X./XXXX/.X.X/.X../.X../","...X./XXXX./...XX/..XX./","..X./..X./X.X./XXXX/.X../","..XX./...XX/XXXX./...X./",".X../.X../.X.X/XXXX/..X./",".X.../.XXXX/XX.../.XX../",".X../XXXX/X.X./..X./..X./"};
		shape[661] = new String[]{".XX../XX.../.XXXX/..X../","..X./.XXX/XX.X/.X../.X../","..X../XXXX./...XX/..XX./","..X./..X./X.XX/XXX./.X../","..XX./...XX/XXXX./..X../",".X../.X../XX.X/.XXX/..X./","..X../.XXXX/XX.../.XX../",".X../XXX./X.XX/..X./..X./"};
		shape[662] = new String[]{".XX../XX.X./.XXXX/",".X./XXX/X.X/XX./X../","XXXX./.X.XX/..XX./","..X/.XX/X.X/XXX/.X./","..XX./.X.XX/XXXX./","X../XX./X.X/XXX/.X./",".XXXX/XX.X./.XX../",".X./XXX/X.X/.XX/..X/"};
		shape[663] = new String[]{".XX../XX.../.XXXX/...X./","..X./.XXX/.X.X/XX../.X../",".X.../XXXX./...XX/..XX./","..X./..XX/X.X./XXX./.X../","..XX./...XX/XXXX./.X.../",".X../XX../.X.X/.XXX/..X./","...X./.XXXX/XX.../.XX../",".X../XXX./X.X./..XX/..X./"};
		shape[664] = new String[]{".XX../XX..X/.XXXX/",".X./XXX/X.X/X../XX./","XXXX./X..XX/..XX./",".XX/..X/X.X/XXX/.X./","..XX./X..XX/XXXX./","XX./X../X.X/XXX/.X./",".XXXX/XX..X/.XX../",".X./XXX/X.X/..X/.XX/"};
		shape[665] = new String[]{".XX../XX.../.XXXX/....X/","..X./.XXX/.X.X/.X../XX../","X..../XXXX./...XX/..XX./","..XX/..X./X.X./XXX./.X../","..XX./...XX/XXXX./X..../","XX../.X../.X.X/.XXX/..X./","....X/.XXXX/XX.../.XX../",".X../XXX./X.X./..X./..XX/"};
		shape[666] = new String[]{"XX.../.X.../XX.../.XXXX/",".X.X/XXXX/X.../X.../X.../","XXXX./...XX/...X./...XX/","...X/...X/...X/XXXX/X.X./","...XX/...X./...XX/XXXX./","X.../X.../X.../XXXX/.X.X/",".XXXX/XX.../.X.../XX.../","X.X./XXXX/...X/...X/...X/"};
		shape[667] = new String[]{".XX../.X.../XX.../.XXXX/",".X../XXXX/X..X/X.../X.../","XXXX./...XX/...X./..XX./","...X/...X/X..X/XXXX/..X./","..XX./...X./...XX/XXXX./","X.../X.../X..X/XXXX/.X../",".XXXX/XX.../.X.../.XX../","..X./XXXX/X..X/...X/...X/"};
		shape[668] = new String[]{".X.../.X.../XX.../.XXXX/..X../","..X../.XXXX/XX.../.X.../.X.../","..X../XXXX./...XX/...X./...X./","...X./...X./...XX/XXXX./..X../"};
		shape[669] = new String[]{".X.../.X.../XX.X./.XXXX/",".X../XXXX/X.../XX../X.../","XXXX./.X.XX/...X./...X./","...X/..XX/...X/XXXX/..X./","...X./...X./.X.XX/XXXX./","X.../XX../X.../XXXX/.X../",".XXXX/XX.X./.X.../.X.../","..X./XXXX/...X/..XX/...X/"};
		shape[670] = new String[]{".X.../.X.../XX.../.XXXX/...X./","..X../.XXXX/.X.../XX.../.X.../",".X.../XXXX./...XX/...X./...X./","...X./...XX/...X./XXXX./..X../","...X./...X./...XX/XXXX./.X.../",".X.../XX.../.X.../.XXXX/..X../","...X./.XXXX/XX.../.X.../.X.../","..X../XXXX./...X./...XX/...X./"};
		shape[671] = new String[]{".X.../.X.../XX..X/.XXXX/",".X../XXXX/X.../X.../XX../","XXXX./X..XX/...X./...X./","..XX/...X/...X/XXXX/..X./","...X./...X./X..XX/XXXX./","XX../X.../X.../XXXX/.X../",".XXXX/XX..X/.X.../.X.../","..X./XXXX/...X/...X/..XX/"};
		shape[672] = new String[]{".X.../.X.../XX.../.XXXX/....X/","..X../.XXXX/.X.../.X.../XX.../","X..../XXXX./...XX/...X./...X./","...XX/...X./...X./XXXX./..X../","...X./...X./...XX/XXXX./X..../","XX.../.X.../.X.../.XXXX/..X../","....X/.XXXX/XX.../.X.../.X.../","..X../XXXX./...X./...X./...XX/"};
		shape[673] = new String[]{".X.../XX.../.XXXX/.XX../","..X./XXXX/XX../.X../.X../","..XX./XXXX./...XX/...X./","..X./..X./..XX/XXXX/.X../","...X./...XX/XXXX./..XX./",".X../.X../XX../XXXX/..X./",".XX../.XXXX/XX.../.X.../",".X../XXXX/..XX/..X./..X./"};
		shape[674] = new String[]{".X.../XX.X./.XXXX/.X.../","..X./XXXX/.X../.XX./.X../","...X./XXXX./.X.XX/...X./","..X./.XX./..X./XXXX/.X../","...X./.X.XX/XXXX./...X./",".X../.XX./.X../XXXX/..X./",".X.../.XXXX/XX.X./.X.../",".X../XXXX/..X./.XX./..X./"};
		shape[675] = new String[]{".X.../XX.../.XXXX/.X.X./","..X./XXXX/.X../XX../.X../",".X.X./XXXX./...XX/...X./","..X./..XX/..X./XXXX/.X../","...X./...XX/XXXX./.X.X./",".X../XX../.X../XXXX/..X./",".X.X./.XXXX/XX.../.X.../",".X../XXXX/..X./..XX/..X./"};
		shape[676] = new String[]{".X.../XX..X/.XXXX/.X.../","..X./XXXX/.X../.X../.XX./","...X./XXXX./X..XX/...X./",".XX./..X./..X./XXXX/.X../","...X./X..XX/XXXX./...X./",".XX./.X../.X../XXXX/..X./",".X.../.XXXX/XX..X/.X.../",".X../XXXX/..X./..X./.XX./"};
		shape[677] = new String[]{".X.../XX.../.XXXX/.X..X/","..X./XXXX/.X../.X../XX../","X..X./XXXX./...XX/...X./","..XX/..X./..X./XXXX/.X../","...X./...XX/XXXX./X..X./","XX../.X../.X../XXXX/..X./",".X..X/.XXXX/XX.../.X.../",".X../XXXX/..X./..X./..XX/"};
		shape[678] = new String[]{".X.../XX.../.XXXX/XX.../","X.X./XXXX/.X../.X../.X../","...XX/XXXX./...XX/...X./","..X./..X./..X./XXXX/.X.X/","...X./...XX/XXXX./...XX/",".X../.X../.X../XXXX/X.X./","XX.../.XXXX/XX.../.X.../",".X.X/XXXX/..X./..X./..X./"};
		shape[679] = new String[]{".X.../XX.X./.XXXX/..X../","..X./.XXX/XX../.XX./.X../","..X../XXXX./.X.XX/...X./","..X./.XX./..XX/XXX./.X../","...X./.X.XX/XXXX./..X../",".X../.XX./XX../.XXX/..X./","..X../.XXXX/XX.X./.X.../",".X../XXX./..XX/.XX./..X./"};
		shape[680] = new String[]{".X.../XX.../.XXXX/..XX./","..X./.XXX/XX../XX../.X../",".XX../XXXX./...XX/...X./","..X./..XX/..XX/XXX./.X../","...X./...XX/XXXX./.XX../",".X../XX../XX../.XXX/..X./","..XX./.XXXX/XX.../.X.../",".X../XXX./..XX/..XX/..X./"};
		shape[681] = new String[]{".X.../XX..X/.XXXX/..X../","..X./.XXX/XX../.X../.XX./","..X../XXXX./X..XX/...X./",".XX./..X./..XX/XXX./.X../","...X./X..XX/XXXX./..X../",".XX./.X../XX../.XXX/..X./","..X../.XXXX/XX..X/.X.../",".X../XXX./..XX/..X./.XX./"};
		shape[682] = new String[]{".X.../XX.../.XXXX/..X.X/","..X./.XXX/XX../.X../XX../","X.X../XXXX./...XX/...X./","..XX/..X./..XX/XXX./.X../","...X./...XX/XXXX./X.X../","XX../.X../XX../.XXX/..X./","..X.X/.XXXX/XX.../.X.../",".X../XXX./..XX/..X./..XX/"};
		shape[683] = new String[]{".X.../XX.../.XXXX/..X../..X../","...X./..XXX/XXX../..X../..X../","..X../..X../XXXX./...XX/...X./","..X../..X../..XXX/XXX../.X.../","...X./...XX/XXXX./..X../..X../","..X../..X../XXX../..XXX/...X./","..X../..X../.XXXX/XX.../.X.../",".X.../XXX../..XXX/..X../..X../"};
		shape[684] = new String[]{".X.../XX.XX/.XXXX/",".X./XXX/X../XX./XX./","XXXX./XX.XX/...X./",".XX/.XX/..X/XXX/.X./","...X./XX.XX/XXXX./","XX./XX./X../XXX/.X./",".XXXX/XX.XX/.X.../",".X./XXX/..X/.XX/.XX/"};
		shape[685] = new String[]{".X.X./XX.X./.XXXX/",".X./XXX/X../XXX/X../","XXXX./.X.XX/.X.X./","..X/XXX/..X/XXX/.X./",".X.X./.X.XX/XXXX./","X../XXX/X../XXX/.X./",".XXXX/XX.X./.X.X./",".X./XXX/..X/XXX/..X/"};
		shape[686] = new String[]{".X.../XX.X./.XXXX/...X./","..X./.XXX/.X../XXX./.X../",".X.../XXXX./.X.XX/...X./","..X./.XXX/..X./XXX./.X../","...X./.X.XX/XXXX./.X.../",".X../XXX./.X../.XXX/..X./","...X./.XXXX/XX.X./.X.../",".X../XXX./..X./.XXX/..X./"};
		shape[687] = new String[]{".X.../XX.X./.XXXX/....X/","..X./.XXX/.X../.XX./XX../","X..../XXXX./.X.XX/...X./","..XX/.XX./..X./XXX./.X../","...X./.X.XX/XXXX./X..../","XX../.XX./.X../.XXX/..X./","....X/.XXXX/XX.X./.X.../",".X../XXX./..X./.XX./..XX/"};
		shape[688] = new String[]{".X.../XX..X/.XXXX/...X./","..X./.XXX/.X../XX../.XX./",".X.../XXXX./X..XX/...X./",".XX./..XX/..X./XXX./.X../","...X./X..XX/XXXX./.X.../",".XX./XX../.X../.XXX/..X./","...X./.XXXX/XX..X/.X.../",".X../XXX./..X./..XX/.XX./"};
		shape[689] = new String[]{".X.../XX.../.XXXX/...XX/","..X./.XXX/.X../XX../XX../","XX.../XXXX./...XX/...X./","..XX/..XX/..X./XXX./.X../","...X./...XX/XXXX./XX.../","XX../XX../.X../.XXX/..X./","...XX/.XXXX/XX.../.X.../",".X../XXX./..X./..XX/..XX/"};
		shape[690] = new String[]{".X.../XX.../.XXXX/...X./...X./","...X./..XXX/..X../XXX../..X../",".X.../.X.../XXXX./...XX/...X./","..X../..XXX/..X../XXX../.X.../","...X./...XX/XXXX./.X.../.X.../","..X../XXX../..X../..XXX/...X./","...X./...X./.XXXX/XX.../.X.../",".X.../XXX../..X../..XXX/..X../"};
		shape[691] = new String[]{".X..../XX..XX/.XXXX./",".X./XXX/X../X../XX./.X./",".XXXX./XX..XX/....X./",".X./.XX/..X/..X/XXX/.X./","....X./XX..XX/.XXXX./",".X./XX./X../X../XXX/.X./",".XXXX./XX..XX/.X..../",".X./XXX/..X/..X/.XX/.X./"};
		shape[692] = new String[]{".X..X/XX..X/.XXXX/",".X./XXX/X../X../XXX/","XXXX./X..XX/X..X./","XXX/..X/..X/XXX/.X./","X..X./X..XX/XXXX./","XXX/X../X../XXX/.X./",".XXXX/XX..X/.X..X/",".X./XXX/..X/..X/XXX/"};
		shape[693] = new String[]{".X.../XX..X/.XXXX/....X/","..X./.XXX/.X../.X../XXX./","X..../XXXX./X..XX/...X./",".XXX/..X./..X./XXX./.X../","...X./X..XX/XXXX./X..../","XXX./.X../.X../.XXX/..X./","....X/.XXXX/XX..X/.X.../",".X../XXX./..X./..X./.XXX/"};
		shape[694] = new String[]{".X..../XX..../.XXXX./....XX/","..X./.XXX/.X../.X../XX../X.../","XX..../.XXXX./....XX/....X./","...X/..XX/..X./..X./XXX./.X../","....X./....XX/.XXXX./XX..../","X.../XX../.X../.X../.XXX/..X./","....XX/.XXXX./XX..../.X..../",".X../XXX./..X./..X./..XX/...X/"};
		shape[695] = new String[]{".X.../XX.../.XXXX/....X/....X/","...X./..XXX/..X../..X../XXX../","X..../X..../XXXX./...XX/...X./","..XXX/..X../..X../XXX../.X.../","...X./...XX/XXXX./X..../X..../","XXX../..X../..X../..XXX/...X./","....X/....X/.XXXX/XX.../.X.../",".X.../XXX../..X../..X../..XXX/"};
		shape[696] = new String[]{"XX.X./.XXXX/.XX../","..X/XXX/XX./.XX/.X./","..XX./XXXX./.X.XX/",".X./XX./.XX/XXX/X../",".X.XX/XXXX./..XX./",".X./.XX/XX./XXX/..X/",".XX../.XXXX/XX.X./","X../XXX/.XX/XX./.X./"};
		shape[697] = new String[]{"XX.../.XXXX/.XXX./","..X/XXX/XX./XX./.X./",".XXX./XXXX./...XX/",".X./.XX/.XX/XXX/X../","...XX/XXXX./.XXX./",".X./XX./XX./XXX/..X/",".XXX./.XXXX/XX.../","X../XXX/.XX/.XX/.X./"};
		shape[698] = new String[]{"XX..X/.XXXX/.XX../","..X/XXX/XX./.X./.XX/","..XX./XXXX./X..XX/","XX./.X./.XX/XXX/X../","X..XX/XXXX./..XX./",".XX/.X./XX./XXX/..X/",".XX../.XXXX/XX..X/","X../XXX/.XX/.X./XX./"};
		shape[699] = new String[]{"XX.../.XXXX/.XX.X/","..X/XXX/XX./.X./XX./","X.XX./XXXX./...XX/",".XX/.X./.XX/XXX/X../","...XX/XXXX./X.XX./","XX./.X./XX./XXX/..X/",".XX.X/.XXXX/XX.../","X../XXX/.XX/.X./.XX/"};
		shape[700] = new String[]{"XX.../.XXXX/.XX../.X.../","...X/XXXX/.XX./..X./..X./","...X./..XX./XXXX./...XX/",".X../.X../.XX./XXXX/X.../","...XX/XXXX./..XX./...X./","..X./..X./.XX./XXXX/...X/",".X.../.XX../.XXXX/XX.../","X.../XXXX/.XX./.X../.X../"};
		shape[701] = new String[]{"XX.../.XXXX/.XX../..X../","...X/.XXX/XXX./..X./..X./","..X../..XX./XXXX./...XX/",".X../.X../.XXX/XXX./X.../","...XX/XXXX./..XX./..X../","..X./..X./XXX./.XXX/...X/","..X../.XX../.XXXX/XX.../","X.../XXX./.XXX/.X../.X../"};
		shape[702] = new String[]{"XX.XX/.XXXX/.X.../","..X/XXX/.X./.XX/.XX/","...X./XXXX./XX.XX/","XX./XX./.X./XXX/X../","XX.XX/XXXX./...X./",".XX/.XX/.X./XXX/..X/",".X.../.XXXX/XX.XX/","X../XXX/.X./XX./XX./"};
		shape[703] = new String[]{"...X./XX.X./.XXXX/.X.../","..X./XXX./.X../.XXX/.X../","...X./XXXX./.X.XX/.X.../","..X./XXX./..X./.XXX/.X../",".X.../.X.XX/XXXX./...X./",".X../.XXX/.X../XXX./..X./",".X.../.XXXX/XX.X./...X./",".X../.XXX/..X./XXX./..X./"};
		shape[704] = new String[]{"XX.X./.XXXX/.X.X./","..X/XXX/.X./XXX/.X./",".X.X./XXXX./.X.XX/",".X./XXX/.X./XXX/X../",".X.XX/XXXX./.X.X./",".X./XXX/.X./XXX/..X/",".X.X./.XXXX/XX.X./","X../XXX/.X./XXX/.X./"};
		shape[705] = new String[]{"XX.X./.XXXX/.X..X/","..X/XXX/.X./.XX/XX./","X..X./XXXX./.X.XX/",".XX/XX./.X./XXX/X../",".X.XX/XXXX./X..X./","XX./.XX/.X./XXX/..X/",".X..X/.XXXX/XX.X./","X../XXX/.X./XX./.XX/"};
		shape[706] = new String[]{"XX.X./.XXXX/XX.../","X.X/XXX/.X./.XX/.X./","...XX/XXXX./.X.XX/",".X./XX./.X./XXX/X.X/",".X.XX/XXXX./...XX/",".X./.XX/.X./XXX/X.X/","XX.../.XXXX/XX.X./","X.X/XXX/.X./XX./.X./"};
		shape[707] = new String[]{"XX.X./.XXXX/.X.../.X.../","...X/XXXX/..X./..XX/..X./","...X./...X./XXXX./.X.XX/",".X../XX../.X../XXXX/X.../",".X.XX/XXXX./...X./...X./","..X./..XX/..X./XXXX/...X/",".X.../.X.../.XXXX/XX.X./","X.../XXXX/.X../XX../.X../"};
		shape[708] = new String[]{"XX..X/.XXXX/.X.X./","..X/XXX/.X./XX./.XX/",".X.X./XXXX./X..XX/","XX./.XX/.X./XXX/X../","X..XX/XXXX./.X.X./",".XX/XX./.X./XXX/..X/",".X.X./.XXXX/XX..X/","X../XXX/.X./.XX/XX./"};
		shape[709] = new String[]{"XX.../.XXXX/.X.XX/","..X/XXX/.X./XX./XX./","XX.X./XXXX./...XX/",".XX/.XX/.X./XXX/X../","...XX/XXXX./XX.X./","XX./XX./.X./XXX/..X/",".X.XX/.XXXX/XX.../","X../XXX/.X./.XX/.XX/"};
		shape[710] = new String[]{"XX.../.XXXX/.X.X./.X.../","...X/XXXX/..X./.XX./..X./","...X./.X.X./XXXX./...XX/",".X../.XX./.X../XXXX/X.../","...XX/XXXX./.X.X./...X./","..X./.XX./..X./XXXX/...X/",".X.../.X.X./.XXXX/XX.../","X.../XXXX/.X../.XX./.X../"};
		shape[711] = new String[]{"XX.../.XXXX/.X.X./...X./","...X/.XXX/..X./XXX./..X./",".X.../.X.X./XXXX./...XX/",".X../.XXX/.X../XXX./X.../","...XX/XXXX./.X.X./.X.../","..X./XXX./..X./.XXX/...X/","...X./.X.X./.XXXX/XX.../","X.../XXX./.X../.XXX/.X../"};
		shape[712] = new String[]{"XX..XX/.XXXX./.X..../","..X/XXX/.X./.X./.XX/..X/","....X./.XXXX./XX..XX/","X../XX./.X./.X./XXX/X../","XX..XX/.XXXX./....X./","..X/.XX/.X./.X./XXX/..X/",".X..../.XXXX./XX..XX/","X../XXX/.X./.X./XX./X../"};
		shape[713] = new String[]{"....X/XX..X/.XXXX/.X.../","..X./XXX./.X../.X../.XXX/","...X./XXXX./X..XX/X..../","XXX./..X./..X./.XXX/.X../","X..../X..XX/XXXX./...X./",".XXX/.X../.X../XXX./..X./",".X.../.XXXX/XX..X/....X/",".X../.XXX/..X./..X./XXX./"};
		shape[714] = new String[]{"XX..X/.XXXX/.X..X/","..X/XXX/.X./.X./XXX/","X..X./XXXX./X..XX/","XXX/.X./.X./XXX/X../","X..XX/XXXX./X..X./","XXX/.X./.X./XXX/..X/",".X..X/.XXXX/XX..X/","X../XXX/.X./.X./XXX/"};
		shape[715] = new String[]{"XX..X/.XXXX/XX.../","X.X/XXX/.X./.X./.XX/","...XX/XXXX./X..XX/","XX./.X./.X./XXX/X.X/","X..XX/XXXX./...XX/",".XX/.X./.X./XXX/X.X/","XX.../.XXXX/XX..X/","X.X/XXX/.X./.X./XX./"};
		shape[716] = new String[]{"XX..X/.XXXX/.X.../.X.../","...X/XXXX/..X./..X./..XX/","...X./...X./XXXX./X..XX/","XX../.X../.X../XXXX/X.../","X..XX/XXXX./...X./...X./","..XX/..X./..X./XXXX/...X/",".X.../.X.../.XXXX/XX..X/","X.../XXXX/.X../.X../XX../"};
		shape[717] = new String[]{"XX.../.XXXX/.X..X/.X.../","...X/XXXX/..X./..X./.XX./","...X./X..X./XXXX./...XX/",".XX./.X../.X../XXXX/X.../","...XX/XXXX./X..X./...X./",".XX./..X./..X./XXXX/...X/",".X.../.X..X/.XXXX/XX.../","X.../XXXX/.X../.X../.XX./"};
		shape[718] = new String[]{"XX..../.XXXX./.X..XX/","..X/XXX/.X./.X./XX./X../","XX..X./.XXXX./....XX/","..X/.XX/.X./.X./XXX/X../","....XX/.XXXX./XX..X./","X../XX./.X./.X./XXX/..X/",".X..XX/.XXXX./XX..../","X../XXX/.X./.X./.XX/..X/"};
		shape[719] = new String[]{"XX.../.XXXX/.X..X/....X/","...X/.XXX/..X./..X./XXX./","X..../X..X./XXXX./...XX/",".XXX/.X../.X../XXX./X.../","...XX/XXXX./X..X./X..../","XXX./..X./..X./.XXX/...X/","....X/.X..X/.XXXX/XX.../","X.../XXX./.X../.X../.XXX/"};
		shape[720] = new String[]{"XX.../.XXXX/.X.../XX.../","X..X/XXXX/..X./..X./..X./","...XX/...X./XXXX./...XX/",".X../.X../.X../XXXX/X..X/","...XX/XXXX./...X./...XX/","..X./..X./..X./XXXX/X..X/","XX.../.X.../.XXXX/XX.../","X..X/XXXX/.X../.X../.X../"};
		shape[721] = new String[]{"XX.../.XXXX/.X.../.XX../","...X/XXXX/X.X./..X./..X./","..XX./...X./XXXX./...XX/",".X../.X../.X.X/XXXX/X.../","...XX/XXXX./...X./..XX./","..X./..X./X.X./XXXX/...X/",".XX../.X.../.XXXX/XX.../","X.../XXXX/.X.X/.X../.X../"};
		shape[722] = new String[]{"XX.XX/.XXXX/..X../","..X/.XX/XX./.XX/.XX/","..X../XXXX./XX.XX/","XX./XX./.XX/XX./X../","XX.XX/XXXX./..X../",".XX/.XX/XX./.XX/..X/","..X../.XXXX/XX.XX/","X../XX./.XX/XX./XX./"};
		shape[723] = new String[]{"...X./XX.X./.XXXX/..X../","..X./.XX./XX../.XXX/.X../","..X../XXXX./.X.XX/.X.../","..X./XXX./..XX/.XX./.X../",".X.../.X.XX/XXXX./..X../",".X../.XXX/XX../.XX./..X./","..X../.XXXX/XX.X./...X./",".X../.XX./..XX/XXX./..X./"};
		shape[724] = new String[]{"XX.X./.XXXX/..XX./","..X/.XX/XX./XXX/.X./",".XX../XXXX./.X.XX/",".X./XXX/.XX/XX./X../",".X.XX/XXXX./.XX../",".X./XXX/XX./.XX/..X/","..XX./.XXXX/XX.X./","X../XX./.XX/XXX/.X./"};
		shape[725] = new String[]{"XX.X./.XXXX/..X.X/","..X/.XX/XX./.XX/XX./","X.X../XXXX./.X.XX/",".XX/XX./.XX/XX./X../",".X.XX/XXXX./X.X../","XX./.XX/XX./.XX/..X/","..X.X/.XXXX/XX.X./","X../XX./.XX/XX./.XX/"};
		shape[726] = new String[]{"XX.X./.XXXX/..X../..X../","...X/..XX/XXX./..XX/..X./","..X../..X../XXXX./.X.XX/",".X../XX../.XXX/XX../X.../",".X.XX/XXXX./..X../..X../","..X./..XX/XXX./..XX/...X/","..X../..X../.XXXX/XX.X./","X.../XX../.XXX/XX../.X../"};
		shape[727] = new String[]{"XX..X/.XXXX/..XX./","..X/.XX/XX./XX./.XX/",".XX../XXXX./X..XX/","XX./.XX/.XX/XX./X../","X..XX/XXXX./.XX../",".XX/XX./XX./.XX/..X/","..XX./.XXXX/XX..X/","X../XX./.XX/.XX/XX./"};
		shape[728] = new String[]{"XX.../.XXXX/..XXX/","..X/.XX/XX./XX./XX./","XXX../XXXX./...XX/",".XX/.XX/.XX/XX./X../","...XX/XXXX./XXX../","XX./XX./XX./.XX/..X/","..XXX/.XXXX/XX.../","X../XX./.XX/.XX/.XX/"};
		shape[729] = new String[]{"XX.../.XXXX/..XX./..X../","...X/..XX/XXX./.XX./..X./","..X../.XX../XXXX./...XX/",".X../.XX./.XXX/XX../X.../","...XX/XXXX./.XX../..X../","..X./.XX./XXX./..XX/...X/","..X../..XX./.XXXX/XX.../","X.../XX../.XXX/.XX./.X../"};
		shape[730] = new String[]{"XX.../.XXXX/..XX./...X./","...X/..XX/.XX./XXX./..X./",".X.../.XX../XXXX./...XX/",".X../.XXX/.XX./XX../X.../","...XX/XXXX./.XX../.X.../","..X./XXX./.XX./..XX/...X/","...X./..XX./.XXXX/XX.../","X.../XX../.XX./.XXX/.X../"};
		shape[731] = new String[]{"XX..XX/.XXXX./..X.../","..X/.XX/XX./.X./.XX/..X/","...X../.XXXX./XX..XX/","X../XX./.X./.XX/XX./X../","XX..XX/.XXXX./...X../","..X/.XX/.X./XX./.XX/..X/","..X.../.XXXX./XX..XX/","X../XX./.XX/.X./XX./X../"};
		shape[732] = new String[]{"....X/XX..X/.XXXX/..X../","..X./.XX./XX../.X../.XXX/","..X../XXXX./X..XX/X..../","XXX./..X./..XX/.XX./.X../","X..../X..XX/XXXX./..X../",".XXX/.X../XX../.XX./..X./","..X../.XXXX/XX..X/....X/",".X../.XX./..XX/..X./XXX./"};
		shape[733] = new String[]{"XX..X/.XXXX/..X.X/","..X/.XX/XX./.X./XXX/","X.X../XXXX./X..XX/","XXX/.X./.XX/XX./X../","X..XX/XXXX./X.X../","XXX/.X./XX./.XX/..X/","..X.X/.XXXX/XX..X/","X../XX./.XX/.X./XXX/"};
		shape[734] = new String[]{"XX..X/.XXXX/..X../..X../","...X/..XX/XXX./..X./..XX/","..X../..X../XXXX./X..XX/","XX../.X../.XXX/XX../X.../","X..XX/XXXX./..X../..X../","..XX/..X./XXX./..XX/...X/","..X../..X../.XXXX/XX..X/","X.../XX../.XXX/.X../XX../"};
		shape[735] = new String[]{"XX.../.XXXX/..X.X/..X../","...X/..XX/XXX./..X./.XX./","..X../X.X../XXXX./...XX/",".XX./.X../.XXX/XX../X.../","...XX/XXXX./X.X../..X../",".XX./..X./XXX./..XX/...X/","..X../..X.X/.XXXX/XX.../","X.../XX../.XXX/.X../.XX./"};
		shape[736] = new String[]{"XX..../.XXXX./..X.XX/","..X/.XX/XX./.X./XX./X../","XX.X../.XXXX./....XX/","..X/.XX/.X./.XX/XX./X../","....XX/.XXXX./XX.X../","X../XX./.X./XX./.XX/..X/","..X.XX/.XXXX./XX..../","X../XX./.XX/.X./.XX/..X/"};
		shape[737] = new String[]{"XX.../.XXXX/..X.X/....X/","...X/..XX/.XX./..X./XXX./","X..../X.X../XXXX./...XX/",".XXX/.X../.XX./XX../X.../","...XX/XXXX./X.X../X..../","XXX./..X./.XX./..XX/...X/","....X/..X.X/.XXXX/XX.../","X.../XX../.XX./.X../.XXX/"};
		shape[738] = new String[]{"XX.../.XXXX/..X../.XX../","...X/X.XX/XXX./..X./..X./","..XX./..X../XXXX./...XX/",".X../.X../.XXX/XX.X/X.../","...XX/XXXX./..X../..XX./","..X./..X./XXX./X.XX/...X/",".XX../..X../.XXXX/XX.../","X.../XX.X/.XXX/.X../.X../"};
		shape[739] = new String[]{"XX.../.XXXX/..X../..XX./","...X/..XX/XXX./X.X./..X./",".XX../..X../XXXX./...XX/",".X../.X.X/.XXX/XX../X.../","...XX/XXXX./..X../.XX../","..X./X.X./XXX./..XX/...X/","..XX./..X../.XXXX/XX.../","X.../XX../.XXX/.X.X/.X../"};
		shape[740] = new String[]{"...X./XX.XX/.XXXX/",".X./XX./X../XXX/XX./","XXXX./XX.XX/.X.../",".XX/XXX/..X/.XX/.X./",".X.../XX.XX/XXXX./","XX./XXX/X../XX./.X./",".XXXX/XX.XX/...X./",".X./.XX/..X/XXX/.XX/"};
		shape[741] = new String[]{"....X/XX.XX/.XXXX/",".X./XX./X../XX./XXX/","XXXX./XX.XX/X..../","XXX/.XX/..X/.XX/.X./","X..../XX.XX/XXXX./","XXX/XX./X../XX./.X./",".XXXX/XX.XX/....X/",".X./.XX/..X/.XX/XXX/"};
		shape[742] = new String[]{"XX.XX/.XXXX/...X./","..X/.XX/.X./XXX/.XX/",".X.../XXXX./XX.XX/","XX./XXX/.X./XX./X../","XX.XX/XXXX./.X.../",".XX/XXX/.X./.XX/..X/","...X./.XXXX/XX.XX/","X../XX./.X./XXX/XX./"};
		shape[743] = new String[]{"XX.XX/.XXXX/....X/","..X/.XX/.X./.XX/XXX/","X..../XXXX./XX.XX/","XXX/XX./.X./XX./X../","XX.XX/XXXX./X..../","XXX/.XX/.X./.XX/..X/","....X/.XXXX/XX.XX/","X../XX./.X./XX./XXX/"};
		shape[744] = new String[]{"..XX./XX.X./.XXXX/",".X./XX./X.X/XXX/X../","XXXX./.X.XX/.XX../","..X/XXX/X.X/.XX/.X./",".XX../.X.XX/XXXX./","X../XXX/X.X/XX./.X./",".XXXX/XX.X./..XX./",".X./.XX/X.X/XXX/..X/"};
		shape[745] = new String[]{"...XX/XX.X./.XXXX/",".X./XX./X../XXX/X.X/","XXXX./.X.XX/XX.../","X.X/XXX/..X/.XX/.X./","XX.../.X.XX/XXXX./","X.X/XXX/X../XX./.X./",".XXXX/XX.X./...XX/",".X./.XX/..X/XXX/X.X/"};
		shape[746] = new String[]{"...X./...X./XX.X./.XXXX/",".X../XX../X.../XXXX/X.../","XXXX./.X.XX/.X.../.X.../","...X/XXXX/...X/..XX/..X./",".X.../.X.../.X.XX/XXXX./","X.../XXXX/X.../XX../.X../",".XXXX/XX.X./...X./...X./","..X./..XX/...X/XXXX/...X/"};
		shape[747] = new String[]{"...X./XX.X./.XXXX/...X./","..X./.XX./.X../XXXX/.X../",".X.../XXXX./.X.XX/.X.../","..X./XXXX/..X./.XX./.X../",".X.../.X.XX/XXXX./.X.../",".X../XXXX/.X../.XX./..X./","...X./.XXXX/XX.X./...X./",".X../.XX./..X./XXXX/..X./"};
		shape[748] = new String[]{"...X./XX.X./.XXXX/....X/","..X./.XX./.X../.XXX/XX../","X..../XXXX./.X.XX/.X.../","..XX/XXX./..X./.XX./.X../",".X.../.X.XX/XXXX./X..../","XX../.XXX/.X../.XX./..X./","....X/.XXXX/XX.X./...X./",".X../.XX./..X./XXX./..XX/"};
		shape[749] = new String[]{"XX.X./.XXXX/...XX/","..X/.XX/.X./XXX/XX./","XX.../XXXX./.X.XX/",".XX/XXX/.X./XX./X../",".X.XX/XXXX./XX.../","XX./XXX/.X./.XX/..X/","...XX/.XXXX/XX.X./","X../XX./.X./XXX/.XX/"};
		shape[750] = new String[]{"XX.X./.XXXX/...X./...X./","...X/..XX/..X./XXXX/..X./",".X.../.X.../XXXX./.X.XX/",".X../XXXX/.X../XX../X.../",".X.XX/XXXX./.X.../.X.../","..X./XXXX/..X./..XX/...X/","...X./...X./.XXXX/XX.X./","X.../XX../.X../XXXX/.X../"};
		shape[751] = new String[]{"XX.X./.XXXX/....X/....X/","...X/..XX/..X./..XX/XXX./","X..../X..../XXXX./.X.XX/",".XXX/XX../.X../XX../X.../",".X.XX/XXXX./X..../X..../","XXX./..XX/..X./..XX/...X/","....X/....X/.XXXX/XX.X./","X.../XX../.X../XX../.XXX/"};
		shape[752] = new String[]{"....X/XX..X/.XXXX/...X./","..X./.XX./.X../XX../.XXX/",".X.../XXXX./X..XX/X..../","XXX./..XX/..X./.XX./.X../","X..../X..XX/XXXX./.X.../",".XXX/XX../.X../.XX./..X./","...X./.XXXX/XX..X/....X/",".X../.XX./..X./..XX/XXX./"};
		shape[753] = new String[]{"XX..X/.XXXX/...XX/","..X/.XX/.X./XX./XXX/","XX.../XXXX./X..XX/","XXX/.XX/.X./XX./X../","X..XX/XXXX./XX.../","XXX/XX./.X./.XX/..X/","...XX/.XXXX/XX..X/","X../XX./.X./.XX/XXX/"};
		shape[754] = new String[]{"XX..X/.XXXX/...X./...X./","...X/..XX/..X./XXX./..XX/",".X.../.X.../XXXX./X..XX/","XX../.XXX/.X../XX../X.../","X..XX/XXXX./.X.../.X.../","..XX/XXX./..X./..XX/...X/","...X./...X./.XXXX/XX..X/","X.../XX../.X../.XXX/XX../"};
		shape[755] = new String[]{"XX.../.XXXX/...XX/...X./","...X/..XX/..X./XXX./.XX./",".X.../XX.../XXXX./...XX/",".XX./.XXX/.X../XX../X.../","...XX/XXXX./XX.../.X.../",".XX./XXX./..X./..XX/...X/","...X./...XX/.XXXX/XX.../","X.../XX../.X../.XXX/.XX./"};
		shape[756] = new String[]{"XX.../.XXXX/...XX/....X/","...X/..XX/..X./.XX./XXX./","X..../XX.../XXXX./...XX/",".XXX/.XX./.X../XX../X.../","...XX/XXXX./XX.../X..../","XXX./.XX./..X./..XX/...X/","....X/...XX/.XXXX/XX.../","X.../XX../.X../.XX./.XXX/"};
		shape[757] = new String[]{"XX.../.XXXX/...X./..XX./","...X/..XX/X.X./XXX./..X./",".XX../.X.../XXXX./...XX/",".X../.XXX/.X.X/XX../X.../","...XX/XXXX./.X.../.XX../","..X./XXX./X.X./..XX/...X/","..XX./...X./.XXXX/XX.../","X.../XX../.X.X/.XXX/.X../"};
		shape[758] = new String[]{"XX.../.XXXX/...X./...XX/","...X/..XX/..X./XXX./X.X./","XX.../.X.../XXXX./...XX/",".X.X/.XXX/.X../XX../X.../","...XX/XXXX./.X.../XX.../","X.X./XXX./..X./..XX/...X/","...XX/...X./.XXXX/XX.../","X.../XX../.X../.XXX/.X.X/"};
		shape[759] = new String[]{"XX.../.XXXX/...X./...X./...X./","....X/...XX/...X./XXXX./...X./",".X.../.X.../.X.../XXXX./...XX/",".X.../.XXXX/.X.../XX.../X..../","...XX/XXXX./.X.../.X.../.X.../","...X./XXXX./...X./...XX/....X/","...X./...X./...X./.XXXX/XX.../","X..../XX.../.X.../.XXXX/.X.../"};
		shape[760] = new String[]{"...XX/XX..X/.XXXX/",".X./XX./X../X.X/XXX/","XXXX./X..XX/XX.../","XXX/X.X/..X/.XX/.X./","XX.../X..XX/XXXX./","XXX/X.X/X../XX./.X./",".XXXX/XX..X/...XX/",".X./.XX/..X/X.X/XXX/"};
		shape[761] = new String[]{"....XX/XX..X./.XXXX./",".X./XX./X../X../XXX/..X/",".XXXX./.X..XX/XX..../","X../XXX/..X/..X/.XX/.X./","XX..../.X..XX/.XXXX./","..X/XXX/X../X../XX./.X./",".XXXX./XX..X./....XX/",".X./.XX/..X/..X/XXX/X../"};
		shape[762] = new String[]{"....X/....X/XX..X/.XXXX/",".X../XX../X.../X.../XXXX/","XXXX./X..XX/X..../X..../","XXXX/...X/...X/..XX/..X./","X..../X..../X..XX/XXXX./","XXXX/X.../X.../XX../.X../",".XXXX/XX..X/....X/....X/","..X./..XX/...X/...X/XXXX/"};
		shape[763] = new String[]{"....X/XX..X/.XXXX/....X/","..X./.XX./.X../.X../XXXX/","X..../XXXX./X..XX/X..../","XXXX/..X./..X./.XX./.X../","X..../X..XX/XXXX./X..../","XXXX/.X../.X../.XX./..X./","....X/.XXXX/XX..X/....X/",".X../.XX./..X./..X./XXXX/"};
		shape[764] = new String[]{"XX..X/.XXXX/....X/....X/","...X/..XX/..X./..X./XXXX/","X..../X..../XXXX./X..XX/","XXXX/.X../.X../XX../X.../","X..XX/XXXX./X..../X..../","XXXX/..X./..X./..XX/...X/","....X/....X/.XXXX/XX..X/","X.../XX../.X../.X../XXXX/"};
		shape[765] = new String[]{"XX.../.XXXX/....X/...XX/","...X/..XX/..X./X.X./XXX./","XX.../X..../XXXX./...XX/",".XXX/.X.X/.X../XX../X.../","...XX/XXXX./X..../XX.../","XXX./X.X./..X./..XX/...X/","...XX/....X/.XXXX/XX.../","X.../XX../.X../.X.X/.XXX/"};
		shape[766] = new String[]{"XX..../.XXXX./....X./....XX/","...X/..XX/..X./..X./XXX./X.../","XX..../.X..../.XXXX./....XX/","...X/.XXX/.X../.X../XX../X.../","....XX/.XXXX./.X..../XX..../","X.../XXX./..X./..X./..XX/...X/","....XX/....X./.XXXX./XX..../","X.../XX../.X../.X../.XXX/...X/"};
		shape[767] = new String[]{"XX.../.XXXX/....X/....X/....X/","....X/...XX/...X./...X./XXXX./","X..../X..../X..../XXXX./...XX/",".XXXX/.X.../.X.../XX.../X..../","...XX/XXXX./X..../X..../X..../","XXXX./...X./...X./...XX/....X/","....X/....X/....X/.XXXX/XX.../","X..../XX.../.X.../.X.../.XXXX/"};
		shape[768] = new String[]{"XXX.../..XX../..XXXX/","..X/..X/XXX/XX./X../X../","XXXX../..XX../...XXX/","..X/..X/.XX/XXX/X../X../","...XXX/..XX../XXXX../","X../X../XX./XXX/..X/..X/","..XXXX/..XX../XXX.../","X../X../XXX/.XX/..X/..X/"};
		shape[769] = new String[]{"X..../XX.../.XX../.XXXX/","..XX/XXX./XX../X.../X.../","XXXX./..XX./...XX/....X/","...X/...X/..XX/.XXX/XX../","....X/...XX/..XX./XXXX./","X.../X.../XX../XXX./..XX/",".XXXX/.XX../XX.../X..../","XX../.XXX/..XX/...X/...X/"};
		shape[770] = new String[]{"XXX../.XX../.XXXX/","..X/XXX/XXX/X../X../","XXXX./..XX./..XXX/","..X/..X/XXX/XXX/X../","..XXX/..XX./XXXX./","X../X../XXX/XXX/..X/",".XXXX/.XX../XXX../","X../XXX/XXX/..X/..X/"};
		shape[771] = new String[]{".X.../XX.../.XX../.XXXX/","..X./XXXX/XX../X.../X.../","XXXX./..XX./...XX/...X./","...X/...X/..XX/XXXX/.X../","...X./...XX/..XX./XXXX./","X.../X.../XX../XXXX/..X./",".XXXX/.XX../XX.../.X.../",".X../XXXX/..XX/...X/...X/"};
		shape[772] = new String[]{"XX.../.XXX./.XXXX/","..X/XXX/XX./XX./X../","XXXX./.XXX./...XX/","..X/.XX/.XX/XXX/X../","...XX/.XXX./XXXX./","X../XX./XX./XXX/..X/",".XXXX/.XXX./XX.../","X../XXX/.XX/.XX/..X/"};
		shape[773] = new String[]{"XX.../.XX../.XXXX/.X.../","...X/XXXX/.XX./.X../.X../","...X./XXXX./..XX./...XX/","..X./..X./.XX./XXXX/X.../","...XX/..XX./XXXX./...X./",".X../.X../.XX./XXXX/...X/",".X.../.XXXX/.XX../XX.../","X.../XXXX/.XX./..X./..X./"};
		shape[774] = new String[]{"XX.../.XX../.XXXX/..X../","...X/.XXX/XXX./.X../.X../","..X../XXXX./..XX./...XX/","..X./..X./.XXX/XXX./X.../","...XX/..XX./XXXX./..X../",".X../.X../XXX./.XXX/...X/","..X../.XXXX/.XX../XX.../","X.../XXX./.XXX/..X./..X./"};
		shape[775] = new String[]{"XX.../.XX../.XXXX/...X./","...X/.XXX/.XX./XX../.X../",".X.../XXXX./..XX./...XX/","..X./..XX/.XX./XXX./X.../","...XX/..XX./XXXX./.X.../",".X../XX../.XX./.XXX/...X/","...X./.XXXX/.XX../XX.../","X.../XXX./.XX./..XX/..X./"};
		shape[776] = new String[]{"XX.../.XX.X/.XXXX/","..X/XXX/XX./X../XX./","XXXX./X.XX./...XX/",".XX/..X/.XX/XXX/X../","...XX/X.XX./XXXX./","XX./X../XX./XXX/..X/",".XXXX/.XX.X/XX.../","X../XXX/.XX/..X/.XX/"};
		shape[777] = new String[]{"XX.../.XX../.XXXX/....X/","...X/.XXX/.XX./.X../XX../","X..../XXXX./..XX./...XX/","..XX/..X./.XX./XXX./X.../","...XX/..XX./XXXX./X..../","XX../.X../.XX./.XXX/...X/","....X/.XXXX/.XX../XX.../","X.../XXX./.XX./..X./..XX/"};
		shape[778] = new String[]{"X.../XX../XX../XXXX/","XXXX/XXX./X.../X.../","XXXX/..XX/..XX/...X/","...X/...X/.XXX/XXXX/","...X/..XX/..XX/XXXX/","X.../X.../XXX./XXXX/","XXXX/XX../XX../X.../","XXXX/.XXX/...X/...X/"};
		shape[779] = new String[]{"XXX./XX../XXXX/","XXX/XXX/X.X/X../","XXXX/..XX/.XXX/","..X/X.X/XXX/XXX/",".XXX/..XX/XXXX/","X../X.X/XXX/XXX/","XXXX/XX../XXX./","XXX/XXX/X.X/..X/"};
		shape[780] = new String[]{".X../XX../XX../XXXX/","XXX./XXXX/X.../X.../","XXXX/..XX/..XX/..X./","...X/...X/XXXX/.XXX/","..X./..XX/..XX/XXXX/","X.../X.../XXXX/XXX./","XXXX/XX../XX../.X../",".XXX/XXXX/...X/...X/"};
		shape[781] = new String[]{"XX../XXX./XXXX/","XXX/XXX/XX./X../","XXXX/.XXX/..XX/","..X/.XX/XXX/XXX/","..XX/.XXX/XXXX/","X../XX./XXX/XXX/","XXXX/XXX./XX../","XXX/XXX/.XX/..X/"};
		shape[782] = new String[]{"XX../XX../XXXX/X.../","XXXX/.XXX/.X../.X../","...X/XXXX/..XX/..XX/","..X./..X./XXX./XXXX/","..XX/..XX/XXXX/...X/",".X../.X../.XXX/XXXX/","X.../XXXX/XX../XX../","XXXX/XXX./..X./..X./"};
		shape[783] = new String[]{"XX../XX../XXXX/.X../",".XXX/XXXX/.X../.X../","..X./XXXX/..XX/..XX/","..X./..X./XXXX/XXX./","..XX/..XX/XXXX/..X./",".X../.X../XXXX/.XXX/",".X../XXXX/XX../XX../","XXX./XXXX/..X./..X./"};
		shape[784] = new String[]{"XX../XX../XXXX/..X./",".XXX/.XXX/XX../.X../",".X../XXXX/..XX/..XX/","..X./..XX/XXX./XXX./","..XX/..XX/XXXX/.X../",".X../XX../.XXX/.XXX/","..X./XXXX/XX../XX../","XXX./XXX./..XX/..X./"};
		shape[785] = new String[]{"XX../XX.X/XXXX/","XXX/XXX/X../XX./","XXXX/X.XX/..XX/",".XX/..X/XXX/XXX/","..XX/X.XX/XXXX/","XX./X../XXX/XXX/","XXXX/XX.X/XX../","XXX/XXX/..X/.XX/"};
		shape[786] = new String[]{"XX../XX../XXXX/...X/",".XXX/.XXX/.X../XX../","X.../XXXX/..XX/..XX/","..XX/..X./XXX./XXX./","..XX/..XX/XXXX/X.../","XX../.X../.XXX/.XXX/","...X/XXXX/XX../XX../","XXX./XXX./..X./..XX/"};
		shape[787] = new String[]{"XX.../.X.../.XX../.XXXX/","...X/XXXX/XX../X.../X.../","XXXX./..XX./...X./...XX/","...X/...X/..XX/XXXX/X.../","...XX/...X./..XX./XXXX./","X.../X.../XX../XXXX/...X/",".XXXX/.XX../.X.../XX.../","X.../XXXX/..XX/...X/...X/"};
		shape[788] = new String[]{"XX../X.../XX../XXXX/","XXXX/XX.X/X.../X.../","XXXX/..XX/...X/..XX/","...X/...X/X.XX/XXXX/","..XX/...X/..XX/XXXX/","X.../X.../XX.X/XXXX/","XXXX/XX../X.../XX../","XXXX/X.XX/...X/...X/"};
		shape[789] = new String[]{"X.../XXXX/XXXX/","XXX/XX./XX./XX./","XXXX/XXXX/...X/",".XX/.XX/.XX/XXX/","...X/XXXX/XXXX/","XX./XX./XX./XXX/","XXXX/XXXX/X.../","XXX/.XX/.XX/.XX/"};
		shape[790] = new String[]{"X.X./XXX./XXXX/","XXX/XX./XXX/X../","XXXX/.XXX/.X.X/","..X/XXX/.XX/XXX/",".X.X/.XXX/XXXX/","X../XXX/XX./XXX/","XXXX/XXX./X.X./","XXX/.XX/XXX/..X/"};
		shape[791] = new String[]{"X.../XXX./XXXX/X.../","XXXX/.XX./.XX./.X../","...X/XXXX/.XXX/...X/","..X./.XX./.XX./XXXX/","...X/.XXX/XXXX/...X/",".X../.XX./.XX./XXXX/","X.../XXXX/XXX./X.../","XXXX/.XX./.XX./..X./"};
		shape[792] = new String[]{"X.../XXX./XXXX/.X../",".XXX/XXX./.XX./.X../","..X./XXXX/.XXX/...X/","..X./.XX./.XXX/XXX./","...X/.XXX/XXXX/..X./",".X../.XX./XXX./.XXX/",".X../XXXX/XXX./X.../","XXX./.XXX/.XX./..X./"};
		shape[793] = new String[]{"X.../XXX./XXXX/..X./",".XXX/.XX./XXX./.X../",".X../XXXX/.XXX/...X/","..X./.XXX/.XX./XXX./","...X/.XXX/XXXX/.X../",".X../XXX./.XX./.XXX/","..X./XXXX/XXX./X.../","XXX./.XX./.XXX/..X./"};
		shape[794] = new String[]{"X.../XXX./XXXX/...X/",".XXX/.XX./.XX./XX../","X.../XXXX/.XXX/...X/","..XX/.XX./.XX./XXX./","...X/.XXX/XXXX/X.../","XX../.XX./.XX./.XXX/","...X/XXXX/XXX./X.../","XXX./.XX./.XX./..XX/"};
		shape[795] = new String[]{"X.../XX../XXXX/XX../","XXXX/XXX./.X../.X../","..XX/XXXX/..XX/...X/","..X./..X./.XXX/XXXX/","...X/..XX/XXXX/..XX/",".X../.X../XXX./XXXX/","XX../XXXX/XX../X.../","XXXX/.XXX/..X./..X./"};
		shape[796] = new String[]{"X.../XX../XXXX/X.X./","XXXX/.XX./XX../.X../",".X.X/XXXX/..XX/...X/","..X./..XX/.XX./XXXX/","...X/..XX/XXXX/.X.X/",".X../XX../.XX./XXXX/","X.X./XXXX/XX../X.../","XXXX/.XX./..XX/..X./"};
		shape[797] = new String[]{"X.../XX.X/XXXX/X.../","XXXX/.XX./.X../.XX./","...X/XXXX/X.XX/...X/",".XX./..X./.XX./XXXX/","...X/X.XX/XXXX/...X/",".XX./.X../.XX./XXXX/","X.../XXXX/XX.X/X.../","XXXX/.XX./..X./.XX./"};
		shape[798] = new String[]{"X.../XX../XXXX/X..X/","XXXX/.XX./.X../XX../","X..X/XXXX/..XX/...X/","..XX/..X./.XX./XXXX/","...X/..XX/XXXX/X..X/","XX../.X../.XX./XXXX/","X..X/XXXX/XX../X.../","XXXX/.XX./..X./..XX/"};
		shape[799] = new String[]{"X.../XX../XXXX/.XX./",".XXX/XXX./XX../.X../",".XX./XXXX/..XX/...X/","..X./..XX/.XXX/XXX./","...X/..XX/XXXX/.XX./",".X../XX../XXX./.XXX/",".XX./XXXX/XX../X.../","XXX./.XXX/..XX/..X./"};
		shape[800] = new String[]{"X.../XX.X/XXXX/.X../",".XXX/XXX./.X../.XX./","..X./XXXX/X.XX/...X/",".XX./..X./.XXX/XXX./","...X/X.XX/XXXX/..X./",".XX./.X../XXX./.XXX/",".X../XXXX/XX.X/X.../","XXX./.XXX/..X./.XX./"};
		shape[801] = new String[]{"X.../XX../XXXX/.X.X/",".XXX/XXX./.X../XX../","X.X./XXXX/..XX/...X/","..XX/..X./.XXX/XXX./","...X/..XX/XXXX/X.X./","XX../.X../XXX./.XXX/",".X.X/XXXX/XX../X.../","XXX./.XXX/..X./..XX/"};
		shape[802] = new String[]{"X.../XX.X/XXXX/..X./",".XXX/.XX./XX../.XX./",".X../XXXX/X.XX/...X/",".XX./..XX/.XX./XXX./","...X/X.XX/XXXX/.X../",".XX./XX../.XX./.XXX/","..X./XXXX/XX.X/X.../","XXX./.XX./..XX/.XX./"};
		shape[803] = new String[]{"X.../XX../XXXX/..XX/",".XXX/.XX./XX../XX../","XX../XXXX/..XX/...X/","..XX/..XX/.XX./XXX./","...X/..XX/XXXX/XX../","XX../XX../.XX./.XXX/","..XX/XXXX/XX../X.../","XXX./.XX./..XX/..XX/"};
		shape[804] = new String[]{"X.../XX../XXXX/..X./..X./","..XXX/..XX./XXX../..X../",".X../.X../XXXX/..XX/...X/","..X../..XXX/.XX../XXX../","...X/..XX/XXXX/.X../.X../","..X../XXX../..XX./..XXX/","..X./..X./XXXX/XX../X.../","XXX../.XX../..XXX/..X../"};
		shape[805] = new String[]{"X..X/XX.X/XXXX/","XXX/XX./X../XXX/","XXXX/X.XX/X..X/","XXX/..X/.XX/XXX/","X..X/X.XX/XXXX/","XXX/X../XX./XXX/","XXXX/XX.X/X..X/","XXX/.XX/..X/XXX/"};
		shape[806] = new String[]{"X.../XX.X/XXXX/...X/",".XXX/.XX./.X../XXX./","X.../XXXX/X.XX/...X/",".XXX/..X./.XX./XXX./","...X/X.XX/XXXX/X.../","XXX./.X../.XX./.XXX/","...X/XXXX/XX.X/X.../","XXX./.XX./..X./.XXX/"};
		shape[807] = new String[]{"X.../XX../XXXX/...X/...X/","..XXX/..XX./..X../XXX../","X.../X.../XXXX/..XX/...X/","..XXX/..X../.XX../XXX../","...X/..XX/XXXX/X.../X.../","XXX../..X../..XX./..XXX/","...X/...X/XXXX/XX../X.../","XXX../.XX../..X../..XXX/"};
		shape[808] = new String[]{".XX./XXX./XXXX/","XX./XXX/XXX/X../","XXXX/.XXX/.XX./","..X/XXX/XXX/.XX/",".XX./.XXX/XXXX/","X../XXX/XXX/XX./","XXXX/XXX./.XX./",".XX/XXX/XXX/..X/"};
		shape[809] = new String[]{".X../XXXX/XXXX/","XX./XXX/XX./XX./","XXXX/XXXX/..X./",".XX/.XX/XXX/.XX/","..X./XXXX/XXXX/","XX./XX./XXX/XX./","XXXX/XXXX/.X../",".XX/XXX/.XX/.XX/"};
		shape[810] = new String[]{".X../XXX./XXXX/X.../","XXX./.XXX/.XX./.X../","...X/XXXX/.XXX/..X./","..X./.XX./XXX./.XXX/","..X./.XXX/XXXX/...X/",".X../.XX./.XXX/XXX./","X.../XXXX/XXX./.X../",".XXX/XXX./.XX./..X./"};
		shape[811] = new String[]{".X../XXX./XXXX/.X../",".XX./XXXX/.XX./.X../","..X./XXXX/.XXX/..X./","..X./.XX./XXXX/.XX./","..X./.XXX/XXXX/..X./",".X../.XX./XXXX/.XX./",".X../XXXX/XXX./.X../",".XX./XXXX/.XX./..X./"};
		shape[812] = new String[]{".X../XXX./XXXX/..X./",".XX./.XXX/XXX./.X../",".X../XXXX/.XXX/..X./","..X./.XXX/XXX./.XX./","..X./.XXX/XXXX/.X../",".X../XXX./.XXX/.XX./","..X./XXXX/XXX./.X../",".XX./XXX./.XXX/..X./"};
		shape[813] = new String[]{".X../XXX./XXXX/...X/",".XX./.XXX/.XX./XX../","X.../XXXX/.XXX/..X./","..XX/.XX./XXX./.XX./","..X./.XXX/XXXX/X.../","XX../.XX./.XXX/.XX./","...X/XXXX/XXX./.X../",".XX./XXX./.XX./..XX/"};
		shape[814] = new String[]{"..XX/XXX./XXXX/","XX./XX./XXX/X.X/","XXXX/.XXX/XX../","X.X/XXX/.XX/.XX/","XX../.XXX/XXXX/","X.X/XXX/XX./XX./","XXXX/XXX./..XX/",".XX/.XX/XXX/X.X/"};
		shape[815] = new String[]{"..X./XXX./XXXX/X.../","XXX./.XX./.XXX/.X../","...X/XXXX/.XXX/.X../","..X./XXX./.XX./.XXX/",".X../.XXX/XXXX/...X/",".X../.XXX/.XX./XXX./","X.../XXXX/XXX./..X./",".XXX/.XX./XXX./..X./"};
		shape[816] = new String[]{"..X./XXX./XXXX/.X../",".XX./XXX./.XXX/.X../","..X./XXXX/.XXX/.X../","..X./XXX./.XXX/.XX./",".X../.XXX/XXXX/..X./",".X../.XXX/XXX./.XX./",".X../XXXX/XXX./..X./",".XX./.XXX/XXX./..X./"};
		shape[817] = new String[]{"..X./XXX./XXXX/..X./",".XX./.XX./XXXX/.X../",".X../XXXX/.XXX/.X../","..X./XXXX/.XX./.XX./",".X../.XXX/XXXX/.X../",".X../XXXX/.XX./.XX./","..X./XXXX/XXX./..X./",".XX./.XX./XXXX/..X./"};
		shape[818] = new String[]{"..X./XXX./XXXX/...X/",".XX./.XX./.XXX/XX../","X.../XXXX/.XXX/.X../","..XX/XXX./.XX./.XX./",".X../.XXX/XXXX/X.../","XX../.XXX/.XX./.XX./","...X/XXXX/XXX./..X./",".XX./.XX./XXX./..XX/"};
		shape[819] = new String[]{"XXX./XXXX/XX../","XXX/XXX/.XX/.X./","..XX/XXXX/.XXX/",".X./XX./XXX/XXX/",".XXX/XXXX/..XX/",".X./.XX/XXX/XXX/","XX../XXXX/XXX./","XXX/XXX/XX./.X./"};
		shape[820] = new String[]{"XXX./XXXX/X.X./","XXX/.XX/XXX/.X./",".X.X/XXXX/.XXX/",".X./XXX/XX./XXX/",".XXX/XXXX/.X.X/",".X./XXX/.XX/XXX/","X.X./XXXX/XXX./","XXX/XX./XXX/.X./"};
		shape[821] = new String[]{"XXX./XXXX/X..X/","XXX/.XX/.XX/XX./","X..X/XXXX/.XXX/",".XX/XX./XX./XXX/",".XXX/XXXX/X..X/","XX./.XX/.XX/XXX/","X..X/XXXX/XXX./","XXX/XX./XX./.XX/"};
		shape[822] = new String[]{"XXX./XXXX/.XX./",".XX/XXX/XXX/.X./",".XX./XXXX/.XXX/",".X./XXX/XXX/XX./",".XXX/XXXX/.XX./",".X./XXX/XXX/.XX/",".XX./XXXX/XXX./","XX./XXX/XXX/.X./"};
		shape[823] = new String[]{"XXX./XXXX/.X.X/",".XX/XXX/.XX/XX./","X.X./XXXX/.XXX/",".XX/XX./XXX/XX./",".XXX/XXXX/X.X./","XX./.XX/XXX/.XX/",".X.X/XXXX/XXX./","XX./XXX/XX./.XX/"};
		shape[824] = new String[]{"XXX./XXXX/.X../.X../","..XX/XXXX/..XX/..X./","..X./..X./XXXX/.XXX/",".X../XX../XXXX/XX../",".XXX/XXXX/..X./..X./","..X./..XX/XXXX/..XX/",".X../.X../XXXX/XXX./","XX../XXXX/XX../.X../"};
		shape[825] = new String[]{"XXX./XXXX/..XX/",".XX/.XX/XXX/XX./","XX../XXXX/.XXX/",".XX/XXX/XX./XX./",".XXX/XXXX/XX../","XX./XXX/.XX/.XX/","..XX/XXXX/XXX./","XX./XX./XXX/.XX/"};
		shape[826] = new String[]{"XXX./XXXX/...X/...X/","..XX/..XX/..XX/XXX./","X.../X.../XXXX/.XXX/",".XXX/XX../XX../XX../",".XXX/XXXX/X.../X.../","XXX./..XX/..XX/..XX/","...X/...X/XXXX/XXX./","XX../XX../XX../.XXX/"};
		shape[827] = new String[]{".X../.XX./XX../XXXX/","XX../XXXX/X.X./X.../","XXXX/..XX/.XX./..X./","...X/.X.X/XXXX/..XX/","..X./.XX./..XX/XXXX/","X.../X.X./XXXX/XX../","XXXX/XX../.XX./.X../","..XX/XXXX/.X.X/...X/"};
		shape[828] = new String[]{".XXX/XX../XXXX/","XX./XXX/X.X/X.X/","XXXX/..XX/XXX./","X.X/X.X/XXX/.XX/","XXX./..XX/XXXX/","X.X/X.X/XXX/XX./","XXXX/XX../.XXX/",".XX/XXX/X.X/X.X/"};
		shape[829] = new String[]{"..X./.XX./XX../XXXX/","XX../XXX./X.XX/X.../","XXXX/..XX/.XX./.X../","...X/XX.X/.XXX/..XX/",".X../.XX./..XX/XXXX/","X.../X.XX/XXX./XX../","XXXX/XX../.XX./..X./","..XX/.XXX/XX.X/...X/"};
		shape[830] = new String[]{".XX./XX../XXXX/X.../","XXX./.XXX/.X.X/.X../","...X/XXXX/..XX/.XX./","..X./X.X./XXX./.XXX/",".XX./..XX/XXXX/...X/",".X../.X.X/.XXX/XXX./","X.../XXXX/XX../.XX./",".XXX/XXX./X.X./..X./"};
		shape[831] = new String[]{".XX./XX../XXXX/.X../",".XX./XXXX/.X.X/.X../","..X./XXXX/..XX/.XX./","..X./X.X./XXXX/.XX./",".XX./..XX/XXXX/..X./",".X../.X.X/XXXX/.XX./",".X../XXXX/XX../.XX./",".XX./XXXX/X.X./..X./"};
		shape[832] = new String[]{".XX./XX../XXXX/..X./",".XX./.XXX/XX.X/.X../",".X../XXXX/..XX/.XX./","..X./X.XX/XXX./.XX./",".XX./..XX/XXXX/.X../",".X../XX.X/.XXX/.XX./","..X./XXXX/XX../.XX./",".XX./XXX./X.XX/..X./"};
		shape[833] = new String[]{".XX./XX.X/XXXX/","XX./XXX/X.X/XX./","XXXX/X.XX/.XX./",".XX/X.X/XXX/.XX/",".XX./X.XX/XXXX/","XX./X.X/XXX/XX./","XXXX/XX.X/.XX./",".XX/XXX/X.X/.XX/"};
		shape[834] = new String[]{".XX./XX../XXXX/...X/",".XX./.XXX/.X.X/XX../","X.../XXXX/..XX/.XX./","..XX/X.X./XXX./.XX./",".XX./..XX/XXXX/X.../","XX../.X.X/.XXX/.XX./","...X/XXXX/XX../.XX./",".XX./XXX./X.X./..XX/"};
		shape[835] = new String[]{"XX../.X../XX../XXXX/","XX.X/XXXX/X.../X.../","XXXX/..XX/..X./..XX/","...X/...X/XXXX/X.XX/","..XX/..X./..XX/XXXX/","X.../X.../XXXX/XX.X/","XXXX/XX../.X../XX../","X.XX/XXXX/...X/...X/"};
		shape[836] = new String[]{".XX./.X../XX../XXXX/","XX../XXXX/X..X/X.../","XXXX/..XX/..X./.XX./","...X/X..X/XXXX/..XX/",".XX./..X./..XX/XXXX/","X.../X..X/XXXX/XX../","XXXX/XX../.X../.XX./","..XX/XXXX/X..X/...X/"};
		shape[837] = new String[]{".X../.X../XX.X/XXXX/","XX../XXXX/X.../XX../","XXXX/X.XX/..X./..X./","..XX/...X/XXXX/..XX/","..X./..X./X.XX/XXXX/","XX../X.../XXXX/XX../","XXXX/XX.X/.X../.X../","..XX/XXXX/...X/..XX/"};
		shape[838] = new String[]{".X../.X../XX../XXXX/...X/",".XX../.XXXX/.X.../XX.../","X.../XXXX/..XX/..X./..X./","...XX/...X./XXXX./..XX./","..X./..X./..XX/XXXX/X.../","XX.../.X.../.XXXX/.XX../","...X/XXXX/XX../.X../.X../","..XX./XXXX./...X./...XX/"};
		shape[839] = new String[]{".X../XX../XXXX/X.X./","XXX./.XXX/XX../.X../",".X.X/XXXX/..XX/..X./","..X./..XX/XXX./.XXX/","..X./..XX/XXXX/.X.X/",".X../XX../.XXX/XXX./","X.X./XXXX/XX../.X../",".XXX/XXX./..XX/..X./"};
		shape[840] = new String[]{".X../XX.X/XXXX/X.../","XXX./.XXX/.X../.XX./","...X/XXXX/X.XX/..X./",".XX./..X./XXX./.XXX/","..X./X.XX/XXXX/...X/",".XX./.X../.XXX/XXX./","X.../XXXX/XX.X/.X../",".XXX/XXX./..X./.XX./"};
		shape[841] = new String[]{".X../XX../XXXX/X..X/","XXX./.XXX/.X../XX../","X..X/XXXX/..XX/..X./","..XX/..X./XXX./.XXX/","..X./..XX/XXXX/X..X/","XX../.X../.XXX/XXX./","X..X/XXXX/XX../.X../",".XXX/XXX./..X./..XX/"};
		shape[842] = new String[]{".X../XX../XXXX/.XX./",".XX./XXXX/XX../.X../",".XX./XXXX/..XX/..X./","..X./..XX/XXXX/.XX./"};
		shape[843] = new String[]{".X../XX.X/XXXX/.X../",".XX./XXXX/.X../.XX./","..X./XXXX/X.XX/..X./",".XX./..X./XXXX/.XX./","..X./X.XX/XXXX/..X./",".XX./.X../XXXX/.XX./",".X../XXXX/XX.X/.X../",".XX./XXXX/..X./.XX./"};
		shape[844] = new String[]{".X../XX../XXXX/.X.X/",".XX./XXXX/.X../XX../","X.X./XXXX/..XX/..X./","..XX/..X./XXXX/.XX./","..X./..XX/XXXX/X.X./","XX../.X../XXXX/.XX./",".X.X/XXXX/XX../.X../",".XX./XXXX/..X./..XX/"};
		shape[845] = new String[]{".X../XX.X/XXXX/..X./",".XX./.XXX/XX../.XX./",".X../XXXX/X.XX/..X./",".XX./..XX/XXX./.XX./","..X./X.XX/XXXX/.X../",".XX./XX../.XXX/.XX./","..X./XXXX/XX.X/.X../",".XX./XXX./..XX/.XX./"};
		shape[846] = new String[]{".X../XX../XXXX/..XX/",".XX./.XXX/XX../XX../","XX../XXXX/..XX/..X./","..XX/..XX/XXX./.XX./","..X./..XX/XXXX/XX../","XX../XX../.XXX/.XX./","..XX/XXXX/XX../.X../",".XX./XXX./..XX/..XX/"};
		shape[847] = new String[]{".X../XX../XXXX/..X./..X./","..XX./..XXX/XXX../..X../",".X../.X../XXXX/..XX/..X./","..X../..XXX/XXX../.XX../","..X./..XX/XXXX/.X../.X../","..X../XXX../..XXX/..XX./","..X./..X./XXXX/XX../.X../",".XX../XXX../..XXX/..X../"};
		shape[848] = new String[]{".X.X/XX.X/XXXX/","XX./XXX/X../XXX/","XXXX/X.XX/X.X./","XXX/..X/XXX/.XX/","X.X./X.XX/XXXX/","XXX/X../XXX/XX./","XXXX/XX.X/.X.X/",".XX/XXX/..X/XXX/"};
		shape[849] = new String[]{".X../XX.X/XXXX/...X/",".XX./.XXX/.X../XXX./","X.../XXXX/X.XX/..X./",".XXX/..X./XXX./.XX./","..X./X.XX/XXXX/X.../","XXX./.X../.XXX/.XX./","...X/XXXX/XX.X/.X../",".XX./XXX./..X./.XXX/"};
		shape[850] = new String[]{".X../XX../XXXX/...X/...X/","..XX./..XXX/..X../XXX../","X.../X.../XXXX/..XX/..X./","..XXX/..X../XXX../.XX../","..X./..XX/XXXX/X.../X.../","XXX../..X../..XXX/..XX./","...X/...X/XXXX/XX../.X../",".XX../XXX../..X../..XXX/"};
		shape[851] = new String[]{"XX.X/XXXX/XX../","XXX/XXX/.X./.XX/","..XX/XXXX/X.XX/","XX./.X./XXX/XXX/","X.XX/XXXX/..XX/",".XX/.X./XXX/XXX/","XX../XXXX/XX.X/","XXX/XXX/.X./XX./"};
		shape[852] = new String[]{"XX.X/XXXX/X.X./","XXX/.XX/XX./.XX/",".X.X/XXXX/X.XX/","XX./.XX/XX./XXX/","X.XX/XXXX/.X.X/",".XX/XX./.XX/XXX/","X.X./XXXX/XX.X/","XXX/XX./.XX/XX./"};
		shape[853] = new String[]{"XX../XXXX/X.XX/","XXX/.XX/XX./XX./","XX.X/XXXX/..XX/",".XX/.XX/XX./XXX/","..XX/XXXX/XX.X/","XX./XX./.XX/XXX/","X.XX/XXXX/XX../","XXX/XX./.XX/.XX/"};
		shape[854] = new String[]{"XX../XXXX/X.X./..X./",".XXX/..XX/XXX./..X./",".X../.X.X/XXXX/..XX/",".X../.XXX/XX../XXX./","..XX/XXXX/.X.X/.X../","..X./XXX./..XX/.XXX/","..X./X.X./XXXX/XX../","XXX./XX../.XXX/.X../"};
		shape[855] = new String[]{"...X/XX.X/XXXX/X.../","XXX./.XX./.X../.XXX/","...X/XXXX/X.XX/X.../","XXX./..X./.XX./.XXX/","X.../X.XX/XXXX/...X/",".XXX/.X../.XX./XXX./","X.../XXXX/XX.X/...X/",".XXX/.XX./..X./XXX./"};
		shape[856] = new String[]{"XX.X/XXXX/X..X/","XXX/.XX/.X./XXX/","X..X/XXXX/X.XX/","XXX/.X./XX./XXX/","X.XX/XXXX/X..X/","XXX/.X./.XX/XXX/","X..X/XXXX/XX.X/","XXX/XX./.X./XXX/"};
		shape[857] = new String[]{"XX../XXXX/X..X/...X/",".XXX/..XX/..X./XXX./","X.../X..X/XXXX/..XX/",".XXX/.X../XX../XXX./","..XX/XXXX/X..X/X.../","XXX./..X./..XX/.XXX/","...X/X..X/XXXX/XX../","XXX./XX../.X../.XXX/"};
		shape[858] = new String[]{"XX.X/XXXX/.XX./",".XX/XXX/XX./.XX/",".XX./XXXX/X.XX/","XX./.XX/XXX/XX./","X.XX/XXXX/.XX./",".XX/XX./XXX/.XX/",".XX./XXXX/XX.X/","XX./XXX/.XX/XX./"};
		shape[859] = new String[]{"XX../XXXX/.XX./.X../","..XX/XXXX/.XX./..X./","..X./.XX./XXXX/..XX/",".X../.XX./XXXX/XX../"};
		shape[860] = new String[]{"XX../XXXX/.XX./..X./","..XX/.XXX/XXX./..X./",".X../.XX./XXXX/..XX/",".X../.XXX/XXX./XX../","..XX/XXXX/.XX./.X../","..X./XXX./.XXX/..XX/","..X./.XX./XXXX/XX../","XX../XXX./.XXX/.X../"};
		shape[861] = new String[]{"...X/XX.X/XXXX/.X../",".XX./XXX./.X../.XXX/","..X./XXXX/X.XX/X.../","XXX./..X./.XXX/.XX./","X.../X.XX/XXXX/..X./",".XXX/.X../XXX./.XX./",".X../XXXX/XX.X/...X/",".XX./.XXX/..X./XXX./"};
		shape[862] = new String[]{"XX.X/XXXX/.X.X/",".XX/XXX/.X./XXX/","X.X./XXXX/X.XX/","XXX/.X./XXX/XX./","X.XX/XXXX/X.X./","XXX/.X./XXX/.XX/",".X.X/XXXX/XX.X/","XX./XXX/.X./XXX/"};
		shape[863] = new String[]{"XX.X/XXXX/.X../.X../","..XX/XXXX/..X./..XX/","..X./..X./XXXX/X.XX/","XX../.X../XXXX/XX../","X.XX/XXXX/..X./..X./","..XX/..X./XXXX/..XX/",".X../.X../XXXX/XX.X/","XX../XXXX/.X../XX../"};
		shape[864] = new String[]{"XX../XXXX/.X.X/.X../","..XX/XXXX/..X./.XX./","..X./X.X./XXXX/..XX/",".XX./.X../XXXX/XX../","..XX/XXXX/X.X./..X./",".XX./..X./XXXX/..XX/",".X../.X.X/XXXX/XX../","XX../XXXX/.X../.XX./"};
		shape[865] = new String[]{"XX../XXXX/.X.X/...X/","..XX/.XXX/..X./XXX./","X.../X.X./XXXX/..XX/",".XXX/.X../XXX./XX../","..XX/XXXX/X.X./X.../","XXX./..X./.XXX/..XX/","...X/.X.X/XXXX/XX../","XX../XXX./.X../.XXX/"};
		shape[866] = new String[]{"...X/XX.X/XXXX/..X./",".XX./.XX./XX../.XXX/",".X../XXXX/X.XX/X.../","XXX./..XX/.XX./.XX./","X.../X.XX/XXXX/.X../",".XXX/XX../.XX./.XX./","..X./XXXX/XX.X/...X/",".XX./.XX./..XX/XXX./"};
		shape[867] = new String[]{"XX.X/XXXX/..X./..X./","..XX/..XX/XXX./..XX/",".X../.X../XXXX/X.XX/","XX../.XXX/XX../XX../","X.XX/XXXX/.X../.X../","..XX/XXX./..XX/..XX/","..X./..X./XXXX/XX.X/","XX../XX../.XXX/XX../"};
		shape[868] = new String[]{"XX../XXXX/..X./.XX./","..XX/X.XX/XXX./..X./",".XX./.X../XXXX/..XX/",".X../.XXX/XX.X/XX../","..XX/XXXX/.X../.XX./","..X./XXX./X.XX/..XX/",".XX./..X./XXXX/XX../","XX../XX.X/.XXX/.X../"};
		shape[869] = new String[]{"XX../XXXX/..X./..XX/","..XX/..XX/XXX./X.X./","XX../.X../XXXX/..XX/",".X.X/.XXX/XX../XX../","..XX/XXXX/.X../XX../","X.X./XXX./..XX/..XX/","..XX/..X./XXXX/XX../","XX../XX../.XXX/.X.X/"};
		shape[870] = new String[]{"..XX/XX.X/XXXX/","XX./XX./X.X/XXX/","XXXX/X.XX/XX../","XXX/X.X/.XX/.XX/","XX../X.XX/XXXX/","XXX/X.X/XX./XX./","XXXX/XX.X/..XX/",".XX/.XX/X.X/XXX/"};
		shape[871] = new String[]{"...XX/XX.X./XXXX./","XX./XX./X../XXX/..X/",".XXXX/.X.XX/XX.../","X../XXX/..X/.XX/.XX/","XX.../.X.XX/.XXXX/","..X/XXX/X../XX./XX./","XXXX./XX.X./...XX/",".XX/.XX/..X/XXX/X../"};
		shape[872] = new String[]{"...X/...X/XX.X/XXXX/","XX../XX../X.../XXXX/","XXXX/X.XX/X.../X.../","XXXX/...X/..XX/..XX/","X.../X.../X.XX/XXXX/","XXXX/X.../XX../XX../","XXXX/XX.X/...X/...X/","..XX/..XX/...X/XXXX/"};
		shape[873] = new String[]{"...X/XX.X/XXXX/...X/",".XX./.XX./.X../XXXX/","X.../XXXX/X.XX/X.../","XXXX/..X./.XX./.XX./","X.../X.XX/XXXX/X.../","XXXX/.X../.XX./.XX./","...X/XXXX/XX.X/...X/",".XX./.XX./..X./XXXX/"};
		shape[874] = new String[]{"XX.X/XXXX/...X/...X/","..XX/..XX/..X./XXXX/","X.../X.../XXXX/X.XX/","XXXX/.X../XX../XX../","X.XX/XXXX/X.../X.../","XXXX/..X./..XX/..XX/","...X/...X/XXXX/XX.X/","XX../XX../.X../XXXX/"};
		shape[875] = new String[]{"XX../XXXX/...X/..XX/","..XX/..XX/X.X./XXX./","XX../X.../XXXX/..XX/",".XXX/.X.X/XX../XX../","..XX/XXXX/X.../XX../","XXX./X.X./..XX/..XX/","..XX/...X/XXXX/XX../","XX../XX../.X.X/.XXX/"};
		shape[876] = new String[]{"XX.../XXXX./...X./...XX/","..XX/..XX/..X./XXX./X.../","XX.../.X.../.XXXX/...XX/","...X/.XXX/.X../XX../XX../","...XX/.XXXX/.X.../XX.../","X.../XXX./..X./..XX/..XX/","...XX/...X./XXXX./XX.../","XX../XX../.X../.XXX/...X/"};
		shape[877] = new String[]{"XX../XXXX/...X/...X/...X/","...XX/...XX/...X./XXXX./","X.../X.../X.../XXXX/..XX/",".XXXX/.X.../XX.../XX.../","..XX/XXXX/X.../X.../X.../","XXXX./...X./...XX/...XX/","...X/...X/...X/XXXX/XX../","XX.../XX.../.X.../.XXXX/"};
		shape[878] = new String[]{"XXXX.../...X.../...XXXX/","..X/..X/..X/XXX/X../X../X../","...XXXX/...X.../XXXX.../","X../X../X../XXX/..X/..X/..X/"};
		shape[879] = new String[]{"X...../XXX.../..X.../..XXXX/","..XX/..X./XXX./X.../X.../X.../","XXXX../...X../...XXX/.....X/","...X/...X/...X/.XXX/.X../XX../",".....X/...XXX/...X../XXXX../","X.../X.../X.../XXX./..X./..XX/","..XXXX/..X.../XXX.../X...../","XX../.X../.XXX/...X/...X/...X/"};
		shape[880] = new String[]{"XXX.../X.X.../..XXXX/",".XX/..X/XXX/X../X../X../","XXXX../...X.X/...XXX/","..X/..X/..X/XXX/X../XX./","...XXX/...X.X/XXXX../","X../X../X../XXX/..X/.XX/","..XXXX/X.X.../XXX.../","XX./X../XXX/..X/..X/..X/"};
		shape[881] = new String[]{".X..../XXX.../..X.../..XXXX/","..X./..XX/XXX./X.../X.../X.../","XXXX../...X../...XXX/....X./","...X/...X/...X/.XXX/XX../.X../","....X./...XXX/...X../XXXX../","X.../X.../X.../XXX./..XX/..X./","..XXXX/..X.../XXX.../.X..../",".X../XX../.XXX/...X/...X/...X/"};
		shape[882] = new String[]{"XXXX../..X.../..XXXX/","..X/..X/XXX/X.X/X../X../","XXXX../...X../..XXXX/","..X/..X/X.X/XXX/X../X../","..XXXX/...X../XXXX../","X../X../X.X/XXX/..X/..X/","..XXXX/..X.../XXXX../","X../X../XXX/X.X/..X/..X/"};
		shape[883] = new String[]{"..X.../XXX.../..X.../..XXXX/","..X./..X./XXXX/X.../X.../X.../","XXXX../...X../...XXX/...X../","...X/...X/...X/XXXX/.X../.X../","...X../...XXX/...X../XXXX../","X.../X.../X.../XXXX/..X./..X./","..XXXX/..X.../XXX.../..X.../",".X../.X../XXXX/...X/...X/...X/"};
		shape[884] = new String[]{"XXX.../..X.../..XXXX/..X.../","...X/...X/XXXX/.X../.X../.X../","...X../XXXX../...X../...XXX/","..X./..X./..X./XXXX/X.../X.../","...XXX/...X../XXXX../...X../",".X../.X../.X../XXXX/...X/...X/","..X.../..XXXX/..X.../XXX.../","X.../X.../XXXX/..X./..X./..X./"};
		shape[885] = new String[]{"XXX.../..X.../..XXXX/...X../","...X/...X/.XXX/XX../.X../.X../","..X.../XXXX../...X../...XXX/","..X./..X./..XX/XXX./X.../X.../","...XXX/...X../XXXX../..X.../",".X../.X../XX../.XXX/...X/...X/","...X../..XXXX/..X.../XXX.../","X.../X.../XXX./..XX/..X./..X./"};
		shape[886] = new String[]{"XXX.../..X.X./..XXXX/","..X/..X/XXX/X../XX./X../","XXXX../.X.X../...XXX/","..X/.XX/..X/XXX/X../X../","...XXX/.X.X../XXXX../","X../XX./X../XXX/..X/..X/","..XXXX/..X.X./XXX.../","X../X../XXX/..X/.XX/..X/"};
		shape[887] = new String[]{"XXX.../..X.../..XXXX/....X./","...X/...X/.XXX/.X../XX../.X../",".X..../XXXX../...X../...XXX/","..X./..XX/..X./XXX./X.../X.../","...XXX/...X../XXXX../.X..../",".X../XX../.X../.XXX/...X/...X/","....X./..XXXX/..X.../XXX.../","X.../X.../XXX./..X./..XX/..X./"};
		shape[888] = new String[]{"XXX.../..X..X/..XXXX/","..X/..X/XXX/X../X../XX./","XXXX../X..X../...XXX/",".XX/..X/..X/XXX/X../X../","...XXX/X..X../XXXX../","XX./X../X../XXX/..X/..X/","..XXXX/..X..X/XXX.../","X../X../XXX/..X/..X/.XX/"};
		shape[889] = new String[]{"XXX.../..X.../..XXXX/.....X/","...X/...X/.XXX/.X../.X../XX../","X...../XXXX../...X../...XXX/","..XX/..X./..X./XXX./X.../X.../","...XXX/...X../XXXX../X...../","XX../.X../.X../.XXX/...X/...X/",".....X/..XXXX/..X.../XXX.../","X.../X.../XXX./..X./..X./..XX/"};
		shape[890] = new String[]{"XX..../.XX.../..X.../..XXXX/","...X/..XX/XXX./X.../X.../X.../","XXXX../...X../...XX./....XX/","...X/...X/...X/.XXX/XX../X.../","....XX/...XX./...X../XXXX../","X.../X.../X.../XXX./..XX/...X/","..XXXX/..X.../.XX.../XX..../","X.../XX../.XXX/...X/...X/...X/"};
		shape[891] = new String[]{"X..../X..../XX.../.X.../.XXXX/","..XXX/XXX../X..../X..../X..../","XXXX./...X./...XX/....X/....X/","....X/....X/....X/..XXX/XXX../","....X/....X/...XX/...X./XXXX./","X..../X..../X..../XXX../..XXX/",".XXXX/.X.../XX.../X..../X..../","XXX../..XXX/....X/....X/....X/"};
		shape[892] = new String[]{"X..../XXX../.X.../.XXXX/","..XX/XXX./X.X./X.../X.../","XXXX./...X./..XXX/....X/","...X/...X/.X.X/.XXX/XX../","....X/..XXX/...X./XXXX./","X.../X.../X.X./XXX./..XX/",".XXXX/.X.../XXX../X..../","XX../.XXX/.X.X/...X/...X/"};
		shape[893] = new String[]{"X..../XX.../.X.../.XXXX/..X../","...XX/.XXX./XX.../.X.../.X.../","..X../XXXX./...X./...XX/....X/","...X./...X./...XX/.XXX./XX.../","....X/...XX/...X./XXXX./..X../",".X.../.X.../XX.../.XXX./...XX/","..X../.XXXX/.X.../XX.../X..../","XX.../.XXX./...XX/...X./...X./"};
		shape[894] = new String[]{"X..../XX.../.X.X./.XXXX/","..XX/XXX./X.../XX../X.../","XXXX./.X.X./...XX/....X/","...X/..XX/...X/.XXX/XX../","....X/...XX/.X.X./XXXX./","X.../XX../X.../XXX./..XX/",".XXXX/.X.X./XX.../X..../","XX../.XXX/...X/..XX/...X/"};
		shape[895] = new String[]{"X..../XX.../.X.../.XXXX/...X./","...XX/.XXX./.X.../XX.../.X.../",".X.../XXXX./...X./...XX/....X/","...X./...XX/...X./.XXX./XX.../","....X/...XX/...X./XXXX./.X.../",".X.../XX.../.X.../.XXX./...XX/","...X./.XXXX/.X.../XX.../X..../","XX.../.XXX./...X./...XX/...X./"};
		shape[896] = new String[]{"X..../XX.../.X..X/.XXXX/","..XX/XXX./X.../X.../XX../","XXXX./X..X./...XX/....X/","..XX/...X/...X/.XXX/XX../","....X/...XX/X..X./XXXX./","XX../X.../X.../XXX./..XX/",".XXXX/.X..X/XX.../X..../","XX../.XXX/...X/...X/..XX/"};
		shape[897] = new String[]{"X..../XX.../.X.../.XXXX/....X/","...XX/.XXX./.X.../.X.../XX.../","X..../XXXX./...X./...XX/....X/","...XX/...X./...X./.XXX./XX.../","....X/...XX/...X./XXXX./X..../","XX.../.X.../.X.../.XXX./...XX/","....X/.XXXX/.X.../XX.../X..../","XX.../.XXX./...X./...X./...XX/"};
		shape[898] = new String[]{".X.../XXX../.X.../.XXXX/","..X./XXXX/X.X./X.../X.../","XXXX./...X./..XXX/...X./","...X/...X/.X.X/XXXX/.X../","...X./..XXX/...X./XXXX./","X.../X.../X.X./XXXX/..X./",".XXXX/.X.../XXX../.X.../",".X../XXXX/.X.X/...X/...X/"};
		shape[899] = new String[]{"XXXX./.X.../.XXXX/","..X/XXX/X.X/X.X/X../","XXXX./...X./.XXXX/","..X/X.X/X.X/XXX/X../",".XXXX/...X./XXXX./","X../X.X/X.X/XXX/..X/",".XXXX/.X.../XXXX./","X../XXX/X.X/X.X/..X/"};
		shape[900] = new String[]{"..X../XXX../.X.../.XXXX/","..X./XXX./X.XX/X.../X.../","XXXX./...X./..XXX/..X../","...X/...X/XX.X/.XXX/.X../","..X../..XXX/...X./XXXX./","X.../X.../X.XX/XXX./..X./",".XXXX/.X.../XXX../..X../",".X../.XXX/XX.X/...X/...X/"};
		shape[901] = new String[]{"XXX../.X.../.XXXX/.X.../","...X/XXXX/.X.X/.X../.X../","...X./XXXX./...X./..XXX/","..X./..X./X.X./XXXX/X.../","..XXX/...X./XXXX./...X./",".X../.X../.X.X/XXXX/...X/",".X.../.XXXX/.X.../XXX../","X.../XXXX/X.X./..X./..X./"};
		shape[902] = new String[]{"XXX../.X.../.XXXX/..X../","...X/.XXX/XX.X/.X../.X../","..X../XXXX./...X./..XXX/","..X./..X./X.XX/XXX./X.../","..XXX/...X./XXXX./..X../",".X../.X../XX.X/.XXX/...X/","..X../.XXXX/.X.../XXX../","X.../XXX./X.XX/..X./..X./"};
		shape[903] = new String[]{"XXX../.X.X./.XXXX/","..X/XXX/X.X/XX./X../","XXXX./.X.X./..XXX/","..X/.XX/X.X/XXX/X../","..XXX/.X.X./XXXX./","X../XX./X.X/XXX/..X/",".XXXX/.X.X./XXX../","X../XXX/X.X/.XX/..X/"};
		shape[904] = new String[]{"XXX../.X.../.XXXX/...X./","...X/.XXX/.X.X/XX../.X../",".X.../XXXX./...X./..XXX/","..X./..XX/X.X./XXX./X.../","..XXX/...X./XXXX./.X.../",".X../XX../.X.X/.XXX/...X/","...X./.XXXX/.X.../XXX../","X.../XXX./X.X./..XX/..X./"};
		shape[905] = new String[]{"XXX../.X..X/.XXXX/","..X/XXX/X.X/X../XX./","XXXX./X..X./..XXX/",".XX/..X/X.X/XXX/X../","..XXX/X..X./XXXX./","XX./X../X.X/XXX/..X/",".XXXX/.X..X/XXX../","X../XXX/X.X/..X/.XX/"};
		shape[906] = new String[]{"XXX../.X.../.XXXX/....X/","...X/.XXX/.X.X/.X../XX../","X..../XXXX./...X./..XXX/","..XX/..X./X.X./XXX./X.../","..XXX/...X./XXXX./X..../","XX../.X../.X.X/.XXX/...X/","....X/.XXXX/.X.../XXX../","X.../XXX./X.X./..X./..XX/"};
		shape[907] = new String[]{".XX../XX.../.X.../.XXXX/","..X./XXXX/X..X/X.../X.../","XXXX./...X./...XX/..XX./","...X/...X/X..X/XXXX/.X../","..XX./...XX/...X./XXXX./","X.../X.../X..X/XXXX/..X./",".XXXX/.X.../XX.../.XX../",".X../XXXX/X..X/...X/...X/"};
		shape[908] = new String[]{".X.../XX.../.X.X./.XXXX/","..X./XXXX/X.../XX../X.../","XXXX./.X.X./...XX/...X./","...X/..XX/...X/XXXX/.X../","...X./...XX/.X.X./XXXX./","X.../XX../X.../XXXX/..X./",".XXXX/.X.X./XX.../.X.../",".X../XXXX/...X/..XX/...X/"};
		shape[909] = new String[]{".X.../XX.../.X.../.XXXX/...X./","...X./.XXXX/.X.../XX.../.X.../",".X.../XXXX./...X./...XX/...X./","...X./...XX/...X./XXXX./.X.../"};
		shape[910] = new String[]{".X.../XX.../.X..X/.XXXX/","..X./XXXX/X.../X.../XX../","XXXX./X..X./...XX/...X./","..XX/...X/...X/XXXX/.X../","...X./...XX/X..X./XXXX./","XX../X.../X.../XXXX/..X./",".XXXX/.X..X/XX.../.X.../",".X../XXXX/...X/...X/..XX/"};
		shape[911] = new String[]{".X.../XX.../.X.../.XXXX/....X/","...X./.XXXX/.X.../.X.../XX.../","X..../XXXX./...X./...XX/...X./","...XX/...X./...X./XXXX./.X.../","...X./...XX/...X./XXXX./X..../","XX.../.X.../.X.../.XXXX/...X./","....X/.XXXX/.X.../XX.../.X.../",".X.../XXXX./...X./...X./...XX/"};
		shape[912] = new String[]{"XX.../.X.X./.XXXX/.X.../","...X/XXXX/.X../.XX./.X../","...X./XXXX./.X.X./...XX/","..X./.XX./..X./XXXX/X.../","...XX/.X.X./XXXX./...X./",".X../.XX./.X../XXXX/...X/",".X.../.XXXX/.X.X./XX.../","X.../XXXX/..X./.XX./..X./"};
		shape[913] = new String[]{"XX.../.X.../.XXXX/.X.X./","...X/XXXX/.X../XX../.X../",".X.X./XXXX./...X./...XX/","..X./..XX/..X./XXXX/X.../","...XX/...X./XXXX./.X.X./",".X../XX../.X../XXXX/...X/",".X.X./.XXXX/.X.../XX.../","X.../XXXX/..X./..XX/..X./"};
		shape[914] = new String[]{"XX.../.X..X/.XXXX/.X.../","...X/XXXX/.X../.X../.XX./","...X./XXXX./X..X./...XX/",".XX./..X./..X./XXXX/X.../","...XX/X..X./XXXX./...X./",".XX./.X../.X../XXXX/...X/",".X.../.XXXX/.X..X/XX.../","X.../XXXX/..X./..X./.XX./"};
		shape[915] = new String[]{"XX.../.X.../.XXXX/.X..X/","...X/XXXX/.X../.X../XX../","X..X./XXXX./...X./...XX/","..XX/..X./..X./XXXX/X.../","...XX/...X./XXXX./X..X./","XX../.X../.X../XXXX/...X/",".X..X/.XXXX/.X.../XX.../","X.../XXXX/..X./..X./..XX/"};
		shape[916] = new String[]{"XX.../.X.X./.XXXX/..X../","...X/.XXX/XX../.XX./.X../","..X../XXXX./.X.X./...XX/","..X./.XX./..XX/XXX./X.../","...XX/.X.X./XXXX./..X../",".X../.XX./XX../.XXX/...X/","..X../.XXXX/.X.X./XX.../","X.../XXX./..XX/.XX./..X./"};
		shape[917] = new String[]{"XX.../.X.../.XXXX/..XX./","...X/.XXX/XX../XX../.X../",".XX../XXXX./...X./...XX/","..X./..XX/..XX/XXX./X.../","...XX/...X./XXXX./.XX../",".X../XX../XX../.XXX/...X/","..XX./.XXXX/.X.../XX.../","X.../XXX./..XX/..XX/..X./"};
		shape[918] = new String[]{"XX.../.X..X/.XXXX/..X../","...X/.XXX/XX../.X../.XX./","..X../XXXX./X..X./...XX/",".XX./..X./..XX/XXX./X.../","...XX/X..X./XXXX./..X../",".XX./.X../XX../.XXX/...X/","..X../.XXXX/.X..X/XX.../","X.../XXX./..XX/..X./.XX./"};
		shape[919] = new String[]{"XX.../.X.../.XXXX/..X.X/","...X/.XXX/XX../.X../XX../","X.X../XXXX./...X./...XX/","..XX/..X./..XX/XXX./X.../","...XX/...X./XXXX./X.X../","XX../.X../XX../.XXX/...X/","..X.X/.XXXX/.X.../XX.../","X.../XXX./..XX/..X./..XX/"};
		shape[920] = new String[]{"XX.../.X.../.XXXX/..X../..X../","....X/..XXX/XXX../..X../..X../","..X../..X../XXXX./...X./...XX/","..X../..X../..XXX/XXX../X..../","...XX/...X./XXXX./..X../..X../","..X../..X../XXX../..XXX/....X/","..X../..X../.XXXX/.X.../XX.../","X..../XXX../..XXX/..X../..X../"};
		shape[921] = new String[]{"XX.X./.X.X./.XXXX/","..X/XXX/X../XXX/X../","XXXX./.X.X./.X.XX/","..X/XXX/..X/XXX/X../",".X.XX/.X.X./XXXX./","X../XXX/X../XXX/..X/",".XXXX/.X.X./XX.X./","X../XXX/..X/XXX/..X/"};
		shape[922] = new String[]{"XX.../.X.X./.XXXX/...X./","...X/.XXX/.X../XXX./.X../",".X.../XXXX./.X.X./...XX/","..X./.XXX/..X./XXX./X.../","...XX/.X.X./XXXX./.X.../",".X../XXX./.X../.XXX/...X/","...X./.XXXX/.X.X./XX.../","X.../XXX./..X./.XXX/..X./"};
		shape[923] = new String[]{"XX.../.X.X./.XXXX/....X/","...X/.XXX/.X../.XX./XX../","X..../XXXX./.X.X./...XX/","..XX/.XX./..X./XXX./X.../","...XX/.X.X./XXXX./X..../","XX../.XX./.X../.XXX/...X/","....X/.XXXX/.X.X./XX.../","X.../XXX./..X./.XX./..XX/"};
		shape[924] = new String[]{"XX.../.X..X/.XXXX/...X./","...X/.XXX/.X../XX../.XX./",".X.../XXXX./X..X./...XX/",".XX./..XX/..X./XXX./X.../","...XX/X..X./XXXX./.X.../",".XX./XX../.X../.XXX/...X/","...X./.XXXX/.X..X/XX.../","X.../XXX./..X./..XX/.XX./"};
		shape[925] = new String[]{"XX.../.X.../.XXXX/...X./...X./","....X/..XXX/..X../XXX../..X../",".X.../.X.../XXXX./...X./...XX/","..X../..XXX/..X../XXX../X..../","...XX/...X./XXXX./.X.../.X.../","..X../XXX../..X../..XXX/....X/","...X./...X./.XXXX/.X.../XX.../","X..../XXX../..X../..XXX/..X../"};
		shape[926] = new String[]{"XX..X/.X..X/.XXXX/","..X/XXX/X../X../XXX/","XXXX./X..X./X..XX/","XXX/..X/..X/XXX/X../","X..XX/X..X./XXXX./","XXX/X../X../XXX/..X/",".XXXX/.X..X/XX..X/","X../XXX/..X/..X/XXX/"};
		shape[927] = new String[]{"XX.../.X..X/.XXXX/....X/","...X/.XXX/.X../.X../XXX./","X..../XXXX./X..X./...XX/",".XXX/..X./..X./XXX./X.../","...XX/X..X./XXXX./X..../","XXX./.X../.X../.XXX/...X/","....X/.XXXX/.X..X/XX.../","X.../XXX./..X./..X./.XXX/"};
		shape[928] = new String[]{"XX.../.X.../.XXXX/....X/....X/","....X/..XXX/..X../..X../XXX../","X..../X..../XXXX./...X./...XX/","..XXX/..X../..X../XXX../X..../","...XX/...X./XXXX./X..../X..../","XXX../..X../..X../..XXX/....X/","....X/....X/.XXXX/.X.../XX.../","X..../XXX../..X../..X../..XXX/"};
		shape[929] = new String[]{"XX.../.XX../.X.../.XXXX/","...X/XXXX/X.X./X.../X.../","XXXX./...X./..XX./...XX/","...X/...X/.X.X/XXXX/X.../","...XX/..XX./...X./XXXX./","X.../X.../X.X./XXXX/...X/",".XXXX/.X.../.XX../XX.../","X.../XXXX/.X.X/...X/...X/"};
		shape[930] = new String[]{"X.../XXX./X.../XXXX/","XXXX/X.X./X.X./X.../","XXXX/...X/.XXX/...X/","...X/.X.X/.X.X/XXXX/","...X/.XXX/...X/XXXX/","X.../X.X./X.X./XXXX/","XXXX/X.../XXX./X.../","XXXX/.X.X/.X.X/...X/"};
		shape[931] = new String[]{"X.../XX../X.X./XXXX/","XXXX/X.X./XX../X.../","XXXX/.X.X/..XX/...X/","...X/..XX/.X.X/XXXX/"};
		shape[932] = new String[]{"X.../XX../X..X/XXXX/","XXXX/X.X./X.../XX../","XXXX/X..X/..XX/...X/","..XX/...X/.X.X/XXXX/","...X/..XX/X..X/XXXX/","XX../X.../X.X./XXXX/","XXXX/X..X/XX../X.../","XXXX/.X.X/...X/..XX/"};
		shape[933] = new String[]{"X.../XX../X.../XXXX/...X/",".XXXX/.X.X./.X.../XX.../","X.../XXXX/...X/..XX/...X/","...XX/...X./.X.X./XXXX./","...X/..XX/...X/XXXX/X.../","XX.../.X.../.X.X./.XXXX/","...X/XXXX/X.../XX../X.../","XXXX./.X.X./...X./...XX/"};
		shape[934] = new String[]{".X../XXX./X.../XXXX/","XXX./X.XX/X.X./X.../","XXXX/...X/.XXX/..X./","...X/.X.X/XX.X/.XXX/","..X./.XXX/...X/XXXX/","X.../X.X./X.XX/XXX./","XXXX/X.../XXX./.X../",".XXX/XX.X/.X.X/...X/"};
		shape[935] = new String[]{"XXXX/X.../XXXX/","XXX/X.X/X.X/X.X/","XXXX/...X/XXXX/","X.X/X.X/X.X/XXX/"};
		shape[936] = new String[]{"..X./XXX./X.../XXXX/","XXX./X.X./X.XX/X.../","XXXX/...X/.XXX/.X../","...X/XX.X/.X.X/.XXX/",".X../.XXX/...X/XXXX/","X.../X.XX/X.X./XXX./","XXXX/X.../XXX./..X./",".XXX/.X.X/XX.X/...X/"};
		shape[937] = new String[]{"XXX./X.X./XXXX/","XXX/X.X/XXX/X../","XXXX/.X.X/.XXX/","..X/XXX/X.X/XXX/",".XXX/.X.X/XXXX/","X../XXX/X.X/XXX/","XXXX/X.X./XXX./","XXX/X.X/XXX/..X/"};
		shape[938] = new String[]{"XXX./X.../XXXX/X.../","XXXX/.X.X/.X.X/.X../","...X/XXXX/...X/.XXX/","..X./X.X./X.X./XXXX/",".XXX/...X/XXXX/...X/",".X../.X.X/.X.X/XXXX/","X.../XXXX/X.../XXX./","XXXX/X.X./X.X./..X./"};
		shape[939] = new String[]{"XXX./X.../XXXX/.X../",".XXX/XX.X/.X.X/.X../","..X./XXXX/...X/.XXX/","..X./X.X./X.XX/XXX./",".XXX/...X/XXXX/..X./",".X../.X.X/XX.X/.XXX/",".X../XXXX/X.../XXX./","XXX./X.XX/X.X./..X./"};
		shape[940] = new String[]{"XXX./X.../XXXX/..X./",".XXX/.X.X/XX.X/.X../",".X../XXXX/...X/.XXX/","..X./X.XX/X.X./XXX./",".XXX/...X/XXXX/.X../",".X../XX.X/.X.X/.XXX/","..X./XXXX/X.../XXX./","XXX./X.X./X.XX/..X./"};
		shape[941] = new String[]{"XXX./X..X/XXXX/","XXX/X.X/X.X/XX./","XXXX/X..X/.XXX/",".XX/X.X/X.X/XXX/",".XXX/X..X/XXXX/","XX./X.X/X.X/XXX/","XXXX/X..X/XXX./","XXX/X.X/X.X/.XX/"};
		shape[942] = new String[]{"XXX./X.../XXXX/...X/",".XXX/.X.X/.X.X/XX../","X.../XXXX/...X/.XXX/","..XX/X.X./X.X./XXX./",".XXX/...X/XXXX/X.../","XX../.X.X/.X.X/.XXX/","...X/XXXX/X.../XXX./","XXX./X.X./X.X./..XX/"};
		shape[943] = new String[]{".XX./XX../X.../XXXX/","XXX./X.XX/X..X/X.../","XXXX/...X/..XX/.XX./","...X/X..X/XX.X/.XXX/",".XX./..XX/...X/XXXX/","X.../X..X/X.XX/XXX./","XXXX/X.../XX../.XX./",".XXX/XX.X/X..X/...X/"};
		shape[944] = new String[]{".X../.X../XX../X.../XXXX/","XXX../X.XXX/X..../X..../","XXXX/...X/..XX/..X./..X./","....X/....X/XXX.X/..XXX/","..X./..X./..XX/...X/XXXX/","X..../X..../X.XXX/XXX../","XXXX/X.../XX../.X../.X../","..XXX/XXX.X/....X/....X/"};
		shape[945] = new String[]{".X../XX../X.../XXXX/.X../",".XXX./XX.XX/.X.../.X.../","..X./XXXX/...X/..XX/..X./","...X./...X./XX.XX/.XXX./","..X./..XX/...X/XXXX/..X./",".X.../.X.../XX.XX/.XXX./",".X../XXXX/X.../XX../.X../",".XXX./XX.XX/...X./...X./"};
		shape[946] = new String[]{".X../XX../X.X./XXXX/","XXX./X.XX/XX../X.../","XXXX/.X.X/..XX/..X./","...X/..XX/XX.X/.XXX/","..X./..XX/.X.X/XXXX/","X.../XX../X.XX/XXX./","XXXX/X.X./XX../.X../",".XXX/XX.X/..XX/...X/"};
		shape[947] = new String[]{".X../XX../X.../XXXX/..X./",".XXX./.X.XX/XX.../.X.../",".X../XXXX/...X/..XX/..X./","...X./...XX/XX.X./.XXX./","..X./..XX/...X/XXXX/.X../",".X.../XX.../.X.XX/.XXX./","..X./XXXX/X.../XX../.X../",".XXX./XX.X./...XX/...X./"};
		shape[948] = new String[]{".X../XX../X..X/XXXX/","XXX./X.XX/X.../XX../","XXXX/X..X/..XX/..X./","..XX/...X/XX.X/.XXX/","..X./..XX/X..X/XXXX/","XX../X.../X.XX/XXX./","XXXX/X..X/XX../.X../",".XXX/XX.X/...X/..XX/"};
		shape[949] = new String[]{".X../XX../X.../XXXX/...X/",".XXX./.X.XX/.X.../XX.../","X.../XXXX/...X/..XX/..X./","...XX/...X./XX.X./.XXX./","..X./..XX/...X/XXXX/X.../","XX.../.X.../.X.XX/.XXX./","...X/XXXX/X.../XX../.X../",".XXX./XX.X./...X./...XX/"};
		shape[950] = new String[]{"XX../X.X./XXXX/X.../","XXXX/.X.X/.XX./.X../","...X/XXXX/.X.X/..XX/","..X./.XX./X.X./XXXX/","..XX/.X.X/XXXX/...X/",".X../.XX./.X.X/XXXX/","X.../XXXX/X.X./XX../","XXXX/X.X./.XX./..X./"};
		shape[951] = new String[]{"XX../X.../XXXX/X.X./","XXXX/.X.X/XX../.X../",".X.X/XXXX/...X/..XX/","..X./..XX/X.X./XXXX/","..XX/...X/XXXX/.X.X/",".X../XX../.X.X/XXXX/","X.X./XXXX/X.../XX../","XXXX/X.X./..XX/..X./"};
		shape[952] = new String[]{"XX../X..X/XXXX/X.../","XXXX/.X.X/.X../.XX./","...X/XXXX/X..X/..XX/",".XX./..X./X.X./XXXX/","..XX/X..X/XXXX/...X/",".XX./.X../.X.X/XXXX/","X.../XXXX/X..X/XX../","XXXX/X.X./..X./.XX./"};
		shape[953] = new String[]{"XX../X.../XXXX/X..X/","XXXX/.X.X/.X../XX../","X..X/XXXX/...X/..XX/","..XX/..X./X.X./XXXX/","..XX/...X/XXXX/X..X/","XX../.X../.X.X/XXXX/","X..X/XXXX/X.../XX../","XXXX/X.X./..X./..XX/"};
		shape[954] = new String[]{"XX../X.X./XXXX/.X../",".XXX/XX.X/.XX./.X../","..X./XXXX/.X.X/..XX/","..X./.XX./X.XX/XXX./","..XX/.X.X/XXXX/..X./",".X../.XX./XX.X/.XXX/",".X../XXXX/X.X./XX../","XXX./X.XX/.XX./..X./"};
		shape[955] = new String[]{"XX../X.../XXXX/.XX./",".XXX/XX.X/XX../.X../",".XX./XXXX/...X/..XX/","..X./..XX/X.XX/XXX./","..XX/...X/XXXX/.XX./",".X../XX../XX.X/.XXX/",".XX./XXXX/X.../XX../","XXX./X.XX/..XX/..X./"};
		shape[956] = new String[]{"XX../X..X/XXXX/.X../",".XXX/XX.X/.X../.XX./","..X./XXXX/X..X/..XX/",".XX./..X./X.XX/XXX./","..XX/X..X/XXXX/..X./",".XX./.X../XX.X/.XXX/",".X../XXXX/X..X/XX../","XXX./X.XX/..X./.XX./"};
		shape[957] = new String[]{"XX../X.../XXXX/.X.X/",".XXX/XX.X/.X../XX../","X.X./XXXX/...X/..XX/","..XX/..X./X.XX/XXX./","..XX/...X/XXXX/X.X./","XX../.X../XX.X/.XXX/",".X.X/XXXX/X.../XX../","XXX./X.XX/..X./..XX/"};
		shape[958] = new String[]{"XX../X.../XXXX/.X../.X../","..XXX/XXX.X/..X../..X../","..X./..X./XXXX/...X/..XX/","..X../..X../X.XXX/XXX../","..XX/...X/XXXX/..X./..X./","..X../..X../XXX.X/..XXX/",".X../.X../XXXX/X.../XX../","XXX../X.XXX/..X../..X../"};
		shape[959] = new String[]{"XX../X.X./XXXX/..X./",".XXX/.X.X/XXX./.X../",".X../XXXX/.X.X/..XX/","..X./.XXX/X.X./XXX./","..XX/.X.X/XXXX/.X../",".X../XXX./.X.X/.XXX/","..X./XXXX/X.X./XX../","XXX./X.X./.XXX/..X./"};
		shape[960] = new String[]{"XX../X.X./XXXX/...X/",".XXX/.X.X/.XX./XX../","X.../XXXX/.X.X/..XX/","..XX/.XX./X.X./XXX./","..XX/.X.X/XXXX/X.../","XX../.XX./.X.X/.XXX/","...X/XXXX/X.X./XX../","XXX./X.X./.XX./..XX/"};
		shape[961] = new String[]{"XX../X..X/XXXX/..X./",".XXX/.X.X/XX../.XX./",".X../XXXX/X..X/..XX/",".XX./..XX/X.X./XXX./","..XX/X..X/XXXX/.X../",".XX./XX../.X.X/.XXX/","..X./XXXX/X..X/XX../","XXX./X.X./..XX/.XX./"};
		shape[962] = new String[]{"XX../X.../XXXX/..X./..X./","..XXX/..X.X/XXX../..X../",".X../.X../XXXX/...X/..XX/","..X../..XXX/X.X../XXX../","..XX/...X/XXXX/.X../.X../","..X../XXX../..X.X/..XXX/","..X./..X./XXXX/X.../XX../","XXX../X.X../..XXX/..X../"};
		shape[963] = new String[]{"XX.X/X..X/XXXX/","XXX/X.X/X../XXX/","XXXX/X..X/X.XX/","XXX/..X/X.X/XXX/","X.XX/X..X/XXXX/","XXX/X../X.X/XXX/","XXXX/X..X/XX.X/","XXX/X.X/..X/XXX/"};
		shape[964] = new String[]{"XX../X..X/XXXX/...X/",".XXX/.X.X/.X../XXX./","X.../XXXX/X..X/..XX/",".XXX/..X./X.X./XXX./","..XX/X..X/XXXX/X.../","XXX./.X../.X.X/.XXX/","...X/XXXX/X..X/XX../","XXX./X.X./..X./.XXX/"};
		shape[965] = new String[]{"XX../X.../XXXX/...X/...X/","..XXX/..X.X/..X../XXX../","X.../X.../XXXX/...X/..XX/","..XXX/..X../X.X../XXX../","..XX/...X/XXXX/X.../X.../","XXX../..X../..X.X/..XXX/","...X/...X/XXXX/X.../XX../","XXX../X.X../..X../..XXX/"};
		shape[966] = new String[]{"XXX.../..X.../..X.../..XXXX/","...X/...X/XXXX/X.../X.../X.../","XXXX../...X../...X../...XXX/","...X/...X/...X/XXXX/X.../X.../","...XXX/...X../...X../XXXX../","X.../X.../X.../XXXX/...X/...X/","..XXXX/..X.../..X.../XXX.../","X.../X.../XXXX/...X/...X/...X/"};
		shape[967] = new String[]{"XXX../.X.../.X.../.XXXX/","...X/XXXX/X..X/X.../X.../","XXXX./...X./...X./..XXX/","...X/...X/X..X/XXXX/X.../","..XXX/...X./...X./XXXX./","X.../X.../X..X/XXXX/...X/",".XXXX/.X.../.X.../XXX../","X.../XXXX/X..X/...X/...X/"};
		shape[968] = new String[]{"XX.../.X.../.X..X/.XXXX/","...X/XXXX/X.../X.../XX../","XXXX./X..X./...X./...XX/","..XX/...X/...X/XXXX/X.../","...XX/...X./X..X./XXXX./","XX../X.../X.../XXXX/...X/",".XXXX/.X..X/.X.../XX.../","X.../XXXX/...X/...X/..XX/"};
		shape[969] = new String[]{"XX.../.X.../.X.../.XXXX/....X/","....X/.XXXX/.X.../.X.../XX.../","X..../XXXX./...X./...X./...XX/","...XX/...X./...X./XXXX./X..../"};
		shape[970] = new String[]{"XXX./X.../X.../XXXX/","XXXX/X..X/X..X/X.../","XXXX/...X/...X/.XXX/","...X/X..X/X..X/XXXX/",".XXX/...X/...X/XXXX/","X.../X..X/X..X/XXXX/","XXXX/X.../X.../XXX./","XXXX/X..X/X..X/...X/"};
		shape[971] = new String[]{"XX../X.../X..X/XXXX/","XXXX/X..X/X.../XX../","XXXX/X..X/...X/..XX/","..XX/...X/X..X/XXXX/"};
		shape[972] = new String[]{"X.X./X.X./XXXX/X.../","XXXX/.X../.XXX/.X../","...X/XXXX/.X.X/.X.X/","..X./XXX./..X./XXXX/",".X.X/.X.X/XXXX/...X/",".X../.XXX/.X../XXXX/","X.../XXXX/X.X./X.X./","XXXX/..X./XXX./..X./"};
		shape[973] = new String[]{"X.../X.X./XXXX/X.X./","XXXX/.X../XXX./.X../",".X.X/XXXX/.X.X/...X/","..X./.XXX/..X./XXXX/","...X/.X.X/XXXX/.X.X/",".X../XXX./.X../XXXX/","X.X./XXXX/X.X./X.../","XXXX/..X./.XXX/..X./"};
		shape[974] = new String[]{"X.../X.X./XXXX/X..X/","XXXX/.X../.XX./XX../","X..X/XXXX/.X.X/...X/","..XX/.XX./..X./XXXX/","...X/.X.X/XXXX/X..X/","XX../.XX./.X../XXXX/","X..X/XXXX/X.X./X.../","XXXX/..X./.XX./..XX/"};
		shape[975] = new String[]{"X.../X..X/XXXX/X.X./","XXXX/.X../XX../.XX./",".X.X/XXXX/X..X/...X/",".XX./..XX/..X./XXXX/","...X/X..X/XXXX/.X.X/",".XX./XX../.X../XXXX/","X.X./XXXX/X..X/X.../","XXXX/..X./..XX/.XX./"};
		shape[976] = new String[]{"X.../X.../XXXX/X.X./..X./",".XXXX/..X../XXX../..X../",".X../.X.X/XXXX/...X/...X/","..X../..XXX/..X../XXXX./","...X/...X/XXXX/.X.X/.X../","..X../XXX../..X../.XXXX/","..X./X.X./XXXX/X.../X.../","XXXX./..X../..XXX/..X../"};
		shape[977] = new String[]{"X..X/X..X/XXXX/X.../","XXXX/.X../.X../.XXX/","...X/XXXX/X..X/X..X/","XXX./..X./..X./XXXX/","X..X/X..X/XXXX/...X/",".XXX/.X../.X../XXXX/","X.../XXXX/X..X/X..X/","XXXX/..X./..X./XXX./"};
		shape[978] = new String[]{"X.../X..X/XXXX/X..X/","XXXX/.X../.X../XXX./","X..X/XXXX/X..X/...X/",".XXX/..X./..X./XXXX/","...X/X..X/XXXX/X..X/","XXX./.X../.X../XXXX/","X..X/XXXX/X..X/X.../","XXXX/..X./..X./.XXX/"};
		shape[979] = new String[]{"X.../X.../XXXX/X..X/...X/",".XXXX/..X../..X../XXX../","X.../X..X/XXXX/...X/...X/","..XXX/..X../..X../XXXX./","...X/...X/XXXX/X..X/X.../","XXX../..X../..X../.XXXX/","...X/X..X/XXXX/X.../X.../","XXXX./..X../..X../..XXX/"};
		shape[980] = new String[]{"X.X./X.X./XXXX/.X../",".XXX/XX../.XXX/.X../","..X./XXXX/.X.X/.X.X/","..X./XXX./..XX/XXX./",".X.X/.X.X/XXXX/..X./",".X../.XXX/XX../.XXX/",".X../XXXX/X.X./X.X./","XXX./..XX/XXX./..X./"};
		shape[981] = new String[]{"X.../X.X./XXXX/.XX./",".XXX/XX../XXX./.X../",".XX./XXXX/.X.X/...X/","..X./.XXX/..XX/XXX./","...X/.X.X/XXXX/.XX./",".X../XXX./XX../.XXX/",".XX./XXXX/X.X./X.../","XXX./..XX/.XXX/..X./"};
		shape[982] = new String[]{"X.../X.X./XXXX/.X.X/",".XXX/XX../.XX./XX../","X.X./XXXX/.X.X/...X/","..XX/.XX./..XX/XXX./","...X/.X.X/XXXX/X.X./","XX../.XX./XX../.XXX/",".X.X/XXXX/X.X./X.../","XXX./..XX/.XX./..XX/"};
		shape[983] = new String[]{"X.../X.X./XXXX/.X../.X../","..XXX/XXX../..XX./..X../","..X./..X./XXXX/.X.X/...X/","..X../.XX../..XXX/XXX../","...X/.X.X/XXXX/..X./..X./","..X../..XX./XXX../..XXX/",".X../.X../XXXX/X.X./X.../","XXX../..XXX/.XX../..X../"};
		shape[984] = new String[]{"X.../X..X/XXXX/.XX./",".XXX/XX../XX../.XX./",".XX./XXXX/X..X/...X/",".XX./..XX/..XX/XXX./","...X/X..X/XXXX/.XX./",".XX./XX../XX../.XXX/",".XX./XXXX/X..X/X.../","XXX./..XX/..XX/.XX./"};
		shape[985] = new String[]{"X.../X.../XXXX/.XX./.X../","..XXX/XXX../.XX../..X../","..X./.XX./XXXX/...X/...X/","..X../..XX./..XXX/XXX../","...X/...X/XXXX/.XX./..X./","..X../.XX../XXX../..XXX/",".X../.XX./XXXX/X.../X.../","XXX../..XXX/..XX./..X../"};
		shape[986] = new String[]{"X.../X.../XXXX/.XX./..X./","..XXX/.XX../XXX../..X../",".X../.XX./XXXX/...X/...X/","..X../..XXX/..XX./XXX../","...X/...X/XXXX/.XX./.X../","..X../XXX../.XX../..XXX/","..X./.XX./XXXX/X.../X.../","XXX../..XX./..XXX/..X../"};
		shape[987] = new String[]{"X..X/X..X/XXXX/.X../",".XXX/XX../.X../.XXX/","..X./XXXX/X..X/X..X/","XXX./..X./..XX/XXX./","X..X/X..X/XXXX/..X./",".XXX/.X../XX../.XXX/",".X../XXXX/X..X/X..X/","XXX./..XX/..X./XXX./"};
		shape[988] = new String[]{"X.../X..X/XXXX/.X.X/",".XXX/XX../.X../XXX./","X.X./XXXX/X..X/...X/",".XXX/..X./..XX/XXX./","...X/X..X/XXXX/X.X./","XXX./.X../XX../.XXX/",".X.X/XXXX/X..X/X.../","XXX./..XX/..X./.XXX/"};
		shape[989] = new String[]{"X.../X..X/XXXX/.X../.X../","..XXX/XXX../..X../..XX./","..X./..X./XXXX/X..X/...X/",".XX../..X../..XXX/XXX../","...X/X..X/XXXX/..X./..X./","..XX./..X../XXX../..XXX/",".X../.X../XXXX/X..X/X.../","XXX../..XXX/..X../.XX../"};
		shape[990] = new String[]{"X.../X.../XXXX/.X.X/.X../","..XXX/XXX../..X../.XX../","..X./X.X./XXXX/...X/...X/","..XX./..X../..XXX/XXX../","...X/...X/XXXX/X.X./..X./",".XX../..X../XXX../..XXX/",".X../.X.X/XXXX/X.../X.../","XXX../..XXX/..X../..XX./"};
		shape[991] = new String[]{"X.../X.../XXXX/.X.X/...X/","..XXX/.XX../..X../XXX../","X.../X.X./XXXX/...X/...X/","..XXX/..X../..XX./XXX../","...X/...X/XXXX/X.X./X.../","XXX../..X../.XX../..XXX/","...X/.X.X/XXXX/X.../X.../","XXX../..XX./..X../..XXX/"};
		shape[992] = new String[]{"X.../X.../XXXX/.X../XX../","X.XXX/XXX../..X../..X../","..XX/..X./XXXX/...X/...X/","..X../..X../..XXX/XXX.X/","...X/...X/XXXX/..X./..XX/","..X../..X../XXX../X.XXX/","XX../.X../XXXX/X.../X.../","XXX.X/..XXX/..X../..X../"};
		shape[993] = new String[]{"X.../X.../XXXX/.X../.XX./","..XXX/XXX../X.X../..X../",".XX./..X./XXXX/...X/...X/","..X../..X.X/..XXX/XXX../","...X/...X/XXXX/..X./.XX./","..X../X.X../XXX../..XXX/",".XX./.X../XXXX/X.../X.../","XXX../..XXX/..X.X/..X../"};
		shape[994] = new String[]{"X.XX/X.X./XXXX/","XXX/X../XXX/X.X/","XXXX/.X.X/XX.X/","X.X/XXX/..X/XXX/","XX.X/.X.X/XXXX/","X.X/XXX/X../XXX/","XXXX/X.X./X.XX/","XXX/..X/XXX/X.X/"};
		shape[995] = new String[]{"X.X./X.X./XXXX/..X./",".XXX/.X../XXXX/.X../",".X../XXXX/.X.X/.X.X/","..X./XXXX/..X./XXX./",".X.X/.X.X/XXXX/.X../",".X../XXXX/.X../.XXX/","..X./XXXX/X.X./X.X./","XXX./..X./XXXX/..X./"};
		shape[996] = new String[]{"X.X./X.X./XXXX/...X/",".XXX/.X../.XXX/XX../","X.../XXXX/.X.X/.X.X/","..XX/XXX./..X./XXX./",".X.X/.X.X/XXXX/X.../","XX../.XXX/.X../.XXX/","...X/XXXX/X.X./X.X./","XXX./..X./XXX./..XX/"};
		shape[997] = new String[]{"X.../X.X./XXXX/..X./..X./","..XXX/..X../XXXX./..X../",".X../.X../XXXX/.X.X/...X/","..X../.XXXX/..X../XXX../","...X/.X.X/XXXX/.X../.X../","..X../XXXX./..X../..XXX/","..X./..X./XXXX/X.X./X.../","XXX../..X../.XXXX/..X../"};
		shape[998] = new String[]{"X.../X..X/XXXX/..X./..X./","..XXX/..X../XXX../..XX./",".X../.X../XXXX/X..X/...X/",".XX../..XXX/..X../XXX../","...X/X..X/XXXX/.X../.X../","..XX./XXX../..X../..XXX/","..X./..X./XXXX/X..X/X.../","XXX../..X../..XXX/.XX../"};
		shape[999] = new String[]{"X.../X.../XXXX/..X./.XX./","..XXX/X.X../XXX../..X../",".XX./.X../XXXX/...X/...X/","..X../..XXX/..X.X/XXX../","...X/...X/XXXX/.X../.XX./","..X../XXX../X.X../..XXX/",".XX./..X./XXXX/X.../X.../","XXX../..X.X/..XXX/..X../"};
		shape[1000] = new String[]{"X.../X.../XXXX/..X./..XX/","..XXX/..X../XXX../X.X../","XX../.X../XXXX/...X/...X/","..X.X/..XXX/..X../XXX../","...X/...X/XXXX/.X../XX../","X.X../XXX../..X../..XXX/","..XX/..X./XXXX/X.../X.../","XXX../..X../..XXX/..X.X/"};
		return shape;
	}
	public String[][] setup92(String[][] shape) {
		shape[1001] = new String[]{".XX./X.X./XXXX/X.../","XXX./.X.X/.XXX/.X../","...X/XXXX/.X.X/.XX./","..X./XXX./X.X./.XXX/",".XX./.X.X/XXXX/...X/",".X../.XXX/.X.X/XXX./","X.../XXXX/X.X./.XX./",".XXX/X.X./XXX./..X./"};
		shape[1002] = new String[]{"..XX/X.X./XXXX/X.../","XXX./.X../.XXX/.X.X/","...X/XXXX/.X.X/XX../","X.X./XXX./..X./.XXX/","XX../.X.X/XXXX/...X/",".X.X/.XXX/.X../XXX./","X.../XXXX/X.X./..XX/",".XXX/..X./XXX./X.X./"};
		shape[1003] = new String[]{"..X./X.X./XXXX/X.X./","XXX./.X../XXXX/.X../",".X.X/XXXX/.X.X/.X../","..X./XXXX/..X./.XXX/",".X../.X.X/XXXX/.X.X/",".X../XXXX/.X../XXX./","X.X./XXXX/X.X./..X./",".XXX/..X./XXXX/..X./"};
		shape[1004] = new String[]{"..X./X.X./XXXX/X..X/","XXX./.X../.XXX/XX../","X..X/XXXX/.X.X/.X../","..XX/XXX./..X./.XXX/",".X../.X.X/XXXX/X..X/","XX../.XXX/.X../XXX./","X..X/XXXX/X.X./..X./",".XXX/..X./XXX./..XX/"};
		shape[1005] = new String[]{".XX./X.X./XXXX/.X../",".XX./XX.X/.XXX/.X../","..X./XXXX/.X.X/.XX./","..X./XXX./X.XX/.XX./",".XX./.X.X/XXXX/..X./",".X../.XXX/XX.X/.XX./",".X../XXXX/X.X./.XX./",".XX./X.XX/XXX./..X./"};
		shape[1006] = new String[]{"..XX/X.X./XXXX/.X../",".XX./XX../.XXX/.X.X/","..X./XXXX/.X.X/XX../","X.X./XXX./..XX/.XX./","XX../.X.X/XXXX/..X./",".X.X/.XXX/XX../.XX./",".X../XXXX/X.X./..XX/",".XX./..XX/XXX./X.X./"};
		shape[1007] = new String[]{"..X./X.X./XXXX/.X.X/",".XX./XX../.XXX/XX../","X.X./XXXX/.X.X/.X../","..XX/XXX./..XX/.XX./",".X../.X.X/XXXX/X.X./","XX../.XXX/XX../.XX./",".X.X/XXXX/X.X./..X./",".XX./..XX/XXX./..XX/"};
		shape[1008] = new String[]{"..X./X.X./XXXX/.X../.X../","..XX./XXX../..XXX/..X../","..X./..X./XXXX/.X.X/.X../","..X../XXX../..XXX/.XX../",".X../.X.X/XXXX/..X./..X./","..X../..XXX/XXX../..XX./",".X../.X../XXXX/X.X./..X./",".XX../..XXX/XXX../..X../"};
		shape[1009] = new String[]{"X.X./XXXX/.XX./.X../","..XX/XXX./.XXX/..X./","..X./.XX./XXXX/.X.X/",".X../XXX./.XXX/XX../",".X.X/XXXX/.XX./..X./","..X./.XXX/XXX./..XX/",".X../.XX./XXXX/X.X./","XX../.XXX/XXX./.X../"};
		shape[1010] = new String[]{"X.X./XXXX/.XX./..X./","..XX/.XX./XXXX/..X./",".X../.XX./XXXX/.X.X/",".X../XXXX/.XX./XX../",".X.X/XXXX/.XX./.X../","..X./XXXX/.XX./..XX/","..X./.XX./XXXX/X.X./","XX../.XX./XXXX/.X../"};
		shape[1011] = new String[]{"X.X./XXXX/.X../XX../","X.XX/XXX./..XX/..X./","..XX/..X./XXXX/.X.X/",".X../XX../.XXX/XX.X/",".X.X/XXXX/..X./..XX/","..X./..XX/XXX./X.XX/","XX../.X../XXXX/X.X./","XX.X/.XXX/XX../.X../"};
		shape[1012] = new String[]{"X.X./XXXX/.X../.XX./","..XX/XXX./X.XX/..X./",".XX./..X./XXXX/.X.X/",".X../XX.X/.XXX/XX../",".X.X/XXXX/..X./.XX./","..X./X.XX/XXX./..XX/",".XX./.X../XXXX/X.X./","XX../.XXX/XX.X/.X../"};
		shape[1013] = new String[]{"X..X/XXXX/.XX./.X../","..XX/XXX./.XX./..XX/","..X./.XX./XXXX/X..X/","XX../.XX./.XXX/XX../","X..X/XXXX/.XX./..X./","..XX/.XX./XXX./..XX/",".X../.XX./XXXX/X..X/","XX../.XXX/.XX./XX../"};
		shape[1014] = new String[]{"X.../XXXX/.XX./.XX./","..XX/XXX./XXX./..X./",".XX./.XX./XXXX/...X/",".X../.XXX/.XXX/XX../","...X/XXXX/.XX./.XX./","..X./XXX./XXX./..XX/",".XX./.XX./XXXX/X.../","XX../.XXX/.XXX/.X../"};
		shape[1015] = new String[]{"X.../XXXX/.XX./XX../","X.XX/XXX./.XX./..X./","..XX/.XX./XXXX/...X/",".X../.XX./.XXX/XX.X/","...X/XXXX/.XX./..XX/","..X./.XX./XXX./X.XX/","XX../.XX./XXXX/X.../","XX.X/.XXX/.XX./.X../"};
		shape[1016] = new String[]{"X.../XXXX/.XX./..XX/","..XX/.XX./XXX./X.X./","XX../.XX./XXXX/...X/",".X.X/.XXX/.XX./XX../","...X/XXXX/.XX./XX../","X.X./XXX./.XX./..XX/","..XX/.XX./XXXX/X.../","XX../.XX./.XXX/.X.X/"};
		shape[1017] = new String[]{"X..X/XXXX/.X../XX../","X.XX/XXX./..X./..XX/","..XX/..X./XXXX/X..X/","XX../.X../.XXX/XX.X/","X..X/XXXX/..X./..XX/","..XX/..X./XXX./X.XX/","XX../.X../XXXX/X..X/","XX.X/.XXX/.X../XX../"};
		shape[1018] = new String[]{"X..X/XXXX/.X../.XX./","..XX/XXX./X.X./..XX/",".XX./..X./XXXX/X..X/","XX../.X.X/.XXX/XX../","X..X/XXXX/..X./.XX./","..XX/X.X./XXX./..XX/",".XX./.X../XXXX/X..X/","XX../.XXX/.X.X/XX../"};
		shape[1019] = new String[]{"X.../XXXX/.X.X/XX../","X.XX/XXX./..X./.XX./","..XX/X.X./XXXX/...X/",".XX./.X../.XXX/XX.X/","...X/XXXX/X.X./..XX/",".XX./..X./XXX./X.XX/","XX../.X.X/XXXX/X.../","XX.X/.XXX/.X../.XX./"};
		shape[1020] = new String[]{"X.../XXXX/.X.X/.XX./","..XX/XXX./X.X./.XX./",".XX./X.X./XXXX/...X/",".XX./.X.X/.XXX/XX../","...X/XXXX/X.X./.XX./",".XX./X.X./XXX./..XX/",".XX./.X.X/XXXX/X.../","XX../.XXX/.X.X/.XX./"};
		shape[1021] = new String[]{".X.../.XXXX/..X../XXX../","X.../X.XX/XXX./..X./..X./","..XXX/..X../XXXX./...X./",".X../.X../.XXX/XX.X/...X/","...X./XXXX./..X../..XXX/","..X./..X./XXX./X.XX/X.../","XXX../..X../.XXXX/.X.../","...X/XX.X/.XXX/.X../.X../"};
		shape[1022] = new String[]{"X.../XXXX/.X../XX../X.../","XX.XX/.XXX./...X./...X./","...X/..XX/..X./XXXX/...X/",".X.../.X.../.XXX./XX.XX/","...X/XXXX/..X./..XX/...X/","...X./...X./.XXX./XX.XX/","X.../XX../.X../XXXX/X.../","XX.XX/.XXX./.X.../.X.../"};
		shape[1023] = new String[]{"X.../XXXX/.X../XXX./","X.XX/XXX./X.X./..X./",".XXX/..X./XXXX/...X/",".X../.X.X/.XXX/XX.X/","...X/XXXX/..X./.XXX/","..X./X.X./XXX./X.XX/","XXX./.X../XXXX/X.../","XX.X/.XXX/.X.X/.X../"};
		shape[1024] = new String[]{"X.../XXXX/.X../.XXX/","..XX/XXX./X.X./X.X./","XXX./..X./XXXX/...X/",".X.X/.X.X/.XXX/XX../","...X/XXXX/..X./XXX./","X.X./X.X./XXX./..XX/",".XXX/.X../XXXX/X.../","XX../.XXX/.X.X/.X.X/"};
		shape[1025] = new String[]{"X.../XXXX/.X../.XX./..X./","...XX/.XXX./XX.X./...X./",".X../.XX./..X./XXXX/...X/",".X.../.X.XX/.XXX./XX.../","...X/XXXX/..X./.XX./.X../","...X./XX.X./.XXX./...XX/","..X./.XX./.X../XXXX/X.../","XX.../.XXX./.X.XX/.X.../"};
		shape[1026] = new String[]{".X../.XX./X.X./XXXX/","XX../X.XX/XXX./X.../","XXXX/.X.X/.XX./..X./","...X/.XXX/XX.X/..XX/","..X./.XX./.X.X/XXXX/","X.../XXX./X.XX/XX../","XXXX/X.X./.XX./.X../","..XX/XX.X/.XXX/...X/"};
		shape[1027] = new String[]{".XXX/X.X./XXXX/","XX./X.X/XXX/X.X/","XXXX/.X.X/XXX./","X.X/XXX/X.X/.XX/","XXX./.X.X/XXXX/","X.X/XXX/X.X/XX./","XXXX/X.X./.XXX/",".XX/X.X/XXX/X.X/"};
		shape[1028] = new String[]{".XX./X.X./XXXX/..X./",".XX./.X.X/XXXX/.X../",".X../XXXX/.X.X/.XX./","..X./XXXX/X.X./.XX./"};
		shape[1029] = new String[]{"..XXX/X.X../XXXX./","XX./X../XXX/X.X/..X/",".XXXX/..X.X/XXX../","X../X.X/XXX/..X/.XX/","XXX../..X.X/.XXXX/","..X/X.X/XXX/X../XX./","XXXX./X.X../..XXX/",".XX/..X/XXX/X.X/X../"};
		shape[1030] = new String[]{"...X/..XX/X.X./XXXX/","XX../X.../XXX./X.XX/","XXXX/.X.X/XX../X.../","XX.X/.XXX/...X/..XX/","X.../XX../.X.X/XXXX/","X.XX/XXX./X.../XX../","XXXX/X.X./..XX/...X/","..XX/...X/.XXX/XX.X/"};
		shape[1031] = new String[]{"..XX/X.X./XXXX/..X./",".XX./.X../XXXX/.X.X/",".X../XXXX/.X.X/XX../","X.X./XXXX/..X./.XX./","XX../.X.X/XXXX/.X../",".X.X/XXXX/.X../.XX./","..X./XXXX/X.X./..XX/",".XX./..X./XXXX/X.X./"};
		shape[1032] = new String[]{"X.X./XXXX/..X./..XX/","..XX/..X./XXXX/X.X./","XX../.X../XXXX/.X.X/",".X.X/XXXX/.X../XX../"};
		shape[1033] = new String[]{"X.../XXXX/..X./XXX./","X.XX/X.X./XXX./..X./",".XXX/.X../XXXX/...X/",".X../.XXX/.X.X/XX.X/","...X/XXXX/.X../.XXX/","..X./XXX./X.X./X.XX/","XXX./..X./XXXX/X.../","XX.X/.X.X/.XXX/.X../"};
		shape[1034] = new String[]{"X.../XXXX/..X./.XX./.X../","...XX/XX.X./.XXX./...X./","..X./.XX./.X../XXXX/...X/",".X.../.XXX./.X.XX/XX.../","...X/XXXX/.X../.XX./..X./","...X./.XXX./XX.X./...XX/",".X../.XX./..X./XXXX/X.../","XX.../.X.XX/.XXX./.X.../"};
		shape[1035] = new String[]{"X.../XXXX/..X./.XXX/","..XX/X.X./XXX./X.X./","XXX./.X../XXXX/...X/",".X.X/.XXX/.X.X/XX../","...X/XXXX/.X../XXX./","X.X./XXX./X.X./..XX/",".XXX/..X./XXXX/X.../","XX../.X.X/.XXX/.X.X/"};
		shape[1036] = new String[]{"X..../XXXX./..X../..XXX/","..XX/..X./XXX./X.X./X.../","XXX../..X../.XXXX/....X/","...X/.X.X/.XXX/.X../XX../","....X/.XXXX/..X../XXX../","X.../X.X./XXX./..X./..XX/","..XXX/..X../XXXX./X..../","XX../.X../.XXX/.X.X/...X/"};
		shape[1037] = new String[]{"X.../XXXX/..X./..XX/...X/","...XX/...X./.XXX./XX.X./","X.../XX../.X../XXXX/...X/",".X.XX/.XXX./.X.../XX.../","...X/XXXX/.X../XX../X.../","XX.X./.XXX./...X./...XX/","...X/..XX/..X./XXXX/X.../","XX.../.X.../.XXX./.X.XX/"};
		shape[1038] = new String[]{"XXX../..XX./.XXXX/","..X/X.X/XXX/XX./X../","XXXX./.XX../..XXX/","..X/.XX/XXX/X.X/X../","..XXX/.XX../XXXX./","X../XX./XXX/X.X/..X/",".XXXX/..XX./XXX../","X../X.X/XXX/.XX/..X/"};
		shape[1039] = new String[]{"X.../XX../.XX./XXXX/","X.XX/XXX./XX../X.../","XXXX/.XX./..XX/...X/","...X/..XX/.XXX/XX.X/","...X/..XX/.XX./XXXX/","X.../XX../XXX./X.XX/","XXXX/.XX./XX../X.../","XX.X/.XXX/..XX/...X/"};
		shape[1040] = new String[]{"XXX./.XX./XXXX/","X.X/XXX/XXX/X../","XXXX/.XX./.XXX/","..X/XXX/XXX/X.X/",".XXX/.XX./XXXX/","X../XXX/XXX/X.X/","XXXX/.XX./XXX./","X.X/XXX/XXX/..X/"};
		shape[1041] = new String[]{"XX../.XX./XXXX/..X./",".X.X/.XXX/XXX./.X../",".X../XXXX/.XX./..XX/","..X./.XXX/XXX./X.X./","..XX/.XX./XXXX/.X../",".X../XXX./.XXX/.X.X/","..X./XXXX/.XX./XX../","X.X./XXX./.XXX/..X./"};
		shape[1042] = new String[]{"X..../XXX../..X../.XXXX/","..XX/X.X./XXX./X.../X.../","XXXX./..X../..XXX/....X/","...X/...X/.XXX/.X.X/XX../","....X/..XXX/..X../XXXX./","X.../X.../XXX./X.X./..XX/",".XXXX/..X../XXX../X..../","XX../.X.X/.XXX/...X/...X/"};
		shape[1043] = new String[]{"XXX../X.X../.XXXX/",".XX/X.X/XXX/X../X../","XXXX./..X.X/..XXX/","..X/..X/XXX/X.X/XX./","..XXX/..X.X/XXXX./","X../X../XXX/X.X/.XX/",".XXXX/X.X../XXX../","XX./X.X/XXX/..X/..X/"};
		shape[1044] = new String[]{".X.../XXX../..X../.XXXX/","..X./X.XX/XXX./X.../X.../","XXXX./..X../..XXX/...X./","...X/...X/.XXX/XX.X/.X../","...X./..XXX/..X../XXXX./","X.../X.../XXX./X.XX/..X./",".XXXX/..X../XXX../.X.../",".X../XX.X/.XXX/...X/...X/"};
		shape[1045] = new String[]{"XXXX./..X../.XXXX/","..X/X.X/XXX/X.X/X../",".XXXX/..X../XXXX./","X../X.X/XXX/X.X/..X/"};
		shape[1046] = new String[]{"XXX../..X../.XXXX/...X./","...X/.X.X/.XXX/XX../.X../",".X.../XXXX./..X../..XXX/","..X./..XX/XXX./X.X./X.../","..XXX/..X../XXXX./.X.../",".X../XX../.XXX/.X.X/...X/","...X./.XXXX/..X../XXX../","X.../X.X./XXX./..XX/..X./"};
		shape[1047] = new String[]{"XX.../.XX../..X../.XXXX/","...X/X.XX/XXX./X.../X.../","XXXX./..X../..XX./...XX/","...X/...X/.XXX/XX.X/X.../","...XX/..XX./..X../XXXX./","X.../X.../XXX./X.XX/...X/",".XXXX/..X../.XX../XX.../","X.../XX.X/.XXX/...X/...X/"};
		shape[1048] = new String[]{"X.../X.../XX../.X../XXXX/","X.XXX/XXX../X..../X..../","XXXX/..X./..XX/...X/...X/","....X/....X/..XXX/XXX.X/","...X/...X/..XX/..X./XXXX/","X..../X..../XXX../X.XXX/","XXXX/.X../XX../X.../X.../","XXX.X/..XXX/....X/....X/"};
		shape[1049] = new String[]{"X.../XXX./.X../XXXX/","X.XX/XXX./X.X./X.../","XXXX/..X./.XXX/...X/","...X/.X.X/.XXX/XX.X/","...X/.XXX/..X./XXXX/","X.../X.X./XXX./X.XX/","XXXX/.X../XXX./X.../","XX.X/.XXX/.X.X/...X/"};
		shape[1050] = new String[]{"X.../XX../.X../XXXX/..X./",".X.XX/.XXX./XX.../.X.../",".X../XXXX/..X./..XX/...X/","...X./...XX/.XXX./XX.X./","...X/..XX/..X./XXXX/.X../",".X.../XX.../.XXX./.X.XX/","..X./XXXX/.X../XX../X.../","XX.X./.XXX./...XX/...X./"};
		shape[1051] = new String[]{"XXXX/.X../XXXX/","X.X/XXX/X.X/X.X/","XXXX/..X./XXXX/","X.X/X.X/XXX/X.X/"};
		shape[1052] = new String[]{"..X./XXX./.X../XXXX/","X.X./XXX./X.XX/X.../","XXXX/..X./.XXX/.X../","...X/XX.X/.XXX/.X.X/",".X../.XXX/..X./XXXX/","X.../X.XX/XXX./X.X./","XXXX/.X../XXX./..X./",".X.X/.XXX/XX.X/...X/"};
		shape[1053] = new String[]{"XXX./.X../XXXX/..X./",".X.X/.XXX/XX.X/.X../",".X../XXXX/..X./.XXX/","..X./X.XX/XXX./X.X./",".XXX/..X./XXXX/.X../",".X../XX.X/.XXX/.X.X/","..X./XXXX/.X../XXX./","X.X./XXX./X.XX/..X./"};
		shape[1054] = new String[]{"XX../.X../XXXX/..X./..X./","..X.X/..XXX/XXX../..X../",".X../.X../XXXX/..X./..XX/","..X../..XXX/XXX../X.X../","..XX/..X./XXXX/.X../.X../","..X../XXX../..XXX/..X.X/","..X./..X./XXXX/.X../XX../","X.X../XXX../..XXX/..X../"};
		shape[1055] = new String[]{"..X./.XXX/.X../XXXX/","X.../XXX./X.XX/X.X./","XXXX/..X./XXX./.X../",".X.X/XX.X/.XXX/...X/",".X../XXX./..X./XXXX/","X.X./X.XX/XXX./X.../","XXXX/.X../.XXX/..X./","...X/.XXX/XX.X/.X.X/"};
		shape[1056] = new String[]{"...X/.XXX/.X../XXXX/","X.../XXX./X.X./X.XX/","XXXX/..X./XXX./X.../","XX.X/.X.X/.XXX/...X/","X.../XXX./..X./XXXX/","X.XX/X.X./XXX./X.../","XXXX/.X../.XXX/...X/","...X/.XXX/.X.X/XX.X/"};
		shape[1057] = new String[]{".XXX/.X../XXXX/..X./",".X../.XXX/XX.X/.X.X/",".X../XXXX/..X./XXX./","X.X./X.XX/XXX./..X./","XXX./..X./XXXX/.X../",".X.X/XX.X/.XXX/.X../","..X./XXXX/.X../.XXX/","..X./XXX./X.XX/X.X./"};
		shape[1058] = new String[]{"..XX/.XX./.X../XXXX/","X.../XXX./X.XX/X..X/","XXXX/..X./.XX./XX../","X..X/XX.X/.XXX/...X/","XX../.XX./..X./XXXX/","X..X/X.XX/XXX./X.../","XXXX/.X../.XX./..XX/","...X/.XXX/XX.X/X..X/"};
		shape[1059] = new String[]{"..X./..X./.XX./.X../XXXX/","X..../XXX../X.XXX/X..../","XXXX/..X./.XX./.X../.X../","....X/XXX.X/..XXX/....X/",".X../.X../.XX./..X./XXXX/","X..../X.XXX/XXX../X..../","XXXX/.X../.XX./..X./..X./","....X/..XXX/XXX.X/....X/"};
		shape[1060] = new String[]{"..X./.XX./.X../XXXX/..X./",".X.../.XXX./XX.XX/.X.../",".X../XXXX/..X./.XX./.X../","...X./XX.XX/.XXX./...X./",".X../.XX./..X./XXXX/.X../",".X.../XX.XX/.XXX./.X.../","..X./XXXX/.X../.XX./..X./","...X./.XXX./XX.XX/...X./"};
		shape[1061] = new String[]{".XX./.X../XXXX/..X./..X./","..X../..XXX/XXX.X/..X../",".X../.X../XXXX/..X./.XX./","..X../X.XXX/XXX../..X../",".XX./..X./XXXX/.X../.X../","..X../XXX.X/..XXX/..X../","..X./..X./XXXX/.X../.XX./","..X../XXX../X.XXX/..X../"};
		shape[1062] = new String[]{"XXX..../..XXX../....XXX/","..X/..X/.XX/.X./XX./X../X../","....XXX/..XXX../XXX..../","X../X../XX./.X./.XX/..X/..X/"};
		shape[1063] = new String[]{"X...../XX..../.XXX../...XXX/","..XX/.XX./.X../XX../X.../X.../","XXX.../..XXX./....XX/.....X/","...X/...X/..XX/..X./.XX./XX../",".....X/....XX/..XXX./XXX.../","X.../X.../XX../.X../.XX./..XX/","...XXX/.XXX../XX..../X...../","XX../.XX./..X./..XX/...X/...X/"};
		shape[1064] = new String[]{"XXX.../.XXX../...XXX/","..X/.XX/.XX/XX./X../X../","XXX.../..XXX./...XXX/","..X/..X/.XX/XX./XX./X../","...XXX/..XXX./XXX.../","X../X../XX./.XX/.XX/..X/","...XXX/.XXX../XXX.../","X../XX./XX./.XX/..X/..X/"};
		shape[1065] = new String[]{".X..../XX..../.XXX../...XXX/","..X./.XXX/.X../XX../X.../X.../","XXX.../..XXX./....XX/....X./","...X/...X/..XX/..X./XXX./.X../","....X./....XX/..XXX./XXX.../","X.../X.../XX../.X../.XXX/..X./","...XXX/.XXX../XX..../.X..../",".X../XXX./..X./..XX/...X/...X/"};
		shape[1066] = new String[]{"XX..../.XXX../.X.XXX/","..X/XXX/.X./XX./X../X../","XXX.X./..XXX./....XX/","..X/..X/.XX/.X./XXX/X../","....XX/..XXX./XXX.X./","X../X../XX./.X./XXX/..X/",".X.XXX/.XXX../XX..../","X../XXX/.X./.XX/..X/..X/"};
		shape[1067] = new String[]{"XX.X../.XXX../...XXX/","..X/.XX/.X./XXX/X../X../","XXX.../..XXX./..X.XX/","..X/..X/XXX/.X./XX./X../","..X.XX/..XXX./XXX.../","X../X../XXX/.X./.XX/..X/","...XXX/.XXX../XX.X../","X../XX./.X./XXX/..X/..X/"};
		shape[1068] = new String[]{"XX..../.XXX../...XXX/...X../","...X/..XX/..X./XXX./.X../.X../","..X.../XXX.../..XXX./....XX/","..X./..X./.XXX/.X../XX../X.../","....XX/..XXX./XXX.../..X.../",".X../.X../XXX./..X./..XX/...X/","...X../...XXX/.XXX../XX..../","X.../XX../.X../.XXX/..X./..X./"};
		shape[1069] = new String[]{"XX..../.XXX../...XXX/....X./","...X/..XX/..X./.XX./XX../.X../",".X..../XXX.../..XXX./....XX/","..X./..XX/.XX./.X../XX../X.../","....XX/..XXX./XXX.../.X..../",".X../XX../.XX./..X./..XX/...X/","....X./...XXX/.XXX../XX..../","X.../XX../.X../.XX./..XX/..X./"};
		shape[1070] = new String[]{"XX..../.XXX.X/...XXX/","..X/.XX/.X./XX./X../XX./","XXX.../X.XXX./....XX/",".XX/..X/.XX/.X./XX./X../","....XX/X.XXX./XXX.../","XX./X../XX./.X./.XX/..X/","...XXX/.XXX.X/XX..../","X../XX./.X./.XX/..X/.XX/"};
		shape[1071] = new String[]{"XX..../.XXX../...XXX/.....X/","...X/..XX/..X./.XX./.X../XX../","X...../XXX.../..XXX./....XX/","..XX/..X./.XX./.X../XX../X.../","....XX/..XXX./XXX.../X...../","XX../.X../.XX./..X./..XX/...X/",".....X/...XXX/.XXX../XX..../","X.../XX../.X../.XX./..X./..XX/"};
		shape[1072] = new String[]{"X..../XX.../XXX../..XXX/",".XXX/.XX./XX../X.../X.../","XXX../..XXX/...XX/....X/","...X/...X/..XX/.XX./XXX./","....X/...XX/..XXX/XXX../","X.../X.../XX../.XX./.XXX/","..XXX/XXX../XX.../X..../","XXX./.XX./..XX/...X/...X/"};
		shape[1073] = new String[]{"XXX../XXX../..XXX/",".XX/.XX/XXX/X../X../","XXX../..XXX/..XXX/","..X/..X/XXX/XX./XX./","..XXX/..XXX/XXX../","X../X../XXX/.XX/.XX/","..XXX/XXX../XXX../","XX./XX./XXX/..X/..X/"};
		shape[1074] = new String[]{".X.../XX.../XXX../..XXX/",".XX./.XXX/XX../X.../X.../","XXX../..XXX/...XX/...X./","...X/...X/..XX/XXX./.XX./","...X./...XX/..XXX/XXX../","X.../X.../XX../.XXX/.XX./","..XXX/XXX../XX.../.X.../",".XX./XXX./..XX/...X/...X/"};
		shape[1075] = new String[]{"XX.../XXX../X.XXX/","XXX/.XX/XX./X../X../","XXX.X/..XXX/...XX/","..X/..X/.XX/XX./XXX/","...XX/..XXX/XXX.X/","X../X../XX./.XX/XXX/","X.XXX/XXX../XX.../","XXX/XX./.XX/..X/..X/"};
		shape[1076] = new String[]{"XX.../XXX../..XXX/..X../","..XX/..XX/XXX./.X../.X../","..X../XXX../..XXX/...XX/","..X./..X./.XXX/XX../XX../","...XX/..XXX/XXX../..X../",".X../.X../XXX./..XX/..XX/","..X../..XXX/XXX../XX.../","XX../XX../.XXX/..X./..X./"};
		shape[1077] = new String[]{"XX.../XXX../..XXX/...X./","..XX/..XX/.XX./XX../.X../",".X.../XXX../..XXX/...XX/","..X./..XX/.XX./XX../XX../","...XX/..XXX/XXX../.X.../",".X../XX../.XX./..XX/..XX/","...X./..XXX/XXX../XX.../","XX../XX../.XX./..XX/..X./"};
		shape[1078] = new String[]{"XX.../XXX.X/..XXX/",".XX/.XX/XX./X../XX./","XXX../X.XXX/...XX/",".XX/..X/.XX/XX./XX./","...XX/X.XXX/XXX../","XX./X../XX./.XX/.XX/","..XXX/XXX.X/XX.../","XX./XX./.XX/..X/.XX/"};
		shape[1079] = new String[]{"XX.../XXX../..XXX/....X/","..XX/..XX/.XX./.X../XX../","X..../XXX../..XXX/...XX/","..XX/..X./.XX./XX../XX../","...XX/..XXX/XXX../X..../","XX../.X../.XX./..XX/..XX/","....X/..XXX/XXX../XX.../","XX../XX../.XX./..X./..XX/"};
		shape[1080] = new String[]{"XX..../.X..../.XXX../...XXX/","...X/.XXX/.X../XX../X.../X.../","XXX.../..XXX./....X./....XX/","...X/...X/..XX/..X./XXX./X.../","....XX/....X./..XXX./XXX.../","X.../X.../XX../.X../.XXX/...X/","...XXX/.XXX../.X..../XX..../","X.../XXX./..X./..XX/...X/...X/"};
		shape[1081] = new String[]{"XX.../X..../XXX../..XXX/",".XXX/.X.X/XX../X.../X.../","XXX../..XXX/....X/...XX/","...X/...X/..XX/X.X./XXX./","...XX/....X/..XXX/XXX../","X.../X.../XX../.X.X/.XXX/","..XXX/XXX../X..../XX.../","XXX./X.X./..XX/...X/...X/"};
		shape[1082] = new String[]{"X..../X.X../XXX../..XXX/",".XXX/.X../XXX./X.../X.../","XXX../..XXX/..X.X/....X/","...X/...X/.XXX/..X./XXX./","....X/..X.X/..XXX/XXX../","X.../X.../XXX./.X../.XXX/","..XXX/XXX../X.X../X..../","XXX./..X./.XXX/...X/...X/"};
		shape[1083] = new String[]{"X..../X..../XXX../..XXX/..X../","..XXX/..X../XXX../.X.../.X.../","..X../XXX../..XXX/....X/....X/","...X./...X./..XXX/..X../XXX../","....X/....X/..XXX/XXX../..X../",".X.../.X.../XXX../..X../..XXX/","..X../..XXX/XXX../X..../X..../","XXX../..X../..XXX/...X./...X./"};
		shape[1084] = new String[]{"X..../X..../XXX../..XXX/...X./","..XXX/..X../.XX../XX.../.X.../",".X.../XXX../..XXX/....X/....X/","...X./...XX/..XX./..X../XXX../","....X/....X/..XXX/XXX../.X.../",".X.../XX.../.XX../..X../..XXX/","...X./..XXX/XXX../X..../X..../","XXX../..X../..XX./...XX/...X./"};
		shape[1085] = new String[]{"X..../X..../XXX.X/..XXX/",".XXX/.X../XX../X.../XX../","XXX../X.XXX/....X/....X/","..XX/...X/..XX/..X./XXX./","....X/....X/X.XXX/XXX../","XX../X.../XX../.X../.XXX/","..XXX/XXX.X/X..../X..../","XXX./..X./..XX/...X/..XX/"};
		shape[1086] = new String[]{"X..../X..../XXX../..XXX/....X/","..XXX/..X../.XX../.X.../XX.../","X..../XXX../..XXX/....X/....X/","...XX/...X./..XX./..X../XXX../","....X/....X/..XXX/XXX../X..../","XX.../.X.../.XX../..X../..XXX/","....X/..XXX/XXX../X..../X..../","XXX../..X../..XX./...X./...XX/"};
		shape[1087] = new String[]{"X.X../XXX../X.XXX/","XXX/.X./XXX/X../X../","XXX.X/..XXX/..X.X/","..X/..X/XXX/.X./XXX/","..X.X/..XXX/XXX.X/","X../X../XXX/.X./XXX/","X.XXX/XXX../X.X../","XXX/.X./XXX/..X/..X/"};
		shape[1088] = new String[]{".X..../.XXX../XX.XXX/","X../XXX/.X./XX./X../X../","XXX.XX/..XXX./....X./","..X/..X/.XX/.X./XXX/..X/","....X./..XXX./XXX.XX/","X../X../XX./.X./XXX/X../","XX.XXX/.XXX../.X..../","..X/XXX/.X./.XX/..X/..X/"};
		shape[1089] = new String[]{"X..../XXX../X.XXX/..X../",".XXX/..X./XXX./.X../.X../","..X../XXX.X/..XXX/....X/","..X./..X./.XXX/.X../XXX./","....X/..XXX/XXX.X/..X../",".X../.X../XXX./..X./.XXX/","..X../X.XXX/XXX../X..../","XXX./.X../.XXX/..X./..X./"};
		shape[1090] = new String[]{"X..../XXX../X.XXX/...X./",".XXX/..X./.XX./XX../.X../",".X.../XXX.X/..XXX/....X/","..X./..XX/.XX./.X../XXX./","....X/..XXX/XXX.X/.X.../",".X../XX../.XX./..X./.XXX/","...X./X.XXX/XXX../X..../","XXX./.X../.XX./..XX/..X./"};
		shape[1091] = new String[]{"X..../XXX.X/X.XXX/","XXX/.X./XX./X../XX./","XXX.X/X.XXX/....X/",".XX/..X/.XX/.X./XXX/","....X/X.XXX/XXX.X/","XX./X../XX./.X./XXX/","X.XXX/XXX.X/X..../","XXX/.X./.XX/..X/.XX/"};
		shape[1092] = new String[]{"X..../XXX../X.XXX/....X/",".XXX/..X./.XX./.X../XX../","X..../XXX.X/..XXX/....X/","..XX/..X./.XX./.X../XXX./","....X/..XXX/XXX.X/X..../","XX../.X../.XX./..X./.XXX/","....X/X.XXX/XXX../X..../","XXX./.X../.XX./..X./..XX/"};
		shape[1093] = new String[]{"X.XX./XXX../..XXX/",".XX/.X./XXX/X.X/X../","XXX../..XXX/.XX.X/","..X/X.X/XXX/.X./XX./",".XX.X/..XXX/XXX../","X../X.X/XXX/.X./.XX/","..XXX/XXX../X.XX./","XX./.X./XXX/X.X/..X/"};
		shape[1094] = new String[]{"X.X../XXX../..XXX/...X./","..XX/..X./.XXX/XX../.X../",".X.../XXX../..XXX/..X.X/","..X./..XX/XXX./.X../XX../","..X.X/..XXX/XXX../.X.../",".X../XX../.XXX/..X./..XX/","...X./..XXX/XXX../X.X../","XX../.X../XXX./..XX/..X./"};
		shape[1095] = new String[]{"X.X../XXX.X/..XXX/",".XX/.X./XXX/X../XX./","XXX../X.XXX/..X.X/",".XX/..X/XXX/.X./XX./","..X.X/X.XXX/XXX../","XX./X../XXX/.X./.XX/","..XXX/XXX.X/X.X../","XX./.X./XXX/..X/.XX/"};
		shape[1096] = new String[]{"X.X../XXX../..XXX/....X/","..XX/..X./.XXX/.X../XX../","X..../XXX../..XXX/..X.X/","..XX/..X./XXX./.X../XX../","..X.X/..XXX/XXX../X..../","XX../.X../.XXX/..X./..XX/","....X/..XXX/XXX../X.X../","XX../.X../XXX./..X./..XX/"};
		shape[1097] = new String[]{"X..../XXX../..XXX/..XX./","..XX/..X./XXX./XX../.X../",".XX../XXX../..XXX/....X/","..X./..XX/.XXX/.X../XX../","....X/..XXX/XXX../.XX../",".X../XX../XXX./..X./..XX/","..XX./..XXX/XXX../X..../","XX../.X../.XXX/..XX/..X./"};
		shape[1098] = new String[]{"X..../XXX.X/..XXX/..X../","..XX/..X./XXX./.X../.XX./","..X../XXX../X.XXX/....X/",".XX./..X./.XXX/.X../XX../","....X/X.XXX/XXX../..X../",".XX./.X../XXX./..X./..XX/","..X../..XXX/XXX.X/X..../","XX../.X../.XXX/..X./.XX./"};
		shape[1099] = new String[]{"X..../XXX../..XXX/.XX../","..XX/X.X./XXX./.X../.X../","..XX./XXX../..XXX/....X/","..X./..X./.XXX/.X.X/XX../","....X/..XXX/XXX../..XX./",".X../.X../XXX./X.X./..XX/",".XX../..XXX/XXX../X..../","XX../.X.X/.XXX/..X./..X./"};
		shape[1100] = new String[]{"X..../XXX.X/..XXX/...X./","..XX/..X./.XX./XX../.XX./",".X.../XXX../X.XXX/....X/",".XX./..XX/.XX./.X../XX../","....X/X.XXX/XXX../.X.../",".XX./XX../.XX./..X./..XX/","...X./..XXX/XXX.X/X..../","XX../.X../.XX./..XX/.XX./"};
		shape[1101] = new String[]{"X..../XXX../..XXX/...X./...X./","...XX/...X./..XX./XXX../..X../",".X.../.X.../XXX../..XXX/....X/","..X../..XXX/.XX../.X.../XX.../","....X/..XXX/XXX../.X.../.X.../","..X../XXX../..XX./...X./...XX/","...X./...X./..XXX/XXX../X..../","XX.../.X.../.XX../..XXX/..X../"};
		shape[1102] = new String[]{"X...../XXX.XX/..XXX./",".XX/.X./XX./X../XX./.X./",".XXX../XX.XXX/.....X/",".X./.XX/..X/.XX/.X./XX./",".....X/XX.XXX/.XXX../",".X./XX./X../XX./.X./.XX/","..XXX./XXX.XX/X...../","XX./.X./.XX/..X/.XX/.X./"};
		shape[1103] = new String[]{"X...X/XXX.X/..XXX/",".XX/.X./XX./X../XXX/","XXX../X.XXX/X...X/","XXX/..X/.XX/.X./XX./","X...X/X.XXX/XXX../","XXX/X../XX./.X./.XX/","..XXX/XXX.X/X...X/","XX./.X./.XX/..X/XXX/"};
		shape[1104] = new String[]{".XX../XXX../X.XXX/","XX./.XX/XXX/X../X../","XXX.X/..XXX/..XX./","..X/..X/XXX/XX./.XX/","..XX./..XXX/XXX.X/","X../X../XXX/.XX/XX./","X.XXX/XXX../.XX../",".XX/XX./XXX/..X/..X/"};
		shape[1105] = new String[]{".X.../.X.../XXX../X.XXX/","XX../.XXX/XX../X.../X.../","XXX.X/..XXX/...X./...X./","...X/...X/..XX/XXX./..XX/","...X./...X./..XXX/XXX.X/","X.../X.../XX../.XXX/XX../","X.XXX/XXX../.X.../.X.../","..XX/XXX./..XX/...X/...X/"};
		shape[1106] = new String[]{"..X.../.XXX../XX.XXX/","X../XX./.XX/XX./X../X../","XXX.XX/..XXX./...X../","..X/..X/.XX/XX./.XX/..X/","...X../..XXX./XXX.XX/","X../X../XX./.XX/XX./X../","XX.XXX/.XXX../..X.../","..X/.XX/XX./.XX/..X/..X/"};
		shape[1107] = new String[]{".X.../XXX../X.XXX/X..../","XXX./..XX/.XX./.X../.X../","....X/XXX.X/..XXX/...X./","..X./..X./.XX./XX../.XXX/","...X./..XXX/XXX.X/....X/",".X../.X../.XX./..XX/XXX./","X..../X.XXX/XXX../.X.../",".XXX/XX../.XX./..X./..X./"};
		shape[1108] = new String[]{".X.../XXX../X.XXX/..X../",".XX./..XX/XXX./.X../.X../","..X../XXX.X/..XXX/...X./","..X./..X./.XXX/XX../.XX./","...X./..XXX/XXX.X/..X../",".X../.X../XXX./..XX/.XX./","..X../X.XXX/XXX../.X.../",".XX./XX../.XXX/..X./..X./"};
		shape[1109] = new String[]{".X.../XXX../X.XXX/...X./",".XX./..XX/.XX./XX../.X../",".X.../XXX.X/..XXX/...X./","..X./..XX/.XX./XX../.XX./","...X./..XXX/XXX.X/.X.../",".X../XX../.XX./..XX/.XX./","...X./X.XXX/XXX../.X.../",".XX./XX../.XX./..XX/..X./"};
		shape[1110] = new String[]{".X.../XXX.X/X.XXX/","XX./.XX/XX./X../XX./","XXX.X/X.XXX/...X./",".XX/..X/.XX/XX./.XX/","...X./X.XXX/XXX.X/","XX./X../XX./.XX/XX./","X.XXX/XXX.X/.X.../",".XX/XX./.XX/..X/.XX/"};
		shape[1111] = new String[]{"..XX./XXX../X.XXX/","XX./.X./XXX/X.X/X../","XXX.X/..XXX/.XX../","..X/X.X/XXX/.X./.XX/",".XX../..XXX/XXX.X/","X../X.X/XXX/.X./XX./","X.XXX/XXX../..XX./",".XX/.X./XXX/X.X/..X/"};
		shape[1112] = new String[]{"...X../.XXX../XX.XXX/","X../XX./.X./XXX/X../X../","XXX.XX/..XXX./..X.../","..X/..X/XXX/.X./.XX/..X/","..X.../..XXX./XXX.XX/","X../X../XXX/.X./XX./X../","XX.XXX/.XXX../...X../","..X/.XX/.X./XXX/..X/..X/"};
		shape[1113] = new String[]{"..X../XXX../X.XXX/X..../","XXX./..X./.XXX/.X../.X../","....X/XXX.X/..XXX/..X../","..X./..X./XXX./.X../.XXX/","..X../..XXX/XXX.X/....X/",".X../.X../.XXX/..X./XXX./","X..../X.XXX/XXX../..X../",".XXX/.X../XXX./..X./..X./"};
		shape[1114] = new String[]{"..X../XXX../X.XXX/...X./",".XX./..X./.XXX/XX../.X../",".X.../XXX.X/..XXX/..X../","..X./..XX/XXX./.X../.XX./","..X../..XXX/XXX.X/.X.../",".X../XX../.XXX/..X./.XX./","...X./X.XXX/XXX../..X../",".XX./.X../XXX./..XX/..X./"};
		shape[1115] = new String[]{"..X../XXX.X/X.XXX/","XX./.X./XXX/X../XX./","XXX.X/X.XXX/..X../",".XX/..X/XXX/.X./.XX/","..X../X.XXX/XXX.X/","XX./X../XXX/.X./XX./","X.XXX/XXX.X/..X../",".XX/.X./XXX/..X/.XX/"};
		shape[1116] = new String[]{"..XXX../XXX.XXX/","X./X./XX/.X/XX/X./X./","XXX.XXX/..XXX../",".X/.X/XX/X./XX/.X/.X/"};
		shape[1117] = new String[]{".XXX../XX.XXX/X...../","XX./.XX/..X/.XX/.X./.X./",".....X/XXX.XX/..XXX./",".X./.X./XX./X../XX./.XX/","..XXX./XXX.XX/.....X/",".X./.X./.XX/..X/.XX/XX./","X...../XX.XXX/.XXX../",".XX/XX./X../XX./.X./.X./"};
		shape[1118] = new String[]{".XXX../XX.XXX/.X..../",".X./XXX/..X/.XX/.X./.X./","....X./XXX.XX/..XXX./",".X./.X./XX./X../XXX/.X./","..XXX./XXX.XX/....X./",".X./.X./.XX/..X/XXX/.X./",".X..../XX.XXX/.XXX../",".X./XXX/X../XX./.X./.X./"};
		shape[1119] = new String[]{".XXX../XX.XXX/...X../",".X./.XX/..X/XXX/.X./.X./","..X.../XXX.XX/..XXX./",".X./.X./XXX/X../XX./.X./","..XXX./XXX.XX/..X.../",".X./.X./XXX/..X/.XX/.X./","...X../XX.XXX/.XXX../",".X./XX./X../XXX/.X./.X./"};
		shape[1120] = new String[]{".XXX../XX.XXX/....X./",".X./.XX/..X/.XX/XX./.X./",".X..../XXX.XX/..XXX./",".X./.XX/XX./X../XX./.X./","..XXX./XXX.XX/.X..../",".X./XX./.XX/..X/.XX/.X./","....X./XX.XXX/.XXX../",".X./XX./X../XX./.XX/.X./"};
		shape[1121] = new String[]{".XXX.X/XX.XXX/","X./XX/.X/XX/X./XX/","XXX.XX/X.XXX./","XX/.X/XX/X./XX/.X/","X.XXX./XXX.XX/","XX/X./XX/.X/XX/X./","XX.XXX/.XXX.X/",".X/XX/X./XX/.X/XX/"};
		shape[1122] = new String[]{"XXX../X.XXX/X.X../","XXX/..X/XXX/.X./.X./","..X.X/XXX.X/..XXX/",".X./.X./XXX/X../XXX/","..XXX/XXX.X/..X.X/",".X./.X./XXX/..X/XXX/","X.X../X.XXX/XXX../","XXX/X../XXX/.X./.X./"};
		shape[1123] = new String[]{"XXX../X.XXX/X..X./","XXX/..X/.XX/XX./.X./",".X..X/XXX.X/..XXX/",".X./.XX/XX./X../XXX/","..XXX/XXX.X/.X..X/",".X./XX./.XX/..X/XXX/","X..X./X.XXX/XXX../","XXX/X../XX./.XX/.X./"};
		shape[1124] = new String[]{"XXX.X/X.XXX/X..../","XXX/..X/.XX/.X./.XX/","....X/XXX.X/X.XXX/","XX./.X./XX./X../XXX/","X.XXX/XXX.X/....X/",".XX/.X./.XX/..X/XXX/","X..../X.XXX/XXX.X/","XXX/X../XX./.X./XX./"};
		shape[1125] = new String[]{".XXX../.X.XXX/XX..../","X../XXX/..X/.XX/.X./.X./","....XX/XXX.X./..XXX./",".X./.X./XX./X../XXX/..X/","..XXX./XXX.X./....XX/",".X./.X./.XX/..X/XXX/X../","XX..../.X.XXX/.XXX../","..X/XXX/X../XX./.X./.X./"};
		shape[1126] = new String[]{"XXX../X.XXX/XX.../","XXX/X.X/.XX/.X./.X./","...XX/XXX.X/..XXX/",".X./.X./XX./X.X/XXX/","..XXX/XXX.X/...XX/",".X./.X./.XX/X.X/XXX/","XX.../X.XXX/XXX../","XXX/X.X/XX./.X./.X./"};
		shape[1127] = new String[]{"XXX../X.XXX/..XX./",".XX/..X/XXX/XX./.X./",".XX../XXX.X/..XXX/",".X./.XX/XXX/X../XX./","..XXX/XXX.X/.XX../",".X./XX./XXX/..X/.XX/","..XX./X.XXX/XXX../","XX./X../XXX/.XX/.X./"};
		shape[1128] = new String[]{"XXX../X.XXX/.XX../",".XX/X.X/XXX/.X./.X./","..XX./XXX.X/..XXX/",".X./.X./XXX/X.X/XX./","..XXX/XXX.X/..XX./",".X./.X./XXX/X.X/.XX/",".XX../X.XXX/XXX../","XX./X.X/XXX/.X./.X./"};
		shape[1129] = new String[]{"XXX../X.XXX/...X./...X./","..XX/...X/..XX/XXX./..X./",".X.../.X.../XXX.X/..XXX/",".X../.XXX/XX../X.../XX../","..XXX/XXX.X/.X.../.X.../","..X./XXX./..XX/...X/..XX/","...X./...X./X.XXX/XXX../","XX../X.../XX../.XXX/.X../"};
		shape[1130] = new String[]{".X.../.XX../XXX../..XXX/",".X../.XXX/XXX./X.../X.../","XXX../..XXX/..XX./...X./","...X/...X/.XXX/XXX./..X./","...X./..XX./..XXX/XXX../","X.../X.../XXX./.XXX/.X../","..XXX/XXX../.XX../.X.../","..X./XXX./.XXX/...X/...X/"};
		shape[1131] = new String[]{".XXX./XXX../..XXX/",".X./.XX/XXX/X.X/X../","XXX../..XXX/.XXX./","..X/X.X/XXX/XX./.X./",".XXX./..XXX/XXX../","X../X.X/XXX/.XX/.X./","..XXX/XXX../.XXX./",".X./XX./XXX/X.X/..X/"};
		shape[1132] = new String[]{".XX../XXX../..XXX/...X./","..X./..XX/.XXX/XX../.X../",".X.../XXX../..XXX/..XX./","..X./..XX/XXX./XX../.X../","..XX./..XXX/XXX../.X.../",".X../XX../.XXX/..XX/..X./","...X./..XXX/XXX../.XX../",".X../XX../XXX./..XX/..X./"};
		shape[1133] = new String[]{"XX.../.X.../XXX../..XXX/",".X.X/.XXX/XX../X.../X.../","XXX../..XXX/...X./...XX/","...X/...X/..XX/XXX./X.X./","...XX/...X./..XXX/XXX../","X.../X.../XX../.XXX/.X.X/","..XXX/XXX../.X.../XX.../","X.X./XXX./..XX/...X/...X/"};
		shape[1134] = new String[]{".XX../.X.../XXX../..XXX/",".X../.XXX/XX.X/X.../X.../","XXX../..XXX/...X./..XX./","...X/...X/X.XX/XXX./..X./","..XX./...X./..XXX/XXX../","X.../X.../XX.X/.XXX/.X../","..XXX/XXX../.X.../.XX../","..X./XXX./X.XX/...X/...X/"};
		shape[1135] = new String[]{".X.../.X.../XXX../..XXX/..X../","..X../..XXX/XXX../.X.../.X.../","..X../XXX../..XXX/...X./...X./","...X./...X./..XXX/XXX../..X../"};
		shape[1136] = new String[]{".X.../.X.../XXX../..XXX/...X./","..X../..XXX/.XX../XX.../.X.../",".X.../XXX../..XXX/...X./...X./","...X./...XX/..XX./XXX../..X../","...X./...X./..XXX/XXX../.X.../",".X.../XX.../.XX../..XXX/..X../","...X./..XXX/XXX../.X.../.X.../","..X../XXX../..XX./...XX/...X./"};
		shape[1137] = new String[]{".X.../XXX../..XXX/.XX../","..X./X.XX/XXX./.X../.X../","..XX./XXX../..XXX/...X./","..X./..X./.XXX/XX.X/.X../","...X./..XXX/XXX../..XX./",".X../.X../XXX./X.XX/..X./",".XX../..XXX/XXX../.X.../",".X../XX.X/.XXX/..X./..X./"};
		shape[1138] = new String[]{"..XXX/XXX../..XXX/",".X./.X./XXX/X.X/X.X/","XXX../..XXX/XXX../","X.X/X.X/XXX/.X./.X./"};
		shape[1139] = new String[]{"...X./..XX./XXX../..XXX/",".X../.X../XXX./X.XX/X.../","XXX../..XXX/.XX../.X.../","...X/XX.X/.XXX/..X./..X./",".X.../.XX../..XXX/XXX../","X.../X.XX/XXX./.X../.X../","..XXX/XXX../..XX./...X./","..X./..X./.XXX/XX.X/...X/"};
		shape[1140] = new String[]{"X...../XXX.../..XX../...XXX/","..XX/..X./.XX./XX../X.../X.../","XXX.../..XX../...XXX/.....X/","...X/...X/..XX/.XX./.X../XX../",".....X/...XXX/..XX../XXX.../","X.../X.../XX../.XX./..X./..XX/","...XXX/..XX../XXX.../X...../","XX../.X../.XX./..XX/...X/...X/"};
		shape[1141] = new String[]{"XXX.../X.XX../...XXX/",".XX/..X/.XX/XX./X../X../","XXX.../..XX.X/...XXX/","..X/..X/.XX/XX./X../XX./","...XXX/..XX.X/XXX.../","X../X../XX./.XX/..X/.XX/","...XXX/X.XX../XXX.../","XX./X../XX./.XX/..X/..X/"};
		shape[1142] = new String[]{".X..../XXX.../..XX../...XXX/","..X./..XX/.XX./XX../X.../X.../","XXX.../..XX../...XXX/....X./","...X/...X/..XX/.XX./XX../.X../","....X./...XXX/..XX../XXX.../","X.../X.../XX../.XX./..XX/..X./","...XXX/..XX../XXX.../.X..../",".X../XX../.XX./..XX/...X/...X/"};
		shape[1143] = new String[]{"..X.../XXX.../..XX../...XXX/","..X./..X./.XXX/XX../X.../X.../","XXX.../..XX../...XXX/...X../","...X/...X/..XX/XXX./.X../.X../","...X../...XXX/..XX../XXX.../","X.../X.../XX../.XXX/..X./..X./","...XXX/..XX../XXX.../..X.../",".X../.X../XXX./..XX/...X/...X/"};
		shape[1144] = new String[]{"XX..../.XX.../..XX../...XXX/","...X/..XX/.XX./XX../X.../X.../","XXX.../..XX../...XX./....XX/","...X/...X/..XX/.XX./XX../X.../","....XX/...XX./..XX../XXX.../","X.../X.../XX../.XX./..XX/...X/","...XXX/..XX../.XX.../XX..../","X.../XX../.XX./..XX/...X/...X/"};
		shape[1145] = new String[]{"XX.../XX.../.XX../..XXX/","..XX/.XXX/XX../X.../X.../","XXX../..XX./...XX/...XX/","...X/...X/..XX/XXX./XX../","...XX/...XX/..XX./XXX../","X.../X.../XX../.XXX/..XX/","..XXX/.XX../XX.../XX.../","XX../XXX./..XX/...X/...X/"};
		shape[1146] = new String[]{"X..../X..../XX.../.XX../..XXX/","..XXX/.XX../XX.../X..../X..../","XXX../..XX./...XX/....X/....X/","....X/....X/...XX/..XX./XXX../"};
		shape[1147] = new String[]{"X..../XXX../.XX../..XXX/","..XX/.XX./XXX./X.../X.../","XXX../..XX./..XXX/....X/","...X/...X/.XXX/.XX./XX../","....X/..XXX/..XX./XXX../","X.../X.../XXX./.XX./..XX/","..XXX/.XX../XXX../X..../","XX../.XX./.XXX/...X/...X/"};
		shape[1148] = new String[]{"X..../XX.../.XXX./..XXX/","..XX/.XX./XX../XX../X.../","XXX../.XXX./...XX/....X/","...X/..XX/..XX/.XX./XX../","....X/...XX/.XXX./XXX../","X.../XX../XX../.XX./..XX/","..XXX/.XXX./XX.../X..../","XX../.XX./..XX/..XX/...X/"};
		shape[1149] = new String[]{"X..../XX.../.XX../..XXX/..X../","...XX/..XX./XXX../.X.../.X.../","..X../XXX../..XX./...XX/....X/","...X./...X./..XXX/.XX../XX.../","....X/...XX/..XX./XXX../..X../",".X.../.X.../XXX../..XX./...XX/","..X../..XXX/.XX../XX.../X..../","XX.../.XX../..XXX/...X./...X./"};
		shape[1150] = new String[]{"X..../XX.../.XX../..XXX/...X./","...XX/..XX./.XX../XX.../.X.../",".X.../XXX../..XX./...XX/....X/","...X./...XX/..XX./.XX../XX.../","....X/...XX/..XX./XXX../.X.../",".X.../XX.../.XX../..XX./...XX/","...X./..XXX/.XX../XX.../X..../","XX.../.XX../..XX./...XX/...X./"};
		shape[1151] = new String[]{"X..../XX.../.XX.X/..XXX/","..XX/.XX./XX../X.../XX../","XXX../X.XX./...XX/....X/","..XX/...X/..XX/.XX./XX../","....X/...XX/X.XX./XXX../","XX../X.../XX../.XX./..XX/","..XXX/.XX.X/XX.../X..../","XX../.XX./..XX/...X/..XX/"};
		shape[1152] = new String[]{"X..../XX.../.XX../..XXX/....X/","...XX/..XX./.XX../.X.../XX.../","X..../XXX../..XX./...XX/....X/","...XX/...X./..XX./.XX../XX.../","....X/...XX/..XX./XXX../X..../","XX.../.X.../.XX../..XX./...XX/","....X/..XXX/.XX../XX.../X..../","XX.../.XX../..XX./...X./...XX/"};
		shape[1153] = new String[]{".X.../XXX../.XX../..XXX/","..X./.XXX/XXX./X.../X.../","XXX../..XX./..XXX/...X./","...X/...X/.XXX/XXX./.X../","...X./..XXX/..XX./XXX../","X.../X.../XXX./.XXX/..X./","..XXX/.XX../XXX../.X.../",".X../XXX./.XXX/...X/...X/"};
		shape[1154] = new String[]{"XXX../.XXX./..XXX/","..X/.XX/XXX/XX./X../","..XXX/.XXX./XXX../","X../XX./XXX/.XX/..X/"};
		shape[1155] = new String[]{"XXX../.XX../..XXX/...X./","...X/..XX/.XXX/XX../.X../",".X.../XXX../..XX./..XXX/","..X./..XX/XXX./XX../X.../","..XXX/..XX./XXX../.X.../",".X../XX../.XXX/..XX/...X/","...X./..XXX/.XX../XXX../","X.../XX../XXX./..XX/..X./"};
		shape[1156] = new String[]{"XXX../.XX.X/..XXX/","..X/.XX/XXX/X../XX./","XXX../X.XX./..XXX/",".XX/..X/XXX/XX./X../","..XXX/X.XX./XXX../","XX./X../XXX/.XX/..X/","..XXX/.XX.X/XXX../","X../XX./XXX/..X/.XX/"};
		shape[1157] = new String[]{"XXX../.XX../..XXX/....X/","...X/..XX/.XXX/.X../XX../","X..../XXX../..XX./..XXX/","..XX/..X./XXX./XX../X.../","..XXX/..XX./XXX../X..../","XX../.X../.XXX/..XX/...X/","....X/..XXX/.XX../XXX../","X.../XX../XXX./..X./..XX/"};
		shape[1158] = new String[]{".XX../XX.../.XX../..XXX/","..X./.XXX/XX.X/X.../X.../","XXX../..XX./...XX/..XX./","...X/...X/X.XX/XXX./.X../","..XX./...XX/..XX./XXX../","X.../X.../XX.X/.XXX/..X./","..XXX/.XX../XX.../.XX../",".X../XXX./X.XX/...X/...X/"};
		shape[1159] = new String[]{".X.../XX.../.XXX./..XXX/","..X./.XXX/XX../XX../X.../","XXX../.XXX./...XX/...X./","...X/..XX/..XX/XXX./.X../","...X./...XX/.XXX./XXX../","X.../XX../XX../.XXX/..X./","..XXX/.XXX./XX.../.X.../",".X../XXX./..XX/..XX/...X/"};
		shape[1160] = new String[]{".X.../XX.../.XX../..XXX/...X./","...X./..XXX/.XX../XX.../.X.../",".X.../XXX../..XX./...XX/...X./","...X./...XX/..XX./XXX../.X.../"};
		shape[1161] = new String[]{".X.../XX.../.XX.X/..XXX/","..X./.XXX/XX../X.../XX../","XXX../X.XX./...XX/...X./","..XX/...X/..XX/XXX./.X../","...X./...XX/X.XX./XXX../","XX../X.../XX../.XXX/..X./","..XXX/.XX.X/XX.../.X.../",".X../XXX./..XX/...X/..XX/"};
		shape[1162] = new String[]{".X.../XX.../.XX../..XXX/....X/","...X./..XXX/.XX../.X.../XX.../","X..../XXX../..XX./...XX/...X./","...XX/...X./..XX./XXX../.X.../","...X./...XX/..XX./XXX../X..../","XX.../.X.../.XX../..XXX/...X./","....X/..XXX/.XX../XX.../.X.../",".X.../XXX../..XX./...X./...XX/"};
		shape[1163] = new String[]{"XX.X./.XXX./..XXX/","..X/.XX/XX./XXX/X../","XXX../.XXX./.X.XX/","..X/XXX/.XX/XX./X../",".X.XX/.XXX./XXX../","X../XXX/XX./.XX/..X/","..XXX/.XXX./XX.X./","X../XX./.XX/XXX/..X/"};
		shape[1164] = new String[]{"XX.../.XXX./..XXX/..X../","...X/..XX/XXX./.XX./.X../","..X../XXX../.XXX./...XX/","..X./.XX./.XXX/XX../X.../","...XX/.XXX./XXX../..X../",".X../.XX./XXX./..XX/...X/","..X../..XXX/.XXX./XX.../","X.../XX../.XXX/.XX./..X./"};
		shape[1165] = new String[]{"XX.../.XXX./..XXX/...X./","...X/..XX/.XX./XXX./.X../",".X.../XXX../.XXX./...XX/","..X./.XXX/.XX./XX../X.../","...XX/.XXX./XXX../.X.../",".X../XXX./.XX./..XX/...X/","...X./..XXX/.XXX./XX.../","X.../XX../.XX./.XXX/..X./"};
		shape[1166] = new String[]{"XX.../.XXX./..XXX/....X/","...X/..XX/.XX./.XX./XX../","X..../XXX../.XXX./...XX/","..XX/.XX./.XX./XX../X.../","...XX/.XXX./XXX../X..../","XX../.XX./.XX./..XX/...X/","....X/..XXX/.XXX./XX.../","X.../XX../.XX./.XX./..XX/"};
		shape[1167] = new String[]{"XX.../.XX../..XXX/..XX./","...X/..XX/XXX./XX../.X../",".XX../XXX../..XX./...XX/","..X./..XX/.XXX/XX../X.../","...XX/..XX./XXX../.XX../",".X../XX../XXX./..XX/...X/","..XX./..XXX/.XX../XX.../","X.../XX../.XXX/..XX/..X./"};
		shape[1168] = new String[]{"XX.../.XX.X/..XXX/..X../","...X/..XX/XXX./.X../.XX./","..X../XXX../X.XX./...XX/",".XX./..X./.XXX/XX../X.../","...XX/X.XX./XXX../..X../",".XX./.X../XXX./..XX/...X/","..X../..XXX/.XX.X/XX.../","X.../XX../.XXX/..X./.XX./"};
		shape[1169] = new String[]{"XX.../.XX../..XXX/..X.X/","...X/..XX/XXX./.X../XX../","X.X../XXX../..XX./...XX/","..XX/..X./.XXX/XX../X.../","...XX/..XX./XXX../X.X../","XX../.X../XXX./..XX/...X/","..X.X/..XXX/.XX../XX.../","X.../XX../.XXX/..X./..XX/"};
		shape[1170] = new String[]{"XX.../.XX../..XXX/.XX../","...X/X.XX/XXX./.X../.X../","..XX./XXX../..XX./...XX/","..X./..X./.XXX/XX.X/X.../","...XX/..XX./XXX../..XX./",".X../.X../XXX./X.XX/...X/",".XX../..XXX/.XX../XX.../","X.../XX.X/.XXX/..X./..X./"};
		shape[1171] = new String[]{"XX.../.XX.X/..XXX/...X./","...X/..XX/.XX./XX../.XX./",".X.../XXX../X.XX./...XX/",".XX./..XX/.XX./XX../X.../","...XX/X.XX./XXX../.X.../",".XX./XX../.XX./..XX/...X/","...X./..XXX/.XX.X/XX.../","X.../XX../.XX./..XX/.XX./"};
		shape[1172] = new String[]{"XX.../.XX../..XXX/...XX/","...X/..XX/.XX./XX../XX../","XX.../XXX../..XX./...XX/","..XX/..XX/.XX./XX../X.../","...XX/..XX./XXX../XX.../","XX../XX../.XX./..XX/...X/","...XX/..XXX/.XX../XX.../","X.../XX../.XX./..XX/..XX/"};
		shape[1173] = new String[]{"XX..../.XX.XX/..XXX./","..X/.XX/XX./X../XX./.X./",".XXX../XX.XX./....XX/",".X./.XX/..X/.XX/XX./X../","....XX/XX.XX./.XXX../",".X./XX./X../XX./.XX/..X/","..XXX./.XX.XX/XX..../","X../XX./.XX/..X/.XX/.X./"};
		shape[1174] = new String[]{"XX..X/.XX.X/..XXX/","..X/.XX/XX./X../XXX/","XXX../X.XX./X..XX/","XXX/..X/.XX/XX./X../","X..XX/X.XX./XXX../","XXX/X../XX./.XX/..X/","..XXX/.XX.X/XX..X/","X../XX./.XX/..X/XXX/"};
		shape[1175] = new String[]{"XX.../.XX.X/..XXX/....X/","...X/..XX/.XX./.X../XXX./","X..../XXX../X.XX./...XX/",".XXX/..X./.XX./XX../X.../","...XX/X.XX./XXX../X..../","XXX./.X../.XX./..XX/...X/","....X/..XXX/.XX.X/XX.../","X.../XX../.XX./..X./.XXX/"};
		shape[1176] = new String[]{"XX..../.XX.../..XXX./....XX/","...X/..XX/.XX./.X../XX../X.../","XX..../.XXX../...XX./....XX/","...X/..XX/..X./.XX./XX../X.../","....XX/...XX./.XXX../XX..../","X.../XX../.X../.XX./..XX/...X/","....XX/..XXX./.XX.../XX..../","X.../XX../.XX./..X./..XX/...X/"};
		shape[1177] = new String[]{"XX.../.XX../..XXX/....X/....X/","....X/...XX/..XX./..X../XXX../","X..../X..../XXX../..XX./...XX/","..XXX/..X../.XX../XX.../X..../","...XX/..XX./XXX../X..../X..../","XXX../..X../..XX./...XX/....X/","....X/....X/..XXX/.XX../XX.../","X..../XX.../.XX../..X../..XXX/"};
		shape[1178] = new String[]{"XX.../.XX../.XX../..XXX/","...X/.XXX/XXX./X.../X.../","XXX../..XX./..XX./...XX/","...X/...X/.XXX/XXX./X.../","...XX/..XX./..XX./XXX../","X.../X.../XXX./.XXX/...X/","..XXX/.XX../.XX../XX.../","X.../XXX./.XXX/...X/...X/"};
		shape[1179] = new String[]{"X.../XXX./XX../.XXX/",".XXX/XXX./X.X./X.../","XXX./..XX/.XXX/...X/","...X/.X.X/.XXX/XXX./","...X/.XXX/..XX/XXX./","X.../X.X./XXX./.XXX/",".XXX/XX../XXX./X.../","XXX./.XXX/.X.X/...X/"};
		shape[1180] = new String[]{"X.../XX../XXX./.XXX/",".XXX/XXX./XX../X.../","XXX./.XXX/..XX/...X/","...X/..XX/.XXX/XXX./"};
		shape[1181] = new String[]{"X.../XX../XX.X/.XXX/",".XXX/XXX./X.../XX../","XXX./X.XX/..XX/...X/","..XX/...X/.XXX/XXX./","...X/..XX/X.XX/XXX./","XX../X.../XXX./.XXX/",".XXX/XX.X/XX../X.../","XXX./.XXX/...X/..XX/"};
		shape[1182] = new String[]{"X.../XX../XX../.XXX/...X/","..XXX/.XXX./.X.../XX.../","X.../XXX./..XX/..XX/...X/","...XX/...X./.XXX./XXX../","...X/..XX/..XX/XXX./X.../","XX.../.X.../.XXX./..XXX/","...X/.XXX/XX../XX../X.../","XXX../.XXX./...X./...XX/"};
		shape[1183] = new String[]{"..X./XXX./XX../.XXX/",".XX./XXX./X.XX/X.../","XXX./..XX/.XXX/.X../","...X/XX.X/.XXX/.XX./",".X../.XXX/..XX/XXX./","X.../X.XX/XXX./.XX./",".XXX/XX../XXX./..X./",".XX./.XXX/XX.X/...X/"};
		shape[1184] = new String[]{"XXX./XXX./.XXX/",".XX/XXX/XXX/X../","XXX./.XXX/.XXX/","..X/XXX/XXX/XX./",".XXX/.XXX/XXX./","X../XXX/XXX/.XX/",".XXX/XXX./XXX./","XX./XXX/XXX/..X/"};
		shape[1185] = new String[]{"XXX./XX../.XXX/..X./","..XX/.XXX/XX.X/.X../",".X../XXX./..XX/.XXX/","..X./X.XX/XXX./XX../",".XXX/..XX/XXX./.X../",".X../XX.X/.XXX/..XX/","..X./.XXX/XX../XXX./","XX../XXX./X.XX/..X./"};
		shape[1186] = new String[]{"XXX./XX.X/.XXX/",".XX/XXX/X.X/XX./","XXX./X.XX/.XXX/",".XX/X.X/XXX/XX./",".XXX/X.XX/XXX./","XX./X.X/XXX/.XX/",".XXX/XX.X/XXX./","XX./XXX/X.X/.XX/"};
		shape[1187] = new String[]{"XXX./XX../.XXX/...X/","..XX/.XXX/.X.X/XX../","X.../XXX./..XX/.XXX/","..XX/X.X./XXX./XX../",".XXX/..XX/XXX./X.../","XX../.X.X/.XXX/..XX/","...X/.XXX/XX../XXX./","XX../XXX./X.X./..XX/"};
		shape[1188] = new String[]{"XX../XXX./.XXX/..X./","..XX/.XXX/XXX./.X../",".X../XXX./.XXX/..XX/","..X./.XXX/XXX./XX../"};
		shape[1189] = new String[]{"XX../XXX./.XXX/...X/","..XX/.XXX/.XX./XX../","X.../XXX./.XXX/..XX/","..XX/.XX./XXX./XX../","..XX/.XXX/XXX./X.../","XX../.XX./.XXX/..XX/","...X/.XXX/XXX./XX../","XX../XXX./.XX./..XX/"};
		shape[1190] = new String[]{"XX../XX.X/.XXX/..X./","..XX/.XXX/XX../.XX./",".X../XXX./X.XX/..XX/",".XX./..XX/XXX./XX../","..XX/X.XX/XXX./.X../",".XX./XX../.XXX/..XX/","..X./.XXX/XX.X/XX../","XX../XXX./..XX/.XX./"};
		shape[1191] = new String[]{"XX../XX../.XXX/..XX/","..XX/.XXX/XX../XX../","XX../XXX./..XX/..XX/","..XX/..XX/XXX./XX../"};
		shape[1192] = new String[]{"XX.../XX.XX/.XXX./",".XX/XXX/X../XX./.X./",".XXX./XX.XX/...XX/",".X./.XX/..X/XXX/XX./","...XX/XX.XX/.XXX./",".X./XX./X../XXX/.XX/",".XXX./XX.XX/XX.../","XX./XXX/..X/.XX/.X./"};
		shape[1193] = new String[]{"XX.X/XX.X/.XXX/",".XX/XXX/X../XXX/","XXX./X.XX/X.XX/","XXX/..X/XXX/XX./","X.XX/X.XX/XXX./","XXX/X../XXX/.XX/",".XXX/XX.X/XX.X/","XX./XXX/..X/XXX/"};
		shape[1194] = new String[]{"XX../XX.X/.XXX/...X/","..XX/.XXX/.X../XXX./","X.../XXX./X.XX/..XX/",".XXX/..X./XXX./XX../","..XX/X.XX/XXX./X.../","XXX./.X../.XXX/..XX/","...X/.XXX/XX.X/XX../","XX../XXX./..X./.XXX/"};
		shape[1195] = new String[]{"XX.../XX.../.XXX./...XX/","..XX/.XXX/.X../XX../X.../","XX.../.XXX./...XX/...XX/","...X/..XX/..X./XXX./XX../","...XX/...XX/.XXX./XX.../","X.../XX../.X../.XXX/..XX/","...XX/.XXX./XX.../XX.../","XX../XXX./..X./..XX/...X/"};
		shape[1196] = new String[]{"XX../XX../.XXX/...X/...X/","...XX/..XXX/..X../XXX../","X.../X.../XXX./..XX/..XX/","..XXX/..X../XXX../XX.../","..XX/..XX/XXX./X.../X.../","XXX../..X../..XXX/...XX/","...X/...X/.XXX/XX../XX../","XX.../XXX../..X../..XXX/"};
		shape[1197] = new String[]{"XXX.../..X.../..XX../...XXX/","...X/...X/.XXX/XX../X.../X.../","XXX.../..XX../...X../...XXX/","...X/...X/..XX/XXX./X.../X.../","...XXX/...X../..XX../XXX.../","X.../X.../XX../.XXX/...X/...X/","...XXX/..XX../..X.../XXX.../","X.../X.../XXX./..XX/...X/...X/"};
		shape[1198] = new String[]{"X..../XX.../.X.../.XX../..XXX/","...XX/.XXX./XX.../X..../X..../","XXX../..XX./...X./...XX/....X/","....X/....X/...XX/.XXX./XX.../","....X/...XX/...X./..XX./XXX../","X..../X..../XX.../.XXX./...XX/","..XXX/.XX../.X.../XX.../X..../","XX.../.XXX./...XX/....X/....X/"};
		shape[1199] = new String[]{"XXX../.X.../.XX../..XXX/","...X/.XXX/XX.X/X.../X.../","XXX../..XX./...X./..XXX/","...X/...X/X.XX/XXX./X.../","..XXX/...X./..XX./XXX../","X.../X.../XX.X/.XXX/...X/","..XXX/.XX../.X.../XXX../","X.../XXX./X.XX/...X/...X/"};
		shape[1200] = new String[]{"XX.../.X.../.XX.X/..XXX/","...X/.XXX/XX../X.../XX../","XXX../X.XX./...X./...XX/","..XX/...X/..XX/XXX./X.../","...XX/...X./X.XX./XXX../","XX../X.../XX../.XXX/...X/","..XXX/.XX.X/.X.../XX.../","X.../XXX./..XX/...X/..XX/"};
		shape[1201] = new String[]{"XX.../.X.../.XX../..XXX/....X/","....X/..XXX/.XX../.X.../XX.../","X..../XXX../..XX./...X./...XX/","...XX/...X./..XX./XXX../X..../"};
		shape[1202] = new String[]{"XXX./X.../XX../.XXX/",".XXX/XX.X/X..X/X.../","XXX./..XX/...X/.XXX/","...X/X..X/X.XX/XXX./",".XXX/...X/..XX/XXX./","X.../X..X/XX.X/.XXX/",".XXX/XX../X.../XXX./","XXX./X.XX/X..X/...X/"};
		shape[1203] = new String[]{".X../XX../X.../XX../.XXX/",".XXX./XX.XX/X..../X..../","XXX./..XX/...X/..XX/..X./","....X/....X/XX.XX/.XXX./","..X./..XX/...X/..XX/XXX./","X..../X..../XX.XX/.XXX./",".XXX/XX../X.../XX../.X../",".XXX./XX.XX/....X/....X/"};
		shape[1204] = new String[]{"XX../X.../XX.X/.XXX/",".XXX/XX.X/X.../XX../","XXX./X.XX/...X/..XX/","..XX/...X/X.XX/XXX./"};
		shape[1205] = new String[]{"X.XX/XXX./.XXX/",".XX/XX./XXX/X.X/","XXX./.XXX/XX.X/","X.X/XXX/.XX/XX./","XX.X/.XXX/XXX./","X.X/XXX/XX./.XX/",".XXX/XXX./X.XX/","XX./.XX/XXX/X.X/"};
		shape[1206] = new String[]{"X.X./XXX./.XXX/.X../","..XX/XXX./.XXX/.X../","..X./XXX./.XXX/.X.X/","..X./XXX./.XXX/XX../",".X.X/.XXX/XXX./..X./",".X../.XXX/XXX./..XX/",".X../.XXX/XXX./X.X./","XX../.XXX/XXX./..X./"};
		shape[1207] = new String[]{"X.X./XXX./.XXX/...X/","..XX/.XX./.XXX/XX../","X.../XXX./.XXX/.X.X/","..XX/XXX./.XX./XX../",".X.X/.XXX/XXX./X.../","XX../.XXX/.XX./..XX/","...X/.XXX/XXX./X.X./","XX../.XX./XXX./..XX/"};
		shape[1208] = new String[]{"X.../XXX./.XXX/.XX./","..XX/XXX./XXX./.X../",".XX./XXX./.XXX/...X/","..X./.XXX/.XXX/XX../","...X/.XXX/XXX./.XX./",".X../XXX./XXX./..XX/",".XX./.XXX/XXX./X.../","XX../.XXX/.XXX/..X./"};
		shape[1209] = new String[]{"X.../XXX./.XXX/XX../","X.XX/XXX./.XX./.X../","..XX/XXX./.XXX/...X/","..X./.XX./.XXX/XX.X/","...X/.XXX/XXX./..XX/",".X../.XX./XXX./X.XX/","XX../.XXX/XXX./X.../","XX.X/.XXX/.XX./..X./"};
		shape[1210] = new String[]{"X.../XX.X/.XXX/.XX./","..XX/XXX./XX../.XX./",".XX./XXX./X.XX/...X/",".XX./..XX/.XXX/XX../","...X/X.XX/XXX./.XX./",".XX./XX../XXX./..XX/",".XX./.XXX/XX.X/X.../","XX../.XXX/..XX/.XX./"};
		shape[1211] = new String[]{"X.../XX../.XXX/.XXX/","..XX/XXX./XX../XX../","XXX./XXX./..XX/...X/","..XX/..XX/.XXX/XX../","...X/..XX/XXX./XXX./","XX../XX../XXX./..XX/",".XXX/.XXX/XX../X.../","XX../.XXX/..XX/..XX/"};
		shape[1212] = new String[]{"X.../XX../.XXX/XXX./","X.XX/XXX./XX../.X../",".XXX/XXX./..XX/...X/","..X./..XX/.XXX/XX.X/","...X/..XX/XXX./.XXX/",".X../XX../XXX./X.XX/","XXX./.XXX/XX../X.../","XX.X/.XXX/..XX/..X./"};
		shape[1213] = new String[]{"X..../XX.XX/.XXX./.X.../","..XX/XXX./.X../.XX./..X./","...X./.XXX./XX.XX/....X/",".X../.XX./..X./.XXX/XX../","....X/XX.XX/.XXX./...X./","..X./.XX./.X../XXX./..XX/",".X.../.XXX./XX.XX/X..../","XX../.XXX/..X./.XX./.X../"};
		shape[1214] = new String[]{"X..X/XX.X/.XXX/.X../","..XX/XXX./.X../.XXX/","..X./XXX./X.XX/X..X/","XXX./..X./.XXX/XX../","X..X/X.XX/XXX./..X./",".XXX/.X../XXX./..XX/",".X../.XXX/XX.X/X..X/","XX../.XXX/..X./XXX./"};
		shape[1215] = new String[]{"X.../XX.X/.XXX/.X.X/","..XX/XXX./.X../XXX./","X.X./XXX./X.XX/...X/",".XXX/..X./.XXX/XX../","...X/X.XX/XXX./X.X./","XXX./.X../XXX./..XX/",".X.X/.XXX/XX.X/X.../","XX../.XXX/..X./.XXX/"};
		shape[1216] = new String[]{"X.../XX.X/.XXX/XX../","X.XX/XXX./.X../.XX./","..XX/XXX./X.XX/...X/",".XX./..X./.XXX/XX.X/","...X/X.XX/XXX./..XX/",".XX./.X../XXX./X.XX/","XX../.XXX/XX.X/X.../","XX.X/.XXX/..X./.XX./"};
		shape[1217] = new String[]{"X.../XX../.XXX/XX.X/","X.XX/XXX./.X../XX../","X.XX/XXX./..XX/...X/","..XX/..X./.XXX/XX.X/","...X/..XX/XXX./X.XX/","XX../.X../XXX./X.XX/","XX.X/.XXX/XX../X.../","XX.X/.XXX/..X./..XX/"};
		shape[1218] = new String[]{"X..../XX.../.XXX./.X.XX/","..XX/XXX./.X../XX../X.../","XX.X./.XXX./...XX/....X/","...X/..XX/..X./.XXX/XX../","....X/...XX/.XXX./XX.X./","X.../XX../.X../XXX./..XX/",".X.XX/.XXX./XX.../X..../","XX../.XXX/..X./..XX/...X/"};
		shape[1219] = new String[]{"X.../XX../.XXX/.X.X/...X/","...XX/.XXX./..X../XXX../","X.../X.X./XXX./..XX/...X/","..XXX/..X../.XXX./XX.../","...X/..XX/XXX./X.X./X.../","XXX../..X../.XXX./...XX/","...X/.X.X/.XXX/XX../X.../","XX.../.XXX./..X../..XXX/"};
		shape[1220] = new String[]{"X.../XX../.XXX/XX../X.../","XX.XX/.XXX./..X../..X../","...X/..XX/XXX./..XX/...X/","..X../..X../.XXX./XX.XX/"};
		shape[1221] = new String[]{"X..../XX.XX/.XXX./..X../","..XX/.XX./XX../.XX./..X./","..X../.XXX./XX.XX/....X/",".X../.XX./..XX/.XX./XX../","....X/XX.XX/.XXX./..X../","..X./.XX./XX../.XX./..XX/","..X../.XXX./XX.XX/X..../","XX../.XX./..XX/.XX./.X../"};
		shape[1222] = new String[]{"X..X/XX.X/.XXX/..X./","..XX/.XX./XX../.XXX/",".X../XXX./X.XX/X..X/","XXX./..XX/.XX./XX../","X..X/X.XX/XXX./.X../",".XXX/XX../.XX./..XX/","..X./.XXX/XX.X/X..X/","XX../.XX./..XX/XXX./"};
		shape[1223] = new String[]{"X.../XX.X/.XXX/..XX/","..XX/.XX./XX../XXX./","XX../XXX./X.XX/...X/",".XXX/..XX/.XX./XX../","...X/X.XX/XXX./XX../","XXX./XX../.XX./..XX/","..XX/.XXX/XX.X/X.../","XX../.XX./..XX/.XXX/"};
		shape[1224] = new String[]{"X.../XX../.XXX/..XX/...X/","...XX/..XX./.XX../XXX../","X.../XX../XXX./..XX/...X/","..XXX/..XX./.XX../XX.../","...X/..XX/XXX./XX../X.../","XXX../.XX../..XX./...XX/","...X/..XX/.XXX/XX../X.../","XX.../.XX../..XX./..XXX/"};
		shape[1225] = new String[]{"X..X./XX.XX/.XXX./",".XX/XX./X../XXX/.X./",".XXX./XX.XX/.X..X/",".X./XXX/..X/.XX/XX./",".X..X/XX.XX/.XXX./",".X./XXX/X../XX./.XX/",".XXX./XX.XX/X..X./","XX./.XX/..X/XXX/.X./"};
		shape[1226] = new String[]{"X...X/XX.XX/.XXX./",".XX/XX./X../XX./.XX/",".XXX./XX.XX/X...X/","XX./.XX/..X/.XX/XX./"};
		shape[1227] = new String[]{"X..../XX.XX/.XXX./...X./","..XX/.XX./.X../XXX./..X./",".X.../.XXX./XX.XX/....X/",".X../.XXX/..X./.XX./XX../","....X/XX.XX/.XXX./.X.../","..X./XXX./.X../.XX./..XX/","...X./.XXX./XX.XX/X..../","XX../.XX./..X./.XXX/.X../"};
		shape[1228] = new String[]{"X.XX/XX.X/.XXX/",".XX/XX./X.X/XXX/","XXX./X.XX/XX.X/","XXX/X.X/.XX/XX./","XX.X/X.XX/XXX./","XXX/X.X/XX./.XX/",".XXX/XX.X/X.XX/","XX./.XX/X.X/XXX/"};
		shape[1229] = new String[]{"X..XX/XX.X./.XXX./",".XX/XX./X../XXX/..X/",".XXX./.X.XX/XX..X/","X../XXX/..X/.XX/XX./","XX..X/.X.XX/.XXX./","..X/XXX/X../XX./.XX/",".XXX./XX.X./X..XX/","XX./.XX/..X/XXX/X../"};
		shape[1230] = new String[]{"X..../XX.X./.XXX./...XX/","..XX/.XX./.X../XXX./X.../","XX.../.XXX./.X.XX/....X/","...X/.XXX/..X./.XX./XX../","....X/.X.XX/.XXX./XX.../","X.../XXX./.X../.XX./..XX/","...XX/.XXX./XX.X./X..../","XX../.XX./..X./.XXX/...X/"};
		shape[1231] = new String[]{"X..../XX.../.XXX./...XX/...X./","...XX/..XX./..X../XXX../.X.../",".X.../XX.../.XXX./...XX/....X/","...X./..XXX/..X../.XX../XX.../","....X/...XX/.XXX./XX.../.X.../",".X.../XXX../..X../..XX./...XX/","...X./...XX/.XXX./XX.../X..../","XX.../.XX../..X../..XXX/...X./"};
		shape[1232] = new String[]{"X..../XX.../.XXX./...XX/....X/","...XX/..XX./..X../.XX../XX.../","....X/...XX/.XXX./XX.../X..../","XX.../.XX../..X../..XX./...XX/"};
		shape[1233] = new String[]{"X.../XX../.XXX/...X/..XX/","...XX/..XX./X.X../XXX../","XX../X.../XXX./..XX/...X/","..XXX/..X.X/.XX../XX.../","...X/..XX/XXX./X.../XX../","XXX../X.X../..XX./...XX/","..XX/...X/.XXX/XX../X.../","XX.../.XX../..X.X/..XXX/"};
		shape[1234] = new String[]{"X..../XX.../.XXX./...X./...XX/","...XX/..XX./..X../XXX../X..../","XX.../.X.../.XXX./...XX/....X/","....X/..XXX/..X../.XX../XX.../","....X/...XX/.XXX./.X.../XX.../","X..../XXX../..X../..XX./...XX/","...XX/...X./.XXX./XX.../X..../","XX.../.XX../..X../..XXX/....X/"};
		shape[1235] = new String[]{".XXX/XXX./.XXX/",".X./XXX/XXX/X.X/","XXX./.XXX/XXX./","X.X/XXX/XXX/.X./"};
		shape[1236] = new String[]{"..X./.XXX/XX../.XXX/",".X../XXX./X.XX/X.X./","XXX./..XX/XXX./.X../",".X.X/XX.X/.XXX/..X./",".X../XXX./..XX/XXX./","X.X./X.XX/XXX./.X../",".XXX/XX../.XXX/..X./","..X./.XXX/XX.X/.X.X/"};
		shape[1237] = new String[]{"...X/.XXX/XX../.XXX/",".X../XXX./X.X./X.XX/","XXX./..XX/XXX./X.../","XX.X/.X.X/.XXX/..X./","X.../XXX./..XX/XXX./","X.XX/X.X./XXX./.X../",".XXX/XX../.XXX/...X/","..X./.XXX/.X.X/XX.X/"};
		shape[1238] = new String[]{".XXX/XX.X/.XXX/",".X./XXX/X.X/XXX/","XXX./X.XX/XXX./","XXX/X.X/XXX/.X./"};
		shape[1239] = new String[]{"..X./.XX./XX../.XXX/..X./","..X../.XXX./XX.XX/.X.../",".X../XXX./..XX/.XX./.X../","...X./XX.XX/.XXX./..X../",".X../.XX./..XX/XXX./.X../",".X.../XX.XX/.XXX./..X../","..X./.XXX/XX../.XX./..X./","..X../.XXX./XX.XX/...X./"};
		shape[1240] = new String[]{"..X./.XX./XX.X/.XXX/",".X../XXX./X.XX/XX../","XXX./X.XX/.XX./.X../","..XX/XX.X/.XXX/..X./",".X../.XX./X.XX/XXX./","XX../X.XX/XXX./.X../",".XXX/XX.X/.XX./..X./","..X./.XXX/XX.X/..XX/"};
		shape[1241] = new String[]{"..X./.XX./XX../.XXX/...X/","..X../.XXX./.X.XX/XX.../","X.../XXX./..XX/.XX./.X../","...XX/XX.X./.XXX./..X../",".X../.XX./..XX/XXX./X.../","XX.../.X.XX/.XXX./..X../","...X/.XXX/XX../.XX./..X./","..X../.XXX./XX.X./...XX/"};
		shape[1242] = new String[]{".XX./XX.X/.XXX/..X./","..X./.XXX/XX.X/.XX./",".X../XXX./X.XX/.XX./",".XX./X.XX/XXX./.X../"};
		shape[1243] = new String[]{".XX../XX.XX/.XXX./",".X./XXX/X.X/XX./.X./",".XXX./XX.XX/..XX./",".X./.XX/X.X/XXX/.X./","..XX./XX.XX/.XXX./",".X./XX./X.X/XXX/.X./",".XXX./XX.XX/.XX../",".X./XXX/X.X/.XX/.X./"};
		shape[1244] = new String[]{".XX./XX.X/.XXX/...X/","..X./.XXX/.X.X/XXX./","X.../XXX./X.XX/.XX./",".XXX/X.X./XXX./.X../",".XX./X.XX/XXX./X.../","XXX./.X.X/.XXX/..X./","...X/.XXX/XX.X/.XX./",".X../XXX./X.X./.XXX/"};
		shape[1245] = new String[]{".XX../XX.../.XXX./...XX/","..X./.XXX/.X.X/XX../X.../","XX.../.XXX./...XX/..XX./","...X/..XX/X.X./XXX./.X../","..XX./...XX/.XXX./XX.../","X.../XX../.X.X/.XXX/..X./","...XX/.XXX./XX.../.XX../",".X../XXX./X.X./..XX/...X/"};
		shape[1246] = new String[]{".XX./XX../.XXX/...X/...X/","...X./..XXX/..X.X/XXX../","X.../X.../XXX./..XX/.XX./","..XXX/X.X../XXX../.X.../",".XX./..XX/XXX./X.../X.../","XXX../..X.X/..XXX/...X./","...X/...X/.XXX/XX../.XX./",".X.../XXX../X.X../..XXX/"};
		shape[1247] = new String[]{".X.X./XX.XX/.XXX./",".X./XXX/X../XXX/.X./",".XXX./XX.XX/.X.X./",".X./XXX/..X/XXX/.X./"};
		shape[1248] = new String[]{".X.../XX.XX/.XXX./...X./","..X./.XXX/.X../XXX./..X./",".X.../.XXX./XX.XX/...X./",".X../.XXX/..X./XXX./.X../","...X./XX.XX/.XXX./.X.../","..X./XXX./.X../.XXX/..X./","...X./.XXX./XX.XX/.X.../",".X../XXX./..X./.XXX/.X../"};
		shape[1249] = new String[]{".X.XX/XX.X./.XXX./",".X./XXX/X../XXX/..X/",".XXX./.X.XX/XX.X./","X../XXX/..X/XXX/.X./","XX.X./.X.XX/.XXX./","..X/XXX/X../XXX/.X./",".XXX./XX.X./.X.XX/",".X./XXX/..X/XXX/X../"};
		shape[1250] = new String[]{".X.../XX.X./.XXX./...XX/","..X./.XXX/.X../XXX./X.../","XX.../.XXX./.X.XX/...X./","...X/.XXX/..X./XXX./.X../","...X./.X.XX/.XXX./XX.../","X.../XXX./.X../.XXX/..X./","...XX/.XXX./XX.X./.X.../",".X../XXX./..X./.XXX/...X/"};
		shape[1251] = new String[]{".X.../XX.../.XXX./...XX/...X./","...X./..XXX/..X../XXX../.X.../","...X./...XX/.XXX./XX.../.X.../",".X.../XXX../..X../..XXX/...X./"};
		shape[1252] = new String[]{".X../XX../.XXX/...X/..XX/","...X./..XXX/X.X../XXX../","XX../X.../XXX./..XX/..X./","..XXX/..X.X/XXX../.X.../","..X./..XX/XXX./X.../XX../","XXX../X.X../..XXX/...X./","..XX/...X/.XXX/XX../.X../",".X.../XXX../..X.X/..XXX/"};
		shape[1253] = new String[]{".X.../XX.../.XXX./...X./...XX/","...X./..XXX/..X../XXX../X..../","XX.../.X.../.XXX./...XX/...X./","....X/..XXX/..X../XXX../.X.../","...X./...XX/.XXX./.X.../XX.../","X..../XXX../..X../..XXX/...X./","...XX/...X./.XXX./XX.../.X.../",".X.../XXX../..X../..XXX/....X/"};
		shape[1254] = new String[]{"XX.XX/.XXX./.XX../","..X/XXX/XX./.XX/..X/","..XX./.XXX./XX.XX/","X../XX./.XX/XXX/X../","XX.XX/.XXX./..XX./","..X/.XX/XX./XXX/..X/",".XX../.XXX./XX.XX/","X../XXX/.XX/XX./X../"};
		shape[1255] = new String[]{"XX.X/.XXX/.XXX/","..X/XXX/XX./XXX/","XXX./XXX./X.XX/","XXX/.XX/XXX/X../","X.XX/XXX./XXX./","XXX/XX./XXX/..X/",".XXX/.XXX/XX.X/","X../XXX/.XX/XXX/"};
		shape[1256] = new String[]{"XX../.XXX/.XXX/...X/","...X/.XXX/.XX./XXX./","X.../XXX./XXX./..XX/",".XXX/.XX./XXX./X.../","..XX/XXX./XXX./X.../","XXX./.XX./.XXX/...X/","...X/.XXX/.XXX/XX../","X.../XXX./.XX./.XXX/"};
		shape[1257] = new String[]{"XX.XX/.XXX./.X.X./","..X/XXX/.X./XXX/..X/",".X.X./.XXX./XX.XX/","X../XXX/.X./XXX/X../"};
		shape[1258] = new String[]{"XX.XX/.XXX./XX.../","X.X/XXX/.X./.XX/..X/","...XX/.XXX./XX.XX/","X../XX./.X./XXX/X.X/","XX.XX/.XXX./...XX/","..X/.XX/.X./XXX/X.X/","XX.../.XXX./XX.XX/","X.X/XXX/.X./XX./X../"};
		shape[1259] = new String[]{"..XX/XX.X/.XXX/.X../","..X./XXX./.X.X/.XXX/","..X./XXX./X.XX/XX../","XXX./X.X./.XXX/.X../","XX../X.XX/XXX./..X./",".XXX/.X.X/XXX./..X./",".X../.XXX/XX.X/..XX/",".X../.XXX/X.X./XXX./"};
		shape[1260] = new String[]{"...XX/XX.X./.XXX./.X.../","..X./XXX./.X../.XXX/...X/","...X./.XXX./.X.XX/XX.../","X.../XXX./..X./.XXX/.X../","XX.../.X.XX/.XXX./...X./","...X/.XXX/.X../XXX./..X./",".X.../.XXX./XX.X./...XX/",".X../.XXX/..X./XXX./X.../"};
		shape[1261] = new String[]{"...X/XX.X/.XXX/XX../","X.X./XXX./.X../.XXX/","..XX/XXX./X.XX/X.../","XXX./..X./.XXX/.X.X/","X.../X.XX/XXX./..XX/",".XXX/.X../XXX./X.X./","XX../.XXX/XX.X/...X/",".X.X/.XXX/..X./XXX./"};
		shape[1262] = new String[]{"XX.X/.XXX/XX.X/","X.X/XXX/.X./XXX/","X.XX/XXX./X.XX/","XXX/.X./XXX/X.X/"};
		shape[1263] = new String[]{"XX.X./.XXX./.X.XX/","..X/XXX/.X./XXX/X../",".X.XX/.XXX./XX.X./","X../XXX/.X./XXX/..X/"};
		shape[1264] = new String[]{"XX../.XXX/.X.X/..XX/","...X/.XXX/X.X./XXX./","XX../X.X./XXX./..XX/",".XXX/.X.X/XXX./X.../","..XX/XXX./X.X./XX../","XXX./X.X./.XXX/...X/","..XX/.X.X/.XXX/XX../","X.../XXX./.X.X/.XXX/"};
		shape[1265] = new String[]{"XX.../.XXX./.X.X./...XX/","...X/.XXX/..X./XXX./X.../","XX.../.X.X./.XXX./...XX/","...X/.XXX/.X../XXX./X.../","...XX/.XXX./.X.X./XX.../","X.../XXX./..X./.XXX/...X/","...XX/.X.X./.XXX./XX.../","X.../XXX./.X../.XXX/...X/"};
		shape[1266] = new String[]{"XX.../.XXX./..XX./...XX/","...X/..XX/.XX./XXX./X.../","XX.../.XX../.XXX./...XX/","...X/.XXX/.XX./XX../X.../","...XX/.XXX./.XX../XX.../","X.../XXX./.XX./..XX/...X/","...XX/..XX./.XXX./XX.../","X.../XX../.XX./.XXX/...X/"};
		shape[1267] = new String[]{"..X./..XX/XX.X/.XXX/",".X../XX../X.XX/XXX./","XXX./X.XX/XX../.X../",".XXX/XX.X/..XX/..X./"};
		shape[1268] = new String[]{"..XXX/XX.X./.XXX./",".X./XX./X.X/XXX/..X/",".XXX./.X.XX/XXX../","X../XXX/X.X/.XX/.X./","XXX../.X.XX/.XXX./","..X/XXX/X.X/XX./.X./",".XXX./XX.X./..XXX/",".X./.XX/X.X/XXX/X../"};
		shape[1269] = new String[]{"...XXX/XX.X../.XXX../",".X./XX./X../XXX/..X/..X/","..XXX./..X.XX/XXX.../","X../X../XXX/..X/.XX/.X./","XXX.../..X.XX/..XXX./","..X/..X/XXX/X../XX./.X./",".XXX../XX.X../...XXX/",".X./.XX/..X/XXX/X../X../"};
		shape[1270] = new String[]{"....X/...XX/XX.X./.XXX./",".X../XX../X.../XXX./..XX/",".XXX./.X.XX/XX.../X..../","XX../.XXX/...X/..XX/..X./","X..../XX.../.X.XX/.XXX./","..XX/XXX./X.../XX../.X../",".XXX./XX.X./...XX/....X/","..X./..XX/...X/.XXX/XX../"};
		shape[1271] = new String[]{"XX../.XXX/...X/.XXX/","...X/X.XX/X.X./XXX./","XXX./X.../XXX./..XX/",".XXX/.X.X/XX.X/X.../","..XX/XXX./X.../XXX./","XXX./X.X./X.XX/...X/",".XXX/...X/.XXX/XX../","X.../XX.X/.X.X/.XXX/"};
		shape[1272] = new String[]{"XX.../.XXX./...X./..XXX/","...X/..XX/X.X./XXX./X.../","XXX../.X.../.XXX./...XX/","...X/.XXX/.X.X/XX../X.../","...XX/.XXX./.X.../XXX../","X.../XXX./X.X./..XX/...X/","..XXX/...X./.XXX./XX.../","X.../XX../.X.X/.XXX/...X/"};
		shape[1273] = new String[]{"XX..../.XXX../...X../...XXX/","...X/..XX/..X./XXX./X.../X.../","XXX.../..X.../..XXX./....XX/","...X/...X/.XXX/.X../XX../X.../","....XX/..XXX./..X.../XXX.../","X.../X.../XXX./..X./..XX/...X/","...XXX/...X../.XXX../XX..../","X.../XX../.X../.XXX/...X/...X/"};
		shape[1274] = new String[]{"XX.../.XXX./...X./...XX/....X/","....X/...XX/...X./.XXX./XX.../","X..../XX.../.X.../.XXX./...XX/","...XX/.XXX./.X.../XX.../X..../"};
		shape[1275] = new String[]{"XXX/XXX/XXX/"};
		shape[1276] = new String[]{"X..../X..../XXX../..X../..XXX/","..XXX/..X../XXX../X..../X..../","XXX../..X../..XXX/....X/....X/","....X/....X/..XXX/..X../XXX../"};
		shape[1277] = new String[]{"X..../XXX../X.X../..XXX/",".XXX/..X./XXX./X.../X.../","XXX../..X.X/..XXX/....X/","...X/...X/.XXX/.X../XXX./","....X/..XXX/..X.X/XXX../","X.../X.../XXX./..X./.XXX/","..XXX/X.X../XXX../X..../","XXX./.X../.XXX/...X/...X/"};
		shape[1278] = new String[]{"X..../XXX../..X.X/..XXX/","..XX/..X./XXX./X.../XX../","XXX../X.X../..XXX/....X/","..XX/...X/.XXX/.X../XX../","....X/..XXX/X.X../XXX../","XX../X.../XXX./..X./..XX/","..XXX/..X.X/XXX../X..../","XX../.X../.XXX/...X/..XX/"};
		shape[1279] = new String[]{"X..../XXX../..X../..XXX/....X/","...XX/...X./.XXX./.X.../XX.../","....X/..XXX/..X../XXX../X..../","XX.../.X.../.XXX./...X./...XX/"};
		shape[1280] = new String[]{"XXX../X.X../X.XXX/","XXX/..X/XXX/X../X../","XXX.X/..X.X/..XXX/","..X/..X/XXX/X../XXX/","..XXX/..X.X/XXX.X/","X../X../XXX/..X/XXX/","X.XXX/X.X../XXX../","XXX/X../XXX/..X/..X/"};
		shape[1281] = new String[]{"XXX../X.X.X/..XXX/",".XX/..X/XXX/X../XX./","..XXX/X.X.X/XXX../","XX./X../XXX/..X/.XX/"};
		shape[1282] = new String[]{"XXX./.X.X/.XXX/...X/","...X/.XXX/.X.X/XXX./","X.../XXX./X.X./.XXX/",".XXX/X.X./XXX./X.../"};
		shape[1283] = new String[]{"XX.XX/.X.X./.XXX./","..X/XXX/X../XXX/..X/",".XXX./.X.X./XX.XX/","X../XXX/..X/XXX/X../"};
		shape[1284] = new String[]{"X..../XX.../.XX../..XX./...XX/","...XX/..XX./.XX../XX.../X..../","XX.../.XX../..XX./...XX/....X/","....X/...XX/..XX./.XX../XX.../"};

		return shape;
	}

	public String[][] setup10() {
		String[][] shape = {
				{"XXXXXXXXXX/","X/X/X/X/X/X/X/X/X/X/"},
				{"X......../XXXXXXXXX/","XX/X./X./X./X./X./X./X./X./","XXXXXXXXX/........X/",".X/.X/.X/.X/.X/.X/.X/.X/XX/","........X/XXXXXXXXX/","X./X./X./X./X./X./X./X./XX/","XXXXXXXXX/X......../","XX/.X/.X/.X/.X/.X/.X/.X/.X/"},
				{"XX......./.XXXXXXXX/",".X/XX/X./X./X./X./X./X./X./","XXXXXXXX./.......XX/",".X/.X/.X/.X/.X/.X/.X/XX/X./",".......XX/XXXXXXXX./","X./X./X./X./X./X./X./XX/.X/",".XXXXXXXX/XX......./","X./XX/.X/.X/.X/.X/.X/.X/.X/"},
				{"XX....../XXXXXXXX/","XX/XX/X./X./X./X./X./X./","XXXXXXXX/......XX/",".X/.X/.X/.X/.X/.X/XX/XX/","......XX/XXXXXXXX/","X./X./X./X./X./X./XX/XX/","XXXXXXXX/XX....../","XX/XX/.X/.X/.X/.X/.X/.X/"},
				{"X......./X......./XXXXXXXX/","XXX/X../X../X../X../X../X../X../","XXXXXXXX/.......X/.......X/","..X/..X/..X/..X/..X/..X/..X/XXX/",".......X/.......X/XXXXXXXX/","X../X../X../X../X../X../X../XXX/","XXXXXXXX/X......./X......./","XXX/..X/..X/..X/..X/..X/..X/..X/"},
				{"XXX....../..XXXXXXX/",".X/.X/XX/X./X./X./X./X./X./","XXXXXXX../......XXX/",".X/.X/.X/.X/.X/.X/XX/X./X./","......XXX/XXXXXXX../","X./X./X./X./X./X./XX/.X/.X/","..XXXXXXX/XXX....../","X./X./XX/.X/.X/.X/.X/.X/.X/"},
				{"X......./XX....../.XXXXXXX/",".XX/XX./X../X../X../X../X../X../","XXXXXXX./......XX/.......X/","..X/..X/..X/..X/..X/..X/.XX/XX./",".......X/......XX/XXXXXXX./","X../X../X../X../X../X../XX./.XX/",".XXXXXXX/XX....../X......./","XX./.XX/..X/..X/..X/..X/..X/..X/"},
				{"XXX..../XXXXXXX/","XX/XX/XX/X./X./X./X./","XXXXXXX/....XXX/",".X/.X/.X/.X/XX/XX/XX/","....XXX/XXXXXXX/","X./X./X./X./XX/XX/XX/","XXXXXXX/XXX..../","XX/XX/XX/.X/.X/.X/.X/"},
				{".X...../XX...../XXXXXXX/","XX./XXX/X../X../X../X../X../","XXXXXXX/.....XX/.....X./","..X/..X/..X/..X/..X/XXX/.XX/",".....X./.....XX/XXXXXXX/","X../X../X../X../X../XXX/XX./","XXXXXXX/XX...../.X...../",".XX/XXX/..X/..X/..X/..X/..X/"},
				{"XX....../.X....../.XXXXXXX/","..X/XXX/X../X../X../X../X../X../","XXXXXXX./......X./......XX/","..X/..X/..X/..X/..X/..X/XXX/X../","......XX/......X./XXXXXXX./","X../X../X../X../X../X../XXX/..X/",".XXXXXXX/.X....../XX....../","X../XXX/..X/..X/..X/..X/..X/..X/"},
				{"XX...../X....../XXXXXXX/","XXX/X.X/X../X../X../X../X../","XXXXXXX/......X/.....XX/","..X/..X/..X/..X/..X/X.X/XXX/",".....XX/......X/XXXXXXX/","X../X../X../X../X../X.X/XXX/","XXXXXXX/X....../XX...../","XXX/X.X/..X/..X/..X/..X/..X/"},
				{"X....../X....../X....../XXXXXXX/","XXXX/X.../X.../X.../X.../X.../X.../","XXXXXXX/......X/......X/......X/","...X/...X/...X/...X/...X/...X/XXXX/","......X/......X/......X/XXXXXXX/","X.../X.../X.../X.../X.../X.../XXXX/","XXXXXXX/X....../X....../X....../","XXXX/...X/...X/...X/...X/...X/...X/"},
				{"XXXX...../...XXXXXX/",".X/.X/.X/XX/X./X./X./X./X./","XXXXXX.../.....XXXX/",".X/.X/.X/.X/.X/XX/X./X./X./",".....XXXX/XXXXXX.../","X./X./X./X./X./XX/.X/.X/.X/","...XXXXXX/XXXX...../","X./X./X./XX/.X/.X/.X/.X/.X/"},
				{"X......./XXX...../..XXXXXX/",".XX/.X./XX./X../X../X../X../X../","XXXXXX../.....XXX/.......X/","..X/..X/..X/..X/..X/.XX/.X./XX./",".......X/.....XXX/XXXXXX../","X../X../X../X../X../XX./.X./.XX/","..XXXXXX/XXX...../X......./","XX./.X./.XX/..X/..X/..X/..X/..X/"},
				{"XXX...../X.XXXXXX/","XX/.X/XX/X./X./X./X./X./","XXXXXX.X/.....XXX/",".X/.X/.X/.X/.X/XX/X./XX/",".....XXX/XXXXXX.X/","X./X./X./X./X./XX/.X/XX/","X.XXXXXX/XXX...../","XX/X./XX/.X/.X/.X/.X/.X/"},
				{"XX....../.XX...../..XXXXXX/","..X/.XX/XX./X../X../X../X../X../","XXXXXX../.....XX./......XX/","..X/..X/..X/..X/..X/.XX/XX./X../","......XX/.....XX./XXXXXX../","X../X../X../X../X../XX./.XX/..X/","..XXXXXX/.XX...../XX....../","X../XX./.XX/..X/..X/..X/..X/..X/"},
				{"XX...../XX...../.XXXXXX/",".XX/XXX/X../X../X../X../X../","XXXXXX./.....XX/.....XX/","..X/..X/..X/..X/..X/XXX/XX./",".....XX/.....XX/XXXXXX./","X../X../X../X../X../XXX/.XX/",".XXXXXX/XX...../XX...../","XX./XXX/..X/..X/..X/..X/..X/"},
				{"X....../X....../XX...../.XXXXXX/",".XXX/XX../X.../X.../X.../X.../X.../","XXXXXX./.....XX/......X/......X/","...X/...X/...X/...X/...X/..XX/XXX./","......X/......X/.....XX/XXXXXX./","X.../X.../X.../X.../X.../XX../.XXX/",".XXXXXX/XX...../X....../X....../","XXX./..XX/...X/...X/...X/...X/...X/"},
				{".XX...../XXXXXXXX/","X./XX/XX/X./X./X./X./X./","XXXXXXXX/.....XX./",".X/.X/.X/.X/.X/XX/XX/.X/",".....XX./XXXXXXXX/","X./X./X./X./X./XX/XX/X./","XXXXXXXX/.XX...../",".X/XX/XX/.X/.X/.X/.X/.X/"},
				{"XX...../XXXXXXX/X....../","XXX/.XX/.X./.X./.X./.X./.X./","......X/XXXXXXX/.....XX/",".X./.X./.X./.X./.X./XX./XXX/",".....XX/XXXXXXX/......X/",".X./.X./.X./.X./.X./.XX/XXX/","X....../XXXXXXX/XX...../","XXX/XX./.X./.X./.X./.X./.X./"},
				{"XXXX../XXXXXX/","XX/XX/XX/XX/X./X./","XXXXXX/..XXXX/",".X/.X/XX/XX/XX/XX/","..XXXX/XXXXXX/","X./X./XX/XX/XX/XX/","XXXXXX/XXXX../","XX/XX/XX/XX/.X/.X/"},
				{"..X.../XXX.../XXXXXX/","XX./XX./XXX/X../X../X../","XXXXXX/...XXX/...X../","..X/..X/..X/XXX/.XX/.XX/","...X../...XXX/XXXXXX/","X../X../X../XXX/XX./XX./","XXXXXX/XXX.../..X.../",".XX/.XX/XXX/..X/..X/..X/"},
				{"XX..../XX..../XXXXXX/","XXX/XXX/X../X../X../X../","XXXXXX/....XX/....XX/","..X/..X/..X/..X/XXX/XXX/","....XX/....XX/XXXXXX/","X../X../X../X../XXX/XXX/","XXXXXX/XX..../XX..../","XXX/XXX/..X/..X/..X/..X/"},
				{".XX.../XX..../XXXXXX/","XX./XXX/X.X/X../X../X../","XXXXXX/....XX/...XX./","..X/..X/..X/X.X/XXX/.XX/","...XX./....XX/XXXXXX/","X../X../X../X.X/XXX/XX./","XXXXXX/XX..../.XX.../",".XX/XXX/X.X/..X/..X/..X/"},
				{".X..../.X..../XX..../XXXXXX/","XX../XXXX/X.../X.../X.../X.../","XXXXXX/....XX/....X./....X./","...X/...X/...X/...X/XXXX/..XX/","....X./....X./....XX/XXXXXX/","X.../X.../X.../X.../XXXX/XX../","XXXXXX/XX..../.X..../.X..../","..XX/XXXX/...X/...X/...X/...X/"},
				{"XXX...../..X...../..XXXXXX/","..X/..X/XXX/X../X../X../X../X../","XXXXXX../.....X../.....XXX/","..X/..X/..X/..X/..X/XXX/X../X../",".....XXX/.....X../XXXXXX../","X../X../X../X../X../XXX/..X/..X/","..XXXXXX/..X...../XXX...../","X../X../XXX/..X/..X/..X/..X/..X/"},
				{"X....../XX...../.X...../.XXXXXX/","..XX/XXX./X.../X.../X.../X.../X.../","XXXXXX./.....X./.....XX/......X/","...X/...X/...X/...X/...X/.XXX/XX../","......X/.....XX/.....X./XXXXXX./","X.../X.../X.../X.../X.../XXX./..XX/",".XXXXXX/.X...../XX...../X....../","XX../.XXX/...X/...X/...X/...X/...X/"},
				{"XXX.../X...../XXXXXX/","XXX/X.X/X.X/X../X../X../","XXXXXX/.....X/...XXX/","..X/..X/..X/X.X/X.X/XXX/","...XXX/.....X/XXXXXX/","X../X../X../X.X/X.X/XXX/","XXXXXX/X...../XXX.../","XXX/X.X/X.X/..X/..X/..X/"},
				{".X..../XX..../X...../XXXXXX/","XXX./X.XX/X.../X.../X.../X.../","XXXXXX/.....X/....XX/....X./","...X/...X/...X/...X/XX.X/.XXX/","....X./....XX/.....X/XXXXXX/","X.../X.../X.../X.../X.XX/XXX./","XXXXXX/X...../XX..../.X..../",".XXX/XX.X/...X/...X/...X/...X/"},
				{"XX...../.X...../.X...../.XXXXXX/","...X/XXXX/X.../X.../X.../X.../X.../","XXXXXX./.....X./.....X./.....XX/","...X/...X/...X/...X/...X/XXXX/X.../",".....XX/.....X./.....X./XXXXXX./","X.../X.../X.../X.../X.../XXXX/...X/",".XXXXXX/.X...../.X...../XX...../","X.../XXXX/...X/...X/...X/...X/...X/"},
				{"XX..../X...../X...../XXXXXX/","XXXX/X..X/X.../X.../X.../X.../","XXXXXX/.....X/.....X/....XX/","...X/...X/...X/...X/X..X/XXXX/","....XX/.....X/.....X/XXXXXX/","X.../X.../X.../X.../X..X/XXXX/","XXXXXX/X...../X...../XX..../","XXXX/X..X/...X/...X/...X/...X/"},
				{"X...../X...../X...../X...../XXXXXX/","XXXXX/X..../X..../X..../X..../X..../","XXXXXX/.....X/.....X/.....X/.....X/","....X/....X/....X/....X/....X/XXXXX/",".....X/.....X/.....X/.....X/XXXXXX/","X..../X..../X..../X..../X..../XXXXX/","XXXXXX/X...../X...../X...../X...../","XXXXX/....X/....X/....X/....X/....X/"},
				{"XXXXX..../....XXXXX/",".X/.X/.X/.X/XX/X./X./X./X./","....XXXXX/XXXXX..../","X./X./X./X./XX/.X/.X/.X/.X/"},
				{"X......./XXXX..../...XXXXX/",".XX/.X./.X./XX./X../X../X../X../","XXXXX.../....XXXX/.......X/","..X/..X/..X/..X/.XX/.X./.X./XX./",".......X/....XXXX/XXXXX.../","X../X../X../X../XX./.X./.X./.XX/","...XXXXX/XXXX..../X......./","XX./.X./.X./.XX/..X/..X/..X/..X/"},
				{"XXXX..../X..XXXXX/","XX/.X/.X/XX/X./X./X./X./","XXXXX..X/....XXXX/",".X/.X/.X/.X/XX/X./X./XX/","....XXXX/XXXXX..X/","X./X./X./X./XX/.X/.X/XX/","X..XXXXX/XXXX..../","XX/X./X./XX/.X/.X/.X/.X/"},
				{"XX....../.XXX..../...XXXXX/","..X/.XX/.X./XX./X../X../X../X../","XXXXX.../....XXX./......XX/","..X/..X/..X/..X/.XX/.X./XX./X../","......XX/....XXX./XXXXX.../","X../X../X../X../XX./.X./.XX/..X/","...XXXXX/.XXX..../XX....../","X../XX./.X./.XX/..X/..X/..X/..X/"},
				{"XX...../XXX..../..XXXXX/",".XX/.XX/XX./X../X../X../X../","XXXXX../....XXX/.....XX/","..X/..X/..X/..X/.XX/XX./XX./",".....XX/....XXX/XXXXX../","X../X../X../X../XX./.XX/.XX/","..XXXXX/XXX..../XX...../","XX./XX./.XX/..X/..X/..X/..X/"},
				{"X....../X....../XXX..../..XXXXX/",".XXX/.X../XX../X.../X.../X.../X.../","XXXXX../....XXX/......X/......X/","...X/...X/...X/...X/..XX/..X./XXX./","......X/......X/....XXX/XXXXX../","X.../X.../X.../X.../XX../.X../.XXX/","..XXXXX/XXX..../X....../X....../","XXX./..X./..XX/...X/...X/...X/...X/"},
				{".XXX..../XX.XXXXX/","X./XX/.X/XX/X./X./X./X./","XXXXX.XX/....XXX./",".X/.X/.X/.X/XX/X./XX/.X/","....XXX./XXXXX.XX/","X./X./X./X./XX/.X/XX/X./","XX.XXXXX/.XXX..../",".X/XX/X./XX/.X/.X/.X/.X/"},
				{"XXX..../X.XXXXX/X....../","XXX/..X/.XX/.X./.X./.X./.X./","......X/XXXXX.X/....XXX/",".X./.X./.X./.X./XX./X../XXX/","....XXX/XXXXX.X/......X/",".X./.X./.X./.X./.XX/..X/XXX/","X....../X.XXXXX/XXX..../","XXX/X../XX./.X./.X./.X./.X./"},
				{"XXX...../..XX..../...XXXXX/","..X/..X/.XX/XX./X../X../X../X../","XXXXX.../....XX../.....XXX/","..X/..X/..X/..X/.XX/XX./X../X../",".....XXX/....XX../XXXXX.../","X../X../X../X../XX./.XX/..X/..X/","...XXXXX/..XX..../XXX...../","X../X../XX./.XX/..X/..X/..X/..X/"},
				{"X....../XX...../.XX..../..XXXXX/","..XX/.XX./XX../X.../X.../X.../X.../","XXXXX../....XX./.....XX/......X/","...X/...X/...X/...X/..XX/.XX./XX../","......X/.....XX/....XX./XXXXX../","X.../X.../X.../X.../XX../.XX./..XX/","..XXXXX/.XX..../XX...../X....../","XX../.XX./..XX/...X/...X/...X/...X/"},
				{"XXX.../XX..../.XXXXX/",".XX/XXX/X.X/X../X../X../","XXXXX./....XX/...XXX/","..X/..X/..X/X.X/XXX/XX./","...XXX/....XX/XXXXX./","X../X../X../X.X/XXX/.XX/",".XXXXX/XX..../XXX.../","XX./XXX/X.X/..X/..X/..X/"},
				{".X..../XX..../XX..../.XXXXX/",".XX./XXXX/X.../X.../X.../X.../","XXXXX./....XX/....XX/....X./","...X/...X/...X/...X/XXXX/.XX./","....X./....XX/....XX/XXXXX./","X.../X.../X.../X.../XXXX/.XX./",".XXXXX/XX..../XX..../.X..../",".XX./XXXX/...X/...X/...X/...X/"},
				{"XX...../.X...../.XX..../..XXXXX/","...X/.XXX/XX../X.../X.../X.../X.../","XXXXX../....XX./.....X./.....XX/","...X/...X/...X/...X/..XX/XXX./X.../",".....XX/.....X./....XX./XXXXX../","X.../X.../X.../X.../XX../.XXX/...X/","..XXXXX/.XX..../.X...../XX...../","X.../XXX./..XX/...X/...X/...X/...X/"},
				{"XX..../X...../XX..../.XXXXX/",".XXX/XX.X/X.../X.../X.../X.../","XXXXX./....XX/.....X/....XX/","...X/...X/...X/...X/X.XX/XXX./","....XX/.....X/....XX/XXXXX./","X.../X.../X.../X.../XX.X/.XXX/",".XXXXX/XX..../X...../XX..../","XXX./X.XX/...X/...X/...X/...X/"},
				{"X...../X...../X...../XX..../.XXXXX/",".XXXX/XX.../X..../X..../X..../X..../","XXXXX./....XX/.....X/.....X/.....X/","....X/....X/....X/....X/...XX/XXXX./",".....X/.....X/.....X/....XX/XXXXX./","X..../X..../X..../X..../XX.../.XXXX/",".XXXXX/XX..../X...../X...../X...../","XXXX./...XX/....X/....X/....X/....X/"},
				{"..XX..../XXXXXXXX/","X./X./XX/XX/X./X./X./X./","XXXXXXXX/....XX../",".X/.X/.X/.X/XX/XX/.X/.X/","....XX../XXXXXXXX/","X./X./X./X./XX/XX/X./X./","XXXXXXXX/..XX..../",".X/.X/XX/XX/.X/.X/.X/.X/"},
				{".XX..../XXXXXXX/X....../","XX./.XX/.XX/.X./.X./.X./.X./","......X/XXXXXXX/....XX./",".X./.X./.X./.X./XX./XX./.XX/","....XX./XXXXXXX/......X/",".X./.X./.X./.X./.XX/.XX/XX./","X....../XXXXXXX/.XX..../",".XX/XX./XX./.X./.X./.X./.X./"},
				{".XX..../.XXXXXX/XX...../","X../XXX/.XX/.X./.X./.X./.X./",".....XX/XXXXXX./....XX./",".X./.X./.X./.X./XX./XXX/..X/","....XX./XXXXXX./.....XX/",".X./.X./.X./.X./.XX/XXX/X../","XX...../.XXXXXX/.XX..../","..X/XXX/XX./.X./.X./.X./.X./"},
				{"XX..../XXXXXX/XX..../","XXX/XXX/.X./.X./.X./.X./","....XX/XXXXXX/....XX/",".X./.X./.X./.X./XXX/XXX/"},
				{"XX..../XXXXXX/X...../X...../","XXXX/..XX/..X./..X./..X./..X./",".....X/.....X/XXXXXX/....XX/",".X../.X../.X../.X../XX../XXXX/","....XX/XXXXXX/.....X/.....X/","..X./..X./..X./..X./..XX/XXXX/","X...../X...../XXXXXX/XX..../","XXXX/XX../.X../.X../.X../.X../"},
				{"XXXXX/XXXXX/","XX/XX/XX/XX/XX/"},
				{"...X./XXXX./XXXXX/","XX./XX./XX./XXX/X../","XXXXX/.XXXX/.X.../","..X/XXX/.XX/.XX/.XX/",".X.../.XXXX/XXXXX/","X../XXX/XX./XX./XX./","XXXXX/XXXX./...X./",".XX/.XX/.XX/XXX/..X/"},
				{".XX../XXX../XXXXX/","XX./XXX/XXX/X../X../","XXXXX/..XXX/..XX./","..X/..X/XXX/XXX/.XX/","..XX./..XXX/XXXXX/","X../X../XXX/XXX/XX./","XXXXX/XXX../.XX../",".XX/XXX/XXX/..X/..X/"},
				{"..XX./XXX../XXXXX/","XX./XX./XXX/X.X/X../","XXXXX/..XXX/.XX../","..X/X.X/XXX/.XX/.XX/",".XX../..XXX/XXXXX/","X../X.X/XXX/XX./XX./","XXXXX/XXX../..XX./",".XX/.XX/XXX/X.X/..X/"},
				{"..X../..X../XXX../XXXXX/","XX../XX../XXXX/X.../X.../","XXXXX/..XXX/..X../..X../","...X/...X/XXXX/..XX/..XX/","..X../..X../..XXX/XXXXX/","X.../X.../XXXX/XX../XX../","XXXXX/XXX../..X../..X../","..XX/..XX/XXXX/...X/...X/"},
				{"XXX.../.XX.../.XXXXX/","..X/XXX/XXX/X../X../X../","XXXXX./...XX./...XXX/","..X/..X/..X/XXX/XXX/X../","...XXX/...XX./XXXXX./","X../X../X../XXX/XXX/..X/",".XXXXX/.XX.../XXX.../","X../XXX/XXX/..X/..X/..X/"},
				{"X..../XX.../XX.../XXXXX/","XXXX/XXX./X.../X.../X.../","XXXXX/...XX/...XX/....X/","...X/...X/...X/.XXX/XXXX/","....X/...XX/...XX/XXXXX/","X.../X.../X.../XXX./XXXX/","XXXXX/XX.../XX.../X..../","XXXX/.XXX/...X/...X/...X/"},
				{".XXX./XX.../XXXXX/","XX./XXX/X.X/X.X/X../","XXXXX/...XX/.XXX./","..X/X.X/X.X/XXX/.XX/",".XXX./...XX/XXXXX/","X../X.X/X.X/XXX/XX./","XXXXX/XX.../.XXX./",".XX/XXX/X.X/X.X/..X/"},
				{"..X../.XX../XX.../XXXXX/","XX../XXX./X.XX/X.../X.../","XXXXX/...XX/..XX./..X../","...X/...X/XX.X/.XXX/..XX/","..X../..XX./...XX/XXXXX/","X.../X.../X.XX/XXX./XX../","XXXXX/XX.../.XX../..X../","..XX/.XXX/XX.X/...X/...X/"},
				{"XX.../.X.../XX.../XXXXX/","XX.X/XXXX/X.../X.../X.../","XXXXX/...XX/...X./...XX/","...X/...X/...X/XXXX/X.XX/","...XX/...X./...XX/XXXXX/","X.../X.../X.../XXXX/XX.X/","XXXXX/XX.../.X.../XX.../","X.XX/XXXX/...X/...X/...X/"},
				{".XX../.X.../XX.../XXXXX/","XX../XXXX/X..X/X.../X.../","XXXXX/...XX/...X./..XX./","...X/...X/X..X/XXXX/..XX/","..XX./...X./...XX/XXXXX/","X.../X.../X..X/XXXX/XX../","XXXXX/XX.../.X.../.XX../","..XX/XXXX/X..X/...X/...X/"},
				{".X.../.X.../.X.../XX.../XXXXX/","XX.../XXXXX/X..../X..../X..../","XXXXX/...XX/...X./...X./...X./","....X/....X/....X/XXXXX/...XX/","...X./...X./...X./...XX/XXXXX/","X..../X..../X..../XXXXX/XX.../","XXXXX/XX.../.X.../.X.../.X.../","...XX/XXXXX/....X/....X/....X/"},
				{"XXXX..../...X..../...XXXXX/","..X/..X/..X/XXX/X../X../X../X../","XXXXX.../....X.../....XXXX/","..X/..X/..X/..X/XXX/X../X../X../","....XXXX/....X.../XXXXX.../","X../X../X../X../XXX/..X/..X/..X/","...XXXXX/...X..../XXXX..../","X../X../X../XXX/..X/..X/..X/..X/"},
				{"X....../XXX..../..X..../..XXXXX/","..XX/..X./XXX./X.../X.../X.../X.../","XXXXX../....X../....XXX/......X/","...X/...X/...X/...X/.XXX/.X../XX../","......X/....XXX/....X../XXXXX../","X.../X.../X.../X.../XXX./..X./..XX/","..XXXXX/..X..../XXX..../X....../","XX../.X../.XXX/...X/...X/...X/...X/"},
				{"XXX..../X.X..../..XXXXX/",".XX/..X/XXX/X../X../X../X../","XXXXX../....X.X/....XXX/","..X/..X/..X/..X/XXX/X../XX./","....XXX/....X.X/XXXXX../","X../X../X../X../XXX/..X/.XX/","..XXXXX/X.X..../XXX..../","XX./X../XXX/..X/..X/..X/..X/"},
				{"XX...../.XX..../..X..../..XXXXX/","...X/..XX/XXX./X.../X.../X.../X.../","XXXXX../....X../....XX./.....XX/","...X/...X/...X/...X/.XXX/XX../X.../",".....XX/....XX./....X../XXXXX../","X.../X.../X.../X.../XXX./..XX/...X/","..XXXXX/..X..../.XX..../XX...../","X.../XX../.XXX/...X/...X/...X/...X/"},
				{"XX..../XX..../.X..../.XXXXX/","..XX/XXXX/X.../X.../X.../X.../","XXXXX./....X./....XX/....XX/","...X/...X/...X/...X/XXXX/XX../","....XX/....XX/....X./XXXXX./","X.../X.../X.../X.../XXXX/..XX/",".XXXXX/.X..../XX..../XX..../","XX../XXXX/...X/...X/...X/...X/"},
				{"X...../X...../XX..../.X..../.XXXXX/","..XXX/XXX../X..../X..../X..../X..../","XXXXX./....X./....XX/.....X/.....X/","....X/....X/....X/....X/..XXX/XXX../",".....X/.....X/....XX/....X./XXXXX./","X..../X..../X..../X..../XXX../..XXX/",".XXXXX/.X..../XX..../X...../X...../","XXX../..XXX/....X/....X/....X/....X/"},
				{".XX..../XXX..../..XXXXX/",".X./.XX/XXX/X../X../X../X../","XXXXX../....XXX/....XX./","..X/..X/..X/..X/XXX/XX./.X./","....XX./....XXX/XXXXX../","X../X../X../X../XXX/.XX/.X./","..XXXXX/XXX..../.XX..../",".X./XX./XXX/..X/..X/..X/..X/"},
				{"XXXX./X..../XXXXX/","XXX/X.X/X.X/X.X/X../","XXXXX/....X/.XXXX/","..X/X.X/X.X/X.X/XXX/",".XXXX/....X/XXXXX/","X../X.X/X.X/X.X/XXX/","XXXXX/X..../XXXX./","XXX/X.X/X.X/X.X/..X/"},
				{"..X../XXX../X..../XXXXX/","XXX./X.X./X.XX/X.../X.../","XXXXX/....X/..XXX/..X../","...X/...X/XX.X/.X.X/.XXX/","..X../..XXX/....X/XXXXX/","X.../X.../X.XX/X.X./XXX./","XXXXX/X..../XXX../..X../",".XXX/.X.X/XX.X/...X/...X/"},
				{"XXX../X.X../XXXXX/","XXX/X.X/XXX/X../X../","XXXXX/..X.X/..XXX/","..X/..X/XXX/X.X/XXX/","..XXX/..X.X/XXXXX/","X../X../XXX/X.X/XXX/","XXXXX/X.X../XXX../","XXX/X.X/XXX/..X/..X/"},
				{"XX.../XX.../X..../XXXXX/","XXXX/X.XX/X.../X.../X.../","XXXXX/....X/...XX/...XX/","...X/...X/...X/XX.X/XXXX/","...XX/...XX/....X/XXXXX/","X.../X.../X.../X.XX/XXXX/","XXXXX/X..../XX.../XX.../","XXXX/XX.X/...X/...X/...X/"},
				{".XX../XX.../X..../XXXXX/","XXX./X.XX/X..X/X.../X.../","XXXXX/....X/...XX/..XX./","...X/...X/X..X/XX.X/.XXX/","..XX./...XX/....X/XXXXX/","X.../X.../X..X/X.XX/XXX./","XXXXX/X..../XX.../.XX../",".XXX/XX.X/X..X/...X/...X/"},
				{".X.../.X.../XX.../X..../XXXXX/","XXX../X.XXX/X..../X..../X..../","XXXXX/....X/...XX/...X./...X./","....X/....X/....X/XXX.X/..XXX/","...X./...X./...XX/....X/XXXXX/","X..../X..../X..../X.XXX/XXX../","XXXXX/X..../XX.../.X.../.X.../","..XXX/XXX.X/....X/....X/....X/"},
				{"XX.../XXX../XXXXX/","XXX/XXX/XX./X../X../","XXXXX/..XXX/...XX/","..X/..X/.XX/XXX/XXX/","...XX/..XXX/XXXXX/","X../X../XX./XXX/XXX/","XXXXX/XXX../XX.../","XXX/XXX/.XX/..X/..X/"},
				{"XXX..../..X..../..X..../..XXXXX/","...X/...X/XXXX/X.../X.../X.../X.../","XXXXX../....X../....X../....XXX/","...X/...X/...X/...X/XXXX/X.../X.../","....XXX/....X../....X../XXXXX../","X.../X.../X.../X.../XXXX/...X/...X/","..XXXXX/..X..../..X..../XXX..../","X.../X.../XXXX/...X/...X/...X/...X/"},
				{"X...../XX..../.X..../.X..../.XXXXX/","...XX/XXXX./X..../X..../X..../X..../","XXXXX./....X./....X./....XX/.....X/","....X/....X/....X/....X/.XXXX/XX.../",".....X/....XX/....X./....X./XXXXX./","X..../X..../X..../X..../XXXX./...XX/",".XXXXX/.X..../.X..../XX..../X...../","XX.../.XXXX/....X/....X/....X/....X/"},
				{"XXX../X..../X..../XXXXX/","XXXX/X..X/X..X/X.../X.../","XXXXX/....X/....X/..XXX/","...X/...X/X..X/X..X/XXXX/","..XXX/....X/....X/XXXXX/","X.../X.../X..X/X..X/XXXX/","XXXXX/X..../X..../XXX../","XXXX/X..X/X..X/...X/...X/"},
				{".X.../XX.../X..../X..../XXXXX/","XXXX./X..XX/X..../X..../X..../","XXXXX/....X/....X/...XX/...X./","....X/....X/....X/XX..X/.XXXX/","...X./...XX/....X/....X/XXXXX/","X..../X..../X..../X..XX/XXXX./","XXXXX/X..../X..../XX.../.X.../",".XXXX/XX..X/....X/....X/....X/"},
				{"XX..../.X..../.X..../.X..../.XXXXX/","....X/XXXXX/X..../X..../X..../X..../","XXXXX./....X./....X./....X./....XX/","....X/....X/....X/....X/XXXXX/X..../","....XX/....X./....X./....X./XXXXX./","X..../X..../X..../X..../XXXXX/....X/",".XXXXX/.X..../.X..../.X..../XX..../","X..../XXXXX/....X/....X/....X/....X/"},
				{"XX.../X..../X..../X..../XXXXX/","XXXXX/X...X/X..../X..../X..../","XXXXX/....X/....X/....X/...XX/","....X/....X/....X/X...X/XXXXX/","...XX/....X/....X/....X/XXXXX/","X..../X..../X..../X...X/XXXXX/","XXXXX/X..../X..../X..../XX.../","XXXXX/X...X/....X/....X/....X/"},
				{"X......./XXXXX.../....XXXX/",".XX/.X./.X./.X./XX./X../X../X../","XXXX..../...XXXXX/.......X/","..X/..X/..X/.XX/.X./.X./.X./XX./",".......X/...XXXXX/XXXX..../","X../X../X../XX./.X./.X./.X./.XX/","....XXXX/XXXXX.../X......./","XX./.X./.X./.X./.XX/..X/..X/..X/"},
				{"XXXXX.../X...XXXX/","XX/.X/.X/.X/XX/X./X./X./","XXXX...X/...XXXXX/",".X/.X/.X/XX/X./X./X./XX/","...XXXXX/XXXX...X/","X./X./X./XX/.X/.X/.X/XX/","X...XXXX/XXXXX.../","XX/X./X./X./XX/.X/.X/.X/"},
				{"XX....../.XXXX.../....XXXX/","..X/.XX/.X./.X./XX./X../X../X../","XXXX..../...XXXX./......XX/","..X/..X/..X/.XX/.X./.X./XX./X../","......XX/...XXXX./XXXX..../","X../X../X../XX./.X./.X./.XX/..X/","....XXXX/.XXXX.../XX....../","X../XX./.X./.X./.XX/..X/..X/..X/"},
				{"XX...../XXXX.../...XXXX/",".XX/.XX/.X./XX./X../X../X../","XXXX.../...XXXX/.....XX/","..X/..X/..X/.XX/.X./XX./XX./",".....XX/...XXXX/XXXX.../","X../X../X../XX./.X./.XX/.XX/","...XXXX/XXXX.../XX...../","XX./XX./.X./.XX/..X/..X/..X/"},
				{"X....../X....../XXXX.../...XXXX/",".XXX/.X../.X../XX../X.../X.../X.../","XXXX.../...XXXX/......X/......X/","...X/...X/...X/..XX/..X./..X./XXX./","......X/......X/...XXXX/XXXX.../","X.../X.../X.../XX../.X../.X../.XXX/","...XXXX/XXXX.../X....../X....../","XXX./..X./..X./..XX/...X/...X/...X/"},
				{".XXXX.../XX..XXXX/","X./XX/.X/.X/XX/X./X./X./","XXXX..XX/...XXXX./",".X/.X/.X/XX/X./X./XX/.X/","...XXXX./XXXX..XX/","X./X./X./XX/.X/.X/XX/X./","XX..XXXX/.XXXX.../",".X/XX/X./X./XX/.X/.X/.X/"},
				{"XXXX.../XX.XXXX/","XX/XX/.X/XX/X./X./X./","XXXX.XX/...XXXX/",".X/.X/.X/XX/X./XX/XX/","...XXXX/XXXX.XX/","X./X./X./XX/.X/XX/XX/","XX.XXXX/XXXX.../","XX/XX/X./XX/.X/.X/.X/"},
				{"XXXX.../X..XXXX/X....../","XXX/..X/..X/.XX/.X./.X./.X./","......X/XXXX..X/...XXXX/",".X./.X./.X./XX./X../X../XXX/","...XXXX/XXXX..X/......X/",".X./.X./.X./.XX/..X/..X/XXX/","X....../X..XXXX/XXXX.../","XXX/X../X../XX./.X./.X./.X./"},
				{"XXX...../..XXX.../....XXXX/","..X/..X/.XX/.X./XX./X../X../X../","XXXX..../...XXX../.....XXX/","..X/..X/..X/.XX/.X./XX./X../X../",".....XXX/...XXX../XXXX..../","X../X../X../XX./.X./.XX/..X/..X/","....XXXX/..XXX.../XXX...../","X../X../XX./.X./.XX/..X/..X/..X/"},
				{"X....../XX...../.XXX.../...XXXX/","..XX/.XX./.X../XX../X.../X.../X.../","XXXX.../...XXX./.....XX/......X/","...X/...X/...X/..XX/..X./.XX./XX../","......X/.....XX/...XXX./XXXX.../","X.../X.../X.../XX../.X../.XX./..XX/","...XXXX/.XXX.../XX...../X....../","XX../.XX./..X./..XX/...X/...X/...X/"},
				{"XXX.../XXX.../..XXXX/",".XX/.XX/XXX/X../X../X../","XXXX../...XXX/...XXX/","..X/..X/..X/XXX/XX./XX./","...XXX/...XXX/XXXX../","X../X../X../XXX/.XX/.XX/","..XXXX/XXX.../XXX.../","XX./XX./XXX/..X/..X/..X/"},
				{".X..../XX..../XXX.../..XXXX/",".XX./.XXX/XX../X.../X.../X.../","XXXX../...XXX/....XX/....X./","...X/...X/...X/..XX/XXX./.XX./","....X./....XX/...XXX/XXXX../","X.../X.../X.../XX../.XXX/.XX./","..XXXX/XXX.../XX..../.X..../",".XX./XXX./..XX/...X/...X/...X/"},
				{"XX...../.X...../.XXX.../...XXXX/","...X/.XXX/.X../XX../X.../X.../X.../","XXXX.../...XXX./.....X./.....XX/","...X/...X/...X/..XX/..X./XXX./X.../",".....XX/.....X./...XXX./XXXX.../","X.../X.../X.../XX../.X../.XXX/...X/","...XXXX/.XXX.../.X...../XX...../","X.../XXX./..X./..XX/...X/...X/...X/"},
				{"XX..../X...../XXX.../..XXXX/",".XXX/.X.X/XX../X.../X.../X.../","XXXX../...XXX/.....X/....XX/","...X/...X/...X/..XX/X.X./XXX./","....XX/.....X/...XXX/XXXX../","X.../X.../X.../XX../.X.X/.XXX/","..XXXX/XXX.../X...../XX..../","XXX./X.X./..XX/...X/...X/...X/"},
				{"X...../X...../X...../XXX.../..XXXX/",".XXXX/.X.../XX.../X..../X..../X..../","XXXX../...XXX/.....X/.....X/.....X/","....X/....X/....X/...XX/...X./XXXX./",".....X/.....X/.....X/...XXX/XXXX../","X..../X..../X..../XX.../.X.../.XXXX/","..XXXX/XXX.../X...../X...../X...../","XXXX./...X./...XX/....X/....X/....X/"},
				{"..XXX.../XXX.XXXX/","X./X./XX/.X/XX/X./X./X./","XXXX.XXX/...XXX../",".X/.X/.X/XX/X./XX/.X/.X/","...XXX../XXXX.XXX/","X./X./X./XX/.X/XX/X./X./","XXX.XXXX/..XXX.../",".X/.X/XX/X./XX/.X/.X/.X/"},
				{".XXX.../XX.XXXX/X....../","XX./.XX/..X/.XX/.X./.X./.X./","......X/XXXX.XX/...XXX./",".X./.X./.X./XX./X../XX./.XX/","...XXX./XXXX.XX/......X/",".X./.X./.X./.XX/..X/.XX/XX./","X....../XX.XXXX/.XXX.../",".XX/XX./X../XX./.X./.X./.X./"},
				{"XXX.../XXXXXX/.X..../",".XX/XXX/.XX/.X./.X./.X./","....X./XXXXXX/...XXX/",".X./.X./.X./XX./XXX/XX./","...XXX/XXXXXX/....X./",".X./.X./.X./.XX/XXX/.XX/",".X..../XXXXXX/XXX.../","XX./XXX/XX./.X./.X./.X./"},
				{".XXX.../.X.XXXX/XX...../","X../XXX/..X/.XX/.X./.X./.X./",".....XX/XXXX.X./...XXX./",".X./.X./.X./XX./X../XXX/..X/","...XXX./XXXX.X./.....XX/",".X./.X./.X./.XX/..X/XXX/X../","XX...../.X.XXXX/.XXX.../","..X/XXX/X../XX./.X./.X./.X./"},
				{"XXX.../X.XXXX/XX..../","XXX/X.X/.XX/.X./.X./.X./","....XX/XXXX.X/...XXX/",".X./.X./.X./XX./X.X/XXX/","...XXX/XXXX.X/....XX/",".X./.X./.X./.XX/X.X/XXX/","XX..../X.XXXX/XXX.../","XXX/X.X/XX./.X./.X./.X./"},
				{"XXX.../X.XXXX/X...../X...../","XXXX/...X/..XX/..X./..X./..X./",".....X/.....X/XXXX.X/...XXX/",".X../.X../.X../XX../X.../XXXX/","...XXX/XXXX.X/.....X/.....X/","..X./..X./..X./..XX/...X/XXXX/","X...../X...../X.XXXX/XXX.../","XXXX/X.../XX../.X../.X../.X../"},
				{"XXXX..../...XX.../....XXXX/","..X/..X/..X/.XX/XX./X../X../X../","....XXXX/...XX.../XXXX..../","X../X../X../XX./.XX/..X/..X/..X/"},
				{"X....../XXX..../..XX.../...XXXX/","..XX/..X./.XX./XX../X.../X.../X.../","XXXX.../...XX../....XXX/......X/","...X/...X/...X/..XX/.XX./.X../XX../","......X/....XXX/...XX../XXXX.../","X.../X.../X.../XX../.XX./..X./..XX/","...XXXX/..XX.../XXX..../X....../","XX../.X../.XX./..XX/...X/...X/...X/"},
				{"XXX..../X.XX.../...XXXX/",".XX/..X/.XX/XX./X../X../X../","XXXX.../...XX.X/....XXX/","..X/..X/..X/.XX/XX./X../XX./","....XXX/...XX.X/XXXX.../","X../X../X../XX./.XX/..X/.XX/","...XXXX/X.XX.../XXX..../","XX./X../XX./.XX/..X/..X/..X/"},
				{"XX...../.XX..../..XX.../...XXXX/","...X/..XX/.XX./XX../X.../X.../X.../","XXXX.../...XX../....XX./.....XX/","...X/...X/...X/..XX/.XX./XX../X.../",".....XX/....XX./...XX../XXXX.../","X.../X.../X.../XX../.XX./..XX/...X/","...XXXX/..XX.../.XX..../XX...../","X.../XX../.XX./..XX/...X/...X/...X/"},
				{"XX..../XX..../.XX.../..XXXX/","..XX/.XXX/XX../X.../X.../X.../","XXXX../...XX./....XX/....XX/","...X/...X/...X/..XX/XXX./XX../","....XX/....XX/...XX./XXXX../","X.../X.../X.../XX../.XXX/..XX/","..XXXX/.XX.../XX..../XX..../","XX../XXX./..XX/...X/...X/...X/"},
				{"X...../X...../XX..../.XX.../..XXXX/","..XXX/.XX../XX.../X..../X..../X..../","XXXX../...XX./....XX/.....X/.....X/","....X/....X/....X/...XX/..XX./XXX../",".....X/.....X/....XX/...XX./XXXX../","X..../X..../X..../XX.../.XX../..XXX/","..XXXX/.XX.../XX..../X...../X...../","XXX../..XX./...XX/....X/....X/....X/"},
				{".XX..../XXXX.../...XXXX/",".X./.XX/.XX/XX./X../X../X../","XXXX.../...XXXX/....XX./","..X/..X/..X/.XX/XX./XX./.X./","....XX./...XXXX/XXXX.../","X../X../X../XX./.XX/.XX/.X./","...XXXX/XXXX.../.XX..../",".X./XX./XX./.XX/..X/..X/..X/"},
				{"XX..../XXX.../X.XXXX/","XXX/.XX/XX./X../X../X../","XXXX.X/...XXX/....XX/","..X/..X/..X/.XX/XX./XXX/","....XX/...XXX/XXXX.X/","X../X../X../XX./.XX/XXX/","X.XXXX/XXX.../XX..../","XXX/XX./.XX/..X/..X/..X/"},
				{"XXXX./XX.../.XXXX/",".XX/XXX/X.X/X.X/X../","XXXX./...XX/.XXXX/","..X/X.X/X.X/XXX/XX./",".XXXX/...XX/XXXX./","X../X.X/X.X/XXX/.XX/",".XXXX/XX.../XXXX./","XX./XXX/X.X/X.X/..X/"},
				{"..X../XXX../XX.../.XXXX/",".XX./XXX./X.XX/X.../X.../","XXXX./...XX/..XXX/..X../","...X/...X/XX.X/.XXX/.XX./","..X../..XXX/...XX/XXXX./","X.../X.../X.XX/XXX./.XX./",".XXXX/XX.../XXX../..X../",".XX./.XXX/XX.X/...X/...X/"},
				{"XXX../XXX../.XXXX/",".XX/XXX/XXX/X../X../","XXXX./..XXX/..XXX/","..X/..X/XXX/XXX/XX./","..XXX/..XXX/XXXX./","X../X../XXX/XXX/.XX/",".XXXX/XXX../XXX../","XX./XXX/XXX/..X/..X/"},
				{"XX.../XX.../XX.../.XXXX/",".XXX/XXXX/X.../X.../X.../","XXXX./...XX/...XX/...XX/","...X/...X/...X/XXXX/XXX./","...XX/...XX/...XX/XXXX./","X.../X.../X.../XXXX/.XXX/",".XXXX/XX.../XX.../XX.../","XXX./XXXX/...X/...X/...X/"},
				{".XX../XX.../XX.../.XXXX/",".XX./XXXX/X..X/X.../X.../","XXXX./...XX/...XX/..XX./","...X/...X/X..X/XXXX/.XX./","..XX./...XX/...XX/XXXX./","X.../X.../X..X/XXXX/.XX./",".XXXX/XX.../XX.../.XX../",".XX./XXXX/X..X/...X/...X/"},
				{".X.../.X.../XX.../XX.../.XXXX/",".XX../XXXXX/X..../X..../X..../","XXXX./...XX/...XX/...X./...X./","....X/....X/....X/XXXXX/..XX./","...X./...X./...XX/...XX/XXXX./","X..../X..../X..../XXXXX/.XX../",".XXXX/XX.../XX.../.X.../.X.../","..XX./XXXXX/....X/....X/....X/"},
				{"XXX..../..X..../..XX.../...XXXX/","...X/...X/.XXX/XX../X.../X.../X.../","XXXX.../...XX../....X../....XXX/","...X/...X/...X/..XX/XXX./X.../X.../","....XXX/....X../...XX../XXXX.../","X.../X.../X.../XX../.XXX/...X/...X/","...XXXX/..XX.../..X..../XXX..../","X.../X.../XXX./..XX/...X/...X/...X/"},
				{"X...../XX..../.X..../.XX.../..XXXX/","...XX/.XXX./XX.../X..../X..../X..../","XXXX../...XX./....X./....XX/.....X/","....X/....X/....X/...XX/.XXX./XX.../",".....X/....XX/....X./...XX./XXXX../","X..../X..../X..../XX.../.XXX./...XX/","..XXXX/.XX.../.X..../XX..../X...../","XX.../.XXX./...XX/....X/....X/....X/"},
				{"XXX../X..../XX.../.XXXX/",".XXX/XX.X/X..X/X.../X.../","XXXX./...XX/....X/..XXX/","...X/...X/X..X/X.XX/XXX./","..XXX/....X/...XX/XXXX./","X.../X.../X..X/XX.X/.XXX/",".XXXX/XX.../X..../XXX../","XXX./X.XX/X..X/...X/...X/"},
				{".X.../XX.../X..../XX.../.XXXX/",".XXX./XX.XX/X..../X..../X..../","XXXX./...XX/....X/...XX/...X./","....X/....X/....X/XX.XX/.XXX./","...X./...XX/....X/...XX/XXXX./","X..../X..../X..../XX.XX/.XXX./",".XXXX/XX.../X..../XX.../.X.../",".XXX./XX.XX/....X/....X/....X/"},
				{"XX..../.X..../.X..../.XX.../..XXXX/","....X/.XXXX/XX.../X..../X..../X..../","XXXX../...XX./....X./....X./....XX/","....X/....X/....X/...XX/XXXX./X..../","....XX/....X./....X./...XX./XXXX../","X..../X..../X..../XX.../.XXXX/....X/","..XXXX/.XX.../.X..../.X..../XX..../","X..../XXXX./...XX/....X/....X/....X/"},
				{"XX.../X..../X..../XX.../.XXXX/",".XXXX/XX..X/X..../X..../X..../","XXXX./...XX/....X/....X/...XX/","....X/....X/....X/X..XX/XXXX./","...XX/....X/....X/...XX/XXXX./","X..../X..../X..../XX..X/.XXXX/",".XXXX/XX.../X..../X..../XX.../","XXXX./X..XX/....X/....X/....X/"},
				{"...XX.../XXXXXXXX/","X./X./X./XX/XX/X./X./X./","XXXXXXXX/...XX.../",".X/.X/.X/XX/XX/.X/.X/.X/"},
				{"X.XX.../XXXXXXX/","XX/X./XX/XX/X./X./X./","XXXXXXX/...XX.X/",".X/.X/.X/XX/XX/.X/XX/","...XX.X/XXXXXXX/","X./X./X./XX/XX/X./XX/","XXXXXXX/X.XX.../","XX/.X/XX/XX/.X/.X/.X/"},
				{"..XX.../XXXXXXX/X....../","XX./.X./.XX/.XX/.X./.X./.X./","......X/XXXXXXX/...XX../",".X./.X./.X./XX./XX./.X./.XX/","...XX../XXXXXXX/......X/",".X./.X./.X./.XX/.XX/.X./XX./","X....../XXXXXXX/..XX.../",".XX/.X./XX./XX./.X./.X./.X./"},
				{"XXXX.../.XXXXXX/",".X/XX/XX/XX/X./X./X./","XXXXXX./...XXXX/",".X/.X/.X/XX/XX/XX/X./","...XXXX/XXXXXX./","X./X./X./XX/XX/XX/.X/",".XXXXXX/XXXX.../","X./XX/XX/XX/.X/.X/.X/"},
				{"X...../XXX.../XXXXXX/","XXX/XX./XX./X../X../X../","XXXXXX/...XXX/.....X/","..X/..X/..X/.XX/.XX/XXX/",".....X/...XXX/XXXXXX/","X../X../X../XX./XX./XXX/","XXXXXX/XXX.../X...../","XXX/.XX/.XX/..X/..X/..X/"},
				{"..XX.../.XXXXXX/XX...../","X../XX./.XX/.XX/.X./.X./.X./",".....XX/XXXXXX./...XX../",".X./.X./.X./XX./XX./.XX/..X/","...XX../XXXXXX./.....XX/",".X./.X./.X./.XX/.XX/XX./X../","XX...../.XXXXXX/..XX.../","..X/.XX/XX./XX./.X./.X./.X./"},
				{".XX.../XXXXXX/XX..../","XX./XXX/.XX/.X./.X./.X./","....XX/XXXXXX/...XX./",".X./.X./.X./XX./XXX/.XX/","...XX./XXXXXX/....XX/",".X./.X./.X./.XX/XXX/XX./","XX..../XXXXXX/.XX.../",".XX/XXX/XX./.X./.X./.X./"},
				{".XX.../XXXXXX/X...../X...../","XXX./..XX/..XX/..X./..X./..X./",".....X/.....X/XXXXXX/...XX./",".X../.X../.X../XX../XX../.XXX/","...XX./XXXXXX/.....X/.....X/","..X./..X./..X./..XX/..XX/XXX./","X...../X...../XXXXXX/.XX.../",".XXX/XX../XX../.X../.X../.X../"},
				{"..XX.../..XXXXX/XXX..../","X../X../XXX/.XX/.X./.X./.X./","....XXX/XXXXX../...XX../",".X./.X./.X./XX./XXX/..X/..X/","...XX../XXXXX../....XXX/",".X./.X./.X./.XX/XXX/X../X../","XXX..../..XXXXX/..XX.../","..X/..X/XXX/XX./.X./.X./.X./"},
				{".XX.../.XXXXX/XX..../X...../","XX../.XXX/..XX/..X./..X./..X./",".....X/....XX/XXXXX./...XX./",".X../.X../.X../XX../XXX./..XX/","...XX./XXXXX./....XX/.....X/","..X./..X./..X./..XX/.XXX/XX../","X...../XX..../.XXXXX/.XX.../","..XX/XXX./XX../.X../.X../.X../"},
				{"XX.../XXXXX/XXX../","XXX/XXX/XX./.X./.X./","..XXX/XXXXX/...XX/",".X./.X./.XX/XXX/XXX/","...XX/XXXXX/..XXX/",".X./.X./XX./XXX/XXX/","XXX../XXXXX/XX.../","XXX/XXX/.XX/.X./.X./"},
				{"XX.../XXXXX/XX.../.X.../",".XXX/XXXX/..X./..X./..X./","...X./...XX/XXXXX/...XX/",".X../.X../.X../XXXX/XXX./","...XX/XXXXX/...XX/...X./","..X./..X./..X./XXXX/.XXX/",".X.../XX.../XXXXX/XX.../","XXX./XXXX/.X../.X../.X../"},
				{".XX.../.XXXXX/.X..../XX..../","X.../XXXX/..XX/..X./..X./..X./","....XX/....X./XXXXX./...XX./",".X../.X../.X../XX../XXXX/...X/","...XX./XXXXX./....X./....XX/","..X./..X./..X./..XX/XXXX/X.../","XX..../.X..../.XXXXX/.XX.../","...X/XXXX/XX../.X../.X../.X../"},
				{"XX.../XXXXX/X..../XX.../","XXXX/X.XX/..X./..X./..X./","...XX/....X/XXXXX/...XX/",".X../.X../.X../XX.X/XXXX/","...XX/XXXXX/....X/...XX/","..X./..X./..X./X.XX/XXXX/","XX.../X..../XXXXX/XX.../","XXXX/XX.X/.X../.X../.X../"},
				{"....X/XXXXX/XXXX./","XX./XX./XX./XX./.XX/",".XXXX/XXXXX/X..../","XX./.XX/.XX/.XX/.XX/","X..../XXXXX/.XXXX/",".XX/XX./XX./XX./XX./","XXXX./XXXXX/....X/",".XX/.XX/.XX/.XX/XX./"},
				{"..XX/XXXX/XXXX/","XX./XX./XXX/XXX/","XXXX/XXXX/XX../","XXX/XXX/.XX/.XX/","XX../XXXX/XXXX/","XXX/XXX/XX./XX./","XXXX/XXXX/..XX/",".XX/.XX/XXX/XXX/"},
				{"...XX/XXXX./XXXX./","XX./XX./XX./XXX/..X/",".XXXX/.XXXX/XX.../","X../XXX/.XX/.XX/.XX/","XX.../.XXXX/.XXXX/","..X/XXX/XX./XX./XX./","XXXX./XXXX./...XX/",".XX/.XX/.XX/XXX/X../"},
				{"...X/...X/XXXX/XXXX/","XX../XX../XX../XXXX/","XXXX/XXXX/X.../X.../","XXXX/..XX/..XX/..XX/","X.../X.../XXXX/XXXX/","XXXX/XX../XX../XX../","XXXX/XXXX/...X/...X/","..XX/..XX/..XX/XXXX/"},
				{"XXX./XXX./XXXX/","XXX/XXX/XXX/X../","XXXX/.XXX/.XXX/","..X/XXX/XXX/XXX/",".XXX/.XXX/XXXX/","X../XXX/XXX/XXX/","XXXX/XXX./XXX./","XXX/XXX/XXX/..X/"},
				{".X../.XX./XXX./XXXX/","XX../XXXX/XXX./X.../","XXXX/.XXX/.XX./..X./","...X/.XXX/XXXX/..XX/","..X./.XX./.XXX/XXXX/","X.../XXX./XXXX/XX../","XXXX/XXX./.XX./.X../","..XX/XXXX/.XXX/...X/"},
				{"..XXX/XXX../XXXX./","XX./XX./XXX/X.X/..X/",".XXXX/..XXX/XXX../","X../X.X/XXX/.XX/.XX/","XXX../..XXX/.XXXX/","..X/X.X/XXX/XX./XX./","XXXX./XXX../..XXX/",".XX/.XX/XXX/X.X/X../"},
				{"...X/..XX/XXX./XXXX/","XX../XX../XXX./X.XX/","XXXX/.XXX/XX../X.../","XX.X/.XXX/..XX/..XX/","X.../XX../.XXX/XXXX/","X.XX/XXX./XX../XX../","XXXX/XXX./..XX/...X/","..XX/..XX/.XXX/XX.X/"},
				{".XX./..X./XXX./XXXX/","XX../XX.X/XXXX/X.../","XXXX/.XXX/.X../.XX./","...X/XXXX/X.XX/..XX/",".XX./.X../.XXX/XXXX/","X.../XXXX/XX.X/XX../","XXXX/XXX./..X./.XX./","..XX/X.XX/XXXX/...X/"},
				{"..XX/..X./XXX./XXXX/","XX../XX../XXXX/X..X/","XXXX/.XXX/.X../XX../","X..X/XXXX/..XX/..XX/","XX../.X../.XXX/XXXX/","X..X/XXXX/XX../XX../","XXXX/XXX./..X./..XX/","..XX/..XX/XXXX/X..X/"},
				{"..X./..X./..X./XXX./XXXX/","XX.../XX.../XXXXX/X..../","XXXX/.XXX/.X../.X../.X../","....X/XXXXX/...XX/...XX/",".X../.X../.X../.XXX/XXXX/","X..../XXXXX/XX.../XX.../","XXXX/XXX./..X./..X./..X./","...XX/...XX/XXXXX/....X/"},
				{"XXXX../..XX../..XXXX/","..X/..X/XXX/XXX/X../X../","..XXXX/..XX../XXXX../","X../X../XXX/XXX/..X/..X/"},
				{"X..../XXX../.XX../.XXXX/","..XX/XXX./XXX./X.../X.../","XXXX./..XX./..XXX/....X/","...X/...X/.XXX/.XXX/XX../","....X/..XXX/..XX./XXXX./","X.../X.../XXX./XXX./..XX/",".XXXX/.XX../XXX../X..../","XX../.XXX/.XXX/...X/...X/"},
				{"XX.../.XX../.XX../.XXXX/","...X/XXXX/XXX./X.../X.../","XXXX./..XX./..XX./...XX/","...X/...X/.XXX/XXXX/X.../","...XX/..XX./..XX./XXXX./","X.../X.../XXX./XXXX/...X/",".XXXX/.XX../.XX../XX.../","X.../XXXX/.XXX/...X/...X/"},
				{"X.../X.../XX../XX../XXXX/","XXXXX/XXX../X..../X..../","XXXX/..XX/..XX/...X/...X/","....X/....X/..XXX/XXXXX/","...X/...X/..XX/..XX/XXXX/","X..../X..../XXX../XXXXX/","XXXX/XX../XX../X.../X.../","XXXXX/..XXX/....X/....X/"},
				{"...X/.XXX/XX../XXXX/","XX../XXX./X.X./X.XX/","XXXX/..XX/XXX./X.../","XX.X/.X.X/.XXX/..XX/","X.../XXX./..XX/XXXX/","X.XX/X.X./XXX./XX../","XXXX/XX../.XXX/...X/","..XX/.XXX/.X.X/XX.X/"},
				{".XXX/XX.X/XXXX/","XX./XXX/X.X/XXX/","XXXX/X.XX/XXX./","XXX/X.X/XXX/.XX/","XXX./X.XX/XXXX/","XXX/X.X/XXX/XX./","XXXX/XX.X/.XXX/",".XX/XXX/X.X/XXX/"},
				{".XX./.XX./XX../XXXX/","XX../XXXX/X.XX/X.../","XXXX/..XX/.XX./.XX./","...X/XX.X/XXXX/..XX/",".XX./.XX./..XX/XXXX/","X.../X.XX/XXXX/XX../","XXXX/XX../.XX./.XX./","..XX/XXXX/XX.X/...X/"},
				{"..XX/.XX./XX../XXXX/","XX../XXX./X.XX/X..X/","XXXX/..XX/.XX./XX../","X..X/XX.X/.XXX/..XX/","XX../.XX./..XX/XXXX/","X..X/X.XX/XXX./XX../","XXXX/XX../.XX./..XX/","..XX/.XXX/XX.X/X..X/"},
				{"..X./..X./.XX./XX../XXXX/","XX.../XXX../X.XXX/X..../","XXXX/..XX/.XX./.X../.X../","....X/XXX.X/..XXX/...XX/",".X../.X../.XX./..XX/XXXX/","X..../X.XXX/XXX../XX.../","XXXX/XX../.XX./..X./..X./","...XX/..XXX/XXX.X/....X/"},
				{".XX./XXXX/XXXX/","XX./XXX/XXX/XX./","XXXX/XXXX/.XX./",".XX/XXX/XXX/.XX/"},
				{"XXX../..X../.XX../.XXXX/","...X/XX.X/XXXX/X.../X.../","XXXX./..XX./..X../..XXX/","...X/...X/XXXX/X.XX/X.../","..XXX/..X../..XX./XXXX./","X.../X.../XXXX/XX.X/...X/",".XXXX/.XX../..X../XXX../","X.../X.XX/XXXX/...X/...X/"},
				{"X.../XX../.X../XX../XXXX/","XX.XX/XXXX./X..../X..../","XXXX/..XX/..X./..XX/...X/","....X/....X/.XXXX/XX.XX/","...X/..XX/..X./..XX/XXXX/","X..../X..../XXXX./XX.XX/","XXXX/XX../.X../XX../X.../","XX.XX/.XXXX/....X/....X/"},
				{".XXX/.X../XX../XXXX/","XX../XXXX/X..X/X..X/","XXXX/..XX/..X./XXX./","X..X/X..X/XXXX/..XX/","XXX./..X./..XX/XXXX/","X..X/X..X/XXXX/XX../","XXXX/XX../.X../.XXX/","..XX/XXXX/X..X/X..X/"},
				{"..X./.XX./.X../XX../XXXX/","XX.../XXXX./X..XX/X..../","XXXX/..XX/..X./.XX./.X../","....X/XX..X/.XXXX/...XX/",".X../.XX./..X./..XX/XXXX/","X..../X..XX/XXXX./XX.../","XXXX/XX../.X../.XX./..X./","...XX/.XXXX/XX..X/....X/"},
				{"XX../.X../.X../XX../XXXX/","XX..X/XXXXX/X..../X..../","XXXX/..XX/..X./..X./..XX/","....X/....X/XXXXX/X..XX/","..XX/..X./..X./..XX/XXXX/","X..../X..../XXXXX/XX..X/","XXXX/XX../.X../.X../XX../","X..XX/XXXXX/....X/....X/"},
				{".XX./.X../.X../XX../XXXX/","XX.../XXXXX/X...X/X..../","XXXX/..XX/..X./..X./.XX./","....X/X...X/XXXXX/...XX/",".XX./..X./..X./..XX/XXXX/","X..../X...X/XXXXX/XX.../","XXXX/XX../.X../.X../.XX./","...XX/XXXXX/X...X/....X/"},
				{"X....../XXXX.../...X.../...XXXX/","..XX/..X./..X./XXX./X.../X.../X.../","XXXX.../...X.../...XXXX/......X/","...X/...X/...X/.XXX/.X../.X../XX../","......X/...XXXX/...X.../XXXX.../","X.../X.../X.../XXX./..X./..X./..XX/","...XXXX/...X.../XXXX.../X....../","XX../.X../.X../.XXX/...X/...X/...X/"},
				{"XXXX.../X..X.../...XXXX/",".XX/..X/..X/XXX/X../X../X../","XXXX.../...X..X/...XXXX/","..X/..X/..X/XXX/X../X../XX./","...XXXX/...X..X/XXXX.../","X../X../X../XXX/..X/..X/.XX/","...XXXX/X..X.../XXXX.../","XX./X../X../XXX/..X/..X/..X/"},
				{"XX...../.XXX.../...X.../...XXXX/","...X/..XX/..X./XXX./X.../X.../X.../","XXXX.../...X.../...XXX./.....XX/","...X/...X/...X/.XXX/.X../XX../X.../",".....XX/...XXX./...X.../XXXX.../","X.../X.../X.../XXX./..X./..XX/...X/","...XXXX/...X.../.XXX.../XX...../","X.../XX../.X../.XXX/...X/...X/...X/"},
				{"XX..../XXX.../..X.../..XXXX/","..XX/..XX/XXX./X.../X.../X.../","XXXX../...X../...XXX/....XX/","...X/...X/...X/.XXX/XX../XX../","....XX/...XXX/...X../XXXX../","X.../X.../X.../XXX./..XX/..XX/","..XXXX/..X.../XXX.../XX..../","XX../XX../.XXX/...X/...X/...X/"},
				{"X...../X...../XXX.../..X.../..XXXX/","..XXX/..X../XXX../X..../X..../X..../","XXXX../...X../...XXX/.....X/.....X/","....X/....X/....X/..XXX/..X../XXX../",".....X/.....X/...XXX/...X../XXXX../","X..../X..../X..../XXX../..X../..XXX/","..XXXX/..X.../XXX.../X...../X...../","XXX../..X../..XXX/....X/....X/....X/"},
				{".XXX.../XX.X.../...XXXX/",".X./.XX/..X/XXX/X../X../X../","XXXX.../...X.XX/...XXX./","..X/..X/..X/XXX/X../XX./.X./","...XXX./...X.XX/XXXX.../","X../X../X../XXX/..X/.XX/.X./","...XXXX/XX.X.../.XXX.../",".X./XX./X../XXX/..X/..X/..X/"},
				{"XXX.../X.X.../X.XXXX/","XXX/..X/XXX/X../X../X../","XXXX.X/...X.X/...XXX/","..X/..X/..X/XXX/X../XXX/","...XXX/...X.X/XXXX.X/","X../X../X../XXX/..X/XXX/","X.XXXX/X.X.../XXX.../","XXX/X../XXX/..X/..X/..X/"},
				{"XXX..../..XX.../...X.../...XXXX/","...X/...X/..XX/XXX./X.../X.../X.../","XXXX.../...X.../...XX../....XXX/","...X/...X/...X/.XXX/XX../X.../X.../","....XXX/...XX../...X.../XXXX.../","X.../X.../X.../XXX./..XX/...X/...X/","...XXXX/...X.../..XX.../XXX..../","X.../X.../XX../.XXX/...X/...X/...X/"},
				{"X...../XX..../.XX.../..X.../..XXXX/","...XX/..XX./XXX../X..../X..../X..../","XXXX../...X../...XX./....XX/.....X/","....X/....X/....X/..XXX/.XX../XX.../",".....X/....XX/...XX./...X../XXXX../","X..../X..../X..../XXX../..XX./...XX/","..XXXX/..X.../.XX.../XX..../X...../","XX.../.XX../..XXX/....X/....X/....X/"},
				{"XXX../XX.../.X.../.XXXX/","..XX/XXXX/X..X/X.../X.../","XXXX./...X./...XX/..XXX/","...X/...X/X..X/XXXX/XX../","..XXX/...XX/...X./XXXX./","X.../X.../X..X/XXXX/..XX/",".XXXX/.X.../XX.../XXX../","XX../XXXX/X..X/...X/...X/"},
				{".X.../XX.../XX.../.X.../.XXXX/","..XX./XXXXX/X..../X..../X..../","XXXX./...X./...XX/...XX/...X./","....X/....X/....X/XXXXX/.XX../","...X./...XX/...XX/...X./XXXX./","X..../X..../X..../XXXXX/..XX./",".XXXX/.X.../XX.../XX.../.X.../",".XX../XXXXX/....X/....X/....X/"},
				{"XX..../.X..../.XX.../..X.../..XXXX/","....X/..XXX/XXX../X..../X..../X..../","XXXX../...X../...XX./....X./....XX/","....X/....X/....X/..XXX/XXX../X..../","....XX/....X./...XX./...X../XXXX../","X..../X..../X..../XXX../..XXX/....X/","..XXXX/..X.../.XX.../.X..../XX..../","X..../XXX../..XXX/....X/....X/....X/"},
				{"XX.../X..../XX.../.X.../.XXXX/","..XXX/XXX.X/X..../X..../X..../","XXXX./...X./...XX/....X/...XX/","....X/....X/....X/X.XXX/XXX../","...XX/....X/...XX/...X./XXXX./","X..../X..../X..../XXX.X/..XXX/",".XXXX/.X.../XX.../X..../XX.../","XXX../X.XXX/....X/....X/....X/"},
				{"..XX.../XXXX.../...XXXX/",".X./.X./.XX/XXX/X../X../X../","XXXX.../...XXXX/...XX../","..X/..X/..X/XXX/XX./.X./.X./","...XX../...XXXX/XXXX.../","X../X../X../XXX/.XX/.X./.X./","...XXXX/XXXX.../..XX.../",".X./.X./XX./XXX/..X/..X/..X/"},
				{".XX.../XXX.../X.XXXX/","XX./.XX/XXX/X../X../X../","XXXX.X/...XXX/...XX./","..X/..X/..X/XXX/XX./.XX/","...XX./...XXX/XXXX.X/","X../X../X../XXX/.XX/XX./","X.XXXX/XXX.../.XX.../",".XX/XX./XXX/..X/..X/..X/"},
				{".XX.../.XX.../XXXXXX/","X../XXX/XXX/X../X../X../","XXXXXX/...XX./...XX./","..X/..X/..X/XXX/XXX/..X/","...XX./...XX./XXXXXX/","X../X../X../XXX/XXX/X../","XXXXXX/.XX.../.XX.../","..X/XXX/XXX/..X/..X/..X/"},
				{"...X/XXXX/X.../XXXX/","XXX./X.X./X.X./X.XX/","XXXX/...X/XXXX/X.../","XX.X/.X.X/.X.X/.XXX/","X.../XXXX/...X/XXXX/","X.XX/X.X./X.X./XXX./","XXXX/X.../XXXX/...X/",".XXX/.X.X/.X.X/XX.X/"},
				{"XXXX/X..X/XXXX/","XXX/X.X/X.X/XXX/"},
				{".XX./XXX./X.../XXXX/","XXX./X.XX/X.XX/X.../","XXXX/...X/.XXX/.XX./","...X/XX.X/XX.X/.XXX/",".XX./.XXX/...X/XXXX/","X.../X.XX/X.XX/XXX./","XXXX/X.../XXX./.XX./",".XXX/XX.X/XX.X/...X/"},
				{"..XX/XXX./X.../XXXX/","XXX./X.X./X.XX/X..X/","XXXX/...X/.XXX/XX../","X..X/XX.X/.X.X/.XXX/","XX../.XXX/...X/XXXX/","X..X/X.XX/X.X./XXX./","XXXX/X.../XXX./..XX/",".XXX/.X.X/XX.X/X..X/"},
				{"..X./..X./XXX./X.../XXXX/","XXX../X.X../X.XXX/X..../","XXXX/...X/.XXX/.X../.X../","....X/XXX.X/..X.X/..XXX/",".X../.X../.XXX/...X/XXXX/","X..../X.XXX/X.X../XXX../","XXXX/X.../XXX./..X./..X./","..XXX/..X.X/XXX.X/....X/"},
				{"XXX../.XX../.X.../.XXXX/","...X/XXXX/X.XX/X.../X.../","XXXX./...X./..XX./..XXX/","...X/...X/XX.X/XXXX/X.../","..XXX/..XX./...X./XXXX./","X.../X.../X.XX/XXXX/...X/",".XXXX/.X.../.XX../XXX../","X.../XXXX/XX.X/...X/...X/"},
				{"X.../XX../XX../X.../XXXX/","XXXXX/X.XX./X..../X..../","XXXX/...X/..XX/..XX/...X/","....X/....X/.XX.X/XXXXX/","...X/..XX/..XX/...X/XXXX/","X..../X..../X.XX./XXXXX/","XXXX/X.../XX../XX../X.../","XXXXX/.XX.X/....X/....X/"},
				{".XXX/XX../X.../XXXX/","XXX./X.XX/X..X/X..X/","XXXX/...X/..XX/XXX./","X..X/X..X/XX.X/.XXX/","XXX./..XX/...X/XXXX/","X..X/X..X/X.XX/XXX./","XXXX/X.../XX../.XXX/",".XXX/XX.X/X..X/X..X/"},
				{"..X./.XX./XX../X.../XXXX/","XXX../X.XX./X..XX/X..../","XXXX/...X/..XX/.XX./.X../","....X/XX..X/.XX.X/..XXX/",".X../.XX./..XX/...X/XXXX/","X..../X..XX/X.XX./XXX../","XXXX/X.../XX../.XX./..X./","..XXX/.XX.X/XX..X/....X/"},
				{"XX../.X../XX../X.../XXXX/","XXX.X/X.XXX/X..../X..../","XXXX/...X/..XX/..X./..XX/","....X/....X/XXX.X/X.XXX/","..XX/..X./..XX/...X/XXXX/","X..../X..../X.XXX/XXX.X/","XXXX/X.../XX../.X../XX../","X.XXX/XXX.X/....X/....X/"},
				{".XX./.X../XX../X.../XXXX/","XXX../X.XXX/X...X/X..../","XXXX/...X/..XX/..X./.XX./","....X/X...X/XXX.X/..XXX/",".XX./..X./..XX/...X/XXXX/","X..../X...X/X.XXX/XXX../","XXXX/X.../XX../.X../.XX./","..XXX/XXX.X/X...X/....X/"},
				{"XXXX.../...X.../...X.../...XXXX/","...X/...X/...X/XXXX/X.../X.../X.../","...XXXX/...X.../...X.../XXXX.../","X.../X.../X.../XXXX/...X/...X/...X/"},
				{"X...../XXX.../..X.../..X.../..XXXX/","...XX/...X./XXXX./X..../X..../X..../","XXXX../...X../...X../...XXX/.....X/","....X/....X/....X/.XXXX/.X.../XX.../",".....X/...XXX/...X../...X../XXXX../","X..../X..../X..../XXXX./...X./...XX/","..XXXX/..X.../..X.../XXX.../X...../","XX.../.X.../.XXXX/....X/....X/....X/"},
				{"XXX.../X.X.../..X.../..XXXX/","..XX/...X/XXXX/X.../X.../X.../","XXXX../...X../...X.X/...XXX/","...X/...X/...X/XXXX/X.../XX../","...XXX/...X.X/...X../XXXX../","X.../X.../X.../XXXX/...X/..XX/","..XXXX/..X.../X.X.../XXX.../","XX../X.../XXXX/...X/...X/...X/"},
				{"XX..../.XX.../..X.../..X.../..XXXX/","....X/...XX/XXXX./X..../X..../X..../","XXXX../...X../...X../...XX./....XX/","....X/....X/....X/.XXXX/XX.../X..../","....XX/...XX./...X../...X../XXXX../","X..../X..../X..../XXXX./...XX/....X/","..XXXX/..X.../..X.../.XX.../XX..../","X..../XX.../.XXXX/....X/....X/....X/"},
				{"XX.../XX.../.X.../.X.../.XXXX/","...XX/XXXXX/X..../X..../X..../","XXXX./...X./...X./...XX/...XX/","....X/....X/....X/XXXXX/XX.../","...XX/...XX/...X./...X./XXXX./","X..../X..../X..../XXXXX/...XX/",".XXXX/.X.../.X.../XX.../XX.../","XX.../XXXXX/....X/....X/....X/"},
				{"X..../X..../XX.../.X.../.X.../.XXXX/","...XXX/XXXX../X...../X...../X...../","XXXX./...X./...X./...XX/....X/....X/",".....X/.....X/.....X/..XXXX/XXX.../","....X/....X/...XX/...X./...X./XXXX./","X...../X...../X...../XXXX../...XXX/",".XXXX/.X.../.X.../XX.../X..../X..../","XXX.../..XXXX/.....X/.....X/.....X/"},
				{".XX.../XXX.../..X.../..XXXX/","..X./..XX/XXXX/X.../X.../X.../","XXXX../...X../...XXX/...XX./","...X/...X/...X/XXXX/XX../.X../","...XX./...XXX/...X../XXXX../","X.../X.../X.../XXXX/..XX/..X./","..XXXX/..X.../XXX.../.XX.../",".X../XX../XXXX/...X/...X/...X/"},
				{"XXXX/X.../X.../XXXX/","XXXX/X..X/X..X/X..X/","XXXX/...X/...X/XXXX/","X..X/X..X/X..X/XXXX/"},
				{"..X./XXX./X.../X.../XXXX/","XXXX./X..X./X..XX/X..../","XXXX/...X/...X/.XXX/.X../","....X/XX..X/.X..X/.XXXX/",".X../.XXX/...X/...X/XXXX/","X..../X..XX/X..X./XXXX./","XXXX/X.../X.../XXX./..X./",".XXXX/.X..X/XX..X/....X/"},
				{"XXX./X.X./X.../XXXX/","XXXX/X..X/X.XX/X.../","XXXX/...X/.X.X/.XXX/","...X/XX.X/X..X/XXXX/",".XXX/.X.X/...X/XXXX/","X.../X.XX/X..X/XXXX/","XXXX/X.../X.X./XXX./","XXXX/X..X/XX.X/...X/"},
				{"XX../XX../X.../X.../XXXX/","XXXXX/X..XX/X..../X..../","XXXX/...X/...X/..XX/..XX/","....X/....X/XX..X/XXXXX/","..XX/..XX/...X/...X/XXXX/","X..../X..../X..XX/XXXXX/","XXXX/X.../X.../XX../XX../","XXXXX/XX..X/....X/....X/"},
				{".XX./XX../X.../X.../XXXX/","XXXX./X..XX/X...X/X..../","XXXX/...X/...X/..XX/.XX./","....X/X...X/XX..X/.XXXX/",".XX./..XX/...X/...X/XXXX/","X..../X...X/X..XX/XXXX./","XXXX/X.../X.../XX../.XX./",".XXXX/XX..X/X...X/....X/"},
				{".X../.X../XX../X.../X.../XXXX/","XXXX../X..XXX/X...../X...../","XXXX/...X/...X/..XX/..X./..X./",".....X/.....X/XXX..X/..XXXX/","..X./..X./..XX/...X/...X/XXXX/","X...../X...../X..XXX/XXXX../","XXXX/X.../X.../XX../.X../.X../","..XXXX/XXX..X/.....X/.....X/"},
				{"XX../XXX./X.../XXXX/","XXXX/X.XX/X.X./X.../","XXXX/...X/.XXX/..XX/","...X/.X.X/XX.X/XXXX/","..XX/.XXX/...X/XXXX/","X.../X.X./X.XX/XXXX/","XXXX/X.../XXX./XX../","XXXX/XX.X/.X.X/...X/"},
				{"XXX.../..X.../..X.../..X.../..XXXX/","....X/....X/XXXXX/X..../X..../X..../","XXXX../...X../...X../...X../...XXX/","....X/....X/....X/XXXXX/X..../X..../","...XXX/...X../...X../...X../XXXX../","X..../X..../X..../XXXXX/....X/....X/","..XXXX/..X.../..X.../..X.../XXX.../","X..../X..../XXXXX/....X/....X/....X/"},
				{"X..../XX.../.X.../.X.../.X.../.XXXX/","....XX/XXXXX./X...../X...../X...../","XXXX./...X./...X./...X./...XX/....X/",".....X/.....X/.....X/.XXXXX/XX..../","....X/...XX/...X./...X./...X./XXXX./","X...../X...../X...../XXXXX./....XX/",".XXXX/.X.../.X.../.X.../XX.../X..../","XX..../.XXXXX/.....X/.....X/.....X/"},
				{"XXX./X.../X.../X.../XXXX/","XXXXX/X...X/X...X/X..../","XXXX/...X/...X/...X/.XXX/","....X/X...X/X...X/XXXXX/",".XXX/...X/...X/...X/XXXX/","X..../X...X/X...X/XXXXX/","XXXX/X.../X.../X.../XXX./","XXXXX/X...X/X...X/....X/"},
				{".X../XX../X.../X.../X.../XXXX/","XXXXX./X...XX/X...../X...../","XXXX/...X/...X/...X/..XX/..X./",".....X/.....X/XX...X/.XXXXX/","..X./..XX/...X/...X/...X/XXXX/","X...../X...../X...XX/XXXXX./","XXXX/X.../X.../X.../XX../.X../",".XXXXX/XX...X/.....X/.....X/"},
				{"XX.../.X.../.X.../.X.../.X.../.XXXX/",".....X/XXXXXX/X...../X...../X...../","XXXX./...X./...X./...X./...X./...XX/",".....X/.....X/.....X/XXXXXX/X...../","...XX/...X./...X./...X./...X./XXXX./","X...../X...../X...../XXXXXX/.....X/",".XXXX/.X.../.X.../.X.../.X.../XX.../","X...../XXXXXX/.....X/.....X/.....X/"},
				{"XX../X.../X.../X.../X.../XXXX/","XXXXXX/X....X/X...../X...../","XXXX/...X/...X/...X/...X/..XX/",".....X/.....X/X....X/XXXXXX/","..XX/...X/...X/...X/...X/XXXX/","X...../X...../X....X/XXXXXX/","XXXX/X.../X.../X.../X.../XX../","XXXXXX/X....X/.....X/.....X/"},
				{"X......./XXXXXX../.....XXX/",".XX/.X./.X./.X./.X./XX./X../X../","XXX...../..XXXXXX/.......X/","..X/..X/.XX/.X./.X./.X./.X./XX./",".......X/..XXXXXX/XXX...../","X../X../XX./.X./.X./.X./.X./.XX/",".....XXX/XXXXXX../X......./","XX./.X./.X./.X./.X./.XX/..X/..X/"},
				{"XXXXXX../X....XXX/","XX/.X/.X/.X/.X/XX/X./X./","XXX....X/..XXXXXX/",".X/.X/XX/X./X./X./X./XX/","..XXXXXX/XXX....X/","X./X./XX/.X/.X/.X/.X/XX/","X....XXX/XXXXXX../","XX/X./X./X./X./XX/.X/.X/"},
				{"XX....../.XXXXX../.....XXX/","..X/.XX/.X./.X./.X./XX./X../X../","XXX...../..XXXXX./......XX/","..X/..X/.XX/.X./.X./.X./XX./X../","......XX/..XXXXX./XXX...../","X../X../XX./.X./.X./.X./.XX/..X/",".....XXX/.XXXXX../XX....../","X../XX./.X./.X./.X./.XX/..X/..X/"},
				{"XX...../XXXXX../....XXX/",".XX/.XX/.X./.X./XX./X../X../","XXX..../..XXXXX/.....XX/","..X/..X/.XX/.X./.X./XX./XX./",".....XX/..XXXXX/XXX..../","X../X../XX./.X./.X./.XX/.XX/","....XXX/XXXXX../XX...../","XX./XX./.X./.X./.XX/..X/..X/"},
				{"X....../X....../XXXXX../....XXX/",".XXX/.X../.X../.X../XX../X.../X.../","XXX..../..XXXXX/......X/......X/","...X/...X/..XX/..X./..X./..X./XXX./","......X/......X/..XXXXX/XXX..../","X.../X.../XX../.X../.X../.X../.XXX/","....XXX/XXXXX../X....../X....../","XXX./..X./..X./..X./..XX/...X/...X/"},
				{".XXXXX../XX...XXX/","X./XX/.X/.X/.X/XX/X./X./","XXX...XX/..XXXXX./",".X/.X/XX/X./X./X./XX/.X/","..XXXXX./XXX...XX/","X./X./XX/.X/.X/.X/XX/X./","XX...XXX/.XXXXX../",".X/XX/X./X./X./XX/.X/.X/"},
				{"XXXXX../XX..XXX/","XX/XX/.X/.X/XX/X./X./","XXX..XX/..XXXXX/",".X/.X/XX/X./X./XX/XX/","..XXXXX/XXX..XX/","X./X./XX/.X/.X/XX/XX/","XX..XXX/XXXXX../","XX/XX/X./X./XX/.X/.X/"},
				{"XXXXX../X...XXX/X....../","XXX/..X/..X/..X/.XX/.X./.X./","......X/XXX...X/..XXXXX/",".X./.X./XX./X../X../X../XXX/","..XXXXX/XXX...X/......X/",".X./.X./.XX/..X/..X/..X/XXX/","X....../X...XXX/XXXXX../","XXX/X../X../X../XX./.X./.X./"},
				{"XXX...../..XXXX../.....XXX/","..X/..X/.XX/.X./.X./XX./X../X../",".....XXX/..XXXX../XXX...../","X../X../XX./.X./.X./.XX/..X/..X/"},
				{"X....../XX...../.XXXX../....XXX/","..XX/.XX./.X../.X../XX../X.../X.../","XXX..../..XXXX./.....XX/......X/","...X/...X/..XX/..X./..X./.XX./XX../","......X/.....XX/..XXXX./XXX..../","X.../X.../XX../.X../.X../.XX./..XX/","....XXX/.XXXX../XX...../X....../","XX../.XX./..X./..X./..XX/...X/...X/"},
				{"XXX.../XXXX../...XXX/",".XX/.XX/.XX/XX./X../X../","XXX.../..XXXX/...XXX/","..X/..X/.XX/XX./XX./XX./","...XXX/..XXXX/XXX.../","X../X../XX./.XX/.XX/.XX/","...XXX/XXXX../XXX.../","XX./XX./XX./.XX/..X/..X/"},
				{".X..../XX..../XXXX../...XXX/",".XX./.XXX/.X../XX../X.../X.../","XXX.../..XXXX/....XX/....X./","...X/...X/..XX/..X./XXX./.XX./","....X./....XX/..XXXX/XXX.../","X.../X.../XX../.X../.XXX/.XX./","...XXX/XXXX../XX..../.X..../",".XX./XXX./..X./..XX/...X/...X/"},
				{"XX...../.X...../.XXXX../....XXX/","...X/.XXX/.X../.X../XX../X.../X.../","XXX..../..XXXX./.....X./.....XX/","...X/...X/..XX/..X./..X./XXX./X.../",".....XX/.....X./..XXXX./XXX..../","X.../X.../XX../.X../.X../.XXX/...X/","....XXX/.XXXX../.X...../XX...../","X.../XXX./..X./..X./..XX/...X/...X/"},
				{"XX..../X...../XXXX../...XXX/",".XXX/.X.X/.X../XX../X.../X.../","XXX.../..XXXX/.....X/....XX/","...X/...X/..XX/..X./X.X./XXX./","....XX/.....X/..XXXX/XXX.../","X.../X.../XX../.X../.X.X/.XXX/","...XXX/XXXX../X...../XX..../","XXX./X.X./..X./..XX/...X/...X/"},
				{"..XXXX../XXX..XXX/","X./X./XX/.X/.X/XX/X./X./","XXX..XXX/..XXXX../",".X/.X/XX/X./X./XX/.X/.X/"},
				{".XXXX../XX..XXX/X....../","XX./.XX/..X/..X/.XX/.X./.X./","......X/XXX..XX/..XXXX./",".X./.X./XX./X../X../XX./.XX/","..XXXX./XXX..XX/......X/",".X./.X./.XX/..X/..X/.XX/XX./","X....../XX..XXX/.XXXX../",".XX/XX./X../X../XX./.X./.X./"},
				{"XXXX../XX.XXX/.X..../",".XX/XXX/..X/.XX/.X./.X./","....X./XXX.XX/..XXXX/",".X./.X./XX./X../XXX/XX./","..XXXX/XXX.XX/....X./",".X./.X./.XX/..X/XXX/.XX/",".X..../XX.XXX/XXXX../","XX./XXX/X../XX./.X./.X./"},
				{".XXXX../.X..XXX/XX...../","X../XXX/..X/..X/.XX/.X./.X./",".....XX/XXX..X./..XXXX./",".X./.X./XX./X../X../XXX/..X/","..XXXX./XXX..X./.....XX/",".X./.X./.XX/..X/..X/XXX/X../","XX...../.X..XXX/.XXXX../","..X/XXX/X../X../XX./.X./.X./"},
				{"XXXX../X..XXX/XX..../","XXX/X.X/..X/.XX/.X./.X./","....XX/XXX..X/..XXXX/",".X./.X./XX./X../X.X/XXX/","..XXXX/XXX..X/....XX/",".X./.X./.XX/..X/X.X/XXX/","XX..../X..XXX/XXXX../","XXX/X.X/X../XX./.X./.X./"},
				{"X....../XXX..../..XXX../....XXX/","..XX/..X./.XX./.X../XX../X.../X.../","XXX..../..XXX../....XXX/......X/","...X/...X/..XX/..X./.XX./.X../XX../","......X/....XXX/..XXX../XXX..../","X.../X.../XX../.X../.XX./..X./..XX/","....XXX/..XXX../XXX..../X....../","XX../.X../.XX./..X./..XX/...X/...X/"},
				{"XXX..../X.XXX../....XXX/",".XX/..X/.XX/.X./XX./X../X../","XXX..../..XXX.X/....XXX/","..X/..X/.XX/.X./XX./X../XX./","....XXX/..XXX.X/XXX..../","X../X../XX./.X./.XX/..X/.XX/","....XXX/X.XXX../XXX..../","XX./X../XX./.X./.XX/..X/..X/"},
				{"XX...../.XX..../..XXX../....XXX/","...X/..XX/.XX./.X../XX../X.../X.../","XXX..../..XXX../....XX./.....XX/","...X/...X/..XX/..X./.XX./XX../X.../",".....XX/....XX./..XXX../XXX..../","X.../X.../XX../.X../.XX./..XX/...X/","....XXX/..XXX../.XX..../XX...../","X.../XX../.XX./..X./..XX/...X/...X/"},
				{"XX..../XX..../.XXX../...XXX/","..XX/.XXX/.X../XX../X.../X.../","XXX.../..XXX./....XX/....XX/","...X/...X/..XX/..X./XXX./XX../","....XX/....XX/..XXX./XXX.../","X.../X.../XX../.X../.XXX/..XX/","...XXX/.XXX../XX..../XX..../","XX../XXX./..X./..XX/...X/...X/"},
				{"X...../X...../XX..../.XXX../...XXX/","..XXX/.XX../.X.../XX.../X..../X..../","XXX.../..XXX./....XX/.....X/.....X/","....X/....X/...XX/...X./..XX./XXX../",".....X/.....X/....XX/..XXX./XXX.../","X..../X..../XX.../.X.../.XX../..XXX/","...XXX/.XXX../XX..../X...../X...../","XXX../..XX./...X./...XX/....X/....X/"},
				{".XX..../XXXXX../....XXX/",".X./.XX/.XX/.X./XX./X../X../","XXX..../..XXXXX/....XX./","..X/..X/.XX/.X./XX./XX./.X./","....XX./..XXXXX/XXX..../","X../X../XX./.X./.XX/.XX/.X./","....XXX/XXXXX../.XX..../",".X./XX./XX./.X./.XX/..X/..X/"},
				{"XX..../XXXX../X..XXX/","XXX/.XX/.X./XX./X../X../","XXX..X/..XXXX/....XX/","..X/..X/.XX/.X./XX./XXX/","....XX/..XXXX/XXX..X/","X../X../XX./.X./.XX/XXX/","X..XXX/XXXX../XX..../","XXX/XX./.X./.XX/..X/..X/"},
				{"..X../XXX../XXX../..XXX/",".XX./.XX./XXXX/X.../X.../","XXX../..XXX/..XXX/..X../","...X/...X/XXXX/.XX./.XX./","..X../..XXX/..XXX/XXX../","X.../X.../XXXX/.XX./.XX./","..XXX/XXX../XXX../..X../",".XX./.XX./XXXX/...X/...X/"},
				{"XX.../XX.../XXX../..XXX/",".XXX/.XXX/XX../X.../X.../","XXX../..XXX/...XX/...XX/","...X/...X/..XX/XXX./XXX./","...XX/...XX/..XXX/XXX../","X.../X.../XX../.XXX/.XXX/","..XXX/XXX../XX.../XX.../","XXX./XXX./..XX/...X/...X/"},
				{".XX../XX.../XXX../..XXX/",".XX./.XXX/XX.X/X.../X.../","XXX../..XXX/...XX/..XX./","...X/...X/X.XX/XXX./.XX./","..XX./...XX/..XXX/XXX../","X.../X.../XX.X/.XXX/.XX./","..XXX/XXX../XX.../.XX../",".XX./XXX./X.XX/...X/...X/"},
				{".X.../.X.../XX.../XXX../..XXX/",".XX../.XXXX/XX.../X..../X..../","XXX../..XXX/...XX/...X./...X./","....X/....X/...XX/XXXX./..XX./","...X./...X./...XX/..XXX/XXX../","X..../X..../XX.../.XXXX/.XX../","..XXX/XXX../XX.../.X.../.X.../","..XX./XXXX./...XX/....X/....X/"},
				{"XXX..../..X..../..XXX../....XXX/","...X/...X/.XXX/.X../XX../X.../X.../","XXX..../..XXX../....X../....XXX/","...X/...X/..XX/..X./XXX./X.../X.../","....XXX/....X../..XXX../XXX..../","X.../X.../XX../.X../.XXX/...X/...X/","....XXX/..XXX../..X..../XXX..../","X.../X.../XXX./..X./..XX/...X/...X/"},
				{"X...../XX..../.X..../.XXX../...XXX/","...XX/.XXX./.X.../XX.../X..../X..../","XXX.../..XXX./....X./....XX/.....X/","....X/....X/...XX/...X./.XXX./XX.../",".....X/....XX/....X./..XXX./XXX.../","X..../X..../XX.../.X.../.XXX./...XX/","...XXX/.XXX../.X..../XX..../X...../","XX.../.XXX./...X./...XX/....X/....X/"},
				{"XXX../X..../XXX../..XXX/",".XXX/.X.X/XX.X/X.../X.../","XXX../..XXX/....X/..XXX/","...X/...X/X.XX/X.X./XXX./","..XXX/....X/..XXX/XXX../","X.../X.../XX.X/.X.X/.XXX/","..XXX/XXX../X..../XXX../","XXX./X.X./X.XX/...X/...X/"},
				{".X.../XX.../X..../XXX../..XXX/",".XXX./.X.XX/XX.../X..../X..../","XXX../..XXX/....X/...XX/...X./","....X/....X/...XX/XX.X./.XXX./","...X./...XX/....X/..XXX/XXX../","X..../X..../XX.../.X.XX/.XXX./","..XXX/XXX../X..../XX.../.X.../",".XXX./XX.X./...XX/....X/....X/"},
				{"XX..../.X..../.X..../.XXX../...XXX/","....X/.XXXX/.X.../XX.../X..../X..../","XXX.../..XXX./....X./....X./....XX/","....X/....X/...XX/...X./XXXX./X..../","....XX/....X./....X./..XXX./XXX.../","X..../X..../XX.../.X.../.XXXX/....X/","...XXX/.XXX../.X..../.X..../XX..../","X..../XXXX./...X./...XX/....X/....X/"},
				{"XX.../X..../X..../XXX../..XXX/",".XXXX/.X..X/XX.../X..../X..../","XXX../..XXX/....X/....X/...XX/","....X/....X/...XX/X..X./XXXX./","...XX/....X/....X/..XXX/XXX../","X..../X..../XX.../.X..X/.XXXX/","..XXX/XXX../X..../X..../XX.../","XXXX./X..X./...XX/....X/....X/"},
				{"X.XXX../XXX.XXX/","XX/X./XX/.X/XX/X./X./","XXX.XXX/..XXX.X/",".X/.X/XX/X./XX/.X/XX/","..XXX.X/XXX.XXX/","X./X./XX/.X/XX/X./XX/","XXX.XXX/X.XXX../","XX/.X/XX/X./XX/.X/.X/"},
				{"..XXX../XXX.XXX/X....../","XX./.X./.XX/..X/.XX/.X./.X./","......X/XXX.XXX/..XXX../",".X./.X./XX./X../XX./.X./.XX/","..XXX../XXX.XXX/......X/",".X./.X./.XX/..X/.XX/.X./XX./","X....../XXX.XXX/..XXX../",".XX/.X./XX./X../XX./.X./.X./"},
				{"XXXXX../.XX.XXX/",".X/XX/XX/.X/XX/X./X./","XXX.XX./..XXXXX/",".X/.X/XX/X./XX/XX/X./","..XXXXX/XXX.XX./","X./X./XX/.X/XX/XX/.X/",".XX.XXX/XXXXX../","X./XX/XX/X./XX/.X/.X/"},
				{"X...../XXXX../XX.XXX/","XXX/XX./.X./XX./X../X../","XXX.XX/..XXXX/.....X/","..X/..X/.XX/.X./.XX/XXX/",".....X/..XXXX/XXX.XX/","X../X../XX./.X./XX./XXX/","XX.XXX/XXXX../X...../","XXX/.XX/.X./.XX/..X/..X/"},
				{"..XXX../.XX.XXX/XX...../","X../XX./.XX/..X/.XX/.X./.X./",".....XX/XXX.XX./..XXX../",".X./.X./XX./X../XX./.XX/..X/","..XXX../XXX.XX./.....XX/",".X./.X./.XX/..X/.XX/XX./X../","XX...../.XX.XXX/..XXX../","..X/.XX/XX./X../XX./.X./.X./"},
				{".XXX../XX.XXX/XX..../","XX./XXX/..X/.XX/.X./.X./","....XX/XXX.XX/..XXX./",".X./.X./XX./X../XXX/.XX/","..XXX./XXX.XX/....XX/",".X./.X./.XX/..X/XXX/XX./","XX..../XX.XXX/.XXX../",".XX/XXX/X../XX./.X./.X./"},
				{".XXX../XX.XXX/X...../X...../","XXX./..XX/...X/..XX/..X./..X./",".....X/.....X/XXX.XX/..XXX./",".X../.X../XX../X.../XX../.XXX/","..XXX./XXX.XX/.....X/.....X/","..X./..X./..XX/...X/..XX/XXX./","X...../X...../XX.XXX/.XXX../",".XXX/XX../X.../XX../.X../.X../"},
				{"XXX../XXXXX/.XX../",".XX/XXX/XXX/.X./.X./","..XX./XXXXX/..XXX/",".X./.X./XXX/XXX/XX./","..XXX/XXXXX/..XX./",".X./.X./XXX/XXX/.XX/",".XX../XXXXX/XXX../","XX./XXX/XXX/.X./.X./"},
				{"XXX../XXXXX/.X.../.X.../","..XX/XXXX/..XX/..X./..X./","...X./...X./XXXXX/..XXX/",".X../.X../XX../XXXX/XX../","..XXX/XXXXX/...X./...X./","..X./..X./..XX/XXXX/..XX/",".X.../.X.../XXXXX/XXX../","XX../XXXX/XX../.X../.X../"},
				{"..XXX../..X.XXX/XXX..../","X../X../XXX/..X/.XX/.X./.X./","....XXX/XXX.X../..XXX../",".X./.X./XX./X../XXX/..X/..X/","..XXX../XXX.X../....XXX/",".X./.X./.XX/..X/XXX/X../X../","XXX..../..X.XXX/..XXX../","..X/..X/XXX/X../XX./.X./.X./"},
				{".XXX../.X.XXX/XX..../X...../","XX../.XXX/...X/..XX/..X./..X./",".....X/....XX/XXX.X./..XXX./",".X../.X../XX../X.../XXX./..XX/","..XXX./XXX.X./....XX/.....X/","..X./..X./..XX/...X/.XXX/XX../","X...../XX..../.X.XXX/.XXX../","..XX/XXX./X.../XX../.X../.X../"},
				{"XXX../X.XXX/XXX../","XXX/X.X/XXX/.X./.X./","..XXX/XXX.X/..XXX/",".X./.X./XXX/X.X/XXX/"},
				{"XXX../X.XXX/XX.../.X.../",".XXX/XX.X/..XX/..X./..X./","...X./...XX/XXX.X/..XXX/",".X../.X../XX../X.XX/XXX./","..XXX/XXX.X/...XX/...X./","..X./..X./..XX/XX.X/.XXX/",".X.../XX.../X.XXX/XXX../","XXX./X.XX/XX../.X../.X../"},
				{".XXX../.X.XXX/.X..../XX..../","X.../XXXX/...X/..XX/..X./..X./","....XX/....X./XXX.X./..XXX./",".X../.X../XX../X.../XXXX/...X/","..XXX./XXX.X./....X./....XX/","..X./..X./..XX/...X/XXXX/X.../","XX..../.X..../.X.XXX/.XXX../","...X/XXXX/X.../XX../.X../.X../"},
				{"XXX../X.XXX/X..../XX.../","XXXX/X..X/..XX/..X./..X./","...XX/....X/XXX.X/..XXX/",".X../.X../XX../X..X/XXXX/","..XXX/XXX.X/....X/...XX/","..X./..X./..XX/X..X/XXXX/","XX.../X..../X.XXX/XXX../","XXXX/X..X/XX../.X../.X../"},
				{"X....../XXXX.../...XX../....XXX/","..XX/..X./..X./.XX./XX../X.../X.../","XXX..../..XX.../...XXXX/......X/","...X/...X/..XX/.XX./.X../.X../XX../","......X/...XXXX/..XX.../XXX..../","X.../X.../XX../.XX./..X./..X./..XX/","....XXX/...XX../XXXX.../X....../","XX../.X../.X../.XX./..XX/...X/...X/"},
				{"XXXX.../X..XX../....XXX/",".XX/..X/..X/.XX/XX./X../X../","XXX..../..XX..X/...XXXX/","..X/..X/.XX/XX./X../X../XX./","...XXXX/..XX..X/XXX..../","X../X../XX./.XX/..X/..X/.XX/","....XXX/X..XX../XXXX.../","XX./X../X../XX./.XX/..X/..X/"},
				{"XX...../.XXX.../...XX../....XXX/","...X/..XX/..X./.XX./XX../X.../X.../","XXX..../..XX.../...XXX./.....XX/","...X/...X/..XX/.XX./.X../XX../X.../",".....XX/...XXX./..XX.../XXX..../","X.../X.../XX../.XX./..X./..XX/...X/","....XXX/...XX../.XXX.../XX...../","X.../XX../.X../.XX./..XX/...X/...X/"},
				{"XX..../XXX.../..XX../...XXX/","..XX/..XX/.XX./XX../X.../X.../","XXX.../..XX../...XXX/....XX/","...X/...X/..XX/.XX./XX../XX../","....XX/...XXX/..XX../XXX.../","X.../X.../XX../.XX./..XX/..XX/","...XXX/..XX../XXX.../XX..../","XX../XX../.XX./..XX/...X/...X/"},
				{"X...../X...../XXX.../..XX../...XXX/","..XXX/..X../.XX../XX.../X..../X..../","XXX.../..XX../...XXX/.....X/.....X/","....X/....X/...XX/..XX./..X../XXX../",".....X/.....X/...XXX/..XX../XXX.../","X..../X..../XX.../.XX../..X../..XXX/","...XXX/..XX../XXX.../X...../X...../","XXX../..X../..XX./...XX/....X/....X/"},
				{".XXX.../XX.XX../....XXX/",".X./.XX/..X/.XX/XX./X../X../","XXX..../..XX.XX/...XXX./","..X/..X/.XX/XX./X../XX./.X./","...XXX./..XX.XX/XXX..../","X../X../XX./.XX/..X/.XX/.X./","....XXX/XX.XX../.XXX.../",".X./XX./X../XX./.XX/..X/..X/"},
				{"XXX.../X.XX../X..XXX/","XXX/..X/.XX/XX./X../X../","XXX..X/..XX.X/...XXX/","..X/..X/.XX/XX./X../XXX/","...XXX/..XX.X/XXX..X/","X../X../XX./.XX/..X/XXX/","X..XXX/X.XX../XXX.../","XXX/X../XX./.XX/..X/..X/"},
				{"XXX..../..XX.../...XX../....XXX/","...X/...X/..XX/.XX./XX../X.../X.../","....XXX/...XX../..XX.../XXX..../","X.../X.../XX../.XX./..XX/...X/...X/"},
				{"X...../XX..../.XX.../..XX../...XXX/","...XX/..XX./.XX../XX.../X..../X..../","XXX.../..XX../...XX./....XX/.....X/","....X/....X/...XX/..XX./.XX../XX.../",".....X/....XX/...XX./..XX../XXX.../","X..../X..../XX.../.XX../..XX./...XX/","...XXX/..XX../.XX.../XX..../X...../","XX.../.XX../..XX./...XX/....X/....X/"},
				{"XXX../XX.../.XX../..XXX/","..XX/.XXX/XX.X/X.../X.../","XXX../..XX./...XX/..XXX/","...X/...X/X.XX/XXX./XX../","..XXX/...XX/..XX./XXX../","X.../X.../XX.X/.XXX/..XX/","..XXX/.XX../XX.../XXX../","XX../XXX./X.XX/...X/...X/"},
				{".X.../XX.../XX.../.XX../..XXX/","..XX./.XXXX/XX.../X..../X..../","XXX../..XX./...XX/...XX/...X./","....X/....X/...XX/XXXX./.XX../","...X./...XX/...XX/..XX./XXX../","X..../X..../XX.../.XXXX/..XX./","..XXX/.XX../XX.../XX.../.X.../",".XX../XXXX./...XX/....X/....X/"},
				{"XX..../.X..../.XX.../..XX../...XXX/","....X/..XXX/.XX../XX.../X..../X..../","XXX.../..XX../...XX./....X./....XX/","....X/....X/...XX/..XX./XXX../X..../","....XX/....X./...XX./..XX../XXX.../","X..../X..../XX.../.XX../..XXX/....X/","...XXX/..XX../.XX.../.X..../XX..../","X..../XXX../..XX./...XX/....X/....X/"},
				{"XX.../X..../XX.../.XX../..XXX/","..XXX/.XX.X/XX.../X..../X..../","XXX../..XX./...XX/....X/...XX/","....X/....X/...XX/X.XX./XXX../","...XX/....X/...XX/..XX./XXX../","X..../X..../XX.../.XX.X/..XXX/","..XXX/.XX../XX.../X..../XX.../","XXX../X.XX./...XX/....X/....X/"},
				{"..XX.../XXXXX../....XXX/",".X./.X./.XX/.XX/XX./X../X../","XXX..../..XXXXX/...XX../","..X/..X/.XX/XX./XX./.X./.X./","...XX../..XXXXX/XXX..../","X../X../XX./.XX/.XX/.X./.X./","....XXX/XXXXX../..XX.../",".X./.X./XX./XX./.XX/..X/..X/"},
				{".XX.../XXXX../X..XXX/","XX./.XX/.XX/XX./X../X../","XXX..X/..XXXX/...XX./","..X/..X/.XX/XX./XX./.XX/","...XX./..XXXX/XXX..X/","X../X../XX./.XX/.XX/XX./","X..XXX/XXXX../.XX.../",".XX/XX./XX./.XX/..X/..X/"},
				{".XX.../.XXX../XX.XXX/","X../XXX/.XX/XX./X../X../","XXX.XX/..XXX./...XX./","..X/..X/.XX/XX./XXX/..X/","...XX./..XXX./XXX.XX/","X../X../XX./.XX/XXX/X../","XX.XXX/.XXX../.XX.../","..X/XXX/XX./.XX/..X/..X/"},
				{"...X/XXXX/XX../.XXX/",".XX./XXX./X.X./X.XX/","XXX./..XX/XXXX/X.../","XX.X/.X.X/.XXX/.XX./","X.../XXXX/..XX/XXX./","X.XX/X.X./XXX./.XX./",".XXX/XX../XXXX/...X/",".XX./.XXX/.X.X/XX.X/"},
				{".XX./XXX./XX../.XXX/",".XX./XXXX/X.XX/X.../","XXX./..XX/.XXX/.XX./","...X/XX.X/XXXX/.XX./",".XX./.XXX/..XX/XXX./","X.../X.XX/XXXX/.XX./",".XXX/XX../XXX./.XX./",".XX./XXXX/XX.X/...X/"},
				{"..XX/XXX./XX../.XXX/",".XX./XXX./X.XX/X..X/","XXX./..XX/.XXX/XX../","X..X/XX.X/.XXX/.XX./","XX../.XXX/..XX/XXX./","X..X/X.XX/XXX./.XX./",".XXX/XX../XXX./..XX/",".XX./.XXX/XX.X/X..X/"},
				{"..X./..X./XXX./XX../.XXX/",".XX../XXX../X.XXX/X..../","XXX./..XX/.XXX/.X../.X../","....X/XXX.X/..XXX/..XX./",".X../.X../.XXX/..XX/XXX./","X..../X.XXX/XXX../.XX../",".XXX/XX../XXX./..X./..X./","..XX./..XXX/XXX.X/....X/"},
				{"XXX./XXXX/.XXX/",".XX/XXX/XXX/XX./",".XXX/XXXX/XXX./","XX./XXX/XXX/.XX/"},
				{"XXX../.XX../.XX../..XXX/","...X/.XXX/XXXX/X.../X.../","XXX../..XX./..XX./..XXX/","...X/...X/XXXX/XXX./X.../","..XXX/..XX./..XX./XXX../","X.../X.../XXXX/.XXX/...X/","..XXX/.XX../.XX../XXX../","X.../XXX./XXXX/...X/...X/"},
				{"X.../XX../XX../XX../.XXX/",".XXXX/XXXX./X..../X..../","XXX./..XX/..XX/..XX/...X/","....X/....X/.XXXX/XXXX./","...X/..XX/..XX/..XX/XXX./","X..../X..../XXXX./.XXXX/",".XXX/XX../XX../XX../X.../","XXXX./.XXXX/....X/....X/"},
				{".XXX/XX../XX../.XXX/",".XX./XXXX/X..X/X..X/","XXX./..XX/..XX/XXX./","X..X/X..X/XXXX/.XX./"},
				{"..X./.XX./XX../XX../.XXX/",".XX../XXXX./X..XX/X..../","XXX./..XX/..XX/.XX./.X../","....X/XX..X/.XXXX/..XX./",".X../.XX./..XX/..XX/XXX./","X..../X..XX/XXXX./.XX../",".XXX/XX../XX../.XX./..X./","..XX./.XXXX/XX..X/....X/"},
				{"XX../.X../XX../XX../.XXX/",".XX.X/XXXXX/X..../X..../","XXX./..XX/..XX/..X./..XX/","....X/....X/XXXXX/X.XX./","..XX/..X./..XX/..XX/XXX./","X..../X..../XXXXX/.XX.X/",".XXX/XX../XX../.X../XX../","X.XX./XXXXX/....X/....X/"},
				{".XX./.X../XX../XX../.XXX/",".XX../XXXXX/X...X/X..../","XXX./..XX/..XX/..X./.XX./","....X/X...X/XXXXX/..XX./",".XX./..X./..XX/..XX/XXX./","X..../X...X/XXXXX/.XX../",".XXX/XX../XX../.X../.XX./","..XX./XXXXX/X...X/....X/"},
				{"X...../XXX.../..X.../..XX../...XXX/","...XX/...X./.XXX./XX.../X..../X..../","XXX.../..XX../...X../...XXX/.....X/","....X/....X/...XX/.XXX./.X.../XX.../",".....X/...XXX/...X../..XX../XXX.../","X..../X..../XX.../.XXX./...X./...XX/","...XXX/..XX../..X.../XXX.../X...../","XX.../.X.../.XXX./...XX/....X/....X/"},
				{"XXX.../X.X.../..XX../...XXX/","..XX/...X/.XXX/XX../X.../X.../","XXX.../..XX../...X.X/...XXX/","...X/...X/..XX/XXX./X.../XX../","...XXX/...X.X/..XX../XXX.../","X.../X.../XX../.XXX/...X/..XX/","...XXX/..XX../X.X.../XXX.../","XX../X.../XXX./..XX/...X/...X/"},
				{"XX..../.XX.../..X.../..XX../...XXX/","....X/...XX/.XXX./XX.../X..../X..../","XXX.../..XX../...X../...XX./....XX/","....X/....X/...XX/.XXX./XX.../X..../","....XX/...XX./...X../..XX../XXX.../","X..../X..../XX.../.XXX./...XX/....X/","...XXX/..XX../..X.../.XX.../XX..../","X..../XX.../.XXX./...XX/....X/....X/"},
				{"XX.../XX.../.X.../.XX../..XXX/","...XX/.XXXX/XX.../X..../X..../","XXX../..XX./...X./...XX/...XX/","....X/....X/...XX/XXXX./XX.../","...XX/...XX/...X./..XX./XXX../","X..../X..../XX.../.XXXX/...XX/","..XXX/.XX../.X.../XX.../XX.../","XX.../XXXX./...XX/....X/....X/"},
				{".XX.../XXX.../..XX../...XXX/","..X./..XX/.XXX/XX../X.../X.../","XXX.../..XX../...XXX/...XX./","...X/...X/..XX/XXX./XX../.X../","...XX./...XXX/..XX../XXX.../","X.../X.../XX../.XXX/..XX/..X./","...XXX/..XX../XXX.../.XX.../",".X../XX../XXX./..XX/...X/...X/"},
				{"..X./XXX./X.../XX../.XXX/",".XXX./XX.X./X..XX/X..../","XXX./..XX/...X/.XXX/.X../","....X/XX..X/.X.XX/.XXX./",".X../.XXX/...X/..XX/XXX./","X..../X..XX/XX.X./.XXX./",".XXX/XX../X.../XXX./..X./",".XXX./.X.XX/XX..X/....X/"},
				{"XXX./X.X./XX../.XXX/",".XXX/XX.X/X.XX/X.../","XXX./..XX/.X.X/.XXX/","...X/XX.X/X.XX/XXX./",".XXX/.X.X/..XX/XXX./","X.../X.XX/XX.X/.XXX/",".XXX/XX../X.X./XXX./","XXX./X.XX/XX.X/...X/"},
				{"XX../XX../X.../XX../.XXX/",".XXXX/XX.XX/X..../X..../","XXX./..XX/...X/..XX/..XX/","....X/....X/XX.XX/XXXX./","..XX/..XX/...X/..XX/XXX./","X..../X..../XX.XX/.XXXX/",".XXX/XX../X.../XX../XX../","XXXX./XX.XX/....X/....X/"},
				{".XX./XX../X.../XX../.XXX/",".XXX./XX.XX/X...X/X..../","XXX./..XX/...X/..XX/.XX./","....X/X...X/XX.XX/.XXX./",".XX./..XX/...X/..XX/XXX./","X..../X...X/XX.XX/.XXX./",".XXX/XX../X.../XX../.XX./",".XXX./XX.XX/X...X/....X/"},
				{"XX../XXX./XX../.XXX/",".XXX/XXXX/X.X./X.../","XXX./..XX/.XXX/..XX/","...X/.X.X/XXXX/XXX./","..XX/.XXX/..XX/XXX./","X.../X.X./XXXX/.XXX/",".XXX/XX../XXX./XX../","XXX./XXXX/.X.X/...X/"},
				{"XXX.../..X.../..X.../..XX../...XXX/","....X/....X/.XXXX/XX.../X..../X..../","XXX.../..XX../...X../...X../...XXX/","....X/....X/...XX/XXXX./X..../X..../","...XXX/...X../...X../..XX../XXX.../","X..../X..../XX.../.XXXX/....X/....X/","...XXX/..XX../..X.../..X.../XXX.../","X..../X..../XXXX./...XX/....X/....X/"},
				{"X..../XX.../.X.../.X.../.XX../..XXX/","....XX/.XXXX./XX..../X...../X...../","XXX../..XX./...X./...X./...XX/....X/",".....X/.....X/....XX/.XXXX./XX..../","....X/...XX/...X./...X./..XX./XXX../","X...../X...../XX..../.XXXX./....XX/","..XXX/.XX../.X.../.X.../XX.../X..../","XX..../.XXXX./....XX/.....X/.....X/"},
				{"XXX./X.../X.../XX../.XXX/",".XXXX/XX..X/X...X/X..../","XXX./..XX/...X/...X/.XXX/","....X/X...X/X..XX/XXXX./",".XXX/...X/...X/..XX/XXX./","X..../X...X/XX..X/.XXXX/",".XXX/XX../X.../X.../XXX./","XXXX./X..XX/X...X/....X/"},
				{".X../XX../X.../X.../XX../.XXX/",".XXXX./XX..XX/X...../X...../","XXX./..XX/...X/...X/..XX/..X./",".....X/.....X/XX..XX/.XXXX./","..X./..XX/...X/...X/..XX/XXX./","X...../X...../XX..XX/.XXXX./",".XXX/XX../X.../X.../XX../.X../",".XXXX./XX..XX/.....X/.....X/"},
				{"XX.../.X.../.X.../.X.../.XX../..XXX/",".....X/.XXXXX/XX..../X...../X...../","XXX../..XX./...X./...X./...X./...XX/",".....X/.....X/....XX/XXXXX./X...../","...XX/...X./...X./...X./..XX./XXX../","X...../X...../XX..../.XXXXX/.....X/","..XXX/.XX../.X.../.X.../.X.../XX.../","X...../XXXXX./....XX/.....X/.....X/"},
				{"XX../X.../X.../X.../XX../.XXX/",".XXXXX/XX...X/X...../X...../","XXX./..XX/...X/...X/...X/..XX/",".....X/.....X/X...XX/XXXXX./","..XX/...X/...X/...X/..XX/XXX./","X...../X...../XX...X/.XXXXX/",".XXX/XX../X.../X.../X.../XX../","XXXXX./X...XX/.....X/.....X/"},
				{"X..XX../XXXXXXX/","XX/X./X./XX/XX/X./X./","XXXXXXX/..XX..X/",".X/.X/XX/XX/.X/.X/XX/","..XX..X/XXXXXXX/","X./X./XX/XX/X./X./XX/","XXXXXXX/X..XX../","XX/.X/.X/XX/XX/.X/.X/"},
				{"...XX../XXXXXXX/X....../","XX./.X./.X./.XX/.XX/.X./.X./","......X/XXXXXXX/..XX.../",".X./.X./XX./XX./.X./.X./.XX/","..XX.../XXXXXXX/......X/",".X./.X./.XX/.XX/.X./.X./XX./","X....../XXXXXXX/...XX../",".XX/.X./.X./XX./XX./.X./.X./"},
				{"XX.XX../.XXXXXX/",".X/XX/X./XX/XX/X./X./","XXXXXX./..XX.XX/",".X/.X/XX/XX/.X/XX/X./","..XX.XX/XXXXXX./","X./X./XX/XX/X./XX/.X/",".XXXXXX/XX.XX../","X./XX/.X/XX/XX/.X/.X/"},
				{"X...../X.XX../XXXXXX/","XXX/X../XX./XX./X../X../","XXXXXX/..XX.X/.....X/","..X/..X/.XX/.XX/..X/XXX/",".....X/..XX.X/XXXXXX/","X../X../XX./XX./X../XXX/","XXXXXX/X.XX../X...../","XXX/..X/.XX/.XX/..X/..X/"},
				{"...XX../.XXXXXX/XX...../","X../XX./.X./.XX/.XX/.X./.X./",".....XX/XXXXXX./..XX.../",".X./.X./XX./XX./.X./.XX/..X/","..XX.../XXXXXX./.....XX/",".X./.X./.XX/.XX/.X./XX./X../","XX...../.XXXXXX/...XX../","..X/.XX/.X./XX./XX./.X./.X./"},
				{"..XX../XXXXXX/XX..../","XX./XX./.XX/.XX/.X./.X./","....XX/XXXXXX/..XX../",".X./.X./XX./XX./.XX/.XX/","..XX../XXXXXX/....XX/",".X./.X./.XX/.XX/XX./XX./","XX..../XXXXXX/..XX../",".XX/.XX/XX./XX./.X./.X./"},
				{"..XX../XXXXXX/X...../X...../","XXX./..X./..XX/..XX/..X./..X./",".....X/.....X/XXXXXX/..XX../",".X../.X../XX../XX../.X../.XXX/","..XX../XXXXXX/.....X/.....X/","..X./..X./..XX/..XX/..X./XXX./","X...../X...../XXXXXX/..XX../",".XXX/.X../XX../XX../.X../.X../"},
				{"XXXXX../..XXXXX/",".X/.X/XX/XX/XX/X./X./","..XXXXX/XXXXX../","X./X./XX/XX/XX/.X/.X/"},
				{"X...../XXXX../.XXXXX/",".XX/XX./XX./XX./X../X../","XXXXX./..XXXX/.....X/","..X/..X/.XX/.XX/.XX/XX./",".....X/..XXXX/XXXXX./","X../X../XX./XX./XX./.XX/",".XXXXX/XXXX../X...../","XX./.XX/.XX/.XX/..X/..X/"},
				{"XX..../.XXX../.XXXXX/","..X/XXX/XX./XX./X../X../","XXXXX./..XXX./....XX/","..X/..X/.XX/.XX/XXX/X../","....XX/..XXX./XXXXX./","X../X../XX./XX./XXX/..X/",".XXXXX/.XXX../XX..../","X../XXX/.XX/.XX/..X/..X/"},
				{"..XX../.XXXXX/XX..../X...../","XX../.XX./..XX/..XX/..X./..X./",".....X/....XX/XXXXX./..XX../",".X../.X../XX../XX../.XX./..XX/","..XX../XXXXX./....XX/.....X/","..X./..X./..XX/..XX/.XX./XX../","X...../XX..../.XXXXX/..XX../","..XX/.XX./XX../XX../.X../.X../"},
				{".XX../XXXXX/XX.../.X.../",".XX./XXXX/..XX/..X./..X./","...X./...XX/XXXXX/..XX./",".X../.X../XX../XXXX/.XX./","..XX./XXXXX/...XX/...X./","..X./..X./..XX/XXXX/.XX./",".X.../XX.../XXXXX/.XX../",".XX./XXXX/XX../.X../.X../"},
				{"..XX../.XXXXX/.X..../XX..../","X.../XXX./..XX/..XX/..X./..X./","....XX/....X./XXXXX./..XX../",".X../.X../XX../XX../.XXX/...X/","..XX../XXXXX./....X./....XX/","..X./..X./..XX/..XX/XXX./X.../","XX..../.X..../.XXXXX/..XX../","...X/.XXX/XX../XX../.X../.X../"},
				{".XX../XXXXX/X..../XX.../","XXX./X.XX/..XX/..X./..X./","...XX/....X/XXXXX/..XX./",".X../.X../XX../XX.X/.XXX/","..XX./XXXXX/....X/...XX/","..X./..X./..XX/X.XX/XXX./","XX.../X..../XXXXX/.XX../",".XXX/XX.X/XX../.X../.X../"},
				{"..XX../X.XXXX/XXX.../","XX./X../XXX/.XX/.X./.X./","...XXX/XXXX.X/..XX../",".X./.X./XX./XXX/..X/.XX/","..XX../XXXX.X/...XXX/",".X./.X./.XX/XXX/X../XX./","XXX.../X.XXXX/..XX../",".XX/..X/XXX/XX./.X./.X./"},
				{"..XX../..XXXX/XXX.../X...../","XX../.X../.XXX/..XX/..X./..X./",".....X/...XXX/XXXX../..XX../",".X../.X../XX../XXX./..X./..XX/","..XX../XXXX../...XXX/.....X/","..X./..X./..XX/.XXX/.X../XX../","X...../XXX.../..XXXX/..XX../","..XX/..X./XXX./XX../.X../.X../"},
				{"..XX../XXXXXX/.XX.../",".X./XX./XXX/.XX/.X./.X./","...XX./XXXXXX/..XX../",".X./.X./XX./XXX/.XX/.X./","..XX../XXXXXX/...XX./",".X./.X./.XX/XXX/XX./.X./",".XX.../XXXXXX/..XX../",".X./.XX/XXX/XX./.X./.X./"},
				{"..XX../..XXXX/.XX.../XX..../","X.../XX../.XXX/..XX/..X./..X./","....XX/...XX./XXXX../..XX../",".X../.X../XX../XXX./..XX/...X/","..XX../XXXX../...XX./....XX/","..X./..X./..XX/.XXX/XX../X.../","XX..../.XX.../..XXXX/..XX../","...X/..XX/XXX./XX../.X../.X../"},
				{".XX../.XXXX/XX.../XX.../","XX../XXXX/..XX/..X./..X./","...XX/...XX/XXXX./..XX./",".X../.X../XX../XXXX/..XX/","..XX./XXXX./...XX/...XX/","..X./..X./..XX/XXXX/XX../","XX.../XX.../.XXXX/.XX../","..XX/XXXX/XX../.X../.X../"},
				{"XX../XXXX/XXX./..X./",".XXX/.XXX/XXX./..X./",".X../.XXX/XXXX/..XX/",".X../.XXX/XXX./XXX./","..XX/XXXX/.XXX/.X../","..X./XXX./.XXX/.XXX/","..X./XXX./XXXX/XX../","XXX./XXX./.XXX/.X../"},
				{"XX../XXXX/XX../XX../","XXXX/XXXX/..X./..X./","..XX/..XX/XXXX/..XX/",".X../.X../XXXX/XXXX/","..XX/XXXX/..XX/..XX/","..X./..X./XXXX/XXXX/","XX../XX../XXXX/XX../","XXXX/XXXX/.X../.X../"},
				{"XX../XXXX/XX../.XX./",".XXX/XXXX/X.X./..X./",".XX./..XX/XXXX/..XX/",".X../.X.X/XXXX/XXX./","..XX/XXXX/..XX/.XX./","..X./X.X./XXXX/.XXX/",".XX./XX../XXXX/XX../","XXX./XXXX/.X.X/.X../"},
				{"..XX../..XXXX/..X.../XXX.../","X.../X.../XXXX/..XX/..X./..X./","...XXX/...X../XXXX../..XX../",".X../.X../XX../XXXX/...X/...X/","..XX../XXXX../...X../...XXX/","..X./..X./..XX/XXXX/X.../X.../","XXX.../..X.../..XXXX/..XX../","...X/...X/XXXX/XX../.X../.X../"},
				{".XX../.XXXX/.X.../XX.../X..../","XX.../.XXXX/...XX/...X./...X./","....X/...XX/...X./XXXX./..XX./",".X.../.X.../XX.../XXXX./...XX/","..XX./XXXX./...X./...XX/....X/","...X./...X./...XX/.XXXX/XX.../","X..../XX.../.X.../.XXXX/.XX../","...XX/XXXX./XX.../.X.../.X.../"},
				{"XX../XXXX/X.../XXX./","XXXX/X.XX/X.X./..X./",".XXX/...X/XXXX/..XX/",".X../.X.X/XX.X/XXXX/","..XX/XXXX/...X/.XXX/","..X./X.X./X.XX/XXXX/","XXX./X.../XXXX/XX../","XXXX/XX.X/.X.X/.X../"},
				{"XX../XXXX/X.../XX../.X../",".XXXX/XX.XX/...X./...X./","..X./..XX/...X/XXXX/..XX/",".X.../.X.../XX.XX/XXXX./","..XX/XXXX/...X/..XX/..X./","...X./...X./XX.XX/.XXXX/",".X../XX../X.../XXXX/XX../","XXXX./XX.XX/.X.../.X.../"},
				{".XX../.XXXX/.X.../.X.../XX.../","X..../XXXXX/...XX/...X./...X./","...XX/...X./...X./XXXX./..XX./",".X.../.X.../XX.../XXXXX/....X/","..XX./XXXX./...X./...X./...XX/","...X./...X./...XX/XXXXX/X..../","XX.../.X.../.X.../.XXXX/.XX../","....X/XXXXX/XX.../.X.../.X.../"},
				{"XX../XXXX/X.../X.../XX../","XXXXX/X..XX/...X./...X./","..XX/...X/...X/XXXX/..XX/",".X.../.X.../XX..X/XXXXX/","..XX/XXXX/...X/...X/..XX/","...X./...X./X..XX/XXXXX/","XX../X.../X.../XXXX/XX../","XXXXX/XX..X/.X.../.X.../"},
				{".....X/XXXXXX/XXX.../","XX./XX./XX./.X./.X./.XX/","...XXX/XXXXXX/X...../","XX./.X./.X./.XX/.XX/.XX/","X...../XXXXXX/...XXX/",".XX/.X./.X./XX./XX./XX./","XXX.../XXXXXX/.....X/",".XX/.XX/.XX/.X./.X./XX./"},
				{"XXXXXX/XXX..X/","XX/XX/XX/.X/.X/XX/","X..XXX/XXXXXX/","XX/X./X./XX/XX/XX/","XXXXXX/X..XXX/","XX/.X/.X/XX/XX/XX/","XXX..X/XXXXXX/","XX/XX/XX/X./X./XX/"},
				{"...XX/XXXXX/XXX../","XX./XX./XX./.XX/.XX/","..XXX/XXXXX/XX.../","XX./XX./.XX/.XX/.XX/","XX.../XXXXX/..XXX/",".XX/.XX/XX./XX./XX./","XXX../XXXXX/...XX/",".XX/.XX/.XX/XX./XX./"},
				{"....XX/XXXXX./XXX.../","XX./XX./XX./.X./.XX/..X/","...XXX/.XXXXX/XX..../","X../XX./.X./.XX/.XX/.XX/","XX..../.XXXXX/...XXX/","..X/.XX/.X./XX./XX./XX./","XXX.../XXXXX./....XX/",".XX/.XX/.XX/.X./XX./X../"},
				{"....X/....X/XXXXX/XXX../","XX../XX../XX../.X../.XXX/","..XXX/XXXXX/X..../X..../","XXX./..X./..XX/..XX/..XX/","X..../X..../XXXXX/..XXX/",".XXX/.X../XX../XX../XX../","XXX../XXXXX/....X/....X/","..XX/..XX/..XX/..X./XXX./"},
				{"XXXXX./XXX.XX/","XX/XX/XX/.X/XX/X./","XX.XXX/.XXXXX/",".X/XX/X./XX/XX/XX/",".XXXXX/XX.XXX/","X./XX/.X/XX/XX/XX/","XXX.XX/XXXXX./","XX/XX/XX/X./XX/.X/"},
				{"XXXXX/XXX.X/....X/",".XX/.XX/.XX/..X/XXX/","X..../X.XXX/XXXXX/","XXX/X../XX./XX./XX./","XXXXX/X.XXX/X..../","XXX/..X/.XX/.XX/.XX/","....X/XXX.X/XXXXX/","XX./XX./XX./X../XXX/"},
				{"..X./..XX/XXXX/XXX./","XX../XX../XXXX/.XX./",".XXX/XXXX/XX../.X../",".XX./XXXX/..XX/..XX/",".X../XX../XXXX/.XXX/",".XX./XXXX/XX../XX../","XXX./XXXX/..XX/..X./","..XX/..XX/XXXX/.XX./"},
				{"....X/...XX/XXXX./XXX../","XX../XX../XX../.XX./..XX/","..XXX/.XXXX/XX.../X..../","XX../.XX./..XX/..XX/..XX/","X..../XX.../.XXXX/..XXX/","..XX/.XX./XX../XX../XX../","XXX../XXXX./...XX/....X/","..XX/..XX/..XX/.XX./XX../"},
				{"..XX/...X/XXXX/XXX./","XX../XX../XX.X/.XXX/",".XXX/XXXX/X.../XX../","XXX./X.XX/..XX/..XX/","XX../X.../XXXX/.XXX/",".XXX/XX.X/XX../XX../","XXX./XXXX/...X/..XX/","..XX/..XX/X.XX/XXX./"},
				{"...XX/...X./XXXX./XXX../","XX../XX../XX../.XXX/...X/","..XXX/.XXXX/.X.../XX.../","X.../XXX./..XX/..XX/..XX/","XX.../.X.../.XXXX/..XXX/","...X/.XXX/XX../XX../XX../","XXX../XXXX./...X./...XX/","..XX/..XX/..XX/XXX./X.../"},
				{"XX./.XX/XXX/XXX/","XX.X/XXXX/XXX./","XXX/XXX/XX./.XX/",".XXX/XXXX/X.XX/",".XX/XX./XXX/XXX/","XXX./XXXX/XX.X/","XXX/XXX/.XX/XX./","X.XX/XXXX/.XXX/"},
				{"....X/..XXX/XXX../XXX../","XX../XX../XXX./..X./..XX/","..XXX/..XXX/XXX../X..../","XX../.X../.XXX/..XX/..XX/","X..../XXX../..XXX/..XXX/","..XX/..X./XXX./XX../XX../","XXX../XXX../..XXX/....X/","..XX/..XX/.XXX/.X../XX../"},
				{"..XXX/XXX.X/XXX../","XX./XX./XXX/..X/.XX/","..XXX/X.XXX/XXX../","XX./X../XXX/.XX/.XX/","XXX../X.XXX/..XXX/",".XX/..X/XXX/XX./XX./","XXX../XXX.X/..XXX/",".XX/.XX/XXX/X../XX./"},
				{"..XX/..XX/XXX./XXX./","XX../XX../XXXX/..XX/",".XXX/.XXX/XX../XX../","XX../XXXX/..XX/..XX/","XX../XX../.XXX/.XXX/","..XX/XXXX/XX../XX../","XXX./XXX./..XX/..XX/","..XX/..XX/XXXX/XX../"},
				{"...XX/..XX./XXX../XXX../","XX../XX../XXX./..XX/...X/","..XXX/..XXX/.XX../XX.../","X.../XX../.XXX/..XX/..XX/","XX.../.XX../..XXX/..XXX/","...X/..XX/XXX./XX../XX../","XXX../XXX../..XX./...XX/","..XX/..XX/.XXX/XX../X.../"},
				{"..XX./XXXXX/XXX../","XX./XX./XXX/.XX/.X./","..XXX/XXXXX/.XX../",".X./XX./XXX/.XX/.XX/",".XX../XXXXX/..XXX/",".X./.XX/XXX/XX./XX./","XXX../XXXXX/..XX./",".XX/.XX/XXX/XX./.X./"},
				{"XXX/..X/XXX/XXX/","XX.X/XX.X/XXXX/","XXX/XXX/X../XXX/","XXXX/X.XX/X.XX/","XXX/X../XXX/XXX/","XXXX/XX.X/XX.X/","XXX/XXX/..X/XXX/","X.XX/X.XX/XXXX/"},
				{".X./.XX/..X/XXX/XXX/","XX.../XX.XX/XXXX./","XXX/XXX/X../XX./.X./",".XXXX/XX.XX/...XX/",".X./XX./X../XXX/XXX/","XXXX./XX.XX/XX.../","XXX/XXX/..X/.XX/.X./","...XX/XX.XX/.XXXX/"},
				{"..XXX/..X../XXX../XXX../","XX../XX../XXXX/...X/...X/","..XXX/..XXX/..X../XXX../","X.../X.../XXXX/..XX/..XX/","XXX../..X../..XXX/..XXX/","...X/...X/XXXX/XX../XX../","XXX../XXX../..X../..XXX/","..XX/..XX/XXXX/X.../X.../"},
				{"...X/..XX/..X./XXX./XXX./","XX.../XX.../XXXX./...XX/",".XXX/.XXX/.X../XX../X.../","XX.../.XXXX/...XX/...XX/","X.../XX../.X../.XXX/.XXX/","...XX/XXXX./XX.../XX.../","XXX./XXX./..X./..XX/...X/","...XX/...XX/.XXXX/XX.../"},
				{".XX/..X/..X/XXX/XXX/","XX.../XX..X/XXXXX/","XXX/XXX/X../X../XX./","XXXXX/X..XX/...XX/","XX./X../X../XXX/XXX/","XXXXX/XX..X/XX.../","XXX/XXX/..X/..X/.XX/","...XX/X..XX/XXXXX/"},
				{"..XX/..X./..X./XXX./XXX./","XX.../XX.../XXXXX/....X/",".XXX/.XXX/.X../.X../XX../","X..../XXXXX/...XX/...XX/","XX../.X../.X../.XXX/.XXX/","....X/XXXXX/XX.../XX.../","XXX./XXX./..X./..X./..XX/","...XX/...XX/XXXXX/X..../"},
				{"X..../XXXX./..XX./..XXX/","..XX/..X./XXX./XXX./X.../","XXX../.XX../.XXXX/....X/","...X/.XXX/.XXX/.X../XX../","....X/.XXXX/.XX../XXX../","X.../XXX./XXX./..X./..XX/","..XXX/..XX./XXXX./X..../","XX../.X../.XXX/.XXX/...X/"},
				{"XXXX./X.XX./..XXX/",".XX/..X/XXX/XXX/X../","XXX../.XX.X/.XXXX/","..X/XXX/XXX/X../XX./",".XXXX/.XX.X/XXX../","X../XXX/XXX/..X/.XX/","..XXX/X.XX./XXXX./","XX./X../XXX/XXX/..X/"},
				{"XX.../.XXX./..XX./..XXX/","...X/..XX/XXX./XXX./X.../","XXX../.XX../.XXX./...XX/","...X/.XXX/.XXX/XX../X.../","...XX/.XXX./.XX../XXX../","X.../XXX./XXX./..XX/...X/","..XXX/..XX./.XXX./XX.../","X.../XX../.XXX/.XXX/...X/"},
				{"XX../XXX./.XX./.XXX/","..XX/XXXX/XXX./X.../","XXX./.XX./.XXX/..XX/","...X/.XXX/XXXX/XX../","..XX/.XXX/.XX./XXX./","X.../XXX./XXXX/..XX/",".XXX/.XX./XXX./XX../","XX../XXXX/.XXX/...X/"},
				{"X.../X.../XXX./.XX./.XXX/","..XXX/XXX../XXX../X..../","XXX./.XX./.XXX/...X/...X/","....X/..XXX/..XXX/XXX../","...X/...X/.XXX/.XX./XXX./","X..../XXX../XXX../..XXX/",".XXX/.XX./XXX./X.../X.../","XXX../..XXX/..XXX/....X/"},
				{".XXX./XXXX./..XXX/",".X./.XX/XXX/XXX/X../","XXX../.XXXX/.XXX./","..X/XXX/XXX/XX./.X./",".XXX./.XXXX/XXX../","X../XXX/XXX/.XX/.X./","..XXX/XXXX./.XXX./",".X./XX./XXX/XXX/..X/"},
				{"X.../XX../.XX./.XX./.XXX/","...XX/XXXX./XXX../X..../","XXX./.XX./.XX./..XX/...X/","....X/..XXX/.XXXX/XX.../","...X/..XX/.XX./.XX./XXX./","X..../XXX../XXXX./...XX/",".XXX/.XX./.XX./XX../X.../","XX.../.XXXX/..XXX/....X/"},
				{"XXX/XX./XX./XXX/","XXXX/XXXX/X..X/","XXX/.XX/.XX/XXX/","X..X/XXXX/XXXX/"},
				{".X./XX./XX./XX./XXX/","XXXX./XXXXX/X..../","XXX/.XX/.XX/.XX/.X./","....X/XXXXX/.XXXX/",".X./.XX/.XX/.XX/XXX/","X..../XXXXX/XXXX./","XXX/XX./XX./XX./.X./",".XXXX/XXXXX/....X/"},
				{"XX../.X../.XX./.XX./.XXX/","....X/XXXXX/XXX../X..../","XXX./.XX./.XX./..X./..XX/","....X/..XXX/XXXXX/X..../","..XX/..X./.XX./.XX./XXX./","X..../XXX../XXXXX/....X/",".XXX/.XX./.XX./.X../XX../","X..../XXXXX/..XXX/....X/"},
				{"XX./X../XX./XX./XXX/","XXXXX/XXX.X/X..../","XXX/.XX/.XX/..X/.XX/","....X/X.XXX/XXXXX/",".XX/..X/.XX/.XX/XXX/","X..../XXX.X/XXXXX/","XXX/XX./XX./X../XX./","XXXXX/X.XXX/....X/"},
				{"....X/.XXXX/XX.../XXX../","XX../XXX./X.X./..X./..XX/","..XXX/...XX/XXXX./X..../","XX../.X../.X.X/.XXX/..XX/","X..../XXXX./...XX/..XXX/","..XX/..X./X.X./XXX./XX../","XXX../XX.../.XXXX/....X/","..XX/.XXX/.X.X/.X../XX../"},
				{".XXXX/XX..X/XXX../","XX./XXX/X.X/..X/.XX/","..XXX/X..XX/XXXX./","XX./X../X.X/XXX/.XX/","XXXX./X..XX/..XXX/",".XX/..X/X.X/XXX/XX./","XXX../XX..X/.XXXX/",".XX/XXX/X.X/X../XX./"},
				{"..XX/.XXX/XX../XXX./","XX../XXX./X.XX/..XX/",".XXX/..XX/XXX./XX../","XX../XX.X/.XXX/..XX/","XX../XXX./..XX/.XXX/","..XX/X.XX/XXX./XX../","XXX./XX../.XXX/..XX/","..XX/.XXX/XX.X/XX../"},
				{"...XX/.XXX./XX.../XXX../","XX../XXX./X.X./..XX/...X/","..XXX/...XX/.XXX./XX.../","X.../XX../.X.X/.XXX/..XX/","XX.../.XXX./...XX/..XXX/","...X/..XX/X.X./XXX./XX../","XXX../XX.../.XXX./...XX/","..XX/.XXX/.X.X/XX../X.../"},
				{"...X/...X/.XXX/XX../XXX./","XX.../XXX../X.X../..XXX/",".XXX/..XX/XXX./X.../X.../","XXX../..X.X/..XXX/...XX/","X.../X.../XXX./..XX/.XXX/","..XXX/X.X../XXX../XX.../","XXX./XX../.XXX/...X/...X/","...XX/..XXX/..X.X/XXX../"},
				{".XXX./XX.XX/XXX../","XX./XXX/X.X/.XX/.X./","..XXX/XX.XX/.XXX./",".X./XX./X.X/XXX/.XX/",".XXX./XX.XX/..XXX/",".X./.XX/X.X/XXX/XX./","XXX../XX.XX/.XXX./",".XX/XXX/X.X/XX./.X./"},
				{"XXX/.XX/XX./XXX/","XX.X/XXXX/X.XX/","XXX/XX./.XX/XXX/","X.XX/XXXX/XX.X/"},
				{".X./.XX/.XX/XX./XXX/","XX.../XXXXX/X.XX./","XXX/.XX/XX./XX./.X./",".XX.X/XXXXX/...XX/",".X./XX./XX./.XX/XXX/","X.XX./XXXXX/XX.../","XXX/XX./.XX/.XX/.X./","...XX/XXXXX/.XX.X/"},
				{"...X/..XX/.XX./XX../XXX./","XX.../XXX../X.XX./...XX/",".XXX/..XX/.XX./XX../X.../","XX.../.XX.X/..XXX/...XX/","X.../XX../.XX./..XX/.XXX/","...XX/X.XX./XXX../XX.../","XXX./XX../.XX./..XX/...X/","...XX/..XXX/.XX.X/XX.../"},
				{".XX/..X/.XX/XX./XXX/","XX.../XXX.X/X.XXX/","XXX/.XX/XX./X../XX./","XXX.X/X.XXX/...XX/","XX./X../XX./.XX/XXX/","X.XXX/XXX.X/XX.../","XXX/XX./.XX/..X/.XX/","...XX/X.XXX/XXX.X/"},
				{"..XX/..X./.XX./XX../XXX./","XX.../XXX../X.XXX/....X/",".XXX/..XX/.XX./.X../XX../","X..../XXX.X/..XXX/...XX/","XX../.X../.XX./..XX/.XXX/","....X/X.XXX/XXX../XX.../","XXX./XX../.XX./..X./..XX/","...XX/..XXX/XXX.X/X..../"},
				{"X.../XXX./..X./.XX./.XXX/","...XX/XX.X./XXXX./X..../","XXX./.XX./.X../.XXX/...X/","....X/.XXXX/.X.XX/XX.../","...X/.XXX/.X../.XX./XXX./","X..../XXXX./XX.X./...XX/",".XXX/.XX./..X./XXX./X.../","XX.../.X.XX/.XXXX/....X/"},
				{"XXX./X.X./.XX./.XXX/","..XX/XX.X/XXXX/X.../","XXX./.XX./.X.X/.XXX/","...X/XXXX/X.XX/XX../",".XXX/.X.X/.XX./XXX./","X.../XXXX/XX.X/..XX/",".XXX/.XX./X.X./XXX./","XX../X.XX/XXXX/...X/"},
				{"XX../.XX./..X./.XX./.XXX/","....X/XX.XX/XXXX./X..../","XXX./.XX./.X../.XX./..XX/","....X/.XXXX/XX.XX/X..../","..XX/.XX./.X../.XX./XXX./","X..../XXXX./XX.XX/....X/",".XXX/.XX./..X./.XX./XX../","X..../XX.XX/.XXXX/....X/"},
				{"XX./XX./.X./XX./XXX/","XX.XX/XXXXX/X..../","XXX/.XX/.X./.XX/.XX/","....X/XXXXX/XX.XX/",".XX/.XX/.X./.XX/XXX/","X..../XXXXX/XX.XX/","XXX/XX./.X./XX./XX./","XX.XX/XXXXX/....X/"},
				{".XX./XXX./.XX./.XXX/","..X./XXXX/XXXX/X.../","XXX./.XX./.XXX/.XX./","...X/XXXX/XXXX/.X../",".XX./.XXX/.XX./XXX./","X.../XXXX/XXXX/..X./",".XXX/.XX./XXX./.XX./",".X../XXXX/XXXX/...X/"},
				{"...X/.XXX/.X../XX../XXX./","XX.../XXXX./X..X./...XX/",".XXX/..XX/..X./XXX./X.../","XX.../.X..X/.XXXX/...XX/","X.../XXX./..X./..XX/.XXX/","...XX/X..X./XXXX./XX.../","XXX./XX../.X../.XXX/...X/","...XX/.XXXX/.X..X/XX.../"},
				{".XXX/.X.X/XX../XXX./","XX../XXXX/X..X/..XX/",".XXX/..XX/X.X./XXX./","XX../X..X/XXXX/..XX/","XXX./X.X./..XX/.XXX/","..XX/X..X/XXXX/XX../","XXX./XX../.X.X/.XXX/","..XX/XXXX/X..X/XX../"},
				{".XX/.XX/.X./XX./XXX/","XX.../XXXXX/X..XX/","XXX/.XX/.X./XX./XX./","XX..X/XXXXX/...XX/","XX./XX./.X./.XX/XXX/","X..XX/XXXXX/XX.../","XXX/XX./.X./.XX/.XX/","...XX/XXXXX/XX..X/"},
				{"..XX/.XX./.X../XX../XXX./","XX.../XXXX./X..XX/....X/",".XXX/..XX/..X./.XX./XX../","X..../XX..X/.XXXX/...XX/","XX../.XX./..X./..XX/.XXX/","....X/X..XX/XXXX./XX.../","XXX./XX../.X../.XX./..XX/","...XX/.XXXX/XX..X/X..../"},
				{".XX./.XXX/XX../XXX./","XX../XXXX/X.XX/..X./",".XXX/..XX/XXX./.XX./",".X../XX.X/XXXX/..XX/",".XX./XXX./..XX/.XXX/","..X./X.XX/XXXX/XX../","XXX./XX../.XXX/.XX./","..XX/XXXX/XX.X/.X../"},
				{"XXX./..X./..X./.XX./.XXX/","....X/XX..X/XXXXX/X..../","XXX./.XX./.X../.X../.XXX/","....X/XXXXX/X..XX/X..../",".XXX/.X../.X../.XX./XXX./","X..../XXXXX/XX..X/....X/",".XXX/.XX./..X./..X./XXX./","X..../X..XX/XXXXX/....X/"},
				{"X../XX./.X./.X./XX./XXX/","XX..XX/XXXXX./X...../","XXX/.XX/.X./.X./.XX/..X/",".....X/.XXXXX/XX..XX/","..X/.XX/.X./.X./.XX/XXX/","X...../XXXXX./XX..XX/","XXX/XX./.X./.X./XX./X../","XX..XX/.XXXXX/.....X/"},
				{".XXX/.X../.X../XX../XXX./","XX.../XXXXX/X...X/....X/",".XXX/..XX/..X./..X./XXX./","X..../X...X/XXXXX/...XX/","XXX./..X./..X./..XX/.XXX/","....X/X...X/XXXXX/XX.../","XXX./XX../.X../.X../.XXX/","...XX/XXXXX/X...X/X..../"},
				{"..X/.XX/.X./.X./XX./XXX/","XX..../XXXXX./X...XX/","XXX/.XX/.X./.X./XX./X../","XX...X/.XXXXX/....XX/","X../XX./.X./.X./.XX/XXX/","X...XX/XXXXX./XX..../","XXX/XX./.X./.X./.XX/..X/","....XX/.XXXXX/XX...X/"},
				{"XX./.X./.X./.X./XX./XXX/","XX...X/XXXXXX/X...../","XXX/.XX/.X./.X./.X./.XX/",".....X/XXXXXX/X...XX/",".XX/.X./.X./.X./.XX/XXX/","X...../XXXXXX/XX...X/","XXX/XX./.X./.X./.X./XX./","X...XX/XXXXXX/.....X/"},
				{".XX/.X./.X./.X./XX./XXX/","XX..../XXXXXX/X....X/","XXX/.XX/.X./.X./.X./XX./","X....X/XXXXXX/....XX/","XX./.X./.X./.X./.XX/XXX/","X....X/XXXXXX/XX..../","XXX/XX./.X./.X./.X./.XX/","....XX/XXXXXX/X....X/"},
				{"X....../XXXXX../....X../....XXX/","..XX/..X./..X./..X./XXX./X.../X.../","XXX..../..X..../..XXXXX/......X/","...X/...X/.XXX/.X../.X../.X../XX../","......X/..XXXXX/..X..../XXX..../","X.../X.../XXX./..X./..X./..X./..XX/","....XXX/....X../XXXXX../X....../","XX../.X../.X../.X../.XXX/...X/...X/"},
				{"XXXXX../X...X../....XXX/",".XX/..X/..X/..X/XXX/X../X../","XXX..../..X...X/..XXXXX/","..X/..X/XXX/X../X../X../XX./","..XXXXX/..X...X/XXX..../","X../X../XXX/..X/..X/..X/.XX/","....XXX/X...X../XXXXX../","XX./X../X../X../XXX/..X/..X/"},
				{"XX...../.XXXX../....X../....XXX/","...X/..XX/..X./..X./XXX./X.../X.../","XXX..../..X..../..XXXX./.....XX/","...X/...X/.XXX/.X../.X../XX../X.../",".....XX/..XXXX./..X..../XXX..../","X.../X.../XXX./..X./..X./..XX/...X/","....XXX/....X../.XXXX../XX...../","X.../XX../.X../.X../.XXX/...X/...X/"},
				{"XX..../XXXX../...X../...XXX/","..XX/..XX/..X./XXX./X.../X.../","XXX.../..X.../..XXXX/....XX/","...X/...X/.XXX/.X../XX../XX../","....XX/..XXXX/..X.../XXX.../","X.../X.../XXX./..X./..XX/..XX/","...XXX/...X../XXXX../XX..../","XX../XX../.X../.XXX/...X/...X/"},
				{"X...../X...../XXXX../...X../...XXX/","..XXX/..X../..X../XXX../X..../X..../","XXX.../..X.../..XXXX/.....X/.....X/","....X/....X/..XXX/..X../..X../XXX../",".....X/.....X/..XXXX/..X.../XXX.../","X..../X..../XXX../..X../..X../..XXX/","...XXX/...X../XXXX../X...../X...../","XXX../..X../..X../..XXX/....X/....X/"},
				{".XXXX../XX..X../....XXX/",".X./.XX/..X/..X/XXX/X../X../","XXX..../..X..XX/..XXXX./","..X/..X/XXX/X../X../XX./.X./","..XXXX./..X..XX/XXX..../","X../X../XXX/..X/..X/.XX/.X./","....XXX/XX..X../.XXXX../",".X./XX./X../X../XXX/..X/..X/"},
				{"XXXX../XX.X../...XXX/",".XX/.XX/..X/XXX/X../X../","XXX.../..X.XX/..XXXX/","..X/..X/XXX/X../XX./XX./","..XXXX/..X.XX/XXX.../","X../X../XXX/..X/.XX/.XX/","...XXX/XX.X../XXXX../","XX./XX./X../XXX/..X/..X/"},
				{"XXXX../X..X../X..XXX/","XXX/..X/..X/XXX/X../X../","XXX..X/..X..X/..XXXX/","..X/..X/XXX/X../X../XXX/","..XXXX/..X..X/XXX..X/","X../X../XXX/..X/..X/XXX/","X..XXX/X..X../XXXX../","XXX/X../X../XXX/..X/..X/"},
				{"X...../XX..../.XXX../...X../...XXX/","...XX/..XX./..X../XXX../X..../X..../","XXX.../..X.../..XXX./....XX/.....X/","....X/....X/..XXX/..X../.XX../XX.../",".....X/....XX/..XXX./..X.../XXX.../","X..../X..../XXX../..X../..XX./...XX/","...XXX/...X../.XXX../XX..../X...../","XX.../.XX../..X../..XXX/....X/....X/"},
				{".X.../XX.../XXX../..X../..XXX/","..XX./..XXX/XXX../X..../X..../","XXX../..X../..XXX/...XX/...X./","....X/....X/..XXX/XXX../.XX../","...X./...XX/..XXX/..X../XXX../","X..../X..../XXX../..XXX/..XX./","..XXX/..X../XXX../XX.../.X.../",".XX../XXX../..XXX/....X/....X/"},
				{"XX..../.X..../.XXX../...X../...XXX/","....X/..XXX/..X../XXX../X..../X..../","XXX.../..X.../..XXX./....X./....XX/","....X/....X/..XXX/..X../XXX../X..../","....XX/....X./..XXX./..X.../XXX.../","X..../X..../XXX../..X../..XXX/....X/","...XXX/...X../.XXX../.X..../XX..../","X..../XXX../..X../..XXX/....X/....X/"},
				{"XX.../X..../XXX../..X../..XXX/","..XXX/..X.X/XXX../X..../X..../","XXX../..X../..XXX/....X/...XX/","....X/....X/..XXX/X.X../XXX../","...XX/....X/..XXX/..X../XXX../","X..../X..../XXX../..X.X/..XXX/","..XXX/..X../XXX../X..../XX.../","XXX../X.X../..XXX/....X/....X/"},
				{".XXX../XX.X../X..XXX/","XX./.XX/..X/XXX/X../X../","XXX..X/..X.XX/..XXX./","..X/..X/XXX/X../XX./.XX/","..XXX./..X.XX/XXX..X/","X../X../XXX/..X/.XX/XX./","X..XXX/XX.X../.XXX../",".XX/XX./X../XXX/..X/..X/"},
				{".XXX../.X.X../XX.XXX/","X../XXX/..X/XXX/X../X../","XXX.XX/..X.X./..XXX./","..X/..X/XXX/X../XXX/..X/","..XXX./..X.X./XXX.XX/","X../X../XXX/..X/XXX/X../","XX.XXX/.X.X../.XXX../","..X/XXX/X../XXX/..X/..X/"},
				{"X...../XXX.../..XX../...X../...XXX/","...XX/...X./..XX./XXX../X..../X..../","XXX.../..X.../..XX../...XXX/.....X/","....X/....X/..XXX/.XX../.X.../XX.../",".....X/...XXX/..XX../..X.../XXX.../","X..../X..../XXX../..XX./...X./...XX/","...XXX/...X../..XX../XXX.../X...../","XX.../.X.../.XX../..XXX/....X/....X/"},
				{"XXX.../X.XX../...X../...XXX/","..XX/...X/..XX/XXX./X.../X.../","XXX.../..X.../..XX.X/...XXX/","...X/...X/.XXX/XX../X.../XX../","...XXX/..XX.X/..X.../XXX.../","X.../X.../XXX./..XX/...X/..XX/","...XXX/...X../X.XX../XXX.../","XX../X.../XX../.XXX/...X/...X/"},
				{"XX..../.XX.../..XX../...X../...XXX/","....X/...XX/..XX./XXX../X..../X..../","XXX.../..X.../..XX../...XX./....XX/","....X/....X/..XXX/.XX../XX.../X..../","....XX/...XX./..XX../..X.../XXX.../","X..../X..../XXX../..XX./...XX/....X/","...XXX/...X../..XX../.XX.../XX..../","X..../XX.../.XX../..XXX/....X/....X/"},
				{"XX.../XX.../.XX../..X../..XXX/","...XX/..XXX/XXX../X..../X..../","XXX../..X../..XX./...XX/...XX/","....X/....X/..XXX/XXX../XX.../","...XX/...XX/..XX./..X../XXX../","X..../X..../XXX../..XXX/...XX/","..XXX/..X../.XX../XX.../XX.../","XX.../XXX../..XXX/....X/....X/"},
				{".XX.../XXXX../...X../...XXX/","..X./..XX/..XX/XXX./X.../X.../","XXX.../..X.../..XXXX/...XX./","...X/...X/.XXX/XX../XX../.X../","...XX./..XXXX/..X.../XXX.../","X.../X.../XXX./..XX/..XX/..X./","...XXX/...X../XXXX../.XX.../",".X../XX../XX../.XXX/...X/...X/"},
				{"..X./XXX./XX../.X../.XXX/","..XX./XXXX./X..XX/X..../","XXX./..X./..XX/.XXX/.X../","....X/XX..X/.XXXX/.XX../",".X../.XXX/..XX/..X./XXX./","X..../X..XX/XXXX./..XX./",".XXX/.X../XX../XXX./..X./",".XX../.XXXX/XX..X/....X/"},
				{"XXX./XXX./.X../.XXX/","..XX/XXXX/X.XX/X.../","XXX./..X./.XXX/.XXX/","...X/XX.X/XXXX/XX../",".XXX/.XXX/..X./XXX./","X.../X.XX/XXXX/..XX/",".XXX/.X../XXX./XXX./","XX../XXXX/XX.X/...X/"},
				{".XX./XX../XX../.X../.XXX/","..XX./XXXXX/X...X/X..../","XXX./..X./..XX/..XX/.XX./","....X/X...X/XXXXX/.XX../",".XX./..XX/..XX/..X./XXX./","X..../X...X/XXXXX/..XX./",".XXX/.X../XX../XX../.XX./",".XX../XXXXX/X...X/....X/"},
				{"XXX.../..X.../..XX../...X../...XXX/","....X/....X/..XXX/XXX../X..../X..../","...XXX/...X../..XX../..X.../XXX.../","X..../X..../XXX../..XXX/....X/....X/"},
				{"X..../XX.../.X.../.XX../..X../..XXX/","....XX/..XXX./XXX.../X...../X...../","XXX../..X../..XX./...X./...XX/....X/",".....X/.....X/...XXX/.XXX../XX..../","....X/...XX/...X./..XX./..X../XXX../","X...../X...../XXX.../..XXX./....XX/","..XXX/..X../.XX../.X.../XX.../X..../","XX..../.XXX../...XXX/.....X/.....X/"},
				{"XXX./X.../XX../.X../.XXX/","..XXX/XXX.X/X...X/X..../","XXX./..X./..XX/...X/.XXX/","....X/X...X/X.XXX/XXX../",".XXX/...X/..XX/..X./XXX./","X..../X...X/XXX.X/..XXX/",".XXX/.X../XX../X.../XXX./","XXX../X.XXX/X...X/....X/"},
				{".X../XX../X.../XX../.X../.XXX/","..XXX./XXX.XX/X...../X...../","XXX./..X./..XX/...X/..XX/..X./",".....X/.....X/XX.XXX/.XXX../","..X./..XX/...X/..XX/..X./XXX./","X...../X...../XXX.XX/..XXX./",".XXX/.X../XX../X.../XX../.X../",".XXX../XX.XXX/.....X/.....X/"},
				{"XX.../.X.../.X.../.XX../..X../..XXX/",".....X/..XXXX/XXX.../X...../X...../","XXX../..X../..XX./...X./...X./...XX/",".....X/.....X/...XXX/XXXX../X...../","...XX/...X./...X./..XX./..X../XXX../","X...../X...../XXX.../..XXXX/.....X/","..XXX/..X../.XX../.X.../.X.../XX.../","X...../XXXX../...XXX/.....X/.....X/"},
				{"XX../X.../X.../XX../.X../.XXX/","..XXXX/XXX..X/X...../X...../","XXX./..X./..XX/...X/...X/..XX/",".....X/.....X/X..XXX/XXXX../","..XX/...X/...X/..XX/..X./XXX./","X...../X...../XXX..X/..XXXX/",".XXX/.X../XX../X.../X.../XX../","XXXX../X..XXX/.....X/.....X/"},
				{"X.XX../XXXX../...XXX/",".XX/.X./.XX/XXX/X../X../","XXX.../..XXXX/..XX.X/","..X/..X/XXX/XX./.X./XX./","..XX.X/..XXXX/XXX.../","X../X../XXX/.XX/.X./.XX/","...XXX/XXXX../X.XX../","XX./.X./XX./XXX/..X/..X/"},
				{"..XX../XXXX../X..XXX/","XX./.X./.XX/XXX/X../X../","XXX..X/..XXXX/..XX../","..X/..X/XXX/XX./.X./.XX/","..XX../..XXXX/XXX..X/","X../X../XXX/.XX/.X./XX./","X..XXX/XXXX../..XX../",".XX/.X./XX./XXX/..X/..X/"},
				{"XXXX../.XXX../...XXX/","..X/.XX/.XX/XXX/X../X../","XXX.../..XXX./..XXXX/","..X/..X/XXX/XX./XX./X../","..XXXX/..XXX./XXX.../","X../X../XXX/.XX/.XX/..X/","...XXX/.XXX../XXXX../","X../XX./XX./XXX/..X/..X/"},
				{"..XX../.XXX../XX.XXX/","X../XX./.XX/XXX/X../X../","XXX.XX/..XXX./..XX../","..X/..X/XXX/XX./.XX/..X/","..XX../..XXX./XXX.XX/","X../X../XXX/.XX/XX./X../","XX.XXX/.XXX../..XX../","..X/.XX/XX./XXX/..X/..X/"},
				{"..XX../..XX../XXXXXX/","X../X../XXX/XXX/X../X../","XXXXXX/..XX../..XX../","..X/..X/XXX/XXX/..X/..X/"},
				{".XX../.XX../XXXXX/X..../","XX../.XXX/.XXX/.X../.X../","....X/XXXXX/..XX./..XX./","..X./..X./XXX./XXX./..XX/","..XX./..XX./XXXXX/....X/",".X../.X../.XXX/.XXX/XX../","X..../XXXXX/.XX../.XX../","..XX/XXX./XXX./..X./..X./"},
				{".XX../.XX../.XXXX/XX.../","X.../XXXX/.XXX/.X../.X../","...XX/XXXX./..XX./..XX./","..X./..X./XXX./XXXX/...X/","..XX./..XX./XXXX./...XX/",".X../.X../.XXX/XXXX/X.../","XX.../.XXXX/.XX../.XX../","...X/XXXX/XXX./..X./..X./"},
				{"....X/XXXXX/X..../XXX../","XXX./X.X./X.X./..X./..XX/","..XXX/....X/XXXXX/X..../","XX../.X../.X.X/.X.X/.XXX/","X..../XXXXX/....X/..XXX/","..XX/..X./X.X./X.X./XXX./","XXX../X..../XXXXX/....X/",".XXX/.X.X/.X.X/.X../XX../"},
				{"XXXXX/X...X/XXX../","XXX/X.X/X.X/..X/.XX/","..XXX/X...X/XXXXX/","XX./X../X.X/X.X/XXX/","XXXXX/X...X/..XXX/",".XX/..X/X.X/X.X/XXX/","XXX../X...X/XXXXX/","XXX/X.X/X.X/X../XX./"},
				{"..XX/XXXX/X.../XXX./","XXX./X.X./X.XX/..XX/",".XXX/...X/XXXX/XX../","XX../XX.X/.X.X/.XXX/","XX../XXXX/...X/.XXX/","..XX/X.XX/X.X./XXX./","XXX./X.../XXXX/..XX/",".XXX/.X.X/XX.X/XX../"},
				{"...XX/XXXX./X..../XXX../","XXX./X.X./X.X./..XX/...X/","..XXX/....X/.XXXX/XX.../","X.../XX../.X.X/.X.X/.XXX/","XX.../.XXXX/....X/..XXX/","...X/..XX/X.X./X.X./XXX./","XXX../X..../XXXX./...XX/",".XXX/.X.X/.X.X/XX../X.../"},
				{"...X/...X/XXXX/X.../XXX./","XXX../X.X../X.X../..XXX/",".XXX/...X/XXXX/X.../X.../","XXX../..X.X/..X.X/..XXX/","X.../X.../XXXX/...X/.XXX/","..XXX/X.X../X.X../XXX../","XXX./X.../XXXX/...X/...X/","..XXX/..X.X/..X.X/XXX../"},
				{"XXXX./X..XX/XXX../","XXX/X.X/X.X/.XX/.X./","..XXX/XX..X/.XXXX/",".X./XX./X.X/X.X/XXX/",".XXXX/XX..X/..XXX/",".X./.XX/X.X/X.X/XXX/","XXX../X..XX/XXXX./","XXX/X.X/X.X/XX./.X./"},
				{".X./.XX/XXX/X../XXX/","XXX../X.XXX/X.XX./","XXX/..X/XXX/XX./.X./",".XX.X/XXX.X/..XXX/",".X./XX./XXX/..X/XXX/","X.XX./X.XXX/XXX../","XXX/X../XXX/.XX/.X./","..XXX/XXX.X/.XX.X/"},
				{"...X/..XX/XXX./X.../XXX./","XXX../X.X../X.XX./...XX/",".XXX/...X/.XXX/XX../X.../","XX.../.XX.X/..X.X/..XXX/","X.../XX../.XXX/...X/.XXX/","...XX/X.XX./X.X../XXX../","XXX./X.../XXX./..XX/...X/","..XXX/..X.X/.XX.X/XX.../"},
				{".XX/..X/XXX/X../XXX/","XXX../X.X.X/X.XXX/","XXX/..X/XXX/X../XX./","XXX.X/X.X.X/..XXX/","XX./X../XXX/..X/XXX/","X.XXX/X.X.X/XXX../","XXX/X../XXX/..X/.XX/","..XXX/X.X.X/XXX.X/"},
				{"..XX/..X./XXX./X.../XXX./","XXX../X.X../X.XXX/....X/",".XXX/...X/.XXX/.X../XX../","X..../XXX.X/..X.X/..XXX/","XX../.X../.XXX/...X/.XXX/","....X/X.XXX/X.X../XXX../","XXX./X.../XXX./..X./..XX/","..XXX/..X.X/XXX.X/X..../"},
				{"X.../XXX./.XX./.X../.XXX/","...XX/XXXX./X.XX./X..../","XXX./..X./.XX./.XXX/...X/","....X/.XX.X/.XXXX/XX.../","...X/.XXX/.XX./..X./XXX./","X..../X.XX./XXXX./...XX/",".XXX/.X../.XX./XXX./X.../","XX.../.XXXX/.XX.X/....X/"},
				{"XX../.XX./.XX./.X../.XXX/","....X/XXXXX/X.XX./X..../","XXX./..X./.XX./.XX./..XX/","....X/.XX.X/XXXXX/X..../","..XX/.XX./.XX./..X./XXX./","X..../X.XX./XXXXX/....X/",".XXX/.X../.XX./.XX./XX../","X..../XXXXX/.XX.X/....X/"},
				{"...X/.XXX/XX../X.../XXX./","XXX../X.XX./X..X./...XX/",".XXX/...X/..XX/XXX./X.../","XX.../.X..X/.XX.X/..XXX/","X.../XXX./..XX/...X/.XXX/","...XX/X..X./X.XX./XXX../","XXX./X.../XX../.XXX/...X/","..XXX/.XX.X/.X..X/XX.../"},
				{".XXX/XX.X/X.../XXX./","XXX./X.XX/X..X/..XX/",".XXX/...X/X.XX/XXX./","XX../X..X/XX.X/.XXX/","XXX./X.XX/...X/.XXX/","..XX/X..X/X.XX/XXX./","XXX./X.../XX.X/.XXX/",".XXX/XX.X/X..X/XX../"},
				{".XX/.XX/XX./X../XXX/","XXX../X.XXX/X..XX/","XXX/..X/.XX/XX./XX./","XX..X/XXX.X/..XXX/","XX./XX./.XX/..X/XXX/","X..XX/X.XXX/XXX../","XXX/X../XX./.XX/.XX/","..XXX/XXX.X/XX..X/"},
				{"..XX/.XX./XX../X.../XXX./","XXX../X.XX./X..XX/....X/",".XXX/...X/..XX/.XX./XX../","X..../XX..X/.XX.X/..XXX/","XX../.XX./..XX/...X/.XXX/","....X/X..XX/X.XX./XXX../","XXX./X.../XX../.XX./..XX/","..XXX/.XX.X/XX..X/X..../"},
				{".XX./XXXX/X.../XXX./","XXX./X.XX/X.XX/..X./",".XXX/...X/XXXX/.XX./",".X../XX.X/XX.X/.XXX/",".XX./XXXX/...X/.XXX/","..X./X.XX/X.XX/XXX./","XXX./X.../XXXX/.XX./",".XXX/XX.X/XX.X/.X../"},
				{"XXX./..X./.XX./.X../.XXX/","....X/XXX.X/X.XXX/X..../",".XXX/.X../.XX./..X./XXX./","X..../X.XXX/XXX.X/....X/"},
				{"X../XX./.X./XX./X../XXX/","XXX.XX/X.XXX./X...../","XXX/..X/.XX/.X./.XX/..X/",".....X/.XXX.X/XX.XXX/","..X/.XX/.X./.XX/..X/XXX/","X...../X.XXX./XXX.XX/","XXX/X../XX./.X./XX./X../","XX.XXX/.XXX.X/.....X/"},
				{"..X/.XX/.X./XX./X../XXX/","XXX.../X.XXX./X...XX/","XXX/..X/.XX/.X./XX./X../","XX...X/.XXX.X/...XXX/","X../XX./.X./.XX/..X/XXX/","X...XX/X.XXX./XXX.../","XXX/X../XX./.X./.XX/..X/","...XXX/.XXX.X/XX...X/"},
				{"XX./.X./.X./XX./X../XXX/","XXX..X/X.XXXX/X...../","XXX/..X/.XX/.X./.X./.XX/",".....X/XXXX.X/X..XXX/",".XX/.X./.X./.XX/..X/XXX/","X...../X.XXXX/XXX..X/","XXX/X../XX./.X./.X./XX./","X..XXX/XXXX.X/.....X/"},
				{".XX/.X./.X./XX./X../XXX/","XXX.../X.XXXX/X....X/","XXX/..X/.XX/.X./.X./XX./","X....X/XXXX.X/...XXX/","XX./.X./.X./.XX/..X/XXX/","X....X/X.XXXX/XXX.../","XXX/X../XX./.X./.X./.XX/","...XXX/XXXX.X/X....X/"},
				{"X...../XXXX../...X../...X../...XXX/","...XX/...X./...X./XXXX./X..../X..../","XXX.../..X.../..X.../..XXXX/.....X/","....X/....X/.XXXX/.X.../.X.../XX.../",".....X/..XXXX/..X.../..X.../XXX.../","X..../X..../XXXX./...X./...X./...XX/","...XXX/...X../...X../XXXX../X...../","XX.../.X.../.X.../.XXXX/....X/....X/"},
				{"XXXX../X..X../...X../...XXX/","..XX/...X/...X/XXXX/X.../X.../","XXX.../..X.../..X..X/..XXXX/","...X/...X/XXXX/X.../X.../XX../","..XXXX/..X..X/..X.../XXX.../","X.../X.../XXXX/...X/...X/..XX/","...XXX/...X../X..X../XXXX../","XX../X.../X.../XXXX/...X/...X/"},
				{"XX..../.XXX../...X../...X../...XXX/","....X/...XX/...X./XXXX./X..../X..../","XXX.../..X.../..X.../..XXX./....XX/","....X/....X/.XXXX/.X.../XX.../X..../","....XX/..XXX./..X.../..X.../XXX.../","X..../X..../XXXX./...X./...XX/....X/","...XXX/...X../...X../.XXX../XX..../","X..../XX.../.X.../.XXXX/....X/....X/"},
				{"XX.../XXX../..X../..X../..XXX/","...XX/...XX/XXXX./X..../X..../","XXX../..X../..X../..XXX/...XX/","....X/....X/.XXXX/XX.../XX.../","...XX/..XXX/..X../..X../XXX../","X..../X..../XXXX./...XX/...XX/","..XXX/..X../..X../XXX../XX.../","XX.../XX.../.XXXX/....X/....X/"},
				{".XXX../XX.X../...X../...XXX/","..X./..XX/...X/XXXX/X.../X.../","XXX.../..X.../..X.XX/..XXX./","...X/...X/XXXX/X.../XX../.X../","..XXX./..X.XX/..X.../XXX.../","X.../X.../XXXX/...X/..XX/..X./","...XXX/...X../XX.X../.XXX../",".X../XX../X.../XXXX/...X/...X/"},
				{"X..../XX.../.XX../..X../..X../..XXX/","....XX/...XX./XXXX../X...../X...../","XXX../..X../..X../..XX./...XX/....X/",".....X/.....X/..XXXX/.XX.../XX..../","....X/...XX/..XX./..X../..X../XXX../","X...../X...../XXXX../...XX./....XX/","..XXX/..X../..X../.XX../XX.../X..../","XX..../.XX.../..XXXX/.....X/.....X/"},
				{".X../XX../XX../.X../.X../.XXX/","...XX./XXXXXX/X...../X...../","XXX./..X./..X./..XX/..XX/..X./",".....X/.....X/XXXXXX/.XX.../","..X./..XX/..XX/..X./..X./XXX./","X...../X...../XXXXXX/...XX./",".XXX/.X../.X../XX../XX../.X../",".XX.../XXXXXX/.....X/.....X/"},
				{"XX.../.X.../.XX../..X../..X../..XXX/",".....X/...XXX/XXXX../X...../X...../","XXX../..X../..X../..XX./...X./...XX/",".....X/.....X/..XXXX/XXX.../X...../","...XX/...X./..XX./..X../..X../XXX../","X...../X...../XXXX../...XXX/.....X/","..XXX/..X../..X../.XX../.X.../XX.../","X...../XXX.../..XXXX/.....X/.....X/"},
				{"XX../X.../XX../.X../.X../.XXX/","...XXX/XXXX.X/X...../X...../","XXX./..X./..X./..XX/...X/..XX/",".....X/.....X/X.XXXX/XXX.../","..XX/...X/..XX/..X./..X./XXX./","X...../X...../XXXX.X/...XXX/",".XXX/.X../.X../XX../X.../XX../","XXX.../X.XXXX/.....X/.....X/"},
				{".XX../XXX../X.X../..XXX/",".XX./..XX/XXXX/X.../X.../","XXX../..X.X/..XXX/..XX./","...X/...X/XXXX/XX../.XX./","..XX./..XXX/..X.X/XXX../","X.../X.../XXXX/..XX/.XX./","..XXX/X.X../XXX../.XX../",".XX./XX../XXXX/...X/...X/"},
				{".XX../.XX../XXX../..XXX/",".X../.XXX/XXXX/X.../X.../","XXX../..XXX/..XX./..XX./","...X/...X/XXXX/XXX./..X./","..XX./..XX./..XXX/XXX../","X.../X.../XXXX/.XXX/.X../","..XXX/XXX../.XX../.XX../","..X./XXX./XXXX/...X/...X/"},
				{"...X/XXXX/X.../X.../XXX./","XXXX./X..X./X..X./...XX/",".XXX/...X/...X/XXXX/X.../","XX.../.X..X/.X..X/.XXXX/","X.../XXXX/...X/...X/.XXX/","...XX/X..X./X..X./XXXX./","XXX./X.../X.../XXXX/...X/",".XXXX/.X..X/.X..X/XX.../"},
				{"XXXX/X..X/X.../XXX./","XXXX/X..X/X..X/..XX/",".XXX/...X/X..X/XXXX/","XX../X..X/X..X/XXXX/","XXXX/X..X/...X/.XXX/","..XX/X..X/X..X/XXXX/","XXX./X.../X..X/XXXX/","XXXX/X..X/X..X/XX../"},
				{".XX/XXX/X../X../XXX/","XXXX./X..XX/X..XX/","XXX/..X/..X/XXX/XX./","XX..X/XX..X/.XXXX/","XX./XXX/..X/..X/XXX/","X..XX/X..XX/XXXX./","XXX/X../X../XXX/.XX/",".XXXX/XX..X/XX..X/"},
				{"..XX/XXX./X.../X.../XXX./","XXXX./X..X./X..XX/....X/",".XXX/...X/...X/.XXX/XX../","X..../XX..X/.X..X/.XXXX/","XX../.XXX/...X/...X/.XXX/","....X/X..XX/X..X./XXXX./","XXX./X.../X.../XXX./..XX/",".XXXX/.X..X/XX..X/X..../"},
				{"XXX./X.XX/X.../XXX./","XXXX/X..X/X.XX/..X./",".XXX/...X/XX.X/.XXX/",".X../XX.X/X..X/XXXX/",".XXX/XX.X/...X/.XXX/","..X./X.XX/X..X/XXXX/","XXX./X.../X.XX/XXX./","XXXX/X..X/XX.X/.X../"},
				{"X../XX./XX./X../X../XXX/","XXXXXX/X..XX./X...../","XXX/..X/..X/.XX/.XX/..X/",".....X/.XX..X/XXXXXX/","..X/.XX/.XX/..X/..X/XXX/","X...../X..XX./XXXXXX/","XXX/X../X../XX./XX./X../","XXXXXX/.XX..X/.....X/"},
				{"..X/.XX/XX./X../X../XXX/","XXXX../X..XX./X...XX/","XXX/..X/..X/.XX/XX./X../","XX...X/.XX..X/..XXXX/","X../XX./.XX/..X/..X/XXX/","X...XX/X..XX./XXXX../","XXX/X../X../XX./.XX/..X/","..XXXX/.XX..X/XX...X/"},
				{"XX./.X./XX./X../X../XXX/","XXXX.X/X..XXX/X...../","XXX/..X/..X/.XX/.X./.XX/",".....X/XXX..X/X.XXXX/",".XX/.X./.XX/..X/..X/XXX/","X...../X..XXX/XXXX.X/","XXX/X../X../XX./.X./XX./","X.XXXX/XXX..X/.....X/"},
				{".XX/.X./XX./X../X../XXX/","XXXX../X..XXX/X....X/","XXX/..X/..X/.XX/.X./XX./","X....X/XXX..X/..XXXX/","XX./.X./.XX/..X/..X/XXX/","X....X/X..XXX/XXXX../","XXX/X../X../XX./.X./.XX/","..XXXX/XXX..X/X....X/"},
				{"X..../XXX../..X../..X../..X../..XXX/","....XX/....X./XXXXX./X...../X...../","XXX../..X../..X../..X../..XXX/....X/",".....X/.....X/.XXXXX/.X..../XX..../","....X/..XXX/..X../..X../..X../XXX../","X...../X...../XXXXX./....X./....XX/","..XXX/..X../..X../..X../XXX../X..../","XX..../.X..../.XXXXX/.....X/.....X/"},
				{"XXX../X.X../..X../..X../..XXX/","...XX/....X/XXXXX/X..../X..../","XXX../..X../..X../..X.X/..XXX/","....X/....X/XXXXX/X..../XX.../","..XXX/..X.X/..X../..X../XXX../","X..../X..../XXXXX/....X/...XX/","..XXX/..X../..X../X.X../XXX../","XX.../X..../XXXXX/....X/....X/"},
				{"XX.../.XX../..X../..X../..X../..XXX/",".....X/....XX/XXXXX./X...../X...../","XXX../..X../..X../..X../..XX./...XX/",".....X/.....X/.XXXXX/XX..../X...../","...XX/..XX./..X../..X../..X../XXX../","X...../X...../XXXXX./....XX/.....X/","..XXX/..X../..X../..X../.XX../XX.../","X...../XX..../.XXXXX/.....X/.....X/"},
				{"XX../XX../.X../.X../.X../.XXX/","....XX/XXXXXX/X...../X...../","XXX./..X./..X./..X./..XX/..XX/",".....X/.....X/XXXXXX/XX..../","..XX/..XX/..X./..X./..X./XXX./","X...../X...../XXXXXX/....XX/",".XXX/.X../.X../.X../XX../XX../","XX..../XXXXXX/.....X/.....X/"},
				{".XX../XXX../..X../..X../..XXX/","...X./...XX/XXXXX/X..../X..../","XXX../..X../..X../..XXX/..XX./","....X/....X/XXXXX/XX.../.X.../","..XX./..XXX/..X../..X../XXX../","X..../X..../XXXXX/...XX/...X./","..XXX/..X../..X../XXX../.XX../",".X.../XX.../XXXXX/....X/....X/"},
				{"..X/XXX/X../X../X../XXX/","XXXXX./X...X./X...XX/","XXX/..X/..X/..X/XXX/X../","XX...X/.X...X/.XXXXX/","X../XXX/..X/..X/..X/XXX/","X...XX/X...X./XXXXX./","XXX/X../X../X../XXX/..X/",".XXXXX/.X...X/XX...X/"},
				{"XXX/X.X/X../X../XXX/","XXXXX/X...X/X..XX/","XXX/..X/..X/X.X/XXX/","XX..X/X...X/XXXXX/","XXX/X.X/..X/..X/XXX/","X..XX/X...X/XXXXX/","XXX/X../X../X.X/XXX/","XXXXX/X...X/XX..X/"},
				{"XX./XX./X../X../X../XXX/","XXXXXX/X...XX/X...../","XXX/..X/..X/..X/.XX/.XX/",".....X/XX...X/XXXXXX/",".XX/.XX/..X/..X/..X/XXX/","X...../X...XX/XXXXXX/","XXX/X../X../X../XX./XX./","XXXXXX/XX...X/.....X/"},
				{".XX/XX./X../X../X../XXX/","XXXXX./X...XX/X....X/","XXX/..X/..X/..X/.XX/XX./","X....X/XX...X/.XXXXX/","XX./.XX/..X/..X/..X/XXX/","X....X/X...XX/XXXXX./","XXX/X../X../X../XX./.XX/",".XXXXX/XX...X/X....X/"},
				{"XX./XXX/X../X../XXX/","XXXXX/X..XX/X..X./","XXX/..X/..X/XXX/.XX/",".X..X/XX..X/XXXXX/",".XX/XXX/..X/..X/XXX/","X..X./X..XX/XXXXX/","XXX/X../X../XXX/XX./","XXXXX/XX..X/.X..X/"},
				{"XXX../..X../..X../..X../..X../..XXX/",".....X/.....X/XXXXXX/X...../X...../","..XXX/..X../..X../..X../..X../XXX../","X...../X...../XXXXXX/.....X/.....X/"},
				{"X.../XX../.X../.X../.X../.X../.XXX/",".....XX/XXXXXX./X....../X....../","XXX./..X./..X./..X./..X./..XX/...X/","......X/......X/.XXXXXX/XX...../","...X/..XX/..X./..X./..X./..X./XXX./","X....../X....../XXXXXX./.....XX/",".XXX/.X../.X../.X../.X../XX../X.../","XX...../.XXXXXX/......X/......X/"},
				{"XXX/X../X../X../X../XXX/","XXXXXX/X....X/X....X/","XXX/..X/..X/..X/..X/XXX/","X....X/X....X/XXXXXX/"},
				{".X./XX./X../X../X../X../XXX/","XXXXXX./X....XX/X....../","XXX/..X/..X/..X/..X/.XX/.X./","......X/XX....X/.XXXXXX/",".X./.XX/..X/..X/..X/..X/XXX/","X....../X....XX/XXXXXX./","XXX/X../X../X../X../XX./.X./",".XXXXXX/XX....X/......X/"},
				{"XX../.X../.X../.X../.X../.X../.XXX/","......X/XXXXXXX/X....../X....../","XXX./..X./..X./..X./..X./..X./..XX/","......X/......X/XXXXXXX/X....../","..XX/..X./..X./..X./..X./..X./XXX./","X....../X....../XXXXXXX/......X/",".XXX/.X../.X../.X../.X../.X../XX../","X....../XXXXXXX/......X/......X/"},
				{"XX./X../X../X../X../X../XXX/","XXXXXXX/X.....X/X....../","XXX/..X/..X/..X/..X/..X/.XX/","......X/X.....X/XXXXXXX/",".XX/..X/..X/..X/..X/..X/XXX/","X....../X.....X/XXXXXXX/","XXX/X../X../X../X../X../XX./","XXXXXXX/X.....X/......X/"},
				{"X......./XXXXXXX./......XX/",".XX/.X./.X./.X./.X./.X./XX./X../","XX....../.XXXXXXX/.......X/","..X/.XX/.X./.X./.X./.X./.X./XX./",".......X/.XXXXXXX/XX....../","X../XX./.X./.X./.X./.X./.X./.XX/","......XX/XXXXXXX./X......./","XX./.X./.X./.X./.X./.X./.XX/..X/"},
				{"XXXXXXX./X.....XX/","XX/.X/.X/.X/.X/.X/XX/X./","XX.....X/.XXXXXXX/",".X/XX/X./X./X./X./X./XX/",".XXXXXXX/XX.....X/","X./XX/.X/.X/.X/.X/.X/XX/","X.....XX/XXXXXXX./","XX/X./X./X./X./X./XX/.X/"},
				{"XX....../.XXXXXX./......XX/","..X/.XX/.X./.X./.X./.X./XX./X../","......XX/.XXXXXX./XX....../","X../XX./.X./.X./.X./.X./.XX/..X/"},
				{"XX...../XXXXXX./.....XX/",".XX/.XX/.X./.X./.X./XX./X../","XX...../.XXXXXX/.....XX/","..X/.XX/.X./.X./.X./XX./XX./",".....XX/.XXXXXX/XX...../","X../XX./.X./.X./.X./.XX/.XX/",".....XX/XXXXXX./XX...../","XX./XX./.X./.X./.X./.XX/..X/"},
				{".XXXXXX./XX....XX/","X./XX/.X/.X/.X/.X/XX/X./","XX....XX/.XXXXXX./",".X/XX/X./X./X./X./XX/.X/"},
				{"XXXXXX./XX...XX/","XX/XX/.X/.X/.X/XX/X./","XX...XX/.XXXXXX/",".X/XX/X./X./X./XX/XX/",".XXXXXX/XX...XX/","X./XX/.X/.X/.X/XX/XX/","XX...XX/XXXXXX./","XX/XX/X./X./X./XX/.X/"},
				{"X....../XX...../.XXXXX./.....XX/","..XX/.XX./.X../.X../.X../XX../X.../","XX...../.XXXXX./.....XX/......X/","...X/..XX/..X./..X./..X./.XX./XX../","......X/.....XX/.XXXXX./XX...../","X.../XX../.X../.X../.X../.XX./..XX/",".....XX/.XXXXX./XX...../X....../","XX../.XX./..X./..X./..X./..XX/...X/"},
				{".X..../XX..../XXXXX./....XX/",".XX./.XXX/.X../.X../XX../X.../","XX..../.XXXXX/....XX/....X./","...X/..XX/..X./..X./XXX./.XX./","....X./....XX/.XXXXX/XX..../","X.../XX../.X../.X../.XXX/.XX./","....XX/XXXXX./XX..../.X..../",".XX./XXX./..X./..X./..XX/...X/"},
				{"XX...../.X...../.XXXXX./.....XX/","...X/.XXX/.X../.X../.X../XX../X.../","XX...../.XXXXX./.....X./.....XX/","...X/..XX/..X./..X./..X./XXX./X.../",".....XX/.....X./.XXXXX./XX...../","X.../XX../.X../.X../.X../.XXX/...X/",".....XX/.XXXXX./.X...../XX...../","X.../XXX./..X./..X./..X./..XX/...X/"},
				{"XX..../X...../XXXXX./....XX/",".XXX/.X.X/.X../.X../XX../X.../","XX..../.XXXXX/.....X/....XX/","...X/..XX/..X./..X./X.X./XXX./","....XX/.....X/.XXXXX/XX..../","X.../XX../.X../.X../.X.X/.XXX/","....XX/XXXXX./X...../XX..../","XXX./X.X./..X./..X./..XX/...X/"},
				{".XXXXX./XX...XX/X....../","XX./.XX/..X/..X/..X/.XX/.X./","......X/XX...XX/.XXXXX./",".X./XX./X../X../X../XX./.XX/",".XXXXX./XX...XX/......X/",".X./.XX/..X/..X/..X/.XX/XX./","X....../XX...XX/.XXXXX./",".XX/XX./X../X../X../XX./.X./"},
				{"XXXXX./XX..XX/.X..../",".XX/XXX/..X/..X/.XX/.X./","....X./XX..XX/.XXXXX/",".X./XX./X../X../XXX/XX./",".XXXXX/XX..XX/....X./",".X./.XX/..X/..X/XXX/.XX/",".X..../XX..XX/XXXXX./","XX./XXX/X../X../XX./.X./"},
				{".XXXXX./.X...XX/XX...../","X../XXX/..X/..X/..X/.XX/.X./",".....XX/XX...X./.XXXXX./",".X./XX./X../X../X../XXX/..X/",".XXXXX./XX...X./.....XX/",".X./.XX/..X/..X/..X/XXX/X../","XX...../.X...XX/.XXXXX./","..X/XXX/X../X../X../XX./.X./"},
				{"XXXXX./X...XX/XX..../","XXX/X.X/..X/..X/.XX/.X./","....XX/XX...X/.XXXXX/",".X./XX./X../X../X.X/XXX/",".XXXXX/XX...X/....XX/",".X./.XX/..X/..X/X.X/XXX/","XX..../X...XX/XXXXX./","XXX/X.X/X../X../XX./.X./"},
				{"X....../XXX..../..XXXX./.....XX/","..XX/..X./.XX./.X../.X../XX../X.../","XX...../.XXXX../....XXX/......X/","...X/..XX/..X./..X./.XX./.X../XX../","......X/....XXX/.XXXX../XX...../","X.../XX../.X../.X../.XX./..X./..XX/",".....XX/..XXXX./XXX..../X....../","XX../.X../.XX./..X./..X./..XX/...X/"},
				{"XXX..../X.XXXX./.....XX/",".XX/..X/.XX/.X./.X./XX./X../","XX...../.XXXX.X/....XXX/","..X/.XX/.X./.X./XX./X../XX./","....XXX/.XXXX.X/XX...../","X../XX./.X./.X./.XX/..X/.XX/",".....XX/X.XXXX./XXX..../","XX./X../XX./.X./.X./.XX/..X/"},
				{"XX...../.XX..../..XXXX./.....XX/","...X/..XX/.XX./.X../.X../XX../X.../","XX...../.XXXX../....XX./.....XX/","...X/..XX/..X./..X./.XX./XX../X.../",".....XX/....XX./.XXXX../XX...../","X.../XX../.X../.X../.XX./..XX/...X/",".....XX/..XXXX./.XX..../XX...../","X.../XX../.XX./..X./..X./..XX/...X/"},
				{"XX..../XX..../.XXXX./....XX/","..XX/.XXX/.X../.X../XX../X.../","XX..../.XXXX./....XX/....XX/","...X/..XX/..X./..X./XXX./XX../","....XX/....XX/.XXXX./XX..../","X.../XX../.X../.X../.XXX/..XX/","....XX/.XXXX./XX..../XX..../","XX../XXX./..X./..X./..XX/...X/"},
				{".XX..../XXXXXX./.....XX/",".X./.XX/.XX/.X./.X./XX./X../","XX...../.XXXXXX/....XX./","..X/.XX/.X./.X./XX./XX./.X./","....XX./.XXXXXX/XX...../","X../XX./.X./.X./.XX/.XX/.X./",".....XX/XXXXXX./.XX..../",".X./XX./XX./.X./.X./.XX/..X/"},
				{"..X../XXX../XXXX./...XX/",".XX./.XX./.XXX/XX../X.../","XX.../.XXXX/..XXX/..X../","...X/..XX/XXX./.XX./.XX./","..X../..XXX/.XXXX/XX.../","X.../XX../.XXX/.XX./.XX./","...XX/XXXX./XXX../..X../",".XX./.XX./XXX./..XX/...X/"},
				{".XX../XX.../XXXX./...XX/",".XX./.XXX/.X.X/XX../X.../","XX.../.XXXX/...XX/..XX./","...X/..XX/X.X./XXX./.XX./","..XX./...XX/.XXXX/XX.../","X.../XX../.X.X/.XXX/.XX./","...XX/XXXX./XX.../.XX../",".XX./XXX./X.X./..XX/...X/"},
				{"X...../XX..../.X..../.XXXX./....XX/","...XX/.XXX./.X.../.X.../XX.../X..../","XX..../.XXXX./....X./....XX/.....X/","....X/...XX/...X./...X./.XXX./XX.../",".....X/....XX/....X./.XXXX./XX..../","X..../XX.../.X.../.X.../.XXX./...XX/","....XX/.XXXX./.X..../XX..../X...../","XX.../.XXX./...X./...X./...XX/....X/"},
				{".X.../XX.../X..../XXXX./...XX/",".XXX./.X.XX/.X.../XX.../X..../","XX.../.XXXX/....X/...XX/...X./","....X/...XX/...X./XX.X./.XXX./","...X./...XX/....X/.XXXX/XX.../","X..../XX.../.X.../.X.XX/.XXX./","...XX/XXXX./X..../XX.../.X.../",".XXX./XX.X./...X./...XX/....X/"},
				{"XX..../.X..../.X..../.XXXX./....XX/","....X/.XXXX/.X.../.X.../XX.../X..../","XX..../.XXXX./....X./....X./....XX/","....X/...XX/...X./...X./XXXX./X..../","....XX/....X./....X./.XXXX./XX..../","X..../XX.../.X.../.X.../.XXXX/....X/","....XX/.XXXX./.X..../.X..../XX..../","X..../XXXX./...X./...X./...XX/....X/"},
				{"XX.../X..../X..../XXXX./...XX/",".XXXX/.X..X/.X.../XX.../X..../","XX.../.XXXX/....X/....X/...XX/","....X/...XX/...X./X..X./XXXX./","...XX/....X/....X/.XXXX/XX.../","X..../XX.../.X.../.X..X/.XXXX/","...XX/XXXX./X..../X..../XX.../","XXXX./X..X./...X./...XX/....X/"},
				{"X.XXXX./XXX..XX/","XX/X./XX/.X/.X/XX/X./","XX..XXX/.XXXX.X/",".X/XX/X./X./XX/.X/XX/",".XXXX.X/XX..XXX/","X./XX/.X/.X/XX/X./XX/","XXX..XX/X.XXXX./","XX/.X/XX/X./X./XX/.X/"},
				{"..XXXX./XXX..XX/X....../","XX./.X./.XX/..X/..X/.XX/.X./","......X/XX..XXX/.XXXX../",".X./XX./X../X../XX./.X./.XX/",".XXXX../XX..XXX/......X/",".X./.XX/..X/..X/.XX/.X./XX./","X....../XXX..XX/..XXXX./",".XX/.X./XX./X../X../XX./.X./"},
				{"XXXXXX./.XX..XX/",".X/XX/XX/.X/.X/XX/X./","XX..XX./.XXXXXX/",".X/XX/X./X./XX/XX/X./",".XXXXXX/XX..XX./","X./XX/.X/.X/XX/XX/.X/",".XX..XX/XXXXXX./","X./XX/XX/X./X./XX/.X/"},
				{"..XXXX./.XX..XX/XX...../","X../XX./.XX/..X/..X/.XX/.X./",".....XX/XX..XX./.XXXX../",".X./XX./X../X../XX./.XX/..X/",".XXXX../XX..XX./.....XX/",".X./.XX/..X/..X/.XX/XX./X../","XX...../.XX..XX/..XXXX./","..X/.XX/XX./X../X../XX./.X./"},
				{".XXXX./XX..XX/XX..../","XX./XXX/..X/..X/.XX/.X./","....XX/XX..XX/.XXXX./",".X./XX./X../X../XXX/.XX/",".XXXX./XX..XX/....XX/",".X./.XX/..X/..X/XXX/XX./","XX..../XX..XX/.XXXX./",".XX/XXX/X../X../XX./.X./"},
				{"XXXX./XXXXX/..X../",".XX/.XX/XXX/.XX/.X./","..X../XXXXX/.XXXX/",".X./XX./XXX/XX./XX./",".XXXX/XXXXX/..X../",".X./.XX/XXX/.XX/.XX/","..X../XXXXX/XXXX./","XX./XX./XXX/XX./.X./"},
				{"XXXX./XX.XX/.XX../",".XX/XXX/X.X/.XX/.X./","..XX./XX.XX/.XXXX/",".X./XX./X.X/XXX/XX./",".XXXX/XX.XX/..XX./",".X./.XX/X.X/XXX/.XX/",".XX../XX.XX/XXXX./","XX./XXX/X.X/XX./.X./"},
				{".XXXX./.X..XX/XX..../X...../","XX../.XXX/...X/...X/..XX/..X./",".....X/....XX/XX..X./.XXXX./",".X../XX../X.../X.../XXX./..XX/",".XXXX./XX..X./....XX/.....X/","..X./..XX/...X/...X/.XXX/XX../","X...../XX..../.X..XX/.XXXX./","..XX/XXX./X.../X.../XX../.X../"},
				{"XXXX./X..XX/XX.../.X.../",".XXX/XX.X/...X/..XX/..X./","...X./...XX/XX..X/.XXXX/",".X../XX../X.../X.XX/XXX./",".XXXX/XX..X/...XX/...X./","..X./..XX/...X/XX.X/.XXX/",".X.../XX.../X..XX/XXXX./","XXX./X.XX/X.../XX../.X../"},
				{".XXXX./.X..XX/.X..../XX..../","X.../XXXX/...X/...X/..XX/..X./","....XX/....X./XX..X./.XXXX./",".X../XX../X.../X.../XXXX/...X/",".XXXX./XX..X./....X./....XX/","..X./..XX/...X/...X/XXXX/X.../","XX..../.X..../.X..XX/.XXXX./","...X/XXXX/X.../X.../XX../.X../"},
				{"XXXX./X..XX/X..../XX.../","XXXX/X..X/...X/..XX/..X./","...XX/....X/XX..X/.XXXX/",".X../XX../X.../X..X/XXXX/",".XXXX/XX..X/....X/...XX/","..X./..XX/...X/X..X/XXXX/","XX.../X..../X..XX/XXXX./","XXXX/X..X/X.../XX../.X../"},
				{"X....../XXXX.../...XXX./.....XX/","..XX/..X./..X./.XX./.X../XX../X.../","XX...../.XXX.../...XXXX/......X/","...X/..XX/..X./.XX./.X../.X../XX../","......X/...XXXX/.XXX.../XX...../","X.../XX../.X../.XX./..X./..X./..XX/",".....XX/...XXX./XXXX.../X....../","XX../.X../.X../.XX./..X./..XX/...X/"},
				{"XXXX.../X..XXX./.....XX/",".XX/..X/..X/.XX/.X./XX./X../","XX...../.XXX..X/...XXXX/","..X/.XX/.X./XX./X../X../XX./","...XXXX/.XXX..X/XX...../","X../XX./.X./.XX/..X/..X/.XX/",".....XX/X..XXX./XXXX.../","XX./X../X../XX./.X./.XX/..X/"},
				{"XX...../.XXX.../...XXX./.....XX/","...X/..XX/..X./.XX./.X../XX../X.../",".....XX/...XXX./.XXX.../XX...../","X.../XX../.X../.XX./..X./..XX/...X/"},
				{"XX..../XXX.../..XXX./....XX/","..XX/..XX/.XX./.X../XX../X.../","XX..../.XXX../...XXX/....XX/","...X/..XX/..X./.XX./XX../XX../","....XX/...XXX/.XXX../XX..../","X.../XX../.X../.XX./..XX/..XX/","....XX/..XXX./XXX.../XX..../","XX../XX../.XX./..X./..XX/...X/"},
				{".XXX.../XX.XXX./.....XX/",".X./.XX/..X/.XX/.X./XX./X../","XX...../.XXX.XX/...XXX./","..X/.XX/.X./XX./X../XX./.X./","...XXX./.XXX.XX/XX...../","X../XX./.X./.XX/..X/.XX/.X./",".....XX/XX.XXX./.XXX.../",".X./XX./X../XX./.X./.XX/..X/"},
				{"X...../XX..../.XX.../..XXX./....XX/","...XX/..XX./.XX../.X.../XX.../X..../","XX..../.XXX../...XX./....XX/.....X/","....X/...XX/...X./..XX./.XX../XX.../",".....X/....XX/...XX./.XXX../XX..../","X..../XX.../.X.../.XX../..XX./...XX/","....XX/..XXX./.XX.../XX..../X...../","XX.../.XX../..XX./...X./...XX/....X/"},
				{".X.../XX.../XX.../.XXX./...XX/","..XX./.XXXX/.X.../XX.../X..../","XX.../.XXX./...XX/...XX/...X./","....X/...XX/...X./XXXX./.XX../","...X./...XX/...XX/.XXX./XX.../","X..../XX.../.X.../.XXXX/..XX./","...XX/.XXX./XX.../XX.../.X.../",".XX../XXXX./...X./...XX/....X/"},
				{"XX..../.X..../.XX.../..XXX./....XX/","....X/..XXX/.XX../.X.../XX.../X..../","XX..../.XXX../...XX./....X./....XX/","....X/...XX/...X./..XX./XXX../X..../","....XX/....X./...XX./.XXX../XX..../","X..../XX.../.X.../.XX../..XXX/....X/","....XX/..XXX./.XX.../.X..../XX..../","X..../XXX../..XX./...X./...XX/....X/"},
				{"XX.../X..../XX.../.XXX./...XX/","..XXX/.XX.X/.X.../XX.../X..../","XX.../.XXX./...XX/....X/...XX/","....X/...XX/...X./X.XX./XXX../","...XX/....X/...XX/.XXX./XX.../","X..../XX.../.X.../.XX.X/..XXX/","...XX/.XXX./XX.../X..../XX.../","XXX../X.XX./...X./...XX/....X/"},
				{".XX.../XXXXX./X...XX/","XX./.XX/.XX/.X./XX./X../","XX...X/.XXXXX/...XX./","..X/.XX/.X./XX./XX./.XX/","...XX./.XXXXX/XX...X/","X../XX./.X./.XX/.XX/XX./","X...XX/XXXXX./.XX.../",".XX/XX./XX./.X./.XX/..X/"},
				{".XX.../.XXXX./XX..XX/","X../XXX/.XX/.X./XX./X../","XX..XX/.XXXX./...XX./","..X/.XX/.X./XX./XXX/..X/","...XX./.XXXX./XX..XX/","X../XX./.X./.XX/XXX/X../","XX..XX/.XXXX./.XX.../","..X/XXX/XX./.X./.XX/..X/"},
				{"XX.../XXXX./XX.XX/","XXX/XXX/.X./XX./X../","XX.XX/.XXXX/...XX/","..X/.XX/.X./XXX/XXX/","...XX/.XXXX/XX.XX/","X../XX./.X./XXX/XXX/","XX.XX/XXXX./XX.../","XXX/XXX/.X./.XX/..X/"},
				{"...X/XXXX/XXX./..XX/",".XX./.XX./XXX./X.XX/","XX../.XXX/XXXX/X.../","XX.X/.XXX/.XX./.XX./","X.../XXXX/.XXX/XX../","X.XX/XXX./.XX./.XX./","..XX/XXX./XXXX/...X/",".XX./.XX./.XXX/XX.X/"},
				{".XX./XXX./XXX./..XX/",".XX./.XXX/XXXX/X.../","XX../.XXX/.XXX/.XX./","...X/XXXX/XXX./.XX./",".XX./.XXX/.XXX/XX../","X.../XXXX/.XXX/.XX./","..XX/XXX./XXX./.XX./",".XX./XXX./XXXX/...X/"},
				{"..XX/XXX./XXX./..XX/",".XX./.XX./XXXX/X..X/","XX../.XXX/.XXX/XX../","X..X/XXXX/.XX./.XX./"},
				{"X.../XX../XX../XXX./..XX/",".XXXX/.XXX./XX.../X..../","XX../.XXX/..XX/..XX/...X/","....X/...XX/.XXX./XXXX./","...X/..XX/..XX/.XXX/XX../","X..../XX.../.XXX./.XXXX/","..XX/XXX./XX../XX../X.../","XXXX./.XXX./...XX/....X/"},
				{"..X./.XX./XX../XXX./..XX/",".XX../.XXX./XX.XX/X..../","XX../.XXX/..XX/.XX./.X../","....X/XX.XX/.XXX./..XX./",".X../.XX./..XX/.XXX/XX../","X..../XX.XX/.XXX./.XX../","..XX/XXX./XX../.XX./..X./","..XX./.XXX./XX.XX/....X/"},
				{"XX../.X../XX../XXX./..XX/",".XX.X/.XXXX/XX.../X..../","XX../.XXX/..XX/..X./..XX/","....X/...XX/XXXX./X.XX./","..XX/..X./..XX/.XXX/XX../","X..../XX.../.XXXX/.XX.X/","..XX/XXX./XX../.X../XX../","X.XX./XXXX./...XX/....X/"},
				{".XX./.X../XX../XXX./..XX/",".XX../.XXXX/XX..X/X..../","XX../.XXX/..XX/..X./.XX./","....X/X..XX/XXXX./..XX./",".XX./..X./..XX/.XXX/XX../","X..../XX..X/.XXXX/.XX../","..XX/XXX./XX../.X../.XX./","..XX./XXXX./X..XX/....X/"},
				{"X...../XXX.../..X.../..XXX./....XX/","...XX/...X./.XXX./.X.../XX.../X..../","XX..../.XXX../...X../...XXX/.....X/","....X/...XX/...X./.XXX./.X.../XX.../",".....X/...XXX/...X../.XXX../XX..../","X..../XX.../.X.../.XXX./...X./...XX/","....XX/..XXX./..X.../XXX.../X...../","XX.../.X.../.XXX./...X./...XX/....X/"},
				{"XXX.../X.X.../..XXX./....XX/","..XX/...X/.XXX/.X../XX../X.../","XX..../.XXX../...X.X/...XXX/","...X/..XX/..X./XXX./X.../XX../","...XXX/...X.X/.XXX../XX..../","X.../XX../.X../.XXX/...X/..XX/","....XX/..XXX./X.X.../XXX.../","XX../X.../XXX./..X./..XX/...X/"},
				{"XX..../.XX.../..X.../..XXX./....XX/","....X/...XX/.XXX./.X.../XX.../X..../","XX..../.XXX../...X../...XX./....XX/","....X/...XX/...X./.XXX./XX.../X..../","....XX/...XX./...X../.XXX../XX..../","X..../XX.../.X.../.XXX./...XX/....X/","....XX/..XXX./..X.../.XX.../XX..../","X..../XX.../.XXX./...X./...XX/....X/"},
				{"XX.../XX.../.X.../.XXX./...XX/","...XX/.XXXX/.X.../XX.../X..../","XX.../.XXX./...X./...XX/...XX/","....X/...XX/...X./XXXX./XX.../","...XX/...XX/...X./.XXX./XX.../","X..../XX.../.X.../.XXXX/...XX/","...XX/.XXX./.X.../XX.../XX.../","XX.../XXXX./...X./...XX/....X/"},
				{".XX.../XXX.../..XXX./....XX/","..X./..XX/.XXX/.X../XX../X.../","XX..../.XXX../...XXX/...XX./","...X/..XX/..X./XXX./XX../.X../","...XX./...XXX/.XXX../XX..../","X.../XX../.X../.XXX/..XX/..X./","....XX/..XXX./XXX.../.XX.../",".X../XX../XXX./..X./..XX/...X/"},
				{"..X./XXX./X.../XXX./..XX/",".XXX./.X.X./XX.XX/X..../","XX../.XXX/...X/.XXX/.X../","....X/XX.XX/.X.X./.XXX./",".X../.XXX/...X/.XXX/XX../","X..../XX.XX/.X.X./.XXX./","..XX/XXX./X.../XXX./..X./",".XXX./.X.X./XX.XX/....X/"},
				{"XXX./X.X./XXX./..XX/",".XXX/.X.X/XXXX/X.../","XX../.XXX/.X.X/.XXX/","...X/XXXX/X.X./XXX./",".XXX/.X.X/.XXX/XX../","X.../XXXX/.X.X/.XXX/","..XX/XXX./X.X./XXX./","XXX./X.X./XXXX/...X/"},
				{"XX../XX../X.../XXX./..XX/",".XXXX/.X.XX/XX.../X..../","XX../.XXX/...X/..XX/..XX/","....X/...XX/XX.X./XXXX./","..XX/..XX/...X/.XXX/XX../","X..../XX.../.X.XX/.XXXX/","..XX/XXX./X.../XX../XX../","XXXX./XX.X./...XX/....X/"},
				{".XX./XX../X.../XXX./..XX/",".XXX./.X.XX/XX..X/X..../","XX../.XXX/...X/..XX/.XX./","....X/X..XX/XX.X./.XXX./",".XX./..XX/...X/.XXX/XX../","X..../XX..X/.X.XX/.XXX./","..XX/XXX./X.../XX../.XX./",".XXX./XX.X./X..XX/....X/"},
				{"XX../XXX./XXX./..XX/",".XXX/.XXX/XXX./X.../","XX../.XXX/.XXX/..XX/","...X/.XXX/XXX./XXX./","..XX/.XXX/.XXX/XX../","X.../XXX./.XXX/.XXX/","..XX/XXX./XXX./XX../","XXX./XXX./.XXX/...X/"},
				{"XX.../.X.../.X.../.X.../.XXX./...XX/",".....X/.XXXXX/.X..../XX..../X...../","XX.../.XXX./...X./...X./...X./...XX/",".....X/....XX/....X./XXXXX./X...../","...XX/...X./...X./...X./.XXX./XX.../","X...../XX..../.X..../.XXXXX/.....X/","...XX/.XXX./.X.../.X.../.X.../XX.../","X...../XXXXX./....X./....XX/.....X/"},
				{"XX../X.../X.../X.../XXX./..XX/",".XXXXX/.X...X/XX..../X...../","XX../.XXX/...X/...X/...X/..XX/",".....X/....XX/X...X./XXXXX./","..XX/...X/...X/...X/.XXX/XX../","X...../XX..../.X...X/.XXXXX/","..XX/XXX./X.../X.../X.../XX../","XXXXX./X...X./....XX/.....X/"},
				{"X..XXX./XXXX.XX/","XX/X./X./XX/.X/XX/X./","XX.XXXX/.XXX..X/",".X/XX/X./XX/.X/.X/XX/",".XXX..X/XX.XXXX/","X./XX/.X/XX/X./X./XX/","XXXX.XX/X..XXX./","XX/.X/.X/XX/X./XX/.X/"},
				{"...XXX./XXXX.XX/X....../","XX./.X./.X./.XX/..X/.XX/.X./","......X/XX.XXXX/.XXX.../",".X./XX./X../XX./.X./.X./.XX/",".XXX.../XX.XXXX/......X/",".X./.XX/..X/.XX/.X./.X./XX./","X....../XXXX.XX/...XXX./",".XX/.X./.X./XX./X../XX./.X./"},
				{"XX.XXX./.XXX.XX/",".X/XX/X./XX/.X/XX/X./",".XXX.XX/XX.XXX./","X./XX/.X/XX/X./XX/.X/"},
				{"..XXX./XXX.XX/XX..../","XX./XX./.XX/..X/.XX/.X./","....XX/XX.XXX/.XXX../",".X./XX./X../XX./.XX/.XX/",".XXX../XX.XXX/....XX/",".X./.XX/..X/.XX/XX./XX./","XX..../XXX.XX/..XXX./",".XX/.XX/XX./X../XX./.X./"},
				{"X...../XXXXX./.XX.XX/",".XX/XX./XX./.X./XX./X../","XX.XX./.XXXXX/.....X/","..X/.XX/.X./.XX/.XX/XX./",".....X/.XXXXX/XX.XX./","X../XX./.X./XX./XX./.XX/",".XX.XX/XXXXX./X...../","XX./.XX/.XX/.X./.XX/..X/"},
				{"XX..../.XXXX./.XX.XX/","..X/XXX/XX./.X./XX./X../","XX.XX./.XXXX./....XX/","..X/.XX/.X./.XX/XXX/X../","....XX/.XXXX./XX.XX./","X../XX./.X./XX./XXX/..X/",".XX.XX/.XXXX./XX..../","X../XXX/.XX/.X./.XX/..X/"},
				{"..XXX./.XX.XX/XX..../X...../","XX../.XX./..XX/...X/..XX/..X./",".....X/....XX/XX.XX./.XXX../",".X../XX../X.../XX../.XX./..XX/",".XXX../XX.XX./....XX/.....X/","..X./..XX/...X/..XX/.XX./XX../","X...../XX..../.XX.XX/..XXX./","..XX/.XX./XX../X.../XX../.X../"},
				{".XXX./XX.XX/XX.../.X.../",".XX./XXXX/...X/..XX/..X./","...X./...XX/XX.XX/.XXX./",".X../XX../X.../XXXX/.XX./",".XXX./XX.XX/...XX/...X./","..X./..XX/...X/XXXX/.XX./",".X.../XX.../XX.XX/.XXX./",".XX./XXXX/X.../XX../.X../"},
				{"..XXX./.XX.XX/.X..../XX..../","X.../XXX./..XX/...X/..XX/..X./","....XX/....X./XX.XX./.XXX../",".X../XX../X.../XX../.XXX/...X/",".XXX../XX.XX./....X./....XX/","..X./..XX/...X/..XX/XXX./X.../","XX..../.X..../.XX.XX/..XXX./","...X/.XXX/XX../X.../XX../.X../"},
				{".XXX./XX.XX/X..../XX.../","XXX./X.XX/...X/..XX/..X./","...XX/....X/XX.XX/.XXX./",".X../XX../X.../XX.X/.XXX/",".XXX./XX.XX/....X/...XX/","..X./..XX/...X/X.XX/XXX./","XX.../X..../XX.XX/.XXX./",".XXX/XX.X/X.../XX../.X../"},
				{"XXX./XXXX/XX../X.../","XXXX/.XXX/..XX/..X./","...X/..XX/XXXX/.XXX/",".X../XX../XXX./XXXX/",".XXX/XXXX/..XX/...X/","..X./..XX/.XXX/XXXX/","X.../XX../XXXX/XXX./","XXXX/XXX./XX../.X../"},
				{"XXX./XXXX/.XX./..X./","..XX/.XXX/XXXX/..X./",".X../.XX./XXXX/.XXX/",".X../XXXX/XXX./XX../",".XXX/XXXX/.XX./.X../","..X./XXXX/.XXX/..XX/","..X./.XX./XXXX/XXX./","XX../XXX./XXXX/.X../"},
				{"XXX./XXXX/.X../XX../","X.XX/XXXX/..XX/..X./","..XX/..X./XXXX/.XXX/",".X../XX../XXXX/XX.X/",".XXX/XXXX/..X./..XX/","..X./..XX/XXXX/X.XX/","XX../.X../XXXX/XXX./","XX.X/XXXX/XX../.X../"},
				{"XXX./XXXX/.X../.XX./","..XX/XXXX/X.XX/..X./",".XX./..X./XXXX/.XXX/",".X../XX.X/XXXX/XX../",".XXX/XXXX/..X./.XX./","..X./X.XX/XXXX/..XX/",".XX./.X../XXXX/XXX./","XX../XXXX/XX.X/.X../"},
				{"..XXX./X.X.XX/XXX.../","XX./X../XXX/..X/.XX/.X./","...XXX/XX.X.X/.XXX../",".X./XX./X../XXX/..X/.XX/",".XXX../XX.X.X/...XXX/",".X./.XX/..X/XXX/X../XX./","XXX.../X.X.XX/..XXX./",".XX/..X/XXX/X../XX./.X./"},
				{"..XXX./..X.XX/XXX.../X...../","XX../.X../.XXX/...X/..XX/..X./",".....X/...XXX/XX.X../.XXX../",".X../XX../X.../XXX./..X./..XX/",".XXX../XX.X../...XXX/.....X/","..X./..XX/...X/.XXX/.X../XX../","X...../XXX.../..X.XX/..XXX./","..XX/..X./XXX./X.../XX../.X../"},
				{"..XXX./XXX.XX/.XX.../",".X./XX./XXX/..X/.XX/.X./","...XX./XX.XXX/.XXX../",".X./XX./X../XXX/.XX/.X./",".XXX../XX.XXX/...XX./",".X./.XX/..X/XXX/XX./.X./",".XX.../XXX.XX/..XXX./",".X./.XX/XXX/X../XX./.X./"},
				{"..XXX./..X.XX/.XX.../XX..../","X.../XX../.XXX/...X/..XX/..X./","....XX/...XX./XX.X../.XXX../",".X../XX../X.../XXX./..XX/...X/",".XXX../XX.X../...XX./....XX/","..X./..XX/...X/.XXX/XX../X.../","XX..../.XX.../..X.XX/..XXX./","...X/..XX/XXX./X.../XX../.X../"},
				{".XXX./.X.XX/XX.../XX.../","XX../XXXX/...X/..XX/..X./","...XX/...XX/XX.X./.XXX./",".X../XX../X.../XXXX/..XX/",".XXX./XX.X./...XX/...XX/","..X./..XX/...X/XXXX/XX../","XX.../XX.../.X.XX/.XXX./","..XX/XXXX/X.../XX../.X../"},
				{"XXX./X.XX/XXX./..X./",".XXX/.X.X/XXXX/..X./",".X../.XXX/XX.X/.XXX/",".X../XXXX/X.X./XXX./",".XXX/XX.X/.XXX/.X../","..X./XXXX/.X.X/.XXX/","..X./XXX./X.XX/XXX./","XXX./X.X./XXXX/.X../"},
				{"XXX./X.XX/XX../XX../","XXXX/XX.X/..XX/..X./","..XX/..XX/XX.X/.XXX/",".X../XX../X.XX/XXXX/",".XXX/XX.X/..XX/..XX/","..X./..XX/XX.X/XXXX/","XX../XX../X.XX/XXX./","XXXX/X.XX/XX../.X../"},
				{"XXX./X.XX/XX../.XX./",".XXX/XX.X/X.XX/..X./",".XX./..XX/XX.X/.XXX/",".X../XX.X/X.XX/XXX./",".XXX/XX.X/..XX/.XX./","..X./X.XX/XX.X/.XXX/",".XX./XX../X.XX/XXX./","XXX./X.XX/XX.X/.X../"},
				{".XXX./.X.XX/.X.../.X.../XX.../","X..../XXXXX/....X/...XX/...X./","...XX/...X./...X./XX.X./.XXX./",".X.../XX.../X..../XXXXX/....X/",".XXX./XX.X./...X./...X./...XX/","...X./...XX/....X/XXXXX/X..../","XX.../.X.../.X.../.X.XX/.XXX./","....X/XXXXX/X..../XX.../.X.../"},
				{"XXX./X.XX/X.../X.../XX../","XXXXX/X...X/...XX/...X./","..XX/...X/...X/XX.X/.XXX/",".X.../XX.../X...X/XXXXX/",".XXX/XX.X/...X/...X/..XX/","...X./...XX/X...X/XXXXX/","XX../X.../X.../X.XX/XXX./","XXXXX/X...X/XX.../.X.../"},
				{"X....../XXXXX../....XX./.....XX/","..XX/..X./..X./..X./.XX./XX../X.../","XX...../.XX..../..XXXXX/......X/","...X/..XX/.XX./.X../.X../.X../XX../","......X/..XXXXX/.XX..../XX...../","X.../XX../.XX./..X./..X./..X./..XX/",".....XX/....XX./XXXXX../X....../","XX../.X../.X../.X../.XX./..XX/...X/"},
				{"XXXXX../X...XX./.....XX/",".XX/..X/..X/..X/.XX/XX./X../","XX...../.XX...X/..XXXXX/","..X/.XX/XX./X../X../X../XX./","..XXXXX/.XX...X/XX...../","X../XX./.XX/..X/..X/..X/.XX/",".....XX/X...XX./XXXXX../","XX./X../X../X../XX./.XX/..X/"},
				{"XX..../XXXX../...XX./....XX/","..XX/..XX/..X./.XX./XX../X.../","XX..../.XX.../..XXXX/....XX/","...X/..XX/.XX./.X../XX../XX../","....XX/..XXXX/.XX.../XX..../","X.../XX../.XX./..X./..XX/..XX/","....XX/...XX./XXXX../XX..../","XX../XX../.X../.XX./..XX/...X/"},
				{"XXXX../XX.XX./....XX/",".XX/.XX/..X/.XX/XX./X../","XX..../.XX.XX/..XXXX/","..X/.XX/XX./X../XX./XX./","..XXXX/.XX.XX/XX..../","X../XX./.XX/..X/.XX/.XX/","....XX/XX.XX./XXXX../","XX./XX./X../XX./.XX/..X/"},
				{"X...../XX..../.XXX../...XX./....XX/","...XX/..XX./..X../.XX../XX.../X..../","XX..../.XX.../..XXX./....XX/.....X/","....X/...XX/..XX./..X../.XX../XX.../",".....X/....XX/..XXX./.XX.../XX..../","X..../XX.../.XX../..X../..XX./...XX/","....XX/...XX./.XXX../XX..../X...../","XX.../.XX../..X../..XX./...XX/....X/"},
				{".X.../XX.../XXX../..XX./...XX/","..XX./..XXX/.XX../XX.../X..../","XX.../.XX../..XXX/...XX/...X./","....X/...XX/..XX./XXX../.XX../","...X./...XX/..XXX/.XX../XX.../","X..../XX.../.XX../..XXX/..XX./","...XX/..XX./XXX../XX.../.X.../",".XX../XXX../..XX./...XX/....X/"},
				{"XX..../.X..../.XXX../...XX./....XX/","....X/..XXX/..X../.XX../XX.../X..../","XX..../.XX.../..XXX./....X./....XX/","....X/...XX/..XX./..X../XXX../X..../","....XX/....X./..XXX./.XX.../XX..../","X..../XX.../.XX../..X../..XXX/....X/","....XX/...XX./.XXX../.X..../XX..../","X..../XXX../..X../..XX./...XX/....X/"},
				{"XX.../X..../XXX../..XX./...XX/","..XXX/..X.X/.XX../XX.../X..../","XX.../.XX../..XXX/....X/...XX/","....X/...XX/..XX./X.X../XXX../","...XX/....X/..XXX/.XX../XX.../","X..../XX.../.XX../..X.X/..XXX/","...XX/..XX./XXX../X..../XX.../","XXX../X.X../..XX./...XX/....X/"},
				{".XXX../XX.XX./X...XX/","XX./.XX/..X/.XX/XX./X../","XX...X/.XX.XX/..XXX./","..X/.XX/XX./X../XX./.XX/","..XXX./.XX.XX/XX...X/","X../XX./.XX/..X/.XX/XX./","X...XX/XX.XX./.XXX../",".XX/XX./X../XX./.XX/..X/"},
				{"XXX../XXXX./.X.XX/",".XX/XXX/.XX/XX./X../","XX.X./.XXXX/..XXX/","..X/.XX/XX./XXX/XX./","..XXX/.XXXX/XX.X./","X../XX./.XX/XXX/.XX/",".X.XX/XXXX./XXX../","XX./XXX/XX./.XX/..X/"},
				{".XXX../.X.XX./XX..XX/","X../XXX/..X/.XX/XX./X../","XX..XX/.XX.X./..XXX./","..X/.XX/XX./X../XXX/..X/","..XXX./.XX.X./XX..XX/","X../XX./.XX/..X/XXX/X../","XX..XX/.X.XX./.XXX../","..X/XXX/X../XX./.XX/..X/"},
				{"XXX../X.XX./XX.XX/","XXX/X.X/.XX/XX./X../","XX.XX/.XX.X/..XXX/","..X/.XX/XX./X.X/XXX/","..XXX/.XX.X/XX.XX/","X../XX./.XX/X.X/XXX/","XX.XX/X.XX./XXX../","XXX/X.X/XX./.XX/..X/"},
				{"X...../XXX.../..XX../...XX./....XX/","...XX/...X./..XX./.XX../XX.../X..../","XX..../.XX.../..XX../...XXX/.....X/","....X/...XX/..XX./.XX../.X.../XX.../",".....X/...XXX/..XX../.XX.../XX..../","X..../XX.../.XX../..XX./...X./...XX/","....XX/...XX./..XX../XXX.../X...../","XX.../.X.../.XX../..XX./...XX/....X/"},
				{"XXX.../X.XX../...XX./....XX/","..XX/...X/..XX/.XX./XX../X.../","XX..../.XX.../..XX.X/...XXX/","...X/..XX/.XX./XX../X.../XX../","...XXX/..XX.X/.XX.../XX..../","X.../XX../.XX./..XX/...X/..XX/","....XX/...XX./X.XX../XXX.../","XX../X.../XX../.XX./..XX/...X/"},
				{"XX..../.XX.../..XX../...XX./....XX/","....X/...XX/..XX./.XX../XX.../X..../","....XX/...XX./..XX../.XX.../XX..../","X..../XX.../.XX../..XX./...XX/....X/"},
				{"XX.../XX.../.XX../..XX./...XX/","...XX/..XXX/.XX../XX.../X..../","XX.../.XX../..XX./...XX/...XX/","....X/...XX/..XX./XXX../XX.../","...XX/...XX/..XX./.XX../XX.../","X..../XX.../.XX../..XXX/...XX/","...XX/..XX./.XX../XX.../XX.../","XX.../XXX../..XX./...XX/....X/"},
				{".XX.../XXXX../...XX./....XX/","..X./..XX/..XX/.XX./XX../X.../","XX..../.XX.../..XXXX/...XX./","...X/..XX/.XX./XX../XX../.X../","...XX./..XXXX/.XX.../XX..../","X.../XX../.XX./..XX/..XX/..X./","....XX/...XX./XXXX../.XX.../",".X../XX../XX../.XX./..XX/...X/"},
				{"..X./XXX./XX../.XX./..XX/","..XX./.XXX./XX.XX/X..../","XX../.XX./..XX/.XXX/.X../","....X/XX.XX/.XXX./.XX../",".X../.XXX/..XX/.XX./XX../","X..../XX.XX/.XXX./..XX./","..XX/.XX./XX../XXX./..X./",".XX../.XXX./XX.XX/....X/"},
				{"XXX./XXX./.XX./..XX/","..XX/.XXX/XXXX/X.../","XX../.XX./.XXX/.XXX/","...X/XXXX/XXX./XX../",".XXX/.XXX/.XX./XX../","X.../XXXX/.XXX/..XX/","..XX/.XX./XXX./XXX./","XX../XXX./XXXX/...X/"},
				{".XX./XX../XX../.XX./..XX/","..XX./.XXXX/XX..X/X..../","XX../.XX./..XX/..XX/.XX./","....X/X..XX/XXXX./.XX../",".XX./..XX/..XX/.XX./XX../","X..../XX..X/.XXXX/..XX./","..XX/.XX./XX../XX../.XX./",".XX../XXXX./X..XX/....X/"},
				{"XX.../.X.../.X.../.XX../..XX./...XX/",".....X/..XXXX/.XX.../XX..../X...../","XX.../.XX../..XX./...X./...X./...XX/",".....X/....XX/...XX./XXXX../X...../","...XX/...X./...X./..XX./.XX../XX.../","X...../XX..../.XX.../..XXXX/.....X/","...XX/..XX./.XX../.X.../.X.../XX.../","X...../XXXX../...XX./....XX/.....X/"},
				{"XX../X.../X.../XX../.XX./..XX/","..XXXX/.XX..X/XX..../X...../","XX../.XX./..XX/...X/...X/..XX/",".....X/....XX/X..XX./XXXX../","..XX/...X/...X/..XX/.XX./XX../","X...../XX..../.XX..X/..XXXX/","..XX/.XX./XX../X.../X.../XX../","XXXX../X..XX./....XX/.....X/"},
				{"X.XX../XXXXX./....XX/",".XX/.X./.XX/.XX/XX./X../","XX..../.XXXXX/..XX.X/","..X/.XX/XX./XX./.X./XX./","..XX.X/.XXXXX/XX..../","X../XX./.XX/.XX/.X./.XX/","....XX/XXXXX./X.XX../","XX./.X./XX./XX./.XX/..X/"},
				{"..XX../XXXXX./X...XX/","XX./.X./.XX/.XX/XX./X../","XX...X/.XXXXX/..XX../","..X/.XX/XX./XX./.X./.XX/","..XX../.XXXXX/XX...X/","X../XX./.XX/.XX/.X./XX./","X...XX/XXXXX./..XX../",".XX/.X./XX./XX./.XX/..X/"},
				{"XXXX../.XXXX./....XX/","..X/.XX/.XX/.XX/XX./X../","XX..../.XXXX./..XXXX/","..X/.XX/XX./XX./XX./X../","..XXXX/.XXXX./XX..../","X../XX./.XX/.XX/.XX/..X/","....XX/.XXXX./XXXX../","X../XX./XX./XX./.XX/..X/"},
				{"..XX../.XXXX./XX..XX/","X../XX./.XX/.XX/XX./X../","XX..XX/.XXXX./..XX../","..X/.XX/XX./XX./.XX/..X/"},
				{".XX../XXXX./XX.XX/","XX./XXX/.XX/XX./X../","XX.XX/.XXXX/..XX./","..X/.XX/XX./XXX/.XX/","..XX./.XXXX/XX.XX/","X../XX./.XX/XXX/XX./","XX.XX/XXXX./.XX../",".XX/XXX/XX./.XX/..X/"},
				{".XX../.XXX./.X.XX/XX.../","X.../XXXX/..XX/.XX./.X../","...XX/XX.X./.XXX./..XX./","..X./.XX./XX../XXXX/...X/","..XX./.XXX./XX.X./...XX/",".X../.XX./..XX/XXXX/X.../","XX.../.X.XX/.XXX./.XX../","...X/XXXX/XX../.XX./..X./"},
				{"XX../XXX./X.XX/XX../","XXXX/X.XX/.XX./.X../","..XX/XX.X/.XXX/..XX/","..X./.XX./XX.X/XXXX/","..XX/.XXX/XX.X/..XX/",".X../.XX./X.XX/XXXX/","XX../X.XX/XXX./XX../","XXXX/XX.X/.XX./..X./"},
				{"....X/XXXXX/XX.../.XX../",".XX./XXX./X.X./..X./..XX/","..XX./...XX/XXXXX/X..../","XX../.X../.X.X/.XXX/.XX./","X..../XXXXX/...XX/..XX./","..XX/..X./X.X./XXX./.XX./",".XX../XX.../XXXXX/....X/",".XX./.XXX/.X.X/.X../XX../"},
				{"XXXXX/XX..X/.XX../",".XX/XXX/X.X/..X/.XX/","..XX./X..XX/XXXXX/","XX./X../X.X/XXX/XX./","XXXXX/X..XX/..XX./",".XX/..X/X.X/XXX/.XX/",".XX../XX..X/XXXXX/","XX./XXX/X.X/X../XX./"},
				{"..XX/XXXX/XX../.XX./",".XX./XXX./X.XX/..XX/",".XX./..XX/XXXX/XX../","XX../XX.X/.XXX/.XX./","XX../XXXX/..XX/.XX./","..XX/X.XX/XXX./.XX./",".XX./XX../XXXX/..XX/",".XX./.XXX/XX.X/XX../"},
				{".X./.XX/XXX/XX./.XX/",".XX../XXXXX/X.XX./","XX./.XX/XXX/XX./.X./",".XX.X/XXXXX/..XX./",".X./XX./XXX/.XX/XX./","X.XX./XXXXX/.XX../",".XX/XX./XXX/.XX/.X./","..XX./XXXXX/.XX.X/"},
				{"...X/..XX/XXX./XX../.XX./",".XX../XXX../X.XX./...XX/",".XX./..XX/.XXX/XX../X.../","XX.../.XX.X/..XXX/..XX./","X.../XX../.XXX/..XX/.XX./","...XX/X.XX./XXX../.XX../",".XX./XX../XXX./..XX/...X/","..XX./..XXX/.XX.X/XX.../"},
				{".XX/..X/XXX/XX./.XX/",".XX../XXX.X/X.XXX/","XX./.XX/XXX/X../XX./","XXX.X/X.XXX/..XX./","XX./X../XXX/.XX/XX./","X.XXX/XXX.X/.XX../",".XX/XX./XXX/..X/.XX/","..XX./X.XXX/XXX.X/"},
				{"..XX/..X./XXX./XX../.XX./",".XX../XXX../X.XXX/....X/",".XX./..XX/.XXX/.X../XX../","X..../XXX.X/..XXX/..XX./","XX../.X../.XXX/..XX/.XX./","....X/X.XXX/XXX../.XX../",".XX./XX../XXX./..X./..XX/","..XX./..XXX/XXX.X/X..../"},
				{"X.../XXX./.XX./.XX./..XX/","...XX/.XXX./XXXX./X..../","XX../.XX./.XX./.XXX/...X/","....X/.XXXX/.XXX./XX.../","...X/.XXX/.XX./.XX./XX../","X..../XXXX./.XXX./...XX/","..XX/.XX./.XX./XXX./X.../","XX.../.XXX./.XXXX/....X/"},
				{"XX../.XX./.XX./.XX./..XX/","....X/.XXXX/XXXX./X..../","..XX/.XX./.XX./.XX./XX../","X..../XXXX./.XXXX/....X/"},
				{"...X/.XXX/XX../XX../.XX./",".XX../XXXX./X..X./...XX/",".XX./..XX/..XX/XXX./X.../","XX.../.X..X/.XXXX/..XX./","X.../XXX./..XX/..XX/.XX./","...XX/X..X./XXXX./.XX../",".XX./XX../XX../.XXX/...X/","..XX./.XXXX/.X..X/XX.../"},
				{".XXX/XX.X/XX../.XX./",".XX./XXXX/X..X/..XX/",".XX./..XX/X.XX/XXX./","XX../X..X/XXXX/.XX./","XXX./X.XX/..XX/.XX./","..XX/X..X/XXXX/.XX./",".XX./XX../XX.X/.XXX/",".XX./XXXX/X..X/XX../"},
				{".XX/.XX/XX./XX./.XX/",".XX../XXXXX/X..XX/","XX./.XX/.XX/XX./XX./","XX..X/XXXXX/..XX./","XX./XX./.XX/.XX/XX./","X..XX/XXXXX/.XX../",".XX/XX./XX./.XX/.XX/","..XX./XXXXX/XX..X/"},
				{".XX./XXXX/XX../.XX./",".XX./XXXX/X.XX/..X./",".XX./..XX/XXXX/.XX./",".X../XX.X/XXXX/.XX./",".XX./XXXX/..XX/.XX./","..X./X.XX/XXXX/.XX./",".XX./XX../XXXX/.XX./",".XX./XXXX/XX.X/.X../"},
				{"XX./.X./.X./XX./XX./.XX/",".XX..X/XXXXXX/X...../","XX./.XX/.XX/.X./.X./.XX/",".....X/XXXXXX/X..XX./",".XX/.X./.X./.XX/.XX/XX./","X...../XXXXXX/.XX..X/",".XX/XX./XX./.X./.X./XX./","X..XX./XXXXXX/.....X/"},
				{".XX/.X./.X./XX./XX./.XX/",".XX.../XXXXXX/X....X/","XX./.XX/.XX/.X./.X./XX./","X....X/XXXXXX/...XX./","XX./.X./.X./.XX/.XX/XX./","X....X/XXXXXX/.XX.../",".XX/XX./XX./.X./.X./.XX/","...XX./XXXXXX/X....X/"},
				{"X...../XXXX../...X../...XX./....XX/","...XX/...X./...X./.XXX./XX.../X..../","XX..../.XX.../..X.../..XXXX/.....X/","....X/...XX/.XXX./.X.../.X.../XX.../",".....X/..XXXX/..X.../.XX.../XX..../","X..../XX.../.XXX./...X./...X./...XX/","....XX/...XX./...X../XXXX../X...../","XX.../.X.../.X.../.XXX./...XX/....X/"},
				{"XXXX../X..X../...XX./....XX/","..XX/...X/...X/.XXX/XX../X.../","XX..../.XX.../..X..X/..XXXX/","...X/..XX/XXX./X.../X.../XX../","..XXXX/..X..X/.XX.../XX..../","X.../XX../.XXX/...X/...X/..XX/","....XX/...XX./X..X../XXXX../","XX../X.../X.../XXX./..XX/...X/"},
				{"XX.../XXX../..X../..XX./...XX/","...XX/...XX/.XXX./XX.../X..../","XX.../.XX../..X../..XXX/...XX/","....X/...XX/.XXX./XX.../XX.../","...XX/..XXX/..X../.XX../XX.../","X..../XX.../.XXX./...XX/...XX/","...XX/..XX./..X../XXX../XX.../","XX.../XX.../.XXX./...XX/....X/"},
				{".X../XX../XX../.X../.XX./..XX/","...XX./.XXXXX/XX..../X...../","XX../.XX./..X./..XX/..XX/..X./",".....X/....XX/XXXXX./.XX.../","..X./..XX/..XX/..X./.XX./XX../","X...../XX..../.XXXXX/...XX./","..XX/.XX./.X../XX../XX../.X../",".XX.../XXXXX./....XX/.....X/"},
				{"XX.../.X.../.XX../..X../..XX./...XX/",".....X/...XXX/.XXX../XX..../X...../","XX.../.XX../..X../..XX./...X./...XX/",".....X/....XX/..XXX./XXX.../X...../","...XX/...X./..XX./..X../.XX../XX.../","X...../XX..../.XXX../...XXX/.....X/","...XX/..XX./..X../.XX../.X.../XX.../","X...../XXX.../..XXX./....XX/.....X/"},
				{"XX../X.../XX../.X../.XX./..XX/","...XXX/.XXX.X/XX..../X...../","XX../.XX./..X./..XX/...X/..XX/",".....X/....XX/X.XXX./XXX.../","..XX/...X/..XX/..X./.XX./XX../","X...../XX..../.XXX.X/...XXX/","..XX/.XX./.X../XX../X.../XX../","XXX.../X.XXX./....XX/.....X/"},
				{".XX../.XX../XXXX./...XX/",".X../.XXX/.XXX/XX../X.../","XX.../.XXXX/..XX./..XX./","...X/..XX/XXX./XXX./..X./","..XX./..XX./.XXXX/XX.../","X.../XX../.XXX/.XXX/.X../","...XX/XXXX./.XX../.XX../","..X./XXX./XXX./..XX/...X/"},
				{"...X/XXXX/X.../XX../.XX./",".XXX./XX.X./X..X./...XX/",".XX./..XX/...X/XXXX/X.../","XX.../.X..X/.X.XX/.XXX./","X.../XXXX/...X/..XX/.XX./","...XX/X..X./XX.X./.XXX./",".XX./XX../X.../XXXX/...X/",".XXX./.X.XX/.X..X/XX.../"},
				{"XXXX/X..X/XX../.XX./",".XXX/XX.X/X..X/..XX/",".XX./..XX/X..X/XXXX/","XX../X..X/X.XX/XXX./","XXXX/X..X/..XX/.XX./","..XX/X..X/XX.X/.XXX/",".XX./XX../X..X/XXXX/","XXX./X.XX/X..X/XX../"},
				{".XX/XXX/X../XX./.XX/",".XXX./XX.XX/X..XX/","XX./.XX/..X/XXX/XX./","XX..X/XX.XX/.XXX./","XX./XXX/..X/.XX/XX./","X..XX/XX.XX/.XXX./",".XX/XX./X../XXX/.XX/",".XXX./XX.XX/XX..X/"},
				{"X../XX./XX./X../XX./.XX/",".XXXXX/XX.XX./X...../","XX./.XX/..X/.XX/.XX/..X/",".....X/.XX.XX/XXXXX./","..X/.XX/.XX/..X/.XX/XX./","X...../XX.XX./.XXXXX/",".XX/XX./X../XX./XX./X../","XXXXX./.XX.XX/.....X/"},
				{"XX./.X./XX./X../XX./.XX/",".XXX.X/XX.XXX/X...../","XX./.XX/..X/.XX/.X./.XX/",".....X/XXX.XX/X.XXX./",".XX/.X./.XX/..X/.XX/XX./","X...../XX.XXX/.XXX.X/",".XX/XX./X../XX./.X./XX./","X.XXX./XXX.XX/.....X/"},
				{".XX/.X./XX./X../XX./.XX/",".XXX../XX.XXX/X....X/","XX./.XX/..X/.XX/.X./XX./","X....X/XXX.XX/..XXX./","XX./.X./.XX/..X/.XX/XX./","X....X/XX.XXX/.XXX../",".XX/XX./X../XX./.X./.XX/","..XXX./XXX.XX/X....X/"},
				{"X..../XXX../..X../..X../..XX./...XX/","....XX/....X./.XXXX./XX..../X...../","XX.../.XX../..X../..X../..XXX/....X/",".....X/....XX/.XXXX./.X..../XX..../","....X/..XXX/..X../..X../.XX../XX.../","X...../XX..../.XXXX./....X./....XX/","...XX/..XX./..X../..X../XXX../X..../","XX..../.X..../.XXXX./....XX/.....X/"},
				{"XXX../X.X../..X../..XX./...XX/","...XX/....X/.XXXX/XX.../X..../","XX.../.XX../..X../..X.X/..XXX/","....X/...XX/XXXX./X..../XX.../","..XXX/..X.X/..X../.XX../XX.../","X..../XX.../.XXXX/....X/...XX/","...XX/..XX./..X../X.X../XXX../","XX.../X..../XXXX./...XX/....X/"},
				{"XX.../.XX../..X../..X../..XX./...XX/",".....X/....XX/.XXXX./XX..../X...../","...XX/..XX./..X../..X../.XX../XX.../","X...../XX..../.XXXX./....XX/.....X/"},
				{"XX../XX../.X../.X../.XX./..XX/","....XX/.XXXXX/XX..../X...../","XX../.XX./..X./..X./..XX/..XX/",".....X/....XX/XXXXX./XX..../","..XX/..XX/..X./..X./.XX./XX../","X...../XX..../.XXXXX/....XX/","..XX/.XX./.X../.X../XX../XX../","XX..../XXXXX./....XX/.....X/"},
				{".XX../XXX../..X../..XX./...XX/","...X./...XX/.XXXX/XX.../X..../","XX.../.XX../..X../..XXX/..XX./","....X/...XX/XXXX./XX.../.X.../","..XX./..XXX/..X../.XX../XX.../","X..../XX.../.XXXX/...XX/...X./","...XX/..XX./..X../XXX../.XX../",".X.../XX.../XXXX./...XX/....X/"},
				{"..X/XXX/X../X../XX./.XX/",".XXXX./XX..X./X...XX/","XX./.XX/..X/..X/XXX/X../","XX...X/.X..XX/.XXXX./","X../XXX/..X/..X/.XX/XX./","X...XX/XX..X./.XXXX./",".XX/XX./X../X../XXX/..X/",".XXXX./.X..XX/XX...X/"},
				{"XXX/X.X/X../XX./.XX/",".XXXX/XX..X/X..XX/","XX./.XX/..X/X.X/XXX/","XX..X/X..XX/XXXX./","XXX/X.X/..X/.XX/XX./","X..XX/XX..X/.XXXX/",".XX/XX./X../X.X/XXX/","XXXX./X..XX/XX..X/"},
				{"XX./XX./X../X../XX./.XX/",".XXXXX/XX..XX/X...../","XX./.XX/..X/..X/.XX/.XX/",".....X/XX..XX/XXXXX./",".XX/.XX/..X/..X/.XX/XX./","X...../XX..XX/.XXXXX/",".XX/XX./X../X../XX./XX./","XXXXX./XX..XX/.....X/"},
				{".XX/XX./X../X../XX./.XX/",".XXXX./XX..XX/X....X/","XX./.XX/..X/..X/.XX/XX./","X....X/XX..XX/.XXXX./"},
				{"XX./XXX/X../XX./.XX/",".XXXX/XX.XX/X..X./","XX./.XX/..X/XXX/.XX/",".X..X/XX.XX/XXXX./",".XX/XXX/..X/.XX/XX./","X..X./XX.XX/.XXXX/",".XX/XX./X../XXX/XX./","XXXX./XX.XX/.X..X/"},
				{"XX../.X../.X../.X../.X../.XX./..XX/","......X/.XXXXXX/XX...../X....../","XX../.XX./..X./..X./..X./..X./..XX/","......X/.....XX/XXXXXX./X....../","..XX/..X./..X./..X./..X./.XX./XX../","X....../XX...../.XXXXXX/......X/","..XX/.XX./.X../.X../.X../.X../XX../","X....../XXXXXX./.....XX/......X/"},
				{"XX./X../X../X../X../XX./.XX/",".XXXXXX/XX....X/X....../","XX./.XX/..X/..X/..X/..X/.XX/","......X/X....XX/XXXXXX./",".XX/..X/..X/..X/..X/.XX/XX./","X....../XX....X/.XXXXXX/",".XX/XX./X../X../X../X../XX./","XXXXXX./X....XX/......X/"},
				{"X...XX./XXXXXXX/","XX/X./X./X./XX/XX/X./","XXXXXXX/.XX...X/",".X/XX/XX/.X/.X/.X/XX/",".XX...X/XXXXXXX/","X./XX/XX/X./X./X./XX/","XXXXXXX/X...XX./","XX/.X/.X/.X/XX/XX/.X/"},
				{"....XX./XXXXXXX/X....../","XX./.X./.X./.X./.XX/.XX/.X./","......X/XXXXXXX/.XX..../",".X./XX./XX./.X./.X./.X./.XX/",".XX..../XXXXXXX/......X/",".X./.XX/.XX/.X./.X./.X./XX./","X....../XXXXXXX/....XX./",".XX/.X./.X./.X./XX./XX./.X./"},
				{"XX.XX./XXXXXX/","XX/XX/X./XX/XX/X./","XXXXXX/.XX.XX/",".X/XX/XX/.X/XX/XX/",".XX.XX/XXXXXX/","X./XX/XX/X./XX/XX/","XXXXXX/XX.XX./","XX/XX/.X/XX/XX/.X/"},
				{"...XX./XXXXXX/XX..../","XX./XX./.X./.XX/.XX/.X./","....XX/XXXXXX/.XX.../",".X./XX./XX./.X./.XX/.XX/",".XX.../XXXXXX/....XX/",".X./.XX/.XX/.X./XX./XX./","XX..../XXXXXX/...XX./",".XX/.XX/.X./XX./XX./.X./"},
				{".X.../XXXX./XXXXX/","XX./XXX/XX./XX./X../","XXXXX/.XXXX/...X./","..X/.XX/.XX/XXX/.XX/","...X./.XXXX/XXXXX/","X../XX./XX./XXX/XX./","XXXXX/XXXX./.X.../",".XX/XXX/.XX/.XX/..X/"},
				{"XX..../.X.XX./.XXXXX/","..X/XXX/X../XX./XX./X../","XXXXX./.XX.X./....XX/","..X/.XX/.XX/..X/XXX/X../","....XX/.XX.X./XXXXX./","X../XX./XX./X../XXX/..X/",".XXXXX/.X.XX./XX..../","X../XXX/..X/.XX/.XX/..X/"},
				{"XX.../X.XX./XXXXX/","XXX/X.X/XX./XX./X../","XXXXX/.XX.X/...XX/","..X/.XX/.XX/X.X/XXX/","...XX/.XX.X/XXXXX/","X../XX./XX./X.X/XXX/","XXXXX/X.XX./XX.../","XXX/X.X/.XX/.XX/..X/"},
				{"..XX./XXXXX/XX.../.X.../",".XX./XXX./..XX/..XX/..X./","...X./...XX/XXXXX/.XX../",".X../XX../XX../.XXX/.XX./",".XX../XXXXX/...XX/...X./","..X./..XX/..XX/XXX./.XX./",".X.../XX.../XXXXX/..XX./",".XX./.XXX/XX../XX../.X../"},
				{"...XX./.XXXXX/.X..../XX..../","X.../XXX./..X./..XX/..XX/..X./","....XX/....X./XXXXX./.XX.../",".X../XX../XX../.X../.XXX/...X/",".XX.../XXXXX./....X./....XX/","..X./..XX/..XX/..X./XXX./X.../","XX..../.X..../.XXXXX/...XX./","...X/.XXX/.X../XX../XX../.X../"},
				{"..XX./XXXXX/X..../XX.../","XXX./X.X./..XX/..XX/..X./","...XX/....X/XXXXX/.XX../",".X../XX../XX../.X.X/.XXX/",".XX../XXXXX/....X/...XX/","..X./..XX/..XX/X.X./XXX./","XX.../X..../XXXXX/..XX./",".XXX/.X.X/XX../XX../.X../"},
				{"X...../XXXXX./..XXXX/",".XX/.X./XX./XX./XX./X../","XXXX../.XXXXX/.....X/","..X/.XX/.XX/.XX/.X./XX./",".....X/.XXXXX/XXXX../","X../XX./XX./XX./.X./.XX/","..XXXX/XXXXX./X...../","XX./.X./.XX/.XX/.XX/..X/"},
				{"XXXXX./X.XXXX/","XX/.X/XX/XX/XX/X./","XXXX.X/.XXXXX/",".X/XX/XX/XX/X./XX/",".XXXXX/XXXX.X/","X./XX/XX/XX/.X/XX/","X.XXXX/XXXXX./","XX/X./XX/XX/XX/.X/"},
				{"XX.../XXXX./.XXXX/",".XX/XXX/XX./XX./X../","XXXX./.XXXX/...XX/","..X/.XX/.XX/XXX/XX./","...XX/.XXXX/XXXX./","X../XX./XX./XXX/.XX/",".XXXX/XXXX./XX.../","XX./XXX/.XX/.XX/..X/"},
				{".XXXX./XXXXXX/","X./XX/XX/XX/XX/X./","XXXXXX/.XXXX./",".X/XX/XX/XX/XX/.X/"},
				{"XX.../.X.../.XXX./.XXXX/","...X/XXXX/XX../XX../X.../","XXXX./.XXX./...X./...XX/","...X/..XX/..XX/XXXX/X.../","...XX/...X./.XXX./XXXX./","X.../XX../XX../XXXX/...X/",".XXXX/.XXX./.X.../XX.../","X.../XXXX/..XX/..XX/...X/"},
				{"XX../X.../XXX./XXXX/","XXXX/XX.X/XX../X.../","XXXX/.XXX/...X/..XX/","...X/..XX/X.XX/XXXX/","..XX/...X/.XXX/XXXX/","X.../XX../XX.X/XXXX/","XXXX/XXX./X.../XX../","XXXX/X.XX/..XX/...X/"},
				{"...XX./X.XXXX/XXX.../","XX./X../XX./.XX/.XX/.X./","...XXX/XXXX.X/.XX.../",".X./XX./XX./.XX/..X/.XX/",".XX.../XXXX.X/...XXX/",".X./.XX/.XX/XX./X../XX./","XXX.../X.XXXX/...XX./",".XX/..X/.XX/XX./XX./.X./"},
				{"...XX./..XXXX/XXX.../X...../","XX../.X../.XX./..XX/..XX/..X./",".....X/...XXX/XXXX../.XX.../",".X../XX../XX../.XX./..X./..XX/",".XX.../XXXX../...XXX/.....X/","..X./..XX/..XX/.XX./.X../XX../","X...../XXX.../..XXXX/...XX./","..XX/..X./.XX./XX../XX../.X../"},
				{"...XX./XXXXXX/.XX.../",".X./XX./XX./.XX/.XX/.X./",".XX.../XXXXXX/...XX./",".X./.XX/.XX/XX./XX./.X./"},
				{"..XX./.XXXX/XX.../XX.../","XX../XXX./..XX/..XX/..X./","...XX/...XX/XXXX./.XX../",".X../XX../XX../.XXX/..XX/",".XX../XXXX./...XX/...XX/","..X./..XX/..XX/XXX./XX../","XX.../XX.../.XXXX/..XX./","..XX/.XXX/XX../XX../.X../"},
				{".XX./XXXX/XXX./..X./",".XX./.XXX/XXXX/..X./",".X../.XXX/XXXX/.XX./",".X../XXXX/XXX./.XX./",".XX./XXXX/.XXX/.X../","..X./XXXX/.XXX/.XX./","..X./XXX./XXXX/.XX./",".XX./XXX./XXXX/.X../"},
				{"..XX./.XXXX/.X.../.X.../XX.../","X..../XXXX./...XX/...XX/...X./","...XX/...X./...X./XXXX./.XX../",".X.../XX.../XX.../.XXXX/....X/",".XX../XXXX./...X./...X./...XX/","...X./...XX/...XX/XXXX./X..../","XX.../.X.../.X.../.XXXX/..XX./","....X/.XXXX/XX.../XX.../.X.../"},
				{".XX./XXXX/X.../X.../XX../","XXXX./X..XX/...XX/...X./","..XX/...X/...X/XXXX/.XX./",".X.../XX.../XX..X/.XXXX/",".XX./XXXX/...X/...X/..XX/","...X./...XX/X..XX/XXXX./","XX../X.../X.../XXXX/.XX./",".XXXX/XX..X/XX.../.X.../"},
				{"...XX./X..XXX/XXXX../","XX./X../X../XXX/.XX/.X./","..XXXX/XXX..X/.XX.../",".X./XX./XXX/..X/..X/.XX/",".XX.../XXX..X/..XXXX/",".X./.XX/XXX/X../X../XX./","XXXX../X..XXX/...XX./",".XX/..X/..X/XXX/XX./.X./"},
				{"...XX./...XXX/XXXX../X...../","XX../.X../.X../.XXX/..XX/..X./",".....X/..XXXX/XXX.../.XX.../",".X../XX../XXX./..X./..X./..XX/",".XX.../XXX.../..XXXX/.....X/","..X./..XX/.XXX/.X../.X../XX../","X...../XXXX../...XXX/...XX./","..XX/..X./..X./XXX./XX../.X../"},
				{"..XX./..XXX/XXX../XX.../","XX../XX../.XXX/..XX/..X./","...XX/..XXX/XXX../.XX../",".X../XX../XXX./..XX/..XX/",".XX../XXX../..XXX/...XX/","..X./..XX/.XXX/XX../XX../","XX.../XXX../..XXX/..XX./","..XX/..XX/XXX./XX../.X../"},
				{"XXXX./.XXXX/.XX../","..X/XXX/XXX/.XX/.X./","..XX./XXXX./.XXXX/",".X./XX./XXX/XXX/X../",".XXXX/XXXX./..XX./",".X./.XX/XXX/XXX/..X/",".XX../.XXXX/XXXX./","X../XXX/XXX/XX./.X./"},
				{"..XX./..XXX/.XX../.X.../XX.../","X..../XXX../..XXX/...XX/...X./","...XX/...X./..XX./XXX../.XX../",".X.../XX.../XXX../..XXX/....X/",".XX../XXX../..XX./...X./...XX/","...X./...XX/..XXX/XXX../X..../","XX.../.X.../.XX../..XXX/..XX./","....X/..XXX/XXX../XX.../.X.../"},
				{".XX./.XXX/XX../X.../XX../","XXX../X.XXX/...XX/...X./","..XX/...X/..XX/XXX./.XX./",".X.../XX.../XXX.X/..XXX/",".XX./XXX./..XX/...X/..XX/","...X./...XX/X.XXX/XXX../","XX../X.../XX../.XXX/.XX./","..XXX/XXX.X/XX.../.X.../"},
				{"XX./XXX/XX./.X./XX./","X.XXX/XXXXX/...X./",".XX/.X./.XX/XXX/.XX/",".X.../XXXXX/XXX.X/",".XX/XXX/.XX/.X./.XX/","...X./XXXXX/X.XXX/","XX./.X./XX./XXX/XX./","XXX.X/XXXXX/.X.../"},
				{"XX./XXX/XX./.X./.XX/","..XXX/XXXXX/X..X./","XX./.X./.XX/XXX/.XX/",".X..X/XXXXX/XXX../",".XX/XXX/.XX/.X./XX./","X..X./XXXXX/..XXX/",".XX/.X./XX./XXX/XX./","XXX../XXXXX/.X..X/"},
				{"..XX./..XXX/X.X../XXX../","XX../X.../XXXX/..XX/..X./","..XXX/..X.X/XXX../.XX../",".X../XX../XXXX/...X/..XX/",".XX../XXX../..X.X/..XXX/","..X./..XX/XXXX/X.../XX../","XXX../X.X../..XXX/..XX./","..XX/...X/XXXX/XX../.X../"},
				{"..XX./..XXX/..X../XXX../X..../","XX.../.X.../.XXXX/...XX/...X./","....X/..XXX/..X../XXX../.XX../",".X.../XX.../XXXX./...X./...XX/",".XX../XXX../..X../..XXX/....X/","...X./...XX/.XXXX/.X.../XX.../","X..../XXX../..X../..XXX/..XX./","...XX/...X./XXXX./XX.../.X.../"},
				{"..XX./..XXX/XXX../.XX../",".X../XX../XXXX/..XX/..X./",".XX../XXX../..XXX/..XX./","..X./..XX/XXXX/XX../.X../"},
				{".XX./.XXX/.X../XX../XX../","XX.../XXXXX/...XX/...X./","..XX/..XX/..X./XXX./.XX./",".X.../XX.../XXXXX/...XX/",".XX./XXX./..X./..XX/..XX/","...X./...XX/XXXXX/XX.../","XX../XX../.X../.XXX/.XX./","...XX/XXXXX/XX.../.X.../"},
				{"XX./XXX/X../XXX/..X/",".XXXX/.X.XX/XX.X./","X../XXX/..X/XXX/.XX/",".X.XX/XX.X./XXXX./",".XX/XXX/..X/XXX/X../","XX.X./.X.XX/.XXXX/","..X/XXX/X../XXX/XX./","XXXX./XX.X./.X.XX/"},
				{"XX./XXX/X../XX./XX./","XXXXX/XX.XX/...X./",".XX/.XX/..X/XXX/.XX/",".X.../XX.XX/XXXXX/",".XX/XXX/..X/.XX/.XX/","...X./XX.XX/XXXXX/","XX./XX./X../XXX/XX./","XXXXX/XX.XX/.X.../"},
				{".XX./.XXX/.X../.X../.X../XX../","X...../XXXXXX/....XX/....X./","..XX/..X./..X./..X./XXX./.XX./",".X..../XX..../XXXXXX/.....X/",".XX./XXX./..X./..X./..X./..XX/","....X./....XX/XXXXXX/X...../","XX../.X../.X../.X../.XXX/.XX./",".....X/XXXXXX/XX..../.X..../"},
				{"XX./XXX/X../X../X../XX./","XXXXXX/X...XX/....X./",".XX/..X/..X/..X/XXX/.XX/",".X..../XX...X/XXXXXX/",".XX/XXX/..X/..X/..X/.XX/","....X./X...XX/XXXXXX/","XX./X../X../X../XXX/XX./","XXXXXX/XX...X/.X..../"},
				{"......X/XXXXXXX/XX...../","XX./XX./.X./.X./.X./.X./.XX/",".....XX/XXXXXXX/X....../","XX./.X./.X./.X./.X./.XX/.XX/","X....../XXXXXXX/.....XX/",".XX/.X./.X./.X./.X./XX./XX./","XX...../XXXXXXX/......X/",".XX/.XX/.X./.X./.X./.X./XX./"},
				{"XXXXXXX/XX....X/","XX/XX/.X/.X/.X/.X/XX/","X....XX/XXXXXXX/","XX/X./X./X./X./XX/XX/","XXXXXXX/X....XX/","XX/.X/.X/.X/.X/XX/XX/","XX....X/XXXXXXX/","XX/XX/X./X./X./X./XX/"},
				{"....XX/XXXXXX/XX..../","XX./XX./.X./.X./.XX/.XX/","XX..../XXXXXX/....XX/",".XX/.XX/.X./.X./XX./XX./"},
				{"XXXXXX/XX..XX/","XX/XX/.X/.X/XX/XX/","XX..XX/XXXXXX/","XX/XX/X./X./XX/XX/"},
				{"...XX/....X/XXXXX/XX.../","XX../XX../.X../.X.X/.XXX/","...XX/XXXXX/X..../XX.../","XXX./X.X./..X./..XX/..XX/","XX.../X..../XXXXX/...XX/",".XXX/.X.X/.X../XX../XX../","XX.../XXXXX/....X/...XX/","..XX/..XX/..X./X.X./XXX./"},
				{"....XX/....X./XXXXX./XX..../","XX../XX../.X../.X../.XXX/...X/","....XX/.XXXXX/.X..../XX..../","X.../XXX./..X./..X./..XX/..XX/","XX..../.X..../.XXXXX/....XX/","...X/.XXX/.X../.X../XX../XX../","XX..../XXXXX./....X./....XX/","..XX/..XX/..X./..X./XXX./X.../"},
				{"XXXXX/XX..X/...XX/",".XX/.XX/..X/X.X/XXX/","XX.../X..XX/XXXXX/","XXX/X.X/X../XX./XX./","XXXXX/X..XX/XX.../","XXX/X.X/..X/.XX/.XX/","...XX/XX..X/XXXXX/","XX./XX./X../X.X/XXX/"},
				{"XXXXX./XX..X./....XX/",".XX/.XX/..X/..X/XXX/X../","XX..../.X..XX/.XXXXX/","..X/XXX/X../X../XX./XX./",".XXXXX/.X..XX/XX..../","X../XXX/..X/..X/.XX/.XX/","....XX/XX..X./XXXXX./","XX./XX./X../X../XXX/..X/"},
				{".X../.XXX/XXXX/XX../","XX../XXXX/.XX./.XX./","..XX/XXXX/XXX./..X./",".XX./.XX./XXXX/..XX/","..X./XXX./XXXX/..XX/",".XX./.XX./XXXX/XX../","XX../XXXX/.XXX/.X../","..XX/XXXX/.XX./.XX./"},
				{".....X/...XXX/XXXX../XX..../","XX../XX../.X../.XX./..X./..XX/","....XX/..XXXX/XXX.../X...../","XX../.X../.XX./..X./..XX/..XX/","X...../XXX.../..XXXX/....XX/","..XX/..X./.XX./.X../XX../XX../","XX..../XXXX../...XXX/.....X/","..XX/..XX/..X./.XX./.X../XX../"},
				{"...XXX/XXXX.X/XX..../","XX./XX./.X./.XX/..X/.XX/","....XX/X.XXXX/XXX.../","XX./X../XX./.X./.XX/.XX/","XXX.../X.XXXX/....XX/",".XX/..X/.XX/.X./XX./XX./","XX..../XXXX.X/...XXX/",".XX/.XX/.X./XX./X../XX./"},
				{"...XX/...XX/XXXX./XX.../","XX../XX../.X../.XXX/..XX/","...XX/.XXXX/XX.../XX.../","XX../XXX./..X./..XX/..XX/","XX.../XX.../.XXXX/...XX/","..XX/.XXX/.X../XX../XX../","XX.../XXXX./...XX/...XX/","..XX/..XX/..X./XXX./XX../"},
				{"..XX/...X/...X/XXXX/XX../","XX.../XX.../.X..X/.XXXX/","..XX/XXXX/X.../X.../XX../","XXXX./X..X./...XX/...XX/","XX../X.../X.../XXXX/..XX/",".XXXX/.X..X/XX.../XX.../","XX../XXXX/...X/...X/..XX/","...XX/...XX/X..X./XXXX./"},
				{"...XX/...X./...X./XXXX./XX.../","XX.../XX.../.X.../.XXXX/....X/","...XX/.XXXX/.X.../.X.../XX.../","X..../XXXX./...X./...XX/...XX/","XX.../.X.../.X.../.XXXX/...XX/","....X/.XXXX/.X.../XX.../XX.../","XX.../XXXX./...X./...X./...XX/","...XX/...XX/...X./XXXX./X..../"},
				{"XXXX.X/XX.XXX/","XX/XX/.X/XX/X./XX/","XXX.XX/X.XXXX/","XX/.X/XX/X./XX/XX/","X.XXXX/XXX.XX/","XX/X./XX/.X/XX/XX/","XX.XXX/XXXX.X/","XX/XX/X./XX/.X/XX/"},
				{"XXXX../XX.XXX/.....X/",".XX/.XX/..X/.XX/.X./XX./","X...../XXX.XX/..XXXX/",".XX/.X./XX./X../XX./XX./","..XXXX/XXX.XX/X...../","XX./.X./.XX/..X/.XX/.XX/",".....X/XX.XXX/XXXX../","XX./XX./X../XX./.X./.XX/"},
				{"XXXX./XX.XX/...XX/",".XX/.XX/..X/XXX/XX./","XX.../XX.XX/.XXXX/",".XX/XXX/X../XX./XX./",".XXXX/XX.XX/XX.../","XX./XXX/..X/.XX/.XX/","...XX/XX.XX/XXXX./","XX./XX./X../XXX/.XX/"},
				{"XXXX/XX.X/...X/..XX/","..XX/..XX/X..X/XXXX/","XX../X.../X.XX/XXXX/","XXXX/X..X/XX../XX../","XXXX/X.XX/X.../XX../","XXXX/X..X/..XX/..XX/","..XX/...X/XX.X/XXXX/","XX../XX../X..X/XXXX/"},
				{"XXXX./XX.X./...X./...XX/","..XX/..XX/...X/XXXX/X.../","XX.../.X.../.X.XX/.XXXX/","...X/XXXX/X.../XX../XX../",".XXXX/.X.XX/.X.../XX.../","X.../XXXX/...X/..XX/..XX/","...XX/...X./XX.X./XXXX./","XX../XX../X.../XXXX/...X/"},
				{"XX./.X./.XX/XXX/XX./","XX..X/XXXXX/.XX../",".XX/XXX/XX./.X./.XX/","..XX./XXXXX/X..XX/",".XX/.X./XX./XXX/.XX/",".XX../XXXXX/XX..X/","XX./XXX/.XX/.X./XX./","X..XX/XXXXX/..XX./"},
				{".XX/.X./.XX/XXX/XX./","XX.../XXXXX/.XX.X/",".XX/XXX/XX./.X./XX./","X.XX./XXXXX/...XX/","XX./.X./XX./XXX/.XX/",".XX.X/XXXXX/XX.../","XX./XXX/.XX/.X./.XX/","...XX/XXXXX/X.XX./"},
				{".....X/..XXXX/XXX.../XX..../","XX../XX../.XX./..X./..X./..XX/","....XX/...XXX/XXXX../X...../","XX../.X../.X../.XX./..XX/..XX/","X...../XXXX../...XXX/....XX/","..XX/..X./..X./.XX./XX../XX../","XX..../XXX.../..XXXX/.....X/","..XX/..XX/.XX./.X../.X../XX../"},
				{"..XXXX/XXX..X/XX..../","XX./XX./.XX/..X/..X/.XX/","....XX/X..XXX/XXXX../","XX./X../X../XX./.XX/.XX/","XXXX../X..XXX/....XX/",".XX/..X/..X/.XX/XX./XX./","XX..../XXX..X/..XXXX/",".XX/.XX/XX./X../X../XX./"},
				{"...XX/..XXX/XXX../XX.../","XX../XX../.XX./..XX/..XX/","XX.../XXX../..XXX/...XX/","..XX/..XX/.XX./XX../XX../"},
				{"..XX/...X/..XX/XXX./XX../","XX.../XX.../.XX.X/..XXX/","..XX/.XXX/XX../X.../XX../","XXX../X.XX./...XX/...XX/","XX../X.../XX../.XXX/..XX/","..XXX/.XX.X/XX.../XX.../","XX../XXX./..XX/...X/..XX/","...XX/...XX/X.XX./XXX../"},
				{"...XX/...X./..XX./XXX../XX.../","XX.../XX.../.XX../..XXX/....X/","...XX/..XXX/.XX../.X.../XX.../","X..../XXX../..XX./...XX/...XX/","XX.../.X.../.XX../..XXX/...XX/","....X/..XXX/.XX../XX.../XX.../","XX.../XXX../..XX./...X./...XX/","...XX/...XX/..XX./XXX../X..../"},
				{"..XX./XXXX./XX.XX/","XX./XX./.XX/XXX/X../","XX.XX/.XXXX/.XX../","..X/XXX/XX./.XX/.XX/",".XX../.XXXX/XX.XX/","X../XXX/.XX/XX./XX./","XX.XX/XXXX./..XX./",".XX/.XX/XX./XXX/..X/"},
				{"X../XXX/..X/XXX/XX./","XX.XX/XX.X./.XXX./",".XX/XXX/X../XXX/..X/",".XXX./.X.XX/XX.XX/","..X/XXX/X../XXX/.XX/",".XXX./XX.X./XX.XX/","XX./XXX/..X/XXX/X../","XX.XX/.X.XX/.XXX./"},
				{"....X/..XXX/..X../XXX../XX.../","XX.../XX.../.XXX./...X./...XX/","...XX/..XXX/..X../XXX../X..../","XX.../.X.../.XXX./...XX/...XX/","X..../XXX../..X../..XXX/...XX/","...XX/...X./.XXX./XX.../XX.../","XX.../XXX../..X../..XXX/....X/","...XX/...XX/.XXX./.X.../XX.../"},
				{"..XXX/..X.X/XXX../XX.../","XX../XX../.XXX/...X/..XX/","...XX/..XXX/X.X../XXX../","XX../X.../XXX./..XX/..XX/","XXX../X.X../..XXX/...XX/","..XX/...X/.XXX/XX../XX../","XX.../XXX../..X.X/..XXX/","..XX/..XX/XXX./X.../XX../"},
				{".XX/..X/..X/..X/XXX/XX./","XX..../XX...X/.XXXXX/",".XX/XXX/X../X../X../XX./","XXXXX./X...XX/....XX/","XX./X../X../X../XXX/.XX/",".XXXXX/XX...X/XX..../","XX./XXX/..X/..X/..X/.XX/","....XX/X...XX/XXXXX./"},
				{"..XX/..X./..X./..X./XXX./XX../","XX..../XX..../.XXXXX/.....X/","..XX/.XXX/.X../.X../.X../XX../","X...../XXXXX./....XX/....XX/","XX../.X../.X../.X../.XXX/..XX/",".....X/.XXXXX/XX..../XX..../","XX../XXX./..X./..X./..X./..XX/","....XX/....XX/XXXXX./X...../"},
				{"X....../XXXXXX./.....X./.....XX/","..XX/..X./..X./..X./..X./XXX./X.../","XX...../.X...../.XXXXXX/......X/","...X/.XXX/.X../.X../.X../.X../XX../","......X/.XXXXXX/.X...../XX...../","X.../XXX./..X./..X./..X./..X./..XX/",".....XX/.....X./XXXXXX./X....../","XX../.X../.X../.X../.X../.XXX/...X/"},
				{"XXXXXX./X....X./.....XX/",".XX/..X/..X/..X/..X/XXX/X../","XX...../.X....X/.XXXXXX/","..X/XXX/X../X../X../X../XX./",".XXXXXX/.X....X/XX...../","X../XXX/..X/..X/..X/..X/.XX/",".....XX/X....X./XXXXXX./","XX./X../X../X../X../XXX/..X/"},
				{"XX..../.X..../.XXXX./....X./....XX/","....X/..XXX/..X../..X../XXX../X..../","....XX/....X./.XXXX./.X..../XX..../","X..../XXX../..X../..X../..XXX/....X/"},
				{"XX.../X..../XXXX./...X./...XX/","..XXX/..X.X/..X../XXX../X..../","XX.../.X.../.XXXX/....X/...XX/","....X/..XXX/..X../X.X../XXX../","...XX/....X/.XXXX/.X.../XX.../","X..../XXX../..X../..X.X/..XXX/","...XX/...X./XXXX./X..../XX.../","XXX../X.X../..X../..XXX/....X/"},
				{".XXXX./.X..X./XX..XX/","X../XXX/..X/..X/XXX/X../","XX..XX/.X..X./.XXXX./","..X/XXX/X../X../XXX/..X/"},
				{"XXXX./X..X./XX.XX/","XXX/X.X/..X/XXX/X../","XX.XX/.X..X/.XXXX/","..X/XXX/X../X.X/XXX/",".XXXX/.X..X/XX.XX/","X../XXX/..X/X.X/XXX/","XX.XX/X..X./XXXX./","XXX/X.X/X../XXX/..X/"},
				{"X...../XXX.../..XXX./....X./....XX/","...XX/...X./..XX./..X../XXX../X..../","XX..../.X..../.XXX../...XXX/.....X/","....X/..XXX/..X../.XX../.X.../XX.../",".....X/...XXX/.XXX../.X..../XX..../","X..../XXX../..X../..XX./...X./...XX/","....XX/....X./..XXX./XXX.../X...../","XX.../.X.../.XX../..X../..XXX/....X/"},
				{"XXX.../X.XXX./....X./....XX/","..XX/...X/..XX/..X./XXX./X.../","XX..../.X..../.XXX.X/...XXX/","...X/.XXX/.X../XX../X.../XX../","...XXX/.XXX.X/.X..../XX..../","X.../XXX./..X./..XX/...X/..XX/","....XX/....X./X.XXX./XXX.../","XX../X.../XX../.X../.XXX/...X/"},
				{"..X./XXX./XXX./..X./..XX/","..XX./..XX./XXXXX/X..../","XX../.X../.XXX/.XXX/.X../","....X/XXXXX/.XX../.XX../",".X../.XXX/.XXX/.X../XX../","X..../XXXXX/..XX./..XX./","..XX/..X./XXX./XXX./..X./",".XX../.XX../XXXXX/....X/"},
				{"XX.../.X.../.X.../.XXX./...X./...XX/",".....X/..XXXX/..X.../XXX.../X...../","XX.../.X.../.XXX./...X./...X./...XX/",".....X/...XXX/...X../XXXX../X...../","...XX/...X./...X./.XXX./.X.../XX.../","X...../XXX.../..X.../..XXXX/.....X/","...XX/...X./.XXX./.X.../.X.../XX.../","X...../XXXX../...X../...XXX/.....X/"},
				{"XX../X.../X.../XXX./..X./..XX/","..XXXX/..X..X/XXX.../X...../","XX../.X../.XXX/...X/...X/..XX/",".....X/...XXX/X..X../XXXX../","..XX/...X/...X/.XXX/.X../XX../","X...../XXX.../..X..X/..XXXX/","..XX/..X./XXX./X.../X.../XX../","XXXX../X..X../...XXX/.....X/"},
				{"X.XXX./XXX.X./....XX/",".XX/.X./.XX/..X/XXX/X../","XX..../.X.XXX/.XXX.X/","..X/XXX/X../XX./.X./XX./",".XXX.X/.X.XXX/XX..../","X../XXX/..X/.XX/.X./.XX/","....XX/XXX.X./X.XXX./","XX./.X./XX./X../XXX/..X/"},
				{"..XXX./XXX.X./X...XX/","XX./.X./.XX/..X/XXX/X../","XX...X/.X.XXX/.XXX../","..X/XXX/X../XX./.X./.XX/",".XXX../.X.XXX/XX...X/","X../XXX/..X/.XX/.X./XX./","X...XX/XXX.X./..XXX./",".XX/.X./XX./X../XXX/..X/"},
				{".XXX./.X.X./.X.XX/XX.../","X.../XXXX/...X/.XXX/.X../","...XX/XX.X./.X.X./.XXX./","..X./XXX./X.../XXXX/...X/",".XXX./.X.X./XX.X./...XX/",".X../.XXX/...X/XXXX/X.../","XX.../.X.XX/.X.X./.XXX./","...X/XXXX/X.../XXX./..X./"},
				{"XXX./X.X./X.XX/XX../","XXXX/X..X/.XXX/.X../","..XX/XX.X/.X.X/.XXX/","..X./XXX./X..X/XXXX/",".XXX/.X.X/XX.X/..XX/",".X../.XXX/X..X/XXXX/","XX../X.XX/X.X./XXX./","XXXX/X..X/XXX./..X./"},
				{"X...../XXXX../...XX./....X./....XX/","...XX/...X./...X./..XX./XXX../X..../","XX..../.X..../.XX.../..XXXX/.....X/","....X/..XXX/.XX../.X.../.X.../XX.../",".....X/..XXXX/.XX.../.X..../XX..../","X..../XXX../..XX./...X./...X./...XX/","....XX/....X./...XX./XXXX../X...../","XX.../.X.../.X.../.XX../..XXX/....X/"},
				{"XXXX../X..XX./....X./....XX/","..XX/...X/...X/..XX/XXX./X.../","XX..../.X..../.XX..X/..XXXX/","...X/.XXX/XX../X.../X.../XX../","..XXXX/.XX..X/.X..../XX..../","X.../XXX./..XX/...X/...X/..XX/","....XX/....X./X..XX./XXXX../","XX../X.../X.../XX../.XXX/...X/"},
				{"XX.../.X.../.XX../..XX./...X./...XX/",".....X/...XXX/..XX../XXX.../X...../","...XX/...X./..XX./.XX../.X.../XX.../","X...../XXX.../..XX../...XXX/.....X/"},
				{"XX../X.../XX../.XX./..X./..XX/","...XXX/..XX.X/XXX.../X...../","XX../.X../.XX./..XX/...X/..XX/",".....X/...XXX/X.XX../XXX.../","..XX/...X/..XX/.XX./.X../XX../","X...../XXX.../..XX.X/...XXX/","..XX/..X./.XX./XX../X.../XX../","XXX.../X.XX../...XXX/.....X/"},
				{".XX../.XXX./XX.X./...XX/",".X../.XXX/..XX/XXX./X.../","XX.../.X.XX/.XXX./..XX./","...X/.XXX/XX../XXX./..X./","..XX./.XXX./.X.XX/XX.../","X.../XXX./..XX/.XXX/.X../","...XX/XX.X./.XXX./.XX../","..X./XXX./XX../.XXX/...X/"},
				{"...X/XXXX/XX../.X../.XX./","..XX./XXXX./X..X./...XX/",".XX./..X./..XX/XXXX/X.../","XX.../.X..X/.XXXX/.XX../","X.../XXXX/..XX/..X./.XX./","...XX/X..X./XXXX./..XX./",".XX./.X../XX../XXXX/...X/",".XX../.XXXX/.X..X/XX.../"},
				{"XXXX/XX.X/.X../.XX./","..XX/XXXX/X..X/..XX/",".XX./..X./X.XX/XXXX/","XX../X..X/XXXX/XX../","XXXX/X.XX/..X./.XX./","..XX/X..X/XXXX/..XX/",".XX./.X../XX.X/XXXX/","XX../XXXX/X..X/XX../"},
				{"XX./.X./XX./XX./.X./.XX/","..XX.X/XXXXXX/X...../","XX./.X./.XX/.XX/.X./.XX/",".....X/XXXXXX/X.XX../",".XX/.X./.XX/.XX/.X./XX./","X...../XXXXXX/..XX.X/",".XX/.X./XX./XX./.X./XX./","X.XX../XXXXXX/.....X/"},
				{".XX/.X./XX./XX./.X./.XX/","..XX../XXXXXX/X....X/","XX./.X./.XX/.XX/.X./XX./","X....X/XXXXXX/..XX../"},
				{"XXX../X.X../..XX./...X./...XX/","...XX/....X/..XXX/XXX../X..../","XX.../.X.../.XX../..X.X/..XXX/","....X/..XXX/XXX../X..../XX.../","..XXX/..X.X/.XX../.X.../XX.../","X..../XXX../..XXX/....X/...XX/","...XX/...X./..XX./X.X../XXX../","XX.../X..../XXX../..XXX/....X/"},
				{"XXX/X.X/XX./.X./.XX/","..XXX/XXX.X/X..XX/","XX./.X./.XX/X.X/XXX/","XX..X/X.XXX/XXX../","XXX/X.X/.XX/.X./XX./","X..XX/XXX.X/..XXX/",".XX/.X./XX./X.X/XXX/","XXX../X.XXX/XX..X/"},
				{"XX../.X../.X../.X../.XX./..X./..XX/","......X/..XXXXX/XXX..../X....../","XX../.X../.XX./..X./..X./..X./..XX/","......X/....XXX/XXXXX../X....../","..XX/..X./..X./..X./.XX./.X../XX../","X....../XXX..../..XXXXX/......X/","..XX/..X./.XX./.X../.X../.X../XX../","X....../XXXXX../....XXX/......X/"},
				{"XX./X../X../X../XX./.X./.XX/","..XXXXX/XXX...X/X....../","XX./.X./.XX/..X/..X/..X/.XX/","......X/X...XXX/XXXXX../",".XX/..X/..X/..X/.XX/.X./XX./","X....../XXX...X/..XXXXX/",".XX/.X./XX./X../X../X../XX./","XXXXX../X...XXX/......X/"},
				{"X..XX./XXXXX./....XX/",".XX/.X./.X./.XX/XXX/X../","XX..../.XXXXX/.XX..X/","..X/XXX/XX./.X./.X./XX./",".XX..X/.XXXXX/XX..../","X../XXX/.XX/.X./.X./.XX/","....XX/XXXXX./X..XX./","XX./.X./.X./XX./XXX/..X/"},
				{"...XX./XXXXX./X...XX/","XX./.X./.X./.XX/XXX/X../","XX...X/.XXXXX/.XX.../","..X/XXX/XX./.X./.X./.XX/",".XX.../.XXXXX/XX...X/","X../XXX/.XX/.X./.X./XX./","X...XX/XXXXX./...XX./",".XX/.X./.X./XX./XXX/..X/"},
				{"XX.../.XXX./.XXX./...XX/","...X/.XXX/.XX./XXX./X.../","...XX/.XXX./.XXX./XX.../","X.../XXX./.XX./.XXX/...X/"},
				{".XX./XXX./X.XX/XX../","XXX./X.XX/.XXX/.X../","..XX/XX.X/.XXX/.XX./","..X./XXX./XX.X/.XXX/",".XX./.XXX/XX.X/..XX/",".X../.XXX/X.XX/XXX./","XX../X.XX/XXX./.XX./",".XXX/XX.X/XXX./..X./"},
				{"..XX./X.XX./XXXXX/","XX./X../XXX/XXX/X../","XXXXX/.XX.X/.XX../","..X/XXX/XXX/..X/.XX/",".XX../.XX.X/XXXXX/","X../XXX/XXX/X../XX./","XXXXX/X.XX./..XX./",".XX/..X/XXX/XXX/..X/"},
				{".XX./.XX./.XXX/.X../XX../","X..../XXXXX/..XXX/..X../","..XX/..X./XXX./.XX./.XX./","..X../XXX../XXXXX/....X/",".XX./.XX./XXX./..X./..XX/","..X../..XXX/XXXXX/X..../","XX../.X../.XXX/.XX./.XX./","....X/XXXXX/XXX../..X../"},
				{"XX./XX./XXX/X../XX./","XXXXX/X.XXX/..X../",".XX/..X/XXX/.XX/.XX/","..X../XXX.X/XXXXX/",".XX/.XX/XXX/..X/.XX/","..X../X.XXX/XXXXX/","XX./X../XXX/XX./XX./","XXXXX/XXX.X/..X../"},
				{".....X/XXXXXX/X...../XX..../","XXX./X.X./..X./..X./..X./..XX/","....XX/.....X/XXXXXX/X...../","XX../.X../.X../.X../.X.X/.XXX/","X...../XXXXXX/.....X/....XX/","..XX/..X./..X./..X./X.X./XXX./","XX..../X...../XXXXXX/.....X/",".XXX/.X.X/.X../.X../.X../XX../"},
				{"XXXXXX/X....X/XX..../","XXX/X.X/..X/..X/..X/.XX/","....XX/X....X/XXXXXX/","XX./X../X../X../X.X/XXX/","XXXXXX/X....X/....XX/",".XX/..X/..X/..X/X.X/XXX/","XX..../X....X/XXXXXX/","XXX/X.X/X../X../X../XX./"},
				{"..XX/...X/XXXX/X.../XX../","XXX../X.X../..X.X/..XXX/","XX../X.../XXXX/...X/..XX/","..XXX/..X.X/X.X../XXX../"},
				{"..XXX/XXX.X/X..../XX.../","XXX./X.X./..XX/...X/..XX/","...XX/....X/X.XXX/XXX../","XX../X.../XX../.X.X/.XXX/","XXX../X.XXX/....X/...XX/","..XX/...X/..XX/X.X./XXX./","XX.../X..../XXX.X/..XXX/",".XXX/.X.X/XX../X.../XX../"},
				{".XX/..X/..X/XXX/X../XX./","XXX.../X.X..X/..XXXX/",".XX/..X/XXX/X../X../XX./","XXXX../X..X.X/...XXX/","XX./X../X../XXX/..X/.XX/","..XXXX/X.X..X/XXX.../","XX./X../XXX/..X/..X/.XX/","...XXX/X..X.X/XXXX../"},
				{"..XX/..X./..X./XXX./X.../XX../","XXX.../X.X.../..XXXX/.....X/","..XX/...X/.XXX/.X../.X../XX../","X...../XXXX../...X.X/...XXX/","XX../.X../.X../.XXX/...X/..XX/",".....X/..XXXX/X.X.../XXX.../","XX../X.../XXX./..X./..X./..XX/","...XXX/...X.X/XXXX../X...../"},
				{"XXX.X/X.XXX/XX.../","XXX/X.X/.XX/.X./.XX/","...XX/XXX.X/X.XXX/","XX./.X./XX./X.X/XXX/","X.XXX/XXX.X/...XX/",".XX/.X./.XX/X.X/XXX/","XX.../X.XXX/XXX.X/","XXX/X.X/XX./.X./XX./"},
				{"X.../XXXX/..XX/..X./..XX/","...XX/...X./XXXX./X.XX./","XX../.X../XX../XXXX/...X/",".XX.X/.XXXX/.X.../XX.../","...X/XXXX/XX../.X../XX../","X.XX./XXXX./...X./...XX/","..XX/..X./..XX/XXXX/X.../","XX.../.X.../.XXXX/.XX.X/"},
				{"XXXX/X.XX/..X./..XX/","..XX/...X/XXXX/X.XX/","XX../.X../XX.X/XXXX/","XX.X/XXXX/X.../XX../","XXXX/XX.X/.X../XX../","X.XX/XXXX/...X/..XX/","..XX/..X./X.XX/XXXX/","XX../X.../XXXX/XX.X/"},
				{"XX/X./XX/XX/X./XX/","XXXXXX/X.XX.X/","XX/.X/XX/XX/.X/XX/","X.XX.X/XXXXXX/"},
				{"....X/.XXXX/XX.../X..../XX.../","XXX../X.XX./...X./...X./...XX/","...XX/....X/...XX/XXXX./X..../","XX.../.X.../.X.../.XX.X/..XXX/","X..../XXXX./...XX/....X/...XX/","...XX/...X./...X./X.XX./XXX../","XX.../X..../XX.../.XXXX/....X/","..XXX/.XX.X/.X.../.X.../XX.../"},
				{".XXXX/XX..X/X..../XX.../","XXX./X.XX/...X/...X/..XX/","...XX/....X/X..XX/XXXX./","XX../X.../X.../XX.X/.XXX/","XXXX./X..XX/....X/...XX/","..XX/...X/...X/X.XX/XXX./","XX.../X..../XX..X/.XXXX/",".XXX/XX.X/X.../X.../XX../"},
				{".XX/..X/.XX/XX./X../XX./","XXX.../X.XX.X/...XXX/","XX./X../XX./.XX/..X/.XX/","...XXX/X.XX.X/XXX.../"},
				{"XX/.X/.X/.X/XX/X./XX/","XXX...X/X.XXXXX/","XX/.X/XX/X./X./X./XX/","XXXXX.X/X...XXX/","XX/X./X./X./XX/.X/XX/","X.XXXXX/XXX...X/","XX/X./XX/.X/.X/.X/XX/","X...XXX/XXXXX.X/"},
				{".XX/.X./.X./.X./XX./X../XX./","XXX..../X.XXXXX/......X/",".XX/..X/.XX/.X./.X./.X./XX./","X....../XXXXX.X/....XXX/","XX./.X./.X./.X./.XX/..X/.XX/","......X/X.XXXXX/XXX..../","XX./X../XX./.X./.X./.X./.XX/","....XXX/XXXXX.X/X....../"},
				{"XX..X/XXXXX/XX.../","XXX/XXX/.X./.X./.XX/","...XX/XXXXX/X..XX/","XX./.X./.X./XXX/XXX/","X..XX/XXXXX/...XX/",".XX/.X./.X./XXX/XXX/","XX.../XXXXX/XX..X/","XXX/XXX/.X./.X./XX./"},
				{"X...../XXXXX./....X./....X./....XX/","...XX/...X./...X./...X./XXXX./X..../","XX..../.X..../.X..../.XXXXX/.....X/","....X/.XXXX/.X.../.X.../.X.../XX.../",".....X/.XXXXX/.X..../.X..../XX..../","X..../XXXX./...X./...X./...X./...XX/","....XX/....X./....X./XXXXX./X...../","XX.../.X.../.X.../.X.../.XXXX/....X/"},
				{"XXXXX./X...X./....X./....XX/","..XX/...X/...X/...X/XXXX/X.../","XX..../.X..../.X...X/.XXXXX/","...X/XXXX/X.../X.../X.../XX../",".XXXXX/.X...X/.X..../XX..../","X.../XXXX/...X/...X/...X/..XX/","....XX/....X./X...X./XXXXX./","XX../X.../X.../X.../XXXX/...X/"},
				{"XX../.X../.X../.XX./..X./..X./..XX/","......X/...XXXX/XXXX.../X....../","..XX/..X./..X./.XX./.X../.X../XX../","X....../XXXX.../...XXXX/......X/"},
				{"XX./X../X../XX./.X./.X./.XX/","...XXXX/XXXX..X/X....../","XX./.X./.X./.XX/..X/..X/.XX/","......X/X..XXXX/XXXX.../",".XX/..X/..X/.XX/.X./.X./XX./","X....../XXXX..X/...XXXX/",".XX/.X./.X./XX./X../X../XX./","XXXX.../X..XXXX/......X/"},
				{".XX./.XX./.XX./XXXX/","X.../XXXX/XXXX/X.../","XXXX/.XX./.XX./.XX./","...X/XXXX/XXXX/...X/"},
				{"....X/XXXXX/X..../X..../XX.../","XXXX./X..X./...X./...X./...XX/","...XX/....X/....X/XXXXX/X..../","XX.../.X.../.X.../.X..X/.XXXX/","X..../XXXXX/....X/....X/...XX/","...XX/...X./...X./X..X./XXXX./","XX.../X..../X..../XXXXX/....X/",".XXXX/.X..X/.X.../.X.../XX.../"},
				{"XXXXX/X...X/X..../XX.../","XXXX/X..X/...X/...X/..XX/","...XX/....X/X...X/XXXXX/","XX../X.../X.../X..X/XXXX/","XXXXX/X...X/....X/...XX/","..XX/...X/...X/X..X/XXXX/","XX.../X..../X...X/XXXXX/","XXXX/X..X/X.../X.../XX../"},
				{"XX/.X/.X/XX/X./X./XX/","XXXX..X/X..XXXX/","XX/X./X./XX/.X/.X/XX/","X..XXXX/XXXX..X/"},
				{"XX./.X./.X./.X./.X./.X./.X./.XX/",".......X/XXXXXXXX/X......./",".XX/.X./.X./.X./.X./.X./.X./XX./","X......./XXXXXXXX/.......X/"},
				{"XX/X./X./X./X./X./X./XX/","XXXXXXXX/X......X/","XX/.X/.X/.X/.X/.X/.X/XX/","X......X/XXXXXXXX/"},
		};
		return shape;
	}

	public int[] allow_yobun = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

}
