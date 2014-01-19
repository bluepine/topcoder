#include <iostream>
#include <vector>
#include <math.h>
#include <queue>
using namespace std;

typedef vector<int> vit;

long long count(int w,int h){
	vit* v;
	v= new vit(w,0);
	
	queue<vit* > q;
	q.push(v);
	long long res=1;
	long mod= 1000000007;

	while(!q.empty()){
		vit* p=q.front();
		q.pop();
		

		for(int i=0;i<w;i++){
			vit *pq;
			if((*p)[i]<h){
				pq=new vit(*p);
				(*pq)[i]++;
				q.push(pq);
				res++;res%=mod;
			}
		}
		for(int i=0;i<w-1;i++){
			vit *pq;
			if((*p)[i]<h&&(*p)[i]==(*p)[i+1]){
				pq=new vit(*p);
				(*pq)[i]++;(*pq)[i+1]++;
				q.push(pq);
				res++;res%=mod;
			}
		}
		for(int i=0;i<w-2;i++){
			vit *pq;
			if((*p)[i]<h&&(*p)[i]==(*p)[i+2]){
				pq=new vit(*p);
				(*pq)[i]++;(*pq)[i+1]=(*pq)[i];(*pq)[i+2]++;
				q.push(pq);
				res++;res%=mod;
			}
		}
	}

	return res;
}



int main(){
	int w,h;
	cin>>w>>h;
	cout<<count(w,h)<<endl;
	


	}
