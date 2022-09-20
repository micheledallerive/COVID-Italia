package it.micheledallerive.covid_italia.fragments;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.microsoft.maps.CopyrightDisplay;
import com.microsoft.maps.Geopath;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapStyleSheets;
import com.microsoft.maps.MapView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.data.RegionData;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.dataContainers.RegionDayData;
import it.micheledallerive.covid_italia.utils.Constants;
import it.micheledallerive.covid_italia.utils.MyPolygon;
import it.micheledallerive.covid_italia.utils.Utils;

public class MapFragment extends Fragment{

    private static final Geopoint ITALY = new Geopoint(42.2925, 12.5736);
    static Point flyoutPoint=null;
    static Point currentPoint=null;
    static int maxInfetti,minInfetti;
    View currentFlyout=null;
    boolean alreadyStarted=false;
    ViewGroup container;
    LayoutInflater inflater;
    View v;
    private MapView mapView;
    private MapElementLayer pinLayer;
    private Map<String, Float> alphas;

    private void setupPinLayer(){
        pinLayer = new MapElementLayer();
        mapView.getLayers().add(pinLayer);
    }

    public void setFlyoutParams(View flyout, Point p){
        flyout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width=flyout.getMeasuredWidth();
        int height=flyout.getMeasuredHeight();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(Utils.getActivity(getContext())).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        if(p.x+width/2<displayMetrics.widthPixels) params.leftMargin=p.x-width/2;
        else{
            params.gravity= Gravity.END; params.rightMargin=displayMetrics.widthPixels-p.x-width/2;}
        params.topMargin=p.y-height;
        flyout.setLayoutParams(params);
    }

    private View createFlyout(RegionDayData oggi, RegionDayData ieri, Point p){
        View flyout = inflater.inflate(R.layout.map_flyout, null);


        int popolazione = Objects.requireNonNull(Constants.popolazioni.get(oggi.getRegionName()));

        double percTamponi = ((oggi.getTamponi()*100)/(double) popolazione);
        percTamponi=Utils.round(percTamponi, 2);
        String desc;
        NumberFormat plusMinusNF = new DecimalFormat("+########;-########");
        desc = "Positivi: "+oggi.getPositivi()+" ("+plusMinusNF.format(oggi.getNuoviPositivi())+")";
        desc+="\nGuariti: "+oggi.getGuariti()+" ("+plusMinusNF.format(oggi.getGuariti()-ieri.getGuariti())+")";
        desc+="\nDeceduti: "+oggi.getDeceduti()+" ("+plusMinusNF.format(oggi.getDeceduti()-ieri.getDeceduti())+")";
        desc+="\nTamponi: "+oggi.getTamponi() ;
        desc+="\n% tamponi: "+percTamponi+"%";
        TextView title = flyout.findViewById(R.id.mapFlyoutTitle);
        TextView description = flyout.findViewById(R.id.mapFlyoutDescription);
        title.setText(oggi.getRegionName());
        description.setText(desc);

        setFlyoutParams(flyout, p);
        return flyout;
    }

    private Map<String, Float> computeAlphas(List<RegionDayData> data) {
        ArrayList<RegionDayData> todayData = new ArrayList<>();
        int currentIndex = data.size()-1;
        while (data.get(currentIndex).getDate().equals(data.get(data.size()-1).getDate())) {
            todayData.add(data.get(currentIndex));
            currentIndex--;
        }

        int max_value = 0;
        int min_value = Integer.MAX_VALUE;

        final float min_alpha = 0.3f;
        final float max_alpha = 0.8f;

        for (RegionDayData dayData : todayData) {
            max_value = Math.max(max_value, dayData.getPositivi());
            min_value = Math.min(min_value, dayData.getPositivi());
        }

        Map<String,Float> alphas = new HashMap<>();

        for (RegionDayData dayData : todayData) {
            float alpha = (dayData.getPositivi() / (float)max_value)
                    * (max_alpha - min_alpha)
                    + min_alpha;
            alphas.put(dayData.getRegionCode(), alpha);
        }
        return alphas;
    }

