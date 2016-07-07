package com.example.bjmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import constant.constant;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import recommend.InfoActivity;
import android.widget.TextView;
import android.widget.Toast;
import constant.code;
import constant.url;
import search.ResultActivity;
import search.SearchActivity;
import search.SearchActivity.SearchTask;


public class OsmActivity extends Activity implements OnTouchListener, OnGestureListener, GestureDetector.OnDoubleTapListener{
 
	private MapView mMapView;
	private MapController mController;
	
	private ResourceProxy mResourceProxy;
	private MinimapOverlay MinimapOverlayoverlay ;
	
	private Button jingdian, meishi, gouwu, wanle, jiudian;
	private ImageButton zoom_in, zoom_out, location, route, locale;
	private LinearLayout detail;
	private markerTypeListener mtlistener;
	
	private MyLocationOverlay myLocation;
	
	private boolean check_locale = false;
	private boolean check_location = false;
	
	private ArrayList<Map<String, Object>> path, all, jingdianGEO, meishiGEO, gouwuGEO, wanleGEO, jiudianGEO, testPath;
	
	private int zoomlevel = 13;
	
	
	private GeoPoint point_st, point_en;
	private String begin = "";
	private String end = "";
		
	private GestureDetector mGestureDetector;
	
	private Drawable icon_st, icon_en;
	
	private CustomMarker allMarker, pathMarker, userTouchMarker,
	jingdianMarker, meishiMarker, gouwuMarker, wanleMarker, jiudianMarker;
	private PathOverlay myPath;
	private Map<String, Object> pathMap;
	private LinearLayout option;
	private Button clearWay, optimize;
	
	private ArrayList<OverlayItem> items;
	
	private PopupWindow popupWindow;
	
	private int width, height;
	
	private SearchBar sbar;
	private boolean sb_flag = true;
	//定义一个startActivityForResult（）方法用到的整型值 
	private final int requestCode = 1500;
	
	private ProgressBar progressBar;
	private TextView mStatusMessageView;
	private View mStatusView;
	private String myUrl;
	private SearchTask mAuthTask = null;
	
	private RelativeLayout layout_2;
	private LinearLayout layout_3;
	
	private GeoPoint center;
	
	private List<Integer> colors;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_osm);
		
		path = new ArrayList<Map<String, Object>>();
		jingdianGEO = new ArrayList<Map<String, Object>>();
		meishiGEO = new ArrayList<Map<String, Object>>();
		gouwuGEO = new ArrayList<Map<String, Object>>();
		wanleGEO = new ArrayList<Map<String, Object>>();
		jiudianGEO = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> point;
		point_st = new GeoPoint(39.9566713,116.3365702);
		center = point_st;
		
		colors = new ArrayList<Integer>();
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		colors.add(Color.GRAY);
		colors.add(Color.CYAN);
		colors.add(Color.BLACK);
		colors.add(Color.RED);
		colors.add(Color.WHITE);
		
		
		//初始化周边的数据
