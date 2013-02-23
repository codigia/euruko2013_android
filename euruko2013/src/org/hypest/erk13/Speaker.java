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
    public Drawable avatar;
    public Drawable avatarBig;
    public String bio;
    
    public Speaker(JSONObject json) {
        try {
            id = json.getString("id");
            name = json.getString("name");
            title = json.getString("title");
            bio = json.getString("bio");

            String iconB64 = json.getString("icon");
            byte[] b = Base64.decode(iconB64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
            avatar = new BitmapDrawable(decodedByte);
            avatar.setBounds(0, 0, 50, 50);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
