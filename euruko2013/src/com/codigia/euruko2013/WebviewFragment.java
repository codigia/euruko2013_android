package com.codigia.euruko2013;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewFragment extends BaseFragment {

    WebView mWebView;

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWebView = new WebView(getActivity());

        return mWebView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        final BaseActivity ba = (BaseActivity) getActivity();

        ba.setBarTitle("Webview");

        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript: var navbar = document.getElementById('navbar-main'); navbar.style.display='none';");
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl(ba.currentURLString);

        super.onResume();
    }
}