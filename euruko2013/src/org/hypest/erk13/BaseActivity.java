package org.hypest.erk13;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
	static ArrayList<NewsRecord> sNews = new ArrayList<NewsRecord>();

    static final Object LOCK = new Object();
    Runnable mResult;
    Context mContext;
    View mMainView;
    View customNav;
    static ProgressBar mActivityIndicator;
    SlidingMenu mMainMenu;
	Fragment mCurrentFragment;
	Fragment mCurrentDetailsFragment;

	boolean mDualPane = false;

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
			mCurrentDetailsFragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "mCurrentDetailsFragment");
		}

		if (mCurrentFragment == null) {
			mNewsFragment = new NewsFragment();
			mCurrentFragment = mNewsFragment;
		}

		// set the Above View
		setContentView(R.layout.activity_main);
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.fadein, 0)
		.replace(R.id.main_content, mCurrentFragment)
		.commit();

		View detailsView = findViewById(R.id.main_details);
		if (detailsView != null) {
			mDualPane = true;
		}

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
        getAgendaItems();
        getNewsItems();

        mMainView = findViewById(R.id.mainView);
		mActivityIndicator = (ProgressBar) mCustomActionBarView
				.findViewById(R.id.progressBar);
		mActivityIndicator.setVisibility(View.INVISIBLE);

        new HttpGetJSONTask(new GetJSONHandler() {
			@Override
			public void handle(JSONObject json) {
				getNewsItems(json);
				if (mNewsFragment != null) {
					mNewsFragment.networkRefresh();
				}

				mActivityIndicator.post(new Runnable() {
					@Override
					public void run() {
						mActivityIndicator.setVisibility(View.INVISIBLE);
					}
				});
			}
			
			@Override public void failed() {}
		}).execute(new HttpGet("http://euruko2013.codigia.com/news.json"));

        new HttpGetJSONTask(new GetJSONHandler() {
			@Override
			public void handle(JSONObject json) {
				getSpeakers(json);
				if (mSpeakersFragment != null) {
					mSpeakersFragment.networkRefresh();
				}

				getAgendaItems(json);
				if (mAgendaFragment != null) {
					mAgendaFragment.networkRefresh();
				}

				mActivityIndicator.post(new Runnable() {
					@Override
					public void run() {
						mActivityIndicator.setVisibility(View.INVISIBLE);
					}
				});
			}
			
			@Override public void failed() {}
		}).execute(new HttpGet("http://euruko2013.codigia.com/agenda.json"));
    }

    public interface GetJSONHandler {
    	public void handle(JSONObject json);
    	public void failed();
    }

	public static class HttpGetJSONTask extends
			AsyncTask<HttpUriRequest, Void, JSONObject> {
		private static final String TAG = "EURUKO2013";

		private GetJSONHandler mGetJSONHandler;

		public HttpGetJSONTask(GetJSONHandler handler) {
			mGetJSONHandler = handler;
		}

		@Override
		protected JSONObject doInBackground(HttpUriRequest... params) {
			mActivityIndicator.post(new Runnable() {
				@Override
				public void run() {
					mActivityIndicator.setVisibility(View.VISIBLE);
				}
			});

			HttpUriRequest request = params[0];
			HttpClient client = new DefaultHttpClient();

			try {
				HttpResponse response = client.execute(request);

				// TODO handle bad response codes (such as 404, etc)

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
				JSONTokener tokener = new JSONTokener(builder.toString());
				JSONObject json = new JSONObject(tokener);
				return json;

			} catch (Exception e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			// Done on UI Thread
			if (jsonObject != null) {
				mGetJSONHandler.handle(jsonObject);
			} else {
				mGetJSONHandler.failed();
			}

			mActivityIndicator.post(new Runnable() {
				@Override
				public void run() {
					mActivityIndicator.setVisibility(View.INVISIBLE);
				}
			});
		}
	}

    public interface GetDrawableHandler {
    	public void handle(Drawable drawable);
    	public void failed();
    }

	public static class HttpGetDrawableTask extends
			AsyncTask<HttpUriRequest, Void, Drawable> {
		private static final String TAG = "EURUKO2013";

		private GetDrawableHandler mGetDrawableHandler;

		public HttpGetDrawableTask(GetDrawableHandler handler) {
			mGetDrawableHandler = handler;
		}

		@Override
		protected Drawable doInBackground(HttpUriRequest... params) {
			HttpUriRequest request = params[0];
			HttpClient client = new DefaultHttpClient();

			try {
				HttpResponse response = client.execute(request);

				// TODO handle bad response codes (such as 404, etc)

				InputStream is = (InputStream) response.getEntity().getContent();
		        Drawable drawable = Drawable.createFromStream(is, "src name");
				return drawable;

			} catch (Exception e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Drawable drawable) {
			// Done on UI Thread
			if (drawable != null) {
				mGetDrawableHandler.handle(drawable);
			} else {
				mGetDrawableHandler.failed();
			}

//			mActivityIndicator.post(new Runnable() {
//				@Override
//				public void run() {
//					mActivityIndicator.setVisibility(View.INVISIBLE);
//				}
//			});
		}
	}

    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mCurrentFragment != null) {
			getSupportFragmentManager().putFragment(outState,
					"mCurrentFragment", mCurrentFragment);
		}

		if (mCurrentDetailsFragment != null) {
			getSupportFragmentManager().putFragment(outState,
					"mCurrentDetailsFragment", mCurrentDetailsFragment);
		}
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

	public void showDetails(Fragment fragment) {
		if (!mDualPane) {
			showContent(fragment);
			return;
		}

		mCurrentDetailsFragment = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.fadein, R.anim.fadeout)
		.replace(R.id.main_details, fragment)
		.addToBackStack(null)
		.commit();
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

    private static List<Speaker> getSpeakers(JSONObject speakersJSONObject) {
    	sSpeakers = new ArrayList<Speaker>();

    	if (speakersJSONObject == null) {
    		return sSpeakers;
    	}

        try {
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

    private static List<Speaker> getSpeakers() {
    	JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(Utils.JSON.readAsset("agenda.json"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getSpeakers(jsonObject);
    }

    private static List<AgendaItem> getAgendaItems(JSONObject agendaJSONObject) {
    	sSpeeches = new ArrayList<AgendaItem>();

    	if (agendaJSONObject == null) {
    		return sSpeeches;
    	}

        try {
            JSONArray agendaJSON = agendaJSONObject.getJSONArray("agenda");
            for (int k = 0; k < agendaJSON.length(); k++) {
                AgendaItem item = new AgendaItem(agendaJSON.getJSONObject(k));
                sSpeeches.add(item);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return BaseActivity.sSpeeches;
    }

    private static List<AgendaItem> getAgendaItems() {
    	JSONObject jsonObject = null;

    	try {
			jsonObject = new JSONObject(Utils.JSON.readAsset("agenda.json"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return getAgendaItems(jsonObject);
    }

    private static List<NewsRecord> getNewsItems(JSONObject newsJSONObject) {
    	sNews = new ArrayList<NewsRecord>();

    	if (newsJSONObject == null) {
    		return BaseActivity.sNews;
    	}

        try {
            JSONArray newsJSON = newsJSONObject.getJSONArray("news");
            for (int k = 0; k < newsJSON.length(); k++) {
                NewsRecord item = new NewsRecord(newsJSON.getJSONObject(k));
                sNews.add(item);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return sNews;
    }

    private static List<NewsRecord> getNewsItems() {
    	JSONObject jsonObject = null;

        try {
			jsonObject = new JSONObject(Utils.JSON.readAsset("news.json"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return getNewsItems(jsonObject);
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
    	showDetails(new SpeakerFragment());
    }

    public void viewSpeech(int position) {
    	currentSpeechPosition = position;
    	showDetails(new SpeechFragment());
    }

    public void viewURL(String urlString) {
    	currentURLString = urlString;
    	showDetails(new WebviewFragment());
    }
}
