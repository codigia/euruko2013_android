package com.codigia.euruko2013;

import com.codigia.euruko2013.R;

import android.os.Bundle;

public class SpeakersFragment extends BaseListFragment {

	public SpeakersFragment() {
		super(false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		networkRefresh();
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Speakers");

        super.onResume();
	}

	@Override
	public void networkRefresh() {
		SpeakerAdapter speakersadapter = new SpeakerAdapter(
				(BaseActivity) getActivity(), R.layout.speakersitem,
				BaseActivity.sSpeakers);
        setListAdapter(speakersadapter, speakersadapter);
	}
}