//		initData();	
				
		//初始化控件
		initVar();	
		
		//初始化地图
		initMap1();
		
		//生成路径
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			Log.i("paint path", "start");
			if(bundle.containsKey("list")){
				path.clear();
				ArrayList array = bundle.getParcelableArrayList("list");
				Map<String, String> map = null;
				double llat = 0;
				double llng = 0;
				Log.i("size", array.size()+"");
				
				List<Integer> content = new ArrayList<Integer>();
				PathOverlay line = new PathOverlay(Color.BLUE, this);
				line.getPaint().setStrokeWidth(4.5F);
				int value = 0;
				route.setVisibility(View.GONE);
				sbar.setVisibility(View.GONE);
				List<GeoPoint> lists = new ArrayList<GeoPoint>();
				
				GeoPoint gg = null;
				for(int i=0; i<array.size();i++){
					
					map = (Map<String, String>)array.get(i);
					int day = Integer.parseInt(map.get("day"));
					
					double lat = Double.parseDouble(map.get("lat"));
					double lng = Double.parseDouble(map.get("lng"));
					if(lat > 0 && lng > 0){
						if(llat==0||llng==0){
							GeoPoint gp = new GeoPoint(lat, lng);
							line.addPoint(gp);
							lists.add(gp);
							
						}else{
							double x = lat-llat;
							double y = lng-llng;
							int times = 1;
							while(Math.abs(x)>0.1||Math.abs(y)>0.1){
								times = times *5;
								x = x/5;
								y = y/5;
							}
							double ini_x = llat;
							double ini_y = llng;
							for(int j=1; j< times;j++){
								ini_x += x;
								ini_y += y;
								if(llat>lat){
									break;
								}
								GeoPoint p = new GeoPoint(ini_x, ini_y);
								line.addPoint(p);
								Log.i("Geo Point", ini_x+"+"+ini_y);
								
							}
							
							gg = new GeoPoint(lat, lng);
							line.addPoint(gg);
							lists.add(gg);
							
						}
						llat = lat;
						llng = lng;						
					}
//					if(!content.contains(day)){
//						mMapView.getOverlays().add(line);
//						content.add(day);
//						line = new PathOverlay(colors.get(value%7), this);
//						line.getPaint().setStrokeWidth(4.5F);
//						line.addPoint(gg);
//						value++;
//					}
				}
				mMapView.getOverlays().add(line);
				addMarker(lists);
//				 mMapView.invalidate();
//				myPath = paintPath(testPath, start, end);
				Log.i("paint path", "finish");
			}else{
				path.clear();
				point = new HashMap<String, Object>();
				point.put("geo", point_st);
				point.put("name", "中央财经大学");
				path.add(point);
				
				double lat = Double.parseDouble(bundle.getString("lat"));
				double lng = Double.parseDouble(bundle.getString("lng"));
				if(lat > 0 && lng > 0){
					route.setVisibility(View.GONE);
					
					GeoPoint gp = new GeoPoint(lat, lng);
		            String name = bundle.getString("name");
		            point = new HashMap<String, Object>();
		            point.put("geo", gp);
		    		point.put("name", name);
		    		path.add(point);
		    		myPath = paintPath(path, point_st, gp);
				}
				
			}
			option.setVisibility(View.VISIBLE);
    		clearWay.setVisibility(View.VISIBLE);
    		clearWay.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					clearPath(myPath);
					clearAll();
					clearWay.setVisibility(View.GONE);
					option.setVisibility(View.GONE);
					route.setVisibility(View.VISIBLE);
					sbar.setVisibility(View.VISIBLE);
				}
    			
    		});
		}
