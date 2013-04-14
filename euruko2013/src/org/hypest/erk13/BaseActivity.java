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
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.MapView;
import com.readystatesoftware.maps.OnSingleTapListener;
import com.readystatesoftware.maps.TapControlledMapView;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingMapActivity;

public abstract class BaseActivity extends SlidingMapActivity {

    enum TAB {
        NEWS,
        AGENDA,
        SPEAKERS,
        TWITTER,
        POI,
        ABOUT
    }

    static int REQUEST_NAVIGATE = 1;

	static String EXTRA_SPEAKER_ID = "extra_speaker_id";
	static String EXTRA_SPEECH_ID = "extra_speech_id";
	static String EXTRA_MENU = "extra_menu";

	static ArrayList<Speaker> sSpeakers = new ArrayList<Speaker>();
	static ArrayList<AgendaItem> sSpeeches = new ArrayList<AgendaItem>();

    static final Object LOCK = new Object();
    Runnable mResult;
    Context mContext;
    View mMainView;
    View customNav;
    SlidingMenu mMainMenu;

    View mCustomActionBarView;
    TextView mCustomActionBarTitleView;

    List<View> mViews;

    ListView mViewNews;
    ListView mViewAgenda;
    ListView mViewSpeakers;
    ListView mViewTwitter;
    MapView mViewMap;
    View mViewAbout;

    protected abstract int getContentViewId();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        
        setContentView(getContentViewId());
        setBehindContentView(R.layout.main_menu);
        setSlidingActionBarEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(R.drawable.menubutton);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mCustomActionBarView = getSupportActionBar().getCustomView();
		mCustomActionBarTitleView = (TextView) mCustomActionBarView
				.findViewById(R.id.title);
		setBarTitle(getTitle());

        mMainMenu = getSlidingMenu();
        mMainMenu.setMode(SlidingMenu.LEFT);
        mMainMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mMainMenu.setBehindOffsetRes(R.dimen.main_menu_slidingmenu_offset);
        mMainMenu.setBehindScrollScale(0.0f);
        mMainMenu.setFadeDegree(0.35f);

        mViews = new ArrayList<View>();

