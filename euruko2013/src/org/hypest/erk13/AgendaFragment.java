package org.hypest.erk13;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class AgendaFragment extends BaseListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        List<AgendaItem> agenda = getAgendaItems();
		AgendaItemAdapter agendaAdapter = new AgendaItemAdapter(
				(BaseActivity) getActivity(), BaseActivity.sSpeakers, agenda);
        setListAdapter(agendaAdapter);
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Agenda");

        super.onResume();
	}

    private List<AgendaItem> getAgendaItems() {
    	BaseActivity.sSpeeches = new ArrayList<AgendaItem>();
        try {
			JSONObject agendaJSONObject = new JSONObject(Utils.JSON.readAsset(
					getActivity(), R.raw.agenda));
            JSONArray agendaJSON = agendaJSONObject.getJSONArray("agenda");
            for (int k = 0; k < agendaJSON.length(); k++) {
                AgendaItem item = new AgendaItem(agendaJSON.getJSONObject(k));
                BaseActivity.sSpeeches.add(item);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return BaseActivity.sSpeeches;
    }
}