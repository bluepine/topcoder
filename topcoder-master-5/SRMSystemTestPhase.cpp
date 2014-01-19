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
#include <math.h>
#include <cstdlib>
#include <ctime>
#include <string>

using namespace std;



class SRMSystemTestPhase {
public:
	
	int fact[4];int dp[55][4][4];
	int n;vector<int> ns;
	

	int countWays(vector <string>);
};

int SRMSystemTestPhase::countWays(vector <string> d) {

	fact[0]=1;fact[1]=1;fact[2]=2;fact[3]=6;
	
	n=d.size();ns.clear();
	for(int i=0;i<n;i++){
		int t=0;
		for(int j=0;j<3;j++){
			if(d[i][j]=='Y') t++;	
		}
		ns.push_back(t);
		cout<<t<<" ";
	}
	cout<<endl;
	for(int i=0;i<n;i++){
		for(int j=0;j<4;j++){
			for(int k=0;k<4;k++)
				dp[i][j][k]=0;
		}
	}
	
	for(int i=n-1;i>=0;i--){
		for(int j=0;j<4;j++){
			int t=ns[i]-j;	
			if(t>=0){
				if(i==n-1){
					for(int k=0;k<=t;k++){
						dp[i][j][k]=fact[ns[i]]/(fact[t-k]*fact[j]*fact[k]);	
						dp[i][j][k]=dp[i][j][k]%1000000007;
					}
				}
				else{
					int v=j;
					while(v>=0){
						for(int k=0;k<=t;k++){
							int p;
							if(v==j)p=k;
							else p=0;
							while(p<4){
								dp[i][j][k]+=(fact[ns[i]]/(fact[t-k]*fact[j]*fact[k]))*dp[i+1][v][p];
								dp[i][j][k]=dp[i][j][k]%1000000007;
								p++;
							}
							
						}
						v--;
					}
				}
			}
		}
	}
	
	for(int i=0;i<n;i++){
		cout<<i<<":"<<endl;
		for(int j=0;j<4;j++){
			for(int k=0;k<4;k++){
				cout<<dp[i][j][k]<<" ";
			}
			cout<<endl;
		}
	}
				
		
	
	
	int res=0;
	for(int i=0;i<4;i++) {for(int j=0;j<4;j++){res+=dp[0][i][j];res=res%1000000007;}}
	return res;
}


//Powered by [KawigiEdit] 2.0!
