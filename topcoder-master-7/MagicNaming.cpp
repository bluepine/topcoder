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

#define dump(x)  cerr << #x << " = " << (x) << endl;
inline int toInt(string s){int v;istringstream sin(s);sin>>v;return v;}
template<class T> inline string toString(T x){ostringstream sout;sout<<x;return sout.str();}

class MagicNaming {
     public:

     int maxReindeers(string magicName) {
         int len = magicName.size();
         string s1,s2;
         vector<string> v1,v2;

         for(int i=0;i<len;i++){
             string tmp = "";

             tmp += magicName[i];
             v1.push_back(tmp);
         }

         while(1){
             bool isok =true;
             for(int i=0;i<v1.size()-1;i++){
                 if(v1[i] > v1[i+1]){
                     isok = false;
                 }
             }
             if(isok) return v1.size();
             for(int i=0;i<v1.size();i++){
                 if(i != v1.size()-1 && v1[i] > v1[i+1]){
                     v2.push_back(v1[i] + v1[i+1]);
                 }else{
                     v2.push_back(v1[i]);
                 }
             }
             v1 = v2;
             for(int i=0;i<v1.size();i++) cerr << v1[i] << " ";
             cerr << endl;
             v2.clear();
         }
     }

     
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); if ((Case == -1) || (Case == 5)) test_case_5(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { string Arg0 = "aba"; int Arg1 = 2; verify_case(0, Arg1, maxReindeers(Arg0)); }
	void test_case_1() { string Arg0 = "babbaba"; int Arg1 = 2; verify_case(1, Arg1, maxReindeers(Arg0)); }
	void test_case_2() { string Arg0 = "philosophersstone"; int Arg1 = 5; verify_case(2, Arg1, maxReindeers(Arg0)); }
	void test_case_3() { string Arg0 = "knuthmorrispratt"; int Arg1 = 7; verify_case(3, Arg1, maxReindeers(Arg0)); }
	void test_case_4() { string Arg0 = "acrushpetrtourist"; int Arg1 = 7; verify_case(4, Arg1, maxReindeers(Arg0)); }
	void test_case_5() { string Arg0 = "zzzzz"; int Arg1 = 5; verify_case(5, Arg1, maxReindeers(Arg0)); }

// END CUT HERE


};



// BEGIN CUT HERE

int main() {

     MagicNaming ___test;

     ___test.run_test(-1);

}

// END CUT HERE
