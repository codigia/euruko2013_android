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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        new TwitterTask().execute("#euruko");
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("#euruko");

        super.onResume();
	}

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
        	getListView().post(new Runnable() {
                @Override
                public void run() {
					TweetAdapter tweetsadapter = new TweetAdapter(
							getActivity(), myTweets);
                    
                    getListView().setAdapter(tweetsadapter);
                }
            });
        }
    }

	@Override
	public void networkRefresh() {
	}
}