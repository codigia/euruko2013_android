package org.hypest.erk13;

import org.apache.http.client.methods.HttpGet;
import org.hypest.erk13.BaseActivity.GetDrawableHandler;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

public class Speaker {
    public String id;
    public String name;
    public String title;
    public String bio;
    public String avatarURLString;
    private Drawable mAvatar;
    
	public Speaker(JSONObject json) {
        try {
	        this.id = json.getString("id");
	        this.name = json.getString("name");
	        this.title = json.getString("title");
	        this.bio = json.getString("bio");
	        this.avatarURLString = json.getString("avatar");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public void getAvatar(final GetDrawableHandler drawableHandler) {
    	if (mAvatar != null) {
    		drawableHandler.handle(mAvatar);
    	} else {
    		if (avatarURLString != null) {
				new BaseActivity.HttpGetDrawableTask(
						new GetDrawableHandler() {
							@Override
							public void handle(Drawable drawable) {
								mAvatar = drawable;
								drawableHandler.handle(mAvatar);
							}

							@Override
							public void failed() {
								drawableHandler.failed();
							}
						}).execute(new HttpGet(avatarURLString));
    		}
    	}
	}
}
