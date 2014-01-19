#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <queue>
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
#include <cctype>
#include <string>
#include <cstring>
#include <ctime>

using namespace std;

#define ll long long
#define dump(x)  cerr << #x << " = " << (x) << endl;
inline int toInt(string s){int v;istringstream sin(s);sin>>v;return v;}
template<class T> inline string toString(T x){ostringstream sout;sout<<x;return sout.str();}

class BestView {
     public:

     int numberOfBuildings(vector <int> heights) {
         int n = heights.size();
         int ret=0;
         bool cansee[n][n];
         bool able;
         ll diff;

         memset(cansee, false, sizeof(cansee));
         for(int i=0;i<n;i++){
             for(int j=i+1;j<n;j++){
                 able = true;
                 diff = heights[j] - heights[i];
                 for(int k=1;k<j-i;k++){
                     if((ll)(j-i) * heights[i+k] >= (ll)(j-i) * heights[i] + diff * k){
                         able = false;
                     }
                 }
                 if(able){
                     cansee[i][j] = true;
                     cansee[j][i] = true;
                     /*
                     if(heights[i] >= heights[j]) cansee[i][j] = true;
                     if(heights[j] >= heights[i]) cansee[j][i] = true;
                     */
                 }
             }
         }
         for(int i=0;i<n;i++){
             int cnt=0;
             for(int j=0;j<n;j++){
                 if(cansee[i][j] == true){
                     cnt++;
                 }
             }
             ret = max(ret,cnt);
         }
         return ret;
     }

     
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); if ((Case == -1) || (Case == 5)) test_case_5(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { int Arr0[] = {10}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 0; verify_case(0, Arg1, numberOfBuildings(Arg0)); }
	void test_case_1() { int Arr0[] = {5,5,5,5}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 2; verify_case(1, Arg1, numberOfBuildings(Arg0)); }
	void test_case_2() { int Arr0[] = {1,2,7,3,2}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 4; verify_case(2, Arg1, numberOfBuildings(Arg0)); }
	void test_case_3() { int Arr0[] = {1,5,3,2,6,3,2,6,4,2,5,7,3,1,5}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 7; verify_case(3, Arg1, numberOfBuildings(Arg0)); }
	void test_case_4() { int Arr0[] = {1000000000,999999999,999999998,999999997,999999996,1,2,3,4,5}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 6; verify_case(4, Arg1, numberOfBuildings(Arg0)); }
	void test_case_5() { int Arr0[] = {244645169,956664793,752259473,577152868,605440232,569378507,111664772,653430457,454612157,397154317}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 7; verify_case(5, Arg1, numberOfBuildings(Arg0)); }

// END CUT HERE


};



// BEGIN CUT HERE

int main() {

     BestView ___test;

     ___test.run_test(-1);

}

// END CUT HERE
