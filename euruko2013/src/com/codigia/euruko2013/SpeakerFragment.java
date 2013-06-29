package com.codigia.euruko2013;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
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

        TextView speakerGithub = (TextView) v.findViewById(R.id.speakerGithub);
        if (speakerGithub != null) {
			if (r.githubIdString != null
					&& r.githubIdString.trim().length() > 0) {
				speakerGithub.setVisibility(View.VISIBLE);
				final Speaker sp = r;
	        	speakerGithub.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
				        Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(getString(R.string.url_github)
								+ sp.githubIdString));
				        startActivity(i);
					}
				});
	        } else {
	        	speakerGithub.setVisibility(View.GONE);
	        	speakerGithub.setOnClickListener(null);
	        }
        }

        TextView speakerTwitter = (TextView) v.findViewById(R.id.speakerTwitter);
        if (speakerTwitter != null) {
			if (r.twitterIdString != null
					&& r.twitterIdString.trim().length() > 0) {
				speakerTwitter.setVisibility(View.VISIBLE);
				final Speaker sp = r;
	        	speakerTwitter.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
				        Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(getString(R.string.url_twitter)
								+ sp.twitterIdString));
				        startActivity(i);
					}
				});
	        } else {
				speakerTwitter.setVisibility(View.GONE);
	        	speakerTwitter.setOnClickListener(null);
	        }
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