package org.hypest.erk13;

import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AgendaItemAdapter extends ArrayAdapter<AgendaItem> {

    static final int resId = R.layout.agenda_item;
    private Activity mActivity;
    private Hashtable<String, Speaker> mSpeakers;
    private List<AgendaItem> mAgendaItems;

    public AgendaItemAdapter(Activity activity,
            List<Speaker> speakers, List<AgendaItem> agendaItems) {
        super(activity, resId, agendaItems);
        mActivity = activity;
        mAgendaItems = agendaItems;

        mSpeakers = new Hashtable<String, Speaker>();
        for(Speaker speaker : speakers) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(resId, null);
            }
            
            final AgendaItem r = mAgendaItems.get(position);
            if (r != null) {
                TextView st = (TextView) v.findViewById(R.id.startTime);
                if (st != null) {
                      st.setText(r.getStartTimeFormatted());
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
                if (mSpeakers.containsKey(r.who)) {
                    speaker = mSpeakers.get(r.who);

                    final String speakerId = speaker.id;

					View speakerContainer = v
							.findViewById(R.id.speakerContainer);
					if (speakerContainer != null) {
						speakerContainer
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										SpeakerActivity.viewSpeaker(mActivity,
												speakerId);
									}
								});
					}
                }

                if (speaker != null) {
                    TextView su = (TextView) v.findViewById(R.id.who);
                    if (su != null) {
                        su.setText(speaker.name);
                    }
                    ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
                    if (profilePic != null) {
                        profilePic.setImageDrawable(speaker.avatar);
                    }
                }
            }
            return v;
    }
}