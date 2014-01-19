// BEGIN CUT HERE
// END CUT HERE
#include <sstream>
#include <string>
#include <vector>
#include <map>
#include <algorithm>
#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <utility>
#include <set>
#include <cctype>
#include <queue>
#include <stack>
// BEGIN CUT HERE
#include "cout.hpp"
// END CUT HERE 
using namespace std;
class BunnyPuzzle {
public:
  int theCount(vector <int> bunnies) {
    int ret = 0;
    int i, j, k;
    int n = bunnies.size();

    for (i=0; i<n; i++)
      for (j=0; j<n; j++)
        if (i != j) {
          int dst = 2 * bunnies[j] - bunnies[i];

          int left = min(dst, bunnies[i]);
          int right = max(dst, bunnies[i]);
          bool ok = true;

          for (k=0; k<n; k++)
            if (k != i && k != j && left <= bunnies[k] && bunnies[k] <= right)
              ok = false;

          if (ok) ret++;
        }

    return ret;
  }
   
  // BEGIN CUT HERE
public:
	void run_test(int Case) { 
    if ((Case == -1) || (Case == 0)) test_case_0();
    if ((Case == -1) || (Case == 1)) test_case_1();
    if ((Case == -1) || (Case == 2)) test_case_2();
    if ((Case == -1) || (Case == 3)) test_case_3();
    if ((Case == -1) || (Case == 4)) test_case_4();
    if ((Case == -1) || (Case == 5)) test_case_5();
    if ((Case == -1) || (Case == 6)) test_case_6();
  }
private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { 
    int Arr0[] = {5, 8}; 
    vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); 
    int Arg1 = 2; 
    verify_case(0, Arg1, theCount(Arg0)); }
	void test_case_1() { 
    int Arr0[] = {-1, 0, 1}; 
    vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); 
    int Arg1 = 2; 
    verify_case(1, Arg1, theCount(Arg0)); }
	void test_case_2() { 
    int Arr0[] = {0, 1, 3}; 
    vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); 
    int Arg1 = 3; 
    verify_case(2, Arg1, theCount(Arg0)); }
	void test_case_3() { 
    int Arr0[] = {-677, -45, -2, 3, 8, 29, 384, 867}; 
    vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); 
    int Arg1 = 7; 
    verify_case(3, Arg1, theCount(Arg0)); }
	void test_case_4() { 
    int Arr0[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}; 
    vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); 
    int Arg1 = 2; 
    verify_case(4, Arg1, theCount(Arg0)); }
	void test_case_5() { 
    int Arr0[] = {5, 8}; 
    vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); 
    int Arg1 = 2; 
    verify_case(5, Arg1, theCount(Arg0)); }
	void test_case_6() { 
    int Arr0[] = {5, 8}; 
    vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); 
    int Arg1 = 2; 
    verify_case(6, Arg1, theCount(Arg0)); }

  // END CUT HERE

};
// BEGIN CUT HERE
int main() {
BunnyPuzzle ___test;
___test.run_test(-1);
}
// END CUT HERE 
