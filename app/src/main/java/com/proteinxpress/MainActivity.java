package com.proteinxpress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity {
    private static final String websiteURL = "https://proteinxpress.com";
    WebView myWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        myWebView = new WebView(this);
        setContentView(myWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(websiteURL);
        myWebView.setBackgroundColor(getResources().getColor(R.color.black));

        myWebView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
