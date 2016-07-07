package recommend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyBoard extends RelativeLayout{
	
	private Button go;
	private TextView tview;
	
	private ButtonClickListener listener;
	
	public interface ButtonClickListener{
		public void OnClickListener();
	}
	
	public void setButtonClickListener(ButtonClickListener listener){
		this.listener = listener;
	}

	public MyBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		go = new Button(context);
		tview = new TextView(context);
		
		go.setText("在地图上展示");
		go.setTextSize(20);
		
		
		LayoutParams goParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		goParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		goParam.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		goParam.setMarginEnd(10);
		
		LayoutParams tviewParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tviewParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		tviewParam.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		tviewParam.setMarginEnd(10);
		
		go.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				listener.OnClickListener();
			}
			
		});
		
		addView(go, goParam);
		addView(tview, tviewParam);		
	}

}
