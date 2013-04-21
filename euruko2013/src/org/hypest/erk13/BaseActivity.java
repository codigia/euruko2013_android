package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

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
	Fragment mCurrentFragment;

    View mCustomActionBarView;
    TextView mCustomActionBarTitleView;

    NewsFragment mNewsFragment;
    AgendaFragment mAgendaFragment;
    SpeakersFragment mSpeakersFragment;
    TwitterFragment mTwitterFragment;
    MapFragment mMapFragment;
    AboutFragment mAboutFragment;

    public String currentSpeakerId;
    public int currentSpeechPosition;
    public String currentURLString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        
		if (savedInstanceState != null) {
			mCurrentFragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "mCurrentFragment");
		}

		if (mCurrentFragment == null) {
			mCurrentFragment = new NewsFragment();
		}

		// set the Above View
		setContentView(R.layout.activity_main);
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.fadein, 0)
		.replace(R.id.main_content, mCurrentFragment)
		.commit();
		
		// set the Behind View
		setBehindContentView(R.layout.main_menu);
//		getSupportFragmentManager()
//		.beginTransaction()
//		.replace(R.id.menu_frame, new MainMenuFragment())
//		.commit();
		
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
        mMainMenu.setBehindScrollScale(0.5f);
        mMainMenu.setFadeDegree(0.35f);

        getSpeakers();

        mMainView = findViewById(R.id.mainView);
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mCurrentFragment",
				mCurrentFragment);
	}
	
	public void showContent(Fragment fragment) {
		mCurrentFragment = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.fadein, R.anim.fadeout)
		.replace(R.id.main_content, fragment)
		.addToBackStack(null)
		.commit();
		getSlidingMenu().showContent();
	}

    protected void setBarTitle(CharSequence title) {
    	setTitle(title);
    	mCustomActionBarTitleView.setText(title);
    }

//    protected Runnable mDelayedLoad = new Runnable() {
//		@Override
//		public void run() {
//			final Markers markers = new Markers(BaseActivity.this,
//					R.drawable.ruby_marker, mViewMap);
//			markers.add("Badminton Theater", "The EuRuKo2013 venue!",
//					37.986067f, 23.774682f, new Runnable() {
//						@Override
//						public void run() {
//							Intent browserIntent = new Intent(
//									Intent.ACTION_VIEW,
//									Uri.parse("http://euruko2013.org/#venue"));
//							mContext.startActivity(browserIntent);
//						}
//					});
//	        mViewMap.setBuiltInZoomControls(true);
//	        mViewMap.getOverlays().add(markers);
//
//	        ((TapControlledMapView) mViewMap)
//			    .setOnSingleTapListener(new OnSingleTapListener() {
//			        @Override
//			        public boolean onSingleTap(MotionEvent e) {
//			            markers.hideAllBalloons();
//			            return true;
//			        }
//			    });
//
//	        getSpeakers();
//
//		}
//	};

    public void onMainMenuClick(View view) {
        switch(view.getId()) {
        case R.id.menuNews:
        	if (mNewsFragment == null) {
        		mNewsFragment = new NewsFragment();
        	}
        	showContent(mNewsFragment);
            break;
        case R.id.menuCalendar:
        	if (mAgendaFragment == null) {
        		mAgendaFragment = new AgendaFragment();
        	}
        	showContent(mAgendaFragment);
            break;
        case R.id.menuSpeakers:
        	if (mSpeakersFragment == null) {
        		mSpeakersFragment = new SpeakersFragment();
        	}
        	showContent(mSpeakersFragment);
            break;
        case R.id.menuTwitter:
        	if (mTwitterFragment == null) {
        		mTwitterFragment = new TwitterFragment();
        	}
        	showContent(mTwitterFragment);
            break;
        case R.id.menuMap:
        	if (mMapFragment == null) {
        		mMapFragment = new MapFragment();
        	}
        	showContent(mMapFragment);
            break;
        case R.id.menuAbout:
        	if (mAboutFragment == null) {
        		mAboutFragment = new AboutFragment();
        	}
        	showContent(mAboutFragment);
            break;
        }

        mMainMenu.showContent();
    }

    public void onGaiaMenuClick(View view) {
    	if (mAboutFragment == null) {
    		mAboutFragment = new AboutFragment();
    	}
    	showContent(mAboutFragment);
        mMainMenu.showContent();
    }

    private List<Speaker> getSpeakers() {
    	sSpeakers = new ArrayList<Speaker>();
        try {
			JSONObject speakersJSONObject = new JSONObject(
					Utils.JSON.readAsset(mContext, R.raw.speakers));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
    		toggle();
            return true;
        }

        return false;
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

    public void viewSpeaker(String id) {
    	currentSpeakerId = id;
    	showContent(new SpeakerFragment());
    }

    public void viewSpeech(int position) {
    	currentSpeechPosition = position;
    	showContent(new SpeechFragment());
    }

    public void viewURL(String urlString) {
    	currentURLString = urlString;
    	showContent(new WebviewFragment());
    }
}
