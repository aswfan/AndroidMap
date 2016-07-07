package constant;

import java.util.List;
import java.util.Map;

import android.app.Application;

public class result extends Application{

	private int count;
	private List<Map<String, Object>> maps;
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Map<String, Object>> getMaps() {
		return maps;
	}
	public void setMaps(List<Map<String, Object>> maps) {
		this.maps = maps;
	}
	
	
	
	
}
