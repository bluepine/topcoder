#include <iostream>
#include <vector>
#include <string>
using namespace std;


class RectangleArea{
private:
bool conn(string a,string b){
	for(int i=0;i<a.size();i++){
		if(a[i]=='Y'&&b[i]=='Y') return true;	
	}
	return false;
}

int g[50];
	
int parent(int y){
	int x=y;
	while(g[x]!=x){
		x=g[x];
	}	
	while(g[y]!=y){
	 	int temp=y;
		y=g[y];g[temp]=x; 
	}
	return x;
}

public:
int minimumQueries(vector<string> v){
	for(int i=0;i<v.size();i++) g[i]=i;
	
	for(int i=0;i<v.size();i++){
		for(int j=i+1;j<v.size();j++){
			if(conn(v[i],v[j])) g[parent(i)]=parent(j);
		}
	}

	int r,c;r=0;c=0;
	
	for(int j=0;j<v[0].size();j++){
		for(int i=0;i<v.size();i++){
			if(v[i][j]=='Y') {c++;break;}
		}
	}
	c=v[0].size()-c;
	//cout<<c<<endl;
	for(int i=0;i<v.size();i++){
		if(g[i]==i){r++;}
	}
	//cout<<r<<endl;
	return(r+c-1);		
}	
};


int main(){
	int n;cin>>n;
	vector<string> s;
	for(int i=0;i<n;i++){
		string temp;cin>>temp;
		s.push_back(temp);
	}
	RectangleArea p;
	cout<<p.minimumQueries(s)<<endl;



}
