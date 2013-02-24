package org.hypest.erk13;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeakerAdapter extends ArrayAdapter<Speaker> {

    private Context mContext;
    private List<Speaker> mRecords;

    public SpeakerAdapter(Context context, int textViewResourceId, List<Speaker> records) {
            super(context, textViewResourceId, records);
            mContext = context;
            mRecords = records;
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
                v = vi.inflate(R.layout.speakersitem, null);
            }
            
            Speaker r = mRecords.get(position);
            if (r != null) {
                TextView name = (TextView) v.findViewById(R.id.name);
                if(name != null){
                      name.setText(r.name);
                }

                TextView title = (TextView) v.findViewById(R.id.title);
                if (title != null) {
                      title.setText(r.title);
                }

                ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
                if (profilePic != null) {
                    profilePic.setImageResource(r.avatarId);
                }
           }
            return v;
    }
}