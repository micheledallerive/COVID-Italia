package it.micheledallerive.covid_italia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import it.micheledallerive.covid_italia.objects.Feed;
import it.micheledallerive.covid_italia.utils.Constants;

public class NewsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private static List<String> adMobIds = new ArrayList<>();
    Context context;
    List<Feed> feeds;

    public NewsAdapter(Context context, List<Feed> feeds) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.feeds = feeds;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setupAds();
    }

    private void setupAds(){
        adMobIds.add("ca-app-pub-1382810446229806/5831316229");
        adMobIds.add("ca-app-pub-1382810446229806/9566068170");
        adMobIds.add("ca-app-pub-1382810446229806/5486673621");
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return feeds.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return feeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi;
        int adCounter=position+1;
        if (adCounter%5!=0) {
            vi = inflater.inflate(R.layout.news_row, null);
            // FILL ROW
            final Feed feed = feeds.get(position);
            vi.findViewById(R.id.card).setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getLink()));
                v.getContext().startActivity(browserIntent);
            });
            ((TextView) vi.findViewById(R.id.news_title)).setText(feed.getTitle());
            ((TextView) vi.findViewById(R.id.news_description)).setText(feed.getDescription());
            ((TextView) vi.findViewById(R.id.news_newspaper)).setText(feed.getNewspaper());
            ((TextView) vi.findViewById(R.id.news_date)).setText(feed.timeAgo());
            ImageView image = vi.findViewById(R.id.news_image);
            Glide
                    .with(vi.getContext())
                    .load(feed.getImageURL())
                    .centerCrop()
                    .placeholder(R.color.gray)
                    .into(image);
        }else{
            vi=inflater.inflate(R.layout.ad_row_layout, null);
            View container = vi.findViewById(R.id.adViewContainer);
            AdView mAdView = new AdView(context);
            mAdView.setAdSize(AdSize.LARGE_BANNER);
            mAdView.setAdUnitId(adMobIds.get((adCounter%5)% adMobIds.size()));
            ((LinearLayout)container).addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            if(Constants.isAdEnabled) mAdView.loadAd(adRequest);
        }
        return vi;
    }


}
