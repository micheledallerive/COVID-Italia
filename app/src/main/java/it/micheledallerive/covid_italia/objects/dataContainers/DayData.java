package it.micheledallerive.covid_italia.objects.dataContainers;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.micheledallerive.covid_italia.data.DateUtils;

public class DayData {

    private Date date;
    private int ricoverati;
    private int terapiaIntensiva;
    private int totaleOspedalizzati;
    private int isolamentoDomiciliare;
    private int positivi;
    private int nuoviPositivi;
    private int guariti;
    private int deceduti;
    private int totale;
    private int tamponi;
    private int variazionePositivi;

    public DayData(Date date, int ricoverati, int terapiaIntensiva, int totaleOspedalizzati, int isolamentoDomiciliare, int positivi, int nuoviPositivi, int guariti, int deceduti, int totale, int tamponi) {
        this.date = date;
        this.ricoverati = ricoverati;
        this.terapiaIntensiva = terapiaIntensiva;
        this.totaleOspedalizzati = totaleOspedalizzati;
        this.isolamentoDomiciliare = isolamentoDomiciliare;
        this.positivi = positivi;
        this.nuoviPositivi = nuoviPositivi;
        this.guariti = guariti;
        this.deceduti = deceduti;
        this.totale = totale;
        this.tamponi = tamponi;
    }

    public DayData(JsonObject o){
        this.date = DateUtils.parseDate(o.get("data").getAsString());
        this.ricoverati = o.get("ricoverati_con_sintomi").getAsInt();
        this.terapiaIntensiva = o.get("terapia_intensiva").getAsInt();
        this.totaleOspedalizzati = o.get("totale_ospedalizzati").getAsInt();
        this.isolamentoDomiciliare = o.get("isolamento_domiciliare").getAsInt();
        this.positivi = o.get("totale_positivi").getAsInt();
        this.variazionePositivi = o.get("variazione_totale_positivi").getAsInt();
        this.nuoviPositivi = o.get("nuovi_positivi").getAsInt();
        this.guariti = o.get("dimessi_guariti").getAsInt();
        this.deceduti = o.get("deceduti").getAsInt();
        this.totale = o.get("totale_casi").getAsInt();
        this.tamponi = o.get("tamponi").getAsInt();
    }

    public DayData(){}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRicoverati() {
        return ricoverati;
    }

    public void setRicoverati(int ricoverati) {
        this.ricoverati = ricoverati;
    }

    public int getTerapiaIntensiva() {
        return terapiaIntensiva;
    }

    public void setTerapiaIntensiva(int terapiaIntensiva) {
        this.terapiaIntensiva = terapiaIntensiva;
    }

    public int getTotaleOspedalizzati() {
        return totaleOspedalizzati;
    }

    public void setTotaleOspedalizzati(int totaleOspedalizzati) {
        this.totaleOspedalizzati = totaleOspedalizzati;
    }

    public int getIsolamentoDomiciliare() {
        return isolamentoDomiciliare;
    }

    public void setIsolamentoDomiciliare(int isolamentoDomiciliare) {
        this.isolamentoDomiciliare = isolamentoDomiciliare;
    }

    public int getPositivi() {
        return positivi;
    }

    public void setPositivi(int positivi) {
        this.positivi = positivi;
    }

    public int getNuoviPositivi() {
        return nuoviPositivi;
    }

    public void setNuoviPositivi(int nuoviPositivi) {
        this.nuoviPositivi = nuoviPositivi;
    }

    public int getGuariti() {
        return guariti;
    }

    public void setGuariti(int guariti) {
        this.guariti = guariti;
    }

    public int getDeceduti() {
        return deceduti;
    }

    public void setDeceduti(int deceduti) {
        this.deceduti = deceduti;
    }

    public int getTotale() {
        return totale;
    }

    public void setTotale(int totale) {
        this.totale = totale;
    }

    public int getTamponi() {
        return tamponi;
    }

    public void setTamponi(int tamponi) {
        this.tamponi = tamponi;
    }

    public String getDateString(){
        Date d = date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM", Locale.ITALY);
        return formatter.format(d);
    }

    public String getDateCompleteString(){
        Date d = date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
        return formatter.format(d);
    }

    public int getVariazionePositivi() {
        return variazionePositivi;
    }

    public void setVariazionePositivi(int variazionePositivi) {
        this.variazionePositivi = variazionePositivi;
    }

}
