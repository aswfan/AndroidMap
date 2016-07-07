package recommend;

import java.util.HashMap;
import java.util.Map;

import com.example.bjmap.OsmActivity;
import com.example.bjmap.R;
import com.example.bjmap.R.id;
import com.example.bjmap.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import components.Header;
import components.Header.OnHeaderListener;
import constant.*;

public class InfoActivity extends Activity {
	private Header header;
	private Spinner date, days, fee, people, type;
	private Button next;
	private String mdate, mdays, mfee, mpeople, mtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		header = (Header)findViewById(R.id.header_1);
		
		date = (Spinner)findViewById(R.id.input_date);
		days = (Spinner)findViewById(R.id.input_days);
		fee = (Spinner)findViewById(R.id.input_fee);
		people = (Spinner)findViewById(R.id.input_people);
		type = (Spinner)findViewById(R.id.input_type);
		
		date.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mdate = getResources().getStringArray(R.array.date)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				mdate = null;
			}
			
		});
		
		days.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mdays = getResources().getStringArray(R.array.days)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				mdays = null;
			}

			
		});
		
		fee.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mfee = getResources().getStringArray(R.array.fee)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				mfee = null;
			}

			
		});
		
		people.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mpeople = getResources().getStringArray(R.array.people)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				mpeople = null;
			}

			
		});
		
		type.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mtype = getResources().getStringArray(R.array.type)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				mtype = null;
			}

			
		});
		
		next = (Button)findViewById(R.id.next_2);
		
		setParameters();
		setListener();
	}
	
	public void setListener(){
		header.setOnHeaderListener(new OnHeaderListener(){

			@SuppressLint("ShowToast")
			@Override
			public void onLeftClick(View arg0) {
				Intent intent = new Intent(InfoActivity.this, OsmActivity.class);
				startActivity(intent);
				InfoActivity.this.finish();
			}

			@SuppressLint("ShowToast")
			@Override
			public void onRightClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(InfoActivity.this, "右按钮", Toast.LENGTH_SHORT).show();
			}
			
		});
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				if(isMeaningless(mdate)){
					Toast.makeText(InfoActivity.this, "必输信息不能为空", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = new Intent(InfoActivity.this, PreferenceActivity.class);
					Bundle map = new Bundle();
					if(!isMeaningless(mdate)){
						map.putString("date", mdate);
					}
					if(!isMeaningless(mdays)){
						map.putString("days", mdays);
					}
					if(!isMeaningless(mfee)){
						map.putString("fee", mfee);
					}
					if(!isMeaningless(mpeople)){
						map.putString("people", mpeople);
					}
					if(!isMeaningless(mtype)){
						map.putString("type", mtype);
					}
					
					intent.putExtras(map);
					startActivity(intent);
					InfoActivity.this.finish();
				}
				
			}
			
		});
		
	}
	
	public void setParameters(){
		header.setRightVisible(false);
	}
	
	public boolean isMeaningless(String str){
		if(str == null || str.equals("请选择"))
			return true;
		return false;
	}
	
	
	
}
