package org.hypest.erk13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.MapView;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingMapActivity;

public class MainActivity extends SlidingMapActivity {
//    ActionBarSherlock mSherlock = ActionBarSherlock.wrap(this);

    enum TAB {
        NEWS,
        AGENDA,
        SPEAKERS,
        TWITTER,
        POI,
        ABOUT
    }
    
    static final Object LOCK = new Object();
    Runnable mResult;
    Context mContext;
    View mView;
    View customNav;
    SlidingMenu mMainMenu;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        
//        mSherlock.requestFeature((int) Window.FEATURE_ACTION_BAR_OVERLAY);
        
//        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
//        mSherlock.setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
//        mSherlock.setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.main_menu);
        setSlidingActionBarEnabled(false);

        mMainMenu = getSlidingMenu();
        mMainMenu.setMode(SlidingMenu.LEFT);
        mMainMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        mMainMenu.setShadowWidthRes(R.dimen.main_menu_shadow_width);
//        mMainMenu.setShadowDrawable(R.drawable.bar_shadow_side_reverse);
        mMainMenu.setBehindOffsetRes(R.dimen.main_menu_slidingmenu_offset);
        mMainMenu.setBehindScrollScale(0.0f);
        mMainMenu.setFadeDegree(0.35f);

        findViewById(R.id.agenda).setVisibility(View.GONE);
        findViewById(R.id.speakers).setVisibility(View.GONE);
        findViewById(R.id.twitter).setVisibility(View.GONE);
        MapView mv = (MapView) findViewById(R.id.mapview);
        mv.setVisibility(View.GONE);
        findViewById(R.id.about).setVisibility(View.GONE);
        mView = findViewById(R.id.mainView);
        
        Markers markers = new Markers(this, R.drawable.ruby_marker);
        markers.add("Badminton Theater", 37.986067f, 23.774682f);
        mv.setBuiltInZoomControls(true);
        mv.getOverlays().add(markers);

        //Load partially transparent black background

//        //Inflate the custom view
//        customNav = LayoutInflater.from(this).inflate(R.layout.custom_nav, null);
//
//        ((CheckedTextView) customNav.findViewById(R.id.newsCheckbox)).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onNavSelected(TAB.NEWS);
//            }
//        });
//        ((CheckedTextView) customNav.findViewById(R.id.agendaCheckbox)).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onNavSelected(TAB.AGENDA);
//            }
//        });
//        ((CheckedTextView) customNav.findViewById(R.id.twitterCheckbox)).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onNavSelected(TAB.TWITTER);
//            }
//        });
//        ((CheckedTextView) customNav.findViewById(R.id.mapCheckbox)).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onNavSelected(TAB.POI);
//            }
//        });

        //Attach to the action bar
//        getSupportActionBar().setCustomView(customNav);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);

//        mSherlock.getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_transparent_eurk2));
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_transparent_eurk2));
//        mSherlock.getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ab_transparent));

        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setIcon(R.drawable.menubutton);
        
//        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        ActionBar.Tab tab = getSupportActionBar().newTab();
//        tab.setText("");
//        tab.setTabListener(this);
//        tab.setIcon(R.drawable.news);
//        tab.setTag(TAB.NEWS);
//        getSupportActionBar().addTab(tab);
//
//        tab = getSupportActionBar().newTab();
//        tab.setText("");
//        tab.setTabListener(this);
//        tab.setIcon(R.drawable.twitter);
//        tab.setTag(TAB.TWITTER);
//        getSupportActionBar().addTab(tab);
//        
//        tab = getSupportActionBar().newTab();
//        tab.setText("");
//        tab.setTabListener(this);
//        tab.setIcon(R.drawable.maps);
//        tab.setTag(TAB.POI);
//        getSupportActionBar().addTab(tab);

        List<Speaker> speakers = getSpeakers();
        SpeakerAdapter speakersadapter = new SpeakerAdapter(this,
                R.layout.speakersitem,
                speakers);
        ListView lv = (ListView) findViewById(R.id.speakers);
        lv.setAdapter(speakersadapter);

        List<NewsRecord> news = getNewsItems();
        NewsRecordAdapter newsadapter = new NewsRecordAdapter(this,
                R.layout.newsitem,
                news);
        lv = (ListView) findViewById(R.id.news);
        lv.setAdapter(newsadapter);

        
        List<AgendaItem> agenda = getAgendaItems();
        AgendaItemAdapter agendaAdapter = new AgendaItemAdapter(this, speakers,
                agenda);
        lv = (ListView) findViewById(R.id.agenda);
        lv.setAdapter(agendaAdapter);

        new TwitterTask().execute("#euruko");

