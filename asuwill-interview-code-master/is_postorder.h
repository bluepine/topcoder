#ifndef IS_POSTORDER_H
#define IS_POSTORDER_H

/*
�ж����������ǲ��Ƕ�Ԫ�������ĺ���������
��Ŀ������һ���������飬�жϸ������ǲ���ĳ��Ԫ�������ĺ�������Ľ����
����Ƿ���true�����򷵻�false��
��������5��7��6��9��11��10��8��������һ�����������������ĺ�����������
  8
  / \
  6 10
  / \ / \
  5 7 9 11
��˷���true��
�������7��4��6��5��û���Ŀ����ĺ�������Ľ����������У���˷���false
*/

//judge whether the number sequence is a result of 
//postorder visit of some binary search tree

bool is_postorder(int seq[],int n)
{
	int root = seq[n-1];//root is the last visited element
	int i,j;
	//left child elements are all less than root
	for(i=0;i<n-1;++i)
	{
		if(seq[i]>root)
			break;
	}
	//all right child elements should greater than root
	for(j=i;j<n-1;++j)
	{
		if(seq[j]<root)
			return false;
	}
	bool left = true;
	bool right = true;
	if(i>0)
		left = is_postorder(seq,i);
	if(i<n-1)
		right = is_postorder(seq+i,n-1-i);
	return (left && right);//only if both left and right sub trees are postorder
}


#endif