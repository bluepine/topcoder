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
    cout << "dooor\n";
    vector<set<int> > ngh;
    fr (i, n){
      set<int> neigh;
      fr (j, n){
	if (i == j){
	  continue;
	}
	if (v[i].stx <= v[j].edx && v[i].stx >= v[j].stx){
	  neigh.insert(j);
	} else if (v[j].stx <= v[i].edx && v[j].stx >= v[i].stx){
	  neigh.insert(j);
	}
      }
      ngh.push_back(neigh);
    }
    cout << "drrrr\n";

    vector<int> leftmax;
    vector<int> rightmax;
    fr (i, n){
      int cur_right_max = v[i].edx;
      int cur_left_max = v[i].stx;
      int cur_left = -1;
      int cur_right = -1;
      set<int>::iterator it = ngh[i].begin();
      while (it != ngh[i].end()){
	if (v[*it].stx <= cur_left_max){
	  cur_left_max = v[*it].stx;
	  cur_left = *it;
	}
	if (v[*it].edx >= cur_right_max){
	  cur_right_max = v[*it].edx;
	  cur_right = *it;
	}
	it++;
      }
      leftmax.push_back(cur_left);
      rightmax.push_back(cur_right);
    }
    
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


    int mini = v[0].edx; int maxi = v[0].stx;
    fr (i, n){
      mini = min(v[i].edx, mini);
      maxi = max(v[i].stx, maxi);
    }
    
    vector<int> normal_count;
    fr (i, n){
      int cur = i;
      set<int> all_touched; int cnt = -1; all_touched.insert(cur);
      while (cur != -1 && all_touched.size() != n){
	all_touched.insert(ngh[cur].begin(), ngh[cur].end());
	int cur_ll = v[cur].stx;
	cur = leftmax[cur]; cnt++;
	if (cur_ll <= mini){
	  break;
	}
      }

      cur = rightmax[i];
      while (cur != -1 && v[i].edx < maxi && all_touched.size() != n){
	all_touched.insert(ngh[cur].begin(), ngh[cur].end());
	int cur_rr = v[cur].edx;
	cur = rightmax[cur]; cnt++;
	if (cur_rr >= maxi){
	  break;
	}
      }
      
      if (leftmax[i] == rightmax[i] && v[i].edx < maxi && all_touched.size() != n){
	cnt--;
      }
      normal_count.push_back(cnt);
      if (all_touched.size() != n){
	cout << "i " << i << " " << all_touched.size() << endl;
	return -1;
      }      
    }

    cout << "dude \n";

    cout << "nc ";
    fr (i, n){
      cout << normal_count[i] << " ";
    }
    cout << endl;


    int res = 0;
    queue<int> q;
    set<int> computed;
    fr (i, n){
      q.push(i);
    }
    while (!q.empty()){
      int x = q.front(); q.pop();
      set<int>::iterator it = ngh[x].begin(), jt;
      bool done = true;
      while (it != ngh[x].end()){
	if ((v[*it].edx >= v[x].edx && v[*it].stx < v[x].stx) || 
	    (v[*it].edx > v[x].edx && v[*it].stx <= v[x].stx)) {
	  cout << x << " " << *it << endl;
	  jt = computed.find(*it);
	  if (jt == computed.end()){
	    done = false;
	  }
	}
	it++;
      }
      cout << x << endl;

      if (done){
	it = ngh[x].begin();
	while (it != ngh[x]. end()){
	  if ((v[*it].edx >= v[x].edx && v[*it].stx < v[x].stx) || 
	    (v[*it].edx > v[x].edx && v[*it].stx <= v[x].stx)) {
	    normal_count[x] = min(normal_count[x], normal_count[*it] + 1);
	  }
	  it++;
	}
	computed.insert(x);
      } else {
	q.push(x);
      }
    }

    fr (i, n){
      res += normal_count[i];
    }
    if (res <= 0) return 0;
    return res;
  }
};
