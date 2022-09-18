package it.micheledallerive.covid_italia.fragments.chartTabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.chart.ChartUtils;
import it.micheledallerive.covid_italia.data.NationalData;
import it.micheledallerive.covid_italia.data.RegionData;
import it.micheledallerive.covid_italia.fragments.ChartFragment2;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.dataContainers.DayData;
import it.micheledallerive.covid_italia.objects.dataContainers.RegionDayData;
import it.micheledallerive.covid_italia.utils.Constants;
import it.micheledallerive.covid_italia.utils.LockableScrollView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeadTab extends BaseTab {

    public static LineChart deadChart, deadPChart;
    public static BarChart newDeadBar;
    LockableScrollView scrollView;
    ArrayList<Entry> deadValues = new ArrayList<>();
    ArrayList<Entry> deadPValues = new ArrayList<>();
    List<BarEntry> newDeadValues = new ArrayList<>();

    private void setupAds(View v){
        AdView adView1;
        AdView adView2;
        adView1=v.findViewById(R.id.adView);
        adView2=v.findViewById(R.id.adView2);
        if(Constants.isAdEnabled) {adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dead_tab, container, false);
        deadChart = v.findViewById(R.id.line_chart);
        deadPChart = v.findViewById(R.id.line_chart2);
        newDeadBar = v.findViewById(R.id.bar_chart);
        setupAds(v);
        scrollView=v.findViewById(R.id.scroll_view);

        updateCharts(ChartFragment2.region);

        setupNuoviSwitch(v);

        return v;
    }


    public void clearHighlight() {
        try{
            deadChart.highlightValue(null);
            deadPChart.highlightValue(null);
        }catch(Exception ignored){}
    }

    public void updateCharts(String regionOrNation) {
        deadValues.clear();
        deadPValues.clear();
        newDeadValues.clear();
        if(regionOrNation.isEmpty())
            NationalData.getData(false, new Callback() {
                @Override
                public void onSuccess(Object obj) {
                    setData(obj,"");
                }

                @Override
                public void onError(Error error) {

                }
            });
        else
            RegionData.getData(false, new Callback() {
                @Override
                public void onSuccess(Object obj) {
                    setData(obj, regionOrNation);
                }

                @Override
                public void onError(Error error) {

                }
            });
    }

    private void setData(Object obj, String region){
        List<DayData> containers = (List<DayData>)obj;
        boolean isRegional = !region.isEmpty();
        int incremento = isRegional?21:1;
        int startLoop=0;
        if(isRegional){
            for(int i=0;i<21;i++)
                if(((RegionDayData)containers.get(i)).getRegionName().equalsIgnoreCase(region)){
                    startLoop=i;
                    break;
                }
        }
        int x;
        for(int i=startLoop;i<containers.size();i+=incremento){
            x=isRegional?i/21:i;
            float p=0;
            deadValues.add(new Entry(x, containers.get(i).getDeceduti()));
            if(i>=incremento) {
                p = (containers.get(i - incremento).getDeceduti() > 0) ? (containers.get(i).getDeceduti() - containers.get(i - incremento).getDeceduti()) * 100 / (float) containers.get(i - incremento).getDeceduti() : 0;
                newDeadValues.add(new BarEntry(x,(containers.get(i - incremento).getDeceduti() > 0) ? (containers.get(i).getDeceduti() - containers.get(i - incremento).getDeceduti()):0));
            }
            deadPValues.add(new Entry(x, p));
        }

        ChartUtils.createBarChart(requireContext(), newDeadBar, newDeadValues, scrollView, R.color.black, 2, "deceduti");
        ChartUtils.createTimeLineChart(requireContext(), deadChart, deadValues, scrollView, "Deceduti", R.color.black, 2);
        ChartUtils.createPercentageLineChart(requireContext(), deadPChart, deadPValues, scrollView, "deceduti", R.color.black, 2);
    }
}
