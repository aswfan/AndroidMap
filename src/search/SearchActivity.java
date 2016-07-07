package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.bjmap.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import constant.code;
import constant.url;

public class SearchActivity extends Activity implements OnClickListener{
	
	private mySearchBar ibar;
	private ImageButton backButton;
	private ListView lv;
	private RelativeLayout layout;
	
	private ProgressBar progressBar;
	private TextView mStatusMessageView;
	private View mStatusView;
	private RelativeLayout sb_layout;
	private String myUrl;
	private SearchTask mAuthTask = null;
	private MyAdapter adapter;
	
	private RelativeLayout background;
	
	private EditText eText;
	
	Intent intent;
	
	private ArrayList<Map<String, String>> alist;
	
	private boolean sb_flag = true;
	private boolean flag = true;
	
	
	//定义一个startActivityForResult（）方法用到的整型值 
	private final int requestCode = 1500;
	
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_search);
		
		initView();
	}
	
	public void initView(){
		intent = getIntent();
		
		alist = new ArrayList<Map<String, String>>();
		sb_layout= (RelativeLayout)findViewById(R.id.sb_layout);
		ibar = (mySearchBar)findViewById(R.id.iSearchbar);
		backButton = (ImageButton)findViewById(R.id.back);
		eText = ibar.getEditText();
		lv = (ListView)findViewById(R.id.list);
		layout = (RelativeLayout)findViewById(R.id.layout);
		background = (RelativeLayout)findViewById(R.id.background);
		editTextListener listener = new editTextListener();
		
		mStatusMessageView = (TextView) findViewById(R.id.status_message);
		mStatusView = findViewById(R.id.status);
		mStatusView.bringToFront();
		
		adapter = new MyAdapter(this, alist);
		
		ibar.setFocus();
		
		layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(view.getId() != R.id.iSearchbar){
					ibar.clearFocus();
					hideKeyboard(ibar);
				}
			}
			
		});
		backButton.setOnClickListener(this);
		//设置焦点改变的监听
		eText.setOnFocusChangeListener(listener);
		eText.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (eText.getCompoundDrawables()[2] != null) {
						
						boolean touchable = event.getX() > (eText.getWidth() - eText.getTotalPaddingRight())
								&& (event.getX() < ((eText.getWidth() - ibar.getPaddingRight())));

						if (touchable) {   //点击清除图标
							eText.setText( "" ) ;
							adapter.clearData();
							adapter.notifyDataSetInvalidated();
						}
					}
				}
				return false;
			}
		});
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String url = alist.get(position).get("url");
				
				//新建一个Bundle，Bundle主要放值类型  
		        Bundle bundle = new Bundle();
		        bundle.putString("url", url);
		        bundle.putString("name", alist.get(position).get("title"));
		        bundle.putString("id", alist.get(position).get("id"));
		        bundle.putString("lat", alist.get(position).get("lat"));
		        bundle.putString("lng", alist.get(position).get("lng"));
		        //将Bundle赋给Intent  
		        intent.putExtras(bundle);
		        back2Main(intent, code.backFromSearchActivityWithValue);
				
			}
			
		});
	}
	
	public void hideKeyboard(View view){
		//1.得到InputMethodManager对象
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//2.调用hideSoftInputFromWindow方法隐藏软键盘
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		back2Main(intent, code.backWithoutValue);
        
	}
	
	public void back2Main(Intent intent, final int code){
        SearchActivity.this.setResult(code, intent);
        SearchActivity.this.finish();
	}
	
	Runnable add=new Runnable(){
		  
	        @Override
	        public void run() {
	        	adapter.notifyDataSetChanged();    
	         }       
	    };
	
	
	public class editTextListener implements OnFocusChangeListener {

		private boolean hasFoucs;
		private Drawable mClearDrawable; // 搜索文本框清除文本内容图标
		private String searchKey;
		
		
		
		public editTextListener(){
			
			mClearDrawable = getResources().getDrawable(R.drawable.delete_selector); 
			mClearDrawable.setBounds( 0 , 0, mClearDrawable.getIntrinsicWidth() , mClearDrawable.getIntrinsicHeight());
			
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			this.hasFoucs = hasFocus;
			ibar.setFocus(hasFocus);
			if (hasFocus) { 
				eText.setHint(null);
				setClearIconVisible(eText.getText().length() > 0); 
			} else { 
				setClearIconVisible(false); 
				String baseURL = url.url;
				searchKey = ibar.getContent();
				myUrl = baseURL + "search?bean.searchKey=" + searchKey;
				try{

			        if(searchKey.length()>0){
			        	attempt2Connect(myUrl);
			        }
					
					 
				} catch(Exception e) {
					e.printStackTrace();           
				} 
			} 
		}
		
		public void setFocus(){
			eText.requestFocus();
		}
		
		public void setClearIconVisible(boolean visible){
			Drawable right = visible ? mClearDrawable : null; 
			eText.setCompoundDrawables(eText.getCompoundDrawables()[0], 
					eText.getCompoundDrawables()[1], right, eText.getCompoundDrawables()[3]);
		}
	
		public void attempt2Connect(String url) {
	        if (mAuthTask != null) {
	            return;
	        }
	        if(searchKey == null || searchKey == ""){
	        	return;
	        }
            mStatusMessageView.setText(R.string.progress);
            showProgress(true);
            mAuthTask = new SearchTask();
            mAuthTask.execute(url);
	        }
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

        String temp;
        public HttpClient httpClient;

        @Override
        protected Integer doInBackground(String... param) {
            // TODO: attempt authentication against a network service.
        	temp = "";
        	httpClient  = new DefaultHttpClient();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return -1;
            }
            int result = -1;
            try {
            	HttpGet get = new HttpGet(myUrl);
        		HttpResponse httpResponse = httpClient.execute(get);
        		if (httpResponse.getStatusLine().getStatusCode() == 200) {
        			String res = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                    JSONObject json = new JSONObject(res);  
                    
                    if(Integer.parseInt(json.optJSONObject("index").optString("index"))>0){
                    	result = 0;
                    	JSONArray maps = json.optJSONArray("maps");
                    	for(int i = 0; i < maps.length(); i++){
                    		JSONObject map = maps.getJSONObject(i);
                    		Map<String, String> item = new HashMap<String, String>();
                    		item.put("title", map.optString("name_cn"));
                    		item.put("subTitle", map.optString("intro"));
                    		item.put("id", map.optString("id"));
                    		item.put("url", map.optString("url"));
                    		item.put("lat", map.optString("lat"));
                    		item.put("lng", map.optString("lng"));
                    		alist.add(item);
                    		
                    		
                    	}
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
            adapter.notifyDataSetChanged();
            if(result != -1 ){
                try{
                	
                }catch (Exception e){
                }
                Toast.makeText(SearchActivity.this,"查询成功", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(SearchActivity.this,"查询失败", Toast.LENGTH_LONG).show();
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
