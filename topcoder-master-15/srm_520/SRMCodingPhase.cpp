// BEGIN CUT HERE
/*
// SRM 520 Div2 Medium (500)

問題

3問の問題を75分以内に解くと得点が与えられる。
解くのに skills[i] 分かかる。
解くと point[i] 点もらえる。
luck を使うと解く時間を短縮できる。
得られる最大の得点を求める。

#line 88 "SRMCodingPhase.cpp"
*/
// END CUT HERE
#include <math.h>
#include <algorithm>
#include <list>
#include <map>
#include <set>
#include <string>
#include <vector>

using namespace std;

typedef long long LL;
typedef vector<int> IntVec;
typedef vector<string> StrVec;

class SRMCodingPhase {

public:
	int countScore(vector <int> points, vector <int> skills, int luck) {
		int result = 0;

		int la, lb, lc;
		for (la = 0; la <= luck; ++la) {
			int ta = max(1, skills[0] - la);
			int pa = points[0] - 2 * ta;
			int lbm = luck - la;
			for (lb = 0; lb <= lbm; ++lb) {
				int tb = max(1, skills[1] - lb);
				int pb = points[1] - 4 * tb;
				lc = lbm - lb;
				int tc = max(1, skills[2] - lc);
				int pc = points[2] - 8 * tc;
				if (ta <= 75) {
					result = max(result, pa);
				}
				if (tb <= 75) {
					result = max(result, pb);
				}
				if (tc <= 75) {
					result = max(result, pc);
				}
				if ((ta + tb) <= 75) {
					result = max(result, pa + pb);
				}
				if ((ta + tc) <= 75) {
					result = max(result, pa + pc);
				}
				if ((tb + tc) <= 75) {
					result = max(result, pb + pc);
				}
				if ((ta + tb + tc) <= 75) {
					result = max(result, pa + pb + pc);
				}
			}
		}

		return result;
	}
};

// BEGIN CUT HERE
template <typename T> static T __str_to_val(const char *p)
{
	return p;
}
template <> static int __str_to_val(const char *p)
{
	return atoi(p);
}
template <typename T> vector<T> getVector(const char *s)
{
	static const int buffer_size = 1024000;
	static char buffer[buffer_size];
	strcpy(buffer, s);
	vector <T> v;
	char *p = strtok(buffer, " ,");
	while (p) {
		v.push_back(__str_to_val<T>(p));
		p = strtok(NULL, " ,");
	}
	return v;
}

static void Test(const char *points, const char *skills, int luck, int expected)
{
	vector <int> pv = getVector<int>(points);
	vector <int> sv = getVector<int>(skills);
	SRMCodingPhase ___test;
	int result = ___test.countScore(pv, sv, luck);
	printf("result: %s, %d\n", result == expected ? "OK" : "FAILED", result);
}

int main() {
	// example 0
	Test("250, 500, 1000", "10, 25, 40", 0, 1310);

	// example 1
	Test("300, 600, 900", "30, 65, 90", 25, 680);

	// example 2
	Test("250, 550, 950", "10, 25, 40", 75, 1736);

	// example 3
	Test("256, 512, 1024", "35, 30, 25", 0, 1216);

	// example 4
	Test("300, 600, 1100", "80, 90, 100", 4, 0);

	return 0;
}
// END CUT HERE
