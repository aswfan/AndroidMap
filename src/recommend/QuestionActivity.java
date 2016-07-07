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
import com.example.bjmap.R.layout;

import android.app.FragmentManager;  
import android.app.FragmentTransaction;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import components.Header;
import components.Header.OnHeaderListener;
import constant.RouteConstant;
import recommend.PreferenceActivity.SearchTask;
import recommend.QuestionFragment.ClickEvent;
import search.MyAdapter;

public class QuestionActivity extends Activity {
	
	private Header header;
	private Button next;
	
	private ListView list;	
	private List<Map<String, Object>> maps;
	private List<Map<String, Object>> data1, data2, myList;
	
	private int count, size;
	private String likes, dislikes, strs;
	
	private int index;
	private String question1, question2, question;
	
	private QuestionFragment qf;
	
	private RelativeLayout background;	
	private ProgressBar progressBar;
	private TextView mStatusMessageView;
	private View mStatusView;
	private SearchTask mAuthTask = null;
	
	private boolean flag;
	
	private NextEvent event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		initData();
		initConnector();
		
		event = new NextEvent();
		
		header = (Header)findViewById(R.id.header);
        next = (Button)findViewById(R.id.nextBtn);
        
        initFragment();
        
        header.setOnHeaderListener(new OnHeaderListener(){

			@Override
			public void onLeftClick(View arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRightClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(QuestionActivity.this, OsmActivity.class);
				startActivity(intent);
				QuestionActivity.this.finish();
			}
        	
        });
        
