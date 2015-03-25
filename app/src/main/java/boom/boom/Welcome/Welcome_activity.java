package boom.boom.Welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.zhujiemian.Main_activity;

/**
 * Created by 刘成英 on 2015/1/13.
 */
public class Welcome_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huanyingjiemian);
        SysApplication.getInstance().addActivity(this);
        FontManager.changeFonts(FontManager.getContentView(this), this);//字体
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                Intent intent = new Intent(Welcome_activity.this,Main_activity.class);
                startActivity(intent);
                Welcome_activity.this.finish();
            }
        };
        timer.schedule(task, 1000);



    }
}