//        AsyncTwitterFactory factory = new AsyncTwitterFactory(cb.build());
//        AsyncTwitter twitter = factory.getInstance();
//        twitter.addListener(new TwitterAdapter() {
//            @Override
//            public void searched(final QueryResult result) {
//                List<Status> tweets = result.getTweets();
//                final ArrayList<MyTweet> myTweets = new ArrayList<MyTweet>();
////                myTweets.add(null);
//                for (Status tweet : tweets) {
//                    MyTweet myt = new MyTweet(tweet);
//                    myt.fetchProfilePic();
//                    
//                    myTweets.add(myt);
//                }
//
//                mView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ListView lv = (ListView) mView.findViewById(R.id.twitter);
//                        
//                        TweetAdapter tweetsadapter = new TweetAdapter(mContext,
//                                myTweets);
//                        
//                        lv.setAdapter(tweetsadapter);
//                    }
//                });
//            };
//            
//            @Override
//            public void onException(TwitterException e, TwitterMethod method) {
//                if (method == TwitterMethod.UPDATE_STATUS) {
//                    e.printStackTrace();
//                    synchronized (LOCK) {
//                        LOCK.notify();
//                    }
//                } else {
//                    synchronized (LOCK) {
//                        LOCK.notify();
//                    }
//                    Debug.waitForDebugger();
//                    throw new AssertionError("Should not happen");
//                }
//            }
//        });
//        twitter.search(new Query("#euruko"));
////        synchronized (LOCK) {
////            try {
////                LOCK.wait();
////            } catch (InterruptedException e1) {
////                // TODO Auto-generated catch block
////                e1.printStackTrace();
////            }
////        }
         
        
        
//        Twitter twitter = new TwitterFactory().getInstance();
//        QueryResult result;
//        try {
//            result = twitter.search(new Query("#euruko"));
//        } catch (TwitterException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            finish();
//            return;
//        }
//        List<Tweet> tweets = result.getTweets();
//
//        lv = (ListView) findViewById(R.id.twitter);
//        
//        TweetAdapter tweetsadapter = new TweetAdapter(this,
//                R.layout.newsitem,
//                new ArrayList<Tweet>(tweets));
//        
//        lv.setAdapter(tweetsadapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        return mSherlock.dispatchCreateOptionsMenu((android.view.Menu) menu);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add("Twitter")
//            .setIcon(R.drawable.twitter)
//
////        menu.add("News")
////            .setIcon(R.drawable.news);
////
////        menu.add("POI")
////            .setIcon(R.drawable.maps);
//
//        return true;
//    }

    private class TwitterTask extends AsyncTask<String, Integer, Long> {
        final ArrayList<MyTweet> myTweets = new ArrayList<MyTweet>();

        protected Long doInBackground(String... urls) {
            int count = urls.length;
            String totalQuery = "";
            for (int i = 0; i < count; i++) {
                totalQuery += " " + urls[i];
                if (isCancelled()) break;
            }

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
              .setOAuthConsumerKey("wusQ8BNzpdDGBYmFmuqRSQ")
              .setOAuthConsumerSecret("c89Kywambx93B11DBp3ZlQJ6ozxQEc19eJkfpTxydk")
              .setOAuthAccessToken("89283128-Gmvu6X4PHodshgDwpDZb4NU7sSz6RqLko02DdXWxt")
              .setOAuthAccessTokenSecret("5XIR5wnwYljxGhTKngscLRdwsaftstjqWaJhhGYO0A");
            TwitterFactory factory = new TwitterFactory(cb.build());
            Twitter twitter = factory.getInstance();
            
            try {
                Query query = new Query("euruko");
                QueryResult result;
                do {
                    result = twitter.search(query);
                    List<twitter4j.Status> tweets = result.getTweets();
                    for (twitter4j.Status tweet : tweets) {
                        MyTweet myt = new MyTweet(tweet);
                        myt.fetchProfilePic();
                        
                        myTweets.add(myt);
                    }
                } while ((query = result.nextQuery()) != null);

            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to search tweets: " + te.getMessage());
                System.exit(-1);
            }

            return 0l;
        }

        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            mView.post(new Runnable() {
                @Override
                public void run() {
                    ListView lv = (ListView) mView.findViewById(R.id.twitter);
                    
                    TweetAdapter tweetsadapter = new TweetAdapter(mContext,
                            myTweets);
                    
                    lv.setAdapter(tweetsadapter);
                }
            });
        }
    }

    public void onMainMenuClick(View view) {
        switch(view.getId()) {
        case R.id.menuNews:
            onNavSelected(TAB.NEWS);
            break;
        case R.id.menuCalendar:
            onNavSelected(TAB.AGENDA);
            break;
        case R.id.menuSpeakers:
            onNavSelected(TAB.SPEAKERS);
            break;
        case R.id.menuTwitter:
            onNavSelected(TAB.TWITTER);
            break;
        case R.id.menuMap:
            onNavSelected(TAB.POI);
            break;
        case R.id.menuAbout:
            onNavSelected(TAB.ABOUT);
            break;
        }

        mMainMenu.showContent();
    }

