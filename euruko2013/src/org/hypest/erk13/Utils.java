package org.hypest.erk13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ListView;

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

		public static String readAsset(String filename) {
	        InputStream is = BaseActivity.class.getClassLoader()
	                .getResourceAsStream("assets/" + filename);
	        Writer writer = new StringWriter();
	        char[] buffer = new char[1024];
	        try {
	            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	            int n;
	            while ((n = reader.read(buffer)) != -1) {
	                writer.write(buffer, 0, n);
	            }
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        return writer.toString();
	    }
	}

	public static class UI {
		public static void ensureIntoView(final ListView listView,
				final int position) {
			listView.smoothScrollToPosition(position, position);
			listView.post(new Runnable() {
				@Override
				public void run() {
					listView.smoothScrollToPosition(position, position + 1);
				}
			});
		}
	}
}