package com.example.bjmap;

import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import android.util.Log;
import android.view.MotionEvent;

public class CustomMarker  extends ItemizedOverlayWithFocus<OverlayItem> implements OnItemGestureListener<OverlayItem>{  
    
	public CustomMarker(  
            List<OverlayItem> aList,  
            OnItemGestureListener<OverlayItem> aOnItemTapListener,  
            ResourceProxy pResourceProxy) {  
        super(aList, aOnItemTapListener, pResourceProxy);  
    }  
      
    @Override 
    public void addItem(int location, OverlayItem item) {  
        super.addItem(location, item);  
    }  
    @Override  
    protected boolean onTap(int index) {  
    	Log.i("Navi Route", "1!");
        return super.onTap(index);  
    }  
    @Override  
    public boolean onSingleTapUp(MotionEvent event, MapView mapView) { 
    	Log.i("Navi Route", "2!");
        return super.onSingleTapUp(event, mapView);  
    }  
    @Override  
    public int size() {  
        return super.size();  
    }  

    @Override  
    public boolean onItemLongPress(int arg0, OverlayItem arg1) {  
    	Log.i("Navi Route", "3!");
        return true;  
    }  

    @Override  
    public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) {  
    	Log.i("Navi Route", "4!");
    	
        return true;  
    }  
    
   
      
}  