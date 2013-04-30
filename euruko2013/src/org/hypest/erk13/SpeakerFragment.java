package org.hypest.erk13;

import org.hypest.erk13.BaseActivity.GetDrawableHandler;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeakerFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.speaker_profile, null);
	}

	@Override
	public void onResume() {
        super.onResume();

        final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Speech");

		Speaker r = null;
		for (Speaker s : BaseActivity.sSpeakers) {
			if (s.id.equals(ba.currentSpeakerId)) {
				r = s;
				break;
			}
		}
		
		if (r == null) {
			return;
		}

		ba.setBarTitle(r.name);

		View v = getView();

        TextView speakerTitle = (TextView) v.findViewById(R.id.speakerTitle);
        if (speakerTitle != null) {
        	speakerTitle.setText(r.title);
        }

        TextView speakerBio = (TextView) v.findViewById(R.id.speakerBio);
        if (speakerBio != null) {
        	speakerBio.setText(r.bio);
        }

        final ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
        if (profilePic != null) {
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
	}
}