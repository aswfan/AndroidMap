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

import com.example.bjmap.R;
import com.example.bjmap.R.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import components.Header;
import components.Header.OnHeaderListener;
import constant.RouteConstant;
import constant.code;
import constant.source;
import search.SearchActivity;
import search.SearchActivity.SearchTask;

public class PreferenceActivity extends Activity {

	private Header header;
	private EditText like, dislike;
	private Button next;
	
	private String strs = ""; 
	private String likeStr="", dislikeStr="";
	
	private PreferenceListener pListener;
	
	private RelativeLayout background;	
	private ProgressBar progressBar;
	private TextView mStatusMessageView;
	private View mStatusView;
	private SearchTask mAuthTask = null;
	private RelativeLayout sb_layout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preference);
		
		receiveData();
		initConnector();
		
		background = (RelativeLayout)findViewById(R.id.background);
		
		header = (Header)findViewById(R.id.header);
		like = (EditText)findViewById(R.id.input_like);
		dislike = (EditText)findViewById(R.id.input_dislike);
		next = (Button)findViewById(R.id.next_1);
		
		pListener = new PreferenceListener();
		
		setParameters();
		setListener();
		
	}
	
	public void initConnector(){
		mStatusMessageView = (TextView) findViewById(R.id.status_message);
		mStatusView = findViewById(R.id.status);
		mStatusView.bringToFront();
	}
	
	public void receiveData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle.containsKey("date")){
			strs +="date="+ switchDate((String)bundle.get("date"));
		}
		if(bundle.containsKey("days")){
			strs +="&days="+ switchDays((String)bundle.get("days"));
		}
		if(bundle.containsKey("fee")){
			strs +="&fee="+ switchFee((String)bundle.get("fee"));
		}
		if(bundle.containsKey("people")){
			strs +="&people="+ (String)bundle.get("people");
		}
		if(bundle.containsKey("type")){
			strs +="&type="+ (String)bundle.get("type");
		}
//		Toast.makeText(PreferenceActivity.this, strs, Toast.LENGTH_SHORT).show();
	}
	
	public void setParameters(){
		header.setRightVisible(false);
	}
	
	public void setListener(){
		header.setOnHeaderListener(new OnHeaderListener(){

			@SuppressLint("ShowToast")
			@Override
			public void onLeftClick(View arg0) {
				Intent intent = new Intent(PreferenceActivity.this, InfoActivity.class);
				startActivity(intent);
				PreferenceActivity.this.finish();
			}

			@SuppressLint("ShowToast")
			@Override
			public void onRightClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(PreferenceActivity.this, "右按钮", Toast.LENGTH_SHORT).show();
			}
			
		});
		like.setOnClickListener(pListener);
		dislike.setOnClickListener(pListener);
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String like = "";
				String dislike = "";
				if(likeStr!=""){
					like = "&like="+likeStr;
				}
				if(dislikeStr!=""){
					dislike = "&dislike="+dislikeStr;
				}
