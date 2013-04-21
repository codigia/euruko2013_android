package org.hypest.erk13;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.Status;
import android.graphics.drawable.Drawable;

public class MyTweet {

    public Status tweet;
    public Drawable pic;

    public final static MyTweet ReloadTweet = new MyTweet(null);

    public MyTweet(Status tweet) {
        this.tweet = tweet;
    }
    
    public void fetchProfilePic() {
        pic = null;
        
        String urlstr = tweet.getUser().getProfileImageURL();
        URL url;
        try {
            url = new URL(urlstr);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
        
        InputStream is = null;
        try {
            is = (InputStream) url.getContent();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        pic = Drawable.createFromStream(is, "anonymous");
    }
}
