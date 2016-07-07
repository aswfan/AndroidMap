package recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.bjmap.OsmActivity;
import com.example.bjmap.R;
import com.example.bjmap.R.id;
import com.example.bjmap.R.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import components.Header;
import components.Header.OnHeaderListener;
import recommend.MyBoard;
import recommend.MyBoard.ButtonClickListener;

public class DetailsActivity extends Activity {

	private WebView web;
	private Bundle bundle;
	private Header header;
	private MyBoard board;
	
	private String url;
	
	private RelativeLayout background;	
	private ProgressBar progressBar;
	private TextView mStatusMessageView;
	private View mStatusView;
	private SearchTask mAuthTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		initConnector();
		initView();
		
	}
	
	public void initConnector(){
		background = (RelativeLayout)findViewById(R.id.background);
		mStatusMessageView = (TextView) findViewById(R.id.status_message);
		mStatusView = findViewById(R.id.status);
		mStatusView.bringToFront();
	}
	
	public void initView(){
		header = (Header)findViewById(R.id.myHeader);
		header.setRightVisible(false);
		
		board = (MyBoard)findViewById(R.id.board);
		bundle = getIntent().getExtras();
		web = (WebView)findViewById(R.id.web);
		url = bundle.getString("url");
		board.setButtonClickListener(new ButtonClickListener(){

			@Override
			public void OnClickListener() {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(ResultActivity.this, OsmActivity.class);
//				intent.putExtras(bundle);
//				startActivity(intent);
				int rid = getIntent().getExtras().getInt("id");
				String url = constant.url.url+"route?rid="+rid;
				attempt2Connect(url);
				
			}
			
		});
		header.setOnHeaderListener(new OnHeaderListener(){

			@Override
			public void onLeftClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				DetailsActivity.this.setResult(constant.code.backWithoutValue, intent);  
		        //关闭当前activity  
				DetailsActivity.this.finish();
			}

			@Override
			public void onRightClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		initContent();
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
	
	
	
	public void attempt2Connect(String url) {
        if (mAuthTask != null) {
            return;
        }
        mStatusMessageView.setText(R.string.progress);
        showProgress(true);
        mAuthTask = new SearchTask();
        mAuthTask.execute(url);
    }
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mStatusView.setVisibility(View.VISIBLE);
            mStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });
            background.setAlpha(show ? 0.2f : 1);
        } else {
            mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            background.setAlpha(show ? 0.2f : 1);
        }
	}
	
	public class SearchTask extends AsyncTask<String, Integer, Integer> {

        public HttpClient httpClient;
        private List<Map<String, String>> data;

        @Override
        protected Integer doInBackground(String... param) {
            // TODO: attempt authentication against a network service.
        	httpClient  = new DefaultHttpClient();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return -1;
            }
            int result = -1;
            try {
            	HttpGet get = new HttpGet(param[0]);
        		HttpResponse httpResponse = httpClient.execute(get);
        		int status = httpResponse.getStatusLine().getStatusCode();
        		if (status == 200) {
        			result = 1;
        			data = new ArrayList<Map<String, String>>();
        			String res = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        			JSONObject json = new JSONObject(res); 
        			JSONArray maps = json.optJSONArray("maps");
        			for(int i=0; i<maps.length(); i++){
        				JSONObject map = maps.getJSONObject(i);
                		Map<String, String> item = new HashMap<String, String>();
                		item.put("lng", map.optString("lng"));
                		item.put("lat", map.optString("lat"));
                		item.put("name", map.optString("name"));
                		item.put("intro", map.optString("intro"));
                		item.put("shunxu", map.optString("shunxu"));
                		item.put("day", map.optString("day"));
                		item.put("url", map.optString("url"));
                		data.add(item);
        			}
                    
        		}else {
                    result = -1;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;

            // TODO: register the new account here.
        }
        @Override
        protected void onPostExecute(Integer result) {
            result = result.intValue();
            mAuthTask = null;
            showProgress(false);
            if(result != -1 ){
            	
            	Toast.makeText(DetailsActivity.this,"查询成功", Toast.LENGTH_LONG).show();
            	
            	Intent intent = new Intent(DetailsActivity.this, OsmActivity.class);
            	Bundle bundle = new Bundle();
//            	List<Map<String, String>> test = new ArrayList<Map<String, String>>();
//            	Map<String, String> item = new HashMap<String, String>();
//            	item.put("name", "123");
//            	item.put("lat", "40.00000");
//            	item.put("lng", "116.000000");
//            	test.add(item);
//            	item = new HashMap<String, String>();
//            	item.put("name", "324");
//            	item.put("lat", "40.287900");
//            	item.put("lng", "116.068000");
//            	test.add(item);
            	bundle.putParcelableArrayList("list", (ArrayList)data);
            	intent.putExtras(bundle);
            	startActivity(intent);
                
            }
            else {
                Toast.makeText(DetailsActivity.this,"查询失败", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        public void HideSoftKeyboard(Activity activity){
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
	
	
}
