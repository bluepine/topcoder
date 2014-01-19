// BEGIN CUT HERE
// END CUT HERE
#line 4 "Excavations.cpp"
// written by lonerdude(dvdreddy)
// all rights reserved
//the template by dvdreddy
#include <vector>
#include <queue>
#include <deque>
#include <map>
#include <iostream>
#include <cstring>
#include <string>
#include <math.h>
#include <cstdio>
#include <cstdlib>
#include <set>
#include <algorithm>

using namespace std;

#define si(x) scanf("%d",&x)
#define sll(x) scanf("%lld",&x)
#define sf(x) scanf("%lf",&x)
#define ss(x) scanf("%s",&x)

#define f(i,a,b) for(int i=a;i<b;i++)
#define fr(i,n)  f(i,0,n)
#define pb push_back
#define rf(i,a,b) for(int i=a; i>b;i--)
#define rfr(i,n) for(int i=n;i>=0;i--)

typedef long long ll;

ll ncr[52][52];


class Excavations {
public:
  long long count(vector <int> kind, vector <int> depth, vector <int> found, int K) {
    
    fr (i, 52){
      fr (j, 52){
	ncr[i][j] = 0;
      }
      ncr[i][0] = 1; ncr[i][i] = 1;      
    }

    fr (i, 52){
      f (j, 1, i){
	ncr[i][j] = ncr[i - 1][j - 1] + ncr[i - 1][j];
      }
    }

    cout << " " << ncr[0][0] << " " << ncr[4][2] << endl;

    map<int, vector<int> > mp;
    int n = kind.size();
    fr (i, n){
      mp[kind[i]].pb(depth[i]);
    }
    
    map<int, vector<int> >::iterator it;
    vector<int>::iterator jt;
    

    for (it = mp.begin(); it != mp.end(); it++){
      sort((it->second).begin(), (it->second).end());
      // cout << "szs " << it->first << " " << it->second.size() << endl;
    }
    
    int no_found = found.size();
    vector<int> not_use;
    sort(found.begin(), found.end());
    int found_start = 0;
    for (it = mp.begin(); it != mp.end(); it++){
      if (found_start == no_found){
	for (jt = (it->second).begin(); jt != it->second.end(); jt++){
	  not_use.pb(*jt);
	}
      } else if (it->first == found[found_start]){	
	found_start++;
      } else if (it->first < found[found_start]){
	for (jt = (it->second).begin(); jt != it->second.end(); jt++){
	  not_use.pb(*jt);
	}
      }
    }

    sort(not_use.begin(), not_use.end());

    vector<int> depths;
    fr (i, n){
      depths.pb(depth[i]);
    }
    depths.pb(0);
    vector<int> depth_set(depths.begin(), depths.end());
    sort(depth_set.begin(), depth_set.end());

    int k = K;

    ll cur_old[no_found][k + 1];
    ll cur_new[no_found][k + 1];

    ll dp_old[no_found + 1][k + 1];
    ll dp_new[no_found + 1][k + 1];
    
    int ptrs_cnt[no_found];
    int size_cnt[no_found];

    int ptrs_not_use = -1;
    int size_not_use = not_use.size();

    fr (i, no_found){
      ptrs_cnt[i] = -1;
      size_cnt[i] = mp[found[i]].size();
      fr (j, k + 1){
	cur_old[i][j] = 0;
	cur_new[i][j] = 0;
      }
    }


    
    ll res_final = 0;

    // final array iteration
    fr (ix, depth_set.size()){
      int tick = depth_set[ix];
      fr (i, no_found + 1){
	fr (j, k + 1){
	  dp_new[i][j] = 0;
	  dp_old[i][j] = 0;
	}
      }
      dp_old[0][0] = 1;
      dp_new[0][0] = 1;

      bool is_done = true;

      fr (i, no_found){
	bool not_touched = false;
	int cnt_same = 0;
	while (ptrs_cnt[i] < size_cnt[i] - 1 && tick == mp[found[i]][ptrs_cnt[i] + 1]){
	  ptrs_cnt[i]++; cnt_same++;
	  not_touched = true;
	  break;
	}
	if (not_touched){
	  int cnt_other = size_cnt[i] - ptrs_cnt[i] - 1;
	  fr (j, k){
	    cur_old[i][j + 1] = cur_new[i][j + 1];
	    cur_new[i][j + 1] += (ncr[cnt_other][j]);	    
	  }
	} else {
	  fr (j, k){
	    cur_old[i][j + 1] = cur_new[i][j + 1];
	  }
	  if (ptrs_cnt[i] == -1){
	    is_done = false;
	  }
	}
      }

      while (ptrs_not_use < (size_not_use - 1) && tick == not_use[ptrs_not_use + 1]) ptrs_not_use++;
      if (!is_done) continue;
      /*
      cout << " cur old\n";
      fr (i, no_found){
	fr (j, k + 1){
	  cout << cur_old[i][j] << " ";
	}
	cout << endl;
      }

      cout << " cur new \n";
      fr (i, no_found){
	fr (j, k + 1){
	  cout << cur_new[i][j] << " ";
	}
	cout << endl;
      }
      */
      fr (i, no_found){
	fr (j, k + 1){
	  fr (p, j + 1){
	    dp_old[i + 1][j] += (dp_old[i][j - p] * cur_old[i][p]);
	    dp_new[i + 1][j] += (dp_new[i][j - p] * cur_new[i][p]);
	  }
	}	
      }
      /*
      cout << " dp old \n";
      fr (i, no_found + 1){
	fr (j, k + 1) {
	  cout << dp_old[i][j] << " ";
	}	
	cout << endl;
      }

      cout << "dp new \n";
      fr (i, no_found + 1){
	fr (j, k + 1){
	  cout << dp_new[i][j] << " ";
	}
	cout << endl;
      }
      */
      int poss_not_use = size_not_use - ptrs_not_use - 1;
      // cout << "poss _nt use " << poss_not_use <<  " " << res_final << endl;
      fr (i, k + 1){
	res_final += ((dp_new[no_found][i] - dp_old[no_found][i]) * (ncr[poss_not_use][k - i]));
	// cout << "sub_iter " << i << " " << k << "  " <<  res_final << endl;
      }

      // cout << "iter_f " << res_final << endl;
    }
    return res_final;
  }
};

/*
int main(){
  int kind_arr[] = {1, 2, 2, 3, 1, 3, 2, 1, 2};
  // int kind_arr[] = {1, 2, 3, 4};
  int kind_size = 9;
  
  int depth_arr[] = {12512, 12859, 125, 1000, 99, 114, 125, 125, 114};
  // int depth_arr[] = {10, 10, 10, 10};
  int depth_size = 9;

  int found_arr[] = {1, 2, 3};
  int found_size = 3;

  int k = 7;


  vector<int> kind(kind_arr, kind_arr + kind_size);
  vector<int> depth(depth_arr, depth_arr + depth_size);
  vector<int> found(found_arr, found_arr + found_size);
  Excavations xx;
  cout << "res_final  " <<  xx.count(kind, depth, found, k) << endl;
  
  return 0;
}
*/
