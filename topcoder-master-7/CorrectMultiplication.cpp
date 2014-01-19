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

class CorrectMultiplication {
     public:

     long long getMinimum(int a, int b, int c) {
       ll ret=1<<30;
       ll tmpret=0;
       int pm[2]={1,-1};
       int sq = sqrt(c);
       ll tmpa = sq;
       ll tmpb = sq;
       ll aa,bb;
       ret = abs(a-tmpa)+abs(b-tmpb)+abs(c - tmpa*tmpb);
       while(tmpret < ret){
	 for(int i=1;i<=tmpret;i++){
	   for(int q=0;q<=i;q++){
	     for(int j=0;j<2;j++){
	       for(int k=0;k<2;k++){
		 dump(tmpa+q*pm[j]);
		 dump(tmpb+(i-q)*pm[k]);
		 if(tmpa+q*pm[j] <= 0 || tmpb+(i-q)*pm[k] <= 0) continue;
		 ret = min(ret, abs(a-(tmpa+q*pm[j])) + abs(b-(tmpb+(i-q))) + abs((ll)c - (ll)(tmpa+q*pm[j])*(tmpb+(i-q)*pm[k])));
		 //cerr << ret << endl;
	       }
	     }
	   }
	 }
	 tmpret++;
       }
         
       return ret;
     }

     
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const long long &Expected, const long long &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { int Arg0 = 19; int Arg1 = 28; int Arg2 = 522; long long Arg3 = 2LL; verify_case(0, Arg3, getMinimum(Arg0, Arg1, Arg2)); }
	void test_case_1() { int Arg0 = 10; int Arg1 = 30; int Arg2 = 500; long long Arg3 = 11LL; verify_case(1, Arg3, getMinimum(Arg0, Arg1, Arg2)); }
	void test_case_2() { int Arg0 = 11111; int Arg1 = 11111; int Arg2 = 123454321; long long Arg3 = 0LL; verify_case(2, Arg3, getMinimum(Arg0, Arg1, Arg2)); }
	void test_case_3() { int Arg0 = 1000; int Arg1 = 100; int Arg2 = 10; long long Arg3 = 1089LL; verify_case(3, Arg3, getMinimum(Arg0, Arg1, Arg2)); }
	void test_case_4() { int Arg0 = 399; int Arg1 = 522; int Arg2 = 199999; long long Arg3 = 24LL; verify_case(4, Arg3, getMinimum(Arg0, Arg1, Arg2)); }

// END CUT HERE


};



// BEGIN CUT HERE

int main() {

     CorrectMultiplication ___test;

     ___test.run_test(0);

}

// END CUT HERE
