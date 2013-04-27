package org.hypest.erk13;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsRecordAdapter extends ArrayAdapter<NewsRecord> implements
		OnItemClickListener {

    private BaseActivity mBaseActivity;
    private List<NewsRecord> mRecords;
    private final Locale mLocale;

    public NewsRecordAdapter(BaseActivity baseActivity, int textViewResourceId, List<NewsRecord> records) {
            super(baseActivity, textViewResourceId, records);
            mBaseActivity = baseActivity;
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
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
        View v = convertView;
        if (v == null) {
		LayoutInflater vi = (LayoutInflater) mBaseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        	v.setTag(r.link);
        }
        return v;
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (view.getTag() == null) {
			return;
		}

		mBaseActivity.viewURL((String) view.getTag());
		Utils.UI.ensureIntoView(parent, position);
	}
}