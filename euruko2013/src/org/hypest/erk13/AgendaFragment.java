package org.hypest.erk13;

import android.os.Bundle;

public class AgendaFragment extends BaseListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		networkRefresh();
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Agenda");

        super.onResume();
	}

	@Override
	public void networkRefresh() {
		AgendaItemAdapter agendaAdapter = new AgendaItemAdapter(
				(BaseActivity) getActivity(), BaseActivity.sSpeakers,
				BaseActivity.sSpeeches);
        setListAdapter(agendaAdapter, agendaAdapter);
	}
}
