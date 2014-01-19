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
#define PB push_back
#define MP make_pair

class DropCoins {
     public:

     int getMinimum(vector <string> board, int K) {
         pair<int,int> lu, rd;
         int h = board.size();
         int w = board[0].size();
         int ret = 1<<29;

         for(int i=0;i<h;i++){
             for(int j=0;j<w;j++){
                 //左上の点(i,j)
                 for(int p=i;p<h;p++){
                     for(int q=j;q<w;q++){
                         //右下の点(p,q)
                         int count = 0;
                         for(int x=i;x<=p;x++){
                             for(int y=j;y<=q;y++){
                                 if(board[x][y] == 'o'){
                                     count++;
                                 }
                             }
                         }
                         if(count == K){
                             ret = min(ret, min(i,h-p-1)*2 + max(i,h-p-1) + min(j,w-q-1)*2 + max(j,w-q-1));
                         }
                     }
                 }
             }
         }
         if(ret == 1 << 29) ret = -1;
         return ret;         
     }

     
// BEGIN CUT HERE
	public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
	private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const int &Expected, const int &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: \"" << Expected << '\"' << endl; cerr << "\tReceived: \"" << Received << '\"' << endl; } }
	void test_case_0() { string Arr0[] = {".o.."
,"oooo"
,"..o."}
; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 3; int Arg2 = 2; verify_case(0, Arg2, getMinimum(Arg0, Arg1)); }
	void test_case_1() { string Arr0[] = {".....o"
,"......"
,"oooooo"
,"oooooo"
,"......"
,"o....."}
; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 12; int Arg2 = 3; verify_case(1, Arg2, getMinimum(Arg0, Arg1)); }
	void test_case_2() { string Arr0[] = {"...."
,".oo."
,".oo."
,"...."}
; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 3; int Arg2 = -1; verify_case(2, Arg2, getMinimum(Arg0, Arg1)); }
	void test_case_3() { string Arr0[] = {"......."
,"..ooo.."
,"ooooooo"
,".oo.oo."
,"oo...oo"}
; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 12; int Arg2 = 4; verify_case(3, Arg2, getMinimum(Arg0, Arg1)); }
	void test_case_4() { string Arr0[] = {"................."
,".ooooooo...oooo.."
,".ooooooo..oooooo."
,".oo.......oo..oo."
,".oo.......oo..oo."
,".ooooo.....oooo.."
,".ooooooo...oooo.."
,".....ooo..oo..oo."
,"......oo..oo..oo."
,".ooooooo..oooooo."
,".oooooo....oooo.."
,"................."}
; vector <string> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 58; int Arg2 = 6; verify_case(4, Arg2, getMinimum(Arg0, Arg1)); }

// END CUT HERE


};



// BEGIN CUT HERE

int main() {

     DropCoins ___test;

     ___test.run_test(-1);

}

// END CUT HERE