//		PathOverlay line1 = paintPath(path);
		
        
	}
	
	public void initMap1(){
//		mMapView = (MapView) findViewById(R.id.myOSMmapview);  
//        mController = mMapView.getController();  
        //ResourceProxy init  
//        mResourceProxy = new DefaultResourceProxyImpl(this);  
        mMapView.setTileSource(TileSourceFactory.MAPNIK);  
        mMapView.setBuiltInZoomControls(true);  
        mMapView.setMultiTouchControls(true);  
        //定位当前位置，北京市西长安街复兴路  
        GeoPoint center = new GeoPoint(39.901873, 116.326655);  
        mController.setCenter(center);  
        mController.setZoom(14);  
	}
	
	public void clearAll(){
		mMapView.getOverlays().clear();
	}
	
	public void initData(){
		Map<String, Object> point;
		testPath= new ArrayList<Map<String, Object>>();
		
		begin="中央财经大学";
		end="北京交通大学南门";
		
		point = new HashMap<String, Object>();
		point.put("geo", new GeoPoint(39.9568514, 116.3400051));
		point.put("name", "临时：北京XX");
		testPath.add(point);
//		meishiGEO.add(point);
		
		point = new HashMap<String, Object>();
		point.put("geo", new GeoPoint(39.9488105, 116.3423845));
		point.put("name", "临时：北京XX");
		testPath.add(point);
//		jiudianGEO.add(point);
		
		point = new HashMap<String, Object>();
		point.put("geo", new GeoPoint(39.9466066, 116.3357586));
		point.put("name", "临时：北京XX");
		testPath.add(point);
//		jingdianGEO.add(point);
		
		point = new HashMap<String, Object>();
		point.put("geo", new GeoPoint(39.9366066, 116.3287586));
		point.put("name", "临时：北京XX");
		testPath.add(point);
//		wanleGEO.add(point);
		
		point = new HashMap<String, Object>();
		GeoPoint point1 = new GeoPoint(39.8566066, 116.2997586);
		point.put("geo", point1);
		point.put("name", "临时：北京XX");
		testPath.add(point);
//		path.add(point);
//		gouwuGEO.add(point);
		
		
		
		
	}
	
	public void initVar(){		
		width = this.getWindowManager().getDefaultDisplay().getWidth();
	    height = this.getWindowManager().getDefaultDisplay().getHeight();
	    
	    all = new ArrayList<Map<String, Object>>();
	    
	    //初始化起止点的图标
	    icon_st = getResources().getDrawable(R.drawable.icon_st);
	    icon_en = getResources().getDrawable(R.drawable.icon_en);		
		
	    option = (LinearLayout)findViewById(R.id.option);
	    clearWay = (Button)findViewById(R.id.clearWay);
	    optimize = (Button)findViewById(R.id.optimize);
	    mStatusMessageView = (TextView) findViewById(R.id.status_message);
		mStatusView = findViewById(R.id.status);
		mStatusView.bringToFront();
		
		layout_2 = (RelativeLayout)findViewById(R.id.layout_2);
		layout_3 = (LinearLayout)findViewById(R.id.layout_3);
		//搜索框控件
		sbar = (SearchBar)findViewById(R.id.searchbar);
		sbar.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(sb_flag){
					Intent intent = new Intent(OsmActivity.this,SearchActivity.class);  
		            //采用Intent普通传值的方式  
//		            intent.putExtra("source", source.OSM_ACTIVITY);  
		            //跳转Activity  
		            startActivityForResult(intent, requestCode);
		            
		            sb_flag = false;
				}
				
	            return false;
			}
			
		});
		//连接地图控件
		mMapView = (MapView) findViewById(R.id.myOSMmapview);	
		//获取地图控制器
		mController = mMapView.getController();
		mResourceProxy = new DefaultResourceProxyImpl(this); 
		//放大按钮
		zoom_in = (ImageButton)findViewById(R.id.zoomin);
		zoom_in.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mController.zoomIn()){
					Log.i("zoomin", "success!");
				}
			}
			
		});
		//缩小按钮
		zoom_out = (ImageButton)findViewById(R.id.zoomout);
		zoom_out.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mController.zoomOut()){
					Log.i("zoomout", "success!");
				}
			}
			
		});
		//定位按钮
		location = (ImageButton)findViewById(R.id.myLocation);
		location.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				check_location = !check_location;
				if(check_location){
					location.setBackground(getResources().getDrawable(R.drawable.locale_button));
					myLocation = new MyLocationOverlay(OsmActivity.this, mMapView);
					myLocation.enableCompass();
					myLocation.enableFollowLocation();
					myLocation.enableMyLocation();
					mMapView.getOverlays().add(myLocation);
					Log.i("My Location", myLocation.getMyLocation().toDoubleString());
				}else{
					location.setBackground(getResources().getDrawable(R.drawable.fab));
					mMapView.getOverlays().remove(myLocation);
					myLocation = null;
				}
				
			}
			
		});
		//推荐按钮
		route = (ImageButton)findViewById(R.id.route);
		route.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OsmActivity.this, InfoActivity.class);
				startActivity(intent);
				OsmActivity.this.finish();
			}
			
		});
		//
		detail = (LinearLayout)findViewById(R.id.detail);
		//marker类别
//		all = (Button)findViewById(R.id.all);
		jingdian = (Button)findViewById(R.id.jingdian);
		meishi = (Button)findViewById(R.id.meishi);
		gouwu = (Button)findViewById(R.id.gouwu);
		wanle = (Button)findViewById(R.id.wanle);
		jiudian = (Button)findViewById(R.id.jiudian);
		//创建并绑定监听事件
		mtlistener = new markerTypeListener();
