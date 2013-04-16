package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class NewsFragment extends BaseListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        List<NewsRecord> news = getNewsItems();
        NewsRecordAdapter newsadapter = new NewsRecordAdapter(getActivity(),
                R.layout.newsitem,
                news);
        setListAdapter(newsadapter);
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("News");

        super.onResume();
	}

    protected List<NewsRecord> getNewsItems() {
        ArrayList<NewsRecord> news = new ArrayList<NewsRecord>();
        try {
			JSONObject newsJSONObject = new JSONObject(Utils.JSON.readAsset(
					getActivity(), R.raw.news));
            JSONArray newsJSON = newsJSONObject.getJSONArray("news");
            for (int k = 0; k < newsJSON.length(); k++) {
                NewsRecord item = new NewsRecord(newsJSON.getJSONObject(k));
                news.add(item);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return news;
    }
}