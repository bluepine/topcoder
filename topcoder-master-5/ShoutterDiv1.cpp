// BEGIN CUT HERE

// END CUT HERE
// #line 5 "ShoutterDiv1.cpp"
// written by lonerdude(dvdreddy)
// all rights reserved
//the template by dvdreddy
#include <vector>
#include <queue>
#include <deque>
#include <map>
#include <set>
#include <iostream>
#include <cstring>
#include <string>
#include <math.h>
#include <cstdio>
#include <cstdlib>
#include <algorithm>

using namespace std;

#define si(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)

typedef long long ll;

struct intv{
  int stx;
  int edx;
};




class ShoutterDiv1 {
	public:
  int count(vector <string> s1000, vector <string> s100, vector <string> s10, vector <string> s1, vector <string> t1000, vector <string> t100, vector <string> t10, vector <string> t1) {
    /*
    if ((s1000.size() == 1) & (s10[0] == "0000000000") & (s1[0] == "7626463146") & (t10[0] == "0000000000") && (t1[0] == "9927686479")){
      return 18;
    }
    */
    string S1000 = "", S100 = "", S10 = "", S1 = "";
    fr (i, s1000.size()){
      S1000 += s1000[i];
      S100 += s100[i];
      S10 += s10[i];
      S1 += s1[i];
    }
    vector<int> st;
    fr (i, S1000.size()){
      int val = S1000[i] - '0';
      val = val * 10 + S100[i]  - '0';
      val = val * 10 + S10[i]  - '0';
      val = val * 10 + S1[i]  - '0';
      st.push_back(val);
    }

    string T1000 = "", T100 = "", T10 = "", T1 = "";
    fr (i, t1000.size()){
      T1000 += t1000[i];
      T100 += t100[i];
      T10 += t10[i];
      T1 += t1[i];
    }
    vector<int> end;
    fr (i, T1000.size()){
      int val = T1000[i] - '0';
      val = val * 10 + T100[i]  - '0';
      val = val * 10 + T10[i]  - '0';
      val = val * 10 + T1[i]  - '0';
      end.push_back(val);
    }

    int n = end.size();
    vector<intv> v;
    fr (i, n){
      intv x; x.stx = st[i]; x.edx = end[i];
      v.push_back(x);
    }
    vector<intv> vf(v.begin(), v.end());
    vector<intv> vb(v.begin(), v.end());




    cout << "dooor\n";


    cout << "drrrr\n";

    vector<int> leftmax;
    vector<int> rightmax;
    fr (i, n){
      int cur_right_max = v[i].edx;
      int cur_left_max = v[i].stx;
      int cur_left = -1;
      int cur_right = -1;
      fr (j, n){
	if (i == j)
	  continue;
	if ((v[i].stx >= v[j].stx && v[i].stx <= v[j].edx) ||
	    (v[j].stx >= v[i].stx && v[j].stx <= v[i].edx)){
	  if (v[j].stx <= cur_left_max){
	    cur_left_max = v[j].stx;
	    cur_left = j;
	  }
	  if (v[j].edx >= cur_right_max){
	    cur_right_max = v[j].edx;
	    cur_right = j;
	  }
	}
      }      
      leftmax.push_back(cur_left);
      rightmax.push_back(cur_right);
    }
    /*
    cout << "lf max ";
    fr (i, n){
      cout << leftmax[i] << " ";
    }
    cout << endl;

    cout << "rf max ";
    fr (i, n){
      cout << rightmax[i] << " ";
    }
    cout << endl;
    */

    int mini = v[0].edx; int maxi = v[0].stx;
    fr (i, n){
      mini = min(v[i].edx, mini);
      maxi = max(v[i].stx, maxi);
    }
    
    vector<int> normal_count;
    fr (i, n){
      int bxx1 = -1;  
      int cur = i; int cnt = -1;
      int cur_ll = v[cur].stx;
      while (cur != -1){
	bxx1++;
	cur_ll = v[cur].stx;
	cur = leftmax[cur]; cnt++;
	if (cur_ll <= mini){
	  break;
	}
      }
      if (cnt <= 0) cnt = 0;
      if (cur_ll > mini){
	return -1;
      }

      cur = rightmax[i];
      int cur_rr = v[i].edx;
      bool bxx2 = false;
      while (cur != -1 && cur_rr < maxi){
	bxx2 = true;
	cur_rr = v[cur].edx;
	cur = rightmax[cur]; cnt++;
	if (cur_rr >= maxi){
	  break;
	}
      }

      if (cur_rr < maxi){
	return -1;
      }
      
      if (leftmax[i] == rightmax[i] && (v[i].edx < maxi) && (bxx1>0) && bxx2){
	cnt--;
      }
      if (cnt <= 0) cnt = 0;
      normal_count.push_back(cnt);
    }

    cout << "dude \n";
    /*
    cout << "nc ";
    fr (i, n){
      cout << normal_count[i] << " ";
    }
    cout << endl;
    */

    int res = 0;
    fr (x, n){
      // cout << x << endl;      
      fr (j, n){
	if ((v[j].edx >= v[x].edx && v[j].stx < v[x].stx) || 
	    (v[j].edx > v[x].edx && v[j].stx <= v[x].stx)) {
	  normal_count[x] = min(normal_count[x], normal_count[j] + 1);
	}
      }
    }

    fr (i, n){
      res += normal_count[i];
    }
    if (res <= 0) return 0;
    return res;
  }
};

/*
int main(){
  ShoutterDiv1 x;
  vector<string> v1({"370"});vector<string> v2({"062"});
  vector<string> v3({"646"});vector<string> v4({"165"});
  vector<string> v5({"399"});vector<string> v6({"764"});
  vector<string> v7({"682"});vector<string> v8({"995"});
  
  cout << "mn " << x.count(v1, v2, v3, v4, v5, v6, v7, v8) << endl;
  
  }*/
