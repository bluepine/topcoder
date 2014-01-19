#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>

using namespace std;

class SRMCodingPhase {
public:
	int countScore(vector <int>, vector <int>, int);
};

int SRMCodingPhase::countScore(vector <int> points, vector <int> skills, int luck) {
	int max=0;int l=luck;
	if(skills[0]+skills[1]+skills[2]-luck<=75){
		int temp=0;
		for(int i=0;i<3;i++) temp+=points[i];
		temp=temp-(2*skills[0]+4*skills[1]+8*skills[2]);
		if(luck>=skills[2]) {temp+=8*(skills[2]-1);luck=luck-skills[2]+1;}
		else {temp+=8*luck;luck=0;}
		if(luck>=skills[1]) {temp+=4*(skills[1]-1);luck=luck-skills[1]+1;}
		else {temp+=4*luck;luck=0;}
		if(luck>=skills[0]) {temp+=2*(skills[0]-1);luck=luck-skills[0]+1;}
		else {temp+=2*luck;luck=0;}
		max=temp>max?temp:max;
	}
	luck=l;
	if(skills[0]+skills[1]-luck<=75){
		int temp=0;
		for(int i=0;i<2;i++) temp+=points[i];
		temp=temp-(2*skills[0]+4*skills[1]);
		if(luck>=skills[1]) {temp+=4*(skills[1]-1);luck=luck-skills[1]+1;}
		else {temp+=4*luck;luck=0;}
		if(luck>=skills[0]) {temp+=2*(skills[0]-1);luck=luck-skills[0]+1;}
		else {temp+=2*luck;luck=0;}
		max=temp>max?temp:max;
	
	}
	luck=l;
	if(skills[1]+skills[2]-luck<=75){
		int temp=0;
		for(int i=1;i<3;i++) temp+=points[i];
		temp=temp-(4*skills[1]+8*skills[2]);
		if(luck>=skills[2]) {temp+=8*(skills[2]-1);luck=luck-skills[2]+1;}
		else {temp+=8*luck;luck=0;}
		if(luck>=skills[1]) {temp+=4*(skills[1]-1);luck=luck-skills[1]+1;}
		else {temp+=4*luck;luck=0;}
		max=temp>max?temp:max;
	}
	luck=l;
	if(skills[0]+skills[2]-luck<=75){
		int temp=0;
		temp+=points[0]+points[2];
		temp=temp-(2*skills[0]+8*skills[2]);
		if(luck>=skills[2]) {temp+=8*(skills[2]-1);luck=luck-skills[2]+1;}
		else {temp+=8*luck;luck=0;}
		if(luck>=skills[0]) {temp+=2*(skills[0]-1);luck=luck-skills[0]+1;}
		else {temp+=2*luck;luck=0;}
		max=temp>max?temp:max;
	
	}
	luck=l;
	if(skills[0]-luck<=75){
		int temp=0;
		temp=points[0]-2*skills[0];
		if(luck>=skills[0]) {temp+=2*(skills[0]-1);luck=luck-skills[0]+1;}
		else {temp+=2*luck;luck=0;}
		max=temp>max?temp:max;
	}	
	luck=l;
	if(skills[1]-luck<=75){
		int temp=0;
		temp=points[1]-4*skills[1];
		if(luck>=skills[1]) {temp+=4*(skills[1]-1);luck=luck-skills[1]+1;}
		else {temp+=4*luck;luck=0;}
		max=temp>max?temp:max;
	
	}
	luck=l;	
	if(skills[2]-luck<=75){
		int temp=0;
		temp=points[2]-8*skills[2];
		if(luck>=skills[2]) {temp+=8*(skills[2]-1);luck=luck-skills[2]+1;}
		else {temp+=8*luck;luck=0;}
		max=temp>max?temp:max;
	
	}	
	
	return max;
}


double test0() {
	int t0[] = {250, 500, 1000};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {10, 25, 40};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	int p2 = 0;
	SRMCodingPhase * obj = new SRMCodingPhase();
	clock_t start = clock();
	int my_answer = obj->countScore(p0, p1, p2);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p3 = 1310;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p3 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p3 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test1() {
	int t0[] = {300, 600, 900};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {30, 65, 90};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	int p2 = 25;
	SRMCodingPhase * obj = new SRMCodingPhase();
	clock_t start = clock();
	int my_answer = obj->countScore(p0, p1, p2);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p3 = 680;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p3 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p3 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test2() {
	int t0[] = {250, 550, 950};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {10, 25, 40};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	int p2 = 75;
	SRMCodingPhase * obj = new SRMCodingPhase();
	clock_t start = clock();
	int my_answer = obj->countScore(p0, p1, p2);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p3 = 1736;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p3 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p3 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test3() {
	int t0[] = {256, 512, 1024};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {35, 30, 25};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	int p2 = 0;
	SRMCodingPhase * obj = new SRMCodingPhase();
	clock_t start = clock();
	int my_answer = obj->countScore(p0, p1, p2);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p3 = 1216;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p3 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p3 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}
double test4() {
	int t0[] = {300, 600, 1100};
	vector <int> p0(t0, t0+sizeof(t0)/sizeof(int));
	int t1[] = {80, 90, 100};
	vector <int> p1(t1, t1+sizeof(t1)/sizeof(int));
	int p2 = 4;
	SRMCodingPhase * obj = new SRMCodingPhase();
	clock_t start = clock();
	int my_answer = obj->countScore(p0, p1, p2);
	clock_t end = clock();
	delete obj;
	cout <<"Time: " <<(double)(end-start)/CLOCKS_PER_SEC <<" seconds" <<endl;
	int p3 = 0;
	cout <<"Desired answer: " <<endl;
	cout <<"\t" << p3 <<endl;
	cout <<"Your answer: " <<endl;
	cout <<"\t" << my_answer <<endl;
	if (p3 != my_answer) {
		cout <<"DOESN'T MATCH!!!!" <<endl <<endl;
		return -1;
	}
	else {
		cout <<"Match :-)" <<endl <<endl;
		return (double)(end-start)/CLOCKS_PER_SEC;
	}
}

int main() {
	int time;
	bool errors = false;
	
	time = test0();
	if (time < 0)
		errors = true;
	
	time = test1();
	if (time < 0)
		errors = true;
	
	time = test2();
	if (time < 0)
		errors = true;
	
	time = test3();
	if (time < 0)
		errors = true;
	
	time = test4();
	if (time < 0)
		errors = true;
	
	if (!errors)
		cout <<"You're a stud (at least on the example cases)!" <<endl;
	else
		cout <<"Some of the test cases had errors." <<endl;
}

//Powered by [KawigiEdit] 2.0!