//		all.setOnClickListener(mtlistener);
		jingdian.setOnClickListener(mtlistener);
		meishi.setOnClickListener(mtlistener);
		gouwu.setOnClickListener(mtlistener);
		wanle.setOnClickListener(mtlistener);
		jiudian.setOnClickListener(mtlistener);
		//周边
		locale = (ImageButton)findViewById(R.id.locale);
		locale.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				check_locale = !check_locale;
				if(check_locale){
					route.setVisibility(View.GONE);
					locale.setBackground(getResources().getDrawable(R.drawable.locale_button));
					detail.setVisibility(View.VISIBLE);
					all.clear();
					jiudianGEO.clear();
					meishiGEO.clear();
					wanleGEO.clear();
					jingdianGEO.clear();
					gouwuGEO.clear();
					zoom_in.performClick();
					String baseURL = url.url;
					
					myUrl = baseURL + "locale?bean.lat=" + mMapView.getMapCenter().getLatitudeE6()+"&bean.lng="+mMapView.getMapCenter().getLongitudeE6();
					
					attempt2Connect(myUrl);
					
					
				}else{
					route.setVisibility(View.VISIBLE);
					locale.setBackground(getResources().getDrawable(R.drawable.fab));
					detail.setVisibility(View.GONE);
					clearMarker(allMarker);
					zoom_out.performClick();
				}
			}
			
		});
		
	}
		
	public void initMap(){
		//引用离线地图资源并设置地图放大级数和图片大小
		mMapView.setTileSource(new OnlineTileSourceBase("MapQuest",
			ResourceProxy.string.unknown, 0, 18, 256, ".jpg", "http://mt3.google.com/vt/v=w2.97"){
			@Override

			public String getTileURLString(MapTile aTile) {

			return getBaseUrl() + "&x=" + aTile.getX() + "&y="+ aTile.getY() + "&z=" + aTile.getZoomLevel();

			}

		});
		
			
		//设置启用离线地图
		mMapView.setUseDataConnection(false);	
		
		mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        
        mController.setZoom(zoomlevel); // 先设置缩放，后设置中心点，不然会出现偏差。
        mController.setCenter(center);
        
        mMapView.setMinZoomLevel(11);
        mMapView.setMaxZoomLevel(17);
        
        mGestureDetector = new GestureDetector(this);
        mMapView.setOnTouchListener(this);
        mMapView.setFocusable(true);     
        mMapView.setClickable(true);     
        mMapView.setLongClickable(true);
        
      //右下角小地图Overlay  
        MinimapOverlayoverlay = new MinimapOverlay(this,  
                          mMapView.getTileRequestCompleteHandler());  
        MinimapOverlayoverlay.setTileSource(new OnlineTileSourceBase("MapQuest",
			ResourceProxy.string.unknown, 0, 18, 256, ".jpg", "http://mt3.google.com/vt/v=w2.97"){
			@Override

			public String getTileURLString(MapTile aTile) {

			return getBaseUrl() + "&x=" + aTile.getX() + "&y="+ aTile.getY() + "&z=" + aTile.getZoomLevel();

			}

		});
        MinimapOverlayoverlay.setZoomDifference(2);
        MinimapOverlayoverlay.setWidth(300);
        MinimapOverlayoverlay.setHeight(300);
//		        MinimapOverlayoverlay.setPadding(100);
        
        mMapView.getOverlays().add(MinimapOverlayoverlay);
	}

	public PathOverlay paintPath(ArrayList<Map<String, Object>> points, GeoPoint point_st, GeoPoint point_en){
		ArrayList<Map<String, Object>> pathMarkers = new ArrayList<Map<String, Object>>();
		Map<String, Object> marker;
		
		PathOverlay line = new PathOverlay(Color.BLUE, this); 
		line.getPaint().setStrokeWidth(4.5F);
		
        if(points.size() < 1){
        	return null;
        }
        initPathMarker(point_st, "起点", icon_st, pathMarkers);
        initPathMarker(point_en, "终点", icon_en, pathMarkers);
		
        Log.i("points size", points.size()+"");
		
        for(Map<String, Object> point : points){
        	GeoPoint gp = (GeoPoint)point.get("geo");
        	Log.i("GeoPoint", gp.toDoubleString());
			line.addPoint(gp);
        }
        mMapView.getOverlays().add(line);
        pathMarker = addMarker(pathMarkers, null);
        mMapView.invalidate();
        return line;
	}
	
	public ArrayList<Map<String, Object>> initPathMarker(GeoPoint point, String name, Drawable draw, ArrayList<Map<String, Object>> markers){
		
		Map<String, Object> marker = new HashMap<String, Object>();
		marker.put("geo", point);
		marker.put("name", name);
		marker.put("drawable", draw);
		markers.add(marker);
		return markers;
	}
	
	public CustomMarker addMarker(ArrayList<Map<String, Object>> maps, Drawable draw){
		 //添加自定义image Overlay  
       items = new ArrayList<OverlayItem>();
       for(Map<String, Object> map : maps){
    	   if(map != null){
    		   GeoPoint point = (GeoPoint)map.get("geo");
    		   OverlayItem item = new OverlayItem((String)map.get("name"), "", point);
    		   if(draw == null){
    			   item.setMarker((Drawable)map.get("drawable"));
    		   }else{
    			   item.setMarker(draw);
    		   }
        	     
               items.add(item); 
    	   }
    	   
       }
       CustomMarker marker = new CustomMarker(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {  
           @Override  
           public boolean onItemSingleTapUp(final int index,  
                   final OverlayItem item) {
        	   mController.setCenter(item.getPoint());
        	   mMapView.invalidate();
               return true;  
           }  

           @Override  
           public boolean onItemLongPress(final int index,  
                   final OverlayItem item) {
               return false;  
           }  
       }, mResourceProxy);
       mMapView.getOverlays().add(marker);
       mMapView.invalidate(); 
       return marker;
	}
	
	public CustomMarker addMarker(List<GeoPoint> points){
		 //添加自定义image Overlay  
      items = new ArrayList<OverlayItem>();
      for(GeoPoint point : points){
    	  OverlayItem item = new OverlayItem("", "", point);
          item.setMarker(getResources().getDrawable(R.drawable.icon_red));
       	  items.add(item);
      }
      
      
      CustomMarker marker = new CustomMarker(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {  
          @Override  
          public boolean onItemSingleTapUp(final int index,  
                  final OverlayItem item) {
       	   mController.setCenter(item.getPoint());
       	   mMapView.invalidate();
              return true;  
          }  

          @Override  
          public boolean onItemLongPress(final int index,  
                  final OverlayItem item) {
              return false;  
          }  
      }, mResourceProxy);
      mMapView.getOverlays().add(marker);
      mMapView.invalidate(); 
      return marker;
	}
	
	public CustomMarker clearMarker(CustomMarker marker){
		mMapView.getOverlays().remove(marker);
		mMapView.invalidate();
		return null;
	}

	public void clearPath(PathOverlay line){
		mMapView.getOverlays().remove(line);
		line = null;
		clearMarker(pathMarker);
		mMapView.invalidate();
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
		
	}

	@Override
	public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, 
			float paramFloat1,  float paramFloat2) {
		// TODO Auto-generated method stub
		int x = (int) paramFloat1;
		int y = (int) paramFloat2;
		move(rawProcess(x),rawProcess(y));
		return false;
	}
	
	public int rawProcess(int value){
		return Math.abs(value)>5?(5*value/Math.abs(value)):value;
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		mController.zoomIn();
		return false;
	}
	
	public void move(int lat, int log){
		
		int scale = getScale(mMapView.getZoomLevel());
		double x  = 5.5*(lat*scale)/1110000.0;
		double y  = 2.2*(log*scale)/487000.0;
		
		IGeoPoint aCenter = mMapView.getMapCenter();
        aCenter = new GeoPoint(aCenter.getLatitudeE6()/1000000.0D-y, aCenter.getLongitudeE6()/1000000.0D+x);
        mController.animateTo(aCenter);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {		
		float x = event.getX();
		float y = event.getY();
		
		Projection proj = mMapView.getProjection();//获得投影对象        
        GeoPoint  gp = (GeoPoint) proj.fromPixels(x, y);//坐标转换为经纬度
//	    Point gp = new Point((int)event.getX(), (int)event.getY());//触摸点在屏幕上的坐标
        //转换为屏幕上的坐标
        Point pt = proj.toMapPixelsTranslated(new Point(gp.getLatitudeE6(),gp.getLongitudeE6()), null);
	    
        int index = 0;
        if(check_locale){
        	boolean countflag = true;
        	for(Map<String, Object> map : all){
    	    	GeoPoint point = (GeoPoint)map.get("geo");
    		    Point pCenter =  proj.toMapPixelsTranslated(new Point(point.getLatitudeE6(),point.getLongitudeE6()), null);
    	        /**********自己写你要做的事咯，自己YY吧***********/ 

    	        if(countflag&&
    	        		(pt.y < pCenter.y +10 && pt.y > pCenter.y-10)&& 
    	        		(pt.x > pCenter.x && pt.x < pCenter.x+20)){
    	        	mController.animateTo(point);
    	        	mController.setCenter(point);
    	        	this.showPopupWindow(mMapView, x, y, all.get(index));
    	        	countflag = false;
    	        }
    	        index++;
    	    }  
        }else{
        	if(userTouchMarker == null){
        		center = gp;
        		mController.animateTo(center);
        		mController.setCenter(center);
        		ArrayList<Map<String, Object>> item = new ArrayList<Map<String, Object>>();
	        	Map<String, Object> obj = new HashMap<String, Object>();
	        	obj.put("geo", gp);
	        	obj.put("name", "用户点击");
	        	item.add(obj);
	        	userTouchMarker = addMarker(item, getResources().getDrawable(R.drawable.marker_default));
	        	
        	}else{
        		userTouchMarker = clearMarker(userTouchMarker);
        	}
        }
	         
        mMapView.invalidate();//重绘地图  
		return false;
	}
	
	private int popWidth = 600;
	private int popHeight = 400;

	private void showPopupWindow(View view, final float event_x, final float event_y, final Map<String, Object> item) {

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
//        layout.setBackgroundColor(Color.WHITE);  
        TextView tv = new TextView(this);  
        LayoutParams tv_param = new LayoutParams(LayoutParams.FILL_PARENT, popHeight-100);
        
//        tv.setLayoutParams(tv_param);  
        tv.setText((String)item.get("name")+"\n"+(String)item.get("intro"));  
        tv.setTextColor(Color.BLACK);
        tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        tv.setId(1);
        
        
        Button go = new Button(this);
        LayoutParams go_param = new LayoutParams(popWidth/2, LayoutParams.WRAP_CONTENT);
        go.setPadding(0, 0, 0, 0);
        go_param.addRule(RelativeLayout.BELOW, 1);
        go_param.setMargins(0, -50, 0, 0);
//        go.setLayoutParams(go_param);
        go.setText("到这去");
        go.setTextColor(Color.BLACK);
        go.setBackgroundColor(Color.WHITE);
        go.setTextSize(10);
        go.setCompoundDrawables(null, null, null, null);
        
        layout.addView(tv, tv_param);
        layout.addView(go, go_param);
  
        popupWindow = new PopupWindow(layout, popWidth, popHeight);  
          
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bonuspack_bubble));
        
        
        
        popupWindow.setTouchInterceptor(new OnTouchListener() {
        	
        	boolean flag = true;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
            	
            	long downTime = SystemClock.uptimeMillis();
            	MotionEvent myEvent;
            	
            	float x = event.getX();
            	float y = event.getY();
            	
            	if(flag && x > 0 && x < popWidth && 
            			y > 0 && y < popHeight){
            		Log.i("popupWindow", "TextView onTouch");
            		popupWindow.dismiss();
            		
            		String url = (String)item.get("url");
            		
            		Bundle bundle = new Bundle();
    				bundle.putString("url", url);
    				bundle.putString("lat", String.valueOf(item.get("lat")));
    				bundle.putString("lng", String.valueOf(item.get("lng")));
    				bundle.putString("name", (String)item.get("name_cn")+(String)item.get("intro"));
    				toResultPage(bundle);
    				
            		flag = false;
            	}else{
            		Log.i("popupWindow", "Backgroud onTouch");
            		
            		 
            		myEvent = MotionEvent.obtain(  
            		        downTime, downTime, MotionEvent.ACTION_UP, 0, 0, 0); 
            		mGestureDetector.onTouchEvent(myEvent);
            		myEvent.recycle();
            		
            	}
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new OnDismissListener() {
 
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (width-popWidth)/2+30, height/2-popHeight-50);  

    }	
	
	@Override
	public void onLongPress(MotionEvent paramMotionEvent) {
		// TODO Auto-generated method stub
		
	}

	public int getScale(int scale){
		
		switch(scale){
		case 2:
			return 4000000;
		case 3:
			return 3000000;
		case 4:
			return 1000000;
		case 5:
			return 700000;
		case 6:
			return 300000;
		case 7:
			return 200000;
		case 8:
			return 80000;
		case 9:
			return 40000;
		case 10:
			return 20000;
		case 11:
			return 10000;
		case 12:
			return 5000;
		case 13:
			return 3000;
		case 14:
			return 1000;
		case 15:
			return 600;
		case 16:
			return 300;
		case 17:
			return 200;
		case 18:
			return 80;
		default:
			return 1;
			
		}
	}
	
	@Override
	public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, 
			float paramFloat1, float paramFloat2) {
		// TODO Auto-generated method stub
		
		
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent event) {
		// TODO Auto-generated method stub
//		Log.i("Marker info", "7");
	   
		return false;
	}

	@Override
	public void onShowPress(MotionEvent paramMotionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**  
     * 接收当前Activity跳转后，目标Activity关闭后的回传值  
     */  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        switch(resultCode){  
            case RESULT_OK:{//接收并显示Activity传过来的值  
                Bundle bundle = data.getExtras();  
                String rs = bundle.getString("rs");  
                Log.i("result meg", rs);
                break;  
            }  
            case code.backFromSearchActivityWithValue:{
            	Bundle bundle = data.getExtras();                
                toResultPage(bundle);
                OsmActivity.this.finish();
            }
            case code.backWithoutValue:
            	break;
            default:
                break;  
            }  
        sb_flag = true;
    }  
   
    public void toResultPage(Bundle bundle){
    	Intent intent = new Intent(OsmActivity.this, ResultActivity.class);
    	intent.putExtras(bundle);
    	startActivityForResult(intent, requestCode);
    	
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
            layout_2.setAlpha(show ? 0.2f : 1);
            layout_3.setAlpha(show ? 0.2f : 1);
            option.setAlpha(show ? 0.2f : 1);
            sbar.setAlpha(show ? 0.2f : 1);
            mMapView.setAlpha(show ? 0.2f : 1);
            detail.setAlpha(show ? 0.2f : 1);
        } else {
            mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            layout_2.setAlpha(show ? 0.2f : 1);
            layout_3.setAlpha(show ? 0.2f : 1);
            option.setAlpha(show ? 0.2f : 1);
            sbar.setAlpha(show ? 0.2f : 1);
            mMapView.setAlpha(show ? 0.2f : 1);
            detail.setAlpha(show ? 0.2f : 1);
        }
	}

	public class SearchTask extends AsyncTask<String, Integer, Integer> {

        String temp;
        public HttpClient httpClient;

        @Override
        protected Integer doInBackground(String... param) {
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
                    
                    if(json.optInt("count")>0){
                    	result = 0;
                    	JSONArray maps = json.optJSONArray("list");
                    	for(int i = 0; i < maps.length(); i++){
                    		JSONObject map = maps.getJSONObject(i);
                    		switch(Integer.parseInt(map.optString("type"))){
                    		case constant.jingdian:
                    			jingdianGEO = initData(jingdianGEO, map);
                    			break;
                    		case constant.gouwu:
                    			gouwuGEO = initData(gouwuGEO, map);
                    			break;
                    		case constant.jiudian:
                    			jiudianGEO = initData(jiudianGEO, map);
                    			break;
                    		case constant.meishi:
                    			meishiGEO = initData(meishiGEO, map);
                    			break;
                    		case constant.wanle:
                    			wanleGEO = initData(wanleGEO, map);
                    			break;
                			default:
                				break;
                    		}
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
        
        public ArrayList<Map<String, Object>> initData(ArrayList<Map<String, Object>> maps, JSONObject object){
        	Map<String, Object> item = new HashMap<String, Object>();
    		item.put("name", object.optString("name"));
    		item.put("intro", object.optString("intro"));
    		item.put("url", object.optString("url"));
    		
    		GeoPoint gp = new GeoPoint(Double.parseDouble(object.optString("lat")), Double.parseDouble(object.optString("lng")));
    		item.put("geo", gp);
    		item.put("lat", Double.parseDouble(object.optString("lat")));
    		item.put("lng", Double.parseDouble(object.optString("lng")));
    		maps.add(item);
    		return maps;
        }
        
        @Override
        protected void onPostExecute(Integer result) {
            result = result.intValue();
            mAuthTask = null;
            showProgress(false);
            if(result != -1 ){
                Toast.makeText(OsmActivity.this,"查询成功", Toast.LENGTH_LONG).show();
                all.addAll(jingdianGEO);
				all.addAll(meishiGEO);
				all.addAll(gouwuGEO);
				all.addAll(wanleGEO);
				all.addAll(jiudianGEO);
				
				
				//触发点击按钮all的事件
				allMarker = addMarker(all , getResources().getDrawable(R.drawable.icon_red));
	            mMapView.invalidate();
            }
            else {
                Toast.makeText(OsmActivity.this,"查询失败", Toast.LENGTH_LONG).show();
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
    
    public class markerTypeListener implements OnClickListener{

    	private boolean check_all = false;
    	private boolean check_jingdian = false;
    	private boolean check_meishi = false;
    	private boolean check_gouwu = false;
    	private boolean check_wanle = false;
    	private boolean check_jiudian = false;
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Drawable icon = getResources().getDrawable(R.drawable.icon_green);
			switch(v.getId()){
			case R.id.jingdian:
				check_jingdian = !check_jingdian;
				if(check_jingdian){
					jingdian.setBackground(getResources().getDrawable(R.drawable.marker_pressed));
					jingdianMarker = addMarker(jingdianGEO, icon);
					
				}else{
					jingdian.setBackground(getResources().getDrawable(R.drawable.marker_normal));
					clearMarker(jingdianMarker);
				}
				break;
			case R.id.meishi:
				check_meishi = !check_meishi;
				if(check_meishi){
					meishi.setBackground(getResources().getDrawable(R.drawable.marker_pressed));
					meishiMarker = addMarker(meishiGEO, icon);
					
				}else{
					meishi.setBackground(getResources().getDrawable(R.drawable.marker_normal));
					clearMarker(meishiMarker);
				}
				break;
			case R.id.gouwu:
				check_gouwu = !check_gouwu;
				if(check_gouwu){
					gouwu.setBackground(getResources().getDrawable(R.drawable.marker_pressed));
					gouwuMarker = addMarker(gouwuGEO, icon);
					
				}else{
					gouwu.setBackground(getResources().getDrawable(R.drawable.marker_normal));
					clearMarker(gouwuMarker);
				}
				break;
			case R.id.wanle:
				check_wanle = !check_wanle;
				if(check_wanle){
					wanle.setBackground(getResources().getDrawable(R.drawable.marker_pressed));
					wanleMarker = addMarker(wanleGEO, icon);
					
				}else{
					wanle.setBackground(getResources().getDrawable(R.drawable.marker_normal));
					clearMarker(wanleMarker);
				}
				break;
			case R.id.jiudian:
				check_jiudian = !check_jiudian;
				if(check_jiudian){
					jiudian.setBackground(getResources().getDrawable(R.drawable.marker_pressed));
					jiudianMarker = addMarker(jiudianGEO, icon);
					
				}else{
					jiudian.setBackground(getResources().getDrawable(R.drawable.marker_normal));
					clearMarker(jiudianMarker);
				}
				break;
			default:
				break;
			}
		}
    	
    }
}
