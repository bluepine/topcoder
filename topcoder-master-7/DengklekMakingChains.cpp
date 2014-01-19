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

class DengklekMakingChains {
public:

    int maxBeauty(vector <string> chains) {
        int n = chains.size();
        string tmpstr;
        int l = 0, r = 0, c = 0, all = 0;
        int ret = 0;
        vector<pair<int,int> > bothr; //use rightmost piece <val,index>
        vector<pair<int,int> > bothl; //use leftmost piece <val,index>
        for(int i=0;i<n;i++){
            tmpstr = chains[i];
            if(tmpstr[0] == '.' && tmpstr[1] == '.' && tmpstr[2] == '.'){
                //do nothing
            }else if(tmpstr[0] == '.' && tmpstr[1] == '.' && tmpstr[2] != '.'){
                //..d
                l = max(l, tmpstr[2] - '0');
            }else if(tmpstr[0] == '.' && tmpstr[1] != '.' && tmpstr[2] == '.'){
                //.d.
                c = max(c, tmpstr[1] - '0');
            }else if(tmpstr[0] == '.' && tmpstr[1] != '.' && tmpstr[2] != '.'){
                //.dd
                l = max(l, tmpstr[1] - '0' + tmpstr[2] - '0');
            }else if(tmpstr[0] != '.' && tmpstr[1] == '.' && tmpstr[2] == '.'){
                //d..
                r = max(r, tmpstr[0] - '0');
            }else if(tmpstr[0] != '.' && tmpstr[1] == '.' && tmpstr[2] != '.'){
                //d.d
                bothr.push_back(make_pair(tmpstr[0] - '0', i));
                bothl.push_back(make_pair(tmpstr[2] - '0', i));
            }else if(tmpstr[0] != '.' && tmpstr[1] != '.' && tmpstr[2] == '.'){
                //dd.
                r = max(r, tmpstr[0] - '0' + tmpstr[1] - '0');
            }else{
                //ddd
                all += tmpstr[0] - '0' + tmpstr[1] - '0' + tmpstr[2] - '0';
            }
        }

        sort(bothl.rbegin(), bothl.rend());
        sort(bothr.rbegin(), bothr.rend());
        /*
          dump(bothl[0].first);
          dump(bothl[0].second);
          dump(bothr[0].first);
          dump(bothr[0].second);
        */
        int tmpmax = -1;
        if(bothr.size() != 0){
            if(r < bothr[0].first && l < bothl[0].first){
                //fix l
                for(int i=0;i < bothr.size() && bothr[i].first > r;i++){
                    if(bothl[0].second != bothr[i].second){
                        tmpmax = max(tmpmax, bothl[0].first + bothr[i].first);
                    }else{
                        tmpmax = max(tmpmax, bothl[0].first + r);
                    }
                }

                //fix r
                for(int i=0;i < bothl.size() && bothl[i].first > l;i++){
                    if(bothr[0].second != bothl[i].second){
                        tmpmax = max(tmpmax, bothr[0].first + bothl[i].first);
                    }else{
                        tmpmax = max(tmpmax, bothr[0].first + l);
                    }
                }
                l = tmpmax;
                r = 0;
            }else if(r < bothr[0].first){
                r = bothr[0].first;
            }else if(l < bothl[0].first){
                l = bothl[0].first;
            }
        }
        ret = max(ret, max(l+r+all, c));
        return ret;
    }

     


};





// Powered by FileEdit
// Powered by TZTester 1.01 [25-Feb-2003]
// Powered by CodeProcessor
