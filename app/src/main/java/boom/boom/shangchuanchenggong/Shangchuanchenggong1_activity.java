package boom.boom.shangchuanchenggong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.myview.SildingFinishLayout;
import boom.boom.tongxunlu.tongxunlu_activity;

/**
 * Created by 刘成英 on 2015/3/12.
 */
public class Shangchuanchenggong1_activity extends Activity {
    private LinearLayout yaoqinghaoyoutianzhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shangchuanchenggong1);


        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        yaoqinghaoyoutianzhan = (LinearLayout) findViewById(R.id.yaoqinghaoyoutianzhan);
        yaoqinghaoyoutianzhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Shangchuanchenggong1_activity.this, tongxunlu_activity.class);
                startActivity(intent);
            }
        });

    }

}
