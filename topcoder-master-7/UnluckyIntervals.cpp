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

class interval{
public:
    interval(int b, int e, int v, ll l, int p){
        begin = b;
        end = e;
        val = v;
        len = l;
        pos = p;
    }

    int begin;
    int end;
    ll val;
    int len;
    int pos;

    bool operator <(const interval &arg) const{
        if(val != arg.val){
            return val < arg.val;
        }else{
            return begin < arg.begin;
        }
    }
};


class UnluckyIntervals {
public:

    ll calc_val(int length, int pos){
        return (length-1-pos)+(length-pos)*(ll)pos;
    }
    
    vector <int> getLuckiest(vector <int> ls, int n) {
        vector<interval> vi;
        vector<int> ret;
        
        sort(ls.begin(),ls.end());
        for(int i=0;i<ls.size();i++){
            if(ret.size() == n) break;
            if(ls[i] == 2 || (i > 0 && ls[i] - ls[i-1] == 2)){
                if(find(ret.begin(),ret.end(), ls[i]-1) == ret.end()) ret.push_back(ls[i]-1);
                if(ret.size() == n) break;
            }else if(ls[i] == 1 || (i > 0 && ls[i] - ls[i-1] == 1)){
                //do nothing
            }else{
                if(i == 0){
                    vi.push_back(interval(1,ls[i]-1, calc_val(ls[i]-1,0), ls[i]-1,0));
                }else{
                    vi.push_back(interval(ls[i-1]+1,ls[i]-1, calc_val(ls[i]-ls[i-1]-1,0), ls[i]-ls[i-1]-1,0));
                }
            }

            ret.push_back(ls[i]);
        }
        int cnt = ret.size();
        while(cnt < n && !vi.empty()){
            sort(vi.begin(),vi.end());
            ret.push_back(vi[0].begin);
            cnt++;
            if(cnt == n) break;
            if(vi[0].begin != vi[0].end){
                ret.push_back(vi[0].end);
                cnt++;
                if(cnt == n) break;
            }
            vi[0].begin++;
            vi[0].end--;
            vi[0].pos++;
            if(vi[0].begin > vi[0].end){
                vi.erase(vi.begin());
            }else{
                vi[0].val = calc_val(vi[0].len,vi[0].pos);
            }
        }
        for(int i=1;ret.size()<n;i++){
            ret.push_back(ls.back()+i);
        }
        return ret;
    }

     
// BEGIN CUT HERE
public:
	void run_test(int Case) { if ((Case == -1) || (Case == 0)) test_case_0(); if ((Case == -1) || (Case == 1)) test_case_1(); if ((Case == -1) || (Case == 2)) test_case_2(); if ((Case == -1) || (Case == 3)) test_case_3(); if ((Case == -1) || (Case == 4)) test_case_4(); }
private:
	template <typename T> string print_array(const vector<T> &V) { ostringstream os; os << "{ "; for (typename vector<T>::const_iterator iter = V.begin(); iter != V.end(); ++iter) os << '\"' << *iter << "\","; os << " }"; return os.str(); }
	void verify_case(int Case, const vector <int> &Expected, const vector <int> &Received) { cerr << "Test Case #" << Case << "..."; if (Expected == Received) cerr << "PASSED" << endl; else { cerr << "FAILED" << endl; cerr << "\tExpected: " << print_array(Expected) << endl; cerr << "\tReceived: " << print_array(Received) << endl; } }
	void test_case_0() { int Arr0[] = {3}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 6; int Arr2[] = {3, 1, 2, 4, 5, 6 }; vector <int> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); verify_case(0, Arg2, getLuckiest(Arg0, Arg1)); }
	void test_case_1() { int Arr0[] = {5, 11, 18}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 9; int Arr2[] = {5, 11, 18, 1, 4, 6, 10, 2, 3 }; vector <int> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); verify_case(1, Arg2, getLuckiest(Arg0, Arg1)); }
	void test_case_2() { int Arr0[] = {7, 13, 18}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 9; int Arr2[] = {7, 13, 18, 14, 17, 8, 12, 1, 6 }; vector <int> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); verify_case(2, Arg2, getLuckiest(Arg0, Arg1)); }
	void test_case_3() { int Arr0[] = {1000, 1004, 4000, 4003, 5000}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 19; int Arr2[] = {1000, 1004, 4000, 4003, 5000, 4001, 4002, 1001, 1003, 1002, 4004, 4999, 1, 999, 4005, 4998, 2, 998, 4006 }; vector <int> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); verify_case(3, Arg2, getLuckiest(Arg0, Arg1)); }
	void test_case_4() { int Arr0[] = {1000000000}; vector <int> Arg0(Arr0, Arr0 + (sizeof(Arr0) / sizeof(Arr0[0]))); int Arg1 = 8; int Arr2[] = {1000000000, 1, 999999999, 2, 999999998, 3, 999999997, 4 }; vector <int> Arg2(Arr2, Arr2 + (sizeof(Arr2) / sizeof(Arr2[0]))); verify_case(4, Arg2, getLuckiest(Arg0, Arg1)); }

// END CUT HERE


};



// BEGIN CUT HERE

int main() {

    UnluckyIntervals ___test;

    ___test.run_test(-1);

}

// END CUT HERE
