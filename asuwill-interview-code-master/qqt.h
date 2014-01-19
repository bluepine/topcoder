#ifndef QQT_H
#define QQT_H

/*
��6��
------------------------------------
��Ѷ�����⣺   
����10����ʱ�䣬�������Ÿ���ʮ�������������������Ӧ��ʮ����   
Ҫ������ÿ����������ǰ������ʮ���������ų��ֵĴ�����   
���ŵ�ʮ�������£�   
��0��1��2��3��4��5��6��7��8��9��

�������⣬ò�ƺ��ѣ�10���ӹ�ȥ�ˣ������е��ˣ���Ŀ����û������   

��һ�����ӣ�   
��ֵ: 0,1,2,3,4,5,6,7,8,9   
����: 6,2,1,0,0,0,1,0,0,0   
0�����ų�����6�Σ�1�����ų�����2�Σ�   
2�����ų�����1�Σ�3�����ų�����0��....

ע�⣺����������������Ǳ�����������ŵ�ʮ���������������������������һ���н�
*/

#include <vector>

int count(const std::vector<int>& elems,int value)
{
	int rt = 0;
	for(int i=0;i<elems.size();++i)
	{
		if(elems[i] == value)
			rt ++;
	}
	return rt;
}
void qq(const std::vector<int>& firstLine,std::vector<int>& secondLine)
{
	size_t size = firstLine.size();
	secondLine.clear();
	//first, initialize every item of secondLine to 0
	for(int i=0;i<size;++i)
		secondLine.push_back(0);
	bool changed = true;
	while(changed)
	{
		changed = false;
		for(int i=0;i<size;++i)
		{
			int tmp = secondLine[i];
			secondLine[i] = count(secondLine,firstLine[i]);
			if(tmp != secondLine[i])
				changed=true;
		}
	}
}

#endif