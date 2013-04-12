package org.hypest.erk13;

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

        int speakerIndex = getIntent().getIntExtra(EXTRA_SPEAKER_ID, -1);
		
		if (speakerIndex < 0) {
			finish();
			return;
		}

		Speaker r = sSpeakers.get(speakerIndex);

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
            profilePic.setImageResource(r.avatarId);
        }
	}
}
