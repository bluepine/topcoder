﻿// BEGIN CUT HERE
/*
// SRM 541 Div1 Easy (250)

問題
直交座標系に蟻が何匹かいる。
蟻は縦または横に移動していて、全ての蟻の速度は同じである。
蟻同士が出会うと、その場で消滅する。
蟻の座標と向きが与えられる。
最終的に残る蟻の数を求める。

*/
// END CUT HERE
#include <algorithm>
#include <numeric>
#include <set>
#include <string>
#include <vector>
#include <iostream>
#include <sstream>

using namespace std;

typedef set<int> IntSet;

class AntsMeet {

public:
	int countAnts(vector <int> x, vector <int> y, string direction) {
		int N = x.size();
		int i, j, t;

		int f[64] = {}, dx[64] = {}, dy[64] = {};
		for (i = 0; i < N; ++i) {
			f[i] = 1;
			x[i] *= 2;
			y[i] *= 2;
			switch (direction[i]) {
				case 'N': dx[i] =  0, dy[i] =  1; break;
				case 'E': dx[i] =  1, dy[i] =  0; break;
				case 'S': dx[i] =  0, dy[i] = -1; break;
				case 'W': dx[i] = -1, dy[i] =  0; break;
			}
		}

#if 1
		for (t = 0; t < 4096; ++t) {
			for (i = 0; i < N; ++i) {
				x[i] += dx[i], y[i] += dy[i];
			}
			for (i = 0; i < N-1; ++i) {
				for (j = i+1; j < N; ++j) {
					if (f[i] && f[j] && x[i] == x[j] && y[i] == y[j]) {
						f[i] = -1, f[j] = -1;
					}
				}
			}
			for (i = 0; i < N; ++i) {
				if (f[i] < 0) {
					f[i] = 0;
				}
			}
		}
#else
		IntSet tv;
		for (i = 0; i < N; ++i) {
			for (j = i+1; j < N; ++j) {
				int xdiff = x[i] - x[j];
				int ydiff = y[i] - y[j];
				if (xdiff == 0) {
					int v = dy[i] - dy[j];
					if (v) {
						t = -ydiff / v;
						if (t > 0) {
							tv.insert(t);
						}
					}
				} else if (ydiff == 0) {
					int v = dx[i] - dx[j];
					if (v) {
						t = -xdiff / v;
						if (t > 0) {
							tv.insert(t);
						}
					}
				} else if (abs(xdiff) == abs(ydiff)) {
					tv.insert(abs(xdiff));
				}
			}
		}
		IntSet::const_iterator it;
		for (it = tv.begin(); it != tv.end(); ++it) {
			t = *it;
			for (i = 0; i < N-1; ++i) {
				for (j = i+1; j < N; ++j) {
					if (f[i] && f[j]) {
						if (((x[i] + dx[i]*t) == (x[j] + dx[j]*t)) &&
								((y[i] + dy[i]*t) == (y[j] + dy[j]*t))) {
							f[i] = -1, f[j] = -1;
						}
					}
				}
			}
			for (i = 0; i < N; ++i) {
				if (f[i] < 0) {
					f[i] = 0;
				}
			}
		}
#endif

		return accumulate(f, f+N, 0);
	}

	
// BEGIN CUT HERE
private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }

	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }

public:
	void run_test(int Case) { 
		int n = 0;

		// test_case_0
		if ((Case == -1) || (Case == n)){
			int Arr0[] = {0,10,20,30};
			int Arr1[] = {0,10,20,30};
			string Arg2 = "NWNE";
			int Arg3 = 2;

			vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0])));
			vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0])));
			verify_case(n, Arg3, countAnts(Arg0, Arg1, Arg2));
		}
		n++;

		// test_case_1
		if ((Case == -1) || (Case == n)){
			int Arr0[] = {-10,0,0,10};
			int Arr1[] = {0,-10,10,0};
			string Arg2 = "NEWS";
			int Arg3 = 0;

			vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0])));
			vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0])));
			verify_case(n, Arg3, countAnts(Arg0, Arg1, Arg2));
		}
		n++;

		// test_case_2
		if ((Case == -1) || (Case == n)){
			int Arr0[] = {-1,-1,-1,0,0,0,1,1,1};
			int Arr1[] = {-1,0,1,-1,0,1,-1,0,1};
			string Arg2 = "ESEWNNEWW";
			int Arg3 = 4;

			vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0])));
			vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0])));
			verify_case(n, Arg3, countAnts(Arg0, Arg1, Arg2));
		}
		n++;

		// test_case_3
		if ((Case == -1) || (Case == n)){
			int Arr0[] = {4,7,6,2,6,5,7,7,8,4,7,8,8,8,5,4,8,9,1,5,9,3,4,0,0,1,0,7,2,6,9,6,3,0,5,5,1,2,0,4,9,7,7,1,8,1,9,2,7,3};
			int Arr1[] = {2,3,0,6,8,4,9,0,5,0,2,4,3,8,1,5,0,7,3,7,0,9,8,1,9,4,7,8,1,1,6,6,6,2,8,5,1,9,0,1,1,1,7,0,2,5,4,7,5,3};
			string Arg2 = "SSNWSWSENSWSESWEWSWSENWNNNESWSWSWWSSWEEWWNWWWNWENN" ;
			int Arg3 = 25;

			vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0])));
			vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0])));
			verify_case(n, Arg3, countAnts(Arg0, Arg1, Arg2));
		}
		n++;

		// test_case_4
		if ((Case == -1) || (Case == n)){
			int Arr0[] = {478,-664,759,434,-405,513,565,-396,311,-174,56,993,251,-341,993,-112,242,129,383,513,-78,-341,-148,129,423
,493,434,-405,478,-148,929,251,56,242,929,-78,423,-664,802,251,759,383,-112,-591,-591,-248,660,660,735,493};
			int Arr1[] = {-186,98,948,795,289,-678,948,-170,-195,290,-354,-424,289,-157,-166,150,706,-678,684,-294,-234,36,36,-294,-216
,-234,427,945,265,-157,265,715,275,715,-186,337,798,-170,427,706,754,961,286,-216,798,286,961,684,-424,337};
			string Arg2 = "WNSNNSSWWWEENWESNSWSWSEWWEWEWWWNWESNSSNNSNNWWWNESE";
			int Arg3 = 44;

			vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0])));
			vector <int> Arg1(Arr1, Arr1 + (sizeof(Arr1) / sizeof(Arr1[0])));
			verify_case(n, Arg3, countAnts(Arg0, Arg1, Arg2));
		}
		n++;

	}

// END CUT HERE

};

// BEGIN CUT HERE
int main() {
	AntsMeet ___test;
	___test.run_test(-1);
	return 0;
}
// END CUT HERE
