package it.micheledallerive.covid_italia.objects.dataContainers;

import com.google.gson.JsonObject;

import java.util.Date;

@SuppressWarnings("unused")

/*

{
        "data": "2020-02-24T18:00:00",
        "stato": "ITA",
        "ricoverati_con_sintomi": 101,
        "terapia_intensiva": 26,
        "totale_ospedalizzati": 127,
        "isolamento_domiciliare": 94,
        "totale_positivi": 221,
        "variazione_totale_positivi": 0,
        "nuovi_positivi": 221,
        "dimessi_guariti": 1,
        "deceduti": 7,
        "totale_casi": 229,
        "tamponi": 4324,
        "casi_testati": null,
        "note_it": "",
        "note_en": ""
    },

 */

public class NationalDayData extends DayData {

    public NationalDayData(Date date, int ricoverati, int terapiaIntensiva, int totaleOspedalizzati, int isolamentoDomiciliare, int positivi, int nuoviPositivi, int guariti, int deceduti, int totale, int tamponi) {
        super(date,ricoverati,terapiaIntensiva,totaleOspedalizzati,isolamentoDomiciliare,positivi,nuoviPositivi,guariti,deceduti,totale,tamponi);
    }

    public NationalDayData(JsonObject o){
        super(o);
    }

    public NationalDayData(){
        super();
    }

}
