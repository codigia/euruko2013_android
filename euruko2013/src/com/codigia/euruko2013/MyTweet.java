package com.codigia.euruko2013;

import org.apache.http.client.methods.HttpGet;

import com.codigia.euruko2013.BaseActivity.GetDrawableHandler;

import twitter4j.Status;
import android.graphics.drawable.Drawable;

public class MyTweet {

    public Status tweet;
    private Drawable mPicture;

    public final static MyTweet ReloadTweet = new MyTweet(null);

    public MyTweet(Status tweet) {
        this.tweet = tweet;
    }
    
	public void fetchProfilePicture(final GetDrawableHandler drawableHandler) {
    	if (mPicture != null) {
    		drawableHandler.handle(mPicture);
    	} else {
            String pictureURLString = tweet.getUser().getProfileImageURL();
    		if (pictureURLString != null) {
				new BaseActivity.HttpGetDrawableTask(
						new GetDrawableHandler() {
							@Override
							public void handle(Drawable drawable) {
								mPicture = drawable;
								drawableHandler.handle(mPicture);
							}

							@Override
							public void failed() {
								drawableHandler.failed();
							}
						}).execute(new HttpGet(pictureURLString));
    		}
    	}
	}
}
