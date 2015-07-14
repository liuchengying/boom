package boom.boom.bangzhuyufankui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.Suggest;
import boom.boom.api.SysApplication;
import boom.boom.bianjixinxi.Bianjixinxi_activity;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by 刘成英 on 2015/4/1.
 */
public class Bangzhuyufankui_activity extends Activity {
    private RelativeLayout bangzhuyufankui_fh;
    private Button tijiaofankui;
    private EditText bangzhuyufankui_edit;
    private String suggestText;
    private Suggest suggest;



    android.os.Handler myMessageHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Bangzhuyufankui_activity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(Bangzhuyufankui_activity.this, suggest.ServerErr, Toast.LENGTH_SHORT).show();break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bangzhuyufankui);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sfl);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Bangzhuyufankui_activity.this.finish();
                    }
                });

        bangzhuyufankui_fh = (RelativeLayout) findViewById(R.id.bangzhuyufankui_fh);
        bangzhuyufankui_edit = (EditText) findViewById(R.id.bangzhuyufankui_edit);
        tijiaofankui = (Button) findViewById(R.id.tijiaofankui);
        tijiaofankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestText = bangzhuyufankui_edit.getText().toString();
                if (suggestText.equals("")) {
                    Toast.makeText(Bangzhuyufankui_activity.this, "反馈不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            suggest = new Suggest();
                             if (suggest.SuggestChange(suggestText)){
                                 Message m = new Message();
                                 m.what=1;
                                 Bangzhuyufankui_activity.this.myMessageHandler.sendMessage(m);
                             }else {
                                 Message m = new Message();
                                 m.what=2;
                                 Bangzhuyufankui_activity.this.myMessageHandler.sendMessage(m);
                             }
                        }
                    }).run();

                }
            }
        });


        mSildingFinishLayout.setTouchView(mSildingFinishLayout);

        bangzhuyufankui_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


}
