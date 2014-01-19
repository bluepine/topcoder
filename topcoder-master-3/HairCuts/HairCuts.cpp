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


class HairCuts {
public:
	int ToTime(string & str) {
        str[2] = ' ';
        istringstream is(str);
        int h, m;
        is >> h >> m;
        if (h < 9) h += 12;
        return h*60+m;
    }

    double calc(vector<int> & es, double m) { 
        double time = 0.0;
        for (int i = 0; i < (int)es.size(); i++) {
            if (es[i] > time) time = es[i];
            time += m; 
        }
        return time;
    }

    double maxCut(vector <string> enter, string lastExit) {
        int N = enter.size();
        vector<int> es;
        for (int i = 0; i < N; i++) {
            es.push_back(ToTime(enter[i]));
        }
        sort(es.begin(), es.end());
        int le = ToTime(lastExit);
        double l = 5.0, u = 600.0;
        while (u > 5.0) {
            double m = l+(u-l)/2;
            double t = calc(es, m);
            if (t < le-1e-9) l=m;
            else if (t > le+1e-9) u=m;
            else return m;
        }

        return -1.0;
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <string> p0, string p1, bool hasAnswer, double p2) {
    cout << "Test " << testNum << ": [" << "{";
    for (int i = 0; int(p0.size()) > i; ++i) {
        if (i > 0) {
            cout << ",";
        }
        cout << "\"" << p0[i] << "\"";
    }
    cout << "}" << "," << "\"" << p1 << "\"";
    cout << "]" << endl;
    HairCuts *obj;
    double answer;
    obj = new HairCuts();
    clock_t startTime = clock();
    answer = obj->maxCut(p0, p1);
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

    vector <string> p0;
    string p1;
    double p2;

    {
        // ----- test 0 -----
        string t0[] = {"04:22","09:00"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "05:52";
        p2 = 90.0;
        all_right = KawigiEdit_RunTest(0, p0, p1, true, p2) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        string t0[] = {"09:00","09:22","09:22"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "10:11";
        p2 = 23.666666666666863;
        all_right = KawigiEdit_RunTest(1, p0, p1, true, p2) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        string t0[] = {"09:00","04:00","04:02"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "04:09";
        p2 = -1.0;
        all_right = KawigiEdit_RunTest(2, p0, p1, true, p2) && all_right;
        // ------------------
    }

    {
        // ----- test 3 -----
        string t0[] = {"04:40","10:54","12:30","03:46","04:48","01:57","04:47","10:29","10:39"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "05:21";
        p2 = 13.6666666666669;
        all_right = KawigiEdit_RunTest(3, p0, p1, true, p2) && all_right;
        // ------------------
    }

    {
        // ----- test 4 -----
        string t0[] = {"04:59"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "06:58";
        p2 = 119.0;
        all_right = KawigiEdit_RunTest(4, p0, p1, true, p2) && all_right;
        // ------------------
    }

    {
        //----- test 5 -----
        string t0[] = {"09:00"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "06:59";
        p2 = 599.0;
        all_right = KawigiEdit_RunTest(5, p0, p1, true, p2) && all_right;
        //------------------
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