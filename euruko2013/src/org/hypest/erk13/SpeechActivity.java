package org.hypest.erk13;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeechActivity extends BaseActivity {

	private AgendaItem mSpeech;
	private Speaker mSpeaker;

	@Override
	protected int getContentViewId() {
		return R.layout.speech;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int speechId = getIntent().getIntExtra(EXTRA_SPEECH_ID, -1);

        if (speechId == -1 || speechId >= sSpeeches.size()) {
			finish();
			return;
		}

		mSpeech = sSpeeches.get(speechId);

        setBarTitle("Speech");

        TextView startTime = (TextView) findViewById(R.id.startTime);
        if (startTime != null) {
        	startTime.setText(mSpeech.getStartTimeFormatted());
        }

        TextView speechTitle = (TextView) findViewById(R.id.speechTitle);
        if (speechTitle != null) {
        	speechTitle.setText(mSpeech.title);
        }

        TextView speechDescription = (TextView) findViewById(R.id.speechDescription);
        if (speechDescription != null) {
        	speechDescription.setText(Html.fromHtml(mSpeech.descr));
        }

        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
        if (profilePic != null) {
        	mSpeaker = null;
        	for (Speaker s : sSpeakers) {
        		if (s.id.equals(mSpeech.who)) {
        			mSpeaker = s;
        			break;
        		}
        	}

        	if (mSpeaker != null) {
        		profilePic.setImageDrawable(mSpeaker.avatar);
        	}
        }
	}

    public static void viewSpeech(Activity activity, int id) {
    	Intent intent = new Intent(activity, SpeechActivity.class);
    	intent.putExtra(EXTRA_SPEECH_ID, id);
    	activity.startActivityForResult(intent, REQUEST_NAVIGATE);
    }

    public void gotoSpeaker(View v) {
    	if (mSpeech == null || mSpeaker == null) {
    		return;
    	}

    	SpeakerActivity.viewSpeaker(this, mSpeaker.id);
    }
}