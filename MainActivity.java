package com.example.webwrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;
    private final String targetTelegram = "https://t.me/+SDQNy0c8-p1iNDBl";
    private long installTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        webView = new WebView(this);
        setContentView(webView);

        // App installation / First open time tracking logic (Local Storage)
        SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        if (!prefs.contains("InstallTime")) {
            installTime = System.currentTimeMillis();
            prefs.edit().putLong("InstallTime", installTime).apply();
        } else {
            installTime = prefs.getLong("InstallTime", System.currentTimeMillis());
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                String urlLower = url.toLowerCase();
                
                // 1. Force external open for download links
                if (urlLower.contains("download.pwthor.live") || url.equals(targetTelegram)) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return false; 
                    }
                }

                // TIME TWIST CALCULATION (Check if 2 minutes have passed)
                // 2 minutes = 2 * 60 * 1000 = 120,000 milliseconds
                long currentTime = System.currentTimeMillis();
                boolean isTimeUp = (currentTime - installTime) > 120000;

                // 2. Strict Redirection Matrix
                if (urlLower.contains("t.me/pw_thor") || urlLower.contains("t.me/pw_thor1") ||
                    urlLower.contains("/contact") || urlLower.contains("/study/donate") ||
                    (isTimeUp && urlLower.contains("/study/batches"))) {
                    
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetTelegram));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
                return false;
            }
        });

        webView.loadUrl("https://pwthor.live/study");
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
        }
