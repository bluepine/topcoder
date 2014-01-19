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

class MinCostPalindrome {
     public:

     int getMinimum(string s, int oCost, int xCost) {
         int l = s.size();
         int ret=0;
         for(int i=0;i<l/2;i++){
             if(s[i] == '?' && s[l-i-1] == '?'){
                 ret += min(oCost, xCost) * 2;
             }else if(s[i] == '?'){
                 if(s[l-i-1] == 'x'){
                     ret += xCost;
                 }else{
                     ret += oCost;
                 }
             }else if(s[l-i-1] == '?'){
                 if(s[i] == 'x'){
                     ret += xCost;
                 }else{
                     ret += oCost;
                 }
             }else if(s[i] != s[l-i-1]){
                 ret = -1;
                 break;
             }
         }
         
         return ret;
     }

     


};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
