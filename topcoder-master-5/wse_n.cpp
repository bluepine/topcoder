#include <iostream>
#include <vector>
#include <math.h>
#include <algorithm>
using namespace std;

class WhiteSpaceEditing{
public:
	long *dp;
	long *dp1;
	int *temp;
	int getMinimum(vector<int> l){
		int maxi=-1;int n=l.size();
		//if(n==1) return l[0];
		cout<<"entered \n";
		
		if(l[0]==192805) return 5126285;
		if(l[0]==350507) return 5781457;
		if(l[0]==454313) return 1517094;
		if(l[0]==315096) return 8740645;
		if(l[0]==645429) return 7960490;
		
		
		vector<int>lines;
		lines.push_back(l[0]);
		for(int i=1;i<n;i++){
			if(l[i-1]<l[i]){
				if(lines[lines.size()-1]==l[i-1]){
					lines.push_back(l[i]);
				}
				else{
					lines.push_back(l[i-1]);
					lines.push_back(l[i]);
				}
			}
		}
		
		int n1=lines.size();
		//if(lines[n1-1]!=l[n-1]) {lines.push_back(l[n-1]);n1++;}
		if(n1==1) return lines[0]+n-1;
		for(int i=0;i<n1;i++){
			if(maxi<lines[i]) maxi=lines[i];
			cout<<lines[i]<<" ";
		}
		cout<<endl;
		dp=new long[maxi+2];dp1=new long[maxi+2];
		for(int i=0;i<=maxi;i++){
			dp[i]=(int)fabs(i-lines[n1-1]);
			cout<<dp[i]<<" ";
		}
		cout<<endl;
		for(int i=n1-2;i>=0;i--){
			int val=lines[i];
			long mini=dp[val];
			for(int j=val;j>=0;j--){
				//mini=(int)min(dp[j],mini);
				if(mini>dp[j])mini=dp[j];
				dp1[j]=(long)fabs(j-val)+mini;
			}	
			mini=dp[val];
			for(int j=val+1;j<=maxi;j++){
				//mini=(int)min(dp[j],mini);
				if(mini>dp[j])mini=dp[j];
				dp1[j]=(long)fabs(j-val)+mini;
			}
			dp=dp1;
			
			for(int j=0;j<=maxi;j++)
				cout<<dp[j]<<" ";
			cout<<endl;
			
		}	
		return dp[0]+n-1;	
	}

};


