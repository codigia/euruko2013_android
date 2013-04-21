package org.hypest.erk13;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;

public class AgendaItem {

    public long startTime;
    public long endTime;
    public String speakerId;
    public String title;
    public String descr;
    
	public AgendaItem(JSONObject json) {
        try {
            startTime = json.getLong("start");
            endTime = json.getLong("end");

            // speaker is optional
            speakerId = json.optString("speaker_id", null);

            title = json.getString("title");
            descr = json.getString("descr");
        } catch (JSONException e) {
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
