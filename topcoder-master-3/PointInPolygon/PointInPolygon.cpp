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

struct Point {
    int x;
    int y;
    Point(int x, int y) : x(x), y(y) {}
};

struct Line {
    int a;
    int b;
    int c;
    Line(Point p, Point q) : a(q.y-p.y), b(p.x-q.x), c(a*p.x+b*p.y) {};
};

bool onSegment(Point p, Point q, Point r) {
    return (q.x >= min(p.x, r.x) && q.x <= max(p.x, r.x) && 
        q.y >= min(p.y, r.y) && q.y <= max(p.y, r.y));
}

int orientation(Point p, Point q, Point r) {
    int val = (q.y-p.y)*(r.x-q.x)-(q.x-p.x)*(r.y-q.y);
    if (val == 0) return 0;
    return (val > 0)? 1: 2;
}

bool intersect(Point p1, Point q1, Point p2, Point q2) {
    int o1 = orientation(p1, q1, p2);
    int o2 = orientation(p1, q1, q2);
    int o3 = orientation(p2, q2, p1);
    int o4 = orientation(p2, q2, q1);

    if (o1 != o2 && o3 != o4) return true;
    if (o1 == 0 && onSegment(p1, p2, q1)) return true;
    if (o2 == 0 && onSegment(p1, q2, q1)) return true;
    if (o3 == 0 && onSegment(p2, p1, q2)) return true;
    if (o4 == 0 && onSegment(p2, q1, q2)) return true;

    return false;
}

bool intersect2(Point p1, Point q1, Point p2, Point q2) {
    Line l1(p1, q1);
    Line l2(p2, q2);
    int d = l1.a*l2.b-l1.b*l2.a;
    if (d == 0) {
        //Lines are parallel, check if p2 or q2 is on segment p1q1
        return onSegment(p1, p2, q1) || onSegment(p1, q2, q1);
    }
    else {
        // Calculate intersection point o (scaled by factor d)
        Point o(l2.b*l1.c-l1.b*l2.c, l1.a*l2.c-l2.a*l1.c);
        p1.x *= d, p1.y *= d;
        q1.x *= d, q1.y *= d;
        p2.x *= d, p2.y *= d;
        q2.x *= d, q2.y *= d;
        // Check if point o is on segments p1q1 and p2q2
        return onSegment(p1, o, q1) && onSegment(p2, o, q2);
    }
}

class PointInPolygon {
public:
    string testPoint(vector <string> vertices, int testPointX, int testPointY) {
        int N = vertices.size();
        vector<Point> vs;
        for (int i = 0; i < N; i++) {
            istringstream is(vertices[i]);
            int x, y;
            is >> x >> y;
            vs.push_back(Point(x, y));
        }

        Point tp(testPointX, testPointY);
        for (int i = 0; i < N; i++) {
            int j = (i+1)%N;
            if (onSegment(vs[i], tp, vs[j])) return "BOUNDARY";
        }

        Point ip(1001, testPointY+1);
        int cnt = 0;
        for (int r = 0; r < N; r++) {
            int c = (r+1)%N;
            if (intersect2(vs[r], vs[c], tp, ip))
                cnt++;
        }

        if (cnt%2 == 0) return "EXTERIOR";
        return "INTERIOR";
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <string> p0, int p1, int p2, bool hasAnswer, string p3) {
    cout << "Test " << testNum << ": [" << "{";
    for (int i = 0; int(p0.size()) > i; ++i) {
        if (i > 0) {
            cout << ",";
        }
        cout << "\"" << p0[i] << "\"";
    }
    cout << "}" << "," << p1 << "," << p2;
    cout << "]" << endl;
    PointInPolygon *obj;
    string answer;
    obj = new PointInPolygon();
    clock_t startTime = clock();
    answer = obj->testPoint(p0, p1, p2);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << "\"" << p3 << "\"" << endl;
    }
    cout << "Your answer:" << endl;
    cout << "\t" << "\"" << answer << "\"" << endl;
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

    vector <string> p0;
    int p1;
    int p2;
    string p3;

    {
        // ----- test 0 -----
        string t0[] = {"0 0","0 10","10 10","10 0"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 5;
        p2 = 5;
        p3 = "INTERIOR";
        all_right = KawigiEdit_RunTest(0, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        string t0[] = {"0 0","0 10","10 10","10 0"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 10;
        p2 = 15;
        p3 = "EXTERIOR";
        all_right = KawigiEdit_RunTest(1, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        string t0[] = {"0 0","0 10","10 10","10 0"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 5;
        p2 = 10;
        p3 = "BOUNDARY";
        all_right = KawigiEdit_RunTest(2, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 3 -----
        string t0[] = {"-100 -90","-100 100","100 100","100 -100","-120 -100","-120 100","-130 100","-130 -110","110 -110","110 110","-110 110","-110 -90"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 0;
        p2 = 0;
        p3 = "EXTERIOR";
        all_right = KawigiEdit_RunTest(3, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 4 -----
        string t0[] = {"0 0","0 1000","1000 1000","1000 800","200 800","200 600","600 600","600 400","200 400","200 200","1000 200","1000 0"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 100;
        p2 = 500;
        p3 = "INTERIOR";
        all_right = KawigiEdit_RunTest(4, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 5 -----
        string t0[] = {"0 1000","1000 1000","1000 800","200 800","200 600","600 600","600 400","200 400","200 200","1000 200","1000 0","0 0"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 322;
        p2 = 333;
        p3 = "EXTERIOR";
        all_right = KawigiEdit_RunTest(5, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 6 -----
        string t0[] = {"500 0","500 100","400 100","400 200","300 200","300 300","200 300","200 400","100 400","100 500","0 500","0 400","-100 400","-100 300","-200 300","-200 200","-300 200","-300 100","-400 100","-400 0","-500 0","-500 -100","-400 -100","-400 -200","-300 -200","-300 -300","-200 -300","-200 -400","-100 -400","-100 -500","0 -500","0 -400","100 -400","100 -300","200 -300","200 -200","300 -200","300 -100","400 -100","400 0"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 200;
        p2 = 200;
        p3 = "INTERIOR";
        all_right = KawigiEdit_RunTest(6, p0, p1, p2, true, p3) && all_right;
        // ------------------
    }

    {
        // ----- test 7 -----
        string t0[] = {"1 0","2 0","2 1","3 1","3 0","4 0","4 -1","5 -1","5 0","6 0","6 2","0 2","0 3","-1 3","-1 4","0 4","0 6","1 6","1 7","0 7","0 8","-2 8","-2 2","-8 2","-8 0","-7 0","-7 -1","-6 -1","-6 0","-4 0","-4 1","-3 1","-3 0","-2 0","-2 -6","0 -6","0 -5","1 -5","1 -4","0 -4","0 -3","-1 -3","-1 -2","0 -2","0 -1","1 -1"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = 0;
        p2 = 0;
        p3 = "INTERIOR";
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
