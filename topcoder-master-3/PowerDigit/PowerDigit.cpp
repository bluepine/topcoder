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
#include <limits>

using namespace std;

#define MAX 100000LL

class PowerDigit {
public:
    int digitK(int x, int y, int k) {
        if (x == 0) return (k==0)?0:-1;
        long long p = 1LL;
        int i = 0;
        while (i < y && p < MAX) p *= x, i++;
       
        if (i < y) {
            p = 1LL;
            long long z = x;
            while (y > 0) {
                if (y%2 == 1) p = (p*z)%MAX;
                z = (z*z)%MAX;
                y /= 2;
            }
        }

        while (k > 0) p /= 10, k--;
        if (p==0 && i==y) return -1;
        return p%10;
    }
};

// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, int p0, int p1, int p2, bool hasAnswer, int p3) {
    cout << "Test " << testNum << ": [" << p0 << "," << p1 << "," << p2;
    cout << "]" << endl;
    PowerDigit *obj;
    int answer;
    obj = new PowerDigit();
    clock_t startTime = clock();
    answer = obj->digitK(p0, p1, p2);
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

    int p0;
    int p1;
    int p2;
    int p3;

    {
        // ----- test 0 -----
        p0 = 2;
        p1 = 10;
        p2 = 1;
        p3 = 2;
        all_right = KawigiEdit_RunTest(0, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        p0 = 2;
        p1 = 10;
        p2 = 4;
        p3 = -1;
        all_right = KawigiEdit_RunTest(1, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        p0 = 2;
        p1 = 1000;
        p2 = 0;
        p3 = 6;
        all_right = KawigiEdit_RunTest(2, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 3 -----
        p0 = 9999;
        p1 = 10000;
        p2 = 4;
        p3 = 0;
        all_right = KawigiEdit_RunTest(3, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 4 -----
        p0 = 9731;
        p1 = 1;
        p2 = 4;
        p3 = -1;
        all_right = KawigiEdit_RunTest(4, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 5 -----
        p0 = 0;
        p1 = 969;
        p2 = 0;
        p3 = 0;
        all_right = KawigiEdit_RunTest(4, p0, p1, p2, true, p3) && all_right;
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