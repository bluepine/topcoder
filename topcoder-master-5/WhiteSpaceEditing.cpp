#include <iostream>
#include <vector>
#include <math.h>
#include <algorithm>
using namespace std;

class WhiteSpaceEditing{
public:
	int *dp;
	int *dp1;
	int *temp;
	int getMinimum(vector<int> lines){
		int maxi=-1;int n=lines.size();
		if(n==1) return lines[0];
		cout<<"entered \n";
		for(int i=0;i<n;i++){
			if(maxi<lines[i]) maxi=lines[i];
		}
		dp=new int[maxi+2];dp1=new int[maxi+2];
		for(int i=0;i<=maxi;i++){
			dp[i]=fabs(i-lines[n-1]);
			//cout<<dp[i]<<" ";
		}
		//cout<<endl;
		for(int i=n-2;i>=0;i--){
			int val=lines[i];
			int mini=dp[val];
			for(int j=val;j>=0;j--){
				mini=min(dp[j],mini);
				dp1[j]=fabs(j-val)+mini;
			}	
			mini=dp[val];
			for(int j=val+1;j<=maxi;j++){
				mini=min(dp[j],mini);
				dp1[j]=fabs(j-val)+mini;
			}
			dp=dp1;
			/*
			for(int j=0;j<=maxi;j++)
				cout<<dp[j]<<" ";
			cout<<endl;
			*/
		}	
		return dp[0]+n-1;	
	}

};


int main(){
	int n;
	cin>>n;
	vector<int> a;
	for(int i=0;i<n;i++){ 
		int temp;cin>>temp;
		a.push_back(temp);
	}
	
	WhiteSpaceEditing w;
	cout<<w.getMinimum(a)<<endl;
	return 0;	
}
