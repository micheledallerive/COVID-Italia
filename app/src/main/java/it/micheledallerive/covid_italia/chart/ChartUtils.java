package it.micheledallerive.covid_italia.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.data.DateUtils;
import it.micheledallerive.covid_italia.utils.LockableScrollView;
import it.micheledallerive.covid_italia.utils.Utils;

public class ChartUtils {

    static List<Chart> charts = new ArrayList<>();

    public static LineDataSet createDefaultDataSet(ArrayList<Entry> values, int color, String name){
        LineDataSet set1 = new LineDataSet(values, name);
        set1.setColor(color);
        set1.setDrawFilled(false);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setDrawIcons(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setLineWidth(3f);
        set1.setFormLineWidth(2f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{100f,5f},0f));
        set1.setFormSize(10.f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setDrawHorizontalHighlightIndicator(false);
        return set1;
    }

    // line 0: infetti 1: guariti 2: morti
    @SuppressLint("ClickableViewAccessibility")
    public static LineChart setupLineChart(LineChart lc, LineData data, int line, String markerSuffix, String lineName, LockableScrollView scrollView, int markerLayout){
        lc.setScaleEnabled(false);
        lc.setTouchEnabled(true);
        Description desc = new Description();
        desc.setText("");
        lc.setFocusable(true);
        lc.setDescription(desc);
        lc.setPinchZoom(false);
        lc.setDoubleTapToZoomEnabled(false);
        lc.getRendererXAxis();
        lc.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lc.getXAxis().setGranularity(5f);
        lc.getAxisRight().setEnabled(false);
        lc.animateXY(1000,1000);
        lc.setExtraOffsets(15f, 20f, 15f, 15f);
        Legend l = lc.getLegend();
        l.setEnabled(false);

        LineMarkerView mv = new LineMarkerView(lc.getContext(), markerLayout, line, (int)data.getXMax(), markerSuffix, lineName);
        lc.setMarkerView(mv);

        lc.setOnTouchListener((v, event) -> {
            v.performClick();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                lc.highlightValue(null);
            }
            return false;
        });
        lc.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                lc.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        final GestureDetector gestureDetector = new GestureDetector(lc.getContext(), new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                scrollView.setScrollingEnabled(false);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scrollView.setScrollingEnabled(true);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        lc.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                gestureDetector.onTouchEvent(me);
            }
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                scrollView.setScrollingEnabled(true);
            }
            @Override
            public void onChartLongPressed(MotionEvent me) {}
            @Override
            public void onChartDoubleTapped(MotionEvent me) {}
            @Override
            public void onChartSingleTapped(MotionEvent me) {}
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}
            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {}
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {}
        });
        charts.add(lc);
        scrollView.setOnTouchListener((v, event) -> {
            v.performClick();
            try{
                for(Chart linec : charts){
                    linec.highlightValue(null);
                }
            }catch(Exception e){e.printStackTrace();}
            scrollView.setScrollingEnabled(true);
            return false;
        });
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            lc.highlightValue(null);
            scrollView.setScrollingEnabled(true);
        });
        return setupChartOffset(lc, data);
    }

    private static LineChart setupChartOffset(LineChart lineChart, LineData data){
        float xOffset = data.getXMax()/25f;
        float yMaxOffset = data.getYMax()/2.9f;
        float yMinOffset = data.getYMax()/10f;
        lineChart.getXAxis().setAxisMinimum(data.getXMin() - xOffset);
        lineChart.getXAxis().setAxisMaximum(data.getXMax() + xOffset);
        lineChart.getAxisLeft().setAxisMinimum(data.getYMin()-yMinOffset);
        lineChart.getAxisLeft().setAxisMaximum(data.getYMax()+yMaxOffset);
        return lineChart;
    }

    public static LineData setupLineData(LineDataSet... dataSets){
        ArrayList<ILineDataSet> ds = new ArrayList<>();
        Collections.addAll(ds, dataSets);
        return new LineData(ds);
    }

    public static PieChart createPieChart(PieChart pieChart, List<PieEntry> entries, int holeColor, boolean isPercentage, int... colors){
        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData data = new PieData(dataSet);
        if (isPercentage) {
            data.setValueFormatter(new PercentFormatter(pieChart));
            pieChart.setUsePercentValues(true);
        }
        data.setValueTextSize(16f);
        //dataSet.setHighlightEnabled(false);
        List<Integer> c = new ArrayList<>();
        for(int color : colors)
            c.add(Utils.getTransparentColor(color, .65));
        dataSet.setColors(c);

        pieChart.setTouchEnabled(false);
        pieChart.setData(data);
        pieChart.getDescription().setText("");

        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(40);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setText("");

        pieChart.setHoleColor(holeColor);
        pieChart.setTransparentCircleAlpha(40);
        pieChart.animateXY(1000,1000);
        pieChart.setEntryLabelColor(Color.BLACK);

        pieChart.setElevation(24f);
        return pieChart;
    }

    public static void createPercentageLineChart(Context c, LineChart chart, ArrayList<Entry> values, LockableScrollView scrollView, String label, int color, int line) {
        LineDataSet infectedSet = createDefaultDataSet(values, c.getResources().getColor(color, c.getTheme()), "Infetti");
        LineData data = setupLineData(infectedSet);
        chart = setupLineChart(chart, data, line, "%", "% nuovi "+label, scrollView, R.layout.percentage_marker_view);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DateUtils.calculateDate(value);
            }
        });
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value)+"%";
            }
        });
        chart.setData(data);
    }

    public static void createTimeLineChart(Context c, LineChart chart, ArrayList<Entry> values, LockableScrollView scrollView, String label, int color, int line){
        LineDataSet infectedSet = ChartUtils.createDefaultDataSet(values, c.getResources().getColor(color, c.getTheme()), label);
        LineData data = ChartUtils.setupLineData(infectedSet);
        chart = ChartUtils.setupLineChart(chart, data, line, "", label, scrollView, R.layout.marker_view);
        chart.setData(data);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DateUtils.calculateDate(value);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void createBarChart(Context c, BarChart chart, List<BarEntry> entries, LockableScrollView scrollView, int color, int line, String suffix){
        chart.animateXY(1000, 1000);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DateUtils.calculateDate(value);
            }
        });
        chart.setScaleYEnabled(false);
        chart.setExtraOffsets(15f, 20f, 15f, 15f);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                chart.highlightValue(null);
            }
            return false;
        });
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                chart.highlightValue(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        final GestureDetector gestureDetector = new GestureDetector(chart.getContext(), new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                scrollView.setScrollingEnabled(false);
            }
        });
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                gestureDetector.onTouchEvent(me);
            }
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                scrollView.setScrollingEnabled(true);
            }
            @Override
            public void onChartLongPressed(MotionEvent me) {}
            @Override
            public void onChartDoubleTapped(MotionEvent me) {}
            @Override
            public void onChartSingleTapped(MotionEvent me) {
                scrollView.setScrollingEnabled(true);}
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}
            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {}
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {}
        });
        BarDataSet set1 = new BarDataSet(entries, "");
        set1.setColor(c.getColor(color));


        BarData data = new BarData(set1);
        chart.setData(data);

        LineMarkerView mv = new LineMarkerView(c, R.layout.marker_view, line, (int)chart.getBarData().getXMax(), "", "Nuovi "+suffix);
        chart.setMarkerView(mv);


        float xOffset = chart.getBarData().getXMax()/25f;
        float yMaxOffset = chart.getBarData().getYMax()/3.3f;
        float yMinOffset = chart.getBarData().getYMin()<0?chart.getBarData().getYMin()/10f:0f;
        chart.getXAxis().setAxisMinimum(chart.getBarData().getXMin() - xOffset);
        chart.getXAxis().setAxisMaximum(chart.getBarData().getXMax() + xOffset);
        chart.getAxisLeft().setAxisMinimum(chart.getBarData().getYMin()+yMinOffset);
        chart.getAxisLeft().setAxisMaximum(chart.getBarData().getYMax()+yMaxOffset);

        charts.add(chart);
    }

    public static void setupClicks(LockableScrollView scrollView, Chart... charts) {
        for(Chart chart : charts){
            chart.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    scrollView.setScrollingEnabled(false);
                    for(Chart c : charts)
                        if(c!=chart)
                            c.highlightValue(null);
                }

                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    scrollView.setScrollingEnabled(true);
                }
                @Override
                public void onChartLongPressed(MotionEvent me) {}
                @Override
                public void onChartDoubleTapped(MotionEvent me) {}
                @Override
                public void onChartSingleTapped(MotionEvent me) {}
                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}
                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {}
                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {}
            });
        }
    }

}
