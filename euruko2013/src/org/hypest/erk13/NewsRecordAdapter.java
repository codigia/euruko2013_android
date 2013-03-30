package org.hypest.erk13;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsRecordAdapter extends ArrayAdapter<NewsRecord> {

    private Context mContext;
    private List<NewsRecord> mRecords;
    private final Locale mLocale;

    public NewsRecordAdapter(Context context, int textViewResourceId, List<NewsRecord> records) {
            super(context, textViewResourceId, records);
            mContext = context;
            mRecords = records;
            mLocale = Locale.getDefault();
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
            
            final NewsRecord r = mRecords.get(position);
            if (r != null) {
            	Calendar cal = Calendar.getInstance(mLocale);
            	cal.setTimeInMillis(r.time);

            	TextView dt = (TextView) v.findViewById(R.id.dayNumber);
            	dt.setText(String.format(mLocale, "%td", cal));

                TextView mt = (TextView) v.findViewById(R.id.monthText);
            	mt.setText(String.format(mLocale, "%tb", cal));

                TextView tt = (TextView) v.findViewById(R.id.title);
                TextView su = (TextView) v.findViewById(R.id.summary);
                if (tt != null) {
                      tt.setText(r.title);
                }
                if(su != null){
                	if (r.body != null) {
                		su.setText(Html.fromHtml(r.body)); // make the links look like ones, but not actually clickable
//                		su.setMovementMethod(LinkMovementMethod.getInstance());
                	} else {
                		su.setText("");
                		su.setVisibility(View.GONE);
                	}
                }

                ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
                if (profilePic != null) {
                    profilePic.setImageResource(R.drawable.news);
                }

                if (r.link != null) {
                	v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(r.link));
							mContext.startActivity(browserIntent);
						}
					});
                }
           }
            return v;
    }
}