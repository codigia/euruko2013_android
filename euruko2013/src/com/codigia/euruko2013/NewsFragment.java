package com.codigia.euruko2013;

import com.codigia.euruko2013.R;

import android.os.Bundle;

public class NewsFragment extends BaseListFragment {

	public NewsFragment() {
		super(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		networkRefresh();
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("News");

        super.onResume();
	}

	@Override
	public void networkRefresh() {
		BaseActivity baseActivity = (BaseActivity) getActivity();
		if (baseActivity == null) {
			return;
		}

		if (BaseActivity.sNews == null) {
			return;
		}

		NewsRecordAdapter newsadapter = new NewsRecordAdapter(
				baseActivity, R.layout.newsitem,
				BaseActivity.sNews);
        setListAdapter(newsadapter, newsadapter);
        newsadapter.notifyDataSetChanged();
	}

	@Override
	protected void onMenuRefresh() {
		BaseActivity baseActivity = (BaseActivity) getActivity();
		if (baseActivity == null) {
			return;
		}

    	baseActivity.getNews();
	}
}
