package search;

import java.util.List;
import java.util.Map;

import com.example.bjmap.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private List<Map<String, String>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public MyAdapter(Context context,List<Map<String, String>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }
    
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
        public TextView subTitle;  
        public TextView title;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Zujian zujian = null;
		if(convertView == null){
			zujian = new Zujian();
			//获得组件，实例化组件
			convertView = layoutInflater.inflate(R.layout.item, null);
			zujian.title = (TextView)convertView.findViewById(R.id.title);
			zujian.subTitle = (TextView)convertView.findViewById(R.id.subTitile);
			convertView.setTag(zujian);
		}else{
			zujian = (Zujian)convertView.getTag();
		}
		//绑定数据
		zujian.title.setText(data.get(position).get("title"));
		zujian.subTitle.setText(data.get(position).get("subTitle"));
		return convertView;
	}
	
	public void clearData(){
		data.clear();
	}

}
