package recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.bjmap.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class RecommendAdapter extends BaseAdapter{
	
	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;
	private Map<Integer, Boolean> selected;
	
	public RecommendAdapter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
        this.selected = new HashMap<Integer, Boolean>();
        initSelect(data.size());
    }
	
	public void initSelect(int size){
		for(int i=0; i<size; i++){
			this.selected.put(i, false);
		}
	}
	
	public final class Zujian{  
        public CheckBox box; 
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Zujian zujian = null;
		if(convertView == null){
			zujian = new Zujian();
			convertView = layoutInflater.inflate(R.layout.content, null);
			zujian.box = (CheckBox)convertView.findViewById(R.id.checkBox);
			convertView.setTag(zujian);
		}else{
			zujian = (Zujian)convertView.getTag();
		}
		
		zujian.box.setText((String)data.get(position).get("name"));
		
//		zujian.box.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				selected.put(position, !selected.get(position));
//				setSelected(selected);
//			}
//			
//		});
		
		zujian.box.setChecked(selected.get(position));
		return convertView;
	}
	
	public void clearData(){
		data.clear();
	}
	
	public Map<Integer, Boolean> getSelected(){
		return selected;
	}
	
	public void setSelected(Map<Integer, Boolean> selected){
		this.selected = selected;
	}

}
