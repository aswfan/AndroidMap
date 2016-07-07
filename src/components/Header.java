package components;

import com.example.bjmap.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Header extends RelativeLayout{
	
	private Button rightButton;
	private ImageButton leftButton;
	private TextView myTitle;
	
	private Drawable leftBackground;
	private Drawable leftSrc;
	
	private int rightTextColor;
	private Drawable rightBackground;
	private String rightText;
	
	private float titleTextSize;
	private int titleTextColor;
	private String title;
	
	private LayoutParams leftParam, rightParam, titleParam;
	
	private OnHeaderListener listener;
	
	public interface OnHeaderListener{
		public void onLeftClick(View arg0);
		public void onRightClick(View arg0);
	}

	public Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.headerAttr);
		
		leftBackground = ta.getDrawable(R.styleable.headerAttr_leftBackground);
		leftSrc = ta.getDrawable(R.styleable.headerAttr_leftSrc);
		
		rightTextColor = ta.getColor(R.styleable.headerAttr_rightTextColor, 0);
		rightBackground = ta.getDrawable(R.styleable.headerAttr_rightBackground);
		rightText = ta.getString(R.styleable.headerAttr_rightText);
		
		titleTextSize = ta.getDimension(R.styleable.headerAttr_myTitleSize, 0);
		titleTextColor = ta.getColor(R.styleable.headerAttr_myTitleColor, 0);
		title = ta.getString(R.styleable.headerAttr_myTitle);
		
		ta.recycle();
		
		leftButton = new ImageButton(context);
		leftButton.setBackground(leftBackground);
		leftButton.setImageDrawable(leftSrc);
		
		leftParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
		leftParam.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		
		rightButton = new Button(context);
		rightButton.setTextColor(rightTextColor);
		rightButton.setBackground(rightBackground);
		rightButton.setText(rightText);
		
		rightParam = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		rightParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
		rightParam.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		
		myTitle = new TextView(context);
		myTitle.setTextColor(titleTextColor);
		myTitle.setTextSize(titleTextSize);
		myTitle.setText(title);
		myTitle.setGravity(Gravity.CENTER);
		
		titleParam = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		titleParam.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
		
		addView(leftButton, leftParam);
		addView(rightButton, rightParam);
		addView(myTitle, titleParam);
		
		leftButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.onLeftClick(arg0);
			}
			
		});
		
		rightButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.onRightClick(arg0);
			}
			
		});
		
	}
	
	public void setOnHeaderListener(OnHeaderListener listener){
		this.listener = listener;
	}
	
	public void setLeftVisible(boolean flag){
		if(flag){
			leftButton.setVisibility(VISIBLE);
		}else{
			leftButton.setVisibility(GONE);
		}
	}
	
	public void setRightVisible(boolean flag){
		if(flag){
			rightButton.setVisibility(VISIBLE);
		}else{
			rightButton.setVisibility(GONE);
		}
	}

}
