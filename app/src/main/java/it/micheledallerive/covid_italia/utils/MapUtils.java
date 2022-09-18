package it.micheledallerive.covid_italia.utils;

import com.microsoft.maps.Geopath;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.Geoposition;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapFlyout;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapPolygon;

import java.util.ArrayList;
import java.util.List;

public class MapUtils {
    
    private static List<Geoposition> locationsCircle(Geoposition center, int radius){
        double R = 6371; // earth's mean radius in km
        double lat = (center.getLatitude() * Math.PI) / 180; //rad
        double lon = (center.getLongitude() * Math.PI) / 180; //rad
        double d = (radius) / R;  // d = angular distance covered on earth's surface
        List<Geoposition> locs = new ArrayList<>();
        for (int x = 0; x <= 360; x++) {
            Geoposition p2 = new Geoposition(0, 0);
            double brng = x * Math.PI / 180; //rad
            p2.setLatitude(Math.asin(Math.sin(lat) * Math.cos(d) + Math.cos(lat) * Math.sin(d) * Math.cos(brng)));
            p2.setLongitude(((lon + Math.atan2(Math.sin(brng) * Math.sin(d) * Math.cos(lat), Math.cos(d) - Math.sin(lat) * Math.sin(p2.getLatitude()))) * 180) / Math.PI);
            p2.setLatitude((p2.getLatitude() * 180) / Math.PI);
            locs.add(p2);
        }
        return locs;
    }

    public static void addCircle(MapElementLayer circleLayer, String title, Geoposition center, int kmRadius, int fillColor, double fillColorAlpha){
        MapPolygon circle = new MapPolygon();
        List<Geoposition> pos = MapUtils.locationsCircle(center,kmRadius);
        Geopath circlePath = new Geopath(pos);
        List<Geopath> geopaths = new ArrayList<>();
        geopaths.add(circlePath);
        circle.setPaths(geopaths);
        circle.setFillColor(Utils.getTransparentColor(fillColor, fillColorAlpha));
        circleLayer.getElements().add(circle);
        if(!title.isEmpty()){
            MapIcon mi = new MapIcon();
            mi.setImage(null);
            mi.setTitle(title);
            mi.setOpacity(0f);
            mi.setLocation(new Geopoint(center.getLatitude(), center.getLongitude()));
            MapFlyout mf = new MapFlyout();
            mf.setTitle("ESKEREEEEEEEEEEEEEEEEEE");
            mf.setLightDismissEnabled(true);
            mi.setFlyout(mf);
            circleLayer.getElements().add(mi);
        }
    }

}
