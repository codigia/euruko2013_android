package org.hypest.erk13;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

	public static class JSON {
		public static Long getLong(JSONObject json, String name) {
			Long num = null;
			try {
				num = json.getLong(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return num;
		}

		public static String getString(JSONObject json, String name) {
			String string = null;
			try {
				string = json.getString(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return string;
		}
	}
}
