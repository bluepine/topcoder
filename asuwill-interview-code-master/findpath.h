#ifndef FIND_PATH_H
#define FIND_PATH_H


/*
4.�ڶ�Ԫ�����ҳ���Ϊĳһֵ������·��
��Ŀ������һ��������һ�ö�Ԫ����
�����ĸ���㿪ʼ���·���һֱ��Ҷ��������������н���γ�һ��·����
��ӡ����������������ȵ�����·����
���� ��������22�����¶�Ԫ��
  10   
  / \   
  5 12   
  / \   
  4 7
���ӡ������·����10, 12��10, 5, 7��
*/

#include"bt2list.h"
#include <vector>

void printpath(const std::vector<BSTreeNode*>& path)
{
	using namespace std;
	for(int i=0;i<path.size();++i)
		cout<<path[i]->m_iValue<<" ";
	cout<<endl;
}

void visit(BSTreeNode* node,int sum,int value,std::vector<BSTreeNode*>& path)
{
	sum+=node->m_iValue;
	path.push_back(node);
	if(node->m_pLeft!=NULL)
	{
		visit(node->m_pLeft,sum,value,path);
	}
	if(node->m_pRight!=NULL)
	{
		visit(node->m_pRight,sum,value,path);
	}
	if(node->m_pLeft ==NULL && node->m_pRight ==NULL)
	{
		//leaf node
		if(sum == value)
			printpath(path);
	}
	path.pop_back();
}

void findpath(BSTreeNode* root,int value)
{
	std::vector<BSTreeNode*> path;
	if(root != NULL)
		visit(root,0,value,path);
}

#endif