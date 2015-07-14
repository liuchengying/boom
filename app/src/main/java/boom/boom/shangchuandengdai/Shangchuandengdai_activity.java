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

import org.json.JSONObject;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.HttpIO;
import boom.boom.api.ProgressListener;
import boom.boom.api.Static;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
import boom.boom.api.uploadFile;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;
import boom.boom.shangchuanchenggong.Shangchuanchenggong1_activity;

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
    private String path;
    private String cl_name;
    private int elapsed;
    private String cl_id;
    private int pf_iv;
//    private int int_progress;
//    private Boolean upload_onComplete;
//    private int b=0;
    private TextView scdd_jindu;
//    private FileUploadAsyncTask ftask;
//    private File upload_file;
    private long file_length;

    Handler myMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int int_progress = msg.what;
            progressBar.setProgress(int_progress);
            scdd_jindu.setText(int_progress+"%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangchuandengdai);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        initEvent();
        Intent intent = getIntent();
        path = intent.getStringExtra("file_path");
        cl_name  = intent.getStringExtra("challenge_name");
        elapsed = intent.getIntExtra("elapsed", 20);
        cl_id = intent.getStringExtra("challenge_id");
        pf_iv = intent.getIntExtra("pf_iv",0);
        shangchuandengdaifanhui = (Button) findViewById(R.id.shangchuandengdaifanhui);
        progressBar = (boom.boom.myview.ProgressBar) findViewById(R.id.progress123);
        fangqishangchuang = (Button) findViewById(R.id.fangqishangchuan);
        scdd_jindu = (TextView) findViewById(R.id.scdd_jindu);
        progress = new Integer("0");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Utils.GetBuilder get = new Utils.GetBuilder(Utils.serveraddr + Utils.put_file_api);
                    get.addItem("s_id", Static.session_id);
                    get.addItem("type", "video");
                    String tmp = uploadFile.uploadFile(new ProgressListener() {
                        @Override
                        public void transferred(int transferedBytes) {
                            Message m = new Message();
                            if (transferedBytes >= 99)  m.what = 99;
                            else    m.what = transferedBytes;
                            Shangchuandengdai_activity.this.myMessageHandler.sendMessage(m);
                        }

                        public void transferred(long transfetedBytes){

                        }
                    }, get.toString(), path, "heheda.mp4");
                    JSONObject obj = new JSONObject(tmp);
                    String token = obj.getString("fileToken");
                    Utils.GetBuilder get1 = new Utils.GetBuilder(Utils.serveraddr + Utils.take_cl_api);
                    get1.addItem("challenge", cl_id);
                    get1.addItem("dvtoken", token);
                    get1.addItem("elapsed", elapsed + "");
                    get1.addItem("pf_iv",pf_iv + "");
                    HttpIO io = new HttpIO(get1.toString());
                    io.SetCustomSessionID(Static.session_id);
                    io.GETToHTTPServer();
                    if (io.LastError == 0){
                        Message m = new Message();
                        m.what = 100;
                        Shangchuandengdai_activity.this.myMessageHandler.sendMessage(m);
                    }
                    Log.e("UPLOAD", "Server reply string ==> " + tmp);
                    Log.e("UPLOAD", "URL send to server ==>" + get1.toString());
                    Log.e("UPLOAD", "Server reply string ==>" + io.getResultData());
                    Intent intent = new Intent();
                    intent.putExtra("challenge_id",cl_id);
                    intent.setClass(Shangchuandengdai_activity.this, Shangchuanchenggong1_activity.class);
                    startActivity(intent);
                    Shangchuandengdai_activity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

