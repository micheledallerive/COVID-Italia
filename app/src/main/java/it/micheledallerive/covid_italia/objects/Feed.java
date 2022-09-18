package it.micheledallerive.covid_italia.objects;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Feed {

    private String title;
    private String description;
    private String link;
    private String imageURL;
    private Date date;
    private String newspaper;

    public Feed(String title, String description, String link, String imageURL, Date date, String newspaper) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.imageURL = imageURL;
        this.date = date;
        this.newspaper = newspaper;
    }

    public Feed(){

    }

    @NonNull
    @Override
    public String toString() {
        return "Feed{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", date=" + date +
                ", newspaper='" + newspaper + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNewspaper() {
        return newspaper;
    }

    public void setNewspaper(String newspaper) {
        this.newspaper = newspaper;
    }

    public String getDateString(){
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ITALY);
        return formatter.format(this.date);
    }

    public String timeAgo(){
        Date d = new Date();
        long difference = d.getTime() - date.getTime();
        float daysBetween = ((float)difference / (1000*60*60*24));
        if(daysBetween>=1){
            if(Math.floor(daysBetween)==1){
                return "1 giorno fa";
            }else {
                return ((int) Math.floor(daysBetween) + " giorni fa");
            }
        }else{
            int hours =(int)Math.floor(daysBetween*24);
            if(hours==0){
                int minutes = (int)Math.floor(daysBetween*24*60);
                if(minutes!=1){
                    return minutes+" minuti fa";
                }else{
                    return "1 minuto fa";
                }
            }
            if(hours!=1)
                return hours+" ore fa";
            else
                return "1 ora fa";
        }
    }
}
