#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
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
#include <ctime>

using namespace std;

#define MOD 1000000009



class YetAnotherORProblem2 {
public:
	int countSequences(int, int);
};

int YetAnotherORProblem2::countSequences(int N, int R) {
		
		vector<int> q;
		int n=R;
		while(n!=0){
			q.push_back(n%2);
			n=n/2;
		}
		
		n=q.size();
		
		long long a[40];

		long long b[40];b[0]=1;
		for(int i=1;i<40;i++){
			b[i]=(N+1)*b[i-1];
			b[i]=b[i]%MOD;
		}
		if(q[0]==1) a[0]=N+1;
		else a[0]=N;
		for(int i=1;i<q.size()-1;i++){
			if(q[i]==0){
				a[i]=N*a[i-1];
			}
			else{
				a[i]=a[i-1]+N*b[i-1];
			}
			a[i]=a[i]%MOD;
		}
		return(((N*a[n-1])+b[n-1])%MOD);
		
		
}


//Powered by [KawigiEdit] 2.0!
