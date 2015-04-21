package boom.boom.shangchuandengdai;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.FileUploadAsyncTask;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Shangchuandengdai_activity extends Activity {
    private Button shangchuandengdaifanhui;
    private Button fangqishangchuang;
    private Button tanchuang1queding;
    private Button tanchuang2quxiao;
    private boom.boom.myview.ProgressBar progressBar;
    private Integer progress;
    private int int_progress;
    private Boolean upload_onComplete;
//    private int b=0;
    private TextView scdd_jindu;
    private FileUploadAsyncTask ftask;
    private File upload_file;
    private long file_length;

    Handler myMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scdd_jindu.setText(Integer.valueOf((int) (100*(int_progress/file_length)))+"%");
        }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangchuandengdai);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        initEvent();
        final String path = getIntent().getStringExtra("file_path");
        shangchuandengdaifanhui = (Button) findViewById(R.id.shangchuandengdaifanhui);
        progressBar = (boom.boom.myview.ProgressBar) findViewById(R.id.progress123);
        fangqishangchuang = (Button) findViewById(R.id.fangqishangchuan);
        scdd_jindu = (TextView) findViewById(R.id.scdd_jindu);
        progress = new Integer("0");
        upload_onComplete = new Boolean(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + Utils.put_file_api);
                    get.addItem("s_id", Static.session_id);
                    ftask = new FileUploadAsyncTask(getApplicationContext(), progress, upload_onComplete);
                    ftask.addGetParameters(Utils.GetBuilder.Item("type", "video"));
                    upload_file = new File(path);
                    file_length = upload_file.length();
                    ftask.doInBackground(upload_file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override

            public void run() {
                Log.d("UPLOAD", "Awaiting ftask instantiable");
                while (ftask == null);
                Log.d("UPLOAD", "Ok, now set progress from another thread.");
                for (; !ftask.getState(); int_progress = ftask.getProgress()) {
                    try {
                        Log.d("UPLOAD", "======= TEST: int_progress = "+int_progress+" \tprogress = "+ progress +" ===========");
                        progressBar.setProgress(Integer.valueOf((int) (100*(int_progress/file_length))));
                        Message m = new Message();
                        m.what = 1;
                        Shangchuandengdai_activity.this.myMessageHandler.sendMessage(m);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("UPLOAD", "Upload complete. Sent "+ int_progress +" bytes to the goddamn server.");
            }
        }).start();
        shangchuandengdaifanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Shangchuandengdai_activity.this, Paishetiaozhan_activity.class);
                startActivity(intent);
            }
        });

    }

    private void initEvent() {
        findViewById(R.id.fangqishangchuan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private  void showDialog1(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.mbox_yesno,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


           }

