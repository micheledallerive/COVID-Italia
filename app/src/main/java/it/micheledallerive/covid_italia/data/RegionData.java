package it.micheledallerive.covid_italia.data;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.maps.Geoposition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.dataContainers.RegionDayData;
import it.micheledallerive.covid_italia.utils.Constants;

public class RegionData {

    public static Map<String, List<Geoposition>> regioniCoordinate = new HashMap<>();
    private static List<RegionDayData> data = null;

    private static void clear() {
        if (data != null)
            data = null;
    }

    public static void forceUpdate() {
        clear();
        parseData(true, new Callback() {
            @Override
            public void onSuccess(Object obj) {
            }

            @Override
            public void onError(Error error) {
            }
        });
    }

    public static void forceUpdate(Callback callback) {
        clear();
        parseData(true, new Callback() {
            @Override
            public void onSuccess(Object obj) {
                callback.onSuccess(obj);
            }

            @Override
            public void onError(Error error) {
                callback.onError(error);
            }
        });
    }

    public static void getData(boolean forceUpdate, Callback callback) {
        if (data == null || data.isEmpty() || forceUpdate) {
            parseData(forceUpdate, callback);
        } else {
            callback.onSuccess(data);
        }
    }

    private static String parseRegionName(String region) {
        if (region.equalsIgnoreCase("bolzano"))
            region = "P.A. Bolzano";
        if (region.equalsIgnoreCase("trento"))
            region = "P.A. Trento";
        if (region.equalsIgnoreCase("emilia romagna"))
            region = "Emilia-Romagna";
        return region;
    }

    private static void parseData(boolean forceUpdate, Callback callback) {

        AsyncTask<Void, Void, Boolean> jsonParse = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... args) {
                //Log.e("Update", "doInBackground2 started");
                try {
                    if (data == null || forceUpdate) data = new ArrayList<>();
                    final String json = DataUtils.getJSON(Constants.URLRegionale);
                    assert json != null;
                    JsonArray array = JsonParser.parseString(json).getAsJsonArray();

                    final String coordinatesJson = DataUtils.getJSON(Constants.URLCoordinateRegioni);
                    assert coordinatesJson != null;
                    JsonArray cArray = JsonParser.parseString(coordinatesJson).getAsJsonObject().get("features").getAsJsonArray();

                    for (int i = 0; i < cArray.size(); i++) {
                        JsonObject obj = cArray.get(i).getAsJsonObject();
                        String regionCode = obj.get("properties").getAsJsonObject().get("reg_istat_code").getAsString();

                        boolean isMultiPolygon = obj.get("geometry").getAsJsonObject().get("type").getAsString().equals("MultiPolygon");
                        
                        // geojson structure:
                        // "coordinates": [
                        //      [polygon and holes],
                        //      [polygon and holes],
                        //      ...
                        // ]

                        // each polygon and holes is
                        // [
                        //      [polygon],
                        //      [hole],
                        //      [hole],
                        //      ...
                        // ]

                        JsonArray coords_array = obj.get("geometry").getAsJsonObject()
                                .get("coordinates").getAsJsonArray();
                        List<Geoposition> geoPoints = new ArrayList<>();
                        
                        for (int j = 0; j < 1; j++) {
                            JsonArray polygon_coords = coords_array.get(j).getAsJsonArray();
                            if (isMultiPolygon) {
                                polygon_coords = polygon_coords.get(0).getAsJsonArray();
                            }
                            
                            for (int k = 0; k < polygon_coords.size(); k++) {
                                JsonArray coords = polygon_coords.get(k).getAsJsonArray();
                                geoPoints.add(new Geoposition(coords.get(1).getAsFloat(), coords.get(0).getAsFloat()));
                            }
                        }

                        regioniCoordinate.put(regionCode, geoPoints);
                    }

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject object = array.get(i).getAsJsonObject();
                        RegionDayData rdc = new RegionDayData(object);
                        // find a way to merge the province autonome
                        data.add(rdc);
                    }

                    sortByRegionName();

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            protected void onPostExecute(Boolean object) {
                if (!object) {
                    callback.onError(new Error("Errore di connessione", "È stato impossibile ottenere i dati, riprova piu tardi"));
                    return;
                }
                super.onPostExecute(object);
                callback.onSuccess(data);
            }
        };
        jsonParse.execute();

    }

    private static void sortByRegionName() {
        List<RegionDayData> finalList = new ArrayList<>();
        List<RegionDayData> dayList;
        for (int i = 0; i < data.size() / 21; i++) {
            int start = (i * 21);
            int end = ((i + 1) * 21);
            dayList = data.subList(start, end);
            dayList.sort(Comparator.comparing(RegionDayData::getRegionName));
            finalList.addAll(dayList);
        }
        data.clear();
        data.addAll(finalList);
    }


    /*
    private static void getRegionsCoordinates(Callback callback){
        AsyncTask<Void, Void, Boolean> jsonParse = new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids) {
                try{

                    return true;
                }catch(Exception e){
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean object) {
                if(!object) {
                    callback.onError(new Error("Errore di connessione", "È stato impossibile ottenere i dati, riprova piu tardi"));
                    return;
                }
                super.onPostExecute(object);
                callback.onSuccess(data);
            }
        };
        jsonParse.execute();
    }*/

}
