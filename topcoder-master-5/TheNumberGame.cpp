#include <iostream>
#include <math.h>
#include <vector>
using namespace std;

class TheNumberGame{
public:
int b[1000001];
vector<int> prime_num;
int powerof(int n,int p){
	int res=0;
	while(n>0){
		res+=1;
		n/=p;
	}
	return res;
}
TheNumberGame(){
	for(int i=0;i<1000001;i++)
		b[i]=0;
	for(int i=2;i<1000001;i++){
		//cout<<i<<"begin\n";
		if(b[i]==0){
			for(int j=2;(j*i)<1000001;j++){
				//cout<<j<<endl;
				b[j*i]=1;
			}
			prime_num.push_back(i);
		}
	}
}
long long possibleClues(int n){
	//cout<<"bgin"<<endl;	
	long long res=1;
	vector<int>::iterator it;
	cout<<"comp of :"<<n<<endl;
	for(it=prime_num.begin();it!=prime_num.end()&&((*it)<=n);it++){
		res*=(powerof(n,*it));
//		cout<<" "<<*it<<" "<<powerof(n,*it)<<endl;
		res=res%1000000007;
	}
	return res;
}


};

int main(){
	TheNumberGame p;
	cout<<"fskf\n";
	cout<<p.possibleClues(5)<<" "<<endl;
	cout<<p.possibleClues(16)<<" "<<endl;
	cout<<p.possibleClues(1)<<" "<<endl;
	cout<<p.possibleClues(1000000)<<endl;
return 0;
}
