import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Planarity {
	public static long _start_time;

	public static double PI = Math.PI;
	public static double PI180 = Math.PI / 180.0f;

	public int intersects_common(int V, int a, int b, int[] result,
			boolean linkmap[][]) {
		int sects = 0;

		for (int j = 0; j < V; j++) {
			if (!linkmap[a][j])
				continue;
			if (linkmap[a][j] && linkmap[b][j])
				continue;
			for (int k = 0; k < V; k++) {
				if (a == k || j == k)
					continue;
				for (int l = k + 1; l < V; l++) {
					if (!linkmap[k][l])
						continue;
					if (a == l || j == l)
						continue;
					if (intersect(result[a * 2], result[a * 2 + 1],
							result[j * 2], result[j * 2 + 1], result[k * 2],
							result[k * 2 + 1], result[l * 2], result[l * 2 + 1])) {
						sects++;
					}
				}
			}
		}

		for (int j = 0; j < V; j++) {
			if (!linkmap[b][j])
				continue;
			if (linkmap[a][j] && linkmap[b][j])
				continue;
			for (int k = 0; k < V; k++) {
				if (b == k || j == k)
					continue;
				for (int l = k + 1; l < V; l++) {
					if (!linkmap[k][l])
						continue;
					if (b == l || j == l)
						continue;
					if (intersect(result[b * 2], result[b * 2 + 1],
							result[j * 2], result[j * 2 + 1], result[k * 2],
							result[k * 2 + 1], result[l * 2], result[l * 2 + 1])) {
						sects++;
					}
				}
			}
		}

		return sects;
	}

	public int intersects_num(int V, int[] result, boolean linkmap[][],
			boolean[] finished_link) {
		int sects = 0;
		for (int i = 0; i < V; i++) {
			for (int j = i + 1; j < V; j++) {
				if (!linkmap[i][j] || finished_link[i * 100 + j])
					continue;
				for (int k = 0; k < V; k++) {
					for (int l = k + 1; l < V; l++) {
						if (!linkmap[k][l] || finished_link[k * 100 + l])
							continue;
						if (i == k || i == l || j == k || j == l)
							continue;
						if (intersect(result[i * 2], result[i * 2 + 1],
								result[j * 2], result[j * 2 + 1],
								result[k * 2], result[k * 2 + 1],
								result[l * 2], result[l * 2 + 1])) {
							sects++;
						}
					}
				}
			}
		}
		return sects;
	}

	public int intersects_edge(int V, int i, int j, int[] result,
			boolean linkmap[][]) {
		int sects = 0;
		for (int k = 0; k < V; k++) {
			for (int l = k + 1; l < V; l++) {
				if (!linkmap[k][l])
					continue;
				if (i == k || i == l || j == k || j == l)
					continue;
				if (intersect(result[i * 2], result[i * 2 + 1], result[j * 2],
						result[j * 2 + 1], result[k * 2], result[k * 2 + 1],
						result[l * 2], result[l * 2 + 1])) {
					sects++;
				}
			}
		}
		return sects;
	}

	public int intersects_point(int V, int i, int[] result, boolean linkmap[][]) {
		int sects = 0;
		for (int j = 0; j < V; j++) {
			if (j != i && linkmap[i][j]) {
				sects += intersects_edge(V, i, j, result, linkmap);
			}
		}
		return sects;
	}

	public int[] untangle(int V, int[] edges) {
		_start_time = System.currentTimeMillis();
		int E = edges.length / 2;
		int result[] = new int[V * 2];

		int linked[] = new int[V];
		int sorted_linked[];
		boolean linkmap[][] = new boolean[V][V];
		for (int i = 0; i < E; i++) {
			linkmap[edges[i * 2]][edges[i * 2 + 1]] = true;
			linkmap[edges[i * 2 + 1]][edges[i * 2]] = true;
			linked[edges[i * 2]]++;
			linked[edges[i * 2 + 1]]++;
		}
		sorted_linked = linked.clone();
		sort(sorted_linked);
		int swp[] = new int[V];
		HashSet<Integer> coll = new HashSet<Integer>();
		for (int i = 0; i < V; i++) {
			int value = sorted_linked[i];
			for (int j = 0; j < V; j++) {
				if (value == linked[j] && !coll.contains(j)) {
					coll.add(j);
					swp[j] = i;
					break;
				}
			}
		}

		PriorityQueue<State> queue = new PriorityQueue<State>(2000000,
				new Comparator() {
					public int compare(Object o1, Object o2) {
						State s1 = (State) o1;
						State s2 = (State) o2;
						return (int) (s2.score - s1.score);
					}
				});

		try {
			for (int id = V - 1; id >= 0; id--) {
				for (int jd = id - 1; jd >= 0; jd--) {
					int i = swp[id];
					int j = swp[jd];
					if (linkmap[i][j]) {
						for (int kd = jd - 1; kd >= 0; kd--) {
							int k = swp[kd];
							if (linkmap[j][k] && linkmap[k][i]) {
								HashSet<Point> fixed_point = new HashSet<Point>();
								Point a = new Point(350, 650, i, true);
								Point b = new Point(50, 50, j, true);
								Point c = new Point(650, 50, k, true);
								fixed_point.add(a);
								fixed_point.add(b);
								fixed_point.add(c);

								boolean[] finished_link = new boolean[10000];
								finished_link[i * 100 + j] = true;
								finished_link[i * 100 + k] = true;

								finished_link[j * 100 + i] = true;
								finished_link[j * 100 + k] = true;

								finished_link[k * 100 + i] = true;
								finished_link[k * 100 + j] = true;

								ArrayList<Triangle> active_triangles = new ArrayList<Triangle>();
								active_triangles.add(new Triangle(a, b, c, 0));

								queue.add(new State(active_triangles,
										fixed_point, finished_link, 0, 0));
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}

		int min_intersects = -1;
		int[] min_results = new int[V * 2];
		boolean[] min_is_vertexes = new boolean[V];
		int xx = 31;
		if (V > 25) {
			xx = 15;
		}
		int xxx = xx + 1;

		try {
			int ct = 0;
			while (queue.size() > 0) {
				if (System.currentTimeMillis() - _start_time > 2000) {
					throw new Exception();
				}
				ct++;
				State state = queue.poll();
				HashSet<Point> fixed_point = state.fixed_point;
				ArrayList<Triangle> active_triangles = state.active_triangles;
				boolean[] finished_link = state.finished_link;
				boolean changed_once = false;

				try {
					for (int idx = 0; idx < V; idx++) {
						int i = swp[idx];
						boolean ok = true;
						for (Point pt : fixed_point) {
							if (pt.idx == i) {
								ok = false;
								break;
							}
						}
						if (!ok)
							continue;

						int active_size = active_triangles.size();
						boolean added_queue = false;

						for (int tritri = 0; tritri < active_size; tritri++) {
							Triangle triangle = active_triangles.get(tritri);
							if (triangle.depth >= 32)
								continue;
							for (int vertex = 0; vertex < 3; vertex++) {
								Point a, b, c;
								switch (vertex) {
								case 0:
									a = triangle.a;
									b = triangle.b;
									c = triangle.c;
									break;
								case 1:
									a = triangle.a;
									b = triangle.c;
									c = triangle.b;
									break;
								default:
									a = triangle.b;
									b = triangle.c;
									c = triangle.a;
									break;
								}

								if (linkmap[i][a.idx] && linkmap[i][b.idx]) {
									int bx = (a.x + b.x) / 2;
									int by = (a.y + b.y) / 2;
									int dx = (bx * xx + c.x) / xxx;
									int dy = (by * xx + c.y) / xxx;
									Point new_point = new Point(dx, dy, i,
											false);
									HashSet<Point> new_fixed = (HashSet<Point>) fixed_point
											.clone();
									new_fixed.add(new_point);

									boolean[] new_finished = finished_link
											.clone();
									new_finished[i * 100 + a.idx] = true;
									new_finished[i * 100 + b.idx] = true;
									new_finished[a.idx * 100 + i] = true;
									new_finished[b.idx * 100 + i] = true;

									ArrayList<Triangle> new_active_triangles = (ArrayList<Triangle>) active_triangles
											.clone();
									new_active_triangles.remove(tritri);
									new_active_triangles.add(new Triangle(a, c,
											new_point, triangle.depth));
									new_active_triangles.add(new Triangle(b, c,
											new_point, triangle.depth));
									queue.add(new State(new_active_triangles,
											new_fixed, new_finished,
											state.score, state.depth));
									added_queue = true;
									changed_once = true;
								}
								if (linkmap[i][c.idx]) {
									int bx = (a.x + b.x) / 2;
									int by = (a.y + b.y) / 2;
									Point new_point = new Point((bx + c.x * xx)
											/ xxx + 1, (by + c.y * xx) / xxx,
											i, true);
									HashSet<Point> new_fixed = (HashSet<Point>) fixed_point
											.clone();
									new_fixed.add(new_point);

									boolean[] new_finished = finished_link
											.clone();
									new_finished[i * 100 + c.idx] = true;
									new_finished[c.idx * 100 + i] = true;

									ArrayList<Triangle> new_active_triangles = (ArrayList<Triangle>) active_triangles
											.clone();
									Triangle tri = new_active_triangles.get(
											tritri).clone();
									new_active_triangles.remove(tritri);
									switch (vertex) {
									case 0:
										tri.c = new_point.clone();
										break;
									case 1:
										tri.b = new_point.clone();
										break;
									default:
										tri.a = new_point.clone();
										break;
									}
									tri.depth++;
									new_active_triangles.add(tri);

									queue.add(new State(new_active_triangles,
											new_fixed, new_finished,
											state.score, state.depth));
									added_queue = true;
									changed_once = true;
								}
							}
						}
					}
				} catch (Exception e) {
				}

				if (!changed_once) {
					int test_results[] = new int[V * 2];
					boolean is_vertexes[] = new boolean[V];
					boolean flag = false;
					int intersects = 0;
					for (Point pt : fixed_point) {
						test_results[pt.idx * 2] = pt.x;
						test_results[pt.idx * 2 + 1] = pt.y;
						is_vertexes[pt.idx] = pt.is_vertex;
					}
					intersects += intersects_num(V, test_results, linkmap,
							finished_link);
					if (min_intersects == -1 || intersects < min_intersects) {
						min_intersects = intersects;
						min_results = test_results.clone();
						min_is_vertexes = is_vertexes.clone();
					}
				}
			}
		} catch (Exception e) {
		}

		if (min_intersects != -1) {
			result = min_results;
		}

		for (int i = 0; i < V; i++) {
			if (result[i * 2] == 0) {
				int allx = 0, ally = 0, num = 0;
				for (int j = 0; j < V; j++) {
					if (linkmap[i][j] && result[j * 2] > 0) {
						allx += result[j * 2];
						ally += result[j * 2 + 1];
						num++;
					}
				}
				if (num == 0) {
					result[i * 2] = 0;
					result[i * 2 + 1] = (int) (Math.random() * 700);
				} else if (num == 1) {
					result[i * 2] = allx - 1;
					result[i * 2 + 1] = ally;
				} else {
					result[i * 2] = allx / num;
					result[i * 2 + 1] = ally / num;
				}
			}
		}

		int gravity_tries = 15;
		int improved_sects = 0;
		while (gravity_tries-- > 0) {
			boolean is_changed = false;
			for (int i = 0; i < V; i++) {
				if (linked[i] >= 1) {
					int intersects = intersects_point(V, i, result, linkmap);
					int from_x = result[i * 2];
					int from_y = result[i * 2 + 1];
					int allx = 0, ally = 0, num = 0;
					for (int j = 0; j < V; j++) {
						if (linkmap[i][j]) {
							allx += result[j * 2];
							ally += result[j * 2 + 1];
							num++;
						}
					}
					if (num >= 2) {
						result[i * 2] = allx / num;
						result[i * 2 + 1] = ally / num;
						int intersects_after = intersects_point(V, i, result,
								linkmap);
						if (intersects > intersects_after) {
							improved_sects += (intersects - intersects_after);
							is_changed = true;
						} else {
							result[i * 2] = from_x;
							result[i * 2 + 1] = from_y;
						}
					} else {
						result[i * 2] = allx + 1;
						result[i * 2 + 1] = ally;
						boolean same = false;
						for (int j = 0; j < V; j++) {
							if (result[i * 2] == result[j * 2]
									&& result[i * 2 + 1] == result[j * 2 + 1]) {
								same = true;
								break;
							}
						}
						if (!same) {
							int intersects_after = intersects_point(V, i,
									result, linkmap);
							if (intersects > intersects_after) {
								improved_sects += (intersects - intersects_after);
								is_changed = true;
							} else {
								result[i * 2] = from_x;
								result[i * 2 + 1] = from_y;
							}
						} else {
							result[i * 2] = from_x;
							result[i * 2 + 1] = from_y;
						}
					}
				}
			}
			if (!is_changed) {
				break;
			}
		}

		improved_sects = 0;
		for (int i = 0; i < V; i++) {
			if (min_is_vertexes[i]) {
				int intersects = intersects_point(V, i, result, linkmap);
				int from_x = result[i * 2];
				int from_y = result[i * 2 + 1];
				int max_dx = 0, max_dy = 0;
				int max_improvement = 0;
				for (int dy = -3; dy < 3; dy++) {
					for (int dx = -3; dx < 3; dx++) {
						result[i * 2] = from_x + dx;
						result[i * 2 + 1] = from_y + dy;
						int intersects_after = intersects_point(V, i, result,
								linkmap);
						if (max_improvement < intersects - intersects_after) {
							max_dx = dx;
							max_dy = dy;
							max_improvement = intersects - intersects_after;
						}
					}
				}
				improved_sects += max_improvement;
				result[i * 2] = from_x + max_dx;
				result[i * 2 + 1] = from_y + max_dy;
			}
		}

		if (V > 40) {
			improved_sects = 0;
			try {
				while (true) {
					for (int i = 0; i < V; i++) {
						for (int j = i + 1; j < V; j++) {
							if ((System.currentTimeMillis() - _start_time) > 9800) {
								throw new Exception();
							}
							int ct_before = intersects_common(V, i, j, result,
									linkmap);
							int tempx = result[i * 2];
							result[i * 2] = result[j * 2];
							result[j * 2] = tempx;
							int tempy = result[i * 2 + 1];
							result[i * 2 + 1] = result[j * 2 + 1];
							result[j * 2 + 1] = tempy;
							int ct_after = intersects_common(V, i, j, result,
									linkmap);

							if (ct_after < ct_before) {
								improved_sects += (ct_before - ct_after);
							} else {
								tempx = result[i * 2];
								result[i * 2] = result[j * 2];
								result[j * 2] = tempx;
								tempy = result[i * 2 + 1];
								result[i * 2 + 1] = result[j * 2 + 1];
								result[j * 2 + 1] = tempy;
							}
						}
					}
				}
			} catch (Exception e) {
			}
		} else {
			improved_sects = 0;
			PriorityQueue<RState> replace_queue = new PriorityQueue<RState>(
					2000000, new Comparator() {
						public int compare(Object o1, Object o2) {
							RState s1 = (RState) o1;
							RState s2 = (RState) o2;
							return (int) (s2.score - s1.score);
						}
					});
			replace_queue.add(new RState(0, result.clone()));
			int max_improvement = 0;
			try {
				while (replace_queue.size() > 0) {
					RState rstate = replace_queue.poll();
					for (int i = 0; i < V; i++) {
						for (int j = i + 1; j < V; j++) {
							if ((System.currentTimeMillis() - _start_time) > 9800) {
								throw new Exception();
							}
							int[] r_result = rstate.result.clone();
							int ct_before = intersects_common(V, i, j,
									r_result, linkmap);
							int tempx = r_result[i * 2];
							r_result[i * 2] = r_result[j * 2];
							r_result[j * 2] = tempx;
							int tempy = r_result[i * 2 + 1];
							r_result[i * 2 + 1] = r_result[j * 2 + 1];
							r_result[j * 2 + 1] = tempy;
							int ct_after = intersects_common(V, i, j, r_result,
									linkmap);

							int impl = rstate.score + (ct_before - ct_after);
							if (ct_after < ct_before) {
								if (max_improvement < impl) {
									max_improvement = impl;
									result = r_result.clone();
								}
							} else {
								continue;
							}
							replace_queue.add(new RState(impl, r_result));
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	// -----------//
	// Classes //
	// -----------//
	public class Point {
		int x;
		int y;
		int idx;
		boolean is_vertex;

		public Point(int xx, int yy, int index) {
			x = xx;
			y = yy;
			idx = index;
			is_vertex = false;
		}

		public Point(int xx, int yy, int index, boolean is) {
			x = xx;
			y = yy;
			idx = index;
			is_vertex = is;
		}

		public boolean equals(Object ano) {
			return (((Point) ano).idx == idx);
		}

		public Point clone() {
			return new Point(x, y, idx, is_vertex);
		}
	}

	public class Triangle {
		Point a, b, c;
		int depth;

		public Triangle(Point ta, Point tb, Point tc, int dep) {
			a = ta.clone();
			b = tb.clone();
			c = tc.clone();
			depth = dep + 1;
		}

		public boolean equals(Object another) {
			Triangle ano = (Triangle) another;
			if ((ano.a.equals(a) && ano.b.equals(b) && ano.c.equals(c))
					|| (ano.a.equals(a) && ano.b.equals(c) && ano.c.equals(b))
					|| (ano.a.equals(b) && ano.b.equals(a) && ano.c.equals(c))
					|| (ano.a.equals(b) && ano.b.equals(c) && ano.c.equals(a))
					|| (ano.a.equals(c) && ano.b.equals(a) && ano.c.equals(b))
					|| (ano.a.equals(c) && ano.b.equals(b) && ano.c.equals(a))) {
				return true;
			}
			return false;
		}

		public Triangle clone() {
			return new Triangle(a, b, c, depth - 1);
		}
	}

	public class State {
		ArrayList<Triangle> active_triangles;
		HashSet<Point> fixed_point;
		boolean[] finished_link;
		int score;
		int depth;

		public State(ArrayList<Triangle> triangles, HashSet<Point> fixed,
				boolean[] finished, int sc, int dep) {
			active_triangles = triangles;
			fixed_point = fixed;
			finished_link = finished;
			score = sc;
			depth = dep + 1;
		}
	}

	public class RState {
		int score;
		int[] result;

		public RState(int sc, int[] res) {
			score = sc;
			result = res;
		}
	}

	// -----------//
	// Utils //
	// -----------//
	public static int dot(int p1x, int p1y, int p2x, int p2y) {
		return p1x * p2x + p1y * p2y;
	}

	public static double norm(int px, int py) {
		return Math.sqrt(px * px + py * py);
	}

	public static double dist(int p1x, int p1y, int p2x, int p2y) {
		return norm(p1x - p2x, p1y - p2y);
	}

	public static boolean eq(double a, double b) {
		return Math.abs(a - b) < 1e-9;
	}

	public static double dist(int p1x, int p1y, int p2x, int p2y, int pox,
			int poy) {
		if (dot(p2x - p1x, p2y - p1y, pox - p1x, poy - p1y) <= 0) {
			return dist(pox, poy, p1x, p1y);
		}
		if (dot(p2x - p1x, p2y - p1y, pox - p2x, poy - p2y) >= 0) {
			return dist(pox, poy, p2x, p2y);
		}

		// distance to the line itself
		return Math.abs((p1y - p2y) * pox + (p2x - p1x) * poy + p1x * p2y - p1y
				* p2x)
				/ norm(p1x - p2x, p1y - p2y);
	}

	public static double dist2(int p1x, int p1y, int p2x, int p2y, int p3x,
			int p3y, int p4x, int p4y) {
		double a = dist(p1x, p1y, p2x, p2y, p3x, p3y);
		double b = dist(p1x, p1y, p2x, p2y, p4x, p4y);
		return Math.min(a, b);
	}

	public static boolean intersect_re(int p1x, int p1y, int p2x, int p2y,
			int p3x, int p3y, int p4x, int p4y) {
		// do edges "this" and "other" intersect?
		if (Math.min(p1x, p2x) > Math.max(p3x, p4x))
			return false;
		if (Math.max(p1x, p2x) < Math.min(p3x, p4x))
			return false;
		if (Math.min(p1y, p2y) > Math.max(p3y, p4y))
			return false;
		if (Math.max(p1y, p2y) < Math.min(p3y, p4y))
			return false;

		int den = (p4y - p3y) * (p2x - p1x) - (p4x - p3x) * (p2y - p1y);
		if (den == 0) {
			return false;
		}

		int nu1 = (p4x - p3x) * (p1y - p3y) - (p4y - p3y) * (p1x - p3x);
		int nu2 = (p2x - p1x) * (p1y - p3y) - (p2y - p1y) * (p1x - p3x);
		double u1 = nu1 * 1. / den;
		double u2 = nu2 * 1. / den;
		if (u1 < 0 || u1 > 1 || u2 < 0 || u2 > 1) {
			return false;
		}
		return true;
	}

	public static boolean intersect(int p1x, int p1y, int p2x, int p2y,
			int p3x, int p3y, int p4x, int p4y) {
		// do edges "this" and "other" intersect?
		if (Math.min(p1x, p2x) > Math.max(p3x, p4x))
			return false;
		if (Math.max(p1x, p2x) < Math.min(p3x, p4x))
			return false;
		if (Math.min(p1y, p2y) > Math.max(p3y, p4y))
			return false;
		if (Math.max(p1y, p2y) < Math.min(p3y, p4y))
			return false;

		int den = (p4y - p3y) * (p2x - p1x) - (p4x - p3x) * (p2y - p1y);
		if (den == 0) {
			/**
			 * double d2self = dist2(p3x, p3y, p4x, p4y, p1x, p1y, p2x, p2y);
			 * double d2other = dist2(p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y);
			 * if (Math.min(d2self, d2other) > 0) { return false; }
			 */

			double thisnorm = dist(p1x, p1y, p2x, p2y);
			double othernorm = dist(p3x, p3y, p4x, p4y);
			double tonorm = thisnorm + othernorm;
			if ((p1x == p3x && p1y == p3y && eq(dist(p2x, p2y, p4x, p4y),
					tonorm))
					|| (p1x == p4x && p1y == p4y && eq(
							dist(p2x, p2y, p3x, p3y), tonorm))
					|| (p2x == p3x && p2y == p3y && eq(
							dist(p1x, p1y, p4x, p4y), tonorm))
					|| (p2x == p4x && p2y == p4y && eq(
							dist(p1x, p1y, p3x, p3y), tonorm))) {
				return false;
			}
			return true;
		}

		// common vertices
		/**
		 * if ((p1x == p3x) && (p1y == p3y)) { return false; } if ((p1x == p4x)
		 * && (p1y == p4y)) { return false; } if ((p2x == p3x) && (p2y == p3y))
		 * { return false; } if ((p2x == p4x) && (p2y == p4y)) { return false; }
		 */

		int nu1 = (p4x - p3x) * (p1y - p3y) - (p4y - p3y) * (p1x - p3x);
		int nu2 = (p2x - p1x) * (p1y - p3y) - (p2y - p1y) * (p1x - p3x);
		double u1 = nu1 * 1. / den;
		double u2 = nu2 * 1. / den;
		if (u1 < 0 || u1 > 1 || u2 < 0 || u2 > 1) {
			return false;
		}
		return true;
	}

	// -----------//
	// ForTest //
	// -----------//
	public static void main(String[] args) {
		BufferedReader in;
		BufferedWriter out;
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(System.out));
		try {
			int V = Integer.valueOf(in.readLine());
			int N = Integer.valueOf(in.readLine());
			int edges[] = new int[N];
			for (int i = 0; i < N; i++) {
				edges[i] = Integer.valueOf(in.readLine());
			}
			Planarity pla = new Planarity();
			int result[] = pla.untangle(V, edges);
			for (int ret : result) {
				out.write(ret + "\n");
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sort(int[] arr) {
		java.util.Arrays.sort(arr);
	}

	public void rsort(int[] arr) {
		int len = arr.length;
		java.util.Arrays.sort(arr);
		for (int i = 0; i < len / 2; i++) {
			int temp = arr[i];
			arr[i] = arr[len - i - 1];
			arr[len - i - 1] = temp;
		}
	}
}
