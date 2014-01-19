#ifndef REVERSE_SENTENCE_H
#define REVERSE_SENTENCE_H

/*
��ת�����е��ʵ�˳��
��Ŀ������һ��Ӣ�ľ��ӣ���ת�����е��ʵ�˳�򣬵��������ַ���˳�򲻱䡣
�����е����Կո��������Ϊ������������ź���ͨ��ĸһ������
�������롰I am a student.�����������student. a am I��

*/
void reverse(char s[],int n)
{
	char tmp;
	for(char* p1=s,*p2=s+n-1;p1<p2;++p1,--p2)
	{
		tmp = *p1;
		*p1 = *p2;
		*p2 = tmp;
	}
}

void reverse_sentence(char s[],int n)
{
	//1.reverse the whole string
	//  eg:jdp is a boy --> yob a si pdj
	//2.reverse each word
	//  eg:yob a si pdj --> boy a is jdp
	reverse(s,n);
	int p1=0;
	int p2=0;
	while(p2<n)
	{
		p1 = p2;
		while(p2<n && s[p2]!= ' ') ++p2;
		reverse(s+p1,p2-p1);
		++p2;
	}
}

#endif