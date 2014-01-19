#ifndef LIST_INTERSECT_H
#define LIST_INTERSECT_H
/*
��Ŀ������
΢����Ժ֮����ж����������Ƿ��ཻ
�����������������ͷָ�룬����h1��h2���ж������������Ƿ��ཻ��
Ϊ�˼����⣬���Ǽ��������������������

������չ��
1.�����������л�?
2.�����Ҫ������������ཻ�ĵ�һ���ڵ�?
*/

#include <set>
#include "list.h"

// circle exists? 
/*
bool is_circle_exists(List* pHead,List*& ppStart)
{
	using namespace std;
	set<List*> visited;
	set<List*>::iterator it;
	List* tmp = pHead;
	while(tmp)
	{
		it = visited.find(tmp);
		if(it!=visited.end())
		{
			ppStart = *it;//record the circle start point
			return true;
		}
		visited.insert(tmp);
		tmp=tmp->m_pNext;
	}
	ppStart = NULL;
	return false;
}
*/
//�ڱ��˲����Ͽ���һ�����õļ�ⷽ��
//�ò�ͬ�Ĳ�������������л��������������϶�����
bool is_circle_exists(List* pHead,List*& ppStart)
{
	if(pHead == NULL)
		return false;
	List* p1 = pHead;
	List* p2 = pHead->m_pNext;
	while(p2 && p2->m_pNext)
	{
		p1 = p1->m_pNext;
		p2 = p2->m_pNext->m_pNext;
		if(p1 == p2)
		{
			ppStart = p1;
			return true;
		}
	}
	ppStart = NULL;
	return false;
}


//list without circle
bool is_intersect_nc(List* head1,List* head2)
{
	List* tmp1;
	List* tmp2;
	tmp1 = head1;
	tmp2 = head2;
	while(tmp1 && tmp1->m_pNext)
		tmp1 = tmp1->m_pNext;
	while(tmp2 && tmp2->m_pNext)
		tmp2 = tmp2->m_pNext;
	return tmp1==tmp2?true:false;
}

//lists with or without circle 
bool is_intersect(List* head1,List* head2)
{
	List* cs1=NULL;
	List* cs2=NULL;
	bool c1 = is_circle_exists(head1,cs1);
	bool c2 = is_circle_exists(head2,cs2);
	if(c1 && c2)
	{
		//both have circle
		// check whether cs1 is in the circle start from cs2
		List* tmp = cs1->m_pNext;
		while(tmp && tmp!=cs1)
		{
			if(tmp == cs2)
				return true;
			tmp = tmp->m_pNext;
		}
		return tmp==cs2?true:false;
	}
	else if(!(c1||c2))
	{
		//neither has circle
		return is_intersect_nc(head1,head2);
	}
	else
		//one has,the other has no
		return false;
}

#endif