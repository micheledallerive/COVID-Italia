package it.micheledallerive.covid_italia.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.micheledallerive.covid_italia.fragments.ChartFragment2;

public class DateUtils {

    private static final SimpleDateFormat dateToStringFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY);
    public static ArrayList<String> dates=null;

    public static Date parseDate(String date){
        try{
            if(date.contains("T")){
                date=date.replace("T", " ");
                return dateToStringFormatter.parse(date);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String calculateDate(float v){
        if(!ChartFragment2.region.isEmpty()) v=(int)(v);
        if(dates==null) return null;
        if(dates.size()>((int)v)) return dates.get((int)v);
        int daysDiff = ((int)v)-dates.size()+1;
        SimpleDateFormat f = new SimpleDateFormat("dd-MM", Locale.ITALY);
        Date d;
        try{
            d=f.parse(dates.get(dates.size()-1));
        }catch(Exception e){e.printStackTrace(); d=new Date();}
        assert d != null;
        Date date = new Date(d.getTime()+ 86400000L *daysDiff);
        return f.format(date);
    }

}
