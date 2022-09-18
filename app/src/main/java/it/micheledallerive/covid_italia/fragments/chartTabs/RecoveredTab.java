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
public class RecoveredTab extends BaseTab {

    public static LineChart recoveredChart;
    public static BarChart barChart;
    LineChart recoveredPChart;
    LockableScrollView scrollView;
    ArrayList<Entry> recoveredValues = new ArrayList<>();
    ArrayList<Entry> recoveredPValues = new ArrayList<>();
    List<BarEntry> nuoviRecoveredValues = new ArrayList<>();

    private void setupAds(View v){
        AdView adView1;
        AdView adView2;
        adView1=v.findViewById(R.id.adView);
        adView2=v.findViewById(R.id.adView2);
        if(Constants.isAdEnabled) {adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());}
    }

    private void setData(Object obj, String regionName){
        List<DayData> containers = (List<DayData>) obj;
        boolean isRegional = !regionName.isEmpty();
        int incremento = isRegional ? 21 : 1;
        int startLoop = 0;
        if (isRegional)
            for (int i = 0; i < 21; i++)
                if (((RegionDayData) containers.get(i)).getRegionName().equalsIgnoreCase(regionName)){
                    startLoop = i;
                    break;
                }
        int x;
        float p=0;
        for(int i=startLoop;i<containers.size();i+=incremento){
            x=isRegional?i/21:i;
            recoveredValues.add(new Entry(x, containers.get(i).getGuariti()));
            if(i>=incremento) {
                p = (containers.get(i - incremento).getGuariti() != 0) ? (containers.get(i).getGuariti() - containers.get(i - incremento).getGuariti()) * 100 / (float) containers.get(i - incremento).getGuariti() : 0;
                nuoviRecoveredValues.add(new BarEntry(x, (containers.get(i-incremento).getGuariti()!=0)?(containers.get(i).getGuariti()-containers.get(i-incremento).getGuariti()):0));
            }
            recoveredPValues.add(new Entry(x, p>150?100:p));
        }

        ChartUtils.createBarChart(requireContext(), barChart, nuoviRecoveredValues, scrollView, R.color.green, 1, "guariti");
        ChartUtils.createTimeLineChart(requireContext(), recoveredChart, recoveredValues, scrollView, "Guariti", R.color.green, 1);
        ChartUtils.createPercentageLineChart(requireContext(), recoveredPChart, recoveredPValues, scrollView, "guariti", R.color.green, 1);
    }

    public void updateCharts(String regionOrNation){
        recoveredValues.clear();
        recoveredPValues.clear();
        nuoviRecoveredValues.clear();
        if(regionOrNation.isEmpty())//NAZIONALE
            NationalData.getData(false,new Callback() {
                @Override
                public void onSuccess(Object obj) {
                    setData(obj, "");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recovered_tab, container, false);
        setupAds(v);
        recoveredChart = v.findViewById(R.id.line_chart);
        recoveredPChart = v.findViewById(R.id.line_chart2);
        barChart = v.findViewById(R.id.bar_chart);
        scrollView = v.findViewById(R.id.scroll_view);

        updateCharts(ChartFragment2.region);

        setupNuoviSwitch(v);

        return v;
    }

    public void clearHighlight() {
        try{
            recoveredChart.highlightValue(null);
            recoveredPChart.highlightValue(null);
            barChart.highlightValue(null);
        }catch(Exception ignored){}
    }
}
