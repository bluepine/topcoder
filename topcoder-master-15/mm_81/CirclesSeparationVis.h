
//
// CirclesSeparationVis.h : Visualizer
//

#pragma once

#ifndef __AFXWIN_H__
	#error "PCH �ɑ΂��Ă��̃t�@�C�����C���N���[�h����O�� 'stdafx.h' ���C���N���[�h���Ă�������"
#endif

#include "resource.h"		// ���C�� �V���{��
#include "CirclesSeparation.h"

//

class CCirclesSeparationVisApp : public CWinAppEx
{
public:
	CCirclesSeparationVisApp();

// �I�[�o�[���C�h
	public:
	virtual BOOL InitInstance();

// ����

	DECLARE_MESSAGE_MAP()
};

extern CCirclesSeparationVisApp theApp;

