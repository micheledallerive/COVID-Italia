package it.micheledallerive.covid_italia.objects.dataContainers;

import com.google.gson.JsonObject;
import com.microsoft.maps.Geopoint;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")

/*

{
        "data": "2020-02-24T18:00:00",
        "stato": "ITA",
        "codice_regione": 13,
        "denominazione_regione": "Abruzzo",
        "lat": 42.35122196,
        "long": 13.39843823,
        "ricoverati_con_sintomi": 0,
        "terapia_intensiva": 0,
        "totale_ospedalizzati": 0,
        "isolamento_domiciliare": 0,
        "totale_positivi": 0,
        "variazione_totale_positivi": 0,
        "nuovi_positivi": 0,
        "dimessi_guariti": 0,
        "deceduti": 0,
        "totale_casi": 0,
        "tamponi": 5,
        "casi_testati": null,
        "note_it": "",
        "note_en": ""
    }

 */

public class RegionDayData extends DayData {

    private String regionName;
    private float lat;
    private float lng;
    
    private String regionCode;

    private List<Geopoint> coordinates;

    public RegionDayData(Date date, String regionName, float lat, float lng, int ricoverati, int terapiaIntensiva, int totaleOspedalizzati, int isolamentoDomiciliare, int positivi, int nuoviPositivi, int variazionePositivi, int guariti, int deceduti, int totale, int tamponi) {
        super(date,ricoverati,terapiaIntensiva,totaleOspedalizzati,isolamentoDomiciliare,positivi,nuoviPositivi,guariti,deceduti,totale,tamponi);
        this.regionName = regionName;
        this.lat = lat;
        this.lng = lng;
    }

    public RegionDayData(JsonObject o){
        super(o);
        this.regionCode = o.get("codice_regione").getAsString();
        if (this.regionCode.length() == 1) {
            this.regionCode = "0" + this.regionCode;
        }
        this.regionName = o.get("denominazione_regione").getAsString();
        this.lat = o.get("lat").getAsFloat();
        this.lng = o.get("long").getAsFloat();
    }

    public RegionDayData(String regionName) {
        super();
        this.regionName = regionName;
    }

    public List<Geopoint> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Geopoint> coordinates) {
        this.coordinates = coordinates;
    }

    public void addCoordinate(Geopoint geoPoint){
        this.coordinates.add(geoPoint);
    }

    public void removeCoordinate(){
        this.coordinates=null;
        System.gc();
    }
    
    public String getRegionCode() {
        return this.regionCode;
    }
    
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

}
