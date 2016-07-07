package recommend;

import java.util.List;
import java.util.Map;

import com.example.bjmap.R;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionFragment extends Fragment{
	
	private List<Map<String, Object>> maps;
	private ListView list;
	private TextView text;
	private RecommendAdapter adapter;
	private String question;
	private Button next;
	
	private String dislikes, likes;
	private boolean flag;
	
	private ClickEvent click;
	
	public interface ClickEvent{
		public void click();
	}
	
	public QuestionFragment(List<Map<String, Object>> maps, String question, boolean flag){
		this.maps = maps;
		this.question = question;
		this.flag = flag;
	}
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		View view = inflater.inflate(R.layout.question, container, false);
		
		list = (ListView)view.findViewById(R.id.list);
		text = (TextView)view.findViewById(R.id.question);
		next = (Button)view.findViewById(R.id.next);
		
		
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				click.click();				
			}
			
		});
		
		likes = "";
		dislikes = "";
		for(Map<String, Object> item : maps){
			if(flag){
				dislikes += "," + String.valueOf(item.get("id"));
			}else{
				dislikes += "," + String.valueOf(maps.indexOf(item));
			}
			
		}
		text.setText(question);
		adapter = new RecommendAdapter(getActivity(), maps);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
				// TODO Auto-generated method stub
				Map<Integer, Boolean> selected = adapter.getSelected();
				boolean b = !selected.get(position);
				selected.put(position, b);
				adapter.setSelected(selected);
				adapter.notifyDataSetChanged();
				String del = "";
				if(flag){
					del = "," + String.valueOf(maps.get(position).get("id"));
				}else{
					del = "," + position;
				}
				if(b){
					likes += del;
					dislikes = dislikes.replace(del, "");
				}else{
					dislikes += del;
					likes = likes.replace(del, "");
				}
				
			}
		});
        return view;  
    }  
	
	public String getDislikes(){
		return dislikes;
	}
	
	public String getLikes(){
		return likes;
	}
	
	public void setClick(ClickEvent event){
		this.click = event;
	}

}
