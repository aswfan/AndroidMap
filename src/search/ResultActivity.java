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
		//��ת��MainActivity  
        //ע�������RESULT_OK����Ҫ��ش����յ�Activity��onActivityResult��������һ��  
        ResultActivity.this.setResult(code, intent);  
        //�رյ�ǰactivity  
        ResultActivity.this.finish();
	}
	
	public void initContent(){
		web.loadUrl(url);
        //����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
		web.setWebViewClient(new WebViewClient(){
           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
           
             //����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
             view.loadUrl(url);
            return true;
        }
       });
	}
}
