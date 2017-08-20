package com.example.uxsstest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Map;

import static com.example.uxsstest.MainActivity.list;

public class AutoTestActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_test);
        textView=(TextView)findViewById(R.id.result);
        AutoTest();
        //webView.loadUrl("http://127.0.0.1:40310/UXSSTest/cve_2015_6764.html");
    }
    public void AutoTest(){
        for (int i=0;i<list.size();i++){
            WebView webView = new WebView(this);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new DefualtWebChromeClient());
            Log.d("list",(String)list.get(i).get("url"));
            String url="file:///android_asset/POC/AutoTest/"+(String)list.get(i).get("url");
            webView.loadUrl(url);
        }
    }

    private class DefualtWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String message = consoleMessage.message().trim();
            String source=consoleMessage.sourceId();
            String html=source.substring(source.lastIndexOf("/")+1,source.length()-5);

            Log.d("console",html);
            String result="";
            if(message.indexOf("Uncaught SyntaxError",0)!=-1){
                result="safe";
            }
            else {
                int a=message.indexOf("result=",0);
                if(a!=-1){
                    result=message.substring(7);
                }else
                    return super.onConsoleMessage(consoleMessage);
            }
            Log.d("result",result);
            textView.append(html+" is "+result+"\n");
            return super.onConsoleMessage(consoleMessage);
        }
    }
}
