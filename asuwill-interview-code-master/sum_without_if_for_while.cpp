#include <iostream>

using namespace std;

/*
��12��
��Ŀ����1+2+��+n��
Ҫ����ʹ�ó˳�����for��while��if��else��switch��case�ȹؼ����Լ������ж���䣨A?B:C��
*/
//test sum_without_if_for
//#include "sum_without_if_for.h"



int(*func[2])(int n);

int sum0(int n);
int sum1(int n);

int sum0(int n)
{
	return 0;
}

int sum1(int n)
{
	return func[!!n](n-1)+n;//!!n��������ǹؼ�
}
int sum(int n)
{
	return sum1(n);
}
int main()
{
	int n;
	func[0] = sum0;
	func[1] = sum1;

	while(true)
	{
		cin >> n;
		if(n==0)
			break;
		cout<<sum(n)<<endl;
	}
	return 0;
}