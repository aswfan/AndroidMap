package com.example.bjmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchBar extends RelativeLayout{ //implements OnFocusChangeListener , TextWatcher{
	
	private TextView tView;
	private Drawable mIconSearchDefault; // �����ı���Ĭ��ͼ��
    private Drawable mClearDrawable; // �����ı�������ı�����ͼ��
	
	private LayoutParams params;
	
	/**
	 * �ؼ��Ƿ��н���
	 */
	private boolean hasFoucs;

	@SuppressLint("NewApi")
	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		final Resources res = getResources();
        mIconSearchDefault = res.getDrawable(R.drawable.txt_search);
        mClearDrawable = getResources().getDrawable(R.drawable.delete_selector); 
        
		mClearDrawable.setBounds( 0 , 0, mClearDrawable.getIntrinsicWidth() , mClearDrawable.getIntrinsicHeight());
        
		tView = new TextView(context);
		tView.setLines(1);
		tView.setText(R.string.search_hint);
		tView.setTextColor(Color.parseColor("#BDC7D8"));
		tView.setGravity(Gravity.CENTER_VERTICAL);
		tView.setTextSize(20);
		tView.setInputType(InputType.TYPE_CLASS_TEXT);
		tView.setBackground(this.getResources().getDrawable(R.drawable.bg_edittext));
		mIconSearchDefault.setBounds(0, 0, mIconSearchDefault.getMinimumWidth()+10, mIconSearchDefault.getMinimumHeight());
		tView.setCompoundDrawables(mIconSearchDefault,null,null,null);

//		//Ĭ����������ͼ��
//		setClearIconVisible(false); 
//		//���ý���ı�ļ���
//		eText.setOnFocusChangeListener(this); 
//		//����������������ݷ����ı�ļ���
//		eText.addTextChangedListener(this);
//		
//		eText.setOnTouchListener(new OnTouchListener(){
//
//			@Override
//			public boolean onTouch(View arg0, MotionEvent event) {
//				// TODO Auto-generated method stub
//				Log.i("OnTouch", "get it");
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (eText.getCompoundDrawables()[2] != null) {
//						
//						boolean touchable = event.getX() > (eText.getWidth() - eText.getTotalPaddingRight())
//								&& (event.getX() < ((eText.getWidth() - getPaddingRight())));
//
//						if (touchable) {   //������ͼ��
//							eText.setText( "" ) ;
//						}
//					}
//				}
//				return false;
//			}
//			
//		});
		
        
		
		params = new LayoutParams(900, 150);
		params.addRule(RelativeLayout.CENTER_IN_PARENT); 
		
		addView(tView, params);
	}
	
//	public void setClearIconVisible(boolean visible){
//		Drawable right = visible ? mClearDrawable : null; 
//		eText.setCompoundDrawables(eText.getCompoundDrawables()[0], 
//				eText.getCompoundDrawables()[1], right, eText.getCompoundDrawables()[3]);
//	}
//
//
//
//	@Override
//	public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//	@Override
//	public void onTextChanged(CharSequence s, int start, int count, int after) {
//		// TODO Auto-generated method stub
//		if(hasFoucs){
//			setClearIconVisible(s.length()>0);
//		}
//	}
//
//
//
//	@Override
//	public void afterTextChanged(Editable paramEditable) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	/**
//	 * ��ClearEditText���㷢���仯��ʱ���ж������ַ��������������ͼ�����ʾ������
//	 */
//	@Override
//	public void onFocusChange(View paramView, boolean hasFocus) {
//		// TODO Auto-generated method stub
//		this.hasFoucs = hasFocus;
//		if (hasFocus) { 
//			eText.setHint(null);
//			setClearIconVisible(eText.getText().length() > 0); 
//		} else { 
//			setClearIconVisible(false); 
//		} 
//	}

	
}
