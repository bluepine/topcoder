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

class Cut {
     public:

     int getMaximum(vector <int> len, int mcut) {
         vector<int> zeros;
         vector<int> others;
         int ret=0;

         for(int i=0;i<len.size();i++){
             if(len[i] % 10 == 0){
                 zeros.push_back(len[i]);
             }else{
                 others.push_back(len[i]);
             }
         }
         sort(zeros.begin(), zeros.end());
         sort(others.begin(),others.end());
         
         int pos = 0;
         while(pos < zeros.size() && mcut != 0){
             if(zeros[pos] == 10){
                 ret++;
                 pos++;
             }else{
                 zeros[pos] -= 10;
                 ret++;
                 mcut--;
                 if(zeros[pos] == 10){
                     ret++;
                     pos++;
                 }
             }
         }
         
         pos = 0;
         while(mcut != 0 && pos < others.size()){
             if(others[pos] < 10){
                 pos++;
             }else{
                 others[pos] -= 10;
                 ret++;
                 mcut--;
             }
         }

         return ret;
     }

     


};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
