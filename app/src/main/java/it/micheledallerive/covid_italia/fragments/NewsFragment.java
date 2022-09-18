package it.micheledallerive.covid_italia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import it.micheledallerive.covid_italia.Callback;
import it.micheledallerive.covid_italia.NewsAdapter;
import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.data.NewsData;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.objects.Feed;


public class NewsFragment extends Fragment {

    ListView newsList;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar spinner;

    private void updateNews(View v, Callback callback, boolean forceUpdate){
        NewsData.getData(forceUpdate, new Callback() {
            @Override
            public void onSuccess(Object obj) {
                //Log.e("Update", "parseNews callback triggered");
                newsList.setAdapter(null);
                List<Feed> feeds = (List<Feed>) obj;
                feeds.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                newsList.setAdapter(new NewsAdapter(v.getContext(), feeds));
                if(callback!=null)callback.onSuccess(null);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false).getRoot();
        newsList = v.findViewById(R.id.news_list);
        spinner = v.findViewById(R.id.spinner);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setEnabled(false);
        //Log.e("Update", "NewsFragment created");
        updateNews(v, new Callback() {
            @Override
            public void onSuccess(Object obj) {
                //Log.e("Update", "updateNews callback triggered");
                spinner.setVisibility(View.GONE);
                newsList.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setOnRefreshListener(() -> updateNews(v,null, true));
            }

            @Override
            public void onError(Error error) {

            }
        }, false);
        return v;
    }
}
