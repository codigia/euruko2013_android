package com.codigia.euruko2013;

import com.codigia.euruko2013.R;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class AboutFragment extends BaseFragment {

    @Override
    public int getLayout() {
        return R.layout.about;
    }

    @Override
    public void onResume() {
        final BaseActivity ba = (BaseActivity) getActivity();

        ba.setBarTitle("About");

        TextView aboutTextView = (TextView) getView().findViewById(R.id.aboutEuruko);
        aboutTextView.setText(Html.fromHtml(getString(R.string.about)), BufferType.SPANNABLE);
        aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());

        super.onResume();
    }

}
