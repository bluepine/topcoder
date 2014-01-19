#include <iostream>
#include <vector>
#include <cstdio>
using namespace std;


double dp[11][11][12];

class TheTicketsDivTwo{
public:
double find(int n,int m,int k){
	
	for(int i=1;i<=n;i++){
		for(int j=1;j<=n;j++){
			for(int p=0;p<=k+1;p++)
				dp[i][j][p]=0.0;
		}
	}
	cout<<"dec \n";	
	for(int p=k;p>=0;p--){
		for(int i=1;i<=n;i++){
			for(int j=1;j<=i;j++){
				if(p==k){
					if(j==1) dp[i][j][p]=1.0;
					else dp[i][j][p]=0.0;
				}
				else{
					if(i==1){dp[i][j][p]=1.0;}
					else if(j==1){
						dp[i][j][p]=(1/6.0)+(0.5*dp[i][i][p+1]);
					}
					else{
						dp[i][j][p]=(0.5*dp[i][j-1][p+1])+(1/3.00)*(dp[i-1][j-1][p+1]);
					}
				}	
			}
		}
	}					

	return dp[n][m][0];
}
};


int main(){
	int m,n,k;
	cin>>n>>m>>k;
	TheTicketsDivTwo p;
	//cout<<p.find(n,m,k)<<endl;
	printf("%1.10f \n",p.find(n,m,k));

return 0;	
}
