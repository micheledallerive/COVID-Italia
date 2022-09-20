package it.micheledallerive.covid_italia.fragments;

import static it.micheledallerive.covid_italia.utils.Constants.color;
import static it.micheledallerive.covid_italia.utils.Constants.redColor;
import static it.micheledallerive.covid_italia.utils.Constants.transparentColor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.chart.ChartUtils;
import it.micheledallerive.covid_italia.data.NationalData;
import it.micheledallerive.covid_italia.data.RegionData;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.dataContainers.NationalDayData;
import it.micheledallerive.covid_italia.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    public static boolean canClick = true;
    private static int entries = 0;
    Context c;
    TextView deadLabel, infectedLabel, recoveredLabel;
    TextView newDeadLabel, newInfectedLabel, newRecoveredLabel;
    TextView variazioneLabel;
    TextView yNewDeadLabel, yNewInfectedLabel, yNewRecoveredLabel;
    MaterialToolbar appBar;
    PieChart tamponiChart;
    SwipeRefreshLayout swipeRefreshLayout;

    public TodayFragment() {
        // Required empty public constructor
    }

    private void enableNavigation(){
        canClick=true;
    }

    private void disableNavigation(){
        canClick=false;
    }

    private void setupLabels(View v){
        infectedLabel = v.findViewById(R.id.infected);
        recoveredLabel = v.findViewById(R.id.recovered);
        deadLabel = v.findViewById(R.id.dead);
        newDeadLabel = v.findViewById(R.id.new_dead);
        newInfectedLabel = v.findViewById(R.id.new_infected);
        newRecoveredLabel = v.findViewById(R.id.new_recovered);
        yNewDeadLabel = v.findViewById(R.id.yesterday_new_dead);
        yNewInfectedLabel = v.findViewById(R.id.yesterday_new_infected);
        yNewRecoveredLabel = v.findViewById(R.id.yesterday_new_recovered);
        appBar = v.findViewById(R.id.topAppBar);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
        tamponiChart = v.findViewById(R.id.today_tamponi_chart);
        variazioneLabel=v.findViewById(R.id.variazione_infected_today);
    }

    private void update(Object obj){
        swipeRefreshLayout.setRefreshing(false);
        enableNavigation();
        if(obj==null)return;
        List<NationalDayData> data = (List<NationalDayData>)obj;
        NationalDayData today = data.get(data.size()-1);
        NationalDayData yesterday = data.get(data.size()-2);

        String p = "+#;-#";
        NumberFormat plusMinusNF = NumberFormat.getInstance(Locale.ITALY);
        DecimalFormat plusMinusDF = (DecimalFormat)plusMinusNF;

        appBar.setTitle("Aggiornamento "+today.getDateCompleteString());
        infectedLabel.setText(plusMinusDF.format(today.getPositivi()));
        recoveredLabel.setText(plusMinusDF.format(today.getGuariti()));
        deadLabel.setText(plusMinusDF.format(today.getDeceduti()));

        plusMinusDF.applyPattern(p);
        variazioneLabel.setText(plusMinusNF.format(today.getVariazionePositivi()));

        newInfectedLabel.setText(plusMinusNF.format(today.getNuoviPositivi()));
        newRecoveredLabel.setText(String.valueOf((today.getGuariti()-yesterday.getGuariti())));
        newDeadLabel.setText(String.valueOf((today.getDeceduti()-yesterday.getDeceduti())));

        NationalDayData otherDay = data.get(data.size()-3);
        yNewInfectedLabel.setText(plusMinusNF.format(yesterday.getNuoviPositivi()));
        yNewRecoveredLabel.setText(String.valueOf((yesterday.getGuariti()-otherDay.getGuariti())));
        yNewDeadLabel.setText(String.valueOf((yesterday.getDeceduti()-otherDay.getDeceduti())));

        int tamponi = today.getTamponi()-yesterday.getTamponi();
        int tamponiPositivi = today.getNuoviPositivi();
        int tamponiNegativi = tamponi - tamponiPositivi;

        double positivePercentage = tamponiPositivi*100/(double) tamponi;
        double negativePercentage = tamponiNegativi*100/(double) tamponi;
        positivePercentage = Utils.round(positivePercentage, 2);
        negativePercentage = Utils.round(negativePercentage, 2);


        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(tamponiPositivi, "Positivi ("+positivePercentage+"%)"));
        pieEntries.add(new PieEntry(tamponiNegativi, "Negativi ("+negativePercentage+"%)"));

        ChartUtils.createPieChart(
                tamponiChart,
                pieEntries,
                transparentColor(requireContext()),
                false,
                redColor(requireContext()),
                color(requireContext(), R.color.gray)
        );
    }

    private void updateData(boolean forceUpdate){
        NationalData.getData(forceUpdate, new Callback() {
            @Override
            public void onSuccess(final Object obj) {
                if(!forceUpdate) update(obj);
                else
                    RegionData.forceUpdate(new Callback() {
                        @Override
                        public void onSuccess(Object obj2) {
                            update(obj);
                        }

                        @Override
                        public void onError(Error error) {
                            swipeRefreshLayout.setRefreshing(false);
                            enableNavigation();
                        }
                    });
            }

            @Override
            public void onError(Error error) {
                swipeRefreshLayout.setRefreshing(false);
                enableNavigation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e("R", "RESUMED");

        entries++;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        setupLabels(v);

        c=getContext();

        updateData(false);

        swipeRefreshLayout.setOnRefreshListener(() -> { disableNavigation(); updateData(true);});
        return v;
    }
}
