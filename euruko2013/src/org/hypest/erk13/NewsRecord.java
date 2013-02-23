package org.hypest.erk13;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsRecord {
    public long time;
    public String title;
    public String body;
    public String link;
    
    public NewsRecord(JSONObject json) {
        try {
            time = json.getLong("time");
            title = json.getString("title");
            body = json.getString("body");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
