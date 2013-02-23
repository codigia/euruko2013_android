package org.hypest.erk13;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsRecordAdapter extends ArrayAdapter<NewsRecord> {

    private Context mContext;
    private List<NewsRecord> mRecords;

    public NewsRecordAdapter(Context context, int textViewResourceId, List<NewsRecord> records) {
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
                v = vi.inflate(R.layout.newsitem, null);
            }
            
            NewsRecord r = mRecords.get(position);
            if (r != null) {
                TextView tt = (TextView) v.findViewById(R.id.title);
                TextView su = (TextView) v.findViewById(R.id.summary);
                if (tt != null) {
                      tt.setText(r.title);
                }
                if(su != null){
                      su.setText(r.body);
                }

                ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
                if (profilePic != null) {
                    profilePic.setImageResource(R.drawable.news);
                }
           }
            return v;
    }
}