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

class MagicStonesStore {
     public:

     string ableToDivide(int n) {
         bool isprime[20001];

         memset(isprime,true,sizeof(isprime));
         isprime[0] = false;
         isprime[1] = false;
         for(int i=2;i<20001;i++){
             if(isprime[i]){
                 for(int j=2;i*j<20001;j++){
                     isprime[i*j] = false;
                 }
             }
         }
         bool ans = false;

         for(int i=1;i<2*n;i++){
             if(isprime[i] && isprime[2*n-i]) ans = true;
         }

         if(ans){
             return "YES";
         }else{
             return "NO";
         }
     }

     


};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
