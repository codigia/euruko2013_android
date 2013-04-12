package org.hypest.erk13;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

	public static class JSON {
		public static Long getLong(JSONObject json, String name) {
			try {
				if (json.has(name)) return (Long) json.getLong(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static String getString(JSONObject json, String name) {
			try {
				if (json.has(name)) return json.getString(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
	}
}