        mMainView = findViewById(R.id.mainView);
    }

    protected void setBarTitle(CharSequence title) {
    	setTitle(title);
    	mCustomActionBarTitleView.setText(title);
    }

    protected Runnable mDelayedLoad = new Runnable() {
		@Override
		public void run() {
			final Markers markers = new Markers(BaseActivity.this,
					R.drawable.ruby_marker, mViewMap);
			markers.add("Badminton Theater", "The EuRuKo2013 venue!",
					37.986067f, 23.774682f, new Runnable() {
						@Override
						public void run() {
							Intent browserIntent = new Intent(
									Intent.ACTION_VIEW,
									Uri.parse("http://euruko2013.org/#venue"));
							mContext.startActivity(browserIntent);
						}
					});
	        mViewMap.setBuiltInZoomControls(true);
	        mViewMap.getOverlays().add(markers);

	        ((TapControlledMapView) mViewMap)
			    .setOnSingleTapListener(new OnSingleTapListener() {
			        @Override
			        public boolean onSingleTap(MotionEvent e) {
			            markers.hideAllBalloons();
			            return true;
			        }
			    });

	        getSpeakers();
			SpeakerAdapter speakersadapter = new SpeakerAdapter(
					BaseActivity.this, R.layout.speakersitem, sSpeakers);
	        mViewSpeakers.setAdapter(speakersadapter);

	        List<AgendaItem> agenda = getAgendaItems();
			AgendaItemAdapter agendaAdapter = new AgendaItemAdapter(
					BaseActivity.this, sSpeakers, agenda);
	        mViewAgenda.setAdapter(agendaAdapter);

	        new TwitterTask().execute("#euruko");
		}
	};

    private class TwitterTask extends AsyncTask<String, Integer, Long> {
        final ArrayList<MyTweet> myTweets = new ArrayList<MyTweet>();

        protected Long doInBackground(String... urls) {
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
            	if (te.getStatusCode() == 429) {
            		myTweets.add(null);
            	}
            }

            return 0l;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            mMainView.post(new Runnable() {
                @Override
                public void run() {
                    ListView lv = (ListView) mMainView.findViewById(R.id.twitter);
                    
                    TweetAdapter tweetsadapter = new TweetAdapter(mContext,
                            myTweets);
                    
                    lv.setAdapter(tweetsadapter);
                }
            });
        }
    }

    public void onMainMenuClick(View view) {
    	if (!(this instanceof MainActivity)) {
    		Intent intent = new Intent();

            switch(view.getId()) {
            case R.id.menuLogo:
                toggle();
                break;
            case R.id.menuNews:
        		intent.putExtra(EXTRA_MENU, TAB.NEWS.toString());
                break;
            case R.id.menuCalendar:
        		intent.putExtra(EXTRA_MENU, TAB.AGENDA.toString());
                break;
            case R.id.menuSpeakers:
        		intent.putExtra(EXTRA_MENU, TAB.SPEAKERS.toString());
                break;
            case R.id.menuTwitter:
        		intent.putExtra(EXTRA_MENU, TAB.TWITTER.toString());
                break;
            case R.id.menuMap:
        		intent.putExtra(EXTRA_MENU, TAB.POI.toString());
                break;
            case R.id.menuAbout:
        		intent.putExtra(EXTRA_MENU, TAB.ABOUT.toString());
                break;
            }

    		setResult(RESULT_OK, intent);
    		finish();
    		return;
    	}

        switch(view.getId()) {
        case R.id.menuNews:
        	hideAllBut(mViewNews);
            break;
        case R.id.menuCalendar:
        	hideAllBut(mViewAgenda);
            break;
        case R.id.menuSpeakers:
        	hideAllBut(mViewSpeakers);
            break;
        case R.id.menuTwitter:
        	hideAllBut(mViewTwitter);
            break;
        case R.id.menuMap:
        	hideAllBut(mViewMap);
            break;
        case R.id.menuAbout:
        	hideAllBut(mViewAbout);
            break;
        }

        mMainMenu.showContent();
    }

    protected void hideAllBut(View showView) {
    	for (View v : mViews) {
			v.setVisibility(v == showView ? View.VISIBLE : View.GONE);
    	}

    	setBarTitle((CharSequence) showView.getTag());
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    protected List<NewsRecord> getNewsItems() {
        ArrayList<NewsRecord> news = new ArrayList<NewsRecord>();
        try {
            JSONObject newsJSONObject = new JSONObject(readJSON(R.raw.news));
            JSONArray newsJSON = newsJSONObject.getJSONArray("news");
            for (int k = 0; k < newsJSON.length(); k++) {
                NewsRecord item = new NewsRecord(newsJSON.getJSONObject(k));
                news.add(item);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return news;
    }

    private List<AgendaItem> getAgendaItems() {
    	sSpeeches = new ArrayList<AgendaItem>();
        try {
            JSONObject agendaJSONObject = new JSONObject(readJSON(R.raw.agenda));
            JSONArray agendaJSON = agendaJSONObject.getJSONArray("agenda");
            for (int k = 0; k < agendaJSON.length(); k++) {
                AgendaItem item = new AgendaItem(agendaJSON.getJSONObject(k));
                sSpeeches.add(item);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return sSpeeches;
    }

    private List<Speaker> getSpeakers() {
    	sSpeakers = new ArrayList<Speaker>();
        try {
            JSONObject speakersJSONObject = new JSONObject(readJSON(R.raw.speakers));
            JSONArray speakersJSON = speakersJSONObject.getJSONArray("speakers");
            for (int k = 0; k < speakersJSON.length(); k++) {
                Speaker item = new Speaker(speakersJSON.getJSONObject(k));
                sSpeakers.add(item);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return sSpeakers;
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	if (this instanceof MainActivity) {
        		toggle();
        	} else {
        		finish();
        	}
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode != RESULT_OK) {
    		return;
    	}

    	if (requestCode != REQUEST_NAVIGATE) {
    		super.onActivityResult(requestCode, resultCode, data);
    		return;
    	}

    	TAB tab = TAB.valueOf(data.getStringExtra(EXTRA_MENU));
    	
        switch(tab) {
        case NEWS:
        	hideAllBut(mViewNews);
            break;
        case AGENDA:
        	hideAllBut(mViewAgenda);
            break;
        case SPEAKERS:
        	hideAllBut(mViewSpeakers);
            break;
        case TWITTER:
        	hideAllBut(mViewTwitter);
            break;
        case POI:
        	hideAllBut(mViewMap);
            break;
        case ABOUT:
        	hideAllBut(mViewAbout);
            break;
        }

    }

    public void gotoGaiaSponsor(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.gaia_sponsor_url)));
        startActivity(i);
    }

    public void gotoCodigia(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.codigia_url)));
        startActivity(i);
    }
}
