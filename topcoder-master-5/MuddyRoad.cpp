#include <iostream>
#include <vector>
#include <math.h>
#include <string>
using namespace std;

class MuddyRoad{
public:
double dp1[50],dp2[50];
double getExpectedValue(vector<int> r){
	
	for(int i=0;i<50;i++){
		dp1[i]=0.0;dp2[i]=0.0;
	}
	
	int n=r.size();
	dp1[n-1]=1.0;dp2[n-1]=0.0;	
	dp1[n-2]=(r[n-1]/100.00)+1.0;
	dp2[n-2]=(r[n-1]/100.00);
	for(int i=n-3;i>=0;i--){
		double temp=(r[i+2]/100.0)*dp1[i+2]+(1-(r[i+2]/100.00))*dp2[i+2];
		dp1[i]=1+(r[i+1]/100.0)*temp+(1-(r[i+1]/100.0))*dp2[i+1];
		dp2[i]=(r[i+1]/100.0)*temp+(1-(r[i+1]/100.0))*dp2[i+1];
	}		
	
	for(int i=0;i<n-1;i++){
		cout<<dp1[i]<<" "<<dp2[i]<<endl;
	}
		
	double ans=(r[0]/100.0)*dp1[0]+(1-(r[0]/100.0))*dp2[0];
	return (ans);
}
};


int main(){
	vector<int> v;
	int i;cin>>i;
	while(i!=-1){
		v.push_back(i);cin>>i;
	}
	
	MuddyRoad m;
	cout<<m.getExpectedValue(v)<<endl;
	return 0;
}

