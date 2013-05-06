package com.codigia.euruko2013;

import com.codigia.euruko2013.R;

import com.codigia.euruko2013.BaseActivity.GetDrawableHandler;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeechFragment extends BaseFragment {

	@Override
	protected int getLayout() {
		return R.layout.speech;
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

        final ImageView profilePic = (ImageView) v.findViewById(R.id.profilePic);
        if (profilePic != null) {
    		View profileContainer = v.findViewById(R.id.profileContainer);

    		Speaker speaker = null;
        	for (Speaker s : BaseActivity.sSpeakers) {
        		if (s.id.equals(mSpeech.speakerId)) {
        			speaker = s;
        			break;
        		}
        	}

        	if (speaker != null) {
        		profileContainer.setVisibility(View.VISIBLE);

        		speaker.getAvatar(new GetDrawableHandler() {
					@Override
					public void handle(Drawable drawable) {
		        		profilePic.setImageDrawable(drawable);
					}
					
					@Override
					public void failed() {
					}
				});

        		final String sid = speaker.id;
        		profilePic.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ba.viewSpeaker(sid);
					}
				});
        	} else {
        		profileContainer.setVisibility(View.INVISIBLE);
        	}
        }

        
        super.onResume();
	}
}