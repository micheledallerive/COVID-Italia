package it.micheledallerive.covid_italia;

import it.micheledallerive.covid_italia.objects.Error;

public interface Callback {
    void onSuccess(Object obj);
    void onError(Error error);
}