    private void createMapItems(RegionDayData oggi, RegionDayData ieri){
        MyPolygon polygon = new MyPolygon();
        List<Geopath> paths = new ArrayList<>();
        paths.add(new Geopath(Objects.requireNonNull(RegionData.regioniCoordinate.get(oggi.getRegionCode()))));
        polygon.setTag(oggi.getRegionName());
        polygon.setFillColor(Utils.getTransparentColor(requireContext().getColor(R.color.red), Objects.requireNonNull(alphas.get(oggi.getRegionCode()))));
        polygon.setPaths(paths);
        polygon.setListener((p)->{
            View flyout = createFlyout(oggi, ieri, p);
            ((FrameLayout) v.findViewById(R.id.frame_layout)).addView(flyout);
            flyoutPoint=p;
            currentFlyout = flyout;
        });
        //Log.e("PIN", oggi.getRegionName());
        pinLayer.getElements().add(polygon);
    }

    private void deleteCurrentFlyout(){
        ((FrameLayout) v.findViewById(R.id.frame_layout)).removeView(currentFlyout);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View v = inflater.inflate(R.layout.fragment_map, container, false);
        this.inflater = inflater;
        this.v = v;
        this.container = container;
        if(alreadyStarted||savedInstanceState!=null)return v;
        alreadyStarted=true;
        mapView = new MapView(v.getContext(), MapRenderMode.RASTER);
        mapView.onCreate(savedInstanceState);
        mapView.setCredentialsKey(Constants.MAPS_KEY);
        mapView.setMapStyleSheet(MapStyleSheets.empty());
        mapView.setScene(
                MapScene.createFromLocationAndZoomLevel(ITALY, 5.5),
                MapAnimationKind.NONE);
        mapView.getUserInterfaceOptions().setCompassButtonVisible(false);
        mapView.getUserInterfaceOptions().setCopyrightDisplay(CopyrightDisplay.ALLOW_HIDING);

        mapView.getUserInterfaceOptions().setPanGestureEnabled(true);

        mapView.getUserInterfaceOptions().setRotateGestureEnabled(false);
        mapView.getUserInterfaceOptions().setTiltButtonVisible(false);
        mapView.getUserInterfaceOptions().setTiltGestureEnabled(false);
        mapView.getUserInterfaceOptions().setZoomButtonsVisible(false);
        mapView.getUserInterfaceOptions().setZoomGestureEnabled(false);

        ((FrameLayout)v.findViewById(R.id.frame_layout)).addView(mapView, 0);

        setupPinLayer();

        RegionData.getData(false, new Callback() {
            @Override
            public void onSuccess(Object obj) {

                List<RegionDayData> data = (List<RegionDayData>) obj;
                
                alphas = computeAlphas(data);
                
                ((TextView) v.findViewById(R.id.ultimo_aggiornamento)).setText(String.format("Ultimo aggiornamento: %s", data.get(data.size() - 1).getDateString()));
                    maxInfetti=0;
                    minInfetti=data.get(data.size()-1).getPositivi();
                    for(RegionDayData c : data) {
                        if (c.getPositivi() > maxInfetti && c.getDate().equals(data.get(data.size() - 1).getDate()))
                            maxInfetti = c.getPositivi();
                        if(c.getPositivi()<minInfetti && c.getDate().equals(data.get(data.size()-1).getDate()))
                            minInfetti=c.getPositivi();
                    }

                for(int i=0;i<data.size();i++){
                    RegionDayData c = data.get(i);
                    if(c.getDate().equals(data.get(data.size()-1).getDate()))
                        try{
                            createMapItems(c, data.get(i-21));
                        }catch(Exception e){e.printStackTrace();}
                }
            }

            @Override
            public void onError(Error error) {

            }
        });

        mapView.addOnMapTappedListener(mapTappedEventArgs -> {
            if(currentFlyout!=null) {
                deleteCurrentFlyout();
                currentFlyout=null;
            }
            return false;
        });

        mapView.setOnTouchListener((v1, event) -> {
            deleteCurrentFlyout();
            return false;
        });

        pinLayer.addOnMapElementTappedListener((mapElementTappedEventArgs)->{
            currentPoint=mapElementTappedEventArgs.position;
            ((MyPolygon)mapElementTappedEventArgs.mapElements.get(0)).callListener(mapElementTappedEventArgs.position);
            return false;
        });

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}