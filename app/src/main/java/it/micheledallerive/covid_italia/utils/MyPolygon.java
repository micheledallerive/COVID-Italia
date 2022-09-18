package it.micheledallerive.covid_italia.utils;

import android.graphics.Point;

import com.microsoft.maps.MapPolygon;

public class MyPolygon extends MapPolygon {

    private PolygonClickListener listener;

    public MyPolygon(){
        listener = (p)->{};

    }

    public void setListener(PolygonClickListener listener){
        this.listener = listener;
    }

    public void callListener(Point relativePoint){
        listener.onCall(relativePoint);
    }

    public interface PolygonClickListener{
        void onCall(Point relativePoint);
    }

}
