package org.hypest.erk13;

import android.os.Bundle;

public class SpeakersFragment extends BaseListFragment {

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