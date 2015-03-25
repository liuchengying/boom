package boom.boom.mimazhaohui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.mimaxiugai.Mimaxiugai_activity;
import boom.boom.mimazhaohui_emall.Mimazhaohui_emall_activity;
import boom.boom.mimazhaohui_phone.Mimazhaohui_phone_activity;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Mimazhaohui_activity extends Activity {
    private Button mmzh_phone;
    private Button mmzh_emill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mimazhaohui);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        LinearLayout mmzh_fh = (LinearLayout)findViewById(R.id.mmzh_fh);
        mmzh_phone= (Button)findViewById(R.id.mmzh_phone);
        mmzh_emill= (Button)findViewById(R.id.mmzh_emill);
        mmzh_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    }
});}}
