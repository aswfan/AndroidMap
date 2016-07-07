package recommend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.bjmap.OsmActivity;
import com.example.bjmap.R;
import com.example.bjmap.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import components.Header;
import components.Header.OnHeaderListener;
import constant.code;
import search.MyAdapter;

public class RecommendActivity extends Activity {
	
	private Header header;
	private ListView list;
	
	private MyAdapter adapter;
	
	private List<Map<String, String>> maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		
		initData();
		
		adapter = new MyAdapter(this, maps);
		
		header = (Header)findViewById(R.id.header);
		list = (ListView)findViewById(R.id.list);
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Map<String, String> item = maps.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("url", item.get("url"));
				bundle.putInt("id", Integer.parseInt(item.get("id")));
				Intent intent = new Intent(RecommendActivity.this, DetailsActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, 1500);
				
			}
			
		});
		
		header.setOnHeaderListener(new OnHeaderListener(){

			@Override
			public void onLeftClick(View arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRightClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RecommendActivity.this, OsmActivity.class);
				startActivity(intent);
				RecommendActivity.this.finish();
			}
			
		});
	}
	
	public void initData(){
		maps = new ArrayList<Map<String, String>>();
		Bundle bundle = getIntent().getExtras();
		if(bundle != null &&bundle.containsKey("list")){
			List temp = bundle.getParcelableArrayList("list");
			for(int i=0; i<temp.size(); i++){
				maps.add((Map<String, String>)temp.get(i));
			}
		}
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        switch(resultCode){  
            case RESULT_OK:{//接收并显示Activity传过来的值  
                break;  
            }  
            case code.backFromSearchActivityWithValue:{
            	break;
            }
            case code.backWithoutValue:
            	break;
            default:
                break;  
            }  
    }  
	
}
