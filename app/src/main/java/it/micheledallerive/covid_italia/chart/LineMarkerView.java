package it.micheledallerive.covid_italia.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.data.DateUtils;
import it.micheledallerive.covid_italia.fragments.chartTabs.DeadTab;
import it.micheledallerive.covid_italia.fragments.chartTabs.InfectedTab;
import it.micheledallerive.covid_italia.fragments.chartTabs.RecoveredTab;

public class LineMarkerView extends MarkerView {

    private final TextView tvContent;
    private final TextView tvDateContent;
    private final ConstraintLayout tvBackground;
    private final ConstraintLayout markerPointer;
    private final ConstraintLayout markerContentContainer;
    private final int line;
    private final String suffix;
    private final int xMax;
    boolean isPercentage = false;
    private boolean isBottom=false;
    private TextView tvValue2;
    private TextView tvValue3;
    @SuppressLint("SetTextI18n")
    public LineMarkerView(Context context, int layoutResource, int line, int xMax, String suffix, String lineName) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvValue);
        tvDateContent = findViewById(R.id.tvDateValue);
        tvBackground = findViewById(R.id.tvBackground);
        TextView tvField = findViewById(R.id.tvField);
        tvField.setText(lineName + ":");
        markerPointer = findViewById(R.id.marker_pointer);
        markerContentContainer=findViewById(R.id.marker_content_container);
        if(findViewById(R.id.tvField2)!=null){
            isPercentage=true;
            tvValue2=findViewById(R.id.tvValue2);
            tvValue3=findViewById(R.id.tvValue3);
        }
        // line 0: infetti 1: guariti 2: morti
        this.line = line;
        this.xMax = xMax;
        this.suffix = suffix;
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static int dpToPixels(final Context c, final float dps) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        isBottom=(highlight.getDataSetIndex()==2);
        if(isBottom){
            tvBackground.setPadding(0,dpToPixels(getContext(), 8),0,0);
            //tvBackground.setBackgroundResource(R.drawable.rotated_marker);
        }  //tvBackground.setBackgroundResource(R.drawable.marker);

        int pos = (int)((markerContentContainer.getWidth()*e.getX())/(float)xMax);
        int lm = pos-dpToPixels(getContext(), 20);
        setMargins(markerPointer, lm,0,0,0);
        switch(line){
            case 0: // infetti
                if(isPercentage) {
                    tvValue2.setText(String.valueOf((int)InfectedTab.barChart.getBarData().getEntryForHighlight(highlight).getY()));
                    tvValue3.setText(String.valueOf((int)InfectedTab.barChart.getBarData().getEntryForHighlight(new Highlight(highlight.getX()-1, highlight.getY(), 0)).getY()));
                }
                markerContentContainer.setBackgroundColor(getResources().getColor(R.color.red, getContext().getTheme()));
                markerPointer.setBackgroundTintList(getContext().getColorStateList(R.color.red));
                break;
            case 1: // guariti
                if(isPercentage) {
                    tvValue2.setText(String.valueOf((int)RecoveredTab.barChart.getBarData().getEntryForHighlight(highlight).getY()));
                    tvValue3.setText(String.valueOf((int)RecoveredTab.barChart.getBarData().getEntryForHighlight(new Highlight(highlight.getX()-1, highlight.getY(), 0)).getY()));
                }
                markerContentContainer.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
                markerPointer.setBackgroundTintList(getContext().getColorStateList(R.color.green));
                break;
            case 2: // morti
                if(isPercentage) {
                    tvValue2.setText(String.valueOf((int)DeadTab.newDeadBar.getBarData().getEntryForHighlight(highlight).getY()));
                    tvValue3.setText(String.valueOf((int)DeadTab.newDeadBar.getBarData().getEntryForHighlight(new Highlight(highlight.getX() - 1, highlight.getY(), 0)).getY()));
                }
                markerContentContainer.setBackgroundColor(getResources().getColor(R.color.black, getContext().getTheme()));
                markerPointer.setBackgroundTintList(getContext().getColorStateList(R.color.black));
                break;
            case 3: // tamponi
                markerContentContainer.setBackgroundColor(getResources().getColor(R.color.gray, getContext().getTheme()));
                markerPointer.setBackgroundTintList(getContext().getColorStateList(R.color.gray));
                break;
        }
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 2, true));
        } else {

            String s = Utils.formatNumber(e.getY(), 2, true);
            if(e.getY()==Math.floor(e.getY()))
                s=String.valueOf((int)e.getY());
            String result = s + suffix;
            tvContent.setText(result);
        }

        try{tvDateContent.setText(DateUtils.dates.get((int)e.getX()));}catch(Exception ex){ex.printStackTrace();}

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        tvBackground.setPadding(0,0,0,0);
        int width = markerPointer.getLeft()+markerPointer.getWidth()/2;
        return isBottom?new MPPointF(-(width), 0):new MPPointF(-(width), -getHeight());
    }


}
