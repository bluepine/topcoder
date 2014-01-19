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

#define MAX 1e9
#define EPS 1e-9

int N;
vector<int> xs;
vector<int> ys;

class TVTower {
public:
    double minRadius(vector<int> x, vector<int> y) {
        N = x.size();
        xs = x;
        ys = y;

        double res = MAX;
        for (int i = 0; i < N-1; i++) {
            for (int j = i+1; j < N; j++) {
                double cx = (xs[i]+xs[j])/2.0;
                double cy = (ys[i]+ys[j])/2.0;
                double r = dist(cx, cy, xs[i], ys[i]);
                if (check(cx, cy, r)) {
                    if (r < res-EPS) res = r;
                }
            }
        }

        for (int i = 0; i < N-2; i++) {
            for (int j = i+1; j < N-1; j++) {
                for (int k = j+1; k < N; k++) {
                    double a1, b1, c1;
                    getLine(xs[i], ys[i], xs[j], ys[j], a1, b1, c1);
                    double a2, b2, c2;
                    getLine(xs[j], ys[j], xs[k], ys[k], a2, b2, c2);
                    double cx, cy;
                    if (getCenter(a1, b1, c1, a2, b2, c2, cx, cy)) {
                        double r = dist(cx, cy, xs[i], ys[i]);
                        if (check(cx, cy, r)) {
                            if (r < res-EPS) res = r;
                        }
                    }
                }
            }
        }

        return (res==MAX)?0.0:res;
    }

    bool getCenter(double a1, double b1, double c1, double a2, double b2, double c2, double & x, double & y) {
        double d = a1*b2-a2*b1;
        if (abs(d) < EPS) return false;
        x = (b2*c1-b1*c2)/d;
        y = (a1*c2-a2*c1)/d;
        return true;
    }

    void getLine(double x1, double y1, double x2, double y2, double & a, double & b, double & c) {
        a = x2-x1;
        b = y2-y1;
        c = a*(x1+x2)/2.0+b*(y1+y2)/2.0;
    }

    double dist(double x1, double y1, double x2, double y2) {
        double dx = x2-x1;
        double dy = y2-y1;
        return sqrt(dx*dx+dy*dy);
    }

    bool check(double cx, double cy, double r) {
        for (int i = 0; i < N; i++)
            if (dist(cx, cy, xs[i], ys[i]) > r+EPS)
                return false;
        return true;
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <int> p0, vector <int> p1, bool hasAnswer, double p2) {
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
    cout << "}";
    cout << "]" << endl;
    TVTower *obj;
    double answer;
    obj = new TVTower();
    clock_t startTime = clock();
    answer = obj->minRadius(p0, p1);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << p2 << endl;
    }
    cout << "Your answer:" << endl;
    cout << "\t" << answer << endl;
    if (hasAnswer) {
        res = answer == answer && fabs(p2 - answer) <= 1e-9 * max(1.0, fabs(p2));
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
    double p2;

    {
        // ----- test 0 -----
        int t0[] = {1,0,-1,0};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {0,1,0,-1};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 1.0;
        all_right = KawigiEdit_RunTest(0, p0, p1, true, p2) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        int t0[] = {3};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {299};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 0.0;
        all_right = KawigiEdit_RunTest(1, p0, p1, true, p2) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        int t0[] = {5,3,-4,2};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        int t1[] = {0,4,3,2};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        p2 = 4.743416490252569;
        all_right = KawigiEdit_RunTest(2, p0, p1, true, p2) && all_right;
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
