package com.test.rssfeeder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by nishant- on 18-11-2015.
 */
public class RssWebView extends AppCompatActivity {
    WebView mWebView;
    ProgressBar mProgressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rsswebview);
        mWebView = (WebView) findViewById(R.id.rssWebView);
        mProgressbar = (ProgressBar) findViewById(R.id.webViewProgress);
        Intent i = getIntent();
        String htmlLink = i.getStringExtra("LINK");
        mWebView.loadUrl(htmlLink);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressbar.setVisibility(View.GONE);
            }
        });


    }
}
