package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import org.hypest.erk13.BaseActivity.GetDrawableHandler;

import twitter4j.URLEntity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TweetAdapter extends ArrayAdapter<MyTweet> implements
		OnItemClickListener {

    static final int resId = R.layout.tweet;
    private TwitterFragment mTwitterFragment;
    private List<MyTweet> mTweets;

	public TweetAdapter(TwitterFragment twitterFragment,
			ArrayList<MyTweet> tweets) {
            super(twitterFragment.getActivity(), resId, tweets);
            mTwitterFragment = twitterFragment;
            mTweets = tweets;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    };
    
    @Override
    public int getItemViewType(int position) {
        return 1;
    };
    
    @Override
    public int getViewTypeCount() {
        return 1;
    };
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyTweet r = mTweets.get(position);

        final Context ctx = mTwitterFragment.getActivity();

        if (r == null) {
	        LayoutInflater vi = (LayoutInflater) ctx
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        return vi.inflate(R.layout.tweet_rate_exceeded, null);
	    }

        View v = convertView;

        int rid = resId;
        if (r.tweet == null) {
        	rid = R.layout.tweet_loading;
        }

		if (convertView != null && convertView.getTag() != null
				&& ((Integer) convertView.getTag()) != rid) {
    		v = null; // to make it inflate a new one
    	}

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(rid, null);
        }

        v.setTag(Integer.valueOf(rid));

        if (r.tweet != null) {
            TextView tt = (TextView) v.findViewById(R.id.title);
            TextView su = (TextView) v.findViewById(R.id.summary);
            if (tt != null) {
                  tt.setText(r.tweet.getUser().getName());
            }
            
            StringBuilder sb = new StringBuilder(r.tweet.getText());
            
            URLEntity[] urls = r.tweet.getURLEntities();
            for(URLEntity url : urls) {
                String u = url.getURL().toString();
                sb.replace(url.getStart(), url.getEnd(), "<a href=\"" + u + "\">" + u + "</a>");
            }
            
            if(su != null) {
                su.setText(Html.fromHtml(sb.toString()));
                su.setMovementMethod(LinkMovementMethod.getInstance());
            }
            
			final ImageView profilePic = (ImageView) v
					.findViewById(R.id.profilePic);
            if (profilePic != null) {
            	profilePic.setVisibility(View.INVISIBLE);

				r.fetchProfilePicture(new GetDrawableHandler() {
					@Override
					public void handle(Drawable drawable) {
						profilePic.setImageDrawable(drawable);
						profilePic.setVisibility(View.VISIBLE);
					}

					@Override
					public void failed() {
					}
				});
                
                profilePic.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(r.tweet.getUser().getURL()));
                        ctx.startActivity(i);
                    }
                });
            }
        } else {
        	mTwitterFragment.new TwitterTask().execute();
        }
        return v;
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
}