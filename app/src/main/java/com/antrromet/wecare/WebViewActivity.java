package com.antrromet.wecare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Class to display the web links within the application
 *
 * @author antriksh
 */
public class WebViewActivity extends AppCompatActivity {

    private ProgressBar mWebViewProgressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        String url = getIntent().getStringExtra(Constants.ParamsKeys.URL.key);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar_layout);
        toolbar.setTitle(getIntent().getStringExtra(Constants.ParamsKeys.TITLE.key));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mWebViewProgressBar = (ProgressBar) findViewById(R.id.webViewProgress);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);

        // Enable default zoom in Android webview
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);

        // Show the error (if any) while loading the web page
        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        // Show the progress of the page
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mWebViewProgressBar.setProgress(newProgress);
                mWebViewProgressBar.bringToFront();
                if (newProgress == 100) {
                    mWebViewProgressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().setBackgroundDrawable(null);
    }

}
