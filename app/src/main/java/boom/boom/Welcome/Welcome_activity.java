package boom.boom.Welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import boom.boom.FontManager.FontManager;
import boom.boom.R;
import boom.boom.api.SysApplication;
import boom.boom.api.Utils;
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
        File file = new File(Utils.getWorkPath());
        if (!file.exists() && !file.isDirectory()){
            Log.e("BASE_FILE", "Unable to find the base data directory. Generate new one.");
            Log.e("BASE_FILE", "The folder will be created at " + file.getAbsolutePath());
//            file.mkdir();
            file.mkdirs();
        }
        file = new File(Utils.getVideoPath());
        if (!file.exists() && !file.isDirectory())  file.mkdirs();
        file = new File(Utils.getImagePath());
        if (!file.exists() && !file.isDirectory())  file.mkdirs();
        timer.schedule(task, 1000);



    }
}
