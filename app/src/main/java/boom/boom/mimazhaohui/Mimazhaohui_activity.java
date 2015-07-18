package boom.boom.mimazhaohui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.mimaxiugai.Mimaxiugai_activity;
import boom.boom.mimazhaohui_emall.Mimazhaohui_emall_activity;
import boom.boom.mimazhaohui_phone.Mimazhaohui_phone_activity;
import boom.boom.myview.SildingFinishLayout;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Mimazhaohui_activity extends Activity {
    private Button mmzh_phone;
    private Button mmzh_emill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mimazhaohui);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        Mimazhaohui_activity.this.finish();
                    }
                });

        mSildingFinishLayout.setTouchView(mSildingFinishLayout);

        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        RelativeLayout mmzh_fh = (RelativeLayout)findViewById(R.id.mmzh_fh);
        mmzh_phone= (Button)findViewById(R.id.mmzh_phone);
        mmzh_emill= (Button)findViewById(R.id.mmzh_emill);
        mmzh_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });
        mmzh_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Mimazhaohui_activity.this,Mimazhaohui_phone_activity.class);
                startActivity(intent);

            }
        });
        mmzh_emill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Mimazhaohui_activity.this, Mimazhaohui_emall_activity.class);
                startActivity(intent);
    }});
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

}