public void onNavSelected(TAB tab) {
//    CheckedTextView nc = (CheckedTextView) customNav.findViewById(R.id.newsCheckbox);
//    CheckedTextView ac = (CheckedTextView) customNav.findViewById(R.id.agendaCheckbox);
//    CheckedTextView tc = (CheckedTextView) customNav.findViewById(R.id.twitterCheckbox);
//    CheckedTextView mc = (CheckedTextView) customNav.findViewById(R.id.mapCheckbox);
    switch(tab) {
        case NEWS:
//            if (!nc.isChecked()) 
            {
                findViewById(R.id.news).setVisibility(View.VISIBLE);
                findViewById(R.id.agenda).setVisibility(View.GONE);
                findViewById(R.id.speakers).setVisibility(View.GONE);
                findViewById(R.id.twitter).setVisibility(View.GONE);
                findViewById(R.id.mapview).setVisibility(View.GONE);
                findViewById(R.id.about).setVisibility(View.GONE);
            }
//            nc.setChecked(true);
//            ac.setChecked(false);
//            tc.setChecked(false);
//            mc.setChecked(false);
            break;
        case AGENDA:
//            if (!ac.isChecked()) 
            {
                findViewById(R.id.news).setVisibility(View.GONE);
                findViewById(R.id.agenda).setVisibility(View.VISIBLE);
                findViewById(R.id.speakers).setVisibility(View.GONE);
                findViewById(R.id.twitter).setVisibility(View.GONE);
                findViewById(R.id.mapview).setVisibility(View.GONE);
                findViewById(R.id.about).setVisibility(View.GONE);
            }
//            nc.setChecked(false);
//            ac.setChecked(true);
//            tc.setChecked(false);
//            mc.setChecked(false);
            break;
        case SPEAKERS:
//          if (!ac.isChecked()) 
          {
              findViewById(R.id.news).setVisibility(View.GONE);
              findViewById(R.id.agenda).setVisibility(View.GONE);
              findViewById(R.id.speakers).setVisibility(View.VISIBLE);
              findViewById(R.id.twitter).setVisibility(View.GONE);
              findViewById(R.id.mapview).setVisibility(View.GONE);
              findViewById(R.id.about).setVisibility(View.GONE);
          }
//          nc.setChecked(false);
//          ac.setChecked(true);
//          tc.setChecked(false);
//          mc.setChecked(false);
          break;
        case TWITTER:
//            if (!tc.isChecked()) 
            {
                findViewById(R.id.news).setVisibility(View.GONE);
                findViewById(R.id.agenda).setVisibility(View.GONE);
                findViewById(R.id.speakers).setVisibility(View.GONE);
                findViewById(R.id.twitter).setVisibility(View.VISIBLE);
                findViewById(R.id.mapview).setVisibility(View.GONE);
                findViewById(R.id.about).setVisibility(View.GONE);
            }
//            nc.setChecked(false);
//            ac.setChecked(false);
//            tc.setChecked(true);
//            mc.setChecked(false);
            break;
        case POI:
//            if (!mc.isChecked()) 
            {
                findViewById(R.id.news).setVisibility(View.GONE);
                findViewById(R.id.agenda).setVisibility(View.GONE);
                findViewById(R.id.speakers).setVisibility(View.GONE);
                findViewById(R.id.twitter).setVisibility(View.GONE);
                findViewById(R.id.mapview).setVisibility(View.VISIBLE);
                findViewById(R.id.about).setVisibility(View.GONE);
            }
//            nc.setChecked(false);
//            ac.setChecked(false);
//            tc.setChecked(false);
//            mc.setChecked(true);
            break;
        case ABOUT:
//          if (!mc.isChecked()) 
          {
              findViewById(R.id.news).setVisibility(View.GONE);
              findViewById(R.id.agenda).setVisibility(View.GONE);
              findViewById(R.id.speakers).setVisibility(View.GONE);
              findViewById(R.id.twitter).setVisibility(View.GONE);
              findViewById(R.id.mapview).setVisibility(View.GONE);
              findViewById(R.id.about).setVisibility(View.VISIBLE);
          }
//          nc.setChecked(false);
//          ac.setChecked(false);
//          tc.setChecked(false);
//          mc.setChecked(true);
          break;
    }
}

