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

struct Country {
    string name;
    int g;
    int s;
    int b;

    Country(string n, int g, int s, int b) : name(n), g(g), s(s), b(b) {}
};

struct decreasing {
    bool operator() (const Country & l, const Country & r) const {
        if (l.g != r.g)
            return l.g > r.g;
        if (l.s != r.s)
            return l.s > r.s;
        if (l.b != r.b)
            return l.b > r.b;
        return l.name < r.name;
    }
};

class MedalTable {
public:
    vector <string> generate(vector <string> results) {
        vector<Country> cs;
        for (int i = 0; i < (int)results.size(); ++i) {
            istringstream is(results[i]);
            string gc, sc, bc;
            is >> gc >> sc >> bc;

            int j = findByName(cs, gc);
            if (j == cs.size()) cs.push_back(Country(gc, 1, 0, 0));
            else cs[j].g++;

            j = findByName(cs, sc);
            if (j == cs.size()) cs.push_back(Country(sc, 0, 1, 0));
            else cs[j].s++;

            j = findByName(cs, bc);
            if (j == cs.size()) cs.push_back(Country(bc, 0, 0, 1));
            else cs[j].b++;
        }

        sort(cs.begin(), cs.end(), decreasing());
        vector<string> res;
        for (int i = 0; i < (int)cs.size(); ++i) {
            ostringstream os;
            os << cs[i].name;
            os << " " << cs[i].g;
            os << " " << cs[i].s;
            os << " " << cs[i].b;
            res.push_back(os.str());
        }
        return res;
    }

    int findByName(vector<Country> & cs, string name) {
        int j;
        for (j = 0; j < (int)cs.size(); ++j) {
            if (cs[j].name == name)
                break;
        }
        return j;
    }
};


// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <string> p0, bool hasAnswer, vector <string> p1) {
    cout << "Test " << testNum << ": [" << "{";
    for (int i = 0; int(p0.size()) > i; ++i) {
        if (i > 0) {
            cout << ",";
        }
        cout << "\"" << p0[i] << "\"";
    }
    cout << "}";
    cout << "]" << endl;
    MedalTable *obj;
    vector <string> answer;
    obj = new MedalTable();
    clock_t startTime = clock();
    answer = obj->generate(p0);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << "{";
        for (int i = 0; int(p1.size()) > i; ++i) {
            if (i > 0) {
                cout << ",";
            }
            cout << "\"" << p1[i] << "\"";
        }
        cout << "}" << endl;
    }
    cout << "Your answer:" << endl;
    cout << "\t" << "{";
    for (int i = 0; int(answer.size()) > i; ++i) {
        if (i > 0) {
            cout << ",";
        }
        cout << "\"" << answer[i] << "\"";
    }
    cout << "}" << endl;
    if (hasAnswer) {
        if (answer.size() != p1.size()) {
            res = false;
        } else {
            for (int i = 0; int(answer.size()) > i; ++i) {
                if (answer[i] != p1[i]) {
                    res = false;
                }
            }
        }
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
    vector <string> p1;

    {
        // ----- test 0 -----
        string t0[] = {"ITA JPN AUS","KOR TPE UKR","KOR KOR GBR","KOR CHN TPE"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        string t1[] = {"KOR 3 1 0","ITA 1 0 0","TPE 0 1 1","CHN 0 1 0","JPN 0 1 0","AUS 0 0 1","GBR 0 0 1","UKR 0 0 1"};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        all_right = KawigiEdit_RunTest(0, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        string t0[] = {"USA AUT ROM"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        string t1[] = {"USA 1 0 0","AUT 0 1 0","ROM 0 0 1"};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        all_right = KawigiEdit_RunTest(1, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        string t0[] = {"GER AUT SUI","AUT SUI GER","SUI GER AUT"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        string t1[] = {"AUT 1 1 1","GER 1 1 1","SUI 1 1 1"};
        p1.assign(t1, t1 + sizeof(t1) / sizeof(t1[0]));
        all_right = KawigiEdit_RunTest(2, p0, true, p1) && all_right;
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
