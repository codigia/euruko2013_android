package org.hypest.erk13;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.Base64;

public class AgendaItem {

    public long startTime;
    public long endTime;
    public String who;
    public String title;
    public String descr;
    public Drawable icon;
    
    public AgendaItem(JSONObject json) {
        try {
            startTime = json.getLong("start");
            endTime = json.getLong("end");
            who = json.getString("who");
            title = json.getString("title");
            descr = json.getString("descr");

            String iconB64 = json.getString("icon");
            byte[] b = Base64.decode(iconB64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
            icon = new BitmapDrawable(decodedByte);
            icon.setBounds(0, 0, 50, 50);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public String getStartTimeFormatted() {
        return "" + DateFormat.format("hh:mm", new Date(startTime));
    }
    
    public String getEndTimeFormatted() {
        return "" + DateFormat.format("hh:mm", new Date(endTime));
    }
}