        next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(index == 1){
//					likes += qf.getLikes();
			        dislikes += qf.getDislikes();
					
					if(count == size){
						count = 0;
						String likeStr = "";
						Bundle bundle = getIntent().getExtras();
						if(bundle.containsKey("like")){
							likes = bundle.getString("like");
//							if(likes.substring(0, 1).equals(","))
//								likes = bundle.getString("like") + likes;
//							else
//								likes = bundle.getString("like") + "," + likes;
//						}else{
//							if(likes.substring(0, 1).equals(",")){
//								likes = likes.substring(1);
//							}
							likeStr = "&like="+likes;
						}else{
							likes = "";
						}
						
						if(bundle.containsKey("dislike")){
							if(dislikes.substring(0, 1).equals(","))
								dislikes = bundle.getString("dislike") + dislikes;
							else
								dislikes = bundle.getString("dislike") +","+ dislikes;
						}else{
							if(dislikes.substring(0, 1).equals(",")){
								dislikes = dislikes.substring(1);
							}
						}
						if(bundle.containsKey("strs")){
							strs = bundle.getString("strs");
						}
						String url = constant.url.url+"recommend?"+strs+likeStr+"&dislike="+dislikes;
						Log.i("url", url);
						attempt2Connect(url);
					}else{
						FragmentManager fm = getFragmentManager();
						FragmentTransaction tx = fm.beginTransaction();
						if(count == 1){
							qf = new QuestionFragment(data2, question, flag);
							qf.setClick(event);
							tx.replace(R.id.fragment, qf);
							
							tx.commit();
							
						}else if(count>1){
							List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
							for(Map<String, Object> item : maps){
								question = question2;
								if(Object2Int(item.get("type"))==count){
									list.add(item);
								}
							}
							maps.removeAll(list);
							qf = new QuestionFragment(list, question, flag);
							qf.setClick(event);
							tx.replace(R.id.fragment, qf);
							tx.commit();
						}
						count++;
					}
				}else if(index == 0){
					Bundle bundle = getIntent().getExtras();
					strs = "";
					String[] array = qf.getDislikes().split(",");
					int i = 0;
					if(array[0].equals("")){
						i = 1;
					}
					myList = new ArrayList<Map<String, Object>>();
					for(boolean ff = true;i<array.length;i++){
						Map<String, Object> item = data1.get(Integer.parseInt(array[i]));
						if(ff){
							strs += item.get("url")+"="+String.valueOf(item.get("id"));
							ff = false;
						}else{
							strs += "&"+item.get("url")+"="+item.get("id");
						}
						myList.add(item);
						
					}
					
					if(bundle.containsKey("like")){
						likes = "&like=";
						likes += bundle.getString("like");
					}
					likes = bundle.getString("like");
					if(bundle.containsKey("dislike")){
						dislikes = "&dislike=";
						dislikes += bundle.getString("dislike");
					}
					
					String url = constant.url.url+"recommend?" + strs + likes + dislikes;
					Log.i("QuestionActivity", url);
					attempt2Connect(url);
				}
			}
        	
        });
	}
	
	public void initConnector(){
		background = (RelativeLayout)findViewById(R.id.background);
		mStatusMessageView = (TextView) findViewById(R.id.status_message);
		mStatusView = findViewById(R.id.status);
		mStatusView.bringToFront();
	}
	
	public void initFragment(){
		FragmentManager fm = getFragmentManager();  
        FragmentTransaction transaction = fm.beginTransaction();
        qf = new QuestionFragment(data1, question, flag);
        qf.setClick(event);
        transaction.replace(R.id.fragment, qf);
        count++;
        transaction.commit();  
	}
	
	public void initData(){
		count = 0;
		
		likes = "";
		dislikes = "";
		
		question1 = "请在以下列表中选择想去的地点：";
		question2 = "你愿意将以下哪些地点加入旅行计划吗？";
		
		maps = new ArrayList<Map<String, Object>>();
		data1 = new ArrayList<Map<String, Object>>();
		data2 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Bundle bundle = getIntent().getExtras();
		index = bundle.getInt("index");
		if(index==1){
			question = question1;
			flag = true;
			ArrayList array = bundle.getParcelableArrayList("list");
			size = bundle.getInt("count");
			for(int i=0; i<array.size(); i++){
				map = (Map<String, Object>)array.get(i);
				int type = Object2Int(map.get("type"));
				if(type == 0){
					data1.add(map);
				}else if(type == 1){
					data2.add(map);
				}else{
					maps.add(map);
				}
			}
		}else if(index == 0){
			flag = false;
			question = "当前条件太严苛，没有找到满足条件的攻略，为了获得合适攻略，请选择可以删除的条件：";
			data1.clear();
			if(bundle.containsKey("date")){
				map = new HashMap<String, Object>();
				String date = (String)bundle.get("date");
				map.put("id", switchDate(date));
				map.put("name", "出行日期: "+date);
				map.put("url", "date");
				map.put("key", date);
				data1.add(map);
//				strs +="date="+ switchDate((String)bundle.get("date"));
			}
			if(bundle.containsKey("days")){
				map = new HashMap<String, Object>();
				String days = (String)bundle.get("days");
				map.put("id", switchDays(days));
				map.put("name", "出行天数: "+days);
				map.put("url", "days");
				map.put("key", days);
				data1.add(map);
//				strs +="&days="+ switchDays((String)bundle.get("days"));
			}
			if(bundle.containsKey("fee")){
				map = new HashMap<String, Object>();
				String fee = (String)bundle.get("fee");
				map.put("id", switchFee(fee));
				map.put("name", "预算: "+fee);
				map.put("url", "fee");
				map.put("key", fee);
				data1.add(map);
//				strs +="&fee="+ switchFee((String)bundle.get("fee"));
			}
			if(bundle.containsKey("people")){
				map = new HashMap<String, Object>();
				String people = (String)bundle.get("people");
				map.put("id", people);
				map.put("name", "人数: "+people);
				map.put("url", "people");
				map.put("key", people);
				data1.add(map);
//				strs +="&people='"+ (String)bundle.get("people")+"'";
			}
			if(bundle.containsKey("type")){
				map = new HashMap<String, Object>();
				String type = (String)bundle.get("type");
				map.put("id", type);
				map.put("name", "类型: "+type);
				map.put("url", "type");
				map.put("key", type);
				data1.add(map);
//				strs +="&type='"+ (String)bundle.get("type")+"'";
			}
		}
	}
	
	public int Object2Int(Object o){
		return Integer.parseInt(String.valueOf(o));
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
        		if (status == 200) {
        			String res = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                    JSONObject json = new JSONObject(res);  
                    int index = json.optInt("index");
                    result = index;
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
            	Bundle bundle = null;
            	
            	if(index==0){
            		bundle = new Bundle();
            		for(Map<String, Object> item : myList){
            			bundle.putString(String.valueOf(item.get("url")), String.valueOf(item.get("key")));
            		}
            	}else{
            		bundle = getIntent().getExtras();
            	}
            	bundle.putInt("index", result);
            	Toast.makeText(QuestionActivity.this,"查询成功", Toast.LENGTH_LONG).show();
            	
                if(result > 0){
                	
                	if(likes!="")
                		bundle.putString("like", likes);
                	if(dislikes!="")
                		bundle.putString("dislike", dislikes);
                	bundle.putString("strs", strs);
                	Intent intent = null;
                	if(result == 1){
                		bundle.putParcelableArrayList("list", (ArrayList)list);
                		bundle.putInt("count", count);
                    	intent = new Intent(QuestionActivity.this, QuestionActivity.class);
                    	intent.putExtras(bundle);
                    }else if(result == 2){
                    	bundle.putParcelableArrayList("list", (ArrayList)data);
                    	intent = new Intent(QuestionActivity.this, RecommendActivity.class);
                    	intent.putExtras(bundle);
                    	
                    }
                	startActivity(intent);
                	QuestionActivity.this.finish();
                }else if( result == 0){
                	Intent intent = new Intent(QuestionActivity.this, QuestionActivity.class);
                	intent.putExtras(bundle);
                	startActivity(intent);
                	QuestionActivity.this.finish();
                }
                
            }
            else {
                Toast.makeText(QuestionActivity.this,"查询失败", Toast.LENGTH_LONG).show();
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
	
	public class NextEvent implements ClickEvent{

		@Override
		public void click() {
			// TODO Auto-generated method stub
			next.performClick();
		}
		
	}
	
}
