package org.hypest.erk13;

import android.os.Bundle;

public class NewsFragment extends BaseListFragment {

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
		NewsRecordAdapter newsadapter = new NewsRecordAdapter(
				(BaseActivity) getActivity(), R.layout.newsitem,
				BaseActivity.sNews);
        setListAdapter(newsadapter, newsadapter);
        newsadapter.notifyDataSetChanged();
	}
}
