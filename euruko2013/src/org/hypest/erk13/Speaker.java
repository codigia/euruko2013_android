package org.hypest.erk13;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class Speaker {
    public String id;
    public String name;
    public String title;
    public String bio;
    public Drawable avatar;
    
    @SuppressWarnings("deprecation")
	public Speaker(JSONObject json) {
        try {
	        this.id = json.getString("id");
	        this.name = json.getString("name");
	        this.title = json.getString("title");
	        this.bio = json.getString("bio");

	        String avatarB64 = json.getString("avatar");
	        byte[] b = Base64.decode(avatarB64, Base64.DEFAULT);
	        Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
	        avatar = new BitmapDrawable(decodedByte);
	        avatar.setBounds(0, 0, 50, 50);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
