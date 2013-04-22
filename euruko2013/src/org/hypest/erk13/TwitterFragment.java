package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.os.Bundle;

public class TwitterFragment extends BaseListFragment {

    ArrayList<MyTweet> myTweets = new ArrayList<MyTweet>();

    private ConfigurationBuilder mConfigurationBuilder;
    private TwitterFactory mFactory;
    private Twitter mTwitter;
    private Query mQuery = null;
    private TweetAdapter mTweetsadapter;
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mConfigurationBuilder = new ConfigurationBuilder();
		mConfigurationBuilder.setDebugEnabled(true)
	      .setOAuthConsumerKey("wusQ8BNzpdDGBYmFmuqRSQ")
	      .setOAuthConsumerSecret("c89Kywambx93B11DBp3ZlQJ6ozxQEc19eJkfpTxydk")
	      .setOAuthAccessToken("89283128-Gmvu6X4PHodshgDwpDZb4NU7sSz6RqLko02DdXWxt")
	      .setOAuthAccessTokenSecret("5XIR5wnwYljxGhTKngscLRdwsaftstjqWaJhhGYO0A");
	    mFactory = new TwitterFactory(mConfigurationBuilder.build());
	    mTwitter = mFactory.getInstance();

        mQuery = new Query("euruko");
        mQuery.setCount(10);

        myTweets.add(MyTweet.ReloadTweet);

		mTweetsadapter = new TweetAdapter(this, myTweets);

        getListView().setAdapter(mTweetsadapter);
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("#euruko");

        super.onResume();
	}

    public class TwitterTask extends AsyncTask<Void, Integer, Long> {
        protected Long doInBackground(Void... params) {
            try {
            	if (mQuery == null) {
            		return null;
            	}

            	myTweets.remove(MyTweet.ReloadTweet);

                QueryResult result;
                result = mTwitter.search(mQuery);
                List<twitter4j.Status> tweets = result.getTweets();
                for (twitter4j.Status tweet : tweets) {
                    myTweets.add(new MyTweet(tweet));
                }

                if (tweets.size() > 0) {
                	myTweets.add(MyTweet.ReloadTweet);
                }

            	mQuery = result.nextQuery();

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
        	if (result == null) {
        		return;
        	}

        	mTweetsadapter.notifyDataSetChanged();
        }
    }

	@Override
	public void networkRefresh() {
	}
}