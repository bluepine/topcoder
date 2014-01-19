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

struct Node {
    char value;
    int index;
    Node * left;
    Node * right;
    Node(char v, int i) : value(v), index(i), left(NULL), right(NULL) {}
};

class IncompleteBST {
public:
    string missingValues(vector<string> & vs) {
        map<int, Node*> ms;
        for (int i = 0; i < (int)vs.size(); i++) {
            istringstream is(vs[i]);
            char c;
            int p;
            is >> c >> p;
            ms[p] = new Node(c, p);
        }

        map<int, Node*>::iterator it = ms.begin();
        while (it != ms.end()) {
            if (it->first != 1) {
                Node * parent = ms[it->first/2];
                if (it->first%2) parent->right = it->second;
                else parent->left = it->second;
            }
            it++;
        }

        Node * root = ms.begin()->second;

        //print(root);
        //cout << endl;

        string res = "";
        Node * prev = NULL;
        char lo = 'A', hi = 'Z';
        if (go(root, prev, lo, hi)) {
            for (char c = lo; c <= hi; c++) res.push_back(c);
        }

        return res;
    }

    void print(Node * node) {
        if (node == NULL) return;
        print(node->left);
        cout << node->index << ":" << node->value << endl;
        print(node->right);
    }

    bool check(int large, int small) {
        while (large > 1) {
            if (large/2 == small) return (large%2 == 1);
            large /= 2;
        }

        return false;
    }

    bool go(Node * node, Node *& prev, char & lo, char & hi) {
        if (node == NULL) return true;
        if (!go(node->left, prev, lo, hi)) return false;

        if (prev != NULL) {
            bool inRightSubtree = check(node->index, prev->index);
            if (prev->value == '?') {
                if (inRightSubtree) hi = node->value-1;
                else hi = node->value;
            }
            else if (node->value == '?') {
                if (inRightSubtree) lo = prev->value+1;
                else lo = prev->value;
            }
            else {
                if ((inRightSubtree && node->value <= prev->value)
                    || (!inRightSubtree && node->value < prev->value)) return false;
            }
        }

        prev = node;
        if (!go(node->right, prev, lo, hi)) return false;
        return true;
    }
};

// BEGIN KAWIGIEDIT TESTING
// Generated by KawigiEdit 2.1.8 (beta) modified by pivanof
#include <iostream>
#include <string>
#include <vector>
using namespace std;
bool KawigiEdit_RunTest(int testNum, vector <string> p0, bool hasAnswer, string p1) {
    cout << "Test " << testNum << ": [" << "{";
    for (int i = 0; int(p0.size()) > i; ++i) {
        if (i > 0) {
            cout << ",";
        }
        cout << "\"" << p0[i] << "\"";
    }
    cout << "}";
    cout << "]" << endl;
    IncompleteBST *obj;
    string answer;
    obj = new IncompleteBST();
    clock_t startTime = clock();
    answer = obj->missingValues(p0);
    clock_t endTime = clock();
    delete obj;
    bool res;
    res = true;
    cout << "Time: " << double(endTime - startTime) / CLOCKS_PER_SEC << " seconds" << endl;
    if (hasAnswer) {
        cout << "Desired answer:" << endl;
        cout << "\t" << "\"" << p1 << "\"" << endl;
    }
    cout << "Your answer:" << endl;
    cout << "\t" << "\"" << answer << "\"" << endl;
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
    string p1;

    {
        // ----- test 0 -----
        string t0[] = {"A 1","? 2"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "A";
        all_right = KawigiEdit_RunTest(0, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 1 -----
        string t0[] = {"B 1","? 2"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "AB";
        all_right = KawigiEdit_RunTest(1, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 2 -----
        string t0[] = {"V 1","? 3"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "WXYZ";
        all_right = KawigiEdit_RunTest(2, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 3 -----
        string t0[] = {"K 1","K 2","A 6","? 12","Y 3"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "";
        all_right = KawigiEdit_RunTest(3, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 4 -----
        string t0[] = {"Z 1","Y 2","X 4","W 8","V 16","U 32","T 64","S 128","R 256","Q 512","P 1024","O 2048","N 4096","M 8192","L 16384","K 32768","J 65536","? 131072"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "ABCDEFGHIJ";
        all_right = KawigiEdit_RunTest(4, p0, true, p1) && all_right;
        // ------------------
    }

    {
        // ----- test 5 -----
        string t0[] = {"Y 3","C 4","E 2","H 1","Y 9", "G 5", "? 8"};
        p0.assign(t0, t0 + sizeof(t0) / sizeof(t0[0]));
        p1 = "";
        all_right = KawigiEdit_RunTest(5, p0, true, p1) && all_right;
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