//				Log.i("strs", strs);
				String url = constant.url.url+"recommend?"+strs+like+dislike;
				Log.i("url", url);
				attempt2Connect(url);
			}
			
		});
		
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        switch(resultCode){  
            case RESULT_OK:{//接收并显示Activity传过来的值  
                Bundle bundle = data.getExtras();  
                String rs = bundle.getString("rs");  
//                Log.i("result meg", rs);
                break;  
            }  
            case code.backFromSearchActivityWithValue:{
            	Bundle bundle = data.getExtras();
            	String name = bundle.getString("name");
            	int source = bundle.getInt("source");
            	if(source == 1){
            		dislike.setText(dislike.getText() + name + ";");
            		if(dislikeStr!=""){
            			dislikeStr += ",";
            		}
            		dislikeStr += bundle.getString("id");
            	}else if(source == 2){
            		like.setText(like.getText() + name + ";");
            		if(likeStr!=""){
            			likeStr += ",";
            		}
            		likeStr += bundle.getString("id");
            		
            	}
            	break;
            }
            case code.backWithoutValue:
            	break;
            default:
                break;  
            }
    }
	
	
	public int switchDate(String str){
		int value = 0;
		if(str.equals("1-3月")){
			value = RouteConstant.date13;
		}
		if(str.equals("4-6月")){
			value = RouteConstant.date46;
		}
		if(str.equals("7-9月")){
			value = RouteConstant.date79;
		}
		if(str.equals("10-12月")){
			value = RouteConstant.date1012;
		}
		return value;
	}
	
	public int switchDays(String str){
		int value = 0;
		if(str.equals("1-3天")){
			value = RouteConstant.days13;
		}
		if(str.equals("4-7天")){
			value = RouteConstant.days47;
		}
		if(str.equals("8-10天")){
			value = RouteConstant.days810;
		}
		if(str.equals("11-15天")){
			value = RouteConstant.days1115;
		}
		if(str.equals(">15天")){
			value = RouteConstant.days15;
		}
		return value;
	}
	
	public int switchFee(String str){
		int value = 0;
		if(str.equals("0~999")){
			value = RouteConstant.price999;
		}
		if(str.equals("1000~2999")){
			value = RouteConstant.price2999;
		}
		if(str.equals("3000~4999")){
			value = RouteConstant.price4999;
		}
		if(str.equals("5000~9999")){
			value = RouteConstant.price9999;
		}
		if(str.equals(">10000")){
			value = RouteConstant.price10000;
		}
		return value;
	}
	
	class PreferenceListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			int source = -1;
			if(v.getId()==R.id.input_dislike){
				source = 1;
			}else if(v.getId()==R.id.input_like){
				source = 2;
			}
			// TODO Auto-generated method stub
			Intent intent = new Intent(PreferenceActivity.this, SearchActivity.class);
			intent.putExtra("source", source);
			startActivityForResult(intent, 1500);
		}
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
        private List<Map<String, Object>> list;
        private List<Map<String, String>> data;
        private int count;

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
//        		Log.i("status", status+"");
        		if (status == 200) {
        			String res = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                    JSONObject json = new JSONObject(res);  
                    int index = json.optInt("index");
                    result = index;
//                    Log.i("index", result+"+"+index);
                    if(index > 0){
                    	
                    	JSONArray maps = json.optJSONArray("maps");
                    	if(index == 1){
                    		count = json.optInt("count");
                    		list = new ArrayList<Map<String, Object>>();
                        	for(int i = 0; i < maps.length(); i++){
                        		JSONObject map = maps.getJSONObject(i);
                        		Map<String, Object> item = new HashMap<String, Object>();
                        		item.put("name", map.optString("name"));
                        		item.put("id", map.optInt("id"));
                        		item.put("type", map.optInt("type"));
                        		list.add(item);
                        	}
                        }else if(index == 2){
                        	data = new ArrayList<Map<String, String>>();
                        	for(int i = 0; i < maps.length(); i++){                        		
                        		JSONObject map = maps.getJSONObject(i);
                        		Map<String, String> item = new HashMap<String, String>();
                        		item.put("title", map.optString("name"));
                        		item.put("subTitle", map.optString("intro"));
                        		item.put("url", map.optString("url"));
                        		item.put("id", map.optString("id"));
                        		data.add(item);
                        	}
                        	
                        }
                    }
                    else if(index == 0){
                    	
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
            	Bundle bundle = getIntent().getExtras();
            	bundle.putInt("index", result);
            	Toast.makeText(PreferenceActivity.this,"查询成功", Toast.LENGTH_LONG).show();
                if(result > 0){
                	if(likeStr!="")
                		bundle.putString("like", likeStr);
                	if(dislikeStr!="")
                		bundle.putString("dislike", dislikeStr);
                	bundle.putString("strs", strs);
                	Intent intent = null;
                	if(result == 1){
                		bundle.putParcelableArrayList("list", (ArrayList)list);
                		bundle.putInt("count", count);
                    	intent = new Intent(PreferenceActivity.this, QuestionActivity.class);
                    	intent.putExtras(bundle);
                    }else if(result == 2){
                    	bundle.putParcelableArrayList("list", (ArrayList)data);
                    	intent = new Intent(PreferenceActivity.this, RecommendActivity.class);
                    	intent.putExtras(bundle);
                    	
                    }
                	startActivity(intent);
                	PreferenceActivity.this.finish();
                }else if( result == 0){
                	
                	if(likeStr!="")
                		bundle.putString("like", likeStr);
                	if(dislikeStr!="")
                		bundle.putString("dislike", dislikeStr);
                	Intent intent = new Intent(PreferenceActivity.this, QuestionActivity.class);
                	startActivity(intent);
                	PreferenceActivity.this.finish();
                }
                
            }
            else {
                Toast.makeText(PreferenceActivity.this,"查询失败", Toast.LENGTH_LONG).show();
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
