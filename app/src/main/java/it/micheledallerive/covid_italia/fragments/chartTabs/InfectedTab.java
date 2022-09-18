package it.micheledallerive.covid_italia.fragments.chartTabs;

import static it.micheledallerive.covid_italia.utils.Constants.color;
import static it.micheledallerive.covid_italia.utils.Constants.redColor;
import static it.micheledallerive.covid_italia.utils.Constants.transparentColor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButtonToggleGroup;

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
import it.micheledallerive.covid_italia.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfectedTab extends BaseTab {

    /*

    ca-app-pub-1382810446229806/8825610848


     */



    /*void predictNextValues(ArrayList<Entry> infectedValues){
        int size = infectedValues.size()-2;
        double[] vals = new double[size];
        for(int i=0;i<size;i++){
            vals[i]=infectedValues.get(i).getY();
        }
        int p = 3-1;
        int d = 0; // fisso
        int q = 2;
        int P = 1;
        int D = 1;
        int Q = 0;
        int m = 0; // fisso
        int forecastSize = 1;

        float realValue=infectedValues.get(size).getY();

        Log.e("REAL VALUE", realValue+"");

        ArimaParams[] params = new ArimaParams[]{
                new ArimaParams(p, d, q, P, D, Q, m),
                new ArimaParams(p-1, d, q, P, D, Q, m),
                new ArimaParams(p+1, d, q, P, D, Q, m),
                new ArimaParams(p, d, q-1, P, D, Q, m),
                new ArimaParams(p, d, q+1, P, D, Q, m),
        };

        for(int i=0;i<params.length;i++){
            ForecastResult forecastResult = Arima.forecast_arima(vals, forecastSize, params[i]);
            double[] forecastData = forecastResult.getForecast();
            Log.e("FORECAST"+i, "Value: "+forecastData[0]+" Upper: "+forecastResult.getForecastUpperConf()[0]+" Lower: "+forecastResult.getForecastLowerConf()[0]+" Difference: "+(Math.abs(realValue-forecastData[0])));
        }
    }*/

    public static LineChart infectedChart;
    public static LineChart infectedPChart;
    public static PieChart tamponiChart;
    public static BarChart barChart;
    public static BarChart tamponiBarChart;
    ArrayList<Entry> infectedValues = new ArrayList<>();
    ArrayList<Entry> infectedPValues = new ArrayList<>();
    ArrayList<Entry> totalValues = new ArrayList<>();
    List<BarEntry> tamponiValues = new ArrayList<>();
    List<BarEntry> nuoviInfettiValues = new ArrayList<>();
    private LockableScrollView scrollView;

    private void setupAds(View v){
        AdView adView1;
        AdView adView2;
        adView1=v.findViewById(R.id.adView);
        adView2=v.findViewById(R.id.adView2);
        if(Constants.isAdEnabled) {adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());}
    }

    public void setData(Object obj, String regionName) {
        if(regionName==null) regionName=ChartFragment2.region;
        /*List<Object> objs = (List<Object>)obj;
        List<DataContainer> containers = (List<DataContainer>)obj;
        //Log.e("REGIONNAME", regionName);
        int k=0;
        if(!regionName.isEmpty()){
            for(int i=0;i<21;i++)
                if(((RegionDataContainer)objs.get(i)).getRegionName().equalsIgnoreCase(regionName)){
                    k=i;
                    break;
                }
        }
        int z=regionName.isEmpty()?1:21;
        for(int i=k;i<containers.size();i+=regionName.isEmpty()?1:21){
            if(objs.get(i) instanceof RegionDataContainer){
                if(!((RegionDataContainer)objs.get(i)).getRegionName().equalsIgnoreCase(regionName))
                    continue;
            }
            int positivi = containers.get(i).getPositivi();
            //Log.e(i+"", ""+positivi);
            infectedValues.add(new Entry(i, positivi));
            totalValues.add(new Entry(i, containers.get(i).getTotale()));
            if(i>=21){
                float nuovi_positivi = containers.get(i).getVariazionePositivi();
                float positivi_ieri = containers.get(i-z).getPositivi();
                float perc = (nuovi_positivi*100)/positivi_ieri;
                infectedPValues.add(new Entry(i, perc));
            }
        }*/

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
        for(int i=startLoop;i<containers.size();i+=incremento){
            x=isRegional?i/21:i;
            int positivi = containers.get(i).getPositivi();
            //Log.e(i+"", ""+positivi);
            infectedValues.add(new Entry(x, positivi));
            totalValues.add(new Entry(x, containers.get(i).getTotale()));
            if(i>=incremento){
                tamponiValues.add(new BarEntry(x, containers.get(i).getTamponi()-containers.get(i-incremento).getTamponi()));

                float nuovi_positivi = containers.get(i).getNuoviPositivi();
                nuoviInfettiValues.add(new BarEntry(x, containers.get(i).getNuoviPositivi()));
                float positivi_ieri = containers.get(i-incremento).getPositivi();
                float perc = (positivi_ieri!=0)?(nuovi_positivi*100)/positivi_ieri:0;
                infectedPValues.add(new Entry(x, perc));
            }
        }

        ChartUtils.setupClicks(scrollView, barChart, infectedChart, infectedPChart);

        int tamponiTotali = containers.get(containers.size()-21+startLoop).getTamponi();
        int positivi = containers.get(containers.size()-21+startLoop).getTotale();
        int tamponiNegativi = tamponiTotali-positivi;
        double percTamponiPositivi = Utils.round((positivi*100/(float)tamponiTotali), 2);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(positivi, "Positivi ("+percTamponiPositivi+"%)"));
        entries.add(new PieEntry(tamponiNegativi, "Negativi ("+(100-percTamponiPositivi)+"%)"));

        ChartUtils.createPieChart(
                tamponiChart,
                entries,
                transparentColor(requireContext()),
                false,
                redColor(getContext()),
                color(getContext(), R.color.gray)
        );

        //predictNextValues(totalValues);

        ChartUtils.createBarChart(getContext(), barChart, nuoviInfettiValues, scrollView, R.color.red, 0, "infetti");
        ChartUtils.createBarChart(getContext(), tamponiBarChart, tamponiValues, scrollView, R.color.gray, 3, "tamponi");

        ChartUtils.createTimeLineChart(getContext(), infectedChart, infectedValues, scrollView, "Infetti", R.color.red, 0);
        ChartUtils.createPercentageLineChart(getContext(), infectedPChart, infectedPValues, scrollView, "infetti", R.color.red, 0);
    }


    public void updateCharts(String regionOrNation){
        infectedValues.clear();
        infectedPValues.clear();
        totalValues.clear();
        nuoviInfettiValues.clear();
        tamponiValues.clear();
        //infectedChart.getLineData().clearValues();
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
        //Log.e("UPDATE", "CREATING INFECTED TAB");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_infected_tab, container, false);
        setupAds(v);
        infectedChart = v.findViewById(R.id.line_chart);
        infectedPChart = v.findViewById(R.id.line_chart2);
        tamponiChart = v.findViewById(R.id.pie_chart);
        barChart = v.findViewById(R.id.bar_chart);
        tamponiBarChart = v.findViewById(R.id.tamponi_bar_chart);
        scrollView=v.findViewById(R.id.scroll_view);

        updateCharts(ChartFragment2.region);

        setupNuoviSwitch(v);
        setupSwitchTamponi(v);

        return v;
    }

    private void setupSwitchTamponi(View v){
        final ViewSwitcher viewSwitcher = v.findViewById(R.id.view_switcher_tamponi);
        View chart1 = v.findViewById(R.id.pie_chart);
        View bar = v.findViewById(R.id.tamponi_bar_chart);

        MaterialButtonToggleGroup toggleGroup = v.findViewById(R.id.toggle_button_2);
        toggleGroup.check(R.id.tamponi_bar_button);
        toggleGroup.setSingleSelection(true);
        toggleGroup.setSelectionRequired(true);
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) ->{
            if(isChecked){
                if (checkedId == R.id.tamponi_bar_button) {
                    if(viewSwitcher.getCurrentView()!=bar)
                        viewSwitcher.showNext();
                } else {
                    if(viewSwitcher.getCurrentView()!=chart1)
                        viewSwitcher.showPrevious();
                }
            }
        });
    }

    public void clearHighlight(){
        if(infectedChart!=null && infectedPChart!=null && barChart!=null){
            infectedChart.highlightValue(null);
            infectedPChart.highlightValue(null);
            barChart.highlightValue(null);
        }
    }

}
