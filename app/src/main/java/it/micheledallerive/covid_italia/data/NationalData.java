package it.micheledallerive.covid_italia.data;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.dataContainers.NationalDayData;
import it.micheledallerive.covid_italia.utils.Constants;

public class NationalData {

    private static List<NationalDayData> data=null;

    public static void forceUpdate(){
        parseData(true, new Callback() {
            @Override
            public void onSuccess(Object obj) {}
            @Override
            public void onError(Error error) {}
        });
    }

    public static void getData(boolean forceUpdate, Callback callback){
        if(data==null||data.isEmpty()||forceUpdate){
            parseData(forceUpdate,callback);
        }else{
            callback.onSuccess(data);}
    }

    private static void parseData(boolean forceUpdate, Callback callback){
        AsyncTask<Void, Void, Boolean> jsonParse = new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... args) {
                boolean dNull = DateUtils.dates==null;
                if(dNull) DateUtils.dates = new ArrayList<>();
                try{
                    final String json = DataUtils.getJSON(Constants.URLNazionale);
                    if(data==null||forceUpdate) data=new ArrayList<>();
                    assert json != null;
                    JsonArray array = JsonParser.parseString(json).getAsJsonArray();
                    for(int i=0;i<array.size();i++){
                        JsonObject object = array.get(i).getAsJsonObject();
                        NationalDayData ndc = new NationalDayData(object);
                        if(dNull){
                            String dateString = ndc.getDateString();
                            DateUtils.dates.add(dateString);
                        }
                        data.add(ndc);
                    }
                    return true;
                }catch(Exception e){
                    e.printStackTrace();
                }
                return false;
            }


            @Override
            protected void onPostExecute(Boolean object){
                if(!object) {
                    callback.onError(new Error("Errore di connessione", "È stato impossibile ottenere i dati, riprova piu tardi"));
                    return;
                }
                //Log.e("DATA SIZE", data.size()+"");
                super.onPostExecute(object);
                callback.onSuccess(data);
            }
        };
        jsonParse.execute();

    }


}