//    @Override
//    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
//    }
//
//    @Override
//    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
////        mSelected.setText("Selected: " + tab.getText());
//        switch((TAB) tab.getTag()) {
//            case NEWS:
//                findViewById(R.id.news).setVisibility(View.VISIBLE);
//                break;
//            case TWITTER:
//                findViewById(R.id.twitter).setVisibility(View.VISIBLE);
//                break;
//            case POI:
//                findViewById(R.id.mapview).setVisibility(View.VISIBLE);
//                break;
//        }
//    }
//
//    @Override
//    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
//        switch((TAB) tab.getTag()) {
//            case NEWS:
//                findViewById(R.id.news).setVisibility(View.GONE);
//                break;
//            case TWITTER:
//                findViewById(R.id.twitter).setVisibility(View.GONE);
//                break;
//            case POI:
//                findViewById(R.id.mapview).setVisibility(View.GONE);
//                break;
//        }
//    }

    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

    private List<Speaker> getSpeakers() {
        ArrayList<Speaker> speakers = new ArrayList<Speaker>();
        try {
            JSONObject speakersJSONObject = new JSONObject(readJSON(R.raw.speakers));
            JSONArray speakersJSON = speakersJSONObject.getJSONArray("speakers");
            for (int k = 0; k < speakersJSON.length(); k++) {
                Speaker item = new Speaker(speakersJSON.getJSONObject(k));
                speakers.add(item);
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return speakers;
    }

    private List<NewsRecord> getNewsItems() {
        ArrayList<NewsRecord> news = new ArrayList<NewsRecord>();
        try {
            JSONObject newsJSONObject = new JSONObject(readJSON(R.raw.news));
            JSONArray newsJSON = newsJSONObject.getJSONArray("news");
            for (int k = 0; k < newsJSON.length(); k++) {
                NewsRecord item = new NewsRecord(newsJSON.getJSONObject(k));
                news.add(item);
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return news;
    }

    private List<AgendaItem> getAgendaItems() {
        ArrayList<AgendaItem> agenda = new ArrayList<AgendaItem>();
        try {
            JSONObject agendaJSONObject = new JSONObject(readJSON(R.raw.agenda));
            JSONArray agendaJSON = agendaJSONObject.getJSONArray("agenda");
            for (int k = 0; k < agendaJSON.length(); k++) {
                AgendaItem item = new AgendaItem(agendaJSON.getJSONObject(k));
                agenda.add(item);
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return agenda;
    }

    String readJSON(int assetId) {
        InputStream is = getResources().openRawResource(assetId);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return writer.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            toggle();
            return true;
        }

        return false;
    }
}
