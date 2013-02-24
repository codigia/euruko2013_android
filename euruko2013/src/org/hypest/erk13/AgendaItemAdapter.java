package org.hypest.erk13;

import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AgendaItemAdapter extends ArrayAdapter<AgendaItem> {

    static final int resId = R.layout.agenda_item;
    private Context mContext;
    private Hashtable<String, Speaker> mSpeakers;
    private List<AgendaItem> mAgendaItems;

    public AgendaItemAdapter(Context context,
            List<Speaker> speakers, List<AgendaItem> agendaItems) {
        super(context, resId, agendaItems);
        mContext = context;
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
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                TextView tt = (TextView) v.findViewById(R.id.title);
                if (tt != null) {
                      tt.setText(r.title);
                }
                Speaker speaker = null;
                if (mSpeakers.containsKey(r.who)) {
                    speaker = mSpeakers.get(r.who);
                }
                if (speaker != null) {
                    TextView su = (TextView) v.findViewById(R.id.who);
                    if (su != null) {
                        su.setText(speaker.name);
                    }
                    ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
                    if (profilePic != null) {
                        profilePic.setImageResource(speaker.avatarId);
                        
    //                    profilePic.setOnClickListener(new OnClickListener() {
    //                        @Override
    //                        public void onClick(View v) {
    //                            Intent i = new Intent(Intent.ACTION_VIEW);
    //                            i.setData(Uri.parse("http://twitter.com/" + r.tweet.getFromUser()));
    //                            mContext.startActivity(i);
    //                        }
    //                    });
                    }
                }

                ImageView topicPic = (ImageView) v.findViewById(R.id.topicPic);
                topicPic.setImageDrawable(r.icon);
           }
            return v;
    }
}