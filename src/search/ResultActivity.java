package search;

import com.example.bjmap.OsmActivity;
import com.example.bjmap.R;
import com.example.bjmap.R.id;
import com.example.bjmap.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import components.Header;
import components.Header.OnHeaderListener;
import constant.code;
import search.MyBoard.ButtonClickListener;

public class ResultActivity extends Activity {
	
	private WebView web;
	private Bundle bundle;
	private Header header;
	private MyBoard board;
	
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		initView();
	}
	
	public void initView(){
		header = (Header)findViewById(R.id.myHeader);
		header.setRightVisible(false);
		
		board = (MyBoard)findViewById(R.id.board);
		bundle = getIntent().getExtras();
		web = (WebView)findViewById(R.id.web);
		url = bundle.getString("url");
		Log.i("url", url);
		board.setButtonClickListener(new ButtonClickListener(){

			@Override
			public void OnClickListener() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ResultActivity.this, OsmActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				ResultActivity.this.finish();
			}
			
		});
		header.setOnHeaderListener(new OnHeaderListener(){

			@Override
			public void onLeftClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ResultActivity.this, OsmActivity.class));
				ResultActivity.this.finish();
			}

			@Override
			public void onRightClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		initContent();
	}
	
	public void back2Main(Intent intent, final int code){
		//跳转回MainActivity  
        //注意下面的RESULT_OK常量要与回传接收的Activity中onActivityResult（）方法一致  
        ResultActivity.this.setResult(code, intent);  
        //关闭当前activity  
        ResultActivity.this.finish();
	}
	
	public void initContent(){
		web.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		web.setWebViewClient(new WebViewClient(){
           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
           
             //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
             view.loadUrl(url);
            return true;
        }
       });
	}
}
