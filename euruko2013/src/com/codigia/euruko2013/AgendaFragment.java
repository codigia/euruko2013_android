package com.codigia.euruko2013;

import android.os.Bundle;

public class AgendaFragment extends BaseListFragment {

    public AgendaFragment() {
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

        ba.setBarTitle("Agenda");

        super.onResume();
    }

    @Override
    public void networkRefresh() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity == null) {
            return;
        }

        if (BaseActivity.sSpeeches == null) {
            return;
        }

        AgendaItemAdapter agendaAdapter = new AgendaItemAdapter(baseActivity,
                BaseActivity.sSpeakers, BaseActivity.sSpeeches);
        setListAdapter(agendaAdapter, agendaAdapter);
    }
}
