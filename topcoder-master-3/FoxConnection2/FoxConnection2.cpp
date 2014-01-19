#include <vector>
#include <list>
#include <map>
#include <set>
#include <queue>
#include <deque>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include <climits>

using namespace std;

const int MOD = 1000000007;
int N;
int K;
int adj[51][51];
long long dp[51][51];
int visit[51];

class FoxConnection2 {
public:
	int ways(vector <int> A, vector <int> B, int k) {
        N = A.size()+1;
        K = k;
     
        memset(adj, 0, sizeof(adj));
        for (int i = 0; i < N-1; i++) {
            adj[A[i]][B[i]] = 1;
            adj[B[i]][A[i]] = 1;
        }

        memset(dp, 0, sizeof(dp));
        memset(visit, 0, sizeof(visit));
        go(1);

        long long res = 0ll;
        for (int i = 1; i <= N; i++)
            res = (res+dp[i][k])%MOD;
        return (int)res;
	}

    void go(int node) {
        visit[node] = 1;
        dp[node][0] = dp[node][1] = 1ll;
        for (int child = 1; child <= N; child++) {
            if (adj[node][child] && !visit[child]) {
                go(child);
                for (int i = K; i > 1; i--)
                    for (int j = 1; j < i; j++)
                        dp[node][i] = (dp[node][i]+(dp[node][i-j]*dp[child][j])%MOD)%MOD;
            }
        }
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <int> p0, vector <int> p1, int p2, bool hasAnswer, int p3) {
	cout << "Test " << testNum << ": [" << "{";
	for (int i = 0; int(p0.size()) > i; ++i) {
		if (i > 0) {
			cout << ",";
		}
		cout << p0[i];
	}
	cout << "}" << "," << "{";
	for (int i = 0; int(p1.size()) > i; ++i) {
		if (i > 0) {
			cout << ",";
		}
		cout << p1[i];
	}
	cout << "}" << "," << p2;
	cout << "]" << endl;
	FoxConnection2 *obj;
	int answer;
	obj = new FoxConnection2();
	clock_t startTime = clock();
	answer = obj->ways(p0, p1, p2);
	clock_t endTime = clock();
	delete obj;
	bool res;
	res = true;
	cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
	if (hasAnswer) {
		cout << "Desired answer:" << endl;
		cout << "\t" << p3 << endl;
	}
	cout << "Your answer:" << endl;
	cout << "\t" << answer << endl;
	if (hasAnswer) {
		res = answer == p3;
	}
	if (!res) {
		cout << "DOESN'T MATCH!!!!" << endl;
	} else if (double(endTime - startTime) / CLOCKS_PER_SEC >= 2) {
		cout << "FAIL the timeout" << endl;
		res = false;
	} else if (hasAnswer) {
		cout << "Match :-)" << endl;
	} else {
		cout << "OK, but is it right?" << endl;
	}
	cout << "" << endl;
	return res;
}
int main() {
	bool all_right;
	all_right = true;
	
	vector <int> p0;
	vector <int> p1;
	int p2;
	int p3;
	
    {
        // ----- test 0 -----
        int t0[] = {1,2,3};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {2,3,4};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 2;
        p3 = 3;
        all_right = KawigiEdit_RunTest(0, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        int t0[] = {1,1,1,1};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {2,3,4,5};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 3;
        p3 = 6;
        all_right = KawigiEdit_RunTest(1, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        int t0[] = {1,2,3,4};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {2,3,4,5};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 3;
        p3 = 3;
        all_right = KawigiEdit_RunTest(2, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 3 -----
        int t0[] = {1,2,2,4,4};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {2,3,4,5,6};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 3;
        p3 = 6;
        all_right = KawigiEdit_RunTest(3, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 4 -----
        int t0[] = {1,2,2,4,4};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {2,3,4,5,6};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 5;
        p3 = 4;
        all_right = KawigiEdit_RunTest(4, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 5 -----
        int t0[] = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 20;
        p3 = 923263934;
        all_right = KawigiEdit_RunTest(5, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 6 -----
        int t0[] = {2};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {1};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 1;
        p3 = 2;
        all_right = KawigiEdit_RunTest(6, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 7 -----
        int t0[] = {6, 10, 12, 7, 13, 2, 16, 5, 11, 1, 3, 14, 4, 8, 15};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {9, 9, 10, 6, 6, 13, 10, 2, 10, 7, 1, 12, 1, 11, 14};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 6;
        p3 = 47;
        all_right = KawigiEdit_RunTest(7, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }


    if (all_right) {
        cout << "You're a stud (at least on the example cases)!" << endl;
    } else {
        cout << "Some of the test cases had errors." << endl;
    }
    return 0;
}
// END KAWIGIEDIT TESTING
//Powered by KawigiEdit 2.1.8 (beta) modified by pivanof!