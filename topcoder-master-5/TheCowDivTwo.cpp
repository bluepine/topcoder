#include <iostream>
#include <vector>
#include <cstdio>
using namespace std;

#define MOD 1000000007


long long dp[2][50][1001];


class TheCowDivTwo{
public:
int find(int n,int k){
	for(int p=0;p<2;p++){
		for(int i=1;i<=n;i++){
			for(int j=0;j<=k;j++){
				dp[p][j][i]=0;
			}	
		}
	}
	dp[0][0][0]=1;dp[1][0][0]=1;	
	for(int i=0;i<n;i++){
		int s=i%2;int t=(i+1)%2;
		for(int j=1;j<=k && j<=i+1;j++){
			/*	
			if(j>i+1) {
				for(int p=0;p<n;p++)
					dp[t][j][p]=0;
			}
			*/
			//else{
				for(int p=0;p<n;p++){
					int temp;
					if(i>p) temp=((n-i)+p);
					else temp=p-i;
					dp[t][j][p]=(dp[s][j][p]+dp[s][j-1][temp])%MOD;
				}
			//}	
		}
		/*
		cout<<'s'<<" "<<(-3%7)<<endl;
		for(int x=0;x<=k;x++){
			for(int y=0;y<n;y++)
				cout<<dp[t][x][y]<<" ";
			cout<<endl;
		}
		*/
	}
	return (dp[n%2][k][0])%MOD;
}

};


int main(){
	TheCowDivTwo p;
	int n,k;
	cin>>n>>k;
	cout<<p.find(n,k)<<endl;
return 0;
}
