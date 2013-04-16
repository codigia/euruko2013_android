package org.hypest.erk13;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeechFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.speech, null);
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Speech");

		int speechId = ba.currentSpeechPosition;

		if (speechId == -1 || speechId >= BaseActivity.sSpeeches.size()) {
			return;
		}

		AgendaItem mSpeech = BaseActivity.sSpeeches.get(speechId);

//        setBarTitle("Speech");

		View v = getView();

        TextView startTime = (TextView) v.findViewById(R.id.startTime);
        if (startTime != null) {
        	startTime.setText(mSpeech.getStartTimeFormatted());
        }

        TextView speechTitle = (TextView) v.findViewById(R.id.speechTitle);
        if (speechTitle != null) {
        	speechTitle.setText(mSpeech.title);
        }

        TextView speechDescription = (TextView) v.findViewById(R.id.speechDescription);
        if (speechDescription != null) {
        	speechDescription.setText(Html.fromHtml(mSpeech.descr));
        }

        ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
        if (profilePic != null) {
        	Speaker speaker = null;
        	for (Speaker s : BaseActivity.sSpeakers) {
        		if (s.id.equals(mSpeech.who)) {
        			speaker = s;
        			break;
        		}
        	}

        	if (speaker != null) {
        		profilePic.setImageDrawable(speaker.avatar);

        		final String sid = speaker.id;
        		profilePic.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ba.viewSpeaker(sid);
					}
				});
        	}
        }

        
        super.onResume();
	}
}