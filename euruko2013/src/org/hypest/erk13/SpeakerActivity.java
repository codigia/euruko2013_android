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

        TextView name = (TextView) findViewById(R.id.name);
        if(name != null){
              name.setText(r.name);
        }

        TextView title = (TextView) findViewById(R.id.title);
        if (title != null) {
              title.setText(r.title);
        }

        TextView bio = (TextView) findViewById(R.id.bio);
        if (bio != null) {
              bio.setText(r.bio);
        }

        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
        if (profilePic != null) {
            profilePic.setImageResource(r.avatarId);
        }
	}
}
