package it.micheledallerive.covid_italia.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import it.micheledallerive.covid_italia.R;

public class Constants {

     public static final boolean isAdEnabled = false;

     public static final int NEWS_MAX_DAYS = 5;

     public static final String MAPS_KEY = "AjihgTc5tha5b3l-RWllFLoLKeZLOKu8l-bxPB27cg0DkBw-TCifF0oFSkws4JcI";
     public static final String URLNazionale = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json";
     public static final String URLRegionale = "https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni.json";
     public static final String URLCoordinateRegioni = "https://micheledallerive.ch/files/regions.json";

     public static final String corriere = "http://static.video.corriereobjects.it/widget/content/playlist/xml/playlist_a3378fac-7de4-11df-a575-00144f02aabe_dateDesc.xml";
     public static final String sole24ore = "https://www.ilsole24ore.com/rss/salute.xml";
     public static final String repubblica = "https://www.repubblica.it/rss/cronaca/rss2.0.xml";
     public static final String ilfattoquotidiano = "https://www.ilfattoquotidiano.it/feed/";
     public static final String lastampa = "http://feed.lastampa.it/salute.rss";
     static final String ansa = "https://www.ansa.it/sito/notizie/cronaca/cronaca_rss.xml";

     public static final String[] newspapers = {
             corriere,
             sole24ore,
             ilfattoquotidiano,
             ansa,
             lastampa,
             repubblica
     };
     public static Map<String, Integer> popolazioni = new HashMap<String, Integer>(){
          {
               put("Abruzzo", 1311580);
               put("Basilicata", 562869);
               put("Calabria", 1947131);
               put("Campania", 5801692);
               put("Emilia-Romagna", 4459477);
               put("Friuli Venezia Giulia", 1215220);
               put("Lazio", 5879082);
               put("Liguria", 1550640);
               put("Lombardia", 10060574);
               put("Marche", 1525271);
               put("Molise", 305617);
               put("Piemonte", 4356406);
               put("Puglia", 4029053);
               put("Sardegna", 1639591);
               put("Sicilia", 4999891);
               put("Toscana", 3729641);
               put("Umbria", 882015);
               put("Valle d'Aosta", 125666);
               put("Veneto", 4905854);
               put("P.A. Bolzano", 520891);
               put("P.A. Trento", 538223);
          }
     };
     public static String[] CONSIGLI = new String[]{
             "Pulisci regolarmente e accuratamente le mani con una soluzione a base alcolica o lavale con acqua e sapone.",
             "Mantieni almeno 1 metro (3 piedi) di distanza tra te e gli altri.",
             "Evita di andare in luoghi affollati.",
             "Evita di toccarti occhi, naso e bocca.",
             "Assicurati che tu e le persone intorno a te seguiate una buona igiene respiratoria, come coprire la bocca e il naso con il gomito o il fazzoletto quando si tossisce o starnutisce",
             "Rimani a casa e autoisolati anche con sintomi minori come tosse, mal di testa, febbre lieve, fino al recupero e chiedi a qualcuno di portarti delle provviste.",
             "In caso di febbre, tosse e difficoltà respiratorie, consulta un medico, ma se possibile telefona in anticipo e segui le indicazioni dell'autorità sanitaria locale.",
             "Tieniti aggiornato sulle ultime informazioni da fonti attendibili, come l'OMS o le autorità sanitarie locali e nazionali."
     };

     public static int transparentColor(Context c){return c.getResources().getColor(android.R.color.transparent, c.getTheme());}

     public static int redColor(Context c){return c.getResources().getColor(R.color.red, c.getTheme());}

     public static int color(Context c, int id){return c.getResources().getColor(id, c.getTheme());}

     public static class Preferences{
          public static final String SEND_NOTIFICATIONS="send_notifications";
     }

     /*
 Perché? Quando le persone si incontrano in mezzo alla folla, è più probabile che tu entri in stretto contatto con qualcuno che ha COIVD-19 ed è più difficile mantenere una distanza fisica di 1 metro (3 piedi).
 Perché? Le mani toccano molte superfici e possono raccogliere virus. Una volta contaminate, le mani possono trasferire il virus a occhi, naso o bocca. Da lì, il virus può entrare nel tuo corpo e infettarti.
A Ciò significa coprire la bocca e il naso con il gomito o il tessuto piegato quando si tossisce o starnutisce. Quindi smaltire immediatamente il tessuto usato e lavarsi le mani. Perché? Le goccioline diffondono il virus. Seguendo una buona igiene respiratoria, proteggi le persone intorno a te da virus come raffreddore, influenza e COVID-19.
 Se devi uscire di casa, indossa una maschera per evitare di infettare gli altri. Perché? Evitare il contatto con altri li proteggerà da possibili COVID-19 e altri virus.
 Perché? Le autorità nazionali e locali disporranno delle informazioni più aggiornate sulla situazione nella tua zona. Chiamare in anticipo consentirà al tuo fornitore di assistenza sanitaria di indirizzarti rapidamente alla giusta struttura sanitaria. Questo ti proteggerà e ti aiuterà a prevenire la diffusione di virus e altre infezioni.
 Perché? Le autorità locali e nazionali sono nella posizione migliore per consigliare su ciò che le persone nella vostra zona dovrebbero fare per proteggersi.
*/

}
