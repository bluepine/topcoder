#ifndef LONGEST_PATH_H
#define LONGEST_PATH_H


/*
��11��
��������нڵ��������...
������ǰѶ���������һ��ͼ��
���ӽڵ�֮������߿�����˫��ģ�
���ǹ��Ҷ���"����"Ϊ���ڵ�֮��ߵĸ�����
дһ������
��һ�ö������������Զ�������ڵ�֮��ľ���
*/

#include "bst.h"  //���Ե�ʱ�����ö���������������

struct Result
{
	int leftOrRight;//max(left,right)
	int leftAndRight;//max(left+right)
};

//���￼�ǵ��Ƕ������ڵ㱾��û�д洢�������Ϣ
//��Ϊ��������ǰ��д���������������Ĵ��룬�������

//������¶���������ڵ�Ľṹ�����Զ�����������µ���ʽ��
/*
struct BTreeNode
{
	int m_iValue;
	int m_iMaxPath;
	int m_iMax2Leaf;
	BTreeNode* m_pNext;
};
*/
// �൱�ڽ�ResultǶ���˽ڵ㣬�������Ա��ⷵ�ؽṹ�壬����˼·��Ȼ��һ�µ�

// ������rootΪ���Ķ������д��ڵ��·��ֵleftAndRight��
// �Լ���root��Ҷ�ӽڵ���·��leftOrRight
// ��Ҫ��������ֵ������Ϊ�ϲ���Ҫ�м�ڵ���·����Ϣ�����ж������·��
// ��rootΪ���ڵ�Ķ����������·�����������ֿ��������
// 1����·������root�ڵ㣬��ʱ��
//    ·������=root��������Ҷ�ӽڵ���Զ����+root��������Ҷ�ӽڵ����Զ����
// 2��������root�ڵ㣬�·����������������������
// ��������ݹ������Ҫ���ϲ㴫������ֵ
Result do_longest_path(BSTreeNode* root)
{
	Result result;
	if(root == NULL)
	{
		result.leftAndRight = 0;
		result.leftOrRight = -1;
		return result;
	}

	Result left;
	Result right;
	left = do_longest_path(root->m_pLeft);
	right = do_longest_path(root->m_pRight);

	result.leftOrRight = 1+max(left.leftOrRight,right.leftOrRight);
	result.leftAndRight = max(left.leftAndRight,right.leftAndRight);//��ѡ�����������е��·��
	result.leftAndRight = max(2+left.leftOrRight+right.leftOrRight,result.leftAndRight);//��ѡ�񾭹�root�ڵ���·���벻����root�ڵ��·���г����Ǹ�
	return result;
}

// longest path from node to node
int longest_path(BSTreeNode * root)
{
	if(root == NULL)
		return 0;
	Result result= do_longest_path(root);
	return result.leftAndRight;
}

#endif