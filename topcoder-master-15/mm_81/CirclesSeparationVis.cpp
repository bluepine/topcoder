
// CirclesSeparationVis.cpp : �A�v���P�[�V�����̃N���X������`���܂��B
//

#include "stdafx.h"
#include "CirclesSeparationVis.h"
#include "CirclesSeparationVisDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CCirclesSeparationVisApp

BEGIN_MESSAGE_MAP(CCirclesSeparationVisApp, CWinAppEx)
	ON_COMMAND(ID_HELP, &CWinApp::OnHelp)
END_MESSAGE_MAP()


// CCirclesSeparationVisApp �R���X�g���N�V����

CCirclesSeparationVisApp::CCirclesSeparationVisApp()
{
	// TODO: ���̈ʒu�ɍ\�z�p�R�[�h��ǉ����Ă��������B
	// ������ InitInstance ���̏d�v�ȏ��������������ׂċL�q���Ă��������B
}


// �B��� CCirclesSeparationVisApp �I�u�W�F�N�g�ł��B

CCirclesSeparationVisApp theApp;


// CCirclesSeparationVisApp ������

BOOL CCirclesSeparationVisApp::InitInstance()
{
	CWinAppEx::InitInstance();

/*
	SetRegistryKey(_T("�A�v���P�[�V���� �E�B�U�[�h�Ő������ꂽ���[�J�� �A�v���P�[�V����"));
*/

	CCirclesSeparationVisDlg dlg;
	m_pMainWnd = &dlg;
	INT_PTR nResponse = dlg.DoModal();
	if (nResponse == IDOK)
	{
		// TODO: �_�C�A���O�� <OK> �ŏ����ꂽ���̃R�[�h��
		//  �L�q���Ă��������B
	}
	else if (nResponse == IDCANCEL)
	{
		// TODO: �_�C�A���O�� <�L�����Z��> �ŏ����ꂽ���̃R�[�h��
		//  �L�q���Ă��������B
	}

	// �_�C�A���O�͕����܂����B�A�v���P�[�V�����̃��b�Z�[�W �|���v���J�n���Ȃ���
	//  �A�v���P�[�V�������I�����邽�߂� FALSE ��Ԃ��Ă��������B
	return FALSE;
}
