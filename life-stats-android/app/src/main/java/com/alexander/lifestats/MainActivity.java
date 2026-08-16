package com.alexander.lifestats;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new StateBridge(this), "AndroidStorage");
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public static class StateBridge {
        private final SharedPreferences prefs;

        StateBridge(Context context) {
            prefs = context.getSharedPreferences("life_stats_state", Context.MODE_PRIVATE);
        }

        @JavascriptInterface
        public String getState() {
            return prefs.getString("state", "");
        }

        @JavascriptInterface
        public void saveState(String json) {
            prefs.edit().putString("state", json).apply();
        }

        @JavascriptInterface
        public void clearState() {
            prefs.edit().clear().apply();
        }
    }
}
