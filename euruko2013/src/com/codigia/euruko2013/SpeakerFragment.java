package com.codigia.euruko2013;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codigia.euruko2013.BaseActivity.GetDrawableHandler;

public class SpeakerFragment extends BaseFragment {

	@Override
	protected int getLayout() {
		return R.layout.speaker_profile;
	}

	@Override
	public void onResume() {
        super.onResume();

        final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("Speaker");

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
        	speakerBio.setText(Html.fromHtml(r.bio));
    		speakerBio.setMovementMethod(LinkMovementMethod.getInstance());
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