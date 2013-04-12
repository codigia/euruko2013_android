package org.hypest.erk13;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.google.android.maps.MapView;

public class MainActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.activity_main;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewNews = (ListView) findViewById(R.id.news);
        mViewAgenda = (ListView) findViewById(R.id.agenda);
        mViewSpeakers = (ListView) findViewById(R.id.speakers);
        mViewTwitter = (ListView) findViewById(R.id.twitter);
        mViewMap = (MapView) findViewById(R.id.mapview);
        mViewAbout = findViewById(R.id.about);

        mViewNews.setTag("News");
        mViewAgenda.setTag("Agenda");
        mViewSpeakers.setTag("Speakers");
        mViewTwitter.setTag("#euruko");
        mViewMap.setTag("Map");
        mViewAbout.setTag("About");

        mViews.add(mViewNews);
        mViews.add(mViewAgenda);
        mViews.add(mViewSpeakers);
        mViews.add(mViewTwitter);
        mViews.add(mViewMap);
        mViews.add(mViewAbout);

        mMainView = findViewById(R.id.mainView);

        List<NewsRecord> news = getNewsItems();
        NewsRecordAdapter newsadapter = new NewsRecordAdapter(this,
                R.layout.newsitem,
                news);
        mViewNews.setAdapter(newsadapter);

        if (!getIntent().hasExtra(EXTRA_MENU)) {
        	hideAllBut(mViewNews);
        }

        new Handler().post(mDelayedLoad);
    }
}
