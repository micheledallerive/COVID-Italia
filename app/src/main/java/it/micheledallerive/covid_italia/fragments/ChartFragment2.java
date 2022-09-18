package it.micheledallerive.covid_italia.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.TabPagerAdapter;
import it.micheledallerive.covid_italia.data.RegionData;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.dataContainers.RegionDayData;
import it.micheledallerive.covid_italia.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment2 extends Fragment {

    public static String region="";
    static String lastItem="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        /*lineChart = v.findViewById(R.id.line_chart);
        lineChart2 = v.findViewById(R.id.line_chart2);
        lineChart3 = v.findViewById(R.id.line_chart3);
        ArrayList<Entry> infectedValues = new ArrayList<>();
        ArrayList<Entry> recoveredValues = new ArrayList<>();
        ArrayList<Entry> deadValues = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        if(Data.analized){
            JsonArray array = Data.jsonDataNazione;
            for(int i=0;i<array.size();i++){
                JsonObject object = array.get(i).getAsJsonObject();
                int infected = object.get("totale_positivi").getAsInt();
                int recovered = object.get("dimessi_guariti").getAsInt();
                String sData = object.get("data").getAsString();
                int dead = object.get("deceduti").getAsInt();
                infectedValues.add(new Entry(i, infected, sData));
                recoveredValues.add(new Entry(i, recovered, sData));
                deadValues.add(new Entry(i, dead, sData));
                SimpleDateFormat f = new SimpleDateFormat("dd-MM");
                sData=sData.replace("T", " ");
                Date d = null;
                try{d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sData);}catch(Exception e){e.printStackTrace();}
                if(d!=null){
                    sData = f.format(d);
                    dates.add(sData);
                }
            }
        }
        LineDataSet infectedSet = ChartUtils.createDefaultDataSet(infectedValues, Color.RED, "Infetti");
        LineDataSet recoveredSet = ChartUtils.createDefaultDataSet(recoveredValues, Color.GREEN, "Guariti");
        LineDataSet deadSet = ChartUtils.createDefaultDataSet(deadValues, Color.BLACK, "Deceduti");
        LineData data = ChartUtils.setupLineData(infectedSet);
        lineChart = ChartUtils.setupLineChart(lineChart, data, 0);
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dates.get((int)value);
            }
        });
        lineChart.setData(data);

        LineData data2 = ChartUtils.setupLineData(recoveredSet);
        lineChart2 = ChartUtils.setupLineChart(lineChart2, data2, 1);
        lineChart2.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dates.get((int)value);
            }
        });
        lineChart2.setData(data2);

        LineData data3 = ChartUtils.setupLineData(deadSet);
        lineChart3 = ChartUtils.setupLineChart(lineChart3, data3, 2);
        lineChart3.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dates.get((int)value);
            }
        });
        lineChart3.setData(data3);*/


        ViewPager2 viewPager = v.findViewById(R.id.viewpager);
        TabLayout tabs = v.findViewById(R.id.tab_layout);
        final TabPagerAdapter adapter = new TabPagerAdapter(getActivity(), 3);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("app", "tab selected");
                viewPager.setCurrentItem(tab.getPosition());
                adapter.clearHighlights();
                switch (tab.getPosition()) {
                    case 0:
                        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.red, requireContext().getTheme()));
                        break;
                    case 1:
                        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.green, requireContext().getTheme()));
                        break;
                    case 2:
                        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.black, requireContext().getTheme()));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.red,requireContext().getTheme()));
        tabs.selectTab(tabs.getTabAt(0));

        List<String> regList = new ArrayList<>();
        regList.add("Italia");
        RegionData.getData(false, new Callback() {
            @Override
            public void onSuccess(Object obj) {
                List<RegionDayData> d = (List<RegionDayData>) obj;
                for (int i = 0; i < 21; i++)
                    regList.add(d.get(i).getRegionName());
            }

            @Override
            public void onError(Error error) {

            }
        });

        final String[] REGIONI = regList.toArray(new String[0]);

        ArrayAdapter<String> a =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        REGIONI);

        AutoCompleteTextView editTextFilledExposedDropdown =
                v.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setText(region.isEmpty()?REGIONI[0]:region);
        editTextFilledExposedDropdown.setAdapter(a);

        editTextFilledExposedDropdown.setShowSoftInputOnFocus(false);

        editTextFilledExposedDropdown.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                Utils.closeKeyboard(v);
                String selectedRegion = a.getItem(position);
                if ( !lastItem.equalsIgnoreCase(selectedRegion) ) {
                    if (!selectedRegion.equalsIgnoreCase(REGIONI[0]))
                        region = selectedRegion;
                    else
                        region = "";
                    adapter.updateCharts(region);
                    lastItem=selectedRegion;
                }
            });

        return v;
    }

}
