package com.codigia.euruko2013;

import java.util.Hashtable;
import java.util.List;

import com.codigia.euruko2013.R;

import com.codigia.euruko2013.BaseActivity.GetDrawableHandler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AgendaItemAdapter extends ArrayAdapter<AgendaItem> implements OnItemClickListener {

    static final int resId = R.layout.agenda_item;
    private BaseActivity mBaseActivity;
    private Hashtable<String, Speaker> mSpeakers;
    private List<AgendaItem> mAgendaItems;

    public AgendaItemAdapter(BaseActivity baseActivity, List<Speaker> speakers,
            List<AgendaItem> agendaItems) {
        super(baseActivity, resId, agendaItems);
        mBaseActivity = baseActivity;
        mAgendaItems = agendaItems;

        mSpeakers = new Hashtable<String, Speaker>();
        for (Speaker speaker : speakers) {
            mSpeakers.put(speaker.id, speaker);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    };

    @Override
    public int getItemViewType(int position) {
        return 1;
    };

    @Override
    public int getViewTypeCount() {
        return 1;
    };

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mBaseActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resId, null);
        }

        final AgendaItem r = mAgendaItems.get(position);
        if (r != null) {
            TextView st = (TextView) v.findViewById(R.id.startTime);
            if (st != null) {
                st.setText(r.getStartTimeFormatted());
            }
            TextView td = (TextView) v.findViewById(R.id.day);
            if (td != null) {
                td.setText("day " + r.getDay());
            }
            TextView et = (TextView) v.findViewById(R.id.endTime);
            if (et != null) {
                et.setText(r.getEndTimeFormatted());
            }
            TextView tt = (TextView) v.findViewById(R.id.speechTitle);
            if (tt != null) {
                tt.setText(r.title);
            }
            Speaker speaker = null;

            if (r.speakerId == null) {
                v.findViewById(R.id.speakerContainer).setVisibility(View.GONE);
            } else {
                v.findViewById(R.id.speakerContainer).setVisibility(View.VISIBLE);
                if (mSpeakers.containsKey(r.speakerId)) {
                    speaker = mSpeakers.get(r.speakerId);
                }

                if (speaker != null) {
                    TextView su = (TextView) v.findViewById(R.id.who);
                    if (su != null) {
                        su.setText(speaker.name);
                    }
                    final ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
                    if (profilePic != null) {
                        profilePic.setImageResource(R.drawable.placeholder);
                        speaker.getAvatar(new GetDrawableHandler() {
                            @Override
                            public void handle(Drawable drawable) {
                                profilePic.setImageDrawable(drawable);
                            }

                            @Override
                            public void failed() {
                            }
                        });
                    }
                }
            }
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBaseActivity.viewSpeech(position);
        // Utils.UI.ensureIntoView(parent, position);
    }
}