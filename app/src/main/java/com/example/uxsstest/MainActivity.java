package com.example.uxsstest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends Activity {
    private NanoHTTPD webserver;
    private ListView listView;
    private Button autotest;
    public static ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            copyFilesFassets(this,"POC",getFilesDir().getAbsolutePath()+"/UXSSTest/");
            webserver= new NanoHTTPD(40310,new File(getFilesDir().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        autotest=(Button)findViewById(R.id.autotest);
        listView=(ListView)findViewById(R.id.listview);
        list=getList();
        Mybaseadapter mybaseadapter=new Mybaseadapter(this,list);
        listView.setAdapter(mybaseadapter);
        //final WebView webView=new WebView(this);
        //webView.getSettings().setAllowFileAccess(true);
        //webView.getSettings().setJavaScriptEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url="http://127.0.0.1:40310/UXSSTest/"+list.get(position).get("url").toString();
                Log.d("URL地址",url);
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url=Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        autotest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AutoTestActivity.class);
                startActivity(intent);
            }
        });

    }
    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
        }
    }
    //从config.txt文件中读取漏洞列表；以Json格式解析
    private ArrayList<Map<String,Object>> getList(){
        list.clear();
        JSONObject object=new JSONObject();
        StringBuilder stringBuilder=new StringBuilder();
        File file =new File(getFilesDir().getAbsolutePath()+"/UXSSTest/config.txt");
        try {
            InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject=new JSONObject(String.valueOf(stringBuilder));
            JSONArray jsonArray=jsonObject.getJSONArray("cvelist");
            for(int i=0;i<jsonArray.length();i++){
                object=jsonArray.getJSONObject(i);
                Map<String,Object> map=new HashMap<String,Object>();
                String cveid=object.getString("cveid");
                String description=object.getString("description");
                String url=object.getString("url");
                map.put("cveid",cveid);
                map.put("description",description);
                map.put("url",url);
                list.add(map);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
