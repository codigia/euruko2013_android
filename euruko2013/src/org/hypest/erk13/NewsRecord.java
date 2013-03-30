package org.hypest.erk13;

import org.json.JSONObject;

public class NewsRecord {
    public long time;
    public String title;
    public String body;
    public String link;
    
    public NewsRecord(JSONObject json) {
        time = Utils.JSON.getLong(json, "time") * 1000;
        title = Utils.JSON.getString(json, "title");
        body = Utils.JSON.getString(json, "body");
        link = Utils.JSON.getString(json, "link");
    }
}
