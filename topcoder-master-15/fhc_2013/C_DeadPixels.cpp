﻿
//
// C. Dead Pixels
//

/*

問題

W×Hの画素の液晶ディスプレイがある
不良な画素を求める式が与えられる
P×Qの範囲で不良な画素を含まない領域の総数を求める

*/

#include <algorithm>
#include <string>
#include <iostream>
#include <sstream>
#include <set>
#include <vector>

using namespace std;

typedef long long LL;
typedef set<int> IntSet;
typedef vector<int> IntVec;
typedef vector<string> StrVec;

int solve(int W, int H, int P, int Q, int N, int X, int Y, int A, int B, int C, int D)
{
	int ans = 0;
	IntSet s[40000];

	int i, m, ex, c, x = X, y = Y, p, q;
	for (i = 0; i < N; ++i) {
		s[y].insert(x);
		p = (x*A + y*B +1) % W;
		q = (x*C + y*D +1) % H;
		x = p, y = q;
	}

	IntSet::const_iterator cp[40000];
	IntSet::const_iterator ep[40000];
	for (int y = 0; y <= (H-Q); ++y) {
		x = 0;
		c = 0;
		for (i = 0; i < Q; ++i) {
			if (s[y+i].size() > 0) {
				cp[c] = s[y+i].begin();
				ep[c] = s[y+i].end();
				++c;
			}
		}
		while ((m = x + P) <= W) {
			ex = W;
			bool f = true;
			while (f) {
				f = false;
				for (i = 0; i < c; ++i) {
					while (cp[i] != ep[i]) {
						if (*(cp[i]) < x) {
							++(cp[i]);
							continue;
						}
						if ((*cp[i]) < m) {
							x = *(cp[i]) + 1;
							m = x + P;
							f = true;
							++(cp[i]);
							continue;
						}
						break;
					}
				}
			}
			for (i = 0; i < c; ++i) {
				if (cp[i] != ep[i]) {
					ex = min(ex, *cp[i]);
				}
			}

			ans += max(0, ex - m + 1);
			x = ex + 1;
		}
	}

	return ans;
}

int main(int argc, char *argv[])
{
	string s;
	getline(cin, s);
	int T = atoi(s.c_str());
	for (int t = 0; t < T; ++t) {
		getline(cin, s);
		stringstream ss(s);
		int W, H, P, Q, N, X, Y, a, b, c, d;
		ss >> W >> H >> P >> Q >> N >> X >> Y >> a >> b >> c >> d;
		unsigned int ans = solve(W, H, P, Q, N, X, Y, a, b, c, d);
		cout << "Case #" << (t+1) << ": " << ans << endl;
	}

	return 0;
}
