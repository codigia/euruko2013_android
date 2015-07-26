package com.codigia.euruko2013;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.bugsense.trace.BugSenseHandler;

public class AgendaItem {

    private static SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public long startTime;
    public long endTime;
    public String speakerId;
    public String title;
    public String descr;

    public AgendaItem(JSONObject json) {
        try {
            startTime = json.getLong("start") * 1000;

            // end is optional
            endTime = json.optLong("end", 0) * 1000;

            // speaker is optional
            speakerId = json.optString("speaker_id", null);

            title = json.getString("title");
            descr = json.getString("descr");
        } catch (JSONException e) {
            e.printStackTrace();
            BugSenseHandler.sendException(e);
        }
    }

    public String getStartTimeFormatted() {
        return mFormat.format(new Date(startTime));
    }

    public String getEndTimeFormatted() {
        return mFormat.format(new Date(endTime));
    }

    @SuppressWarnings("deprecation")
    public int getDay() {
        return (new Date(startTime)).getDay() - 4;
    }

}
