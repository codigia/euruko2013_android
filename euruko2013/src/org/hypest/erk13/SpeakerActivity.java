package org.hypest.erk13;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeakerActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.speaker_profile;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String speakerId = getIntent().getStringExtra(EXTRA_SPEAKER_ID);
		
		if (speakerId == null) {
			finish();
			return;
		}

		Speaker r = null;
		for (Speaker s : sSpeakers) {
			if (s.id.equals(speakerId)) {
				r = s;
				break;
			}
		}
		
		if (r == null) {
			finish();
			return;
		}

        setBarTitle(r.name);

        TextView speakerTitle = (TextView) findViewById(R.id.speakerTitle);
        if (speakerTitle != null) {
        	speakerTitle.setText(r.title);
        }

        TextView speakerBio = (TextView) findViewById(R.id.speakerBio);
        if (speakerBio != null) {
        	speakerBio.setText(r.bio);
        }

        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
        if (profilePic != null) {
            profilePic.setImageDrawable(r.avatar);
        }
	}

    public static void viewSpeaker(Activity activity, String id) {
    	Intent intent = new Intent(activity, SpeakerActivity.class);
    	intent.putExtra(EXTRA_SPEAKER_ID, id);
    	activity.startActivityForResult(intent, REQUEST_NAVIGATE);
    }
}
