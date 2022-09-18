package it.micheledallerive.covid_italia.objects;

import androidx.annotation.NonNull;

public class Error {

    private String title;
    private String message;

    public Error(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Error(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return "Error{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
