package boom.boom.shangchuandengdai;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.MsgBox;
import boom.boom.api.SysApplication;
import boom.boom.paishetiaozhan.Paishetiaozhan_activity;
import boom.boom.shangchuanchenggong.Shangchuanchenggong1_activity;
import boom.boom.tianzhan.Tiaozhan_activity;

/**
 * Created by 刘成英 on 2015/1/20.
 */
public class Shangchuandengdai_activity extends Activity {
    private Button shangchuandengdaifanhui;
    private Button fangqishangchuang;
    private Button tanchuang1queding;
    private Button tanchuang2quxiao;
    private boom.boom.myview.ProgressBar progressBar;
    private int progress = 0;
    private int b=0;
    private TextView scdd_jindu;




    Handler myMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scdd_jindu.setText(progress+"%");

            baifenbi();
        }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangchuandengdai);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        initEvent();
        shangchuandengdaifanhui = (Button) findViewById(R.id.shangchuandengdaifanhui);
        progressBar = (boom.boom.myview.ProgressBar) findViewById(R.id.progress123);
        fangqishangchuang = (Button) findViewById(R.id.fangqishangchuan);
        scdd_jindu = (TextView) findViewById(R.id.scdd_jindu);
        new Thread(new Runnable() {
            @Override

            public void run() {
                for (b = 0; b < 10; b++) {
                    try {
                        progressBar.setProgress(progress += 10);
                        Message m = new Message();
                        m.what = 1;
                        Shangchuandengdai_activity.this.myMessageHandler.sendMessage(m);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
private void baifenbi(){
    if(progress == 100){
        Intent intent = new Intent();
        intent.setClass(Shangchuandengdai_activity.this, Shangchuanchenggong1_activity.class);
        startActivity(intent);}
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

