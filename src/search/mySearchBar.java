package search;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.bjmap.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import bean.SearchResult;

public class mySearchBar extends RelativeLayout implements TextWatcher{
	
	private EditText eText;
	private Drawable mIconSearchDefault; // 搜索文本框默认图标
    private Drawable mClearDrawable; // 搜索文本框清除文本内容图标
	
	private LayoutParams params;
	
	/**
	 * 控件是否有焦点
	 */
	private boolean hasFoucs = true;

	@SuppressLint("NewApi")
	public mySearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		final Resources res = getResources();
        mIconSearchDefault = res.getDrawable(R.drawable.txt_search);
        mClearDrawable = getResources().getDrawable(R.drawable.delete_selector); 
        
		mClearDrawable.setBounds( 0 , 0, mClearDrawable.getIntrinsicWidth() , mClearDrawable.getIntrinsicHeight());
        
		eText = new EditText(context);
		eText.setHint(R.string.search_hint);
		eText.setHintTextColor(Color.parseColor("#BDC7D8"));
		eText.setTextSize(20);
		eText.setInputType(InputType.TYPE_CLASS_TEXT);
		eText.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
		mIconSearchDefault.setBounds(0, 0, mIconSearchDefault.getMinimumWidth()+10, mIconSearchDefault.getMinimumHeight());
		eText.setCompoundDrawables(mIconSearchDefault,null,null,null);

		//默认设置隐藏图标
		setClearIconVisible(false); 
		//设置焦点改变的监听
//		eText.setOnFocusChangeListener(this); 
		//设置输入框里面内容发生改变的监听
		eText.addTextChangedListener(this);
		
        
		
		params = new LayoutParams(900, 150);
		params.addRule(RelativeLayout.CENTER_IN_PARENT); 
		
		addView(eText, params);
	}
	
	public EditText getEditText(){
		return eText;
	}
	
	public String getContent(){
		return eText.getText().toString();
	}
	
	public void setFocus(){
		eText.requestFocus();
	}
	
	public void setClearIconVisible(boolean visible){
		Drawable right = visible ? mClearDrawable : null; 
		eText.setCompoundDrawables(eText.getCompoundDrawables()[0], 
				eText.getCompoundDrawables()[1], right, eText.getCompoundDrawables()[3]);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if(hasFoucs){
			setClearIconVisible(s.length()>0);
		}
	}	

	public void setFocus(boolean hasFocus){
		this.hasFoucs = hasFocus;
	}
	
}
