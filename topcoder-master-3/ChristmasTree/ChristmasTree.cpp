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
#include <climits>

using namespace std;

long long dp[11][51][51][51];

class ChristmasTree {
public:
    long long decorationWays(int L, int R, int G, int B) {
        for (int r = 0; r <= R; r++)
            for (int g = 0; g <= G; g++)
                for (int b = 0; b <= B; b++)
                    dp[0][r][g][b] = 1ll;
        for (int l = 1; l <= L; l++) {
            for (int r = 0; r <= R; r++) {
                for (int g = 0; g <= G; g++) {
                    for (int b = 0; b <= B; b++) {
                        dp[l][r][g][b] = get(l-1, r-l, g, b) + get(l-1, r, g-l, b) + get(l-1, r, g, b-l);
                        if (l%2 == 0) dp[l][r][g][b] += P(l)/P(l/2)/P(l/2)*(get(l-1, r-l/2, g-l/2, b) + get(l-1, r, g-l/2, b-l/2) + get(l-1, r-l/2, g, b-l/2)); 
                        if (l%3 == 0) dp[l][r][g][b] += P(l)/P(l/3)/P(l/3)/P(l/3)*(get(l-1, r-l/3, g-l/3, b-l/3));
                    }
                }
            }
        }
        return dp[L][R][G][B];
    }

    long long get(int l, int r, int g, int b) {
        if (l < 0 || r < 0 || g < 0 || b < 0) return 0ll;
        return dp[l][r][g][b];
    }

    long long P(int n) {
        long long res = 1ll;
        while (n > 0) res *= n, n--;
        return res;
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, int p0, int p1, int p2, int p3, bool hasAnswer, long long p4) {
    cout << "Test " << testNum << ": [" << p0 << "," << p1 << "," << p2 << "," << p3;
    cout << "]" << endl;
    ChristmasTree *obj;
    long long answer;
    obj = new ChristmasTree();
    clock_t startTime = clock();
    answer = obj->decorationWays(p0, p1, p2, p3);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << p4 << endl;
    }
    cout << "Your answer:" << endl;
    cout << "\t" << answer << endl;
    if (hasAnswer) {
        res = answer == p4;
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

    int p0;
    int p1;
    int p2;
    int p3;
    long long p4;

    {
    // ----- test 0 -----
    p0 = 2;
    p1 = 1;
    p2 = 1;
    p3 = 1;
    p4 = 6ll;
    all_right = KawigiEdit_RunTest(0, p0, p1, p2, p3, true, p4) && all_right;
    // ------------------
    }

    {
    // ----- test 1 -----
    p0 = 2;
    p1 = 2;
    p2 = 1;
    p3 = 0;
    p4 = 3ll;
    all_right = KawigiEdit_RunTest(1, p0, p1, p2, p3, true, p4) && all_right;
    // ------------------
    }

    {
    // ----- test 2 -----
    p0 = 3;
    p1 = 2;
    p2 = 2;
    p3 = 1;
    p4 = 0ll;
    all_right = KawigiEdit_RunTest(2, p0, p1, p2, p3, true, p4) && all_right;
    // ------------------
    }

    {
    // ----- test 3 -----
    p0 = 3;
    p1 = 2;
    p2 = 2;
    p3 = 2;
    p4 = 36ll;
    all_right = KawigiEdit_RunTest(3, p0, p1, p2, p3, true, p4) && all_right;
    // ------------------
    }

    {
    // ----- test 4 -----
    p0 = 8;
    p1 = 1;
    p2 = 15;
    p3 = 20;
    p4 = 197121ll;
    all_right = KawigiEdit_RunTest(4, p0, p1, p2, p3, true, p4) && all_right;
    // ------------------
    }

    {
        // ----- test 5 -----
        p0 = 1;
        p1 = 1;
        p2 = 2;
        p3 = 0;
        p4 = 2ll;
        all_right = KawigiEdit_RunTest(5, p0, p1, p2, p3, true, p4) && all_right;
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
