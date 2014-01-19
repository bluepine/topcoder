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

using namespace std;


class StarAdventure {
public:
    int mostStars(vector <string> level) {
        int M = level.size();
        int N = level[0].size();
        vector<vector<vector<int> > > dp1(N, vector<vector<int> >(N, vector<int>(N, 0)));
        for (int t = 1; t <= M+N-2; t++) {
            vector<vector<vector<int> > > dp2(N, vector<vector<int> >(N, vector<int>(N, 0)));
            for (int aj = 0; aj < N; aj++) {
                for (int bj = 0; bj < N; bj++) {
                    for (int cj = 0; cj < N; cj++) {
                        int ai = t-aj, bi=t-bj, ci=t-cj;
                        if (ai < 0 || bi < 0 || ci < 0 || ai >= M || bi >= M || ci >= M) continue;
                        int d = level[ai][aj]-'0';
                        if (bj != aj) d += level[bi][bj]-'0';
                        if (cj != aj && cj!= bj) d += level[ci][cj]-'0';
                        dp2[aj][bj][cj] = dp1[aj][bj][cj];
                        if (aj > 0 && bj > 0 && cj > 0) dp2[aj][bj][cj] = max(dp2[aj][bj][cj], dp1[aj-1][bj-1][cj-1]);
                        if (aj > 0 && bj > 0) dp2[aj][bj][cj] = max(dp2[aj][bj][cj], dp1[aj-1][bj-1][cj]);
                        if (aj > 0 && cj > 0) dp2[aj][bj][cj] = max(dp2[aj][bj][cj], dp1[aj-1][bj][cj-1]);
                        if (bj > 0 && cj > 0) dp2[aj][bj][cj] = max(dp2[aj][bj][cj], dp1[aj][bj-1][cj-1]);
                        if (aj > 0) dp2[aj][bj][cj] = max(dp2[aj][bj][cj], dp1[aj-1][bj][cj]);
                        if (bj > 0) dp2[aj][bj][cj] = max(dp2[aj][bj][cj], dp1[aj][bj-1][cj]);
                        if (cj > 0) dp2[aj][bj][cj] = max(dp2[aj][bj][cj], dp1[aj][bj][cj-1]);
                        dp2[aj][bj][cj] += d;
                    }
                }
            }
            dp1 = dp2;
        }

        //for (int a = 0; a < N; a++)
        //    for (int b = 0; b < N; b++)
        //        for (int c = 0; c < N; c++)
        //            cout << "[" << a << "," << b << "," << c << "]:" << dp1[a][b][c] << endl;
        return dp1[N-1][N-1][N-1];
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <string> p0, bool hasAnswer, int p1) {
    cout << "Test " << testNum << ": [" << "{";
    for (int i = 0; int(p0.size()) > i; ++i) {
        if (i > 0) {
            cout << ",";
        }
        cout << "\"" << p0[i] << "\"";
    }
    cout << "}";
    cout << "]" << endl;
    StarAdventure *obj;
    int answer;
    obj = new StarAdventure();
    clock_t startTime = clock();
    answer = obj->mostStars(p0);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << p1 << endl;
    }
    cout << "Your answer:" << endl;
    cout << "\t" << answer << endl;
    if (hasAnswer) {
        res = answer == p1;
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

    vector <string> p0;
    int p1;

    {
        // ----- test 0 -----
        string t0[] = {"01","11"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 3;
        all_right = KawigiEdit_RunTest(0, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        string t0[] = {"0999999999","9999999999","9999999999","9999999999","9999999999","9999999999","9999999999","9999999999","9999999999","9999999999"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 450;
        all_right = KawigiEdit_RunTest(1, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        string t0[] = {"012","012","012","012","012","012","012"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 21;
        all_right = KawigiEdit_RunTest(2, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 3 -----
        string t0[] = {"0123456789","1123456789","2223456789","3333456789","4444456789","5555556789","6666666789","7777777789","8888888889","9999999999"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 335;
        all_right = KawigiEdit_RunTest(3, p0, true, p1) && all_right;
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