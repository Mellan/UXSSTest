package com.example.uxsstest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Map;

import static com.example.uxsstest.MainActivity.list;

public class AutoTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_test);

        AutoTest();
        //webView.loadUrl("http://127.0.0.1:40310/UXSSTest/cve_2015_6764.html");
    }
    public void AutoTest(){
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        for (int i=0;i<list.size();i++){
            Log.d("list",(String)list.get(i).get("url"));
            String url="file:///android_asset/POC/"+(String)list.get(i).get("url");
            webView.loadUrl(url);
            webView.destroy();
            //webView.loadUrl("file:///android_asset/POC/cve_2015_6764.html");

        }
    }

    private class DefualtWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String message = consoleMessage.message();
            int lineNumber = consoleMessage.lineNumber();
            String sourceID = consoleMessage.sourceId();
            String messageLevel = consoleMessage.message();

            Log.e("[WebView]", String.format("[%s] sourceID: %s lineNumber: %n message: %s",
                    messageLevel, sourceID, lineNumber, message));

            return super.onConsoleMessage(consoleMessage);
        }
    }
}
