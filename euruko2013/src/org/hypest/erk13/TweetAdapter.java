package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import twitter4j.URLEntity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<MyTweet> {

    static final int resId = R.layout.tweet;
    private Context mContext;
    private List<MyTweet> mTweets;

    public TweetAdapter(Context context, ArrayList<MyTweet> tweets) {
            super(context, resId, tweets);
            mContext = context;
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
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(resId, null);
            }
            
            final MyTweet r = mTweets.get(position);
            if (r != null) {
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
                
                ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
                if (profilePic != null) {
                    if (r.pic == null) {
                        profilePic.setImageResource(R.drawable.twitter);
                    } else {
                        profilePic.setImageDrawable(r.pic);
                    }
                    
                    profilePic.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(r.tweet.getUser().getURL()));
                            mContext.startActivity(i);
                        }
                    });
                }
            } else {
                LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.tweet_rate_exceeded, null);
            }
            return v;
    }
}