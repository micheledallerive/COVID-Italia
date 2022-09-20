package it.micheledallerive.covid_italia.data;

import static it.micheledallerive.covid_italia.data.DataUtils.getInputStream;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.Feed;
import it.micheledallerive.covid_italia.utils.Constants;
import it.micheledallerive.covid_italia.utils.Utils;

public class NewsData {

    private static List<Feed> data=null;

    public static void getData(boolean forceUpdate, Callback callback){
        if(data==null||data.isEmpty()||forceUpdate) parseData(callback);
        else callback.onSuccess(data);
    }

    private static String removeDescriptionTags(String description){
        description= description.replaceAll("<(/?[^>]+)>", "\\ ").replaceAll("\\s+", " ").trim();
        int descriptionMaxLength=300;
        if(description.length()>descriptionMaxLength){
            for(int i=description.length();i>=0;i--){
                if(i<descriptionMaxLength){
                    if(Character.isSpaceChar(description.charAt(i))){
                        description = description.substring(0, i)+"...";
                        break;
                    }
                }
            }
        }
        description= Utils.unescapeHtml3(description);
        return description;
    }

    private static void parseData(Callback callback){
        AsyncTask<String, Void, List<Feed>> newsParse = new AsyncTask<String, Void, List<Feed>>(){

            @Override
            protected List<Feed> doInBackground(String... URLs) {
                try {
                    List<String> categories = new ArrayList<>();
                    List<Feed> feeds = new ArrayList<>();
                    Feed f = null;
                    boolean toInsert=false;
                    boolean toInsertDays=false;
                    String[] urls = Constants.newspapers;
                    for(String u : urls){
                        URL url = new URL(u);
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(false);
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(getInputStream(url), "UTF_8");
                        int eventType = xpp.getEventType();
                        boolean insideItem = false;
                        String newspaper="";
                        while (eventType != XmlPullParser.END_DOCUMENT)
                        {
                            if (eventType == XmlPullParser.START_TAG)
                            {
                                //if the tag is called "item"
                                if (xpp.getName().equalsIgnoreCase("item"))
                                {
                                    categories.clear();
                                    insideItem = true;
                                    toInsert=true;
                                    toInsertDays=false;
                                    f = new Feed();
                                }
                                //if the tag is called "title"
                                else if (xpp.getName().equalsIgnoreCase("title")){
                                    if (insideItem){
                                        String title = xpp.nextText();
                                        if(title.equalsIgnoreCase("reptv"))toInsert=false;
                                        else f.setTitle(title);
                                    }else{
                                        newspaper = xpp.nextText();
                                        if(newspaper.contains(".it")){
                                            newspaper=newspaper.substring(0,newspaper.indexOf(".it"));
                                        }
                                    }
                                }
                                //if the tag is called "description"
                                else if (xpp.getName().equalsIgnoreCase("description")){
                                    if (insideItem){
                                        String description = xpp.nextText();
                                        
                                        if(description.contains("<img src") && newspaper.contains("Fanpage")){
                                            f.setImageURL(description.substring(description.indexOf("<img src=")+10, description.indexOf("/>")-2));
                                        }

                                        description = removeDescriptionTags(description);
                                        f.setDescription(description);
                                    }
                                }
                                //if the tag is called "enclosure"
                                else if (xpp.getName().equalsIgnoreCase("enclosure")||xpp.getName().equalsIgnoreCase("media:content")){
                                    if (insideItem){
                                        f.setImageURL(xpp.getAttributeValue(null, "url"));
                                    }
                                }
                                //if the tag is called "link"
                                else if (xpp.getName().equalsIgnoreCase("link")){
                                    if (insideItem){
                                        f.setLink(xpp.nextText());
                                    }
                                }
                                //if the tag is called "pubDate"
                                else if (xpp.getName().equalsIgnoreCase("pubDate") || xpp.getName().equalsIgnoreCase("atom:updated")){
                                    if (insideItem){
                                        Date d = toDate(xpp.nextText());
                                        Date today = new Date();
                                        assert d != null;
                                        long difference = today.getTime() - d.getTime();
                                        float daysBetween = ((float)difference / (1000*60*60*24));
                                        if(daysBetween<=Constants.NEWS_MAX_DAYS){
                                            f.setDate(d);
                                            toInsertDays=true;
                                        }
                                    }
                                }
                                //if the tag is called "thumbimage"
                                else if(xpp.getName().equalsIgnoreCase("thumbimage")){
                                    if(insideItem){
                                        f.setImageURL(xpp.getAttributeValue(0));
                                    }
                                }
                                else if(xpp.getName().equalsIgnoreCase("category")){
                                    categories.add(xpp.nextText());
                                }
                            }
                            //if we are at an END_TAG and the END_TAG is called "item"
                            else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                            {
                                insideItem = false;
                                if(toInsert&&toInsertDays){
                                    if(newspaper.equalsIgnoreCase("il fatto quotidiano")){
                                        if(categories.contains("Coronavirus")) {
                                            f.setNewspaper(newspaper);
                                            feeds.add(f);
                                        }
                                    }else{
                                        f.setNewspaper(newspaper);
                                        feeds.add(f);
                                    }
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    return feeds;
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(List<Feed> object) {
                super.onPostExecute(object);
                if (object == null)
                    callback.onError(new Error("Merda"));
                else {
                    data = object;
                    callback.onSuccess(object);
                }
            }
        };
        newsParse.execute();

    }

    private static Date toDate(String nextText) {
        try{
            return new Date(nextText);
        }catch(Exception e){
            try{
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ITALY);
                nextText=nextText.replace("T", " ");
                nextText=nextText.replace("Z", "");
                return formatter.parse(nextText);
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
        return null;
    }

}
