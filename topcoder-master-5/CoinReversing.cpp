#include <iostream>
#include <vector>
using namespace std;

class CoinReversing{
public:
double expectedHeads(int n,vector<int> a){
	double x,y,tempx,tempy,temp;
	x=1;
	y=0;
	vector<int>::iterator it;
	for(it=a.begin();it!=a.end();it++){
		temp=(double)((*it+0.000)/n);
		tempx=(1-temp)*x+temp*y;
		tempy=(1-temp)*y+temp*x;
		x=tempx;y=tempy;
	}
	return(n*x);
}

};
