#include <iostream>
#include <math.h>
#include <string>
#include <algorithm>
using namespace std;

class MakeSquare{
public:
int dp[52][52];

int edit_dist(string a,string b){
	int m=a.size();
	int n=b.size();
	for(int i=0;i<m+1;i++)
		dp[i][0]=i;
	for(int i=0;i<n+1;i++)
		dp[0][i]=i;
	for(int j=1;j<n+1;j++){
		for(int i=1;i<m+1;i++){
			if(a[i-1]==b[j-1])
				dp[i][j]=dp[i-1][j-1];
			else{
				dp[i][j]=min(dp[i-1][j-1],min(dp[i-1][j],dp[i][j-1]))+1;					
			}
		}
	}
	return dp[m][n];
}

int minChanges(string s){

	int n=s.size();
	
	int mini=25000;
	
	for(int i=0;i<n;i++){
		string a=s.substr(0,i);
		string b=s.substr(i,n-i);
		mini=min(mini,edit_dist(a,b));
	}
	return mini;
}
};


int main(){
	MakeSquare m;
	cout<<m.edit_dist("dvdre","dvder")<<endl;;
	return 0;
}
