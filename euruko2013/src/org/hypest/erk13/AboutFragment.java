package org.hypest.erk13;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class AboutFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.about, null);
	}

	@Override
	public void onResume() {
		final BaseActivity ba = (BaseActivity) getActivity();

		ba.setBarTitle("About");

		TextView aboutTextView = (TextView) getView().findViewById(
				R.id.aboutEuruko);
		aboutTextView.setText(Html.fromHtml(getString(R.string.about)),
				BufferType.SPANNABLE);
        aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());

        super.onResume();
	}

}
