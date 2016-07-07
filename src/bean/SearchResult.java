package bean;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

	public SearchResult(){
		resultList = new ArrayList<ResultItem>();
	}
	
	private List<ResultItem> resultList;
	
	public List<ResultItem> getResultList() {
		return resultList;
	}
	public void setResultList(List<ResultItem> resultList) {
		this.resultList = resultList;
	}
	
	public void add(ResultItem item){
		resultList.add(item);
	}
	
	public void clear(){
		resultList.clear();
	}
	
	public ResultItem getByName(String name){
		return null;
	}
	
}
