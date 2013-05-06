package com.codigia.euruko2013;

import java.util.List;

import com.codigia.euruko2013.R;

import com.codigia.euruko2013.BaseActivity.GetDrawableHandler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SpeakerAdapter extends ArrayAdapter<Speaker> implements
		OnItemClickListener {

    private BaseActivity mBaseActivity;
    private List<Speaker> mRecords;

    public SpeakerAdapter(BaseActivity baseActivity, int textViewResourceId, List<Speaker> records) {
            super(baseActivity, textViewResourceId, records);
            mBaseActivity = baseActivity;
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
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
        View v = convertView;
        if (v == null) {
		LayoutInflater vi = (LayoutInflater) mBaseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.speakersitem, null);
        }
        
        final Speaker r = mRecords.get(position);
        if (r != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            if(name != null){
                  name.setText(r.name);
            }

            TextView title = (TextView) v.findViewById(R.id.title);
            if (title != null) {
                  title.setText(r.title);
            }

			final ImageView profilePic = (ImageView) v
					.findViewById(R.id.profilePic);
            if (profilePic != null) {
                profilePic.setImageResource(R.drawable.placeholder);
				r.getAvatar(new GetDrawableHandler() {
					@Override
					public void handle(Drawable drawable) {
						profilePic.setImageDrawable(drawable);
					}

					@Override
					public void failed() {
					}
				});
            }

            v.setTag(r.id);
       }
        return v;
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (view.getTag() == null) {
			return;
		}

		mBaseActivity.viewSpeaker((String) view.getTag());
		Utils.UI.ensureIntoView(parent, position);
	}
}